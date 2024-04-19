package id.ois.gp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import id.ois.gp.database.MasterUrlDataSource;

public class SettingActivity extends AppCompatActivity {

    private MasterUrlDataSource dataSource;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        dataSource = new MasterUrlDataSource(this);
        dataSource.open();

        String topUrl = dataSource.getTopUrl();
        if (topUrl != null) {
            url = topUrl;
        } else {
            url = "http://api-gp.farkhan.biz.id";
        }

        EditText editText = findViewById(R.id.editText);
        editText.setText(url);

        findViewById(R.id.buttonSave).setOnClickListener(
                v -> {

                    dataSource.updateUrl(1, editText.getText().toString());
                    Intent intent = new Intent(SettingActivity.this, MainActivity.class);
                    startActivity(intent);
                }
        );

    }
}
