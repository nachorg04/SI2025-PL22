package app.dto;

/**
 * Data Transfer Object para la Historia #33529.
 * Transporta la información desde la base de datos hasta la vista.
 */
public class AccederReportajeDTO {
    
    // Campos para la tabla de la izquierda (Lista de eventos)
    private int id_evento;
    private String nombre_evento;
    
    // Campos para el visor de la derecha (Detalle del reportaje)
    private String titulo;
    private String subtitulo;
    private String cuerpo;
    private String fecha;
    private String hora;

    // Constructor vacío (necesario para que la librería de BD funcione)
    public AccederReportajeDTO() {}

    // Getters: Son fundamentales para que SwingUtil pueda rellenar la JTable automáticamente
    public int getId_evento() { return id_evento; }
    public String getNombre_evento() { return nombre_evento; }
    public String getTitulo() { return titulo; }
    public String getSubtitulo() { return subtitulo; }
    public String getCuerpo() { return cuerpo; }
    public String getFecha() { return fecha; }
    public String getHora() { return hora; }

    // Setters: Necesarios para que la base de datos inyecte los valores
    public void setId_evento(int id_evento) { this.id_evento = id_evento; }
    public void setNombre_evento(String nombre_evento) { this.nombre_evento = nombre_evento; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setSubtitulo(String subtitulo) { this.subtitulo = subtitulo; }
    public void setCuerpo(String cuerpo) { this.cuerpo = cuerpo; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public void setHora(String hora) { this.hora = hora; }
}