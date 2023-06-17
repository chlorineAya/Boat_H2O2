package org.koishi.launcher.h2o2pro;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.StatusBarManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.*;
import android.view.accessibility.AccessibilityEvent;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import org.json.JSONObject;
import org.koishi.launcher.h2o2pro.tool.GetGameJson;
import org.koishi.launcher.h2o2pro.ui.custom.CustomFragment;
import org.koishi.launcher.h2o2pro.ui.home.HomeFragment;
import org.koishi.launcher.h2o2pro.ui.install.InstallFragment;
import org.koishi.launcher.h2o2pro.ui.manager.ManagerFragment;
import org.koishi.launcher.h2o2pro.ui.version.VersionFragment;

import java.io.FileInputStream;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawer;
    private Dialog mDialog;
    private Toolbar toolbar1;

    private HomeFragment homeFragment;
    private Intent intent;
    private VersionFragment versionFragment;
    private ManagerFragment managerFragment;
    private InstallFragment installFragment;
    private CustomFragment customFragment;

    //private int mPrevSelectedId;
    //private int mSelectedId;
    private NavigationView navigationView;
    private String fragmentId;
    private View dialogBg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setTheme(R.style.RedTheme_NoActionBar);

        /*
        String sys = GetGameJson.getAppCfg("followSys","true");
        if (sys.equals("true")){
            String getThemeType = GetGameJson.getAppCfg("theme","1");
            if (this.getApplicationContext().getResources().getConfiguration().uiMode==0x21){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                Window window = getWindow();
                if (getThemeType.equals("0")){
                    setTheme(R.style.AppTheme_NoActionBar);
                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                } else if (getThemeType.equals("1")){
                    setTheme(R.style.BlueTheme_NoActionBar);
                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                } else if (getThemeType.equals("2")){
                    setTheme(R.style.PurpleTheme_NoActionBar);
                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                } else if (getThemeType.equals("3")){
                    setTheme(R.style.OrangeTheme_NoActionBar);
                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                } else if (getThemeType.equals("4")){
                    setTheme(R.style.RedTheme_NoActionBar);
                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                } else if (getThemeType.equals("5")){
                    setTheme(R.style.GreenTheme_NoActionBar);
                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                } else if (getThemeType.equals("6")) {
                    setTheme(R.style.PinkTheme_NoActionBar);
                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                }
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                if (getThemeType.equals("0")){
                    setTheme(R.style.AppTheme_NoActionBar);
                } else if (getThemeType.equals("1")){
                    setTheme(R.style.BlueTheme_NoActionBar);
                } else if (getThemeType.equals("2")){
                    setTheme(R.style.PurpleTheme_NoActionBar);
                } else if (getThemeType.equals("3")){
                    setTheme(R.style.OrangeTheme_NoActionBar);
                } else if (getThemeType.equals("4")){
                    setTheme(R.style.RedTheme_NoActionBar);
                } else if (getThemeType.equals("5")){
                    setTheme(R.style.GreenTheme_NoActionBar);
                } else if (getThemeType.equals("6")) {
                    setTheme(R.style.PinkTheme_NoActionBar);
                }
            }


        } else {
            String getDarkType = GetGameJson.getAppCfg("darkMode","1");
            if (getDarkType.equals("0")){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                String getThemeType = GetGameJson.getAppCfg("theme","1");
                if (getThemeType.equals("0")){
                    setTheme(R.style.AppTheme_NoActionBar);
                } else if (getThemeType.equals("1")){
                    setTheme(R.style.BlueTheme_NoActionBar);
                } else if (getThemeType.equals("2")){
                    setTheme(R.style.PurpleTheme_NoActionBar);
                } else if (getThemeType.equals("3")){
                    setTheme(R.style.OrangeTheme_NoActionBar);
                } else if (getThemeType.equals("4")){
                    setTheme(R.style.RedTheme_NoActionBar);
                } else if (getThemeType.equals("5")){
                    setTheme(R.style.GreenTheme_NoActionBar);
                } else if (getThemeType.equals("6")) {
                    setTheme(R.style.PinkTheme_NoActionBar);
                }
            } else if (getDarkType.equals("1")){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                String getThemeType = GetGameJson.getAppCfg("theme","0");
                Window window = getWindow();
                if (getThemeType.equals("0")){
                    setTheme(R.style.AppTheme_NoActionBar);
                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                } else if (getThemeType.equals("1")){
                    setTheme(R.style.BlueTheme_NoActionBar);
                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                } else if (getThemeType.equals("2")){
                    setTheme(R.style.PurpleTheme_NoActionBar);
                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                } else if (getThemeType.equals("3")){
                    setTheme(R.style.OrangeTheme_NoActionBar);
                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                } else if (getThemeType.equals("4")){
                    setTheme(R.style.RedTheme_NoActionBar);
                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                } else if (getThemeType.equals("5")){
                    setTheme(R.style.GreenTheme_NoActionBar);
                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                } else if (getThemeType.equals("6")) {
                    setTheme(R.style.PinkTheme_NoActionBar);
                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                }
            }
        }
         */

        setContentView(R.layout.activity_main);

        dialogBg = findViewById(R.id.dialog_bg);

        toolbar1 = findViewById(R.id.toolbar);
        actionBar();
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        intent = getIntent();
        navigationView.setCheckedItem(R.id.fragment_home);
        getSupportActionBar().setTitle(getResources().getString(R.string.menu_home));
        initFragment1();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            drawer.closeDrawer(GravityCompat.START);
            new Handler().postDelayed(() -> {navigationView.setCheckedItem(R.id.fragment_custom);
                getSupportActionBar().setTitle(getResources().getString(R.string.menu_more));
                initFragment5();
                },350);
        }
        /*
        if (item.getItemId() == R.id.action_home) {
            drawer.closeDrawer(GravityCompat.START);
            new Handler().postDelayed(() -> {navigationView.setCheckedItem(R.id.fragment_home);
                getSupportActionBar().setTitle(getResources().getString(R.string.menu_home));
                initFragment1();
                },350);
        }
        if (item.getItemId() == R.id.action_theme) {
            drawer.closeDrawer(GravityCompat.START);
            showTheme();
        }

         */
        return super.onOptionsItemSelected(item);
    }

    public void actionBar() {
        setSupportActionBar(toolbar1);
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar1, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.fragment_home:
                drawer.closeDrawer(GravityCompat.START);
                new Handler().postDelayed(() -> {
                    navigationView.setCheckedItem(R.id.fragment_home);
                    getSupportActionBar().setTitle(getResources().getString(R.string.menu_home));
                    initFragment1();
                },350);

                break;
            case R.id.fragment_version:
                drawer.closeDrawer(GravityCompat.START);
                new Handler().postDelayed(() -> {
                    navigationView.setCheckedItem(R.id.fragment_version);
                    getSupportActionBar().setTitle(getResources().getString(R.string.menu_ver));
                    initFragment2();
                },350);
                break;
            case R.id.fragment_manager:
                drawer.closeDrawer(GravityCompat.START);
                new Handler().postDelayed(() -> {
                    navigationView.setCheckedItem(R.id.fragment_manager);
                    getSupportActionBar().setTitle(getResources().getString(R.string.menu_manager));
                    initFragment3();
                },350);
                break;
            case R.id.fragment_install:
                drawer.closeDrawer(GravityCompat.START);
                new Handler().postDelayed(() -> {
                    navigationView.setCheckedItem(R.id.fragment_install);
                    getSupportActionBar().setTitle(getResources().getString(R.string.menu_install));
                    initFragment4();
                },350);
                break;
            case R.id.fragment_custom:
                drawer.closeDrawer(GravityCompat.START);
                new Handler().postDelayed(() -> {navigationView.setCheckedItem(R.id.fragment_custom);
                    getSupportActionBar().setTitle(getResources().getString(R.string.menu_more));
                    initFragment5();},350);
                break;
            case R.id.activity_terminal:
                new Handler().postDelayed(() -> {
                    startActivity(new Intent(this, TerminalActivity.class));
                    },350);
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (!getSupportActionBar().getTitle().equals(getResources().getString(R.string.menu_home))) {
                navigationView.setCheckedItem(R.id.fragment_home);
                getSupportActionBar().setTitle(getResources().getString(R.string.menu_home));
                initFragment1();
                drawer.closeDrawer(GravityCompat.START);
            } else {
                finish();
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mDialog != null) {
            mDialog.dismiss();
        } else {

        }

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus){
        if (hasFocus){
            dialogBg.setVisibility(View.GONE);
        }else{
            dialogBg.setVisibility(View.VISIBLE);
            if (mDialog != null) {
                mDialog.dismiss();
            } else {
            }
        }
    }

    private void initFragment1() {
        //开启事务，fragment的控制是由事务来实现的
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        //第一种方式（add），初始化fragment并添加到事务中，如果为null就new一个
        if (homeFragment == null) {
            homeFragment = new HomeFragment();
        }
        //transaction.replace(R.id.fragment_home, homeFragment);
        transaction.replace(R.id.content, homeFragment);
        //隐藏所有fragment
        //hideFragment(transaction);
        //显示需要显示的fragment
        //transaction.show(homeFragment);

        //第二种方式(replace)，初始化fragment
//        if(f1 == null){
//            f1 = new MyFragment("消息");
//        }
//        transaction.replace(R.id.main_frame_layout, f1);


        //提交事务
        transaction.commit();
    }

    private void initFragment2() {
        //FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        //if (versionFragment == null) {
        //    versionFragment = new VersionFragment();
        //}
        //transaction.replace(R.id.content, versionFragment);
        //transaction.commit();
        startActivity(new Intent(MainActivity.this, VersionsActivity.class));
    }

    private void initFragment3() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        if (managerFragment == null) {
            managerFragment = new ManagerFragment();
        }
        transaction.replace(R.id.content, managerFragment);
        transaction.commit();
    }

    private void initFragment4() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        if (installFragment == null) {
            installFragment = new InstallFragment();
        }
        transaction.replace(R.id.content, installFragment);
        transaction.commit();
    }

    private void initFragment5() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        if (customFragment == null) {
            customFragment = new CustomFragment();
        }
        transaction.replace(R.id.content, customFragment);
        transaction.commit();
    }

    public void showTheme() {
        mDialog = new BottomSheetDialog(MainActivity.this,R.style.BottomSheetDialogStyle);
        View dialogView = MainActivity.this.getLayoutInflater().inflate(R.layout.custom_dialog_theme, null,false);
        mDialog.setContentView(dialogView);
        //mDialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundColor(Color.TRANSPARENT);
        ImageButton defaultButton = dialogView.findViewById(R.id.theme_default);
        ImageButton blueButton = dialogView.findViewById(R.id.theme_blue);
        ImageButton purpleButton = dialogView.findViewById(R.id.theme_purple);
        ImageButton orangeButton = dialogView.findViewById(R.id.theme_orange);
        ImageButton redButton = dialogView.findViewById(R.id.theme_red);
        ImageButton greenButton = dialogView.findViewById(R.id.theme_green);
        ImageButton pinkButton = dialogView.findViewById(R.id.theme_pink);

        String getThemeType = GetGameJson.getAppCfg("theme","1");
        if (getThemeType.equals("0")){
            defaultButton.setEnabled(false);
            defaultButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher_check_file_true));
        } else if (getThemeType.equals("1")){
            blueButton.setEnabled(false);
            blueButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher_check_file_true));
        } else if (getThemeType.equals("2")){
            purpleButton.setEnabled(false);
            purpleButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher_check_file_true));
        } else if (getThemeType.equals("3")){
            orangeButton.setEnabled(false);
            orangeButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher_check_file_true));
        } else if (getThemeType.equals("4")){
            redButton.setEnabled(false);
            redButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher_check_file_true));
        } else if (getThemeType.equals("5")){
            greenButton.setEnabled(false);
            greenButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher_check_file_true));
        } else if (getThemeType.equals("6")) {
            pinkButton.setEnabled(false);
            pinkButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher_check_file_true));
        }

        SeekBar sk = dialogView.findViewById(R.id.if_dark);
        String dark = GetGameJson.getAppCfg("darkMode","1");
        int dk = Integer.parseInt(dark);
        sk.setProgress(dk);
        sk.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            /**
             * 拖动条停止拖动的时候调用
             */
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
            /**
             * 拖动条开始拖动的时候调用
             */
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            /**
             * 拖动条进度改变的时候调用
             */
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                //description.setText("当前进度："+progress+"%");
                GetGameJson.setAppJson("darkMode",Integer.toString(progress));
            }
        });

        CheckBox cb = dialogView.findViewById(R.id.follow_sys);
        String sys = GetGameJson.getAppCfg("followSys","true");
        if (sys.equals("true")) {
            sk.setEnabled(false);
            cb.setChecked(true);
        } else {
            sk.setEnabled(true);
            cb.setChecked(false);
        }
        cb.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // 判断CheckBox是否被勾选了
            if (cb.isChecked()) {// CheckBox被勾选了 我们就把你的状态保存起来
                sk.setEnabled(false);
                GetGameJson.setAppJson("followSys","true");
            } else {
                sk.setEnabled(true);
                GetGameJson.setAppJson("followSys","false");
            }

        });

        defaultButton.setOnClickListener(v->{
            GetGameJson.setAppJson("theme","0");
            finish();
            startActivity(new Intent(MainActivity.this,MainActivity.class));
        });
        blueButton.setOnClickListener(v->{
            GetGameJson.setAppJson("theme","1");
            finish();
            startActivity(new Intent(MainActivity.this,MainActivity.class));
        });
        purpleButton.setOnClickListener(v->{
            GetGameJson.setAppJson("theme","2");
            finish();
            startActivity(new Intent(MainActivity.this,MainActivity.class));
        });
        orangeButton.setOnClickListener(v->{
            GetGameJson.setAppJson("theme","3");
            finish();
            startActivity(new Intent(MainActivity.this,MainActivity.class));
        });
        redButton.setOnClickListener(v->{
            GetGameJson.setAppJson("theme","4");
            finish();
            startActivity(new Intent(MainActivity.this,MainActivity.class));
        });
        greenButton.setOnClickListener(v->{
            GetGameJson.setAppJson("theme","5");
            finish();
            startActivity(new Intent(MainActivity.this,MainActivity.class));
        });
        pinkButton.setOnClickListener(v->{
            GetGameJson.setAppJson("theme","6");
            finish();
            startActivity(new Intent(MainActivity.this,MainActivity.class));
        });

        WindowManager windowManager = MainActivity.this.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
        lp.width = (int)(display.getWidth()*0.9); //设置宽度 dialog.getWindow().setAttributes(lp);
        mDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        mDialog.show();
    }

}