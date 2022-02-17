package clientpack;

import javax.swing.*;

public class main {
    public static void main(String[] args) {
        //JOptionPane.showMessageDialog(null, "Client");
        new ClientTcp(3030).start();
    }
}
