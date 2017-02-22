package com.guang.app.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.guang.app.R;

/**
 * 右边有个一键清除文本按钮的EditText
 * http://www.jianshu.com/p/ddada793f3e7
 * Created by xiaoguang on 2017/2/22.
 */
public class OneKeyClearEditText  extends AppCompatEditText implements View.OnFocusChangeListener, TextWatcher {
    private Drawable mClearDrawable;// 一键删除的按钮
    private int colorAccent;//获得主题的颜色
    private boolean hasFocus;// 控件是否有焦点
    public OneKeyClearEditText(Context context) {
        this(context, null);
    }

    public OneKeyClearEditText(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.editTextStyle);
    }

    public OneKeyClearEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.getTheme()
                .obtainStyledAttributes(new int[] {android.R.attr.colorAccent});
        colorAccent = array.getColor(0, 0xFF00FF);
        array.recycle();
        initClearDrawable(context);
    }
    private void initClearDrawable(Context context) {
        mClearDrawable = getCompoundDrawables()[2];// 获取EditText的DrawableRight,假如没有设置我们就使用默认的图片
        if (mClearDrawable == null) {
//            http://www.jianshu.com/p/e22d9dd93d4a
            mClearDrawable = ContextCompat.getDrawable(context, R.mipmap.ic_delete_01);
//            mClearDrawable = ContextCompat.getDrawable(R.mipmap.ic_delete_01);
        }
        if (!isInEditMode()) {
            DrawableCompat.setTint(mClearDrawable, colorAccent);//设置删除按钮的颜色和TextColor的颜色一致
        }
        mClearDrawable.setBounds(0, 0, (int) getTextSize(), (int) getTextSize());//设置Drawable的宽高和TextSize的大小一致
        setClearIconVisible(true);
        // 设置焦点改变的监听
        setOnFocusChangeListener(this);
        // 设置输入框里面内容发生改变的监听
        addTextChangedListener(this);
    }
    /**
     * 设置清除图标的显示与隐藏，调用setCompoundDrawables为EditText绘制上去
     *
     * @param visible
     */
    private void setClearIconVisible(boolean visible) {
        Drawable right = visible ? mClearDrawable : null;
        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mClearDrawable != null && event.getAction() == MotionEvent.ACTION_UP) {
            int x = (int) event.getX();
            // 判断触摸点是否在水平范围内
            boolean isInnerWidth = (x > (getWidth() - getTotalPaddingRight()))
                    && (x < (getWidth() - getPaddingRight()));
            // 获取删除图标的边界，返回一个Rect对象
            Rect rect = mClearDrawable.getBounds();
            // 获取删除图标的高度
            int height = rect.height();
            int y = (int) event.getY();
            // 计算图标底部到控件底部的距离
            int distance = (getHeight() - height) / 2;
            // 判断触摸点是否在竖直范围内(可能会有点误差)
            // 触摸点的纵坐标在distance到（distance+图标自身的高度）之内，则视为点中删除图标
            boolean isInnerHeight = (y > distance) && (y < (distance + height));
            if (isInnerHeight && isInnerWidth) {
                this.setText("");
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        this.hasFocus = hasFocus;
        if (hasFocus) {
            setClearIconVisible(getText().length() > 0);
        } else {
            setClearIconVisible(false);
        }

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    /**
     * 当输入框里面内容发生变化的时候回调的方法
     */
    @Override
    public void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        if (hasFocus) {
            setClearIconVisible(text.length() > 0);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}


