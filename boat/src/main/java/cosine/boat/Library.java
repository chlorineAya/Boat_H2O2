package cosine.boat;

import android.app.*;
import android.os.*;
import android.widget.*;
import java.io.*;
import java.util.*;
import android.view.*;
import android.view.View.*;
import java.util.zip.*;
import android.webkit.*;
import java.nio.file.*;
import android.app.AlertDialog.*;
import android.content.*;
import android.widget.AdapterView.*;

import cosine.boat.Decompression.ProgressUtil.ZipListener;
import cosine.boat.Decompression.*;
import java.util.stream.Stream;
public class Library extends Activity 
{
	private Button button1,button2,button3,button4;
	private TextView textview1,textview2,textview3;
	private Spinner spinner1;
	private ListView listview1;
	private ArrayList<String> modelspinner;
	private File file;
	private int ing,buffer;
	private String mode,Path,string;
	
	private int ings;
	public boolean modes = false;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.runtime);
		
		button1=(Button)findViewById(R.id.runtimeButton1);
		button2=(Button)findViewById(R.id.runtimeButton2);
		button3=(Button)findViewById(R.id.runtimeButton3);
		button4=(Button)findViewById(R.id.runtimeButton4);
		textview1=(TextView)findViewById(R.id.runtimeTextView1);
		textview2=(TextView)findViewById(R.id.runtimeTextView2);
		textview3=(TextView)findViewById(R.id.runtimeTextView3);
		spinner1=(Spinner)findViewById(R.id.runtimeSpinner1);
		listview1=(ListView)findViewById(R.id.runtimeListView1);
		spinners();
		listviews();
		Path = getApplication().getFilesDir().getParentFile().getPath();
		string=Path+"/";
		//"/data/data/net.zhuoweizhang.boardwalk/app_runtime";
		file = new File(string);
		textview3.setText(string);
		File[] files = file.listFiles();
        if (files == null) {
            Toast.makeText(this, "访问失败", Toast.LENGTH_SHORT).show();
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			Map<String, Object> map;
			map = new HashMap<String, Object>();
			map.put("text", ">/+-*|");
			map.put("img",R.drawable.timg);
			list.add(map);
			SimpleAdapter sa = new SimpleAdapter(Library.this, list,
												 R.layout.list, new String[] { "img", "text" },
												 new int[] { R.id.listImageView1, R.id.listTextView1 });
			listview1.setAdapter(sa);
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map;
		Arrays.sort(files);
		for (File targetFile : files) {
			map = new HashMap<String, Object>();
			map.put("text", targetFile.getName());
			File filesss=new File(string+"/"+targetFile.getName());
			File[] filess = filesss.listFiles();
			if (filesss.isDirectory()) {
				map.put("img", R.drawable.folder);
			} else if (filesss.isFile()) {
				map.put("img",over(targetFile.getName()));
			}else if(filess==null){
				map.put("img",R.drawable.wen);
			}
			else{
				map.put("img",over(targetFile.getName()));
			}
			list.add(map);
		}
		SimpleAdapter sa = new SimpleAdapter(Library.this, list,
											 R.layout.list, new String[] { "img", "text" },
											 new int[] { R.id.listImageView1, R.id.listTextView1 });
		listview1.setAdapter(sa);
		listview1.setOnItemLongClickListener(new OnItemLongClickListener(){
				@Override
				public boolean onItemLongClick(AdapterView<?> p1, View p2, int p3, long p4)
				{
					Object Texts=listview1.getItemAtPosition(p3);
					Map entry = (Map)Texts;
					String  Text= (String) entry.get("text");
					String bo=Text+"/";
						String go=string+bo;
						String dui="/sdcard/";
					Gestures(go,dui);
					return true;
				}
			});
		button1.setOnClickListener(cases);
		button2.setOnClickListener(cases);
		button3.setOnClickListener(cases);
		button4.setOnClickListener(cases);
	}
	private void listviews()
	{
		listview1.setOnItemClickListener(new AdapterView.OnItemClickListener(){
				private ArrayList urls;
				Boolean fil;
				int Folder,contain;
				private File[] files;
				private String boing;
				@Override
				public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4)
				{
					Object Texts=listview1.getItemAtPosition(p3);
					Map entry = (Map)Texts;
					String  Text= (String) entry.get("text");
						boing=Text+"/";
					if(Judgement(Text)==0&&Detect(string+boing)==1){
						string=string+boing;
						Filelistview(string);
					}else{
					}
				}
			});}
	private void Filelistview(String to)
	{
		string=to;
		file=new File(to);
		textview3.setText(file.getAbsolutePath());
		File[] files = file.listFiles();
		if (files == null) {
			Toast.makeText(this, "访问被阻", Toast.LENGTH_SHORT).show();
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			Map<String, Object> map;
				map = new HashMap<String, Object>();
				map.put("text", "Empty");
				map.put("img",R.drawable.timg);
				list.add(map);
			SimpleAdapter sa = new SimpleAdapter(Library.this, list,
												 R.layout.list, new String[] { "img", "text" },
												 new int[] { R.id.listImageView1, R.id.listTextView1 });
			listview1.setAdapter(sa);
			return;
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map;
		Arrays.sort(files);
		for (File targetFile : files) {
			map = new HashMap<String, Object>();
			map.put("text", targetFile.getName());
			File filesss=new File(to+"/"+targetFile.getName());
			File[] filess = filesss.listFiles();
			if (filesss.isDirectory()) {
				map.put("img", R.drawable.folder);
			} else if (filesss.isFile()) {
				map.put("img",over(targetFile.getName()));
			}else if(filess==null){
				map.put("img",R.drawable.wen);
			}
			else{
				map.put("img",over(targetFile.getName()));
				}
			list.add(map);
		}
		SimpleAdapter sa = new SimpleAdapter(Library.this, list,
											 R.layout.list, new String[] { "img", "text" },
											 new int[] { R.id.listImageView1, R.id.listTextView1 });
		listview1.setAdapter(sa);
	}
	private int over(String text)
	{
		if(text.indexOf(".sh")!=-1){
			ings=R.drawable.viihgv;
		}else if(text.indexOf(".tar")!=-1){
			ings=R.drawable.wen;
		}else if(text.indexOf(".gz")!=-1){
			ings=R.drawable.wen;
		}else if(text.indexOf(".png")!=-1){
			ings=R.drawable.vpver;
		}else if(text.indexOf(".mp3")!=-1){
			ings=R.drawable.vmnvff;
		}else if(text.indexOf(".log")!=-1){
			ings=R.drawable.vrty;
		}else if(text.indexOf(".obj")!=-1){
			ings=R.drawable.wen;
		}else if(text.indexOf(".mtl")!=-1){
			ings=R.drawable.wen;
		}else if(text.indexOf(".json")!=-1){
			ings=R.drawable.vqw;
		}else if(text.indexOf(".re")!=-1){
			ings=R.drawable.wen;
		}else if(text.indexOf(".bat")!=-1){
			ings=R.drawable.vqw;
		}else if(text.indexOf(".apk")!=-1){
			ings=R.drawable.viihgv;
		}else if(text.indexOf(".rc")!=-1){
			ings=R.drawable.viihgv;
		}else if(text.indexOf(".zip") != -1){ 
			ings=R.drawable.wen;
		}else if(text.indexOf(".xml")!=-1){
			ings=R.drawable.vrty;
		}else if(text.indexOf(".so")!=-1){
			ings=R.drawable.viihgv;
		}else if(text.indexOf(".html")!=-1){
			ings=R.drawable.vpouy;
		}else if(text.indexOf(".txt")!=-1){
			ings=R.drawable.vqw;
		}else if(text.indexOf(".js")!=-1){
			ings=R.drawable.vqw;
		}else if(text.indexOf(".jar")!=-1){
			ings=R.drawable.file;
		}else if(text.indexOf(".cfg")!=-1){
			ings=R.drawable.vnfdr;
		}else if(text.indexOf(".ttf")!=-1){
			ings=R.drawable.verrt;
		}else if(text.indexOf(">/+-*|")!=-1){
			ings=R.drawable.verrt;
		}else if(text.indexOf("Empty")!=-1){
			ings=R.drawable.timg;
		}else {
			ings=R.drawable.verrt;
		}
		return ings;
	}
	private void spinners()
	{
		File file = new File("/sdcard/boat/runtime/");
        File[] files = file.listFiles();
        if (files == null) {
            Toast.makeText(this, "没有运行库", Toast.LENGTH_SHORT).show();
			String[] computent = new String[] {"»Empty«"};
			ArrayAdapter <String>spinners =new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, computent);
			spinner1.setAdapter(spinners);
			file.mkdir();//创建文件夹
            return;
        }
        modelspinner = new ArrayList<String>() {

		
			public Stream<String> stream() {
				return null;
			}

			
			public Stream<String> parallelStream() {
				return null;
			}

		};
        for (File targetFile : files) {
            modelspinner.add(targetFile.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, modelspinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
					mode=modelspinner.get(pos);
				}
				@Override
				public void onNothingSelected(AdapterView<?> p1)
				{
				}});
    }
	private int Detect(String computer)
	{
		File file = new File(computer);
		if(file.isDirectory()){
			buffer=1;
		}
		if(file.isFile()){
			buffer=0;
		}
		return buffer;
	}
	private int Judgement(String text)
	{
		if(text.indexOf(".zip") != -1){ 
			ing=1;
		}else if(text.indexOf(".xml")!=-1){
			ing=1;
		}else if(text.indexOf(".so")!=-1){
			ing=1;
		}else if(text.indexOf(".html")!=-1){
			ing=1;
		}else if(text.indexOf(".txt")!=-1){
			ing=1;
		}else if(text.indexOf(".jar")!=-1){
			ing=1;
		}else if(text.indexOf(".cfg")!=-1){
			ing=1;
		}else if(text.indexOf(".ttf")!=-1){
			ing=1;
		}else if(text.indexOf(">/+-*|")!=-1){
			ing=1;
		}else if(text=="Empty"){
			ing=1;
		}else {
			ing=0;
		}
		return ing;
	}
	private View.OnClickListener cases= new OnClickListener(){
		@Override
		public void onClick(View p1)
		{
			String one=Path+"/";
			String two="/sdcard/boat/runtime/"+mode;
			if(p1==button1){
				File Filezip = new File(two);
				if(!Filezip.exists()){
					unZip("导入了一个寂寞",false);
				}else{
					if(Filezip.isDirectory()){
						unZip("导入了一个寂寞",false);
					}else if(Filezip.isFile()){
						
						if(mode.indexOf(".zip") != -1){ 
							unZip("已开始导入，勿退出",true);
							Gesture(two,one);
						}else{
							unZip("无法解压该类型的文件",true);
						}
					}
				}
			}
		else if(p1==button2){
				File and=new File(string);
				if(and.getParentFile()==null){
				}else {
					String Hurry=and.getParentFile().getAbsolutePath().toString()+"/";
					Filelistview(Hurry);
				}
			}else if(p1==button3){
				string=Path+"/";
				Filelistview(string);
			}else if(p1==button4){
				Filelistview(string);
			}
		}
};
		private void unZip(String p0, boolean p1)
		{
			Toast.makeText(this,p0,Toast.LENGTH_SHORT).show();
		}
	private void deleteDirWihtFile(File file2)
	{
		if(file2==null||!file2.exists()||!file2.isDirectory())
			return;
		for(File file :file2.listFiles()){
			if(file.isFile()){
				file.delete();
			}else if(file.isDirectory())
				deleteDirWihtFile(file);
		}
		file2.delete();
	}
	private void Gesture(String a,String b)
    {
    	MyDialogFragment uFunctionalInterface = new MyDialogFragment(a,b);
		uFunctionalInterface.show(getFragmentManager(), "mydialog");
		uFunctionalInterface.setCancelable(false);
		setTheme(R.style.AppTheme);
    }
	class MyDialogFragment extends DialogFragment
	{
		private ProgressBar progressBar1;
		private String one,two;
		private TextView textview4;
		private int DynamicState;
		private TextView textview5;
		private Button button5;
		public MyDialogFragment(String a, String b){
			this.one=a;
			this.two=b;
		}
		private Handler UIhandle = new Handler(){
			@Override
			public void handleMessage(Message msg){
				super.handleMessage(msg);
				if(msg.what==DynamicState) {
					textview4.setText(progressBar1.getProgress()+"%");
					UIhandle.sendEmptyMessageDelayed(DynamicState, 500);
				}
			}
		};
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState)
		{
			View v = LayoutInflater.from(Library.this).inflate(R.layout.question, null);
			progressBar1 = (ProgressBar) v.findViewById(R.id.questionProgressBar1); 
			textview4=(TextView)v.findViewById(R.id.questionTextView1);
			textview5=(TextView)v.findViewById(R.id.questionTextView2);
			button5=(Button)v.findViewById(R.id.questionButton1);
			button5.setOnClickListener(new View.OnClickListener(){
					@Override
					public void onClick(View p1)
					{
						deleteDirWihtFile(new File(Path+"/app_runtime/"));
						Filelistview(Path+"/");
						dismiss();
					}
				});
		ProgressUtil.UnZipFile(one, two, new ZipListener() { 
					public void zipSuccess() { 
						dismiss();
					}
					public void zipStart() { 
					textview5.setText("正在解压");
					} 
					public void zipProgress(int progress) { 
					UIhandle.sendEmptyMessageDelayed(DynamicState, 500);
					progressBar1.setProgress(progress);
					} 
					public void zipFail() { 
					button5.setVisibility(0);
					textview4.setText("选择");
					textview5.setText("解压失败");
					} 
				}); 
			Builder builder = new Builder(Library.this);
			builder.setView(v);
			builder.setCancelable(false);
			return builder.create();
		}
}
	private void Gestures(String a,String b)
    {
    	MyDialog uface = new MyDialog(a,b);
		uface.show(getFragmentManager(), "log");
		setTheme(R.style.AppTheme);
    }
	class MyDialog extends DialogFragment
	{
		private ProgressBar progressBar1;
		private String one,two;
		private TextView textview4;
		private TextView textview5;
		private Button button5;
		public MyDialog(String a, String b){
			this.one=a;
			this.two=b;
		}
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState)
		{
			View v = LayoutInflater.from(Library.this).inflate(R.layout.question, null);
			progressBar1 = (ProgressBar) v.findViewById(R.id.questionProgressBar1); 
			textview4=(TextView)v.findViewById(R.id.questionTextView1);
			textview5=(TextView)v.findViewById(R.id.questionTextView2);
			button5=(Button)v.findViewById(R.id.questionButton1);
			progressBar1.setVisibility(8);
			textview4.setText("删除输出文件");
			button5.setText("删除");
			button5.setVisibility(0);
			button5.setOnClickListener(new View.OnClickListener(){
					@Override
					public void onClick(View p1)
					{
						File v=new File(one);
						if(!v.isFile()){
							deleteDirWihtFile(v);
						}else if(!v.isDirectory()){
							v.delete();
							progressBar1.setVisibility(0);
							progressBar1.setProgress(100);
						}
						Filelistview(string);
						dismiss();
					}
				});
			Builder builder = new Builder(Library.this);
			builder.setView(v);
			builder.setCancelable(false);
			return builder.create();
		}
	}
}
