package com.android.formapp.form;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.InverseBindingAdapter;
import android.databinding.InverseBindingListener;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.android.formapp.listener.OnFormContentChangeListener;
import com.android.formapp.R;
import com.android.formapp.databinding.ViewFormEditItemBinding;
import com.android.formapp.utils.DrawableUtils;


/**
 * 可编辑表单项
 *
 * @作者: caoyl
 * @创建日期: 2019/8/28 9:40
 */
public class FormEditableView extends FrameLayout implements TextWatcher {

    private Context                     context;
    private ViewFormEditItemBinding     dataBinding;
    private int                         bottomLineSize;
    private int                         bottomLineColor;
    private Paint                       paint;
    private OnFormContentChangeListener onFormContentChangeListener;

    public FormEditableView(Context context) {
        this(context, null);
    }

    public FormEditableView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FormEditableView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView(attrs);
    }

    private void initView(AttributeSet attrs) {
        dataBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.view_form_edit_item, this, true);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FormEditableView);
        setText(a.getString(R.styleable.FormEditableView_fev_left_text), a.getString(R.styleable.FormEditableView_fev_right_text));
        setRightEditTextHint(a.getString(R.styleable.FormEditableView_fev_right_text_hint));
        setLeftIcon(a.getResourceId(R.styleable.FormEditableView_fev_left_icon, 0));
        bottomLineSize = a.getDimensionPixelSize(R.styleable.FormEditableView_fev_bottom_line_size, 0);
        bottomLineColor = a.getColor(R.styleable.FormEditableView_fev_bottom_line_color,
                                     getResources().getColor(R.color.colorAccent));
        dataBinding.tvRightContent.addTextChangedListener(this);
        showOrHideBottomLine(a.getBoolean(R.styleable.FormEditableView_fev_bottom_line_visibility, true));
        a.recycle();
        setWillNotDraw(false);

    }



    /**
     * 设置右侧文本框提示项
     */
    public void setRightEditTextHint(String hint) {
        if (!TextUtils.isEmpty(hint)) {
            dataBinding.tvRightContent.setHint(hint);
        }
    }

    /**
     * 设置文本
     *
     * @param leftContent  左侧文本
     * @param rightContent 右侧文本
     */
    private void setText(String leftContent, String rightContent) {
        if (!TextUtils.isEmpty(leftContent)) {
            dataBinding.tvLeftContent.setText(leftContent);
        }
        if (!TextUtils.isEmpty(rightContent)) {
            dataBinding.tvRightContent.setText(rightContent);
        }
    }

    /**
     * 设置右侧文本值
     */
    public void setRightText(String rightContent) {
        if (!TextUtils.isEmpty(rightContent)) {
            dataBinding.tvRightContent.setText(rightContent);
        }
        //invalidate();
    }

    public void setFevRightTextChangedListener(OnFormContentChangeListener listener) {
        this.onFormContentChangeListener = listener;
    }

    /**
     * 获取右侧文本值
     */
    public String getRightText() {
        return dataBinding.tvRightContent.getText().toString();
    }

    /**
     * 设置左侧图标
     *
     * @param drawableId 资源id
     */
    @SuppressLint("ResourceType")
    public void setLeftIcon(@DrawableRes int drawableId) {
        if (drawableId > 0) {
            setLeftIcon(getResources().getDrawable(drawableId));
        }
    }

    /**
     * 设置左侧图标
     *
     * @param drawable 资源
     */
    public void setLeftIcon(Drawable drawable) {
        if (drawable != null) {
            Drawable[] drawables = dataBinding.tvLeftContent.getCompoundDrawables();
            drawables[0] = drawable;
            setDrawables(drawables, true);
        }
    }

    /**
     * 设置图标资源
     *
     * @param drawable
     * @param isLeftContent
     */
    private void setDrawables(Drawable[] drawable, boolean isLeftContent) {
        if (drawable == null || drawable.length != 4) {
            return;
        }
        if (isLeftContent) {
            DrawableUtils.setCompoundDrawables(dataBinding.tvLeftContent, drawable[0], drawable[1], drawable[2], drawable[3]);
        } else {
            DrawableUtils.setCompoundDrawables(dataBinding.tvRightContent, drawable[0], drawable[1], drawable[2], drawable[3]);
        }
    }

    /**
     * 是否隐藏底部线条
     *
     * @param isShow 是否显示
     */
    public void showOrHideBottomLine(boolean isShow) {
        if (isShow) {
            dataBinding.rowSpaceLine.setVisibility(VISIBLE);
        } else {
            dataBinding.rowSpaceLine.setVisibility(GONE);
        }
    }

    /**
     * 底部线条高度
     *
     * @param bottomLineSize 高度值
     */
    public void setBottomLineSize(int bottomLineSize) {
        this.bottomLineSize = bottomLineSize;
    }

    /**
     * 底部线条颜色
     *
     * @param bottomLineColor 颜色
     */
    public void setBottomLineColor(int bottomLineColor) {
        this.bottomLineColor = bottomLineColor;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (bottomLineSize > 0) {
            setPaint();
            canvas.drawRect(0, getMeasuredHeight() - bottomLineSize, getMeasuredWidth(), getMeasuredHeight(), paint);
        }
    }

    /**
     * 设置画笔
     */
    private void setPaint() {
        if (paint == null) {
            paint = new Paint();
        }
        paint.setAntiAlias(true);
        paint.setColor(bottomLineColor);
        paint.setStyle(Paint.Style.FILL);
    }


    /**
     * 文本编辑表单，设置右侧文本
     */
    @BindingAdapter(value = {"fev_right_text"})
    public static void setFevRightText(FormEditableView formEditableView, String text) {
        formEditableView.setRightText(text);
    }

    @InverseBindingAdapter(attribute = "fev_right_text", event = "fevRightTextChanged")
    public static String getFevRightText(FormEditableView formEditableView) {
        return formEditableView.getRightText();
    }

    @BindingAdapter(value = {"fevRightTextChanged"})
    public static void setFevRightTextChangedListener(FormEditableView formEditableView, InverseBindingListener listener) {
        formEditableView.setFevRightTextChangedListener(listener::onChange);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (onFormContentChangeListener!=null){
            onFormContentChangeListener.onChange();
        }
    }
}
