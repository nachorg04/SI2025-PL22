package app.dto;

public class OfrecerReportajesDTO {
    // Atributos de los eventos
    private int id_evento;
    private String nombre_evento;
    private String reportero_asignado;
    private String tematicas_evento;

    // Atributos de las empresas
    private int id_empresa;
    private String nombre_empresa;
    private String email;
    private double tarifa_plana;
    private int tiene_tarifa_plana;
    private int al_corriente_pago;
    private String tarifa_plana_info;
    private String pago_info;

    // Atributos del ofrecimiento
    private String estado;
    private int tiene_acceso;

    // --- NUEVOS ATRIBUTOS PARA EL EMBARGO (HU 34348) ---
    private String fecha_fin_embargo;
    private boolean acepta_embargos;
    private String estado_embargo;

    public OfrecerReportajesDTO() {}

    public int getId_evento() { return id_evento; }
    public void setId_evento(int id_evento) { this.id_evento = id_evento; }

    public String getNombre_evento() { return nombre_evento; }
    public void setNombre_evento(String nombre_evento) { this.nombre_evento = nombre_evento; }

    public String getReportero_asignado() { return reportero_asignado; }
    public void setReportero_asignado(String reportero_asignado) { this.reportero_asignado = reportero_asignado; }

    public String getTematicas_evento() { return tematicas_evento; }
    public void setTematicas_evento(String tematicas_evento) { this.tematicas_evento = tematicas_evento; }

    public int getId_empresa() { return id_empresa; }
    public void setId_empresa(int id_empresa) { this.id_empresa = id_empresa; }

    public String getNombre_empresa() { return nombre_empresa; }
    public void setNombre_empresa(String nombre_empresa) { this.nombre_empresa = nombre_empresa; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public double getTarifa_plana() { return tarifa_plana; }
    public void setTarifa_plana(double tarifa_plana) { this.tarifa_plana = tarifa_plana; }

    public int getTiene_tarifa_plana() { return tiene_tarifa_plana; }
    public void setTiene_tarifa_plana(int tiene_tarifa_plana) { this.tiene_tarifa_plana = tiene_tarifa_plana; }

    public int getAl_corriente_pago() { return al_corriente_pago; }
    public void setAl_corriente_pago(int al_corriente_pago) { this.al_corriente_pago = al_corriente_pago; }

    public String getTarifa_plana_info() { return tarifa_plana_info; }
    public void setTarifa_plana_info(String tarifa_plana_info) { this.tarifa_plana_info = tarifa_plana_info; }

    public String getPago_info() { return pago_info; }
    public void setPago_info(String pago_info) { this.pago_info = pago_info; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public int getTiene_acceso() { return tiene_acceso; }
    public void setTiene_acceso(int tiene_acceso) { this.tiene_acceso = tiene_acceso; }

    // --- GETTERS Y SETTERS DEL EMBARGO ---
    public boolean isAcepta_embargos() { return acepta_embargos; }
    public void setAcepta_embargos(boolean acepta_embargos) { this.acepta_embargos = acepta_embargos; }

    public String getEstado_embargo() { return estado_embargo; }
    public void setEstado_embargo(String estado_embargo) { this.estado_embargo = estado_embargo; }

    public String getFecha_fin_embargo() { return fecha_fin_embargo; }
    public void setFecha_fin_embargo(String fecha_fin_embargo) { 
        this.fecha_fin_embargo = fecha_fin_embargo; 
        calcularEstadoEmbargo();
    }

    // Método que evalúa la fecha recibida por SQL y asigna el texto de la columna
    private void calcularEstadoEmbargo() {
        if (this.fecha_fin_embargo == null || this.fecha_fin_embargo.trim().isEmpty()) {
            this.estado_embargo = "NINGUNO";
            return;
        }
        try {
            // Intentamos parsear formato con hora (datetime)
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            java.util.Date fechaEmbargo = sdf.parse(this.fecha_fin_embargo);
            if (fechaEmbargo.after(new java.util.Date())) {
                this.estado_embargo = "ACTIVO";
            } else {
                this.estado_embargo = "CADUCADO";
            }
        } catch (Exception e) {
            try {
                 // Si falla, intentamos parsear formato sin hora (date)
                 java.text.SimpleDateFormat sdf2 = new java.text.SimpleDateFormat("yyyy-MM-dd");
                 java.util.Date fechaEmbargo = sdf2.parse(this.fecha_fin_embargo);
                 if (fechaEmbargo.after(new java.util.Date())) this.estado_embargo = "ACTIVO";
                 else this.estado_embargo = "CADUCADO";
            } catch (Exception e2) {
                 this.estado_embargo = "Desconocido";
            }
        }
    }

    @Override
    public String toString() {
        if (estado != null) {
            return nombre_empresa + " (" + estado.substring(0, 1) + ")";
        }
        return nombre_empresa;
    }
}
