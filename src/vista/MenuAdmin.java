package vista;

import dao.ReporteDAO;
import dao.StockDAO;
import modelo.Cita;
import org.bson.Document;

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

            // Guardar también en MongoDB (colección "reportes")
        ReporteDAO reporteDAO = new ReporteDAO();
        for (Cita cita : citas) {
            reporteDAO.guardarCitaComoReporte(cita);  // ← se guarda en colección "reportes"
            modelo.addRow(cita.toTableRow());
        }


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
        StockDAO stockDAO = new StockDAO();
        JFrame ventanaStock = new JFrame("Gestión de Stock");
        ventanaStock.setSize(700, 500);
        ventanaStock.setLocationRelativeTo(this);
        ventanaStock.setLayout(new BorderLayout());

        // Tabla y modelo
        String[] columnas = {"ID", "Producto", "Cantidad", "Unidad"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        JTable tablaStock = new JTable(modelo);
        JScrollPane scrollPane = new JScrollPane(tablaStock);

        // Cargar datos desde MongoDB
        modelo.setRowCount(0);
        for (Document doc : stockDAO.obtenerStock()) {
            modelo.addRow(new Object[]{
                    doc.getObjectId("_id").toHexString(),
                    doc.getString("producto"),
                    doc.getInteger("cantidad"),
                    doc.getString("unidad")
            });
        }

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        JButton btnAgregar = new JButton("Agregar");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");

        btnAgregar.setBackground(new Color(0, 153, 0));
        btnAgregar.setForeground(Color.WHITE);
        btnEliminar.setBackground(new Color(204, 0, 0));
        btnEliminar.setForeground(Color.WHITE);
        btnEditar.setBackground(new Color(0, 102, 204));
        btnEditar.setForeground(Color.WHITE);

        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);

        // Acción AGREGAR
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

            int res = JOptionPane.showConfirmDialog(ventanaStock, panel, "Agregar Producto", JOptionPane.OK_CANCEL_OPTION);
            if (res == JOptionPane.OK_OPTION) {
                try {
                    String producto = campoProducto.getText().trim();
                    int cantidad = Integer.parseInt(campoCantidad.getText().trim());
                    String unidad = campoUnidad.getText().trim();

                    if (producto.isEmpty() || unidad.isEmpty()) throw new Exception("Campos vacíos");

                    stockDAO.agregarProducto(producto, cantidad, unidad);

                    modelo.setRowCount(0); // recargar
                    for (Document doc : stockDAO.obtenerStock()) {
                        modelo.addRow(new Object[]{
                                doc.getObjectId("_id").toHexString(),
                                doc.getString("producto"),
                                doc.getInteger("cantidad"),
                                doc.getString("unidad")
                        });
                    }

                    JOptionPane.showMessageDialog(ventanaStock, "Producto agregado correctamente.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(ventanaStock, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Acción ELIMINAR
        btnEliminar.addActionListener(e -> {
            int fila = tablaStock.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(ventanaStock, "Seleccione un producto para eliminar.");
                return;
            }

            String id = (String) modelo.getValueAt(fila, 0);
            int confirm = JOptionPane.showConfirmDialog(ventanaStock, "¿Seguro que desea eliminar este producto?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                stockDAO.eliminarProductoPorId(id);

                modelo.removeRow(fila);
                JOptionPane.showMessageDialog(ventanaStock, "Producto eliminado.");
            }
        });

        // Acción EDITAR
        btnEditar.addActionListener(e -> {
            int fila = tablaStock.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(ventanaStock, "Seleccione un producto para editar.");
                return;
            }

            String id = (String) modelo.getValueAt(fila, 0);
            String productoActual = (String) modelo.getValueAt(fila, 1);
            int cantidadActual = (int) modelo.getValueAt(fila, 2);
            String unidadActual = (String) modelo.getValueAt(fila, 3);

            JTextField campoProducto = new JTextField(productoActual);
            JTextField campoCantidad = new JTextField(String.valueOf(cantidadActual));
            JTextField campoUnidad = new JTextField(unidadActual);

            JPanel panel = new JPanel(new GridLayout(3, 2));
            panel.add(new JLabel("Producto:"));
            panel.add(campoProducto);
            panel.add(new JLabel("Cantidad:"));
            panel.add(campoCantidad);
            panel.add(new JLabel("Unidad:"));
            panel.add(campoUnidad);

            int res = JOptionPane.showConfirmDialog(ventanaStock, panel, "Editar Producto", JOptionPane.OK_CANCEL_OPTION);
            if (res == JOptionPane.OK_OPTION) {
                try {
                    String productoNuevo = campoProducto.getText().trim();
                    int cantidadNueva = Integer.parseInt(campoCantidad.getText().trim());
                    String unidadNueva = campoUnidad.getText().trim();

                    if (productoNuevo.isEmpty() || unidadNueva.isEmpty()) throw new Exception("Campos vacíos");

                    stockDAO.actualizarProducto(id, productoNuevo, cantidadNueva, unidadNueva);

                    modelo.setValueAt(productoNuevo, fila, 1);
                    modelo.setValueAt(cantidadNueva, fila, 2);
                    modelo.setValueAt(unidadNueva, fila, 3);

                    JOptionPane.showMessageDialog(ventanaStock, "Producto actualizado correctamente.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(ventanaStock, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

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
