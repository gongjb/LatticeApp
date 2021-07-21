package com.gongjiebin.latticeapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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
     * 可编辑视图
     */
    private AutoLineDeleteView ao_del_layout;

    private EditText ed_tag;

    List<KVBean> tags = new ArrayList<>();

    AutoLineDeleteView.AutoEditParams autoEditParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_line_layout);
        ao_del_layout = findViewById(R.id.ao_del_layout);

        ed_tag = findViewById(R.id.ed_tag);

        ReKVBean kvBean = new ReKVBean();
        kvBean.setValue("害羞");

        ReKVBean kvBean1 = new ReKVBean();
        kvBean1.setValue("很怕事的");

        ReKVBean kvBean2 = new ReKVBean();
        kvBean2.setValue("总是那么无聊");

        ReKVBean kvBean3 = new ReKVBean();
        kvBean3.setValue("如果觉得还行");


        ReKVBean kvBean4 = new ReKVBean();
        kvBean4.setValue("给个star就更好了，谢谢!!!");

        ReKVBean kvBean5 = new ReKVBean();
        kvBean5.setValue("来一个超级长的Tag,其实我也不知道说什么，反正挺无语的。！！！！！！");

        tags.add(kvBean);
        tags.add(kvBean1);
        tags.add(kvBean2);
        tags.add(kvBean3);
        tags.add(kvBean4);
        tags.add(kvBean5);


        ao_del_layout.setViews(tags);


        /**
         *  编辑视图与预览视图是一样的设置方法，不过最终需要调用setEditParams设置
         */
        autoEditParams = new AutoLineDeleteView.AutoEditParams();

        //选中与未选中背景颜色变化
        autoEditParams.bg_color = (R.drawable.bg_baijiu_info);
        autoEditParams.select_bg_color = R.drawable.bg_re_baijiu_info;
        // 字体是否加粗显示
        autoEditParams.isTextBold = true;

        // 选中与未选中字体大小设置
        autoEditParams.textSize = (12);
        autoEditParams.textSelectSize = 18;

        // 选中与未选中字体颜色变化
        autoEditParams.textColor = (android.R.color.white);
        autoEditParams.textSelectColor = (android.R.color.holo_red_dark);

        // AutoLineLayout.TYPE_RADIO 单选  AutoLineLayout.TYPE_GROUP 复选
        autoEditParams.sel_type = AutoLineLayout.TYPE_RADIO;
        autoEditParams.isShowDelImg  = false;

        ao_del_layout.post(new Runnable() {
            @Override
            public void run() {
                autoEditParams.width = (ao_del_layout.getWidth());
                ao_del_layout.setEditParams(autoEditParams);
                ao_del_layout.startView();
            }
        });

        initListener();
    }


    public void initListener() {

        ao_del_layout.setOnDelectTagListener(new AutoLineDeleteView.OnDelectTagListener<ReKVBean>() {
            @Override
            public void onDel(View v, ReKVBean bean) {
                tags.remove(bean);

                //刷新编辑视图
                ao_del_layout.removeViews();
                ao_del_layout.startView();
            }
        });

        ao_del_layout.setOnItemClickListener(new AutoLineLayout.OnItemClickListener<ReKVBean>() {
            @Override
            public void onClickItem(View view, ReKVBean kvBean) {
                Log.i("GJB", "编辑视图点击了" + kvBean.getValue());
            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add:
                String tag = ed_tag.getText().toString();
                if (TextUtils.isEmpty(tag)){
                    Toast.makeText(this,"请输入标签",Toast.LENGTH_LONG).show();
                    return;
                }
                ReKVBean kvBean = new ReKVBean();
                kvBean.setValue(tag);
                tags.add(kvBean);

                ao_del_layout.removeViews();
                ao_del_layout.startView();
                ed_tag.setText("");
                break;
            case R.id.btn_edit:
                autoEditParams.isShowDelImg = true; // 删除图片是否可见。默认可见
                ao_del_layout.removeViews();
                ao_del_layout.startView();
                //autoEditParams.delImg = R.mipmap.ic_launcher;//    设置删除图片
                //autoEditParams.IsDelImgLeft = true; // 删除图片/显示在左边 默认显示在右边
                break;

            case R.id.btn_sel:
                List<ReKVBean> reKVBeans = ao_del_layout.getSelKvList();
                StringBuilder builder = new StringBuilder();
                for(ReKVBean reKVBean : reKVBeans){
                    builder.append(reKVBean.getValue());
                    builder.append("\n");
                }
                Toast.makeText(this,builder.toString(),Toast.LENGTH_LONG).show();
                break;
            case R.id.btn_change:
                // 切换为复选状态
                autoEditParams.sel_type = AutoLineLayout.TYPE_GROUP;
                ao_del_layout.removeViews();
                ao_del_layout.startView();
                break;
        }
    }
}
