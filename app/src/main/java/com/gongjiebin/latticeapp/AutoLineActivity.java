package com.gongjiebin.latticeapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

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
     *
     */
    private AutoLineLayout ao_layout;

    private EditText ed_tag;

    BaseLatticeView.ImageTextParams imageTextParams;
    List<KVBean> tags = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_line_layout);
        ao_layout = findViewById(R.id.ao_layout);

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
        imageTextParams = new LatticeView.ImageTextParams();
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
    }

    public void onClick(View view) {
        String tag = ed_tag.getText().toString();
        if (TextUtils.isEmpty(tag)) return;
        KVBean kvBean = new KVBean();
        kvBean.value = tag;
        tags.add(kvBean);
        ao_layout.removeViews();
        ao_layout.startView();
        ed_tag.setText("");
    }
}
