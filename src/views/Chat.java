package views;

import stream.EchoClient;

import java.awt.*;

public class Chat {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            EchoClient model = new EchoClient(Integer.parseInt(args[0]));
            new FrameManager(model);
        });
    }
}
