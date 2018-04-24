package com.leaves.nine.mustamets;

import android.graphics.Canvas;
import android.os.Bundle;
import android.view.MotionEvent;

import java.util.ArrayList;

/**
 * Created by lasse on 16/03/2018.
 */

public class SceneManager {
    private ArrayList<Scene> scenes = new ArrayList<>();
    public static int ACTIVE_SCENE;

    public SceneManager(Bundle saveState) {
        ACTIVE_SCENE = 0;
        scenes.add(new GamePlayScene(saveState));
    }

    public void receiveTouch(MotionEvent event) {
        scenes.get(ACTIVE_SCENE).receiveTouch(event);
    }

    public void update() {
        scenes.get(ACTIVE_SCENE).update();
    }

    public void draw(Canvas canvas) {
        scenes.get(ACTIVE_SCENE).draw(canvas);
    }
}