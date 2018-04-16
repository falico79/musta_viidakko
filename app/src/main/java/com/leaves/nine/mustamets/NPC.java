package com.leaves.nine.mustamets;

import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by lasse on 26/03/2018.
 */

public class NPC implements StoryObject, GameObject {

    public NPC(AnimationManager animationManager, Rect rect) {

    }

    @Override
    public void draw(Canvas canvas) {

    }

    @Override
    public void update() {

    }

    @Override
    public void openStoryBoard() {

    }

    @Override
    public void advanceStory() {

    }

    public static NPC addNPC(XmlResourceParser parser) throws IOException, XmlPullParserException {
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


        return new NPC(new AnimationManager(animations.toArray(new Animation[animations.size()])), new Rect(
                (int)(Constants.SCREEN_WIDTH * x),
                (int)(Constants.SCREEN_HEIGHT * y),
                (int)(Constants.SCREEN_WIDTH * x + Constants.SCREEN_WIDTH * 0.05f),
                (int)(Constants.SCREEN_HEIGHT * y + Constants.SCREEN_HEIGHT * 0.1f)));

    }
}
