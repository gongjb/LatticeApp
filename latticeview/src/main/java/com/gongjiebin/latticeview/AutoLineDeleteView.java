package com.gongjiebin.latticeview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * @param <T>
 * @author gongjiebin
 * 编辑自动换行的控件
 */
public class AutoLineDeleteView<T extends KVBean> extends AutoLineLayout {


    private AutoEditParams editParams;

    public void setEditParams(AutoEditParams editParams) {
        this.editParams = editParams;
    }

    public AutoLineDeleteView(Context context) {
        super(context);
    }

    public AutoLineDeleteView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoLineDeleteView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean startView() {
        if (editParams == null) return false;
        if (views == null || views.size() == 0) return false;
        if (getW() == 0) return false;

        // 创建view
        createViews(views, 0);

        return true;
    }


    /**
     * 采用递归创建行数
     */
    public void createViews(List<T> views, int position) {
        if (views == null || views.size() == 0) return;
        LinearLayout linearLayout = new LinearLayout(mContext);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL); //
        //linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        linearLayout.setPadding(0, 10, 0, 10);
        linearLayout.setLayoutParams(layoutParams);
        List<T> viewList = new ArrayList<>();
        viewList.addAll(views);

        List<T> delStr = new ArrayList<>();
        int views_w = 0; //
        for (int i = 0; i < viewList.size(); i++) {
            final T view = viewList.get(i);
            if (view != null) {
                View v_main = View.inflate(mContext, R.layout.item_auto_list_layout, null);
                TextView textView = v_main.findViewById(R.id.tv_tag_name);
                textView.setText(view.value);

                if (editParams.textSize != 0)
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, editParams.textSize);
                if (editParams.textColor != 0)
                    textView.setTextColor(mContext.getResources().getColor(editParams.getTextColor()));
                if (editParams.bg_color != 0)
                    textView.setBackgroundResource(editParams.getBg_color());


                ImageView iv_del = v_main.findViewById(R.id.iv_del);
                if(editParams.isShowDelImg){
                    iv_del.setVisibility(View.VISIBLE);
                    if (editParams.IsDelImgLeft) {
                        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                        iv_del.setLayoutParams(params);
                    }

                    if (editParams.delImg != 0)
                        iv_del.setImageResource(editParams.delImg);
                }else{
                    iv_del.setVisibility(View.GONE);
                }

                // 绑定删除事件
                iv_del.setOnClickListener(onDeleteTbs(view));
                // 绑定点击事件
                textView.setOnClickListener(getOnItemClickListener(view));
                float textWidth = textView.getPaint().measureText(viewList.get(i).value) + dip2px(mContext, 20);
                views_w += textWidth;
                if (views_w > getW()) {
                    if (i == 0) {
                        delStr.add(viewList.get(i));
                        linearLayout.addView(v_main, i);
                    } else {
                        break;
                    }
                } else {
                    delStr.add(viewList.get(i));
                    linearLayout.addView(v_main, i);
                }
            }
        }

        for (int i = 0; i < delStr.size(); i++) {
            viewList.remove(delStr.get(i));
        }
        ll_lattice.addView(linearLayout, position);
        createViews(viewList, position + 1); // 递归调用
    }


    public View.OnClickListener onDeleteTbs(final T s) {
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDelectTagListener != null) {
                    onDelectTagListener.onDel(v, s);
                }
            }
        };
        return onClickListener;
    }


    private OnDelectTagListener onDelectTagListener;

    public void setOnDelectTagListener(OnDelectTagListener onDelectTagListener) {
        this.onDelectTagListener = onDelectTagListener;
    }

    public interface OnDelectTagListener<T extends KVBean> {
        void onDel(View v, T bean/**/);
    }


    /**
     * @author gongjiebin
     */
    public static class AutoEditParams extends BaseLatticeView.ImageTextParams {
        /**
         * 你可以设定你喜欢的删除图片，不设置的话默认使用本系统的
         */
        public int delImg;

        /**
         * 删除图片是不是放在左边, true 则显示在左上角
         */
        public boolean IsDelImgLeft;

        /**
         *  是否显示删除图片,默认显示
         */
        public boolean isShowDelImg = true;
    }
}
