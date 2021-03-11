package zones_textes_1;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;

public class ClientView extends JFrame {


    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;


    private JTextArea textArea;
    DocumentListener textAreaListener = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            onTextChange();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            onTextChange();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
        }
    };


    private ClientNetworkConfig networkConfig;



    // each setText() will trigger this event , we added a flag to disable it when we update via network
    private void onTextChange() {
        this.sendText(textArea.getText());
    }



    private void sendText(String newText) {
        System.out.println("'" + newText + "' Sent via the Client View");
        networkConfig.publishMessage(newText);

    }

    public ClientView(String queueName) {
        this.setTitle("Client");

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        textArea =new JTextArea(10,50);
        contentPane.add(textArea);

        // network configuration
        this.networkConfig = new ClientNetworkConfig(queueName,this);
        networkConfig.initConnection();
        // end network configuration

        textArea.getDocument().addDocumentListener(textAreaListener);


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

        String queueName = (args.length != 0) ? args[0] : "incoming-text1";
        ClientView app = new ClientView(queueName);
        app.pack();
        app.setVisible(true);
    }
}
