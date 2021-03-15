package com.koishi.launcher.h2o2.func;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.koishi.launcher.h2o2.R;
import android.view.View;

import android.app.*;
import android.content.*;
import android.widget.*;
import java.io.IOException;
import java.io.File;
import java.io.InputStream;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipEntry;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import android.os.Handler;
import android.os.Message;

public class InitialActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);
		
		new Thread(new Runnable(){
                @Override
                public void run()
                {
    		        try {		
                        unZip(InitialActivity.this, "app_runtime.zip",  "/data/data/com.koishi.launcher.h2o2");
                        han.sendEmptyMessage(0);     
        } catch (IOException e) {
            e.printStackTrace();
        }
        
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
    Handler han=new Handler(){
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            Intent intent1=new Intent(InitialActivity.this,LoginActivity.class);
			startActivity(intent1);
			 InitialActivity.this.finish();
            
         }	
        };
    
}
