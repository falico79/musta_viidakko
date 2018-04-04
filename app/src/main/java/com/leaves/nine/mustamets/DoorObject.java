package com.leaves.nine.mustamets;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by mikae on 3.4.2018.
 */

public class DoorObject implements GameObject {

    private static Rect target;
    private Rect source;
    private Bitmap itemBitmap;

    public DoorObject(int x, int y, int w, int h, int imageID){
        itemBitmap = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), imageID);
        target = new Rect(x, y, x+w, y+h);
        source = new Rect(0, 0, itemBitmap.getWidth(), itemBitmap.getHeight());
        this.target = new Rect(target.left, target.top, target.right, target.bottom);

    }

    public static boolean touchCollide(Rect rect) {
        return Rect.intersects(target, rect);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(itemBitmap, source, target,null);
    }

    @Override
    public void update() {

    }
}
