package vista;

import modelo.Cita;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import vista.Home;

public class MenuAsistente extends JFrame {
    private JPanel panelPrincipa;
    private JButton registrarCitaButton;
    private JButton verCitasButton;
    private JButton eliminarCitasButton;
    private JButton cerrarSesionButton;
    private JPanel panelSecundario;
    private JPanel PanelBotones;
    private JPanel PanelContenido;


    public MenuAsistente() {
        setTitle("SIVET - Panel de Asistente Veterinario");
        setContentPane(panelPrincipa);
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        configurarEventos();

        setExtendedState(JFrame.MAXIMIZED_BOTH); // para maximizar full screen
        setVisible(true);
    }

    private void configurarEventos() {
        // Registrar nueva cita
        registrarCitaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarNuevaCita();
            }

        });
        verCitasButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verCitas();
            }
        });

        // Ver todas las citas
        verCitasButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verTodasLasCitas();
            }
        });

        // Eliminar citas
        eliminarCitasButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarCita();
            }
        });

        // Cerrar sesión
        cerrarSesionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cerrarSesion();
            }
        });
    }

    private void registrarNuevaCita() {
        // Mostrar formulario para registrar nueva cita
        JPanel panel = new JPanel(new java.awt.GridLayout(5, 2, 5, 5));

        JTextField propietarioField = new JTextField();
        JTextField mascotaField = new JTextField();
        JTextField fechaField = new JTextField();
        JTextField horaField = new JTextField();
        JTextField motivoField = new JTextField();

        panel.add(new JLabel("Propietario:"));
        panel.add(propietarioField);
        panel.add(new JLabel("Mascota:"));
        panel.add(mascotaField);
        panel.add(new JLabel("Fecha (dd/mm/yyyy):"));
        panel.add(fechaField);
        panel.add(new JLabel("Hora:"));
        panel.add(horaField);
        panel.add(new JLabel("Motivo:"));
        panel.add(motivoField);

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Registrar Nueva Cita", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String propietario = propietarioField.getText().trim();
                String mascota = mascotaField.getText().trim();
                String hora = horaField.getText().trim();
                String motivo = motivoField.getText().trim();

                if (propietario.isEmpty() || mascota.isEmpty() || hora.isEmpty() || motivo.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Por simplicidad, usar fecha actual
                java.util.Date fecha = new java.util.Date();

                Cita nuevaCita = new Cita(propietario, mascota, fecha, hora, motivo);
                Home.getCitas().add(nuevaCita);

                JOptionPane.showMessageDialog(this,
                        "¡Cita registrada exitosamente!\n\n" + nuevaCita.toString(),
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Error al registrar la cita: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void verTodasLasCitas() {
        ArrayList<Cita> citas = Home.getCitas();

        if (citas.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay citas registradas en el sistema.",
                    "Sin citas",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("=== CITAS REGISTRADAS ===\n\n");

            for (int i = 0; i < citas.size(); i++) {
                sb.append((i + 1) + ". " + citas.get(i).toString());
                sb.append("\n" + "=".repeat(40) + "\n");
            }

            JTextArea textArea = new JTextArea(sb.toString());
            textArea.setEditable(false);
            textArea.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 12));

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new java.awt.Dimension(600, 400));

            JOptionPane.showMessageDialog(this, scrollPane,
                    "Lista de Citas", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void eliminarCita() {
        ArrayList<Cita> citas = Home.getCitas();

        if (citas.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay citas para eliminar.",
                    "Sin citas",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Crear lista de opciones para seleccionar
        String[] opciones = new String[citas.size()];
        for (int i = 0; i < citas.size(); i++) {
            Cita cita = citas.get(i);
            opciones[i] = "ID:" + cita.getId() + " - " + cita.getPropietario() +
                    " (" + cita.getMascota() + ") - " + cita.getFechaFormateada();
        }

        String seleccion = (String) JOptionPane.showInputDialog(this,
                "Seleccione la cita a eliminar:",
                "Eliminar Cita",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]);

        if (seleccion != null) {
            // Encontrar la cita seleccionada
            int idSeleccionado = Integer.parseInt(seleccion.split(" - ")[0].replace("ID:", ""));

            Cita citaAEliminar = null;
            for (Cita cita : citas) {
                if (cita.getId() == idSeleccionado) {
                    citaAEliminar = cita;
                    break;
                }
            }

            if (citaAEliminar != null) {
                int confirmacion = JOptionPane.showConfirmDialog(this,
                        "¿Está seguro de eliminar esta cita?\n\n" + citaAEliminar.toString(),
                        "Confirmar eliminación",
                        JOptionPane.YES_NO_OPTION);

                if (confirmacion == JOptionPane.YES_OPTION) {
                    citas.remove(citaAEliminar);
                    JOptionPane.showMessageDialog(this,
                            "Cita eliminada exitosamente.",
                            "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    }

    private void cerrarSesion() {
        int respuesta = JOptionPane.showConfirmDialog(this,
                "¿Está seguro que desea cerrar sesión?",
                "Confirmar cierre de sesión",
                JOptionPane.YES_NO_OPTION);

        if (respuesta == JOptionPane.YES_OPTION) {
            this.dispose();
            new Home();
        }
    }

    // En tu clase MenuAsistente.java, agrega este método para el botón "Ver Citas":

    private void verCitas() {
        // Obtener las citas desde Home
        ArrayList<Cita> citas = Home.getCitas();

        if (citas.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay citas agendadas en el sistema.",
                    "Sin citas",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Crear ventana para mostrar las citas
        JFrame ventanaCitas = new JFrame("Ver Citas Agendadas");
        ventanaCitas.setSize(800, 600);
        ventanaCitas.setLocationRelativeTo(this);

        // Crear tabla para mostrar las citas
        String[] columnas = {"ID", "Propietario", "Mascota", "Fecha", "Hora", "Motivo", "Estado"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer la tabla no editable
            }
        };

        // Llenar la tabla con las citas
        for (Cita cita : citas) {
            modelo.addRow(cita.toTableRow());
        }

        JTable tabla = new JTable(modelo);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getTableHeader().setReorderingAllowed(false);

        // Configurar anchos de columnas
        tabla.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
        tabla.getColumnModel().getColumn(1).setPreferredWidth(120); // Propietario
        tabla.getColumnModel().getColumn(2).setPreferredWidth(100); // Mascota
        tabla.getColumnModel().getColumn(3).setPreferredWidth(80);  // Fecha
        tabla.getColumnModel().getColumn(4).setPreferredWidth(60);  // Hora
        tabla.getColumnModel().getColumn(5).setPreferredWidth(200); // Motivo
        tabla.getColumnModel().getColumn(6).setPreferredWidth(80);  // Estado

        JScrollPane scrollPane = new JScrollPane(tabla);

        // Panel para botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        JButton btnActualizar = new JButton("Actualizar Lista");
        JButton btnDetalle = new JButton("Ver Detalle");
        JButton btnCerrar = new JButton("Cerrar");

        panelBotones.add(btnActualizar);
        panelBotones.add(btnDetalle);
        panelBotones.add(btnCerrar);

        // Eventos de botones
        btnActualizar.addActionListener(e -> {
            // Limpiar tabla
            modelo.setRowCount(0);
            // Volver a cargar las citas
            ArrayList<Cita> citasActualizadas = Home.getCitas();
            for (Cita cita : citasActualizadas) {
                modelo.addRow(cita.toTableRow());
            }
            JOptionPane.showMessageDialog(ventanaCitas, "Lista actualizada correctamente.");
        });

        btnDetalle.addActionListener(e -> {
            int filaSeleccionada = tabla.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(ventanaCitas,
                        "Por favor seleccione una cita para ver los detalles.",
                        "Seleccionar cita",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Obtener la cita seleccionada
            int idCita = (int) modelo.getValueAt(filaSeleccionada, 0);
            Cita citaSeleccionada = null;

            for (Cita cita : Home.getCitas()) {
                if (cita.getId() == idCita) {
                    citaSeleccionada = cita;
                    break;
                }
            }

            if (citaSeleccionada != null) {
                JOptionPane.showMessageDialog(ventanaCitas,
                        citaSeleccionada.toString(),
                        "Detalle de la Cita",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        btnCerrar.addActionListener(e -> ventanaCitas.dispose());

        // Configurar layout de la ventana
        ventanaCitas.setLayout(new BorderLayout());
        ventanaCitas.add(scrollPane, BorderLayout.CENTER);
        ventanaCitas.add(panelBotones, BorderLayout.SOUTH);

        // Agregar título
        JLabel titulo = new JLabel("CITAS AGENDADAS EN EL SISTEMA", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        ventanaCitas.add(titulo, BorderLayout.NORTH);

        ventanaCitas.setVisible(true);
    }

}