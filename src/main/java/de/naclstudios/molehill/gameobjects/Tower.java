package de.naclstudios.molehill.gameobjects;

import de.edgelord.saltyengine.collision.collider.CircleCollider;
import de.edgelord.saltyengine.components.CooldownComponent;
import de.edgelord.saltyengine.core.event.CollisionEvent;
import de.edgelord.saltyengine.core.graphics.SaltyGraphics;
import de.edgelord.saltyengine.gameobject.EmptyGameObject;
import de.edgelord.saltyengine.gameobject.GameObject;
import de.edgelord.saltyengine.input.Input;
import de.edgelord.saltyengine.input.MouseInputHandler;
import de.edgelord.saltyengine.scene.SceneManager;
import de.edgelord.saltyengine.transform.Transform;
import de.edgelord.saltyengine.utils.ColorUtil;

import java.awt.*;
import java.awt.event.MouseEvent;

public abstract class Tower extends EmptyGameObject {

    public static final String TAG = "de.naclstudios.mole-hill.tower";

    private Enemy targetedEnemy = null;
    private float range;
    private float damage;
    private float speed;
    private int rate;

    private boolean shouldShoot = false;

    private CooldownComponent attackCooldown;
    private CircleCollider collider;

    private boolean allowShoot = false;

    private int prize;

    private boolean selected = false;

    private MouseInputHandler inputHandler;

    private static Color RANGE_COLOR = ColorUtil.changeAlpha(ColorUtil.PLAIN_RED, .5f);

    private boolean upgraded = false;
    
    public Tower(float xPos, float yPos, float width, float height, float range, float damage, float speed, int rate, int prize) {
        super(xPos, yPos, width, height, TAG);


        this.range = range;
        this.damage = damage;
        this.speed = speed;
        this.rate = rate;
        this.prize = prize;

        updateCooldown();

        addComponent(attackCooldown);
        collider = new CircleCollider(getTransform());
        updateCollider();

        setCollider(collider);

        getPhysics().addTagToIgnore(TAG);

        inputHandler = new MouseInputHandler() {
            @Override
            public void mouseMoved(MouseEvent e) {

            }

            @Override
            public void mouseDragged(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (!mouseTouches()) {
                    selected = false;
                }
            }

            @Override
            public void mouseExitedScreen(MouseEvent e) {

            }

            @Override
            public void mouseEnteredScreen(MouseEvent e) {

            }

            @Override
            public void mouseWheelMoved(MouseEvent e) {

            }
        };

        Input.addMouseInputHandler(inputHandler);
    }

    public abstract void upgrade();

    @Override
    public void onFixedTick() {
        if (targetedEnemy != null) {
            getTransform().rotateToPoint(targetedEnemy.getTransform().getCentre());
        }
    }

    @Override
    public void onCollisionDetectionFinish(java.util.List<CollisionEvent> collisions) {
        shouldShoot = false;

        float currentMaxProgress = 0f;
        Enemy currentFarthestEnemy = new Enemy(0, 0, 0, 0, 0, 0, 0);

        for (int i = 0; i < collisions.size(); i++) {
            GameObject gameObject = collisions.get(i).getOtherGameObject();

            if (gameObject.getTag().equals(Enemy.TAG)) {
                shouldShoot = true;
                Enemy enemy = (Enemy) gameObject;

                if (enemy.getProgress() > currentMaxProgress) {
                    currentMaxProgress = enemy.getProgress();
                    currentFarthestEnemy = enemy;
                }
            }
        }

        targetedEnemy = currentFarthestEnemy;
    }

    @Override
    public void draw(SaltyGraphics saltyGraphics) {

        if (!allowShoot) {
            drawRange(saltyGraphics);
        }

        if (selected) {
            drawRange(saltyGraphics);
        }
    }

    public void drawRange(SaltyGraphics saltyGraphics) {
        saltyGraphics.setColor(RANGE_COLOR);
        saltyGraphics.drawOval(collider.getTransform());
    }

    public void updateCollider() {
        collider.setTransform(new Transform(getX() - range / 2f, getY() - range / 2f, getWidth() + range, getHeight() + range));
    }

    public void updateCooldown() {
        attackCooldown = new CooldownComponent(this, "attack-cooldown", rate, () -> shouldShoot && allowShoot) {
            @Override
            public void run() {
                shoot();
            }
        };
    }

    protected void shoot() {
        SceneManager.getCurrentScene().addGameObject(new Bullet(getTransform().getCentre(), 35, 35, targetedEnemy, speed, damage));
    }

    @Override
    public void removeFromCurrentScene() {
        super.removeFromCurrentScene();
        Input.getMouseHandlers().remove(inputHandler);
    }

    public float getRange() {
        return range;
    }

    public void setRange(float range) {
        this.range = range;
    }

    public float getDamage() {
        return damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public boolean isAllowShoot() {
        return allowShoot;
    }

    public void setAllowShoot(boolean allowShoot) {
        this.allowShoot = allowShoot;
    }

    public int getPrize() {
        return prize;
    }

    public void setPrize(int prize) {
        this.prize = prize;
    }


    public boolean isUpgraded() {
        return upgraded;
    }

    public void setUpgraded(boolean upgraded) {
        this.upgraded = upgraded;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Enemy getTargetedEnemy() {
        return targetedEnemy;
    }

    public void setTargetedEnemy(Enemy targetedEnemy) {
        this.targetedEnemy = targetedEnemy;
    }
}
