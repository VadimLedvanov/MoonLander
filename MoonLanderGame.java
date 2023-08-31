package com.javarush.games.moonlander;

import com.javarush.engine.cell.*;



public class MoonLanderGame extends Game {
    public static final int WIDTH = 64;
    public static final int HEIGHT = 64;
    private Rocket rocket;
    private GameObject landscape;

    private boolean isUpPressed;
    private boolean isLeftPressed;
    private boolean isRightPressed;
    private GameObject platform;

    private boolean isGameStopped;
    private int score;

    @Override
    public void initialize() {
        setScreenSize(WIDTH, HEIGHT);
        createGame();
        showGrid(false);
    }

    private void drawScene() {
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                setCellColor(i, j, Color.SILVER);
            }
        }

        rocket.draw(this);
        landscape.draw(this);
    }

    private void createGame() {
        createGameObjects();
        drawScene();
        setTurnTimer(50);
        isUpPressed = false;
        isLeftPressed = false;
        isRightPressed = false;
        isGameStopped = false;
        score = 1000;
        setScore(score);
    }

    @Override
    public void onTurn(int step) {
        rocket.move(isUpPressed, isLeftPressed, isRightPressed);
        check();
        if (score > 0)
            score -= 1;
        setScore(score);
        drawScene();
    }

    @Override
    public void setCellColor(int x, int y, Color color) {
        if (x < 0 || x >= WIDTH || y < 0 || y >= HEIGHT)
            return;
        super.setCellColor(x, y, color);
    }

    private void createGameObjects() {
        rocket = new Rocket(WIDTH / 2, 0);
        landscape = new GameObject(0, 25, ShapeMatrix.LANDSCAPE);
        platform = new GameObject(23, MoonLanderGame.HEIGHT - 1, ShapeMatrix.PLATFORM);
    }

    @Override
    public void onKeyPress(Key key) {
        if (key == Key.UP)
            isUpPressed = true;
        else if (key == Key.LEFT)
        {
            isLeftPressed = true;
            isRightPressed = false;
        }
        else if (key == Key.RIGHT)
        {
            isRightPressed = true;
            isLeftPressed = false;
        }

        if (key == Key.SPACE && isGameStopped) {
            createGame();
            return;
        }
    }

    @Override
    public void onKeyReleased(Key key) {
        if (key == Key.UP)
            isUpPressed = false;
        else if (key == Key.LEFT)
        {
            isLeftPressed = false;
        }
        else if (key == Key.RIGHT)
        {
            isRightPressed = false;
        }
    }

    private void check() {
        if (rocket.isCollision(landscape) && !(rocket.isCollision(platform) && rocket.isStopped()))
            gameOver();
        if (rocket.isCollision(platform) && rocket.isStopped())
            win();
    }

    private void win() {
        rocket.land();
        isGameStopped = true;
        showMessageDialog(Color.AZURE, "WELL DONE!", Color.AQUA, 50);
        stopTurnTimer();
    }

    private void gameOver() {
        score = 0;
        setScore(score);
        rocket.crash();
        isGameStopped = true;
        showMessageDialog(Color.SLATEGREY, "LOOSER", Color.SLATEBLUE, 50);
        stopTurnTimer();
    }
}
