package cosine.boat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import cosine.boat.logcat.Logcat;
import cosine.boat.logcat.LogcatService;

public class LauncherActivity extends Activity {

    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        final String logPath = "/mnt/sdcard/games/com.koishi.launcher/h2o2/log.txt";
        Logcat.initializeOutOfProcess(this, logPath, LogcatService.class);

        setContentView(R.layout.launcher_layout);
        Intent i = new Intent(LauncherActivity.this, BoatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("config", "/sdcard/games/com.koishi.launcher/h2o2/config.txt");
        i.putExtras(bundle);
        this.startActivity(i);
        this.finish();
    }

}
