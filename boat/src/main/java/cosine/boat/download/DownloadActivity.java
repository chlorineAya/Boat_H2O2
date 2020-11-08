package cosine.boat.download;

import java.io.File;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.util.Log;
import android.view.Menu;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import cosine.boat.R;
import android.widget.Button;
import android.view.View;
import java.io.IOException;
import android.content.Intent;

public class DownloadActivity extends Activity {

	public Button downloaddir;
	public Button downloadrun;
	public Button delf;

	public static final String ROOT_DIR = "/mnt/sdcard/mythroad";
	private final String TAG="DownloadActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_down);

		Log.d(TAG, "Environment.getExternalStorageDirectory()=" + Environment.getExternalStorageDirectory());
		Log.d(TAG, "getCacheDir().getAbsolutePath()=" + getCacheDir().getAbsolutePath());

		this.downloaddir = (Button)findViewById(R.id.downloaddir);
		downloaddir.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					showDownLoadDialog();

				}
			});
		this.downloadrun = (Button)findViewById(R.id.downloadrun);
		downloadrun.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					showDownLoadDialog2();

				}
			});
		this.delf = (Button)findViewById(R.id.delf);
		delf.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					delete();

				}
			});
		if (new File("/sdcard/mythroad/download").exists()) {

			downloaddir.setEnabled(false);
			downloadrun.setEnabled(false);

        }

		mopo();
	}

	/**
     * 检测储存卡是否安装
     */
    private boolean mopo() {
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			file();
			return true;
		} else {
			new AlertDialog.Builder(DownloadActivity.this).setTitle("Warning:")
				.setMessage("No permission to r/w Sdcard! ").setIcon(R.drawable.ic_launcher)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						finish();
					}

				}).show();
		}

		return false;
	}

    private void delete() {
		Toast.makeText(DownloadActivity.this, "Waiting", 1000).show();
		new Thread(new Runnable(){
				@Override
				public void run() {
					FileUtil.delDir("/sdcard/mythroad/");
					han3.sendEmptyMessage(0);
				}
			}).start();
    }
    /**
     * 创建文件夹
     */
	private void file() {
		File destDir = new File(ROOT_DIR);
		if (!destDir.exists()) {
			destDir.mkdirs();
		}
	}

	private void showDownLoadDialog() {
		new AlertDialog.Builder(this).setTitle("Download")
			.setMessage("Are you sure？")
			.setPositiveButton("Yes", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Log.d(TAG, "onClick 1 = " + which);
					doDownLoadWork();
				}
			})
			.setNegativeButton("No", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Log.d(TAG, "onClick 2 = " + which);
				}
			})
			.show();
	}

	private void showDownLoadDialog2() {
		new AlertDialog.Builder(this).setTitle("Download")
			.setMessage("Are you sure？")
			.setPositiveButton("Yes", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Log.d(TAG, "onClick 1 = " + which);
					doDownLoadWork2();
				}
			})
			.setNegativeButton("No", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Log.d(TAG, "onClick 2 = " + which);
				}
			})
			.show();
	}



	public void unzipdir() {
		Toast.makeText(DownloadActivity.this, "Installing", 1000).show();
		new Thread(new Runnable(){
				@Override
				public void run() {
					FileUtil.unZip(new File("/sdcard/mythroad").listFiles()[0].getAbsolutePath().toString(), "/sdcard/mythroad");
					FileUtil.dirCopy("/sdcard/mythroad/boat", "/sdcard/boat");
					FileUtil.delDir("/sdcard/mythroad/");
					han.sendEmptyMessage(0);
				}
			}).start();
	}
	public void unziprun() {
		Toast.makeText(DownloadActivity.this, "Installing", 1000).show();
		new Thread(new Runnable(){
				@Override
				public void run() {
				    
					FileUtil.unZip(new File("/sdcard/mythroad").listFiles()[0].getAbsolutePath().toString(), "/sdcard/mythroad");
					FileUtil.dirCopy("/sdcard/mythroad/app_runtime", "/data/data/cosine.boat/app_runtime");
					FileUtil.delDir("/sdcard/mythroad/");
					han2.sendEmptyMessage(0);
				}
			}).start();
	}

	Handler han=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Toast.makeText(DownloadActivity.this, "Done", 1000).show();

        }};
    Handler han2=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Toast.makeText(DownloadActivity.this, "Done", 1000).show();

        }};
	Handler han3=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Toast.makeText(DownloadActivity.this, "Deleted", 1000).show();
            downloaddir.setEnabled(true);
            downloadrun.setEnabled(true);
            Intent i = new Intent(DownloadActivity.this, DownloadActivity.class);
			startActivity(i);// TODO: Implement this method
        }};
        
    

	private void doDownLoadWork() {
		DownLoaderTask task = new DownLoaderTask("https://cn1.cube64128.xyz:666/nextcloud/s/XbeSGS2kcq2R9Mc/download", "/mnt/sdcard/mythroad", this);
		task.execute();
	}
	private void doDownLoadWork2() {
		DownLoaderTask2 task2 = new DownLoaderTask2("https://cn1.cube64128.xyz:666/nextcloud/s/cioM4wWL6W2eS8Z/download", "/mnt/sdcard/mythroad/", this);
		task2.execute();
	}

}
