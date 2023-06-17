package org.koishi.launcher.h2o2pro.ui.custom;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import androidx.browser.customtabs.CustomTabsIntent;
import androidx.preference.CheckBoxPreference;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;
import androidx.preference.SwitchPreferenceCompat;

import org.koishi.launcher.h2o2pro.LogcatActivity;
import org.koishi.launcher.h2o2pro.MainActivity;
import org.koishi.launcher.h2o2pro.R;
import org.koishi.launcher.h2o2pro.tool.GetGameJson;
import org.koishi.launcher.h2o2pro.tool.file.AppExecute;

import java.io.File;
import java.io.IOException;

public class SettingsFragment extends PreferenceFragmentCompat {

    public Preference delRun,delCfg,crash,logView,langTr;
    public EditTextPreference etId,editJvm,editMcf;
    public SwitchPreferenceCompat appTheme;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings_preferences, rootKey);
        etId = (EditTextPreference) findPreference("set_id");
        editJvm = (EditTextPreference) findPreference("set_jvm");
        editMcf= (EditTextPreference) findPreference("set_mcf");

        assert etId != null;
        etId.setText(GetGameJson.getBoatCfg("auth_player_name","player"));

        assert  editJvm != null;
        editJvm.setText(GetGameJson.getBoatCfg("extraJavaFlags","-client -Xmx750M"));

        assert  editMcf != null;
        editMcf.setText(GetGameJson.getBoatCfg("extraMinecraftFlags",""));

        delRun = findPreference("set_reset_cfg");
        if (delRun != null) {
            delRun.setOnPreferenceClickListener(preference -> {
                del();
                return true;
            });
        }
        delCfg = findPreference("set_reset_run");
        if (delCfg != null) {
            delCfg.setOnPreferenceClickListener(preference -> {
                reset();
                return true;
            });
        }

        appTheme = findPreference("set_theme");
        assert appTheme != null;
        if (Build.VERSION.SDK_INT >= 31) {
            appTheme.setChecked(true);
        } else {
            appTheme.setChecked(false);
        }

        langTr = findPreference("set_lang_tr");
        if (langTr != null){
            langTr.setOnPreferenceClickListener(preference -> {
                CustomTabsIntent intent = new CustomTabsIntent.Builder().setToolbarColor(getResources().getColor(R.color.colorPrimary)).build();
                intent.launchUrl(requireActivity(), Uri.parse("https://wwa.lanzoui.com/iGyPkvlgqdc\n"));
                return true;
            });
        }
        logView = findPreference("set_log");
        if (logView != null) {
            logView.setOnPreferenceClickListener(preference -> {
                Intent i = (new Intent(requireActivity(), LogcatActivity.class));
                startActivity(i);
                return true;
            });
        }
        crash = findPreference("set_crash");
        if (crash != null) {
            crash.setOnPreferenceClickListener(preference -> {
                crash();
                return true;
            });
        }
    }

    public void reset() {
        AlertDialog alertDialog1 = new AlertDialog.Builder(getActivity())
                .setTitle(getResources().getString(R.string.action))//标题
                .setIcon(R.drawable.ic_boat)//图标
                .setMessage(getResources().getString(R.string.resetwarn))
                .setPositiveButton("Yes Yes Yes", new DialogInterface.OnClickListener() {//添加"Yes"按钮
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //TODO
                        deleteFile("/storage/emulated/0/games/com.koishi.launcher/h2o2/h2ocfg.json");
                        try {
                            /*
                            FileWriter fr = new FileWriter("/storage/emulated/0/games/com.koishi.launcher/h2o2/h2ocfg.json");
                            fr.write("{\"mouseMode\":\"false\",\"backToRightClick\":\"false\",\"jumpToLeft\":\"None\",\"email\":\"\",\"password\":\"\",\"dontEsc\":\"false\",\"pack\":\"Default\",\"allVerLoad\":\"true\",\"openGL\":\"libGL112.so.1\"}");
                            fr.close();
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            startActivity(intent);// TODO: Implement this method
                            System.out.println("success");
                            getActivity().finish();
                             */ AppExecute.output(getActivity(),"h2o2.zip",  Environment.getExternalStorageDirectory() + "/games/com.koishi.launcher/h2o2");
                            Intent i2 = new Intent(getActivity(), MainActivity.class);
                            i2.putExtra("fragment", getResources().getString(R.string.menu_home));
                            startActivity(i2);
                            getActivity().finish();
                        } catch (IOException e) {
                            System.out.println(e);
                            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("No No No", new DialogInterface.OnClickListener() {//添加"Yes"按钮
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //TODO
                    }
                })
                .create();

        alertDialog1.show();
    }

    public void del() {
        AlertDialog alertDialog1 = new AlertDialog.Builder(getActivity())
                .setTitle(getResources().getString(R.string.action))//标题
                .setIcon(R.drawable.ic_boat)//图标
                .setMessage(getResources().getString(R.string.resetwarn))
                .setPositiveButton("Yes Yes Yes", new DialogInterface.OnClickListener() {//添加"Yes"按钮
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        delRun.setEnabled(false);
                        delCfg.setEnabled(false);
                        //TODO
                        new Thread(() -> {
                            //String file2= "/data/data/com.koishi.launcher.h2o2/app_runtime";
                            killlist();
                            /*
                             File file = new File(file2);
                             if(file.isDirectory()){
                             deleteDirectory(file2);
                             }
                             if(file.isFile()){
                             deleteFile(file2);
                             }
                             */
                            han.sendEmptyMessage(0);
                        }).start();

                    }
                })
                .setNegativeButton("No No No", new DialogInterface.OnClickListener() {//添加"Yes"按钮
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //TODO
                    }
                })
                .create();

        alertDialog1.show();
    }

    private void killlist() {
        File file2 = new File("/data/data/org.koishi.launcher.h2o2pro/app_runtime/");
        deleteDirWihtFile(file2);
    }

    public boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.isFile() && file.exists()) {
            return file.delete();
        }
        return false;
    }

    private void deleteDirWihtFile(File file2) {
        if (file2 == null || !file2.exists() || !file2.isDirectory())
            return;
        for (File file : file2.listFiles()) {
            if (file.isFile())
                file.delete();
            else if (file.isDirectory())
                deleteDirWihtFile(file);

        }
        file2.delete();
    }

    private void crash() {
        throw new RuntimeException("Crash test from SettingsActivity. 这是从设置里点进来的崩溃，给我发这个是没用的！！请在log.txt或者client_output.txt中找原因。路径是/sdcard/games/com.koishi.launcher/h2o2。");
    }

    @SuppressLint("HandlerLeak")
    Handler han = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                Intent intent1 = new Intent(getActivity(), MainActivity.class);
                intent1.putExtra("fragment", getResources().getString(R.string.menu_home));
                startActivity(intent1);
                Toast.makeText(getActivity(), getResources().getString(R.string.delete), Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        }
    };
}
