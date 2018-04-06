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
    private Rect rectPanel;
    private Rect target;
    private Rect source;
    private Rect healthBarBackground;
    private Rect healthBar;
    private static int HEALTH_BAR_MAX_WIDTH;
    private static int HEALTH_BAR_MAX_HEIGHT;

    private Bitmap bananaCountIcon;

    private static int bananas = 100;
    private static int health = 50;

    public UserInterface() {
        rectPanel = new Rect(0,0,Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT / 10);
        bananaCountIcon = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.banaaniterttu);
        source = new Rect(0, 0, bananaCountIcon.getWidth(), bananaCountIcon.getHeight());
        target = new Rect(Constants.SCREEN_WIDTH / 128,
                Constants.SCREEN_HEIGHT / 72,
                (Constants.SCREEN_WIDTH / 128)+(Constants.SCREEN_WIDTH / 21),
                (Constants.SCREEN_HEIGHT / 72)+(Constants.SCREEN_HEIGHT / 12));

        HEALTH_BAR_MAX_HEIGHT = (int)(Constants.SCREEN_HEIGHT * 0.1);
        HEALTH_BAR_MAX_WIDTH = (int)(Constants.SCREEN_WIDTH * 0.2);

        healthBarBackground = new Rect((int)(Constants.SCREEN_WIDTH * 0.4),
                (int)(Constants.SCREEN_HEIGHT / 72),
                (int)(Constants.SCREEN_WIDTH * 0.6),
                (int)((Constants.SCREEN_HEIGHT / 72)+(Constants.SCREEN_HEIGHT / 20)));

        healthBar = new Rect((int)(Constants.SCREEN_WIDTH * 0.4),
                (int)(Constants.SCREEN_HEIGHT / 72),
                (int)(Constants.SCREEN_WIDTH * 0.4+getHealthBarWidth(health)),
                (int)((Constants.SCREEN_HEIGHT / 72)+(Constants.SCREEN_HEIGHT / 20)));

    }

    private float getHealthBarWidth(int health){
        return HEALTH_BAR_MAX_WIDTH * (health / 100.0f);
    }

    public static void addBanana() {
        bananas++;
    }

    public static void removeBanana() {
        if (bananas > 0 && health <= 95) {
            bananas--;
            health += 5;
        }


    }

    public static int getBananas() {
        return bananas;
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setTextSize(Constants.SCREEN_HEIGHT / 14);
        paint.setColor(Color.YELLOW);
        drawBananaCount(canvas, paint, "x " + bananas);
        paint.setColor(Color.BLACK);
        drawHealth(canvas, paint, health);
        System.out.println("HEALTH  " + getHealthBarWidth(health));
    }

    public boolean playerCollide(Rect rect) {
        return Rect.intersects(new Rect(0, 0, target.right + (Constants.SCREEN_WIDTH / 10), target.bottom), rect);// || Rect.intersects(rectangle2, player.getRectangle());
    }

    private void drawBananaCount(Canvas canvas, Paint paint, String text) {
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.getClipBounds(rectPanel);
//        int cHeight = rectPanel.height();
//        int cWidth = rectPanel.width();
        paint.getTextBounds(text, 0, text.length(), rectPanel);
        float x = Constants.SCREEN_WIDTH / 18;
        float y = Constants.SCREEN_HEIGHT / 14;
        canvas.drawText(text, x, y, paint);
//        canvas.drawRect(target, paint);
        canvas.drawBitmap(bananaCountIcon, source, target,null);
    }

    private void drawHealth(Canvas canvas, Paint paint, int health) {
//        paint.setTextAlign(Paint.Align.CENTER);
        canvas.getClipBounds(rectPanel);
//        int cHeight = rectPanel.height();
//        int cWidth = rectPanel.width();
//        paint.getTextBounds(text, 0, text.length(), rectPanel);
//        float x = Constants.SCREEN_WIDTH / 2;
//        float y = Constants.SCREEN_HEIGHT / 14;
//        canvas.drawText(text, x, y, paint);
//        canvas.drawRect(target, paint);
        healthBar = new Rect((int)(Constants.SCREEN_WIDTH * 0.4),
                (int)(Constants.SCREEN_HEIGHT / 72),
                (int)(Constants.SCREEN_WIDTH * 0.4+getHealthBarWidth(health)),
                (int)((Constants.SCREEN_HEIGHT / 72)+(Constants.SCREEN_HEIGHT / 20)));
        canvas.drawRect(healthBarBackground, paint);
        paint.setColor(Color.RED);
        canvas.drawRect(healthBar, paint);
    }

    @Override
    public void update() {

    }
}
