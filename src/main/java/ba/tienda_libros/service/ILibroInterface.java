package ba.tienda_libros.service;

import ba.tienda_libros.model.Libro;

import java.util.List;

public interface ILibroInterface {

    public List<Libro> listarLibros();

    public void guardarLibro(Libro libro);

    public Libro buscarLibroPorId(Long id);

    public void eliminarLibro(Libro libro);
}
