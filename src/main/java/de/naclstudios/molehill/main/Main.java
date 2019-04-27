package de.naclstudios.molehill.main;

import de.edgelord.saltyengine.core.Game;
import de.edgelord.saltyengine.core.GameConfig;
import de.edgelord.saltyengine.scene.SceneManager;

public class Main extends Game {

    public static void main(String[] args) {
        init(GameConfig.config(1920, 1080, "Mole Hill", 5));
        start(60);
    }
}