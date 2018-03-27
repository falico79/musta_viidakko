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

    public Collectible(Rect target, int imageId){
        itemBitmap = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), imageId);
        source = new Rect(0, 0, itemBitmap.getWidth(), itemBitmap.getHeight());
        this.target = new Rect(target.left, target.top, target.left + target.width(), target.top + (int)((float)itemBitmap.getHeight()/(float)itemBitmap.getWidth()*target.width()));

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
}
