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

/**
 * Created by mikae on 3.4.2018.
 */

public class DoorObject implements GameObject, StoryObject {

    private static Rect rectPanel;
    private static Rect target;
    private Rect source;
    private Bitmap itemBitmap;
    private String exitText;
    public static boolean drawPopup = false;

    public DoorObject(int x, int y, int w, int h, int imageID, int exitText){
        rectPanel = new Rect((int)(Constants.SCREEN_WIDTH * 0.8), (int)(Constants.SCREEN_HEIGHT * 0.6),
                (int)(Constants.SCREEN_WIDTH * 0.2), (int)(Constants.SCREEN_HEIGHT * 0.4));

        itemBitmap = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), imageID);
        target = new Rect(x, y, x+w, y+h);
        source = new Rect(0, 0, itemBitmap.getWidth(), itemBitmap.getHeight());
        this.exitText = Constants.CURRENT_CONTEXT.getString(exitText);
    }

    public static boolean playerCollide(Rect rect) {
        if (Rect.intersects(new Rect(target.left, (int)(target.top + target.top*0.3), target.right, (int)(target.bottom - target.bottom * 0.3)), rect)){
            drawPopup = true;
            return true;
        }
        drawPopup = false;
        return false;
    }

    public static void drawPopupMessage(Canvas canvas, Paint paint, String text) {
        paint.setTextAlign(Paint.Align.CENTER);

        float x = Constants.SCREEN_WIDTH * 0.5f;
        float y = Constants.SCREEN_HEIGHT * 0.5f;
        canvas.drawRect(rectPanel, new Paint(Color.argb(50, 0, 255, 200)));
        canvas.drawText(text, x, y, paint);


    }

    public static DoorObject addDoorObject(XmlResourceParser parser) throws IOException, XmlPullParserException {
        float x = 0.5f, y = 0.5f;
        int kuvaId = -1;
        int exitId = -1;

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
            } else if(parser.getName().equals("image")) {
                if(parser.getAttributeCount() == 1) {

                    kuvaId = parser.getAttributeResourceValue(0, -1);
                }
                parser.next();
            } else if(parser.getName().equals("exitQuestion")) {
                if(parser.getAttributeCount() == 1) {
                    exitId = parser.getAttributeResourceValue(0, -1);
                }
                parser.next();
            }
        }


        return new DoorObject(
                (int)(Constants.SCREEN_WIDTH * x - Constants.SCREEN_WIDTH * 0.05f*4),
                (int)(Constants.SCREEN_HEIGHT * y - Constants.SCREEN_HEIGHT * 0.1f*4),
                (int)(Constants.SCREEN_WIDTH * 0.05f*8),
                (int)(Constants.SCREEN_HEIGHT * 0.1f*8), kuvaId, exitId);

    }

    @Override
    public void draw(Canvas canvas) {


        canvas.drawBitmap(itemBitmap, source, target,null);
    }

    @Override
    public void update() {

    }

    @Override
    public StoryBoard openStoryBoard() {
        String yesText = Constants.CURRENT_CONTEXT.getString(R.string.Yes);
        ArrayList<String> vastaukset = new ArrayList<>();
        vastaukset.add(yesText);
        return new StoryBoard(this, exitText, vastaukset, 0);
    }

    @Override
    public void advanceStory() {

    }
}
