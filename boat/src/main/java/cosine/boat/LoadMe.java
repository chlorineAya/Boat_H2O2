package cosine.boat;

import dalvik.system.*;
import java.io.*;
import java.lang.reflect.*;
import java.util.*;

public class LoadMe {

    public static native int chdir(String str);
    public static native int jliLaunch(String[] strArr);
    public static native void redirectStdio(String file);
    public static native void setenv(String str, String str2);
    public static native void setupJLI();
    public static native int dlopen(String name);
	public static native void setLibraryPath(String path);
	public static native void patchLinker();

    static {
        System.loadLibrary("boat");
    }

    public static int exec(LauncherConfig config) {
        try {

            MinecraftVersion mcVersion = MinecraftVersion.fromDirectory(new File(config.get("currentVersion")));
            String runtimePath = config.get("runtimePath");

			String arch = "aarch64";
			String vm_variant = "server";
			
			String token =config.get("auth_access_token");

            //String libraryPath = runtimePath + "/j2re-image/lib/" + arch + "/jli:" + runtimePath + "/j2re-image/lib/" + arch + ":" + runtimePath + "/lwjgl-3:" + runtimePath;

            //setLibraryPath(libraryPath);
			patchLinker();

            String home = config.get("home");

            setenv("HOME", home);
            setenv("JAVA_HOME" , runtimePath + "/j2re-image");
			setenv("LIBGL_MIPMAP", "3");
			setenv("LIBGL_NORMALIZE", "1");

            // openjdk
            dlopen(runtimePath + "/j2re-image/lib/" + arch + "/libfreetype.so");
            dlopen(runtimePath + "/j2re-image/lib/" + arch + "/libpng16.so.16");
            dlopen(runtimePath + "/j2re-image/lib/" + arch + "/libfontmanager.so");
            dlopen(runtimePath + "/j2re-image/lib/" + arch + "/libpng16.so");
            dlopen(runtimePath + "/j2re-image/lib/" + arch + "/jli/libjli.so");
            dlopen(runtimePath + "/j2re-image/lib/" + arch + "/" + vm_variant + "/libjvm.so");
            dlopen(runtimePath + "/j2re-image/lib/" + arch + "/libverify.so");
            dlopen(runtimePath + "/j2re-image/lib/" + arch + "/libjava.so");
            dlopen(runtimePath + "/j2re-image/lib/" + arch + "/libnet.so");
            dlopen(runtimePath + "/j2re-image/lib/" + arch + "/libnio.so");
            dlopen(runtimePath + "/j2re-image/lib/" + arch + "/libawt.so");
            dlopen(runtimePath + "/j2re-image/lib/" + arch + "/libawt_headless.so");

			boolean isLwjgl3=false;
            if (mcVersion.minimumLauncherVersion >= 21) {
                isLwjgl3 = true;
            }

			String libraryPath;
			String classPath;

            // others
            dlopen(runtimePath + "/libopenal.so.1");
            dlopen(runtimePath + "/libGL.so.1");

			if (!isLwjgl3) {
				libraryPath = runtimePath + "/j2re-image/lib/" + arch + "/jli:" + runtimePath + "/j2re-image/lib/" + arch + ":" + runtimePath + "/lwjgl-2:" + runtimePath;
				classPath = config.get("runtimePath") + "/lwjgl-2/lwjgl.jar:" + config.get("runtimePath") + "/lwjgl-2/lwjgl_util.jar:" + mcVersion.getClassPath(config);
				dlopen(runtimePath + "/lwjgl-2/liblwjgl64.so");
				setLibraryPath(libraryPath);
			} else {
				libraryPath = runtimePath + "/j2re-image/lib/aarch64/jli:" + runtimePath + "/j2re-image/lib/aarch64:" + runtimePath + "/lwjgl-3:" + runtimePath;
				classPath = runtimePath + "/lwjgl-3/lwjgl-jemalloc.jar:" + runtimePath + "/lwjgl-3/lwjgl-tinyfd.jar:" + runtimePath + "/lwjgl-3/lwjgl-opengl.jar:" + runtimePath + "/lwjgl-3/lwjgl-openal.jar:" + runtimePath + "/lwjgl-3/lwjgl-glfw.jar:" + runtimePath + "/lwjgl-3/lwjgl-stb.jar:" + runtimePath + "/lwjgl-3/lwjgl.jar:" +  mcVersion.getClassPath(config);
				dlopen(runtimePath + "/libglfw.so");
				dlopen(runtimePath + "/lwjgl-3/liblwjgl.so");
				dlopen(runtimePath + "/lwjgl-3/liblwjgl_stb.so");
				dlopen(runtimePath + "/lwjgl-3/liblwjgl_tinyfd.so");
				dlopen(runtimePath + "/lwjgl-3/liblwjgl_opengl.so");
				setLibraryPath(libraryPath);
            }

            setupJLI();	

            redirectStdio(home + "/client_output.txt");
            chdir(home);


            Vector<String> args = new Vector<String>();

            args.add(runtimePath +  "/j2re-image/bin/java");
            args.add("-cp");
            args.add(classPath);
            args.add("-Djava.library.path=" + libraryPath);

			args.add("-Dorg.lwjgl.util.Debug=true");
			args.add("-Dorg.lwjgl.util.DebugLoader=true");

			args.add("-Dfml.ignoreInvalidMinecraftCertificates=true");
			args.add("-Dfml.ignorePatchDiscrepancies=true");
			//禁止显示加载窗口
			args.add("-Dfml.earlyprogresswindow=false");
			//添加tｍpdir以获得权限读取json
			args.add("-Djava.io.tmpdir=/data/data/com.koishi.launcher.h2o2/cache");


            String extraJavaFlags[] = config.get("extraJavaFlags").split(" ");

            for (String flag : extraJavaFlags) {
                args.add(flag);
            }

            args.add(mcVersion.mainClass);

			String minecraftArgs[]=null;
            if (isLwjgl3) {
                minecraftArgs = mcVersion.getMinecraftArguments(config, true);	
            } else {
                minecraftArgs = mcVersion.getMinecraftArguments(config, false);	
            }
            for (String flag : minecraftArgs) {
                args.add(flag);
            }
			
			int bb;
			bb = 100;
            args.add("Update20210310");
			args.add("--width");
            args.add(Integer.toString(BoatApplication.getCurrentActivity().getResources().getDisplayMetrics().widthPixels));
            args.add("--height");
            args.add(Integer.toString(BoatApplication.getCurrentActivity().getResources().getDisplayMetrics().heightPixels));

			if (mcVersion.minimumLauncherVersion >= 21) {
				//判断minimumLauncherVersion是否大于21，大于的是高版本，高版本需要用方法加载不同版本的loader，这个只是其中一个
				/*
				 args.add("--launchTarget");
				 args.add("fmlclient");
				 args.add("--fml.forgeVersion");
				 args.add("31.2.47");
				 args.add("--fml.mcVersion");
				 args.add("1.15.2");
				 args.add("--fml.forgeGroup");
				 args.add("net.minecraftforge");
				 args.add("--fml.mcpVersion");
				 args.add("20200515.085601");
				 */
				 args.add("--tweakClass");
				 args.add("optifine.OptiFineTweaker");
			}
			//判断minimumLauncherVersion是否在14-21之间以及size的值，1.7.10forge的size为72996，需要单独加载cpw.mods.xx
			if (mcVersion.minimumLauncherVersion >= 14 && mcVersion.minimumLauncherVersion < 21 && mcVersion.assetIndex.size != 72996) {
				// 1.8-1.12.2
				args.add("--tweakClass");
				if (mcVersion.id.indexOf("OptiFine") != -1) {
					args.add("optifine.OptiFineTweaker");
				} else {
					args.add("net.minecraftforge.fml.common.launcher.FMLTweaker");
				}
			}

			if (mcVersion.minimumLauncherVersion >= 14 && mcVersion.minimumLauncherVersion < 21 && mcVersion.assetIndex.size == 72996) {
				// Below 1.7.10
				args.add("--tweakClass");
				if (mcVersion.id.indexOf("OptiFine") != -1) {
					args.add("optifine.OptiFineTweaker");
				} else {
					args.add("cpw.mods.fml.common.launcher.FMLTweaker");
				}

            }
			if (token.equals("0000")){
			//args.add("--demo");
			}


			String extraMinecraftArgs[] = config.get("extraMinecraftFlags").split(" ");
			for (String flag : extraMinecraftArgs) {
				args.add(flag);
			}
			String finalArgs[] = new String[args.size()];
			for (int i = 0; i < args.size(); i++) {

				finalArgs[i] = args.get(i);
				System.out.println(finalArgs[i]);
			}

			/*
			 String finalArgs[] = new String[]{
			 "/data/user/0/jackpal.androidterm/app_HOME/boat_runtime/aarch64/j2re-image/bin/java",
			 "-Dminecraft.client.jar=/storage/emulated/0/boat/gamedir/versions/1.16.4/1.16.4.jar",
			 "-Duser.home=null",
			 "-XX:+UnlockExperimentalVMOptions",
			 "-XX:+UseG1GC",
			 "-XX:G1NewSizePercent=20",
			 "-XX:G1ReservePercent=20",
			 "-XX:MaxGCPauseMillis=50",
			 "-XX:G1HeapRegionSize=16M",
			 "-XX:-UseAdaptiveSizePolicy",
			 "-XX:-OmitStackTraceInFastThrow",
			 "-Xmn128m",
			 "-Xmx4096m",
			 "-Dfml.ignoreInvalidMinecraftCertificates=true",
			 "-Dfml.ignorePatchDiscrepancies=true",
			 "-Djava.library.path=" + libraryPath,
			 "-Dminecraft.launcher.brand=HMCL",
			 "-Dminecraft.launcher.version=3.3.163",
			 "-Djava.io.tmpdir=/data/user/0/jackpal.androidterm/app_HOME/tmp", 
			 "-cp",
			 "/storage/emulated/0/boat/gamedir/libraries/net/fabricmc/tiny-mappings-parser/0.2.2.14/tiny-mappings-parser-0.2.2.14.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/net/fabricmc/sponge-mixin/0.8.2+build.24/sponge-mixin-0.8.2+build.24.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/net/fabricmc/tiny-remapper/0.3.0.70/tiny-remapper-0.3.0.70.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/net/fabricmc/access-widener/1.0.0/access-widener-1.0.0.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/net/fabricmc/fabric-loader-sat4j/2.3.5.4/fabric-loader-sat4j-2.3.5.4.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/com/google/jimfs/jimfs/1.2-fabric/jimfs-1.2-fabric.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/org/ow2/asm/asm/9.0/asm-9.0.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/org/ow2/asm/asm-analysis/9.0/asm-analysis-9.0.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/org/ow2/asm/asm-commons/9.0/asm-commons-9.0.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/org/ow2/asm/asm-tree/9.0/asm-tree-9.0.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/org/ow2/asm/asm-util/9.0/asm-util-9.0.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/com/google/guava/guava/21.0/guava-21.0.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/net/fabricmc/intermediary/1.16.4/intermediary-1.16.4.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/net/fabricmc/fabric-loader/0.10.8/fabric-loader-0.10.8.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/com/mojang/patchy/1.1/patchy-1.1.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/oshi-project/oshi-core/1.1/oshi-core-1.1.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/net/java/dev/jna/jna/4.4.0/jna-4.4.0.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/net/java/dev/jna/platform/3.4.0/platform-3.4.0.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/com/ibm/icu/icu4j/66.1/icu4j-66.1.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/com/mojang/javabridge/1.0.22/javabridge-1.0.22.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/net/sf/jopt-simple/jopt-simple/5.0.3/jopt-simple-5.0.3.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/io/netty/netty-all/4.1.25.Final/netty-all-4.1.25.Final.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/org/apache/commons/commons-lang3/3.5/commons-lang3-3.5.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/commons-io/commons-io/2.5/commons-io-2.5.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/commons-codec/commons-codec/1.10/commons-codec-1.10.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/net/java/jinput/jinput/2.0.5/jinput-2.0.5.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/net/java/jutils/jutils/1.0.0/jutils-1.0.0.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/com/mojang/brigadier/1.0.17/brigadier-1.0.17.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/com/mojang/datafixerupper/4.0.26/datafixerupper-4.0.26.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/com/google/code/gson/gson/2.8.0/gson-2.8.0.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/com/mojang/authlib/2.0.27/authlib-2.0.27.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/org/apache/commons/commons-compress/1.8.1/commons-compress-1.8.1.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/org/apache/httpcomponents/httpclient/4.3.3/httpclient-4.3.3.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/commons-logging/commons-logging/1.1.3/commons-logging-1.1.3.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/org/apache/httpcomponents/httpcore/4.3.2/httpcore-4.3.2.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/it/unimi/dsi/fastutil/8.2.1/fastutil-8.2.1.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/org/apache/logging/log4j/log4j-api/2.8.1/log4j-api-2.8.1.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/org/apache/logging/log4j/log4j-core/2.8.1/log4j-core-2.8.1.jar:" +
			 "/data/user/0/jackpal.androidterm/app_HOME/boat_runtime/aarch64/lwjgl-3/lwjgl.jar:" +
			 "/data/user/0/jackpal.androidterm/app_HOME/boat_runtime/aarch64/lwjgl-3/lwjgl-jemalloc.jar:" +
			 "/data/user/0/jackpal.androidterm/app_HOME/boat_runtime/aarch64/lwjgl-3/lwjgl-openal.jar:" +
			 "/data/user/0/jackpal.androidterm/app_HOME/boat_runtime/aarch64/lwjgl-3/lwjgl-opengl.jar:" +
			 "/data/user/0/jackpal.androidterm/app_HOME/boat_runtime/aarch64/lwjgl-3/lwjgl-glfw.jar:" +
			 "/data/user/0/jackpal.androidterm/app_HOME/boat_runtime/aarch64/lwjgl-3/lwjgl-stb.jar:" +
			 "/data/user/0/jackpal.androidterm/app_HOME/boat_runtime/aarch64/lwjgl-3/lwjgl-tinyfd.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/com/mojang/text2speech/1.11.3/text2speech-1.11.3.jar:" +
			 "/storage/emulated/0/boat/gamedir/versions/1.16.4/1.16.4.jar",
			 "net.fabricmc.loader.launch.knot.KnotClient",
			 "--username",
			 "cosine",
			 "--version",
			 "HMCL 3.3.163",
			 "--gameDir",
			 "/storage/emulated/0/boat/gamedir",
			 "--assetsDir",
			 "/storage/emulated/0/boat/gamedir/assets",
			 "--assetIndex",
			 "1.16",
			 "--uuid",
			 "f940db7497ea30aca0f3f57f0ab8bbf3",
			 "--accessToken",
			 "046c840acd7b4727b080024b1e360a4f",
			 "--userType",
			 "mojang",
			 "--versionType",
			 "HMCL 3.3.163",
			 "--width",
			 "2270",
			 "--height",
			 "1080" 
			 };
			 */
			/*
			 String finalArgs[] = new String[]{
			 "/data/user/0/jackpal.androidterm/app_HOME/boat_runtime/aarch64/j2re-image/bin/java", 
			 "-Dminecraft.client.jar=/storage/emulated/0/boat/gamedir/versions/1.16.4/1.16.4.jar", 
			 "-Duser.home=null", 
			 "-XX:+UnlockExperimentalVMOptions", 
			 "-XX:+UseG1GC", 
			 "-XX:G1NewSizePercent=20", 
			 "-XX:G1ReservePercent=20", 
			 "-XX:MaxGCPauseMillis=50", 
			 "-XX:G1HeapRegionSize=16M", 
			 "-XX:-UseAdaptiveSizePolicy", 
			 "-XX:-OmitStackTraceInFastThrow", 
			 "-Xmn128m", 
			 "-Xmx3968m", 
			 "-Dfml.ignoreInvalidMinecraftCertificates=true", 
			 "-Dfml.ignorePatchDiscrepancies=true", 
			 "-Djava.library.path=" + libraryPath, 
			 "-Dminecraft.launcher.brand=HMCL", 
			 "-Dminecraft.launcher.version=3.3.163", 
			 "-Djava.io.tmpdir=/data/user/0/jackpal.androidterm/app_HOME/tmp", 
			 //"-Dfml.earlyprogresswindow=false", 
			 "-cp", 
			 "/storage/emulated/0/boat/gamedir/libraries/net/minecraftforge/forge/1.16.4-35.1.29/forge-1.16.4-35.1.29.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/org/ow2/asm/asm/7.2/asm-7.2.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/org/ow2/asm/asm-commons/7.2/asm-commons-7.2.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/org/ow2/asm/asm-tree/7.2/asm-tree-7.2.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/org/ow2/asm/asm-util/7.2/asm-util-7.2.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/org/ow2/asm/asm-analysis/7.2/asm-analysis-7.2.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/cpw/mods/modlauncher/8.0.6/modlauncher-8.0.6.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/cpw/mods/grossjava9hacks/1.3.0/grossjava9hacks-1.3.0.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/net/minecraftforge/accesstransformers/2.2.0-shadowed/accesstransformers-2.2.0-shadowed.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/net/minecraftforge/eventbus/3.0.5-service/eventbus-3.0.5-service.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/net/minecraftforge/forgespi/3.2.0/forgespi-3.2.0.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/net/minecraftforge/coremods/3.0.0/coremods-3.0.0.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/net/minecraftforge/unsafe/0.2.0/unsafe-0.2.0.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/com/electronwill/night-config/core/3.6.2/core-3.6.2.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/com/electronwill/night-config/toml/3.6.2/toml-3.6.2.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/org/jline/jline/3.12.1/jline-3.12.1.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/org/apache/maven/maven-artifact/3.6.0/maven-artifact-3.6.0.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/net/jodah/typetools/0.8.3/typetools-0.8.3.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/org/apache/logging/log4j/log4j-api/2.11.2/log4j-api-2.11.2.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/org/apache/logging/log4j/log4j-core/2.11.2/log4j-core-2.11.2.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/net/minecrell/terminalconsoleappender/1.2.0/terminalconsoleappender-1.2.0.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/net/sf/jopt-simple/jopt-simple/5.0.4/jopt-simple-5.0.4.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/org/spongepowered/mixin/0.8.2/mixin-0.8.2.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/com/mojang/patchy/1.1/patchy-1.1.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/oshi-project/oshi-core/1.1/oshi-core-1.1.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/net/java/dev/jna/jna/4.4.0/jna-4.4.0.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/net/java/dev/jna/platform/3.4.0/platform-3.4.0.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/com/ibm/icu/icu4j/66.1/icu4j-66.1.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/com/mojang/javabridge/1.0.22/javabridge-1.0.22.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/io/netty/netty-all/4.1.25.Final/netty-all-4.1.25.Final.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/com/google/guava/guava/21.0/guava-21.0.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/org/apache/commons/commons-lang3/3.5/commons-lang3-3.5.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/commons-io/commons-io/2.5/commons-io-2.5.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/commons-codec/commons-codec/1.10/commons-codec-1.10.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/net/java/jinput/jinput/2.0.5/jinput-2.0.5.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/net/java/jutils/jutils/1.0.0/jutils-1.0.0.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/com/mojang/brigadier/1.0.17/brigadier-1.0.17.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/com/mojang/datafixerupper/4.0.26/datafixerupper-4.0.26.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/com/google/code/gson/gson/2.8.0/gson-2.8.0.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/com/mojang/authlib/2.1.28/authlib-2.1.28.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/org/apache/commons/commons-compress/1.8.1/commons-compress-1.8.1.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/org/apache/httpcomponents/httpclient/4.3.3/httpclient-4.3.3.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/commons-logging/commons-logging/1.1.3/commons-logging-1.1.3.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/org/apache/httpcomponents/httpcore/4.3.2/httpcore-4.3.2.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/it/unimi/dsi/fastutil/8.2.1/fastutil-8.2.1.jar:" +
			 "/data/user/0/jackpal.androidterm/app_HOME/boat_runtime/aarch64/lwjgl-3/lwjgl.jar:" +
			 "/data/user/0/jackpal.androidterm/app_HOME/boat_runtime/aarch64/lwjgl-3/lwjgl-jemalloc.jar:" +
			 "/data/user/0/jackpal.androidterm/app_HOME/boat_runtime/aarch64/lwjgl-3/lwjgl-openal.jar:" +
			 "/data/user/0/jackpal.androidterm/app_HOME/boat_runtime/aarch64/lwjgl-3/lwjgl-opengl.jar:" +
			 "/data/user/0/jackpal.androidterm/app_HOME/boat_runtime/aarch64/lwjgl-3/lwjgl-glfw.jar:" +
			 "/data/user/0/jackpal.androidterm/app_HOME/boat_runtime/aarch64/lwjgl-3/lwjgl-stb.jar:" +
			 "/data/user/0/jackpal.androidterm/app_HOME/boat_runtime/aarch64/lwjgl-3/lwjgl-tinyfd.jar:" +
			 "/storage/emulated/0/boat/gamedir/libraries/com/mojang/text2speech/1.11.3/text2speech-1.11.3.jar:" +
			 "/storage/emulated/0/boat/gamedir/versions/1.16.4/1.16.4.jar", 
			 "cpw.mods.modlauncher.Launcher", 
			 "--username", 
			 "cosine", 
			 "--version", 
			 "HMCL 3.3.163", 
			 "--gameDir", 
			 "/storage/emulated/0/boat/gamedir", 
			 "--assetsDir", 
			 "/storage/emulated/0/boat/gamedir/assets", 
			 "--assetIndex", 
			 "1.16", 
			 "--uuid", 
			 "f940db7497ea30aca0f3f57f0ab8bbf3", 
			 "--accessToken", 
			 "af06500900654b59b63415e3e62caf95", 
			 "--userType", 
			 "mojang", 
			 "--versionType", 
			 "HMCL 3.3.163", 
			 "--width", 
			 "2270", 
			 "--height", 
			 "1080", 
			 "--launchTarget", 
			 "fmlclient", 
			 "--fml.forgeVersion", 
			 "35.1.29", 
			 "--fml.mcVersion", 
			 "1.16.4", 
			 "--fml.forgeGroup", 
			 "net.minecraftforge", 
			 "--fml.mcpVersion", 
			 "20201102.104115"
			 };
			 */

            System.out.println("OpenJDK exited with code : " + jliLaunch(finalArgs));
			/*
			 File so_map = new File("/proc/self/maps");
			 BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(so_map)));

			 File map_log= new File("/sdcard/boat/boat_mem_maps.txt");
			 BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(map_log)));
			 String line;
			 while((line = br.readLine()) != null){
			 bw.write(line + "\n");

			 }
			 */

        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
		return 0;
    }

}
/*
[ 01-02 12:09:39.305 20726:21155 I/System.out ]
MC启动参数:/data/data/com.mio.boat/app_runtime/j2re-image/bin/java

[ 01-02 12:09:39.305 20726:21155 I/System.out ]
MC启动参数:-cp

[ 01-02 12:09:39.305 20726:21155 I/System.out ]
MC启动参数:/data/data/com.mio.boat/app_runtime/lwjgl-3/lwjgl-jemalloc.jar:/data/data/com.mio.boat/app_runtime/lwjgl-3/lwjgl-tinyfd.jar:/data/data/com.mio.boat/app_runtime/lwjgl-3/lwjgl-opengl.jar:/data/data/com.mio.boat/app_runtime/lwjgl-3/lwjgl-openal.jar:/data/data/com.mio.boat/app_runtime/lwjgl-3/lwjgl-glfw.jar:/data/data/com.mio.boat/app_runtime/lwjgl-3/lwjgl-stb.jar:/data/data/com.mio.boat/app_runtime/lwjgl-3/lwjgl.jar:/storage/emulated/0/Android/data/com.mio.boat/澪/MC/1.16/.minecraft/versions/fabric-loader-0.10.8-1.16.4/fabric-loader-0.10.8-1.16.4.jar:/storage/emulated/0/Android/data/com.mio.boat/澪/MC/1.16/.minecraft/libraries/net/fabricmc/tiny-mappings-parser/0.2.2.14/tiny-mappings-parser-0.2.2.14.jar:/storage/emulated/0/Android/data/com.mio.boat/澪/MC/1.16/.minecraft/libraries/net/fabricmc/sponge-mixin/0.8.2+build.24/sponge-mixin-0.8.2+build.24.jar:/storage/emulated/0/Android/data/com.mio.boat/澪/MC/1.16/.minecraft/libraries/net/fabricmc/tiny-remapper/0.3.0.70/tiny-remapper-0.3.0.70.jar:/storage/emulated/0/Android/data/com.mio.boat/澪/MC/1.16/.minecraft/libraries/net/fabricmc/access-widener/1.0.0/access-widener-1.0.0.jar:/storage/emulated/0/Android/data/com.mio.boat/澪/MC/1.16/.minecraft/libraries/net/fabricmc/fabric-loader-sat4j/2.3.5.4/fabric-loader-sat4j-2.3.5.4.jar:/storage/emulated/0/Android/data/com.mio.boat/澪/MC/1.16/.minecraft/libraries/com/google/jimfs/jimfs/1.2-fabric/jimfs-1.2-fabric.jar:/storage/emulated/0/Android/data/com.mio.boat/澪/MC/1.16/.minecraft/libraries/org/ow2/asm/asm/9.0/asm-9.0.jar:/storage/emulated/0/Android/data/com.mio.boat/澪/MC/1.16/.minecraft/libraries/org/ow2/asm/asm-analysis/9.0/asm-analysis-9.0.jar:/storage/emulated/0/Android/data/com.mio.boat/澪/MC/1.16/.minecraft/libraries/org/ow2/asm/asm-commons/9.0/asm-commons-9.0.jar:/storage/emulated/0/Android/data/com.mio.boat/澪/MC/1.16/.minecraft/libraries/org/ow2/asm/asm-tree/9.0/asm-tree-9.0.jar:/storage/emulated/0/Android/data/com.mio.boat/澪/MC/1.16/.minecraft/libraries/org/ow2/asm/asm-util/9.0/asm-util-9.0.jar:/storage/emulated/0/Android/data/com.mio.boat/澪/MC/1.16/.minecraft/libraries/com/google/guava/guava/21.0/guava-21.0.jar:/storage/emulated/0/Android/data/com.mio.boat/澪/MC/1.16/.minecraft/libraries/net/fabricmc/intermediary/1.16.4/intermediary-1.16.4.jar:/storage/emulated/0/Android/data/com.mio.boat/澪/MC/1.16/.minecraft/libraries/net/fabricmc/fabric-loader/0.10.8/fabric-loader-0.10.8.jar:/storage/emulated/0/Android/data/com.mio.boat/澪/MC/1.16/.minecraft/libraries/com/mojang/patchy/1.1/patchy-1.1.jar:/storage/emulated/0/Android/data/com.mio.boat/澪/MC/1.16/.minecraft/libraries/oshi-project/oshi-core/1.1/oshi-core-1.1.jar:/storage/emulated/0/Android/data/com.mio.boat/澪/MC/1.16/.minecraft/libraries/net/java/dev/jna/jna/4.4.0/jna-4.4.0.jar:/storage/emulated/0/Android/data/com.mio.boat/澪/MC/1.16/.minecraft/libraries/net/java/dev/jna/platform/3.4.0/platform-3.4.0.jar:/storage/emulated/0/Android/data/com.mio.boat/澪/MC/1.16/.minecraft/libraries/com/ibm/icu/icu4j/66.1/icu4j-66.1.jar:/storage/emulated/0/Android/data/com.mio.boat/澪/MC/1.16/.minecraft/libraries/com/mojang/javabridge/1.0.22/javabridge-1.0.22.jar:/storage/emulated/0/Android/data/com.mio.boat/澪/MC/1.16/.minecraft/libraries/net/sf/jopt-simple/jopt-simple/5.0.3/jopt-simple-5.0.3.jar:/storage/emulated/0/Android/data/com.mio.boat/澪/MC/1.16/.minecraft/libraries/io/netty/netty-all/4.1.25.Final/netty-all-4.1.25.Final.jar:/storage/emulated/0/Android/data/com.mio.boat/澪/MC/1.16/.minecraft/libraries/com/google/guava/guava/21.0/guava-21.0.jar:/storage/emulated/0/Android/data/com.mio.boat/澪/MC/1.16/.minecraft/libraries/org/apache/commons/commons-lang3/3.5/commons-lang3-3.5.jar:/storage/emulated/0/Android/data/com.mio.boat/澪/MC/1.16/.minecraft/libraries/commons-io/commons-io/2.5/commons-io-2.5.jar:/storage/emulated/0/Android/data/com.mio.boat/澪/MC/1.16/.minecraft/libraries/commons-codec/commons-codec/1.10/commons-codec-1.10.jar:/storage/emulated/0/Android/data/com.mio.boat/澪/MC/1.16/.minecraft/libra

[ 01-02 12:09:39.305 20726:21155 I/System.out ]
MC启动参数:-Djava.library.path=/storage/emulated/0/Android/data/com.mio.boat/澪/MC/1.16/.minecraft/mods/:/data/data/com.mio.boat/app_runtime/j2re-image/lib/aarch64/jli:/data/data/com.mio.boat/app_runtime/j2re-image/lib/aarch64:/data/data/com.mi


[ 01-02 12:09:39.305 20726:21155 I/System.out ]
MC启动参数:-Djava.library.path=/storage/emulated/0/Android/data/com.mio.boat/澪/MC/1.16/.minecraft/mods/:/data/data/com.mio.boat/app_runtime/j2re-image/lib/aarch64/jli:/data/data/com.mio.boat/app_runtime/j2re-image/lib/aarch64:/data/data/com.mio.boat/app_runtime/lwjgl-3:/data/data/com.mio.boat/app_runtime

[ 01-02 12:09:39.305 20726:21155 I/System.out ]
MC启动参数:-server

[ 01-02 12:09:39.305 20726:21155 I/System.out ]
MC启动参数:-Xms1024M

[ 01-02 12:09:39.305 20726:21155 I/System.out ]
MC启动参数:-Xmx2048M

[ 01-02 12:09:39.305 20726:21155 I/System.out ]
MC启动参数:-Djava.io.tmpdir=/data/data/com.mio.boat/cache

[ 01-02 12:09:39.305 20726:21155 I/System.out ]
MC启动参数:-Dfml.ignoreInvalidMinecraftCertificates=true

[ 01-02 12:09:39.305 20726:21155 I/System.out ]
MC启动参数:-Dfml.ignorePatchDiscrepancies=true

[ 01-02 12:09:39.305 20726:21155 I/System.out ]
MC启动参数:-Dorg.lwjgl.util.DebugLoader=true

[ 01-02 12:09:39.305 20726:21155 I/System.out ]
MC启动参数:-Dorg.lwjgl.util.Debug=true

[ 01-02 12:09:39.305 20726:21155 I/System.out ]
MC启动参数:net.fabricmc.loader.launch.knot.KnotClient

[ 01-02 12:09:39.305 20726:21155 I/System.out ]
MC启动参数:--username

[ 01-02 12:09:39.305 20726:21155 I/System.out ]
MC启动参数:Mio

[ 01-02 12:09:39.305 20726:21155 I/System.out ]
MC启动参数:--version

[ 01-02 12:09:39.305 20726:21155 I/System.out ]
MC启动参数:1.16.4

[ 01-02 12:09:39.305 20726:21155 I/System.out ]
MC启动参数:--gameDir

[ 01-02 12:09:39.305 20726:21155 I/System.out ]
MC启动参数:/storage/emulated/0/Android/data/com.mio.boat/澪/MC/1.16/.minecraft

[ 01-02 12:09:39.305 20726:21155 I/System.out ]
MC启动参数:--assetsDir

[ 01-02 12:09:39.305 20726:21155 I/System.out ]
MC启动参数:/storage/emulated/0/Android/data/com.mio.boat/澪/MC/1.16/.minecraft/assets

[ 01-02 12:09:39.305 20726:21155 I/System.out ]
MC启动参数:--assetIndex

[ 01-02 12:09:39.305 20726:21155 I/System.out ]
MC启动参数:1.16

[ 01-02 12:09:39.305 20726:21155 I/System.out ]
MC启动参数:--uuid

[ 01-02 12:09:39.305 20726:21155 I/System.out ]
MC启动参数:BLUUID

[ 01-02 12:09:39.305 20726:21155 I/System.out ]
MC启动参数:--accessToken

[ 01-02 12:09:39.305 20726:21155 I/System.out ]
MC启动参数:0

[ 01-02 12:09:39.305 20726:21155 I/System.out ]
MC启动参数:--userType

[ 01-02 12:09:39.305 20726:21155 I/System.out ]
MC启动参数:mojang

[ 01-02 12:09:39.305 20726:21155 I/System.out ]
MC启动参数:--versionType

[ 01-02 12:09:39.305 20726:21155 I/System.out ]
MC启动参数:release

[ 01-02 12:09:39.305 20726:21155 I/System.out ]
MC启动参数:--width

[ 01-02 12:09:39.305 20726:21155 I/System.out ]
MC启动参数:2159

[ 01-02 12:09:39.305 20726:21155 I/System.out ]
MC启动参数:--height

[ 01-02 12:09:39.305 20726:21155 I/System.out ]
MC启动参数:1036


*/
