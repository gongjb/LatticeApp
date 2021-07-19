package com.gongjiebin.latticeview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static android.view.MotionEvent.ACTION_DOWN;

/**
 * @author gongjiebin
 * @version v1.0
 * 此信息流布局详情展示，用于9宫格/4宫格以及自定义宫格展示。
 * <p>
 * 适用于类似朋友圈/编辑宫格图片-提供删除图片的能力。
 */
public class Images3LinearLayout extends LatticeView {

    public String TAG = Images3LinearLayout.this.getClass().getSimpleName();

    private Images3Params params;
    private boolean isFourChange;
    private List<LinearLayout> imageParents;

    private SparseArray<ImageView> imageViews;

    public List<LinearLayout> getImageParents() {
        return imageParents;
    }

    public SparseArray<ImageView> getImageViews() {
        return imageViews;
    }

    // 存放图片路径， 用于将图片插入到指定位置/列队
    private LinkedList<String> paths;

    public void setParams(Images3Params params) {
        this.params = params;
    }

    public Images3LinearLayout(Context context) {
        super(context);
    }

    public Images3LinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Images3LinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public LinkedList<String> getPaths() {
        return paths;
    }

    /**
     * 检查参数是否填写正确
     *
     * @return
     */
    public boolean checkParams() {
        if (params == null) {
            Log.e(TAG, "params is null");
            return false;
        }
        // 必须设置图片数组。
        if (params.urls == null) {
            Log.e(TAG, "params image url is null");
            return false;
        }
        // 必须设置一行显示多少个view
        if (params.maxLine <= 0) {
            Log.e(TAG, "params maxLine is null");
            return false;
        }

        if (ll_lattice.getChildCount() > 0) {
            Log.e(TAG, "移除view");
            removeViewsAll();
        }

        // 可以不设置id,但是如果设置了id, id的数量就要与图片数组的数量保持一致
        if (params.latticeIds != null && params.latticeIds.length != params.urls.length) {
            Log.e(TAG, "params latticeIds error");
            return false;
        }

        // 图片加载器不能为空
        if (params.imageLoader == null) {
            Log.e(TAG, "params imageLoader is null");
            return false;
        }
        // 一般采用设置父布局的宽度，或者自身宽度或者屏幕宽度，不允许为0
        if (params.width == 0) {
            Log.e(TAG, "params width is null");
            return false;
        }

        return true;
    }

    /**
     * 清除有可能改变功能/布局的一切因素
     */
    public void removeViewsAll() {
        isFourChange = false; //
        params.latticeIds = null; // 清空ID
        ll_lattice.removeAllViews();
    }


    /**
     * 插入一张图片到指定位置，
     *
     * @param position  插入图片的位置
     * @param lastImage 图片的url 也可以是本地图片
     */
    public void addImage(int position, String lastImage) {

        this.addImageView(position, lastImage);
    }


    /**
     * 添加一张图片到指定位置,并刷新params中的urls
     *
     * @param position
     * @param lastImage
     */
    private void addImageView(int position, String lastImage) {
        paths.add(position, lastImage);
        params.urls = new String[paths.size()];
        paths.toArray(params.urls); // 重新排列图片
    }


    /*
     清除编辑模式
     */
    public boolean removeEditModel() {
        if (params == null) {
            Log.e(TAG, "params is null");
            return false;
        }
        // 恢复默认预览模式
        params.type = EditType.PREVIEW_TYPE;
        params.isLastDelete = false;
        params.deleteImage = 0;
        params.lastImage = 0;
        removeLastImage();
        return true;
    }


    /**
     * 清除最后一张为项目中的图片
     */
    private void removeLastImage() {
        if (paths == null) return;
        if (isNumber(paths.getLast())) {
            paths.removeLast();
            params.urls = new String[paths.size()];
            paths.toArray(params.urls); // 重新排列图片
        }
    }


    /**
     * 删除图片中任意位置的图片
     */
    public void removePositionImage(int position) {
        if (paths != null && paths.size() >= position) {
            paths.remove(position);
            params.urls = new String[paths.size()];
            paths.toArray(params.urls); // 重新排列图片
        }
    }


    /**
     * 改变视图的大小
     * <p>
     * 此方法会对视图的宽高进行修改
     *
     * @param w 取1-10，
     */
    public boolean changedWidth(float w) {
        if (w < 1 || w > 10) {
            Log.e(TAG, "取值范围不对。");
            return false;
        }
        if (params == null) return false;
        int number = (int) Math.ceil(getWidth() * (0.1 * w));
        params.width = number;
        startView(); // 重新生成UI
        return true;
    }


    /**
     * 启用编辑模式
     *
     * @param type 编辑的类型 在EditType中定义
     * @param images（一般最多2张图片， 当参数长度为2时，游标为0的参数应该是删除图片，游标为1的是添加图片）
     * @return
     */
    public boolean enableEditModel(EditType type, int... images) {
        if (params == null) {
            Log.e(TAG, "params is null");
            return false;
        }

        if (type == null) {
            Log.e(TAG, "EditType is null");
            return false;
        }

        this.removeEditModel();

        this.params.type = type;
        // 加上这两个参数，视图就是编辑模式
        switch (type) {
            case ADD_TYPE:
                if (images.length > 0) addType(images[0]);
                break;
            case DELETE_TYPE:
                if (images.length > 0) deleteType(images[0]);
                break;
            case ALL_TYPE:
                if (images.length > 1) allType(images[0], images[1]);
                break;
        }

        // 刷新UI
        startView();
        return true;
    }


    /**
     * 添加类型
     *
     * @param lastImage
     */
    private void addType(int lastImage) {
        // 最后一张编辑图片只能是本地图片
        if (params == null) return;
        params.isLastDelete = true; // 这个参数是控制最后一张图片是不是要显示删除图片/
        params.lastImage = lastImage; // 如果有增加图片的功能， 给最后一个view提供一张图片
        if (paths == null) paths = new LinkedList<>();
        this.addImageView(paths.size(), params.lastImage + "");
    }


    /**
     * 删除类型
     */
    private void deleteType(int deleteImage) {
        if (params == null) return;
        params.isLastDelete = true; // 这个参数是控制最后一张图片是不是要显示删除图片/
        params.deleteImage = deleteImage; // 删除图片
    }


    /**
     * 全编辑模式
     */
    private void allType(int deleteImage, int lastImage) {
        params.isLastDelete = true; // 这个参数是控制最后一张图片是不是要显示删除图片/
        params.deleteImage = deleteImage; // 删除图片
        params.lastImage = lastImage; // 如果有增加图片的功能， 给最后一个view提供一张图片
        // 最后一张编辑图片只能是本地图片
        if (paths == null) paths = new LinkedList<>();
        this.addImageView(paths.size(), params.lastImage + "");/**/
    }

    /**
     * 此方法生成图片展示
     *
     * @return 是否成功显示布局
     */
    public boolean startView() {
        if (!checkParams()) {
            return false;
        }


//        this.fourHandler();
        double viewNumber = params.urls.length;
        // view 的数量 / 每列显示最大个数
        double line = viewNumber / (double) params.maxLine;
        paths = new LinkedList<>();
        for (String path : params.urls) {
            paths.add(path);
        }
        imageParents = new ArrayList<>();
        imageViews = new SparseArray<>();
        // 清除原先的布局，重新布置

        int countLine = (int) Math.ceil(line); // 向上取整，得到总行数

        for (int i = 0; i < countLine; i++) {
            // 循环加入行 LinearLayout视图
            LinearLayout linearLayout = new LinearLayout(mContext);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(params.width, LinearLayout.LayoutParams.WRAP_CONTENT);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL); //
            //linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            linearLayout.setPadding(0, params.space, 0, 0);
            linearLayout.setLayoutParams(layoutParams);


            int intstart = (i) * params.maxLine; // 数组开始的位置
            int intend = (i + 1) * params.maxLine; // 数组结束的位置
            int num;
            if (intend > params.urls.length)// 最后一行了
                num = (params.urls.length % params.maxLine);
            else num = params.maxLine;


            String[] lineImage = new String[params.maxLine];
            final int[] lattIds = new int[params.maxLine];
            if (params.latticeIds == null) {
                params.latticeIds = new int[params.urls.length];
                for (int ids = 0; ids < params.latticeIds.length; ids++) {
                    params.latticeIds[ids] = ids; // 给出默认id.
                }
            }

            System.arraycopy(params.latticeIds, intstart, lattIds, 0, num);
            System.arraycopy(params.urls, intstart, lineImage, 0, num);


            for (int j = 0; j < lineImage.length; j++) {
                final LinearLayout lio = new LinearLayout(mContext);
                LinearLayout.LayoutParams imageparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 3.0f);
                lio.setOrientation(LinearLayout.VERTICAL); //
                lio.setGravity(Gravity.CENTER);
                //lio.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                lio.setLayoutParams(imageparams);
                if (!TextUtils.isEmpty(lineImage[j])) {
                    lio.setId(lattIds[j]);
                    // 只有相对布局才能覆盖 - 主要
                    RelativeLayout relativeLayout = new RelativeLayout(mContext);
                    RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    relativeLayout.setLayoutParams(relativeParams);
                    imageParents.add(lio);

                    final ImageView imageView = new ImageView(mContext);
                    // 计算ImageView的高度
                    int h = params.width / params.maxLine;
                    LinearLayout.LayoutParams ir = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, h);
                    imageView.setScaleType(params.scaleType);
                    ir.setMargins(params.space, params.space, params.space, 0);
                    imageView.setLayoutParams(ir);
                    //imageView.setPadding(params.space, params.space, params.space, 0);

                    final String url = lineImage[j];

                    if (isNumber(url)) {
                        Integer mipmapImage = Integer.parseInt(url);
                        // 加载本地的mipmap中的图片
                        params.imageLoader.displayDrawableImage(mContext, mipmapImage, imageView);
                    } else {
                        params.imageLoader.displayImage(mContext, url, imageView);
                    }

                    relativeLayout.addView(imageView, 0);

                    int index = 0;
                    String[] us;
                    // 计算点击的位置
                    us = params.urls;
                    index = i * params.maxLine + (j);
                    // 保存图片
                    imageViews.put(index, imageView);
                    if (params.deleteImage != 0) {
                        // 需要设置删除图片
//                        LinearLayout.LayoutParams delectParams = new LinearLayout.LayoutParams(params.deleteImageSize==0?50:params.deleteImageSize,
//                                params.deleteImageSize==0?50:params.deleteImageSize);

                        LinearLayout.LayoutParams delectParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        ImageView delect = new ImageView(mContext);
                        delect.setScaleType(ImageView.ScaleType.FIT_XY);
                        int delPadding = params.deleteImagePadding;
                        if (delPadding != 0) {
                            // 上下左右的padding
                            delect.setPadding(delPadding, delPadding, delPadding, delPadding);
                        }
                        // 计算偏移量
                        int marginDeviation = params.width / params.maxLine - 60;
                        //delectParams.setMarginStart(marginDeviation);
                        delectParams.setMargins(marginDeviation, 10, 0, 0);

                        if (isNumber(us[index])
                                && (index + 1) == us.length
                                && params.isLastDelete) {
                            delect.setLayoutParams(delectParams);
                        } else {
                            // params.imageLoader.displayDrawableImage(mContext,params.deleteImage,delect);
                            delect.setImageDrawable(mContext.getResources().getDrawable(params.deleteImage));
                            delect.setLayoutParams(delectParams);
                            // 绑定删除图片的点击事件
                            delect.setOnClickListener(getDeleteImageOnClickListener(us, index));
                        }

                        relativeLayout.addView(delect, 1);
                    }


                    lio.addView(relativeLayout);
                    lio.setClickable(true);


                    lio.setOnClickListener(getOnItemClickListener(us, index));
                    lio.setOnLongClickListener(getOnItemLongClickListener(us, index));
                }
                linearLayout.addView(lio, j);
            }


            ll_lattice.addView(linearLayout, i);
        }
        if (params.bg_color != 0)
            ll_lattice.setBackgroundColor(mContext.getResources().getColor(params.bg_color));
        return true;
    }


    @Override
    public View.OnLongClickListener getOnItemLongClickListener(final Object[] urls, final int position) {
        View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // 这里可以开启拖拽删除
                if ((params.type == EditType.DRAG_TYPE || params.type == EditType.ALL_TYPE) && imageViews.indexOfKey(position) >= 0) {
                    if (((urls.length - 1) == position) && isNumber(paths.get(position))) {
                        // 不支持项目中的图片拖拽
                        if (getOnPageItemOnLongClickListener() != null) {
                            getOnPageItemOnLongClickListener().onLongClick(v, urls, position);
                        }
                        return true;
                    }
                    ImageView viv = imageViews.valueAt(position);
                    int[] location = new int[2];
                    v.getLocationOnScreen(location);
                    int x = location[0];
                    int y = location[1];
                    final RelativeLayout layout = (RelativeLayout) RelativeLayout.inflate(mContext, R.layout.view_drop_layout, null);
                    final RelativeLayout relativeLayout = layout.findViewById(R.id.rl_layout);
                    final RelativeLayout rl_del = layout.findViewById(R.id.rl_del);
                    final ImageView imageView = layout.findViewById(R.id.iv_content);
                    final TextView tv_del = layout.findViewById(R.id.tv_del);
                    int iw = params.width / params.maxLine;
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(iw, iw);
                    layoutParams.leftMargin = x;
                    layoutParams.topMargin = y - LatticeView.dip2px(mContext, 25);
                    imageView.setLayoutParams(layoutParams);
                    String u = (String) urls[position];
                    if (isNumber(u)) {
                        params.imageLoader.displayDrawableImage(mContext, Integer.valueOf(u), imageView);
                    } else {
                        params.imageLoader.displayImage(mContext, urls[position], imageView);
                    }

                    PopupWindow popupWindow = Image3DragDropPopWindow.createFunctionPopupWindow(layout);
                    imageView.setOnTouchListener(getTouchListener(popupWindow, relativeLayout, rl_del, tv_del, viv.getWidth(), position, urls));
                }


                if (getOnPageItemOnLongClickListener() != null) {
                    getOnPageItemOnLongClickListener().onLongClick(v, urls, position);
                }
                return true;
            }
        };

        return onLongClickListener;
    }


    public View.OnTouchListener getTouchListener(final PopupWindow pop,
                                                 final RelativeLayout mainRl,
                                                 final RelativeLayout rl_del,
                                                 final TextView tv_del,
                                                 final int wh,
                                                 final int position,
                                                 final Object[] urls) {


        View.OnTouchListener onTouchListener = new View.OnTouchListener() {
            int lastX, lastY;    //保存手指点下的点的坐标

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case ACTION_DOWN:

                        //将点下的点的坐标保存
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        if (pop != null && pop.isShowing()) {
                            int[] location = new int[2];
                            rl_del.getLocationOnScreen(location);
                            int del_y = location[1];

                            int[] location1 = new int[2];
                            view.getLocationOnScreen(location1);
                            int img_y = location1[1] + wh;
                            if (del_y <= img_y) {
                                removePositionImage(position);
                                if (onPageDeleteImageClickListener != null) {
                                    onPageDeleteImageClickListener.onDeleteClick(view, (String[]) urls, position);
                                }
                                startView(); // 刷新UI
                            }
                            pop.dismiss();
                        }
                        break;

                    case MotionEvent.ACTION_MOVE:
                        int[] location = new int[2];
                        rl_del.getLocationOnScreen(location);
                        int del_y = location[1];

                        int[] location1 = new int[2];
                        view.getLocationOnScreen(location1);
                        int img_y = location1[1] + wh;
                        if (del_y <= img_y) {
                            tv_del.setTextColor(Color.parseColor("#ffffff"));
                            rl_del.setBackgroundColor(Color.parseColor("#ff0000"));
                        } else {
                            // 不能删除
                            tv_del.setTextColor(Color.parseColor("#000000"));
                            rl_del.setBackgroundColor(Color.parseColor("#E9E9E9"));
                        }

                        //计算出需要移动的距离
                        int dx = (int) event.getRawX() - lastX;
                        int dy = (int) event.getRawY() - lastY;


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
                mainRl.invalidate();
                return true;
            }
        };
        return onTouchListener;
    }


    /**
     * 重写父类getOnItemClickListener方法. 添加onClickEndView监听方法
     *
     * @param urls
     * @param position
     * @return
     */
    @Override
    public OnItemClickListener getOnItemClickListener(final Object[] urls, final int position) {
        OnItemClickListener onItemClickListener = new OnItemClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "click " + position + "==" + urls[position] + "===" + (urls.length - 1) + "==" + params.isLastDelete + "=== " + params.lastImage);
                boolean isOnClick = true;
                if (onImages3PageItemOnClickListener != null) {
                    if (params != null) {
                        if ((urls.length - 1) == position && isNumber((String) urls[position])) {
                            // 点击的是最后一个
                            if (params.isLastDelete && params.lastImage != 0) {
                                // 确定点击的是添加按钮 // 返回是否向下分发事件
                                isOnClick = onImages3PageItemOnClickListener.onClickEndView(v, urls, position);
                            }
                        }
                    }
                }

                if (Images3LinearLayout.super.getOnItemClickListener(urls, position) != null && isOnClick) {
                    Images3LinearLayout.super.getOnItemClickListener(urls, position).onClick(v);
                }
            }
        };

        return onItemClickListener;
    }


    private OnImages3PageItemOnClickListener onImages3PageItemOnClickListener;

    public OnImages3PageItemOnClickListener getOnImages3PageItemOnClickListener() {
        return onImages3PageItemOnClickListener;
    }

    public void setOnImages3PageItemOnClickListener(OnImages3PageItemOnClickListener onImages3PageItemOnClickListener) {
        // 给父类添加监听事件
        super.setOnPageItemOnClickListener(onImages3PageItemOnClickListener);
        this.onImages3PageItemOnClickListener = onImages3PageItemOnClickListener;
    }

    public interface OnImages3PageItemOnClickListener extends LatticeView.OnPageItemOnClickListener {
        /**
         * 该方法主要监听当前点击的是不是最后一个view， 并且只有在添加图片编辑的情况下，才会被调用
         *
         * @param v
         * @param urls
         * @return 是否向下分发事件， 如果返回false, 就不在响应onClick事件。 返回true,则相反
         */
        boolean onClickEndView(View v, Object[] urls, int position);
    }


    /**
     * @param index
     * @return
     */
    public View.OnClickListener getDeleteImageOnClickListener(final String[] urls, final int index) {
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "click " + index);
                urls[index] = null; // 删除
                List<String> list = removeNullStringArray(urls);
                String[] us = new String[list.size()];
                list.toArray(us); // 装入数组中
                params.urls = us;
                removeViewsAll();
                startView(); // 重新画出UI
                if (onPageDeleteImageClickListener != null) {
                    onPageDeleteImageClickListener.onDeleteClick(v, urls, index);
                }
            }
        };
        return onClickListener;
    }


    /**
     * 监听图片被删除事件
     */
    private OnPageDeleteImageClickListener onPageDeleteImageClickListener;

    public void setOnPageDeleteImageClickListener(OnPageDeleteImageClickListener onPageDeleteImageClickListener) {
        this.onPageDeleteImageClickListener = onPageDeleteImageClickListener;
    }


    public interface OnPageDeleteImageClickListener {
        /**
         * @param view     点击删除视图
         * @param urls     删除之后的图片合集
         * @param position 删除的位置
         */
        void onDeleteClick(View view, String[] urls, int position);
    }


    /**
     * 启用编辑的类型
     */
    public enum EditType {
        // 图片添加模式
        ADD_TYPE,
        // 图片删除模式
        DELETE_TYPE,
        // 添加与删除共存
        ALL_TYPE,
        // 拖拽删除
        DRAG_TYPE,
        // 预览
        PREVIEW_TYPE,

    }


    /**
     * 给本视图设置基本参数
     */
    public static class Images3Params extends LatticeView.ImageTextParams {
        public String[] urls; // 图片路径集合（必须）
        //ImageView.ScaleType. 图片显示的样式， 默认铺满
        public ImageView.ScaleType scaleType = ImageView.ScaleType.FIT_XY;
        //本控件的宽度，或者屏幕的宽度。或者设置一个合适的值
        public int width;// （必须）
        // 图片之间的间距，默认2px
        public int space = 2;
        //是否在4张图片时做特殊处理，仿微信朋友圈实现方式,默认不需要处理. 只有在预览时生效
//        public boolean four_special;
        // 删除图片. 当你需要设置删除图片时。设置该值
        public int deleteImage;
        // 给删除图片一个padding
        public int deleteImagePadding;
        // 最后一个不显示删除图片 (一般情况下是添加的图片)
        public boolean isLastDelete;
        // 最后一张图片（isLastDelete = true是才会生效）
        public int lastImage;

        public EditType type = EditType.PREVIEW_TYPE;

        public String[] getUrls() {
            return urls;
        }

        public Images3Params setUrls(String[] urls) {
            this.urls = urls;
            return this;
        }

        @Override
        public int[] getLatticeIds() {
            return latticeIds;
        }

        @Override
        public Images3Params setLatticeIds(int[] latticeIds) {
            this.latticeIds = latticeIds;
            return this;
        }

        @Override
        public int getMaxLine() {
            return maxLine;
        }


        @Override
        public Images3Params setBg_color(int bg_color) {
            super.bg_color = bg_color;
            return this;
        }

        @Override
        public Images3Params setImageLoader(ImageLoader imageLoader) {
            super.imageLoader = imageLoader;
            return this;
        }

        @Override
        public Images3Params setMaxLine(int maxLine) {
//            four_special = false;
            this.maxLine = maxLine;
            return this;
        }

        public ImageView.ScaleType getScaleType() {
            return scaleType;
        }

        public Images3Params setScaleType(ImageView.ScaleType scaleType) {
            this.scaleType = scaleType;
            return this;
        }

        public int getWidth() {
            return width;
        }

        public Images3Params setWidth(int width) {
            this.width = width;
            return this;
        }

        public int getSpace() {
            return space;
        }

        public Images3Params setSpace(int space) {
            this.space = space;
            return this;
        }

//        public boolean isFour_special() {
//            return four_special;
//        }
//
//        public Images3Params setFour_special(boolean four_special) {
//            this.four_special = four_special;
//            return this;
//        }

        public int getDeleteImage() {
            return deleteImage;
        }

        public Images3Params setDeleteImage(int deleteImage) {
            this.deleteImage = deleteImage;
            return this;
        }

        public int getDeleteImagePadding() {
            return deleteImagePadding;
        }

        public Images3Params setDeleteImagePadding(int deleteImagePadding) {
            this.deleteImagePadding = deleteImagePadding;
            return this;
        }

        public boolean isLastDelete() {
            return isLastDelete;
        }

        public Images3Params setLastDelete(boolean lastDelete) {
            isLastDelete = lastDelete;
            return this;
        }
    }
}
