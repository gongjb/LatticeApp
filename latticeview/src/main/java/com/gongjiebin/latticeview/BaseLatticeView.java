package com.gongjiebin.latticeview;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * @author gongjiebin
 * @version 1.0
 * 格子视图基类
 * <p>
 * 需要实现有规则的布局必须直接或者间接继承此类。
 */
public abstract class BaseLatticeView extends LinearLayout {

    public ImageTextParams imageTextBeanParams;

    public Context mContext;

    public LinearLayout ll_lattice;

    public BaseLatticeView(Context context) {
        super(context);
        initView(context);
    }

    public BaseLatticeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public BaseLatticeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public BaseLatticeView setImageTextParams(ImageTextParams imageTextParams) {
        this.imageTextBeanParams = imageTextParams;
        return this;
    }


    /**
     * 加载视图（初始化时调用）
     */
    public abstract void loadView();


    /**
     * 开始绘制视图
     *
     * @return true 绘制成功，
     */
    public abstract boolean startView();


    public void initView(Context context) {
        this.mContext = context;
        ll_lattice = this;
        LayoutParams mainLayout = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        ll_lattice.setOrientation(LinearLayout.VERTICAL); //
        ll_lattice.setLayoutParams(mainLayout);
        loadView();
    }


    /**
     * 清除view
     */
    public void removeViews() {
        if (ll_lattice != null && ll_lattice.getChildCount() > 0) ll_lattice.removeAllViews();
    }

    /**
     * 判断字符串是否由数字所组成
     *
     * @param temp 要判断的字符串数据
     * @return 如果字符串由数字组成返回true，否则返回false
     */
    public boolean isNumber(String temp) {
        if (TextUtils.isEmpty(temp)) return false;
        char[] data = temp.toCharArray();         // 将字符串变为字符数组，可以取出每一位字符进行判断
        for (int x = 0; x < data.length; x++) {   // 循环判断
            if (data[x] > '9' || data[x] < '0') { // 不是数字字符范围
                return false;                     // 后续不再判断
            }
        }
        return true;                        // 如果全部验证通过返回true
    }


    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);

    }


    /**
     * 抛出点击事件
     */
    public OnPageItemOnClickListener onPageItemOnClickListener;

    /**
     * 抛出长按点击事件
     */
    public OnPageItemOnLongClickListener onPageItemOnLongClickListener;


    public OnPageItemOnLongClickListener getOnPageItemOnLongClickListener() {
        return onPageItemOnLongClickListener;
    }

    public void setOnPageItemOnLongClickListener(OnPageItemOnLongClickListener onPageItemOnLongClickListener) {
        this.onPageItemOnLongClickListener = onPageItemOnLongClickListener;
    }

    public void setOnPageItemOnClickListener(OnPageItemOnClickListener onPageItemOnClickListener) {
        this.onPageItemOnClickListener = onPageItemOnClickListener;
    }

    public OnPageItemOnClickListener getOnPageItemOnClickListener() {
        return onPageItemOnClickListener;
    }

    /**
     * 页面监听点击事件
     */
    public interface OnPageItemOnClickListener {
        /**
         * @param v        当前点击的view 视图
         * @param urls     如果有图片携带的是图片路径/如果是文字就是String数组
         * @param position 点击的位置
         */
        void onClick(View v, Object[] urls, int position);
    }


    /**
     * 页面监听长按事件
     */
    public interface OnPageItemOnLongClickListener {
        /**
         * @param v        当前点击的view 视图
         * @param urls     如果有图片携带的是图片路径/如果是文字就是String数组
         * @param position 点击的位置
         */
        void onLongClick(View v, Object[] urls, int position);
    }


    /**
     * 参数设置
     *
     * @param <T> 图片加载器
     */
    public static class ImageTextParams<T extends ImageLoader> {
        public T imageLoader; // 设置图片加载器（必须）
        // 图片集合(ids)
        public Object[] images;
        // 选中的图片集合
        public Object[] selectImages;
        // 图片下面的文字集合
        public String[] text;
        // 每个布局对应的ID
        public int[] latticeIds;
        // 一行最多显示几个
        public int maxLine;
        // 背景颜色(默认白色)
        public int bg_color = android.R.color.white;
        // 字体颜色/初始颜色
        public int textColor;
        // 字体选中之后的颜色
        public int textSelectColor;
        //字体选中时是否需要加粗, 如果用户设置了默认粗体显示（isTextBold = true），不会调用切换样式
        public boolean textSelectIsBold;
        // 字体大小/初始大小 sp
        public int textSize;
        // 字体选中之后的
        public int textSelectSize;
        // 字体向上 padding距离
        public int textPaddingTop;
        // 字体是否加粗
        public boolean isTextBold;
        // true, 表示都是隐藏状态
        public boolean isHide;

        /*
        selectIndex为-1时将不显示滑块

        如果有默认被选中的，可以设置此参数， 设置此参数第一次打开页面，onPageItemOnClickListener中的onClick方法将会被调用

        如果你想设置你的tab第2个默认第一个被加载， 请将selectIndex设置为1

        如果你设置selectIndex 大于 text数组中的数量，此设置将会失效。

        */
        public int selectIndex = -1;

        public T getImageLoader() {
            return imageLoader;
        }

        public ImageTextParams setImageLoader(T imageLoader) {
            this.imageLoader = imageLoader;
            return this;
        }

        public Object[] getImages() {
            return images;
        }

        public ImageTextParams setImages(Object[] images) {
            this.images = images;
            return this;
        }

        public String[] getText() {
            return text;
        }

        public ImageTextParams setText(String[] text) {
            this.text = text;
            return this;
        }

        public int[] getLatticeIds() {
            return latticeIds;
        }

        public ImageTextParams setLatticeIds(int[] latticeIds) {
            this.latticeIds = latticeIds;
            return this;
        }

        public int getMaxLine() {
            return maxLine;
        }

        public ImageTextParams setMaxLine(int maxLine) {
            this.maxLine = maxLine;
            return this;
        }

        public int getBg_color() {
            return bg_color;
        }

        public ImageTextParams setBg_color(int bg_color) {
            this.bg_color = bg_color;
            return this;
        }

        public int getTextColor() {
            return textColor;
        }

        public ImageTextParams setTextColor(int textColor) {
            this.textColor = textColor;
            return this;
        }

        public int getTextSelectColor() {
            return textSelectColor;
        }

        public ImageTextParams setTextSelectColor(int textSelectColor) {
            this.textSelectColor = textSelectColor;
            return this;
        }

        public boolean isTextSelectIsBold() {
            return textSelectIsBold;
        }

        public ImageTextParams setTextSelectIsBold(boolean textSelectIsBold) {
            this.textSelectIsBold = textSelectIsBold;
            return this;
        }

        public int getTextSize() {
            return textSize;
        }

        public ImageTextParams setTextSize(int textSize) {
            this.textSize = textSize;
            return this;
        }

        public int getTextSelectSize() {
            return textSelectSize;
        }

        public ImageTextParams setTextSelectSize(int textSelectSize) {
            this.textSelectSize = textSelectSize;
            return this;
        }

        public int getTextPaddingTop() {
            return textPaddingTop;
        }

        public ImageTextParams setTextPaddingTop(int textPaddingTop) {
            this.textPaddingTop = textPaddingTop;
            return this;
        }

        public boolean isTextBold() {
            return isTextBold;
        }

        public ImageTextParams setTextBold(boolean textBold) {
            isTextBold = textBold;
            return this;
        }

        public boolean isHide() {
            return isHide;
        }

        public ImageTextParams setHide(boolean hide) {
            isHide = hide;
            return this;
        }
    }

}
