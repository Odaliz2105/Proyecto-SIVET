package main;

import vista.Login;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Login - SIVET");
        frame.setContentPane(new Login().getBackgroundPane());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(400, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }
}