import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;
import java.util.Scanner;

public class Snake{

    private static final int DIM = 200;

    private static final int[] x = new int[DIM*DIM/16];
    private static final int[] y = new int[DIM*DIM/16];

    private static int dots;
    private static int difficulty;
    private static Apple apple;
    private static int score = 0;
    private static int frameRate;
    private static boolean dirRight = true;
    private static boolean dirLeft = false;
    private static boolean dirUp = false;
    private static boolean dirDown = false;
    private static boolean inGame = true;

    private static void exitScreen() {
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.text(100, 100, "Thank you for playing!");
    }

    private static void gameStart() {
        frameRate = 1200;
        StdDraw.clear(StdDraw.BLACK);
        dots = 1;
        for (int i = 0; i < dots; i++) {
            x[i] = DIM/2;
            y[i] = DIM/2;
        }
        apple = new Apple();
        while (inGame) {
            StdDraw.setPenColor(Color.WHITE);
            StdDraw.text(100, 80, "Move to Start");
            StdDraw.setPenColor(Color.GREEN);
            StdDraw.filledSquare(DIM/2.0, DIM/2.0, 2);
            if (StdDraw.isKeyPressed(KeyEvent.VK_LEFT) || StdDraw.isKeyPressed(KeyEvent.VK_RIGHT) || StdDraw.isKeyPressed(KeyEvent.VK_UP) || StdDraw.isKeyPressed(KeyEvent.VK_DOWN)) {
                while (inGame) {
                    if(StdDraw.isKeyPressed(KeyEvent.VK_ESCAPE)){
                        System.exit(1);
                    }
                    StdDraw.show(frameRate / 20);
                    StdDraw.clear(StdDraw.BLACK);
                    checks();
                }
            }
        }
    }
    private static void paint() {
        StdDraw.setFont(new Font("Times New Roman", Font.BOLD, 12));
        StdDraw.setPenColor(Color.RED);
        StdDraw.filledSquare(apple.getX(), apple.getY(), 2);
        StdDraw.setPenColor(Color.GREEN);
        for (int i = 0; i < dots; i++) {
            StdDraw.filledSquare(x[i], y[i], 2);
        }
        StdDraw.setPenColor(StdDraw.WHITE);
        String printScore = "Score: ";
        String printFPS = "FPS: ";
        StdDraw.text(15, 205, printScore + score);
        StdDraw.text(190, 205, printFPS + (frameRate/20));
        StdDraw.square(DIM/2.0, DIM/2.0, DIM/2.0);
    }
    private static void checks() {
        checkKey();
        move();
        paint();
        checkApple();
        collisionDetect();
        if (System.currentTimeMillis() >= apple.getStartTime() + apple.getEndTime()) {
            apple = new Apple();
        }
    }
    private static void checkKey() {
        if (StdDraw.isKeyPressed(KeyEvent.VK_DOWN) && (!dirUp)) {
            dirDown = true;
            dirRight = false;
            dirLeft = false;
        }
        else if (StdDraw.isKeyPressed(KeyEvent.VK_UP) && (!dirDown)) {
            dirUp = true;
            dirRight = false;
            dirLeft = false;
        }
        else if (StdDraw.isKeyPressed(KeyEvent.VK_RIGHT) && (!dirLeft)) {
            dirRight = true;
            dirDown = false;
            dirUp = false;
        }
        else if (StdDraw.isKeyPressed(KeyEvent.VK_LEFT) && (!dirRight)) {
            dirLeft = true;
            dirDown = false;
            dirUp = false;
        }
        if(StdDraw.isKeyPressed(KeyEvent.VK_CONTROL)){
            if(frameRate > 10){
                frameRate -= 10;
            }
            else {
                frameRate = 10;
            }
        }
        else if(StdDraw.isKeyPressed(KeyEvent.VK_SHIFT)){
            frameRate += 10;
        }
        else if(StdDraw.isKeyPressed(KeyEvent.VK_W)){
            dots++;
            score++;
            paint();
        }
        else if(StdDraw.isKeyPressed(KeyEvent.VK_S)){
            if(dots > 1 && score > 0) {
                dots--;
                score--;
            }
            else {
                dots = 1;
                score = 0;
            }
            paint();
        }
    }

    private static void move() {
        for (int i = dots; i > 0; i--) {
            x[i] = x[(i - 1)];
            y[i] = y[(i - 1)];
        }
        x[0] = (int) StdDraw.mouseX();
        y[0] = (int) StdDraw.mouseY();
//        if (dirDown && !dirUp) {
//            y[0] -= 5;
//        }
//        else if (dirUp && !dirDown) {
//            y[0] += 5;
//        }
//        else if (dirRight && !dirLeft) {
//            x[0] += 5;
//        }
//        else if (dirLeft && !dirRight) {
//            x[0] -= 5;
//        }
    }

    private static void collisionDetect() {
        for (int i = dots; i > 0; i--) {
            if ((i > 1) && (x[0] == x[i]) && (y[0] == y[i])) {
                inGame = false;
                gameOver();
            }
        }
        if (y[0] >= DIM || y[0] <= 0 || x[0] <= 0 || x[0] >= DIM) {
            inGame = false;
            gameOver();
        }
    }
    private static void checkApple() {
        if ((x[0] <= apple.getX()+5 && x[0] >= apple.getX()-5) && (y[0] <= apple.getY()+5 && y[0] >= apple.getY()-5)) {
            dots++;
            score++;
            apple = new Apple();
            paint();
        }
    }

    private static void gameOver() {
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.setFont(new Font("Times New Roman", Font.BOLD, 16));
        if (score >= DIM*DIM/16) {
            StdDraw.text(100, 100, "YOU WIN!");
            StdDraw.text(100, 90, "Score: " + score);
            StdDraw.text(100, 80, "Play Again? Y/N ");
            StdDraw.show();
        }
        else {
            StdDraw.text(100, 100, "Game Over");
            StdDraw.text(100, 90, "Score: " + score);
            StdDraw.text(100, 80, "Play Again? Y/N ");
            StdDraw.show();
            while (!inGame) {
                if (StdDraw.isKeyPressed(KeyEvent.VK_Y)) {
                    score = 0;
                    inGame = true;
                    StdDraw.clear(StdDraw.BLACK);
                    gameStart();
                }
                if (StdDraw.isKeyPressed(KeyEvent.VK_N)) {
                    StdDraw.clear(StdDraw.BLACK);
                    exitScreen();
                }
            }
        }
    }

    private static class Apple {
        private int x;
        private int y;
        private double startTime;
        private double endTime;
        private static final Random RNG = new Random(Long.getLong("seed", System.nanoTime()));
        Apple(){
            startTime = System.currentTimeMillis();
            endTime = RNG.nextInt(100-difficulty*10) * 2000;
            int width = 200;
            int limit = width - 20;
            x = (10 * (RNG.nextInt(limit) / 10));
            y = (10 * (RNG.nextInt(limit) / 10));
            if(x >= width - 20){
                x -= 30;
            }
            else if(x <= 20){
                x+= 30;
            }
            if(y >= width - 20){
                y -= 30;
            }
            else if(y <= 20){
                y += 30;
            }
        }
        int getX(){
            return x;
        }
        int getY(){
            return y;
        }
        double getStartTime(){
            return startTime;
        }
        double getEndTime(){
            return endTime;
        }
    }

    public static void main(final String[] args) {
        Scanner console = new Scanner(System.in);
        System.out.print("Select Difficulty (1-9) >>> ");
        difficulty = console.nextInt();

        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setXscale(0, DIM);
        StdDraw.setYscale(0, DIM);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.square(DIM/2.0, DIM/2.0, DIM/2.0);
        gameStart();
    }
}