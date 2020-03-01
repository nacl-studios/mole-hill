package de.naclstudios.molehill.gameobjects;

import de.edgelord.saltyengine.core.graphics.SaltyGraphics;
import de.edgelord.saltyengine.effect.Frame;
import de.edgelord.saltyengine.effect.Spritesheet;
import de.edgelord.saltyengine.effect.SpritesheetAnimation;
import de.edgelord.saltyengine.effect.image.SaltyImage;
import de.edgelord.saltyengine.transform.Coordinates;
import de.edgelord.saltyengine.utils.SaltySystem;

import java.util.List;

public class StandardEnemy extends Enemy {

    private static SaltyImage spritesheet = SaltySystem.createPreferredImage("images/enemy_standard_spritesheet.png");
    private static List<Frame> forwardFrames = new Spritesheet(spritesheet, 320, 335).getFrames(new Coordinates(1, 1), new Coordinates(2, 1));

    private SpritesheetAnimation forwardAnimation;

    private int ticks = 0;
    private int nextFrameTicks = 25;

    public StandardEnemy(float xPos, float yPos) {
        super(xPos, yPos, 50, 55, 1.5f, 5, 25);

        forwardAnimation = new SpritesheetAnimation();
        forwardAnimation.setFrames(forwardFrames);
    }

    @Override
    public void onFixedTick() {
        super.onFixedTick();

        if (ticks >= nextFrameTicks) {
            ticks = 0;
            forwardAnimation.nextFrame();
        } else {
            ticks++;
        }
    }

    @Override
    public void draw(SaltyGraphics saltyGraphics) {
        forwardAnimation.drawCurrentFrame(this, saltyGraphics);
    }
}
