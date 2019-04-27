package de.naclstudios.molehill.scene;

import de.edgelord.saltyengine.core.graphics.SaltyGraphics;
import de.edgelord.saltyengine.scene.Scene;
import de.edgelord.saltyengine.transform.Vector2f;
import de.naclstudios.molehill.gameobjects.Enemy;
import de.naclstudios.molehill.gameobjects.Tower;
import de.naclstudios.molehill.main.Main;

public class TDScene extends Scene {

    public TDScene() {

        addGameObject(new Enemy(0, 0, 100, 50, .5f, 5));
        addGameObject(new Tower(250, 100, 100, 100, 350, 1, .5f, 250));
    }

    @Override
    public void draw(SaltyGraphics saltyGraphics) {
        super.draw(saltyGraphics);

        for (Vector2f point : Main.track) {
            saltyGraphics.drawOval(point.getX() - 2, point.getY() - 2, 4, 4);
        }
    }
}
