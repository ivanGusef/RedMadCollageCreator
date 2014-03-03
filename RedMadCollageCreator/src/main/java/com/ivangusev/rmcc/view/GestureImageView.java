package com.ivangusev.rmcc.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;
import com.ivangusev.rmcc.view.gesturedetector.MoveGestureDetector;
import com.ivangusev.rmcc.view.gesturedetector.RotateGestureDetector;

/**
 * Created by ivan on 23.02.14.
 */
public class GestureImageView extends ImageView implements View.OnTouchListener {

    private final Matrix mMatrix = new Matrix();

    private float mScaleFactor = 1.0f;
    private float mRotationDegrees = 0.f;
    private float mFocusX = 0.f;
    private float mFocusY = 0.f;

    private ScaleGestureDetector mScaleDetector;
    private RotateGestureDetector mRotateDetector;
    private MoveGestureDetector mMoveDetector;
    private GestureDetector mTapUpDetector;

    public GestureImageView(Context context) {
        this(context, null, 0);
    }

    public GestureImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GestureImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        mRotateDetector = new RotateGestureDetector(context, new RotateListener());
        mMoveDetector = new MoveGestureDetector(context, new MoveListener());
        mTapUpDetector = new GestureDetector(context, new TapUpListener());
        setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (isTouchOutsideDrawable(event)) return false;
        if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_DOWN) {
            bringToFront();
            requestLayout();
            postInvalidate();
        }
        mTapUpDetector.onTouchEvent(event);
        mRotateDetector.onTouchEvent(event);
        mScaleDetector.onTouchEvent(event);
        mMoveDetector.onTouchEvent(event);

        redrawMatrix();
        return true;
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        setupMatrix();
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        setupMatrix();
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        setupMatrix();
    }

    private boolean isTouchOutsideDrawable(MotionEvent event) {
        final boolean actionDown = (event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_DOWN;
        return actionDown && !isGestureAccepted(event.getX(), event.getY());
    }

    private void setupMatrix() {
        float scaledImageCenterX = (getDrawable().getIntrinsicWidth() * mScaleFactor) / 2;
        float scaledImageCenterY = (getDrawable().getIntrinsicHeight() * mScaleFactor) / 2;

        mFocusX = scaledImageCenterX;
        mFocusY = scaledImageCenterY;

        mMatrix.reset();
        setImageMatrix(mMatrix);
    }

    private void redrawMatrix() {
        float scaledImageCenterX = (getDrawable().getIntrinsicWidth() * mScaleFactor) / 2;
        float scaledImageCenterY = (getDrawable().getIntrinsicHeight() * mScaleFactor) / 2;

        mMatrix.reset();
        mMatrix.postScale(mScaleFactor, mScaleFactor);
        mMatrix.postRotate(mRotationDegrees, scaledImageCenterX, scaledImageCenterY);
        mMatrix.postTranslate(mFocusX - scaledImageCenterX, mFocusY - scaledImageCenterY);
        setImageMatrix(mMatrix);
    }

    private boolean isGestureAccepted(float eventX, float eventY) {
        float scaledImageCenterX = (getDrawable().getIntrinsicWidth() * mScaleFactor) / 2;
        float scaledImageCenterY = (getDrawable().getIntrinsicHeight() * mScaleFactor) / 2;

        final float[] pts = new float[2];
        pts[0] = eventX;
        pts[1] = eventY;

        final RectF rect = new RectF(
                mFocusX - scaledImageCenterX,
                mFocusY - scaledImageCenterY,
                mFocusX + scaledImageCenterX,
                mFocusY + scaledImageCenterY
        );
        final Matrix matrix = new Matrix();
        matrix.setRotate(-mRotationDegrees, mFocusX, mFocusY);
        matrix.mapPoints(pts);

        return rect.contains(pts[0], pts[1]);
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();
            return true;
        }
    }

    private class RotateListener extends RotateGestureDetector.SimpleOnRotateGestureListener {

        @Override
        public boolean onRotate(RotateGestureDetector detector) {
            mRotationDegrees -= detector.getRotationDegreesDelta();
            return true;
        }
    }

    private class MoveListener extends MoveGestureDetector.SimpleOnMoveGestureListener {

        @Override
        public boolean onMove(MoveGestureDetector detector) {
            PointF d = detector.getFocusDelta();
            mFocusX += d.x;
            mFocusY += d.y;
            invalidate();
            return true;
        }
    }

    private class TapUpListener implements GestureDetector.OnGestureListener {

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            bringToFront();
            return true;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    }
}
