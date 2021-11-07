package org.koishi.launcher.h2o2pro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.mistake.revision.VanillaActivity;

import org.json.JSONObject;
import org.koishi.launcher.h2o2pro.adapters.BaseRecycleAdapter;
//import org.koishi.launcher.h2o2pro.adapters.SeachRecordAdapter;
import org.koishi.launcher.h2o2pro.tool.GetGameJson;
import org.koishi.launcher.h2o2pro.tool.data.DbDao;
import org.koishi.launcher.h2o2pro.tool.file.AppExecute;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.Collator;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class VersionsActivity extends AppCompatActivity {

    private Button mbtn_serarch;
    private Dialog mDialog;
    private EditText met_search;
    private FloatingActionButton dir,ver;
    private LinearLayout page;
    //private TabItem rb1,rb2;
    private TabLayout tab;
    private RecyclerView mRecyclerView,mVerRecyclerView;
    private TextView mtv_deleteAll;
    private SearchDirAdapter mAdapter;
    private VersionRecyclerAdapter mVerAdapter;
    private String getBoatDir;
    private String sd1 = "/storage/emulated/0/games/com.koishi.launcher/h2o2/gamedir";
    private String sd2 = "/sdcard/games/com.koishi.launcher/h2o2/gamedir";
    private String sd3 = "/mnt/sdcard/games/com.koishi.launcher/h2o2/gamedir";

    private List<String> verList;

    private DbDao mDbDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_versions);
        getWindow().setStatusBarColor(getResources().getColor(R.color.material_card_background));
        page = findViewById(R.id.dir_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> {
            finish();
            startActivity(new Intent(VersionsActivity.this,HomeActivity.class));
        });
        Typeface tf = Typeface.createFromAsset(this.getAssets(),
                "Sans.ttf");
        TextView bigTitle= (TextView) toolbar.getChildAt(0);
        bigTitle.setTypeface(tf);
        bigTitle.setText(getResources().getString(R.string.menu_ver));

        //rb1 = findViewById(R.id.ver_title_dir);
        //rb2 = findViewById(R.id.ver_title_ver);

        initViews();
        initVers();

        //rb1.setOnClickListener(v->{
            //mRecyclerView.setVisibility(View.VISIBLE);
            //mVerRecyclerView.setVisibility(View.GONE);
        //});
        //rb2.setOnClickListener(v->{
           // mRecyclerView.setVisibility(View.GONE);
            //mVerRecyclerView.setVisibility(View.VISIBLE);
        //});
        dir = findViewById(R.id.ver_new_dir);
        ver = findViewById(R.id.ver_new_ver);
        ver.hide();
        tab = findViewById(R.id.ver_tab);
        tab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                //  tab.getPosition()  返回数字，从0开始
                // tab.getText()  返回字符串类型，从0开始
                if (tab.getPosition()==0){
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mVerRecyclerView.setVisibility(View.GONE);
                    ver.hide();
                    dir.show();
                }
                if (tab.getPosition()==1){
                    mRecyclerView.setVisibility(View.GONE);
                    mVerRecyclerView.setVisibility(View.VISIBLE);
                    dir.hide();
                    ver.show();
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void initViews() {
        mDbDao =new DbDao(this);
        //mbtn_serarch = findViewById(R.id.btn_serarch);
        //met_search = findViewById(R.id.et_search);
        //mtv_deleteAll = findViewById(R.id.tv_deleteAll);
        //mtv_deleteAll.setOnClickListener(view -> {
            //mDbDao.deleteData();
            //mAdapter.updata(mDbDao.queryData(""));
        //});
        mRecyclerView = findViewById(R.id.mRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new SearchDirAdapter(mDbDao.queryData(""), this);
        mAdapter.setRvItemOnclickListener(position -> {
            mDbDao.delete(mDbDao.queryData("").get(position));
            mAdapter.updata(mDbDao.queryData(""));
        });
        if (!mDbDao.hasData(sd1)){
            mDbDao.insertData(sd1);
            mAdapter.updata(mDbDao.queryData(""));
        }
        mRecyclerView.setAdapter(mAdapter);
        //事件监听
        /*
        mbtn_serarch.setOnClickListener(view -> {

            if (met_search.getText().toString().trim().length() != 0){
                boolean hasData = mDbDao.hasData(met_search.getText().toString().trim());
                if (!hasData){
                    mDbDao.insertData(met_search.getText().toString().trim());
                }else {
                    Toast.makeText(VersionsActivity.this, "该内容已在历史记录中", Toast.LENGTH_SHORT).show();
                }

                //
                mAdapter.updata(mDbDao.queryData(""));

            }else {
                Toast.makeText(VersionsActivity.this, "请输入内容", Toast.LENGTH_SHORT).show();
            }

        });
         */
    }

    public void initVers(){
        File versionlist = new File(GetGameJson.getBoatCfg("game_directory","null") + "/versions");
        if (versionlist.isDirectory() && versionlist.exists()) {
            Comparator cp = Collator.getInstance(Locale.CHINA);
            String[] getVer = versionlist.list();
            List< String > verList = Arrays.asList(getVer);  //此集合无法操作添加元素
            Collections.sort(verList, cp);
            mVerRecyclerView = findViewById(R.id.mVerRecyclerView);
            mVerRecyclerView.setLayoutManager(new LinearLayoutManager(this));//设置布局管理器
            mVerRecyclerView.setAdapter(mVerAdapter = new VersionRecyclerAdapter(this, verList));
        } else {
            mVerRecyclerView = findViewById(R.id.mVerRecyclerView);
            mVerRecyclerView.setAdapter(null);
        }
    }

    public void newDir(View v){
        showDirDialog();
    }

    public void newVer(View v){
        finish();
        startActivity(new Intent(VersionsActivity.this, VanillaActivity.class));
    }

    public void showDirDialog() {
        mDialog = new Dialog(VersionsActivity.this);
        View dialogView = VersionsActivity.this.getLayoutInflater().inflate(R.layout.custom_dialog_directory, null);
        mDialog.setContentView(dialogView);
        //mDialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundColor(Color.TRANSPARENT);
        MaterialButton cancel = dialogView.findViewById(R.id.custom_dir_cancel);
        MaterialButton start = dialogView.findViewById(R.id.custom_dir_ok);
        TextInputLayout lay = dialogView.findViewById(R.id.dialog_dir_lay);
        lay.setError(getString(R.string.ver_input_hint));
        start.setEnabled(false);
        TextInputEditText et = dialogView.findViewById(R.id.dialog_dir_name);
        et.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {
            }

            @Override
            public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {
            }

            @Override
            public void afterTextChanged(Editable p1) {
                String value = et.getText().toString();
                if (value.matches("(/storage/emulated/0|/sdcard|/mnt/sdcard).*")){
                    lay.setErrorEnabled(false);
                    start.setEnabled(true);
                } else {
                    lay.setError(getString(R.string.ver_input_hint));
                    start.setEnabled(false);
                }
            }
        });

        cancel.setOnClickListener(v-> mDialog.dismiss());
        start.setOnClickListener(v->{
            if (et.getText().toString().trim().length() != 0){
                boolean hasData = mDbDao.hasData(et.getText().toString().trim());
                if (!hasData){
                    File f = new File(et.getText().toString().trim());
                    if (f.exists()){
                        if (f.isDirectory()){
                            getBoatDir = et.getText().toString();
                            newDir();
                        } else {
                            Snackbar.make(page, getResources().getString(R.string.ver_not_dir), Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    } else {
                        getBoatDir = et.getText().toString();
                        newDir();
                    }
                }else {
                    Snackbar.make(page, getResources().getString(R.string.ver_already_exists), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

                //
                mAdapter.updata(mDbDao.queryData(""));

            }else {
                Snackbar.make(page, "Please input", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }

        });

        mDialog.setContentView(dialogView);
        WindowManager windowManager = VersionsActivity.this.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
        lp.width = (int)(display.getWidth()*0.9); //设置宽度 dialog.getWindow().setAttributes(lp);
        mDialog.show();
    }

    public void newDir(){
        new Thread(() -> {
            try {
                AppExecute.output(VersionsActivity.this, "pack.zip", getBoatDir);
                //Snackbar.make(getView(), getResources().getString(R.string.install_done), Snackbar.LENGTH_LONG)
                //.setAction("Action", null).show();
                han.sendEmptyMessage(1);
            } catch (IOException e) {
                Snackbar.make(page, getResources().getString(R.string.ver_not_right_dir)+e.toString(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                han.sendEmptyMessage(0);
            }
        }).start();
    }

    @SuppressLint("HandlerLeak")
    Handler han = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                mDialog.dismiss();
            }
            if (msg.what == 1) {
               mDialog.dismiss();
               mDbDao.insertData(getBoatDir);
               mAdapter.updata(mDbDao.queryData(""));
               Snackbar.make(page, getResources().getString(R.string.ver_add_done), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
            if (msg.what == 2) {
               //mVerRecyclerView.setAdapter(null);
               //initVers();
                Snackbar.make(page, getResources().getString(R.string.ver_add_done), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }

        }
    };

    //public void refresh(){
        //finish();
        //startActivity(new Intent(VersionsActivity.this,VersionsActivity.class));
    //}

    class SearchDirAdapter extends BaseRecycleAdapter<String> {
        public SearchDirAdapter(List< String > datas, Context mContext) {
            super(datas, mContext);
        }

        @SuppressLint({"ResourceAsColor", "UseCompatLoadingForDrawables"})
        @Override
        protected void bindData(BaseViewHolder holder, final int position) {

            TextView textView = (TextView) holder.getView(R.id.tv_record);
            TextView textView1 = (TextView) holder.getView(R.id.tv_name);
            LinearLayout lay = (LinearLayout) holder.getView(R.id.ver_item);
            ImageView check = (ImageView) holder.getView(R.id.ver_check_icon);
            MaterialButton del = (MaterialButton) holder.getView(R.id.tv_remove_dir);
            MaterialButton delDir = (MaterialButton) holder.getView(R.id.tv_del_dir);
            textView.setText(datas.get(position));
            if (datas.get(position).equals(GetGameJson.getBoatCfg("game_directory", "null"))) {
                //lay.setSelected(true);
                //check.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher_check_file_blue_true));
                lay.setBackground(getResources().getDrawable(R.drawable.recycler_button_pressed));
            } else {
                //lay.setSelected(false);
                //check.setImageDrawable(getResources().getDrawable(R.drawable.cv_shape));
                lay.setBackground(getResources().getDrawable(R.drawable.recycler_button_normal));
            }
            if (datas.get(position).equals(sd1) || datas.get(position).equals(sd2) || datas.get(position).equals(sd3)) {
                del.setVisibility(View.GONE);
                delDir.setVisibility(View.GONE);
            } else {
                del.setVisibility(View.VISIBLE);
                delDir.setVisibility(View.VISIBLE);
            }

            String str1 = textView.getText().toString();
            str1 = str1.substring(0, str1.lastIndexOf("/"));
            int idx = str1.lastIndexOf("/");
            str1 = str1.substring(idx+1,str1.length());
            textView1.setText(str1);

            File f = new File(textView.getText().toString());
            if (f.isDirectory() && f.exists()){

            } else {
                check.setImageDrawable(getResources().getDrawable(R.drawable.xicon_red));
                delDir.setVisibility(View.VISIBLE);
            }
            /*
            if (f.exists() && f.isDirectory()) {
                mRvItemOnclickListener.RvItemOnclick(position);
                mAdapter.updata(mDbDao.queryData(""));
            } else {
                if (null != mRvItemOnclickListener) {
                    mRvItemOnclickListener.RvItemOnclick(position);
                    mAdapter.updata(mDbDao.queryData(""));
                }
            }
             */
            lay.setOnClickListener(v -> {
                if (f.exists() && f.isDirectory()) {
                    setDir(textView.getText().toString());
                    //finish();
                    //startActivity(new Intent(VersionsActivity.this,VersionsActivity.class));
                    mAdapter.updata(mDbDao.queryData(""));
                    mVerRecyclerView.setAdapter(null);
                    initVers();
                } else {
                    if (null != mRvItemOnclickListener) {
                        mRvItemOnclickListener.RvItemOnclick(position);
                        mAdapter.updata(mDbDao.queryData(""));
                        Snackbar.make(page, getResources().getString(R.string.ver_null_dir), Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        mVerRecyclerView.setAdapter(null);
                    }
                }
                
            });
            //
            del.setOnClickListener(view -> {
                //if (null != mRvItemOnclickListener) {
                //if (datas.get(position).equals(GetGameJson.getBoatCfg("game_directory", "null"))) {
                //setDir(sd1);
                //} else {

                //}
                // }
                if (null!=mRvItemOnclickListener){
                    mRvItemOnclickListener.RvItemOnclick(position);
                }
            });

            delDir.setOnClickListener(view -> {
                if (null != mRvItemOnclickListener) {
                    if (datas.get(position).equals(GetGameJson.getBoatCfg("game_directory", "null"))) {
                        setDir(sd1);
                    } else {

                    }
                    AlertDialog alertDialog1 = new AlertDialog.Builder(VersionsActivity.this)
                            .setTitle(getResources().getString(R.string.action))//标题
                            .setIcon(R.drawable.ic_boat)//图标
                            .setMessage(R.string.ver_if_del)
                            .setPositiveButton("Yes Yes Yes", new DialogInterface.OnClickListener() {//添加"Yes"按钮
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    File f = new File(datas.get(position));
                                    //TODO
                                    mRvItemOnclickListener.RvItemOnclick(position);
                                    //finish();
                                    //startActivity(new Intent(VersionsActivity.this,VersionsActivity.class));
                                    mAdapter.updata(mDbDao.queryData(""));
                                    new Thread(() -> {
                                        //String file2= "/data/data/com.koishi.launcher.h2o2/app_runtime";
                                        deleteDirWihtFile(f);
                            /*
                             File file = new File(file2);
                             if(file.isDirectory()){
                             deleteDirectory(file2);
                             }
                             if(file.isFile()){
                             deleteFile(file2);
                             }
                             */
                                    }).start();

                                }
                            })
                            .setNegativeButton("No No No", new DialogInterface.OnClickListener() {//添加"Yes"按钮
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //TODO
                                }
                            })
                            .create();

                    alertDialog1.show();
                }
            });
        }

        public void deleteDirWihtFile(File dir) {
            if (dir == null || !dir.exists() || !dir.isDirectory())
                return;
            for (File file : dir.listFiles()) {
                if (file.isFile())
                    file.delete(); // 删除所有文件
                else if (file.isDirectory())
                    deleteDirWihtFile(file); // 递规的方式删除文件夹
            }
            dir.delete();// 删除目录本身
        }

        @Override
        public int getLayoutId() {
            return R.layout.dir_item;
        }

        public void setDir(String dir) {
            try {
                FileInputStream in = new FileInputStream("/storage/emulated/0/games/com.koishi.launcher/h2o2/config.txt");
                byte[] b = new byte[in.available()];
                in.read(b);
                in.close();
                String str = new String(b);
                JSONObject json = new JSONObject(str);
                json.remove("game_directory");
                json.remove("game_assets");
                json.remove("assets_root");
                json.remove("currentVersion");
                json.put("game_directory", dir);
                json.put("game_assets", dir + "/assets/virtual/legacy");
                json.put("assets_root", dir + "/assets");
                json.put("currentVersion", dir + "/versions");
                FileWriter fr = new FileWriter(new File("/storage/emulated/0/games/com.koishi.launcher/h2o2/config.txt"));
                fr.write(json.toString());
                fr.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    class VersionRecyclerAdapter extends RecyclerView.Adapter<VersionRecyclerAdapter.MyViewHolder>{
        private List<String> datas;
        private LayoutInflater inflater;
        public VersionRecyclerAdapter(Context context,List<String> datas){
            inflater=LayoutInflater.from(context);
            this.datas=datas;
        }
        //创建每一行的View 用RecyclerView.ViewHolder包装
        @Override
        public VersionRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView=inflater.inflate(R.layout.version_local_item,null);
            return new MyViewHolder(itemView);
        }
        //给每一行View填充数据
        @Override
        public void onBindViewHolder(VersionRecyclerAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
            holder.textview.setText(datas.get(position));
            File f = new File(GetGameJson.getBoatCfg("game_directory","Null")+"/versions/"+datas.get(position));
            if (f.isDirectory()&&f.exists()){
            }else{
                holder.rl.setEnabled(false);
                holder.ic.setImageDrawable(getResources().getDrawable(R.drawable.xicon_red));
            }
            holder.rl.setOnClickListener(v->{
                holder.dirs = datas.get(position);
                showExecDialog(holder.dirs);
            });
            holder.btn.setOnClickListener(v->{
                AlertDialog alertDialog1 = new AlertDialog.Builder(VersionsActivity.this)
                        .setTitle(getResources().getString(R.string.action))//标题
                        .setIcon(R.drawable.ic_boat)//图标
                        .setMessage(R.string.ver_if_del)
                        .setPositiveButton("Yes Yes Yes", new DialogInterface.OnClickListener() {//添加"Yes"按钮
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                holder.ic.setImageDrawable(getResources().getDrawable(R.drawable.xicon_red));
                                holder.btn.setVisibility(View.INVISIBLE);
                                holder.textview.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                                holder.rl.setEnabled(false);
                                File f = new File(GetGameJson.getBoatCfg("game_directory","Null")+"/versions/"+datas.get(position));
                                //TODO
                                new Thread(() -> {
                                    //String file2= "/data/data/com.koishi.launcher.h2o2/app_runtime";
                                    if (f.isDirectory()){
                                        deleteDirWihtFile(f);
                                    }else{
                                        deleteFile(GetGameJson.getBoatCfg("game_directory","Null")+"/versions/"+datas.get(position));
                                    }
                            /*
                             File file = new File(file2);
                             if(file.isDirectory()){
                             deleteDirectory(file2);
                             }
                             if(file.isFile()){
                             deleteFile(file2);
                             }
                             */
                                    han.sendEmptyMessage(2);
                                }).start();

                            }
                        })
                        .setNegativeButton("No No No", new DialogInterface.OnClickListener() {//添加"Yes"按钮
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //TODO
                            }
                        })
                        .create();

                alertDialog1.show();
            });
        }

        public void showExecDialog(String dir) {
            mDialog = new Dialog(VersionsActivity.this);
            View dialogView = VersionsActivity.this.getLayoutInflater().inflate(R.layout.custom_dialog_choose_exec, null);
            mDialog.setContentView(dialogView);
            String load = GetGameJson.getAppCfg("allVerLoad","false");
            String loadDir;
            if (load.equals("true")){
                loadDir = GetGameJson.getBoatCfg("game_directory","Null")+"/versions/"+dir;
            }else{
                loadDir = GetGameJson.getBoatCfg("game_directory","Null");
            }
            LinearLayout lay = dialogView.findViewById(R.id.ver_exec_mod);
            lay.setOnClickListener(v->{
                Intent i = new Intent(VersionsActivity.this, ModsActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("mod", loadDir);
                i.putExtras(bundle);
                //i.putExtra("dat",c);
                VersionsActivity.this.startActivity(i);
            });
            //mDialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundColor(Color.TRANSPARENT);

            mDialog.setContentView(dialogView);
            WindowManager windowManager = VersionsActivity.this.getWindowManager();
            Display display = windowManager.getDefaultDisplay();
            WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
            lp.width = (int)(display.getWidth()*0.9); //设置宽度 dialog.getWindow().setAttributes(lp);
            mDialog.show();
        }
        //数据源的数量
        @Override
        public int getItemCount() {
            return datas.size();
        }
        class MyViewHolder extends RecyclerView.ViewHolder{
            private TextView textview;
            private MaterialButton btn;
            private ImageView ic;
            private LinearLayout rl;
            private String dirs;

            public MyViewHolder(View itemView) {
                super(itemView);
                textview = itemView.findViewById(R.id.ver_name);
                btn = itemView.findViewById(R.id.ver_remove);
                rl = itemView.findViewById(R.id.ver_item);
                ic = itemView.findViewById(R.id.ver_icon);
            }
        }

        public void deleteDirWihtFile(File dir) {
            if (dir == null || !dir.exists() || !dir.isDirectory())
                return;
            for (File file : dir.listFiles()) {
                if (file.isFile())
                    file.delete(); // 删除所有文件
                else if (file.isDirectory())
                    deleteDirWihtFile(file); // 递规的方式删除文件夹
            }
            dir.delete();// 删除目录本身
        }

        public boolean deleteFile(String filePath) {
            File file = new File(filePath);
            if (file.isFile() && file.exists()) {
                return file.delete();
            }
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(VersionsActivity.this,HomeActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        //updateDirList();
        //initViews();
        //mRecyclerView.post(() -> updateDirList());

        String currentDir = GetGameJson.getBoatCfg("game_directory","Null");
        File f = new File(currentDir);
        //if (mRecyclerView.isComputingLayout()) {
           //updateDirList();
            //if (f.exists()&&f.isDirectory()){
                //if (mDbDao.hasData(dir.trim())){
                    //mDbDao.delete(dir);
                    //updateDirList();
                //}else{
//
               // }
            //}
       // }
        if (f.exists()&&f.isDirectory()){
            initVers();
        } else {
            setDir(sd1);
            Snackbar.make(page, getResources().getString(R.string.ver_null_dir), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            mDbDao.delete(currentDir);
            mAdapter.updata(mDbDao.queryData(""));
            initVers();
        }
    }

    public void setDir(String dir) {
        try {
            FileInputStream in = new FileInputStream("/storage/emulated/0/games/com.koishi.launcher/h2o2/config.txt");
            byte[] b = new byte[in.available()];
            in.read(b);
            in.close();
            String str = new String(b);
            JSONObject json = new JSONObject(str);
            json.remove("game_directory");
            json.remove("game_assets");
            json.remove("assets_root");
            json.remove("currentVersion");
            json.put("game_directory", dir);
            json.put("game_assets", dir + "/assets/virtual/legacy");
            json.put("assets_root", dir + "/assets");
            json.put("currentVersion", dir + "/versions");
            FileWriter fr = new FileWriter(new File("/storage/emulated/0/games/com.koishi.launcher/h2o2/config.txt"));
            fr.write(json.toString());
            fr.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
