package com.android.formapp.utils;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.view.View;
import android.widget.TextView;

import com.android.formapp.MyApp;


/**
 * Drawable 常用的状态变化
 *
 * @author caoyoulin
 * @date 2019-09-03
 */


public class DrawableUtils {

    /**
     * 获取ColorStateList实例
     * 列如 ：对TextView设置不同状态时其文字颜色。
     */

    public static ColorStateList getColorStateList(int normal, int pressed, int focused, int unable) {
        int[] colors = new int[]{pressed, focused, normal, focused, unable, normal};
        int[][] states = new int[6][];
        states[0] = new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled};
        states[1] = new int[]{android.R.attr.state_enabled, android.R.attr.state_focused};
        states[2] = new int[]{android.R.attr.state_enabled};
        states[3] = new int[]{android.R.attr.state_focused};
        states[4] = new int[]{android.R.attr.state_window_focused};
        states[5] = new int[]{};
        ColorStateList colorList = new ColorStateList(states, colors);
        return colorList;
    }

    public static ColorStateList getColorStateList(int normal, int pressed) {
        int[] colors = new int[]{pressed, pressed, normal};
        int[][] states = new int[3][];
        states[0] = new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled};
        states[1] = new int[]{-android.R.attr.state_enabled};
        states[2] = new int[]{};
        ColorStateList colorList = new ColorStateList(states, colors);
        return colorList;
    }

    public static ColorStateList getColorSelect(int normal, int select) {
        int[] colors = new int[]{select, select, normal};
        int[][] states = new int[3][];
        states[0] = new int[]{android.R.attr.state_selected};
        states[1] = new int[]{android.R.attr.state_pressed};
        states[2] = new int[]{};
        ColorStateList colorList = new ColorStateList(states, colors);
        return colorList;
    }

    /**
     * int strokeWidth = 5; // 3dp 边框宽度
     * int roundRadius = 15; // 8dp 圆角半径
     * int strokeColor = Color.parseColor("#2E3135");//边框颜色
     * int fillColor = Color.parseColor("#DFDFE0");//内部填充颜色
     *
     * @return
     */
    public static GradientDrawable getGradientDrawable(int fillColor, int strokeColor, int roundRadius, int strokeWidth) {

        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(MyApp.getInstance().getResources().getColor(fillColor));
        if (strokeColor > 0) {
            drawable.setStroke(CommonUtils.Dp2Px(MyApp.getInstance(), strokeWidth), MyApp.getInstance().getResources().getColor(strokeColor));
        }
        drawable.setCornerRadius(CommonUtils.Dp2Px(MyApp.getInstance(), roundRadius));
        return drawable;
    }

    public static Drawable getCircleDrawable(@ColorRes int normalColor, @ColorRes int pressedColor, @DimenRes int roundRadius) {
        int size = CommonUtils.Dp2Px(MyApp.getInstance(), roundRadius);
        StateListDrawable bg = new StateListDrawable();
        GradientDrawable drawableNor = new GradientDrawable();
        drawableNor.setSize(size, size);
        drawableNor.setCornerRadius(size / 2);
        drawableNor.setColor(MyApp.getInstance().getResources().getColor(normalColor));

        GradientDrawable drawablePrressed = new GradientDrawable();
        drawablePrressed.setSize(size, size);
        drawablePrressed.setCornerRadius(size / 2);
        drawablePrressed.setColor(MyApp.getInstance().getResources().getColor(pressedColor));

        // View.PRESSED_ENABLED_STATE_SET
        bg.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled}, drawablePrressed);
        // View.ENABLED_FOCUSED_STATE_SET
        bg.addState(new int[]{-android.R.attr.state_enabled}, drawableNor);
        // View.ENABLED_STATE_SET
        bg.addState(new int[]{android.R.attr.state_enabled}, drawableNor);
        // View.FOCUSED_STATE_SET
        bg.addState(new int[]{android.R.attr.state_focused}, drawableNor);
        // View.EMPTY_STATE_SET
        bg.addState(new int[]{}, drawableNor);
        return bg;
    }

    /**
     * 获取带圆角的StateListDrawable
     *
     * @param normalColor  默认的颜色
     * @param pressedColor 按下的颜色
     * @param roundRadius  圆角的大小  dp
     */
    public static Drawable getStateListDrawableRadius(@ColorRes int normalColor, @ColorRes int pressedColor, int roundRadius) {
        int size = CommonUtils.Dp2Px(MyApp.getInstance(), roundRadius);
        StateListDrawable bg = new StateListDrawable();
        GradientDrawable drawableNor = new GradientDrawable();
        drawableNor.setCornerRadius(roundRadius);
        drawableNor.setColor(MyApp.getInstance().getResources().getColor(normalColor));

        GradientDrawable drawablePrressed = new GradientDrawable();
        drawablePrressed.setSize(size, size);
        drawablePrressed.setCornerRadius(size / 2);
        drawablePrressed.setColor(MyApp.getInstance().getResources().getColor(pressedColor));

        // View.PRESSED_ENABLED_STATE_SET
        bg.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled}, drawablePrressed);
        // View.ENABLED_FOCUSED_STATE_SET
        bg.addState(new int[]{-android.R.attr.state_enabled}, drawableNor);
        // View.ENABLED_STATE_SET
        bg.addState(new int[]{android.R.attr.state_enabled}, drawableNor);
        // View.FOCUSED_STATE_SET
        bg.addState(new int[]{android.R.attr.state_focused}, drawableNor);
        // View.EMPTY_STATE_SET
        bg.addState(new int[]{}, drawableNor);
        return bg;
    }

    /**
     * 获取StateListDrawable实例
     *
     * @param idNormal  默认的资源id
     * @param idPressed 按下的资源id
     * @param isEnabled 不可用的资源id
     */
    public static StateListDrawable getStateListDrawable(@DrawableRes int idNormal, @DrawableRes int idPressed, @DrawableRes int isEnabled) {
        StateListDrawable bg = new StateListDrawable();
        Drawable normal = idNormal == -1 ? null : MyApp.getInstance().getResources().getDrawable(idNormal);
        Drawable pressed = idPressed == -1 ? null : MyApp.getInstance().getResources().getDrawable(idPressed);
        Drawable enabled = isEnabled == -1 ? null : MyApp.getInstance().getResources().getDrawable(isEnabled);
        // View.PRESSED_ENABLED_STATE_SET
        bg.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled}, pressed);
        // View.ENABLED_FOCUSED_STATE_SET
        bg.addState(new int[]{-android.R.attr.state_enabled}, enabled);
        // View.ENABLED_STATE_SET
        bg.addState(new int[]{android.R.attr.state_enabled}, normal);
        // View.FOCUSED_STATE_SET
        bg.addState(new int[]{android.R.attr.state_focused}, enabled);
        // View.EMPTY_STATE_SET
        bg.addState(new int[]{}, normal);
        return bg;
    }

    public static StateListDrawable getStateListDrawable(Drawable normal, Drawable pressed, Drawable enabled) {
        StateListDrawable bg = new StateListDrawable();
        // View.PRESSED_ENABLED_STATE_SET
        bg.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled}, pressed);
        // View.ENABLED_FOCUSED_STATE_SET
        bg.addState(new int[]{-android.R.attr.state_enabled}, enabled);
        // View.ENABLED_STATE_SET
        bg.addState(new int[]{android.R.attr.state_enabled}, normal);
        // View.FOCUSED_STATE_SET
        bg.addState(new int[]{android.R.attr.state_focused}, enabled);
        // View.EMPTY_STATE_SET
        bg.addState(new int[]{}, normal);
        return bg;
    }


    /**
     * 获取StateListDrawable实例
     */
    public static StateListDrawable getStateSelectDrawable(@DrawableRes int selectRes, @DrawableRes int defaultRes) {
        StateListDrawable bg = new StateListDrawable();
        Drawable normal = defaultRes == -1 ? null : MyApp.getInstance().getResources().getDrawable(defaultRes);
        Drawable select = selectRes == -1 ? null : MyApp.getInstance().getResources().getDrawable(selectRes);

        bg.addState(new int[]{android.R.attr.state_selected}, select);
        bg.addState(new int[]{}, normal);
        return bg;
    }

    /**
     * 获取StateListDrawable实例
     * 之定义资源，例如形状，边框，颜色
     *
     * @param colorPressed 按下的颜色
     * @param colorNormal  默认的颜色
     * @param cornerRadius 圆角半径  DP
     * @param strokeWidth  边框宽度 dP
     * @param strokeColor  边框颜色
     * @return
     */
    public static StateListDrawable getStateListDrawable(@ColorRes int colorNormal, @ColorRes int colorPressed,
                                                         int cornerRadius, int strokeWidth, @ColorRes int strokeColor) {

        Resources resources = MyApp.getInstance().getResources();

        StateListDrawable bg = new StateListDrawable();
        Drawable pressed = createDrawable(resources.getColor(colorPressed), cornerRadius, strokeWidth, resources.getColor(strokeColor));
        LayerDrawable normal = createDrawable(resources.getColor(colorPressed), resources.getColor(colorNormal), cornerRadius, strokeWidth, resources.getColor(strokeColor));
        // View.PRESSED_ENABLED_STATE_SET
        bg.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled}, pressed);
        // View.ENABLED_FOCUSED_STATE_SET
        // View.ENABLED_STATE_SET
        bg.addState(new int[]{-android.R.attr.state_enabled}, normal);
        // View.FOCUSED_STATE_SET
        // View.EMPTY_STATE_SET
        bg.addState(new int[]{}, normal);
        return bg;
    }

    /**
     * 更具指定的参数获取LayerDrawable, 双层展示, 一般用于默认状态下的Drawable
     *
     * @param colorPressed 按下的颜色
     * @param colorNormal  默认的颜色
     * @param cornerRadius 圆角半径
     * @param strokeWidth  边框宽度
     * @param strokeColor  边框颜色
     * @return
     */

    private static LayerDrawable createDrawable(@ColorInt int colorPressed, @ColorInt int colorNormal,
                                                int cornerRadius, int strokeWidth, @ColorInt int strokeColor) {
        //默认的颜色为透明，那么第一层的颜色也为透明
        if (colorNormal == 0) {
            colorPressed = 0;
        }

        //第一层
        GradientDrawable drawableTop = new GradientDrawable();
        drawableTop.setCornerRadius(cornerRadius);
        drawableTop.setShape(GradientDrawable.RECTANGLE);

        drawableTop.setColor(colorPressed);
        drawableTop.setStroke(strokeWidth, strokeColor);

        //第二层
        GradientDrawable drawableBottom = new GradientDrawable();
        drawableBottom.setCornerRadius(cornerRadius);
        drawableBottom.setShape(GradientDrawable.RECTANGLE);
        drawableBottom.setColor(colorNormal);

        LayerDrawable layerDrawable = new LayerDrawable(new GradientDrawable[]{drawableTop, drawableBottom});
        //对某个内部的资源manage
        layerDrawable.setLayerInset(1, 0, 0, CommonUtils.Dp2Px(MyApp.getInstance(), 1.3f), CommonUtils.Dp2Px(MyApp.getInstance(), 1.8f));
        return layerDrawable;
    }

    /**
     * 更具指定的参数获取Drawable， 单层显示   一般用于按下状态下的Drawable
     *
     * @param colorPressed 按下的颜色
     * @param cornerRadius 圆角半径
     * @param strokeWidth  边框宽度
     * @param strokeColor  边框颜色
     * @return
     */
    private static Drawable createDrawable(@ColorInt int colorPressed, int cornerRadius, int strokeWidth, @ColorInt int strokeColor) {
        GradientDrawable drawablePressed = new GradientDrawable();
        drawablePressed.setCornerRadius(cornerRadius);
        drawablePressed.setColor(colorPressed);
        drawablePressed.setStroke(strokeWidth, strokeColor);
        return drawablePressed;
    }

    /**
     * ripple
     */
    public static Drawable createRipple(@ColorRes int pressed) {
        return createRipple(android.R.color.transparent, pressed);
    }

    public static Drawable createRippleNormal(@ColorRes int normal) {
        return createRipple(normal, android.R.color.darker_gray);
    }

    public static Drawable createRipple() {
        return createRipple(android.R.color.transparent, android.R.color.darker_gray);
    }

    public static Drawable createRipple(@ColorRes int normal, @ColorRes int pressed) {
        return createRipple(pressed, normal, pressed);
    }

    public static Drawable createRipple(@ColorRes int rippleColor, @ColorRes int normal, @ColorRes int pressed) {
        Drawable normalDrawable = new ColorDrawable(MyApp.getInstance().getResources().getColor(normal));
        Drawable pressedDrawable = new ColorDrawable(MyApp.getInstance().getResources().getColor(pressed));
        return createRipple(rippleColor, normalDrawable, pressedDrawable);
    }

    public static Drawable createRipple(@ColorRes int rippleColor, Drawable normalDrawable, Drawable pressedDrawable) {
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[]{-android.R.attr.state_enabled}, pressedDrawable);
        drawable.addState(new int[]{}, normalDrawable);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ColorStateList colorList = ColorStateList.valueOf(MyApp.getInstance().getResources().getColor(rippleColor));
            return new RippleDrawable(colorList, normalDrawable.getAlpha() == 0 ? null : normalDrawable,
                    normalDrawable.getAlpha() == 0 ? normalDrawable : null);
        } else {
            drawable.addState(new int[]{android.R.attr.state_pressed},
                    pressedDrawable);
            drawable.addState(new int[]{android.R.attr.state_focused},
                    pressedDrawable);
            drawable.addState(new int[]{android.R.attr.state_selected},
                    pressedDrawable);
            return drawable;
        }
    }

    public static void setCompoundDrawables(TextView view, Drawable drawLeft,
                                            Drawable drawTop,
                                            Drawable drawRight,
                                            Drawable drawBottom) {
        if (drawLeft != null) {
            drawLeft.setBounds(0, 0, drawLeft.getMinimumWidth(), drawLeft.getMinimumHeight());
        }
        if (drawTop != null) {
            drawTop.setBounds(0, 0, drawTop.getMinimumWidth(), drawTop.getMinimumHeight());
        }
        if (drawRight != null) {
            drawRight.setBounds(0, 0, drawRight.getMinimumWidth(), drawRight.getMinimumHeight());
        }
        if (drawBottom != null) {
            drawBottom.setBounds(0, 0, drawBottom.getMinimumWidth(), drawBottom.getMinimumHeight());
        }
        view.setCompoundDrawables(drawLeft, drawTop, drawRight, drawBottom);
    }

    public static void setBackgroundCompat(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }
}
