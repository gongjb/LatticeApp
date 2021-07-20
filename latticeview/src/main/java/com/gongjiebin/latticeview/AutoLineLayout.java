package com.gongjiebin.latticeview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gongjiebin.latticeview.LatticeView;

import java.util.ArrayList;
import java.util.List;

/**
 * 自动换行
 */
public class AutoLineLayout<T extends KVBean> extends LatticeView {

    /**
     * 存放已经绘制出来的views
     */
    public List<T> views;

    private int w;

    public AutoLineLayout(Context context) {
        super(context);
    }

    public AutoLineLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoLineLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public void setViews(List<T> views) {
        this.views = views;
    }

    public List<T> getViews() {
        return views;
    }


    @Override
    public void removeViews() {
        super.removeViews();
    }



    @Override
    public boolean startView() {
        if (views == null || views.size() == 0) return false;

        if (getW() == 0) return false;
        // 创建view
        createView(getViews(), 0);

        return true;
    }




    /**
     * 采用递归创建行数
     */
    public void createView(List<T> views, int position) {
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
                TextView textView = new TextView(mContext);
                LinearLayout.LayoutParams tr = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                tr.setMargins(dip2px(mContext, 2), 0, dip2px(mContext, 2), 0);
                textView.setLayoutParams(tr);
                textView.setPadding(dip2px(mContext, 5), dip2px(mContext, 5), dip2px(mContext, 5), dip2px(mContext, 5));
                textView.setText(viewList.get(i).value);

                if(imageTextBeanParams.textSize!=0)
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, imageTextBeanParams.textSize);
                if(imageTextBeanParams.getTextColor()!=0)
                textView.setTextColor(mContext.getResources().getColor(imageTextBeanParams.getTextColor()));
                if(imageTextBeanParams.getBg_color()!=0)
                textView.setBackgroundResource(imageTextBeanParams.getBg_color());

                textView.setOnClickListener(getOnItemClickListener(view));

                float textWidth = textView.getPaint().measureText(viewList.get(i).value) + dip2px(mContext, 25);
                views_w += textWidth;
                if (views_w > getW()) {
                    if (i == 0) {
                        delStr.add(viewList.get(i));
                        linearLayout.addView(textView, i);
                    } else {
                        break;
                    }
                } else {
                    delStr.add(viewList.get(i));
                    linearLayout.addView(textView, i);
                }
            }
        }

        for (int i = 0; i < delStr.size(); i++) {
            viewList.remove(delStr.get(i));
        }
        ll_lattice.addView(linearLayout, position);

        this.createView(viewList, position + 1); // 递归调用
    }


    /**
     * bind item onclick
     */
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener<T extends KVBean>{
       void onClickItem(View view,T t);
    }

    public LatticeView.OnItemClickListener getOnItemClickListener(final T kv) {
        LatticeView.OnItemClickListener onItemClickListener = new LatticeView.OnItemClickListener(){
            @Override
            public void onClick(View v) {
                if(AutoLineLayout.this.onItemClickListener!=null){
                    AutoLineLayout.this.onItemClickListener.onClickItem(v,kv);
                }
            }
        };

        return onItemClickListener;
    }



}
