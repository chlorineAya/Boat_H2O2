package com.download.service.downloader;

import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.*;
import java.util.Map.*;
/**
 * 下载管理器，断点续传
 *
 * @author Cheny
 
 */
public class DownloadManager {

    
	private DownloadTask mDownloadTasks1;
	private DownloadTask mDownloadTasks2;
	private DownloadTask mDownloadTasks3;
	
	
    
    private static final String TAG = "DownloadManager";
	private FilePoint mfilepoint1;
	private FilePoint mfilepoint2;
	private FilePoint mfilepoint3;

	private static DownloadManager mInstance;
	
	
    public static DownloadManager getInstance() {//管理器初始化
        if (mInstance == null) {
            synchronized (DownloadManager.class) {
                if (mInstance == null) {
                    mInstance = new DownloadManager();
                }
            }
        }
        return mInstance;
    }
	

    public DownloadManager() {

    }
    public boolean get1(){
		return mDownloadTasks1.isDownloading();
	}
	public boolean get2(){
		return mDownloadTasks1.isDownloading();
	}
	public boolean get3(){
		return mDownloadTasks1.isDownloading();
	}
	public boolean download1_stop_start(){
		if(mDownloadTasks1!=null){
			if (!mDownloadTasks1.isDownloading()) {
				mDownloadTasks1.start();
				return true;
			} else {

				mDownloadTasks1.pause();
				return false;
			}
		}else{
			return false;
		}
		
	}
	public boolean download2_stop_start(){
		if(mDownloadTasks2!=null){
			if (!mDownloadTasks2.isDownloading()) {
				mDownloadTasks2.start();
				return true;
			} else {

				mDownloadTasks2.pause();
				return false;
			}
		}else{
			return false;
		}
		
	}
	public boolean download3_stop_start(){
		if(mDownloadTasks3!=null){
			if (!mDownloadTasks3.isDownloading()) {
				mDownloadTasks3.start();
				return true;
			} else {

				mDownloadTasks3.pause();
				return false;
			}
		}else{
			return false;
		}
		
	}
   

   
    public String getFileName(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    //mDownloadTasks.pause();
            
        
    

    public void add1(String url, String filePath, String fileName,int a,Object object,int size, DownloadListner l) {
        
        if (TextUtils.isEmpty(fileName)) {
            fileName = getFileName(url);
        }
			mfilepoint1=new FilePoint(url, filePath, fileName,a,object,size);
			mDownloadTasks1= new DownloadTask(mfilepoint1, l);
		
	}
	public void add2(String url, String filePath, String fileName,int a,Object object,int size, DownloadListner l) {

        if (TextUtils.isEmpty(fileName)) {
            fileName = getFileName(url);
        }
		mfilepoint2=new FilePoint(url, filePath, fileName,a,object,size);
		mDownloadTasks2= new DownloadTask(mfilepoint2, l);

	}
	public void add3(String url, String filePath, String fileName,int a,Object object,int size, DownloadListner l) {

        if (TextUtils.isEmpty(fileName)) {
            fileName = getFileName(url);
        }
		mfilepoint3=new FilePoint(url, filePath, fileName,a,object,size);
		mDownloadTasks3= new DownloadTask(mfilepoint3, l);

	}
    

        

	public FilePoint Self_destruction1() {
		
		if(mDownloadTasks1!=null){
			mDownloadTasks1.cancel();
			return mfilepoint1;
		}else{
			
			return null;
		}
		/*for (Map.Entry<String, DownloadTask> entry : mDownloadTasks.entrySet()) {    
			//System.out.println("key= " +  + " and value= " + );    
			String a=entry.getKey();
			DownloadTask b = entry.getValue();
			b.cancel();
		}*/
		
	}
	public FilePoint Self_destruction2() {

		if(mDownloadTasks2!=null){
			mDownloadTasks2.cancel();
			return mfilepoint2;
		}else{

			return null;
		}
		/*for (Map.Entry<String, DownloadTask> entry : mDownloadTasks.entrySet()) {    
		 //System.out.println("key= " +  + " and value= " + );    
		 String a=entry.getKey();
		 DownloadTask b = entry.getValue();
		 b.cancel();
		 }*/

	}
	public FilePoint Self_destruction3() {

		if(mDownloadTasks3!=null){
			mDownloadTasks3.cancel();
			return mfilepoint3;
		}else{

			return null;
		}
		/*for (Map.Entry<String, DownloadTask> entry : mDownloadTasks.entrySet()) {    
		 //System.out.println("key= " +  + " and value= " + );    
		 String a=entry.getKey();
		 DownloadTask b = entry.getValue();
		 b.cancel();
		 }*/

	}
	
	
	
}
