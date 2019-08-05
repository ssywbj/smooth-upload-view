package com.suheng.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;

/**
 * 圆角ImageView
 */
public class RoundImageView extends AppCompatImageView {
    private float mCornersRadius;

    public RoundImageView(Context context) {
        super(context);
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.RoundImageView, 0, 0);
        try {
            mCornersRadius = typedArray.getDimension(R.styleable.RoundImageView_rivCornersRadius, 0);
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), toString());
        } finally {
            typedArray.recycle();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (mCornersRadius > 0) {
            Path path = new Path();
            RectF rectF = new RectF(0, 0, getWidth(), getHeight());
            path.addRoundRect(rectF, mCornersRadius, mCornersRadius, Path.Direction.CCW);
            canvas.clipPath(path);
        }
        super.draw(canvas);
    }
}
