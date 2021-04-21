package views;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Bo2View extends JFrame {

    private JPanel contentPane;

    public Bo2View()
    {

        this.setTitle("BO2 App");

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);
        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);



    }


    public static void main(String[] args)
    {
        Bo2View app= new Bo2View() ;

        app.pack();
        app.setVisible(true);
    }


}
