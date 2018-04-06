package com.leaves.nine.mustamets;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.Random;

/**
 * Created by lasse on 26/03/2018.
 */

public class StoryItem extends Collectible implements StoryObject {
    private int state = 0;

    private long targetTime;

    private boolean completed = false;
    private Canvas myCanvas;

    public StoryItem(AnimationManager animationManager, Rect target) {
        super(target, animationManager);
    }

    public StoryItem(){

    }

    @Override
    public void update() {
        //super.update();

        if(animationManager.isAnimationDone() && state == 1) {
            state = 0;
            animationManager.playAnim(state);
            targetTime = System.currentTimeMillis() + 5000 + new Random().nextInt(20000);
        }
        if(state == 0){
            if(System.currentTimeMillis()>= targetTime) {
                state = 1;
                animationManager.playAnim(state);
            }
        }

        animationManager.update();


    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        //animationManager.draw(canvas, target);
        openStoryBoard();
        myCanvas = canvas;
    }

    @Override
    public void openStoryBoard() {
        if (DoorObject.drawPopup && !completed) {
            Paint paint = new Paint();
            paint.setColor(Color.GREEN);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTextSize((Constants.SCREEN_HEIGHT / 15));
            DoorObject.drawPopupMessage(myCanvas, paint, "Kerää sahanterä ensin!!!");
        }
    }

    @Override
    public void advanceStory() {
        completed = true;
    }
}
