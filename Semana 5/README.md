# SpeedFast - Desarrollo Orientado a Objetos II

Actividad formativa de la Semana 5 para la asignatura Desarrollo Orientado a Objetos II.

## Objetivo

Simular repartidores que retiran pedidos desde una zona de carga compartida. La simulación usa sincronización para garantizar que cada pedido sea retirado y entregado por un único repartidor.

## Diseño

- `Pedido` representa un pedido con identificador, dirección y estado.
- `EstadoPedido` define el ciclo `PENDIENTE`, `EN_REPARTO` y `ENTREGADO`.
- `ZonaDeCarga` es el recurso compartido que mantiene los pedidos pendientes.
- `Repartidor` implementa `Runnable` y procesa pedidos hasta vaciar la zona de carga.
- `Main` crea los pedidos, inicia tres repartidores y verifica los estados finales.

## Sincronización

`ZonaDeCarga` utiliza una `Queue<Pedido>` y métodos `synchronized` para agregar y retirar pedidos. Al retirar un pedido, la zona lo elimina de la cola y lo cambia a `EN_REPARTO` dentro de la misma operación sincronizada. Así, ningún otro repartidor puede retirar el mismo pedido.

La entrega simulada con `Thread.sleep()` ocurre fuera del bloqueo. Esto permite que otros repartidores retiren y entreguen pedidos en paralelo.

## Estructura

```text
Semana 5/
|-- README.md
|-- S5_ Instrucciones y pauta de evaluación.docx
|-- SpeedFast.iml
`-- src/
    `-- main/
        `-- java/
            `-- speedfast/
                |-- EstadoPedido.java
                |-- Main.java
                |-- Pedido.java
                |-- Repartidor.java
                `-- ZonaDeCarga.java
```

## Ejecución

### Desde IntelliJ IDEA

1. Abrir la carpeta `Semana 5`.
2. Configurar el proyecto con JDK 17.
3. Abrir `src/main/java/speedfast/Main.java`.
4. Ejecutar el método `main`.

### Desde la terminal

```bash
javac -encoding UTF-8 -d out src/main/java/speedfast/*.java
java -cp out speedfast.Main
```

El orden de los mensajes puede variar entre ejecuciones porque los tres repartidores trabajan en paralelo. Cada pedido debe aparecer una sola vez en estado `EN_REPARTO` y finalizar en `ENTREGADO`.

---

Proyecto académico semanal | Duoc UC

## Autor

- Javier A. Moraga Rojas
