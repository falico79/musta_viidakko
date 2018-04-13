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
import java.util.ArrayList;


/**
 * Created by lasse on 16/03/2018.
 */

public class GamePlayScene implements Scene {
    private Rect r = new Rect();

    private DoorObject doorObject;
    private UserInterface userInterface;
    private Player player;
    private Point playerPosition;
    private Background background;
    private Background foreground;
    private ObstacleManager obstacleManager;
    private CollectibleManager collectiblesManager;



    public static boolean gameOver = false;
    private long damageMillis = 0;
    private boolean killMonkey = false;

    private int[] mapList;
    private int currentMapIndex = -1;

    public GamePlayScene() {
        player = new Player(
                new Rect((int)(Constants.SCREEN_WIDTH * 0.5f),
                        (int)(Constants.SCREEN_HEIGHT * 0.75f),
                        (int)(Constants.SCREEN_WIDTH * 0.5f + Constants.SCREEN_WIDTH * 0.1f),
                        (int)(Constants.SCREEN_HEIGHT * 0.75f + Constants.SCREEN_HEIGHT * 0.2f)),
                (Constants.SCREEN_WIDTH * 0.4f) );
        playerPosition = new Point((int)(Constants.SCREEN_WIDTH * 0.5f), (int)(Constants.SCREEN_HEIGHT * 0.75f));
        player.setPos(playerPosition);
        player.updatePosition(damageMillis, killMonkey);

        mapList = new int[]{ R.xml.map001 };

        userInterface = new UserInterface();

        collectiblesManager = new CollectibleManager();




        loadMap(nextMap());

        obstacleManager = new ObstacleManager(1, Color.argb(0,0,0,0));

        background = new Background(R.drawable.background);
        foreground = new Background(R.drawable.foreground);

        doorObject = new DoorObject(Constants.SCREEN_WIDTH - ((int)(Constants.SCREEN_WIDTH / 3f)),
                Constants.SCREEN_HEIGHT / 13,
                Constants.SCREEN_WIDTH / 3, Constants.SCREEN_HEIGHT / 3, R.drawable.kaatunutpuu);
    }

    private int nextMap()
    {
        return currentMapIndex < mapList.length - 1 ? mapList[++currentMapIndex] : mapList[0];
    }


    public void loadMap(int fileId) {
        XmlResourceParser parser = Constants.CURRENT_CONTEXT.getResources().getXml(fileId);

        try {
            parser.next();

            while (parser.next() != XmlPullParser.END_DOCUMENT) {
                switch (parser.getName()) {
                    case "item":
                        Collectible temp = addItem(parser);
                        if(temp!=null) {
                            collectiblesManager.addCollectibles(temp);
                            parser.next();
                        }
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

            return Collectible.addBanaani(parser);
        } else if(parser.getAttributeValue(0).equals("storyitem")) {

            return StoryItem.addStoryItem(parser);
        }
        return null;
    }



    @Override
    public void update() {
        if(!gameOver) {
            player.updatePosition(damageMillis, killMonkey);
            obstacleManager.update();

            Collectible object;
            collectiblesManager.updateStoryItems();
            Rect test = new Rect(player.getRectangle().centerX(), player.getRectangle().centerY(), player.getRectangle().centerX() + 1, player.getRectangle().centerY() + 1);
            if ((object = collectiblesManager.playerCollide(test)) != null) {
                if (object instanceof StoryItem)
                    ((StoryItem)object).advanceStory();

            }
            userInterface.update();
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
                }
                if (userInterface.musicButtonClick(touchPoint)) {
                    UserInterface.stopMusic();
                    // stop music
                }
                if (DoorObject.touchCollide(touchPoint)){

                    UserInterface.DoDamage(10);
                    damageMillis = System.currentTimeMillis() + 250;
                    if (UserInterface.health == 0)
                        killMonkey = true;
                    // VÄLIAIKAINEN TESTI DAMAGE

                }
                else {
                    damageMillis = 0;
                }
                player.moveTo(playerPosition);


                break;
        }
    }
}
