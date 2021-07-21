package com.gongjiebin.latticeview;

/**
 * @author gongjiebin
 *
 * Tag编辑实体。 如果你需要
 */
public class KVBean {

    private long key; //
    // 这个是值
    private String value;


    public void setKey(long key) {
        this.key = key;
    }

    public long getKey() {
        return key;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    // 是否选中。 true 当前选中了某一个视图
    public boolean isSel;
}
