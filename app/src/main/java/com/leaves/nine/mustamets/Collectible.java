package com.leaves.nine.mustamets;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import java.util.Random;

/**
 * Created by mikae on 19.3.2018.
 */

public class Collectible implements GameObject {

    Rect source;
    Rect target;

    Bitmap itemBitmap;

    public Collectible(Rect rect){
        itemBitmap = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.luu);
        source = new Rect(0, 0, itemBitmap.getWidth(), itemBitmap.getHeight());
        target = new Rect(rect.left, rect.top, rect.left + rect.width(), rect.top + (int)((float)itemBitmap.getHeight()/(float)itemBitmap.getWidth()*rect.width()));

    }

    public Rect getRectangle() {
        return this.target;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(itemBitmap, source, target,null);
    }

    public boolean playerCollide(Rect rect) {
        return Rect.intersects(target, rect);// || Rect.intersects(rectangle2, player.getRectangle());
    }

    @Override
    public void update() {

    }

    public void update(Point point) {
        //rectangle.set(point.x - rectangle.width()/2, point.y - rectangle.height()/2, point.x + rectangle.width()/2, point.y + rectangle.height()/2);

    }
}
