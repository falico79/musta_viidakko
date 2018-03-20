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

    private PointF newPos;
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
        newPos = new PointF((float)(rectangle.centerX()), (float) rectangle.centerY());

        moveTo = new Point(rectangle.centerX(),rectangle.centerY());
    }

    public Rect getRectangle() {
        return this.rectangle;
    }

    public void moveTo(Point pos) {
        this.moveTo = pos;
        float vecX, vecY;
        vecX = (float)(moveTo.x-rectangle.centerX());
        vecY = (float)(moveTo.y-rectangle.centerY());
        angle = (float)Math.atan2(vecX,vecY);
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
        newPos.set((float)pos.x,(float)pos.y);
        rectangle.set(moveTo.x - rectangle.width()/2, moveTo.y - rectangle.height()/2, moveTo.x + rectangle.width()/2, moveTo.y + rectangle.height()/2);
    }

    public void updatePosition() {
        float oldLeft = rectangle.left;
        long currentTime = System.currentTimeMillis();
        if((int)newPos.x != moveTo.x) {
            float newX = (float)Math.sin(angle) * speed * (float)(currentTime-lastTime)/1000;

            //if(moveTo.x-rectangle.centerX() < 0)
                newX = newPos.x+newX;
            //else newX = newPos.x + newX;

            float newY = (float)Math.cos(angle) * speed * (float)(currentTime-lastTime)/1000;

            //if(moveTo.y-rectangle.centerY() < 0) {
                newY = newPos.y + newY;
           // } else newY = newPos.y+ newY;
            newPos.set(newX, newY);
        }




        rectangle.set((int)newPos.x - rectangle.width()/2, (int)newPos.y - rectangle.height()/2, (int)newPos.x + rectangle.width()/2, (int)newPos.y + rectangle.height()/2);

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
