package com.leaves.nine.mustamets;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import java.util.Random;

/**
 * Created by mikae on 19.3.2018.
 */

public class Collectible implements GameObject {

    Rect target;

    AnimationManager animationManager;

    public Collectible(Rect target, AnimationManager animation){
        this.animationManager = animation;
        this.target = target;
        this.animationManager.playAnim(0);
    }

    public Rect getRectangle() {
        return this.target;
    }

    @Override
    public void draw(Canvas canvas) {
        animationManager.draw(canvas, target);
    }

    public boolean playerCollide(Rect rect) {
        return Rect.intersects(target, rect);// || Rect.intersects(rectangle2, player.getRectangle());
    }

    @Override
    public void update() {
        animationManager.update();
    }
}
