import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;
import java.util.Scanner;

public class Snake{

    private static final int DIM = 150;

    private static final int[] x = new int[DIM*DIM/16];
    private static final int[] y = new int[DIM*DIM/16];

    private static int dots;
    private static String mode;
    private static boolean collisionsEnabled;
    private static int difficulty;
    private static Apple apple;
    private static int score;
    private static int frameRate;
    private static boolean dirRight = true;
    private static boolean dirLeft = false;
    private static boolean dirUp = false;
    private static boolean dirDown = false;
    private static boolean inGame = true;

    private static void exitScreen() {
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.text(DIM/2.0, DIM/2.0, "Thank you for playing!");
    }

    private static void gameStart() {
        frameRate = 1200;
        score = 0;
        mode = "gridlock";
        collisionsEnabled = true;
        StdDraw.clear(StdDraw.BLACK);
        dots = 1;
        for (int i = 0; i < dots; i++) {
            x[i] = DIM/2;
            y[i] = DIM/2;
        }
        apple = new Apple();
        while (inGame) {
            StdDraw.setPenColor(Color.WHITE);
            StdDraw.square(DIM/2.0, DIM/2.0, DIM/2.0);
            StdDraw.text(DIM/2.0, DIM/2.0-20, "Move to Start");
            StdDraw.setPenColor(Color.GREEN);
            StdDraw.filledSquare(DIM/2.0, DIM/2.0, 2);
            Random r = new Random();
            if (StdDraw.isKeyPressed(KeyEvent.VK_LEFT) || StdDraw.isKeyPressed(KeyEvent.VK_RIGHT) || StdDraw.isKeyPressed(KeyEvent.VK_UP) || StdDraw.isKeyPressed(KeyEvent.VK_DOWN)) {
                while (inGame) {
                    if(StdDraw.isKeyPressed(KeyEvent.VK_ESCAPE)){
                        System.exit(1);
                    }
                    StdDraw.show(frameRate / 20);
                    if(difficulty == 0){
                        StdDraw.clear(new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255), 5));
                    }
                    else {
                        StdDraw.clear(StdDraw.BLACK);
                    }
                    checks();
                }
            }
        }
    }
    private static void paint() {
        StdDraw.setFont(new Font("Times New Roman", Font.BOLD, 12));
        StdDraw.setPenColor(Color.RED);
        if(mode.equals("gridless")){
            StdDraw.filledCircle(apple.x, apple.y, 2);
            StdDraw.setPenColor(Color.GREEN);
            for (int i = 0; i < dots; i++) {
                StdDraw.filledCircle(x[i], y[i], 2);
            }
        }
        else{
            StdDraw.filledSquare(apple.x, apple.y, 2);
            StdDraw.setPenColor(Color.GREEN);
            for (int i = 0; i < dots; i++) {
                StdDraw.filledSquare(x[i], y[i], 2);
            }
        }
        StdDraw.setPenColor(StdDraw.WHITE);
        String printScore = "Score: ";
        String printMode = "Mode: ";
        String printHeadPos = "Head Location: (";
        String printFPS = "FPS: ";
        StdDraw.text(DIM/10.0, DIM+5, printScore + score);
        StdDraw.text(DIM/2.0, DIM+5, printMode + mode);
        StdDraw.text(DIM-10, DIM+5, printFPS + (frameRate/20));
        StdDraw.text(DIM/5.0, -5, (printHeadPos + x[0] + ", " + y[0] + ")"));
        StdDraw.square(DIM/2.0, DIM/2.0, DIM/2.0);
    }
    private static void checks() {
        checkKey();
        move();
        paint();
        checkApple();
        collisionDetect();
        if (System.currentTimeMillis() >= apple.startTime + apple.endTime) {
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
        if(StdDraw.isKeyPressed(KeyEvent.VK_W)){
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
        if(StdDraw.isKeyPressed(KeyEvent.VK_Z)){
            collisionsEnabled = false;
        }
        else  if(StdDraw.isKeyPressed(KeyEvent.VK_X)){
            collisionsEnabled = true;
        }
        if(StdDraw.mousePressed()){
            if(mode.equals("gridlock")){
                mode = "gridless";
            }
            else{
                mode = "gridlock";
                x[0] = 5*(x[0]/5);
                y[0] = 5*(y[0]/5);
            }
        }
    }

    private static void move() {
        for (int i = dots; i > 0; i--) {
            x[i] = x[(i - 1)];
            y[i] = y[(i - 1)];
        }

        if(mode.equals("gridless")){
            x[0] = (int) StdDraw.mouseX();
            y[0] = (int) StdDraw.mouseY();
        }
        else {
            if (dirDown && !dirUp) {
                y[0] -= 5;
            }
            else if (dirUp && !dirDown) {
                y[0] += 5;
            }
            else if (dirRight && !dirLeft) {
                x[0] += 5;
            }
            else if (dirLeft && !dirRight) {
                x[0] -= 5;
            }
        }
    }

    private static void collisionDetect() {
        for (int i = dots; i > 0; i--) {
            if ((i > 1) && (x[0] == x[i]) && (y[0] == y[i])) {
                if(collisionsEnabled){
                    inGame = false;
                    gameOver();
                }
            }
        }
        if (y[0] >= DIM || y[0] <= 0 || x[0] <= 0 || x[0] >= DIM) {
            if(collisionsEnabled){
                inGame = false;
                gameOver();
            }
            else{
                if (y[0] < 5) {
                    y[0] = DIM-5;
                }
                else if (y[0] > DIM-5) {
                    y[0] = 5;
                }
                else if (x[0] > DIM-5) {
                    x[0] = 5;
                }
                else {
                    x[0] = DIM-5;
                }
            }
        }
    }
    private static void checkApple() {
        if ((x[0] <= apple.x+4 && x[0] >= apple.x-4) && (y[0] <= apple.y+4 && y[0] >= apple.y-4)) {
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
            StdDraw.text(DIM/2.0, DIM/2.0+10, "YOU WIN!");
            StdDraw.text(DIM/2.0, DIM/2.0, "Score: " + score);
            StdDraw.text(DIM/2.0, DIM/2.0-10, "Play Again? Y/N ");
            StdDraw.show();
        }
        else {
            StdDraw.text(DIM/2.0, DIM/2.0+10, "Game Over");
            StdDraw.text(DIM/2.0, DIM/2.0, "Score: " + score);
            StdDraw.text(DIM/2.0, DIM/2.0-10, "Play Again? Y/N ");
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
        int x;
        int y;
        double startTime;
        double endTime;
        static final Random RNG = new Random(Long.getLong("seed", System.nanoTime()));
        Apple(){
            startTime = System.currentTimeMillis();
            if(difficulty == 10){
                endTime = RNG.nextInt(5) * 2000;
            }
            else{
                endTime = RNG.nextInt(100-difficulty*10) * 2000;
            }
            int limit = DIM - 20;
            x = (10 * (RNG.nextInt(limit) / 10));
            y = (10 * (RNG.nextInt(limit) / 10));
            if(x >= DIM - 20){
                x -= 30;
            }
            else if(x <= 20){
                x+= 30;
            }
            if(y >= DIM - 20){
                y -= 30;
            }
            else if(y <= 20){
                y += 30;
            }
        }
    }

    public static void main(final String[] args) {

        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setXscale(0, DIM);
        StdDraw.setYscale(0, DIM);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(DIM / 2.0, DIM / 2.0, "Select Difficulty (1-9)");
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                difficulty = StdDraw.nextKeyTyped()-48;
                break;
            }
        }
        if (difficulty == 0) {
            StdDraw.clear(StdDraw.BLACK);
            StdDraw.text(DIM / 2.0, DIM / 2.0, "Warning: You have selected extreme mode! Confirm Selection");
            StdDraw.text(DIM / 2.0, DIM / 2.0 - 10, "Confirm Selection");
            while (true) {
                if (StdDraw.hasNextKeyTyped()) {
                    difficulty = StdDraw.nextKeyTyped()-48;
                    break;
                }
            }
        }
        gameStart();
    }
}