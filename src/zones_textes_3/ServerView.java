package zones_textes_3;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

public class ServerView extends JFrame {

    private JPanel contentPane;
    private JPanel rootPanel;


    private ServerNetworkConfig networkConfig;

    Map<String, JTextArea> textAreaMap = new HashMap<String, JTextArea>();

    String calculateNewText(String oldText, MessageObject messageObject) {

        String message = oldText;
        int offset = messageObject.getOffset();

        if (messageObject.getOperationType() == 1) // insertion
        {
            String addedMessage = messageObject.getMessage();
            try {
                String messagePart1 = (offset != 0) ? message.substring(0, offset) : "";
                System.out.println("part1 : " + messagePart1);
                System.out.println("added message : " + addedMessage);
                String messagePart2 = (message.length() != 0 && offset != message.length()) ? message.substring(offset, message.length()) : "";
                System.out.println("part2 : " + messagePart2);
                message = messagePart1 + addedMessage + messagePart2;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else { // deletion
            int deleteLength = messageObject.getLength();
            String messagePart1 = (offset != 0) ? message.substring(0, offset) : "";
            String messagePart2 = message.substring(offset + deleteLength, message.length());
            message = messagePart1 + messagePart2;
        }
        return message;
    }

    public void updateText(String queueName, byte[] array) {
        MessageObject messageObject = MessageOperations.deserialize(array);
        textAreaMap.get(queueName).setText(calculateNewText(textAreaMap.get(queueName).getText(), messageObject));
    }


    public void addNewClientView(String queueName) {

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

        JTextArea textArea = new JTextArea(20, 20);
        textArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JLabel label = new JLabel();
        label.setText(queueName);
        label.setFont(new Font("Calibri", Font.PLAIN, 20));
        textPanel.add(label);
        textPanel.add(textArea);
        rootPanel.add(textPanel);


        textAreaMap.put(queueName, textArea);//add panel to Hashmap

        rootPanel.validate();// apply changes to the main panel
        rootPanel.repaint();
    }

    public ServerView() {

        this.setTitle("Server");
        this.networkConfig = new ServerNetworkConfig(this);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        rootPanel = new JPanel();
        rootPanel.setLayout(new FlowLayout());
        contentPane.add(rootPanel);


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
