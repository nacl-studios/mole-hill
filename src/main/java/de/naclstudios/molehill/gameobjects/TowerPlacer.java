package de.naclstudios.molehill.gameobjects;

import de.edgelord.saltyengine.core.graphics.SaltyGraphics;
import de.edgelord.saltyengine.gameobject.EmptyGameObject;
import de.edgelord.saltyengine.input.Input;
import de.edgelord.saltyengine.scene.SceneManager;
import de.naclstudios.molehill.main.Main;


public class TowerPlacer extends EmptyGameObject {

    private Tower sampleTower;

    public TowerPlacer(Tower sampleTower) {
        super(0, 0, 0, 0, "empty");

        this.sampleTower = sampleTower;
    }

    @Override
    public void onFixedTick() {
        sampleTower.getTransform().positionByCentre(Input.getCursorPosition());
        sampleTower.updateCollider();

        if (Input.mouseDown) {
            SceneManager.getCurrentScene().addGameObject(sampleTower);
            sampleTower.setAllowShoot(true);
            removeFromCurrentScene();
            Main.decreaseHealth(sampleTower.getPrize());
        }
    }

    @Override
    public void draw(SaltyGraphics saltyGraphics) {
        sampleTower.draw(saltyGraphics);
    }
}
