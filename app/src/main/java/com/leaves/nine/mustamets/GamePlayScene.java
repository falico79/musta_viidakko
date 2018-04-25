package com.leaves.nine.mustamets;

import android.graphics.Canvas;
import android.view.MotionEvent;

import java.util.ArrayList;

/**
 * Created by lasse on 16/03/2018.
 */

public class GamePlayScene implements Scene {

    public static boolean gameOver = false;

    public static int[] mapList;
    private static int currentMapIndex = 0;

    StoryBoard storyBoard;


    public GamePlayScene() {
        Constants.objectManager = new GameObjectManager();

        mapList = new int[]{ R.xml.map001, R.xml.map002, R.xml.map003, R.xml.map004 };

        Constants.objectManager.loadMap(nextMap());

        storyBoard = new StoryBoard(
                Constants.CURRENT_CONTEXT.getString(R.string.help_text),
                Constants.CURRENT_CONTEXT.getString(R.string.button_continue),0);


    }

    public static int nextMap()
    {
        return currentMapIndex < mapList.length - 1 ? mapList[currentMapIndex++] : mapList[0];
    }

    @Override
    public void update() {
        if(!gameOver) {
            Constants.objectManager.update();
        }
    }

    @Override
    public void draw(Canvas canvas) {

        Constants.objectManager.draw(canvas);
        if (storyBoard != null) {
            storyBoard.draw(canvas);
        }
    }

    @Override
    public void terminate() {
        SceneManager.ACTIVE_SCENE = 0;
    }

    @Override
    public void receiveTouch(MotionEvent event) {

        Constants.objectManager.receiveTouch(event);
        if (storyBoard != null) {
            if (storyBoard.receiveTouch(event)) {
                storyBoard = null;
            }
        }
    }
}
