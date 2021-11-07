package com.mistake.revision;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import org.koishi.launcher.h2o2pro.R;

public class DialogFragmentDownloadMinecraft extends DialogFragment
{
	
	private String version;
	private String homepath;
	private String address;

	@Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View  base = inflater.inflate(R.layout.dialog_fragment_download_minecraft,container,false); //  此处的布局文件是普通的线性布局（此博客忽略）
		getDialog().requestWindowFeature(STYLE_NO_TITLE);
		setCancelable(false);

		version = getArguments().getString("version");
		homepath = getArguments().getString("game");
		address= getArguments().getString("address");

		//loading_config(version, homepath, address);

        return base;
    }

}

