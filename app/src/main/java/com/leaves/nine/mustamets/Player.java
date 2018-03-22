package com.leaves.nine.mustamets;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;

/**
 * Created by lasse on 16/03/2018.
 */

public class Player implements GameObject {
    private Rect rectangle;


    private Animation idle;
    private Animation walkRight;
    private Animation walkLeft;
    private float speed;
    private AnimationManager animManager;

    long lastTime;

    private float angle = 90;

    private PointF calculatedMovement;
    private Point moveTo;

    public Player(Rect rectangle, float speed) {
        this.rectangle = rectangle;
        this.speed = speed;

        //BitmapFactory bf = new BitmapFactory();
        Bitmap idleImage = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.alienblue);
        Bitmap walk1 = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.alienblue_walk1);
        Bitmap walk2 = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.alienblue_walk2);

        idle = new Animation(new Bitmap[]{idleImage},2);
        walkRight = new Animation(new Bitmap[]{walk1,walk2}, 0.5f);

        Matrix m = new Matrix();
        m.preScale(-1,1);
        walk1 = Bitmap.createBitmap(walk1, 0, 0, walk1.getWidth(), walk1.getHeight(), m, false);
        walk2 = Bitmap.createBitmap(walk2, 0, 0, walk2.getWidth(), walk2.getHeight(), m, false);

        walkLeft = new Animation(new Bitmap[]{walk1,walk2}, 0.5f);

        animManager = new AnimationManager(new Animation[]{idle, walkRight, walkLeft});
        lastTime = System.currentTimeMillis();
        calculatedMovement = new PointF((float)(rectangle.centerX()), (float) rectangle.centerY());

        moveTo = new Point(rectangle.centerX(),rectangle.centerY());
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

    public void updatePosition() {
        float oldLeft = rectangle.left;
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




        rectangle.set(
                (int)calculatedMovement.x - rectangle.width()/2,
                (int)calculatedMovement.y - rectangle.height()/2,
                (int)calculatedMovement.x + rectangle.width()/2,
                (int)calculatedMovement.y + rectangle.height()/2);

        int state = 0;
        if(rectangle.left-oldLeft > 5) {
            state = 1;
        } else if ( rectangle.left - oldLeft < -5) {
            state = 2;
        }
        animManager.playAnim(state);
        animManager.update();

        lastTime = System.currentTimeMillis();
    }
}
