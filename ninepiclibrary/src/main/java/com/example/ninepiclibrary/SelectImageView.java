package com.example.ninepiclibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.ninepiclibrary.util.SizeUtils;

import java.util.ArrayList;
import java.util.List;

public class SelectImageView extends ViewGroup {

    int hSpace = SizeUtils.dp2px(30);//每张图片的横向间距
    int vSpace = SizeUtils.dp2px(30);//每张图片的纵向间距
    int childWidth = 0;//每张图片的宽度
    int childHeight = 0;//每张图片的高度
    int maxNum = 9;//最大图片数量
    int lineNum = 3;//每行的数量
    int circleRadius = SizeUtils.dp2px(30); // 删除按钮半径
    // 是否可以编辑
    boolean isEdit = false;

    // 自定义添加按钮
    Drawable addImage;

    List<String> imageList;
    // 添加按钮回调
    public OnClickListener itemAddClickListener;
    // 添加图片点击回调
    public ItemImageClickListener itemImageClickListener;

    public SelectImageView(Context context) {
        this(context, null);
    }

    public SelectImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SelectImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.SelectImageView);
        hSpace = (int) t.getDimension(R.styleable.SelectImageView_h_space, hSpace);
        vSpace = (int) t.getDimension(R.styleable.SelectImageView_v_space, vSpace);
        maxNum = t.getInt(R.styleable.SelectImageView_max_num, maxNum);
        lineNum = t.getInt(R.styleable.SelectImageView_line_num, lineNum);
        addImage = t.getDrawable(R.styleable.SelectImageView_add_image);
        circleRadius = (int) t.getDimension(R.styleable.SelectImageView_circle_radius, circleRadius);
        isEdit = t.getBoolean(R.styleable.SelectImageView_is_edit, isEdit);

        t.recycle();
        imageList = new ArrayList<>();
        // 编辑则不用显示添加功能
        if (isEdit)
            addView(new DeleteImageView(context));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int rw = MeasureSpec.getSize(widthMeasureSpec);
        int rh = MeasureSpec.getSize(heightMeasureSpec);
        // 图片宽高一样 整个ViewGroup的宽度VG减去横向间距的宽度 除以一行的数量 可得宽高
        childHeight = childWidth = (rw - hSpace * (lineNum - 1)) / lineNum;
        int childCount = this.getChildCount();
        for (int i = 0; i < childCount; i++) {
            DeleteImageView child = (DeleteImageView) this.getChildAt(i);
            // 设置子view的左边间距
            child.setLeft((i % lineNum) * (childWidth + hSpace));
            // 设置子view的上边间距
            child.setTop((i / lineNum) * (childWidth + vSpace));
        }
        // 以下重新计算整个view的宽高
        int vw = rw;
        int vh = childHeight;
        if (childCount < lineNum) {
            vw = childCount * (childWidth + hSpace);
        }

        if (childCount > lineNum) {
            int line = (childCount - 1) / lineNum + 1;
            vh = line * childHeight + (line - 1) * hSpace;
        }
        setMeasuredDimension(vw, vh);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = this.getChildCount();
        for (int i = 0; i < childCount; i++) {
            DeleteImageView child = (DeleteImageView) this.getChildAt(i);
            final int finalI1 = i;
            child.layout(child.getLeft(), child.getTop(), child.getLeft() + childWidth, child.getTop() + childHeight);
            // 展示删除按钮
            child.setCircleVisibility(isEdit);
            child.setCircleRadius(circleRadius);
            if (i == childCount - 1 && imageList.size() != maxNum && isEdit) {
                child.setCircleVisibility(false);
                // 最后一个view 并且图片不全的时候 显示自定义图片
                if (addImage != null) {
                    child.setImageDrawable(addImage);
                } else {
                    child.setBackgroundColor(Color.BLACK);
                }
                child.setOnClickListener(itemAddClickListener);
            } else {
                final String filePath = imageList.get(i);
                Glide.with(getContext()).load(filePath).into(child);
                child.setScaleType(DeleteImageView.ScaleType.CENTER_CROP);
                child.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (itemImageClickListener != null)
                            // 图片点击回调
                            itemImageClickListener.imageClick(finalI1, filePath);
                    }
                });
            }
            // 删除某个图片
            child.setImageDeleteListener(new ItemImageDeleteClickListener() {
                @Override
                public void imageDeleteClick() {
                    // 需要区分图片达到最大的时候
                    if (imageList.size() == maxNum && isEdit) {
                        imageList.remove(finalI1);
                    } else {
                        imageList.remove(finalI1);
                        removeViewAt(finalI1);
                    }
                    requestLayout();
                    postInvalidate();
                }
            });
        }
    }

    /**
     * 添加图片
     *
     * @param filePaths
     */
    public void addPhoto(List<String> filePaths) {
        if (imageList.size() + filePaths.size() <= maxNum) {
            int addNum;
            if (imageList.size() + filePaths.size() == maxNum && isEdit) { // 如果到了最大数量 就少加一个view  用之前加号的view显示图片
                addNum = filePaths.size() - 1;
            } else {
                addNum = filePaths.size();
            }
            for (int i = 0; i < addNum; i++) {
                addView(new DeleteImageView(getContext()));
            }
            imageList.addAll(filePaths);
            requestLayout();
            postInvalidate();
        }
    }

    /**
     * 设置加号的点击事件
     *
     * @param listener
     */
    public void setOnAddClickListener(OnClickListener listener) {
        itemAddClickListener = listener;
    }

    /**
     * 设置图片点击事件
     *
     * @param listener
     */
    public void setOnImageClickListener(ItemImageClickListener listener) {
        itemImageClickListener = listener;
    }

    /**
     * 获取当前还能新增多少图片
     */
    public int getRemainNum() {
        return maxNum - imageList.size();
    }

    /**
     * 获取当前选择的图片的filepath list
     *
     * @return
     */
    public List<String> getImageList() {
        return imageList;
    }

}


