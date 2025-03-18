import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.io.*;


public class SnakeGame extends GameEngine{
    int width = 500;
    int height = 500;

    Image head;
    Image body;
    Image apple;

    int playerX = 50;
    int playerY = 100;
    int speed = 10;

    int appleX, appleY;
    private int highScore = 0;
    private final String highScoreFile = "highscore.txt";


    Random random = new Random();
    boolean gameOver;

    String playerDirection = "RIGHT";
    List<Point> snakeBody = new ArrayList<>();

    private enum GameState {INTRO, HELP, PLAYING, GAME_OVER}
    private GameState currentState =GameState.INTRO;

    public void init(){
        setWindowSize(width, height);

        head = loadImage("src/resources/head.png");
        body = loadImage("src/resources/dot.png");
        apple = loadImage("src/resources/apple.png");
        snakeBody.add(new Point(playerX, playerY));
        spawnApple();
        loadHighScore();
    }

    private void loadHighScore() {
        try (BufferedReader reader = new BufferedReader(new FileReader(highScoreFile))) {
            String line = reader.readLine();
            if (line != null) {
                highScore = Integer.parseInt(line.trim());
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("No high score found, starting fresh.");
        }
    }

    private void saveHighScore() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(highScoreFile))) {
            writer.write(String.valueOf(highScore));
        } catch (IOException e) {
            System.out.println("Error saving high score.");
        }
    }

    private void checkHighScore(){
        int currentScore = snakeBody.size() - 1;
        if (currentScore > highScore) {
            highScore = currentScore;
            saveHighScore();
        }
    }


    public void keyPressed(KeyEvent event){
        if(event.getKeyCode() == KeyEvent.VK_LEFT && !playerDirection.equals("RIGHT")){
            playerDirection = "LEFT";
        } else if (event.getKeyCode() == KeyEvent.VK_RIGHT && !playerDirection.equals("LEFT")) {
            playerDirection = "RIGHT";
        }else if(event.getKeyCode() == KeyEvent.VK_UP && !playerDirection.equals("DOWN")){
            playerDirection = "UP";
        }
        else if(event.getKeyCode() == KeyEvent.VK_DOWN && !playerDirection.equals("UP")){
            playerDirection = "DOWN";
        }


        if (event.getKeyCode() == KeyEvent.VK_ENTER) {
            if (currentState == GameState.GAME_OVER || currentState == GameState.INTRO){
                currentState = GameState.PLAYING;
                resetGame();
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent event){
        int x = event.getX();
        int y = event.getY();

        if (currentState == GameState.INTRO){
            // START BUTTON:
            if (x >= width / 2 - 50 && x<= width / 2 + 70 && y >= height / 2 - 25 && y<= height / 2 + 25) {
                currentState = GameState.PLAYING;
                resetGame();
            }
            // HELP BUTTON:
            else if (x >= width - 60 && x<= width - 10 && y>= 10 && y <= 40){
                currentState = GameState.HELP;
            }
        } else if (currentState == GameState.HELP) {
            // BACK BUTTON:
            if (x >= width / 2 - 50 && x <= width / 2 + 50 && y >= height -60 && y<= height - 30){
                currentState = GameState.INTRO;
            }
        }
    }

    public void spawnApple(){
        appleX = 20 + random.nextInt(90) *5;
        appleY = 75 + random.nextInt(80) * 5;
    }

    public void growSnake(){
        Point tail = snakeBody.get(snakeBody.size()-1);
        snakeBody.add(new Point(tail));
    }

    @Override
    public void update(double dt) {
        if (currentState == GameState.PLAYING) {
            int prevX = playerX;
            int prevY = playerY;

            //Player Movement
            if (playerDirection.equals("LEFT")) {
                playerX -= speed;
            }
            if (playerDirection.equals("RIGHT")) {
                playerX += speed;
            }
            if (playerDirection.equals("UP")) {
                playerY -= speed;
            }
            if (playerDirection.equals("DOWN")) {
                playerY += speed;
            }

            for (int i = snakeBody.size() - 1; i > 0; i--) {
                snakeBody.set(i, new Point(snakeBody.get(i - 1)));
            }

            if (!snakeBody.isEmpty()) {
                snakeBody.set(0, new Point(prevX, prevY));
            }

            //Check Collision with Fruit
            if (Math.abs(playerX - appleX) <= speed && Math.abs(playerY - appleY) <= speed) {
                spawnApple();
                growSnake();
            }

            //Check Collision with Body
            for (int i = 1; i < snakeBody.size(); i++) {
                if (playerX == snakeBody.get(i).x && playerY == snakeBody.get(i).y) {
                    currentState = GameState.GAME_OVER;
                    break;
                }
            }

            //Check Collision with the wall
            if (playerX < 0 || playerX >= width || playerY < 50 || playerY >= height) {
                currentState = GameState.GAME_OVER;
                checkHighScore();

            }
        }
    }

    @Override
    public void paintComponent() {
        switch (currentState) {
            case INTRO:
                drawIntroScreen();
                break;
            case PLAYING:
                drawGame();
                break;
            case HELP:
                drawHelpScreen();
                break;
            case GAME_OVER:
                drawGameOverScreen();
                break;
        }
    }

    private void drawIntroScreen() {
        changeBackgroundColor(black);
        clearBackground(500, 500);
        changeColor(Color.GREEN);
        drawText(width / 2 - 80, height/3, "SNAKE GAME", 30);

        changeColor(Color.GREEN);
        drawSolidRectangle(width / 2 - 50, height / 2 - 25, 120, 50);
        changeColor(Color.BLACK);
        drawText(width / 2 - 40, height / 2 + 5, "START GAME", 15);

        changeColor(Color.GREEN);
        drawSolidRectangle(width - 60, 10, 50, 30);
        changeColor(Color.BLACK);
        drawText(width - 55, 30, "HELP", 15);
    }

    private void drawHelpScreen(){
        changeBackgroundColor(black);
        clearBackground(width, height);
        changeColor(Color.WHITE);
        drawText(50, 100, "How to Play:", 24);
        drawText(50, 150, "- Use arrow keys to move", 18);
        drawText(50, 180, "- Eat the fruit to get points", 18);
        drawText(50, 210, "- You die if you hit the wall or yourself", 18);
        changeColor(Color.RED);
        drawSolidRectangle(width / 2 - 50, height - 60, 100, 30);
        changeColor(Color.WHITE);
        drawText(width / 2 - 30, height - 40, "BACK", 18);
    }

    private void drawGameOverScreen() {
        changeBackgroundColor(black);
        clearBackground(500, 500);
        changeColor(Color.RED);
        drawText(width / 2 - 100, height/3, "GAME OVER", 30);
        drawText(width / 2 - 100, height/2, "Press ENTER to restart", 24);
        changeColor(Color.WHITE);
        drawText(width / 2 - 100, height / 2 + 40, "Score: " + (snakeBody.size() - 1), 20);
        drawText(width / 2 - 100, height / 2 + 70, "High Score: " + highScore, 20);
    }

    private void drawGame(){
        changeBackgroundColor(black);
        clearBackground(500, 500);
        changeColor(Color.GRAY);
        drawSolidRectangle(0, 0, width, 50);
        changeColor(Color.WHITE);
        drawText(20, 30, "Score: " + (snakeBody.size() - 1), 20);
        drawText(250, 30, "High Score: " + highScore, 20);

        //Fruit
        drawImage(apple, appleX, appleY);

        //Player
        for(int i = 0; i < snakeBody.size(); i++){
            Image image = (i ==0) ? head: body;
            drawImage(image, snakeBody.get(i).x, snakeBody.get(i).y);
        }
    }
    private void resetGame() {
        playerX = 50;
        playerY = 100;
        snakeBody.clear();
        snakeBody.add(new Point(width() / 2, height() / 2));
        playerDirection = "RIGHT";
        spawnApple();

    }


    public static void main(String[] args) {
        createGame(new SnakeGame());
    }
}