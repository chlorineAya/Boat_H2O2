package cosine.boat.activity;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class LoginTask
{
    public static String[] login(String username,String password){

        try
        {
            JSONObject json=new JSONObject();
            json.put("username",username);
            json.put("password",password);
            json.put("agent",new JSONObject().put("name","Minecraft").put("version",1));
            json.put("requestUser",true);
            json.put("clientToken","mio_launcher");
			String str=getJsonData(json,"https://authserver.mojang.com/authenticate",false);
            JSONObject data=new JSONObject(str);
			String[] msg=new String[4];
            //token
            msg[0]=data.getString("accessToken");
            //id
            msg[1]=data.getJSONObject("selectedProfile").getString("name");
            //uuid
            msg[2]=getUUID(msg[1]);
			//true id
//			msg[3]=data.getJSONObject("selectedProfile").getString("id");
            
            return msg;
        }
        catch (Exception e)
        {
			String[] s=new String[1];
			s[0]="error:"+e.toString();
			return s;
		}
    }
	public static boolean checkif(String at){

        try
        {
            JSONObject json=new JSONObject();
            json.put("accessToken",at);
            json.put("clientToken","boat_launcher");
            if(getJsonData(json,"https://authserver.mojang.com/validate",true).equals("204")){
				return true;
			}else{
				return false;
			}
        }
        catch (JSONException e)
        {
			
		}

        return false;
    }
    private static String getJsonData(JSONObject jsonParam,String urls,boolean nodata) {
        StringBuffer sb=new StringBuffer();
        try {
            // 创建url资源
            URL url = new URL(urls);
            // 建立http连接
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			SSLContext sc=SSLContext.getInstance("SSL");
			sc.init(null, new TrustManager[] { new TrustAnyTrustManager() },
					new java.security.SecureRandom());

			conn.setSSLSocketFactory(sc.getSocketFactory());
			conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
            // 设置允许输出
            conn.setDoOutput(true);
            // 设置允许输入
            conn.setDoInput(true);
            // 设置不用缓存
            conn.setUseCaches(false);
            // 设置传递方式
            conn.setRequestMethod("POST");
            // 设置维持长连接
            conn.setRequestProperty("Connection", "Keep-Alive");
            // 设置文件字符集:
            conn.setRequestProperty("Charset", "UTF-8");
            // 转换为字节数组
            byte[] data = (jsonParam.toString()).getBytes();
            // 设置文件长度
            conn.setRequestProperty("Content-Length", String.valueOf(data.length));
            // 设置文件类型:
            conn.setRequestProperty("Content-Type", "application/json");
            // 开始连接请求
            conn.connect();        
            OutputStream out = new DataOutputStream(conn.getOutputStream()) ;
            // 写入请求的字符串
            out.write(data);
            out.flush();
            out.close();

            System.out.println(conn.getResponseCode());
			if(nodata)
			{
				return conn.getResponseCode()+"";
			}
            // 请求返回的状态
            if (HttpsURLConnection.HTTP_OK == conn.getResponseCode()){
                System.out.println("连接成功");
                // 请求返回的数据
                InputStream in1 = conn.getInputStream();
                try {
                    String readLine=new String();
                    BufferedReader responseReader=new BufferedReader(new InputStreamReader(in1,"UTF-8"));
                    while((readLine=responseReader.readLine())!=null){
                        sb.append(readLine).append("\n");
                    }
                    responseReader.close();
                    System.out.println(sb.toString());

                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            } else {
                System.out.println("error++");
				
            }

        } catch (Exception e) {
            System.out.println(e);
        }

        return sb.toString();

    }
    public static String getUUID(String playerID)
    {
        try
        {
            Document doc= Jsoup.connect("https://mcuuid.net/?q="+playerID).get();
            return doc.getElementById("results_id").attr("value");
        }
        catch (IOException e)
        {
            return e.toString();
        }

    }
    
    public static void set(String key,String value) {
        try
        {
            FileInputStream in=new FileInputStream("/sdcard/boat/config.txt");
            byte[] b=new byte[in.available()];
            in.read(b);
            in.close();
            String str=new String(b);
            JSONObject json=new JSONObject(str);
            json.remove(key);
            json.put(key, value);
            FileWriter fr=new FileWriter(new File("/sdcard/boat/config.txt"));
            fr.write(json.toString());
            fr.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }
	public static String get(String key) {
        try
        {
            FileInputStream in=new FileInputStream("/sdcard/boat/config.txt");
            byte[] b=new byte[in.available()];
            in.read(b);
            in.close();
            String str=new String(b);
            JSONObject json=new JSONObject(str);
            return json.getString(key);
        }
        catch (Exception e)
        {
            return e.toString();
        }
    }
	private static class TrustAnyTrustManager implements X509TrustManager {

		public void checkClientTrusted(X509Certificate[] chain, String authType)
		throws CertificateException {
		}

		public void checkServerTrusted(X509Certificate[] chain, String authType)
		throws CertificateException {
		}

		public X509Certificate[] getAcceptedIssuers() {
			return new X509Certificate[] {};
		}
	}

	private static class TrustAnyHostnameVerifier implements HostnameVerifier {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	}
}

