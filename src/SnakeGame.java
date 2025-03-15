import java.awt.*;
import java.awt.event.KeyEvent;

public class SnakeGame extends GameEngine{

    Image head;
    Image body;
    Image apple;

    int playerX = 20;
    int playerY = 20;
    int speed = 5;

    String playerDirection = "RIGHT";
    
    public void init(){
        setWindowSize(500, 500);
        head = loadImage("src/resources/head.png");
        body = loadImage("src/resources/dot.png");
        apple = loadImage("src/resources/apple.png");
    }


    public void keyPressed(KeyEvent event){
        if(event.getKeyCode() == KeyEvent.VK_LEFT){
            playerDirection = "LEFT";
        } else if (event.getKeyCode() == KeyEvent.VK_RIGHT) {
            playerDirection = "RIGHT";
        }else if(event.getKeyCode() == KeyEvent.VK_UP){
            playerDirection = "UP";
        }
        else if(event.getKeyCode() == KeyEvent.VK_DOWN){
            playerDirection = "DOWN";
        }
    }




    @Override
    public void update(double dt) {
        if(playerDirection == "LEFT"){
            playerX -= speed;
        }
        if(playerDirection == "RIGHT"){
            playerX += speed;
        }
        if(playerDirection == "UP"){
            playerY -= speed;
        }
        if(playerDirection == "DOWN"){
            playerY += speed;
        }
    }

    @Override
    public void paintComponent() {
        changeBackgroundColor(black);
        clearBackground(500, 500);

        //Player
        drawImage(head, playerX, playerY);
    }

    public static void main(String[] args) {
        createGame(new SnakeGame());
    }
}