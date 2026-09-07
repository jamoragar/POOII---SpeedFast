# Desarrollo Orientado a Objetos II

Repositorio de actividades y proyectos desarrollados para la asignatura Desarrollo Orientado a Objetos II de Duoc UC.

Cada carpeta semanal contiene sus instrucciones, proyectos Java y material de apoyo correspondiente. Los proyectos se mantienen separados para conservar la progresion de los contenidos trabajados.

## Estructura del repositorio

```text
POO II/
|-- Semana 1/
|   |-- SistemaNotificaciones/
|   |-- src/                         # Primera iteracion de SpeedFast
|   `-- PRY2203_Exp1_S1_...docx
|-- Semana 2/
|   |-- SistemaTransporte/
|   |-- src/                         # Segunda iteracion de SpeedFast
|   `-- PRY2203_Exp1_S2_...docx
|-- Semana 3/
|   |-- BibliotecaSimple/
|   |-- src/                         # Actividad sumativa: SpeedFast
|   `-- PRY2203_Exp1_S3_...docx
|-- Semana 4/
|   `-- S4_ Instrucciones y pauta de evaluacion.docx
|-- PRY2203_Exp1_guia_aprendizaje_...docx
`-- PRY2203_Exp2_guia_de_aprendizaje_...docx
```

## Proyecto SpeedFast

SpeedFast evoluciona durante las primeras tres semanas. Modela la gestion de pedidos de comida, encomiendas y compras express mediante conceptos de programacion orientada a objetos.

- **Semana 1:** herencia, sobrecarga, sobrescritura y polimorfismo para la asignacion de repartidores.
- **Semana 2:** abstraccion y calculo de tiempos estimados de entrega.
- **Semana 3:** actividad sumativa que incorpora interfaces para reservar, despachar, cancelar y consultar el historial de entregas.

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

Por ejemplo, para ejecutar la actividad de Semana 3, usa `Semana 3` como directorio de trabajo.

## Autor

- Javier A. Moraga Rojas
