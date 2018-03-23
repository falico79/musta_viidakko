package com.leaves.nine.mustamets;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;

import java.util.Random;

/**
 * Created by lasse on 16/03/2018.
 */

public class GamePlayScene implements Scene {
    //private SceneManager manager;
    private Rect r = new Rect();

    private Player player;
    private Point playerPosition;
    private Background background;
    private Background foreground;
    private ObstacleManager obstacleManager;
    private BackgroundItem backgroundItem;
    private Collectible banana;
    private CollectibleManager bananas;
    private Random random;
    private boolean movingPlayer = false;

    private boolean gameOver = false;
    private long gameOverTime;

    public GamePlayScene() {
        player = new Player(new Rect(100, 100, 200, 200), 250.0f );
        playerPosition = new Point(Constants.SCREEN_WIDTH/2, 3*Constants.SCREEN_HEIGHT/4);
        player.setPos(playerPosition);
        player.updatePosition();

        random = new Random();

        bananas = new CollectibleManager();
        bananas.addCollectibles(new Rect(200, 200, 300, 300));
        //banana = new Collectible(new Rect(100, 100, 200, 200), Color.rgb(255, 255, 0));
       // banana.update(new Point(Constants.SCREEN_WIDTH / 2, Constants.SCREEN_HEIGHT / 3));

        backgroundItem = new BackgroundItem(new Rect(100, 100, 200, 200), Color.rgb(127, 255, 0));
        backgroundItem.update(new Point(Constants.SCREEN_WIDTH / 2, Constants.SCREEN_HEIGHT / 2));

        obstacleManager = new ObstacleManager(200,350,75, Color.BLACK);

        background = new Background(R.drawable.background);
        foreground = new Background(R.drawable.foreground);
    }


    public void reset() {
        playerPosition = new Point(Constants.SCREEN_WIDTH/2, 3*Constants.SCREEN_HEIGHT/4);
        player.setPos(playerPosition);
        obstacleManager = new ObstacleManager(200,350,75, Color.BLACK);
        movingPlayer = false;
    }

    @Override
    public void update() {
        if(!gameOver) {
            player.updatePosition();
            obstacleManager.update();

            if (bananas.playerCollide(player.getRectangle()))
                System.out.println("Collected items: "+bananas.getCollectedItems());
//            if(obstacleManager.playerCollide(player.getRectangle())) {
//                gameOver = true;
//                gameOverTime = System.currentTimeMillis();
//            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        //canvas.drawColor(Color.rgb(127, 63, 0));
        background.draw(canvas);
        backgroundItem.draw(canvas);
      //  if (banana != null)
      //      banana.draw(canvas);
        bananas.draw(canvas);
        player.draw(canvas);
        obstacleManager.draw(canvas);

        foreground.draw(canvas);

//        if (gameOver) {
//            Paint paint = new Paint();
//            paint.setTextSize(100);
//            paint.setColor(Color.MAGENTA);
//            drawCenterText(canvas, paint, "Game Over");
//        }
    }
    private void drawCenterText(Canvas canvas, Paint paint, String text) {
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.getClipBounds(r);
        int cHeight = r.height();
        int cWidth = r.width();
        paint.getTextBounds(text, 0, text.length(), r);
        float x = cWidth / 2f - r.width() / 2f - r.left;
        float y = cHeight / 2f + r.height() / 2f - r.bottom;
        canvas.drawText(text, x, y, paint);
    }

    @Override
    public void terminate() {
        SceneManager.ACTIVE_SCENE = 0;
    }

    @Override
    public void receiveTouch(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                int x = (int)event.getX();
                int y = (int)event.getY();
                int w = player.getRectangle().width();
                int h = player.getRectangle().height();

                Rect rect = new Rect(x-w/2, y-h/2, x+w/2, y+h/2);

                if(!obstacleManager.playerCollide(rect)) {
                    playerPosition.set(x,y);

                    if (banana != null && banana.playerCollide(player.getRectangle())){
                        banana = null;
//                        banana.update(new Point(Constants.SCREEN_WIDTH / random.nextInt(10)+2, Constants.SCREEN_HEIGHT / random.nextInt(10)+2));
                    }
                }

                    player.moveTo(playerPosition);

                break;
        }
    }
}
