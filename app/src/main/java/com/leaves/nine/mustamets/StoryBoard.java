package com.leaves.nine.mustamets;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import java.util.ArrayList;
import java.util.BitSet;

/**
 * Created by mikae on 17.4.2018.
 */

public class StoryBoard implements GameObject {

    private Rect board;
    private String message;
    private ArrayList<String> options;
    private int correctAnswer;
    private Bitmap backgroundImage;

    public StoryBoard(/*StoryObject caller, */String message, ArrayList<String> options, int correctAnswer) {

        // options: 2-4 options
        // correctAnswer: index of correct answer in arraylist options
        this.message = message;
        this.options = options;
        this.correctAnswer = correctAnswer;

        int width = (int) (Constants.SCREEN_WIDTH * 0.5);
        int height = (int) (Constants.SCREEN_HEIGHT * 0.9);
        int x = (int) (Constants.SCREEN_WIDTH * 0.25);
        int y = (int) (Constants.SCREEN_HEIGHT * 0.05);

        board = new Rect(x, y, x + width, y + height);
        // backgroundImage =
    }

    @Override
    public void draw(Canvas canvas) {
        int textSize = Constants.SCREEN_HEIGHT / 24;
        Paint paint = new Paint();

        paint.setColor(Color.WHITE);
        paint.setTextSize(textSize);
        canvas.drawRect(board, paint);

        paint.setColor(Color.BLACK);
        StaticLayout staticLayout = new StaticLayout(message, new TextPaint(paint), board.width() - textSize*2, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
        canvas.save();


        canvas.translate(board.left + textSize, board.top + textSize);
        staticLayout.draw(canvas);
        canvas.restore();


//        canvas.drawText(message, board.left + textSize, board.right - textSize, board.left + textSize, board.top + textSize, paint);

//        canvas.drawBitmap(backgroundImage, board.left, board.top ,null);
    }

    @Override
    public void update() {

    }
}
