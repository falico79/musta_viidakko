package com.leaves.nine.mustamets;

import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by mikae on 21.3.2018.
 */

public class GameObjectManager {


    private Background background;
    private Background foreground;

    private Player player;
    private Point playerPosition;

    private ArrayList<Collectible> collectibles = null;
    //private ArrayList<StoryItem> storyItems = null;
    private ArrayList<NPC> npcs = null;
    // private ArrayList<Bush> bushes;

    private DoorObject goal;

    //private Random random;

    private long damageMillis = 0;

    private UserInterface userInterface;

    private ObstacleManager obstacleManager;

    private StoryBoard board = null;



    public GameObjectManager() {
        //random = new Random();
        collectibles = new ArrayList<>();
        //storyItems = new ArrayList<>();
        npcs = new ArrayList<>();

        player = new Player(new Rect(0, 0, (int)(Constants.SCREEN_WIDTH * 0.1f), (int)(Constants.SCREEN_HEIGHT * 0.2f)), (Constants.SCREEN_WIDTH * 0.4f) );

        userInterface = new UserInterface();

        obstacleManager = new ObstacleManager(1, Color.argb(0,0,0,0));



    }

    public void addCollectibles(Collectible item) {
        collectibles.add(item);
    }

    public void addStoryItems(StoryItem item) {collectibles.add(item);}

    public void addNPCs(NPC item) {npcs.add(item);}

    public void addDoor(DoorObject item) {goal = item;}

    public void setPlayer() {

    }

    private void addItem(XmlResourceParser parser) throws IOException, XmlPullParserException {
        if( parser.getAttributeCount() < 1) {
            throw new XmlPullParserException("virheellinen xml");
        }

        for(int i = 0; i < parser.getAttributeCount(); i++) {

            if (parser.getAttributeValue(i).equals("banaani")) {

                addCollectibles(Collectible.addBanaani(parser));
                parser.next();
            } else if (parser.getAttributeValue(i).equals("storyitem")) {

                addStoryItems(StoryItem.addStoryItem(parser));
            } else if (parser.getAttributeValue(i).equals("npc")) {
                addNPCs(NPC.addNPC(parser));
            } else if (parser.getAttributeValue(i).equals("door")) {
                addDoor(DoorObject.addDoorObject(parser));
            }
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
                        //parser.next();
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

        connectObjectivesToDoorObject();
        
    }

    private void connectObjectivesToDoorObject() {
        goal.addObjective(getStoryItemById(goal.getRequirements()));

        getStoryItemById(goal.getRequirements()).connectExit(goal);

        System.out.println("connected");

    }

    private StoryItem getStoryItemById(String id) {
        for (Collectible item : collectibles) {
            if ((item instanceof StoryItem)){
                if(((StoryItem)item).getId().equals(id)) {
                    return ((StoryItem)item);
                }
            }
        }
        return null;
    }

    public void update() {
        player.updatePosition(damageMillis);

        Collectible object;

        if(goal != null) {
            goal.update();
        }

        for(NPC item : npcs) {
            item.update();
        }

        if(storyBoard != null) {
            storyBoard.update();
            System.out.println("update StoryBoard");
        }

        updateStoryItems();
        Rect test = new Rect(player.getRectangle().centerX(), player.getRectangle().centerY(), player.getRectangle().centerX() + 1, player.getRectangle().centerY() + 1);
        if ((object = playerCollide(test)) != null) {
            if (object instanceof StoryItem)
                if(((StoryItem) object).getId().equals("tera"))
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

        if(storyBoard != null) {
            storyBoard.draw(canvas);
        }
        for(NPC item : npcs) {
            item.draw(canvas);
        }
        if(goal != null) {
            goal.draw(canvas);
        }

        player.draw(canvas);
        foreground.draw(canvas);
        obstacleManager.draw(canvas);
        userInterface.draw(canvas);
        //storyBoard.draw(canvas);
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

                if(storyBoard!=null) {
                    if(storyBoard.receiveTouch(event)) {
                        storyBoard = null;
                    }

                }

                Rect touchPoint = new Rect(x, y, x+1, y+1);
                if (userInterface.playerCollide(touchPoint)) {
                    UserInterface.eatBanana();
                }
                if (userInterface.musicButtonClick(touchPoint)) {
                    UserInterface.stopMusic();
                    // stop music
                }

                    if (userInterface.menuButtonClick(touchPoint)) {
                        Intent intent = new Intent(Constants.CURRENT_CONTEXT, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                        UserInterface.ring.stop();
                        Constants.CURRENT_CONTEXT.startActivity(intent);
                        // Go to Main Menu
                }
                if (goal.playerCollide(touchPoint)){
                     storyBoard = goal.openStoryBoard();

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

