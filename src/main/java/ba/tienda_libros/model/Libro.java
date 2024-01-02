package ba.tienda_libros.model;

import jakarta.persistence.*;
import lombok.*;

@Entity@Data@NoArgsConstructor@AllArgsConstructor@ToString
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombreLibro;
    private String autor;
    private Double precio;
    private Integer existencias;
}
