package cosine.boat;

import android.view.MotionEvent;
import android.os.Bundle;
import android.app.Activity;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.view.LayoutInflater;
import android.view.Gravity;
import android.view.WindowManager.LayoutParams;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import java.nio.ByteBuffer;
import android.widget.LinearLayout;
import android.os.Handler;
import android.os.Message;
import android.widget.EditText;
import android.widget.TextView;
import android.text.TextWatcher;
import android.text.Editable;
import android.view.inputmethod.EditorInfo;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.ViewGroup;
import android.view.TextureView;
import android.graphics.SurfaceTexture;
import android.view.Surface;
import android.view.SurfaceView;
import android.app.AlertDialog;
import android.view.View.OnClickListener;
import java.util.Timer;
import android.content.DialogInterface;
import java.util.TimerTask;
import android.support.v7.widget.CardView;
import android.view.WindowManager;
import android.os.Build;
import android.content.Intent;
import java.io.FileInputStream;
import org.json.JSONObject;

//import com.koishi.launcher.h2o2.MainActivity;


public class BoatActivity extends Activity implements View.OnClickListener, View.OnTouchListener, TextWatcher, TextView.OnEditorActionListener, TextureView.SurfaceTextureListener, SurfaceHolder.Callback  
{

	private Timer timer;

	private Timer timer2;
	
	private Button button;

	private Button bu1;
	private Button bu2;

	private Button bu3;
	private Button bu4;
	private Button bu5;
	private Button bu6;

	private Button bu7;
	private Button bu8;

	private Button bu9;
	
	private CardView cv1;
	
	private String noesc,jl;
	
	private boolean alpha = false;

	@Override
	public void surfaceCreated(SurfaceHolder p1)
	{
		// TODO: Implement this method
		System.out.println("Surface is available!");
		BoatActivity.setBoatNativeWindow(p1.getSurface());

		new Thread(){
			@Override
			public void run(){

				LauncherConfig config = LauncherConfig.fromFile(getIntent().getExtras().getString("config"));
				LoadMe.exec(config);		
				Message msg = new Message();
				msg.what = -1;
				mHandler.sendMessage(msg);
			}
		}.start();
	}

	@Override
	public void surfaceChanged(SurfaceHolder p1, int p2, int p3, int p4)
	{
		// TODO: Implement this method
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder p1)
	{
		// TODO: Implement this method
		throw new RuntimeException("Surface is destroyed!");
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		//mainSurfaceView = new SurfaceView(this);
		//mainSurfaceView.getHolder().addCallback(this);
		
		setContentView(R.layout.main);
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			alpha=true;
		}else{
			alpha=false;

		}
		
		base = (RelativeLayout)findViewById(R.id.base);
		/*
		popupWindow = new PopupWindow();
		popupWindow.setWidth(LayoutParams.FILL_PARENT);
		popupWindow.setHeight(LayoutParams.FILL_PARENT);
		popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
		popupWindow.setFocusable(true);
		base = (RelativeLayout)LayoutInflater.from(BoatActivity.this).inflate(R.layout.overlay,null);
		*/
		mainTextureView = (TextureView)this.findViewById(R.id.main_surface);
		mainTextureView.setSurfaceTextureListener(this);
		mouseCursor = (ImageView)base.findViewById(R.id.mouse_cursor);
		touchPad = this.findButton(R.id.touch_pad);
		
		cv1=(CardView)base.findViewById(R.id.cv1);
		
		jl = cp3();
		noesc = cp4();
		
        //Control the 2/3 screen
        fwc = this.findButton(R.id.fwc);
        
		touchPad2 = this.findButton(R.id.touch_pad2);
		touchPad2.setOnTouchListener(new View.OnTouchListener(){
				private long currentMS;
				private float itialY,itY,itX,itialX;
				private int moveX,moveY,meX,meY;

			
				@Override
				public boolean onTouch(View p1, MotionEvent p2)
				{
					if (cursorMode == BoatInput.CursorDisabled){
						switch(p2.getActionMasked()){
							case MotionEvent.ACTION_DOWN:
								moveX = 0;
								moveY = 0;
								initialX = (int)p2.getX();
								initialY = (int)p2.getY();
								itialX=p2.getX();
								itialY=p2.getY();
								currentMS = System.currentTimeMillis();
								loc.setText("Location: "+initialX+","+initialY);
							case MotionEvent.ACTION_MOVE:
								moveX += Math.abs(p2.getX() - itialX);
								moveY += Math.abs(p2.getY() - itialY);
								long movesTime = System.currentTimeMillis() - currentMS;//移动时间
								if(movesTime>400&&moveX<3&&moveY<3){
									BoatInput.setMouseButton(BoatInput.Button1, true);
									BoatInput.setPointer(baseX + (int)p2.getX() -initialX, baseY + (int)p2.getY() - initialY);
									return false; 
								}
								BoatInput.setPointer(baseX + (int)p2.getX() -initialX, baseY + (int)p2.getY() - initialY);
								loc.setText("Location: "+(baseX + (int)p2.getX() -initialX)+","+((int)p2.getY() - initialY));
								break;
							case MotionEvent.ACTION_UP:
								baseX += ((int)p2.getX() - initialX);
								baseY += ((int)p2.getY() - initialY);
								BoatInput.setMouseButton(BoatInput.Button1, false);
								BoatInput.setMouseButton(BoatInput.Button3, false);
								BoatInput.setPointer(baseX, baseY);
								long moveTime = System.currentTimeMillis() - currentMS;
								if(moveTime<200&&(moveX<2||moveY<2)){
									BoatInput.setMouseButton(BoatInput.Button3, true);
									BoatInput.setMouseButton(BoatInput.Button3, false);
									return false; 
								}
								
								loc.setText("Location: "+baseX+","+baseY);

								break;
							default:
								break;
						}
					}
					
					else if (cursorMode == BoatInput.CursorEnabled){
						baseX = (int)p2.getX();
						baseY = (int)p2.getY();
						BoatInput.setPointer(baseX, baseY);
						if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
							BoatInput.setMouseButton(BoatInput.Button1, true);

						}
						if (p2.getActionMasked() == MotionEvent.ACTION_UP){
							BoatInput.setMouseButton(BoatInput.Button1, false);
						}
						loc.setText("Location: "+baseX+","+baseY);
			}
				
			
			mouseCursor.setX(p2.getX());
			mouseCursor.setY(p2.getY());
			return true;
				}
			});
		
		fwc.setVisibility(View.INVISIBLE);
		
		controlUp = this.findButton(R.id.control_up);
		controlDown = this.findButton(R.id.control_down);
		controlLeft = this.findButton(R.id.control_left);
		controlRight = this.findButton(R.id.control_right);
		controlJump = this.findButton(R.id.control_jump);
		controlInv = this.findButton(R.id.control_inventory);
		controlLshift = this.findButton(R.id.control_lshift);
		controlLctrl = this.findButton(R.id.control_lctrl);
		controlLctrl.setText("□");
		controlLctrl.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View p1) {
                    if (controlLctrl.getText().toString().equals("□")) {
						BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Control_L, 0, true);
						controlLctrl.setText("■");
                    }else if(controlLctrl.getText().toString().equals("■")){
						BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Control_L, 0, false);
						controlLctrl.setText("□");
                    }
				}
			});
		
		
		controlBs = this.findButton(R.id.control_bs);
		
		//Input TextView with full screen.
		input = (EditText)base.findViewById(R.id.texttest);		
		send = this.findButton(R.id.send);
		send.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View p1) {
                 BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_t, 0, true);
                }
            });
		
		//Shift Lock
		controlLshift.setText("△");
		controlLshift.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View p1) {
                    if (controlLshift.getText().toString().equals("△")) {
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Shift_L, 0, true);
                    controlLshift.setText("▲");
                    controlLshift.setBackground(getResources().getDrawable(R.drawable.control_button_normal_2));
                    }else if(controlLshift.getText().toString().equals("▲")){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Shift_L, 0, false);
                    controlLshift.setText("△");
                    controlLshift.setBackground(getResources().getDrawable(R.drawable.control_button_normal));
                    }
                    }
                    });
		
//		control0 = this.findButton(R.id.control_0);
		control1 = this.findButton(R.id.control_1);
		control2 = this.findButton(R.id.control_2);
		control3 = this.findButton(R.id.control_3);
		control4 = this.findButton(R.id.control_4);
		control5 = this.findButton(R.id.control_5);
		control6 = this.findButton(R.id.control_6);
		control7 = this.findButton(R.id.control_7);
		control8 = this.findButton(R.id.control_8);
		control9 = this.findButton(R.id.control_9);
		
        //New keys 
        //New default layout keys
        controlDebug = this.findButton(R.id.control_debug);
        controlThrow = this.findButton(R.id.control_throw);
        controlSwitch = this.findButton(R.id.control_switch);

        //All keys open
        allkeyon = this.findButton(R.id.allkeyon);
        allkeyoff = this.findButton(R.id.allkeyoff);
        
        allkey = (LinearLayout)base.findViewById(R.id.allkey);
        
        //Most keys of the keyboard.
		keb_a = this.findButton(R.id.keb_a);
		keb_b = this.findButton(R.id.keb_b);
		keb_c = this.findButton(R.id.keb_c);
		keb_d = this.findButton(R.id.keb_d);
		keb_e = this.findButton(R.id.keb_e);
		keb_f = this.findButton(R.id.keb_f);
		keb_g = this.findButton(R.id.keb_g);
		keb_h = this.findButton(R.id.keb_h);
		keb_i = this.findButton(R.id.keb_i);
		keb_j = this.findButton(R.id.keb_j);
		keb_k = this.findButton(R.id.keb_k);
		keb_l = this.findButton(R.id.keb_l);
		keb_m = this.findButton(R.id.keb_m);
		keb_n = this.findButton(R.id.keb_n);
		keb_o = this.findButton(R.id.keb_o);
		keb_p = this.findButton(R.id.keb_p);
		keb_q = this.findButton(R.id.keb_q);
		keb_r = this.findButton(R.id.keb_r);
		keb_s = this.findButton(R.id.keb_s);    
		keb_t = this.findButton(R.id.keb_t);
		keb_u = this.findButton(R.id.keb_u);
		keb_v = this.findButton(R.id.keb_v);
		keb_w = this.findButton(R.id.keb_w);
		keb_x = this.findButton(R.id.keb_x);
		keb_y = this.findButton(R.id.keb_y);
		keb_z = this.findButton(R.id.keb_z);
		
		keb_1 = this.findButton(R.id.keb_1);
		keb_2 = this.findButton(R.id.keb_2);
		keb_3 = this.findButton(R.id.keb_3);
		keb_4 = this.findButton(R.id.keb_4);
		keb_5 = this.findButton(R.id.keb_5);
		keb_6 = this.findButton(R.id.keb_6);
		keb_7 = this.findButton(R.id.keb_7);
		keb_8 = this.findButton(R.id.keb_8);
		keb_9 = this.findButton(R.id.keb_9);
		keb_0 = this.findButton(R.id.keb_0);
		
		keb_f1 = this.findButton(R.id.keb_f1);
		keb_f2 = this.findButton(R.id.keb_f2);
		keb_f3 = this.findButton(R.id.keb_f3);
		keb_f4 = this.findButton(R.id.keb_f4);
		keb_f5 = this.findButton(R.id.keb_f5);
		keb_f6 = this.findButton(R.id.keb_f6);
		keb_f7 = this.findButton(R.id.keb_f7);
		keb_f8 = this.findButton(R.id.keb_f8);
		keb_f9 = this.findButton(R.id.keb_f9);
		keb_f10 = this.findButton(R.id.keb_f10);
		keb_f11 = this.findButton(R.id.keb_f11);
		keb_f12 = this.findButton(R.id.keb_f12);
        
        keb_grave = this.findButton(R.id.keb_grave);
        keb_tab = this.findButton(R.id.keb_tab);
        keb_caps = this.findButton(R.id.keb_caps);
        keb_lshift = this.findButton(R.id.keb_lshift);
        keb_lctrl = this.findButton(R.id.keb_lctrl);
        keb_lalt = this.findButton(R.id.keb_lalt);
        keb_lwin = this.findButton(R.id.keb_lwin);
        
        keb_pause = this.findButton(R.id.keb_pause);
        keb_insert = this.findButton(R.id.keb_insert);
        keb_delete = this.findButton(R.id.keb_delete);
        keb_home = this.findButton(R.id.keb_home);
        
        keb_up = this.findButton(R.id.keb_up);
        keb_down = this.findButton(R.id.keb_down);
        keb_left = this.findButton(R.id.keb_left);
        keb_right = this.findButton(R.id.keb_right);
        
        keb_minus = this.findButton(R.id.keb_minus);
        keb_equal = this.findButton(R.id.keb_equal);
        keb_backspace = this.findButton(R.id.keb_backspace);
        keb_lbracket = this.findButton(R.id.keb_lbracket);
        keb_rbracket = this.findButton(R.id.keb_rbracket);
        keb_semicolon = this.findButton(R.id.keb_semicolon);
        keb_apostrophe = this.findButton(R.id.keb_apostrophe);
        keb_comma = this.findButton(R.id.keb_comma);
        keb_period = this.findButton(R.id.keb_period);
        keb_slash = this.findButton(R.id.keb_slash);
        keb_backslash = this.findButton(R.id.keb_backslash);
        
        keb_return = this.findButton(R.id.keb_return);
        keb_rshift = this.findButton(R.id.keb_rshift);
        keb_ralt = this.findButton(R.id.keb_ralt);
        keb_rctrl = this.findButton(R.id.keb_rctrl);
        keb_rwin = this.findButton(R.id.keb_rwin);
        keb_app = this.findButton(R.id.keb_app);
        
        show = this.findButton(R.id.show);
        hide = this.findButton(R.id.hide);

		//Layout open
		show.setVisibility(View.INVISIBLE);
		allkeyoff.setVisibility(View.INVISIBLE);
		
		//十字键
		
		button=(Button)base.findViewById(R.id.mainButton1);
		
		bu1=(Button)base.findViewById(R.id.buttonButton1);
		bu2=(Button)base.findViewById(R.id.buttonButton2);
		bu3=(Button)base.findViewById(R.id.buttonButton3);
		bu4=(Button)base.findViewById(R.id.buttonButton4);
		bu5=(Button)base.findViewById(R.id.buttonButton5);
		bu6=(Button)base.findViewById(R.id.buttonButton6);
		bu7=(Button)base.findViewById(R.id.buttonButton7);
		bu8=(Button)base.findViewById(R.id.buttonButton8);
		bu9=(Button)base.findViewById(R.id.buttonButton9);
		button.setOnTouchListener(new View.OnTouchListener(){

				private float ddx;

				private float ddy;

				private TextView 文本;

				private int ww;

				private int wh;

				@Override
				public boolean onTouch(View p1, MotionEvent p2)
				{
					if (jl.equals("false")){
					if(p2.getAction()==MotionEvent.ACTION_DOWN){
						 ddx=p2.getX()-button.getX();
						ddy= p2.getY()-button.getY();
						 ww=button.getWidth();
						 wh=button.getHeight();
						
						if(ddy<wh/3&&ddx<ww/3){
							
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_w, 0,true);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_a, 0,true);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_s, 0,false);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_d, 0,false);
							
							bu1.setAlpha(0);
							bu3.setAlpha(0);
							bu7.setAlpha(0);
							bu9.setAlpha(0);
						}else if(ddy<wh/3&&ddx>ww/3&&ddx<ww*2/3){
							
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_w, 0,true);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_a, 0,false);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_s, 0,false);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_d, 0,false);
							
							bu1.setAlpha(1);
							bu3.setAlpha(1);
							bu7.setAlpha(0);
							bu9.setAlpha(0);
							
							
						}
						else if(ddy<wh/3&&ddx>ww*2/3){
							
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_w, 0,true);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_a, 0,false);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_s, 0,false);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_d, 0,true);
							
							bu1.setAlpha(0);
							bu3.setAlpha(0);
							bu7.setAlpha(0);
							bu9.setAlpha(0);
							
						}
						else if(ddy>wh/3&&ddy<wh*2/3&&ddx<ww/3){
							
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_w, 0,false);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_a, 0,true);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_s, 0,false);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_d, 0,false);
							
							bu1.setAlpha(1);
							bu3.setAlpha(0);
							bu7.setAlpha(1);
							bu9.setAlpha(0);
							
							
							
							
						}else if(ddy>wh/3&&ddy<wh*2/3&&ddx>ww/3&&ddx<ww*2/3){
							
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_w, 0,false);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_a, 0,false);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_s, 0,false);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_d, 0,false);
						    
							bu1.setAlpha(0);
							bu3.setAlpha(0);
							bu7.setAlpha(0);
							bu9.setAlpha(0);
							
							
						}
						else if(ddy>wh/3&&ddy<wh*2/3&&ddx>ww*2/3){
							
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_w, 0,false);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_a, 0,false);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_s, 0,false);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_d, 0,true);
							
							bu1.setAlpha(0);
							bu3.setAlpha(1);
							bu7.setAlpha(0);
							bu9.setAlpha(1);
							
							
							
						}
						else if(ddy>wh*2/3&&ddx<ww/3){
							
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_w, 0,false);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_a, 0,true);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_s, 0,true);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_d, 0,false);
							
							bu1.setAlpha(0);
							bu3.setAlpha(0);
							bu7.setAlpha(0);
							bu9.setAlpha(0);
						}else if(ddy>wh*2/3&&ddx>ww/3&&ddx<ww*2/3){
							
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_w, 0,false);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_a, 0,false);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_s, 0,true);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_d, 0,false);
							
							bu1.setAlpha(0);
							bu3.setAlpha(0);
							bu7.setAlpha(1);
							bu9.setAlpha(1);
						}
						else if(ddy>wh*2/3&&ddx>ww*2/3){
							
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_w, 0,false);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_a, 0,false);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_s, 0,true);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_d, 0,true);
							
							bu1.setAlpha(0);
							bu3.setAlpha(0);
							bu7.setAlpha(0);
							bu9.setAlpha(0);
						}
					}else if(p2.getAction()==MotionEvent.ACTION_MOVE){
					
						float ddxx=p2.getX()-button.getX();
						float ddxy=p2.getY()-button.getY();
						
						if(ddxy<wh/3&&ddxy>0&&ddxx<ww/3&&ddxx>0){
							
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_w, 0,true);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_a, 0,true);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_s, 0,false);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_d, 0,false);
							

						}else if(ddxy<wh/3&&ddxy>0&&ddxx>ww/3&&ddxx<ww*2/3&&ddxx>0){
							
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_w, 0,true);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_a, 0,false);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_s, 0,false);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_d, 0,false);
							
							bu1.setAlpha(1);
							bu3.setAlpha(1);
							bu7.setAlpha(0);
							bu9.setAlpha(0);
				
							
						}
						else if(ddxy<wh/3&&ddxy>0&&ddxx>0&&ddxx>ww*2/3){
							
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_w, 0,true);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_a, 0,false);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_s, 0,false);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_d, 0,true);
		
						}
						else if(ddxy>wh/3&&ddxy<wh*2/3&&ddxx<ww/3){
							
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_w, 0,false);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_a, 0,true);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_s, 0,false);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_d, 0,false);

							bu1.setAlpha(1);
							bu3.setAlpha(0);
							bu7.setAlpha(1);
							bu9.setAlpha(0);
							
							
						}else if(ddxy>wh/3&&ddxy<wh*2/3&&ddxx>ww/3&&ddxx<ww*2/3){
	
							
							
						}
						else if(ddxy<wh/3&&ddxy>wh*2/3&&ddxx<ww/3&&ddxx>ww*2/3){
	
							
							
						}
						else if(ddxy>wh/3&&ddxy<wh*2/3&&ddxx>ww*2/3){
							
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_w, 0,false);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_a, 0,false);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_s, 0,false);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_d, 0,true);
							

							bu1.setAlpha(0);
							bu3.setAlpha(1);
							bu7.setAlpha(0);
							bu9.setAlpha(1);

							
						}
						else if(ddxy>wh*2/3&&ddxx<ww/3){
							
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_w, 0,false);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_a, 0,true);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_s, 0,true);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_d, 0,false);
		
						}else if(ddxy>wh*2/3&&ddxx>ww/3&&ddxx<ww*2/3){
							
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_w, 0,false);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_a, 0,false);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_s, 0,true);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_d, 0,false);
				
							bu1.setAlpha(0);
							bu3.setAlpha(0);
							bu7.setAlpha(1);
							bu9.setAlpha(1);
							
							
						}
						else if(ddxy>wh*2/3&&ddxx>ww*2/3){
							
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_w, 0,false);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_a, 0,false);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_s, 0,true);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_d, 0,true);
				
							
						}
						
						
					}else if(p2.getAction()==MotionEvent.ACTION_UP){
					    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_w, 0,false);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_a, 0,false);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_s, 0,false);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_d, 0,false);
					
						bu1.setAlpha(0);
						bu3.setAlpha(0);
						bu7.setAlpha(0);
						bu9.setAlpha(0);
						
						
						
					}
					}else{
					
					    if(p2.getAction()==MotionEvent.ACTION_DOWN){
						 ddx=p2.getX()-button.getX();
						ddy= p2.getY()-button.getY();
						 ww=button.getWidth();
						 wh=button.getHeight();
						
						if(ddy<wh/3&&ddx<ww/3){
							
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_w, 0,true);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_a, 0,true);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_s, 0,false);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_d, 0,false);
							
							bu1.setAlpha(0);
							bu3.setAlpha(0);
							bu7.setAlpha(0);
							bu9.setAlpha(0);
						}else if(ddy<wh/3&&ddx>ww/3&&ddx<ww*2/3){
							
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_w, 0,true);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_a, 0,false);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_s, 0,false);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_d, 0,false);
							
							bu1.setAlpha(1);
							bu3.setAlpha(1);
							bu7.setAlpha(0);
							bu9.setAlpha(0);
							
							
						}
						else if(ddy<wh/3&&ddx>ww*2/3){
							
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_w, 0,true);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_a, 0,false);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_s, 0,false);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_d, 0,true);
							
							bu1.setAlpha(0);
							bu3.setAlpha(0);
							bu7.setAlpha(0);
							bu9.setAlpha(0);
							
						}
						else if(ddy>wh/3&&ddy<wh*2/3&&ddx<ww/3){
							
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_w, 0,false);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_a, 0,true);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_s, 0,false);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_d, 0,false);
							
							bu1.setAlpha(1);
							bu3.setAlpha(0);
							bu7.setAlpha(1);
							bu9.setAlpha(0);
							
							
							
							
						}else if(ddy>wh/3&&ddy<wh*2/3&&ddx>ww/3&&ddx<ww*2/3){
							
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_w, 0,false);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_a, 0,false);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_s, 0,false);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_d, 0,false);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_space, 0, true);
						    
							bu1.setAlpha(0);
							bu3.setAlpha(0);
							bu7.setAlpha(0);
							bu9.setAlpha(0);
							
							
						}
						else if(ddy>wh/3&&ddy<wh*2/3&&ddx>ww*2/3){
							
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_w, 0,false);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_a, 0,false);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_s, 0,false);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_d, 0,true);
							
							bu1.setAlpha(0);
							bu3.setAlpha(1);
							bu7.setAlpha(0);
							bu9.setAlpha(1);
							
							
							
						}
						else if(ddy>wh*2/3&&ddx<ww/3){
							
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_w, 0,false);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_a, 0,true);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_s, 0,true);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_d, 0,false);
							
							bu1.setAlpha(0);
							bu3.setAlpha(0);
							bu7.setAlpha(0);
							bu9.setAlpha(0);
						}else if(ddy>wh*2/3&&ddx>ww/3&&ddx<ww*2/3){
							
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_w, 0,false);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_a, 0,false);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_s, 0,true);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_d, 0,false);
							
							bu1.setAlpha(0);
							bu3.setAlpha(0);
							bu7.setAlpha(1);
							bu9.setAlpha(1);
						}
						else if(ddy>wh*2/3&&ddx>ww*2/3){
							
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_w, 0,false);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_a, 0,false);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_s, 0,true);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_d, 0,true);
							
							bu1.setAlpha(0);
							bu3.setAlpha(0);
							bu7.setAlpha(0);
							bu9.setAlpha(0);
						}
					}else if(p2.getAction()==MotionEvent.ACTION_MOVE){
					
						float ddxx=p2.getX()-button.getX();
						float ddxy=p2.getY()-button.getY();
						
						if(ddxy<wh/3&&ddxy>0&&ddxx<ww/3&&ddxx>0){
							
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_w, 0,true);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_a, 0,true);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_s, 0,false);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_d, 0,false);
							

						}else if(ddxy<wh/3&&ddxy>0&&ddxx>ww/3&&ddxx<ww*2/3&&ddxx>0){
							
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_w, 0,true);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_a, 0,false);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_s, 0,false);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_d, 0,false);
							
							bu1.setAlpha(1);
							bu3.setAlpha(1);
							bu7.setAlpha(0);
							bu9.setAlpha(0);
				
							
						}
						else if(ddxy<wh/3&&ddxy>0&&ddxx>0&&ddxx>ww*2/3){
							
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_w, 0,true);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_a, 0,false);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_s, 0,false);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_d, 0,true);
		
						}
						else if(ddxy>wh/3&&ddxy<wh*2/3&&ddxx<ww/3){
							
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_w, 0,false);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_a, 0,true);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_s, 0,false);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_d, 0,false);

							bu1.setAlpha(1);
							bu3.setAlpha(0);
							bu7.setAlpha(1);
							bu9.setAlpha(0);
							
							
						}else if(ddxy>wh/3&&ddxy<wh*2/3&&ddxx>ww/3&&ddxx<ww*2/3){
	
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_space, 0, true);
							
						}
						else if(ddxy<wh/3&&ddxy>wh*2/3&&ddxx<ww/3&&ddxx>ww*2/3){
	
							
							
						}
						else if(ddxy>wh/3&&ddxy<wh*2/3&&ddxx>ww*2/3){
							
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_w, 0,false);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_a, 0,false);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_s, 0,false);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_d, 0,true);
							

							bu1.setAlpha(0);
							bu3.setAlpha(1);
							bu7.setAlpha(0);
							bu9.setAlpha(1);

							
						}
						else if(ddxy>wh*2/3&&ddxx<ww/3){
							
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_w, 0,false);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_a, 0,true);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_s, 0,true);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_d, 0,false);
		
						}else if(ddxy>wh*2/3&&ddxx>ww/3&&ddxx<ww*2/3){
							
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_w, 0,false);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_a, 0,false);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_s, 0,true);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_d, 0,false);
				
							bu1.setAlpha(0);
							bu3.setAlpha(0);
							bu7.setAlpha(1);
							bu9.setAlpha(1);
							
							
						}
						else if(ddxy>wh*2/3&&ddxx>ww*2/3){
							
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_w, 0,false);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_a, 0,false);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_s, 0,true);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_d, 0,true);
				
							
						}
						
						
					}else if(p2.getAction()==MotionEvent.ACTION_UP){
					    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_w, 0,false);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_a, 0,false);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_s, 0,false);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_d, 0,false);
							BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_space, 0, false);
					
						bu1.setAlpha(0);
						bu3.setAlpha(0);
						bu7.setAlpha(0);
						bu9.setAlpha(0);
						
						
					
					}
					// TODO: Implement this method
					
				}
				return false;
				}
			});
		
		itemBar = (LinearLayout)base.findViewById(R.id.item_bar);
		mousePrimary = this.findButton(R.id.mouse_primary);
		mouseSecondary = this.findButton(R.id.mouse_secondary);
		mouseThird = this.findButton(R.id.mouse_third);
		mousePgup = this.findButton(R.id.mouse_pgup);
		mousePgdn = this.findButton(R.id.mouse_pgdn);
		esc = this.findButton(R.id.esc);
		controlChat = this.findButton(R.id.control_chat);
		controlCommand = this.findButton(R.id.control_command);
		control3rd = this.findButton(R.id.control_3rd);
		inputScanner = (EditText)base.findViewById(R.id.input_scanner);
		loc = (TextView)base.findViewById(R.id.loc);
		inputScanner.setFocusable(true);
		inputScanner.addTextChangedListener(this);
		inputScanner.setOnEditorActionListener(this);
		inputScanner.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI|EditorInfo.IME_FLAG_NO_FULLSCREEN|EditorInfo.IME_ACTION_DONE);
		inputScanner.setSelection(1);	
		
		int height = getWindowManager().getDefaultDisplay().getHeight();
		int width = getWindowManager().getDefaultDisplay().getWidth();	
		int scale = 1;	
		while(width / (scale + 1) >= 320 && height / (scale + 1) >= 240) {
			scale++;
		}	
		RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)itemBar.getLayoutParams();
		lp.height = 20 * scale;
		lp.width = 20 * scale * 9;
		itemBar.setLayoutParams(lp);
		
		//popupWindow.setContentView(base);
		
		mHandler = new MyHandler();
		
		
	}
	
	private String cp3() {
        try {
            FileInputStream in=new FileInputStream("/sdcard/games/com.koishi.launcher/h2o2/h2ocfg.json");
            byte[] b=new byte[in.available()];
            in.read(b);
            in.close();
            String str=new String(b);
            JSONObject json=new JSONObject(str);
            return json.getString("jumpToLeft");
        } catch (Exception e) {
            System.out.println(e);
        }
        return "Error";
    }
	
	private String cp4() {
        try {
            FileInputStream in=new FileInputStream("/sdcard/games/com.koishi.launcher/h2o2/h2ocfg.json");
            byte[] b=new byte[in.available()];
            in.read(b);
            in.close();
            String str=new String(b);
            JSONObject json=new JSONObject(str);
            return json.getString("dontEsc");
        } catch (Exception e) {
            System.out.println(e);
        }
        return "Error";
    }
	
	@Override
	protected void onPause()
	{
		// TODO: Implement this method
		super.onPause();
		if (cursorMode == BoatInput.CursorDisabled){
		if (noesc.equals("false")){
		BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Escape, 0, true);
		}else{
		}
		}else{
		}
		//popupWindow.dismiss();
	}
	
    @Override
	public void onWindowFocusChanged(boolean hasFocus)
	{
		// TODO: Implement this method
		super.onWindowFocusChanged(hasFocus);
		/*
		if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
				| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
		//button.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);  
		
		
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			alpha=true;
		}else{
			alpha=false;
			
		}
		*/
	}
    
	@Override
	public void onBackPressed()
	{
		BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Escape, 0, true);
		// TODO: Implement this method
	}
	
	
	public static native void setBoatNativeWindow(Surface surface);
	static {
		System.loadLibrary("boat");
		
	}
	
	public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {

		// TODO: Implement this method
		System.out.println("SurfaceTexture is available!");
		BoatActivity.setBoatNativeWindow(new Surface(surface));

		new Thread(){
			@Override
			public void run(){

				LauncherConfig config = LauncherConfig.fromFile(getIntent().getExtras().getString("config"));
				LoadMe.exec(config);		
				Message msg = new Message();
				msg.what = -1;
				mHandler.sendMessage(msg);
			}
		}.start();
	}

	@Override
	public void onSurfaceTextureSizeChanged(SurfaceTexture p1, int p2, int p3)
	{
		// TODO: Implement this method
	}

	@Override
	public boolean onSurfaceTextureDestroyed(SurfaceTexture p1)
	{
		// TODO: Implement this method
		return false;
	}

	@Override
	public void onSurfaceTextureUpdated(SurfaceTexture p1)
	{
		// TODO: Implement this method
	}
	
	public void setCursorMode(int mode){
		
		Message msg=new Message();
		msg.what = mode;
		mHandler.sendMessage(msg);
	}
	
	public void setCursorPos(int x, int y){

		Message msg = new Message();
		msg.what = BoatInput.CursorSetPos;
		msg.arg1 = x;
		msg.arg2 = y;
		mHandler.sendMessage(msg);
	}

	//private boolean overlayCreated = false;
	//private PopupWindow popupWindow;
	//private RelativeLayout base;
	private PopupWindow popupWindow;
	private RelativeLayout base;
	private Button touchPad;
	private Button controlUp;
	private Button controlDown;
	private Button controlLeft;
	private Button controlRight;
	private Button controlJump;
	private Button controlInv;
	private Button controlLshift;
	private Button controlLctrl;
	private Button controlBs;
//	private Button control0;
	private Button control1;
	private Button control2;
	private Button control3;
	private Button control4;
	private Button control5;
	private Button control6;
	private Button control7;
	private Button control8;
	private Button control9;
	private LinearLayout itemBar;
	private Button mousePrimary;
	private Button mouseSecondary;
	private Button mouseThird;
	private Button mousePgup;
	private Button mousePgdn;
	private Button controlChat;
	private Button controlCommand;
	private Button control3rd;
	private ImageView mouseCursor;
	private Button esc;

    private EditText input;
    private Button send;

    private Button touchPad2;
    private Button fwc;

    private Button controlDebug;
    private Button controlThrow;
    private Button controlSwitch;

	private Button allkeyon;
	private Button allkeyoff;
	
	private LinearLayout allkey;
	
	private Button keb_a;
	private Button keb_b;
	private Button keb_c;
	private Button keb_d;
	private Button keb_e;
	private Button keb_f;
	private Button keb_g;
	private Button keb_h;
	private Button keb_i;
	private Button keb_j;
	private Button keb_k;
	private Button keb_l;
	private Button keb_m;
	private Button keb_n;
	private Button keb_o;
	private Button keb_p;
	private Button keb_q;
	private Button keb_r;
	private Button keb_s;
	private Button keb_t;
	private Button keb_u;
	private Button keb_v;
	private Button keb_w;
	private Button keb_x;
	private Button keb_y;
	private Button keb_z;
	
	private Button keb_f1;
	private Button keb_f2;
	private Button keb_f3;
	private Button keb_f4;
	private Button keb_f5;
	private Button keb_f6;
	private Button keb_f7;
	private Button keb_f8;
	private Button keb_f9;
	private Button keb_f10;
	private Button keb_f11;
	private Button keb_f12;
	
	private Button keb_1;
	private Button keb_2;
	private Button keb_3;
	private Button keb_4;
	private Button keb_5;
	private Button keb_6;
	private Button keb_7;
	private Button keb_8;
	private Button keb_9;
	private Button keb_0;
	
	private Button keb_grave;
	private Button keb_tab;
	private Button keb_caps;
	private Button keb_lshift;
	private Button keb_lctrl;
	private Button keb_lalt;
	private Button keb_lwin;
	
	private Button keb_pause;
	private Button keb_insert;
	private Button keb_delete;
	private Button keb_home;
	
	private Button keb_up;
	private Button keb_down;
	private Button keb_left;
	private Button keb_right;
	
	private Button keb_minus;
	private Button keb_equal;
	private Button keb_backspace;
	private Button keb_lbracket;
	private Button keb_rbracket;
	private Button keb_semicolon;
	private Button keb_apostrophe;
	private Button keb_comma;
	private Button keb_period;
	private Button keb_slash;
	private Button keb_backslash;
	
	private Button keb_return;
	private Button keb_rshift;
	private Button keb_ralt;
	private Button keb_rctrl;
	private Button keb_rwin;
	private Button keb_app;
	
	private Button show;
	private Button hide;
	
	private TextView loc;
	
	private TextureView mainTextureView;
	private SurfaceView mainSurfaceView;
	
	private EditText inputScanner;
	
	
	
	private Button findButton(int id){
		Button b = (Button)findViewById(id);
		b.setOnTouchListener(this);
		return b;
	}
	
	public int cursorMode = BoatInput.CursorEnabled;
	
	private class MyHandler extends Handler{
		@Override
		public void handleMessage(Message msg)
		{

			switch (msg.what)
			{
				case BoatInput.CursorDisabled:
					BoatActivity.this.mouseCursor.setVisibility(View.INVISIBLE);
					BoatActivity.this.itemBar.setVisibility(View.VISIBLE);
					BoatActivity.this.cursorMode = BoatInput.CursorDisabled;
					BoatActivity.this.touchPad.setVisibility(View.INVISIBLE);
					//BoatActivity.this.touchPad2.setVisibility(View.VISIBLE);
					BoatActivity.this.fwc.setVisibility(View.VISIBLE);
					
					BoatActivity.this.control1.setVisibility(View.VISIBLE);
					BoatActivity.this.control2.setVisibility(View.VISIBLE);
					BoatActivity.this.control3.setVisibility(View.VISIBLE);
					BoatActivity.this.control4.setVisibility(View.VISIBLE);
					BoatActivity.this.control5.setVisibility(View.VISIBLE);
					BoatActivity.this.control6.setVisibility(View.VISIBLE);
					BoatActivity.this.control7.setVisibility(View.VISIBLE);
					BoatActivity.this.control8.setVisibility(View.VISIBLE);
					BoatActivity.this.control9.setVisibility(View.VISIBLE);
					BoatActivity.this.controlInv.setVisibility(View.VISIBLE);
					BoatActivity.this.controlThrow.setVisibility(View.VISIBLE);
					BoatActivity.this.controlSwitch.setVisibility(View.VISIBLE);
					break;
				
				case BoatInput.CursorEnabled:
					BoatActivity.this.mouseCursor.setVisibility(View.VISIBLE);
					BoatActivity.this.itemBar.setVisibility(View.INVISIBLE);
					BoatActivity.this.cursorMode = BoatInput.CursorEnabled;
					BoatActivity.this.touchPad.setVisibility(View.INVISIBLE);
					//BoatActivity.this.touchPad2.setVisibility(View.INVISIBLE);
					BoatActivity.this.fwc.setVisibility(View.INVISIBLE);
					
					BoatActivity.this.control1.setVisibility(View.INVISIBLE);
					BoatActivity.this.control2.setVisibility(View.INVISIBLE);
					BoatActivity.this.control3.setVisibility(View.INVISIBLE);
					BoatActivity.this.control4.setVisibility(View.INVISIBLE);
					BoatActivity.this.control5.setVisibility(View.INVISIBLE);
					BoatActivity.this.control6.setVisibility(View.INVISIBLE);
					BoatActivity.this.control7.setVisibility(View.INVISIBLE);
					BoatActivity.this.control8.setVisibility(View.INVISIBLE);
					BoatActivity.this.control9.setVisibility(View.INVISIBLE);
					BoatActivity.this.controlInv.setVisibility(View.INVISIBLE);
					BoatActivity.this.controlThrow.setVisibility(View.INVISIBLE);
					BoatActivity.this.controlSwitch.setVisibility(View.INVISIBLE);
					
					break;
				
				case BoatInput.CursorSetPos:
					BoatActivity.this.mouseCursor.setX((float)msg.arg1);
					BoatActivity.this.mouseCursor.setY((float)msg.arg2);
					break;
				
				default:
				    //Intent intent1=new Intent(BoatActivity.this,MainActivity.class);
					//startActivity(intent1);
				    BoatActivity.this.finish();
				    break;
			}
		}
	}
	
	private MyHandler mHandler;
	

	private int initialX;
	private int initialY;
	private int baseX;
	private int baseY;
	
	
	
	@Override
	public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4)
	{
		// TODO: Implement this method
	}

	@Override
	public void onTextChanged(CharSequence p1, int p2, int p3, int p4)
	{
		// TODO: Implement this method
	}

	@Override
	public void afterTextChanged(Editable p1)
	{
		// TODO: Implement this method
		String newText = p1.toString();
		if (newText.length() < 1){
			
			BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_BackSpace, 0, true);
			BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_BackSpace, 0, false);
			inputScanner.setText(">");
			inputScanner.setSelection(1);
		}
		if (newText.length() > 1){
			for(int i = 1; i < newText.length(); i++){
				BoatInput.setKey(0, newText.charAt(i), true);
				BoatInput.setKey(0, newText.charAt(i), false);
			}
			
			inputScanner.setText(">");
			inputScanner.setSelection(1);
		}
	}
	
	@Override
	public boolean onEditorAction(TextView p1, int p2, KeyEvent p3)
	{
		// TODO: Implement this method
		
		BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Return, '\n', true);
		BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Return, '\n', false);
        return false;  
	}
	
	private void overlayshow()
	{
		//this.controlUp.setVisibility(View.VISIBLE);
		//this.controlDown.setVisibility(View.VISIBLE);
		//this.controlLeft.setVisibility(View.VISIBLE);
		//this.controlRight.setVisibility(View.VISIBLE);
		this.controlJump.setVisibility(View.VISIBLE);
		this.controlLshift.setVisibility(View.VISIBLE);
		this.controlLctrl.setVisibility(View.VISIBLE);
		this.mousePrimary.setVisibility(View.VISIBLE);
		this.mouseSecondary.setVisibility(View.VISIBLE);
		this.mouseThird.setVisibility(View.VISIBLE);
		this.mousePgup.setVisibility(View.VISIBLE);
		this.mousePgdn.setVisibility(View.VISIBLE);
		this.controlBs.setVisibility(View.VISIBLE);
		this.control3rd.setVisibility(View.VISIBLE);
		this.controlChat.setVisibility(View.VISIBLE);
		this.controlCommand.setVisibility(View.VISIBLE);
		this.esc.setVisibility(View.VISIBLE);
		this.button.setVisibility(View.VISIBLE);
		this.controlDebug.setVisibility(View.VISIBLE);
		this.inputScanner.setVisibility(View.VISIBLE);
		this.input.setVisibility(View.VISIBLE);
		this.send.setVisibility(View.VISIBLE);
		this.allkeyon.setVisibility(View.VISIBLE);
		this.show.setVisibility(View.INVISIBLE);
		this.hide.setVisibility(View.VISIBLE);
		
		this.bu2.setVisibility(View.VISIBLE);
		this.bu4.setVisibility(View.VISIBLE);
		this.bu5.setVisibility(View.VISIBLE);
		this.bu6.setVisibility(View.VISIBLE);
		this.bu8.setVisibility(View.VISIBLE);
		this.cv1.setVisibility(View.VISIBLE);
		
		if (cursorMode == BoatInput.CursorEnabled){
			this.controlInv.setVisibility(View.INVISIBLE);
			this.controlSwitch.setVisibility(View.INVISIBLE);
			this.controlThrow.setVisibility(View.INVISIBLE);
		}else{
			this.controlInv.setVisibility(View.VISIBLE);
			this.controlSwitch.setVisibility(View.VISIBLE);
			this.controlThrow.setVisibility(View.VISIBLE);
		}
	}

	private void overlayhide()
	{
		//this.controlUp.setVisibility(View.INVISIBLE);
		//this.controlDown.setVisibility(View.INVISIBLE);
		//this.controlLeft.setVisibility(View.INVISIBLE);
		//this.controlRight.setVisibility(View.INVISIBLE);
		this.controlJump.setVisibility(View.INVISIBLE);
		this.controlLshift.setVisibility(View.INVISIBLE);
		this.controlLctrl.setVisibility(View.INVISIBLE);
		this.controlInv.setVisibility(View.INVISIBLE);
		this.mousePrimary.setVisibility(View.INVISIBLE);
		this.mouseSecondary.setVisibility(View.INVISIBLE);
		this.mouseThird.setVisibility(View.INVISIBLE);
		this.mousePgup.setVisibility(View.INVISIBLE);
		this.mousePgdn.setVisibility(View.INVISIBLE);
		this.controlBs.setVisibility(View.INVISIBLE);
		this.control3rd.setVisibility(View.INVISIBLE);
		this.controlChat.setVisibility(View.INVISIBLE);
		this.controlCommand.setVisibility(View.INVISIBLE);
		this.esc.setVisibility(View.INVISIBLE);
		this.button.setVisibility(View.INVISIBLE);
		this.controlDebug.setVisibility(View.INVISIBLE);
		this.controlSwitch.setVisibility(View.INVISIBLE);
		this.controlThrow.setVisibility(View.INVISIBLE);
		this.inputScanner.setVisibility(View.INVISIBLE);
		this.input.setVisibility(View.INVISIBLE);
		this.send.setVisibility(View.INVISIBLE);
		this.allkeyon.setVisibility(View.INVISIBLE);
		this.allkeyoff.setVisibility(View.INVISIBLE);
		this.show.setVisibility(View.VISIBLE);
		this.hide.setVisibility(View.INVISIBLE);
		this.allkey.setVisibility(View.GONE);
		
		this.bu2.setVisibility(View.GONE);
		this.bu4.setVisibility(View.GONE);
		this.bu5.setVisibility(View.GONE);
		this.bu6.setVisibility(View.GONE);
		this.bu8.setVisibility(View.GONE);
		this.cv1.setVisibility(View.GONE);
	}

	private void allkeyon()
	{
		this.allkeyon.setVisibility(View.INVISIBLE);
		this.allkeyoff.setVisibility(View.VISIBLE);
		this.allkey.setVisibility(View.VISIBLE);
		
		//this.controlUp.setVisibility(View.INVISIBLE);
		//this.controlDown.setVisibility(View.INVISIBLE);
		//this.controlLeft.setVisibility(View.INVISIBLE);
		//this.controlRight.setVisibility(View.INVISIBLE);
		//this.controlLshift.setVisibility(View.INVISIBLE);
		this.bu2.setVisibility(View.GONE);
		this.bu4.setVisibility(View.GONE);
		this.bu5.setVisibility(View.GONE);
		this.bu6.setVisibility(View.GONE);
		this.bu8.setVisibility(View.GONE);
		this.control3rd.setVisibility(View.INVISIBLE);
		this.button.setVisibility(View.INVISIBLE);
		this.controlChat.setVisibility(View.INVISIBLE);
		this.controlCommand.setVisibility(View.INVISIBLE);
		this.controlDebug.setVisibility(View.INVISIBLE);
		this.inputScanner.setVisibility(View.INVISIBLE);
		this.cv1.setVisibility(View.GONE);

	
	}

	private void allkeyoff()
	{
		this.allkeyon.setVisibility(View.VISIBLE);
		this.allkeyoff.setVisibility(View.INVISIBLE);
		this.allkey.setVisibility(View.GONE);
		
		//this.controlUp.setVisibility(View.VISIBLE);
		//this.controlDown.setVisibility(View.VISIBLE);
		//this.controlLeft.setVisibility(View.VISIBLE);
		//this.controlRight.setVisibility(View.VISIBLE);
		//this.controlLshift.setVisibility(View.VISIBLE);
		this.control3rd.setVisibility(View.VISIBLE);
		this.button.setVisibility(View.VISIBLE);
		this.bu2.setVisibility(View.VISIBLE);
		this.bu4.setVisibility(View.VISIBLE);
		this.bu5.setVisibility(View.VISIBLE);
		this.bu6.setVisibility(View.VISIBLE);
		this.bu8.setVisibility(View.VISIBLE);
		this.controlChat.setVisibility(View.VISIBLE);
		this.controlCommand.setVisibility(View.VISIBLE);
		this.controlDebug.setVisibility(View.VISIBLE);
		this.inputScanner.setVisibility(View.VISIBLE);
		
		this.bu2.setVisibility(View.VISIBLE);
		this.bu4.setVisibility(View.VISIBLE);
		this.bu5.setVisibility(View.VISIBLE);
		this.bu6.setVisibility(View.VISIBLE);
		this.bu8.setVisibility(View.VISIBLE);
		this.cv1.setVisibility(View.VISIBLE);
	}
	
	
	@Override
	public void onClick(View p1)
	{
		// TODO: Implement this method
		if (p1 == inputScanner){
			inputScanner.setSelection(1);
		}
	}
	
	@Override
	public boolean onTouch(View p1, MotionEvent p2)
	{
		
	if (p1 == inputScanner){
			inputScanner.setSelection(1);
			return false;

		}
		
		if (p1 == mousePrimary){
			if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setMouseButton(BoatInput.Button1, true);

			}
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setMouseButton(BoatInput.Button1, false);

			}
			
			if (cursorMode == BoatInput.CursorDisabled){
				switch(p2.getActionMasked()){
					case MotionEvent.ACTION_DOWN:
						initialX = (int)p2.getX();
						initialY = (int)p2.getY();
					case MotionEvent.ACTION_MOVE:
						BoatInput.setPointer(baseX + (int)p2.getX() -initialX, baseY + (int)p2.getY() - initialY);
						break;
					case MotionEvent.ACTION_UP:
						baseX += ((int)p2.getX() - initialX);
						baseY += ((int)p2.getY() - initialY);
						
						BoatInput.setPointer(baseX, baseY);
						break;
					default:
						break;
				}
			}
			return false;
			
		}
		
		if (p1 == mouseSecondary){
			if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setMouseButton(BoatInput.Button3, true);

			}
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setMouseButton(BoatInput.Button3, false);

			}
			if (cursorMode == BoatInput.CursorDisabled){
				switch(p2.getActionMasked()){
					case MotionEvent.ACTION_DOWN:
						initialX = (int)p2.getX();
						initialY = (int)p2.getY();
					case MotionEvent.ACTION_MOVE:
						BoatInput.setPointer(baseX + (int)p2.getX() -initialX, baseY + (int)p2.getY() - initialY);
						break;
					case MotionEvent.ACTION_UP:
						baseX += ((int)p2.getX() - initialX);
						baseY += ((int)p2.getY() - initialY);
						
						BoatInput.setPointer(baseX, baseY);
						break;
					default:
						break;
				}
			}
			return false;
		}
		
		if (p1 == mouseThird){
			if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setMouseButton(BoatInput.Button2, true);

			}
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setMouseButton(BoatInput.Button2, false);

			}
			return false;
		}
		
		if (p1 == mousePgup){
		if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
			timer=new Timer();
							timer.schedule(new TimerTask() {
									@Override
									public void run() {
										BoatInput.setMouseButton(BoatInput.Button4, false);
										BoatInput.setMouseButton(BoatInput.Button4, true);
									}
								},0,100);
		}
		
		if (p2.getActionMasked() == MotionEvent.ACTION_UP){
		timer.cancel();
		BoatInput.setMouseButton(BoatInput.Button4, false);
		}
			return false;
		}
		
		if (p1 == mousePgdn){
		if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
		 timer2=new Timer();
							timer2.schedule(new TimerTask() {
									@Override
									public void run() {
										BoatInput.setMouseButton(BoatInput.Button5, false);
										BoatInput.setMouseButton(BoatInput.Button5, true);
									}
								},0,100);
		}
		
		if (p2.getActionMasked() == MotionEvent.ACTION_UP){
		timer2.cancel();
		BoatInput.setMouseButton(BoatInput.Button5, false);
		}
			return false;
		}
		
		if (p1 == controlChat){
			
			if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_t, 0, true);

			}
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_t, 0, false);

			}
			
			return false;
		}
		if (p1 == input){
			
			if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_t, 0, true);

			}
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_t, 0, false);

			}
			
			return false;
		}
		if (p1 == controlCommand){

			if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_slash, 0, true);
			}
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_slash, 0, false);
			}

			return false;
		}
		if (p1 == controlBs){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_BackSpace, 0, true);

			}
			if (p2.getActionMasked() == MotionEvent.ACTION_MOVE){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_BackSpace, 0, true);

			}
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_BackSpace, 0, false);

			}

			return false;		
		}
		if (p1 == control3rd){

			if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F5, 0, true);

			}
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F5, 0, false);

			}

			return false;
		}

        if (p1 == controlDebug){

			if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F3, 0, true);

			}
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F3, 0, false);

			}

			return false;
		}
		if (p1 == controlThrow){

			if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_q, 0, true);

			}
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_q, 0, false);

			}

			return false;
		}
		
		if (p1 == controlSwitch){

			if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_f, 0, true);

			}
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_f, 0, false);

			}

			return false;
		}
		
		if (p1 == keb_a){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_a, 0, true);

			}
		
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_a, 0, false);

			}

			return false;		
		}
		if (p1 == keb_b){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_b, 0, true);

			}
	
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_b, 0, false);

			}

			return false;		
		}
		if (p1 == keb_c){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_c, 0, true);

			}
		
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_c, 0, false);

			}

			return false;		
		}
		if (p1 == keb_d){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_d, 0, true);

			}
		
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_d, 0, false);

			}

			return false;		
		}
		if (p1 == keb_e){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_e, 0, true);

			}
		
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_e, 0, false);

			}

			return false;		
		}
		if (p1 == keb_f){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_f, 0, true);

			}
		
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_f, 0, false);

			}

			return false;		
		}
		if (p1 == keb_g){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_g, 0, true);

			}
		
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_g, 0, false);

			}

			return false;		
		}
		if (p1 == keb_h){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_h, 0, true);

			}
		
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_h, 0, false);

			}

			return false;		
		}
		if (p1 == keb_i){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_i, 0, true);

			}
		
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_i, 0, false);

			}

			return false;		
		}
		if (p1 == keb_j){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_j, 0, true);

			}
		
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_j, 0, false);

			}

			return false;		
		}
		if (p1 == keb_k){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_k, 0, true);

			}
		
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_k, 0, false);

			}

			return false;		
		}
		if (p1 == keb_l){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_l, 0, true);

			}
		
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_l, 0, false);

			}

			return false;		
		}
		if (p1 == keb_m){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_m, 0, true);

			}
		
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_m, 0, false);

			}

			return false;		
		}
		if (p1 == keb_n){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_n, 0, true);

			}
		
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_n, 0, false);

			}

			return false;		
		}
		if (p1 == keb_o){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_o, 0, true);

			}
		
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_o, 0, false);

			}

			return false;		
		}
		if (p1 == keb_p){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_p, 0, true);

			}
		
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_p, 0, false);

			}

			return false;		
		}
		if (p1 == keb_q){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_q, 0, true);

			}
		
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_q, 0, false);

			}

			return false;		
		}
		if (p1 == keb_r){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_r, 0, true);

			}
			
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_r, 0, false);

			}

			return false;		
		}
		if (p1 == keb_s){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_s, 0, true);

			}
			
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_s, 0, false);

			}

			return false;		
		}
		if (p1 == keb_t){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_t, 0, true);

			}
	
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_t, 0, false);

			}

			return false;		
		}
		if (p1 == keb_u){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_u, 0, true);

			}
		
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_u, 0, false);

			}

			return false;		
		}
		if (p1 == keb_v){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_v, 0, true);

			}
		
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_v, 0, false);

			}

			return false;		
		}
		if (p1 == keb_w){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_w, 0, true);

			}
		
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_w, 0, false);

			}

			return false;		
		}
		if (p1 == keb_x){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_x, 0, true);

			}
		
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_x, 0, false);

			}

			return false;		
		}
		if (p1 == keb_y){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_y, 0, true);

			}
		
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_y, 0, false);

			}

			return false;		
		}
		if (p1 == keb_z){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_z, 0, true);

			}
		
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_z, 0, false);

			}

			return false;		
		}
		
		if (p1 == keb_f1){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F1, 0, true);

			}
		
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F1, 0, false);

			}

			return false;		
		}
		if (p1 == keb_f2){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F2, 0, true);

			}
			
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F2, 0, false);

			}

			return false;		
		}
		if (p1 == keb_f3){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F3, 0, true);

			}
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F3, 0, false);

			}

			return false;		
		}
		if (p1 == keb_f4){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F4, 0, true);

			}
		
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F4, 0, false);

			}

			return false;		
		}
		if (p1 == keb_f5){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F5, 0, true);

			}
			
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F5, 0, false);

			}

			return false;		
		}
		if (p1 == keb_f6){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F6, 0, true);

			}
			
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F6, 0, false);

			}

			return false;		
		}
		if (p1 == keb_f7){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F7, 0, true);

			}
	
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F7, 0, false);

			}

			return false;		
		}
		if (p1 == keb_f8){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F8, 0, true);

			}
		
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F8, 0, false);

			}

			return false;		
		}
		if (p1 == keb_f9){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F9, 0, true);

			}
		
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F9, 0, false);

			}

			return false;		
		}
		if (p1 == keb_f10){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F10, 0, true);

			}
		
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F10, 0, false);

			}

			return false;		
		}
		if (p1 == keb_f11){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F11, 0, true);

			}
		
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F11, 0, false);

			}

			return false;		
		}
		if (p1 == keb_f12){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F12, 0, true);

			}
		
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F12, 0, false);

			}

			return false;		
		}
		if (p1 == keb_1){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_1, 0, true);

			}
		
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_1, 0, false);

			}

			return false;		
		}
		if (p1 == keb_2){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_2, 0, true);

			}
		
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_2, 0, false);

			}

			return false;		
		}
		if (p1 == keb_3){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_3, 0, true);

			}
		
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_3, 0, false);

			}

			return false;		
		}
		if (p1 == keb_4){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_4, 0, true);

			}
		
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_4, 0, false);

			}

			return false;		
		}
		if (p1 == keb_5){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_5, 0, true);

			}
		
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_5, 0, false);

			}

			return false;		
		}
		if (p1 == keb_6){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_6, 0, true);

			}
		
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_6, 0, false);

			}

			return false;		
		}
		if (p1 == keb_7){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_7, 0, true);

			}
		
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_7, 0, false);

			}

			return false;		
		}
		if (p1 == keb_8){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_8, 0, true);

			}
		
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_8, 0, false);

			}

			return false;		
		}
		if (p1 == keb_9){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_9, 0, true);

			}
		
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_9, 0, false);

			}

			return false;		
		}
		if (p1 == keb_0){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_0, 0, true);

			}
			
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_0, 0, false);

			}

			return false;		
		}
		
		if (p1 == keb_grave){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_grave, 0, true);

			}
			
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_grave, 0, false);

			}

			return false;		
		}
		if (p1 == keb_tab){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Tab, 0, true);

			}
		
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Tab, 0, false);

			}

			return false;		
		}
		if (p1 == keb_caps){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Caps_Lock, 0, true);

			}
			
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Caps_Lock, 0, false);

			}

			return false;		
		}
		
		if (p1 == keb_lshift){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Shift_L, 0, true);

			}
			
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Shift_L, 0, false);

			}

			return false;		
		}
		
		if (p1 == keb_lctrl){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Control_L, 0, true);

			}
			
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Control_L, 0, false);

			}

			return false;		
		}
		if (p1 == keb_lalt){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Alt_L, 0, true);

			}
			
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Alt_L, 0, false);

			}

			return false;		
		}
		if (p1 == keb_lwin){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Meta_L, 0, true);

			}
			
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Meta_L, 0, false);

			}

			return false;		
		}
		if (p1 == keb_pause){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Pause, 0, true);

			}
			
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Pause, 0, false);

			}

			return false;		
		}
		if (p1 == keb_insert){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Insert, 0, true);

			}
			
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Insert, 0, false);

			}

			return false;		
		}
		if (p1 == keb_delete){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Delete, 0, true);

			}
			
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Delete, 0, false);

			}

			return false;		
		}
		if (p1 == keb_home){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Home, 0, true);

			}
			
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Home, 0, false);

			}

			return false;		
		}
		if (p1 == keb_up){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Up, 0, true);

			}
			
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Up, 0, false);

			}

			return false;		
		}
		if (p1 == keb_down){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Down, 0, true);

			}
			
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Down, 0, false);

			}

			return false;		
		}
		if (p1 == keb_left){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Left, 0, true);

			}
			
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Left, 0, false);

			}

			return false;		
		}
		if (p1 == keb_right){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Right, 0, true);

			}
			
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Right, 0, false);

			}

			return false;		
		}
		if (p1 == keb_minus){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_minus, 0, true);

			}
			
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_minus, 0, false);

			}

			return false;		
		}if (p1 == keb_equal){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_equal, 0, true);

			}
			
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_equal, 0, false);

			}

			return false;		
		}
		if (p1 == keb_backspace){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_BackSpace, 0, true);

			}
			
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_BackSpace, 0, false);

			}

			return false;		
		}
		if (p1 == keb_lbracket){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_bracketleft, 0, true);

			}
			
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_bracketleft, 0, false);

			}

			return false;		
		}
		if (p1 == keb_rbracket){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_bracketright, 0, true);

			}
			
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_bracketright, 0, false);

			}

			return false;		
		}
		if (p1 == keb_semicolon){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_semicolon, 0, true);

			}
			
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_semicolon, 0, false);

			}

			return false;		
		}
		if (p1 == keb_apostrophe){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_apostrophe, 0, true);

			}
			
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_apostrophe, 0, false);

			}

			return false;		
		}
		if (p1 == keb_comma){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_comma, 0, true);

			}
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_comma, 0, false);

			}

			return false;		
		}
		if (p1 == keb_period){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_period, 0, true);

			}
			
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_period, 0, false);

			}

			return false;		
		}
		if (p1 == keb_slash){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_slash, 0, true);

			}
			
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_slash, 0, false);

			}

			return false;		
		}
		if (p1 == keb_backslash){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_backslash, 0, true);

			}
			
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_backslash, 0, false);

			}

			return false;		
		}
		if (p1 == keb_return){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Return, 0, true);

			}
			
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Return, 0, false);

			}

			return false;		
		}
		if (p1 == keb_rshift){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Shift_R, 0, true);

			}
			
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Shift_R, 0, false);

			}

			return false;		
		}
		if (p1 == keb_ralt){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Alt_R, 0, true);

			}
			
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Alt_R, 0, false);

			}

			return false;		
		}
		if (p1 == keb_rctrl){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Control_R, 0, true);

			}
		
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Control_R, 0, false);

			}

			return false;		
		}
		if (p1 == keb_app){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Menu, 0, true);

			}
			
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Menu, 0, false);

			}

			return false;		
		}
		if (p1 == keb_rwin){
		
		    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Meta_R, 0, true);

			}
			
			if (p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Meta_R, 0, false);

			}

			return false;		
		}

        if (p1 == show){

		    overlayshow();
		    
			return false;		
		}		
		if (p1 == hide){
		
		    overlayhide();

			return false;		
		}
		if (p1 == allkeyon){
		
		    allkeyon();

			return false;		
		}
		if (p1 == allkeyoff){
		
		    allkeyoff();

			return false;		
		}
		
		if (p1 == control1){
			if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_1, 0, true);

			}
			else if(p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_1, 0, false);

			}
			return false;
		}
		if (p1 == control2){
			if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_2, 0, true);

			}
			else if(p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_2, 0, false);

			}
			return false;
		}
		if (p1 == control3){
			if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_3, 0, true);

			}
			else if(p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_3, 0, false);

			}
			return false;
		}
		if (p1 == control4){
			if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_4, 0, true);

			}
			else if(p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_4, 0, false);

			}
			return false;
		}
		if (p1 == control5){
			if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_5, 0, true);

			}
			else if(p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_5, 0, false);

			}
			return false;
		}
		if (p1 == control6){
			if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_6, 0, true);

			}
			else if(p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_6, 0, false);

			}
			return false;
		}
		if (p1 == control7){
			if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_7, 0, true);

			}
			else if(p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_7, 0, false);

			}
			return false;
		}
		if (p1 == control8){
			if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_8, 0, true);

			}
			else if(p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_8, 0, false);

			}
			return false;
		}
		if (p1 == control9){
			if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_9, 0, true);

			}
			else if(p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_9, 0, false);

			}
			return false;
		}
		if (p1 == controlUp){
			if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_w, 0, true);
				
			}
			else if(p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_w, 0, false);
				
			}
			return false;
		}
		if (p1 == controlInv){
			if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_e, 0, true);

			}
			else if(p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_e, 0, false);

			}
			return false;
		}
		if (p1 == controlLshift){
			if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Shift_L, 0, true);

			}
			else if(p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Shift_L, 0, false);

			}
			return false;
		}
		if (p1 == controlDown){
			if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_s, 0, true);
				
			}
			else if(p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_s, 0, false);
				
			}
			return false;
		}
		if (p1 == controlLeft){
			if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_a, 0, true);
				
			}
			else if(p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_a, 0, false);
				
			}
			return false;
		}
		if (p1 == controlRight){
			
			if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_d, 0, true);
				
			}
			else if(p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_d, 0, false);
				
			}
			return false;
		}
		if (p1 == controlJump){

			if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){			   
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_space, 0, true);				
			}
			else if(p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_space, 0, false);
				
			}

			return false;
		}
		if (p1 == esc){

			if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Escape, 0, true);

			}
			else if(p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Escape, 0, false);

			}
			return false;
		}
		
		if (p1 == send){
		if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){ 
		BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_t, 0, true);
		       
		        final EditText editText = new EditText(BoatActivity.this);
                AlertDialog.Builder dialog = new AlertDialog.Builder(BoatActivity.this);
                int margin = 80;
                int margin2 = 0;
                dialog.setTitle("Input");
		        dialog.setView(editText);
                dialog.setCancelable(true);
                dialog.setPositiveButton(getResources().getString(R.string.control_send), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        inputScanner.setText("\\"+editText.getText().toString());
                        BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Return, 0, true);
                    }
                });
                dialog.setNegativeButton(getResources().getString(R.string.control_set), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    inputScanner.setText("\\"+editText.getText().toString());
                    }
                });
                dialog.show();
				

			}
			else if(p2.getActionMasked() == MotionEvent.ACTION_UP){
				BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_t, 0, false);


			}
			return false;
		
		}
		if (p1 == touchPad){
			if (cursorMode == BoatInput.CursorDisabled){
				switch(p2.getActionMasked()){
					case MotionEvent.ACTION_DOWN:
						initialX = (int)p2.getX();
						initialY = (int)p2.getY();
					case MotionEvent.ACTION_MOVE:
						BoatInput.setPointer(baseX + (int)p2.getX() -initialX, baseY + (int)p2.getY() - initialY);
						break;
					case MotionEvent.ACTION_UP:
						baseX += ((int)p2.getX() - initialX);
						baseY += ((int)p2.getY() - initialY);
						
						BoatInput.setPointer(baseX, baseY);
						break;
					default:
						break;
				}
			}
			else if (cursorMode == BoatInput.CursorEnabled){
				baseX = (int)p2.getX();
				baseY = (int)p2.getY();
				BoatInput.setPointer(baseX, baseY);
				if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
					BoatInput.setMouseButton(BoatInput.Button1, true);

				}
				if (p2.getActionMasked() == MotionEvent.ACTION_UP){
					BoatInput.setMouseButton(BoatInput.Button1, false);
				}
			}
			
			mouseCursor.setX(p2.getX());
			mouseCursor.setY(p2.getY());
			return true;
		}
		return false;
		
	}

	
}



