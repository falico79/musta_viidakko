package com.leaves.nine.mustamets;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import java.util.Random;

/**
 * Created by mikae on 19.3.2018.
 */

public class Collectible implements GameObject {

    Rect rectangle;
    int color;

    public Collectible(Rect rect, int color){
        this.rectangle = rect;
        this.color = color;
        rectangle.set(rect.centerX() - rectangle.width()/2, rect.centerY() - rectangle.height()/2, rect.centerX() + rectangle.width()/2, rect.centerY() + rectangle.height()/2);
    }

    public Rect getRectangle() {
        return this.rectangle;
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawRect(rectangle, paint);
    }

    public boolean playerCollide(Rect rect) {
        return Rect.intersects(rectangle, rect);// || Rect.intersects(rectangle2, player.getRectangle());
    }

    @Override
    public void update() {

    }

    public void update(Point point) {
        //rectangle.set(point.x - rectangle.width()/2, point.y - rectangle.height()/2, point.x + rectangle.width()/2, point.y + rectangle.height()/2);

    }
}
