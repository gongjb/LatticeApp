package com.gongjiebin.latticeview;

import android.content.Context;
import android.widget.ImageView;

/**
 * @author gongjiebin
 * @version v1.0 2021/06/12
 */
public interface ImageLoader {

    /**
     *  加载文件中或者网络图片
     * @param context
     * @param path 文件路径
     * @param imageView 图片控件
     */
     void displayImage(Context context, Object path, ImageView imageView);


    /**
     *  加载 本地mipmap中的图片
     */
     void displayDrawableImage(Context context, Integer path, ImageView imageView);

}
