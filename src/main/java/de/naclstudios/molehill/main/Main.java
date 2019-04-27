package de.naclstudios.molehill.main;

import de.edgelord.saltyengine.core.Game;
import de.edgelord.saltyengine.core.GameConfig;
import de.edgelord.saltyengine.core.graphics.SaltyGraphics;
import de.edgelord.saltyengine.displaymanager.display.SplashWindow;
import de.edgelord.saltyengine.effect.image.SaltyImage;
import de.edgelord.saltyengine.scene.SceneManager;
import de.edgelord.saltyengine.transform.Transform;
import de.edgelord.saltyengine.transform.Vector2f;
import de.edgelord.saltyengine.utils.Grid;
import de.naclstudios.molehill.gameobjects.Enemy;
import de.naclstudios.molehill.scene.TDScene;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Main extends Game {

    public static List<Vector2f> track = new ArrayList<>();
    public static List<Transform> moleHills = new ArrayList<>();

    private static int score = 0;
    private static int health = 200;

    public static Grid grid = null;
    public static SaltyImage gridImage = new SaltyImage(960, 540);

    public static void main(String[] args) {
        init(GameConfig.config(960, 540, "Mole Hill", 5));
        start(60, SplashWindow.Splash.NO_SPLASH);
        getHost().toggleFullscreen();

        grid = new Grid(96, 54);

        getHostAsDisplayManager().getStage().setHighQuality(false);
        getHostAsDisplayManager().getStage().putToLQRenderHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        track.add(new Vector2f(500, 0));
        track.add(new Vector2f(500, 500));
        track.add(new Vector2f(1000, 500));
        track.add(new Vector2f(1000, 750));

        moleHills.add(new Transform(250, 100, 75, 75));

        SceneManager.setCurrentScene(new TDScene());

        SaltyGraphics gridDrawGraphics = new SaltyGraphics(gridImage.createGraphics());

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {

                gridDrawGraphics.outlineRect(grid.getTile(i, j));
            }
        }
    }

    public static void scoreEnemy(Enemy enemy) {
        score += enemy.getMaxHealth();
    }

    public static int getScore() {
        return score;
    }

    public static int getHealth() {
        return health;
    }

    public static void decreaseHealth(int amount) {
        health -= amount;
    }
}