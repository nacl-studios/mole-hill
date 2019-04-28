package de.naclstudios.molehill.gameobjects;

import de.edgelord.saltyengine.components.animation.LinearTransformAnimations;
import de.edgelord.saltyengine.core.graphics.SaltyGraphics;
import de.edgelord.saltyengine.effect.Frame;
import de.edgelord.saltyengine.effect.Spritesheet;
import de.edgelord.saltyengine.effect.SpritesheetAnimation;
import de.edgelord.saltyengine.effect.image.SaltyImage;
import de.edgelord.saltyengine.transform.Coordinates;
import de.edgelord.saltyengine.transform.Vector2f;

import java.util.LinkedList;

public class BombMole extends Tower {

    private int ticks = 0;
    private int ticksPerFrame = 10;
    private boolean shootAnimEnded = true;

    private boolean shoot = false;
    private int shootDelayTicks = 0;
    private int shootDelay = 30;

    private LinearTransformAnimations idleAnimationHeight = new LinearTransformAnimations(this, "idle-anim-height", LinearTransformAnimations.Control.HEIGHT);
    private float originalMaxY;

    private static Coordinates[] animFrames = new Coordinates[] {
            new Coordinates(1, 1),
            new Coordinates(2, 1),
            new Coordinates(3, 1),
            new Coordinates(1, 2),
            new Coordinates(2, 2)
    };

    private static SaltyImage freeze = new SaltyImage("images/bomb_mole_freeze.png");
    private static Spritesheet spritesheet = new Spritesheet(new SaltyImage("images/bomb_mole_spritesheet.png"), 610, 678);
    private static LinkedList<Frame> frames = new LinkedList<>(spritesheet.getManualFrames(animFrames));

    private SpritesheetAnimation animation;

    public BombMole(float xPos, float yPos) {
        super(50, xPos, yPos, 96, 120, 750, 5f, 5f, 350);

        animation = new SpritesheetAnimation(this);
        animation.setFrames(frames);

        idleAnimationHeight.addKeyframe(200, 13);
        idleAnimationHeight.addKeyframe(400, 0);
        idleAnimationHeight.addKeyframe(500, 0);
        idleAnimationHeight.setLoop(true);
        idleAnimationHeight.enable();

        addComponent(idleAnimationHeight);
    }

    @Override
    public void draw(SaltyGraphics saltyGraphics) {
        super.draw(saltyGraphics);
        if (isAllowShoot() && !shootAnimEnded) {
            animation.drawCurrentFrame(saltyGraphics);
        } else {
            saltyGraphics.drawImage(freeze, this);
        }
    }

    @Override
    public void onFixedTick() {
        if (!shootAnimEnded) {
            if (ticks >= ticksPerFrame) {
                ticks = 0;
                animation.nextFrame();
                if (animation.getCurrentFrame() == 4) {
                    shootAnimEnded = true;
                    idleAnimationHeight.start();
                }
            } else {
                ticks++;
            }
        }

        if (shoot) {
            if (shootDelayTicks >= shootDelay) {
                shootDelayTicks = 0;
                shoot = false;
                super.shoot();
            } else {
                shootDelayTicks++;
            }
        }

        setY(originalMaxY - getHeight());
    }

    @Override
    protected void shoot() {
        idleAnimationHeight.stop();
        setHeight(120);
        shoot = true;
        shootAnimEnded = false;
        animation.resetFrameNumber();
    }

    @Override
    public void setAllowShoot(boolean allowShoot) {
        if (allowShoot) {
            originalMaxY = getTransform().getMaxY();
        }
        super.setAllowShoot(allowShoot);
    }
}
