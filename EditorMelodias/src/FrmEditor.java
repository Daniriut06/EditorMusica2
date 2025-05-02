import java.awt.event.ActionListener;
import java.util.Arrays;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

import entidades.Figura;
import entidades.Nota;
import entidades.NotaMusical;

public class FrmEditor extends JFrame {

    private JTable tblMelodia;
    JComboBox cmbNota, cmbFigura, cmbOctava;
    String nombreArchivo = "";

    public FrmEditor() {
        setSize(700, 500);
        setTitle("Editor de Melodías");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JToolBar tbEditor = new JToolBar();

        JButton btnCargar = new JButton();
        btnCargar.setIcon(new ImageIcon(getClass().getResource("/iconos/AbrirArchivos.png")));
        btnCargar.setToolTipText("Agregar");
        btnCargar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                cargarMelodia();
            }
        });
        tbEditor.add(btnCargar);

        JButton btnGuardar = new JButton();
        btnGuardar.setIcon(new ImageIcon(getClass().getResource("/iconos/Guardar.png")));
        btnGuardar.setToolTipText("Guardar");
        btnGuardar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                guardarMelodia();
            }
        });
        tbEditor.add(btnGuardar);

        JButton btnAgregar = new JButton();
        btnAgregar.setIcon(new ImageIcon(getClass().getResource("/iconos/AgregarNota.png")));
        btnAgregar.setToolTipText("Agregar Nota");
        btnAgregar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                agregarNota();
            }
        });
        tbEditor.add(btnAgregar);

        cmbNota = new JComboBox();
        cmbNota.setModel(new DefaultComboBoxModel(
                Arrays.stream(Nota.values()).map(n -> n.name().replace("_SOSTENIDO", "#")).toArray(String[]::new)));

        cmbNota.setToolTipText("Nota Musical");
        tbEditor.add(cmbNota);
        System.out.println("Figuras cargadas: " + Arrays.toString(Figura.values()));

        cmbFigura = new JComboBox();
        cmbFigura.setToolTipText("Figura Musical");
        cmbFigura.setModel(
                new DefaultComboBoxModel(Arrays.stream(Figura.values()).map(Enum::name).toArray(String[]::new)));
        tbEditor.add(cmbFigura);

        cmbOctava = new JComboBox<>();
        cmbOctava.setToolTipText("Octava");
        for (int i = 0; i <= 7; i++) { // octavas de 1 a 7, seguras para la mayoría de notas
            cmbOctava.addItem(i);
        }

        tbEditor.add(cmbOctava);

        JButton btnModificar = new JButton();
        btnModificar.setIcon(new ImageIcon(getClass().getResource("/iconos/ModificarNota.png")));
        btnModificar.setToolTipText("Modificar Nota");
        btnModificar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                modificarNota();
            }
        });
        tbEditor.add(btnModificar);

        JButton btnQuitar = new JButton();
        btnQuitar.setIcon(new ImageIcon(getClass().getResource("/iconos/QuitarNota.png")));
        btnQuitar.setToolTipText("Quitar Nota");
        btnQuitar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                quitarNota();
            }
        });
        tbEditor.add(btnQuitar);

        JButton btnReproducir = new JButton();
        btnReproducir.setIcon(new ImageIcon(getClass().getResource("/iconos/Reproducir.png")));
        btnReproducir.setToolTipText("Reproducir Melodia");
        btnReproducir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                reproducir();
            }
        });
        tbEditor.add(btnReproducir);

        tblMelodia = new JTable();
        DefaultTableModel dtm = new DefaultTableModel(null, Melodia.encabezados);
        tblMelodia.setModel(dtm);

        JScrollPane spMelodia = new JScrollPane(tblMelodia);

        getContentPane().add(tbEditor, BorderLayout.NORTH);
        getContentPane().add(spMelodia, BorderLayout.CENTER);
        // Cuando el usuario selecciona una fila, actualizamos los combos
        // automáticamente
        tblMelodia.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblMelodia.getSelectedRow() >= 0) {
                int fila = tblMelodia.getSelectedRow();
                Nodo nodoSeleccionado = melodia.obtenerNodo(fila);
                if (nodoSeleccionado != null) {
                    Nota nota = nodoSeleccionado.getNotaMusical().getNota();
                    Figura figura = nodoSeleccionado.getNotaMusical().getFigura();
                    int octava = nodoSeleccionado.getNotaMusical().getOctava();

                    cmbNota.setSelectedIndex(nota.ordinal());
                    cmbFigura.setSelectedIndex(figura.ordinal());
                    cmbOctava.setSelectedItem(octava);
                }
            }
        });

    }

    private Melodia melodia = new Melodia();

    private void cargarMelodia() {
        nombreArchivo = Archivo.elegirArchivo();
        melodia.desdeJSON(nombreArchivo);
        melodia.mostrar(tblMelodia);
        ;
    }

    private void guardarMelodia() {
        if (nombreArchivo.equals("")) {
            nombreArchivo = Archivo.elegirArchivo();
        }
        melodia.guardarJSON(nombreArchivo);
        JOptionPane.showMessageDialog(null, "El archivo fue guardado");
    }

    private void agregarNota() {
        NotaMusical nota = new NotaMusical(Nota.values()[cmbNota.getSelectedIndex()],
                Figura.values()[cmbFigura.getSelectedIndex()], cmbOctava.getSelectedIndex());
        melodia.agregar(new Nodo(nota));
        melodia.mostrar(tblMelodia);
    }

    private void reproducir() {
        melodia.reproducirMelodia();
    }

    private void quitarNota() {
        if (tblMelodia.getSelectedRow() >= 0)
            melodia.eliminar(melodia.obtenerNodo(tblMelodia.getSelectedRow()));
        melodia.mostrar(tblMelodia);
    }

    private void modificarNota() {
        if (tblMelodia.getSelectedRow() >= 0) {
            Nodo nodoSeleccionado = melodia.obtenerNodo(tblMelodia.getSelectedRow());
            nodoSeleccionado.getNotaMusical().setNota(Nota.values()[cmbNota.getSelectedIndex()]);
            nodoSeleccionado.getNotaMusical().setFigura(Figura.values()[cmbFigura.getSelectedIndex()]);
            nodoSeleccionado.getNotaMusical().setOctava(cmbOctava.getSelectedIndex());
            melodia.mostrar(tblMelodia);
        }
    }

}
