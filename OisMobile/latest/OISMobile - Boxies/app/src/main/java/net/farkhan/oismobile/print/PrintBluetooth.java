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
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import net.farkhan.oismobile.utils.PrefHelper;
import net.farkhan.oismobile.utils.PrinterCommand;
import net.farkhan.oismobile.utils.Shelter;
import net.farkhan.oismobile.view.MainActivity;
import net.farkhan.oismobile.R;
import net.farkhan.oismobile.bluetooth.DeviceList;
import net.farkhan.oismobile.utils.UnicodeFormatter;
import net.farkhan.oismobile.utils.Utils;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Set;
import java.util.UUID;



public class PrintBluetooth extends Activity {
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

    @Override
    public void onCreate(Bundle mSavedInstanceState) {
        super.onCreate(mSavedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.print_bluetooth);

        SharedPreferences sharedPreferences = getSharedPreferences("connected", Context.MODE_PRIVATE);
        String deviceAddress = sharedPreferences.getString("deviceAddress",null);

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
                Intent connectIntent = new Intent(PrintBluetooth.this,
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

//                new Thread(new Runnable() {
//                    @Override
//                    public void run()
//                    {
//                        // do the thing that takes a long time
//
//                        try {
//                            Thread.sleep(Toast.LENGTH_LONG);
//                            ListPairedDevices();
////                            Intent connectIntent = new Intent(PrintBluetooth.this,
////                                    DeviceList.class);
////                            startActivityForResult(connectIntent,
////                                    REQUEST_CONNECT_DEVICE);
//                        } catch (InterruptedException e) {
//
//                        }
//                    }
//                }).start();
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
                    Toast.makeText(PrintBluetooth.this, "Message1", Toast.LENGTH_LONG).show();
                } else {
                    if (!mBluetoothAdapter.isEnabled()) {
                        Intent enableBtIntent = new Intent(
                                BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBtIntent,
                                REQUEST_ENABLE_BT);
                    } else {
                        ListPairedDevices();
                        Intent connectIntent = new Intent(PrintBluetooth.this,
                                DeviceList.class);
                        startActivityForResult(connectIntent,
                                REQUEST_CONNECT_DEVICE);
                    }
                }
            }
        });


        printBill();


    }// onCreate

    private void print() {
        showpDialog();
        Thread t = new Thread() {
            public void run() {
                try {
                    String company = "test";

                    os = mBluetoothSocket.getOutputStream();
                    byte[] printformat = new byte[]{0x1B,0x21,0x03};
                    os.write(printformat);

                    String BILL = "";

                    BILL = "\n"+ company  +"\n\n";
                    BILL = BILL + "Terima Kasih Atas Kunjugan Anda \n\n";
                    BILL = BILL + "\n";
                    BILL = BILL + "\n";

                    printText("test");
                    //This is printer specific code you can comment ==== > Start
//
//                    // Setting height
//                    int gs = 29;
//                    os.write(intToByteArray(gs));
//                    int h = 104;
//                    os.write(intToByteArray(h));
//                    int n = 162;
//                    os.write(intToByteArray(n));
//
//                    // Setting Width
//                    int gs_width = 29;
//                    os.write(intToByteArray(gs_width));
//                    int w = 119;
//                    os.write(intToByteArray(w));
//                    int n_width = 2;
//                    os.write(intToByteArray(n_width));


                    //printer specific code you can comment ==== > End

                } catch (Exception e) {
                    Log.e("Main", "Exe ", e);
                }

            }
        };
        t.start();

        if(t.isAlive() == true){
            hidepDialog();
            gotoMainActivity();
            Toast.makeText(PrintBluetooth.this, "Berhasil melakukan transaksi", Toast.LENGTH_LONG).show();
        }
    }

    protected void printBill() {
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

                PrefHelper prefHelper = new PrefHelper(this);

                String company = prefHelper.getCompany();
                String datetimeIn = prefHelper.getDatetimeIn();
                String user = prefHelper.getUserName();
                String nopol = prefHelper.getNopol();
                String notran = prefHelper.getNotran();


                printText(company);
                printNewLine();
                printText(datetimeIn);
                printNewLine();
                printText(notran);
                printNewLine();
                printText("Nopol: "+nopol);
                printNewLine();
                printText("Petugas: "+user);
                printNewLine();
                printPhoto2();
                //printNewLine();
                //printTextS("SIMPAN TIKET PARKIR ANDA INI");
                //printNewLine();
                //printTextS("DENGAN AMAN DAN JANGAN LUPA");
                //printNewLine();
                //printTextS("KUNCI GANDA KENDARAAN ANDA");
                //printNewLine();
                //printTextS("KEHILANGAN ATAU KERUSAKAN");
                //printNewLine();
                //printTextS("DILUAR TANGGUNG JAWAB PENGELOLA");
                //printNewLine();
                //printTextS("                               ");
                //printNewLine();
                //printTextS("                               ");
                //printNewLine();
                //printTextS("-------------------------------");
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
                printText(command);
            }else{
                Log.e("Print Photo error", "the file isn't exists");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("PrintTools", "the file isn't exists");
        }
    }

    public void printPhoto2() {
        try {

            Bitmap bmp = Shelter.img;

            if(bmp!=null){
                byte[] command = Utils.decodeBitmap(bmp);
                outputStream.write(PrinterCommand.ESC_ALIGN_CENTER);
                printText(command);

                Shelter.img = null;
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

     //print text
    private void printTextS(String msg) {
        try {
            // Print normal text
            byte[] cc = new byte[]{0x1b,0x21,0x01};
            outputStream.write(cc);
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