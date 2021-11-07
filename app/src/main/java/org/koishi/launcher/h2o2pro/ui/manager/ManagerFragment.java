package org.koishi.launcher.h2o2pro.ui.manager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;
import org.koishi.launcher.h2o2pro.R;
import org.koishi.launcher.h2o2pro.VersionsActivity;
import org.koishi.launcher.h2o2pro.tool.data.DbDao;
import org.koishi.launcher.h2o2pro.tool.file.AppExecute;
import org.koishi.launcher.h2o2pro.tool.GetGameJson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

public class ManagerFragment extends Fragment {

    public LinearLayout openVer;
    public ManagerViewModel managerViewModel;
    public TextInputEditText mDirectory,mOutput;
    public Button mSetButton, mResetButton, mOutputButton;
    public ProgressBar pbM;
    public String d;

    public DbDao mDbDao;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        managerViewModel =
                new ViewModelProvider(this).get(ManagerViewModel.class);
        View root = inflater.inflate(R.layout.fragment_manager, container, false);
        mDirectory = root.findViewById(R.id.manager_directory);
        mSetButton = root.findViewById(R.id.manager_set_button);
        mResetButton = root.findViewById(R.id.manager_reset_button);
        mOutput = root.findViewById(R.id.manager_output_pack);
        mOutputButton =root.findViewById(R.id.manager_output_button);

        pbM = root.findViewById(R.id.pb_m);

        openVer = root.findViewById(R.id.manager_open_ver);
        openVer.setOnClickListener(v->{
            startActivity(new Intent(requireActivity(), VersionsActivity.class));
            requireActivity().finish();
        });

        mSetButton.setOnClickListener(v -> setDirectory());
        mResetButton.setOnClickListener(v -> reset());
        mDirectory.setText(GetGameJson.getBoatCfg("game_directory","/storage/emulated/0/games/com.koishi.launcher/h2o2/gamedir"));

        mOutputButton.setOnClickListener(v -> setOutput());

        return root;
    }

    public void setDirectory() {
        String file = mDirectory.getText().toString();
        File f = new File(file);
        pbM.setVisibility(View.VISIBLE);
        mDirectory.setEnabled(false);
        mSetButton.setEnabled(false);
        mResetButton.setEnabled(false);
        mOutput.setEnabled(false);
        mOutputButton.setEnabled(false);
        if (!f.exists()) {
            new Thread(() -> {
                try {
                    AppExecute.output(getActivity(), "boat.zip", file);
                    //Snackbar.make(getView(), getResources().getString(R.string.install_done), Snackbar.LENGTH_LONG)
                            //.setAction("Action", null).show();
                    han.sendEmptyMessage(0);
                } catch (IOException e) {
                    Snackbar.make(getView(), e.toString(), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    han.sendEmptyMessage(-1);
                }
            }).start();

        } else {
            Snackbar.make(getView(), getResources().getString(R.string.install_done), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            setDir(file);
            mDirectory.setText(file);
        }

    }

    public void setOutput() {
        String file = mOutput.getText().toString();
        pbM.setVisibility(View.VISIBLE);
        mDirectory.setEnabled(false);
        mSetButton.setEnabled(false);
        mResetButton.setEnabled(false);
        mOutput.setEnabled(false);
        mOutputButton.setEnabled(false);
        new Thread(() -> {
            try {
                AppExecute.output(getActivity(), "pack.zip", file);
                //Snackbar.make(getView(), getResources().getString(R.string.install_done), Snackbar.LENGTH_LONG)
                //.setAction("Action", null).show();
                han.sendEmptyMessage(1);
            } catch (IOException e) {
                Snackbar.make(getView(), e.toString(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                han.sendEmptyMessage(-1);
            }
        }).start();

    }

    @SuppressLint("HandlerLeak")
    Handler han = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                Snackbar.make(getView(), getResources().getString(R.string.install_done), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                setDir(mDirectory.getText().toString());
                mDirectory.setText(mDirectory.getText().toString());
            }
            if (msg.what == 1) {
                Snackbar.make(getView(), getResources().getString(R.string.install_done), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                boolean hasData = mDbDao.hasData(Objects.requireNonNull(mOutput.getText()).toString());
                if (!hasData){
                    mDbDao.insertData(mOutput.getText().toString());
                }
                //setDir(mOutput.getText().toString());
                //mDirectory.setText(mOutput.getText().toString());
                pbM.setVisibility(View.GONE);
                mDirectory.setEnabled(true);
                mSetButton.setEnabled(true);
                mResetButton.setEnabled(true);
                mOutput.setEnabled(true);
                mOutputButton.setEnabled(true);
            }
            if (msg.what == -1) {
                reset();
            }
        }
    };

    public void reset() {
        pbM.setVisibility(View.GONE);
        mDirectory.setEnabled(true);
        mSetButton.setEnabled(true);
        mResetButton.setEnabled(true);
        mOutput.setEnabled(true);
        mOutputButton.setEnabled(true);
        setDir("/storage/emulated/0/games/com.koishi.launcher/h2o2/gamedir");
        mDirectory.setText("/storage/emulated/0/games/com.koishi.launcher/h2o2/gamedir");
    }

    public static void setDir(String dir) {
        try {
            FileInputStream in = new FileInputStream("/storage/emulated/0/games/com.koishi.launcher/h2o2/config.txt");
            byte[] b = new byte[in.available()];
            in.read(b);
            in.close();
            String str = new String(b);
            JSONObject json = new JSONObject(str);
            json.remove("game_directory");
            json.remove("game_assets");
            json.remove("assets_root");
            json.remove("currentVersion");
            json.put("game_directory", dir);
            json.put("game_assets", dir + "/assets/virtual/legacy");
            json.put("assets_root", dir + "/assets");
            json.put("currentVersion", dir + "/versions");
            FileWriter fr = new FileWriter(new File("/storage/emulated/0/games/com.koishi.launcher/h2o2/config.txt"));
            fr.write(json.toString());
            fr.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}