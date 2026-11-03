package biblioteca;
import java.time.LocalDate;
import java.time.LocalTime;

public class Main {
    public static void main(String[] args) {
        RepositorioSalas salas = new RepositorioSalasMemoria();
        RepositorioReservas reservas = new RepositorioReservasMemoria();
        ReservaFacade fachada = new ReservaFacade(salas, reservas);

        System.out.println("=== PRUEBA ESTACIÓN 5: FACADE Y REPOSITORIOS ===");
        
        System.out.println("1. Reserva válida (Laura, 4 personas, por capacidad):");
        fachada.realizarReserva(
            new Estudiante("E001", "Laura"),
            LocalDate.now().plusDays(7),
            LocalTime.of(14, 0),
            2,
            4,
            new SeleccionPorCapacidad()
        ).ifPresent(System.out::println);

        System.out.println("\n2. Reserva sin sala adecuada (10 personas):");
        fachada.realizarReserva(
            new Estudiante("E002", "Pedro"),
            LocalDate.now().plusDays(8),
            LocalTime.of(10, 0),
            2,
            10,
            new SeleccionPorCapacidad()
        );
    }
}
