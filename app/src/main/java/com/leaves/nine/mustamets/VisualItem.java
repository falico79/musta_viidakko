package com.leaves.nine.mustamets;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import com.leaves.nine.mustamets.GameObject;

/**
 * Created by mikae on 19.3.2018.
 */

public class VisualItem implements GameObject {

    private Rect rectangle;
    private int color;

    public VisualItem(Rect rectangle, int color) {

        this.rectangle = rectangle;
        this.color = color;
    }


    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawRect(rectangle, paint);
    }

    @Override
    public void update(){

    }

    public void update(Point point) {
        rectangle.set(point.x - rectangle.width()/2, point.y - rectangle.height()/2, point.x + rectangle.width()/2, point.y + rectangle.height()/2);

    }
}
