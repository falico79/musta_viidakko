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

    private static Rect rectPanel;
    private static Rect target;
    private Rect source;
    private Bitmap itemBitmap;
    public static boolean drawPopup = false;

    public DoorObject(int x, int y, int w, int h, int imageID){
        rectPanel = new Rect((int)(Constants.SCREEN_WIDTH * 0.8), (int)(Constants.SCREEN_HEIGHT * 0.6),
                (int)(Constants.SCREEN_WIDTH * 0.2), (int)(Constants.SCREEN_HEIGHT * 0.4));

        itemBitmap = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), imageID);
        target = new Rect(x, y, x+w, y+h);
        source = new Rect(0, 0, itemBitmap.getWidth(), itemBitmap.getHeight());
        this.target = new Rect(target.left, target.top, target.right, target.bottom);

    }

    public static boolean touchCollide(Rect rect) {
        if (Rect.intersects(new Rect(target.left, (int)(target.top + target.top*0.3), target.right, (int)(target.bottom - target.bottom * 0.3)), rect)){
            drawPopup = true;
            return true;
        }
        drawPopup = false;
        return false;
    }

    public static void drawPopupMessage(Canvas canvas, Paint paint, String text) {
        paint.setTextAlign(Paint.Align.CENTER);

        float x = Constants.SCREEN_WIDTH * 0.5f;
        float y = Constants.SCREEN_HEIGHT * 0.5f;
        canvas.drawRect(rectPanel, new Paint(Color.argb(50, 0, 255, 200)));
        canvas.drawText(text, x, y, paint);

    }

    @Override
    public void draw(Canvas canvas) {


        canvas.drawBitmap(itemBitmap, source, target,null);
    }

    @Override
    public void update() {

    }
}
