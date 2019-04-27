package de.naclstudios.molehill.gameobjects;

import de.edgelord.saltyengine.collision.collider.ShapeCollider;
import de.edgelord.saltyengine.core.event.CollisionEvent;
import de.edgelord.saltyengine.core.graphics.SaltyGraphics;
import de.edgelord.saltyengine.gameobject.EmptyGameObject;
import de.edgelord.saltyengine.gameobject.GameObject;
import de.edgelord.saltyengine.transform.Vector2f;
import de.edgelord.saltyengine.utils.ColorUtil;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.List;

public class Tower extends EmptyGameObject {

    public static final String TAG = "de.naclstudios.mole-hill.tower";

    private Enemy targetedEnemy = null;
    private float range;
    
    public Tower(float xPos, float yPos, float width, float height, float range) {
        super(xPos, yPos, width, height, TAG);

        this.range = range;
        updateCollider();
    }

    @Override
    public void onCollisionDetectionFinish(List<CollisionEvent> collisions) {

        float currentMaxProgress = 0f;
        Enemy currentFarthestEnemy = new Enemy(0, 0, 0, 0);

        for (int i = 0; i < collisions.size(); i++) {
            GameObject gameObject = collisions.get(i).getOtherGameObject();

            if (gameObject.getTag().equals(Enemy.TAG)) {
                Enemy enemy = (Enemy) gameObject;

                currentFarthestEnemy = enemy.getProgress() > currentMaxProgress ? enemy : currentFarthestEnemy;
            }
        }

        targetedEnemy = currentFarthestEnemy;
    }

    @Override
    public void draw(SaltyGraphics saltyGraphics) {
        saltyGraphics.setColor(ColorUtil.RED);
        saltyGraphics.drawOval(targetedEnemy.getTransform().getCentre().getX() - 3, targetedEnemy.getTransform().getCentre().getY() - 3, 6, 6);

        saltyGraphics.setColor(ColorUtil.PURPLE_COLOR);
        saltyGraphics.drawOval(this);

        ShapeCollider shapeCollider = (ShapeCollider) getCollider();

        saltyGraphics.setColor(new Color(255, 0, 0, 150));
        saltyGraphics.drawShape(shapeCollider.getShape());
    }

    private void updateCollider() {
        Vector2f centre = getTransform().getCentre();
        float diameter = range / 2f;

        setCollider(new ShapeCollider(new Ellipse2D.Float(centre.getX() - diameter, centre.getY() - diameter, range, range)));
    }

    public float getRange() {
        return range;
    }

    public void setRange(float range) {
        this.range = range;
    }
}
