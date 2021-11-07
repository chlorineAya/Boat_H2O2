package cosine.boat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cat.ereza.customactivityoncrash.CustomActivityOnCrash;
import cat.ereza.customactivityoncrash.config.CaocConfig;

public class CrashActivity extends AppCompatActivity {

    public TextView crash;
    public Button restart;
    public CaocConfig config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crash);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setStatusBarColor(getResources().getColor(R.color.material_card_background));
        crash = findViewById(R.id.crash);
        Typeface tf = Typeface.createFromAsset(this.getAssets(),
                "Sans.ttf");
        TextView bigTitle= (TextView) toolbar.getChildAt(0);
        bigTitle.setTypeface(tf);
        bigTitle.setText(getResources().getString(R.string.crash_title));
        restart = findViewById(R.id.restart);
        restart.setTypeface(tf);
        restart.setOnClickListener(rv->restart());
        initData();

    }

    private void initData() {
        //可以获取到的四个信息:
        String stackString = CustomActivityOnCrash.getStackTraceFromIntent(getIntent());//将堆栈跟踪作为字符串获取。
        String logString = CustomActivityOnCrash.getActivityLogFromIntent(getIntent()); //获取错误报告的Log信息
        String allString = CustomActivityOnCrash.getAllErrorDetailsFromIntent(this, getIntent());// 获取所有的信息
        config = CustomActivityOnCrash.getConfigFromIntent(getIntent());//获得配置信息,比如设置的程序崩溃显示的页面和重新启动显示的页面等等信息
        crash.setText(allString);
    }

    public void oc(View v) {
        if (config != null)
            CustomActivityOnCrash.closeApplication(CrashActivity.this, config);
    }

    public void restart(){
        Intent i = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        this.finish();
    }
}