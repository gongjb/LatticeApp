package com.gongjiebin.latticeview;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

public class Image3DragDropPopWindow {


    /**
     * 有编辑框的pop
     * @param layout  视图
     * @return
     */
    public static PopupWindow createFunctionPopupWindow(final View layout) {

        final PopupWindow pop = new PopupWindow(layout,  LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        ColorDrawable cd = new ColorDrawable(-0000);
        pop.setBackgroundDrawable(cd);

        // 设置出现和消失样式
//        pop.setAnimationStyle(R.style.popwin_anim_style);

        pop.update();
        pop.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        pop.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        pop.setTouchable(true); // 设置popupwindow可点击
        pop.setOutsideTouchable(true); // 设置popupwindow外部可点击
        pop.setFocusable(true); // 获取焦点
        pop.showAtLocation(layout, Gravity.BOTTOM, 0, 0);

        // 设置动画
//        pop.setAlphaValueAnimation(1.0f, 0.5f);

        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
//mAdapter.getData().get(position).getInformationListBean()
                // implementation 'org.greenrobot:eventbus:3.1.1'
            }
        });


        pop.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.e("POST","111111111");
                /**** 如果点击了popupwindow的外部，popupwindow也会消失 ****/
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    pop.dismiss();
                    return true;
                }
                return false;
            }
        });





        return pop;
    }


    /**
     * 有编辑框的pop
     * @param layout  视图
     * @return
     */
    public static PopupWindow createFunctionPopupWindow(ImageLoader loader ,
                                                        final Context context, final View layout,
                                                        String path, final int w, final int h, int left, int top) {

        // 需要显示的view
       final RelativeLayout relativeLayout = layout.findViewById(R.id.rl_layout);
       final ImageView imageView = layout.findViewById(R.id.iv_content);
        //初始设置一个layoutParams
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(w,h);
        layoutParams.leftMargin = left;
        layoutParams.topMargin = top-LatticeView.dip2px(context,25);

        imageView.setLayoutParams(layoutParams);
        loader.displayImage(context,path,imageView);

        final PopupWindow pop = new PopupWindow(layout,  LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        ColorDrawable cd = new ColorDrawable(-0000);
        pop.setBackgroundDrawable(cd);

        // 设置出现和消失样式
//        pop.setAnimationStyle(R.style.popwin_anim_style);

        pop.update();
        pop.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        pop.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        pop.setTouchable(true); // 设置popupwindow可点击
        pop.setOutsideTouchable(true); // 设置popupwindow外部可点击
        pop.setFocusable(true); // 获取焦点
        pop.showAtLocation(layout, Gravity.BOTTOM, 0, 0);

        // 设置动画
//        pop.setAlphaValueAnimation(1.0f, 0.5f);

        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {

            }
        });


        pop.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                /**** 如果点击了popupwindow的外部，popupwindow也会消失 ****/
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    pop.dismiss();
                    return true;
                }
                return false;
            }
        });

        //设置屏幕触摸事件
        imageView.setOnTouchListener(getTouchListener(pop,relativeLayout,w));

        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                imageView.setFocusable(true);
                imageView.setFocusableInTouchMode(true);
                imageView.requestFocus();
                imageView.findFocus();
                return false;
            }
        });

        handler.sendEmptyMessageDelayed(0,500);
        return pop;
    }



    public static View.OnTouchListener getTouchListener(final  PopupWindow pop,final RelativeLayout relativeLayout, final int wh){
        View.OnTouchListener onTouchListener =  new View.OnTouchListener() {
            int lastX, lastY;    //保存手指点下的点的坐标

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        //将点下的点的坐标保存
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        if(pop!=null && pop.isShowing()){
                            lastX = (int) event.getRawX();
                            lastY = (int) event.getRawY();

                            pop.dismiss();
                        }
                        break;

                    case MotionEvent.ACTION_MOVE:

                        //计算出需要移动的距离
                        int dx = (int) event.getRawX() - lastX;
                        int dy = (int) event.getRawY() - lastY;

                        Log.e("MOVE","MOVE  dx = " + dx);
                        //将移动距离加上，现在本身距离边框的位置
                        int left = view.getLeft() + dx;
                        int top = view.getTop() + dy;
                        //获取到layoutParams然后改变属性，在设置回去
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view
                                .getLayoutParams();
                        layoutParams.height = wh;
                        layoutParams.width = wh;
                        layoutParams.leftMargin = left;
                        layoutParams.topMargin = top;
                        view.setLayoutParams(layoutParams);
                        //记录最后一次移动的位置
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        break;
                }
                //刷新界面
                relativeLayout.invalidate();
                return true;
            }
        };
        return onTouchListener;
    }
}
