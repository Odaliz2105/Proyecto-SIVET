package vista;

import modelo.Cita;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import vista.Home;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MenuAsistente extends JFrame {
    private JPanel panelPrincipa;
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
        inicializarDashboard();

        setVisible(true);
    }

    private void configurarEventos() {
        // Ver todas las citas - SOLO ESTE EVENT LISTENER
        verCitasButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verCitas(); // Este método abre la tabla con las citas
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

    // Agregar estos métodos a tu clase MenuAsistente.java

    private void inicializarDashboard() {
        // Crear el panel principal del dashboard
        JPanel dashboardPanel = new JPanel();
        dashboardPanel.setLayout(new BoxLayout(dashboardPanel, BoxLayout.Y_AXIS));
        dashboardPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "PANEL DE CONTROL - ASISTENTE",
                0, 0,
                new Font("Arial", Font.BOLD, 14)
        ));
        dashboardPanel.setBackground(new Color(230, 240, 250));

        // Título del dashboard
        JLabel tituloDashboard = new JLabel("📊 ESTADÍSTICAS DEL DÍA", JLabel.CENTER);
        tituloDashboard.setFont(new Font("Arial", Font.BOLD, 16));
        tituloDashboard.setForeground(new Color(25, 100, 150));
        tituloDashboard.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Panel de estadísticas
        JPanel estadisticasPanel = crearPanelEstadisticas();

        // Panel de próximas citas
        JPanel proximasCitasPanel = crearPanelProximasCitas();

        // Panel de acciones rápidas
        JPanel accionesPanel = crearPanelAccionesRapidas();

        // Agregar componentes al dashboard
        dashboardPanel.add(Box.createVerticalStrut(10));
        dashboardPanel.add(tituloDashboard);
        dashboardPanel.add(Box.createVerticalStrut(15));
        dashboardPanel.add(estadisticasPanel);
        dashboardPanel.add(Box.createVerticalStrut(10));
        dashboardPanel.add(proximasCitasPanel);
        dashboardPanel.add(Box.createVerticalStrut(10));
        dashboardPanel.add(accionesPanel);
        dashboardPanel.add(Box.createVerticalGlue());

        // Agregar el dashboard al PanelContenido (lado derecho)
        if (PanelContenido != null) {
            PanelContenido.removeAll();
            PanelContenido.setLayout(new BorderLayout());
            PanelContenido.add(dashboardPanel, BorderLayout.CENTER);
            PanelContenido.revalidate();
            PanelContenido.repaint();
        }
    }

    private JPanel crearPanelEstadisticas() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 8));
        panel.setBorder(BorderFactory.createTitledBorder("📈 Estadísticas Rápidas"));
        panel.setBackground(Color.WHITE);

        // Obtener estadísticas
        ArrayList<Cita> todasCitas = Home.getCitas();
        int totalCitas = todasCitas.size();
        int citasHoy = contarCitasHoy(todasCitas);
        int citasPendientes = contarCitasPorEstado(todasCitas, "Agendada");
        int citasCompletadas = contarCitasPorEstado(todasCitas, "Completada");

        // Crear etiquetas con estadísticas
        JLabel lblTotalCitas = new JLabel("📋 Total de Citas:");
        JLabel valueTotalCitas = new JLabel(String.valueOf(totalCitas));
        valueTotalCitas.setFont(new Font("Arial", Font.BOLD, 14));
        valueTotalCitas.setForeground(new Color(50, 120, 200));

        JLabel lblCitasHoy = new JLabel("📅 Citas de Hoy:");
        JLabel valueCitasHoy = new JLabel(String.valueOf(citasHoy));
        valueCitasHoy.setFont(new Font("Arial", Font.BOLD, 14));
        valueCitasHoy.setForeground(new Color(255, 140, 0));

        JLabel lblPendientes = new JLabel("⏳ Pendientes:");
        JLabel valuePendientes = new JLabel(String.valueOf(citasPendientes));
        valuePendientes.setFont(new Font("Arial", Font.BOLD, 14));
        valuePendientes.setForeground(new Color(220, 20, 60));

        // Agregar componentes
        panel.add(lblTotalCitas);
        panel.add(valueTotalCitas);
        panel.add(lblCitasHoy);
        panel.add(valueCitasHoy);
        panel.add(lblPendientes);
        panel.add(valuePendientes);

        return panel;
    }

    private JPanel crearPanelProximasCitas() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("🕒 Próximas Citas"));
        panel.setBackground(Color.WHITE);

        // Obtener próximas 3 citas del día de hoy
        ArrayList<Cita> proximasCitas = obtenerProximasCitas(3);

        if (proximasCitas.isEmpty()) {
            JLabel sinCitas = new JLabel("No hay citas programadas para hoy", JLabel.CENTER);
            sinCitas.setForeground(Color.GRAY);
            panel.add(sinCitas, BorderLayout.CENTER);
        } else {
            JPanel listaCitas = new JPanel();
            listaCitas.setLayout(new BoxLayout(listaCitas, BoxLayout.Y_AXIS));
            listaCitas.setBackground(Color.WHITE);

            for (Cita cita : proximasCitas) {
                JPanel citaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                citaPanel.setBackground(new Color(245, 255, 245));
                citaPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 230, 200)));

                String textoCita = String.format("🐾 %s - %s (%s)",
                        cita.getHora(),
                        cita.getPropietario(),
                        cita.getMascota());

                JLabel labelCita = new JLabel(textoCita);
                labelCita.setFont(new Font("Arial", Font.PLAIN, 11));
                citaPanel.add(labelCita);

                listaCitas.add(citaPanel);
                listaCitas.add(Box.createVerticalStrut(3));
            }

            JScrollPane scrollPane = new JScrollPane(listaCitas);
            scrollPane.setPreferredSize(new Dimension(0, 120));
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            panel.add(scrollPane, BorderLayout.CENTER);
        }

        return panel;
    }

    private JPanel crearPanelAccionesRapidas() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("⚡ Acciones Rápidas"));
        panel.setBackground(Color.WHITE);

        JButton btnActualizarDatos = new JButton("🔄 Actualizar Dashboard");
        btnActualizarDatos.setBackground(new Color(70, 130, 180));
        btnActualizarDatos.setForeground(Color.WHITE);
        btnActualizarDatos.setFocusPainted(false);

        JButton btnVerTodasCitas = new JButton("📋 Ver Todas las Citas");
        btnVerTodasCitas.setBackground(new Color(60, 179, 113));
        btnVerTodasCitas.setForeground(Color.WHITE);
        btnVerTodasCitas.setFocusPainted(false);

        // Eventos para los botones
        btnActualizarDatos.addActionListener(e -> {
            inicializarDashboard(); // Recargar el dashboard
            JOptionPane.showMessageDialog(this, "Dashboard actualizado correctamente",
                    "Actualizado", JOptionPane.INFORMATION_MESSAGE);
        });

        btnVerTodasCitas.addActionListener(e -> verCitas());

        panel.add(btnActualizarDatos);
        panel.add(btnVerTodasCitas);

        return panel;
    }

    // Métodos auxiliares para las estadísticas
    private int contarCitasHoy(ArrayList<Cita> citas) {
        java.util.Date hoy = new java.util.Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
        String fechaHoy = sdf.format(hoy);

        int contador = 0;
        for (Cita cita : citas) {
            if (cita.getFechaFormateada().equals(fechaHoy)) {
                contador++;
            }
        }
        return contador;
    }

    private int contarCitasPorEstado(ArrayList<Cita> citas, String estado) {
        int contador = 0;
        for (Cita cita : citas) {
            if (cita.getEstado().equals(estado)) {
                contador++;
            }
        }
        return contador;
    }

    private ArrayList<Cita> obtenerProximasCitas(int limite) {
        ArrayList<Cita> proximasCitas = new ArrayList<>();
        java.util.Date hoy = new java.util.Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
        String fechaHoy = sdf.format(hoy);

        // Filtrar citas de hoy y ordenar por hora
        for (Cita cita : Home.getCitas()) {
            if (cita.getFechaFormateada().equals(fechaHoy) &&
                    cita.getEstado().equals("Agendada")) {
                proximasCitas.add(cita);
            }
        }

        // Ordenar por hora (simple ordenamiento)
        proximasCitas.sort((c1, c2) -> c1.getHora().compareTo(c2.getHora()));

        // Limitar el número de resultados
        if (proximasCitas.size() > limite) {
            return new ArrayList<>(proximasCitas.subList(0, limite));
        }

        return proximasCitas;
    }
}