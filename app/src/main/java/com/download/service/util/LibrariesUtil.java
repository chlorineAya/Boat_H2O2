package com.download.service.util;

public class LibrariesUtil
{
	private String name;
	private String url;
	private String path;
	private int size;
	private boolean download;
	public LibrariesUtil(){
	
	}
	
	
	public String getname(){
		
		return name;
	}
	public String geturl(){

		return url;
	}
	public String getpath(){

		return path;
	}
	public int getsize(){
		
		return size;
	}
	public boolean get(){
		return download;
	}
	public void setname(String a){
		name=a;
	}
	public void seturl(String a){
		url=a;
	}
	public void setpath(String a){
		path=a;
	}
	public void setsize(int a){
		size=a;
	}
	public void set(boolean a){
		download=a;
	}
	
	
	
}
