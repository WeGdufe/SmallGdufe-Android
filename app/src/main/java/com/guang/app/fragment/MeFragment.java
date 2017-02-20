package com.guang.app.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.guang.app.AppConfig;
import com.guang.app.R;
import com.guang.app.activity.LoginActivity;
import com.guang.app.util.FileUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MeFragment extends Fragment {

    @Bind(R.id.tv_me_icon)
    ImageView tvMeIcon;
    @Bind(R.id.tv_me_sno)
    TextView tvMeSno;
    @Bind(R.id.tv_me_name)
    TextView tvMeName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        ButterKnife.bind(this, view);
        tvMeSno.setText(AppConfig.sno);
        return view;
    }

    @OnClick(R.id.tv_me_exit) void logout() {
        startActivity(new Intent(getActivity(), LoginActivity.class));
        FileUtils.expireStoredAccount(getActivity());//防止点退出后重新打开APP会进入旧帐号
        getActivity().finish();
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
