package ba.tienda_libros.view;

import ba.tienda_libros.model.Libro;
import ba.tienda_libros.service.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@Component
public class LibroForm extends JFrame {

    LibroService libroService;
    private JPanel panel;
    private JTable tablaLibros;
    private JTextField libroTexto;
    private JTextField precioTexto;
    private JTextField existenciasTexto;
    private JButton crearButton;
    private JButton modificarButton;
    private JButton eliminarButton;
    private JTextField autorTexto;
    private DefaultTableModel tablaModeloLibros;
    private JTextField idTexto;

    @Autowired
    public LibroForm(LibroService libroService) {
        this.libroService = libroService;

        this.iniciarForma();
        crearButton.addActionListener(e -> agregarLibro());
        tablaLibros.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                cargarLibroSeleccionado();
            }
        });

        modificarButton.addActionListener(e -> modificarLibro());
        eliminarButton.addActionListener(e -> eliminarLibro()   );
    }



    private void iniciarForma() {
        setContentPane(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setVisible(true);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension tamanioPantalla = toolkit.getScreenSize();
        int x = (tamanioPantalla.width - getWidth()/ 2) ;
        int y = (tamanioPantalla.height - getHeight()/ 2);
        setLocation(x, y);
    }

    private void agregarLibro(){
        if(libroTexto.getText().equals("")){
            mostrarMensaje("El nombre del libro es requerido");
            libroTexto.requestFocusInWindow();
            return;
        }
        var nombreLibro = libroTexto.getText();
        var autor = autorTexto.getText();
        var precio = Double.parseDouble(precioTexto.getText());
        var existencias = Integer.parseInt(existenciasTexto.getText());
        var libro = new Libro(null, nombreLibro, autor, precio, existencias);

        this.libroService.guardarLibro(libro);
        mostrarMensaje("Libro agregado correctamente");
        limpiarFormulario();
        listarLibros();
    }

    private void cargarLibroSeleccionado(){
        var renglon = tablaLibros.getSelectedRow();
        if(renglon != -1){
            String idLibro = tablaLibros.getModel().getValueAt(renglon, 0).toString();
            idTexto.setText(idLibro);
            String nombreLibro = tablaLibros.getModel().getValueAt(renglon, 1).toString();
            libroTexto.setText(nombreLibro);
            String autor = tablaLibros.getModel().getValueAt(renglon, 2).toString();
            autorTexto.setText(autor);
            String precio = tablaLibros.getModel().getValueAt(renglon, 3).toString();
            precioTexto.setText(precio);
            String existencias = tablaLibros.getModel().getValueAt(renglon, 4).toString();
            existenciasTexto.setText(existencias);
        }
    }

    private void modificarLibro(){
        if(this.idTexto.getText().equals("")){
            mostrarMensaje("Debe seleccionar un libro");
            return;
        }
        else{
            if(libroTexto.getText().equals("")){
                mostrarMensaje("El nombre del libro es requerido");
                libroTexto.requestFocusInWindow();
                return;
            }
        }
        var idLibro = Long.parseLong(idTexto.getText());
        var nombreLibro = libroTexto.getText();
        var autor = autorTexto.getText();
        var precio = Double.parseDouble(precioTexto.getText());
        var existencias = Integer.parseInt(existenciasTexto.getText());
        var libro = new Libro(idLibro, nombreLibro, autor, precio, existencias);
        libroService.guardarLibro(libro);
        mostrarMensaje("Libro modificado correctamente");
        limpiarFormulario();
        listarLibros();
    }

    private void eliminarLibro(){
        var renglon = tablaLibros.getSelectedRow();
        if(renglon != -1){
            String idLibro = tablaLibros.getModel().getValueAt(renglon, 0).toString();
            var libro = new Libro();
            libro.setId(Long.parseLong(idLibro));
            libroService.eliminarLibro(libro);
            mostrarMensaje("Libro eliminado correctamente");
            limpiarFormulario();
            listarLibros();
        }else{
            mostrarMensaje("Debe seleccionar un libro");
        }
    }
    private void limpiarFormulario(){
        libroTexto.setText("");
        autorTexto.setText("");
        precioTexto.setText("");
        existenciasTexto.setText("");

    }

    private void mostrarMensaje(String mensaje){
        JOptionPane.showMessageDialog(this, mensaje);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here

        idTexto = new JTextField();
        idTexto.setVisible(false);

        this.tablaModeloLibros = new DefaultTableModel(0,5){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        String[] cabeceros = {"Id", "Libro", "Autor", "Precio", "Existencias"};
        tablaModeloLibros.setColumnIdentifiers(cabeceros);

        this.tablaLibros = new JTable(tablaModeloLibros);
        tablaLibros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); //hace que solo se seleccione un solo registro a la vez
        listarLibros();
    }

    private void listarLibros(){
        tablaModeloLibros.setRowCount(0);

        var libros = libroService.listarLibros();
        libros.forEach(libro -> {
            Object[] renglonLibro = {
                    libro.getId(),
                    libro.getNombreLibro(),
                    libro.getAutor(),
                    libro.getPrecio(),
                    libro.getExistencias()
            };
            tablaModeloLibros.addRow(renglonLibro);
        });
    }
}

