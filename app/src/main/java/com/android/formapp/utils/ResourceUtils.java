package com.android.formapp.utils;

import android.content.Context;


import com.android.formapp.R;

import java.lang.reflect.Field;

/**
 * 资源查找工具类
 *
 * @作者: caoyl
 * @创建日期: 2019/8/29 9:20
 */
public class ResourceUtils {

    public static int getResource(Context context, String imgName) {
        int resId = context.getResources().getIdentifier(imgName, "drawable", context.getPackageName());
        if (resId > 0) {
            return resId;
        }
        return -1;
    }

    public static int getResByReflect(String imgName) {
        Class drawable = R.drawable.class;
        Field field = null;
        int resId = 0;
        try {
            field = drawable.getField(imgName);
            resId = field.getInt(field.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resId;
    }
}
