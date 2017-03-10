package com.guang.app.fragment;


import android.os.Bundle;
import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.guang.app.R;
//未使用，如需社交功能或其他则添加到这
public class ShareFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_share, container,false);
		return view;
	}
}
