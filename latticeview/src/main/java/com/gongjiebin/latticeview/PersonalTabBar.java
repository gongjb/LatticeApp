package com.gongjiebin.latticeview;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author gongjiebin
 * @version v1.0
 *
 * 一般用于顶部的tab或者底部的tab
 *
 * 提供底部滚动条平移动画，只支持屏幕内移动，只支持横向移动
 *
 * 支持横向排列/竖向排列
 */
public class PersonalTabBar extends LatticeView {

    private TextParams params;

    private ImageView slideView;

    private float fromXDelta; // 动画启动时x的位置

    /**
     *  可选值 LinearLayout.HORIZONTAL ｜ LinearLayout.VERTICAL
     */
    private int direction = LinearLayout.HORIZONTAL; // 默认横向显示

    public void setDirection(int direction) {
        this.direction = direction;
    }


    public void setParams(TextParams params) {
        this.params = params;
    }

    public PersonalTabBar(Context context) {
        super(context);
    }

    public PersonalTabBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PersonalTabBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



    /**
     * 加载控件，
     *
     * @return
     */
    public boolean startView() {
        if (params == null) return false;
        // 必须设置文字数组
        if (params.text == null) return false;
        if (params.width == 0) return false;
        // 可以不设置id,但是如果设置了id, id的数量就要与text数组的数量保持一致
        if (params.latticeIds != null && params.latticeIds.length != params.text.length)
            return false;


        textViews.clear();
        // 开始添加视图
        LinearLayout linearLayout = new LinearLayout(mContext);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        linearLayout.setOrientation(direction); //
        //linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        linearLayout.setPadding(0, 10, 0, 10);
        linearLayout.setGravity(Gravity.CENTER_VERTICAL);
        linearLayout.setLayoutParams(layoutParams);


        for (int i = 0; i < params.text.length; i++) {
            // 向linearLayout 添加 textView
            LayoutParams llparame = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 3.0f);
            TextView textView = new TextView(mContext);
            textView.setLayoutParams(llparame);
            // 设置text的上、下间距
            textView.setPadding(0, params.textTBPadding, 0, params.textTBPadding);
            // 设置布局居中
            textView.setGravity(Gravity.CENTER);
            textView.setText(params.text[i]);
            if (params.textColor != 0)
                textView.setTextColor(mContext.getResources().getColor(params.textColor));
            if (params.textSize != 0)
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, params.textSize);
            if (params.isTextBold) // 整体的字体都被加粗显示
                textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

            if (params.textPaddingTop != 0)
                textView.setPadding(0, params.textPaddingTop, 0, 0);

            textViews.add(textView);
            // 设置点击事件
            textView.setOnClickListener(getOnItemClickListener(params.text, i));
            linearLayout.addView(textView, i);
        }


        /**
         *  添加滑动标签 / selectIndex==-1,表示用户不需要滑动标签
         */
        LinearLayout slideLinear = new LinearLayout(mContext);
        LayoutParams slideParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        slideLinear.setOrientation(LinearLayout.HORIZONTAL); //
        //linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        slideLinear.setPadding(0, 10, 0, 10);
        slideLinear.setGravity(Gravity.CENTER_VERTICAL);
        slideLinear.setLayoutParams(slideParams);
        if (params.selectIndex != -1) {
            slideView = new ImageView(mContext);
            int wnum = params.width / params.text.length;
            LayoutParams viewParams = new LayoutParams(wnum, dip2px(mContext, 5));
            // 添加样式。。。
            slideView.setImageResource(params.slideStyle);
            slideView.setLayoutParams(viewParams);
            if (params.slideStyleLRPadding == 0) {
                // 计算单个滑块的padding值
                int sw = (params.width / params.text.length) / 3;
                params.slideStyleLRPadding = sw;
            }
            if (params.slideStyleLRPadding != 0)
                slideView.setPadding(params.slideStyleLRPadding, 0, params.slideStyleLRPadding, 0);
            slideLinear.addView(slideView);
        }

        ll_lattice.addView(linearLayout, 0);
        ll_lattice.addView(slideLinear, 1);
        if (params.bg_color != 0)
            ll_lattice.setBackgroundColor(mContext.getResources().getColor(params.bg_color));


        if (params.selectIndex != -1 && params.selectIndex < params.text.length) {
            selectOnClick(params.selectIndex);
        }

        return true;
    }


    /**
     * 指定显示的项，但不会调用OnPageItemOnClickListener.onclick()
     * <p>
     * 此方法，请在 view加载完成之后跳用
     * <p>
     * view.post(); 方法中调用
     */
    public void showItemPosition(int position) {
        if (params != null) {
            if (slideView != null && params.selectIndex!=-1) {
                final int toLine = (slideView.getWidth() * position); // 移动的位置
                TranslateAnimation animation = new TranslateAnimation(fromXDelta, toLine, 0, 0);
                animation.setDuration(params.duration);//设置动画持续时间
//        animation.setRepeatCount(1);//设置重复次数
                animation.setFillEnabled(true);//使其可以填充效果从而不回到原地
                animation.setFillAfter(true);//不回到起始位置
                setAnimationListener(animation, toLine);
                slideView.startAnimation(animation);
            }

            if (params.textSelectColor != 0) {
                // 做textView切换颜色的操作
                changeTextColor(position, params.textColor, params.textSelectColor);
            }

            if(params.textSelectIsBold){
                if(!params.isTextBold){
                    // 用户设置了默认粗体显示，不会调用切换样式
                    changeTextStyle(position,Typeface.NORMAL,Typeface.BOLD);
                }
            }

            if (params.textSelectSize != 0) {
                // 切换textView 的颜色
                changeTextSize(position, params.textSize, params.textSelectSize);
            }
        }
    }


    /**
     * 重写注册监听事件。 要在本类做一些动画操作
     */
    public LatticeView.OnItemClickListener getOnItemClickListener(final Object[] urls, final int position) {
        LatticeView.OnItemClickListener onItemClickListener = new LatticeView.OnItemClickListener() {
            @Override
            public void onClick(final View v) {
                showItemPosition(position);
                if (getOnPageItemOnClickListener() != null) {
                    getOnPageItemOnClickListener().onClick(v, urls, position);
                }
            }
        };
        return onItemClickListener;
    }

    @Override
    public void selectOnClick(int position) {
        //super.selectOnClick(position);
        if (textViews != null) {
            for (int i = 0; i < textViews.size(); i++) {
                TextView textView = textViews.get(i);
                if (position == i) {
                    // 点击选中的
                    textView.performClick(); // 触发点击事件
                }
            }
        }
    }

    /**
     * 设置动画的监听事件
     *
     * @param animation
     * @param toLine
     */
    public void setAnimationListener(TranslateAnimation animation, final int toLine) {
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                fromXDelta = toLine;
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


    /**
     * 设置属性Text,
     */
    public static class TextParams extends LatticeView.ImageTextParams {
        /**
         * 字体下面有一个滑动的图片的样式。
         * <p>
         * 就是一张背景图，或者是drawable中的一个xml文件
         */
        public int slideStyle;
        /**
         * 设置滑动标签的左右边距
         * <p>
         * 如果不设置， 适配字体宽度显示
         */
        public int slideStyleLRPadding;
        /**
         * 设置textView的上下间距
         */
        public int textTBPadding;


        //本控件的宽度，或者屏幕的宽度。或者设置一个合适的值
        public int width;// （必须）

        public long duration = 400; // 动画执行时长
    }
}
