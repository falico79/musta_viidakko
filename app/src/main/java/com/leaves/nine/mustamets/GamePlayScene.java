package com.leaves.nine.mustamets;

import android.graphics.Canvas;
import android.view.MotionEvent;

/**
 * Created by lasse on 16/03/2018.
 */

public class GamePlayScene implements Scene {

    private GameObjectManager gameObjectManager;

    public static boolean gameOver = false;

    private static int[] mapList;
    private static int currentMapIndex = -1;

    public GamePlayScene() {
        gameObjectManager = new GameObjectManager();

        mapList = new int[]{ R.xml.map001, R.xml.map002, R.xml.map003, R.xml.map004 };
        gameObjectManager.loadMap(mapList[2]);
    }

    public static int nextMap()
    {
        return currentMapIndex < mapList.length - 1 ? mapList[++currentMapIndex] : mapList[0];
    }

    @Override
    public void update() {
        if(!gameOver) {
            gameObjectManager.update();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        gameObjectManager.draw(canvas);
    }

    @Override
    public void terminate() {
        SceneManager.ACTIVE_SCENE = 0;
    }

    @Override
    public void receiveTouch(MotionEvent event) {
        gameObjectManager.receiveTouch(event);
    }
}
