package com.koishi.launcher.h2o2.func;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.koishi.launcher.h2o2.R;
import android.view.View;
import android.widget.ImageView;
import android.app.AlertDialog;
import android.view.View.OnClickListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class DonateActivity extends AppCompatActivity {
	
	private String [] data = {"🥇 异星之尘","🥈 Yoyo ​Yukari","🥉 小绿","4. SakerVeLib","5. JiuXia2025","6. 小航","7. B站Minecraft菠萝君","8. 热心市民","9. 余情","10. 余生","11. Vindows95","12. YJ","13. 眼角一丝安逸"};
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);
		
		Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar9);
		setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
				}
			});
		ArrayAdapter<String> arrayAdapter= new ArrayAdapter<String> (
			DonateActivity.this, android.R.layout.simple_list_item_1,data);
        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(arrayAdapter);

    }
	public void vx(View view) {
		ImageView image = new ImageView(this);
		image.setImageResource(R.drawable.qr);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getResources().getString(R.string.menu_5_adt));
		builder.setIcon(R.drawable.ic_boat);
		builder.setView(image);
		//点击对话框以外的区域是否让对话框消失
		builder.setCancelable(true);
		//设置正面按钮
		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
				}
			});
		AlertDialog dialog = builder.create();
		//对话框显示的监听事件
		dialog.setOnShowListener(new DialogInterface.OnShowListener() {
				@Override
				public void onShow(DialogInterface dialog) {

				}
			});
		//对话框消失的监听事件
		dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {

				}
			});
		//显示对话框
		dialog.show();
	}
	public void afd(View view) {
		Intent intent = new Intent();
		intent.setData(Uri.parse("https://afdian.net/@boat-h2o"));//Url
		intent.setAction(Intent.ACTION_VIEW);
		this.startActivity(intent); 
	}
	public void prt(View view) {
		Intent intent = new Intent();
		intent.setData(Uri.parse("https://www.patreon.com/boatapp_h2o"));//Url
		intent.setAction(Intent.ACTION_VIEW);
		this.startActivity(intent);
	}
    
}
