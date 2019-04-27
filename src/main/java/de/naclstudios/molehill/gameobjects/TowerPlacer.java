package de.naclstudios.molehill.gameobjects;

import de.edgelord.saltyengine.core.graphics.SaltyGraphics;
import de.edgelord.saltyengine.gameobject.EmptyGameObject;
import de.edgelord.saltyengine.input.Input;
import de.edgelord.saltyengine.input.MouseInputHandler;
import de.edgelord.saltyengine.scene.SceneManager;

import java.awt.event.MouseEvent;

public class TowerPlacer extends EmptyGameObject {

    private Tower sampleTower;

    public TowerPlacer(Tower sampleTower) {
        super(0, 0, 0, 0, "empty");

        this.sampleTower = sampleTower;

        Input.addMouseInputHandler(new MouseInputHandler() {
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
        });
    }

    @Override
    public void onFixedTick() {
        sampleTower.getTransform().positionByCentre(Input.getCursorPosition());
        sampleTower.updateCollider();

        if (Input.mouseDown) {
            SceneManager.getCurrentScene().addGameObject(sampleTower);
            sampleTower.setAllowShoot(true);
            removeFromCurrentScene();
        }
    }

    @Override
    public void draw(SaltyGraphics saltyGraphics) {
        sampleTower.draw(saltyGraphics);
    }
}
