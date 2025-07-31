package vista;

import modelo.Cita;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MenuAdmin extends JFrame {
    private JPanel PanelPrincipal;
    private JPanel PanelBotones;
    private JPanel PanelSecundario;
    private JPanel PanelContenido;
    private JButton verReportesButton;
    private JButton stockButton;
    private JButton cerrarSesiónButton;

    public MenuAdmin() {
        setTitle("SIVET - Panel de Administrador");
        setContentPane(PanelPrincipal);
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        configurarEventos();
        setExtendedState(JFrame.MAXIMIZED_BOTH); // para maximizar full screen
        setVisible(true);
    }

    private void configurarEventos() {
        // Botón para ver reportes
        verReportesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarReportes();
            }
        });

        // Botón para ver/gestionar stock
        stockButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gestionarStock();
            }
        });

        // Botón para cerrar sesión
        cerrarSesiónButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cerrarSesion();
            }
        });
    }

    private void mostrarReportes() {
        ArrayList<Cita> citas = vista.Home.getCitas(); // Cargar desde Home

        if (citas.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay citas registradas actualmente.",
                    "Reporte vacío",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Crear ventana de reportes
        JFrame ventanaReportes = new JFrame("Reporte de Citas Agendadas");
        ventanaReportes.setSize(800, 600);
        ventanaReportes.setLocationRelativeTo(this);
        ventanaReportes.setLayout(new BorderLayout());

        // Encabezados de tabla
        String[] columnas = {"ID", "Propietario", "Mascota", "Fecha", "Hora", "Motivo", "Estado"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabla no editable
            }
        };

        // Llenar tabla con las citas
        for (Cita cita : citas) {
            modelo.addRow(cita.toTableRow());
        }

        JTable tabla = new JTable(modelo);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(tabla);

        // Botones adicionales (opcional)
        JButton btnCerrar = new JButton("Cerrar");

        btnCerrar.addActionListener(e -> ventanaReportes.dispose());

        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.add(btnCerrar);

        JLabel titulo = new JLabel("REPORTE GENERAL DE CITAS AGENDADAS", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        ventanaReportes.add(titulo, BorderLayout.NORTH);
        ventanaReportes.add(scrollPane, BorderLayout.CENTER);
        ventanaReportes.add(panelBotones, BorderLayout.SOUTH);

        ventanaReportes.setVisible(true);
    }


    private void gestionarStock() {
        // Crear ventana
        JFrame ventanaStock = new JFrame("Gestión de Stock");
        ventanaStock.setSize(700, 500);
        ventanaStock.setLocationRelativeTo(this);
        ventanaStock.setLayout(new BorderLayout());

        // Datos simulados de stock
        String[] columnas = {"ID", "Producto", "Cantidad", "Unidad"};
        Object[][] datos = {
                {1, "Vacuna Rabia", 25, "dosis"},
                {2, "Antiparasitario", 40, "tabletas"},
                {3, "Jeringas", 100, "unidades"},
                {4, "Vitaminas", 60, "ml"}
        };

        DefaultTableModel modelo = new DefaultTableModel(datos, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tablaStock = new JTable(modelo);
        JScrollPane scrollPane = new JScrollPane(tablaStock);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        JButton btnAgregar = new JButton("Agregar");
        btnAgregar.setBackground(new java.awt.Color(0, 153, 0)); // Verde
        btnAgregar.setForeground(java.awt.Color.WHITE);          // Texto blanco

        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.setBackground(new java.awt.Color(204, 0, 0)); // Rojo
        btnEliminar.setForeground(java.awt.Color.WHITE);          // Texto blanco


        //JButton btnCerrar = new JButton("Cerrar");


        panelBotones.add(btnAgregar);
        panelBotones.add(btnEliminar);
        //panelBotones.add(btnCerrar);

        // Acción botón Agregar
        btnAgregar.addActionListener(e -> {
            JTextField campoProducto = new JTextField();
            JTextField campoCantidad = new JTextField();
            JTextField campoUnidad = new JTextField();

            JPanel panel = new JPanel(new GridLayout(3, 2));
            panel.add(new JLabel("Producto:"));
            panel.add(campoProducto);
            panel.add(new JLabel("Cantidad:"));
            panel.add(campoCantidad);
            panel.add(new JLabel("Unidad:"));
            panel.add(campoUnidad);

            int result = JOptionPane.showConfirmDialog(ventanaStock, panel,
                    "Agregar Producto", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                try {
                    String producto = campoProducto.getText().trim();
                    int cantidad = Integer.parseInt(campoCantidad.getText().trim());
                    String unidad = campoUnidad.getText().trim();

                    if (producto.isEmpty() || unidad.isEmpty()) {
                        JOptionPane.showMessageDialog(ventanaStock,
                                "Todos los campos son obligatorios.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    int idNuevo = modelo.getRowCount() + 1;
                    modelo.addRow(new Object[]{idNuevo, producto, cantidad, unidad});
                    JOptionPane.showMessageDialog(ventanaStock,
                            "Producto agregado correctamente.",
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(ventanaStock,
                            "Cantidad debe ser un número.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Acción botón Eliminar
        btnEliminar.addActionListener(e -> {
            int filaSeleccionada = tablaStock.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(ventanaStock,
                        "Seleccione un producto para eliminar.",
                        "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirmacion = JOptionPane.showConfirmDialog(ventanaStock,
                    "¿Está seguro que desea eliminar este producto?",
                    "Confirmar", JOptionPane.YES_NO_OPTION);

            if (confirmacion == JOptionPane.YES_OPTION) {
                modelo.removeRow(filaSeleccionada);
                JOptionPane.showMessageDialog(ventanaStock,
                        "Producto eliminado.",
                        "Eliminado", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Cerrar ventana
        //btnCerrar.addActionListener(e -> ventanaStock.dispose());

        // Componentes a la ventana
        ventanaStock.add(scrollPane, BorderLayout.CENTER);
        ventanaStock.add(panelBotones, BorderLayout.SOUTH);

        ventanaStock.setVisible(true);
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
