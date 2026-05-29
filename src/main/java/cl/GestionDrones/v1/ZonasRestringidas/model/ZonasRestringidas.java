package cl.GestionDrones.v1.ZonasRestringidas.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "zonas_restringidas")
public class ZonasRestringidas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nombre_lugar", nullable = false, length = 100)
    private String nombreLugar;

    @Column(name = "motivo", length = 255)
    private String motivo;

    @Column(name = "latitud", nullable = false)
    private Double latitud;

    @Column(name = "longitud", nullable = false)
    private Double longitud;


    @Column(name = "radio_metros", nullable = false)
    private Integer radioMetros;

    public ZonasRestringidas() {}

    public ZonasRestringidas(Integer id, String nombreLugar, String motivo, Double latitud, Double longitud, Integer radioMetros) {
        this.id = id;
        this.nombreLugar = nombreLugar;
        this.motivo = motivo;
        this.latitud = latitud;
        this.longitud = longitud;
        this.radioMetros = radioMetros;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombreLugar() { return nombreLugar; }
    public void setNombreLugar(String nombreLugar) { this.nombreLugar = nombreLugar; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public Double getLatitud() { return latitud; }
    public void setLatitud(Double latitud) { this.latitud = latitud; }

    public Double getLongitud() { return longitud; }
    public void setLongitud(Double longitud) { this.longitud = longitud; }

    public Integer getRadioMetros() { return radioMetros; }
    public void setRadioMetros(Integer radioMetros) { this.radioMetros = radioMetros; }
}