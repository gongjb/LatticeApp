package com.gongjiebin.latticeview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
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
    public void loadView() {
        super.loadView();
    }

    @Override
    public boolean startView() {
        if (editParams == null) return false;
        if (views == null || views.size() == 0) return false;
        if (editParams.width  == 0) return false;
        textViews.clear();
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
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
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
                textView.setText(view.getValue());
                if(!view.isSel){
                    if(editParams.textSize!=0)
                        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, editParams.textSize);
                    if(editParams.getTextColor()!=0)
                        textView.setTextColor(mContext.getResources().getColor(editParams.getTextColor()));
                        textView.setBackgroundColor(Color.parseColor(editParams.bg_color));
                }else{
                    if(editParams.textSelectSize!=0)
                        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, editParams.textSelectSize);
                    if(editParams.textSelectColor!=0)
                        textView.setTextColor(mContext.getResources().getColor(editParams.textSelectColor));
                    if(editParams.select_bg_color!=0)
                        textView.setBackgroundResource(editParams.select_bg_color);
                }

                if(editParams.isTextBold){
                    // 加粗显示
                    textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                }

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
                float textWidth = textView.getPaint().measureText(viewList.get(i).getValue()) + dip2px(mContext, 20);
                views_w += textWidth;
                if (views_w > editParams.width ) {
                    if (i == 0) {
                        delStr.add(viewList.get(i));
                        /**
                         *  保存我们的textView以便切换状态
                         */
                        textView.setTag(view);
                        textViews.add(textView);
                        linearLayout.addView(v_main, i);
                    } else {
                        break;
                    }
                } else {
                    delStr.add(viewList.get(i));
                    /**
                     *  保存我们的textView以便切换状态
                     */
                    textView.setTag(view);
                    textViews.add(textView);
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





    public OnClickListener onDeleteTbs(final T s) {
        OnClickListener onClickListener = new OnClickListener() {
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







}
