package net.valet.utils;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.androidnetworking.utils.Utils;

import net.valet.R;
import net.valet.helper.PrintPic;
import net.valet.helper.PrinterCommands;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class BluetoothUtil {
    private static final UUID PRINTER_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final String Innerprinter_Address = "00:11:22:33:44:55";
    public static BluetoothAdapter getBTAdapter() {
        return BluetoothAdapter.getDefaultAdapter();
    }
    private static OutputStream out;

    public static BluetoothDevice getDevice(BluetoothAdapter bluetoothAdapter) {
        BluetoothDevice innerprinter_device = null;
        Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
        for (BluetoothDevice device : devices) {
            if (device.getAddress().equals(Innerprinter_Address)) {
                innerprinter_device = device;
                break;
            }
        }
        return innerprinter_device;
    }
    public static BluetoothSocket getSocket(BluetoothDevice device) throws IOException {
        BluetoothSocket socket = device.createRfcommSocketToServiceRecord(PRINTER_UUID);
        socket.connect();
        return socket;
    }

    public static void sendData(byte[] title, byte[] bytes,
                                byte[] bytes1,byte[] bytes1a, byte[] bytes2, BluetoothSocket socket, Context context) throws IOException {

        out = socket.getOutputStream();
        out.write(PrinterCommands.ESC_ALIGN_CENTER);
        out.write(new byte[]{0x1B,0x21,0x00});
        out.write(title);

        out.write(PrinterCommands.ESC_ALIGN_LEFT);

        out.write(new byte[]{0x1B,0x21,0x10});
        out.write(bytes1a);
        out.write(new byte[]{0x1B,0x21,0x00});
        out.write(bytes);

        out.write(new byte[]{0x1B,0x21,0x00});
        out.write(PrinterCommands.ESC_ALIGN_LEFT);
        out.write(bytes2);

        Drawable d = ContextCompat.getDrawable(context, R.drawable.blank_car_1);
        Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        PrintPic printPic = PrintPic.getInstance();
        printPic.init(bitmap);
        byte[] bitmapdata = printPic.printDraw();

        out.write(PrinterCommands.ESC_ALIGN_CENTER);
        out.write(bitmapdata);
        byte[] ket = "Keterangan".getBytes();
        out.write(ket);
        out.write(PrinterCommands.FEED_LINE);
        out.write(PrinterCommands.FEED_LINE);
        out.write(PrinterCommands.FEED_LINE);
        out.write(PrinterCommands.FEED_LINE);
        out.write(PrinterCommands.FEED_LINE);
        out.write(PrinterCommands.FEED_LINE);
        byte[] petugas = "Petugas      Pemilik".getBytes();
        out.write(petugas);
        out.write(PrinterCommands.FEED_LINE);
        out.write(PrinterCommands.FEED_LINE);
        byte[] line = "________      ________".getBytes();
        out.write(line);
        out.write(PrinterCommands.FEED_LINE);
        out.write(PrinterCommands.FEED_LINE);
        out.write(PrinterCommands.FEED_LINE);
        out.write(PrinterCommands.FEED_LINE);
        out.write(PrinterCommands.FEED_LINE);
        out.write(PrinterCommands.FEED_LINE);
        out.close();
    }

    public static void sendData2(byte[] title, byte[] bytes, byte[] bytes1, BluetoothSocket socket, Context context) throws IOException {

        OutputStream out = socket.getOutputStream();
        out.write(PrinterCommands.ESC_ALIGN_CENTER);
        out.write(title);

        out.write(PrinterCommands.ESC_ALIGN_LEFT);

        out.write(new byte[]{0x1B,0x21,0x10});
        out.write(bytes);

        out.write(new byte[]{0x1B,0x21,0x00});
        out.write(bytes1);

        out.write(PrinterCommands.FEED_LINE);
        out.write(PrinterCommands.FEED_LINE);
        out.write(PrinterCommands.FEED_LINE);
        out.write(PrinterCommands.FEED_LINE);
        out.close();
    }

}