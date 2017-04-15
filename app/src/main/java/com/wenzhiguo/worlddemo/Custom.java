package com.wenzhiguo.worlddemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by dell on 2017/4/14.
 */

public class Custom extends View {

    private int mImageHight;
    private int mImageWidth;
    private BitmapRegionDecoder mBitmapRegionDecoder;
    private int mPhoneHeight;
    private int mPhoneWidth;
    private Rect mRect;

    public Custom(Context context) {
        super(context);
    }

    public Custom(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Custom(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private static BitmapFactory.Options mOptions = new BitmapFactory.Options();

    static {
        //设置图片的颜色质量,使其不要太占内存
        mOptions.inPreferredConfig = Bitmap.Config.RGB_565;
    }

    public void setInput(InputStream inputStream) {
        //通过图片参数对象设置不会把这张图片加载到内存中,避免OOM的问题
        mOptions.inJustDecodeBounds = true;
        //将流和图片参数对象进行关联     参数:1.流  2.null 3.图片参数对象
        BitmapFactory.decodeStream(inputStream, null, mOptions);
        //获取图片的宽和高
        mImageHight = mOptions.outHeight;
        mImageWidth = mOptions.outWidth;
        //BitmapRegionDecoder用于解码图片,把图片字节流其中的一部分以矩形区域展示并转化成Bitmap对象.
        //参数1.流资源  2.对加载的图片进行复制
        try {
            mBitmapRegionDecoder = BitmapRegionDecoder.newInstance(inputStream, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获得手机屏幕的宽和高
        mPhoneHeight = getMeasuredHeight();
        mPhoneWidth = getMeasuredWidth();
        //设置默认加载到图片的中心位置
        int top = (mImageHight - mPhoneHeight) / 2;
        int left = (mImageWidth - mPhoneWidth) / 2;
        int buttom = top + mPhoneHeight;
        int right = left + mPhoneWidth;
        //将四个点的位置放到矩阵里面进行保存
        mRect = new Rect(left, top, right, buttom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //创建一个矩形图用手机屏幕放入图片
        Bitmap bitmap = mBitmapRegionDecoder.decodeRegion(mRect, mOptions);
        canvas.drawBitmap(bitmap, 0, 0, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        int mDownX = 0;
        int mDownY = 0;
        int mMoveX = 0;
        int mMoveY = 0;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = (int) x;
                mDownY = (int) y;
                break;
            case MotionEvent.ACTION_MOVE:
                mMoveX = (int) x;
                mMoveY = (int) y;
                //算出移动的距离x,y
                int a = mDownX - mMoveX;
                int b = mDownY - mMoveY;
                //把终点变成启点
                mDownX = mMoveX;
                mDownY = mMoveY;
                //根据移动的时候给显示的图片进行移动
                setRect(a, b);
                break;
        }
        //重走ondraw方法
        postInvalidate();
        return true;
    }

    private void setRect(int a, int b) {
        //修改矩阵的值
        mRect.offset(a, b);
        //判断是否手机的屏幕都到了图片的左部,上部,右部,下部
        if (mRect.left <= 0) {
            if (mRect.top <= 0 || mRect.bottom >= mImageHight) {
                mRect.top = 0;
                mRect.bottom = mImageHight;
            }
            //当到了左边,把最左边的参数设置为0
            mRect.left = 0;
            //最右边的参数设置为屏幕的宽
            mRect.right = mImageWidth;

        }//不能让用户超出右边界,右边界图片的宽
        else if (mRect.right >= mImageHight) {
            if (mRect.top <= 0 || mRect.bottom >= mImageHight) {
                mRect.top = 0;
                mRect.bottom = mImageHight;
            }
            //当到了右边界,把最左边的参数设置 图片宽-屏幕宽
            mRect.left = mImageHight - mImageWidth;
            //右边的参数,设置给图片的宽
            mRect.right = mImageHight;
        }//不能让用户超出顶部,顶部边界0
        else if (mRect.top <= 0) {
            if (mRect.left <= 0 || mRect.right >= mImageWidth) {
                mRect.left = 0;
                mRect.right = mImageWidth;
            }
            //当到了顶部,把最上面参数设置为0
            mRect.top = 0;
            //最下面的参数为屏幕高
            mRect.bottom = mImageHight;
        }//不让用户超出底部,顶部边界是图片的高
        else if (mRect.bottom >= mImageHight) {
            if (mRect.left <= 0 || mRect.right >= mImageWidth) {
                mRect.left = 0;
                mRect.right = mImageWidth;
            }
            //当到了顶部,图片高-屏幕高
            mRect.top = mImageHight - mImageHight;
            //最下面的参数为图片的高
            mRect.bottom = mImageHight;
        }
    }
}
