package com.gongjiebin.latticeapp;

import android.content.Context;

import android.widget.ImageView;


import com.bumptech.glide.Glide;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.gongjiebin.latticeview.ImageLoader;


/**
 *  定义图片加载器。 这里我使用的是Glide图片加载器
 *
 *  如果要对图片进行不同的处理， 可以再次实现ImageLoader接口
 */
public class GlideImageLoader implements ImageLoader {

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {



        Glide.with(context).load(path)
                .transform(new CenterCrop(), new RoundedCorners(10))
                .into(imageView);
    }


    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    public void displayDrawableImage(Context context, Integer path, ImageView imageView) {
        Glide.with(context).load(path)
               // .diskCacheStrategy(DiskCacheStrategy.DATA)
                .transform(new CenterCrop(), new RoundedCorners(10))
                .into(imageView);
    }



}
