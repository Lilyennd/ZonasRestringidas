package cl.GestionDrones.v1.ZonasRestringidas.service;
import java.util.List;


import org.springframework.stereotype.Service;

import cl.GestionDrones.v1.ZonasRestringidas.dto.UpdateZonasRequest;
import cl.GestionDrones.v1.ZonasRestringidas.mapper.ZonasRestringidasMapper;
import cl.GestionDrones.v1.ZonasRestringidas.model.ZonasRestringidas;
import cl.GestionDrones.v1.ZonasRestringidas.repository.ZonasRestringidasRepository;

@Service
public class ZonasRestringidasService {

    private final ZonasRestringidasRepository repository;

    public ZonasRestringidasService(ZonasRestringidasRepository repository) {
        this.repository = repository;
    }

    // 1. Obtener todas las zonas
    public List<ZonasRestringidas> getAllZonas() {
        return repository.findAll(); 
    }

    //get
    public ZonasRestringidas getZonaById(Integer id) {
        return repository.findById(id).orElse(null);
    }

   //post
    public ZonasRestringidas createZona(ZonasRestringidas zona) {
        return repository.save(zona);
    }

    //put
    public ZonasRestringidas updateZona(Integer id, UpdateZonasRequest request) {
        ZonasRestringidas zonaExistente = getZonaById(id);
        if (zonaExistente == null) {
            return null;}
        ZonasRestringidasMapper.updateEntity(zonaExistente, request);
        return repository.save(zonaExistente);
    }

    // delete
    public boolean deleteZona(Integer id) {
        if (!repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }

    // contar
    public long getTotalZonas() {
        return repository.contarTotalZonas();
    }

    // busq nombre
    public List<ZonasRestringidas> buscarPorNombre(String nombreLugar) {
        return repository.buscarPorNombreLugar(nombreLugar);
    }

    // busq motivo 
    public List<ZonasRestringidas> buscarPorMotivo(String motivo) {
        return repository.buscarPorMotivo(motivo);
    }

}
