package cosine.boat;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.SurfaceTexture;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.PointerIcon;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnSystemUiVisibilityChangeListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;


@RequiresApi(api = Build.VERSION_CODES.O)
public class BoatActivityMk extends Activity implements TextureView.SurfaceTextureListener, SurfaceHolder.Callback, OnTouchListener, View.OnClickListener, TextWatcher, TextView.OnEditorActionListener, View.OnCapturedPointerListener {
    public static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000;

    static {
        System.loadLibrary("boat");

    }

    private final static String TAG = BoatActivityMk.class.getSimpleName();

    public int cursorMode = BoatInput.CursorEnabled;
    private TextView text;
    private Button button;
    private int height;
    private int width;
    private RelativeLayout base;
    private TextView text2;
    private EditText inputScanner;
    private String rc, noesc;
    private boolean alpha = false;
    private boolean pri = false;
    private boolean sec = false;
    private boolean ter = false;
    private boolean shang = false;
    private boolean xia = false;
    private int posX = 0;
    private int posY = 0;
    private int initialX = 0;
    private int initialY = 0;
    private int baseX = 0;
    private int baseY = 0;
    private boolean Lock = false;
    private ImageView mouseCursor;
    private TextureView mainTextureView;
    private MyHandler mHandler;

    View controllerView;

    public static native void setBoatNativeWindow(Surface surface);

    //private String d;
    @Override
    public boolean onTouch(View p1, MotionEvent event) {

        if (p1 == button) {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_HOVER_ENTER:
                case MotionEvent.ACTION_HOVER_MOVE:
                case MotionEvent.ACTION_HOVER_EXIT:
                case MotionEvent.ACTION_MOVE:
                    //移动
                    int x = (int) event.getAxisValue(MotionEvent.AXIS_X);
                    int y = (int) event.getAxisValue(MotionEvent.AXIS_Y);


                    if (cursorMode == BoatInput.CursorDisabled) {

                        addPos(x, y);

                    } else if (cursorMode == BoatInput.CursorEnabled) {

                        setMargins(x, y);
                    }
                    break;
            }

        }

        return true;
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture p1, int p2, int p3) {
        // TODO: Implement this method
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture p1) {
        // TODO: Implement this method
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture p1) {
        // TODO: Implement this method
    }

    @Override
    public void surfaceCreated(SurfaceHolder p1) {
        // TODO: Implement this method
        System.out.println("Surface is available!");
        BoatActivityMk.setBoatNativeWindow(p1.getSurface());

        new Thread() {
            @Override
            public void run() {
                LauncherConfig config = LauncherConfig.fromFile(getIntent().getExtras().getString("config"));
                LoadMe.exec(config);
                Message msg = new Message();
                msg.what = -1;
                mHandler.sendMessage(msg);
            }
        }.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder p1, int p2, int p3, int p4) {
        // TODO: Implement this method
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder p1) {
        // TODO: Implement this method
        throw new RuntimeException("Surface is destroyed!");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        new Thread(new Runnable() {

            private java.lang.Process logcatProcess;

            @Override
            public void run() {
                logcatProcess = null;
                BufferedReader bufferedReader = null;
                try {
                    /* 获取系统logcat日志信息 */
                    logcatProcess = Runtime.getRuntime().exec(new String[]{"logcat", "ActivityManager:I *:S”l"});
                    bufferedReader = new BufferedReader(new InputStreamReader(logcatProcess.getInputStream()));

                    String line;

                    while ((line = bufferedReader.readLine()) != null) {
                        if (line.indexOf("cat=[android.intent.category.HOME]") > 0) {
                            BoatInput.setMouseButton(BoatInput.Button2, true);
                            BoatInput.setMouseButton(BoatInput.Button2, false);
                            /* 这里可以处理你对点击Home的操作哦 我这里是完全退出应用*/

                        }
                    }

                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        }).start();

        this.getWindow().setFlags(FLAG_HOMEKEY_DISPATCHED, FLAG_HOMEKEY_DISPATCHED);//关键代码
        setContentView(R.layout.overlay_mk);

        text = (TextView) findViewById(R.id.mainTextView1);
        text2 = (TextView) findViewById(R.id.mainTextView2);
        base = (RelativeLayout) findViewById(R.id.mainRelativeLayout1);
        mainTextureView = (TextureView) this.findViewById(R.id.mouse_surface);
        //mainTextureView.setSurfaceTexture(new   SurfaceHolder.Callback(){});

        rc = cp2();
        noesc = cp4();

        mainTextureView.setSurfaceTextureListener(this);
        mouseCursor = (ImageView) findViewById(R.id.mouse_cursor2);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            alpha = true;
        } else {
            alpha = false;

        }

        Resources resources = this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();

        width = dm.widthPixels;
        height = dm.heightPixels - 1;


        mHandler = new MyHandler();

        //Intent intent = this.getIntent();
        //d = intent.getStringExtra("dat");

        button = findViewById(R.id.touch_pad22);
        button.setOnTouchListener(this);
        button.setFocusable(true);

        inputScanner = base.findViewById(R.id.insc);

        inputScanner.setFocusable(true);
        inputScanner.addTextChangedListener(this);
        inputScanner.setOnEditorActionListener(this);
        inputScanner.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI | EditorInfo.IME_FLAG_NO_FULLSCREEN | EditorInfo.IME_ACTION_DONE);
        inputScanner.setSelection(1);

        //adjustPos((int)event.getAxisValue(MotionEvent.AXIS_X),(int)event.getAxisValue(MotionEvent.AXIS_Y));


        button.setOnHoverListener(new View.OnHoverListener() {

            private PointerIcon mOrgPI;

				/*@Override
				public boolean onHover(View v, MotionEvent event) {
					int what = event.getAction();


					switch(what){
						case MotionEvent.ACTION_HOVER_ENTER:  //鼠标进入view
							//Log.i(TAG, "bottom ACTION_HOVER_ENTER...");
							mOrgPI = getWindow().getDecorView().getPointerIcon();
							getWindow().getDecorView().setPointerIcon(PointerIcon.load(getResources(), R.drawable.point));
							break;
						case MotionEvent.ACTION_HOVER_MOVE:  //鼠标在view上
							//Log.i(TAG, "bottom ACTION_HOVER_MOVE...");
							break;
						case MotionEvent.ACTION_HOVER_EXIT:  //鼠标离开view
							//Log.i(TAG, "bottom ACTION_HOVER_EXIT...");
							getWindow().getDecorView().setPointerIcon(mOrgPI);
							break;
					}
					return false;
				}
			});*/

            @Override
            public boolean onHover(View v, MotionEvent event) {
                {

                    switch (event.getAction()) {
                        case MotionEvent.ACTION_HOVER_ENTER:
                            if (cursorMode == BoatInput.CursorDisabled) {
                                //游戏界面
                                if (alpha) {
                                    mOrgPI = getWindow().getDecorView().getPointerIcon();

                                    //getWindow().getDecorView().setPointerIcon(PointerIcon.load(getResources(), R.drawable.alpha_point));
                                }
                            } else if (cursorMode == BoatInput.CursorEnabled) {
                                //操作界面
                                if (alpha) {
                                    mOrgPI = getWindow().getDecorView().getPointerIcon();

                                    //getWindow().getDecorView().setPointerIcon(PointerIcon.load(getResources(), R.drawable.point));
                                }

                            }

                        case MotionEvent.ACTION_HOVER_MOVE:

                            //鼠标进入存在

                            int x = (int) event.getAxisValue(MotionEvent.AXIS_X);
                            int y = (int) event.getAxisValue(MotionEvent.AXIS_Y);
                            if (x > width) {
                                width = x;
                            }
                            if (y > height) {
                                height = y;
                            }

                            if (cursorMode == BoatInput.CursorDisabled) {
                                //游戏界面
                                //边界判断

                                addPos(x, y);

                            } else if (cursorMode == BoatInput.CursorEnabled) {
                                //操作界面
                                setMargins(x, y);

                            }
                            break;
                        case MotionEvent.ACTION_HOVER_EXIT:
                            if (alpha) {

                                getWindow().getDecorView().setPointerIcon(mOrgPI);

                            }
                            break;
                    }
                }
                return false;
            }

        });
        button.setOnSystemUiVisibilityChangeListener(p1 -> {

            if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
                View v = BoatActivityMk.this.getWindow().getDecorView();
                v.setSystemUiVisibility(View.GONE);
            } else if (Build.VERSION.SDK_INT >= 19) {
                //for new api versions.
                View decorView = getWindow().getDecorView();
                int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
                decorView.setSystemUiVisibility(uiOptions);
            }


        });

        Handler h = new Handler();
        h.postDelayed(new Runnable() {


            @Override
            public void run() {
                Lock = true;
            }
        }, 3000);

    }

    public void loadCursor(){
        controllerView = findViewById(R.id.controller_view);
        controllerView.setFocusable(true);
        controllerView.setDefaultFocusHighlightEnabled(false);
        controllerView.setOnCapturedPointerListener(this);
        Handler h = new Handler();
        h.postDelayed(() -> {
            controllerView.requestPointerCapture();
        }, 500);//这个线程延时很重要，不做延时或者延时时间短是无法实现view的onCapturedPointer事件监听的
    }

    @Override
    public boolean onCapturedPointer(View view, MotionEvent event) {
        Log.d("MyTouchTest", "onCapturedPointer: " + event.getAction() + " " + event.getSource());
        int horizonOffset = (int)event.getX();
        int verticalOffset = (int)event.getY();
        BoatInput.setPointer(horizonOffset,verticalOffset);
        mouseCursor.setX(horizonOffset);
        mouseCursor.setY(verticalOffset);
        return true;
        //return handleMotionEvent(motionEvent);
    }

    //-------x
//
//
//
//y
    private void addPos(int X, int Y) {

        //1


        if (X <= 0 && Y <= 0) {
            posX -= 10;
            posY -= 10;
            setMargins(posX, posY);
        }
        //3
        else if (Y <= 0 && X >= width) {
            posX += 10;

            initialX += 10;


            posY -= 10;
            setMargins(initialX, posY);
        }
        //9
        else if (X >= width && Y >= height) {
            posX += 10;
            posY += 10;
            initialX += 10;
            initialY += 10;

            setMargins(initialX, initialY);
        }
        //7
        else if (Y >= height && X <= 0) {
            posX -= 10;
            posY += 10;
            initialY += 10;
            setMargins(posX, initialY);
        }
        //4
        else if (X <= 0 && Y > 0 && Y < height) {
            posX -= 10;
            int y = posY + Y;
            setMargins(posX, y);
        }
        //2
        else if (Y <= 0 && X > 0 && X < width) {
            int x = posX + X;
            posY -= 10;
            setMargins(x, posY);
        }
        //6
        else if (X >= width && Y > 0 && Y < height) {
            posX += 10;
            initialX += 10;
            int y = posY + Y;
            setMargins(initialX, y);
        }
        //8
        else if (Y >= height && X > 0 && X < width) {


            posY += 10;


            int x = posX + X;
            initialY += 10;
            setMargins(x, initialY);
        }
        //5
        else if (X > 0 && X < width && Y > 0 && Y < height) {

            int x = posX + X;
            int y = posY + Y;


            initialX = x;
            initialY = y;

            setMargins(x, y);

        }


    }


    // @Override

    public void setMargins(int thisX, int thisY) {

        if (Lock) {

            if (cursorMode == BoatInput.CursorEnabled) {
                //操作界面
                baseX = thisX;
                baseY = thisY;
            }


            BoatInput.setPointer(thisX, thisY);
            text.setText("[" + thisX + "][" + thisY + "]");
            text2.setText("[" + posX + "][" + posY + "]\n[" + initialX + "][" + initialY + "]\n[" + baseX + "][" + baseY + "]");

        }

    }

    @Override
    public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {
        // TODO: Implement this method
    }

    @Override
    public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {
        // TODO: Implement this method
    }

    @Override
    public void afterTextChanged(Editable p1) {
        // TODO: Implement this method
        String newText = p1.toString();
        if (newText.length() < 1) {

            BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_BackSpace, 0, true);
            BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_BackSpace, 0, false);
            inputScanner.setText(">");
            inputScanner.setSelection(1);
        }
        if (newText.length() > 1) {
            for (int i = 1; i < newText.length(); i++) {
                BoatInput.setKey(0, newText.charAt(i), true);
                BoatInput.setKey(0, newText.charAt(i), false);
            }

            inputScanner.setText(">");
            inputScanner.setSelection(1);
        }
    }

    @Override
    public boolean onEditorAction(TextView p1, int p2, KeyEvent p3) {
        // TODO: Implement this method

        BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Return, '\n', true);
        BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Return, '\n', false);
        return false;
    }

    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {

        switch (event.getButtonState()) {
            case MotionEvent.ACTION_DOWN:
                //  text.setText(event.getX(0)+"?");
                if (pri) {
                    BoatInput.setMouseButton(BoatInput.Button1, false);
                    pri = false;
                }
                if (ter) {
                    BoatInput.setMouseButton(BoatInput.Button2, false);
                    ter = false;
                }
                if (sec) {
                    BoatInput.setMouseButton(BoatInput.Button3, false);
                    sec = false;
                }
                if (shang) {
                    BoatInput.setMouseButton(BoatInput.Button4, false);
                    shang = false;
                }
                if (xia) {
                    BoatInput.setMouseButton(BoatInput.Button5, false);
                    xia = false;
                }
                break;
            case MotionEvent.BUTTON_PRIMARY:
                text.append("Pri");
                pri = true;
                BoatInput.setMouseButton(BoatInput.Button1, true);

                break;
            case MotionEvent.BUTTON_TERTIARY:
                text.append("Ter");
                ter = true;
                BoatInput.setMouseButton(BoatInput.Button2, true);

                break;
            case MotionEvent.BUTTON_SECONDARY:
                text.append("Sec");
                sec = true;
                BoatInput.setMouseButton(BoatInput.Button3, true);

                break;
        }


        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_SCROLL:

                if (event.getAxisValue(MotionEvent.AXIS_VSCROLL) < 0.0f) {
                    text.append("down");//5
                    BoatInput.setMouseButton(BoatInput.Button5, true);
                    xia = true;

                } else {
                    text.append("up");//4
                    BoatInput.setMouseButton(BoatInput.Button4, true);
                    shang = true;
                }
                break;

        }

        return false;
    }

    @Override
    protected void onPause() {
        // TODO: Implement this method
        super.onPause();
        if (cursorMode == BoatInput.CursorDisabled) {
            if (noesc.equals("false")) {
                BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Escape, 0, true);
            } else {
            }
        } else {
        }

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO: Implement this method
        super.onWindowFocusChanged(hasFocus);
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
            alpha = true;
        } else {
            alpha = false;

        }
    }

    //private boolean overlayCreated = false;
    //private PopupWindow popupWindow;
    //private RelativeLayout base;

    @Override
    public void onBackPressed() {
        // TODO: Implement this method
    }

    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {

        // TODO: Implement this method
        System.out.println("SurfaceTexture is available!");
        BoatActivityMk.setBoatNativeWindow(new Surface(surface));

        new Thread() {
            @Override
            public void run() {

                LauncherConfig config = LauncherConfig.fromFile(getIntent().getExtras().getString("config"));
                LoadMe.exec(config);
                Message msg = new Message();
                msg.what = -1;
                mHandler.sendMessage(msg);
            }
        }.start();
    }

    public void setCursorMode(int mode) {

        Message msg = new Message();
        msg.what = mode;
        mHandler.sendMessage(msg);
    }

    public void setCursorPos(int x, int y) {

        Message msg = new Message();
        msg.what = BoatInput.CursorSetPos;
        msg.arg1 = x;
        msg.arg2 = y;
        mHandler.sendMessage(msg);
    }

    private String cp2() {
        try {
            FileInputStream in = new FileInputStream("/sdcard/games/com.koishi.launcher/h2o2/h2ocfg.json");
            byte[] b = new byte[in.available()];
            in.read(b);
            in.close();
            String str = new String(b);
            JSONObject json = new JSONObject(str);
            return json.getString("backToRightClick");
        } catch (Exception e) {
            System.out.println(e);
        }
        return "false";
    }

    private String cp4() {
        try {
            FileInputStream in = new FileInputStream("/sdcard/games/com.koishi.launcher/h2o2/h2ocfg.json");
            byte[] b = new byte[in.available()];
            in.read(b);
            in.close();
            String str = new String(b);
            JSONObject json = new JSONObject(str);
            return json.getString("dontEsc");
        } catch (Exception e) {
            System.out.println(e);
        }
        return "false";
    }

    @Override
    public void onClick(View p1) {
        // TODO: Implement this method
        if (p1 == inputScanner) {
            inputScanner.setSelection(1);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (rc.equals("false")) {
                BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Escape, 0, true);
                return true;
            } else if (rc.equals("true")) {
                BoatInput.setMouseButton(BoatInput.Button3, true);
                return true;
            }
        }
        if (keyCode == KeyEvent.KEYCODE_HOME) {
            BoatInput.setMouseButton(BoatInput.Button2, true);
            return true;
        }
        Integer lwjglCode = AndroidKeyCodes.keyCodeMap.get(keyCode);
        if (lwjglCode != null) {

            BoatInput.setKey(lwjglCode, 0, true);
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }


    //物理按键操作

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (rc.equals("true")) {
                BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Escape, 0, false);
                return true;
            } else if (rc.equals("false")) {
                BoatInput.setMouseButton(BoatInput.Button3, false);
                return true;
            }
        }
        if (keyCode == KeyEvent.KEYCODE_HOME) {
            BoatInput.setMouseButton(BoatInput.Button2, false);
            return true;
        }
        Integer lwjglCode = AndroidKeyCodes.keyCodeMap.get(keyCode);
        if (lwjglCode != null) {

            BoatInput.setKey(lwjglCode, 0, false);
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }/*

     */

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 0:
                    BoatActivityMk.this.mouseCursor.setVisibility(View.INVISIBLE);
                    //BoatActivityMk.this.itemBar.setVisibility(View.VISIBLE);
                    BoatActivityMk.this.cursorMode = BoatInput.CursorDisabled;
					/*
					posX=((posX+intX)-baseX)-intX;
					posY=((posY+intY)-baseY)-intY;*/
                    BoatActivityMk.this.loadCursor();
                    BoatActivityMk.this.button.setFocusable(false);
                    BoatActivityMk.this.button.setVisibility(View.GONE);
                    break;
                case 1:
                    BoatActivityMk.this.mouseCursor.setVisibility(View.VISIBLE);
                    //BoatActivityMk.this.itemBar.setVisibility(View.INVISIBLE);
                    BoatActivityMk.this.cursorMode = BoatInput.CursorEnabled;
                    BoatActivityMk.this.button.setFocusable(true);
                    BoatActivityMk.this.button.setVisibility(View.VISIBLE);
                    BoatActivityMk.this.posX = 0;
                    BoatActivityMk.this.posY = 0;
                    BoatActivityMk.this.initialX = 0;
                    BoatActivityMk.this.initialY = 0;
                    BoatActivityMk.this.baseX = 0;
                    BoatActivityMk.this.baseY = 0;
                    break;
                case 2:
                    BoatActivityMk.this.mouseCursor.setX((float) msg.arg1);
                    BoatActivityMk.this.mouseCursor.setY((float) msg.arg2);
                    break;
                default:
                    BoatActivityMk.this.finish();
                    break;
            }
        }
    }


}



