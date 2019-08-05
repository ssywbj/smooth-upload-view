package com.suheng.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class SmoothUploadView extends FrameLayout {
    private static final String TAG = SmoothUploadView.class.getSimpleName();
    private ProgressBar mProgressBar;
    private ImageView mImageView;
    private int mMaxProgress;
    private int mCurrentProgress;
    private boolean mIsLayoutDimenNotZero;

    public SmoothUploadView(Context context) {
        super(context);
        this.initView();
    }

    public SmoothUploadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView();
    }

    public SmoothUploadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initView();
    }

    private void initView() {
        View.inflate(getContext(), R.layout.smooth_upload_view, this);

        mImageView = findViewById(R.id.upload_image);

        mProgressBar = findViewById(R.id.upload_progress);
        this.setMaxProgress(100);
        mProgressBar.setRotation(90);
        setVisibility(GONE);
    }

    /**
     * 更新上传进度
     *
     * @param progress 百分比[0.0, 1.0]
     */
    public void updateProgress(double progress) {
        this.updateProgress((int) (progress * mMaxProgress));
    }

    /**
     * 更新上传进度
     *
     * @param progress 当前进度
     */
    public void updateProgress(int progress) {
        if (mIsLayoutDimenNotZero) {
            if (progress < 0) {
                return;
            }
            if (progress >= mMaxProgress) {
                progress = mMaxProgress;
            }
            if (mProgressBar.getProgress() > 0) {
                this.smoothUpdateProgress(mMaxProgress - progress);
                mCurrentProgress = progress;
            }
        }
    }

    /**
     * 平滑更新进度条(比更新时一跳一跳的效果好很多)
     *
     * @param progress 当前进度
     */
    private void smoothUpdateProgress(int progress) {
        ObjectAnimator animation = ObjectAnimator.ofInt(mProgressBar, "progress", progress);
        animation.setDuration(600);
        animation.setInterpolator(new LinearInterpolator());
        animation.start();
    }

    /**
     * 重置进度（可用于重新上传时调用）
     */
    public void resetProgress() {
        mProgressBar.setProgress(mMaxProgress);
        mCurrentProgress = 0;
    }

    /**
     * 设置上传的最大进度，默认的的最大进度为100
     *
     * @param maxProgress 最大进度值
     */
    public void setMaxProgress(int maxProgress) {
        mMaxProgress = maxProgress;
        mProgressBar.setMax(mMaxProgress);
    }

    /**
     * 设置需要上传的图片的缩略图
     *
     * @param bitmap 图片Bitmap对象
     */
    public void setImageBitmap(Bitmap bitmap) {
        this.resetProgress();
        mImageView.setImageBitmap(bitmap);
        this.setProgressView();
    }


    /**
     * 设置需要上传的图片的缩略图
     *
     * @param drawable 图片Drawable对象
     */
    public void setImageDrawable(@Nullable Drawable drawable) {
        this.resetProgress();
        mImageView.setImageDrawable(drawable);
        this.setProgressView();
    }

    /**
     * 设置需要上传的图片的缩略图
     *
     * @param resId 图片资源id
     */
    public void setImageResource(@DrawableRes int resId) {
        this.resetProgress();
        mImageView.setImageResource(resId);
        this.setProgressView();
    }

    /**
     * 设置覆盖在图片上面的进度条尺寸
     */
    private void setProgressView() {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        mIsLayoutDimenNotZero = (layoutParams.width != 0 && layoutParams.height != 0);

        if (mIsLayoutDimenNotZero) {
            final ViewGroup.LayoutParams imageLParams = mImageView.getLayoutParams();
            Log.d(TAG, "image dimen before post:  width = " + mImageView.getWidth()
                    + ",  height = " + mImageView.getHeight() + ", layout width = " + imageLParams.width
                    + ", layout height = " + imageLParams.height);
            mImageView.post(new Runnable() {
                @Override
                public void run() {
                    Log.i(TAG, "image dimen after post:  width = " + mImageView.getWidth()
                            + ", height = " + mImageView.getHeight() + ", layout width = " + imageLParams.width
                            + ", layout height = " + imageLParams.height);

                    if (mImageView.getWidth() == 0 || mImageView.getHeight() == 0) {
                        /*
                         有可能第一次post后没有拿到图片的宽高，此时需要递归调用到拿到图片的宽高为止（
                         第一次调用后需要把视图设置为可见(VISIBLE)，免得会反复调用该方法）
                         */
                        setProgressView();
                    } else {
                        ViewGroup.LayoutParams layoutParams = mProgressBar.getLayoutParams();
                        layoutParams.height = mImageView.getWidth();
                        layoutParams.width = mImageView.getHeight();
                        mProgressBar.setLayoutParams(layoutParams);
                    }
                }
            });

            setVisibility(VISIBLE);//设置图片后需要把视图，以免该方法进入死循环
        } else {
            //预防SmoothUploadView的尺寸被人为设置成Odp导致该方法进入死循环
            Log.e(TAG, "upload layout width or height dimen is zero: width = " +
                    layoutParams.width + ", height = " + layoutParams.height);
        }
    }

    public int getCurrentProgress() {
        return mCurrentProgress;
    }
}
