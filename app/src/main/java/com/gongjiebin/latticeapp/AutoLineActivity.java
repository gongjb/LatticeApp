package com.gongjiebin.latticeapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.gongjiebin.latticeview.AutoLineDeleteView;
import com.gongjiebin.latticeview.AutoLineLayout;
import com.gongjiebin.latticeview.BaseLatticeView;
import com.gongjiebin.latticeview.KVBean;
import com.gongjiebin.latticeview.LatticeView;

import java.util.ArrayList;
import java.util.List;

/**
 * 自动换行视图 演示
 *
 * @author gongjiebin
 * @version v1.0
 */
public class AutoLineActivity extends AppCompatActivity {


    /**
     * 预览视图
     */
    private AutoLineLayout ao_layout;


    /**
     * 可编辑视图
     */
    private AutoLineDeleteView ao_del_layout;

    private EditText ed_tag;

    BaseLatticeView.ImageTextParams imageTextParams;
    List<KVBean> tags = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_line_layout);
        ao_layout = findViewById(R.id.ao_layout);
        ao_del_layout = findViewById(R.id.ao_del_layout);

        ed_tag = findViewById(R.id.ed_tag);

        KVBean kvBean = new KVBean();
        kvBean.value = "害羞";

        KVBean kvBean1 = new KVBean();
        kvBean1.value = "很怕事的";

        KVBean kvBean2 = new KVBean();
        kvBean2.value = "总是那么无聊";

        KVBean kvBean3 = new KVBean();
        kvBean3.value = "如果觉得还行";


        KVBean kvBean4 = new KVBean();
        kvBean4.value = "给个star就更好了，谢谢!!!";

        KVBean kvBean5 = new KVBean();
        kvBean5.value = "来一个超级长的Tag,其实我也不知道说什么，反正挺无语的。！！！！！！";

        tags.add(kvBean1);
        tags.add(kvBean2);
        tags.add(kvBean3);
        tags.add(kvBean4);
        tags.add(kvBean);
        tags.add(kvBean5);


        ao_layout.setViews(tags);
        ao_del_layout.setViews(tags);
        imageTextParams = new BaseLatticeView.ImageTextParams();
        imageTextParams.bg_color = (R.drawable.bg_baijiu_info);
        imageTextParams.textColor = (android.R.color.white);
        imageTextParams.textSize = (12);
        ao_layout.post(new Runnable() {
            @Override
            public void run() {
                // 设置控件的宽度 取值可以按照自己的需求来，也可取屏幕的宽度
                ao_layout.setW(ao_layout.getWidth());
                ao_layout.setImageTextParams(imageTextParams);
                ao_layout.startView();
            }
        });


        /**
         *  编辑视图与预览视图是一样的设置方法，不过最终需要调用setEditParams设置
         */
        final AutoLineDeleteView.AutoEditParams autoEditParams = new AutoLineDeleteView.AutoEditParams();
        autoEditParams.bg_color = (R.drawable.bg_baijiu_info);
        autoEditParams.textColor = (android.R.color.white);
        autoEditParams.textSize = (12);
 //       autoEditParams.isShowDelImg = false; // 删除图片是否可见。默认可见
//        autoEditParams.delImg = R.mipmap.ic_launcher; 设置删除图片
//        autoEditParams.IsDelImgLeft = true; // 删除图片/显示在左边 默认显示在右边
        ao_del_layout.post(new Runnable() {
            @Override
            public void run() {
                ao_del_layout.setW(ao_del_layout.getWidth());
                ao_del_layout.setEditParams(autoEditParams);
                ao_del_layout.startView();
            }
        });

        initListener();
    }


    public void initListener() {

        ao_layout.setOnItemClickListener(new AutoLineLayout.OnItemClickListener() {
            @Override
            public void onClickItem(View view,KVBean kvBean) {
                //view 是点击的TextView
                Log.i("GJB", "点击了" + kvBean.value);
            }
        });

        ao_del_layout.setOnDelectTagListener(new AutoLineDeleteView.OnDelectTagListener() {
            @Override
            public void onDel(View v,KVBean bean) {
                tags.remove(bean);
                //刷新预览视图
                ao_layout.removeViews();
                ao_layout.startView();
                //刷新编辑视图
                ao_del_layout.removeViews();
                ao_del_layout.startView();
            }
        });

        ao_del_layout.setOnItemClickListener(new AutoLineLayout.OnItemClickListener() {
            @Override
            public void onClickItem(View view, KVBean kvBean) {
                Log.i("GJB", "编辑视图点击了" + kvBean.value);
            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add:
                String tag = ed_tag.getText().toString();
                if (TextUtils.isEmpty(tag)) return;
                KVBean kvBean = new KVBean();
                kvBean.value = tag;
                tags.add(kvBean);
                ao_layout.removeViews();
                ao_layout.startView();

                ao_del_layout.removeViews();
                ao_del_layout.startView();

                ed_tag.setText("");
                break;

        }
    }
}
