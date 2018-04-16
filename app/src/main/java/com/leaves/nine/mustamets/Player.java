package com.leaves.nine.mustamets;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.Log;

import static com.leaves.nine.mustamets.UserInterface.*;
import static com.leaves.nine.mustamets.UserInterface.health;

/**
 * Created by lasse on 16/03/2018.
 */

public class Player implements GameObject {
    private Rect rectangle;
    private boolean killMonkey = false;

    private float speed;
    private AnimationManager animManager;

    private long lastTime;

    private float angle = 90;
    private int state = 0;
    private PointF calculatedMovement;
    private Point moveTo;

    public Player(Rect rectangle, float speed) {
        this.rectangle = rectangle;
        this.speed = speed;

        //BitmapFactory bf = new BitmapFactory();
        Bitmap idleImage = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.uuk1);
        Bitmap walk1 = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.uuk1);
        Bitmap walk2 = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.uuk2);
        Bitmap die1 = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.dying1);
        Bitmap die2 = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.dying2);
        Bitmap die3 = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.dying3);
        Bitmap takingDamage = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.takingdamage);

        Animation idleLookLeft = new Animation(new Bitmap[]{idleImage}, 2);
        Animation walkLeft = new Animation(new Bitmap[]{walk1, walk2}, 0.5f);

        Animation dieLeft = new Animation(new Bitmap[]{die1, die2, die3}, 0.5f);
        Animation damageLeft = new Animation(new Bitmap[]{takingDamage}, 0.5f);

        Animation idleDeadLeft = new Animation(new Bitmap[]{die3}, 2f);

        Matrix m = new Matrix();
        m.preScale(-1,1);
        idleImage = Bitmap.createBitmap(idleImage, 0, 0, idleImage.getWidth(), idleImage.getHeight(), m, false);
        walk1 = Bitmap.createBitmap(walk1, 0, 0, walk1.getWidth(), walk1.getHeight(), m, false);
        walk2 = Bitmap.createBitmap(walk2, 0, 0, walk2.getWidth(), walk2.getHeight(), m, false);

        die1 = Bitmap.createBitmap(die1, 0, 0, die1.getWidth(), die1.getHeight(), m, false);
        die2 = Bitmap.createBitmap(die2, 0, 0, die2.getWidth(), die2.getHeight(), m, false);
        die3 = Bitmap.createBitmap(die3, 0, 0, die3.getWidth(), die3.getHeight(), m, false);
        takingDamage = Bitmap.createBitmap(takingDamage, 0, 0, takingDamage.getWidth(), takingDamage.getHeight(), m, false);

        Animation idleLookRight = new Animation(new Bitmap[]{idleImage}, 2);
        Animation walkRight = new Animation(new Bitmap[]{walk1, walk2}, 0.5f);

        Animation dieRight = new Animation(new Bitmap[]{die1, die2, die3}, 0.5f);
        Animation damageRight = new Animation(new Bitmap[]{takingDamage}, 0.5f);

        Animation idleDeadRight = new Animation(new Bitmap[]{die3}, 2f);

        animManager = new AnimationManager(new Animation[]{idleLookRight, idleLookLeft, walkRight, walkLeft, dieRight, dieLeft, damageRight, damageLeft, idleDeadRight, idleDeadLeft});
        lastTime = System.currentTimeMillis();
        calculatedMovement = new PointF((float)(rectangle.centerX()), (float) rectangle.centerY());

        moveTo = new Point(rectangle.centerX(),rectangle.centerY());

    }

    public void killCharacter() {
        killMonkey = true;
    }

    public void doDamage(int damagePercentage) {
        if ((targetHealthValue - damagePercentage) > 0) {
            targetHealthValue -= damagePercentage;
            health = targetHealthValue;
        } else {
            targetHealthValue = 0;
            health = targetHealthValue;
            System.out.println("APINA KUOLI");
        }
    }

    public Rect getRectangle() {
        return this.rectangle;
    }

    public void moveTo(Point pos) {
        this.moveTo = pos;
        float deltaX, deltaY;
        deltaX = (float)(moveTo.x-rectangle.centerX());
        deltaY = (float)(moveTo.y-rectangle.centerY());
        angle = (float)Math.atan2(deltaX,deltaY);
    }

    @Override
    public void draw(Canvas canvas) {
        animManager.draw(canvas, rectangle);
    }

    @Override
    public void update() {
        animManager.update();
    }

    public void setPos(Point pos) {
        moveTo = pos;
        calculatedMovement.set((float)pos.x,(float)pos.y);
        rectangle.set(moveTo.x - rectangle.width()/2, moveTo.y - rectangle.height()/2, moveTo.x + rectangle.width()/2, moveTo.y + rectangle.height()/2);
    }

    public void updatePosition(long damageTime) {
        float oldLeft = calculatedMovement.x;
        long currentTime = System.currentTimeMillis();
        float deltaX, deltaY;

        deltaX = (float)Math.sin(angle) * speed * (float)(currentTime-lastTime)/1000;
        deltaY = (float)Math.cos(angle) * speed * (float)(currentTime-lastTime)/1000;

        if(!(calculatedMovement.x <= (float)moveTo.x + Math.abs(deltaX) && calculatedMovement.x >= (float)moveTo.x - Math.abs(deltaX)) &&
                !(calculatedMovement.y <= (float)moveTo.y +Math.abs(deltaY) && calculatedMovement.y >= (float)moveTo.y - Math.abs(deltaY))) {

            float newX = calculatedMovement.x + deltaX;
            float newY = calculatedMovement.y + deltaY;

            calculatedMovement.set(newX, newY);
        } else {
            calculatedMovement.set(moveTo.x, moveTo.y);
        }

        if (!killMonkey) {
            rectangle.set(
                    (int)calculatedMovement.x - rectangle.width()/2,
                    (int)calculatedMovement.y - rectangle.height()/2,
                    (int)calculatedMovement.x + rectangle.width()/2,
                    (int)calculatedMovement.y + rectangle.height()/2);
        }


        //idleLookRight, idleLookLeft, walkRight, walkLeft, dieRight, dieLeft, damageRight, damageLeft, idleDeadRight, idleDeadLeft
        //Log.i("Test", "state: " + state);


        if (!killMonkey) {
            if (damageTime > System.currentTimeMillis()) {
                if(calculatedMovement.x > oldLeft || state == 2 || state == 0) {
                    state = 6;
                } else if ( calculatedMovement.x < oldLeft || state == 1 || state == 3) {
                    state = 7;
                }
            } else {
                if(calculatedMovement.x > oldLeft ) {
                    state = 2;
                } else if ( calculatedMovement.x < oldLeft ) {
                    state = 3;
                } else if(state == 2 || state == 0 || state == 6) {
                    state = 0;
                } else {
                    state = 1;
                }
            }
        } else {
            if(calculatedMovement.x > oldLeft || state == 2 || state == 0) {
                state = 4;
            } else if ( calculatedMovement.x < oldLeft || state == 1 || state == 3) {
                state = 5;
            }

            if ( animManager.isAnimationDone()) {
                if (state == 4)
                    state = 8;
                if (state == 5)
                    state = 9;

                GamePlayScene.gameOver = true;
            }
        }

        animManager.playAnim(state);
        animManager.update();

        lastTime = System.currentTimeMillis();
    }
}
