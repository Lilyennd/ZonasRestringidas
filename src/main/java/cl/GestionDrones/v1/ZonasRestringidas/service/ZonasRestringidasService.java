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


    public boolean verificarCoordenadas(String coordenadasOrigen, String coordenadasDestino) {
    List<ZonasRestringidas> todasLasZonas = repository.findAll();

    double[] origen  = parsearCoordenadas(coordenadasOrigen);
    double[] destino = parsearCoordenadas(coordenadasDestino);

    for (ZonasRestringidas zona : todasLasZonas) {
        double distOrigen  = calcularDistanciaMetros(origen[0],  origen[1],  zona.getLatitud(), zona.getLongitud());
        double distDestino = calcularDistanciaMetros(destino[0], destino[1], zona.getLatitud(), zona.getLongitud());

        if (distOrigen <= zona.getRadioMetros() || distDestino <= zona.getRadioMetros()) {
            return true;
        }
    }
    return false;
}

private double[] parsearCoordenadas(String coordenadas) {
    String[] partes = coordenadas.split(",");
    return new double[]{
        Double.parseDouble(partes[0].trim()),
        Double.parseDouble(partes[1].trim())
    };
}

private double calcularDistanciaMetros(double lat1, double lon1, double lat2, double lon2) {
    final int RADIO_TIERRA = 6_371_000;
    double dLat = Math.toRadians(lat2 - lat1);
    double dLon = Math.toRadians(lon2 - lon1);
    double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
             + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
             * Math.sin(dLon / 2) * Math.sin(dLon / 2);
    return RADIO_TIERRA * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
}

}
