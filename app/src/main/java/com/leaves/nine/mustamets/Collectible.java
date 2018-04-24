package com.leaves.nine.mustamets;

import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Random;

/**
 * Created by mikae on 19.3.2018.
 */

public class Collectible implements GameObject {

    Rect target;

    AnimationManager animationManager;
    static Animation bananaAnimation = null;

    public Collectible(Rect target, AnimationManager animation){
        this.animationManager = animation;
        this.target = target;
        this.animationManager.playAnim(0);
    }

    public Collectible(){

    }

    public static Collectible addBanaani(XmlResourceParser parser) throws IOException, XmlPullParserException {
        if (bananaAnimation==null) {
            Bitmap bananaImage = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(),R.drawable.banaani);
            bananaAnimation = new Animation(new Bitmap[]{bananaImage}, 2);
        }
        float x = 0.5f, y = 0.5f;
        while(parser.next() != XmlPullParser.END_TAG) {
            for(int i = 0; i < parser.getAttributeCount(); i++) {
                if(parser.getAttributeName(i).equals("x")) {
                    x = Float.parseFloat(parser.getAttributeValue(i)) /100;
                } else if(parser.getAttributeName(i).equals("y")) {
                    y = Float.parseFloat(parser.getAttributeValue(i)) /100;
                }
            }
        }

        return new Collectible(new Rect(
                (int)(Constants.SCREEN_WIDTH * x),
                (int)(Constants.SCREEN_HEIGHT * y),
                (int)(Constants.SCREEN_WIDTH * x + Constants.SCREEN_WIDTH * 0.05f),
                (int)(Constants.SCREEN_HEIGHT * y + Constants.SCREEN_HEIGHT *0.1f)), new AnimationManager(new Animation[]{bananaAnimation}));
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
