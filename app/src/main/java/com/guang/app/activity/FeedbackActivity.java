package com.guang.app.activity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.guang.app.R;
import com.guang.app.api.WorkApiFactory;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 反馈
 * Created by xiaoguang on 2017/2/13.
 */
public class FeedbackActivity extends QueryActivity {

    @Bind(R.id.ed_feedback_contact)
    EditText edFeedbackContact;
    @Bind(R.id.ed_feedback_content)
    EditText edFeedbackContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);
        addTitleBackBtn();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected boolean shouldHideLoadingIcon() {
        return true;
    }

    @OnClick(R.id.btn_feedback_submit) void submitFeedback() {
        String contact = edFeedbackContact.getText().toString();
        String content = edFeedbackContent.getText().toString();

        WorkApiFactory factory = WorkApiFactory.getInstance();
        factory.submitFeedback(contact, content,new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Object value) {
                Toast.makeText(FeedbackActivity.this, "非常感谢", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(FeedbackActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {
            }
        });
    }
}
