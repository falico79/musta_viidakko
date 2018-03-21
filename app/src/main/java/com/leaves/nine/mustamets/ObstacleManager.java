package com.leaves.nine.mustamets;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;

/**
 * Created by lasse on 16/03/2018.
 */

public class ObstacleManager {
    private ArrayList<Obstacle> obstacles;
    private int score = 0;

    private int playerGap;
    private int obstacleGap;
    private int obstacleHeight;
    private int color;

    private long startTime;
    private long initTime;

    public ObstacleManager(int playerGap, int obstacleGap, int obstacleHeight, int color) {
        this.playerGap = playerGap;
        this.obstacleGap = obstacleGap;
        this.obstacleHeight = obstacleHeight;
        this.color = color;

        startTime = initTime = System.currentTimeMillis();

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

//        if(obstacles.get(obstacles.size()-1).getRectangle().top >= Constants.SCREEN_HEIGHT) {
//            int xStart = (int)(Math.random()*(Constants.SCREEN_WIDTH-playerGap));
//            obstacles.add(0, new Obstacle(obstacleHeight, color, xStart, obstacles.get(0).getRectangle().top - obstacleHeight - obstacleGap, playerGap));
//            obstacles.remove(obstacles.size()-1);
//            score++;
//        }
    }

    public void draw(Canvas canvas) {
        for(Obstacle ob : obstacles) {
            ob.draw(canvas);
        }
    }

}
