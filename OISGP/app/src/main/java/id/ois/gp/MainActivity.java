package id.ois.gp;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.Settings;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.Locale;

import id.ois.gp.database.MasterUrlDataSource;

public class MainActivity extends AppCompatActivity {


    private NfcAdapter nfcAdapter;
    TextView desc, load;
    private Handler handler;
    private Runnable animationRunnable;
    private boolean isAnimating = true;
    private int dotCount = 3;
    private String url, noTran;
    private MasterUrlDataSource dataSource;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button_bottom_left).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ScannerActivity.class);
            startActivity(intent);
        });

        initUrl();

        desc = findViewById(R.id.desc);
        load = findViewById(R.id.loading);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            Toast.makeText(this, "NFC tidak didukung di perangkat ini", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        if (!nfcAdapter.isEnabled()) {
            Toast.makeText(this, "Silakan aktifkan NFC di pengaturan", Toast.LENGTH_SHORT).show();
        }

        handler = new Handler();


        desc.setVisibility(View.GONE);

        findViewById(R.id.button_bottom_right).setOnClickListener(v -> {
            submitData();
        });

    }

    private void initUrl() {
        dataSource = new MasterUrlDataSource(this);
        dataSource.open();

        String topUrl = dataSource.getTopUrl();
        if (topUrl != null) {
            url = topUrl;
        } else {
            url = "http://api-gp.farkhan.biz.id";
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = null;
        if (android.os.Build.VERSION.SDK_INT >= 31) {
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE);
        }else{
            pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        }
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        nfcAdapter.disableForegroundDispatch(this);
    }


    private void animateLoadingDots() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                StringBuilder builder = new StringBuilder("Processing data");
                for (int i = 0; i < dotCount; i++) {
                    builder.append(".");
                }
                load.setText(builder.toString());

                dotCount = (dotCount + 1) % 4; // Maksimum 3 titik, mulai dari 1
                if (isAnimating) {
                    handler.postDelayed(this, 500); // Waktu interval antara perubahan teks
                }
            }
        });
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (tag != null) {
            byte[] tagId = tag.getId();
            String uid = bytesToHexString(tagId);
            //Toast.makeText(this, uid, Toast.LENGTH_SHORT).show();
            selectData(uid.replaceAll("\\s", ""),"nfc");
        }
    }

    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X ", b));
        }
        return sb.toString();
    }

    private void selectData(String noTran, String type) {

        load.setVisibility(View.VISIBLE);
        isAnimating = true;
        animateLoadingDots();

        Log.d("test", url);

        AndroidNetworking.post(url + "/select.php")
                .addBodyParameter("parameter", noTran)
                .addBodyParameter("type", type)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        isAnimating = false;
                        desc.setVisibility(View.VISIBLE);

                        Log.d("test", response.toString());

                        try {
                            boolean success = response.getBoolean("success");
                            String msg = response.getString("message");

                            if(success){
                                desc.setText(msg);
                                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }else{
                                desc.setText(msg);
                                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            desc.setText(e.getMessage());

                            Log.d("test", e.getMessage());
                            e.printStackTrace();
                        }

                        load.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.d("test", anError.getMessage());
                        desc.setText(anError.getMessage());
                        load.setVisibility(View.GONE);
                        isAnimating = false;
                        Toast.makeText(MainActivity.this, anError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void submitData() {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
