package org.koishi.launcher.h2o2pro;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import org.koishi.launcher.h2o2pro.tool.AssetsUtils;
import org.koishi.launcher.h2o2pro.tool.GetGameJson;
import org.koishi.launcher.h2o2pro.tool.file.AppExecute;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class SplashActivity extends AppCompatActivity {

    public LinearLayout splash;
    public TextView splashCheck;

    boolean existMcConfig = FileExists("/storage/emulated/0/games/com.koishi.launcher/h2o2/config.txt");
    boolean existH2oConfig = FileExists("/storage/emulated/0/games/com.koishi.launcher/h2o2/h2ocfg.json");
    //boolean existH2oMd = FileExists("/storage/emulated/0/games/com.koishi.launcher/h2o2/info.md");
    boolean existRuntime = FileExists("/data/data/org.koishi.launcher.h2o2pro/app_runtime/libopenal.so.1");
    boolean existGame = FileExists(GetGameJson.getBoatCfg("game_directory", "/storage/emulated/0/games/com.koishi.launcher/h2o2/gamedir"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setTheme(R.style.Theme_Boat_H2O2_Custom_GREEN);
        setContentView(R.layout.activity_splah);
        splash = findViewById(R.id.splash_view);
        splashCheck = findViewById(R.id.splash_check);
        start();
    }

    public void start() {
        new Handler().postDelayed(() -> startApp(), 1000);
    }

    public void updateMarkDown() {
        File md = new File("/storage/emulated/0/games/com.koishi.launcher/h2o2/markdown");
        if (md.exists()&&md.isDirectory()){
        } else {
            md.mkdir();
        }
        copyData();
        /*
        AssetsUtils.getInstance(SplashActivity.this).copyAssetsToSD("markdown", "games/com.koishi.launcher/h2o2/markdown").setFileOperateCallback(new AssetsUtils.FileOperateCallback() {
            @Override
            public void onSuccess() {
                // TODO: 文件复制成功时，主线程回调
            }

            @Override
            public void onFailed(String error) {
                // TODO: 文件复制失败时，主线程回调
            }
        });

         */
    }

    public void copyData()

    {
        InputStream in = null;

        FileOutputStream out = null;

        //String path = this.getApplicationContext().getFilesDir()

                //.getAbsolutePath() + "/mydb.db3"; // data/data目录
        String path = "/storage/emulated/0/games/com.koishi.launcher/h2o2/markdown/info.md";

        File file = new File(path);
            try
            {
                in = this.getAssets().open("markdown/info.md"); // 从assets目录下复制
                out = new FileOutputStream(file);
                int length = -1;
                byte[] buf = new byte[1024];
                while ((length = in.read(buf)) != -1)
                {
                    out.write(buf, 0, length);
                }
                out.flush();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            finally{
                if (in != null)
                {
                    try {
                        in.close();
                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }
                if (out != null)
                {
                    try {
                        out.close();
                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }
            }
        }



    public void updateAuthlib() {
        AssetsUtils.getInstance(SplashActivity.this).copyAssetsToSD("authlib", "games/com.koishi.launcher/h2o2/authlib").setFileOperateCallback(new AssetsUtils.FileOperateCallback() {
            @Override
            public void onSuccess() {
                // TODO: 文件复制成功时，主线程回调
            }

            @Override
            public void onFailed(String error) {
                // TODO: 文件复制失败时，主线程回调
            }
        });
    }

    public void startApp() {
        updateMarkDown();
        updateAuthlib();
        if (existRuntime) {
            if (existGame && existMcConfig && existH2oConfig) {
                updateMarkDown();
                Intent i = new Intent(this, HomeActivity.class);
                i.putExtra("fragment", getResources().getString(R.string.menu_home));
                startActivity(i);
                this.finish();
            } else {
                new Thread(() -> {
                    try {
                        AppExecute.output(SplashActivity.this, "h2o2.zip", "/storage/emulated/0/games/com.koishi.launcher/h2o2");
                    } catch (IOException e) {
                        Snackbar.make(splash, e.toString(), Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }).start();
                splashCheck.setText(getResources().getString(R.string.launcher_initial_install_start));
                new Handler().postDelayed(() -> {
                    Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                    i.putExtra("fragment", getResources().getString(R.string.menu_home));
                    startActivity(i);
                    finish();
                }, 3000);
            }
        } else {
            if (existGame && existMcConfig && existH2oConfig) {
                startActivity(new Intent(SplashActivity.this, InitialActivity.class));
                this.finish();
            } else {
                new Thread(() -> {
                    try {
                        AppExecute.output(SplashActivity.this, "h2o2.zip", "/storage/emulated/0/games/com.koishi.launcher/h2o2");
                    } catch (IOException e) {
                        Snackbar.make(splash, e.toString(), Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }).start();
                updateMarkDown();
                splashCheck.setText(getResources().getString(R.string.launcher_initial_install_start));
                new Handler().postDelayed(() -> {
                    startActivity(new Intent(SplashActivity.this, InitialActivity.class));
                    finish();
                }, 3000);
            }
        }

    }

    public static boolean FileExists(String strFile) {
        try {
            File f = new File(strFile);
            if (!f.exists()) {
                return false;
            }

        } catch (Exception e) {
            return false;
        }

        return true;
    }

}

