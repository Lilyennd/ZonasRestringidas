package cl.GestionDrones.v1.ZonasRestringidas.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {


@ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> manejarValidaciones(MethodArgumentNotValidException ex) {
        Map<String, Object> respuesta = new HashMap<>();
        Map<String, String> errores = new HashMap<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errores.put(error.getField(), error.getDefaultMessage());
        }

        respuesta.put("timestamp", LocalDateTime.now());
        respuesta.put("mensaje", "Error en los datos enviados");
        respuesta.put("errores", errores);
        respuesta.put("status", HttpStatus.BAD_REQUEST.value());

        return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
    }

@ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> manejarExceptionGeneral(Exception ex) {
        Map<String, Object> respuesta = new HashMap<>();
        
        respuesta.put("timestamp", LocalDateTime.now());
        respuesta.put("mensaje", "Ocurrió un error inesperado en el servidor");
        respuesta.put("detalle", ex.getMessage());
        respuesta.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());

        return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
    }

@ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> manejarRecursoNoEncontrado(ResourceNotFoundException ex) {
        Map<String, Object> respuesta = new HashMap<>();
        
        respuesta.put("timestamp", LocalDateTime.now());
        respuesta.put("mensaje", ex.getMessage());
        respuesta.put("error", "Recurso no encontrado");
        respuesta.put("status", HttpStatus.NOT_FOUND.value());

        return new ResponseEntity<>(respuesta, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> manejarMensajeIlegible(HttpMessageNotReadableException ex) {
        Map<String, Object> response = new HashMap<>();
        
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("problema", "Error de formato en el JSON enviado");
        
        String detalleError = "Revisa que los tipos de datos sean correctos (por ejemplo, no enviar texto en campos numéricos o de fecha).";
        if (ex.getMostSpecificCause() != null) {
            detalleError += " Detalle técnico: " + ex.getMostSpecificCause().getMessage();
        }
        response.put("detalle", detalleError);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}