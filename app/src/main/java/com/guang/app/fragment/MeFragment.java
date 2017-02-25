package com.guang.app.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.apkfuns.logutils.LogUtils;
import com.guang.app.AppConfig;
import com.guang.app.R;
import com.guang.app.activity.LoginActivity;
import com.guang.app.api.JwApiFactory;
import com.guang.app.model.BasicInfo;
import com.guang.app.model.Schedule;
import com.guang.app.util.FileUtils;

import org.litepal.crud.DataSupport;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MeFragment extends Fragment {
    public static JwApiFactory factory = JwApiFactory.getInstance();
    public static final long localId = 1; //用户基本信息存在数据库的id

    @Bind(R.id.tv_me_icon)
    ImageView tvMeIcon;
    @Bind(R.id.tv_me_sno)
    TextView tvMeSno;
    @Bind(R.id.tv_me_name)
    TextView tvMeName;
    @Bind(R.id.tv_me_class)
    TextView tvMeClass;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        ButterKnife.bind(this, view);
        tvMeSno.setText(AppConfig.sno);

        BasicInfo basicInfo = DataSupport.find(BasicInfo.class,localId);
        if(null != basicInfo) {
            setBasicInfo4View(basicInfo);
        }else{
//            LogUtils.e("网络查询基本信息");
            queryBasicInfo();
        }
        return view;
    }

    private void queryBasicInfo(){
        factory.getBasicInfo(new Observer<BasicInfo>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(BasicInfo value) {
                value.setId(localId);
                value.save();
                setBasicInfo4View(value);
            }

            @Override
            public void onError(Throwable e) {
                LogUtils.e(e.toString());
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void setBasicInfo4View(BasicInfo value) {
        if (value.getSex().equals("女")){
            tvMeName.setTextColor(getResources().getColor(R.color.pink));
        }
        tvMeName.setText(value.getName());
        tvMeClass.setText(value.getClassroom());
        String iconUrl = AppConfig.Avator_URL+value.getNamePy().charAt(0);
//        String iconUrl = AppConfig.Avator_URL+value.getNamePy().charAt(0);
//        tvMeIcon.setImageDrawable(Drawable.createFromResourceStream(getResources().getDrawable(R.mipmap.avatar_H)));
        tvMeIcon.setBackgroundResource(R.mipmap.avatar_h);
//        tvMeIcon.setImageDrawable(Drawable.c(R.mipmap.avatar_H));

//        tvMeIcon
    }

    @OnClick(R.id.tv_me_exit) void logout() {
        startActivity(new Intent(getActivity(), LoginActivity.class));
        FileUtils.expireStoredAccount(getActivity());//防止点退出后重新打开APP会进入旧帐号
        DataSupport.deleteAll(Schedule.class);  //清空课程表
        DataSupport.deleteAll(BasicInfo.class);
        getActivity().finish();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
