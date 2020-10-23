package views;

import stream.EchoClient;
import stream.ReadThread;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.net.Socket;

public class FrameManager extends JFrame {

    public EchoClient model;
    private JTextArea display;
    private JFrame frame;

    public FrameManager(EchoClient model) {
        this.model = model;
        createChatWindow();

        Socket socket = model.getEchoSocket();

        ReadThread readThread = new ReadThread(socket, this);
        readThread.start();
    }

    private void createChatWindow() {
        // Creating container for middle panel and message panel
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        // Create middle panel
        JPanel middlePanel = new JPanel();
        middlePanel.setBorder(new TitledBorder(new EtchedBorder(), "Chat"));

        // create the middle panel components
        display = new JTextArea(30,70);
        display.setEditable(false);
        JScrollPane scroll = new JScrollPane(display);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        // Create message panel
        JPanel messagePanel = new JPanel();

        // Create message text field
        JTextField message = new JTextField(50);

        // Create send message button
        JButton sendMessageButton = new JButton("SEND");
        sendMessageButton.setForeground(Color.BLACK);
        sendMessageButton.setFont(new Font("Consolas", Font.BOLD, 15));
        sendMessageButton.setBackground(Color.WHITE);
        sendMessageButton.addActionListener(event -> {
            sendMessage(message.getText());
            message.setText("");
        });

        // Add scroll to middle panel
        middlePanel.add(scroll);

        // Add text area and button to message panel
        messagePanel.add(message);
        messagePanel.add(sendMessageButton);

        // Add panels to container and this to our frame
        frame = new JFrame();
        container.add(middlePanel);
        container.add(messagePanel);
        frame.add(container);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Send message when press enter
        frame.getRootPane().setDefaultButton(sendMessageButton);
    }


    private void sendMessage(String message) {
        if (message.length() > 0) {
            //display.append(message + "\n");
            display.setCaretPosition(display.getDocument().getLength());
            model.sendMessage(message);
        }
    }

    public void readReceived(String message) {
        if (message.length() > 0) {
            display.append(message + "\n");
            display.setCaretPosition(display.getDocument().getLength());
        }
    }

    public void setClientName(String name) {
        frame.setTitle(name);
    }

}
