import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;


public class SnakeGame extends GameEngine{

    int width = 500;
    int height = 500;

    Image head;
    Image body;
    Image apple;

    int playerX = 20;
    int playerY = 20;
    int speed = 5;

    int appleX, appleY;

    Random random = new Random();
    boolean gameOver;

    String playerDirection = "RIGHT";

    public void init(){
        setWindowSize(width, height);
        head = loadImage("src/resources/head.png");
        body = loadImage("src/resources/dot.png");
        apple = loadImage("src/resources/apple.png");
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
    }

    public void spawnApple(){
        appleX = random.nextInt(width / 5)*5;
        appleY = random.nextInt(height / 5)*5;
    }

    @Override
    public void update(double dt) {
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

        //Check Collision with Fruit
        if (Math.abs(playerX - appleX) <= speed && Math.abs(playerY - appleY) <= speed) {
            spawnApple();
        }

    }

    @Override
    public void paintComponent() {
        changeBackgroundColor(black);
        clearBackground(500, 500);

        //Player
        drawImage(head, playerX, playerY);
        drawImage(apple, appleX, appleY);

    }


    public static void main(String[] args) {
        createGame(new SnakeGame());
    }
}