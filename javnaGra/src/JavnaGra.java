import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;


public class JavnaGra extends JPanel implements ActionListener, KeyListener {


    int boardWidth = 360;
    int boardHeight = 640;

    Image backgroundImg;
    Image dragonImg;
    Image topObsticle;
    Image bottomObsticle;

    int dragonX = boardWidth / 8;
    int dragonY = boardHeight / 2;
    int dragonWidth = 54;
    int dragonHeight = 44;



    class Dragon{
        int x = dragonX;
        int y = dragonY;
        int width = dragonWidth;
        int height = dragonHeight;
        Image img;

        Dragon(Image img){
            this.img = img;
        }
    }

    int obsticleX = boardWidth;
    int obsticleY = 0;
    int obsticleWidth = 100; //z 64
    int obsticleHeight = 512; //z 512

    class Obsticle{
        int x = obsticleX;
        int y = obsticleY;
        int width = obsticleWidth;
        int height = obsticleHeight;
        Image img;
        boolean passed = false;

        Obsticle(Image img){
            this.img = img;
        }
    }

//logika gry
    Dragon dragon;
    int velocityX = -4; //predkosc przeszkod
    int velocityY = 0;
    int gravity = 1;

    ArrayList<Obsticle> obsticles;
    Random random = new Random();

    Timer gameLoop;
    Timer placeObsticlesTimer;

    boolean gameOver = false;

    double score = 0;

    JavnaGra(){
        setPreferredSize(new Dimension(boardWidth, boardHeight));
//        setBackground(Color.blue);

        setFocusable(true); // podpięcie przycisku
        addKeyListener(this);

        backgroundImg = new ImageIcon(getClass().getResource("./javnaGrabg.png")).getImage();
        dragonImg = new ImageIcon(getClass().getResource("./floppyDragon.png")).getImage();
        topObsticle = new ImageIcon(getClass().getResource("./bottomObsticleReal.png")).getImage();
        bottomObsticle = new ImageIcon(getClass().getResource("./bottomObsticleReal.png")).getImage();

        dragon = new Dragon(dragonImg);
        obsticles = new ArrayList<Obsticle>();

        placeObsticlesTimer = new Timer(1800, new ActionListener() { //zmieniłam z 1500 bo jestem noobem
            @Override
            public void actionPerformed(ActionEvent e) {
                placeObsticles();
            }
        });

        placeObsticlesTimer.start();

        gameLoop = new Timer(1000/60, this);
        gameLoop.start(); // rysuje klatki 60 na sekunde, bez tego nie zapetla
    }

    public void placeObsticles(){
        int randomObsticleY = (int)(obsticleY - obsticleHeight/4 - Math.random()*(obsticleHeight/2));
        int openingSpace = boardHeight/4;

        Obsticle topObsticle = new Obsticle(bottomObsticle);
        topObsticle.y = randomObsticleY;
        obsticles.add(topObsticle);

        Obsticle botObsticle = new Obsticle(bottomObsticle);
        botObsticle.y = topObsticle.y + openingSpace + obsticleHeight;
        botObsticle.width = 150;
        obsticles.add(botObsticle);
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){
        g.drawImage(backgroundImg, 0, 0, boardWidth, boardHeight, null);

        g.drawImage(dragon.img, dragon.x, dragon.y, dragon.width, dragon.height, null);

        for (int i = 0; i < obsticles.size(); i++){
            Obsticle obsticle = obsticles.get(i);
            g.drawImage(obsticle.img, obsticle.x, obsticle.y, obsticle.width, obsticle.height, null);
        }

        g.setColor(Color.white);
        g.setFont(new Font("Pixelated", Font.BOLD, 32));
        if (gameOver){
            g.drawString("Przegryw XD", 100, 300);
            g.drawString("punkty: " + String.valueOf((int) score), 100, 332);
        }else{
            g.drawString(String.valueOf((int)score), 20, 35);
        }
    }

    public void move(){
        velocityY += gravity;
        dragon.y += velocityY; // a więc ucieka tyle ile damy mu spacji
        dragon.y = Math.max(dragon.y, 0);  //nie bedzie uciekał poza ekran xD

        for (int i = 0; i < obsticles.size(); i++){
            Obsticle obsticle = obsticles.get(i);
            obsticle.x += velocityX; //na każdą klatkę przeszkoda będzie poruszać się o 4px (bo -4)

                if(!obsticle.passed && dragon.x > obsticle.x + obsticle.width){ //czyli jak mine prawa strone przeszkody + jej szerokosc
                    obsticle.passed = true;
                    score += 0.5;
                }

                if (collision(dragon, obsticle)){
                    gameOver = true;
                }
        }

        if(dragon.y > boardHeight){
            gameOver = true;
        }
    }

    public boolean collision(Dragon a, Obsticle b){
        return a.x < b.x + b.width-10 &&  // brzeg lewy smoka nie przechodzi za prawy brzeg przeszkody
               a.x + a.width-10 > b.x &&  // brzeg prawy smoka nie przechodzi przed lewy brzeg przeszkody
               a.y < b.y + b.height-10 &&  // brzeg górny smoka nie przechodzi za dolny brzeg przeszkody
               a.y + a.height-10 > b.y;  // brzeg dolny smoka nie przechodzi przed górny brzeg przeszkody
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if(gameOver){
            placeObsticlesTimer.stop();
            gameLoop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            velocityY = -9;
            if(gameOver){
                dragon.y = dragon.y;
                velocityY = 0;
                obsticles.clear();
                score = 0;
                gameOver = false;
                gameLoop.start();
                placeObsticlesTimer.start();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }


    @Override
    public void keyReleased(KeyEvent e) {
    }
}
