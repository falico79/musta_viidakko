package com.leaves.nine.mustamets;

import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by lasse on 26/03/2018.
 */

public class StoryItem extends Collectible implements StoryObject {
    private int state = 0;

    private long targetTime;

    private static boolean completed = false;
    private Canvas myCanvas;

    public StoryItem(AnimationManager animationManager, Rect target) {
        super(target, animationManager);
        targetTime = System.currentTimeMillis() + 1000;
    }

    public StoryItem(){

    }

    @Override
    public void update() {
        //super.update();

        if(animationManager.isAnimationDone() && state == 1) {
            state = 0;
            animationManager.playAnim(state);
            targetTime = System.currentTimeMillis() + 2000+ new Random().nextInt(5000);
        }
        if(state == 0){
            if(System.currentTimeMillis()>= targetTime) {
                state = 1;
                animationManager.playAnim(state);
            }
        }

        animationManager.update();


    }

    public static StoryItem addStoryItem(XmlResourceParser parser) throws IOException, XmlPullParserException {
        float x = 0.5f, y = 0.5f;
        ArrayList<Animation> animations = new ArrayList<>();

        while(parser.next() != XmlPullParser.END_TAG) {

            if(parser.getName().equals("position")) {
                for (int i = 0; i < parser.getAttributeCount(); i++) {
                    if (parser.getAttributeName(i).equals("x")) {
                        x = Float.parseFloat(parser.getAttributeValue(i)) / 100;
                    } else if (parser.getAttributeName(i).equals("y")) {
                        y = Float.parseFloat(parser.getAttributeValue(i)) / 100;
                    }
                }
                parser.next();
            } else if(parser.getName().equals("animation")) {
                if(parser.getAttributeCount() == 1) {
                    float animTime = parser.getAttributeFloatValue(0, 0.0f);


                    ArrayList<Bitmap> pictures = new ArrayList<>();
                    while (parser.next() != XmlPullParser.END_TAG) {
                        pictures.add(BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), parser.getAttributeResourceValue(0, -1)));
                        parser.next();
                    }

                    animations.add(new Animation(pictures.toArray(new Bitmap[pictures.size() ]), animTime));
                }
            }
        }


        return new StoryItem(new AnimationManager(animations.toArray(new Animation[animations.size()])), new Rect(
                (int)(Constants.SCREEN_WIDTH * x),
                (int)(Constants.SCREEN_HEIGHT * y),
                (int)(Constants.SCREEN_WIDTH * x + Constants.SCREEN_WIDTH * 0.05f),
                (int)(Constants.SCREEN_HEIGHT * y + Constants.SCREEN_HEIGHT * 0.1f)));

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
        else if (completed){
//            System.out.println("Next map");
//            GamePlayScene.nextMap();
        }
    }

    @Override
    public void advanceStory() {
        completed = true;
    }
}
