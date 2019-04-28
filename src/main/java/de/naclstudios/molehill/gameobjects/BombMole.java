package de.naclstudios.molehill.gameobjects;

import de.edgelord.saltyengine.components.animation.LinearTransformAnimations;
import de.edgelord.saltyengine.core.graphics.SaltyGraphics;
import de.edgelord.saltyengine.effect.Frame;
import de.edgelord.saltyengine.effect.Spritesheet;
import de.edgelord.saltyengine.effect.SpritesheetAnimation;
import de.edgelord.saltyengine.effect.image.SaltyImage;
import de.edgelord.saltyengine.scene.SceneManager;
import de.edgelord.saltyengine.transform.Coordinates;
import de.edgelord.saltyengine.transform.Vector2f;
import de.naclstudios.molehill.main.Main;

import java.util.LinkedList;

public class BombMole extends Tower {

    private int ticks = 0;
    private int idleTicks = 0;
    private int ticksPerFrame = 10;
    private int idleTicksPerFrame = 10;
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

    private static Coordinates[] tankAnimFrames = new Coordinates[] {
            new Coordinates(1, 1),
            new Coordinates(2, 1),
            new Coordinates(3, 1),
            new Coordinates(4, 1),
            new Coordinates(1, 2),
            new Coordinates(2, 2),
            new Coordinates(3, 2)
    };

    private static Coordinates[] idleTankAnimFrames = new Coordinates[] {
            new Coordinates(1, 1),
            new Coordinates(2, 1),
            new Coordinates(3, 1),
            new Coordinates(4, 1),
            new Coordinates(1, 2),
            new Coordinates(2, 2),
            new Coordinates(3, 2)
    };

    private static SaltyImage freeze = new SaltyImage("images/bomb_mole_freeze.png");
    private static Spritesheet spritesheet = new Spritesheet(new SaltyImage("images/bomb_mole_spritesheet.png"), 610, 678);
    private static Spritesheet tankSpritesheet = new Spritesheet(new SaltyImage("images/tank_mole_shoot_spritesheet.png"), 419, 726);
    private static Spritesheet idleTankSpritesheet = new Spritesheet(new SaltyImage("images/tank_mole_idle_spritesheet.png"), 419, 726);
    private static LinkedList<Frame> frames = new LinkedList<>(spritesheet.getManualFrames(animFrames));
    private static LinkedList<Frame> tankFrames = new LinkedList<>(tankSpritesheet.getManualFrames(tankAnimFrames));
    private static LinkedList<Frame> idleTankFrames = new LinkedList<>(idleTankSpritesheet.getManualFrames(idleTankAnimFrames));

    private SpritesheetAnimation animation;
    private SpritesheetAnimation tankAnimation;
    private SpritesheetAnimation idleTankAnimation;

    private boolean idleTank = false;

    private boolean waitForBlink = false;
    private int blinkWaitTicks = 0;
    private int blinkWaitDuration = 20;

    public BombMole(float xPos, float yPos) {
        super(xPos, yPos, 96, 120, 750, 5f, 5f, 500, 50);

        animation = new SpritesheetAnimation(this);
        animation.setFrames(frames);

        tankAnimation = new SpritesheetAnimation(this);
        tankAnimation.setFrames(tankFrames);

        idleTankAnimation = new SpritesheetAnimation(this);
        idleTankAnimation.setFrames(idleTankFrames);

        idleAnimationHeight.addKeyframe(200, 13);
        idleAnimationHeight.addKeyframe(400, 0);
        idleAnimationHeight.addKeyframe(500, 0);
        idleAnimationHeight.setLoop(true);
        idleAnimationHeight.enable();

        addComponent(idleAnimationHeight);
    }

    @Override
    public void upgrade() {
        Main.decreaseHealth(getPrize() * 2);
        setDamage(getDamage() * 2);
        removeComponent(idleAnimationHeight);

        Vector2f centre = getTransform().getCentre();

        setHeight(250);
        setWidth(150);
        setX(centre.getX() - (getWidth() / 2f));
    }

    @Override
    public void draw(SaltyGraphics saltyGraphics) {
        super.draw(saltyGraphics);
        if (isAllowShoot() && !shootAnimEnded) {
            if (isUpgraded()) {
                tankAnimation.drawCurrentFrame(saltyGraphics);
            } else {
                animation.drawCurrentFrame(saltyGraphics);
            }
        } else if (!isUpgraded()) {
            saltyGraphics.drawImage(freeze, this);
        }

        if (idleTank) {
            idleTankAnimation.drawCurrentFrame(saltyGraphics);
        }
    }

    @Override
    public void onFixedTick() {

        if (idleTank) {
            if (idleTicks >= idleTicksPerFrame) {
                idleTankAnimation.nextFrame();
                idleTicks = 0;
            } else {
                idleTicks++;
            }
        }
        if (!shootAnimEnded) {
            if (ticks >= ticksPerFrame) {
                ticks = 0;
                animation.nextFrame();
                if (animation.getCurrentFrame() == 4) {
                    shootAnimEnded = true;
                    startIdleAnimation();
                }
            } else {
                ticks++;
            }
        }

        if (shoot) {
            if (shootDelayTicks >= shootDelay) {
                shootDelayTicks = 0;
                shoot = false;
                shootBullet();
            } else {
                shootDelayTicks++;
            }
        }

        setY(originalMaxY - getHeight());
    }

    @Override
    protected void shoot() {
        endIdleAnimation();
        if (!isUpgraded()) {
            setHeight(120);
        }
        shoot = true;
        shootAnimEnded = false;
        animation.resetFrameNumber();
    }

    private void shootBullet() {
        if (isUpgraded()) {
            SceneManager.getCurrentScene().addGameObject(new Bullet(getPosition(), 35, 35, getTargetedEnemy(), getSpeed(), getDamage()));
        } else {
            SceneManager.getCurrentScene().addGameObject(new Bullet(getTransform().getCentre(), 35, 35, getTargetedEnemy(), getSpeed(), getDamage()));
        }
    }

    @Override
    public void setAllowShoot(boolean allowShoot) {
        if (allowShoot) {
            originalMaxY = getTransform().getMaxY();
        }
        super.setAllowShoot(allowShoot);
    }

    public void startIdleAnimation() {
        if (!isUpgraded()) {
            idleAnimationHeight.start();
        } else {
            idleTank = true;
        }
    }

    public void endIdleAnimation() {
        if (!isUpgraded()) {
            idleAnimationHeight.stop();
        } else {
            idleTank = false;
        }
    }
}
