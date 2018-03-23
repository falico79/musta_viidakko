package com.leaves.nine.mustamets;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Created by lasse on 22/03/2018.
 */

public class Background implements GameObject {
    private Rect source;
    private Rect target;


    private Bitmap background;



    public Background (int imageId) {
        background = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), imageId);
        source = new Rect(0, 0, background.getWidth(), background.getHeight());
        target = new Rect(0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(background, source, target, null);
    }

    @Override
    public void update() {

    }
}
