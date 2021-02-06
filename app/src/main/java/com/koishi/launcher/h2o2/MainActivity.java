package com.koishi.launcher.h2o2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import org.json.JSONObject;

import com.koishi.launcher.h2o2.R;

import com.koishi.launcher.h2o2.func.AboutActivity;
import com.koishi.launcher.h2o2.func.CustomActivity;
import com.koishi.launcher.h2o2.func.DonateActivity;
import com.koishi.launcher.h2o2.func.ExecuteActivity;
import com.koishi.launcher.h2o2.func.KeysActivity;
import com.koishi.launcher.h2o2.func.ModsActivity;
import com.koishi.launcher.h2o2.func.PacksActivity;
import com.koishi.launcher.h2o2.func.TexturesActivity;
import com.koishi.launcher.h2o2.func.WorldsActivity;
import android.widget.TextView;
import cosine.boat.LauncherActivity;
import android.widget.Toast;
import android.widget.Button;
import com.koishi.launcher.h2o2.func.DownloadActivity;

/**
 * NavigationView在Toolbar下方展示
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public Spinner spinner;
    public TextView dirshow,vet;
	public Button gomc,gorun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);	

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //这是带Home旋转开关按钮
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //这是不带Home旋转开关按钮
//      ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,  R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();//该方法会自动和Toolbar关联, 将开关的图片显示在了Toolbar上

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //TextView
        dirshow = (TextView)findViewById(R.id.dirshow);
		dirshow.setText(dirs());
		
		gomc = (Button)findViewById(R.id.gomc);
		gorun = (Button)findViewById(R.id.gorun);
		
		vet = (TextView)findViewById(R.id.vet);
		

		check();
		
		haveFile();
		



    }

    public void list() {
		final Spinner list=(Spinner) findViewById(R.id.ver);
        File versionlist = new File(dirshow.getText().toString() + "/versions");
		final String[] ver = versionlist.list() ;
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ver);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		list.setAdapter(adapter);
        list.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> p1, View p2, int p3, long p4) {
					if (!((String)p1.getItemAtPosition(p3)).equals("Null")) {
						setVersion((String)p1.getItemAtPosition(p3));
					}
					String str = (String) list.getSelectedItem();
					vet.setText(str);
					
				}
                @Override
                public void onNothingSelected(AdapterView<?> p1) {
                }
			});
		list.setSelection(adapter.getPosition(ve()));
    }

    public void check() {
        boolean tmp = fileIsExists(dirshow.getText().toString());
        boolean tmp2 = fileIsExists("/sdcard/games/com.koishi.launcher/h2o2/config.txt");
        boolean tmp3 = fileIsExists("/sdcard/games/com.koishi.launcher/h2o2/gamedir");
        if (tmp) {
            if (tmp2){
                if (tmp3){
                    list();
                }else{
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.menu_19), Toast.LENGTH_SHORT).show();
			        Intent i5 = new Intent(this, PacksActivity.class);
			        startActivity(i5);
                }
			}else{
			    Toast.makeText(getApplicationContext(), getResources().getString(R.string.menu_19), Toast.LENGTH_SHORT).show();
			    Intent i5 = new Intent(this, PacksActivity.class);
			    startActivity(i5);
			}
		} else {
			Toast.makeText(getApplicationContext(), getResources().getString(R.string.menu_18), Toast.LENGTH_SHORT).show();
			Intent i5 = new Intent(this, PacksActivity.class);
			startActivity(i5);
		}
    }

    public void setVersion(String version) {
        try {
            FileInputStream in=new FileInputStream("/sdcard/games/com.koishi.launcher/h2o2/config.txt");
            byte[] b=new byte[in.available()];
            in.read(b);
            in.close();
            String str=new String(b);
            JSONObject json=new JSONObject(str);
            json.remove("currentVersion");
            json.put("currentVersion", dirshow.getText().toString() + "/versions/" + version.trim());
            FileWriter fr=new FileWriter(new File("/sdcard/games/com.koishi.launcher/h2o2/config.txt"));
            fr.write(json.toString());
            fr.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

	public String ve() {
        try {
            FileInputStream in=new FileInputStream("/sdcard/games/com.koishi.launcher/h2o2/config.txt");
            byte[] b=new byte[in.available()];
            in.read(b);
            in.close();
            String str=new String(b);
			if (str.equals("")) {
				return "null";
			}
            JSONObject json=new JSONObject(str);
            String cnt=json.getString("currentVersion");
            return cnt.substring(cnt.indexOf("versions") + 9);
        } catch (Exception e) {
            return e.toString();
        }
    }

    public String dirs() {
        try {
            FileInputStream in=new FileInputStream("/sdcard/games/com.koishi.launcher/h2o2/config.txt");
            byte[] b=new byte[in.available()];
            in.read(b);
            in.close();
            String str=new String(b);
            JSONObject json=new JSONObject(str);
            return json.getString("game_directory");
        } catch (Exception e) {
            System.out.println(e);
        }
        return "Error";
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
		super.onResume();
        dirshow.setText(dirs());
        check();
		haveFile();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.m1:

		        break;
		    case R.id.m2:
                Intent i2 = new Intent(this, ExecuteActivity.class);
		        startActivity(i2);
		        break;
		    case R.id.m3:
                Intent i3 = new Intent(this, CustomActivity.class);
		        startActivity(i3);
		        break;
		    case R.id.m4:
                Intent i4 = new Intent(this, KeysActivity.class);
		        startActivity(i4);
		        break;
		    case R.id.m5:
                Intent i5 = new Intent(this, PacksActivity.class);
		        startActivity(i5);
		        break;
		    case R.id.m6:
                Intent i6 = new Intent(this, WorldsActivity.class);
		        startActivity(i6);
		        break;
		    case R.id.m7:
                Intent i7 = new Intent(this, TexturesActivity.class);
		        startActivity(i7);
		        break;
		    case R.id.m8:
                Intent i8 = new Intent(this, ModsActivity.class);
		        startActivity(i8);
		        break;
		    case R.id.m9:
                Intent i9 = new Intent(this, DonateActivity.class);
		        startActivity(i9);
		        break;
		    case R.id.m10:
                Intent i10 = new Intent(this, AboutActivity.class);
		        startActivity(i10);
		        break;
		    default:
		        break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

	public void start(View view) {
		File file = new File(dirshow.getText().toString() + "/versions");
		boolean tmp = fileIsExists("/data/data/com.koishi.launcher.h2o2/app_runtime/libopenal.so.1");
		final String aa = vet.getText().toString();
		if (file.exists() && file.isDirectory()) {
			if (file.list().length == 0) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.menu_13), Toast.LENGTH_SHORT).show();
			}
		} 
		if (file.list().length > 0) {
			if(!tmp){
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.menu_12), Toast.LENGTH_SHORT).show();
			}
			if (tmp){
			    if (aa.equals("")){
			    Toast.makeText(getApplicationContext(), getResources().getString(R.string.menu_14), Toast.LENGTH_SHORT).show();
			    }else{
				Intent i0 = new Intent(this, LauncherActivity.class);
				startActivity(i0);
				}
			}
		}
	}
	
	public void mc (View view){
		Intent i5 = new Intent(this, PacksActivity.class);
		startActivity(i5);
	}
	
	public void runtime (View view){
		Intent ir = new Intent(this, ExecuteActivity.class);
		startActivity(ir);
	}
	
	public void haveFile(){
		File file = new File(dirshow.getText().toString() + "/versions");
		boolean tmp = fileIsExists("/data/data/com.koishi.launcher.h2o2/app_runtime/libopenal.so.1");
		if (file.exists() && file.isDirectory()) {
			if (file.list().length == 0) {
				gomc.setText(getResources().getString(R.string.menu_13));
				gomc.setTextColor(0xffff0000);
			}
		}
		if (!tmp){
			gorun.setText(getResources().getString(R.string.menu_12));
			gorun.setTextColor(0xffff0000);
		}else{
			gorun.setTextColor(getResources().getColor(R.color.colorPrimary));
			gorun.setText(getResources().getString(R.string.menu_16));
		}
	}
	public boolean fileIsExists(String strFile) {
        try {
            File f=new File(strFile);
            if (!f.exists()) {
                return false;
            }

        } catch (Exception e) {
            return false;
        }

        return true;
    }
}
