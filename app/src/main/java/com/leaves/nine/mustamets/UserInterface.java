package com.leaves.nine.mustamets;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by mikae on 26.3.2018.
 */

public class UserInterface implements GameObject{
    private Rect rectPanel;
  
    private Rect targetMusic;
    private Rect sourceMusic;
    private Rect targetBanana;
    private Rect sourceBanana;
    private Rect targetHealth;
    private Rect sourceHealth;

    private Rect healthBarBackground;
    private Rect healthBar;
    private static int HEALTH_BAR_MAX_WIDTH;
    private static int HEALTH_BAR_MAX_HEIGHT;
    private static MediaPlayer eatBanana;
    private static MediaPlayer ring;
    private static AnimationManager heartAnimationManager;

    private Bitmap bananaCountIcon;
  
    private static Bitmap MusicButton;
    private Bitmap MusicButtonOff;

    public static int musicIcon = R.drawable.musicbutton;
    static ArrayList<Integer> playlist;
    static Random random;

    private Bitmap healthBarFrame;
    private Bitmap healthBarFrameLow;

    private static int bananas = 100;
    private static int targetHealthValue = 5;
    private static float currentHealthValue = 0;

    private static int state = 0;
    private static long targetTime;
    private static int heartBeatTime;

    private static final int DEFAULT_HEARTBEAT_DELAY = 300;



//    private static boolean lowHealth = false;

    public UserInterface() {
        rectPanel = new Rect(0,0,Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT / 10);
        bananaCountIcon = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.banaaniterttu);
        healthBarFrame = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.healthbar);
        healthBarFrameLow = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.healthbarlow);

        Animation heartStillAnimation = new Animation(new Bitmap[]{healthBarFrame}, 2);
        Animation heartBeatAnimation = new Animation(new Bitmap[]{healthBarFrame, healthBarFrameLow, healthBarFrame, healthBarFrameLow}, 0.35f);

        heartAnimationManager = new AnimationManager(new Animation[]{ heartStillAnimation, heartBeatAnimation });

        sourceBanana = new Rect(0, 0, bananaCountIcon.getWidth(), bananaCountIcon.getHeight());
        targetBanana = new Rect(Constants.SCREEN_WIDTH / 128,
                Constants.SCREEN_HEIGHT / 65,
                (Constants.SCREEN_WIDTH / 128)+(Constants.SCREEN_WIDTH / 21),
                (Constants.SCREEN_HEIGHT / 65)+(Constants.SCREEN_HEIGHT / 12));

        MusicButton = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), musicIcon);
        sourceMusic = new Rect(0, 0, MusicButton.getWidth(), MusicButton.getHeight());
        targetMusic = new Rect((int) (Constants.SCREEN_WIDTH * 0.9),
                Constants.SCREEN_HEIGHT / 65,
                (int) ((Constants.SCREEN_WIDTH * 0.9)+(Constants.SCREEN_WIDTH / 21)),
                (Constants.SCREEN_HEIGHT / 65)+(Constants.SCREEN_HEIGHT / 12));

        sourceHealth = new Rect(0,0,healthBarFrame.getWidth(), healthBarFrame.getHeight());
        targetHealth = new Rect(
                (int)(Constants.SCREEN_WIDTH * 0.5 - Constants.SCREEN_WIDTH * 0.1),
                (int)((Constants.SCREEN_WIDTH * 0.2) / 2.25 - (Constants.SCREEN_WIDTH * 0.2) / 2.25 * 1.1),
                (int)(Constants.SCREEN_WIDTH * 0.5 + Constants.SCREEN_WIDTH * 0.1),
                (int)(((Constants.SCREEN_WIDTH * 0.2) / 2.25 - (Constants.SCREEN_WIDTH * 0.2) / 2.25 * 1.1)+((Constants.SCREEN_WIDTH * 0.2) / 2.25)));

        HEALTH_BAR_MAX_HEIGHT = (int)(targetHealth.height() * 0.2);
        HEALTH_BAR_MAX_WIDTH = (int)(targetHealth.width() * 0.73);

        ring= MediaPlayer.create(Constants.CURRENT_CONTEXT,R.raw.taustamelu);
        ring.setLooping(true);
        ring.start();

        random=new Random();

        playlist = new ArrayList<>();
        playlist.add(R.raw.eating1);
        playlist.add(R.raw.eating2);

        eatBanana = MediaPlayer.create(Constants.CURRENT_CONTEXT,playlist.get(1));

        healthBar = new Rect((int)(targetHealth.right - HEALTH_BAR_MAX_WIDTH),
                (int)(targetHealth.height() * 0.45 - HEALTH_BAR_MAX_HEIGHT * 0.5),
                (int)(targetHealth.right - HEALTH_BAR_MAX_WIDTH + getHealthBarWidth(targetHealthValue)),
                (int)(targetHealth.height() * 0.45 + HEALTH_BAR_MAX_HEIGHT * 0.5));

        heartBeatTime = (int)(DEFAULT_HEARTBEAT_DELAY * currentHealthValue / 20);
        targetTime = System.currentTimeMillis() + heartBeatTime;
        currentHealthValue = targetHealthValue;
    }



    private float getHealthBarWidth(int health){
        return HEALTH_BAR_MAX_WIDTH * (health / 100.0f);
    }

    public static void addBanana() {
        bananas++;
    }

    public static void DoDamage(int damage){
        if (targetHealthValue - damage > 0) {
            targetHealthValue -= damage;
        } else {
            targetHealthValue = 0;
            System.out.println("APINA KUOLI");
        }
    }


    public static void removeBanana() {
        if (bananas > 0 && targetHealthValue <= 95) {
            bananas--;
            targetHealthValue += 5;
            eatBanana.start();
        }
    }

    public static void stopMusic() {
        if (ring.isPlaying()){
            ring.stop();
            musicIcon = R.drawable.musicbuttonoff; //ikonin vaihto off-moodiin
            MusicButton = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), musicIcon);
        } else {
            ring = MediaPlayer.create(Constants.CURRENT_CONTEXT,R.raw.taustamelu);
            ring.setLooping(true);
            ring.start();
                musicIcon = R.drawable.musicbutton; //ikonin vaihto takaisin
                MusicButton = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), musicIcon);
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
        drawHealth(canvas, paint);
//        System.out.println("HEALTH  " + getHealthBarWidth(targetHealthValue));
        drawMusicButton(canvas, paint);
    }

    public boolean playerCollide(Rect rect) {
        return Rect.intersects(new Rect(0, 0, targetBanana.right + (Constants.SCREEN_WIDTH / 10), targetBanana.bottom), rect);// || Rect.intersects(rectangle2, player.getRectangle());
    }

    public boolean musicButtonClick(Rect rect) {
        return Rect.intersects(new Rect(targetMusic.left, targetMusic.top, targetMusic.right, targetMusic.bottom), rect);// || Rect.intersects(rectangle2, player.getRectangle());
    }



    private void drawBananaCount(Canvas canvas, Paint paint, String text) {
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.getClipBounds(rectPanel);
        paint.getTextBounds(text, 0, text.length(), rectPanel);
        float x = Constants.SCREEN_WIDTH / 18;
        float y = Constants.SCREEN_HEIGHT / 14;
        canvas.drawText(text, x, y, paint);
        canvas.drawBitmap(bananaCountIcon, sourceBanana, targetBanana,null);
    }

    private void drawMusicButton(Canvas canvas, Paint paint) {
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.getClipBounds(rectPanel);
        canvas.drawBitmap(MusicButton, sourceMusic, targetMusic,null);}

    private void drawHealth(Canvas canvas, Paint paint) {
        canvas.getClipBounds(rectPanel);
        paint.setColor(Color.argb(255, 220,39, 55));


        if (currentHealthValue < 100 && currentHealthValue + 0.75 < targetHealthValue) {
             currentHealthValue += 0.75;
        } else if (currentHealthValue - 0.75 > targetHealthValue && currentHealthValue > 0) {
            currentHealthValue -= 0.75;
        } else {
            currentHealthValue = targetHealthValue;
        }

        healthBar = new Rect((int)(targetHealth.right - HEALTH_BAR_MAX_WIDTH),
                (int)(targetHealth.height() * 0.45 - HEALTH_BAR_MAX_HEIGHT * 0.5),
                (int)(targetHealth.right - HEALTH_BAR_MAX_WIDTH + getHealthBarWidth((int)(currentHealthValue))),
                (int)(targetHealth.height() * 0.45 + HEALTH_BAR_MAX_HEIGHT * 0.5));
        canvas.drawRect(healthBar, paint);
//        canvas.drawBitmap(healthBarFrame, sourceHealth, targetHealth,null);
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
