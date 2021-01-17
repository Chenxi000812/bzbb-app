package com.example.bzbb.custom;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.bzbb.Utils.HttpUtils;
import com.youth.banner.loader.ImageLoader;

public class BannerImageLoad extends ImageLoader {
    //在该方法内用Glide进行加载图片
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        HttpUtils.loadImg(context,(String) path,imageView);
    }
}
