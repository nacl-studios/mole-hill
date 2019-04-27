package de.naclstudios.molehill.gameobjects;

import de.edgelord.saltyengine.core.event.CollisionEvent;
import de.edgelord.saltyengine.core.graphics.SaltyGraphics;
import de.edgelord.saltyengine.gameobject.EmptyGameObject;
import de.edgelord.saltyengine.gameobject.GameObject;
import de.edgelord.saltyengine.transform.Vector2f;

public class Bullet extends EmptyGameObject {

    public static final String TAG = "de.naclstudios.mole-hill.Bullet";
    private GameObject target;
    private float speed;
    private float damage;

    public Bullet(Vector2f position, float width, float height, GameObject target, float speed, float damage) {
        super(position.getX(), position.getY(), width, height, TAG);

        this.target = target;
        this.speed = speed;
        this.damage = damage;
    }

    @Override
    public void onFixedTick() {
        getTransform().rotateToPoint(target.getTransform().getCentre());
        moveToFacedDirection(speed);
    }

    @Override
    public void draw(SaltyGraphics saltyGraphics) {
        saltyGraphics.drawOval(this);
    }

    @Override
    public void onCollision(CollisionEvent event) {
        if (event.getOtherGameObject().getTag().equals(Enemy.TAG)) {
            Enemy enemy = (Enemy) event.getOtherGameObject();
            enemy.decreaseHealth(damage);
            removeFromCurrentScene();
        }
    }
}
