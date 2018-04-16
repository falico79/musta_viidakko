package com.leaves.nine.mustamets;

import android.content.res.XmlResourceParser;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;


/**
 * Created by lasse on 16/03/2018.
 */

public class GamePlayScene implements Scene {

    private GameObjectManager gameObjectManager;



    public static boolean gameOver = false;

    private int[] mapList;
    private int currentMapIndex = -1;

    public GamePlayScene() {

        mapList = new int[]{ R.xml.map003 };


        gameObjectManager = new GameObjectManager();




        gameObjectManager.loadMap(nextMap());



    }

    private int nextMap()
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
