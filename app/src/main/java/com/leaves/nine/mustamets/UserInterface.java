package com.leaves.nine.mustamets;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by mikae on 26.3.2018.
 */

public class UserInterface implements GameObject{
    Rect rectPanel;
    Rect target;
    Rect source;

    Bitmap bananaCountIcon;

    private static int bananas = 0;


    public UserInterface() {
        rectPanel = new Rect(0,0,Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT / 10);
        bananaCountIcon = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.banaaniterttu);
        source = new Rect(0, 0, bananaCountIcon.getWidth(), bananaCountIcon.getHeight());
        target = new Rect(15,15,90,90);


    }

    public static void addBanana() {
        bananas++;
    }

    public static void removeBanana() {
        if (bananas > 0)
            bananas--;
    }

    public static int getBananas() {
        return bananas;
    }

    @Override
    public void draw(Canvas canvas) {
                    Paint paint = new Paint();
            paint.setTextSize(75);
            paint.setColor(Color.YELLOW);
        drawBananaCount(canvas, paint, "x " + bananas);
    }

    public boolean playerCollide(Rect rect) {
        return Rect.intersects(target, rect);// || Rect.intersects(rectangle2, player.getRectangle());
    }

    private void drawBananaCount(Canvas canvas, Paint paint, String text) {
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.getClipBounds(rectPanel);
        int cHeight = rectPanel.height();
        int cWidth = rectPanel.width();
        paint.getTextBounds(text, 0, text.length(), rectPanel);
        float x = Constants.SCREEN_WIDTH / 18;
        float y = Constants.SCREEN_HEIGHT / 14;
        canvas.drawText(text, x, y, paint);
//        canvas.drawRect(target, paint);
        canvas.drawBitmap(bananaCountIcon, source, target,null);
    }

    @Override
    public void update() {

    }
}
