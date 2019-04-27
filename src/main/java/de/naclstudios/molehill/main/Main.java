package de.naclstudios.molehill.main;

import de.edgelord.saltyengine.core.Game;
import de.edgelord.saltyengine.core.GameConfig;
import de.edgelord.saltyengine.displaymanager.display.SplashWindow;
import de.edgelord.saltyengine.scene.SceneManager;
import de.edgelord.saltyengine.transform.Vector2f;
import de.naclstudios.molehill.scene.TDScene;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Main extends Game {

    public static List<Vector2f> track = new ArrayList<>();

    public static void main(String[] args) {
        init(GameConfig.config(960, 540, "Mole Hill", 5));
        start(60, SplashWindow.Splash.NO_SPLASH);
        getHost().toggleFullscreen();

        getHostAsDisplayManager().getStage().setHighQuality(false);
        getHostAsDisplayManager().getStage().putToLQRenderHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        track.add(new Vector2f(500, 0));
        track.add(new Vector2f(500, 500));
        track.add(new Vector2f(1000, 500));
        track.add(new Vector2f(1000, 750));

        SceneManager.setCurrentScene(new TDScene());
    }
}