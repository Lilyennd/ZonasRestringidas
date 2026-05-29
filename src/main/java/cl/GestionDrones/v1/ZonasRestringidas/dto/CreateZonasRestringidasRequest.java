package cl.GestionDrones.v1.ZonasRestringidas.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record CreateZonasRestringidasRequest(
    @NotBlank(message = "El nombre del lugar no puede estar vacío") 
    String nombreLugar,

    @NotBlank(message = "El motivo de la restricción no puede estar vacío") 
    String motivo,

    @NotNull(message = "La latitud es obligatoria") 
    Double latitud,

    @NotNull(message = "La longitud es obligatoria") 
    Double longitud,

    @NotNull(message = "El radio en metros es obligatorio") 
    Integer radioMetros
) {}