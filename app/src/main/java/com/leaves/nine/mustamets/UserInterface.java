package com.leaves.nine.mustamets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;

import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;


import java.util.ArrayList;

/**
 * Created by mikae on 26.3.2018.
 */

public class UserInterface implements GameObject {

    private static final int POWER_UP_STRENGTH = 20;
    private Rect targetMusic;
    private Rect sourceMusic;
    private Rect targetMenu;
    private Rect sourceMenu;
    private Rect targetBanana;
    private Rect sourceBanana;
    private Rect targetHealth;

    private Rect healthBar;
    private static int HEALTH_BAR_MAX_WIDTH;
    private static int HEALTH_BAR_MAX_HEIGHT;
    private static MediaPlayer eatBananaSound;
    public static MediaPlayer ring;
    public static MediaPlayer pickupitem;
    private static AnimationManager heartAnimationManager;

    private Bitmap bananaCountIcon;
  
    private static Bitmap musicButton;
    private Bitmap musicButtonOff;

    private static Bitmap menuButton;

    public static int menuIcon = R.drawable.menu;

    public static int musicIcon = R.drawable.musicbutton;
    private static ArrayList<Integer> playlist;

    private static int bananas = 100;
    protected static int targetHealthValue = 50;
    private static float currentHealthValue = 0;

    private static int state = 0;
    private static long targetTime;
    private static int heartBeatTime;

    private static final int DEFAULT_HEARTBEAT_DELAY = 300;
    public static int health;

    private final int BUTTON_WIDTH = (int)(Constants.SCREEN_WIDTH * 0.07);
    private final int BUTTON_HEIGHT = (int) (Constants.SCREEN_HEIGHT * 0.12);

    public UserInterface() {

        initializeBananaButton();
        initializeMusicButton();
        initializeMenuButton();
        initializeHealthBar();

        HEALTH_BAR_MAX_HEIGHT = (int)(targetHealth.height() * 0.2);
        HEALTH_BAR_MAX_WIDTH = (int)(targetHealth.width() * 0.73);

        ring = MediaPlayer.create(Constants.CURRENT_CONTEXT,R.raw.taustamelu);
        ring.setLooping(true);
        ring.start();

        playlist = new ArrayList<>();
        playlist.add(R.raw.eating1);
        playlist.add(R.raw.eating2);

        eatBananaSound = MediaPlayer.create(Constants.CURRENT_CONTEXT,playlist.get(1));

        healthBar = getHealthBarRectangle(targetHealthValue);

        heartBeatTime = (int)(DEFAULT_HEARTBEAT_DELAY * currentHealthValue / 20);
        targetTime = System.currentTimeMillis() + heartBeatTime;
        currentHealthValue = targetHealthValue;

        health = targetHealthValue;
    }

    public static void resetValues(){
        targetHealthValue = 100;
        health = targetHealthValue;
        currentHealthValue = targetHealthValue;
        bananas = 3;
        state = 0;
        GamePlayScene.gameOver = false;
    }

    private void initializeHealthBar() {
        Bitmap healthBarFrame = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.healthbar);
        Bitmap healthBarFrameLow = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.healthbarlow);

        Animation heartStillAnimation = new Animation(new Bitmap[]{healthBarFrame}, 2);
        Animation heartBeatAnimation = new Animation(new Bitmap[]{healthBarFrame, healthBarFrameLow, healthBarFrame, healthBarFrameLow}, 0.35f);

        heartAnimationManager = new AnimationManager(new Animation[]{ heartStillAnimation, heartBeatAnimation });

//        int width = (int)(Constants.SCREEN_WIDTH * 0.24);
//        int height = (int)(Constants.SCREEN_WIDTH * 0.09);
//        int x = (int)(Constants.SCREEN_WIDTH * 0.38);
//        int y = (int)((Constants.SCREEN_WIDTH * 0.09) - (Constants.SCREEN_WIDTH * 0.09) * 1.1);
//
//        targetHealth = new Rect(x, y, x+width, y+height);
        int left = (int)(Constants.SCREEN_WIDTH * 0.4);
        int top = (int)(Constants.SCREEN_WIDTH * 0.09 - Constants.SCREEN_WIDTH * 0.09 * 1.1);
        int right = (int)(Constants.SCREEN_WIDTH * 0.6);
        int bottom = (int)(top + Constants.SCREEN_WIDTH * 0.09);

        targetHealth = new Rect(left, top, right, bottom);
    }

    private void initializeBananaButton() {
        bananaCountIcon = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.banaaninappi);
        sourceBanana = new Rect(0, 0, bananaCountIcon.getWidth(), bananaCountIcon.getHeight());

        targetBanana = new Rect(Constants.SCREEN_WIDTH / 128,
                Constants.SCREEN_HEIGHT / 65,
                (int) ((Constants.SCREEN_WIDTH / 128)+BUTTON_WIDTH),
                (int) ((Constants.SCREEN_HEIGHT / 65)+BUTTON_HEIGHT));
    }

    private void initializeMusicButton() {
        musicButton = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), musicIcon);
        sourceMusic = new Rect(0, 0, musicButton.getWidth(), musicButton.getHeight());
        targetMusic = new Rect((int) (Constants.SCREEN_WIDTH * 0.82),
                Constants.SCREEN_HEIGHT / 65,
                (int) ((Constants.SCREEN_WIDTH * 0.82)+BUTTON_WIDTH),
                (Constants.SCREEN_HEIGHT / 65)+BUTTON_HEIGHT);
    }

    private void initializeMenuButton() {
        menuButton = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), menuIcon);
        sourceMenu = new Rect(0, 0, menuButton.getWidth(), menuButton.getHeight());
        targetMenu = new Rect((int) (Constants.SCREEN_WIDTH * 0.92),
                Constants.SCREEN_HEIGHT / 65,
                (int) ((Constants.SCREEN_WIDTH * 0.92)+BUTTON_WIDTH),
                (Constants.SCREEN_HEIGHT / 65)+BUTTON_HEIGHT);
    }


    private Rect getHealthBarRectangle(int health){
        return new Rect((int)(targetHealth.right - HEALTH_BAR_MAX_WIDTH),
                (int)(targetHealth.height() * 0.45 - HEALTH_BAR_MAX_HEIGHT * 0.5),
                (int)(targetHealth.right - HEALTH_BAR_MAX_WIDTH + HEALTH_BAR_MAX_WIDTH * (health / 100.0f)),
                (int)(targetHealth.height() * 0.45 + HEALTH_BAR_MAX_HEIGHT * 0.5));
    }

    public static void addBanana() {
        pickupitem = MediaPlayer.create(Constants.CURRENT_CONTEXT,R.raw.pop);
        pickupitem.start();
        bananas++;

    }

    public static void eatBanana() {
        if (bananas > 0 && targetHealthValue <= 100 - POWER_UP_STRENGTH && health != 0) {
            bananas--;
            targetHealthValue += POWER_UP_STRENGTH;
            health = targetHealthValue;
            eatBananaSound.start();
        }
    }

    public static void stopMusic() {
        if (ring.isPlaying()){
            ring.stop();
            musicIcon = R.drawable.musicbuttonoff; //ikonin vaihto off-moodiin
            musicButton = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), musicIcon);
        } else {
            ring = MediaPlayer.create(Constants.CURRENT_CONTEXT,R.raw.taustamelu);
            ring.setLooping(true);
            ring.start();
            musicIcon = R.drawable.musicbutton; //ikonin vaihto takaisin
            musicButton = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), musicIcon);
        }
    }

    public static int getBananas() {
        return bananas;
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(Constants.SCREEN_HEIGHT / 15);
        drawBananaCount(canvas, paint, "x " + bananas);
        drawHealth(canvas, paint);
        drawMusicButton(canvas);
        drawMenuButton (canvas);
    }

    private void drawMenuButton(Canvas canvas) {
        canvas.drawBitmap(menuButton, sourceMenu, targetMenu,null);
    }

    public boolean playerCollide(Rect rect) {
        return Rect.intersects(new Rect(0, 0, targetBanana.right + (Constants.SCREEN_WIDTH / 10), targetBanana.bottom), rect);// || Rect.intersects(rectangle2, player.getRectangle());
    }

    public boolean musicButtonClick(Rect rect) {
        return Rect.intersects(new Rect(targetMusic.left, targetMusic.top, targetMusic.right, targetMusic.bottom), rect);// || Rect.intersects(rectangle2, player.getRectangle());
    }

    public boolean menuButtonClick(Rect rect) {
        return Rect.intersects(new Rect(targetMenu.left, targetMenu.top, targetMenu.right, targetMenu.bottom), rect);// || Rect.intersects(rectangle2, player.getRectangle());
    }

    private void drawBananaCount(Canvas canvas, Paint paint, String text) {

        paint.setColor(Color.YELLOW);
        paint.getTextBounds(text, 0, text.length(), new Rect(0,0,Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT / 10));
        float x = targetBanana.width() * 1.3f;
        float y = targetBanana.height() * 0.8f;
        canvas.drawText(text, x, y, paint);
        canvas.drawBitmap(bananaCountIcon, sourceBanana, targetBanana,null);

    }

    private void drawMusicButton(Canvas canvas) {
        canvas.drawBitmap(musicButton, sourceMusic, targetMusic,null);}

    private void drawHealth(Canvas canvas, Paint paint) {
        paint.setColor(Color.argb(255, 220,39, 55));

        if (currentHealthValue < 100 && currentHealthValue + 0.75 < targetHealthValue) {
             currentHealthValue += 0.75;
        } else if (currentHealthValue - 0.75 > targetHealthValue && currentHealthValue > 0) {
            currentHealthValue -= 0.75;
        } else {
            currentHealthValue = targetHealthValue;
        }

        healthBar = getHealthBarRectangle((int) currentHealthValue);

        canvas.drawRect(healthBar, paint);
        heartAnimationManager.draw(canvas, targetHealth);
    }

    @Override
    public void update() {
        heartBeatTime = (int)(DEFAULT_HEARTBEAT_DELAY * currentHealthValue / 20);

        if (targetHealthValue <= 20){
            if(heartAnimationManager.isAnimationDone() && state == 1) {
                state = 0;
                heartAnimationManager.playAnim(state);
                targetTime = System.currentTimeMillis() + heartBeatTime;
            }
            if(state == 0){
                if(System.currentTimeMillis()>= targetTime) {
                    state = 1;
                    heartAnimationManager.playAnim(state);
                }
            }
        }
        else {
            state = 0;
            heartAnimationManager.playAnim(state);
            targetTime = System.currentTimeMillis() + heartBeatTime;
        }

        heartAnimationManager.update();
    }
}
