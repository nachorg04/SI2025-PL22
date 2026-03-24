package app.dto;

/**
 * DTO para la funcionalidad Revisar Reportaje.
 *
 * Incluye campos de:
 * - Reportaje seleccionado
 * - Comentarios de revisión
 */
public class RevisarReportajeDTO {

    // Datos de reportaje
    private int id_reportaje;
    private String evento;
    private String titulo;
    private String subtitulo;
    private String cuerpo;
    private String estado_revision;
    private String ruta;

    // Comentarios
    private int id_comentario;
    private String autor_comentario;
    private String comentario;
    private String fecha_hora;

    public RevisarReportajeDTO() {
        // constructor vacío para mapeo POJO de DbUtils
    }

    public int getId_reportaje() {
        return id_reportaje;
    }

    public void setId_reportaje(int id_reportaje) {
        this.id_reportaje = id_reportaje;
    }

    public String getEvento() {
        return evento;
    }

    public void setEvento(String evento) {
        this.evento = evento;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getSubtitulo() {
        return subtitulo;
    }

    public void setSubtitulo(String subtitulo) {
        this.subtitulo = subtitulo;
    }

    public String getCuerpo() {
        return cuerpo;
    }

    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }

    public String getEstado_revision() {
        return estado_revision;
    }

    public void setEstado_revision(String estado_revision) {
        this.estado_revision = estado_revision;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public int getId_comentario() {
        return id_comentario;
    }

    public void setId_comentario(int id_comentario) {
        this.id_comentario = id_comentario;
    }

    public String getAutor_comentario() {
        return autor_comentario;
    }

    public void setAutor_comentario(String autor_comentario) {
        this.autor_comentario = autor_comentario;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getFecha_hora() {
        return fecha_hora;
    }

    public void setFecha_hora(String fecha_hora) {
        this.fecha_hora = fecha_hora;
    }

    @Override
    public String toString() {
        String etqEvento = (evento == null || evento.isBlank()) ? "Sin evento" : evento;
        String etqTitulo = (titulo == null || titulo.isBlank()) ? "Sin título" : titulo;
        return etqTitulo + " (" + etqEvento + ")";
    }
}