import javax.swing.*;
import java.awt.*;

class LoadFruits{

    Image appleFull = new ImageIcon(getClass().getResource("/img/apple.png")).getImage();
    Image appleSlice1 = new ImageIcon(getClass().getResource("/img/apple-slice1.png")).getImage();
    Image appleSlice2 = new ImageIcon(getClass().getResource("/img/apple-slice2.png")).getImage();

    Image bananaFull = new ImageIcon(getClass().getResource("/img/banana.png")).getImage();
    Image bananaSlice1 = new ImageIcon(getClass().getResource("/img/banana-slice1.png")).getImage();
    Image bananaSlice2 = new ImageIcon(getClass().getResource("/img/banana-slice2.png")).getImage();

    Image pearFull = new ImageIcon(getClass().getResource("/img/pear.png")).getImage();
    Image pearSlice1 = new ImageIcon(getClass().getResource("/img/pear-slice1.png")).getImage();
    Image pearSlice2 = new ImageIcon(getClass().getResource("/img/pear-slice2.png")).getImage();

    Image watermelonFull = new ImageIcon(getClass().getResource("/img/watermelon.png")).getImage();
    Image watermelonSlice1 = new ImageIcon(getClass().getResource("/img/watermelon-slice1.png")).getImage();
    Image watermelonSlice2 = new ImageIcon(getClass().getResource("/img/watermelon-slice2.png")).getImage();

    Image goldenApple = new ImageIcon(getClass().getResource("/img/golden-apple.png")).getImage();
    Image goldenAppleSlice = new ImageIcon(getClass().getResource("/img/golden-apple-slice.png")).getImage();

    public Fruits getFruit(){
        int rand = (int) (Math.random() * 5);
        if (rand == 0){
            return new Fruits(appleFull, appleSlice1, appleSlice2, "apple");
        }
        if (rand == 1){
            return new Fruits(bananaFull, bananaSlice1, bananaSlice2, "banana");
        }
        if (rand == 2){
            return new Fruits(pearFull, pearSlice1, pearSlice2, "pear");
        }
        if (rand == 3){
            return new Fruits(watermelonFull, watermelonSlice1, watermelonSlice2, "watermelon");
        }
        if (rand == 4){
            int goldRandom = (int) (Math.random() * 5);
            if (goldRandom == 2){
                return new Fruits(goldenApple, goldenAppleSlice, goldenAppleSlice, "golden apple");
            }
            else {
                return getFruit();
            }

        }

        return null;
    }


}

class Fruits {

    public int x = 0;
    public int y = -100;
    public Image fruit;
    public Image slice1;
    public Image slice2;
    public String name;
    public Image firstFruit;

    public Fruits(Image fruit, Image fruitSlice1, Image fruitSlice2, String name){
        this.name = name;
        this.fruit = fruit;
        this.slice1 = fruitSlice1;
        this.slice2 = fruitSlice2;
        this.firstFruit = fruit;
    }

    public Fruits(Image fruit, Image fruitSlice1, Image fruitSlice2, int x, int y){

        this.fruit = fruit;
        this.slice1 = fruitSlice1;
        this.slice2 = fruitSlice2;
        this.x = x;
        this.y = y;
    }
    public void setFruit(){

        if (this.fruit == this.firstFruit){
            this.fruit = this.slice1;
        }
        else{
            this.fruit = this.slice2;
        }

    }
    public void setX(int x){
        this.x = x;
    }
    public void setY(int y){
        this.y = y;
    }

    public String toString(){

        return String.valueOf(this.y);

    }

}
