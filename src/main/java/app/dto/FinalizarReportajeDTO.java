package app.dto;

public class FinalizarReportajeDTO {

    private Integer id_reportaje;
    private Integer id_evento;
    private String descripcion;
    private String titulo;
    private String subtitulo;
    private String cuerpo;

    private Integer id_multimedia;
    private String ruta;
    private String tipo;

    private Integer id_reportero;
    private String nombre_reportero;
    private Integer revision_finalizada;

    private Integer id_comentario;
    private String autor_comentario;
    private String comentario;
    private String fecha_hora;

    public Integer getId_reportaje() {
        return id_reportaje;
    }

    public void setId_reportaje(Integer id_reportaje) {
        this.id_reportaje = id_reportaje;
    }

    public Integer getId_evento() {
        return id_evento;
    }

    public void setId_evento(Integer id_evento) {
        this.id_evento = id_evento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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

    public Integer getId_multimedia() {
        return id_multimedia;
    }

    public void setId_multimedia(Integer id_multimedia) {
        this.id_multimedia = id_multimedia;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Integer getId_reportero() {
        return id_reportero;
    }

    public void setId_reportero(Integer id_reportero) {
        this.id_reportero = id_reportero;
    }

    public String getNombre_reportero() {
        return nombre_reportero;
    }

    public void setNombre_reportero(String nombre_reportero) {
        this.nombre_reportero = nombre_reportero;
    }

    public Integer getRevision_finalizada() {
        return revision_finalizada;
    }

    public void setRevision_finalizada(Integer revision_finalizada) {
        this.revision_finalizada = revision_finalizada;
    }

    public Integer getId_comentario() {
        return id_comentario;
    }

    public void setId_comentario(Integer id_comentario) {
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

    public boolean isRevisionFinalizada() {
        return revision_finalizada != null && revision_finalizada == 1;
    }

    @Override
    public String toString() {
        if (ruta != null && !ruta.isBlank()) {
            return ruta;
        }
        String etqTitulo = (titulo == null || titulo.isBlank()) ? "Sin título" : titulo;
        String etqEvento = (descripcion == null || descripcion.isBlank()) ? "Sin evento" : descripcion;
        return etqTitulo + " (" + etqEvento + ")";
    }
}