package com.leaves.nine.mustamets;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.BitSet;

/**
 * Created by mikae on 17.4.2018.
 */

public class StoryBoard implements GameObject {

    private Rect board;
    private Rect button;
    private Rect buttonSource;
    private Rect backgroundSource;
    private String message;
    private ArrayList<String> options;
    private ArrayList<Button> buttons;
    private int correctAnswer;
    private Bitmap backgroundGraphics;

    StoryObject callback = null;

    public StoryBoard(StoryObject caller, String message, ArrayList<String> options, int correctAnswer) {

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

        buttons = getButtons(options);

        backgroundGraphics = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.paperikorkea);
        backgroundSource = new Rect(0,0,backgroundGraphics.getWidth(), backgroundGraphics.getHeight());

        callback = caller;
    }

    private ArrayList<Button> getButtons(ArrayList<String> options){

        ArrayList<Button> buttons = new ArrayList<>();
        int[] buttonLefts = {(int) (Constants.SCREEN_WIDTH * 0.28), (int) (Constants.SCREEN_WIDTH * 0.52),
                (int) (Constants.SCREEN_WIDTH * 0.28), (int) (Constants.SCREEN_WIDTH * 0.52)};
        int[] buttonTops = {(int) (Constants.SCREEN_HEIGHT * 0.8), (int) (Constants.SCREEN_HEIGHT * 0.8),
                (int) (Constants.SCREEN_HEIGHT * 0.65), (int) (Constants.SCREEN_HEIGHT * 0.65)};

        Bitmap buttonGraphic = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.paperikapea);
        buttonSource = new Rect(0,0,buttonGraphic.getWidth(), buttonGraphic.getHeight());

        for (int i = 0; i < options.size(); i++){
            buttons.add(new Button(
                    new Rect(buttonLefts[i],buttonTops[i],
                            (int)(buttonLefts[i] + Constants.SCREEN_WIDTH * 0.2),
                            (int)(buttonTops[i] + Constants.SCREEN_HEIGHT * 0.1)),
                    options.get(i),  buttonGraphic));
        }

        return buttons;
    }

    @Override
    public void draw(Canvas canvas) {
        int textSize = Constants.SCREEN_HEIGHT / 24;
        Paint paint = new Paint();

        paint.setTextSize(textSize);
        canvas.drawBitmap(backgroundGraphics, backgroundSource, board, null);

        paint.setTypeface(Constants.PAPYRUS_FONT);
        paint.setColor(Color.BLACK);
        StaticLayout staticLayout = new StaticLayout(message, new TextPaint(paint), board.width() - textSize*2, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
//        canvas.save();

        int i = 0;
        for (Button button : buttons) {
            canvas.drawBitmap(button.getGraphics(), buttonSource, button.getRectangle(),null);
            canvas.drawText(options.get(i++), button.getRectangle().left + textSize, button.getRectangle().top + textSize * 1.5f, paint);
        }

        canvas.translate(board.left + textSize, board.top + textSize);
        staticLayout.draw(canvas);
//        canvas.restore();


//        canvas.drawText(message, board.left + textSize, board.right - textSize, board.left + textSize, board.top + textSize, paint);

//        canvas.drawBitmap(backgroundImage, board.left, board.top ,null);
    }

    @Override
    public void update() {

    }

    public void receiveTouch(MotionEvent event){

        int x = (int) event.getX();
        int y = (int) event.getY();

        Rect touchPoint = new Rect(x, y, x+1, y+1);

        switch (event.getAction()){
            case MotionEvent.ACTION_UP:
                for (Button button: buttons){
                    if (button.getRectangle().intersect(touchPoint) &&
                            buttons.indexOf(button) == correctAnswer){
                        System.out.println("OIKEIN");
                    } else {
                        System.out.println("VÄÄRIN");
                    }
                }
        }
    }
}
