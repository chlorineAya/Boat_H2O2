package com.download.service.downloader;

/**
 * Created by Cheny on 2017/4/29.
 */
public class FilePoint {
    private String fileName;//文件名
    private String url;//下载地址
    private String filePath;

	private int a ;

	private Object object;

	private int size;//下载目录
	
    public FilePoint(String url) {
        this.url = url;
    }
    public FilePoint(String filePath, String url) {
        this.filePath = filePath;
        this.url = url;
    }

    public FilePoint(String url, String filePath, String fileName) {
        this.url = url;
        this.filePath = filePath;
        this.fileName = fileName;
    }
	public FilePoint(String url, String filePath, String fileName, int a,Object object,int size) {
        this.url = url;
        this.filePath = filePath;
        this.fileName = fileName;
		this.a=a;
		this.object=object;
		this.size=size;
    }
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFilePath() {
        return filePath;
    }
	
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
	public int getA() {
        return a;
    }
	public Object getObject() {
        return object;
    }
	public int getSize() {
        return size;
    }
	
}
