package zones_textes_1;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class ServerView extends JFrame{

    private JPanel contentPane;
    private JPanel rootPanel ;
    private JPanel textPanel1 ;
    private JPanel textPanel2 ;
    private JLabel labelText1 ;
    private JLabel labelText2  ;

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


        textPanel1 = new JPanel() ;
        textPanel1.setLayout(new BoxLayout(textPanel1, BoxLayout.Y_AXIS));

        textPanel2 = new JPanel() ;
        textPanel2.setLayout(new BoxLayout(textPanel2, BoxLayout.Y_AXIS));


        textArea1 = new JTextArea(30,30);
        textArea1.setBorder(BorderFactory.createLineBorder(Color.BLACK)) ;
        textArea1.setEditable(false);
        textArea1.setLineWrap(true);
        textArea1.setWrapStyleWord(true);

        textArea2 = new JTextArea(30,30);
        textArea2.setBorder(BorderFactory.createLineBorder(Color.BLACK)) ;
        textArea2.setEditable(false);
        textArea2.setLineWrap(true);
        textArea2.setWrapStyleWord(true);


        labelText1 = new JLabel();
        labelText1.setText("Client 1");
        labelText1.setFont(new Font("Calibri", Font.PLAIN, 20));


        labelText2 = new JLabel();
        labelText2.setText("Client 2");
        labelText2.setFont(new Font("Calibri", Font.PLAIN, 20));



        textPanel1.add(labelText1) ;
        textPanel1.add(textArea1);

        textPanel2.add(labelText2) ;
        textPanel2.add(textArea2);

        rootPanel.add(textPanel1) ;
        rootPanel.add(textPanel2) ;
        contentPane.add(rootPanel) ;


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
        networkConfig.closeServer();
        dispose();
    }

    public static void main(String[] args) {

        ServerView app = new ServerView();
        app.pack();
        app.setVisible(true);
    }

}
