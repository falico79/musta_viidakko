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

    private UserInterface userInterface;
    private Player player;
    private Point playerPosition;
    private Background background;
    private Background foreground;
    private ObstacleManager obstacleManager;
    private VisualItem visualItem;
    private CollectibleManager collectiblesManager;
    private Random random;
    private boolean movingPlayer = false;

    private boolean gameOver = false;
    private long gameOverTime;

    public GamePlayScene() {
        player = new Player(new Rect(100, 100, 300, 300), 500.0f );
        playerPosition = new Point(Constants.SCREEN_WIDTH/2, 3*Constants.SCREEN_HEIGHT/4);
        player.setPos(playerPosition);
        player.updatePosition();

        userInterface = new UserInterface();
        random = new Random();

        collectiblesManager = new CollectibleManager();
        collectiblesManager.addCollectibles(new Rect(200, 200, 300, 300),R.drawable.banaani);
        collectiblesManager.addCollectibles(new Rect(400, 400, 500, 500),R.drawable.banaani);

//        visualItem = new VisualItem(new Rect(100, 100, 200, 200), Color.rgb(127, 255, 0));
//        visualItem.update(new Point(Constants.SCREEN_WIDTH / 2, Constants.SCREEN_HEIGHT / 2));

        obstacleManager = new ObstacleManager(1, Color.argb(0,0,0,0));

        background = new Background(R.drawable.background);
        foreground = new Background(R.drawable.foreground);
    }


    public void reset() {
        playerPosition = new Point(Constants.SCREEN_WIDTH/2, 3*Constants.SCREEN_HEIGHT/4);
        player.setPos(playerPosition);
        obstacleManager = new ObstacleManager(1, Color.BLACK);
        movingPlayer = false;
    }

    @Override
    public void update() {
        if(!gameOver) {
            player.updatePosition();
            obstacleManager.update();

            if (collectiblesManager.playerCollide(player.getRectangle())) {


            }

        }
    }

    @Override
    public void draw(Canvas canvas) {

        background.draw(canvas);

        collectiblesManager.draw(canvas);
        player.draw(canvas);
        obstacleManager.draw(canvas);

        foreground.draw(canvas);

        userInterface.draw(canvas);
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
                    playerPosition.set(x, y);
                }

                Rect touchPoint = new Rect(x, y, x+1, y+1);
                if (userInterface.playerCollide(touchPoint)) {
                    UserInterface.removeBanana();
                    // add health
                }

                player.moveTo(playerPosition);


                break;
        }
    }
}
