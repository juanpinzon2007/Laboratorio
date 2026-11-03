package biblioteca;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        // 1. Salas disponibles (Estación 2)
        List<Sala> salas = List.of(
                new Sala("A", "Sala A", 2),
                new Sala("B", "Sala B", 4),
                new Sala("C", "Sala C", 6),
                new Sala("D", "Sala D", 8)
        );

        System.out.println("Salas de la biblioteca:");
        salas.forEach(System.out::println);
        System.out.println("--------------------------------------------------");

        // 2. Prueba positiva de construcción con Builder (Estación 3)
        System.out.println("Creando reserva válida...");
        Reserva reserva = new ReservaBuilder()
                .estudiante(new Estudiante("E001", "Laura"))
                .fecha(java.time.LocalDate.now().plusDays(7))
                .horaInicio(java.time.LocalTime.of(14, 0))
                .duracionHoras(2)
                .cantidadPersonas(4)
                .sala(new Sala("B", "Sala B", 4))
                .build();

        System.out.println(reserva);
        System.out.println("--------------------------------------------------");

        // 3. Prueba negativa: forzar un error de capacidad (Estación 3)
        System.out.println("Creando reserva inválida (5 personas en sala de 4)...");
        try {
            Reserva reservaInvalida = new ReservaBuilder()
                    .estudiante(new Estudiante("E002", "Pedro"))
                    .fecha(java.time.LocalDate.now().plusDays(5))
                    .horaInicio(java.time.LocalTime.of(10, 0))
                    .duracionHoras(1)
                    .cantidadPersonas(5) // 5 personas
                    .sala(new Sala("B", "Sala B", 4)) // Sala capacidad 4
                    .build();
        } catch (IllegalStateException e) {
            System.out.println("Construcción rechazada correctamente: " + e.getMessage());
        }
    }
}
