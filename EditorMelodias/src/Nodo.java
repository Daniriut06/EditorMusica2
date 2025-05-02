import entidades.NotaMusical;

public class Nodo {

    private NotaMusical nota;

    public Nodo siguiente;

    public Nodo(NotaMusical nota) {
        this.nota = nota;
        this.siguiente = null;
    }

    public NotaMusical getNotaMusical() {
        return nota;
    }

    public void setNotaMusical(NotaMusical nota) {
        this.nota = nota;
    }

}
