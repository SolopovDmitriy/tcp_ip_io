package serverpack;

import javax.swing.*;

public class main {
    public static void main(String[] args) {
        //JOptionPane.showMessageDialog(null, "Server");
        new ServerTcp(3030).start();
    }
}
