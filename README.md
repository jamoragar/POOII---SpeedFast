# Desarrollo Orientado a Objetos II

Repositorio de actividades y proyectos desarrollados para la asignatura Desarrollo Orientado a Objetos II de Duoc UC.

Cada carpeta semanal contiene sus instrucciones, proyectos Java y material de apoyo correspondiente. Los proyectos se mantienen separados para conservar la progresión de los contenidos trabajados.

## Estructura del repositorio

```text
POO II/
|-- Semana 1/
|   |-- README.md
|   |-- src/                         # Primera iteración de SpeedFast
|   `-- PRY2203_Exp1_S1_...docx
|-- Semana 2/
|   |-- README.md
|   |-- src/                         # Segunda iteración de SpeedFast
|   `-- PRY2203_Exp1_S2_...docx
|-- Semana 3/
|   |-- README.md
|   |-- src/                         # Actividad sumativa: SpeedFast
|   `-- PRY2203_Exp1_S3_...docx
|-- Semana 4/
|   |-- README.md
|   |-- src/                         # Cuarta iteración de SpeedFast
|   `-- S4_ Instrucciones y pauta de evaluación.docx
```

## Proyecto SpeedFast

SpeedFast evoluciona durante las cuatro semanas. Modela la gestión de pedidos de comida, encomiendas y compras express mediante conceptos de programación orientada a objetos y concurrencia.

- **Semana 1:** herencia, sobrecarga, sobrescritura y polimorfismo para la asignación de repartidores.
- **Semana 2:** abstracción y cálculo de tiempos estimados de entrega.
- **Semana 3:** actividad sumativa que incorpora interfaces para reservar, despachar, cancelar y consultar el historial de entregas.
- **Semana 4:** actividad formativa que simula entregas en paralelo con `Runnable`, `ExecutorService`, pausas controladas y acceso sincronizado al historial.

La documentacion especifica y las instrucciones de ejecucion de cada iteracion se encuentran en el `README.md` de su respectiva carpeta semanal.

## Requisitos

- Java Development Kit (JDK) 17 o superior.
- IntelliJ IDEA Community o Ultimate, opcional para abrir los proyectos.

## Ejecucion desde la terminal

Ubicate en la carpeta de la semana y compila el proyecto SpeedFast:

```bash
javac -encoding UTF-8 -d out src/main/java/speedfast/*.java
java -cp out speedfast.Main
```

Por ejemplo, para ejecutar la actividad de Semana 4, usa `Semana 4` como directorio de trabajo.

## Autor

- Javier A. Moraga Rojas
