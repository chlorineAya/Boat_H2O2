package org.koishi.launcher.h2o2pro;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class LogcatActivity extends AppCompatActivity {

    public LinearLayout logLay;
    public ListView log,log2;
    public TabLayout tab;

    //public TextView textList, li;
    public List< String > clientTxt() {
        //将读出来的一行行数据使用List存储
        String filePath = "/storage/emulated/0/games/com.koishi.launcher/h2o2/client_output.txt";

        List newList = new ArrayList< String >();
        try {
            File file = new File(filePath);
            int count = 0;//初始化 key值
            if (file.isFile() && file.exists()) {//文件存在
                InputStreamReader isr = new InputStreamReader(new FileInputStream(file));
                BufferedReader br = new BufferedReader(isr);
                String lineTxt = null;
                while ((lineTxt = br.readLine()) != null) {
                    if (!"".equals(lineTxt)) {
                        String reds = lineTxt.split("\\+")[0]; //java 正则表达式
                        newList.add(count, reds);
                        count++;
                    }
                }
                isr.close();
                br.close();
            } else {
                Snackbar.make(logLay, "File not found.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newList;
    }

    public List< String > appTxt() {
        //将读出来的一行行数据使用List存储
        String filePath = "/storage/emulated/0/games/com.koishi.launcher/h2o2/log.txt";

        List newList = new ArrayList< String >();
        try {
            File file = new File(filePath);
            int count = 0;//初始化 key值
            if (file.isFile() && file.exists()) {//文件存在
                InputStreamReader isr = new InputStreamReader(new FileInputStream(file));
                BufferedReader br = new BufferedReader(isr);
                String lineTxt = null;
                while ((lineTxt = br.readLine()) != null) {
                    if (!"".equals(lineTxt)) {
                        String reds = lineTxt.split("\\+")[0]; //java 正则表达式
                        newList.add(count, reds);
                        count++;
                    }
                }
                isr.close();
                br.close();
            } else {
                Snackbar.make(logLay, "File not found.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_log, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logf:
                initLogs();
                break;
            case R.id.loga:
                AlertDialog alertDialog1 = new AlertDialog.Builder(LogcatActivity.this)
                        .setTitle(getResources().getString(R.string.action))//标题
                        .setIcon(R.drawable.ic_boat)//图标
                        .setMessage("Boat log:\n/storage/emulated/0/games/com.koishi.launcher/h2o2/client_output.txt\nClient log:\n/storage/emulated/0/games/com.koishi.launcher/h2o2/log.txt")
                        .setNegativeButton("OK", new DialogInterface.OnClickListener() {//添加"Yes"按钮
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //TODO
                            }
                        })
                        .create();

                alertDialog1.show();
                break;
            default:
                break;
        }
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logcat);

        getWindow().setStatusBarColor(getResources().getColor(R.color.material_card_background));

        logLay = findViewById(R.id.log_lay);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        Typeface tf = Typeface.createFromAsset(this.getAssets(),
                "Sans.ttf");
        TextView bigTitle= (TextView) toolbar.getChildAt(0);
        bigTitle.setTypeface(tf);
        bigTitle.setText(getResources().getString(R.string.log_title));

        tab = findViewById(R.id.log_tab);
        tab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                //  tab.getPosition()  返回数字，从0开始
                // tab.getText()  返回字符串类型，从0开始
                if (tab.getPosition()==0){
                    log();
                }
                if (tab.getPosition()==1){
                    log2();
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

                }
            });
        initLogs();
        log();
        }

        //textList = (TextView) findViewById(R.id.tl);
        //textList.setText("Loading log...");
        //li = (TextView) findViewById(R.id.li);

        //li.setText(" 1");
        //li.setVisibility(View.GONE);

        /*
        textList.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                if (s.length() != 0) {
                    String str = "";
                    for (int i = 0; i < textList.getLineCount() + 1; i++) {
                        li.append(str + " " + (i + 1) + "\n");
                    }
                    li.setText(str);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    String str = "";
                    for (int i = 0; i < textList.getLineCount() + 1; i++) {
                        li.append(str + " " + (i + 1) + "\n");

                    }
                    li.setText(str);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

         */


        //new Handler().postDelayed(() -> log(), 500);

    public void log() {
        log.setVisibility(View.VISIBLE);
        log2.setVisibility(View.GONE);

    }

    public void log2() {
        log.setVisibility(View.GONE);
        log2.setVisibility(View.VISIBLE);
    }

    public void initLogs(){
        log = findViewById(R.id.view_log);
        ArrayAdapter< String > adapter = new ArrayAdapter<>(this, R.layout.log_list_item, clientTxt());
        log.setAdapter(adapter);
        log2 = findViewById(R.id.view_log2);
        ArrayAdapter< String > adapter2 = new ArrayAdapter<>(this, R.layout.log_list_item, appTxt());
        log2.setAdapter(adapter2);
    }


}