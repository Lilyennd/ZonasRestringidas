package cl.GestionDrones.v1.ZonasRestringidas.mapper;



import cl.GestionDrones.v1.ZonasRestringidas.dto.CreateZonasRestringidasRequest;
import cl.GestionDrones.v1.ZonasRestringidas.dto.UpdateZonasRequest;
import cl.GestionDrones.v1.ZonasRestringidas.model.ZonasRestringidas;



public class ZonasRestringidasMapper {

public static ZonasRestringidas toEntity(CreateZonasRestringidasRequest request) {
        ZonasRestringidas zona = new ZonasRestringidas();
        zona.setNombreLugar(request.nombreLugar());
        zona.setMotivo(request.motivo());
        zona.setLatitud(request.latitud());
        zona.setLongitud(request.longitud());
        zona.setRadioMetros(request.radioMetros());
        return zona;
    }

 public static void updateEntity(ZonasRestringidas zonaExistente, UpdateZonasRequest request) {
        zonaExistente.setNombreLugar(request.nombreLugar());
        zonaExistente.setMotivo(request.motivo());
        zonaExistente.setLatitud(request.latitud());
        zonaExistente.setLongitud(request.longitud());
        zonaExistente.setRadioMetros(request.radioMetros());
    }

}
