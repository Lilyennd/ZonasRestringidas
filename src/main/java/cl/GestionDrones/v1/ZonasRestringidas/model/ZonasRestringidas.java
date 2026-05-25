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
    private int id;

    @Column(name = "nombre_lugar", nullable = false, length = 100)
    private String nombreLugar; // Aquí guardas la comuna (ej: "Pudahuel") o Región (ej: "Metropolitana")

    @Column(name = "motivo", length = 255)
    private String motivo; // Ej: "Cercanía a Aeropuerto Arturo Merino Benítez"

    public ZonasRestringidas() {}

    public ZonasRestringidas(int id, String nombreLugar, String tipoLimite, String motivo) {
        this.id = id;
        this.nombreLugar = nombreLugar;
        this.motivo = motivo;
    }


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombreLugar() { return nombreLugar; }
    public void setNombreLugar(String nombreLugar) { this.nombreLugar = nombreLugar; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
}