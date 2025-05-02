import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.fasterxml.jackson.core.type.TypeReference;

import entidades.NotaMusical;

public class Melodia {

    private Nodo cabeza;

    public Melodia() {
        cabeza = null;
    }

    public void agregar(Nodo n) {
        if (n != null) {
            if (cabeza == null) {
                cabeza = n;
            } else {
                Nodo apuntador = cabeza;
                while (apuntador.siguiente != null) {
                    apuntador = apuntador.siguiente;
                }
                apuntador.siguiente = n;
            }
            n.siguiente = null;
        }
    }

    public void eliminar(Nodo n) {
        if (n != null && cabeza != null) {
            // Buscar el nodo
            boolean encontrado = false;
            Nodo apuntador = cabeza;
            Nodo anterior = null;
            while (apuntador != null && !encontrado) {
                if (apuntador == n) {
                    encontrado = true;
                } else {
                    anterior = apuntador;
                    apuntador = apuntador.siguiente;
                }
            }
            if (encontrado) {
                if (anterior == null) {
                    cabeza = apuntador.siguiente;
                } else {
                    anterior.siguiente = apuntador.siguiente;
                }
            }
        }
    }

    // metodo que devuelve la cantidad de primitivas de la lista
    public int obtenerLongitud() {
        // Contar los Primitivas
        int totalNodos = 0;
        // Recorrer la lista hasta la ultimo Primitiva
        Nodo apuntador = cabeza;
        while (apuntador != null) {
            totalNodos++;
            apuntador = apuntador.siguiente;
        }
        return totalNodos;
    }

    // Devuelve el nodo ubicado en una posicion
    public Nodo obtenerNodo(int posicion) {
        int p = 0;
        Nodo apuntador = cabeza;
        while (apuntador != null && p != posicion) {
            apuntador = apuntador.siguiente;
            p++;
        }
        if (apuntador != null && p == posicion) {
            return apuntador;
        } else {
            return null;
        }
    }

    // metodo que guarda el dibujo en un archivo JSON
    public void guardarJSON(String nombreArchivo) {
        List<NotaMusical> listaNotas = new ArrayList<>();
        Nodo actual = cabeza;
        while (actual != null) {
            listaNotas.add(actual.getNotaMusical());
            actual = actual.siguiente;
        }

        Archivo.guardarJson(nombreArchivo, listaNotas);
    }

    public void desdeJSON(String nombreArchivo) {
        List<NotaMusical> listaNotas = Archivo.leerJson(nombreArchivo, new TypeReference<List<NotaMusical>>() {
        });
        cabeza = null;
        if (listaNotas != null) {
            for (NotaMusical notaMusical : listaNotas) {
                var nodo = new Nodo(notaMusical);
                agregar(nodo);
            }
        }
    }

    public static String[] encabezados = new String[] { "Nota", "Figura", "Octava" };

    // Muestra la lista en una tabla
    public void mostrar(JTable tbl) {
        int filas = obtenerLongitud();
        String[][] datos = new String[filas][5];
        Nodo apuntador = cabeza;
        filas = 0;
        while (apuntador != null) {
            datos[filas][0] = apuntador.getNotaMusical().getNota().toString();
            datos[filas][1] = apuntador.getNotaMusical().getFigura().toString();
            datos[filas][2] = String.valueOf(apuntador.getNotaMusical().getOctava());
            apuntador = apuntador.siguiente;
            filas++;
        }
        DefaultTableModel dtm = new DefaultTableModel(datos, encabezados);
        tbl.setModel(dtm);
    }

    // Reproduce una melodía completa
    public void reproducirMelodia() {
        Nodo apuntador = cabeza;
        while (apuntador != null) {
            ReproductorAudioMIDI.reproducirNota(apuntador.getNotaMusical());
            apuntador = apuntador.siguiente;
        }
    }

}
