package com.example.ninepiclibrary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.Nullable;

import com.example.ninepiclibrary.util.SizeUtils;

public class DeleteImageView extends androidx.appcompat.widget.AppCompatImageView {

    private String DEFAULT_DELETE_TEXT = "X";

    // 圆形画笔
    Paint paint;

    // 文字画笔
    TextPaint textPaint;

    // 圆形位置
    private int cx = 0;
    private int cy = 0;
    private int circleRadius = SizeUtils.dp2px(30);

    // 是否显示删除圆角
    boolean mShowCircle = true;

    // 点击删除按钮
    private ItemImageDeleteClickListener itemImageDeleteClickListener;

    public DeleteImageView(Context context) {
        this(context, null);
    }

    public DeleteImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DeleteImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public void initView(Context context) {
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);
        // text
        textPaint = new TextPaint();
        textPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(30);
        textPaint.setColor(Color.WHITE);
    }

    /**
     * 设置是否展示删除圆角
     *
     * @param show
     */
    public void setCircleVisibility(boolean show) {
        this.mShowCircle = show;
        invalidate();
    }

    /**
     * 设置删除圆形的半径
     * @param radius
     */
    public void setCircleRadius(int radius) {
        this.circleRadius = radius;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 获取图片进行右上角的处理
        Drawable drawable = getDrawable();
        if (null != drawable && mShowCircle) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            @SuppressLint("DrawAllocation") final Rect rectSrc = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            @SuppressLint("DrawAllocation") final Rect rectDest = new Rect(0, circleRadius / 2, getWidth() - circleRadius / 2, getHeight());
            canvas.drawBitmap(bitmap, rectSrc, rectDest, paint);
        } else {
            super.onDraw(canvas);
        }
        // allocations per draw cycle.
        if (!mShowCircle) return;

        // 文字
        int textWidth = getTextWidth(DEFAULT_DELETE_TEXT, textPaint);
        int textHeight = getTextHeight(DEFAULT_DELETE_TEXT, textPaint);

        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;
        cx = contentWidth - circleRadius;
        cy = circleRadius;
        canvas.drawCircle(cx, cy, circleRadius, paint);
        canvas.drawText(DEFAULT_DELETE_TEXT, cx, cy + textHeight / 2, textPaint);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 没有删除按钮，则不用监听删除功能
        if (mShowCircle) {
            if (event.getAction() == MotionEvent.ACTION_UP) { // 获取屏幕上点击的坐标
                float x = event.getX();
                float y = event.getY();
                // 如果坐标在我们的文字区域内，则将点击的文字改颜色，扩大了点击区域
                if ((cx - circleRadius) < x && x < (cx + circleRadius) && (cy - circleRadius) < y && y < (cy + circleRadius)) {
                    itemImageDeleteClickListener.imageDeleteClick();
                    return true;
                }
            }
        }
        // 这句话不要修改
        return super.onTouchEvent(event);
    }

    public void setImageDeleteListener(ItemImageDeleteClickListener click) {
        this.itemImageDeleteClickListener = click;
    }

    private int getTextWidth(String text, Paint paint){
        Rect rect = new Rect();// 文字所在区域的矩形
        paint.getTextBounds(text, 0, text.length(), rect);
        return rect.width();
    }

    private int getTextHeight(String text, Paint paint){
        Rect rect = new Rect();// 文字所在区域的矩形
        paint.getTextBounds(text, 0, text.length(), rect);
        return rect.height();
    }

}
