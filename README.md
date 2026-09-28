# SpeedFast - Desarrollo Orientado a Objetos II

Repositorio de actividades semanales para la asignatura Desarrollo Orientado a Objetos II de Duoc UC.

## Objetivo

Desarrollar el sistema de gestión de pedidos de SpeedFast mediante programación orientada a objetos, concurrencia, interfaces gráficas y persistencia con JDBC. Cada semana mantiene un proyecto independiente para conservar la progresión de los contenidos trabajados.

## Diseño

- **Semana 1:** integra herencia, sobrecarga, sobrescritura y polimorfismo para la asignación de repartidores.
- **Semana 2:** incorpora abstracción y cálculo de tiempos estimados de entrega.
- **Semana 3:** incorpora interfaces para reservar, despachar, cancelar y consultar el historial de entregas.
- **Semana 4:** simula entregas en paralelo con `Runnable`, `ExecutorService` y acceso sincronizado al historial.
- **Semana 5:** sincroniza el retiro de pedidos desde una zona de carga compartida para evitar entregas duplicadas.
- **Semana 6:** incorpora ventanas Swing para registrar y listar pedidos, asignar repartidores y simular entregas concurrentes.
- **Semana 7:** incorpora MySQL y JDBC mediante DAO para guardar y consultar pedidos, repartidores y entregas.

## Sincronización

Las semanas 4 y 5 incorporan ejecución concurrente y acceso sincronizado a los recursos compartidos. Esto permite que varios repartidores procesen pedidos sin duplicar las entregas.

En la Semana 6, `SwingWorker` ejecuta la espera simulada en segundo plano y el hilo de eventos de Swing actualiza los datos y la interfaz. Esto permite realizar entregas en paralelo sin bloquear las ventanas.

En la Semana 7, las operaciones JDBC se ejecutan en segundo plano y la asignación de entregas utiliza transacciones en MySQL.

## Estructura

```text
POO II/
|-- Semana 1/
|   |-- README.md
|   |-- src/                         # Actividad Formativa 1 de SpeedFast
|   `-- PRY2203_Exp1_S1_...docx
|-- Semana 2/
|   |-- README.md
|   |-- src/                         # Actividad Formativa 2 de SpeedFast
|   `-- PRY2203_Exp1_S2_...docx
|-- Semana 3/
|   |-- README.md
|   |-- src/                         # Actividad Sumativa 1 de SpeedFast
|   `-- PRY2203_Exp1_S3_...docx
|-- Semana 4/
|   |-- README.md
|   |-- src/                         # Actividad Formativa 3 de SpeedFast
|   `-- S4_ Instrucciones y pauta de evaluación.docx
|-- Semana 5/
|   |-- README.md
|   |-- src/                         # Actividad Sumativa 2 de SpeedFast
|   `-- S5_ Instrucciones y pauta de evaluación.docx
|-- Semana 6/
|   |-- README.md
|   |-- src/                         # Actividad Formativa 4 SpeedFast UI
|   `-- S6_ Instrucciones y pauta de evaluación.docx
`-- Semana 7/
    |-- README.md
    |-- src/                         # Persistencia con JDBC y MySQL
    |-- sql/
    |-- lib/
    |-- compose.yaml
    |-- ejemplo/                     # Referencia del profesor
    `-- S7_ Instrucciones y pauta de evaluación.docx
```

## Ejecución

### Desde IntelliJ IDEA

1. Abrir la carpeta de la semana correspondiente.
2. Configurar el proyecto con JDK 17.
3. Abrir `src/main/java/speedfast/Main.java` para las semanas 1 a 5, o `src/main/Main.java` del módulo `SpeedFast` para las semanas 6 y 7.
4. Ejecutar el método `main`.

Para las semanas 6 y 7, habilitar el plugin **Swing UI Designer** y compilar los formularios con **Build > Rebuild Project**. Cada `README.md` detalla la configuración necesaria del diseñador. Para Semana 7, iniciar además Docker Desktop y ejecutar `docker compose up -d --wait` desde esa carpeta; el conector JDBC está vinculado en el módulo.

### Desde la terminal

Desde la carpeta de cualquiera de las semanas 1 a 5:

```bash
javac -encoding UTF-8 -d out src/main/java/speedfast/*.java
java -cp out speedfast.Main
```

Las semanas 6 y 7 utilizan archivos `.form` que `javac` por sí solo no procesa. Para esas semanas se debe compilar y ejecutar desde IntelliJ IDEA.

La documentación específica y las instrucciones de ejecución de cada proyecto se encuentran en el `README.md` de su respectiva carpeta semanal.

---

Proyecto académico semanal | Duoc UC

## Autor

- Javier A. Moraga Rojas
