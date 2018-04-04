package com.leaves.nine.mustamets;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.view.MotionEvent;

import java.util.Random;

/**
 * Created by lasse on 16/03/2018.
 */

public class GamePlayScene implements Scene {
    //private SceneManager manager;
    private Rect r = new Rect();

    private DoorObject doorObject;
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
        player = new Player(
                new Rect((int)(Constants.SCREEN_WIDTH * 0.5f),
                        (int)(Constants.SCREEN_HEIGHT * 0.75f),
                        (int)(Constants.SCREEN_WIDTH * 0.5f + Constants.SCREEN_WIDTH * 0.1f),
                        (int)(Constants.SCREEN_HEIGHT * 0.75f + Constants.SCREEN_HEIGHT * 0.2f)),
                (Constants.SCREEN_WIDTH * 0.4f) );
        playerPosition = new Point((int)(Constants.SCREEN_WIDTH * 0.5f), (int)(Constants.SCREEN_HEIGHT * 0.75f));
        player.setPos(playerPosition);
        player.updatePosition();

        userInterface = new UserInterface();
        random = new Random();

        collectiblesManager = new CollectibleManager();


        Bitmap collectibleImage = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(),R.drawable.banaani);
        Animation stillAnimation = new Animation(new Bitmap[]{collectibleImage}, 2);
        AnimationManager aniManager = new AnimationManager(new Animation[]{stillAnimation});
        collectiblesManager.addCollectibles(new Collectible(new Rect((int)(Constants.SCREEN_WIDTH * 0.5f),
                (int)(Constants.SCREEN_HEIGHT * 0.3f),
                (int)(Constants.SCREEN_WIDTH * 0.5f + Constants.SCREEN_WIDTH * 0.05f),
                (int)(Constants.SCREEN_HEIGHT *0.3f + Constants.SCREEN_HEIGHT *0.1f)), aniManager));

        collectiblesManager.addCollectibles(new Collectible(new Rect((int)(Constants.SCREEN_WIDTH * 0.3f),
                (int)(Constants.SCREEN_HEIGHT * 0.7f),
                (int)(Constants.SCREEN_WIDTH * 0.3f + Constants.SCREEN_WIDTH * 0.05f),
                (int)(Constants.SCREEN_HEIGHT * 0.7f+ Constants.SCREEN_HEIGHT *0.1f)), aniManager));

        Bitmap tera = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.sahantera1);
        stillAnimation = new Animation(new Bitmap[]{tera}, 2);
        Bitmap tera2 = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.sahantera2);
        Bitmap tera3 = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.sahantera3);
        Bitmap tera4 = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.sahantera4);
        Bitmap tera5 = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.sahantera5);
        Animation blink = new Animation(new Bitmap[]{tera2, tera3,tera4,tera5,tera4,tera3,tera2},0.5f );
        aniManager = new AnimationManager(new Animation[]{stillAnimation, blink});

        collectiblesManager.addCollectibles(new StoryItem(aniManager, new Rect((int)(Constants.SCREEN_WIDTH * 0.6f),
                (int)(Constants.SCREEN_HEIGHT * 0.4f),
                (int)(Constants.SCREEN_WIDTH * 0.6f + Constants.SCREEN_WIDTH * 0.05f),
                (int)(Constants.SCREEN_HEIGHT * 0.4f + Constants.SCREEN_HEIGHT * 0.1f))));
        /*Bitmap idleImage = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.b(), R.drawable.uuk1);
        Bitmap walk1 = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.uuk1);
        Bitmap walk2 = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.uuk2);

        idleLookLeft = new Animation(new Bitmap[]{idleImage},2);
        walkLeft = new Animation(new Bitmap[]{walk1,walk2}, 0.5f);

        Matrix m = new Matrix();
        m.preScale(-1,1);
        idleImage = Bitmap.createBitmap(idleImage, 0, 0, idleImage.getWidth(), idleImage.getHeight(), m, false);
        walk1 = Bitmap.createBitmap(walk1, 0, 0, walk1.getWidth(), walk1.getHeight(), m, false);
        walk2 = Bitmap.createBitmap(walk2, 0, 0, walk2.getWidth(), walk2.getHeight(), m, false);

        idleLookRight = new Animation(new Bitmap[]{idleImage},2);
        walkRight = new Animation(new Bitmap[]{walk1,walk2}, 0.5f);

        animManager = new AnimationManager(new Animation[]{idleLookRight, idleLookLeft, walkRight, walkLeft});
        lastTime = System.currentTimeMillis();
        calculatedMovement = new PointF((float)(rectangle.centerX()), (float) rectangle.centerY());

        moveTo = new Point(rectangle.centerX(),rectangle.centerY());
*/
//        visualItem = new VisualItem(new Rect(100, 100, 200, 200), Color.rgb(127, 255, 0));
//        visualItem.update(new Point(Constants.SCREEN_WIDTH / 2, Constants.SCREEN_HEIGHT / 2));

        obstacleManager = new ObstacleManager(1, Color.argb(0,0,0,0));

        background = new Background(R.drawable.background);
        foreground = new Background(R.drawable.foreground);

        doorObject = new DoorObject(Constants.SCREEN_WIDTH - ((int)(Constants.SCREEN_WIDTH / 3f)),
                Constants.SCREEN_HEIGHT / 13,
                Constants.SCREEN_WIDTH / 3, Constants.SCREEN_HEIGHT / 3, R.drawable.kaatunutpuu);
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

            Collectible object;
            if ((object = collectiblesManager.playerCollide(player.getRectangle())) != null) {


            }

        }
    }

    @Override
    public void draw(Canvas canvas) {

        background.draw(canvas);

        collectiblesManager.draw(canvas);
        doorObject.draw(canvas);
        player.draw(canvas);
        obstacleManager.draw(canvas);


        foreground.draw(canvas);

        userInterface.draw(canvas);

        if (DoorObject.drawPopup) {
            Paint paint = new Paint();
            paint.setColor(Color.GREEN);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTextSize((Constants.SCREEN_HEIGHT / 15));
            DoorObject.drawPopupMessage(canvas, paint, "Kerää sahanterä ensin!!!");
        }

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
                if (DoorObject.touchCollide(touchPoint)){
                    System.out.println("");
                    System.out.println("Puun runko");
                    System.out.println("");
                }
                player.moveTo(playerPosition);


                break;
        }
    }
}
