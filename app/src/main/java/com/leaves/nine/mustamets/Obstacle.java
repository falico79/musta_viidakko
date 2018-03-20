package com.leaves.nine.mustamets;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by lasse on 16/03/2018.
 */

public class Obstacle implements GameObject {
    private Rect rectangle;
    private int color;

    public Rect getRectangle() {
        return rectangle;
    }


    public Obstacle(int x, int y, int w, int h, int color) {
        this.color = color;

        rectangle = new Rect(x, y, x+w, y+h);
    }

    public boolean playerCollide(Rect rect) {
        return Rect.intersects(rectangle, rect);// || Rect.intersects(rectangle2, player.getRectangle());
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawRect(rectangle, paint);
    }

    @Override
    public void update() {

    }
}
