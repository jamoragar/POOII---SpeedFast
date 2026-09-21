# SpeedFast - Desarrollo Orientado a Objetos II

Actividad formativa de la Semana 6 para la asignatura Desarrollo Orientado a Objetos II.

## Objetivo

Registrar pedidos, visualizar su información y asignar repartidores mediante una interfaz gráfica. La aplicación usa Java Swing para simular entregas en paralelo sin bloquear las ventanas.

## Diseño

- `Pedido` representa un pedido con identificador, dirección, tipo, estado y repartidor asignado.
- `EstadoPedido` define el ciclo `PENDIENTE`, `EN_REPARTO` y `ENTREGADO`.
- `TipoPedido` define las categorías comida, encomienda y express.
- `Repartidor` representa al repartidor y su disponibilidad para realizar entregas.
- `ControladorPedidos` mantiene los datos en memoria y coordina el registro y las entregas.
- `VentanaPrincipal`, `VentanaRegistroPedido` y `VentanaListaPedidos` permiten navegar, registrar y consultar pedidos mediante formularios `.form`.
- `Main` inicia la ventana principal en el hilo de eventos de Swing.

## Sincronización

`ControladorPedidos` valida y reserva al repartidor en el hilo de eventos de Swing antes de cambiar el pedido a `EN_REPARTO`. Así, ningún pedido puede iniciar dos entregas y cada repartidor atiende un pedido a la vez.

La entrega simulada con `Thread.sleep()` ocurre en segundo plano mediante `SwingWorker`. Al finalizar, el pedido cambia a `ENTREGADO`, se libera al repartidor y se actualiza la tabla en el hilo de eventos. Esto permite realizar entregas en paralelo sin bloquear la interfaz.

## Estructura

```text
Semana 6/
|-- README.md
|-- S6_ Instrucciones y pauta de evaluación.docx
|-- SpeedFast.iml
`-- src/
    |-- controlador/
    |   `-- ControladorPedidos.java
    |-- main/
    |   `-- Main.java
    |-- modelo/
    |   |-- EstadoPedido.java
    |   |-- Pedido.java
    |   |-- Repartidor.java
    |   `-- TipoPedido.java
    `-- vista/
        |-- VentanaListaPedidos.form
        |-- VentanaListaPedidos.java
        |-- VentanaPrincipal.form
        |-- VentanaPrincipal.java
        |-- VentanaRegistroPedido.form
        `-- VentanaRegistroPedido.java
```

## Ejecución

### Desde IntelliJ IDEA

1. Abrir la carpeta `Semana 6`.
2. Configurar el módulo `SpeedFast` con JDK 17 y habilitar el plugin **Swing UI Designer**. Si es necesario, importar `SpeedFast.iml` y marcar `src` como **Sources Root**.
3. En **Settings > Editor > GUI Designer**, seleccionar **Generate GUI into: Binary class files** y activar **Automatically copy form runtime classes to the output directory**.
4. Ejecutar **Build > Rebuild Project**.
5. Abrir `src/main/Main.java` del módulo `SpeedFast`.
6. Ejecutar el método `main`.

Los archivos `.form` de `src/vista` se editan con el diseñador visual de IntelliJ. La carpeta `ejercicio_s6` contiene el proyecto independiente del profesor y se conserva como referencia.

### Desde la terminal

La compilación con `javac` por sí sola no procesa los archivos `.form`. Para esta semana se debe compilar y ejecutar el proyecto desde IntelliJ IDEA siguiendo los pasos anteriores.

Cada pedido debe registrarse con un ID entero positivo y único, una dirección y un tipo. Las entregas duran tres segundos y la tabla refleja sus cambios de estado. Los datos se mantienen en memoria durante la sesión y se pierden al cerrar la aplicación.

---

Proyecto académico semanal | Duoc UC

## Autor

- Javier A. Moraga Rojas
