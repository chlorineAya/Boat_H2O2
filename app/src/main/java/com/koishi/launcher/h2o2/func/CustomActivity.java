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
import android.widget.Switch;
import android.widget.CompoundButton;
import android.support.v7.widget.CardView;
import android.widget.LinearLayout;
import android.view.ViewGroup;
import android.os.Message;
import android.widget.Toast;

public class CustomActivity extends AppCompatActivity {

    public TextView name,mem,c_p1,c_p2,c_p3,c_p4;
    public TextView mdl;

    public Switch c_sw1,c_sw2,c_sw3,c_sw4;
    public CardView ccc;

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
		name = (TextView)findViewById(R.id.name);
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

        mem = (TextView)findViewById(R.id.mem);
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

        ccc = (CardView)findViewById(R.id.ccc);
        c_p1 = (TextView)findViewById(R.id.c_p1);
        c_p1.addTextChangedListener(new TextWatcher(){
                @Override
                public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {
                }

                @Override
                public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {
                }

                @Override
                public void afterTextChanged(Editable p1) {
                    c_p1(p1.toString());
                }
            });
        c_p1.setText(cp1());

        c_p2 = (TextView)findViewById(R.id.c_p2);
        c_p2.addTextChangedListener(new TextWatcher(){
                @Override
                public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {
                }

                @Override
                public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {
                }

                @Override
                public void afterTextChanged(Editable p1) {
                    c_p2(p1.toString());
                }
            });
        c_p2.setText(cp2());

        c_p3 = (TextView)findViewById(R.id.c_p3);
        c_p3.addTextChangedListener(new TextWatcher(){
                @Override
                public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {
                }

                @Override
                public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {
                }

                @Override
                public void afterTextChanged(Editable p1) {
                    c_p3(p1.toString());
                }
            });
        c_p3.setText(cp3());

        c_p4 = (TextView)findViewById(R.id.c_p4);
        c_p4.addTextChangedListener(new TextWatcher(){
                @Override
                public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {
                }

                @Override
                public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {
                }

                @Override
                public void afterTextChanged(Editable p1) {
                    c_p4(p1.toString());
                }
            });
        c_p4.setText(cp4());

        c_sw1 = (Switch)findViewById(R.id.c_sw1);
        c_sw1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if (isChecked) {
						c_p1.setText("true");
						c_sw2.setEnabled(true);
					} else {
						c_p1.setText("false");
						c_sw2.setEnabled(false);
					}
				}
			});

        c_sw2 = (Switch)findViewById(R.id.c_sw2);
        c_sw2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if (isChecked) {
						c_p2.setText("true");
					} else {
						c_p2.setText("false");
					}
				}
			});
        c_sw3 = (Switch)findViewById(R.id.c_sw3);
        c_sw3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if (isChecked) {
						c_p3.setText("true");
					} else {
						c_p3.setText("false");
					}
				}
			});

        c_sw4 = (Switch)findViewById(R.id.c_sw4);
        c_sw4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if (isChecked) {
						c_p4.setText("true");
					} else {
						c_p4.setText("false");
					}
				}
			});

        String cp01= cp1();
        String cp02= cp2();
        String cp03= cp3();
        String cp04= cp4();

        if (cp01.equals("true")) {
            c_sw1.setChecked(true);
            c_sw2.setEnabled(true);
            ccc.setEnabled(true);
        } else {
            c_sw1.setChecked(false);
            c_sw2.setEnabled(false);
            ccc.setEnabled(false);
        }

        if (cp02.equals("true")) {
            c_sw2.setChecked(true);
        } else {
            c_sw2.setChecked(false);
        }

        if (cp03.equals("true")) {
            c_sw3.setChecked(true);
        } else {
            c_sw3.setChecked(false);
        }

        if (cp04.equals("true")) {
            c_sw4.setChecked(true);
        } else {
            c_sw4.setChecked(false);
        }

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

    private void c_p1(String p1) {
        try {
            FileInputStream in=new FileInputStream("/sdcard/games/com.koishi.launcher/h2o2/h2ocfg.json");
            byte[] b=new byte[in.available()];
            in.read(b);
            in.close();
            String str=new String(b);
            JSONObject json=new JSONObject(str);
            json.remove("mouseMode");
            json.put("mouseMode", p1);
            FileWriter fr=new FileWriter(new File("/sdcard/games/com.koishi.launcher/h2o2/h2ocfg.json"));
            fr.write(json.toString());
            fr.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private String cp1() {
        try {
            FileInputStream in=new FileInputStream("/sdcard/games/com.koishi.launcher/h2o2/h2ocfg.json");
            byte[] b=new byte[in.available()];
            in.read(b);
            in.close();
            String str=new String(b);
            JSONObject json=new JSONObject(str);
            return json.getString("mouseMode");
        } catch (Exception e) {
            System.out.println(e);
        }
        return "Error";
    }

    private void c_p2(String p2) {
        try {
            FileInputStream in=new FileInputStream("/sdcard/games/com.koishi.launcher/h2o2/h2ocfg.json");
            byte[] b=new byte[in.available()];
            in.read(b);
            in.close();
            String str=new String(b);
            JSONObject json=new JSONObject(str);
            json.remove("backToRightClick");
            json.put("backToRightClick", p2);
            FileWriter fr=new FileWriter(new File("/sdcard/games/com.koishi.launcher/h2o2/h2ocfg.json"));
            fr.write(json.toString());
            fr.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private String cp2() {
        try {
            FileInputStream in=new FileInputStream("/sdcard/games/com.koishi.launcher/h2o2/h2ocfg.json");
            byte[] b=new byte[in.available()];
            in.read(b);
            in.close();
            String str=new String(b);
            JSONObject json=new JSONObject(str);
            return json.getString("backToRightClick");
        } catch (Exception e) {
            System.out.println(e);
        }
        return "Error";
    }

    private void c_p3(String p3) {
        try {
            FileInputStream in=new FileInputStream("/sdcard/games/com.koishi.launcher/h2o2/h2ocfg.json");
            byte[] b=new byte[in.available()];
            in.read(b);
            in.close();
            String str=new String(b);
            JSONObject json=new JSONObject(str);
            json.remove("jumpToLeft");
            json.put("jumpToLeft", p3);
            FileWriter fr=new FileWriter(new File("/sdcard/games/com.koishi.launcher/h2o2/h2ocfg.json"));
            fr.write(json.toString());
            fr.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private String cp3() {
        try {
            FileInputStream in=new FileInputStream("/sdcard/games/com.koishi.launcher/h2o2/h2ocfg.json");
            byte[] b=new byte[in.available()];
            in.read(b);
            in.close();
            String str=new String(b);
            JSONObject json=new JSONObject(str);
            return json.getString("jumpToLeft");
        } catch (Exception e) {
            System.out.println(e);
        }
        return "Error";
    }

    private void c_p4(String p4) {
        try {
            FileInputStream in=new FileInputStream("/sdcard/games/com.koishi.launcher/h2o2/h2ocfg.json");
            byte[] b=new byte[in.available()];
            in.read(b);
            in.close();
            String str=new String(b);
            JSONObject json=new JSONObject(str);
            json.remove("dontEsc");
            json.put("dontEsc", p4);
            FileWriter fr=new FileWriter(new File("/sdcard/games/com.koishi.launcher/h2o2/h2ocfg.json"));
            fr.write(json.toString());
            fr.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private String cp4() {
        try {
            FileInputStream in=new FileInputStream("/sdcard/games/com.koishi.launcher/h2o2/h2ocfg.json");
            byte[] b=new byte[in.available()];
            in.read(b);
            in.close();
            String str=new String(b);
            JSONObject json=new JSONObject(str);
            return json.getString("dontEsc");
        } catch (Exception e) {
            System.out.println(e);
        }
        return "Error";
    }

    public void setName(View view) {
        final EditText editText = new EditText(CustomActivity.this);
		editText.setText(name.getText().toString());
		AlertDialog.Builder dialog = new AlertDialog.Builder(CustomActivity.this);
		dialog.setTitle("ID");
		dialog.setView(editText, 50, 0, 50, 0);

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
    
    public void sc1(View view){
        if (c_sw1.isChecked()){
            c_sw1.setChecked(false);
            c_sw2.setEnabled(false);
            ccc.setEnabled(false);
            
        }else{
            c_sw1.setChecked(true);
            c_sw2.setEnabled(true);
            ccc.setEnabled(true);
        }
        
    }
    
    public void sc2(View view){
        if (c_sw2.isChecked()){
            c_sw2.setChecked(false);
            
        }else{
            c_sw2.setChecked(true);
            
        }
        
    }
    
    public void sc3(View view){
        if (c_sw3.isChecked()){
            c_sw3.setChecked(false);
            
        }else{
            c_sw3.setChecked(true);
            
        }
        
    }
    
    public void sc4(View view){
        if (c_sw4.isChecked()){
            c_sw4.setChecked(false);
            
        }else{
            c_sw4.setChecked(true);
            
        }
        
    }
    
    public void del(View view){
        AlertDialog alertDialog1 = new AlertDialog.Builder(CustomActivity.this)
						 .setTitle(getResources().getString(R.string.menu_5_adt))//标题
						 .setIcon(R.drawable.ic_boat)//图标
						 .setMessage(getResources().getString(R.string.menu_5_adm))
						 .setPositiveButton("Yes Yes Yes", new DialogInterface.OnClickListener() {//添加"Yes"按钮
							 @Override
							 public void onClick(DialogInterface dialogInterface, int i) {
								 //TODO
								 new Thread(new Runnable(){
										 @Override
										 public void run() {
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
										 }
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
	
	public boolean deleteDirectory(String filePath) {
		boolean flag = false;
        //如果filePath不以文件分隔符结尾，自动添加文件分隔符
        if (!filePath.endsWith(File.separator)) {
            filePath = filePath + File.separator;
        }
        File dirFile = new File(filePath);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        flag = true;
        File[] files = dirFile.listFiles();
        //遍历删除文件夹下的所有文件(包括子目录)
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
				//删除子文件
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) break;
            } else {
				//删除子目录
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) break;
            }
        }
        if (!flag) return false;
        //删除当前空目录
        return dirFile.delete();
    }
    Handler han=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
			if (msg.what == 0) {
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.menu_5_del), Toast.LENGTH_SHORT).show();
			}
        }};
    public void setJVM(View view) {
        final EditText editText = new EditText(CustomActivity.this);
		editText.setText(mem.getText().toString());
		AlertDialog.Builder dialog = new AlertDialog.Builder(CustomActivity.this);
		dialog.setTitle("Jvm Flags");
		dialog.setView(editText, 50, 0, 50, 0);
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
    private void ramInfo() {
		ActivityManager am=(ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo mi=new MemoryInfo();
		am.getMemoryInfo(mi);
		String[] available=fileSize(mi.availMem);
		String[] total=fileSize(mi.totalMem);
		mdl.setText("RAM " + available[0] + available[1] + "/" + total[0] + total[1]); 
	}
	private String[] fileSize(long size) {  
        String str="";  
        if (size >= 1000) {  
            str = "KB";  
            size /= 1000;  
            if (size >= 1000) {  
                str = "MB";  
                size /= 1000;  
            }  
        }  
        /*将每3个数字用,分隔如:1,000*/  
        DecimalFormat formatter=new DecimalFormat();  
        formatter.setGroupingSize(3);  
        String result[]=new String[2];  
        result[0] = formatter.format(size);  
        result[1] = str;  
        return result;  
    }  

}
