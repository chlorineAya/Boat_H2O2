package com.mistake.revision.Download;


import com.download.service.util.Config;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import android.app.*;
import android.os.*;
import android.widget.*;
import com.alibaba.fastjson.*;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Request.Builder;
import com.download.service.util.*;
import com.download.service.*;
import com.download.service.downloader.DownloadManager;
import java.util.*;
import java.io.*;
import android.content.*;
import android.view.*;
import android.graphics.*;
import android.provider.*;
import com.mistake.revision.adapter.*;
import android.view.View.*;
import android.view.WindowManager.*;

import androidx.annotation.RequiresApi;

import java.nio.charset.*;
import com.download.service.downloader.*;

import org.koishi.launcher.h2o2pro.R;

public class DownloadService extends Service
{

	//API_version_manifest_json="http://launchermeta.mojang.com/mc/game/version_manifest.json";
	//API_version_manifest_json_bmclapi="https://bmclapi2.bangbang93.com/mc/game/version_manifest.json";
	//API_version_manifest_json_mcbbs="https://download.mcbbs.net/mc/game/version_manifest.json";

	//"https://launchermeta.mojang.com/";
	//"https://launcher.mojang.com/";
	//bmclapi="https://bmclapi2.bangbang93.com/";
	//mcbbs="https://download.mcbbs.net/";

	//API_Assets="http://resources.download.minecraft.net/";
	//API:Assets_bmclapi="https://bmclapi2.bangbang93.com/assets/";
	//API_Assets_mcbbs="https://download.mcbbs.net/assets/";

	//API_Libraries="https://libraries.minecraft.net/";
	//API_Libraries_bmclapi="https://bmclapi2.bangbang93.com/maven/";
	//API:Libraries_mcbbs="https://download.mcbbs.net/maven/";

	//version_json_server_client_bmclapi="https://bmclapi2.bangbang93.com/version/";
	//version_json_server_client_mcbbs="https://download.mcbbs.net/version/";

	private  DownloadManager mDownloadManager;

	private ArrayList<LibrariesUtil>libraries;
	private ArrayList<AssetsUtil>assets;


	private View base;

	private TextView mlist;
	private TextView mpath1,mpath2, mpath3;
	private ProgressBar mpath_progress1, mpath_progress2, mpath_progress3;
	private TextView overall_progress, stages_progress;

	//private boolean switch_api;

	private TextView title;
	private LinearLayout mwindow;
	private Button button;
	private WindowManager.LayoutParams layoutparams_button;

	private ImageButton close;
	private ImageButton pause_start1,pause_start2,pause_start3;
//
	private static String Source_address;
	private static String 
	API_Version_client_server_json,
	API_Assets,
	API_Libraries,
	API_Manifest_Version_json;

	private static String 
	game_directory,
	assets_root;

	private static String 
	Version,
	Version_assets,
	Version_json,
	Version_jar,
	//Version_url,
	Version_libraries;

	private String assets_id;

	private String Txt;



	//某读取文本
	private  String ReadString(String sourcePath){

		StringBuffer sb = new StringBuffer();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(sourcePath));
			String str;
			while((str = br.readLine()) != null) {
				sb.append(str);
				sb.append("\r\n");
			}
			br.close();
		}catch(Exception e){
			setText(e.toString());
		}

		return sb.toString();

	}



	private boolean download1=false;
	private boolean download2=false;
	private boolean download3=false;
	public static boolean isStarted = false;
	private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;
	private  ArrayList<Object> Temporary;
	private int libraries_loader_size=0;
	private int libraries_loading_size=0;
	private int assets_loader_size=0;
	private int assets_loading_size=0;
    @RequiresApi(api = Build.VERSION_CODES.M)
	@Override
    public void onCreate()  {
        super.onCreate();
        isStarted = true;
		windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        layoutParams = new WindowManager.LayoutParams();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;

        } else {
			layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;

        }
        layoutParams.format = PixelFormat.RGBA_8888;
        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

		int height = windowManager.getDefaultDisplay().getHeight();
		int width = windowManager.getDefaultDisplay().getWidth();	

		if(height>width){
			layoutParams.width = width*9/10;
			layoutParams.height = width*8/10;
			layoutParams.x = (width/2)-(layoutParams.width/2);
			layoutParams.y = (height/2)-(layoutParams.height/2);
		}else{
			layoutParams.width = height*9/10;
			layoutParams.height = height*8/10;
			layoutParams.x = (width/2)-(layoutParams.width/2);
			layoutParams.y = (height/2)-(layoutParams.height/2);
		}
		layoutParams.alpha=0.8f;
		mDownloadManager = new DownloadManager();


		showWindow();
		Open_notice_Service();

		//只被调用一次

    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
		loading(intent);
        return super.onStartCommand(intent, flags, startId);
    }

	private void loading(Intent intent)
	{

		loading_config(
			intent.getExtras().getString("version"),
			intent.getExtras().getString("game"),
			intent.getExtras().getString("address"));



	}

    @RequiresApi(api = Build.VERSION_CODES.M)
	private void showWindow() {
        if (Settings.canDrawOverlays(this)) {
			try{
				LayoutInflater layoutInflater = LayoutInflater.from(this);
				base = layoutInflater.inflate(R.layout.download, null);
				mpath1=(TextView)basefindViewById(R.id.downloadTextView1);
				mpath2=(TextView)basefindViewById(R.id.downloadTextView2);
				mpath3=(TextView)basefindViewById(R.id.downloadTextView3);
				mpath_progress1=(ProgressBar)base.findViewById(R.id.downloadProgressBar1);
				mpath_progress2=(ProgressBar)base.findViewById(R.id.downloadProgressBar2);
				mpath_progress3=(ProgressBar)base.findViewById(R.id.downloadProgressBar3);
				overall_progress=(TextView)basefindViewById(R.id.downloadTextView4);
				stages_progress=(TextView)basefindViewById(R.id.downloadTextView5);
				mlist=(TextView)basefindViewById(R.id.downloadTextView6);
				//title=(TextView)basefindViewById(R.id.downloadTextView7);
				close=(ImageButton)base.findViewById(R.id.downloadImageButton1);
				pause_start1=(ImageButton)base.findViewById(R.id.downloadImageButton2);
				pause_start2=(ImageButton)base.findViewById(R.id.downloadImageButton3);
				pause_start3=(ImageButton)base.findViewById(R.id.downloadImageButton4);





				close.setOnClickListener(onclick);
				pause_start1.setOnClickListener(onclick);
				pause_start2.setOnClickListener(onclick);
				pause_start3.setOnClickListener(onclick);



				mwindow=(LinearLayout)base.findViewById(R.id.downloadLinearLayout1);
				mwindow.setOnTouchListener(window);
				button=new Button(this);
				button.setBackgroundResource(R.drawable.ic_h2o2_low_px);
				button.setVisibility(View.GONE);
				layoutparams_button=new WindowManager.LayoutParams();
				layoutparams_button.copyFrom(layoutParams);
				layoutparams_button.width=64;
				layoutparams_button.height=64;
				button.setOnTouchListener(window);
				button.setOnClickListener(onclick);
				windowManager.addView(button,layoutparams_button);
				windowManager.addView(base,layoutParams);



				mpath_progress1.setProgress(0);
				mpath_progress2.setProgress(0);
				mpath_progress3.setProgress(0);
				mpath1.setText("");
				mpath2.setText("");
				mpath3.setText("");





			}catch(Exception e){
				Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show();



			}
		}
    }

	public  void loading_config(String version,String homepath,String address){



		//Version = "1.7.10";//下载版本--->manifest_version_json
		//game_directory = "/storage/emulated/0/boat/gamedir";//主游戏目录->结尾不带"/"
		//Source_address = "https://download.mcbbs.net";//需要提供的源->结尾不带"/"
		Version = version;//下载版本--->manifest_version_json
		game_directory = homepath;//主游戏目录->结尾不带"/"
		Source_address = address;//需要提供的源->结尾不带"/"


		API_Version_client_server_json = Source_address + "/version/";
		API_Assets = Source_address + "/assets/";
		API_Libraries = Source_address + "/maven/";
		API_Manifest_Version_json = Source_address + "/mc/game/version_manifest.json";

		assets_root = game_directory+"/assets";
		Version_jar = game_directory+"/versions/"+Version+"/"+ Version+".jar";
		Version_json = game_directory+"/versions/"+Version+"/"+ Version+".json";
		//Version_url=url;
		Version_libraries = game_directory+"/libraries/";
		Version_assets = assets_root+"/";


		//如何终结



		/*{
		 "assets_root":"/storage/emulated/0/boat/gamedir/assets",
		 "auth_access_token":"0",
		 "auth_player_name":"Steve",
		 "auth_session":"0",
		 "auth_uuid":"00000000-0000-0000-0000-000000000000",
		 "currentVersion":"/storage/emulated/0/boat/gamedir/versions/1.7.10",
		 "extraJavaFlags":"-server -Xms500M -Xmx500M",
		 "extraMinecraftFlags":"",
		 "game_assets":"/storage/emulated/0/boat/gamedir/assets/virtual/legacy",
		 "game_directory":"/storage/emulated/0/boat/gamedir",
		 "home":"/storage/emulated/0/boat",
		 "runtimePath":"/data/user/0/cosine.boat/app_runtime/32",
		 "user_properties":"{}",
		 "user_type":"mojang"
		 }*/



		mhandler.sendEmptyMessage(0);

	}





	private View.OnTouchListener window= new View.OnTouchListener(){

		private int x;
        private int y;
		private long Time1;
		private int movedX=0;
		private int movedY=0;
		private long Time2;

        @Override
        public boolean onTouch(View v, MotionEvent event)
		{
			switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x = (int) event.getRawX();
                    y = (int) event.getRawY();
					Time1 = System.currentTimeMillis();
                    break;
                case MotionEvent.ACTION_MOVE:
					int nowX = (int) event.getRawX();
                    int nowY = (int) event.getRawY();
					movedX = nowX - x;
					movedY = nowY - y;
					Time2 = System.currentTimeMillis() - Time1;
                    x = nowX;
                    y = nowY;
					layoutParams.x = layoutParams.x + movedX;
					layoutParams.y = layoutParams.y + movedY;
					windowManager.updateViewLayout(base, layoutParams);
					layoutparams_button.x = layoutparams_button.x + movedX;
					layoutparams_button.y = layoutparams_button.y + movedY;
					windowManager.updateViewLayout(button, layoutparams_button);

					break;
				case MotionEvent.ACTION_UP:
					if(Time2<100){

					}

					break;
			}
			return false;
		}
	};
    private View.OnClickListener onclick=new View.OnClickListener(){

		@Override
		public void onClick(View p1)
		{
			if(p1==close){
				base.setVisibility(View.GONE);
				button.setVisibility(View.VISIBLE);


			}else if(p1==pause_start1){

				download1(mDownloadManager.Self_destruction1());



			}else if(p1==pause_start2){

				download2(mDownloadManager.Self_destruction2());

			}else if(p1==pause_start3){

				download3(mDownloadManager.Self_destruction3());




			}

			else if(p1==button){
				base.setVisibility(View.VISIBLE);
				button.setVisibility(View.GONE);

			}

		}
	};


	private TextView basefindViewById(int id){
		TextView a=base.findViewById(id);
		a.setTextColor(Color.GRAY);
		return a;
	}


	private void append(String name,int a,int b){
		Message msg = new Message(); 

		msg.what =10; 
		msg.obj=name; 
		msg.arg1=a;
		msg.arg2=b;
		mhandler.sendMessage(msg);
		setText(name);

	}
	private  void setText(String a){
		mlist.append(a+"\n");
	}
	private String turn_Length(long len){
		if(len<1024){
			return len+"B";
		}
		if(len<1024*1024){
			return String.format("%.2f%s",(len/1024.0),"K");
		}
		if(len<1024*1024*1024)
			return String.format("%.2f%s",(len/(1024*1024.0)),"M");
		return String.format("%.2f%s",(len/(1024*1024*1024.0)),"G");
	}
	
	private void run_download_libraries(){
		success_libraries=0;
		overall_libraries=0;
		Temporary=new ArrayList<Object>();
		ArrayList<Object> unexists_File_Manager=new ArrayList<Object>();
		for(LibrariesUtil util:libraries){
			if(util.get()){
				File file=new File(Version_libraries+util.getpath());
				util.setpath(file.getAbsolutePath());

				util.seturl(Turn_Url(API_Libraries,util.getname()));

				if(file.exists()){
					if(file.isDirectory()){
						file.delete();
						unexists_File_Manager.add(util);
						overall_libraries+=util.getsize();
					}else{
						if(file.length()!=util.getsize()){
							file.delete();
							unexists_File_Manager.add(util);
							overall_libraries+=util.getsize();
						}
					}
				}else{
					unexists_File_Manager.add(util);
					overall_libraries+=util.getsize();
				}
			}else{
				/*File file=new File(Turn_Path(Version_libraries,util.getname()));
				 util.setpath(file.getAbsolutePath());
				 if(switch_api){
				 util.seturl(Turn_Url(APILibraries_bmclapi,util.getname()));
				 }else{
				 util.seturl(Turn_Url(APILibraries,util.getname()));
				 }
				 if(file.exists()){
				 if(file.isDirectory()){
				 file.delete();
				 unexists_File_Manager.add(util);
				 }else{
				 //此为数据缺失
				 }
				 }else{
				 unexists_File_Manager.add(util);
				 }*/

			}
		}
		if(unexists_File_Manager.size()>=1){
			libraries_loader_size=unexists_File_Manager.size();

			Temporary=unexists_File_Manager;
			mhandler.sendEmptyMessageDelayed(1000,800);
		}else{
			mhandler.sendEmptyMessage(5);
		}
	}
	private void download_status1(boolean status){
		if(status){
			pause_start1.setBackgroundResource(R.drawable.download_pause);
		}else{
			pause_start1.setBackgroundResource(R.drawable.download_play);
		}

	}private void download_status2(boolean status){
		if(status){
			pause_start2.setBackgroundResource(R.drawable.download_pause);
		}else{
			pause_start2.setBackgroundResource(R.drawable.download_play);
		}

	}private void download_status3(boolean status){
		if(status){
			pause_start3.setBackgroundResource(R.drawable.download_pause);
		}else{
			pause_start3.setBackgroundResource(R.drawable.download_play);
		}

	}


	private void run_download_assets(){
		success_libraries=0;
		overall_libraries=0;
		Temporary=new ArrayList<Object>();
		ArrayList<Object> unexists_File_Manager=new ArrayList<Object>();
		for(AssetsUtil util:assets){
			if(util.get()){
				File file=new File(Version_assets+"objects/"+util.gethash().substring(0,2)+"/"+util.gethash());
				if(file.exists()){
					if(file.isDirectory()){
						file.delete();
						unexists_File_Manager.add(util);
						overall_libraries+=util.getsize();
					}else{
						if(file.length()!=util.getsize()){
							file.delete();
							unexists_File_Manager.add(util);
							overall_libraries+=util.getsize();
						}
					}
				}else{
					unexists_File_Manager.add(util);
					overall_libraries+=util.getsize();
				}
			}
		}
		if(unexists_File_Manager.size()>=1){
			assets_loader_size=unexists_File_Manager.size();
			Temporary=unexists_File_Manager;
			mhandler.sendEmptyMessageDelayed(2000,800);
		}else{
			mhandler.sendEmptyMessage(6);
		}
	}
	

	private void download1(FilePoint f){

		if(null!=f){
			download_manager_1(
				f.getUrl(),//链接
				f.getFilePath(),//路径
				f.getFileName(),//文件名
				f.getA(),
				f.getObject(),
				f.getSize());

			//完全移植
			setText("重新下载>"+f.getObject());
			//选择继续or选择新下载

		}

	}
	private void download2(FilePoint f){
		if(null!=f){
			download_manager_2(
				f.getUrl(),//链接
				f.getFilePath(),//路径
				f.getFileName(),//文件名
				f.getA(),
				f.getObject(),
				f.getSize());

			//完全移植
			setText("重新下载>"+f.getObject());
			//选择继续or选择新下载
		}


	}
	private void download3(FilePoint f){
		if(null!=f){
			download_manager_3(
				f.getUrl(),//链接
				f.getFilePath(),//路径
				f.getFileName(),//文件名
				f.getA(),
				f.getObject(),
				f.getSize());

			//完全移植
			setText("重新下载>"+f.getObject());
			//选择继续or选择新下载
		}


	}
	private void download_manager_1(//第一个通道
		final String url,//链接
		final String path,//路径
		final String name,//文件名
		final int a,
		final Object object,
		final int size
	) {
        mDownloadManager.add1(url, path, name,a,object, size,new DownloadListner() {


				@Override
				public void onFinished() {
					download1=false;
					if(a==2000){
						assets_loading_size+=1;
						append((String)object,assets_loading_size,assets_loader_size);
					}else if(a==1000){
						libraries_loading_size+=1;

						append((String)object,libraries_loading_size,libraries_loader_size);
					}

					success_libraries+=size;
					mhandler.sendEmptyMessage(a);

				}
				@Override
				public void onProgress(float progress, long i, long s)
				{
					mpath_progress1.setProgress((int) (progress * 100));
					mpath1.setText(object+"："+turn_Length(i) + File.pathSeparatorChar+turn_Length(s));

				}
				@Override
				public void onPause() {//暂停
					setText(object+"Pause");

				}
				@Override
				public void onCancel() {//取消
					setText(object+"Cancel");

				}
			});
		download1=true;
		download_status1(mDownloadManager.download1_stop_start());

    }
	private  void download_manager_2(//第一个通道
		final String url,//链接
		final String path,//路径
		final String name,//文件名
		final int a,
		final Object object,
		final int size

	) {
        mDownloadManager.add2(url, path, name, a, object,size ,new DownloadListner() {



				@Override
				public void onFinished() {
					download2=false;
					if(a==2000){
						assets_loading_size+=1;

						append((String)object,assets_loading_size,assets_loader_size);
					}else if(a==1000){
						libraries_loading_size+=1;
						append((String)object,libraries_loading_size,libraries_loader_size);
					}
					success_libraries+=size;

					mhandler.sendEmptyMessage(a);

				}
				@Override
				public void onProgress(float progress, long i, long s)
				{
					mpath_progress2.setProgress((int) (progress * 100));
					mpath2.setText(object+"："+turn_Length(i) + File.pathSeparatorChar+turn_Length(s));

				}
				@Override
				public void onPause() {//暂停
					setText(object+"Pause");
				}
				@Override
				public void onCancel() {//取消
					setText(object+"Cancel");
				}
			});
		download2=true;
		download_status2(mDownloadManager.download2_stop_start());


    }
	private  void download_manager_3(//第一个通道
		final String url,//链接
		final String path,//路径
		final String name,//文件名
		final int a,
		final Object object,
		final int size
	) {
        mDownloadManager.add3(url, path, name,a,object,size, new DownloadListner() {



				@Override
				public void onFinished() {
					download3=false;
					if(a==2000){
						assets_loading_size+=1;

						append((String)object,assets_loading_size,assets_loader_size);
					}else if(a==1000){
						libraries_loading_size+=1;
						append((String)object,libraries_loading_size,libraries_loader_size);
					}
					success_libraries+=size;

					mhandler.sendEmptyMessage(a);
				}
				@Override
				public void onProgress(float progress, long i, long s)
				{
					mpath_progress3.setProgress((int) (progress * 100));
					mpath3.setText(object+"："+turn_Length(i) + File.pathSeparatorChar+turn_Length(s));

				}
				@Override
				public void onPause() {//暂停
					setText(object+"Pause");
				}
				@Override
				public void onCancel() {//取消
					setText(object+"Cancel");
				}
			});
		download3=true;
		download_status3(mDownloadManager.download3_stop_start());

    }
	private void setlibraries(){
		overall_progress.setText(getResources().getString(R.string.total)+libraries_loading_size+"/"+libraries_loader_size);
		stages_progress.setText(turn_Length(success_libraries)+File.pathSeparatorChar+turn_Length(overall_libraries));
	}
	private void setassets(){
		overall_progress.setText(getResources().getString(R.string.total)+assets_loading_size+"/"+assets_loader_size);
		stages_progress.setText(turn_Length(success_libraries)+File.pathSeparatorChar+turn_Length(overall_libraries));

	}
	private long overall_libraries;
	private long success_libraries;

	private NotificationManager manager; 
	private Notification notif; 




	private void Open_notice_Service(){

		manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE); 
		notif = new Notification(); 
		notif.icon = R.drawable.java; 
		notif.tickerText = "新通知"; 
		//通知栏显示所用到的布局文件 
		notif.contentView = new RemoteViews(getPackageName(), R.layout.notice);
		manager.notify(1, notif); 

	}


	public  Handler mhandler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
				case 1000:
					if(Temporary.size()>=1){
						if(!download1){
							mhandler.sendEmptyMessage(101);
							setlibraries();
						}else{
							if(!download2){
								mhandler.sendEmptyMessage(101);
								setlibraries();
							}else{
								if(!download3){
									mhandler.sendEmptyMessage(101);
									setlibraries();
								}else{

								}
							}
						}
					}else{
						setlibraries();
						Message msg1=new Message();
						msg1.what=8;
						msg1.arg1=5;
						mhandler.sendMessage(msg1);
					}
					break;
				case 2000:
					if(Temporary.size()>=1){
						if(!download1){
							mhandler.sendEmptyMessage(101);
							setassets();
						}else{
							if(!download2){
								mhandler.sendEmptyMessage(101);
								setassets();
							}else{
								if(!download3){
									mhandler.sendEmptyMessage(101);
									setassets();
								}else{


								}
							}
						}
					}else{
						setassets();
						Message msg2=new Message();
						msg2.what=8;
						msg2.arg1=6;
						mhandler.sendMessage(msg2);
					}
					break;
				case 0:

					mhandler.sendEmptyMessageAtTime(1, 500);
					break;
				case 1:
					setText("(Download):"+Version+"!");
					Dwonload_Version_json(Version,API_Version_client_server_json+Version+"/json");

					break;
				case 2:



					Txt = ReadString(game_directory + "/versions/" + Version + "/" + Version + ".json");



					//String Url=API_Version_client_server_json + Version + "/client";
					Download_client_Assets(Txt);






					break;
				case 3:



					VersionLibraries(Txt);
					VersionAssets(ReadString(Version_assets + "indexes/" + assets_id + ".json"));
					break;
				case 4:
					run_download_libraries();
					break;
				case 101:

					if(Temporary.get(0)instanceof AssetsUtil){

						object_turn_assets(API_Assets);

					}else if(Temporary.get(0) instanceof LibrariesUtil){
						object_turn_libraries();
					}

					break;
				case 5:
					run_download_assets();
					break;
				case 6:
					/*
					 for(LibrariesUtil a:libraries){
					 append(Turn_Url(APILibraries_bmclapi,a.getname()));
					 append(Turn_Path(Version_libraries,a.getname()));
					 }*/

					mpath_progress1.setProgress(100);
					mpath_progress2.setProgress(100);
					mpath_progress3.setProgress(100);
					mpath1.setText("已完成"+Version+"下载");
					mpath2.setText("已完成"+Version+"下载");
					mpath3.setText("已完成"+Version+"下载");
					append("已完成"+Version+"下载",100,100);
					break;
				case 8:
					if(!download2&&!download3&&!download1){
						mhandler.sendEmptyMessage(3);
					}
					break;
				case 9:
					//网页文件无法下载调用
					/*try
					 {

					 msg.obj;
					 }
					 catch (IOException e)
					 {
					 setText(e.toString());
					 }*/
					break;
				case 10: 




					notif.contentView.setTextViewText(R.id.noticeTextView1,(String)msg.obj); 
					notif.contentView.setTextViewText(R.id.noticeTextView2,overall_progress.getText().toString()); 
					notif.contentView.setTextViewText(R.id.noticeTextView3,stages_progress.getText().toString()); 
					notif.contentView.setProgressBar(R.id.noticeProgressBar1,msg.arg2,msg.arg1 , false); 
					manager.notify(1, notif); 




					break; 
			}
			super.handleMessage(msg);
		}
	};


	/*
	 private String[] turn(String [] a){

	 }*/
	private  void object_turn_libraries()
	{
		//101101102103201202203
		try{
			ArrayList<Object> list=new ArrayList<>();
			if(Temporary.size()>1){
				LibrariesUtil rock=(LibrariesUtil) Temporary.get(0);
				String url=rock.geturl();
				String path = Path(rock.getpath());
				String name = Path_name(rock.getpath());
				if (!download1){
					download_manager_1(url,path,name,1000,name,rock.getsize());
					for (int i = 0; i < Temporary.size(); i++){
						if (i!=0){
							list.add((LibrariesUtil)Temporary.get(i));
						}
					}
					Temporary=(ArrayList<Object>)list;
					if(!download2){
						mhandler.sendEmptyMessage(101);
					}else{
						if(!download3){
							mhandler.sendEmptyMessage(101);
						}else{

						}
					}//true未加载
				}else{
					if(!download2){
						download_manager_2(url,path,name,1000,name,rock.getsize());
						for (int i = 0; i < Temporary.size(); i++){
							if (i!=0){
								list.add((LibrariesUtil)Temporary.get(i));
							}
						}
						Temporary=(ArrayList<Object>)list;
						if(!download3){
							mhandler.sendEmptyMessage(101);
						}else{
							if(!download1){
								mhandler.sendEmptyMessage(101);
							}else{

							}
						}//true未加载
					}else{
						if(!download3){
							download_manager_3(url,path,name,1000,name,rock.getsize());
							for (int i = 0; i < Temporary.size(); i++){
								if (i!=0){
									list.add((LibrariesUtil)Temporary.get(i));
								}
							}
							Temporary=(ArrayList<Object>)list;
							if(!download2){
								mhandler.sendEmptyMessage(101);
							}else{
								if(!download3){
									mhandler.sendEmptyMessage(101);
								}else{
									//list2.clear();

								}
							}//true未加载
						}
					}
				}
			}else{
				LibrariesUtil rock=(LibrariesUtil) Temporary.get(0);
				String url=rock.geturl();
				String path = Path(rock.getpath());
				String name = Path_name(rock.getpath());
				if(!download1){
					download_manager_1(url,path,name,1000,name,rock.getsize());
					Temporary.remove(0);
				}else{
					if(!download2){
						download_manager_2(url,path,name,1000,name,rock.getsize());
						Temporary.remove(0);
					}else{
						if(!download3){
							download_manager_3(url,path,name,1000,name,rock.getsize());
							Temporary.remove(0);
						}else{

						}
					}
				}
			}

		}catch(Exception e){
			setText(e.toString());
		}

	}
	private  void object_turn_assets(String Assets)
	{
		//10 11 01 10 21 03 2 0 1 20 22 03
		try{
			ArrayList<Object> list=new ArrayList<>();
			if(Temporary.size()>1){
				AssetsUtil rock=(AssetsUtil) Temporary.get(0);
				String url=Assets+rock.gethash().substring(0,2)+"/"+ rock.gethash();
				String path = Version_assets+"objects/"+rock.gethash().substring(0,2);
				String name = rock.gethash();
				if(!download1){
					download_manager_1(url,path,name,2000,rock.getname(),rock.getsize());
					for (int i = 0; i < Temporary.size(); i++){
						if (i!=0){
							list.add((AssetsUtil)Temporary.get(i));
						}
					}
					Temporary=(ArrayList<Object>)list;
					if(!download2){
						mhandler.sendEmptyMessage(101);
					}else{
						if(!download3){
							mhandler.sendEmptyMessage(101);
						}else{

						}
					}//true未加载
				}else{
					if(!download2){
						download_manager_2(url,path,name,2000,rock.getname(),rock.getsize());
						for (int i = 0; i < Temporary.size(); i++){
							if (i!=0){
								list.add((AssetsUtil)Temporary.get(i));
							}
						}
						Temporary=(ArrayList<Object>)list;
						if(!download3){
							mhandler.sendEmptyMessage(101);
						}else{
							if(!download1){
								mhandler.sendEmptyMessage(101);
							}else{

							}
						}//true未加载
					}else{
						if(!download3){
							download_manager_3(url,path,name,2000,rock.getname(),rock.getsize());
							for (int i = 0; i < Temporary.size(); i++){
								if (i!=0){
									list.add((AssetsUtil)Temporary.get(i));
								}
							}
							Temporary=(ArrayList<Object>)list;
							if(!download2){
								mhandler.sendEmptyMessage(101);
							}else{
								if(!download3){
									mhandler.sendEmptyMessage(101);
								}else{
									//list2.clear();
								}
							}//true未加载
						}
					}
				}
			}else if(Temporary.size()==1){
				AssetsUtil rock=(AssetsUtil) Temporary.get(0);
				String url=Assets+rock.gethash().substring(0,2)+"/"+ rock.gethash();
				String path = Version_assets+"objects/"+rock.gethash().substring(0,2);
				String name = rock.gethash();
				if(!download1){
					download_manager_1(url,path,name,2000,rock.getname(),rock.getsize());
					Temporary.remove(0);
				}else{
					if(!download2){
						download_manager_2(url,path,name,2000,rock.getname(),rock.getsize());
						Temporary.remove(0);
					}else{
						if(!download3){
							download_manager_3(url,path,name,2000,rock.getname(),rock.getsize());
							Temporary.remove(0);
						}else{

						}
					}
				}
			}

		}catch(Exception e){
			setText(e.toString());
		}
	}
	private void Dwonload_Version_json(String id, String url){
		File e=new File(Version_json);
		if(!e.exists()){
			download_manager_1(url,game_directory+"/versions/"+id,Version+".json",2,"Version->"+Version+"->json",0) ;
		}else{
			if(e.isDirectory()){
				e.delete();
				download_manager_1(url,game_directory+"/versions/"+id,Version+".json",2,"Version->"+Version+"->json",0) ;
			}else{
				mhandler.sendEmptyMessage(2);
			}
		}
	}
	public void Get_okHttp_Response_body_string(String url1,final int msg1)
	{
		HttpUtil.sendOkHttpRequest(url1, new okhttp3.Callback(){
				@Override
				public void onFailure(Call call, final IOException e) {
					//
				}
				@Override
				public void onResponse(Call p1, Response p2) throws IOException
				{
					final String url=p2.body().string();
					new Thread(){
						public void run(){
							Message msg = new Message();
							msg.what = msg1;
							msg.obj = url;

							mhandler.sendMessage(msg);
						}
					}.start();
				}
			});
	}

	private void Download_client_Assets(String json){
		try{
			JSONObject objects=JSON.parseObject(json);
			JSONObject client= objects.getJSONObject("downloads");
			JSONObject main= client.getJSONObject("client");

			String Url=(String) main.get("url");
			int size=Integer.valueOf((Integer) main.get("size"));


			JSONObject assetindex= objects.getJSONObject("assetIndex");
			assets_id=(String)assetindex.get("id");
			String url=((String)assetindex.get("url"));
			File version_assetindex=new File(Version_assets+"indexes/"+(String)assetindex.get("id")+".json");
			File version_client=new File(Version_jar);


			if(!version_client.exists()&&!version_assetindex.exists()){
				download_manager_2(Url,game_directory+"/versions/"+Version,Version+".jar",8,"Version->"+Version+"->jar",size);
				download_manager_3(url,Version_assets+"indexes",(String)assetindex.get("id")+".json",8,"Version->"+assets_id+"->assets->json",0);
			}else if(!version_client.exists()&&version_assetindex.exists()){
				download_manager_2(Url,game_directory+"/versions/"+Version,Version+".jar",8,"Version->"+Version+"->jar",size);
			}else if(version_client.exists()&&!version_assetindex.exists()){
				download_manager_3(url,Version_assets+"indexes",(String)assetindex.get("id")+".json",8,"Version->"+assets_id+"->assets->json",0);
			}else if(version_client.exists()&&version_assetindex.exists()){
				if(version_client.isDirectory()){
					version_client.delete();
					setText("已检测client文件错误!");
					download_manager_2(Url,game_directory+"/versions/"+Version,Version+".jar",8,"Version->"+Version+"->jar",size);
				}else{
					if(version_client.length()!=(int)main.get("size")){
						version_client.delete();
						setText("已检测client文件错误!");
						download_manager_2(Url,game_directory+"/versions/"+Version,Version+".jar",8,"Version->"+Version+"->jar",size);
					}else{
						Message msg=new Message();
						msg.what=8;
						msg.arg1=3;
						mhandler.sendMessage(msg);
					}
				}
			}
		}catch(Exception e){
			setText(e.toString());
		}

	} 
	//https://bmclapi2.bangbang93.com/version/1.7.10/server client json 
	private String Turn_Url(String api,String a){
		String b=a.substring(0, a.lastIndexOf(":"));
		String c=a.substring(a.lastIndexOf(":")+1);
		String d=b.substring(b.lastIndexOf(":")+1)+"-"+c+".jar";
		String e=b.substring(0, a.lastIndexOf(":"));
		String f=e.replace(".","/");
		String g=f.replace(":","/");
		return api+g+"/"+c+"/"+d;
	}
	private String Turn_Path(String api,String a){
		String b=a.substring(0, a.lastIndexOf(":"));
		String c=a.substring(a.lastIndexOf(":")+1);
		String d=b.substring(b.lastIndexOf(":")+1)+"-"+c+".jar";
		String e=b.substring(0, a.lastIndexOf(":"));
		String f=e.replace(".","/");
		String g=f.replace(":","/");
		return api+g+"/"+c+"/"+d;
	}
	private void VersionLibraries(String json){
		if(json==null){
			mhandler.sendEmptyMessage(3);
			return;
		}else{
			JSONObject version_json = JSON.parseObject(json);
			JSONArray version_libraries = version_json.getJSONArray("libraries");
			libraries=new ArrayList<LibrariesUtil>();
			for (int i = 0 ; i < version_libraries.size();i++){
				JSONObject key = (JSONObject)version_libraries.get(i);
				JSONObject downloads = (JSONObject)key.get("downloads");
				JSONObject artifact=(JSONObject)downloads.get("artifact");
				if(artifact!=null){
					LibrariesUtil util =new LibrariesUtil();
					util.setname((String)key.get("name"));
					util.setpath((String)artifact.get("path"));
					util.seturl((String)artifact.get("url"));
					util.setsize((int)artifact.get("size"));
					util.set(true);
					libraries.add(util);
				}else{
					/*LibrariesUtil util =new LibrariesUtil();
					 util.setname((String)key.get("name"));
					 util.setpath("");
					 util.seturl("");
					 util.setsize(0);
					 util.set(false);
					 libraries.add(util);
					 */
				}
				/*
				 JSONObject downloads = (JSONObject)key.get("downloads");
				 JSONObject artifact=(JSONObject)downloads.get("artifact");
				 if(artifact!=null){
				 util.setpath((String)artifact.get("path"));
				 util.seturl(((String)artifact.get("url")).replace(APILibraries,Libraries));
				 util.setsize((int)artifact.get("size"));
				 util.set(true);
				 }else{
				 }
				 get_libraries_list.add(util);*/
			}
			mhandler.sendEmptyMessage(4);
		}
	} 
	public void VersionAssets(String json){
		if(json==null){
			mhandler.sendEmptyMessage(2);
			return;
		}
		try {
			//String json= ReadString("/sdcard/assets.json");
			JSONObject assetindex=JSON.parseObject(json);
			JSONObject objects= assetindex.getJSONObject("objects");
			String turn_txt=objects.toJSONString();
			Gson gson = new Gson();	
			Map<String, VersionAssetsUtil> map = gson.fromJson(turn_txt,new TypeToken<Map<String,VersionAssetsUtil>>() {}.getType());    
			assets=new ArrayList<AssetsUtil>();
			for (Map.Entry<String, VersionAssetsUtil> entry : map.entrySet()) {    
				//System.out.println("key= " +  + " and value= " + );    
				AssetsUtil util=new AssetsUtil();
				util.setname(entry.getKey());
				util.sethash(entry.getValue().gethash());
				util.setsize(entry.getValue().getsize());
				util.set(true);
				assets.add(util);
			}
		} catch (Exception e) {
			//e.toString();
			//建议重载
			setText(e.toString());
		}
	} 
	private String Path_name(String path){
		return path.substring(path.lastIndexOf("/")+1); 
	}
	private String Path(String path){
		return path.substring(0, path.lastIndexOf("/"));
	}







}
