package cl.GestionDrones.v1.ZonasRestringidas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import cl.GestionDrones.v1.ZonasRestringidas.model.ZonasRestringidas;


@Repository
public interface ZonasRestringidasRepository extends JpaRepository<ZonasRestringidas, Integer> {
    

    
    @Query(value = "SELECT * FROM zonas_restringidas WHERE nombre_lugar = :nombre", nativeQuery = true)
    List<ZonasRestringidas> buscarPorNombreLugar(@Param("nombre") String nombre);

    @Query(value = "SELECT * FROM zonas_restringidas WHERE motivo = :motivo", nativeQuery = true)
    List<ZonasRestringidas> buscarPorMotivo(@Param("motivo") String motivo);
    
    @Query(value = "SELECT COUNT(*) FROM zonas_restringidas", nativeQuery = true)
    long contarTotalZonas();
    
}
