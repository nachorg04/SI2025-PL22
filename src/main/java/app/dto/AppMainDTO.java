package app.dto;

/**
 * DTO genérico para cargar los nombres en los ComboBoxes
 */
public class AppMainDTO {
    
    // IMPORTANTE: El nombre de esta variable debe coincidir EXACTAMENTE 
    // con el nombre de la columna en el SELECT de tu SQL.
    private String nombre;

    // Getter y Setter obligatorios para que DbUtil pueda hacer la magia
    public String getNombre() { 
        return nombre; 
    }
    
    public void setNombre(String nombre) { 
        this.nombre = nombre; 
    }
}