package cl.GestionDrones.v1.ZonasRestringidas.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import cl.GestionDrones.v1.ZonasRestringidas.dto.CreateZonasRestringidasRequest;
import cl.GestionDrones.v1.ZonasRestringidas.dto.UpdateZonasRequest;
import cl.GestionDrones.v1.ZonasRestringidas.mapper.ZonasRestringidasMapper;
import cl.GestionDrones.v1.ZonasRestringidas.model.ZonasRestringidas;
import cl.GestionDrones.v1.ZonasRestringidas.service.ZonasRestringidasService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "Zonas Restringidas", description = "Operaciones relacionadas con el control y delimitación de zonas con prohibición de vuelo")
@RestController
@RequestMapping("/api/v1/ZonasRestringidas")
public class ZonasRestringidasController {

    private final ZonasRestringidasService service;

    public ZonasRestringidasController(ZonasRestringidasService service) {
        this.service = service;
    }

    @Operation(summary = "Obtener todas las zonas restringidas", description = "Retorna una lista con todas las zonas de vuelo restringidas del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de zonas obtenida con éxito", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ZonasRestringidas.class))),
        @ApiResponse(responseCode = "204", description = "No existen zonas restringidas registradas", content = @Content)
    })
    @GetMapping
    public ResponseEntity<Map<String, Object>> listarTodas() {
        List<ZonasRestringidas> zonas = service.getAllZonas();
        Map<String, Object> response = new HashMap<>();

        if (zonas.isEmpty()) {
            response.put("status", HttpStatus.NO_CONTENT.value());
            response.put("mensaje", "No existen zonas restringidas registradas");
            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        }

        response.put("status", HttpStatus.OK.value());
        response.put("total_zonas", service.getTotalZonas());
        response.put("datos", zonas);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Buscar zona restringida por ID", description = "Busca y retorna los detalles de una zona prohibida utilizando su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Zona restringida encontrada con éxito", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ZonasRestringidas.class))),
        @ApiResponse(responseCode = "404", description = "La zona con el ID proporcionado no existe", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> obtenerPorId(
        @Parameter(description = "ID único de la zona restringida", required = true, example = "1")
        @PathVariable Integer id
    ) {
        ZonasRestringidas zona = service.getZonaById(id);
        Map<String, Object> response = new HashMap<>();

        if (zona == null) {
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("mensaje", "La zona con el ID proporcionado no existe");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        response.put("status", HttpStatus.OK.value());
        response.put("datos", zona);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Registrar una nueva zona restringida", description = "Crea una nueva delimitación espacial con restricción de vuelo")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Zona restringida registrada exitosamente", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ZonasRestringidas.class))),
        @ApiResponse(responseCode = "400", description = "Errores de validación en los datos enviados", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Map<String, Object>> crear(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Estructura JSON de la nueva zona restringida", required = true)
            @Valid @RequestBody CreateZonasRestringidasRequest request, 
            BindingResult result) {
        
        if (result.hasErrors()) {
            return manejarErrores(result);
        }

        ZonasRestringidas zona = ZonasRestringidasMapper.toEntity(request);
        ZonasRestringidas nuevaZona = service.createZona(zona);

        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.CREATED.value());
        response.put("mensaje", "Zona restringida registrada exitosamente");
        response.put("datos", nuevaZona);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar zona restringida", description = "Modifica los límites o parámetros de una zona prohibida usando su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Zona restringida actualizada exitosamente", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ZonasRestringidas.class))),
        @ApiResponse(responseCode = "400", description = "Errores de validación en los datos modificados", content = @Content),
        @ApiResponse(responseCode = "404", description = "No se puede actualizar. La zona con el ID proporcionado no existe", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizar(
            @Parameter(description = "ID de la zona restringida a actualizar", required = true, example = "1")
            @PathVariable Integer id, 
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Estructura JSON con los nuevos parámetros de la zona", required = true)
            @Valid @RequestBody UpdateZonasRequest request, 
            BindingResult result) {

        if (result.hasErrors()) {
            return manejarErrores(result);
        }

        ZonasRestringidas zonaActualizada = service.updateZona(id, request);
        Map<String, Object> response = new HashMap<>();

        if (zonaActualizada == null) {
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("mensaje", "No se puede actualizar. La zona con el ID proporcionado no existe");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        response.put("status", HttpStatus.OK.value());
        response.put("mensaje", "Zona restringida actualizada exitosamente");
        response.put("datos", zonaActualizada);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Eliminar zona restringida", description = "Elimina de forma permanente una zona prohibida mediante su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Zona restringida eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "No se puede eliminar. La zona con el ID proporcionado no existe", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminar(
        @Parameter(description = "ID de la zona restringida a eliminar", required = true, example = "1")
        @PathVariable Integer id
    ) {
        boolean eliminado = service.deleteZona(id);
        Map<String, Object> response = new HashMap<>();

        if (!eliminado) {
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("mensaje", "No se puede eliminar. La zona con el ID proporcionado no existe");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        response.put("status", HttpStatus.OK.value());
        response.put("mensaje", "Zona restringida eliminada exitosamente");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Buscar zonas por nombre de lugar", description = "Filtra y retorna las zonas restringidas que coincidan con un lugar")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Zonas encontradas con éxito", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ZonasRestringidas.class))),
        @ApiResponse(responseCode = "404", description = "No se encontraron zonas con el nombre especificado", content = @Content)
    })
    @GetMapping("/buscar/nombre/{lugar}")
    public ResponseEntity<Map<String, Object>> buscarPorNombre(
        @Parameter(description = "Nombre o parte del nombre del lugar prohibido", required = true, example = "Aeropuerto")
        @PathVariable String lugar
    ) {
        List<ZonasRestringidas> zonas = service.buscarPorNombre(lugar);
        Map<String, Object> response = new HashMap<>();

        if (zonas.isEmpty()) {
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("mensaje", "No se encontraron zonas con el nombre: " + lugar);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        response.put("status", HttpStatus.OK.value());
        response.put("datos", zonas);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Buscar zonas por motivo de restricción", description = "Retorna un listado de zonas filtrando de acuerdo al texto de prohibición")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Zonas encontradas con éxito", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ZonasRestringidas.class))),
        @ApiResponse(responseCode = "404", description = "No se encontraron zonas con el motivo de restricción provisto", content = @Content)
    })
    @GetMapping("/buscar/motivo/{texto}") 
    public ResponseEntity<Map<String, Object>> buscarPorMotivo(
        @Parameter(description = "Palabra clave o descripción del motivo de restricción", required = true, example = "Militar")
        @PathVariable String texto
    ) {
        List<ZonasRestringidas> zonas = service.buscarPorMotivo(texto);
        Map<String, Object> response = new HashMap<>();

        if (zonas.isEmpty()) {
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("mensaje", "No se encontraron zonas con el motivo: " + texto);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        response.put("status", HttpStatus.OK.value());
        response.put("datos", zonas);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Verificar coordenadas en zona restringida", description = "Analiza si un par de coordenadas de origen y destino intersectan con alguna zona restringida")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Análisis completado (Retorna true si interfiere, false en caso contrario)", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = Boolean.class))),
        @ApiResponse(responseCode = "400", description = "Las coordenadas de origen y destino son obligatorias", content = @Content)
    })
    @PostMapping("/verificar")
    public ResponseEntity<Object> verificarCoordenadas(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Objeto JSON que contiene 'coordenadasOrigen' y 'coordenadasDestino'", required = true)
        @RequestBody Map<String, String> body
    ) {

        String coordenadasOrigen = body.get("coordenadasOrigen");
        String coordenadasDestino = body.get("coordenadasDestino");

        if (coordenadasOrigen == null || coordenadasDestino == null) {
            Map<String, Object> error = new HashMap<>();
            error.put("status", HttpStatus.BAD_REQUEST.value());
            error.put("error", "Las coordenadas de origen y destino son obligatorias");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        boolean estaRestringida = service.verificarCoordenadas(coordenadasOrigen, coordenadasDestino);
        return ResponseEntity.ok(estaRestringida);
    }

    private ResponseEntity<Map<String, Object>> manejarErrores(BindingResult result) {
        Map<String, Object> response = new HashMap<>();
        Map<String, String> errores = new HashMap<>();

        for (FieldError error : result.getFieldErrors()) {
            errores.put(error.getField(), error.getDefaultMessage());
        }

        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("problema", "Errores de validación en los datos enviados");
        response.put("errores", errores);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}