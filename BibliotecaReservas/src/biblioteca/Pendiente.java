package biblioteca;
public class Pendiente implements EstadoReserva {
    public String nombre() { return "Pendiente"; }
    public void confirmar(Reserva reserva) {}
    public void cancelar(Reserva reserva) {}
    public void finalizar(Reserva reserva) {}
}
