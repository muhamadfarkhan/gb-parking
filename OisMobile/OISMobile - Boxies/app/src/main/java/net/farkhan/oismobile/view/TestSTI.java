package net.farkhan.oismobile.view;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.newland.sdk.ModuleManage;

import net.farkhan.oismobile.R;
import net.farkhan.oismobile.utils.PrefHelper;

import java.nio.charset.StandardCharsets;

import id.co.softorb.lib.passti.PASSTI;
import id.co.softorb.lib.passti.STIUtility;

public class TestSTI extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;

    private PASSTI passti;
    private STIUtility sti;
    PrefHelper prefHelper = new PrefHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_sti);
        setSupportActionBar(findViewById(R.id.toolbar));

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_test_sti);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        ModuleManage.getInstance();

        sti.initLib("758F40D46D95D1641448AA19B9282C05");
        sti.initBank();

        sti.SetMID(prefHelper.getMID().getBytes());

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_test_sti);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}