package com.leaves.nine.mustamets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
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

public class UserInterface implements GameObject, PopupMenu.OnMenuItemClickListener {

  
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
    private static MediaPlayer ring;
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


    private void initializeHealthBar() {
        Bitmap healthBarFrame = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.healthbar);
        Bitmap healthBarFrameLow = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.healthbarlow);

        Animation heartStillAnimation = new Animation(new Bitmap[]{healthBarFrame}, 2);
        Animation heartBeatAnimation = new Animation(new Bitmap[]{healthBarFrame, healthBarFrameLow, healthBarFrame, healthBarFrameLow}, 0.35f);

        heartAnimationManager = new AnimationManager(new Animation[]{ heartStillAnimation, heartBeatAnimation });

        targetHealth = new Rect(
                (int)(Constants.SCREEN_WIDTH * 0.5 - Constants.SCREEN_WIDTH * 0.1),
                (int)((Constants.SCREEN_WIDTH * 0.2) / 2.25 - (Constants.SCREEN_WIDTH * 0.2) / 2.25 * 1.1),
                (int)(Constants.SCREEN_WIDTH * 0.5 + Constants.SCREEN_WIDTH * 0.1),
                (int)(((Constants.SCREEN_WIDTH * 0.2) / 2.25 - (Constants.SCREEN_WIDTH * 0.2) / 2.25 * 1.1)+((Constants.SCREEN_WIDTH * 0.2) / 2.25)));
    }

    private void initializeBananaButton() {
        bananaCountIcon = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.banaaninappi);
        sourceBanana = new Rect(0, 0, bananaCountIcon.getWidth(), bananaCountIcon.getHeight());
        targetBanana = new Rect(Constants.SCREEN_WIDTH / 128,
                Constants.SCREEN_HEIGHT / 65,
                (Constants.SCREEN_WIDTH / 128)+(Constants.SCREEN_WIDTH / 21),
                (Constants.SCREEN_HEIGHT / 65)+(Constants.SCREEN_HEIGHT / 12));
    }

    private void initializeMusicButton() {
        musicButton = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), musicIcon);
        sourceMusic = new Rect(0, 0, musicButton.getWidth(), musicButton.getHeight());
        targetMusic = new Rect((int) (Constants.SCREEN_WIDTH * 0.9),
                Constants.SCREEN_HEIGHT / 65,
                (int) ((Constants.SCREEN_WIDTH * 0.9)+(Constants.SCREEN_WIDTH / 21)),
                (Constants.SCREEN_HEIGHT / 65)+(Constants.SCREEN_HEIGHT / 12));
    }

    private void initializeMenuButton() {
        menuButton = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), menuIcon);
        sourceMenu = new Rect(0, 0, menuButton.getWidth(), menuButton.getHeight());
        targetMenu = new Rect((int) (Constants.SCREEN_WIDTH * 0.8),
                Constants.SCREEN_HEIGHT / 65,
                (int) ((Constants.SCREEN_WIDTH * 0.8)+(Constants.SCREEN_WIDTH / 21)),
                (Constants.SCREEN_HEIGHT / 65)+(Constants.SCREEN_HEIGHT / 12));
    }


    private Rect getHealthBarRectangle(int health){
        return new Rect((int)(targetHealth.right - HEALTH_BAR_MAX_WIDTH),
                (int)(targetHealth.height() * 0.45 - HEALTH_BAR_MAX_HEIGHT * 0.5),
                (int)(targetHealth.right - HEALTH_BAR_MAX_WIDTH + HEALTH_BAR_MAX_WIDTH * (health / 100.0f)),
                (int)(targetHealth.height() * 0.45 + HEALTH_BAR_MAX_HEIGHT * 0.5));
    }

    public static void addBanana() {
        bananas++;
    }

    public static void eatBanana() {
        if (bananas > 0 && targetHealthValue <= 95 && health != 0) {
            bananas--;
            targetHealthValue += 5;
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

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(Constants.CURRENT_CONTEXT, v, Gravity.CENTER);
        MenuInflater inflater = popup.getMenuInflater();
        popup.setOnMenuItemClickListener(this);
        inflater.inflate(R.menu.main_menu, popup.getMenu());
        popup.show();
        //Popup-menun tulostus
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.continue_game:
                //archive(item);
                return true;
            case R.id.new_game:
                //delete(item);
                return true;
            case R.id.help:
                Toast.makeText(Constants.CURRENT_CONTEXT, R.string.help_text,
                        Toast.LENGTH_LONG).show();
                return true;
            default:
                return false;
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
        float x = targetBanana.width() * 1.5f;
        float y = Constants.SCREEN_HEIGHT / 14;
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
