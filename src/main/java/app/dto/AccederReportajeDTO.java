package app.dto;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

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
    private String fecha_fin_embargo;
    private int acceso_especial_embargo;
    private int tiene_acceso;

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

    public String getFecha_fin_embargo() { return fecha_fin_embargo; }
    public void setFecha_fin_embargo(String fecha_fin_embargo) { this.fecha_fin_embargo = fecha_fin_embargo; }

    public int getAcceso_especial_embargo() { return acceso_especial_embargo; }
    public void setAcceso_especial_embargo(int acceso_especial_embargo) { this.acceso_especial_embargo = acceso_especial_embargo; }

    public int getTiene_acceso() { return tiene_acceso; }
    public void setTiene_acceso(int tiene_acceso) { this.tiene_acceso = tiene_acceso; }

    public String getRuta() { return ruta; }
    public void setRuta(String ruta) { this.ruta = ruta; }

    public List<String> getFotos() { return fotos; }
    public void setFotos(List<String> fotos) { this.fotos = fotos; }

    public List<String> getVideos() { return videos; }
    public void setVideos(List<String> videos) { this.videos = videos; }

    public boolean isAccesoEspecialEmbargo() {
        return acceso_especial_embargo == 1;
    }

    public boolean isEmbargoActivo() {
        if (fecha_fin_embargo == null || fecha_fin_embargo.isBlank()) {
            return false;
        }
        LocalDateTime fechaEmbargo = parseFechaEmbargo(fecha_fin_embargo);
        return fechaEmbargo != null && fechaEmbargo.isAfter(LocalDateTime.now());
    }

    public boolean isAccesoBloqueadoPorEmbargo() {
        return isEmbargoActivo() && !isAccesoEspecialEmbargo();
    }

    public boolean isAccesoSoloTextoPorEmbargo() {
        return isEmbargoActivo() && isAccesoEspecialEmbargo();
    }

    public boolean isAccesoCompleto() {
        return !isEmbargoActivo();
    }

    private LocalDateTime parseFechaEmbargo(String valor) {
        try {
            return LocalDateTime.parse(valor, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } catch (DateTimeParseException e) {
            try {
                return LocalDate.parse(valor, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay();
            } catch (DateTimeParseException ex) {
                return null;
            }
        }
    }
}
