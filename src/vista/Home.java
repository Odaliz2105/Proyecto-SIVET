package vista;

import modelo.Cita;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import com.toedter.calendar.JDateChooser;

public class Home extends JFrame {
    private JPanel panelHome;
    private JTabbedPane tabbedPane1;
    private JPanel panelAgendar;
    private JLabel iconoAgendarCita;
    private JTextField textPropietario;
    private JTextField textMascota;
    private JComboBox<String> comboBoxHora;
    private JTextField textMotivo;
    private JButton agendarCitaButton;
    private JPanel panelLogin;
    private JButton iniciarSesionButton;
    private JComboBox<String> comboBoxUsuario;
    private JPasswordField contraseña;
    private JButton limpiarButton;
    private JPanel PanelSecundario;
    private JPanel panelContacto;
    private JPanel panelServicios;
    private JPanel PanelPie;
    private JDateChooser dateChooser; // Para el calendario (JCalendar library)

    // Lista estática para almacenar citas (temporal, después será BD)
    public static ArrayList<Cita> listaCitas = new ArrayList<>();

    public Home() {
        setTitle("VETERINARIA SIVET - Sistema de Gestión");
        setContentPane(panelHome);
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        inicializarComponentes();
        configurarEventos();

        setVisible(true);
    }

    private void inicializarComponentes() {
        // Configurar combo de horas
        String[] horas = {
                "Seleccionar hora", "08:00", "08:30", "09:00", "09:30", "10:00", "10:30",
                "11:00", "11:30", "12:00", "12:30", "14:00", "14:30",
                "15:00", "15:30", "16:00", "16:30", "17:00", "17:30"
        };
        comboBoxHora.setModel(new DefaultComboBoxModel<>(horas));

        // Configurar combo de usuarios para login
        String[] usuarios = {"Seleccionar rol", "Administrador", "Asistente Veterinario"};
        comboBoxUsuario.setModel(new DefaultComboBoxModel<>(usuarios));

        // Si tienes el JDateChooser en el form, puedes inicializarlo
        if (dateChooser != null) {
            dateChooser.setDate(new Date()); // Fecha actual por defecto
        }
    }

    private void configurarEventos() {
        // Evento para agendar cita
        agendarCitaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agendarCita();
            }
        });

        // Evento para limpiar campos de cita
        limpiarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarCamposCita();
            }
        });

        // Evento para iniciar sesión
        iniciarSesionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iniciarSesion();
            }
        });
    }

    private void agendarCita() {
        // Validar campos
        if (validarCamposCita()) {
            try {
                String propietario = textPropietario.getText().trim();
                String mascota = textMascota.getText().trim();
                String hora = (String) comboBoxHora.getSelectedItem();
                String motivo = textMotivo.getText().trim();

                // Obtener fecha del calendario
                Date fecha;
                if (dateChooser != null) {
                    fecha = dateChooser.getDate();
                    if (fecha == null) {
                        JOptionPane.showMessageDialog(this, "Por favor seleccione una fecha.",
                                "Campo requerido", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                } else {
                    fecha = new Date(); // Fecha actual si no hay dateChooser
                }

                // Crear nueva cita
                Cita nuevaCita = new Cita(propietario, mascota, fecha, hora, motivo);

                // Agregar a la lista
                listaCitas.add(nuevaCita);

                // Mostrar mensaje de confirmación
                JOptionPane.showMessageDialog(this,
                        "¡CITA AGENDADA EXITOSAMENTE!\n\n" +
                                "Propietario: " + propietario + "\n" +
                                "Mascota: " + mascota + "\n" +
                                "Fecha: " + nuevaCita.getFechaFormateada() + "\n" +
                                "Hora: " + hora + "\n" +
                                "Motivo: " + motivo,
                        "Cita Agendada",
                        JOptionPane.INFORMATION_MESSAGE);

                // Limpiar campos después de agendar
                limpiarCamposCita();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Error al agendar la cita: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean validarCamposCita() {
        if (textPropietario.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese el nombre del propietario.",
                    "Campo requerido", JOptionPane.WARNING_MESSAGE);
            textPropietario.requestFocus();
            return false;
        }

        if (textMascota.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese el nombre de la mascota.",
                    "Campo requerido", JOptionPane.WARNING_MESSAGE);
            textMascota.requestFocus();
            return false;
        }

        if (comboBoxHora.getSelectedIndex() == 0 || comboBoxHora.getSelectedItem().equals("Seleccionar hora")) {
            JOptionPane.showMessageDialog(this, "Por favor seleccione una hora.",
                    "Campo requerido", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (textMotivo.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese el motivo de la consulta.",
                    "Campo requerido", JOptionPane.WARNING_MESSAGE);
            textMotivo.requestFocus();
            return false;
        }

        return true;
    }

    private void limpiarCamposCita() {
        textPropietario.setText("");
        textMascota.setText("");
        textMotivo.setText("");
        comboBoxHora.setSelectedIndex(0);
        if (dateChooser != null) {
            dateChooser.setDate(new Date()); // Resetear a fecha actual
        }
    }

    private void iniciarSesion() {
        String usuario = (String) comboBoxUsuario.getSelectedItem();
        String password = new String(contraseña.getPassword());

        if (validarCamposLogin(usuario, password)) {
            // Credenciales específicas
            boolean credencialesCorrectas = false;

            if (usuario.equals("Administrador") && password.equals("admin123")) {
                credencialesCorrectas = true;
                this.dispose();
                new MenuAdmin();
                JOptionPane.showMessageDialog(null,
                        "¡Bienvenido Administrador!",
                        "Inicio de sesión exitoso",
                        JOptionPane.INFORMATION_MESSAGE);

            } else if (usuario.equals("Asistente Veterinario") && password.equals("asistente123")) {
                credencialesCorrectas = true;
                this.dispose();
                new MenuAsistente();
                JOptionPane.showMessageDialog(null,
                        "¡Bienvenido Asistente Veterinario!",
                        "Inicio de sesión exitoso",
                        JOptionPane.INFORMATION_MESSAGE);
            }

            if (!credencialesCorrectas) {
                JOptionPane.showMessageDialog(this,
                        "Credenciales incorrectas.\n\n" +
                                "Credenciales de prueba:\n" +
                                "Administrador: admin123\n" +
                                "Asistente Veterinario: asistente123",
                        "Error de autenticación",
                        JOptionPane.ERROR_MESSAGE);
                contraseña.setText("");
                contraseña.requestFocus();
            }
        }
    }

    private boolean validarCamposLogin(String usuario, String password) {
        if (usuario == null || usuario.equals("Seleccionar rol")) {
            JOptionPane.showMessageDialog(this, "Por favor seleccione un tipo de usuario.",
                    "Campo requerido", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (password.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese su contraseña.",
                    "Campo requerido", JOptionPane.WARNING_MESSAGE);
            contraseña.requestFocus();
            return false;
        }

        return true;
    }

    // Método para obtener las citas (será útil para otras clases)
    public static ArrayList<Cita> getCitas() {
        return listaCitas;
    }
}