package zones_textes_1;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class ServerView extends JFrame{
    private JPanel contentPane;

    private JPanel rootPanel ;
    private JTextArea textArea1;
    private JTextArea textArea2;


    private ServerNetworkConfig networkConfig ;




    public void updateText(int fieldNumber,String message )
    {
        if (fieldNumber ==1 )
        {
            textArea1.setText(message);
        }
        else
        {
            textArea2.setText(message);
        }
    }
    public ServerView() {

        this.setTitle("Server");
        this.networkConfig = new ServerNetworkConfig(this) ;

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        rootPanel = new JPanel() ;
        rootPanel.setLayout(new FlowLayout());
        textArea1 = new JTextArea(10,50);
        textArea1.setEditable(false);

        textArea2 = new JTextArea(10,50);
        textArea2.setEditable(false);

        rootPanel.add(textArea1);
        rootPanel.add(textArea2);

        contentPane.add(rootPanel) ;


        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {

        ServerView app = new ServerView();
        app.pack();
        app.setVisible(true);
    }

}
