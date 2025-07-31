package modelo;

import vista.MenuAdmin;
import vista.MenuAsistente;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Login extends JFrame {
    private JPanel backgroundPane;
    private static ArrayList<Usuario> usuarios = new ArrayList<>();

    public Login() {
        // Usuarios por defecto
        if (usuarios.isEmpty()) {
            usuarios.add(new Usuario("Administrador", "admin123"));
            usuarios.add(new Usuario("Asistente Veterinario", "asistente123"));
        }

        backgroundPane = new JPanel(new BorderLayout());

        JPanel contenido = new JPanel(new GridLayout(5, 2, 10, 10));
        contenido.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titulo = new JLabel("SIVET - Iniciar Sesión", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));

        JLabel lblUsuario = new JLabel("Tipo de Usuario:");
        JComboBox<String> comboUsuario = new JComboBox<>(new String[]{
                "Seleccionar rol", "Administrador", "Asistente Veterinario"
        });

        JLabel lblPassword = new JLabel("Contraseña:");
        JPasswordField passwordField = new JPasswordField();

        JButton btnLogin = new JButton("Iniciar Sesión");
        JButton btnLimpiar = new JButton("Limpiar");
        JButton btnCrearUsuario = new JButton("Crear Usuario");

        // Agregar componentes
        backgroundPane.add(titulo, BorderLayout.NORTH);
        contenido.add(lblUsuario);
        contenido.add(comboUsuario);
        contenido.add(lblPassword);
        contenido.add(passwordField);
        contenido.add(btnLogin);
        contenido.add(btnLimpiar);
        contenido.add(new JLabel());  // vacío para alinear
        contenido.add(btnCrearUsuario);
        backgroundPane.add(contenido, BorderLayout.CENTER);

        // Eventos
        btnLogin.addActionListener(e -> {
            String rol = (String) comboUsuario.getSelectedItem();
            String password = new String(passwordField.getPassword());

            if (rol == null || rol.equals("Seleccionar rol")) {
                JOptionPane.showMessageDialog(this, "Seleccione un tipo de usuario.");
                return;
            }

            if (password.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingrese su contraseña.");
                return;
            }

            boolean autenticado = false;

            for (Usuario u : usuarios) {
                if (u.getRol().equals(rol) && u.getContraseña().equals(password)) {
                    autenticado = true;
                    this.dispose();
                    if (rol.equals("Administrador")) {
                        new MenuAdmin();
                        JOptionPane.showMessageDialog(null, "¡Bienvenido Administrador!");
                    } else {
                        new MenuAsistente();
                        JOptionPane.showMessageDialog(null, "¡Bienvenido Asistente!");
                    }
                    break;
                }
            }

            if (!autenticado) {
                JOptionPane.showMessageDialog(this,
                        "Credenciales incorrectas.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                passwordField.setText("");
            }
        });

        btnLimpiar.addActionListener(e -> {
            comboUsuario.setSelectedIndex(0);
            passwordField.setText("");
        });

        btnCrearUsuario.addActionListener(e -> {
            String nuevoRol = (String) comboUsuario.getSelectedItem();
            String nuevaClave = new String(passwordField.getPassword());

            if (nuevoRol.equals("Seleccionar rol")) {
                JOptionPane.showMessageDialog(this, "Seleccione un rol válido.");
                return;
            }

            if (nuevaClave.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingrese una contraseña.");
                return;
            }

            // Verifica si ya existe
            for (Usuario u : usuarios) {
                if (u.getRol().equals(nuevoRol)) {
                    JOptionPane.showMessageDialog(this,
                            "Ya existe un usuario con ese rol.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            usuarios.add(new Usuario(nuevoRol, nuevaClave));
            JOptionPane.showMessageDialog(this, "Usuario creado correctamente.");
            passwordField.setText("");
        });
    }

    public JPanel getBackgroundPane() {
        return backgroundPane;
    }

    // Clase interna Usuario
    private static class Usuario {
        private String rol;
        private String contraseña;

        public Usuario(String rol, String contraseña) {
            this.rol = rol;
            this.contraseña = contraseña;
        }

        public String getRol() {
            return rol;
        }

        public String getContraseña() {
            return contraseña;
        }
    }
}
