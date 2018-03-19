package com.leaves.nine.mustamets;

import android.graphics.Canvas;
import android.view.MotionEvent;

/**
 * Created by lasse on 16/03/2018.
 */

public interface Scene {
    void update();
    void draw(Canvas canvas);
    void terminate();
    void receiveTouch(MotionEvent event);
}
