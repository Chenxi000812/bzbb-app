package com.example.bzbb.Handler;

import android.app.Activity;
import android.content.Context;

import android.os.Handler;

public abstract class MyHandler extends Handler{

    protected Activity activity;

    MyHandler(Activity activity) {
        this.activity = activity;
    }
}
