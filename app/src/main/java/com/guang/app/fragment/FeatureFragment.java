package com.guang.app.fragment;


import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.apkfuns.logutils.LogUtils;
import com.guang.app.R;
import com.guang.app.activity.BookActivity;
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

	//第二排
//	@OnClick(R.id.menu_searchBook) void searchBook() {
//		Intent intent = new Intent(getActivity(), BookActivity.class);
//		intent.putExtra("do",BookActivity.DoCurrentBook);
//		startActivity(intent);
//	}
	@OnClick(R.id.menu_currentBook) void currentBook() {
		Intent intent = new Intent(getActivity(), BookActivity.class);
		intent.putExtra(BookActivity.doWhat,BookActivity.DoCurrentBook);
		startActivity(intent);
	}
	@OnClick(R.id.menu_borrowedBook) void borrowedBook() {
		Intent intent = new Intent(getActivity(), BookActivity.class);
		intent.putExtra(BookActivity.doWhat,BookActivity.DoBorrowedBook);
		startActivity(intent);
	}

	//第三排
	@OnClick(R.id.menu_few_sztz) void queryFewSztz() {
		startActivity(new Intent(getActivity(), FewSztzActivity.class));
	}
	@OnClick(R.id.menu_calendar) void queryXiaoLi() {
//		startActivity(new Intent(getActivity(), XiaoLiActivity.class));
		Dialog dia = new Dialog(getActivity(), R.style.edit_AlertDialog_style);
		dia.setContentView(R.layout.xiaoli);
		ImageView imageView = (ImageView) dia.findViewById(R.id.img_xiaoli);
		imageView.setBackgroundResource(R.mipmap.xiaoli);
		dia.show();

		dia.setCanceledOnTouchOutside(true); // Sets whether this dialog is
		Window w = dia.getWindow();
		WindowManager.LayoutParams lp = w.getAttributes();
		lp.x = 0;
		lp.y = 40;
		dia.onWindowAttributesChanged(lp);

//		Dialog dialog = new XiaoLiDialog(getActivity());
//		dialog.show();
	}
}
