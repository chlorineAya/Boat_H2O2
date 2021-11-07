package com.download.service.util;

public class AssetsUtil
{
	private String name;
	private String hash;
	private int size;
	private boolean download;
	public AssetsUtil(){
	}
	
	public String gethash(){

		return hash;
	}
	
	
	
	public int getsize(){

		return size;
	}
	public String getname(){

		return name;
	}
	public boolean get(){

		return download;
	}

	public void set(boolean a){
		download=a;
	}
	public void setname(String a){
		name=a;
	}
	public void sethash(String a){
		hash=a;
	}
	public void setsize(int a){
		size=a;
	}
	
	
	
	
	
}
