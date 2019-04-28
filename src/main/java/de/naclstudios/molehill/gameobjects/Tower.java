package de.naclstudios.molehill.gameobjects;

import de.edgelord.saltyengine.collision.collider.ShapeCollider;
import de.edgelord.saltyengine.components.CooldownComponent;
import de.edgelord.saltyengine.core.event.CollisionEvent;
import de.edgelord.saltyengine.core.graphics.SaltyGraphics;
import de.edgelord.saltyengine.gameobject.EmptyGameObject;
import de.edgelord.saltyengine.gameobject.GameObject;
import de.edgelord.saltyengine.scene.SceneManager;
import de.edgelord.saltyengine.transform.Vector2f;
import de.edgelord.saltyengine.utils.ColorUtil;
import de.naclstudios.molehill.main.Main;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.List;

public class Tower extends EmptyGameObject {

    public static final String TAG = "de.naclstudios.mole-hill.tower";

    private Enemy targetedEnemy = null;
    private float range;
    private float damage;
    private float speed;
    private int rate;

    private boolean shouldShoot = false;

    private CooldownComponent attackCooldown;
    private ShapeCollider collider = new ShapeCollider(null);

    private boolean allowShoot = false;
    
    public Tower(int cost, float xPos, float yPos, float width, float height, float range, float damage, float speed, int rate) {
        super(xPos, yPos, width, height, TAG);


        this.range = range;
        this.damage = damage;
        this.speed = speed;
        this.rate = rate;
        updateCollider();
        updateCooldown();

        addComponent(attackCooldown);
        setCollider(collider);

        Main.decreaseHealth(cost);
        getCollisionDetectionIgnore().add(TAG);
    }

    /*
    @Override
    public void onFixedTick() {
        if (targetedEnemy != null) {
            getTransform().rotateToPoint(targetedEnemy.getTransform().getCentre());
        }
    }
    */

    @Override
    public void onCollisionDetectionFinish(List<CollisionEvent> collisions) {

        shouldShoot = false;

        float currentMaxProgress = 0f;
        Enemy currentFarthestEnemy = new Enemy(0, 0, 0, 0, 0, 0);

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
    }

    public void drawRange(SaltyGraphics saltyGraphics) {
        saltyGraphics.setColor(new Color(255, 0, 0, 150));
        saltyGraphics.drawShape(collider.getShape());
    }

    public void updateCollider() {
        Vector2f centre = getTransform().getCentre();
        float diameter = range / 2f;

        collider.setShape(new Ellipse2D.Float(centre.getX() - diameter, centre.getY() - diameter, range, range));
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
}
