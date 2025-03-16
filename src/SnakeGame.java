import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class SnakeGame extends GameEngine{
    int width = 500;
    int height = 500;

    Image head;
    Image body;
    Image apple;

    int playerX = 50;
    int playerY = 100;
    int speed = 5;

    int appleX, appleY;

    Random random = new Random();
    boolean gameOver;

    String playerDirection = "RIGHT";
    List<Point> snakeBody = new ArrayList<>();

    private enum GameState {INTRO, PLAYING, GAME_OVER}
    private GameState currentState =GameState.INTRO;

    public void init(){
        setWindowSize(width, height);
        head = loadImage("src/resources/head.png");
        body = loadImage("src/resources/dot.png");
        apple = loadImage("src/resources/apple.png");
        snakeBody.add(new Point(playerX, playerY));
        spawnApple();
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
            if (currentState == GameState.INTRO || currentState == GameState.GAME_OVER) {
                currentState = GameState.PLAYING;
                resetGame();
            }
        }
    }

    public void spawnApple(){
        appleX = random.nextInt(width / 5) * 5;
        appleY = 50 + random.nextInt((height - 50) / 5) * 5;
    }

    public void growSnake(){
        Point tail = snakeBody.get(snakeBody.size()-1);
        snakeBody.add(new Point(tail));
    }

    @Override
    public void update(double dt) {
        int prevX = playerX;
        int prevY = playerY;

        //Player Movement
        if(playerDirection.equals("LEFT")){
            playerX -= speed;
        }
        if(playerDirection.equals("RIGHT")){
            playerX += speed;
        }
        if(playerDirection.equals("UP")){
            playerY -= speed;
        }
        if(playerDirection.equals("DOWN")){
            playerY += speed;
        }

        for(int i = snakeBody.size() - 1;i >0; i--){
            snakeBody.set(i, new Point(snakeBody.get(i-1)));
        }

        if(!snakeBody.isEmpty()){
            snakeBody.set(0, new Point(prevX, prevY));
        }

        //Check Collision with Fruit
        if (Math.abs(playerX - appleX) <= speed && Math.abs(playerY - appleY) <= speed) {
            spawnApple();
            growSnake();
        }

        //Check Collision with Body
        for (int i = 1; i < snakeBody.size(); i++){
            if(playerX == snakeBody.get(i).x && playerY== snakeBody.get(i).y) {
                currentState = GameState.GAME_OVER;
                break;
            }
        }

        //Check Collision with the wall
        if (playerX < 0 || playerX >= width || playerY < 50 || playerY >= height) {
            currentState = GameState.GAME_OVER;
            return;
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
            case GAME_OVER:
                drawGameOverScreen();
                break;
        }
    }

    private void drawIntroScreen() {
        changeBackgroundColor(black);
        clearBackground(500, 500);
        changeColor(Color.GREEN);
        drawText(width / 2 - 100, height/3, "SNAKE GAME", 30);
        drawText(width / 2 - 110, height/2, "Press ENTER to start", 24);
    }

    private void drawGameOverScreen() {
        changeBackgroundColor(black);
        clearBackground(500, 500);
        changeColor(Color.RED);
        drawText(width / 2 - 100, height/3, "GAME OVER", 30);
        drawText(width / 2 - 100, height/2, "Press ENTER to restart", 24);
        changeColor(Color.WHITE);
        drawText(width / 2 - 100, height / 2 + 40, "Score: " + (snakeBody.size() - 1), 20);
    }

    private void drawGame(){
        changeBackgroundColor(black);
        clearBackground(500, 500);
        changeColor(Color.GRAY);
        drawSolidRectangle(0, 0, width, 50);
        changeColor(Color.WHITE);
        drawText(20, 30, "Score: " + (snakeBody.size() - 1), 20);


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