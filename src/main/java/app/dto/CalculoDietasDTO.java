package app.dto;

public class CalculoDietasDTO {

    private Integer id_reportaje;
    private Integer id_evento;
    private String titulo;
    private String descripcion;
    private String provincia_evento;
    private String pais_evento;
    private String provincia_residencia;
    private Double coste_dia;
    private Double dieta_alojamiento;
    private Double dieta_manutencion;
    private Integer numero_dias;

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

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getProvincia_evento() {
        return provincia_evento;
    }

    public void setProvincia_evento(String provincia_evento) {
        this.provincia_evento = provincia_evento;
    }

    public String getPais_evento() {
        return pais_evento;
    }

    public void setPais_evento(String pais_evento) {
        this.pais_evento = pais_evento;
    }

    public String getProvincia_residencia() {
        return provincia_residencia;
    }

    public void setProvincia_residencia(String provincia_residencia) {
        this.provincia_residencia = provincia_residencia;
    }

    public Double getCoste_dia() {
        return coste_dia;
    }

    public void setCoste_dia(Double coste_dia) {
        this.coste_dia = coste_dia;
    }

    public Double getDieta_alojamiento() {
        return dieta_alojamiento;
    }

    public void setDieta_alojamiento(Double dieta_alojamiento) {
        this.dieta_alojamiento = dieta_alojamiento;
    }

    public Double getDieta_manutencion() {
        return dieta_manutencion;
    }

    public void setDieta_manutencion(Double dieta_manutencion) {
        this.dieta_manutencion = dieta_manutencion;
    }

    public Integer getNumero_dias() {
        return numero_dias;
    }

    public void setNumero_dias(Integer numero_dias) {
        this.numero_dias = numero_dias;
    }

    @Override
    public String toString() {
        String tituloMostrado = (titulo == null || titulo.isBlank()) ? "Sin título" : titulo;
        String eventoMostrado = (descripcion == null || descripcion.isBlank()) ? "Sin evento" : descripcion;
        return tituloMostrado + " (" + eventoMostrado + ")";
    }
}