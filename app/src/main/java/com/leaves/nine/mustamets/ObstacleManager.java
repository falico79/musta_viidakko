package com.leaves.nine.mustamets;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

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

    public boolean playerCollide(Player player) {
        for (Obstacle ob: obstacles) {
            if (ob.playerCollide(player)){
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
//        int currY = -5*Constants.SCREEN_HEIGHT/4;
//        while(currY < 0) {
//            int xStart = (int)(Math.random()*(Constants.SCREEN_WIDTH-playerGap));
//            obstacles.add(new Obstacle(obstacleHeight, color, xStart, currY, playerGap));
//            currY += obstacleGap + obstacleHeight;
//        }
    }

    public void update() {
//        int elapsedTime = (int)(System.currentTimeMillis()-startTime);
//        startTime = System.currentTimeMillis();
//        float speed = (float)(Math.sqrt(1 + (startTime-initTime)/2000.0f)) * Constants.SCREEN_HEIGHT/10000.0f;
//        for(Obstacle ob : obstacles) {
//            ob.incrementY(speed*elapsedTime);
//        }
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
        Paint paint = new Paint();
        paint.setTextSize(100);
        paint.setColor(Color.MAGENTA);
        canvas.drawText("" + score, 50, 50 + paint.descent() - paint.ascent(), paint);
    }

}
