package part2v2;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class ServerView extends JFrame{
    private JPanel contentPane;

    private JPanel rootPanel ;
    private JTextArea textArea1;
    private JTextArea textArea2;

    public ServerView(String queueName) {

        //this.networkConfig = new ClientNetworkConfig(queueName) ;
        //networkConfig.initConnection();

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        rootPanel = new JPanel() ;

        textArea1 = new JTextArea(10,50);
        textArea2 = new JTextArea(10,50);
/*        textArea1.setBounds(100, 100, 450, 560);
        textArea2.setBounds(100, 100, 450, 560);*/
        rootPanel.add(textArea1);
        rootPanel.add(textArea2);


        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
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

        String queueName = (args.length != 0)?args[0]:"incoming-text1" ;
        ClientView app = new ClientView(queueName);
        //app.pack();
        app.setVisible(true);
    }

}
