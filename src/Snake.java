import java.awt.*;
import java.awt.event.KeyEvent;

public class Snake{

    private final int WIDTH = 200;
    private final int HEIGHT = 200;
    private final int DOTSIZE = 5;
    private final int NUMBEROFSPACES = 900;
    private final int MIDDLE = 100;

    private final int[] x = new int[NUMBEROFSPACES];
    private final int[] y = new int[NUMBEROFSPACES];

    private int dots;
    private int difficulty;
    private int appleNum;
    private Apple[] apples;
    private int score = 0;
    private final Font tMN = new Font("Times New Roman", Font.BOLD, 12);
    private final Font endGame = new Font("Times New Roman", Font.BOLD, 16);
    private int frameRate;
    private int defaultFrameRate = 1200;
    private final int left = KeyEvent.VK_LEFT;
    private final int up = KeyEvent.VK_UP;
    private final int down = KeyEvent.VK_DOWN;
    private final int right = KeyEvent.VK_RIGHT;
    private boolean dirRight = true;
    private boolean dirLeft = false;
    private boolean dirUp = false;
    private boolean dirDown = false;
    private boolean firstMove = false;
    private boolean inGame = true;

    private Snake() {
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.square(MIDDLE, MIDDLE, MIDDLE);
        difficulty = 0;
        gameStart();
    }

    private void exitScreen() {
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.text(150, 150, "Thank you for playing!");
    }

    private void gameStart() {
        frameRate = defaultFrameRate;
        StdDraw.clear(StdDraw.BLACK);
        dots = 1;
        for (int i = 0; i < dots; i++) {
            x[i] = MIDDLE;
            y[i] = MIDDLE;
        }
        appleNum = 1;
        apples = new Apple[appleNum];

        for (int i = 0; i < appleNum; i++) {
            apples[i] = new Apple(difficulty);
        }
        while (inGame) {
            if (StdDraw.isKeyPressed(left) || StdDraw.isKeyPressed(right) || StdDraw.isKeyPressed(up) || StdDraw.isKeyPressed(down)) {
                checks();
                firstMove = true;
            }
            while (firstMove) {
                StdDraw.show(frameRate / 20);
                StdDraw.clear(StdDraw.BLACK);
                checks();
            }
        }
    }
    private void paint() {
        StdDraw.setFont(tMN);
        StdDraw.setPenColor(Color.RED);
        for (int i = 0; i < appleNum; i++) {
            StdDraw.filledSquare(apples[i].getX(), apples[i].getY(), 2);
        }
        StdDraw.setPenColor(Color.GREEN);
        for (int i = 0; i < dots; i++) {
            StdDraw.filledSquare(x[i], y[i], 2);
        }
        StdDraw.setPenColor(StdDraw.WHITE);
        String printScore = "Score: ";
        String printFPS = "FPS: ";
        StdDraw.text(15, 205, printScore + score);
        StdDraw.text(190, 205, printFPS + (frameRate/20));
        StdDraw.square(MIDDLE, MIDDLE, MIDDLE);
    }
    private void checks() {
        checkKey();
        move();
        paint();
        checkApple();
        collisionDetect();
        for (int i = 0; i < appleNum; i++) {
            if (System.currentTimeMillis() >= apples[i].getStartTime()
                    + apples[i].getEndTime()) {
                apples[i] = new Apple(difficulty);
            }
        }
    }
    private void checkKey() {
        if (StdDraw.isKeyPressed(down) && (!dirUp)) {
            dirDown = true;
            dirRight = false;
            dirLeft = false;
        }
        else if (StdDraw.isKeyPressed(up) && (!dirDown)) {
            dirUp = true;
            dirRight = false;
            dirLeft = false;
        }
        else if (StdDraw.isKeyPressed(right) && (!dirLeft)) {
            dirRight = true;
            dirDown = false;
            dirUp = false;
        }
        else if (StdDraw.isKeyPressed(left) && (!dirRight)) {
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
    
    private void move() {
        for (int i = dots; i > 0; i--) {
            x[i] = x[(i - 1)];
            y[i] = y[(i - 1)];
        }

        if (dirDown && !dirUp) {
            y[0] -= DOTSIZE;
        } else if (dirUp && !dirDown) {
            y[0] += DOTSIZE;
        } else if (dirRight && !dirLeft) {
            x[0] += DOTSIZE;
        } else if (dirLeft && !dirRight) {
            x[0] -= DOTSIZE;
        }
    }

    private void collisionDetect() {
        for (int i = dots; i > 0; i--) {
            if ((i > 1) && (x[0] == x[i]) && (y[0] == y[i])) {
                inGame = false;
                gameOver();
            }
        }
        if (y[0] >= HEIGHT || y[0] <= 0 || x[0] <= 0 || x[0] >= WIDTH) {
//            x[0] = MIDDLE;
//            y[0] = MIDDLE;
            inGame = false;
            gameOver();
        }
    }
    private void checkApple() {
        for (int i = 0; i < appleNum; i++) {
            if ((x[0] == apples[i].getX()) && (y[0] == apples[i].getY())) {
                dots++;
                score++;
                apples[i] = new Apple(difficulty);
                paint();
            }
        }
    }
    
    private void gameOver() {
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.setFont(endGame);
        if (score >= NUMBEROFSPACES) {
            StdDraw.text(100, 100, "YOU WIN!");
            StdDraw.text(100, 90, "Score: " + score);
            StdDraw.text(100, 80, "Play Again? Y/N ");
            StdDraw.show();
        } else {
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
    public static void main(final String[] args) {
        new Snake();
    }
}
