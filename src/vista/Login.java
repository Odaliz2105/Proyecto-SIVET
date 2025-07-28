package vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class Login {
    private JPanel backgroundPane;
    private JPanel panelLogin;
    private JPasswordField passwordField1;
    private JButton iniciarSesionButton;
    private JComboBox<String> comboBoxUsuario;
    private JLabel icono;

    // Constructor
    public Login() {
        // Estilos y colores
        backgroundPane.setBackground(new Color(240, 248, 255)); // azul claro
        panelLogin.setBackground(new Color(255, 255, 255)); // blanco
        panelLogin.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        iniciarSesionButton.setBackground(new Color(0, 123, 255));
        iniciarSesionButton.setForeground(Color.WHITE);
        iniciarSesionButton.setFocusPainted(false);
        iniciarSesionButton.setFont(new Font("Arial", Font.BOLD, 14));

        comboBoxUsuario.removeAllItems();
        comboBoxUsuario.addItem("Administrador");
        comboBoxUsuario.addItem("Asistente Veterinario");

        // Imagen redonda
        ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/imagenes/login.png")));


        // Acción del botón
        iniciarSesionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iniciarSesion();
            }
        });
    }

    private void iniciarSesion() {
        String usuario = comboBoxUsuario.getSelectedItem().toString();
        String contraseña = new String(passwordField1.getPassword());

        if (usuario.equals("Administrador") && contraseña.equals("admin123")) {
            JOptionPane.showMessageDialog(null, "¡Bienvenido, Administrador!");
            // Lógica para abrir panel de administrador
        } else if (usuario.equals("Asistente Veterinario") && contraseña.equals("veterinario123")) {
            JOptionPane.showMessageDialog(null, "¡Bienvenido, Asistente Veterinario!");
            // Lógica para abrir panel veterinario
        } else {
            JOptionPane.showMessageDialog(null, "Credenciales incorrectas", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public JPanel getBackgroundPane() {
        return backgroundPane;
    }

    // Método para hacer imagen redonda
    private Image makeRoundedImage(Image image) {
        int size = Math.min(image.getWidth(null), image.getHeight(null));
        BufferedImage rounded = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = rounded.createGraphics();
        g2.setClip(new java.awt.geom.Ellipse2D.Float(0, 0, size, size));
        g2.drawImage(image, 0, 0, size, size, null);
        g2.dispose();
        return rounded;
    }


}
