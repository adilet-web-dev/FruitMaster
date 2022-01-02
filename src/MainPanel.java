import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class MainPanel extends JPanel implements ActionListener{
    //DEFAULT SIZE
    final static int WIDTH = 1000;
    final static int HEIGHT = 600;
    //chef can't go further
    final static int LEFT_BORDER = 220;
    final static int RIGHT_BORDER = 680;
    //images
    private final Image background;
    private final Image chefRight;
    private final Image chefLeft;
    private final Image chefHitting1Right;
    private final Image chefHitting2Right;
    private final Image chefHitting1Left;
    private final Image chefHitting2Left;
    private final Image bridge;
    private final Image glass;
    private final Image glassBroke1;
    private final Image glassBroke2;
    private final Image desk;
    private final Image start;

    private ArrayList<Image> food = new ArrayList<>();
    Cursor cursor = new Cursor(Cursor.HAND_CURSOR);

    Timer timer;
    //chef position
    static int x = 500;
    static int y = 400;

    Random random = new Random();

    static int SPEED = 17;
    static int fallingSpeed = 3;
    static int fruit_delay = 60;

    static int queue = 0;
    static int hitting = 0;
    static boolean is_hitting = false;
    static int glassTenacity = 3;
    static int score = 0;

    static int delay = 10;
    static boolean running = false;

    //moving direction
    enum Direction{ right, left, stop }
    static Direction dir;
    static Direction chefSide;

    LoadFruits fruitTypes = new LoadFruits();
    ArrayList<Fruits> fallingFruits = new ArrayList<>();

    MainPanel(){

        //set size
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MykeyAdapter());

        //init images
        chefRight = new ImageIcon(getClass().getResource("img/chef.png")).getImage();
        chefLeft = new ImageIcon(getClass().getResource("img/chef_left.png")).getImage();

        chefHitting1Right = new ImageIcon(getClass().getResource("img/chef-hitting1.png")).getImage();
        chefHitting2Right = new ImageIcon(getClass().getResource("img/chef-hitting2.png")).getImage();
        chefHitting1Left = new ImageIcon(getClass().getResource("img/chef-hitting1_left.png")).getImage();
        chefHitting2Left = new ImageIcon(getClass().getResource("img/chef-hitting2_left.png")).getImage();

        background = new ImageIcon(getClass().getResource("img/background.png")).getImage();
        bridge = new ImageIcon(getClass().getResource("/img/bridge.png")).getImage();

        glass = new ImageIcon(getClass().getResource("/img/glass.png")).getImage();
        glassBroke1 = new ImageIcon(getClass().getResource("/img/glass-broke1.png")).getImage();
        glassBroke2 = new ImageIcon(getClass().getResource("/img/glass-broke2.png")).getImage();

        desk = new ImageIcon(getClass().getResource("/img/desk.png")).getImage();
        start = new ImageIcon(getClass().getResource("/img/start.png")).getImage();

        for (int i = 0; i < 3; i++){
            food.add(new ImageIcon(getClass().getResource("/img/food" + (i+1) + ".png")).getImage());
        }

        running = true;
        //moving direction
        dir = Direction.stop;
        chefSide = Direction.right;

        startGame();

    }

    public void startGame(){
        running = true;
        timer = new Timer(7, this);
        timer.start();

    }

    public void paint(Graphics g){

        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g.setFont(new Font("Silkscreen", Font.PLAIN, 30));

        g2d.drawImage(background, 0, 0, null);
        g2d.drawImage(bridge, 250, 500, null);

        paintStatistics(g2d);
        paintFruits(g2d);
        paintMixingFood(g2d);
        paintGlass(g2d);
        animateChef(g2d);

        if (glassTenacity == 0){
            paintEndResults(g2d);
            stopTheGame();
        }

    }

    public void paintStatistics(Graphics g){
        g.drawString("Score: " + score, 50, 100);
        g.drawString("Glass: " + glassTenacity, 50, 150);
    }

    public void paintFruits(Graphics2D g2d){
        for (Fruits i: fallingFruits){
            g2d.drawImage(i.fruit, i.x, i.y, null);
        }
    }

    public void paintMixingFood(Graphics2D g2d){
        queue++;
        if (queue >= 0 && queue < 20){
            g2d.drawImage(food.get(0), 250, 510, null);
        }
        else if (queue >= 20 && queue < 40){
            g2d.drawImage(food.get(1), 250, 510, null);
        }
        else if (queue >= 40 && queue < 60){
            g2d.drawImage(food.get(2), 250, 510, null);
            queue = 1;
        }

    }

    public void paintGlass(Graphics2D g2d){
        switch (glassTenacity){
            case 3: g2d.drawImage(glass, 250, 450, null); break;
            case 2: g2d.drawImage(glassBroke1, 250, 450, null); break;
            case 1: g2d.drawImage(glassBroke2, 250, 450, null); break;
        }
    }

    public void animateChef(Graphics2D g2d){
        if (hitting >= 0){hitting--;}


        if (hitting < 20 && hitting > 13){

            switch (chefSide) {
                case right: paintChef(g2d, chefHitting1Right); break;
                case left: paintChef(g2d, chefHitting1Left); break;
            }

        }
        if (hitting <= 13  && hitting > 0){

            switch (chefSide) {
                case right: paintChef(g2d, chefHitting2Right); break;
                case left: paintChef(g2d, chefHitting2Left); break;
            }

        }

        if (hitting == 0) is_hitting = false;

        if (!(is_hitting)){
            switch (chefSide) {
                case right: paintChef(g2d, chefRight); break;
                case left: paintChef(g2d, chefLeft); break;
            }
        }
    }

    protected void paintChef(Graphics2D g2d, Image image){
        g2d.drawImage(image, x, y, null);
    }

    public void paintEndResults(Graphics2D g2d){
        g2d.drawImage(glassBroke2, 250, 450, null);
        g2d.drawImage(desk, 50, 35, null);
        g2d.drawString("Score: " + score, 400, 250);
        g2d.drawString("Press Enter to start", 280, 300);

    }

    public void stopTheGame(){
        running = false;
    }

    public void move(){
        moveChef();
        moveFruits();
    }

    public void moveChef(){
        if (dir == Direction.right){
            if (x + SPEED < RIGHT_BORDER){
                x += SPEED; dir = Direction.stop;
            }
        }
        else if (dir == Direction.left){
            if (x - SPEED > LEFT_BORDER){
                x -= SPEED; dir = Direction.stop;
            }
        }
    }

    public void moveFruits(){
        for (int i = 0; i < fallingFruits.size(); i++){
            Fruits fruit = fallingFruits.get(i);
            fruit.setY(fruit.y + fallingSpeed);
            fallingFruits.set(i, fruit);

            if (fruit.y > 500){
                handleFallenFruits(fruit);
                fallingFruits.remove(i);
            }

        }
    }

    public void createFruits(){

        fruit_delay--;

        if (fruit_delay < 0){

            int x_random = (int) (Math.random() * 400);
            int amount = (int) (Math.random()*5);

            if (amount == 4){
                int lag = 10;
                for (int i = 0; i < 3; i++){
                    Fruits currentAfter = fruitTypes.getFruit(); //add random fruit
                    currentAfter.setY(-100-lag);
                    currentAfter.setX(x_random + 250);
                    fallingFruits.add(currentAfter);
                    lag = lag - 15;
                }
            }

            else{
                Fruits current = fruitTypes.getFruit(); //add random fruit
                current.setY(-100);
                current.setX(x_random + 250);
                fallingFruits.add(current);
            }

            setDifficulty();
        }

    }

    public void setDifficulty(){
        if (score < 100){
            fruit_delay = 60;
        }
        if (score >= 100 && score < 200){
            fruit_delay = 50;
        }
        if (score >= 200){
            fruit_delay = 40;
        }
    }

    public void slice() {

        for (int i = 0; i < fallingFruits.size(); i++) {
            Fruits fruit = fallingFruits.get(i);
            if (Math.abs(fruit.x - x) < 50)  //if food is near the chef on x axis
            {
                if (fruit.y > 310 && fruit.y < 350) {

                    if (fruit.name == "golden apple") {
                        sliceAll();
                    }
                    fruit.setFruit();
                    fallingFruits.set(i, fruit);

                }
                if (fruit.y >= 350 && fruit.y < 500) {
                    if (fruit.name == "golden apple") {
                        sliceAll();
                    }
                    fruit.setFruit();
                    fruit.setFruit();
                    fallingFruits.set(i, fruit);
                }

            }

        }
    }
    public void sliceAll(){
        for (int i = 0; i < fallingFruits.size(); i++) {
            Fruits fruit = fallingFruits.get(i);
            fruit.setFruit();
            fruit.setFruit();
            fallingFruits.set(i, fruit);
        }
    }
    public void handleFallenFruits(Fruits fruit){

        if (fruit.fruit == fruit.firstFruit){
            glassTenacity--;
        }
        else {
            if (fruit.fruit == fruit.slice1){
                score++;
            }
            else if (fruit.fruit == fruit.slice2){
               score += 2;
            }
        }

    }


    @Override
    public void actionPerformed(ActionEvent e) {

        if (running){
            move();
            repaint();
            createFruits();
        }

    }

    public class MykeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()){
                case KeyEvent.VK_RIGHT: {
                    dir = Direction.right;
                    chefSide = Direction.right;
                    break;
                }

                case KeyEvent.VK_LEFT: {
                    dir = Direction.left;
                    chefSide = Direction.left;
                    break;
                }
                case KeyEvent.VK_SPACE: {
                    hitting = 20;
                    is_hitting = true;
                    slice();
                    break;
                }
                case KeyEvent.VK_ENTER: {
                    fallingFruits.clear();
                    score = 0;
                    running = true;
                    glassTenacity = 3;
                    break;
                }
            }

        }
    }
}
