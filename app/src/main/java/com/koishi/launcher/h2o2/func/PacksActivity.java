package com.koishi.launcher.h2o2.func;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

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

import com.koishi.launcher.h2o2.R;
import android.view.View;
import android.text.TextWatcher;
import android.text.Editable;
import org.json.JSONObject;
import com.leon.lfilepickerlibrary.LFilePicker;
import com.koishi.launcher.h2o2.tools.Consant;
import java.nio.charset.Charset;

public class PacksActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView listview1;
	private ArrayList<String> mlist;
	private File file;
	private String Path,string;
	private TextView tv;
	private ProgressDialog pd;
//	private MyAdapter myAdapter;

	private ImageButton btnCancel,btnSet,btnNew;
	private EditText packName;
	private LinearLayout ll5;

	private int ings;
	public boolean modes = false;

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.m5_add:
                packName.setText("");
				check();
				ll5.setVisibility(View.INVISIBLE);
				packName.setVisibility(View.VISIBLE);
				btnSet.setVisibility(View.VISIBLE);
				btnNew.setVisibility(View.VISIBLE);
				btnCancel.setVisibility(View.VISIBLE);
		        break;
		    case R.id.m5_dn:

		        break;
				
			case R.id.m5_ref:
				Filelistview(Path + "/");
				break;
			default:
				break;
		}
		return true;
	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packs);

		Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar5);
		setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
				}
			});
		listview1 = (ListView)findViewById(R.id.runtimeListView1);

		btnSet = (ImageButton)findViewById(R.id.btnset);
		btnCancel = (ImageButton)findViewById(R.id.btncancel);
		btnNew = (ImageButton)findViewById(R.id.btnnew);
		btnSet.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		btnNew.setOnClickListener(this);
		ll5 = (LinearLayout)findViewById(R.id.ll5);
		packName = (EditText)findViewById(R.id.pn);
		packName.addTextChangedListener(new TextWatcher(){
				@Override
                public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {
					check();
				}
                @Override
                public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {
                }

                @Override
                public void afterTextChanged(Editable p1) {
					check();
                }
            });
		pd = new ProgressDialog(PacksActivity.this);
        pd.setMessage("Waiting...");
        pd.setIndeterminate(true);
        pd.setCancelable(false);
//		listview1.setAdapter(myAdapter);
		listviews();
		tv = (TextView)findViewById(R.id.tv_dir);
		tv.addTextChangedListener(new TextWatcher(){
                @Override
                public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {
                }

                @Override
                public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {
                }

                @Override
                public void afterTextChanged(Editable p1) {
                    tv(p1.toString());				
                }
            });
        tv.setText(t());
		creatfile();
		Path = "/storage/emulated/0/games/com.koishi.launcher/packs";
		string = Path + "/";
		//"/data/data/net.zhuoweizhang.boardwalk/app_runtime";
		file = new File(string);
		File[] files = file.listFiles();
        if (files == null) {
            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			Map<String, Object> map;
			map = new HashMap<String, Object>();
			map.put("text", ">/+-*|");
			map.put("img", R.drawable.timg);
			list.add(map);
			SimpleAdapter sa = new SimpleAdapter(PacksActivity.this, list,
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
			File filesss=new File(string + "/" + targetFile.getName());
			File[] filess = filesss.listFiles();
			if (filesss.isDirectory()) {
				map.put("img", R.drawable.folder);
			} else if (filesss.isFile()) {
				map.put("img", over(targetFile.getName()));
			} else if (filess == null) {
				map.put("img", R.drawable.wen);
			} else {
				map.put("img", over(targetFile.getName()));
			}
			list.add(map);
		}
		SimpleAdapter sa = new SimpleAdapter(PacksActivity.this, list,
											 R.layout.list, new String[] { "img", "text" },
											 new int[] { R.id.listImageView1, R.id.listTextView1 });
		listview1.setAdapter(sa);
		listview1.setOnItemLongClickListener(new OnItemLongClickListener(){
				@Override
				public boolean onItemLongClick(AdapterView<?> p1, View p2, int p3, long p4) {
					Object Texts=listview1.getItemAtPosition(p3);
					Map entry = (Map)Texts;
					final String  Text= (String) entry.get("text");
					String bo=Text + "/";
					String go=string + bo;
					String dui="/sdcard/";
					AlertDialog alertDialog1 = new AlertDialog.Builder(listview1.getContext())
						.setTitle(getResources().getString(R.string.menu_5_adt))//标题
						.setIcon(R.drawable.ic_boat)//图标
						.setMessage(getResources().getString(R.string.menu_5_adm))
						.setPositiveButton("Yes Yes Yes", new DialogInterface.OnClickListener() {//添加"Yes"按钮
							@Override
							public void onClick(DialogInterface dialogInterface, int i) {
								//TODO
								pd.show();
								tv.setText("/storage/emulated/0/games/com.koishi.launcher/h2o2/gamedir");
								new Thread(new Runnable(){
										@Override
										public void run() {
											String file2= Path + "/" + Text;
											File file = new File(file2);
											if (file.isDirectory()) {
												deleteDirectory(file2);
											}
											if (file.isFile()) {
												deleteFile(file2);
											}
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
					return true;
				}
			});
    }
    private void check() {
		final String s = packName.getText().toString();
        if (s.equals("")) {
			btnSet.setEnabled(false);
			btnNew.setEnabled(false);
		} else {
			btnSet.setEnabled(true);
			btnNew.setEnabled(true);
		}
    }
    private void listviews() {
		listview1.setOnItemClickListener(new AdapterView.OnItemClickListener(){
				private ArrayList urls;
				Boolean fil;
				int Folder,contain;
				private File[] files;
				private String boing;
				@Override
				public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4) {
					Object Texts=listview1.getItemAtPosition(p3);
					Map entry = (Map)Texts;
					String  Text= (String) entry.get("text");
					boing = Text + "/";
					tv.setText("/storage/emulated/0/games/com.koishi.launcher/packs/" + Text + "/boat/gamedir");
				}
			});}
	private void Filelistview(String to) {
		string = to;
		file = new File(to);
		File[] files = file.listFiles();
		if (files == null) {
			Toast.makeText(this, "No permission", Toast.LENGTH_SHORT).show();
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			Map<String, Object> map;
			map = new HashMap<String, Object>();
			map.put("text", "Empty");
			map.put("img", R.drawable.timg);
			list.add(map);
			SimpleAdapter sa = new SimpleAdapter(PacksActivity.this, list,
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
			File filesss=new File(to + "/" + targetFile.getName());
			File[] filess = filesss.listFiles();
			if (filesss.isDirectory()) {
				map.put("img", R.drawable.folder);
			} else if (filesss.isFile()) {
				map.put("img", over(targetFile.getName()));
			} else if (filess == null) {
				map.put("img", R.drawable.wen);
			} else {
				map.put("img", over(targetFile.getName()));
			}
			list.add(map);
		}
		SimpleAdapter sa = new SimpleAdapter(PacksActivity.this, list,
											 R.layout.list, new String[] { "img", "text" },
											 new int[] { R.id.listImageView1, R.id.listTextView1});

		listview1.setAdapter(sa);
	}
	private void creatfile() {
		String dir = "/sdcard/games/com.koishi.launcher/packs"; // 需要创建的目录，sdcard目录一定存在，所以只用判断一级目录
		File file = new File(dir);
		if (!file.exists())// 判断当前目录是否存在，存在返回true,否则返回false
			file.mkdir(); // 如果不存在则创建目录
		return;
	}

	private int over(String text) {
		/*
		 if (text.indexOf(".sh") != -1) {
		 ings = R.drawable.viihgv;
		 } else if (text.indexOf(".tar") != -1) {
		 ings = R.drawable.wen;
		 } else if (text.indexOf(".gz") != -1) {
		 ings = R.drawable.wen;
		 } else if (text.indexOf(".png") != -1) {
		 ings = R.drawable.vpver;
		 } else if (text.indexOf(".mp3") != -1) {
		 ings = R.drawable.vmnvff;
		 } else if (text.indexOf(".log") != -1) {
		 ings = R.drawable.vrty;
		 } else if (text.indexOf(".obj") != -1) {
		 ings = R.drawable.wen;
		 } else if (text.indexOf(".mtl") != -1) {
		 ings = R.drawable.wen;
		 } else if (text.indexOf(".json") != -1) {
		 ings = R.drawable.vqw;
		 } else if (text.indexOf(".re") != -1) {
		 ings = R.drawable.wen;
		 } else if (text.indexOf(".bat") != -1) {
		 ings = R.drawable.vqw;
		 } else if (text.indexOf(".apk") != -1) {
		 ings = R.drawable.viihgv;
		 } else if (text.indexOf(".rc") != -1) {
		 ings = R.drawable.viihgv;
		 } else if (text.indexOf(".zip") != -1) { 
		 ings = R.drawable.wen;
		 } else if (text.indexOf(".xml") != -1) {
		 ings = R.drawable.vrty;
		 } else if (text.indexOf(".so") != -1) {
		 ings = R.drawable.viihgv;
		 } else if (text.indexOf(".html") != -1) {
		 ings = R.drawable.vpouy;
		 } else if (text.indexOf(".txt") != -1) {
		 ings = R.drawable.vqw;
		 } else if (text.indexOf(".js") != -1) {
		 ings = R.drawable.vqw;
		 } else if (text.indexOf(".jar") != -1) {
		 ings = R.drawable.file;
		 } else if (text.indexOf(".cfg") != -1) {
		 ings = R.drawable.vnfdr;
		 } else if (text.indexOf(".ttf") != -1) {
		 ings = R.drawable.verrt;
		 } else if (text.indexOf(">/+-*|") != -1) {
		 ings = R.drawable.verrt;
		 } else if (text.indexOf("Empty") != -1) {
		 ings = R.drawable.timg;
		 } else {
		 ings = R.drawable.verrt;
		 }
		 */
		return ings;
	}
	public void showDirectory(View view) {
		AlertDialog alertDialog2 = new AlertDialog.Builder(listview1.getContext())
			.setTitle(getResources().getString(R.string.menu_5_adtt))//标题
			.setIcon(R.drawable.ic_boat)//图标
			.setMessage(Path)
			.setPositiveButton("Ok", new DialogInterface.OnClickListener() {//添加"Yes"按钮
				@Override
				public void onClick(DialogInterface dialogInterface, int i) {
					//TODO
				}
			})
			.create();
		alertDialog2.show();

	}

	Handler han=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
			if (msg.what == 0) {
				creatfile();
				Filelistview(Path + "/");
				pd.hide();
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.menu_5_tt), Toast.LENGTH_SHORT).show();
			}
        }};
	Handler han2=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
			if (msg.what == 0) {
				boolean tmp = fileIsExists("/sdcard/games/com.koishi.launcher/packs/" + packName.getText().toString() + "/boat/gamedir");
				if (tmp) {
					Filelistview(Path + "/");
					pd.hide();
					Toast.makeText(getApplicationContext(), getResources().getString(R.string.menu_5_ttd), Toast.LENGTH_SHORT).show();
				} else {
					new Thread(new Runnable(){
							@Override
							public void run() {
								File file2=new File("/sdcard/games/com.koishi.launcher/packs/" + packName.getText().toString());
								deleteDirWihtFile(file2);	
								han3.sendEmptyMessage(0);  
							}
						}).start();
				}
			}
        }};
	Handler han3=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
			if (msg.what == 0) {
				Filelistview(Path + "/");
				pd.hide();
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.menu_5_ttdn), Toast.LENGTH_SHORT).show();
			}
        }};
    public void onClick(View v) {
        if (v == btnSet) {
			new LFilePicker().withActivity(this)
				.withRequestCode(Consant.REQUESTCODE_FROM_FRAGMENT)
				.withMutilyMode(false)
				.withFileFilter(new String[]{"zip"})
				.withTheme(R.style.LFileTheme)
				.withTitle("Open From Packs")
				.start();

        }
        if (v == btnCancel) {
			ll5.setVisibility(View.VISIBLE);
			packName.setVisibility(View.GONE);
			btnSet.setVisibility(View.GONE);
			btnNew.setVisibility(View.GONE);
			btnCancel.setVisibility(View.GONE);
        }
		if (v == btnNew) {
			try {
				output(PacksActivity.this, "boat.zip",  Environment.getExternalStorageDirectory() + "/games/com.koishi.launcher/packs/" + packName.getText().toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
			ll5.setVisibility(View.VISIBLE);
			packName.setVisibility(View.GONE);
			btnSet.setVisibility(View.GONE);
			btnNew.setVisibility(View.GONE);
			btnCancel.setVisibility(View.GONE);
			Filelistview(Path + "/");
		}

    }
	/*
	 class MyAdapter extends BaseAdapter {
	 private Context mContext;
	 private ArrayList<String> mList;
	 private LayoutInflater mInflater;

	 public MyAdapter(Context mContext, ArrayList<String> mList) {
	 super();
	 this.mContext = mContext;
	 this.mList = mList;
	 mInflater = LayoutInflater.from(mContext);
	 }

	 @Override
	 public int getCount() {
	 // TODO Auto-generated method stub
	 return mlist.size();
	 }

	 @Override
	 public Object getItem(int position) {
	 // TODO Auto-generated method stub
	 return position;
	 }

	 @Override
	 public long getItemId(int position) {
	 return position;
	 }


	 @Override
	 public int getViewTypeCount() {
	 return 2;
	 }


	 @Override
	 public View getView(final int position, View convertView, ViewGroup parent) {
	 //			System.out.println("position-->"+position+"  ---convertView--"+convertView);
	 convertView = mInflater.inflate(R.layout.list, parent, false);
	 btnDel=convertView.findViewById(R.id.btnDel);
	 btnDel.setOnClickListener(new OnClickListener(){
	 @Override
	 public void onClick(View p1) {
	 File file2=new File("/sdcard/games/com.koishi.launcher/packs"+mList.get(position));
	 deleteDirWihtFile(file2);
	 Filelistview(Path+"/");
	 }
	 });
	 return convertView;
	 }

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
	 */

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

	private void tv(String dir) {
        try {
            FileInputStream in=new FileInputStream("/sdcard/games/com.koishi.launcher/h2o2/config.txt");
            byte[] b=new byte[in.available()];
            in.read(b);
            in.close();
            String str=new String(b);
            JSONObject json=new JSONObject(str);
            json.remove("game_directory");
			json.remove("game_assets");
			json.remove("assets_root");
			json.remove("currentVersion");
            json.put("game_directory", dir);
			json.put("game_assets", dir + "/assets/virtual/legacy");
			json.put("assets_root", dir + "/assets");
			json.put("currentVersion", dir + "/versions");
            FileWriter fr=new FileWriter(new File("/sdcard/games/com.koishi.launcher/h2o2/config.txt"));
            fr.write(json.toString());
            fr.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private String t() {
        try {
            FileInputStream in=new FileInputStream("/sdcard/games/com.koishi.launcher/h2o2/config.txt");
            byte[] b=new byte[in.available()];
            in.read(b);
            in.close();
            String str=new String(b);
            JSONObject json=new JSONObject(str);
            return json.getString("game_directory");
        } catch (Exception e) {
            System.out.println(e);
        }
        return "None";
    }

	public void setDefault(View view) {
	boolean tmp = fileIsExists("/storage/emulated/0/games/com.koishi.launcher/h2o2/config.txt");
	boolean tmp2 = fileIsExists("/storage/emulated/0/games/com.koishi.launcher/h2o2/gamedir");
	if (!tmp){
	    try {
			output(PacksActivity.this, "h2o2.zip",  Environment.getExternalStorageDirectory() + "/games/com.koishi.launcher/h2o2/");
		} catch (IOException e) {
			
		}
	}
	if (!tmp2){
	    try {
			output(PacksActivity.this, "h2o2.zip",  Environment.getExternalStorageDirectory() + "/games/com.koishi.launcher/h2o2/");
		} catch (IOException e) {
			
		}
	}
		tv.setText("/storage/emulated/0/games/com.koishi.launcher/h2o2/gamedir");
		Toast.makeText(getApplicationContext(), getResources().getString(R.string.menu_5_ttt), Toast.LENGTH_SHORT).show();
	}
	
//   Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Consant.REQUESTCODE_FROM_FRAGMENT) {
                List<String> list = data.getStringArrayListExtra("paths");
                for (final String s : list) {
					pd.show();
					new Thread(new Runnable(){
							@Override
							public void run() {
								try {
									unzip(s, "/sdcard/games/com.koishi.launcher/packs/" + packName.getText().toString());
									han2.sendEmptyMessage(0);
								} catch (IOException e) {
								}

							}
						}).start();
				}
			}
		}
    }


	@Override
	public void onResume() {
	    super.onResume();
		creatfile();
	    Filelistview(Path + "/");
	    ll5.setVisibility(View.VISIBLE);
		packName.setVisibility(View.GONE);
		btnSet.setVisibility(View.GONE);
		btnNew.setVisibility(View.GONE);
		btnCancel.setVisibility(View.GONE);
	}

	public static void unzip(String zipFilePath, String targetPath)
	throws IOException {
		OutputStream os = null;
		InputStream is = null;
		ZipFile zipFile = null;
		try {
			//获取要解压的文件，指定解压格式
			zipFile = new ZipFile(zipFilePath, Charset.forName("GBK"));
			Enumeration<?> entryEnum = zipFile.entries();
			if (null != entryEnum) {
				ZipEntry zipEntry = null;
				while (entryEnum.hasMoreElements()) {
					zipEntry = (ZipEntry) entryEnum.nextElement();
					if (zipEntry.getSize() > 0) {
						File targetFile = new File(targetPath
												   + File.separator + zipEntry.getName());
						os = new BufferedOutputStream(new FileOutputStream(targetFile));
						is = zipFile.getInputStream(zipEntry);
						byte[] buffer = new byte[4096];
						int readLen = 0;
						while ((readLen = is.read(buffer, 0, 4096)) >= 0) {
							os.write(buffer, 0, readLen);
							os.flush();
						}
						is.close();
						os.close();
					}
					//如果是文件夹，则创建文件夹
					if (zipEntry.isDirectory()) {
						String pathTemp = targetPath + File.separator
                            + zipEntry.getName();
						File file = new File(pathTemp);
						file.mkdirs();
					}
				}
			}
		} catch (IOException ex) {
			throw ex;
		} finally {
			//关闭流
			if (null != zipFile) {
				zipFile.close();
				zipFile = null;
			}
			if (null != is) {
				is.close();
			}
			if (null != os) {
				os.close();
			}
		}
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
	public boolean deleteFile(String filePath) {
		File file = new File(filePath);
        if (file.isFile() && file.exists()) {
			return file.delete();
        }
        return false;
    }

    /**
     * 删除文件夹以及目录下的文件
     * @param   filePath 被删除目录的文件路径
     * @return  目录删除成功返回true，否则返回false
     */
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
}

