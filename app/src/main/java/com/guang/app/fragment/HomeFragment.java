package com.guang.app.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.guang.app.R;


public class HomeFragment extends Fragment {
//	@Bind(R.id.menu_score) ImageButton btn_socre;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_home, container,false);
		return view;
	}


}
