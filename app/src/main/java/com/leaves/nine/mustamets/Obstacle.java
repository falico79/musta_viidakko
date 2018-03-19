package com.leaves.nine.mustamets;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by lasse on 16/03/2018.
 */

public class Obstacle implements GameObject {
    private Rect rectangle;
    //private Rect rectangle2;
    private int color;

    public Rect getRectangle() {
        return rectangle;
    }

    public void incrementY(float y) {
        rectangle.top += y;
        rectangle.bottom += y;
//        rectangle2.top += y;
//        rectangle2.bottom += y;
    }

    public Obstacle(int x, int y, int w, int h, int color) {
        this.color = color;

        rectangle = new Rect(x, y, x+w, y+h);
//        rectangle2 = new Rect(startX + playerGap, startY, Constants.SCREEN_WIDTH, startY + rectangleHeight);
    }

    public boolean playerCollide(Rect rect) {
        return Rect.intersects(rectangle, rect);// || Rect.intersects(rectangle2, player.getRectangle());
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawRect(rectangle, paint);
//        canvas.drawRect(rectangle2, paint);
    }

    @Override
    public void update() {

    }
}
