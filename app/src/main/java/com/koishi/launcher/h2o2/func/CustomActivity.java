package com.koishi.launcher.h2o2.func;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.koishi.launcher.h2o2.R;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.text.TextWatcher;
import android.text.Editable;
import android.os.Handler;
import java.io.FileInputStream;
import org.json.JSONObject;
import java.io.FileWriter;
import java.io.File;
import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import java.text.DecimalFormat;

public class CustomActivity extends AppCompatActivity {
    
    public EditText name,mem;
    public TextView mdl;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);
		
		Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar3);
		setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
				}
			});
		name = (EditText)findViewById(R.id.name);
	    name.addTextChangedListener(new TextWatcher(){
                @Override
                public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {
                }

                @Override
                public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {
                }

                @Override
                public void afterTextChanged(Editable p1) {
                    name(p1.toString());

                }

            });
        name.setText(nam());

        mem = (EditText)findViewById(R.id.mem);
		mem.addTextChangedListener(new TextWatcher(){
                @Override
                public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {
                }

                @Override
                public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {
                }

                @Override
                public void afterTextChanged(Editable p1) {
                    mem(p1.toString());
                }
            });
        mem.setText(me());
        this.mdl = (TextView)findViewById(R.id.mdl);
		ramInfo();
		final Handler handler=new Handler();  
        final Runnable runnable=new Runnable() {  
        @Override  
        public void run() {  
        // TODO Auto-generated method stub  
        //要做的事情  
        ramInfo();
        handler.postDelayed(this, 1000);  // 1秒后执行
        }  
        };  
        handler.postDelayed(runnable, 1000);//1秒执行一次runnable

        
    }
    private void name(String player) {
        try {
            FileInputStream in=new FileInputStream("/sdcard/games/com.koishi.launcher/h2o2/config.txt");
            byte[] b=new byte[in.available()];
            in.read(b);
            in.close();
            String str=new String(b);
            JSONObject json=new JSONObject(str);
            json.remove("auth_player_name");
            json.put("auth_player_name", player);
            FileWriter fr=new FileWriter(new File("/sdcard/games/com.koishi.launcher/h2o2/config.txt"));
            fr.write(json.toString());
            fr.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private String nam() {
        try {
            FileInputStream in=new FileInputStream("/sdcard/games/com.koishi.launcher/h2o2/config.txt");
            byte[] b=new byte[in.available()];
            in.read(b);
            in.close();
            String str=new String(b);
            JSONObject json=new JSONObject(str);
            return json.getString("auth_player_name");
        } catch (Exception e) {
            System.out.println(e);
        }
        return "Error";
    }

    private void mem(String nc) {
        try {
            FileInputStream in=new FileInputStream("/sdcard/games/com.koishi.launcher/h2o2/config.txt");
            byte[] b=new byte[in.available()];
            in.read(b);
            in.close();
            String str=new String(b);
            JSONObject json=new JSONObject(str);
            json.remove("extraJavaFlags");
            json.put("extraJavaFlags", nc);
            FileWriter fr=new FileWriter(new File("/sdcard/games/com.koishi.launcher/h2o2/config.txt"));
            fr.write(json.toString());
            fr.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private String me() {
        try {
            FileInputStream in=new FileInputStream("/sdcard/games/com.koishi.launcher/h2o2/config.txt");
            byte[] b=new byte[in.available()];
            in.read(b);
            in.close();
            String str=new String(b);
            JSONObject json=new JSONObject(str);
            return json.getString("extraJavaFlags");
        } catch (Exception e) {
            System.out.println(e);
        }
        return "Error";
    }
    public void setName(View view){
        final EditText editText = new EditText(CustomActivity.this);
		editText.setText(name.getText().toString());
                AlertDialog.Builder dialog = new AlertDialog.Builder(CustomActivity.this);
                dialog.setTitle("ID");
                dialog.setView(editText);
                dialog.setCancelable(true);
                dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        name.setText(editText.getText().toString());
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dialog.show();
    }
    public void setJVM(View view){
        final EditText editText = new EditText(CustomActivity.this);
		editText.setText(mem.getText().toString());
                AlertDialog.Builder dialog = new AlertDialog.Builder(CustomActivity.this);
                dialog.setTitle("Jvm Flags");
                dialog.setView(editText);
                dialog.setCancelable(true);
                dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mem.setText(editText.getText().toString());
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dialog.show();
    }
    private void ramInfo(){
		ActivityManager am=(ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo mi=new MemoryInfo();
		am.getMemoryInfo(mi);
		String[] available=fileSize(mi.availMem);
		String[] total=fileSize(mi.totalMem);
		mdl.setText("RAM "+available[0]+available[1]+"/"+total[0]+total[1]); 
	}
	private String[] fileSize(long size){  
        String str="";  
        if(size>=1000){  
            str="KB";  
            size/=1000;  
            if(size>=1000){  
                str="MB";  
                size/=1000;  
            }  
        }  
        /*将每3个数字用,分隔如:1,000*/  
        DecimalFormat formatter=new DecimalFormat();  
        formatter.setGroupingSize(3);  
        String result[]=new String[2];  
        result[0]=formatter.format(size);  
        result[1]=str;  
        return result;  
    }  
	
}
