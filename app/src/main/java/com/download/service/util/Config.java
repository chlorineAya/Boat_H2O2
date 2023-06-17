package com.download.service.util;

public class Config
{
	public Config(){
		
	}

	private String auth_uuid;
	private String extraMinecraftFlags;
	private String auth_player_name;
	private String auth_session;
	private String extraJavaFlags;
	private String home;
	private String runtimePath;
	private String auth_access_token;
	private String currentVersion;
	private String game_assets;
	private String user_type;
	private String game_directory;
	private String user_properties;
	private String assets_root;
	
	public void setauth_uuid(String a){
		auth_uuid=a;
	};
	public void setextraMinecraftFlags(String a){
		extraMinecraftFlags=a;
	};
	public void setauth_player_name(String a){
		auth_player_name=a;
	};
	public void setauth_session(String a){
		auth_session=a;
	};
	public void setextraJavaFlags(String a){
		extraJavaFlags=a;
	};
	public void sethome(String a){
		home=a;
	};
	public void setruntimePath(String a){
		runtimePath=a;
	};
	public void setauth_access_token(String a){
		auth_access_token=a;
	};
	public void setcurrentVersion(String a){
		currentVersion=a;
	};
	public void setgame_assets(String a){
		game_assets=a;
	};
	public void setuser_type(String a){
		user_type=a;
	};
	public void setgame_directory(String a){
		game_directory=a;
	};
	public void setuser_properties(String a){
		user_properties=a;
	};
	public void setassets_root(String a){
		assets_root=a;
	};
	
	public String setauth_uuid()
	{
		return auth_uuid;
	};
	public String setextraMinecraftFlags()
	{return extraMinecraftFlags;
	};
	public String setauth_player_name()
	{return auth_player_name;
	};
	public String setauth_session()
	{return auth_session;
	};
	public String setextraJavaFlags()
	{return extraJavaFlags;
	};
	public String sethome()
	{return home;
	};
	public String setruntimePath()
	{return runtimePath;
	};
	public String setauth_access_token()
	{return auth_access_token;
	};
	public String setcurrentVersion()
	{return currentVersion;
	};
	public String setgame_assets()
	{return game_assets;
	};
	public String setuser_type()
	{return user_type;
	};
	public String setgame_directory()
	{return game_directory;
	};
	public String setuser_properties()
	{return user_properties;
	};
	public String setassets_root()
	{return assets_root;
	};
	
}


/*
"auth_uuid":"00000000-0000-0000-0000-000000000000"
"extraMinecraftFlags":""
"auth_player_name":"Fire"
"auth_session":"0"
"extraJavaFlags":"-server -Xms500M -Xmx500M"
"home":"/storage/emulated/0/boat"
"runtimePath":"/data/user/0/cosine.boat/app_runtime"
"auth_access_token":"0"
"currentVersion":"/storage/emulated/0/boat/gamedir/versions/1.7.10"
"game_assets":"/storage/emulated/0/boat/gamedir/assets/virtual/legacy"
"user_type":"mojang"
"game_directory":"/storage/emulated/0/boat/gamedir"
"user_properties":"{}"
"assets_root":"/storage/emulated/0/boat/gamedir/assets"



*/
