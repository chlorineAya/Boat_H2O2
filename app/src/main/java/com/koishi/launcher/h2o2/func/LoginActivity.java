package com.koishi.launcher.h2o2.func;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.koishi.launcher.h2o2.R;
import com.koishi.launcher.h2o2.tools.DBHelper;
import com.koishi.launcher.h2o2.tools.LoginTask;
import android.os.Handler;
import android.os.Message;
import android.content.Intent;

import android.view.Menu;
import android.view.MenuItem;
import android.view.KeyEvent;
import android.view.WindowManager;
import java.io.FileInputStream;
import org.json.JSONObject;
import java.io.FileWriter;
import java.io.File;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.ShapeDrawable;

import android.widget.ImageView;
import android.widget.ProgressBar;
import com.koishi.launcher.h2o2.MainActivity;
import android.widget.CompoundButton;
import android.support.v7.widget.CardView;
import android.net.Uri;
import android.widget.PopupMenu;
import java.util.Locale;

public class LoginActivity extends Activity implements OnClickListener {
	private EditText mUserName;
	private EditText mPassword,信息,信息2,信息0;
	private ImageView img1;
	private Button mLoginButton,offline,newacc,ls;
	private ImageButton mDropDown;
	private DBHelper dbHelper;
	private CheckBox mCheckBox,info;
	private PopupWindow popView;
	private TextView logt;
	private MyAdapter dropDownAdapter;
	
	private PopupMenu popupMenu;
	
	private CardView userinfo,inputcv;

	private ProgressBar pb;

	private boolean run = false;
    private final Handler handler = new Handler();
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
		    case R.id.register:
                Intent intent = new Intent();
				intent.setData(Uri.parse("https://www.minecraft.net/zh-hans/store/minecraft-java-edition/buy"));//Url
				intent.setAction(Intent.ACTION_VIEW);
				this.startActivity(intent);
		        break;
			default:
				break;
		}
		return true;
	}


	@Override
    public boolean dispatchKeyEvent(KeyEvent event) {
	    if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {	    
	        finish();
            return true;
        } else if (event.getKeyCode() == KeyEvent.KEYCODE_HOME) {	    
	        finish();
            return true;
        } else if (event.getKeyCode() == KeyEvent.KEYCODE_MENU) {	    
	        finish();
            return true;
        } else if (event.getKeyCode() == KeyEvent.KEYCODE_POWER) {	    
	        finish();
            return true;
        } else {
	        return super.dispatchKeyEvent(event);
	    }
    }



    static public final String SYSTEM_DIALOG_REASON_KEY = "reason";
    static public final String SYSTEM_DIALOG_REASON_GLOBAL_ACTIONS = "globalactions";
    static public final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";
    static public final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";
    static public final String SYSTEM_DIALOG_REASON_ASSIST = "assist";


    public void onReceive(Context arg0, Intent arg1) {
        String action = arg1.getAction();
        //按下Home键会发送ACTION_CLOSE_SYSTEM_DIALOGS的广播
        if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {

            String reason = arg1.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
            if (reason != null) {
                if (reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY)) {
                    // 短按home键
                    finish();
                } else if (reason.equals(SYSTEM_DIALOG_REASON_RECENT_APPS)) {
                    // RECENT_APPS键
                    finish();
                }
            }
        }
    }

    @Override
	public void onResume() {
		super.onResume();


	}

	@Override
	public void onPause() {
		super.onPause();

	}
	/*
	public void setLanguage(View v){
	    popupMenu.show();
	}
	
	public void switchEnglish() {
		switchLanguage(Locale.ENGLISH);
	}

	public void switchChinese() {
		switchLanguage(Locale.CHINESE);
	}
	
	public void switchJapanese() {
		switchLanguage(Locale.JAPAN);
	}

	private void switchLanguage(Locale locale) {
		DisplayMetrics metrics = getResources().getDisplayMetrics();
        Configuration configuration = getResources().getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale);
        } else {
            configuration.locale = locale;
        }
        getResources().updateConfiguration(configuration, metrics);
        //重新启动Activity
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
	}
	*/

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.activity_login);

		run = true;
        handler.postDelayed(task, 1000);

        信息 = (EditText)findViewById(R.id.msg);
		信息2 = (EditText)findViewById(R.id.msg2);
		信息0 = (EditText)findViewById(R.id.msg0);

		pb = (ProgressBar)findViewById(R.id.pb);

        logt = (TextView)findViewById(R.id.login_text);
		img1 = (ImageView)findViewById(R.id.img1);
		
		//ls = (Button)findViewById(R.id.sl);
		/*
	    popupMenu = new PopupMenu(this, ls);//button是popupMenu的宿主，也可以是其他任意view
        popupMenu.getMenuInflater().inflate(R.menu.menu_language, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
		    case R.id.l_zhcn:
                switchChinese();
		        break;
		    case R.id.l_enus:
		        switchEnglish();
		        break;
		    case R.id.l_ja:
		        switchJapanese();
		        break;
			default:
				break;
		}
		return true;
            }
        });
        */
        

		信息2.addTextChangedListener(new TextWatcher(){
                @Override
                public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {
                }

                @Override
                public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {
                }

                @Override
                public void afterTextChanged(Editable p1) {
                    uuid(p1.toString());

                }


            });
        信息2.setText(uid());
        信息.addTextChangedListener(new TextWatcher(){
                @Override
                public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {
                }

                @Override
                public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {
                }

                @Override
                public void afterTextChanged(Editable p1) {
                    name(p1.toString());

                }


            });
        信息.setText(nam());
        信息0.addTextChangedListener(new TextWatcher(){
                @Override
                public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {
                }

                @Override
                public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {
                }

                @Override
                public void afterTextChanged(Editable p1) {
                    token(p1.toString());

                }


            });
        信息0.setText(tok());
		
		inputcv = (CardView)findViewById(R.id.inputcv);

		this.offline = (Button)findViewById(R.id.offline);

		offline.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (dbHelper.queryAllUserName().length > 0) {
						String havefunc = "havefunc";
						Intent intent3 = new Intent(LoginActivity.this, MainActivity.class);
						intent3.putExtra("noplay",havefunc);
						startActivity(intent3);// TODO: Implement this method
						LoginActivity.this.finish();
						}else{
							//信息0.setText("0000");
							//String nofunc = "nofunc";
							String nofunc = "havefunc";
							Intent intent3 = new Intent(LoginActivity.this, MainActivity.class);
							intent3.putExtra("noplay",nofunc);
							startActivity(intent3);// TODO: Implement this method
							LoginActivity.this.finish();
						}
					
				}
			});
		
		this.newacc = (Button)findViewById(R.id.newacc);
		newacc.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					newacc.getBackground().setAlpha(90);
					信息2.setText("");
					信息0.setText("");
					newacc.setEnabled(false);
					mUserName.setVisibility(View.VISIBLE);
					mPassword.setVisibility(View.VISIBLE);
					mCheckBox.setVisibility(View.VISIBLE);
					mDropDown.setVisibility(View.VISIBLE);
					inputcv.setVisibility(View.VISIBLE);
					info.setVisibility(View.VISIBLE);
					img1.setVisibility(View.GONE);
					logt.setVisibility(View.GONE);
				}
			});
			
		info = (CheckBox)findViewById(R.id.infocb);
		info.setChecked(false);
		info.setEnabled(false);
		info.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){ 
				@Override 
				public void onCheckedChanged(CompoundButton buttonView, 
											 boolean isChecked) { 
					// TODO Auto-generated method stub 
					if(isChecked){ 
						userinfo.setVisibility(View.VISIBLE);
						信息2.setText(uid());
						信息.setText(uid());
						信息0.setText(uid());
					}else{ 
						userinfo.setVisibility(View.GONE);
					} 
				} 
			}); 
			
		initWidget();
		
		

		final String id = 信息.getText().toString();
		if (!id.equals("")) {
		} else {
	        信息.setText("Player");
		}
		logt.setText(getResources().getString(R.string.login_welcome) + "\n" + 信息.getText().toString());

        
			
		userinfo = (CardView)findViewById(R.id.userinfo);


	}

	private final Runnable task = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            if (run) {

                handler.postDelayed(this, 1000);
            }
        }
    };
	
	public void hideinfo(View view){
		userinfo.setVisibility(View.GONE);
		info.setChecked(false);
	}

	private void initWidget() {
		dbHelper = new DBHelper(this);
		if (dbHelper.queryAllUserName().length <= 0) {
			//offline.setText("Demo");
		}
		mUserName = (EditText) findViewById(R.id.username);
		mPassword = (EditText) findViewById(R.id.password);
		mLoginButton = (Button) findViewById(R.id.login);
		mDropDown = (ImageButton) findViewById(R.id.dropdown_button);
		mCheckBox = (CheckBox) findViewById(R.id.remember);
		mLoginButton.setOnClickListener(this);
		mDropDown.setOnClickListener(this);
		initLoginUserName();

		final String password = mPassword.getText().toString();
	    if (!password.equals("")) {
			mUserName.setVisibility(View.GONE);
			mPassword.setVisibility(View.GONE);
			mCheckBox.setVisibility(View.GONE);
			mDropDown.setVisibility(View.GONE);
			inputcv.setVisibility(View.GONE);
			info.setVisibility(View.GONE);
			img1.setVisibility(View.VISIBLE);
			logt.setVisibility(View.VISIBLE);
		} else {
		    newacc.getBackground().setAlpha(90);
		    newacc.setEnabled(false);
			mUserName.setVisibility(View.VISIBLE);
			mPassword.setVisibility(View.VISIBLE);
			mCheckBox.setVisibility(View.VISIBLE);
			mDropDown.setVisibility(View.VISIBLE);
			inputcv.setVisibility(View.VISIBLE);
			info.setVisibility(View.VISIBLE);
			img1.setVisibility(View.GONE);
			logt.setVisibility(View.GONE);
		}


	}

	private void initLoginUserName() {
		String[] usernames = dbHelper.queryAllUserName();
		if (usernames.length > 0) {
			String tempName = usernames[usernames.length - 1];
			mUserName.setText(tempName);
			mUserName.setSelection(tempName.length());
			String tempPwd = dbHelper.queryPasswordByName(tempName);
			int checkFlag = dbHelper.queryIsSavedByName(tempName);
			if (checkFlag == 0) {
				mCheckBox.setChecked(false);
			} else if (checkFlag == 1) {
				mCheckBox.setChecked(true);
			}
			mPassword.setText(tempPwd);
		}
		mUserName.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start, int before,
										  int count) {
					mPassword.setText("");
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
											  int after) {

				}

				@Override
				public void afterTextChanged(Editable s) {

				}
			});
	}

	String[] 用户信息;

	public void doLogin() {
		final String userName = mUserName.getText().toString();
		final String password = mPassword.getText().toString();
		if (!userName.equals("") && !password.equals("")) {
			Toast.makeText(LoginActivity.this, "Please wait...", 1000).show();
			mLoginButton.setEnabled(false);
			pb.setVisibility(View.VISIBLE);
			mLoginButton.getBackground().setAlpha(90);
			//登录操作为耗时操作，必须放到线程中执行
			new Thread(new Runnable(){
					@Override
					public void run() {
						if (!LoginTask.checkif(LoginTask.get("auth_access_token"))) {
							//token不可用
							//调用LoginTask
							用户信息 = LoginTask.login(userName, password);
							登录信息检测.sendEmptyMessage(0);
						} else {
							//token可用，无需调用登录
							登录信息检测.sendEmptyMessage(1);
						}


					}
				}).start();
		} else {
			Toast.makeText(LoginActivity.this, "None", 1000).show();
		}

	}

	@Override
	public void onClick(View v) {

		if (v == mLoginButton) {
			doLogin();
		}
		if (v == mDropDown) {
			if (popView != null) {
				if (!popView.isShowing()) {
					popView.showAsDropDown(mUserName);
				} else {
					popView.dismiss();
				}
			} else {
				if (dbHelper.queryAllUserName().length > 0) {
					initPopView(dbHelper.queryAllUserName());
					if (!popView.isShowing()) {
						popView.showAsDropDown(mUserName);
					} else {
						popView.dismiss();
					}
				} else {
					Toast.makeText(this, "None", Toast.LENGTH_LONG).show();
				}

			}
		}



	}
//隐藏输入信息
	private void uuid(String 玩家id) {
        try {
            FileInputStream in=new FileInputStream("/sdcard/games/com.koishi.launcher/h2o2/config.txt");
            byte[] b=new byte[in.available()];
            in.read(b);
            in.close();
            String str=new String(b);
            JSONObject json=new JSONObject(str);
            json.remove("auth_uuid");
            json.put("auth_uuid", 玩家id);
            FileWriter fr=new FileWriter(new File("/sdcard/games/com.koishi.launcher/h2o2/config.txt"));
            fr.write(json.toString());
            fr.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    private String uid() {
        try {
            FileInputStream in=new FileInputStream("/sdcard/games/com.koishi.launcher/h2o2/config.txt");
            byte[] b=new byte[in.available()];
            in.read(b);
            in.close();
            String str=new String(b);
            JSONObject json=new JSONObject(str);
            return json.getString("auth_uuid");
        } catch (Exception e) {
            System.out.println(e);
        }
        return "config.txt not found.";
    }
    private void name(String 玩家) {
        try {
            FileInputStream in=new FileInputStream("/sdcard/games/com.koishi.launcher/h2o2/config.txt");
            byte[] b=new byte[in.available()];
            in.read(b);
            in.close();
            String str=new String(b);
            JSONObject json=new JSONObject(str);
            json.remove("auth_player_name");
            json.put("auth_player_name", 玩家);
            FileWriter fr=new FileWriter(new File("/sdcard/games/com.koishi.launcher/h2o2/config.txt"));
            fr.write(json.toString());
            fr.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    private String nam() {
        try {
            FileInputStream in=new FileInputStream("/sdcard/games/com.koishi.launcher/h2o2/config.txt");
            byte[] b=new byte[in.available()];
            in.read(b);
            in.close();
            String str=new String(b);
            JSONObject json=new JSONObject(str);
            return json.getString("auth_player_name");
        } catch (Exception e) {
            System.out.println(e);
        }
        return "config.txt not found.";
    }
    private void token(String 玩) {
        try {
            FileInputStream in=new FileInputStream("/sdcard/games/com.koishi.launcher/h2o2/config.txt");
            byte[] b=new byte[in.available()];
            in.read(b);
            in.close();
            String str=new String(b);
            JSONObject json=new JSONObject(str);
            json.remove("auth_access_token");
            json.put("auth_access_token", 玩);
            FileWriter fr=new FileWriter(new File("/sdcard/games/com.koishi.launcher/h2o2/config.txt"));
            fr.write(json.toString());
            fr.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    private String tok() {
        try {
            FileInputStream in=new FileInputStream("/sdcard/games/com.koishi.launcher/h2o2/config.txt");
            byte[] b=new byte[in.available()];
            in.read(b);
            in.close();
            String str=new String(b);
            JSONObject json=new JSONObject(str);
            return json.getString("auth_access_token");
        } catch (Exception e) {
            System.out.println(e);
        }
        return "config.txt not found.";
    }

	private void initPopView(String[] usernames) {
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < usernames.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("name", usernames[i]);
			map.put("drawable", R.drawable.xicon);
			list.add(map);
		}
		dropDownAdapter = new MyAdapter(this, list, R.layout.dropdown_item,
										new String[] { "name", "drawable" }, new int[] { R.id.textview,
											R.id.delete });
		ListView listView = new ListView(this);
		listView.setAdapter(dropDownAdapter);

		popView = new PopupWindow(listView, mUserName.getWidth(),
								  ViewGroup.LayoutParams.WRAP_CONTENT, true);
		popView.setFocusable(true);
		popView.setOutsideTouchable(true);
		popView.setBackgroundDrawable(getResources().getDrawable(R.drawable.pwbg));
		// popView.showAsDropDown(mUserName);
	}

	class MyAdapter extends SimpleAdapter {

		private List<HashMap<String, Object>> data;

		public MyAdapter(Context context, List<HashMap<String, Object>> data,
						 int resource, String[] from, int[] to) {
			super(context, data, resource, from, to);
			this.data = data;
		}

		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
							ViewGroup parent) {
			System.out.println(position);
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(LoginActivity.this).inflate(
					R.layout.dropdown_item, null);
				holder.btn = (ImageButton) convertView
					.findViewById(R.id.delete);
				holder.tv = (TextView) convertView.findViewById(R.id.textview);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tv.setText(data.get(position).get("name").toString());
			holder.tv.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						String[] usernames = dbHelper.queryAllUserName();
						mUserName.setText(usernames[position]);
						mPassword.setText(dbHelper
										  .queryPasswordByName(usernames[position]));
						popView.dismiss();
					}
				});
			holder.btn.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						String[] usernames = dbHelper.queryAllUserName();
						if (usernames.length > 0) {
							dbHelper.delete(usernames[position]);
							popView.dismiss();
						}
						String[] newusernames = dbHelper.queryAllUserName();
						if (newusernames.length > 0) {
							initPopView(newusernames);
							popView.showAsDropDown(mUserName);
						} else {
							popView.dismiss();
							popView = null;
						}
					}
				});
			return convertView;
		}
	}

	@Override
	public void onBackPressed() {

	}

	Handler 登录信息检测=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                if (用户信息 == null) {
                    Toast.makeText(LoginActivity.this, 用户信息[0], 1000).show();
                    mCheckBox.setChecked(false);
                    mLoginButton.setEnabled(true);
					pb.setVisibility(View.GONE);
				    mLoginButton.getBackground().setAlpha(255);
				    logt.setText(":(");
                } else if (用户信息.length == 1) {
                    //用户信息长度为1时说明出现了错误，使用toast输出错误信息
                    Toast.makeText(LoginActivity.this, "Failed", 1000).show();
                    mCheckBox.setChecked(false);
                    mLoginButton.setEnabled(true);
					pb.setVisibility(View.GONE);
				    mLoginButton.getBackground().setAlpha(255);
				    logt.setText(":(");
                } else {

                    final String resultAc = mUserName.getText().toString();
			        final String resultPw = mPassword.getText().toString();
				    if (mCheckBox.isChecked()) {
						dbHelper.insertOrUpdate(resultAc, resultPw, 1);
			        } else {
						dbHelper.insertOrUpdate(resultAc, "", 0);
			        }

                    //以下三行用于将获取的登录信息写入config.txt
                    //需要添加文件操作权限
//                LoginTask.set("auth_access_token", userCon[0]);
//                LoginTask.set("auth_player_name", userCon[1]);
//                LoginTask.set("auth_uuid", userCon[2]);

                    //将获取到的token等显示出来
                    信息.setText(用户信息[1]);
                    信息2.setText(用户信息[2]);
                    信息0.setText(用户信息[0]);
					pb.setVisibility(View.GONE);
					String havefunc = "havefunc";
                    Toast.makeText(LoginActivity.this, "Done", 1000).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
					intent.putExtra("noplay",havefunc);
		            startActivity(intent);// TODO: Implement this method
		            finish();


                }
            } else {
                //token可用就显示登录成功
                final String resultAc = mUserName.getText().toString();
				final String resultPw = mPassword.getText().toString();
				pb.setVisibility(View.GONE);
				if (mCheckBox.isChecked()) {
					dbHelper.insertOrUpdate(resultAc, resultPw, 1);
				} else {
					dbHelper.insertOrUpdate(resultAc, "", 0);
				}
                Toast.makeText(LoginActivity.this, "Done", 1000).show();
				String havefunc = "havefunc";
                Intent intent2 = new Intent(LoginActivity.this, MainActivity.class);
				intent2.putExtra("noplay",havefunc);
		        startActivity(intent2);// TODO: Implement this method

            }

        }};

	class ViewHolder {
		private TextView tv;
		private ImageButton btn;
	}

	@Override
	protected void onStop() {
		super.onStop();

	}

}
