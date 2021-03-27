package zones_textes_2;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
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
            onTextChange(e, 1);
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            onTextChange(e, -1);
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
        }
    };


    private ClientNetworkConfig networkConfig;


    // each setText() will trigger this event
    private void onTextChange(DocumentEvent e, int modif) {


        String message = "";
        int offset = e.getOffset();
        int modifLength = e.getLength();

        if (modif == 1) {
            try {
                message = textArea.getText(e.getOffset(), e.getLength());
            } catch (BadLocationException badLocationException) {
                badLocationException.printStackTrace();
            }
        }

        MessageObject messageObject = new MessageObject(message,modifLength,offset, modif);

        this.sendMessageObject(messageObject);
    }


    private void sendMessageObject(MessageObject messageObject) {

        networkConfig.publishMessage(MessageOperations.getByteArray(messageObject));

    }

    public ClientView(String queueName) {
        this.setTitle("Client");

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        textArea = new JTextArea(10, 50);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        contentPane.add(textArea);

        // network configuration
        this.networkConfig = new ClientNetworkConfig(queueName, this);
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
        networkConfig.closeClient();
        dispose();
    }

    public static void main(String[] args) {

        String queueName = (args.length != 0) ? args[0] : "incoming-text1";
        ClientView app = new ClientView(queueName);
        app.pack();
        app.setVisible(true);
    }
}
