package org.koishi.launcher.h2o2pro;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;

import org.koishi.launcher.h2o2pro.ui.custom.SettingsFragment;
import org.koishi.launcher.h2o2pro.ui.home.WelcomeFragment;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {

    public Boolean isPermed;
    public CheckBox skip;
    public SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Toolbar tb = findViewById(R.id.toolbar);
        Typeface tf = Typeface.createFromAsset(this.getAssets(),
                "Sans.ttf");
        TextView bigTitle= (TextView) tb.getChildAt(0);
        bigTitle.setTypeface(tf);
        bigTitle.setText(getResources().getString(R.string.app_name));
        getWindow().setStatusBarColor(getResources().getColor(R.color.material_card_background));

        skip = findViewById(R.id.welcome_skip);

        sp = this.getSharedPreferences("isChecked", 0);
        boolean result = sp.getBoolean("skip", false); // 这里就是开始取值了 false代表的就是如果没有得到对应数据我们默认显示为false
        // 把得到的状态设置给CheckBox组件
        skip.setChecked(result);
        skip.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (skip.isChecked()) {
                sp = this.getSharedPreferences("isChecked", 0);
                SharedPreferences.Editor edit = sp.edit();
                edit.putBoolean("skip", true);
                edit.apply();
            } else {
                sp = this.getSharedPreferences("isChecked", 0);
                SharedPreferences.Editor edit = sp.edit();
                edit.putBoolean("skip", false);
                edit.apply();
            }
        });
        checkPermission();
        if (isPermed) {

        }else {
            skip.setChecked(false);
        }
        //if (isTabletDevice(WelcomeActivity.this) == true){
            //skip.setChecked(false);
            //finish();
            //Toast.makeText(WelcomeActivity.this,getResources().getString(R.string.stop_app),Toast.LENGTH_LONG).show();
        //} else{

        //}
        if (skip.isChecked()) {
            startActivity(new Intent(this,SplashActivity.class));
            this.finish();
        } else {
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.perm, new WelcomeFragment())
                .commit();
        /*
        if (!(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !Environment.isExternalStorageManager())) {
            //表明已经有这个权限了
            getPremissionBtn.setEnabled(false);
            startBtn.setEnabled(true);
            getPremissionBtn.setText(getResources().getString(R.string.welcome_perm_true));
            getPremissionBtn.setTextColor(getResources().getColor(R.color.green_light));
        } else {
            getPremissionBtn.setEnabled(true);
            startBtn.setEnabled(false);
            getPremissionBtn.setText(getResources().getString(R.string.welcome_perm));
            getPremissionBtn.setTextColor(getResources().getColor(R.color.red_light));

        }
         */
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {

    }

    public Boolean checkPermission() {
        isPermed = true;
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            if (this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                isPermed = false;
            }
            if (this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) !=PackageManager.PERMISSION_GRANTED) {
                isPermed = false;
            }
        }
        return isPermed;
    }

    /*
    private boolean isTabletDevice(Context context) {
        if ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >=
                Configuration.SCREENLAYOUT_SIZE_LARGE){
            return true;
        } else {
            return false;
        }
    }
     */
}