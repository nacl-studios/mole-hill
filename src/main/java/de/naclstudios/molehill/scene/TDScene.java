package de.naclstudios.molehill.scene;

import de.edgelord.saltyengine.core.Game;
import de.edgelord.saltyengine.core.graphics.SaltyGraphics;
import de.edgelord.saltyengine.scene.Scene;
import de.edgelord.saltyengine.transform.Vector2f;
import de.edgelord.saltyengine.ui.elements.BorderedLabel;
import de.edgelord.saltyengine.ui.elements.Button;
import de.edgelord.saltyengine.ui.elements.Label;
import de.edgelord.saltyengine.ui.elements.TextElement;
import de.naclstudios.molehill.gameobjects.Enemy;
import de.naclstudios.molehill.gameobjects.Tower;
import de.naclstudios.molehill.gameobjects.TowerPlacer;
import de.naclstudios.molehill.main.Main;

import java.awt.Color;
import java.awt.event.MouseEvent;

public class TDScene extends Scene {

    private Label scoreLabel;
    private Button buyBombMole;

    private int ticks = 0;
    private float currentBuff = 0f;

    public TDScene() {

        scoreLabel = new BorderedLabel(Integer.toString(Main.getScore()), 0, 0, Game.getGameWidth(), 300);
        scoreLabel.setVerticalAlignment(TextElement.VerticalAlignment.TOP);
        scoreLabel.setFont(scoreLabel.getFont().deriveFont(25f));
        scoreLabel.setForegroundColor(Color.BLACK);

        buyBombMole = new Button("Buy Bomb Mole", Main.grid.getTransform(8, 4, 2, 1), "buy-bomb-mole") {
            @Override
            public void onClick(MouseEvent e) {
                addGameObject(new TowerPlacer(new Tower(50, 0, 0, 50, 50, 350, 0.5f, 5f, 150)));
            }
        };

        getUI().addElement(scoreLabel);
        getUI().addElement(buyBombMole);
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
            addGameObject(new Enemy(0, 0, 100, 50, .5f + currentBuff, 5 + currentBuff));

        } else {
            ticks++;
        }
    }
}
