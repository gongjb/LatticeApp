package com.gongjiebin.latticeapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.gongjiebin.latticeview.BaseLatticeView;
import com.gongjiebin.latticeview.PersonalTabBar;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class TabBarActivity extends AppCompatActivity {

    private PersonalTabBar pl_bar;

    private ViewPager v_page;

    private PersonalTabBar.TextParams textParams;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbar_layout);
        pl_bar=findViewById(R.id.pl_bar);
        v_page = findViewById(R.id.v_page);

        textParams = new PersonalTabBar.TextParams();
        textParams.text = new String[]{"T1","T2","T3","T4","T5"};
        textParams.selectIndex = 1; // 默认第一个被选中
        textParams.isTextBold = true;// 字体是否加粗
        textParams.textTBPadding = PersonalTabBar.dip2px(this,3);
        textParams.textColor = R.color.black;
        textParams.duration = 500; // 动画执行时长//
        //textParams.bg_color = R.color.black; 整个tab的背景颜色
        // 不设置默认跟字体保持一样的宽度
        //textParams.slideStyleLRPadding = PersonalTabBar.dip2px(this,2);;
        textParams.textSelectColor = R.color.colorAccent; //选中时的颜色
        // 滑块的背景
        textParams.slideStyle = R.drawable.bg_baijiu_info;

        List<FragmentText1> fragments = new ArrayList<>();

        for(String t : textParams.text){
            FragmentText1 fragment = new FragmentText1();
            fragment.setTextView(t);
            fragments.add(fragment);
        }

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(),this);
        adapter.setBaseFragments(fragments);
        adapter.setPAGE_COUNT(fragments.size());
        v_page.setAdapter(adapter);

        pl_bar.post(new Runnable() {
            @Override
            public void run() {
                // 需要等PersonalTabBar控件加载完成。才能得到getWidth（） ===
                // 假设我们知道控件的宽度， 可直接设置。
                // 也可以设置成屏幕的宽度， 也可以直接给一个数值。看自己的需求
                textParams.width = pl_bar.getWidth();
                pl_bar.setParams(textParams);
                pl_bar.startView(); // 加载布局
            }
        });

        // 绑定
        this.initListener();
    }


    /**
     *  完成
     */
    public void initListener(){
        pl_bar.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 如果需要调用showItemPosition这个方法，在onCreate方法中必须延时加载，才会生效
                pl_bar.showItemPosition(1);
            }
        },400);


        pl_bar.setOnPageItemOnClickListener(new BaseLatticeView.OnPageItemOnClickListener() {
            @Override
            public void onClick(View v, Object[] urls, int position) {
                v_page.setCurrentItem(position);
            }
        });

        v_page.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                // 完成与viewpage的绑定
                pl_bar.showItemPosition(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }
}
