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
import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



@RestController
@RequestMapping("/api/v1/ZonasRestringidas")
public class ZonasRestringidasController {

    private final ZonasRestringidasService service;

    public ZonasRestringidasController(ZonasRestringidasService service) {
        this.service = service;
    }

    // POST
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

    // GET
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> obtenerPorId(@PathVariable Integer id) {
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

    // POST
    @PostMapping
    public ResponseEntity<Map<String, Object>> crear(
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

    // PUT
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizar(
            @PathVariable Integer id, 
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

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable Integer id) {
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
    
    @GetMapping("/buscar/nombre/{lugar}")
    public ResponseEntity<Map<String, Object>> buscarPorNombre(@PathVariable String lugar) {
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

    
    @GetMapping("/buscar/motivo/{texto}") 
    public ResponseEntity<Map<String, Object>> buscarPorMotivo(@PathVariable String texto) {
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


    @PostMapping("/verificar")
    public ResponseEntity<Object> verificarCoordenadas(
    @RequestBody Map<String, String> body) {

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
}