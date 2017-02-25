package com.guang.app.widget;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

import com.guang.app.R;

/**
 * 右边有个一键清除文本按钮的EditText
 * Created by xiaoguang on 2017/2/25.
 */
public class EditTextWithDel  extends EditText {

    private Drawable imgInable;
    private Context mContext;

    public EditTextWithDel(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public EditTextWithDel(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public EditTextWithDel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        imgInable = mContext.getResources().getDrawable(R.mipmap.ic_delete_01);
        addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                setDrawable();
            }
        });
        setDrawable();
    }

    // 设置删除图片
    private void setDrawable() {
        if (length() < 1)
            setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        else
            setCompoundDrawablesWithIntrinsicBounds(null, null, imgInable, null);
    }

    // 处理删除事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (imgInable != null && event.getAction() == MotionEvent.ACTION_UP) {
            int eventX = (int) event.getRawX();
            int eventY = (int) event.getRawY();
            Rect rect = new Rect();
            getGlobalVisibleRect(rect);
            rect.left = rect.right - 100;
            if (rect.contains(eventX, eventY))
                setText("");
        }
        return super.onTouchEvent(event);
    }
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}