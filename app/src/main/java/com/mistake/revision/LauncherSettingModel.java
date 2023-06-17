package com.mistake.revision;

public class LauncherSettingModel {
    
    public LauncherSettingModel(){
        //默认模板初始化
        super();
		
        downloadType = "official";

        /*
        MinecraftParameter = new MinecraftParameter();
        MinecraftParameter.auth_uuid = "00000000-0000-0000-0000-000000000000";
        MinecraftParameter.extraMinecraftFlags = "";
        MinecraftParameter.auth_player_name = "Steve";
        MinecraftParameter.auth_session = "0";
        MinecraftParameter.extraJavaFlags = "-server -Xms850M -Xmx850M";
        MinecraftParameter.home = "/storage/emulated/0/games/com.koishi.launcher/h2o2";
        MinecraftParameter.runtimePath = "/data/user/0/cosine.boat/app_runtime/";
        MinecraftParameter.auth_access_token = "0";
        MinecraftParameter.currentVersion = "/storage/emulated/0/boat/gamedir/versions/";
        MinecraftParameter.game_assets = "/storage/emulated/0/boat/gamedir/assets/virtual/legacy";
        MinecraftParameter.user_type = "mojang";
        MinecraftParameter.game_directory = "/storage/emulated/0/boat/gamedir";
		MinecraftParameter.user_properties = "{}";
		MinecraftParameter.assets_root = "/storage/emulated/0/boat/gamedir/assets";
         */
		
    }

    private  String downloadType; //下载源："office"官方 "bmclapi"国内BMCLAPI "mcbbs"国内MCBBS
    private MinecraftParameter MinecraftParameter; //Config配置
    
    //Config配置
    public class MinecraftParameter {

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

        //Getter and Setter
		public String getauth_uuid() { return auth_uuid; }
		public void setauth_uuid(String auth_uuid) { this.auth_uuid = auth_uuid; } 
		public String getextraMinecraftFlags() { return extraMinecraftFlags; }
		public void setextraMinecraftFlags(String extraMinecraftFlags) { this.extraMinecraftFlags = extraMinecraftFlags; } 
		public String getauth_player_name() { return auth_player_name; }
		public void setauth_player_name(String auth_player_name) { this.auth_player_name = auth_player_name; } 
		public String getauth_session() { return auth_session; }
		public void setauth_session(String auth_session) { this.auth_session = auth_session; } 
		public String getextraJavaFlags() { return extraJavaFlags; }
		public void setextraJavaFlags(String extraJavaFlags) { this.extraJavaFlags = extraJavaFlags; } 
		public String gethome() { return home; }
		public void sethome(String home) { this.home = home; } 
		public String getruntimePath() { return runtimePath; }
		public void setruntimePath(String runtimePath) { this.runtimePath = runtimePath; } 
		public String getauth_access_token() { return auth_access_token; }
		public void setauth_access_token(String auth_access_token) { this.auth_access_token = auth_access_token; } 
		public String getcurrentVersion() { return currentVersion; }
		public void setcurrentVersion(String currentVersion) { this.currentVersion = currentVersion; } 
		public String getgame_assets() { return game_assets; }
		public void setgame_assets(String game_assets) { this.game_assets = game_assets; } 
		public String getuser_type() { return user_type; }
		public void getuser_type(String user_type) { this.user_type = user_type; } 
		public String getgame_directory() { return game_directory; }
		public void setgame_directory(String game_directory) { this.game_directory = game_directory; } 
		public String getuser_properties() { return user_properties; }
		public void setuser_properties(String user_properties) { this.user_properties = user_properties; } 
		public String getassets_root() { return assets_root; }
		public void setassets_root(String assets_root) { this.assets_root = assets_root; } 
    }

    //Getter and Setter
    public String getDownloadType() { return downloadType; }
    public void setDownloadType(String downloadType) { this.downloadType = downloadType; }
	
    public MinecraftParameter getMinecraftParameter() { return MinecraftParameter; }
    public void setMinecraftParameter(MinecraftParameter MinecraftParameter) { this.MinecraftParameter = MinecraftParameter; }
	
}
