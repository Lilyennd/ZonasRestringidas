package cl.GestionDrones.v1.ZonasRestringidas.dto;

import jakarta.validation.constraints.NotBlank;

public record VerificarZonaRequest (@NotBlank(message = "Las coordenadas de origen son obligatorias")
    String coordenadasOrigen,

    @NotBlank(message = "Las coordenadas de destino son obligatorias")
    String coordenadasDestino) {

}
