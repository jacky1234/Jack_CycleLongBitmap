package com.sahadev.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import com.sahadev.cylinderapplication.R;

/**
 * Created by shangbin on 2016/6/16.
 * Email: sahadev@foxmail.com
 */
public class CylinderImageView extends View {
    //用于裁剪的原始图片资源
    private Bitmap mSourceBitmap = null;

    // 图片的高宽
    private int mBitmapHeight, mBitmapWidth;

    // 移动单位，每次移动多少个单位
    private final int mMoveUnit = 1;

    // 图片整体移动的偏移量
    private int xOffset = 0;

    // 用于持有两张拼接图片的引用，并释放原先的图片资源
    private Bitmap mPointerA, mPointerB;

    /**
     * 循环滚动标志位
     */
    private boolean mRunningFlag;

    public CylinderImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initVideoView();
    }

    public CylinderImageView(Context context) {
        super(context);
        initVideoView();
    }

    public CylinderImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initVideoView();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mSourceBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.android_m_hero_1200);

        /**
         *  获取图片的高宽
         */
        mBitmapHeight = mSourceBitmap.getHeight();
        mBitmapWidth = mSourceBitmap.getWidth();

        mRunningFlag = true;

        setFocusableInTouchMode(true);
        requestFocus();
    }

    private void initVideoView() {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 简单设置一下控件的宽高,这里的高度以图片的高度为准
        setMeasuredDimension(widthMeasureSpec, MeasureSpec.makeMeasureSpec(mBitmapHeight, MeasureSpec.EXACTLY));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        recycleTmpBitmap();

        // 如果一张图片轮播完，则从头开始
        if (xOffset >= mBitmapWidth) {
            xOffset = 0;
        }

        //  第一张图的宽带
        int tempWidth = xOffset + getMeasuredWidth() >= mBitmapWidth ? mBitmapWidth - xOffset : getMeasuredWidth();

        mPointerA = Bitmap
                .createBitmap(mSourceBitmap, xOffset, 0, tempWidth, getMeasuredHeight());

        // 绘制这张图
        canvas.drawBitmap(mPointerA, getMatrix(), null);

        // 如果最后的图片已经不足以填充整个屏幕，则截取图片的头部以连接上尾部，形成一个闭环
        if (tempWidth < getMeasuredWidth()) {
            Rect dst = new Rect(tempWidth, 0, getMeasuredWidth(), mBitmapHeight);
            mPointerB = Bitmap.createBitmap(mSourceBitmap, 0, 0, getMeasuredWidth() - tempWidth,
                    getMeasuredHeight());
            /**
             * 将另一张图片绘制在这张图片的后半部分
             * dst 指在canvas中的位置
             */

            canvas.drawBitmap(mPointerB, null, dst, null);
        }

        // 累计图片的偏移量
        xOffset += mMoveUnit;

        if (mRunningFlag) {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    invalidate();
                }
            }, 30);
        }
    }

    /**
     * 回收临时图像
     */
    private void recycleTmpBitmap() {
        if (mPointerA != null) {
            mPointerA.recycle();
            mPointerA = null;
        }

        if (mPointerB != null) {
            mPointerB.recycle();
            mPointerB = null;
        }
    }

    /**
     * 暂停
     */
    public void resume() {
        mRunningFlag = true;
        invalidate();
    }

    /**
     * 恢复
     */
    public void pause() {
        mRunningFlag = false;
    }

    /**
     * 回收清理工作
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        pause();
        recycleTmpBitmap();
        mSourceBitmap.recycle();
    }
}
