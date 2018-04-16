package com.leaves.nine.mustamets;

import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.ArrayList;

/**
 * Created by lasse on 16/03/2018.
 */

public class ObstacleManager {
    private ArrayList<Obstacle> obstacles;

    private int obstacleHeight;
    private int color;

    public ObstacleManager(int obstacleHeight, int color) {

        this.obstacleHeight = obstacleHeight;
        this.color = color;

        obstacles = new ArrayList<>();

        populateObstacles();
    }

    public boolean playerCollide(Rect rect) {
        for (Obstacle ob: obstacles) {
            if (ob.playerCollide(rect)){
                return true;
            }
        }
        return false;
    }

    private void populateObstacles() {
        obstacles.add(new Obstacle(0,0, Constants.SCREEN_WIDTH, obstacleHeight, color));
        obstacles.add(new Obstacle(0,Constants.SCREEN_HEIGHT - obstacleHeight, Constants.SCREEN_WIDTH, obstacleHeight, color));
        obstacles.add(new Obstacle(0,0,obstacleHeight, Constants.SCREEN_HEIGHT, color));
        obstacles.add(new Obstacle(Constants.SCREEN_WIDTH-obstacleHeight, 0, obstacleHeight, Constants.SCREEN_HEIGHT, color));

    }

    public void update() {

    }

    public void draw(Canvas canvas) {
        for(Obstacle ob : obstacles) {
            ob.draw(canvas);
        }
    }

}
