package app.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object para la historia de acceso a reportajes.
 */
public class AccederReportajeDTO {

    // Campos para la tabla de eventos
    private int id_evento;
    private String nombre_evento;

    // Campos para el detalle del reportaje
    private int id_reportaje;
    private String titulo;
    private String subtitulo;
    private String cuerpo;
    private String fecha;
    private String hora;

    // Campo auxiliar para recuperar multimedia desde SQL
    private String ruta;

    // Campos cargados manualmente para la vista y el JSON
    private List<String> fotos = new ArrayList<>();
    private List<String> videos = new ArrayList<>();

    public AccederReportajeDTO() {
    }

    public int getId_evento() { return id_evento; }
    public void setId_evento(int id_evento) { this.id_evento = id_evento; }

    public String getNombre_evento() { return nombre_evento; }
    public void setNombre_evento(String nombre_evento) { this.nombre_evento = nombre_evento; }

    public int getId_reportaje() { return id_reportaje; }
    public void setId_reportaje(int id_reportaje) { this.id_reportaje = id_reportaje; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getSubtitulo() { return subtitulo; }
    public void setSubtitulo(String subtitulo) { this.subtitulo = subtitulo; }

    public String getCuerpo() { return cuerpo; }
    public void setCuerpo(String cuerpo) { this.cuerpo = cuerpo; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getHora() { return hora; }
    public void setHora(String hora) { this.hora = hora; }

    public String getRuta() { return ruta; }
    public void setRuta(String ruta) { this.ruta = ruta; }

    public List<String> getFotos() { return fotos; }
    public void setFotos(List<String> fotos) { this.fotos = fotos; }

    public List<String> getVideos() { return videos; }
    public void setVideos(List<String> videos) { this.videos = videos; }
}
