package com.leaves.nine.mustamets;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by mikae on 21.3.2018.
 */

public class CollectibleManager {

    private ArrayList<Collectible> collectibles;
    private Random random;


    private int collectedItems = 0;

    public CollectibleManager() {
        random = new Random();
        collectibles = new ArrayList<>();

    }

    public void addCollectibles(Collectible item) {
        collectibles.add(item);
    }

    public int getCollectedItems()
    {
        return collectedItems;
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
        for(Collectible item : collectibles) {
            item.draw(canvas);
        }
    }
}
