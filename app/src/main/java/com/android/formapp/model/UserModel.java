package com.android.formapp.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.android.formapp.BR;

import java.io.Serializable;

/**
 *
 */
public class UserModel extends BaseObservable implements Serializable {
    private String name;

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }
}
