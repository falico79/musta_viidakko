package com.leaves.nine.mustamets;

import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Created by lasse on 26/03/2018.
 */

public class StoryItem extends Collectible implements StoryObject {
    public StoryItem(Rect target, int imageId) {
        super(target, imageId);
    }

    @Override
    public void update() {
        super.update();


    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);


    }

    @Override
    public void openStoryBoard() {

    }

    @Override
    public void advanceStory() {

    }
}
