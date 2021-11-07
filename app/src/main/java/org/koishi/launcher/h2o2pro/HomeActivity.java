package org.koishi.launcher.h2o2pro;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import org.koishi.launcher.h2o2pro.tool.GetGameJson;
import org.koishi.launcher.h2o2pro.ui.custom.CustomFragment;
import org.koishi.launcher.h2o2pro.ui.custom.FuncFragment;
import org.koishi.launcher.h2o2pro.ui.home.HomeFragment;
import org.koishi.launcher.h2o2pro.ui.install.InstallFragment;
import org.koishi.launcher.h2o2pro.ui.manager.ManagerFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private LinearLayout l1,l2,l3,l4;
    private FragmentPagerAdapter mPagerAdapter;
    private SpannableStringBuilder style;
    private TabLayout tabLayout;
    private TextView bigTitle;
    private Typeface tf;
    private Toolbar toolbar;

    private ImageView iv1,iv2,iv3,iv4;
    private TextView tv1,tv2,tv3,tv4;
    List<String> titles = new ArrayList<>();
     ArrayList< Fragment > fgLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tf = Typeface.createFromAsset(this.getAssets(),
                "Sans.ttf");
        bigTitle= (TextView) toolbar.getChildAt(0);
        bigTitle.setTypeface(tf);
        tabLayout = findViewById(R.id.home_tab);
        titles.add("fragment1");
        titles.add("fragment2");
        titles.add("fragment3");
        titles.add("fragment4");
        initView();
        l1 = findViewById(R.id.home_nav_1);
        l2 = findViewById(R.id.home_nav_2);
        l3 = findViewById(R.id.home_nav_3);
        l4 = findViewById(R.id.home_nav_4);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        tv4 = findViewById(R.id.tv4);
        iv1 = findViewById(R.id.iv1);
        iv2 = findViewById(R.id.iv2);
        iv3 = findViewById(R.id.iv3);
        iv4 = findViewById(R.id.iv4);
        iv1.setImageDrawable(getResources().getDrawable(R.drawable.ic_btm_home));
        iv1.setSelected(true);
        iv2.setImageDrawable(getResources().getDrawable(R.drawable.ic_btm_install));
        iv3.setImageDrawable(getResources().getDrawable(R.drawable.ic_btm_manager));
        iv4.setImageDrawable(getResources().getDrawable(R.drawable.ic_btm_custom));

        l1.setOnClickListener(v->{
            viewPager.setCurrentItem(0);
            style = new SpannableStringBuilder(getResources().getString(R.string.app_name));
            style.setSpan(new ForegroundColorSpan(getResources().getColor(cosine.boat.R.color.colorPrimary)), 0, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            style.setSpan(new ForegroundColorSpan(getResources().getColor(cosine.boat.R.color.appBlack_ff)), 10, 13, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            bigTitle.setText(style);
            iv1.setBackground(getResources().getDrawable(R.drawable.ic_btm_selected_bg));
            iv2.setBackground(getResources().getDrawable(R.color.empty));
            iv3.setBackground(getResources().getDrawable(R.color.empty));
            iv4.setBackground(getResources().getDrawable(R.color.empty));
        });
        l2.setOnClickListener(v->{
            viewPager.setCurrentItem(1);
            bigTitle.setText(getResources().getString(R.string.menu_install));
            bigTitle.setTextColor(getResources().getColor(R.color.appBlack_ff));
            iv1.setBackground(getResources().getDrawable(R.color.empty));
            iv2.setBackground(getResources().getDrawable(R.drawable.ic_btm_selected_bg));
            iv3.setBackground(getResources().getDrawable(R.color.empty));
            iv4.setBackground(getResources().getDrawable(R.color.empty));
        });
        l3.setOnClickListener(v->{
            viewPager.setCurrentItem(2);
            bigTitle.setText(getResources().getString(R.string.menu_manager));
            bigTitle.setTextColor(getResources().getColor(R.color.appBlack_ff));
            iv1.setBackground(getResources().getDrawable(R.color.empty));
            iv2.setBackground(getResources().getDrawable(R.color.empty));
            iv3.setBackground(getResources().getDrawable(R.drawable.ic_btm_selected_bg));
            iv4.setBackground(getResources().getDrawable(R.color.empty));
        });
        l4.setOnClickListener(v->{
            viewPager.setCurrentItem(3);
            bigTitle.setText(getResources().getString(R.string.menu_more));
            bigTitle.setTextColor(getResources().getColor(R.color.appBlack_ff));
            iv1.setBackground(getResources().getDrawable(R.color.empty));
            iv2.setBackground(getResources().getDrawable(R.color.empty));
            iv3.setBackground(getResources().getDrawable(R.color.empty));
            iv4.setBackground(getResources().getDrawable(R.drawable.ic_btm_selected_bg));
        });
    }

    private void initView() {
        /**
         * viewpager初始化
         */
        viewPager = findViewById(R.id.main_viewpager);
        /**
         * ViewPager的监听
         */
        setViewPagerListener();
        /**
         * fragment相关
         */
        initFragment();

        //tab.setupWithViewPager(viewPager);
        viewPager.setAdapter(mPagerAdapter);   //设置适配器
        viewPager.setOffscreenPageLimit(4); //预加载所有页
        viewPager.setCurrentItem(0);
    }

    private void initFragment() {
        //底部导航栏有几项就有几个Fragment
        fgLists = new ArrayList<>(4);
        fgLists.add(new HomeFragment());
        fgLists.add(new InstallFragment());
        fgLists.add(new ManagerFragment());
        fgLists.add(new FuncFragment());
        //fgLists.add(new MoreFragment());

        //设置适配器用于装载Fragment,ViewPager的好朋友
        mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fgLists.get(position);  //得到Fragment
            }

            @Override
            public int getCount() {
                return fgLists.size();  //得到数量
            }

        };

        viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fgLists.get(position);
            }

            @Override
            public int getCount() {
                return fgLists.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                super.destroyItem(container, position, object);
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {

                return titles.get(position);
            }
        });

        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            startActivity(new Intent(HomeActivity.this,SettingsActivity.class));
            //this.finish();
        }
        /*
        if (item.getItemId() == R.id.action_home) {
            drawer.closeDrawer(GravityCompat.START);
            new Handler().postDelayed(() -> {navigationView.setCheckedItem(R.id.fragment_home);
                getSupportActionBar().setTitle(getResources().getString(R.string.menu_home));
                initFragment1();
                },350);
        }
        if (item.getItemId() == R.id.action_theme) {
            drawer.closeDrawer(GravityCompat.START);
            showTheme();
        }

         */
        return super.onOptionsItemSelected(item);
    }

    //这里有3中滑动过程,我们用点击后就可以
    private void setViewPagerListener() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == 0 && positionOffset == 0){
                    style = new SpannableStringBuilder(getResources().getString(R.string.app_name));
                    style.setSpan(new ForegroundColorSpan(getResources().getColor(cosine.boat.R.color.colorPrimary)), 0, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    style.setSpan(new ForegroundColorSpan(getResources().getColor(cosine.boat.R.color.appBlack_ff)), 10, 13, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    bigTitle.setText(style);
                } else if (position == 1){
                } else if (position == 2){
                } else if (position == 3){
                }
            }

            @Override
            public void onPageSelected(int position) {
                //滑动页面后做的事，这里与BottomNavigationView结合，使其与正确page对应
                if (position == 0){
                    style = new SpannableStringBuilder(getResources().getString(R.string.app_name));
                    style.setSpan(new ForegroundColorSpan(getResources().getColor(cosine.boat.R.color.colorPrimary)), 0, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    style.setSpan(new ForegroundColorSpan(getResources().getColor(cosine.boat.R.color.appBlack_ff)), 10, 13, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    bigTitle.setText(style);
                    iv1.setSelected(true);
                    iv2.setSelected(false);
                    iv3.setSelected(false);
                    iv4.setSelected(false);
                    iv1.setBackground(getResources().getDrawable(R.drawable.ic_btm_selected_bg));
                    iv2.setBackground(getResources().getDrawable(R.color.empty));
                    iv3.setBackground(getResources().getDrawable(R.color.empty));
                    iv4.setBackground(getResources().getDrawable(R.color.empty));
                } else if (position == 1){
                    bigTitle.setText(getResources().getString(R.string.menu_install));
                    bigTitle.setTextColor(getResources().getColor(R.color.appBlack_ff));
                    iv1.setSelected(false);
                    iv2.setSelected(true);
                    iv3.setSelected(false);
                    iv4.setSelected(false);
                    iv1.setBackground(getResources().getDrawable(R.color.empty));
                    iv2.setBackground(getResources().getDrawable(R.drawable.ic_btm_selected_bg));
                    iv3.setBackground(getResources().getDrawable(R.color.empty));
                    iv4.setBackground(getResources().getDrawable(R.color.empty));
                } else if (position == 2){
                    bigTitle.setText(getResources().getString(R.string.menu_manager));
                    bigTitle.setTextColor(getResources().getColor(R.color.appBlack_ff));
                    iv1.setSelected(false);
                    iv2.setSelected(false);
                    iv3.setSelected(true);
                    iv4.setSelected(false);
                    iv1.setBackground(getResources().getDrawable(R.color.empty));
                    iv2.setBackground(getResources().getDrawable(R.color.empty));
                    iv3.setBackground(getResources().getDrawable(R.drawable.ic_btm_selected_bg));
                    iv4.setBackground(getResources().getDrawable(R.color.empty));
                } else if (position == 3){
                    bigTitle.setText(getResources().getString(R.string.menu_more));
                    bigTitle.setTextColor(getResources().getColor(R.color.appBlack_ff));
                    iv1.setSelected(false);
                    iv2.setSelected(false);
                    iv3.setSelected(false);
                    iv4.setSelected(true);
                    iv1.setBackground(getResources().getDrawable(R.color.empty));
                    iv2.setBackground(getResources().getDrawable(R.color.empty));
                    iv3.setBackground(getResources().getDrawable(R.color.empty));
                    iv4.setBackground(getResources().getDrawable(R.drawable.ic_btm_selected_bg));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //final boolean active = (getApplicationContext().getResources().getConfiguration().uiMode
                //& Configuration.UI_MODE_NIGHT_YES)!= 0;
        //if (active){
            //initView();
        //} else {
            //initView();
        //}
    }

}