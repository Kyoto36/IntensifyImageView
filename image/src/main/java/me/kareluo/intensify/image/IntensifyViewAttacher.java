package me.kareluo.intensify.image;

import android.content.Context;
import android.gesture.Gesture;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

/**
 * Created by felix on 15/12/17.
 */
public class IntensifyViewAttacher<P extends IntensifyView & IntensifyImage>
        implements View.OnTouchListener {
    private static final String TAG = "IntensifyViewAttacher";

    private P mIntensifyView;
    private ScaleGestureDetector mScaleGestureDetector;
    private GestureDetector mGestureDetector;

    public IntensifyViewAttacher(P intensifyView) {
        mIntensifyView = intensifyView;
        Context context = intensifyView.getContext();
        mScaleGestureDetector = new ScaleGestureDetector(context, new OnScaleGestureAdapter());
        mGestureDetector = new GestureDetector(context, new OnGestureAdapter());
        mIntensifyView.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        boolean gesture = mGestureDetector.onTouchEvent(event);
        boolean scale = mScaleGestureDetector.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                if (!gesture) mIntensifyView.home();
                break;
        }
        return gesture | scale;
    }

    private class OnScaleGestureAdapter extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mIntensifyView.addScale(detector.getScaleFactor(),
                    Math.round(detector.getFocusX()), Math.round(detector.getFocusY()));
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            mIntensifyView.home();
        }
    }

    private class OnGestureAdapter extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            mIntensifyView.nextStepScale(Math.round(e.getX()), Math.round(e.getY()));
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            mIntensifyView.scroll(distanceX, distanceY);
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            mIntensifyView.fling(velocityX, velocityY);
            return true;
        }
    }
}
