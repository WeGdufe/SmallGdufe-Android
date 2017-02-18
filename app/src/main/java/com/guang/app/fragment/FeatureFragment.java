package com.guang.app.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apkfuns.logutils.LogUtils;
import com.guang.app.R;
import com.guang.app.activity.FewSztzActivity;
import com.guang.app.activity.ScoreActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class FeatureFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
//		View view = inflater.inflate(R.layout.menu, container,false);
		View view = inflater.inflate(R.layout.menu,null,false);
		ButterKnife.bind(this, view);
		LogUtils.e("xxx");

		return view;
	}
	@OnClick(R.id.menu_score) void queryScore() {
		startActivity(new Intent(getActivity(), ScoreActivity.class));
	}
	@OnClick(R.id.menu_few_sztz) void queryFewSztz() {
		startActivity(new Intent(getActivity(), FewSztzActivity.class));
	}

}
