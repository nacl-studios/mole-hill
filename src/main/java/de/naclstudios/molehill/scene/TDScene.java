package de.naclstudios.molehill.scene;

import de.edgelord.saltyengine.core.Game;
import de.edgelord.saltyengine.core.graphics.SaltyGraphics;
import de.edgelord.saltyengine.gameobject.GameObject;
import de.edgelord.saltyengine.input.Input;
import de.edgelord.saltyengine.input.MouseInputHandler;
import de.edgelord.saltyengine.scene.Scene;
import de.edgelord.saltyengine.scene.SceneManager;
import de.edgelord.saltyengine.transform.Transform;
import de.edgelord.saltyengine.transform.Vector2f;
import de.edgelord.saltyengine.ui.elements.*;
import de.edgelord.saltyengine.utils.ColorUtil;
import de.naclstudios.molehill.gameobjects.*;
import de.naclstudios.molehill.main.Main;

import java.awt.Color;
import java.awt.event.MouseEvent;

public class TDScene extends Scene {

    private Label scoreLabel;
    private Button buyBombMole;

    private int ticks = 0;
    private float currentBuff = 0f;

    private ProgressBar healthBar;

    public static Button upgradeButton = new Button("Upgrade (- 0 health)", Main.grid.getTransform(8, 1, 2, 1), "upgrade-button") {
        @Override
        public void onClick(MouseEvent e) {
            if (Main.currentSelectedTower != null) {
                Main.currentSelectedTower.upgrade();
                Main.currentSelectedTower.setUpgraded(true);
            }
        }
    };

    public TDScene() {

        scoreLabel = new BorderedLabel(Integer.toString(Main.getScore()), 0, 0, Game.getGameWidth(), 300);
        scoreLabel.setFont(scoreLabel.getFont().deriveFont(25f));
        scoreLabel.setForegroundColor(Color.BLACK);

        buyBombMole = new Button("Buy Bomb Mole", Main.grid.getTransform(8, 4, 2, 1), "buy-bomb-mole") {
            @Override
            public void onClick(MouseEvent e) {
                addGameObject(new TowerPlacer(new BombMole(0, 0)));
            }
        };

        buyBombMole.setFont(buyBombMole.getFont().deriveFont(35f));

        Transform healthBarSketch = Main.grid.getTransform(7, 0,3, 1);
        healthBar = new ProgressBar(healthBarSketch.getPosition(), healthBarSketch.getWidth(), 35);
        healthBar.setMaxValue(Main.MAX_HEALTH);
        healthBar.setCornerArc(30f);
        healthBar.setBackgroundColor(ColorUtil.PLAIN_BLUE);

        upgradeButton.setFont(buyBombMole.getFont());

        getUI().addElement(scoreLabel);
        getUI().addElement(buyBombMole);
        getUI().addElement(healthBar);

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

                for (int i = 0; i < getGameObjects().size(); i++) {
                    GameObject gameObject = getGameObjects().get(i);

                    if (gameObject.getTag().equals(Tower.TAG)) {
                        Tower tower = (Tower) gameObject;
                        if (gameObject.mouseTouches()) {
                            tower.setSelected(true);
                            Main.currentSelectedTower = tower;
                            if (!tower.isUpgraded()) {
                                addUpgradeButton();
                            }

                            return;
                        }
                    }
                }

                getUI().removeElement(upgradeButton);
                Main.currentSelectedTower = null;
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
    public void draw(SaltyGraphics saltyGraphics) {
        super.draw(saltyGraphics);

        for (Vector2f point : Main.track) {
            saltyGraphics.drawOval(point.getX() - 2, point.getY() - 2, 4, 4);
        }

        scoreLabel.setText(Integer.toString(Main.getScore()));
        saltyGraphics.drawImage(Main.gridImage, 0, 0);
    }

    @Override
    public void onFixedTick() {
        super.onFixedTick();
        if (ticks == 200) {
            ticks = 0;
            currentBuff += .1f;
            addGameObject(new StandardEnemy(0, 0));

        } else {
            ticks++;
        }

        healthBar.setCurrentValue(Main.getHealth());
    }

    public static void addUpgradeButton() {
        upgradeButton.setText("Upgrade (costs " + Main.currentSelectedTower.getPrize() * 2 + ")");
        SceneManager.getCurrentScene().getUI().addElement(upgradeButton);
    }
}
