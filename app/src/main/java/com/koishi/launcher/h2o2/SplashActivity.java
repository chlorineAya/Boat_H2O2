package com.koishi.launcher.h2o2;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.os.Environment;
import java.io.IOException;
import android.content.Context;
import java.io.File;
import java.io.InputStream;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipEntry;
import java.io.FileOutputStream;
import android.text.TextUtils;
import com.koishi.launcher.h2o2.func.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		
        setContentView(R.layout.activity_splash);
        /*activity继承AppCompatActivity使用getSupportActionBar().hide()来隐藏ActionBar，且
		 * 必须写在 setContentView后面，如果在styles.xml中设置了NoTitleBar就不用写。*/
        /*getSupportActionBar().hide();*/
		boolean tmp = fileIsExists("/sdcard/games/com.koishi.launcher/h2o2/gamedir");
		boolean tmp2 = fileIsExists("/sdcard/games/com.koishi.launcher/h2o2/config.txt");
        if (tmp && tmp2){
			start();
		}else{
		    try {
				output(SplashActivity.this, "h2o2.zip",  Environment.getExternalStorageDirectory() + "/games/com.koishi.launcher/h2o2" );
			} catch (IOException e) {
				e.printStackTrace();
			}
			start();
		}
		
        
    }

	public void start(){
		
		new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					Intent intent1=new Intent(SplashActivity.this,LoginActivity.class);
					startActivity(intent1);
					SplashActivity.this.finish();
				}
			},3000);//3000表示延迟的毫秒数。
	}
	private void output(Context context, String assetName, String outputDirectory) throws IOException {
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
	private File renameFile(String oldPath, String newPath) {
        if (TextUtils.isEmpty(oldPath)) {
            return null;
        }

        if (TextUtils.isEmpty(newPath)) {
            return null;
        }
        File oldFile = new File(oldPath);
        File newFile = new File(newPath);
        boolean b = oldFile.renameTo(newFile);
        File file2 = new File(newPath);
        return file2;
    }
	
	public boolean fileIsExists(String strFile) {
        try {
            File f=new File(strFile);
            if (!f.exists()) {
                return false;
            }

        } catch (Exception e) {
            return false;
        }

        return true;
    }
    
}
