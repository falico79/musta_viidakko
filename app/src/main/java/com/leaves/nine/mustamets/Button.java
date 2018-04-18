package com.leaves.nine.mustamets;

import android.graphics.Bitmap;
import android.graphics.Rect;

/**
 * Created by mikae on 18.4.2018.
 */

public class Button {
    private String text;
    private Rect rectangle;
    private Bitmap graphics;

    public Button(Rect rectangle, String text, Bitmap graphics){
        this.rectangle = rectangle;
        this.text = text;
        this.graphics = graphics;
    }

    public String getText(){
        return this.text;
    }

    public Rect getRectangle(){
        return this.rectangle;
    }

    public Bitmap getGraphics(){
        return this.graphics;
    }
}
