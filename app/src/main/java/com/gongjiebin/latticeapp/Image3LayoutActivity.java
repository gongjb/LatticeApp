package com.gongjiebin.latticeapp;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.app.AppCompatActivity;

import com.gongjiebin.latticeview.Images3LinearLayout;


/**
 * 展示9宫格能力
 */
public class Image3LayoutActivity extends AppCompatActivity implements
        Images3LinearLayout.OnPageDeleteImageClickListener,
        Images3LinearLayout.OnImages3PageItemOnClickListener,
Images3LinearLayout.OnPageItemOnLongClickListener,
        View.OnClickListener {
    private String TAG = Image3LayoutActivity.this.getClass().getSimpleName();
    private Images3LinearLayout image3Layout;
    private Images3LinearLayout.Images3Params images3Params;
    private String[] urls = null;
    private String lastImage = "https://res.wx.qq.com/wxdoc/dist/assets/img/0.4cb08bb4.jpg"; // 追加的图片
    private TextView tv_max_line;
    private TextView tv_width;
    // 调整宽度的百分比
    private int width_tag = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_a_layout);
        image3Layout = findViewById(R.id.il_mian);
        tv_max_line = findViewById(R.id.tv_max_line);
        tv_width = findViewById(R.id.tv_width);

        //R.mipmap.send_photo_join + ""

        urls = new String[]{
                ("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpcs4.clubstatic.lenovo.com.cn%2Fdata%2Fattachment%2Fforum%2F201807%2F18%2F142304to0n1u0uurf07fyr.jpg&refer=http%3A%2F%2Fpcs4.clubstatic.lenovo.com.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1625208782&t=b6434aff7770ea5e58b75bb8e9132068"),
                ("https://res.wx.qq.com/wxdoc/dist/assets/img/0.4cb08bb4.jpg"),
                ("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fclubimg.club.vmall.com%2Fdata%2Fattachment%2Fforum%2F201907%2F31%2F010210loua4oghrbtvhbzk.jpg&refer=http%3A%2F%2Fclubimg.club.vmall.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1625195254&t=e729a2ad5bffd6051ba2dde13d621f57"),
                ("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fattach.bbs.miui.com%2Fforum%2F201209%2F01%2F2150537dqggg4hgsgifsfd.jpg&refer=http%3A%2F%2Fattach.bbs.miui.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1625208782&t=de983abe865105ea8151fa2fa2f47bab"),
                ("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1642572855,2753515187&fm=26&gp=0.jpg"),
                ("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fattach.bbs.miui.com%2Fforum%2F201407%2F04%2F222814riphovmoww4jm4vd.jpg&refer=http%3A%2F%2Fattach.bbs.miui.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1625208782&t=1329812383a601a44cb7f62b3a330da9"),
                ("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpcs4.clubstatic.lenovo.com.cn%2Fdata%2Fattachment%2Fforum%2F201807%2F18%2F142304to0n1u0uurf07fyr.jpg&refer=http%3A%2F%2Fpcs4.clubstatic.lenovo.com.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1625208782&t=b6434aff7770ea5e58b75bb8e9132068")
        };


        images3Params = new Images3LinearLayout.Images3Params();

        // 添加到头部view
        images3Params = new Images3LinearLayout.Images3Params();

        images3Params.imageLoader = new GlideImageLoader();
        images3Params.bg_color = android.R.color.white;
        images3Params.setUrls(urls);

        images3Params.maxLine = 3;

        images3Params.space = 4; // 图片间距
        images3Params.scaleType = ImageView.ScaleType.CENTER; // 图片居中显示。 默认是铺满


        image3Layout.post(new Runnable() {
            @Override
            public void run() {
                // 等到视图加载完，才能获取宽度
                images3Params.width = image3Layout.getWidth();
                image3Layout.setParams(images3Params);
                image3Layout.startView(); // 预览
            }
        });

        // 绑定启用编辑的事件
        findViewById(R.id.btn_edit).setOnClickListener(this);
        // 启用删除图片
        findViewById(R.id.btn_del).setOnClickListener(this);
        // 启用添加图片
        findViewById(R.id.btn_add_img).setOnClickListener(this);
        // 增加每行显示的最大布局数量
        findViewById(R.id.btn_addition).setOnClickListener(this);
        //减每行显示的最大布局数量
        findViewById(R.id.btn_subtraction).setOnClickListener(this);
        // 预览模式
        findViewById(R.id.btn_preview).setOnClickListener(this);
        // 宽度和高度随比例缩放
        findViewById(R.id.btn_width_subtraction).setOnClickListener(this);
        // 宽度和高度随比例缩放
        findViewById(R.id.btn_width_addition).setOnClickListener(this);
        // 4张图特殊展示
        findViewById(R.id.btn_four).setOnClickListener(this);
        findViewById(R.id.btn_drag).setOnClickListener(this);

        // 绑定点击事件
        image3Layout.setOnImages3PageItemOnClickListener(this);
        // 删除事件监听
        image3Layout.setOnPageDeleteImageClickListener(this);
        // 长按事件监听
        image3Layout.setOnPageItemOnLongClickListener(this);

    }


    @Override
    public void onDeleteClick(View view, String[] urls, int position) {
        // 删除成功之后被回调， 处理一些删除之后的逻辑
        Log.e(TAG,"------" + urls[position]);
    }


    @Override
    public void onClick(View v, Object[] urls, int position) {
        //urls 这个是返回的图片路径，需要强制转换一下
        Log.d(TAG, "处理自己的逻辑 - 比如启动预览,点击了 + " + position);
    }

    @Override
    public void onLongClick(View v, Object[] urls, int position) {
        // 长按事件处罚
        Log.d(TAG, "触发长按事件 + " + position);


    }

    @Override
    public boolean onClickEndView(View v, Object[] urls, int position) {
        Log.d(TAG, "点击了最后一个追加图片按钮, 插入一张图片到倒数第二的位置"+position);
        image3Layout.addImage(position, lastImage);
        image3Layout.startView();
        // 如果返回 false . onClick(View v, Object[] urls, int position) 将不在被回调，不继续分发事件 ， true则相反
        return false;
    }


    @Override
    public void onClick(View v) {
        if(image3Layout.getPaths()==null || image3Layout.getPaths().size()<=0){
            Toast.makeText(this, "图片已被清除，请重新加载该界面", Toast.LENGTH_LONG).show();
            return;
        }

        switch (v.getId()) {
            case R.id.btn_del:
                // 打开删除模式 每次在切换模式之前必须清除之前的模式
                image3Layout.enableEditModel(Images3LinearLayout.EditType.DELETE_TYPE,R.mipmap.shanchu);
                break;
            case R.id.btn_add_img:
                //添加模式  每次在切换模式之前必须清除之前的模式
                image3Layout.enableEditModel(Images3LinearLayout.EditType.ADD_TYPE, R.mipmap.send_photo_join); // 添加模式
                break;
            case R.id.btn_edit:
                // 开启编辑模式 每次在切换模式之前必须清除之前的模式
                image3Layout.enableEditModel(Images3LinearLayout.EditType.ALL_TYPE,  R.mipmap.shanchu, R.mipmap.send_photo_join); // 开启全模式
                break;
            case R.id.btn_drag:
                // 开启拖拽删除模式 每次在切换模式之前必须清除之前的模式
                image3Layout.enableEditModel(Images3LinearLayout.EditType.PREVIEW_TYPE);
                break;
            case R.id.btn_addition:
                // 控制视图大小
                int maxLine = (images3Params.maxLine+ 1);
                tv_max_line.setText(maxLine + "");
                images3Params.setMaxLine(maxLine);
                image3Layout.startView(); // 刷新UI
                break;
            case R.id.btn_subtraction:
                // 减
                maxLine = (images3Params.maxLine - 1);
                if (maxLine < 1) {
                    Toast.makeText(this, "设置不能小于1", Toast.LENGTH_LONG).show();
                    return;
                }
                tv_max_line.setText(maxLine + "");
                images3Params.setMaxLine(maxLine);
                image3Layout.startView(); // 刷新UI
                break;
            case R.id.btn_preview:
                image3Layout.removeEditModel(); // 清除编辑模式 恢复预览模式
                image3Layout.startView(); // 恢复UI
                break;
            case R.id.btn_width_subtraction:
                width_tag -= 1;
                tv_width.setText(width_tag + "");
                image3Layout.changedWidth(width_tag);
                break;
            case R.id.btn_width_addition:
                width_tag += 1;
                tv_width.setText(width_tag + "");
                image3Layout.changedWidth(width_tag);
                break;
            case R.id.btn_four:
                // 展示朋友圈4张图片的展示方式
                images3Params.urls = new String[]{("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpcs4.clubstatic.lenovo.com.cn%2Fdata%2Fattachment%2Fforum%2F201807%2F18%2F142304to0n1u0uurf07fyr.jpg&refer=http%3A%2F%2Fpcs4.clubstatic.lenovo.com.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1625208782&t=b6434aff7770ea5e58b75bb8e9132068"),
                        ("https://res.wx.qq.com/wxdoc/dist/assets/img/0.4cb08bb4.jpg"),
                        ("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fclubimg.club.vmall.com%2Fdata%2Fattachment%2Fforum%2F201907%2F31%2F010210loua4oghrbtvhbzk.jpg&refer=http%3A%2F%2Fclubimg.club.vmall.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1625195254&t=e729a2ad5bffd6051ba2dde13d621f57"),
                        ("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fattach.bbs.miui.com%2Fforum%2F201209%2F01%2F2150537dqggg4hgsgifsfd.jpg&refer=http%3A%2F%2Fattach.bbs.miui.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1625208782&t=de983abe865105ea8151fa2fa2f47bab")};

                width_tag = 6;
                maxLine = 2;
                images3Params.setMaxLine(maxLine);
                image3Layout.changedWidth(width_tag);


                tv_max_line.setText(maxLine+ "");
                tv_width.setText(width_tag+"");
                break;

        }
    }



}
