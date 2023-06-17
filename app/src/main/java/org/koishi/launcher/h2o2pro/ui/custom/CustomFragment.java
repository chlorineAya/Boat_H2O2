package org.koishi.launcher.h2o2pro.ui.custom;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;

import org.koishi.launcher.h2o2pro.R;
import org.koishi.launcher.h2o2pro.SettingsActivity;
import org.koishi.launcher.h2o2pro.TerminalActivity;

public class CustomFragment extends Fragment {

    /****************
     * 此页面不再使用
     * 改为SettingsFragment
     ******************/

    public LinearLayout pg1,pg2,pg3;

    public AppCompatImageButton vx,afd,ptr,qq,dis,git;
    public ImageButton libBoat,libMio,libCrash,libLogin,libXz,libCommon,libJsoup,libGson,libShell,libMd,libHttp,libFast,thkBoat,thkMio,thkLogin,thkFire,thkFish,thkShulker;
    public LinearLayout cSet,cTer,cDonate,cApp,cLib,cThk;
    public TextView ab;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_custom, container, false);

        cSet = root.findViewById(R.id.open_set);
        cTer = root.findViewById(R.id.open_ter);
        cDonate = root.findViewById(R.id.open_donate);
        cApp = root.findViewById(R.id.open_about);
        cLib = root.findViewById(R.id.open_lib);
        cThk = root.findViewById(R.id.open_thk);

        cSet.setOnClickListener(v->{
            startActivity(new Intent(requireActivity(), SettingsActivity.class));
        });
        cTer.setOnClickListener(v->{
            startActivity(new Intent(requireActivity(), TerminalActivity.class));
        });
        cDonate.setOnClickListener(v->openDonate());
        cApp.setOnClickListener(v->showAboutDialog());
        cLib.setOnClickListener(v->openLibDialog());
        cThk.setOnClickListener(v->openThkDialog());

        return root;
    }

    private void crash() {
        throw new RuntimeException("Crash test from CustomFragment.");
    }

    /****************
     *
     * 发起添加群流程。群号：Boat_H2O2 &amp;&amp; 高 考 冲 刺 (572291176) 的 key 为： -c3oG3cfXX-v6W8MbEl9_Fl2JjuuvpC6
     * 调用 joinQQGroup(-c3oG3cfXX-v6W8MbEl9_Fl2JjuuvpC6) 即可发起手Q客户端申请加群 Boat_H2O2 &amp;&amp; 高 考 冲 刺 (572291176)
     *
     * @param key 由官网生成的key
     * @return 返回true表示呼起手Q成功，返回false表示呼起失败
     ******************/
    public boolean joinQQGroup(String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26jump_from%3Dwebapi%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            return false;
        }
    }

    //----------Menu点击----------

    public void openDonate() {
        Dialog mDialog = new Dialog(requireActivity());
        View dialogView = requireActivity().getLayoutInflater().inflate(R.layout.custom_donate_layout, null);
        mDialog.setContentView(dialogView);

        vx = dialogView.findViewById(R.id.donate_wc);
        afd = dialogView.findViewById(R.id.donate_afd);
        ptr = dialogView.findViewById(R.id.donate_ptr);

        vx.setOnClickListener(v->{
            CustomTabsIntent intent = new CustomTabsIntent.Builder().setToolbarColor(getResources().getColor(R.color.colorPrimary)).build();
            intent.launchUrl(requireActivity(), Uri.parse("https://gitee.com/NaCln4c1/naclfile/raw/master/Documents/ic_donate_wx.png"));
        });
        afd.setOnClickListener(v->{
            CustomTabsIntent intent = new CustomTabsIntent.Builder().setToolbarColor(getResources().getColor(R.color.colorPrimary)).build();
            intent.launchUrl(requireActivity(), Uri.parse("https://afdian.net/@boat-h2o"));
        });
        ptr.setOnClickListener(v->{
            CustomTabsIntent intent = new CustomTabsIntent.Builder().setToolbarColor(getResources().getColor(R.color.colorPrimary)).build();
            intent.launchUrl(requireActivity(), Uri.parse("https://www.patreon.com/boatapp_h2o"));
        });

        mDialog.setContentView(dialogView);
        WindowManager windowManager = requireActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
        lp.width = (int)(display.getWidth()*0.9); //设置宽度 dialog.getWindow().setAttributes(lp);
        mDialog.show();
    }

    public void showDonateDialog() {
        Dialog mDialog = new Dialog(requireActivity());
        View dialogView = requireActivity().getLayoutInflater().inflate(R.layout.custom_dialog_donate, null);
        mDialog.setContentView(dialogView);
        MaterialButton cancel = dialogView.findViewById(R.id.custom_donate_cancel);
        MaterialButton start = dialogView.findViewById(R.id.custom_donate_ok);
        cancel.setOnClickListener(v -> mDialog.dismiss());
        start.setOnClickListener(v -> {
            joinQQGroup("-c3oG3cfXX-v6W8MbEl9_Fl2JjuuvpC6");
        });
        mDialog.setContentView(dialogView);
        WindowManager windowManager = requireActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
        lp.width = (int)(display.getWidth()*0.9); //设置宽度 dialog.getWindow().setAttributes(lp);
        mDialog.show();
    }

    public void showAboutDialog() {
        Dialog mDialog = new Dialog(requireActivity());
        View dialogView = requireActivity().getLayoutInflater().inflate(R.layout.custom_about_layout, null);
        mDialog.setContentView(dialogView);

        ab = dialogView.findViewById(R.id.ab);
        ab.setMovementMethod(LinkMovementMethod.getInstance());

        qq = dialogView.findViewById(R.id.about_qq);
        dis = dialogView.findViewById(R.id.about_dis);
        git = dialogView.findViewById(R.id.about_git);

        qq.setOnClickListener(v->{
            showDonateDialog();
            mDialog.dismiss();
        });
        dis.setOnClickListener(v->{
            CustomTabsIntent intent = new CustomTabsIntent.Builder().setToolbarColor(getResources().getColor(R.color.colorPrimary)).build();
            intent.launchUrl(requireActivity(), Uri.parse("https://discord.gg/Ktw9sYx879\n"));
        });
        git.setOnClickListener(v->{
            CustomTabsIntent intent = new CustomTabsIntent.Builder().setToolbarColor(getResources().getColor(R.color.colorPrimary)).build();
            intent.launchUrl(requireActivity(), Uri.parse("https://github.com/NaCln4c1"));
        });

        mDialog.setContentView(dialogView);
        WindowManager windowManager = requireActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
        lp.width = (int)(display.getWidth()*0.9); //设置宽度 dialog.getWindow().setAttributes(lp);
        mDialog.show();
    }

    public void openLibDialog() {
        Dialog mDialog = new Dialog(requireActivity());
        View dialogView = requireActivity().getLayoutInflater().inflate(R.layout.custom_lib_layout, null);
        mDialog.setContentView(dialogView);

        libBoat = dialogView.findViewById(R.id.lib_boat);
        libMio = dialogView.findViewById(R.id.lib_mio);
        libCrash = dialogView.findViewById(R.id.lib_cat);
        libLogin = dialogView.findViewById(R.id.lib_login);
        libXz = dialogView.findViewById(R.id.lib_xz);
        libCommon = dialogView.findViewById(R.id.lib_common);
        libJsoup = dialogView.findViewById(R.id.lib_jsoup);
        libGson = dialogView.findViewById(R.id.lib_gson);
        libShell = dialogView.findViewById(R.id.lib_shell);
        libMd = dialogView.findViewById(R.id.lib_md);
        libHttp = dialogView.findViewById(R.id.lib_okhttp);
        libFast = dialogView.findViewById(R.id.lib_fastjson);

        libBoat.setOnClickListener(v->{
            CustomTabsIntent intent = new CustomTabsIntent.Builder().setToolbarColor(getResources().getColor(R.color.colorPrimary)).build();
            intent.launchUrl(requireActivity(), Uri.parse("https://github.com/AOF-Dev/BoatApp\n"));
        });
        libMio.setOnClickListener(v->{
            CustomTabsIntent intent = new CustomTabsIntent.Builder().setToolbarColor(getResources().getColor(R.color.colorPrimary)).build();
            intent.launchUrl(requireActivity(), Uri.parse("https://github.com/ShirosakiMio/BoatApp\n"));
        });
        libCrash.setOnClickListener(v->{
            CustomTabsIntent intent = new CustomTabsIntent.Builder().setToolbarColor(getResources().getColor(R.color.colorPrimary)).build();
            intent.launchUrl(requireActivity(), Uri.parse("https://github.com/Ereza/CustomActivityOnCrash\n"));
        });
        libLogin.setOnClickListener(v->{
            CustomTabsIntent intent = new CustomTabsIntent.Builder().setToolbarColor(getResources().getColor(R.color.colorPrimary)).build();
            intent.launchUrl(requireActivity(), Uri.parse("https://github.com/Ratsiiel/minecraft-auth-library\n"));
        });
        libXz.setOnClickListener(v->{
            CustomTabsIntent intent = new CustomTabsIntent.Builder().setToolbarColor(getResources().getColor(R.color.colorPrimary)).build();
            intent.launchUrl(requireActivity(), Uri.parse("https://tukaani.org/xz/java.html\n"));
        });
        libCommon.setOnClickListener(v->{
            CustomTabsIntent intent = new CustomTabsIntent.Builder().setToolbarColor(getResources().getColor(R.color.colorPrimary)).build();
            intent.launchUrl(requireActivity(), Uri.parse("https://github.com/apache/commons-compress\n"));
        });
        libJsoup.setOnClickListener(v->{
            CustomTabsIntent intent = new CustomTabsIntent.Builder().setToolbarColor(getResources().getColor(R.color.colorPrimary)).build();
            intent.launchUrl(requireActivity(), Uri.parse("https://github.com/jhy/jsoup\n"));
        });
        libGson.setOnClickListener(v->{
            CustomTabsIntent intent = new CustomTabsIntent.Builder().setToolbarColor(getResources().getColor(R.color.colorPrimary)).build();
            intent.launchUrl(requireActivity(), Uri.parse("https://github.com/google/gson\n"));
        });
        libShell.setOnClickListener(v->{
            CustomTabsIntent intent = new CustomTabsIntent.Builder().setToolbarColor(getResources().getColor(R.color.colorPrimary)).build();
            intent.launchUrl(requireActivity(), Uri.parse("https://github.com/jackpal/Android-Terminal-Emulator\n"));
        });
        libMd.setOnClickListener(v->{
            CustomTabsIntent intent = new CustomTabsIntent.Builder().setToolbarColor(getResources().getColor(R.color.colorPrimary)).build();
            intent.launchUrl(requireActivity(), Uri.parse("https://gitee.com/wateryuan/MarkDownTest\n"));
        });
        libHttp.setOnClickListener(v->{
            CustomTabsIntent intent = new CustomTabsIntent.Builder().setToolbarColor(getResources().getColor(R.color.colorPrimary)).build();
            intent.launchUrl(requireActivity(), Uri.parse("https://github.com/ejlchina/okhttps\n"));
        });
        libFast.setOnClickListener(v->{
            CustomTabsIntent intent = new CustomTabsIntent.Builder().setToolbarColor(getResources().getColor(R.color.colorPrimary)).build();
            intent.launchUrl(requireActivity(), Uri.parse("https://github.com/alibaba/fastjson\n"));
        });

        mDialog.setContentView(dialogView);
        WindowManager windowManager = requireActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
        lp.width = (int)(display.getWidth()*0.9); //设置宽度 dialog.getWindow().setAttributes(lp);
        mDialog.show();
    }

    public void openThkDialog() {
        Dialog mDialog = new Dialog(requireActivity());
        View dialogView = requireActivity().getLayoutInflater().inflate(R.layout.custom_thank_layout, null);
        mDialog.setContentView(dialogView);

        thkBoat = dialogView.findViewById(R.id.thk_boat);
        thkMio = dialogView.findViewById(R.id.thk_mio);
        thkLogin = dialogView.findViewById(R.id.thk_ratsiiel);
        thkFire = dialogView.findViewById(R.id.thk_fire);
        thkFish = dialogView.findViewById(R.id.thk_fish);
        thkShulker = dialogView.findViewById(R.id.thk_shulker);

        thkBoat.setOnClickListener(v->{
            CustomTabsIntent intent = new CustomTabsIntent.Builder().setToolbarColor(getResources().getColor(R.color.colorPrimary)).build();
            intent.launchUrl(requireActivity(), Uri.parse("https://github.com/CosineMath\n"));
        });
        thkMio.setOnClickListener(v->{
            CustomTabsIntent intent = new CustomTabsIntent.Builder().setToolbarColor(getResources().getColor(R.color.colorPrimary)).build();
            intent.launchUrl(requireActivity(), Uri.parse("https://github.com/ShirosakiMio\n"));
        });
        thkLogin.setOnClickListener(v->{
            CustomTabsIntent intent = new CustomTabsIntent.Builder().setToolbarColor(getResources().getColor(R.color.colorPrimary)).build();
            intent.launchUrl(requireActivity(), Uri.parse("https://github.com/Ratsiiel\n"));
        });
        thkFire.setOnClickListener(v->{
            CustomTabsIntent intent = new CustomTabsIntent.Builder().setToolbarColor(getResources().getColor(R.color.colorPrimary)).build();
            intent.launchUrl(requireActivity(), Uri.parse("https://github.com/MCfire-miracle\n"));
        });
        thkFish.setOnClickListener(v->{
            CustomTabsIntent intent = new CustomTabsIntent.Builder().setToolbarColor(getResources().getColor(R.color.colorPrimary)).build();
            intent.launchUrl(requireActivity(), Uri.parse("https://github.com/TSaltedfishKing\n"));
        });
        thkShulker.setOnClickListener(v->{
            CustomTabsIntent intent = new CustomTabsIntent.Builder().setToolbarColor(getResources().getColor(R.color.colorPrimary)).build();
            intent.launchUrl(requireActivity(), Uri.parse("https://github.com/ShulkerSakura\n"));
        });

        mDialog.setContentView(dialogView);
        WindowManager windowManager = requireActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
        lp.width = (int)(display.getWidth()*0.9); //设置宽度 dialog.getWindow().setAttributes(lp);
        mDialog.show();
    }

}