package com.guang.app.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apkfuns.logutils.LogUtils;
import com.guang.app.R;
import com.guang.app.activity.BookActivity;
import com.guang.app.activity.FewSztzActivity;
import com.guang.app.activity.MapActivity;
import com.guang.app.activity.ScoreActivity;
import com.guang.app.activity.SearchBookActivity;
import com.guang.app.activity.XiaoLiActivity;

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

	//第二排
	@OnClick(R.id.menu_searchBook) void searchBook() {
		Intent intent = new Intent(getActivity(), SearchBookActivity.class);
		startActivity(intent);
	}
	@OnClick(R.id.menu_currentBook) void currentBook() {
		Intent intent = new Intent(getActivity(), BookActivity.class);
		intent.putExtra(BookActivity.doWhat,BookActivity.doCurrentBook);
		startActivity(intent);
	}
	@OnClick(R.id.menu_borrowedBook) void borrowedBook() {
		Intent intent = new Intent(getActivity(), BookActivity.class);
		intent.putExtra(BookActivity.doWhat,BookActivity.doBorrowedBook);
		startActivity(intent);
	}

	//第三排
	@OnClick(R.id.menu_few_sztz) void queryFewSztz() {
		startActivity(new Intent(getActivity(), FewSztzActivity.class));
	}
	@OnClick(R.id.menu_calendar) void queryXiaoLi() {
		Intent intent = new Intent(getActivity(), XiaoLiActivity.class);
		intent.putExtra(XiaoLiActivity.doWhat,XiaoLiActivity.doXiaoLi);
		startActivity(intent);
	}
	@OnClick(R.id.menu_timeTable) void queryTimeTable() {
		Intent intent = new Intent(getActivity(), XiaoLiActivity.class);
		intent.putExtra(XiaoLiActivity.doWhat,XiaoLiActivity.doTimeTable);
		startActivity(intent);
	}
	@OnClick(R.id.menu_map) void queryMap() {
		Intent intent = new Intent(getActivity(), MapActivity.class);
		intent.putExtra(MapActivity.doWhat, MapActivity.doMapSanShui);
		startActivity(intent);
	}

}
