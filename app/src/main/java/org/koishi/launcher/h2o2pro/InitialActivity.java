package org.koishi.launcher.h2o2pro;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class InitialActivity extends AppCompatActivity {

    public ConstraintLayout initial;

    @SuppressLint("HandlerLeak")
    Handler han = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Intent intent1 = new Intent(InitialActivity.this, HomeActivity.class);
            intent1.putExtra("fragment", getResources().getString(R.string.menu_home));
            startActivity(intent1);
            InitialActivity.this.finish();

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setTheme(R.style.Theme_Boat_H2O2_Custom_GREEN);
        setContentView(R.layout.activity_initial);
        initial = findViewById(R.id.initial_view);
        new Thread(() -> {
            try {
                unZip(InitialActivity.this, "app_runtime.zip",  "/data/data/org.koishi.launcher.h2o2pro");
                han.sendEmptyMessage(0);
            } catch (IOException e) {
                Snackbar.make(initial, e.toString(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }

        }).start();
    }

    private void unZip(Context context, String assetName, String outputDirectory) throws IOException {
        File file = new File(outputDirectory);
        if (!file.exists()) {
            file.mkdirs();
        }

        InputStream inputStream = null;
        inputStream = context.getAssets().open(assetName);
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        ZipEntry zipEntry = zipInputStream.getNextEntry();
        byte[] buffer = new byte[1024 * 1024];
        int count = 0;
        while (zipEntry != null) {
            //如果是一个目录
            if (zipEntry.isDirectory()) {
                //String name = zipEntry.getName();
                //name = name.substring(0, name.length() - 1);
                file = new File(outputDirectory + File.separator + zipEntry.getName());
                file.mkdir();
            } else {
                //如果是文件
                file = new File(outputDirectory + File.separator
                        + zipEntry.getName());
                //创建该文件
                file.createNewFile();
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                while ((count = zipInputStream.read(buffer)) > 0) {
                    fileOutputStream.write(buffer, 0, count);
                }

                fileOutputStream.close();
            }
            //定位到下一个文件入口
            zipEntry = zipInputStream.getNextEntry();

        }
        zipInputStream.close();
    }

}