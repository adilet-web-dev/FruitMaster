import javax.swing.*;

public class MainFrame extends JFrame{

    MainPanel panel;

    MainFrame(){

        panel = new MainPanel();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(panel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);

    }
}
