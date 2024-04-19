package net.farkhan.oismobile.print;


import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.l.kotlin.api.ApiService;

import net.farkhan.oismobile.R;
import net.farkhan.oismobile.bluetooth.DeviceList;
import net.farkhan.oismobile.model.ResponseCheck;
import net.farkhan.oismobile.model.ResponseCheck1;
import net.farkhan.oismobile.model.ResponseInsert;
import net.farkhan.oismobile.utils.PrefHelper;
import net.farkhan.oismobile.utils.UnicodeFormatter;
import net.farkhan.oismobile.utils.Utils;
import net.farkhan.oismobile.view.MainActivity;
import net.glxn.qrgen.android.QRCode;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Set;
import java.util.UUID;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class PrintOutBluetooth extends Activity {
    protected static final String TAG = "TAG";
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    Button mScan, mPrint, mDisc, mTest;
    BluetoothAdapter mBluetoothAdapter;
    BluetoothDevice bluetoothDevice;
    private UUID applicationUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private ProgressDialog mBluetoothConnectProgressDialog;
    private BluetoothSocket mBluetoothSocket;
    BluetoothDevice mBluetoothDevice;
    OutputStream os;
    private static OutputStream outputStream;

    // Progress dialog
    private ProgressDialog pDialog;

    private TextView txtCompany, txtNoPlat, txtKendaraan, txtMasuk, txtKeluar, txtPetugas, txtTotal, txtWaktu, txtGreeting;

    // temporary string to show the parsed response
    private String jsonResponse;
    String notran,regno,username,vehclass, company, rangetime, fee, pic, out, in, veh, nopol;

    ApiService apiService;

    @Override
    public void onCreate(Bundle mSavedInstanceState) {
        super.onCreate(mSavedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.print_bluetooth);

        apiService = ApiService.Companion.create(PrintOutBluetooth.this);

        SharedPreferences sharedPreferences = getSharedPreferences("connected", Context.MODE_PRIVATE);
        String deviceAddress = sharedPreferences.getString("deviceAddress",null);

        Intent in = getIntent();
        notran = in.getStringExtra("NOTRAN");

        PrefHelper prefHelper = new PrefHelper(this);

        username = prefHelper.getUserName();

        if(notran.isEmpty()){
            Toast.makeText(this, "Notran not found", Toast.LENGTH_LONG).show();
        }

        countBill(notran);

        Log.v(TAG, "Coming incoming address " + deviceAddress);

        if(deviceAddress == null){

            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            /*if (mBluetoothAdapter == null) {
                Toast.makeText(PrintBluetooth.this, "Tidak ada perangkat terhubung", 2000).show();
            } else {*/
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(
                        BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent,
                        REQUEST_ENABLE_BT);
            } else {
                ListPairedDevices();
                Intent connectIntent = new Intent(PrintOutBluetooth.this,
                        DeviceList.class);
                startActivityForResult(connectIntent,
                        REQUEST_CONNECT_DEVICE);
            }
            /*}*/
        }else{
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(
                        BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent,
                        REQUEST_ENABLE_BT);
            }else{
                mBluetoothDevice = mBluetoothAdapter
                        .getRemoteDevice(deviceAddress);
                Log.v(TAG, "Coming incoming address " + deviceAddress);


//                Thread mBlutoothConnectThread = new Thread();
//                    mBlutoothConnectThread.start();

                try {
                    mBluetoothSocket = mBluetoothDevice
                            .createRfcommSocketToServiceRecord(applicationUUID);
                    mBluetoothAdapter.cancelDiscovery();
                    mBluetoothSocket.connect();
                    mHandler.sendEmptyMessage(0);
                } catch (IOException eConnectException) {
                    Log.d(TAG, "CouldNotConnectToSocket", eConnectException);
//            closeSocket(mBluetoothSocket);
                    return;
                }

           }

        }



        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);


        mScan = (Button) findViewById(R.id.Scan);
        mScan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View mView) {
                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (mBluetoothAdapter == null) {
                    Toast.makeText(PrintOutBluetooth.this, "Message1", Toast.LENGTH_LONG).show();
                } else {
                    if (!mBluetoothAdapter.isEnabled()) {
                        Intent enableBtIntent = new Intent(
                                BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBtIntent,
                                REQUEST_ENABLE_BT);
                    } else {
                        ListPairedDevices();
                        Intent connectIntent = new Intent(PrintOutBluetooth.this,
                                DeviceList.class);
                        startActivityForResult(connectIntent,
                                REQUEST_CONNECT_DEVICE);
                    }
                }
            }
        });




    }// onCreate

    private void countBill(String notran) {
        apiService.check(notran,username)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseCheck1>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseCheck1 responseCheck) {
                        String result = responseCheck.getResult();
                        String msg = responseCheck.getMsg();

                        assert result != null;
                        if(result.equals("true")){
                            Toast.makeText(PrintOutBluetooth.this, msg , Toast.LENGTH_SHORT).show();
                            company = responseCheck.getCompany();
                            fee = String.valueOf(responseCheck.getFEE());
                            rangetime = responseCheck.getTimerange();
                            out = responseCheck.getDATETIMEOUT();
                            pic = responseCheck.getPic();
                            in = responseCheck.getDatas().getDATETIMEIN();
                            veh = responseCheck.getJeniskend();
                            nopol = responseCheck.getDatas().getREGNO();
                            printBill(company,fee,rangetime,out,pic,in,veh,nopol);
                        }else{
                            Toast.makeText(PrintOutBluetooth.this, msg, Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(PrintOutBluetooth.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {


                    }
                });
    }



    protected void printBill(String com,String fee,String ran,String out,String pic,String in,String veh,String nopol) {
        if(mBluetoothSocket == null){
            Intent BTIntent = new Intent(getApplicationContext(), DeviceList.class);
            startActivityForResult(BTIntent,
                    REQUEST_CONNECT_DEVICE);
        }
        else{
            OutputStream opstream = null;
            try {
                opstream = mBluetoothSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            outputStream = opstream;

            //print command
            try {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                outputStream = mBluetoothSocket.getOutputStream();
                byte[] printformat = new byte[]{0x1B,0x21,0x03};
                //outputStream.write(printformat);

                printNewLine();
                printText(com);
                printNewLine();
                printText("Masuk: "+in);
                printNewLine();
                printText("Keluar: "+out);
                printNewLine();
                printText("Kendaraan: "+veh);
                printNewLine();
                printText("Nopol: "+nopol);
                printNewLine();
                printText("Waktu: "+ran);
                printNewLine();
                printText("Biaya: "+fee);
                printNewLine();
                printText("Petugas: "+pic);
                printNewLine();
                printText("Terima kasih atas kunjungan Anda");
                printNewLine();
                printNewLine();
                printNewLine();
                printNewLine();
                printNewLine();
                printNewLine();

                finish();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void printPhoto(Bitmap bmp) {
        try {
            if(bmp!=null){
                byte[] command = Utils.decodeBitmap(bmp);
                outputStream.write(new byte[] { 0x1b, 'a', 0x01 });
                printText(command);
            }else{
                Log.e("Print Photo error", "the file isn't exists");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("PrintTools", "the file isn't exists");
        }
    }

    //print new line
    private void printNewLine() {
        try {
            outputStream.write(10);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

    }

    @Override
    public void onBackPressed() {

        gotoMainActivity();
        //setResult(RESULT_CANCELED);
        //finish();
    }

//    public void onActivityResult(int mRequestCode, int mResultCode,
//                                 Intent mDataIntent) {
//        super.onActivityResult(mRequestCode, mResultCode, mDataIntent);
//
//        switch (mRequestCode) {
//            case REQUEST_CONNECT_DEVICE:
//                if (mResultCode == Activity.RESULT_OK) {
//                    Bundle mExtra = mDataIntent.getExtras();
//                    String mDeviceAddress = mExtra.getString("DeviceAddress");
//                    Log.v(TAG, "Coming incoming address " + mDeviceAddress);
//                    mBluetoothDevice = mBluetoothAdapter
//                            .getRemoteDevice(mDeviceAddress);
//                    mBluetoothConnectProgressDialog = ProgressDialog.show(this,
//                            "Connecting...", mBluetoothDevice.getName() + " : "
//                                    + mBluetoothDevice.getAddress(), true, false);
//                    Thread mBlutoothConnectThread = new Thread(this);
//                    mBlutoothConnectThread.start();
//                    // pairToDevice(mBluetoothDevice); This method is replaced by
//                    // progress dialog with thread
//                }
//                break;
//
//            case REQUEST_ENABLE_BT:
//                if (mResultCode == Activity.RESULT_OK) {
//                    ListPairedDevices();
//                    Intent connectIntent = new Intent(PrintBluetooth.this,
//                            DeviceList.class);
//                    startActivityForResult(connectIntent, REQUEST_CONNECT_DEVICE);
//                } else {
//                    Toast.makeText(PrintBluetooth.this, "Mohon aktifkan koneksi bluetooth.", Toast.LENGTH_LONG).show();
//                }
//                break;
//        }
//    }

    private void ListPairedDevices() {
        Set<BluetoothDevice> mPairedDevices = mBluetoothAdapter
                .getBondedDevices();
        if (mPairedDevices.size() > 0) {
            for (BluetoothDevice mDevice : mPairedDevices) {

                Log.v(TAG, "PairedDevices: " + mDevice.getName() + "  "
                        + mDevice.getAddress());
            }


        }
    }

    //print text
    private void printText(String msg) {
        try {
            // Print normal text
            outputStream.write(msg.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //print byte[]
    private void printText(byte[] msg) {
        try {
            // Print normal text
            outputStream.write(msg);
            printNewLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {

    }

    private void closeSocket(BluetoothSocket nOpenSocket) {
        try {
            nOpenSocket.close();
            Log.d(TAG, "SocketClosed");
        } catch (IOException ex) {
            Log.d(TAG, "CouldNotCloseSocket");
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG, mBluetoothAdapter.getAddress().toString());
            Log.d(TAG,"DeviceConnected");
//            Toast.makeText(PrintBluetooth.this, "Device Connected", Toast.LENGTH_LONG).show();
        }
    };

    public static byte intToByteArray(int value) {
        byte[] b = ByteBuffer.allocate(4).putInt(value).array();

        for (int k = 0; k < b.length; k++) {
            System.out.println("Selva  [" + k + "] = " + "0x"
                    + UnicodeFormatter.byteToHex(b[k]));
        }

        return b[3];
    }

    public byte[] sel(int val) {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.putInt(val);
        buffer.flip();
        return buffer.array();
    }
    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void gotoMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


}