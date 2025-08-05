
package vista;

import com.toedter.calendar.JDateChooser;
import dao.MascotaDAO;
import modelo.Cita;
import dao.CitaDAO;
import org.bson.Document;
import org.bson.types.ObjectId;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;

public class MenuAsistente extends JFrame {
    private JPanel panelPrincipa;
    private JButton registrarCitaButton;
    private JButton verCitasButton;
    private JButton eliminarCitasButton;
    private JButton cerrarSesionButton;
    private JPanel panelSecundario;
    private JPanel PanelBotones;
    private JPanel PanelContenido;
    private JButton verMascotaButton;

    private CitaDAO citaDAO;
    /**
     * Ventana del menú principal para usuarios asistentes.
     * Proporciona acceso a funcionalidades  como
     * eliminacion, creacion y registro de citas.
     *
     * @author Odaliz Aracely
     * @version 1.0
     * @since 2024
     */
    public MenuAsistente() {
        citaDAO = new CitaDAO();

        setTitle("SIVET - Panel de Asistente Veterinario");
        setContentPane(panelPrincipa);
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        configurarEventos();

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);

        verMascotaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MascotaDAO mascotaDAO = new MascotaDAO();
                java.util.List<Document> mascotas = mascotaDAO.obtenerTodasLasMascotas();

                if (mascotas.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No hay mascotas registradas.");
                    return;
                }

                JFrame ventana = new JFrame("Ver y Editar Mascotas");
                ventana.setSize(900, 500);
                ventana.setLocationRelativeTo(null);
                ventana.setLayout(new BorderLayout());

                String[] columnas = {"ID", "Nombre", "Especie", "Raza", "Sexo", "Propietario"};
                DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
                    @Override
                    public boolean isCellEditable(int row, int col) {
                        return col == 2 || col == 3 || col == 4; // especie, raza, sexo editables
                    }
                };

                for (Document doc : mascotas) {
                    ObjectId id = doc.getObjectId("_id");
                    String nombre = doc.getString("nombre");
                    String especie = doc.getString("especie");
                    String raza = doc.getString("raza");
                    String sexo = doc.getString("sexo");

                    String propietario = "Desconocido";
                    if (doc.containsKey("propietario_id") && doc.get("propietario_id") instanceof ObjectId propietarioId) {
                        String nombreProp = mascotaDAO.obtenerNombrePropietarioPorId(propietarioId);
                        propietario = propietarioId.toHexString() + " - " + nombreProp;
                    } else if (doc.containsKey("propietario")) {
                        propietario = doc.getString("propietario");
                    }

                    modelo.addRow(new Object[]{
                            id != null ? id.toHexString() : "Sin ID",
                            nombre,
                            especie != null ? especie : "",
                            raza != null ? raza : "",
                            sexo != null ? sexo : "",
                            propietario
                    });
                }

                JTable tabla = new JTable(modelo);
                JScrollPane scrollPane = new JScrollPane(tabla);

                JButton btnActualizar = new JButton("Actualizar");

                btnActualizar.addActionListener(evt -> {
                    int fila = tabla.getSelectedRow();
                    if (fila == -1) {
                        JOptionPane.showMessageDialog(ventana, "Selecciona una mascota para actualizar.");
                        return;
                    }

                    String id = (String) modelo.getValueAt(fila, 0);
                    String especie = (String) modelo.getValueAt(fila, 2);
                    String raza = (String) modelo.getValueAt(fila, 3);
                    String sexo = (String) modelo.getValueAt(fila, 4);

                    boolean exito = mascotaDAO.actualizarCamposMascota(id, especie, raza, sexo);
                    if (exito) {
                        JOptionPane.showMessageDialog(ventana, "Mascota actualizada exitosamente.");
                    } else {
                        JOptionPane.showMessageDialog(ventana, "Error al actualizar la mascota.");
                    }
                });

                ventana.add(new JLabel("Haz doble clic en una celda para editar especie, raza o sexo.",
                        JLabel.CENTER), BorderLayout.NORTH);
                ventana.add(scrollPane, BorderLayout.CENTER);
                ventana.add(btnActualizar, BorderLayout.SOUTH);

                ventana.setVisible(true);
            }
        });


    }

    private void configurarEventos() {
        registrarCitaButton.addActionListener(e -> registrarNuevaCita());
        verCitasButton.addActionListener(e -> verCitas());
        eliminarCitasButton.addActionListener(e -> eliminarCita());
        cerrarSesionButton.addActionListener(e -> cerrarSesion());
    }

    private void registrarNuevaCita() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));

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

                Date fecha = new Date(); // puedes cambiar por una fecha ingresada si deseas
                Cita nuevaCita = new Cita(propietario, mascota, fecha, hora, motivo);

                CitaDAO citaDAO = new CitaDAO();
                MascotaDAO mascotaDAO = new MascotaDAO();

                // Guardar la cita
                citaDAO.guardarCita(nuevaCita);

                // Verificar si ya existe la mascota con ese propietario
                boolean existe = mascotaDAO.obtenerTodasLasMascotas().stream().anyMatch(doc ->
                        mascota.equalsIgnoreCase(doc.getString("nombre")) &&
                                propietario.equalsIgnoreCase(doc.getString("propietario"))
                );

                if (!existe) {
                    mascotaDAO.crearMascota(
                            mascota,
                            "Especie no especificada", // puedes reemplazarlo por un campo futuro
                            propietario
                    );
                }

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


    private void verCitas() {
        ArrayList<Cita> citas = citaDAO.obtenerTodas();

        if (citas.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay citas agendadas en el sistema.",
                    "Sin citas",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JFrame ventanaCitas = new JFrame("Ver Citas Agendadas");
        ventanaCitas.setSize(800, 600);
        ventanaCitas.setLocationRelativeTo(this);

        String[] columnas = {"ID", "Propietario", "Mascota", "Fecha", "Hora", "Motivo", "Estado"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (Cita cita : citas) {
            modelo.addRow(cita.toTableRow());
        }

        JTable tabla = new JTable(modelo);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getTableHeader().setReorderingAllowed(false);

        tabla.getColumnModel().getColumn(0).setPreferredWidth(50);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(120);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(100);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(80);
        tabla.getColumnModel().getColumn(4).setPreferredWidth(60);
        tabla.getColumnModel().getColumn(5).setPreferredWidth(200);
        tabla.getColumnModel().getColumn(6).setPreferredWidth(80);

        JScrollPane scrollPane = new JScrollPane(tabla);

        JPanel panelBotones = new JPanel(new FlowLayout());
        JButton btnActualizar = new JButton("Actualizar Lista");
        JButton btnDetalle = new JButton("Ver Detalle");
        JButton btnCerrar = new JButton("Cerrar");

        panelBotones.add(btnActualizar);
        panelBotones.add(btnDetalle);
        panelBotones.add(btnCerrar);

        btnActualizar.addActionListener(e -> {
            modelo.setRowCount(0);
            ArrayList<Cita> citasActualizadas = citaDAO.obtenerTodas();
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

            String idCita = (String) modelo.getValueAt(filaSeleccionada, 0);
            Cita citaSeleccionada = null;

            for (Cita cita : citas) {
                if (cita.getId().equals(idCita)) {
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

        ventanaCitas.setLayout(new BorderLayout());
        ventanaCitas.add(scrollPane, BorderLayout.CENTER);
        ventanaCitas.add(panelBotones, BorderLayout.SOUTH);

        JLabel titulo = new JLabel("CITAS AGENDADAS EN EL SISTEMA", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        ventanaCitas.add(titulo, BorderLayout.NORTH);

        ventanaCitas.setVisible(true);
    }

    private void eliminarCita() {
        ArrayList<Cita> citas = citaDAO.obtenerTodas();

        if (citas.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay citas para eliminar.",
                    "Sin citas",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

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
            String idSeleccionado = seleccion.split(" - ")[0].replace("ID:", "");

            Cita citaAEliminar = null;
            for (Cita cita : citas) {
                if (cita.getId().equals(idSeleccionado)) {
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
                    boolean eliminado = citaDAO.eliminarCita(idSeleccionado);
                    if (eliminado) {
                        JOptionPane.showMessageDialog(this,
                                "Cita eliminada exitosamente.",
                                "Éxito",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "No se pudo eliminar la cita.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
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
}