package biblioteca;

import java.time.LocalDate;
import java.time.LocalTime;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== PRUEBAS ESTACIÓN 7: PATRÓN STATE ===");
        
        Estudiante est = new Estudiante("E999", "Prueba State");
        Sala sala = new Sala("S1", "Sala Pruebas", 5);

        System.out.println("\n--- 1 y 2. Confirmar pendiente y Cancelar confirmada ---");
        Reserva r1 = new ReservaBuilder().estudiante(est).fecha(LocalDate.now().plusDays(1))
            .horaInicio(LocalTime.of(10, 0)).duracionHoras(1).cantidadPersonas(2).sala(sala).build();
        
        System.out.println("Estado inicial: " + r1.getNombreEstado());
        r1.confirmar();
        System.out.println("Después de confirmar: " + r1.getNombreEstado());
        r1.cancelar();
        System.out.println("Después de cancelar: " + r1.getNombreEstado());

        System.out.println("\n--- 3. Intentar confirmar una cancelada ---");
        try {
            r1.confirmar();
        } catch (IllegalStateException e) {
            System.out.println("Error capturado: " + e.getMessage());
        }

        System.out.println("\n--- 4. Finalizar otra reserva confirmada ---");
        Reserva r2 = new ReservaBuilder().estudiante(est).fecha(LocalDate.now().plusDays(2))
            .horaInicio(LocalTime.of(11, 0)).duracionHoras(1).cantidadPersonas(2).sala(sala).build();
        r2.confirmar();
        System.out.println("Estado antes de finalizar: " + r2.getNombreEstado());
        r2.finalizar();
        System.out.println("Después de finalizar: " + r2.getNombreEstado());

        System.out.println("\n--- 5. Intentar cancelar una finalizada ---");
        try {
            r2.cancelar();
        } catch (IllegalStateException e) {
            System.out.println("Error capturado: " + e.getMessage());
        }
    }
}
