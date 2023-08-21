package com.gongjiebin.latticeapp;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.gongjiebin.latticeview.BaseLatticeView;
import com.gongjiebin.latticeview.LatticeView;

/**
 * @author gongjiebin
 *
 *  普通的格子视图
 *
 *
 */
public class LatticeActivity extends AppCompatActivity {

    private LatticeView ll_view;
    private Button btn_show; // 切换状态。 只显示
    private Button btn_sel; //选择状态
    private Button btn_textBold;
    private Button btn_sel_bold;
    LatticeView.ImageTextParams imageTextParams;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lattice_layout);
        ll_view = findViewById(R.id.ll_view);
        btn_show = findViewById(R.id.btn_show);
        btn_sel = findViewById(R.id.btn_sel);
        btn_textBold = findViewById(R.id.btn_textBold);
        btn_sel_bold = findViewById(R.id.btn_sel_bold);

        imageTextParams= new LatticeView.ImageTextParams();
        // 未被选中图片
        imageTextParams.images = new Integer[]{R.mipmap.tab_home, R.mipmap.tab_index, R.mipmap.tab_cart};
        // 选中之后应该展示的图片
        imageTextParams.selectImages = new Integer[]{R.mipmap.tab_home_current,R.mipmap.tab_index_current, R.mipmap.tab_cart_current};
        imageTextParams.text = new String[]{"提问", "发动态", "发文章"};
        imageTextParams.maxLine = imageTextParams.selectImages.length; // 每一行显示的个数
        imageTextParams.imageHigh = 24;
        imageTextParams.imageWidth = 24;
        imageTextParams.textSize = 10; // text的字体大小
        imageTextParams.textPaddingTop = LatticeView.dip2px(this, 6); //字体向上给一个padding
        imageTextParams.selectIndex = 0; // 默认第一个被选中
        imageTextParams.textColor = R.color.black; // 字体默认颜色
        imageTextParams.imageType = ImageView.ScaleType.FIT_XY;
        imageTextParams.textSelectColor = R.color.f0; // 字体被选中的颜色
        //imageTextParams.bg_color_int = R.color.colorPrimary_main;
        //imageTextParams.animation = createAnimation();
        ll_view.setImageTextParams(imageTextParams);
        imageTextParams.imageLoader = new GlideImageLoader();
        ll_view.startView(); // 开始加载布局
        initListener();
    }

    public void initListener(){
        //   绑定点击事件
        ll_view.setOnPageItemOnClickListener(new BaseLatticeView.OnPageItemOnClickListener() {
            @Override
            public void onClick(View v, Object[] urls, int position) {
                Log.e("GJB","position = " + position);
            }

            @Override
            public void onClick(View v, ImageView imageView, Object[] urls, int position) {
                // 执行动画
                createAnmation(imageView);
            }
        });


        btn_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageTextParams.textSelectSize = 0; // 清除选中字体变大
                imageTextParams.isTextBold = false; //清除加粗
                imageTextParams.selectIndex = -1; // 清除选中
                imageTextParams.textSelectColor = 0; // 清除颜色
                ll_view.removeViews();
                ll_view.startView(); // 开始加载布局
            }
        });


        btn_sel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageTextParams.selectIndex = 0; // 第一个被选中
                imageTextParams.textSelectColor = R.color.f0;
                ll_view.removeViews();
                ll_view.startView(); // 开始加载布局
            }
        });

        btn_textBold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                imageTextParams.isTextBold = true;
                ll_view.removeViews();
                ll_view.startView(); // 开始加载布局
            }
        });

        btn_sel_bold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageTextParams.isTextBold = false; //
                imageTextParams.textSelectSize = 21;
                ll_view.removeViews();
                ll_view.startView(); // 开始加载布局
            }
        });
    }

    private void createAnmation(ImageView imageView) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(imageView, "scaleY", 1.0f, 0.6f);
        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(imageView, "scaleX", 1.0f, 0.6f);

        ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(imageView, "scaleY", 0.6f, 1f);
        ObjectAnimator objectAnimator3 = ObjectAnimator.ofFloat(imageView, "scaleX", 0.6f, 1f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(objectAnimator, objectAnimator1);

        AnimatorSet animatorSet1 = new AnimatorSet();
        animatorSet1.playTogether(objectAnimator2, objectAnimator3);


        AnimatorSet animatorSet3 = new AnimatorSet();
        animatorSet3.setDuration(300);
        animatorSet3.playSequentially(animatorSet, animatorSet1);
        animatorSet3.start();
    }
}
