package com.leaves.nine.mustamets;

import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Created by lasse on 26/03/2018.
 */

public class StoryItem extends Collectible implements StoryObject {
    AnimationManager animationManager;


    public StoryItem(AnimationManager animationManager, Rect target) {
        super(target, animationManager);
    }

    @Override
    public void update() {
        super.update();

        animationManager.update();

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        animationManager.draw(canvas, target);


    }

    @Override
    public void openStoryBoard() {

    }

    @Override
    public void advanceStory() {

    }
}
