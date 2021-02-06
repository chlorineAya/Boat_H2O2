package com.koishi.launcher.h2o2.func;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.koishi.launcher.h2o2.R;
import org.json.JSONObject;
import java.io.FileInputStream;
import android.app.ProgressDialog;
import com.leon.lfilepickerlibrary.LFilePicker;
import android.os.Handler;
import android.os.Message;
import java.io.File;
import android.widget.Toast;
import com.koishi.launcher.h2o2.tools.Consant;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.util.zip.ZipFile;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.io.FileOutputStream;
import java.io.BufferedOutputStream;
import android.content.Intent;
import android.app.Activity;
import java.util.List;
import android.text.TextUtils;

public class DownloadActivity extends AppCompatActivity {
    
    String a;
	private ProgressDialog pd;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
		
		Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar0);
		setSupportActionBar(toolbar);
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
				}
			});
			
		pd = new ProgressDialog(DownloadActivity.this);
		 pd.setMessage("Waiting...");
		 pd.setIndeterminate(true);
		 pd.setCancelable(false);
        a = t();
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
	
    public void imp (View view){
        new LFilePicker().withActivity(this)
			.withRequestCode(Consant.REQUESTCODE_FROM_FRAGMENT)
			.withMutilyMode(false)
			.withFileFilter(new String[]{"zip"})
			.withTheme(R.style.LFileTheme)
			.withTitle("Open From Packs")
			.start();
		
    
    }
    public void gomana(View view){
    
    }
    
    Handler han2=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
			if (msg.what == 0) {
				boolean tmp = fileIsExists("/sdcard/games/com.koishi.launcher/tmp/boat/gamedir");
				if (tmp) {
				renameFile("/sdcard/games/com.koishi.launcher/tmp/boat","/sdcard/games/com.koishi.launcher/tmp/h2o2");
				String pathname = "/sdcard/games/com.koishi.launcher/tmp/h2o2"; 
					final File file = new File(pathname); 
					//复制到的位置 
					String topathname =  "/sdcard/games/com.koishi.launcher";
					final File toFile = new File(topathname); 
					new Thread(new Runnable(){
							@Override
							public void run() {
								try { 
									copy(file, toFile); 
								} catch (Exception e) { 
									// TODO Auto-generated catch block 
									e.printStackTrace(); 
								} 
								han4.sendEmptyMessage(0);
							}

						}).start();
				} else {
					new Thread(new Runnable(){
							@Override
							public void run() {
								File file2=new File("/sdcard/games/com.koishi.launcher/tmp");
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
				pd.hide();
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.menu_5_ttdn), Toast.LENGTH_SHORT).show();
			}
        }};
       
    Handler han4=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
			if (msg.what == 0) {
				pd.hide();
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.menu_5_ttd), Toast.LENGTH_SHORT).show();
			}
        }};
    
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
									unzip(s, "/sdcard/games/com.koishi.launcher/tmp");
									han2.sendEmptyMessage(0);
								} catch (IOException e) {
								}

							}
						}).start();
				}
			}
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
	
	public static void copy(File file, File toFile) throws Exception { 
		byte[] b = new byte[1024]; 
		int a; 
		FileInputStream fis; 
		FileOutputStream fos; 
		if (file.isDirectory()) { 
			String filepath = file.getAbsolutePath(); 
			filepath = filepath.replaceAll("\\\\", "/"); 
			String toFilepath = toFile.getAbsolutePath(); 
			toFilepath = toFilepath.replaceAll("\\\\", "/"); 
			int lastIndexOf = filepath.lastIndexOf("/"); 
			toFilepath = toFilepath + filepath.substring(lastIndexOf , filepath.length()); 
			File copy=new File(toFilepath); 
			//复制文件夹 
			if (!copy.exists()) { 
				copy.mkdir(); 
			} 
			//遍历文件夹 
			for (File f : file.listFiles()) { 
				copy(f, copy); 
			} 
		} else { 
			if (toFile.isDirectory()) { 
				String filepath = file.getAbsolutePath(); 
				filepath = filepath.replaceAll("\\\\", "/"); 
				String toFilepath = toFile.getAbsolutePath(); 
				toFilepath = toFilepath.replaceAll("\\\\", "/"); 
				int lastIndexOf = filepath.lastIndexOf("/"); 
				toFilepath = toFilepath + filepath.substring(lastIndexOf , filepath.length()); 

				//写文件 
				File newFile = new File(toFilepath); 
				fis = new FileInputStream(file); 
				fos = new FileOutputStream(newFile); 
				while ((a = fis.read(b)) != -1) { 
					fos.write(b, 0, a); 
				} 
			} else { 
				//写文件 
				fis = new FileInputStream(file); 
				fos = new FileOutputStream(toFile); 
				while ((a = fis.read(b)) != -1) { 
					fos.write(b, 0, a); 
				} 
			} 

		} 
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
    private File renameFile(String oldPath, String newPath) {
        if (TextUtils.isEmpty(oldPath)) {
            return null;
        }

        if (TextUtils.isEmpty(newPath)) {
            return null;
        }
        File oldFile = new File(oldPath);
        File newFile = new File(newPath);
        boolean b = oldFile.renameTo(newFile);
        File file2 = new File(newPath);
        return file2;
    }
	
}
