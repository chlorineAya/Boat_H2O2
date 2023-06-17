package com.download.service.downloader;

/**
 * 下载监听
 *
 * @author Cheny
 */
public interface DownloadListner
{

	
    void onFinished();
    void onProgress(float progress,long i ,long s);
    void onPause();
    void onCancel();
}

