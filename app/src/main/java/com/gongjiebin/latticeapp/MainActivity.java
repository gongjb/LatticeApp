package com.gongjiebin.latticeapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;


import com.gongjiebin.latticeview.Images3LinearLayout;
import com.gongjiebin.latticeview.LatticeView;

public class MainActivity extends AppCompatActivity {
    private String TAG = MainActivity.this.getClass().getSimpleName();

    Images3LinearLayout img_main;

    private Images3LinearLayout.Images3Params images3Params;

    private String[] urls = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





        urls = new String[]{
                "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpcs4.clubstatic.lenovo.com.cn%2Fdata%2Fattachment%2Fforum%2F201807%2F18%2F142304to0n1u0uurf07fyr.jpg&refer=http%3A%2F%2Fpcs4.clubstatic.lenovo.com.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1625208782&t=b6434aff7770ea5e58b75bb8e9132068",
                "https://res.wx.qq.com/wxdoc/dist/assets/img/0.4cb08bb4.jpg",
                "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fclubimg.club.vmall.com%2Fdata%2Fattachment%2Fforum%2F201907%2F31%2F010210loua4oghrbtvhbzk.jpg&refer=http%3A%2F%2Fclubimg.club.vmall.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1625195254&t=e729a2ad5bffd6051ba2dde13d621f57",
                "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fatt.xmnn.cn%2Fbbs%2Fforum%2F201310%2F08%2F120516oqdxu9iliugmqsgm.jpg&refer=http%3A%2F%2Fatt.xmnn.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1625208782&t=3a374a1c180dc7d2937c8729d7676f49",
                "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fattach.bbs.miui.com%2Fforum%2F201209%2F01%2F2150537dqggg4hgsgifsfd.jpg&refer=http%3A%2F%2Fattach.bbs.miui.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1625208782&t=de983abe865105ea8151fa2fa2f47bab",
                "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1642572855,2753515187&fm=26&gp=0.jpg"
                ,"https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fattach.bbs.miui.com%2Fforum%2F201407%2F04%2F222814riphovmoww4jm4vd.jpg&refer=http%3A%2F%2Fattach.bbs.miui.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1625208782&t=1329812383a601a44cb7f62b3a330da9"};
        img_main = findViewById(R.id.img_main);
        images3Params = new Images3LinearLayout.Images3Params();

        // 添加到头部view
        final Images3LinearLayout.Images3Params images3Params = new Images3LinearLayout.Images3Params();

        images3Params.imageLoader = new GlideImageLoader();
        images3Params.bg_color =android.R.color.white;
        images3Params .setUrls(urls);


        if (images3Params.urls != null && (images3Params.urls.length <= 2)) {
            images3Params.maxLine = 2; // 两张图片就
        } else {
            images3Params.maxLine = 3;
        }
        images3Params.space = 4; // 图片间距
        images3Params.scaleType = ImageView.ScaleType.CENTER; // 图片居中显示。 默认是铺满

        img_main.post(new Runnable() {
            @Override
            public void run() {
                // 等到视图加载完，才能获取宽度
                images3Params.width = img_main.getWidth();
                img_main.setParams(images3Params);
                img_main.startView();
            }
        });

        img_main.setOnPageItemOnClickListener(new LatticeView.OnPageItemOnClickListener() {
            /*
             * @param v        当前点击的view 视图
             * @param urls     如果有图片携带的是图片路径/如果是文字就是String数组
             * @param position 点击的位置
             */
            @Override
            public void onClick(View v, Object[] urls, int position) {
                // 图片点击事件
                Log.d(TAG, "position" + position);
            }
        });

        initListener();
    }


    public void initListener(){
        findViewById(R.id.btn_lattice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 宫格视图
                startActivity(new Intent(MainActivity.this,Image3LayoutActivity.class));
            }
        });

        findViewById(R.id.btn_tabbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // tabBar
                startActivity(new Intent(MainActivity.this,TabBarActivity.class));
            }
        });


        findViewById(R.id.btn_grid).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 格子视图
                startActivity(new Intent(MainActivity.this,LatticeActivity.class));
            }
        });


        findViewById(R.id.btn_auto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 自动换行
                startActivity(new Intent(MainActivity.this,AutoLineActivity.class));
            }
        });
    }
}
