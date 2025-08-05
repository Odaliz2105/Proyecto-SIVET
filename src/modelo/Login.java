/**

 * Clase modelo que representa la información de login y sesión.
        * Gestiona las credenciales de acceso al sistema.
 *
         * @author Odaliz Aracely
 * @version 1.0
        * @since 2024
        */

import dao.UsuarioDAO;
import vista.MenuAdmin;
import vista.MenuAsistente;
import javax.swing.*;
import java.awt.*;
import org.bson.Document;

public class Login extends JFrame {
    private JPanel backgroundPane;

    public Login() {
        UsuarioDAO usuarioDAO = new UsuarioDAO();

        backgroundPane = new JPanel(new BorderLayout());

        JPanel contenido = new JPanel(new GridLayout(5, 2, 10, 10));
        contenido.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titulo = new JLabel("SIVET - Iniciar Sesión", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));

        JLabel lblUsuario = new JLabel("Usuario:");
        JTextField campoUsuario = new JTextField();

        JLabel lblPassword = new JLabel("Contraseña:");
        JPasswordField campoPassword = new JPasswordField();

        JButton btnLogin = new JButton("Iniciar Sesión");
        JButton btnLimpiar = new JButton("Limpiar");

        backgroundPane.add(titulo, BorderLayout.NORTH);
        contenido.add(lblUsuario);
        contenido.add(campoUsuario);
        contenido.add(lblPassword);
        contenido.add(campoPassword);
        contenido.add(btnLogin);
        contenido.add(btnLimpiar);
        backgroundPane.add(contenido, BorderLayout.CENTER);

        setContentPane(backgroundPane);
        setTitle("Login");
        setSize(400, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        // Eventos
        btnLogin.addActionListener(e -> {
            String usuario = campoUsuario.getText().trim();
            String contraseña = new String(campoPassword.getPassword()).trim();

            if (usuario.isEmpty() || contraseña.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete todos los campos.");
                return;
            }

            if (usuarioDAO.autenticar(usuario, contraseña)) {
                Document usuarioDoc = usuarioDAO.obtenerUsuarioPorUsuario(usuario);
                if (usuarioDoc != null) {
                    String rol = usuarioDoc.getString("rol");
                    this.dispose();
                    if ("administrador".equalsIgnoreCase(rol)) {
                        new MenuAdmin();
                        JOptionPane.showMessageDialog(null, "¡Bienvenido Administrador!");
                    } else if ("asistente".equalsIgnoreCase(rol)) {
                        new MenuAsistente();
                        JOptionPane.showMessageDialog(null, "¡Bienvenido Asistente!");
                    } else {
                        JOptionPane.showMessageDialog(this, "Rol no reconocido.");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "No se encontró el usuario en la base de datos.");
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "Credenciales incorrectas.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnLimpiar.addActionListener(e -> {
            campoUsuario.setText("");
            campoPassword.setText("");
        });
    }
}
