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
    private Rect targetBanana;
    private Rect sourceBanana;
    private Rect targetHealth;
    private Rect sourceHealth;
    private Rect healthBarBackground;
    private Rect healthBar;
    private static int HEALTH_BAR_MAX_WIDTH;
    private static int HEALTH_BAR_MAX_HEIGHT;

    private Bitmap bananaCountIcon;
    private Bitmap healthBarFrame;

    private static int bananas = 100;
    private static int health = 5;

    public UserInterface() {
        rectPanel = new Rect(0,0,Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT / 10);
        bananaCountIcon = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.banaaniterttu);
        healthBarFrame = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.healthbar);

        sourceBanana = new Rect(0, 0, bananaCountIcon.getWidth(), bananaCountIcon.getHeight());
        targetBanana = new Rect(Constants.SCREEN_WIDTH / 128,
                Constants.SCREEN_HEIGHT / 65,
                (Constants.SCREEN_WIDTH / 128)+(Constants.SCREEN_WIDTH / 21),
                (Constants.SCREEN_HEIGHT / 65)+(Constants.SCREEN_HEIGHT / 12));



//        healthBarBackground = new Rect((int)(Constants.SCREEN_WIDTH * 0.4),
//                (int)(Constants.SCREEN_HEIGHT / 65),
//                (int)(Constants.SCREEN_WIDTH * 0.6),
//                (int)((Constants.SCREEN_HEIGHT / 65)+HEALTH_BAR_MAX_HEIGHT));



        sourceHealth = new Rect(0,0,healthBarFrame.getWidth(), healthBarFrame.getHeight());
        targetHealth = new Rect(
                (int)(Constants.SCREEN_WIDTH * 0.5 - Constants.SCREEN_WIDTH * 0.1),
                (int)((Constants.SCREEN_WIDTH * 0.2) / 2.25 - (Constants.SCREEN_WIDTH * 0.2) / 2.25 * 1.1),
                (int)(Constants.SCREEN_WIDTH * 0.5 + Constants.SCREEN_WIDTH * 0.1),
                (int)(((Constants.SCREEN_WIDTH * 0.2) / 2.25 - (Constants.SCREEN_WIDTH * 0.2) / 2.25 * 1.1)+((Constants.SCREEN_WIDTH * 0.2) / 2.25)));

        HEALTH_BAR_MAX_HEIGHT = (int)(targetHealth.height() * 0.2);
        HEALTH_BAR_MAX_WIDTH = (int)(targetHealth.width() * 0.73);


        healthBar = new Rect((int)(targetHealth.right - HEALTH_BAR_MAX_WIDTH),
                (int)(targetHealth.height() * 0.45 - HEALTH_BAR_MAX_HEIGHT * 0.5),
                (int)(targetHealth.right - HEALTH_BAR_MAX_WIDTH + getHealthBarWidth(health)),
                (int)(targetHealth.height() * 0.45 + HEALTH_BAR_MAX_HEIGHT * 0.5));
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
        return Rect.intersects(new Rect(0, 0, targetBanana.right + (Constants.SCREEN_WIDTH / 10), targetBanana.bottom), rect);// || Rect.intersects(rectangle2, player.getRectangle());
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
//        canvas.drawRect(targetBanana, paint);
        canvas.drawBitmap(bananaCountIcon, sourceBanana, targetBanana,null);
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
//        canvas.drawRect(targetBanana, paint);
//        healthBar = new Rect((int)(Constants.SCREEN_WIDTH * 0.4),
//                (int)(Constants.SCREEN_HEIGHT * 0.03),
//                (int)(Constants.SCREEN_WIDTH * 0.4 + getHealthBarWidth(health)),
//                (int)((Constants.SCREEN_HEIGHT * 0.03)+HEALTH_BAR_MAX_HEIGHT));
        healthBar = new Rect((int)(targetHealth.right - HEALTH_BAR_MAX_WIDTH),
                (int)(targetHealth.height() * 0.45 - HEALTH_BAR_MAX_HEIGHT * 0.5),
                (int)(targetHealth.right - HEALTH_BAR_MAX_WIDTH + getHealthBarWidth(health)),
                (int)(targetHealth.height() * 0.45 + HEALTH_BAR_MAX_HEIGHT * 0.5));
//        canvas.drawRect(healthBarBackground, paint);
        paint.setColor(Color.argb(255, 220,39, 55));
        canvas.drawRect(healthBar, paint);
        canvas.drawBitmap(healthBarFrame, sourceHealth, targetHealth,null);
    }

    @Override
    public void update() {

    }
}
