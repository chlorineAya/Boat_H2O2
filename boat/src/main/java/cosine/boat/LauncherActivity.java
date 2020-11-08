package cosine.boat;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.os.Bundle;
import cosine.boat.logcat.Logcat;
import cosine.boat.logcat.LogcatService;
import ru.ivanarh.jndcrash.NDCrashError;
import ru.ivanarh.jndcrash.NDCrash;
import ru.ivanarh.jndcrash.NDCrashService;
import ru.ivanarh.jndcrash.NDCrashUnwinder;
import android.content.Intent;
import android.widget.TextView;
import android.widget.SeekBar;
import java.io.*;
import android.util.*;
import java.util.*;
import android.os.Handler;
import android.os.Message;
import android.graphics.Color;
import android.text.Html;
import android.widget.Spinner;
import android.widget.CheckBox;
import android.text.TextWatcher;
import android.text.Editable;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AdapterView;
import org.json.JSONObject;
import android.view.Menu;
import android.view.MenuItem;
import cosine.boat.activity.MainActivity;
import android.view.KeyEvent;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.content.SharedPreferences;
import android.content.Context;
import android.widget.ImageView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.example.custompullmenu.ui.DirchangeActivity;
import cosine.boat.download.DownloadActivity;
import android.view.WindowManager;

public class LauncherActivity extends Activity implements View.OnClickListener, OnCheckedChangeListener, View.OnLongClickListener, SeekBar.OnSeekBarChangeListener
{

    public Button playButton,save,reset;
	public EditText configText,mem,name,ram,bgdir;
	public Spinner Spinner;
    public Button excuteButton,nofile;
	public boolean mode = false;
    public EditText inputText;
	public TextView outputText,cfg_id,cfg_jvm;
	public CheckBox cb;
	public String str;
	private static String bg=null;
	private SeekBar seekBar;
    private TextView tv1;
    private EditText et1,et2;
    private int number;
	
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu); 
        
        //通过getMenuInflater()方法得到MenuInflater对象，再调用它的inflate()方法就可以给当前活动创建菜单了，第一个参数：用于指定我们通过哪一个资源文件来创建菜单；第二个参数：用于指定我们的菜单项将添加到哪一个Menu对象当中。
        return true; // true：允许创建的菜单显示出来，false：创建的菜单将无法显示。
    }
    
    

    /**
    *菜单的点击事件
    */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
   

        switch (item.getItemId()){
            case R.id.logout:
                Intent i = new Intent(this, MainActivity.class);
		        startActivity(i);
		        break;
		    case R.id.dirset:
                Intent i2 = new Intent(this, DownloadActivity.class);
		        startActivity(i2);
		        break;
		    default:
		        break;
		        }
		    return true;
		    }
	
	private class MyHandler extends Handler{
		@Override
		public void handleMessage(Message msg)
		{
			
			switch (msg.what){
				case -1:
				    String str0 = "<font color='#FF0000'>No such file!\n</font>";
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
				    playButton.setEnabled(true);
				    break;
				case -9:
				    playButton.setEnabled(false);   
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
	private MyHandler mHandler;
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
		
		final String logPath = "/mnt/sdcard/boat/log.txt";
		Logcat.initializeOutOfProcess( this, logPath, LogcatService.class);
		
		final String reportPath = "/mnt/sdcard/boat/crash.txt";
		System.out.println("Crash report: " + reportPath);
		final NDCrashError error = NDCrash.initializeOutOfProcess( this, reportPath, NDCrashUnwinder.libcorkscrew, NDCrashService.class);
		if (error == NDCrashError.ok) {
			System.out.println("NDCrash: OK");
			// Initialization is successful. 
		} else {
			System.out.println("NDCrash: Error");
			System.out.println(error.name());
			// Initialization failed, check error value. 
		} 
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.launcher_layout);
        
        SharedPreferences sp = this.getSharedPreferences("添加", Context.MODE_PRIVATE);
        

        
        
		this.mHandler = new MyHandler();
        this.playButton = (Button) findViewById(R.id.launcher_play_button);
        this.playButton.setOnClickListener(this);
        this.reset = (Button) findViewById(R.id.reset);
        this.reset.setOnClickListener(this);
		this.configText = (EditText) findViewById(R.id.launcher_config_text);
		this.nofile = (Button) findViewById(R.id.nofile);
		this.ram = (EditText)findViewById(R.id.ram);
		this.nofile.setOnClickListener(this);
		nofile.setVisibility(8);
		
		
		
//------Spinner、EditText通过json解析和文本监听器进行内存，版本，ID修改------		

       name=(EditText)findViewById(R.id.name);
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
        
        mem=(EditText)findViewById(R.id.mem);
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
        bgdir=(EditText)findViewById(R.id.bgdir);
        bgdir.addTextChangedListener(new TextWatcher(){
                @Override
                public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {
                }

                @Override
                public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {
                }

                @Override
                public void afterTextChanged(Editable p1) {
                    bgdir(p1.toString());
					
                }
				
				
            });
        bgdir.setText(bg());
        
        ImageView image1 = (ImageView) findViewById(R.id.iv1); 
		
		//获得ImageView对象
         /*为什么图片一定要转化为 Bitmap格式的！！ */
        Bitmap bitmap = getLoacalBitmap(bgdir.getText().toString()); //从本地取图片(在cdcard中获取)  //
        image1 .setImageBitmap(bitmap); //设置Bitmap
 
       
        final Spinner list=(Spinner) findViewById(R.id.ver);
		File versionlist = new File("/sdcard/boat/gamedir/versions");
		final String[] ver = versionlist.list() ;
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,ver);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		list.setAdapter(adapter);


        // 设置下拉列表的条目被选择监听器

        list.setOnItemSelectedListener(new OnItemSelectedListener() {



				@Override

				public void onItemSelected(AdapterView<?> p1, View p2, int p3, long p4) {
					
					if (!((String)p1.getItemAtPosition(p3)).equals("Null")) {
						setVersion((String)p1.getItemAtPosition(p3));
					}
					String str = (String) list.getSelectedItem();
					playButton.setText(getResources().getString(R.string.play)+"("+str+")");
					
					               }

                @Override
                public void onNothingSelected(AdapterView<?> p1) {
                }

			});


        list.setSelection(adapter.getPosition(ve()));
        
	
	
	
	

		
		this.configText.setText( getPreferences(MODE_PRIVATE).getString("config", "/sdcard/boat/config.txt"));
		getPreferences(MODE_PRIVATE).edit().putString("config", this.configText.getText().toString()).commit();
		
		if (this.configText.getText().toString() != null && !this.configText.getText().toString().equals("")){
			if (!new File(this.configText.getText().toString()).exists()){
				LauncherConfig.toFile(this.configText.getText().toString(), new LauncherConfig());
				
			}
					
		}
		this.excuteButton = (Button)findViewById(R.id.launcher_excute_button);
		this.excuteButton.setOnClickListener(this);
		this.save = (Button)findViewById(R.id.save);
		this.save.setOnClickListener(this);
		this.excuteButton.setOnLongClickListener(this);
		this.inputText = (EditText)findViewById(R.id.launcher_input_text);
		this.outputText = (TextView)findViewById(R.id.launcher_output_text);
		this.cfg_jvm = (TextView)findViewById(R.id.cfg_jvm);
		this.cfg_id = (TextView)findViewById(R.id.cfg_id);
		
		outputText.append("Runtime directory: " + this.getDir("runtime", 0) + "\n");
		outputText.append("Boat H2O version: 0.9.99 Snapshot\n");
		if(!new File("/data/data/cosine.boat/app_runtime/libopenal.so.1").exists()){
		
		
		
		String stra = "<font color='#FF0000'>Runtime:false\n</font>";
	    outputText.append(Html.fromHtml(stra));
		playButton.setVisibility(8);
		nofile.setVisibility(0);
		
		} else {
		
		   
		
		String stra = "<font color='#00FF00'>Runtime:true\n</font>";
	    outputText.append(Html.fromHtml(stra));
		
		}
		outputText.append("\n");
		if(!new File("/sdcard/boat/gamedir/versions").exists()){
		
		String stra = "<font color='#FF0000'>Gamedir:false\n</font>";
	    outputText.append(Html.fromHtml(stra));
		playButton.setVisibility(8);
		nofile.setVisibility(0);
		
		} else {
		
		   
		
		String stra = "<font color='#00FF00'>Gamedir:true\n</font>";
	    outputText.append(Html.fromHtml(stra));
		
		}
		
		outputText.append("\n");
		this.cb = (CheckBox) findViewById(R.id.cb);
		this.cb.setOnCheckedChangeListener(this);
		cb.setChecked(false);
		
		tv1=findViewById(R.id.tv1);
        et1=findViewById(R.id.et1);
        et2=findViewById(R.id.et2);
        seekBar=findViewById(R.id.seekBar);
		seekBar.setMax(1024);
		
        //设置监听器 监听数值改变情况
        seekBar.setOnSeekBarChangeListener(this);
        
   		et1.addTextChangedListener(new TextWatcher(){
                @Override
                public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {
                }

                @Override
                public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {
                }

                @Override
                public void afterTextChanged(Editable p1) {
                    et1(p1.toString());
					
                }
				
				
            });
        et1.setText(e1());
        et2.addTextChangedListener(new TextWatcher(){
                @Override
                public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {
                }

                @Override
                public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {
                }

                @Override
                public void afterTextChanged(Editable p1) {
                    et2(p1.toString());
					mem.setText(et1.getText().toString()+et2.getText().toString());
                }
				
				
            });
        et2.setText(e2());
   
		int number2 =Integer.parseInt(et1.getText().toString());
		seekBar.setProgress(number2);
		
		mem.setText("-client -Xms"+et1.getText().toString()+"M -Xmx"+et1.getText().toString()+"M"+et2.getText().toString());
    }
    
    
    @Override
	public void onCheckedChanged(CompoundButton checkBox, boolean checked) {
		// TODO Auto-generated method stub
		switch (checkBox.getId()) {
		case R.id.cb:
			if (checked) {
			configText.setVisibility(0);
			mem.setVisibility(0);
			cfg_jvm.setVisibility(0);
			inputText.setVisibility(0);
			outputText.setVisibility(0);
			excuteButton.setVisibility(0);
			bgdir.setVisibility(0);
			save.setVisibility(0);
			}else{
			configText.setVisibility(8);
			mem.setVisibility(8);
			cfg_jvm.setVisibility(8);
			inputText.setVisibility(8);
			outputText.setVisibility(8);
			excuteButton.setVisibility(8);
			bgdir.setVisibility(8);
			save.setVisibility(8);
			}
			break;
		default:
		    break;
		}
	}
	
	//数值改变
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        
		number = progress;
		tv1.setText("Ram"+number+"M");
		et1.setText(number+"");	
		}
    //开始拖动
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        tv1.setText("Ram"+number+"M");
    }
    //停止拖动
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        tv1.setText("Ram"+number+"M");
		mem.setText("-client -Xms"+number+"M -Xmx"+number+"M"+et2.getText().toString());
    }
	
	private String result = "";
	private String error = "";
	
	public void excuteCommand(final String args[]){
		
		
		new Thread(){
			@Override
			public void run(){
				try
				{
					Process p = new ProcessBuilder(args).start();
					BufferedReader bri=new BufferedReader(new InputStreamReader(p.getInputStream()));

					result = "";
					String linei;
					while((linei = bri.readLine()) != null){
						result = result + "\n" + linei;			
						
					}
					
					BufferedReader bre=new BufferedReader(new InputStreamReader(p.getErrorStream()));

					error = "";
					String linee;
					while((linee = bre.readLine()) != null){
						error = error + "\n" + linee;			

					}

					p.waitFor();
					int e = p.exitValue();
					
					Message endMsg=new Message();
					endMsg.what = e;
					mHandler.sendMessage(endMsg);
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}.start();
		
		
	}
	
	 public static Bitmap getLoacalBitmap(String url) {
         try {
              FileInputStream fis = new FileInputStream(url);
              return BitmapFactory.decodeStream(fis);  ///把流转化为Bitmap图片        

           } catch (FileNotFoundException e) {
              e.printStackTrace();
              return null;
         }
    }
	
	private void setVersion(String version) {
        try {
            FileInputStream in=new FileInputStream("/sdcard/boat/config.txt");
            byte[] b=new byte[in.available()];
            in.read(b);
            in.close();
            String str=new String(b);
            JSONObject json=new JSONObject(str);
            json.remove("currentVersion");
            json.put("currentVersion", "/sdcard/boat/gamedir/versions/" + version.trim()
                     );
            FileWriter fr=new FileWriter(new File("/sdcard/boat/config.txt"));
            fr.write(json.toString());
            fr.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
	private String ve() {
        try {
            FileInputStream in=new FileInputStream("/sdcard/boat/config.txt");
            byte[] b=new byte[in.available()];
            in.read(b);
            in.close();
            String str=new String(b);
			if (str.equals("")) {
				return "null";
			}
            JSONObject json=new JSONObject(str);
            String cnt=json.getString("currentVersion");
            return cnt.substring(cnt.indexOf("versions") + 9);
        } catch (Exception e) {
            return e.toString();
        }
    }
    private void bgdir(String 背景)
    {
        try
        {
            FileInputStream in=new FileInputStream("/sdcard/boat/config.txt");
            byte[] b=new byte[in.available()];
            in.read(b);
            in.close();
            String str=new String(b);
            JSONObject json=new JSONObject(str);
            json.remove("background");
            json.put("background", 背景);
            FileWriter fr=new FileWriter(new File("/sdcard/boat/config.txt"));
            fr.write(json.toString());
            fr.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }
 
    private String bg()
    {
        try
        {
            FileInputStream in=new FileInputStream("/sdcard/boat/config.txt");
            byte[] b=new byte[in.available()];
            in.read(b);
            in.close();
            String str=new String(b);
            JSONObject json=new JSONObject(str);
            return json.getString("background");
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        return "config.txt not found.";
    }
	private void name(String 玩家)
    {
        try
        {
            FileInputStream in=new FileInputStream("/sdcard/boat/config.txt");
            byte[] b=new byte[in.available()];
            in.read(b);
            in.close();
            String str=new String(b);
            JSONObject json=new JSONObject(str);
            json.remove("auth_player_name");
            json.put("auth_player_name", 玩家);
            FileWriter fr=new FileWriter(new File("/sdcard/boat/config.txt"));
            fr.write(json.toString());
            fr.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }
 
    private String nam()
    {
        try
        {
            FileInputStream in=new FileInputStream("/sdcard/boat/config.txt");
            byte[] b=new byte[in.available()];
            in.read(b);
            in.close();
            String str=new String(b);
            JSONObject json=new JSONObject(str);
            return json.getString("auth_player_name");
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        return "config.txt not found.";
    }
    private void mem(String nc)
    {
        try
        {
            FileInputStream in=new FileInputStream("/sdcard/boat/config.txt");
            byte[] b=new byte[in.available()];
            in.read(b);
            in.close();
            String str=new String(b);
            JSONObject json=new JSONObject(str);
            json.remove("extraJavaFlags");
            json.put("extraJavaFlags", nc);
            FileWriter fr=new FileWriter(new File("/sdcard/boat/config.txt"));
            fr.write(json.toString());
            fr.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }
  
    private String me()
    {
        try
        {
            FileInputStream in=new FileInputStream("/sdcard/boat/config.txt");
            byte[] b=new byte[in.available()];
            in.read(b);
            in.close();
            String str=new String(b);
            JSONObject json=new JSONObject(str);
            return json.getString("extraJavaFlags");
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        return "config.txt not found.";
    }
    private void et1(String aaa)
    {
        try
        {
            FileInputStream in=new FileInputStream("/sdcard/boat/config.txt");
            byte[] b=new byte[in.available()];
            in.read(b);
            in.close();
            String str=new String(b);
            JSONObject json=new JSONObject(str);
            json.remove("mem");
            json.put("mem", aaa);
            FileWriter fr=new FileWriter(new File("/sdcard/boat/config.txt"));
            fr.write(json.toString());
            fr.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }
  
    private String e1()
    {
        try
        {
            FileInputStream in=new FileInputStream("/sdcard/boat/config.txt");
            byte[] b=new byte[in.available()];
            in.read(b);
            in.close();
            String str=new String(b);
            JSONObject json=new JSONObject(str);
            return json.getString("mem");
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        return "config.txt not found.";
    }
    private void et2(String bbb)
    {
        try
        {
            FileInputStream in=new FileInputStream("/sdcard/boat/config.txt");
            byte[] b=new byte[in.available()];
            in.read(b);
            in.close();
            String str=new String(b);
            JSONObject json=new JSONObject(str);
            json.remove("ejf");
            json.put("ejf", bbb);
            FileWriter fr=new FileWriter(new File("/sdcard/boat/config.txt"));
            fr.write(json.toString());
            fr.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }
  
    private String e2()
    {
        try
        {
            FileInputStream in=new FileInputStream("/sdcard/boat/config.txt");
            byte[] b=new byte[in.available()];
            in.read(b);
            in.close();
            String str=new String(b);
            JSONObject json=new JSONObject(str);
            return json.getString("ejf");
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        return "config.txt not found.";
    }
     
    
	public void install(final String packagePath){
		new Thread(){
			@Override
			public void run(){
				Message endMsg = null;
				File packageFile = new File(packagePath);
				if (!packageFile.exists()){
					endMsg = new Message();
					endMsg.what = -1;
					mHandler.sendMessage(endMsg);
					
				}else{
				{
				endMsg = new Message();
				endMsg.what = -2;
				mHandler.sendMessage(endMsg);
				}
				Utils.extractTarXZ(packagePath, getDir("runtime", 0));
				endMsg = new Message();
				endMsg.what = -3;
				mHandler.sendMessage(endMsg);
				
				if (Utils.setExecutable(getDir("runtime", 0))){
					endMsg = new Message();
					endMsg.what = -4;
					mHandler.sendMessage(endMsg);
				}
				else{
					endMsg = new Message();
					endMsg.what = -5;
					mHandler.sendMessage(endMsg);
				}
				endMsg = new Message();
				endMsg.what = -6;
				mHandler.sendMessage(endMsg);
				
				LauncherConfig config = LauncherConfig.fromFile(configText.getText().toString());
				config.remove("runtimePath");
				config.put("runtimePath", getDir("runtime", 0).getAbsolutePath());
				LauncherConfig.toFile(configText.getText().toString(), config);
				endMsg = new Message();
				endMsg.what = -7;
				mHandler.sendMessage(endMsg);
			   
					if(!new File("/data/data/cosine.boat/app_runtime/libopenal.so.1").exists()){
			        
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
				    
				    }else{
				    
				    endMsg = new Message();
				    endMsg.what = -8;
				    mHandler.sendMessage(endMsg);
				}
			}
		}
		}.start();
	}
	
	
	
	//OnClickListener
    public void onClick(View v) {
        if (v == this.playButton) {			
            Intent i = new Intent(this, BoatActivity.class);
			Bundle bundle=new Bundle();
			bundle.putString("config", configText.getText().toString());
			i.putExtras(bundle);
			this.startActivity(i);
			
			
        }
        else if (v == this.save){
        if(!new File(bgdir.getText().toString()).exists()){
        Toast.makeText(LauncherActivity.this, "Null", 1000).show();
        bgdir.setText("");
        }else{
        Toast.makeText(LauncherActivity.this, "Saved", 1000).show();
        }
        }
        else if (v==this.nofile){
        Toast.makeText(LauncherActivity.this, R.string.cfg_nofile, 1000).show();
		}
		else if (v==this.reset){
	
		}
		else if(v == this.excuteButton){
			if (!mode){
				if (!inputText.getText().equals("")){
					String packagePath = inputText.getText().toString();
					install(packagePath);
				}
			}
		
			else{
				if (!inputText.getText().equals("")){
					String cmd = inputText.getText().toString();
					excuteCommand(cmd.split(" "));

				}
			}
			
		}
		
    }
    
    private void killlist()
	{
		File file2=new File("/data/data/" + getPackageName() + "/app_runtime/");
		deleteDirWihtFile(file2);
	}
	private void deleteDirWihtFile(File file2)
	{
		if(file2==null||!file2.exists()||!file2.isDirectory())
			return;
		for(File file :file2.listFiles()){
			if(file.isFile())
				file.delete();
			else if(file.isDirectory())
				deleteDirWihtFile(file);

		}
		file2.delete();
	}
	
	//OnLongClickListener
	@Override
	public boolean onLongClick(View p1)
	{
		// TODO: Implement this method
		if (p1 == excuteButton){
			excuteButton.setText(R.string.excute);
			mode = true;
			outputText.append("\n~~~For debuging only~~~\n");
			return true;
		}
		return false;
	}

	@Override
	protected void onPause()
	{
		// TODO: Implement this method
		super.onPause();
		getPreferences(MODE_PRIVATE).edit().putString("config", this.configText.getText().toString()).commit();
		if(!new File(bgdir.getText().toString()).exists()){
        bgdir.setText("");
        }
	}
    public boolean fileIsExists(String strFile)
    {
        try
        {
            File f=new File(strFile);
            if(!f.exists())
            {
                return false;
            }
 
        }
        catch (Exception e)
        {
            return false;
        }
 
        return true;
    }
 
    
}
