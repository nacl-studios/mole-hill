package de.naclstudios.molehill.gameobjects;

import de.edgelord.saltyengine.core.event.CollisionEvent;
import de.edgelord.saltyengine.core.graphics.SaltyGraphics;
import de.edgelord.saltyengine.gameobject.GameObject;
import de.edgelord.saltyengine.transform.Vector2f;
import de.naclstudios.molehill.main.Main;

public class Enemy extends GameObject {

    private long progress = 0;
    private int currentTargetIndex = 0;
    private Vector2f currentTarget;

    private float speed;
    private float health;

    public static final String TAG = "de.naclstudios.mole-hill.enemy";

    public Enemy(float xPos, float yPos, float width, float height, float speed, float health) {
        super(xPos, yPos, width, height, TAG);

        this.speed = speed;
        this.health = health;

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
    public void onFixedTick() {

        if (getTransform().contains(currentTarget)) {
            if (currentTargetIndex == Main.track.size() - 1) {
                removeFromCurrentScene();
                return;
            }
            currentTargetIndex++;
            updateTarget();
        }

        getTransform().rotateToPoint(currentTarget);
        moveToFacedDirection(speed);
        progress++;
    }

    @Override
    public void draw(SaltyGraphics saltyGraphics) {
        saltyGraphics.drawRect(this);
    }

    public void decreaseHealth(float damage) {
        health -= damage;

        if (health <= 0) {
            removeFromCurrentScene();
        }
    }

    public long getProgress() {
        return progress;
    }

    public void setProgress(long progress) {
        this.progress = progress;
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
}
