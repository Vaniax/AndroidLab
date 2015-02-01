package de.tubs.androidlab.instameet.ui;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class SwipeDetector implements OnTouchListener {

    public static enum Action {
        LR, // Left to Right
        RL, // Right to Left
        TB, // Top to bottom
        BT, // Bottom to Top
        None // when no action was detected
    }

    private static final String TAG = "SwipeDetector";
    private static final int MIN_DISTANCE = 100;
    private float downX, downY, upX, upY;
    private Action swipeDetected = Action.None;

    public boolean swipeDetected() {
        return swipeDetected != Action.None;
    }

    public Action getAction() {
        return swipeDetected;
    }
    
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN: {
            downX = event.getX();
            downY = event.getY();
            swipeDetected = Action.None;
            return false; // allow other events like Click to be processed
        }
        case MotionEvent.ACTION_MOVE: {
            upX = event.getX();
            upY = event.getY();

            float deltaX = downX - upX;
            float deltaY = downY - upY;

            // horizontal swipe detection
            if (Math.abs(deltaX) > MIN_DISTANCE) {
                // left or right
                if (deltaX < 0) {
                    swipeDetected = Action.LR;
                    return true;
                }
                if (deltaX > 0) {
                    swipeDetected = Action.RL;
                    return true;
                }
            } else 

                // vertical swipe detection
                if (Math.abs(deltaY) > MIN_DISTANCE) {
                    // top or down
                    if (deltaY < 0) {
                        swipeDetected = Action.TB;
                        return false;
                    }
                    if (deltaY > 0) {
                        swipeDetected = Action.BT;
                        return false;
                    }
                } 
            return true;
        }
        }
        return false;
    }
}