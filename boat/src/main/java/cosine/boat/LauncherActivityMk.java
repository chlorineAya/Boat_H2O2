package cosine.boat;

import android.app.Activity;
import android.os.Bundle;
import cosine.boat.logcat.Logcat;
import cosine.boat.logcat.LogcatService;
import android.content.Intent;

public class LauncherActivityMk extends Activity {

    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

		final String logPath = "/mnt/sdcard/games/com.koishi.launcher/h2o2/log.txt";
		Logcat.initializeOutOfProcess(this, logPath, LogcatService.class);

        setContentView(R.layout.launcher_layout_mk);
		
		Intent intent = this.getIntent();
		String c = intent.getStringExtra("data");
		
		Intent i = new Intent(this, BoatActivityMk.class);
		Bundle bundle=new Bundle();
		bundle.putString("config", "/sdcard/games/com.koishi.launcher/h2o2/config.txt");
		i.putExtras(bundle);
		i.putExtra("dat",c);
		this.startActivity(i);
		this.finish();
    }



}
