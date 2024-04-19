package id.ois.gp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

import id.ois.gp.database.MasterUrlDataSource;

public class ScannerActivity extends AppCompatActivity {

    private CodeScanner mCodeScanner;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1001;
    private String url;
    private MasterUrlDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        CodeScannerView scannerView = findViewById(R.id.scanner_view);

        initUrl();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST_CODE);
        }

        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.startPreview();

        mCodeScanner.setDecodeCallback(result -> runOnUiThread(() -> {

            selectData(result.toString(),"QR");

        }));

        scannerView.setOnClickListener(view -> mCodeScanner.startPreview());
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mCodeScanner.startPreview();
            } else {
                Toast.makeText(ScannerActivity.this, "Please permit to accessing camera", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        CAMERA_PERMISSION_REQUEST_CODE);
            }
        }
    }

    private void selectData(String noTran, String type) {

        Log.d("test", url);

        AndroidNetworking.post(url + "/select.php")
                .addBodyParameter("parameter", noTran)
                .addBodyParameter("type", type)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("test", response.toString());

                        try {
                            boolean success = response.getBoolean("success");
                            String msg = response.getString("message");

                            if(success){
                                Toast.makeText(ScannerActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(ScannerActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {

                            Log.d("test", e.getMessage());
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.d("test", anError.getMessage());
                        Toast.makeText(ScannerActivity.this, anError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
