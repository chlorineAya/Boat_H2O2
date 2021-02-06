package com.koishi.launcher.h2o2.func;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.koishi.launcher.h2o2.R;
import android.view.View;
import android.app.AlertDialog;
import android.widget.TextView;
import android.text.method.LinkMovementMethod;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

public class AboutActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
		
		Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar10);
		setSupportActionBar(toolbar);	
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
				}
			});
        
    }
    public void a1(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getResources().getString(R.string.menu_10));
		builder.setIcon(R.drawable.ic_boat);
		builder.setMessage(getResources().getString(R.string.menu_10_1));
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
    public void a2(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle(getResources().getString(R.string.m102));
				builder.setIcon(R.drawable.ic_boat);
                builder.setMessage(getResources().getString(R.string.menu_10_2));
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
    public void a3(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle(getResources().getString(R.string.m103));
				builder.setIcon(R.drawable.ic_boat);
                builder.setMessage(getResources().getString(R.string.menu_10_3));
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
    public void a4(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle(getResources().getString(R.string.m104));
				builder.setIcon(R.drawable.ic_boat);
                builder.setMessage(getResources().getString(R.string.menu_10_4));
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
    public void boat(View view) {
        Intent intent = new Intent();
		intent.setData(Uri.parse("https://github.com/AOF-Dev/BoatApp"));//Url
		intent.setAction(Intent.ACTION_VIEW);
		this.startActivity(intent); 
    }
    public void h2o(View view) {
        Intent intent = new Intent();
		intent.setData(Uri.parse("https://github.com/NaCln4c1/Boat_H2O-v4"));//Url
		intent.setAction(Intent.ACTION_VIEW);
		this.startActivity(intent);
    }
    public void box(View view) {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle(getResources().getString(R.string.m105));
				builder.setIcon(R.drawable.ic_boat);
                builder.setMessage(getResources().getString(R.string.m107));
				//点击对话框以外的区域是否让对话框消失
				builder.setCancelable(true);
				//设置正面按钮
				builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					});
				builder.setNegativeButton(getResources().getString(R.string.m108), new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
                            go();
							dialog.dismiss();
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
    public void lfp(View view) {
        Intent intent = new Intent();
		intent.setData(Uri.parse("https://github.com/leonHua/LFilePicker"));//Url
		intent.setAction(Intent.ACTION_VIEW);
		this.startActivity(intent);
    }
    public void auth(View view) {
        Intent intent = new Intent();
		intent.setData(Uri.parse("http://www.coolapk.com/u/1996447"));//Url
		intent.setAction(Intent.ACTION_VIEW);
		this.startActivity(intent);
    }
	public void go(){
		Intent intent = new Intent();
		intent.setData(Uri.parse("https://github.com/AOF-Dev/MCinaBox"));//Url
		intent.setAction(Intent.ACTION_VIEW);
		this.startActivity(intent);
	}
    
}
