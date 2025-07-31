package modelo;

import vista.MenuAdmin;
import vista.MenuAsistente;

import javax.swing.*;

public class Login extends JFrame {
    private JPanel backgroundPane;

    public Login() {
        // Crear el panel principal
        backgroundPane = new JPanel();
        backgroundPane.setLayout(new java.awt.BorderLayout());

        // Crear contenido del login
        JPanel contenido = new JPanel(new java.awt.GridLayout(4, 2, 10, 10));
        contenido.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titulo = new JLabel("SIVET - Iniciar Sesión", JLabel.CENTER);
        titulo.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 18));

        JLabel lblUsuario = new JLabel("Tipo de Usuario:");
        JComboBox<String> comboUsuario = new JComboBox<>(new String[]{
                "Seleccionar rol", "Administrador", "Asistente Veterinario"
        });

        JLabel lblPassword = new JLabel("Contraseña:");
        JPasswordField passwordField = new JPasswordField();

        JButton btnLogin = new JButton("Iniciar Sesión");
        JButton btnLimpiar = new JButton("Limpiar");

        // Agregar componentes
        backgroundPane.add(titulo, java.awt.BorderLayout.NORTH);
        contenido.add(lblUsuario);
        contenido.add(comboUsuario);
        contenido.add(lblPassword);
        contenido.add(passwordField);
        contenido.add(btnLogin);
        contenido.add(btnLimpiar);
        backgroundPane.add(contenido, java.awt.BorderLayout.CENTER);

        // Eventos
        btnLogin.addActionListener(e -> {
            String usuario = (String) comboUsuario.getSelectedItem();
            String password = new String(passwordField.getPassword());

            if (usuario == null || usuario.equals("Seleccionar rol")) {
                JOptionPane.showMessageDialog(this, "Seleccione un tipo de usuario.");
                return;
            }

            if (password.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingrese su contraseña.");
                return;
            }

            // Validar credenciales
            boolean credencialesCorrectas = false;

            if (usuario.equals("Administrador") && password.equals("admin123")) {
                credencialesCorrectas = true;
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
                if (frame != null) frame.dispose();
                new MenuAdmin();
                JOptionPane.showMessageDialog(null, "¡Bienvenido Administrador!");

            } else if (usuario.equals("Asistente Veterinario") && password.equals("asistente123")) {
                credencialesCorrectas = true;
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
                if (frame != null) frame.dispose();
                new MenuAsistente();
                JOptionPane.showMessageDialog(null, "¡Bienvenido Asistente Veterinario!");
            }

            if (!credencialesCorrectas) {
                JOptionPane.showMessageDialog(this,
                        "Credenciales incorrectas.\n\n" +
                                "Credenciales de prueba:\n" +
                                "Administrador: admin123\n" +
                                "Asistente Veterinario: asistente123");
                passwordField.setText("");
            }
        });

        btnLimpiar.addActionListener(e -> {
            comboUsuario.setSelectedIndex(0);
            passwordField.setText("");
        });
    }

    public JPanel getBackgroundPane() {
        return backgroundPane;
    }
}