package com.koishi.launcher.h2o2.func;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.koishi.launcher.h2o2.R;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import android.os.Message;
import android.os.Handler;
import android.text.Html;
import java.io.File;
import com.koishi.launcher.h2o2.tools.Utils;
import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;
import com.leon.lfilepickerlibrary.LFilePicker;
import com.koishi.launcher.h2o2.tools.Consant;
import android.content.Intent;
import android.app.Activity;
import java.util.List;

public class ExecuteActivity extends AppCompatActivity implements View.OnClickListener {
    
    public Button excuteButton;
    public TextView inputText;
    public TextView outputText;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_execute);
		
		Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar2);
		setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
				}
			});
		this.excuteButton=(Button)findViewById(R.id.launcher_excute_button);
		this.inputText=(TextView)findViewById(R.id.launcher_input_text);
		this.outputText=(TextView)findViewById(R.id.launcher_output_text);
		
		this.excuteButton.setOnClickListener(this);
		
		this.mHandler = new MyHandler();
    }
	private String result = "";
	private String error ="";
    public void excuteCommand(final String args[]) {
		new Thread(){
			@Override
			public void run() {
				try {
					Process p = new ProcessBuilder(args).start();
					BufferedReader bri=new BufferedReader(new InputStreamReader(p.getInputStream()));

					result = "";
					String linei;
					while ((linei = bri.readLine()) != null) {
						result = result + "\n" + linei;			

					}

					BufferedReader bre=new BufferedReader(new InputStreamReader(p.getErrorStream()));

					error = "";
					String linee;
					while ((linee = bre.readLine()) != null) {
						error = error + "\n" + linee;			

					}

					p.waitFor();
					int e = p.exitValue();

					Message endMsg=new Message();
					endMsg.what = e;
					mHandler.sendMessage(endMsg);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
	//Output Text Message
	private MyHandler mHandler;
	private class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
				case -1:
				    String str0 = "<font color='#FF0000'>\nNo such file!\n</font>";
				    outputText.append(Html.fromHtml(str0));
				    outputText.append("\n");
					break;
				case -2:
					outputText.append("Installing...\n");
					break;
				case -3:
					outputText.append("Package has been extracted in " + getDir("runtime", 0).getAbsolutePath() + "\n");
					break;
				case -4:
					outputText.append("Try to set executing permission: true\n");
					break;
				case -5:
					outputText.append("Try to set executing permission: false\n");
					break;
				case -6:
					outputText.append("Setting up property...\n");
					break;
				case -7:
					outputText.append("Install Finished. Now checking the runtime file if exists...\n");
					break;
				case -8:
				    String str2 = "<font color='#FF00FF00'>Install successfully\n</font>";
				    outputText.append(Html.fromHtml(str2));
				    outputText.append("\n");
				    break;
				case -9:
				    String str1 = "<font color='#FF0000'>Wrong runtime have been installed.</font>";
                    outputText.append(Html.fromHtml(str1));
                    outputText.append("\n");
				    break;
				case -10:
					outputText.append("Now deleting...\n");
					break;
				case -11:
					outputText.append("Done.\n");
					break;
				default:
					outputText.append(result + "\n");
					outputText.append("\n");
					outputText.append(error + "\n");
					outputText.append("Command process exited with value: " + msg.what + "\n");
			}
		}
	}
	public boolean mode = false;
	@Override
	public void onClick(View p1){
	    if (p1 == excuteButton){
	        if (!mode) {
				if (!inputText.getText().equals("")) {
					String packagePath = inputText.getText().toString();
					install(packagePath);
				}
			} else {
				if (!inputText.getText().equals("")) {
					String cmd = inputText.getText().toString();
					excuteCommand(cmd.split(" "));

				}
			}
	    }
	}
	public void setdir(View view){
	    final EditText editText = new EditText(ExecuteActivity.this);
                AlertDialog.Builder dialog = new AlertDialog.Builder(ExecuteActivity.this);
                dialog.setTitle("Directory of runtime");
		        dialog.setView(editText, 50, 0, 50, 0);
                dialog.setCancelable(true);
                dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        inputText.setText(editText.getText().toString());
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dialog.show();
	}
	public void install(final String packagePath) {
		new Thread(){
			@Override
			public void run() {
				Message endMsg = null;
				File packageFile = new File(packagePath);
				if (!packageFile.exists()) {
					endMsg = new Message();
					mHandler.sendMessage(endMsg);
					endMsg.what = -1;
					

				} else {
					{
						endMsg = new Message();
						endMsg.what = -2;
						mHandler.sendMessage(endMsg);
					}
					Utils.extractTarXZ(packagePath, getDir("runtime", 0));
					endMsg = new Message();
					endMsg.what = -3;
					mHandler.sendMessage(endMsg);

					if (Utils.setExecutable(getDir("runtime", 0))) {
						endMsg = new Message();
						endMsg.what = -4;
						mHandler.sendMessage(endMsg);
					} else {
						endMsg = new Message();
						endMsg.what = -5;
						mHandler.sendMessage(endMsg);
					}
					endMsg = new Message();
					endMsg.what = -6;
					mHandler.sendMessage(endMsg);

					endMsg = new Message();
					endMsg.what = -7;
					mHandler.sendMessage(endMsg);

					if (!new File("/data/data/com.koishi.launcher.h2o2/app_runtime/libopenal.so.1").exists()) {

						endMsg = new Message();
						endMsg.what = -9;
						mHandler.sendMessage(endMsg);
						endMsg = new Message();
						endMsg.what = -10;
						mHandler.sendMessage(endMsg);
						killlist();
						endMsg = new Message();
						endMsg.what = -11;
						mHandler.sendMessage(endMsg);

				    } else {

						endMsg = new Message();
						endMsg.what = -8;
						mHandler.sendMessage(endMsg);				}
				}
			}
		}.start();
	}
	public void goset (View view){
		new LFilePicker().withActivity(this)
			.withRequestCode(Consant.REQUESTCODE_FROM_FRAGMENT)
			.withMutilyMode(false)
			.withFileFilter(new String[]{"tar.xz"})
			.withTheme(R.style.LFileTheme)
			.withTitle("Open From Packs")
			.start();
	}
	
	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Consant.REQUESTCODE_FROM_FRAGMENT) {
                List<String> list = data.getStringArrayListExtra("paths");
                for (final String s : list) {
					inputText.setText(s);
					
				}
			}
		}
    }
	
	private void killlist() {
		File file2=new File("/data/data/" + getPackageName() + "/app_runtime/");
		deleteDirWihtFile(file2);
	}
	private void deleteDirWihtFile(File file2) {
		if (file2 == null || !file2.exists() || !file2.isDirectory())
			return;
		for (File file :file2.listFiles()) {
			if (file.isFile())
				file.delete();
			else if (file.isDirectory())
				deleteDirWihtFile(file);

		}
		file2.delete();
	}
    
}
