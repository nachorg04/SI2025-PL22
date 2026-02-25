package app.dto;

public class EmpresaDisplayDTO {
	private Integer idEmpresa;
	private String nombre;

	public Integer getIdEmpresa() { return idEmpresa; }
	public void setIdEmpresa(Integer idEmpresa) { this.idEmpresa = idEmpresa; }

	// TRUCO: Puerta secreta para SQLite
	public void setId_empresa(Integer id_empresa) { this.idEmpresa = id_empresa; }

	public String getNombre() { return nombre; }
	public void setNombre(String nombre) { this.nombre = nombre; }
}
