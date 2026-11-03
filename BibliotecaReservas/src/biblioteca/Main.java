package biblioteca;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        RepositorioSalas salas = new RepositorioSalasMemoria();
        RepositorioReservas reservas = new RepositorioReservasMemoria();
        
        List<ObservadorReserva> observadores = List.of(
            new NotificadorEstudiante(),
            new RegistroEventos()
        );

        ReservaFacade fachada = new ReservaFacade(salas, reservas, observadores);

        System.out.println("=== PRUEBA ESTACIÓN 6: PATRÓN OBSERVER ===");
        System.out.println("--- 1. Creando reserva (pasa a Confirmada automáticamente) ---");
        Reserva reserva = fachada.realizarReserva(
            new Estudiante("E001", "Laura"),
            LocalDate.now().plusDays(7),
            LocalTime.of(14, 0),
            2,
            4,
            new SeleccionPorCapacidad()
        ).orElseThrow();

        System.out.println("\n--- 2. Cancelando la reserva (pasa a Cancelada) ---");
        reserva.cancelar();
    }
}
