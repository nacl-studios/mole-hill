package de.naclstudios.molehill.gameobjects;

import de.edgelord.saltyengine.core.event.CollisionEvent;
import de.edgelord.saltyengine.core.graphics.SaltyGraphics;
import de.edgelord.saltyengine.gameobject.GameObject;
import de.edgelord.saltyengine.transform.Dimensions;
import de.edgelord.saltyengine.transform.Transform;
import de.edgelord.saltyengine.transform.Vector2f;
import de.naclstudios.molehill.main.Main;

import java.awt.*;

public class Enemy extends GameObject {

    private float progress = 0f;
    private int currentTargetIndex = 0;
    private Vector2f currentTarget;

    private float speed;
    private float health;
    private float maxHealth;
    private int lifeSteal;

    private boolean alive = true;

    public static final String TAG = "de.naclstudios.mole-hill.enemy";

    public Enemy(float xPos, float yPos, float width, float height, float speed, float health, int lifeSteal) {
        super(xPos, yPos, width, height, TAG);

        this.speed = speed;
        this.health = health;
        this.maxHealth = health;
        this.lifeSteal = lifeSteal;

        updateTarget();
    }

    @Override
    public void initialize() {

        getPhysics().addTagToIgnore(Tower.TAG);
    }

    @Override
    public void onCollision(CollisionEvent collisionEvent) {
    }

    @Override
    public void removeFromCurrentScene() {
        super.removeFromCurrentScene();
        alive = false;
    }

    @Override
    public void onFixedTick() {

        if (getTransform().contains(new Transform(currentTarget, new Dimensions(1, 1)))) {
            if (currentTargetIndex == Main.track.size() - 1) {
                removeFromCurrentScene();
                Main.decreaseHealth(getLifeSteal());
                return;
            }
            currentTargetIndex++;
            updateTarget();
        }

        getTransform().rotateToPoint(currentTarget);
        moveToFacedDirection(speed);

        progress += speed;
    }

    @Override
    public void draw(SaltyGraphics saltyGraphics) {
        saltyGraphics.setColor(Color.BLACK);
        saltyGraphics.drawRect(this);
    }

    public void decreaseHealth(float damage) {
        health -= damage;

        if (health <= 0) {
            removeFromCurrentScene();
            Main.scoreEnemy(this);
        }
    }

    public float getProgress() {
        return progress;
    }

    private void updateTarget() {
        currentTarget = Main.track.get(currentTargetIndex);
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public int getCurrentTargetIndex() {
        return currentTargetIndex;
    }

    public void setCurrentTargetIndex(int currentTargetIndex) {
        this.currentTargetIndex = currentTargetIndex;
    }

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public float getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(float maxHealth) {
        this.maxHealth = maxHealth;
    }

    public int getLifeSteal() {
        return lifeSteal;
    }

    public void setLifeSteal(int lifeSteal) {
        this.lifeSteal = lifeSteal;
    }
}
