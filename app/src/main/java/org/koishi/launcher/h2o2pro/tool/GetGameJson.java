package org.koishi.launcher.h2o2pro.tool;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;

public class GetGameJson {

    public static String boatCfg = "/storage/emulated/0/games/com.koishi.launcher/h2o2/config.txt";
    public static String h2oCfg = "/storage/emulated/0/games/com.koishi.launcher/h2o2/h2ocfg.json";

    public static void setBoatJson(String name, String value) {
        try {
            FileInputStream in = new FileInputStream(boatCfg);
            byte[] b = new byte[in.available()];
            in.read(b);
            in.close();
            String str = new String(b);
            JSONObject json = new JSONObject(str);
            json.remove(name);
            json.put(name, value);
            FileWriter fr = new FileWriter(new File(boatCfg));
            fr.write(json.toString());
            fr.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void setAppJson(String name, String value) {
        try {
            FileInputStream in = new FileInputStream(h2oCfg);
            byte[] b = new byte[in.available()];
            in.read(b);
            in.close();
            String str = new String(b);
            JSONObject json = new JSONObject(str);
            json.remove(name);
            json.put(name, value);
            FileWriter fr = new FileWriter(new File(h2oCfg));
            fr.write(json.toString());
            fr.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void setExtraJson(String name, String value, String dir) {
        try {
            FileInputStream in = new FileInputStream(dir);
            byte[] b = new byte[in.available()];
            in.read(b);
            in.close();
            String str = new String(b);
            JSONObject json = new JSONObject(str);
            json.remove(name);
            json.put(name, value);
            FileWriter fr = new FileWriter(new File(dir));
            fr.write(json.toString());
            fr.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    //---------------------获取json的值---------------------

    //h2ocfg
    public static String getBoatCfg(String name, String defaultValue) {
        try {
            FileInputStream in=new FileInputStream(boatCfg);
            byte[] b=new byte[in.available()];
            in.read(b);
            in.close();
            String str=new String(b);
            JSONObject json=new JSONObject(str);
            return json.getString(name);
        } catch (Exception e) {
            System.out.println(e);
        }
        return defaultValue;
    }

    public static String getAppCfg(String name, String defaultValue) {
        try {
            FileInputStream in=new FileInputStream(h2oCfg);
            byte[] b=new byte[in.available()];
            in.read(b);
            in.close();
            String str=new String(b);
            JSONObject json=new JSONObject(str);
            return json.getString(name);
        } catch (Exception e) {
            System.out.println(e);
        }
        return defaultValue;
    }

    public static String getExtraCfg(String value, String defaultValue, String dir) {
        try {
            FileInputStream in=new FileInputStream(dir);
            byte[] b=new byte[in.available()];
            in.read(b);
            in.close();
            String str=new String(b);
            JSONObject json=new JSONObject(str);
            return json.getString(value);
        } catch (Exception e) {
            System.out.println(e);
        }
        return defaultValue;
    }
}
