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
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by mikae on 21.3.2018.
 */

public class GameObjectManager {

    private StoryBoard storyBoard;

    private Background background;
    private Background foreground;

    private Player player;
    private Point playerPosition;

    private ArrayList<Collectible> collectibles;
    private ArrayList<StoryItem> storyItems;
    private ArrayList<NPC> npcs;
    // private ArrayList<Bush> bushes;

    private DoorObject goal;

    private Random random;

    private long damageMillis = 0;
    private boolean killMonkey = false;

    private UserInterface userInterface;

    private ObstacleManager obstacleManager;




    public GameObjectManager() {
        random = new Random();
        collectibles = new ArrayList<>();

        player = new Player(new Rect(0, 0, (int)(Constants.SCREEN_WIDTH * 0.1f), (int)(Constants.SCREEN_HEIGHT * 0.2f)), (Constants.SCREEN_WIDTH * 0.4f) );

        userInterface = new UserInterface();

        obstacleManager = new ObstacleManager(1, Color.argb(0,0,0,0));
        ArrayList<String> asd = new ArrayList<String>();
        asd.add("A");
        asd.add("B");
        asd.add("C");
        asd.add("D");
        storyBoard = new StoryBoard("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged.", asd, 2);

    }

    public void addCollectibles(Collectible item) {
        collectibles.add(item);
    }

    public void addStoryItems(StoryItem item) {storyItems.add(item);}

    public void addNPCs(NPC item) {npcs.add(item);}

    public void setPlayer() {

    }

    private void addItem(XmlResourceParser parser) throws IOException, XmlPullParserException {
        if( parser.getAttributeCount() != 1) {
            throw new XmlPullParserException("virheellinen xml");
        }
        if( parser.getAttributeValue(0).equals("banaani")) {

            addCollectibles(Collectible.addBanaani(parser));
        } else if(parser.getAttributeValue(0).equals("storyitem")) {

            addStoryItems(StoryItem.addStoryItem(parser));
        }
    }

    public void loadMap(int fileId) {
        XmlResourceParser parser = Constants.CURRENT_CONTEXT.getResources().getXml(fileId);

        try {
            parser.next();

            while (parser.next() != XmlPullParser.END_DOCUMENT) {
                switch (parser.getName()) {
                    case "item":
                        addItem(parser);
                        parser.next();
                        break;
                    case "background":
                        background = new Background(parser.getAttributeResourceValue(0, -1));
                        parser.next();
                        break;
                    case "foreground":
                        foreground = new Background(parser.getAttributeResourceValue(0, -1));
                        parser.next();
                        break;
                    default:
                        continue;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        playerPosition = new Point((int)(Constants.SCREEN_WIDTH * 0.5f), (int)(Constants.SCREEN_HEIGHT * 0.75f));
        player.setPos(playerPosition);
        player.updatePosition(damageMillis);
        
        goal = new DoorObject(Constants.SCREEN_WIDTH - ((int)(Constants.SCREEN_WIDTH / 3f)),
                Constants.SCREEN_HEIGHT / 13,
                Constants.SCREEN_WIDTH / 3, Constants.SCREEN_HEIGHT / 3, R.drawable.kaatunutpuu);
    }

    public void update() {
        player.updatePosition(damageMillis);

        Collectible object;
        updateStoryItems();
        Rect test = new Rect(player.getRectangle().centerX(), player.getRectangle().centerY(), player.getRectangle().centerX() + 1, player.getRectangle().centerY() + 1);
        if ((object = playerCollide(test)) != null) {
            if (object instanceof StoryItem)
                ((StoryItem)object).advanceStory();

        }
        userInterface.update();
        obstacleManager.update();
    }


    public void updateStoryItems() {
        for (Collectible item : collectibles) {
            if ((item instanceof StoryItem)){
                item.update();
            }
        }
    }

    public Collectible playerCollide(Rect rect) {
        for (Collectible item : collectibles) {
            if (item.playerCollide(rect)) {
                collectibles.remove(item);
                if (!(item instanceof StoryItem)){
                    UserInterface.addBanana();
                }

                return item;
            }
        }
        return null;
    }

    public void draw(Canvas canvas) {
        background.draw(canvas);

        for(Collectible item : collectibles) {
            item.draw(canvas);
        }

        goal.draw(canvas);
        player.draw(canvas);
        foreground.draw(canvas);
        obstacleManager.draw(canvas);
        userInterface.draw(canvas);
        storyBoard.draw(canvas);
    }

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
                    UserInterface.eatBanana();
                }
                if (userInterface.musicButtonClick(touchPoint)) {
                    UserInterface.stopMusic();
                    // stop music
                }
                if (DoorObject.playerCollide(touchPoint)){

                    player.doDamage(10);
                    damageMillis = System.currentTimeMillis() + 250;
                    if (UserInterface.health == 0)
                        player.killCharacter();
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

