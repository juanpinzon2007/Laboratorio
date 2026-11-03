# Sistema de Reservas de Salas de Biblioteca (BibliotecaReservas)

Prototipo académico desarrollado en **Java SE 17** (sin dependencias externas ni frameworks), enfocado en la aplicación práctica de **Patrones de Diseño de Software** y los **Principios SOLID**, garantizando alta cohesión, bajo acoplamiento y extensibilidad.

---

## 🏛️ Arquitectura del Proyecto y Organización de Paquetes

Todo el código fuente está organizado estrictamente bajo el paquete `biblioteca` en `BibliotecaReservas/src/biblioteca/`:

```text
BibliotecaReservas/
└── src/
    └── biblioteca/
        ├── Main.java                         # Punto de entrada y orquestación de pruebas
        │
        ├── [Dominio]
        ├── Estudiante.java                   # Entidad del estudiante solicitante
        ├── Sala.java                         # Entidad de sala (código, nombre, capacidad)
        ├── Reserva.java                      # Entidad central con ciclo de vida y observadores
        │
        ├── [Patrón Builder]
        ├── ReservaBuilder.java               # Construcción paso a paso y validaciones
        │
        ├── [Patrón Strategy]
        ├── EstrategiaSeleccionSala.java      # Interfaz de estrategia de selección
        ├── SeleccionPorCapacidad.java        # Estrategia por mejor ajuste de capacidad
        ├── SeleccionSalaPreferida.java       # Estrategia de sala preferida con validación
        │
        ├── [Capa de Persistencia / Repositorios]
        ├── RepositorioSalas.java             # Abstracción para consultar salas disponibles
        ├── RepositorioSalasMemoria.java      # Implementación en memoria de salas
        ├── RepositorioReservas.java          # Abstracción para registro de reservas
        ├── RepositorioReservasMemoria.java   # Implementación en memoria de reservas
        │
        ├── [Patrón Facade]
        ├── ReservaFacade.java                # Fachada que simplifica y coordina el caso de uso
        │
        ├── [Patrón Observer]
        ├── ObservadorReserva.java            # Contrato para los suscriptores a eventos
        ├── NotificadorEstudiante.java        # Observador que notifica cambios al estudiante
        ├── RegistroEventos.java              # Observador que registra logs en consola
        │
        └── [Patrón State]
            ├── EstadoReserva.java            # Interfaz polimórfica para estados
            ├── Pendiente.java                # Estado inicial (permite confirmar o cancelar)
            ├── Confirmada.java               # Estado activo (permite cancelar o finalizar)
            ├── Cancelada.java                # Estado terminal (rechaza confirmación)
            └── Finalizada.java               # Estado terminal (rechaza cancelaciones)
```

---

## 🧩 Patrones de Diseño Implementados

1. **Builder (`ReservaBuilder`):**  
   Encapsula la creación del objeto complejo `Reserva`. Centraliza validaciones de precondición (datos obligatorios, fechas futuras, duración, concurrencia y validación de capacidad de sala) evitando constructores largos (*Long Parameter List*).

2. **Strategy (`EstrategiaSeleccionSala`):**  
   Permite desacoplar el algoritmo de asignación de salas. Facilita alternar dinámicamente entre `SeleccionPorCapacidad` (sala con menor capacidad sobrante) y `SeleccionSalaPreferida` (asignación a sala específica si cumple la capacidad requerida).

3. **Facade (`ReservaFacade`):**  
   Ofrece una interfaz unificada de alto nivel para el cliente (`Main`). Coordina la consulta de disponibilidad, la selección con la estrategia elegida, la construcción de la reserva, la suscripción de observadores, la persistencia en el repositorio y la confirmación inicial.

4. **Observer (`ObservadorReserva`):**  
   Establece una relación uno a muchos desacoplada. Ante cada transición en `Reserva.cambiarEstado()`, se notifica automáticamente a `NotificadorEstudiante` y `RegistroEventos` sin que el dominio conozca las implementaciones concretas.

5. **State (`EstadoReserva`):**  
   Encapsula el comportamiento y reglas de transición de cada fase (`Pendiente`, `Confirmada`, `Cancelada`, `Finalizada`). Elimina estructuras condicionales dispersas (`if`/`else` o `switch`) sobre cadenas de texto (*Primitive Obsession*). Las transiciones inválidas arrojan `IllegalStateException` y no emiten eventos espurios.

---

## 📐 Principios SOLID Aplicados

- **S — Responsabilidad Única (SRP):** Cada clase tiene un único rol bien definido (construir, seleccionar, notificar, persistir o gestionar estados), evitando *God Objects*.
- **O — Abierto / Cerrado (OCP):** El sistema permite incorporar nuevas estrategias u observadores creando nuevas clases que implementen sus contratos, sin modificar el código cliente ni `Reserva`.
- **L — Sustitución de Liskov (LSP):** Todas las implementaciones de repositorios, estrategias y estados respetan sus contratos y precondiciones de forma transparente.
- **I — Segregación de Interfaces (ISP):** Interfaces pequeñas y atómicas (`EstrategiaSeleccionSala`, `RepositorioSalas`, `RepositorioReservas`, `ObservadorReserva`), evitando interfaces monolíticas con métodos innecesarios.
- **D — Inversión de Dependencias (DIP):** `ReservaFacade` y `Reserva` dependen exclusivamente de abstracciones (interfaces), desacoplándose de las clases concretas en memoria o de consola.

---

## 🧪 Matriz de Trazabilidad y Pruebas Mínimas (P01 a P11)

| ID | Escenario | Resultado esperado | Patrón / Componente | Clase(s) responsable(s) |
| :---: | :--- | :--- | :--- | :--- |
| **P01** | Construcción con todos los datos | Se crea una reserva válida. | Builder | `ReservaBuilder`, `Reserva` |
| **P02** | Construcción sin estudiante | Se rechaza con mensaje claro. | Builder | `ReservaBuilder` |
| **P03** | Capacidad para 4 personas | Selecciona Sala B (capacidad 4). | Strategy | `SeleccionPorCapacidad` |
| **P04** | Preferida C para 4 personas | Selecciona Sala C (capacidad 6). | Strategy | `SeleccionSalaPreferida` |
| **P05** | Preferida B para 7 personas | No selecciona sala ($4 < 7$). | Strategy | `SeleccionSalaPreferida` |
| **P06** | Reserva exitosa | Se registra y confirma. | Facade | `ReservaFacade`, `RepositorioReservasMemoria` |
| **P07** | Cambio a Confirmada | Ambos observadores reciben evento. | Observer + State | `Pendiente`, `Confirmada`, `ObservadorReserva` |
| **P08** | Cancelación de confirmada | Cambia a Cancelada y notifica. | State + Observer | `Confirmada`, `Cancelada`, `ObservadorReserva` |
| **P09** | Confirmar cancelada | Se rechaza con excepción; no notifica. | State | `Cancelada` |
| **P10** | Solicitud para 10 personas | No crea reserva por falta de capacidad. | Strategy + Facade | `SeleccionPorCapacidad`, `ReservaFacade` |
| **P11** | Finalizar confirmada | Cambia a Finalizada y notifica. | State + Observer | `Confirmada`, `Finalizada`, `ObservadorReserva` |

---

## 🌿 Flujo de Ramas y Estaciones de Trabajo

El desarrollo se realizó de forma incremental mediante ramas dedicadas para cada hito del laboratorio:

- `main`: Rama principal de consolidación del proyecto completo.
- `estacion1`: Preparación del entorno y análisis del dominio.
- `estacion2`: Modelado de clases del dominio y primera versión ejecutable.
- `estacion3`: Implementación del patrón Builder (`ReservaBuilder`).
- `estacion4`: Implementación del patrón Strategy (`EstrategiaSeleccionSala`).
- `estacion5`: Implementación de repositorios desacoplados y patrón Facade (`ReservaFacade`).
- `estacion6`: Implementación del patrón Observer (`NotificadorEstudiante`, `RegistroEventos`).
- `estacion7`: Implementación del patrón State y validación del ciclo de vida.
- `estacion8`: Integración final y suite de validación completa.

---

## 🚀 Compilación y Ejecución

Al no requerir herramientas de compilación externas, el proyecto se compila y ejecuta con cualquier JDK 17 o superior:

### En PowerShell / Windows:
```powershell
# Compilar todas las clases hacia el directorio de salida out
& "javac" --release 17 -encoding UTF-8 -d "BibliotecaReservas/out" (Get-ChildItem "BibliotecaReservas/src/biblioteca/*.java").FullName

# Ejecutar la aplicación principal
& "java" "-Dfile.encoding=UTF-8" -cp "BibliotecaReservas/out" biblioteca.Main
```

### En Bash / Linux / macOS:
```bash
# Compilar
javac --release 17 -encoding UTF-8 -d BibliotecaReservas/out BibliotecaReservas/src/biblioteca/*.java

# Ejecutar
java -Dfile.encoding=UTF-8 -cp BibliotecaReservas/out biblioteca.Main
```
