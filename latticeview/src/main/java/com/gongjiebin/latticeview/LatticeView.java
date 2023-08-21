package com.gongjiebin.latticeview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * /**
 *
 * @author 龚洁斌
 * @version v1.0 2021/06/12
 * <p>
 * 格子视图= 用于普通排列的选项卡，上图下字
 */
public class LatticeView extends BaseLatticeView {

    // 存放文字控件
    public List<TextView> textViews;
    //存放图片控件
    public List<ImageView> imageViews;
    // textViews | imageViews 的父亲
    public List<LinearLayout> linearLayouts;

    public LatticeView(Context context) {
        super(context);
    }

    public LatticeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LatticeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public void loadView() {
        textViews = new ArrayList<>();
        imageViews = new ArrayList<>();
        linearLayouts = new ArrayList<>();
    }


    /**
     * 加载控件，
     *
     * @return
     */
    public boolean startView() {

        if (imageTextBeanParams == null) return false;
        // 必须设置图片数组。
        if (imageTextBeanParams.images == null) return false;
        // 必须设置文字数组
        if (imageTextBeanParams.text == null) return false;
        // 图片数组的数量与文字数组一定要一致
        if (imageTextBeanParams.images.length != imageTextBeanParams.text.length) return false;
        // 必须设置一行显示多少个view
        if (imageTextBeanParams.maxLine <= 0) return false;
        // 可以不设置id,但是如果设置了id, id的数量就要与图片数组的数量保持一致
        if (imageTextBeanParams.latticeIds != null && imageTextBeanParams.latticeIds.length != imageTextBeanParams.images.length)
            return false;

        linearLayouts.clear();
        textViews.clear();
        imageViews.clear();
        double viewNumber = imageTextBeanParams.images.length;
        // view 的数量 / 每列显示最大个数
        double line = viewNumber / (double) imageTextBeanParams.maxLine;

        int countLine = (int) Math.ceil(line); // 向上取整，得到总行数

        for (int i = 0; i < countLine; i++) {
            // 循环加入行 LinearLayout视图
            LinearLayout linearLayout = new LinearLayout(mContext);
            LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL); //
            //linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            linearLayout.setPadding(0, dip2px(getContext(), 6), 0, dip2px(getContext(), 6));
            linearLayout.setLayoutParams(layoutParams);

            int intstart = (i) * imageTextBeanParams.maxLine; // 数组开始的位置
            int intend = (i + 1) * imageTextBeanParams.maxLine; // 数组结束的位置
            int num;
            if (intend > imageTextBeanParams.images.length)// 最后一行了
                num = (imageTextBeanParams.images.length % imageTextBeanParams.maxLine);
            else num = imageTextBeanParams.maxLine;


            Object[] lineImage = new Object[imageTextBeanParams.maxLine];
            String[] lineText = new String[imageTextBeanParams.maxLine];
            int[] lattIds = new int[imageTextBeanParams.maxLine];
            if (imageTextBeanParams.latticeIds == null) {
                imageTextBeanParams.latticeIds = new int[imageTextBeanParams.images.length];
                for (int ids = 0; ids < imageTextBeanParams.latticeIds.length; ids++) {
                    imageTextBeanParams.latticeIds[ids] = ids; // 给出默认id.
                }
            }

            System.arraycopy(imageTextBeanParams.latticeIds, intstart, lattIds, 0, num);
            System.arraycopy(imageTextBeanParams.images, intstart, lineImage, 0, num);
            System.arraycopy(imageTextBeanParams.text, intstart, lineText, 0, num);


            for (int j = 0; j < lineImage.length; j++) {
                LinearLayout lio = new LinearLayout(mContext);
                LayoutParams imageparams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 3.0f);
                lio.setOrientation(LinearLayout.VERTICAL); //
                lio.setGravity(Gravity.CENTER);
                //lio.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                lio.setLayoutParams(imageparams);
                if (!TextUtils.isEmpty(lineText[j])) {
                    lio.setId(lattIds[j]);
                    ImageView imageView = new ImageView(mContext);
                    LayoutParams ir;
                    if (imageTextBeanParams.imageWidth != 0 && imageTextBeanParams.imageHigh != 0) {
                        ir = new LayoutParams(dip2px(getContext(), imageTextBeanParams.imageWidth), dip2px(getContext(), imageTextBeanParams.imageHigh));
                    } else {
                        ir = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                    }
                    if (imageTextBeanParams.imageType != null) {
                        imageView.setScaleType(imageTextBeanParams.imageType);
                    }
                    imageView.setLayoutParams(ir);
                    imageTextBeanParams.imageLoader.displayImage(mContext, lineImage[j],imageView);
                    lio.addView(imageView, 0);
                    imageViews.add(imageView);
                    TextView textView = new TextView(mContext);
                    LayoutParams tr = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                    textView.setLayoutParams(tr);
                    textView.setText(lineText[j]);
                    textViews.add(textView);// 存入集合中，
                    if (imageTextBeanParams.textColor != 0)
                        textView.setTextColor(mContext.getResources().getColor(imageTextBeanParams.textColor));
                    if (imageTextBeanParams.textSize != 0)
                        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, imageTextBeanParams.textSize);
                    if (imageTextBeanParams.isTextBold)
                        textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    if (imageTextBeanParams.textPaddingTop != 0)
                        textView.setPadding(0, imageTextBeanParams.textPaddingTop, 0, 0);
                    lio.addView(textView, 1);

                    lio.setClickable(true);
                    int index = i * imageTextBeanParams.maxLine + j;
                    lio.setOnClickListener(getOnItemClickListener(imageTextBeanParams.images, index));
                    lio.setOnLongClickListener(getOnItemLongClickListener(imageTextBeanParams.images, index));
                    linearLayouts.add(lio);
                }

                if (imageTextBeanParams.isHide) lio.setVisibility(View.INVISIBLE);
                linearLayout.addView(lio, j);
            }
            ll_lattice.addView(linearLayout, i);
        }

        if (imageTextBeanParams.bg_color_int != 0) {
            ll_lattice.setBackgroundColor(imageTextBeanParams.bg_color_int);
        } else {
            ll_lattice.setBackgroundColor(Color.parseColor(imageTextBeanParams.bg_color));
        }

        if (imageTextBeanParams.selectIndex != -1 && imageTextBeanParams.selectIndex < imageTextBeanParams.text.length) {

            selectOnClick(imageTextBeanParams.selectIndex);
        }
        return true;
    }


    /**
     * 注册点击事件， 如果在页面中直接注册，点击事件是没办法生效的。
     */
    public interface OnItemClickListener extends OnClickListener {
    }


    public OnItemClickListener getOnItemClickListener(final Object[] urls, final int position) {
        OnItemClickListener onItemClickListener = new OnItemClickListener() {
            @Override
            public void onClick(View v) {
                selectOnClick(position);
                ImageView imageView = imageViews.get(position);
                if (imageTextBeanParams.animation != null) {
                    imageView.startAnimation(imageTextBeanParams.animation);
                }
                if (onPageItemOnClickListener != null) {
                    onPageItemOnClickListener.onClick(v, urls, position);
                    onPageItemOnClickListener.onClick(v, imageView, urls, position);
                }
            }
        };
        return onItemClickListener;
    }




    public OnLongClickListener getOnItemLongClickListener(final Object[] urls, final int position) {
        OnLongClickListener onItemClickListener = new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onPageItemOnLongClickListener != null) {

                    onPageItemOnLongClickListener.onLongClick(v, urls, position);
                }
                // 如返回false, 事件分发导致会接连调用onClick事件
                return true;
            }
        };
        return onItemClickListener;
    }

    /**
     * 改变字体颜色
     *
     * @param position
     */
    public void changeTextColor(int position, int textColor, int textSelectColor) {
        if (textViews != null) {
            for (int i = 0; i < textViews.size(); i++) {
                TextView textView = textViews.get(i);
                if (textView == null) continue;
                if (position == i) {
                    // 点击选中的
                    textView.setTextColor(mContext.getResources().getColor(textSelectColor));
                } else {
                    // 其它的恢复原状
                    textView.setTextColor(mContext.getResources().getColor(textColor));
                }
            }
        }
    }


    /**
     * 改变textView 的字体样式
     *
     * @param position        被选中的textView
     * @param textStyle       未被选中的默认样式 取值 Typeface.NORMAL ｜ Typeface.BOLD
     * @param textSelectStyle 选中之后的样式 取值 Typeface.NORMAL ｜ Typeface.BOLD
     *                        <p>
     *                        textStyle == textSelectStyle 样式就不会发生变化。
     */
    public void changeTextStyle(int position, int textStyle, int textSelectStyle) {
        //textSelectIsBold
        if (textViews != null) {
            for (int i = 0; i < textViews.size(); i++) {
                TextView textView = textViews.get(i);
                if (textView == null) continue;
                if (position == i) {
                    // 点击选中的
                    textView.setTypeface(Typeface.defaultFromStyle(textSelectStyle));
                } else {
                    // 其它的恢复原状
                    textView.setTypeface(Typeface.defaultFromStyle(textStyle));
                }
            }
        }
    }


    /**
     * 改变字体大小
     *
     * @param position
     */
    public void changeTextSize(int position, int textSize, int textSelectSize) {
        if (textViews != null) {
            for (int i = 0; i < textViews.size(); i++) {
                TextView textView = textViews.get(i);
                if (position == i) {
                    // 点击选中的
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSelectSize);
                } else {
                    // 其它的恢复原状
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
                }
            }
        }
    }


    /**
     * 改变图片
     *
     * @param position
     */
    public void changeImage(int position) {
        if (imageViews != null && imageTextBeanParams.selectImages != null) {
            for (int i = 0; i < imageViews.size(); i++) {
                ImageView imageView = imageViews.get(i);
                if (imageTextBeanParams.animation != null) {
                    imageView.clearAnimation();
                }
                if (position == i) {
                    // 点击选中的
                    imageTextBeanParams.imageLoader.displayImage(mContext, imageTextBeanParams.selectImages[i], imageView);
                } else {
                    // 其它的恢复原状
                    imageTextBeanParams.imageLoader.displayImage(mContext, imageTextBeanParams.images[i], imageView);
                }
            }
        }
    }


    public List<String> removeNullStringArray(String[] arrayString) {
        List<String> list1 = new ArrayList<>();
        for (int i = 0; i < arrayString.length; i++) {
            if (arrayString[i] != null && arrayString[i].length() != 0) { //过滤掉数组arrayString里面的空字符串
                list1.add(arrayString[i]);
            }
        }
        return list1;
    }


    /**
     * @param position
     */
    public void selectOnClick(int position) {
        if (imageTextBeanParams != null && imageTextBeanParams.textSelectColor != 0) {
            // 做textView切换颜色的操作
            changeTextColor(position, imageTextBeanParams.textColor, imageTextBeanParams.textSelectColor);
        }

        if (imageTextBeanParams != null && imageTextBeanParams.textSelectSize != 0) {
            // 切换textView 的大小
            changeTextSize(position, imageTextBeanParams.textSize, imageTextBeanParams.textSelectSize);
        }

        if (imageTextBeanParams != null
                && imageTextBeanParams.selectImages != null
                && imageTextBeanParams.selectIndex != -1) {
            // 改变图片
            changeImage(position);
        }
    }
}
