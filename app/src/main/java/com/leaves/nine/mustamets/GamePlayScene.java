package com.leaves.nine.mustamets;

import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Random;

/**
 * Created by lasse on 16/03/2018.
 */

public class GamePlayScene implements Scene {
    //private SceneManager manager;
    private Rect r = new Rect();

    private DoorObject doorObject;
    private StoryItem storyItem;
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
    private Animation bananaAnimation;

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

        storyItem = new StoryItem();

        Bitmap bananaImage = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(),R.drawable.banaani);
        bananaAnimation = new Animation(new Bitmap[]{bananaImage}, 2);

        loadMap(R.xml.map001);

        Bitmap tera = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.sahantera1);
        Animation stillAnimation = new Animation(new Bitmap[]{tera}, 2);
        Bitmap tera2 = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.sahantera2);
        Bitmap tera3 = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.sahantera3);
        Bitmap tera4 = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.sahantera4);
        Bitmap tera5 = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.sahantera5);
        Animation blink = new Animation(new Bitmap[]{tera2, tera3,tera4,tera5,tera4,tera3,tera2},0.5f );
        AnimationManager aniManager = new AnimationManager(new Animation[]{stillAnimation, blink});

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

    public void loadMap(int fileId) {
        XmlResourceParser parser = Constants.CURRENT_CONTEXT.getResources().getXml(fileId);

        try {
            parser.next();

            while (parser.next() != XmlPullParser.END_TAG) {
                switch (parser.getName()) {
                    case "item":
                        collectiblesManager.addCollectibles(addItem(parser));
                        break;
                    default:
                        continue;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private Collectible addItem(XmlResourceParser parser) throws IOException, XmlPullParserException {
        if( parser.getAttributeCount() != 1) {
            throw new XmlPullParserException("virheellinen xml");
        }
        if( parser.getAttributeValue(0).equals("banaani")) {
            return addBanaani(parser);
        }
        return null;
    }

    private Collectible addBanaani(XmlResourceParser parser) throws IOException, XmlPullParserException {
        float x = 0.5f, y = 0.5f;
        while(parser.next() != XmlPullParser.END_TAG) {
            System.out.println("test");
            for(int i = 0; i < parser.getAttributeCount(); i++) {
                if(parser.getAttributeName(i).equals("x")) {
                    x = Float.parseFloat(parser.getAttributeValue(i)) /100;
                } else if(parser.getAttributeName(i).equals("y")) {
                    y = Float.parseFloat(parser.getAttributeValue(i)) /100;
                }
            }
        }

        return new Collectible(new Rect(new Rect((int)(Constants.SCREEN_WIDTH * x),
                (int)(Constants.SCREEN_HEIGHT * y),
                (int)(Constants.SCREEN_WIDTH * x + Constants.SCREEN_WIDTH * 0.05f),
                (int)(Constants.SCREEN_HEIGHT * y + Constants.SCREEN_HEIGHT *0.1f))), new AnimationManager(new Animation[]{bananaAnimation}));
    }

    @Override
    public void update() {
        if(!gameOver) {
            player.updatePosition();
            obstacleManager.update();

            Collectible object;
            if ((object = collectiblesManager.playerCollide(player.getRectangle())) != null) {
                if (object instanceof StoryItem)
                    storyItem.advanceStory();

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
