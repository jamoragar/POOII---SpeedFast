# SpeedFast - Desarrollo Orientado a Objetos II

Actividad sumativa de la Semana 8 para la asignatura Desarrollo Orientado a Objetos II.

## Objetivo

Gestionar repartidores, pedidos y entregas mediante operaciones CRUD con Java Swing, JDBC y MySQL. Los datos se guardan en `speedfast_db` y se consultan desde los formularios gráficos.

## Diseño

- `Repartidor`, `Pedido` y `Entrega` representan las tres entidades del modelo.
- `TipoPedido` y `EstadoPedido` definen los valores permitidos para los pedidos.
- `RepartidorDAO`, `PedidoDAO` y `EntregaDAO` son interfaces con los métodos `create`, `readAll`, `update` y `delete`. Sus implementaciones JDBC se encuentran en `dao.impl`.
- Los controladores conectan las ventanas con los DAO. El SQL se mantiene exclusivamente en las implementaciones DAO.
- `ConexionDB` obtiene las credenciales desde `config/db.properties`. También admite las variables `SPEEDFAST_DB_URL`, `SPEEDFAST_DB_USER` y `SPEEDFAST_DB_PASSWORD`, con prioridad sobre el archivo.
- `VentanaPrincipal` permite abrir las ventanas de repartidores, pedidos y entregas. Cada gestión tiene su formulario `.form`, tabla, validaciones y botones de creación, actualización, eliminación, limpieza y consulta.
- Los ID son automáticos. Seleccionar una fila permite editarla; la eliminación requiere confirmación. Las tablas no se editan directamente.
- Las entregas seleccionan pedidos y repartidores mediante listas cargadas desde MySQL. Se permiten varias entregas por pedido. El estado del pedido se modifica manualmente desde su formulario, sin simulación ni cambios automáticos al registrar entregas.
- MySQL impide eliminar pedidos o repartidores que tienen entregas asociadas. Primero deben eliminarse o reasignarse esas entregas.

El esquema usa las tablas **`repartidores`, `pedidos` y `entregas`**, en plural, según la pauta de Semana 8. Pueden coexistir con las tablas singulares de Semana 7, que no se modifican. La mención aislada a `ClienteDAO` en la pauta se interpreta como `RepartidorDAO`, conforme a las entidades y relaciones solicitadas.

## Sincronización

`SwingWorker` ejecuta JDBC en segundo plano. Los controles de la ventana ocupada se deshabilitan temporalmente para evitar operaciones repetidas, mientras las demás ventanas permanecen disponibles. La actualización visual y los mensajes se realizan en el hilo de eventos de Swing.

Después de cada modificación se refrescan las ventanas abiertas y sus listas relacionadas. Se conserva la selección por ID y el contenido que se esté editando en otras ventanas. El refresco de la tabla se realiza en una sola operación para mantener coherentes los índices de ordenación. Las modificaciones externas se consultan con **Refrescar** o al volver a abrir la ventana.

Las conexiones, sentencias y resultados se cierran mediante `try-with-resources`. Una consulta fallida muestra un error y conserva los datos visibles; esos datos pueden estar desactualizados. No se muestra un mensaje de éxito cuando falla una operación.

## Estructura

```text
Semana 8/
|-- README.md
|-- SpeedFast.iml
|-- .idea/                         # Módulo, Java 17 y Swing UI Designer
|-- config/
|   `-- db.properties.example     # Plantilla sin credenciales reales
|-- lib/
|   `-- mysql-connector-j-26.7.0.jar
|-- sql/
|   `-- speedfast_db.sql           # DDL obligatorio para reproducir el modelo
`-- src/
    |-- modelo/
    |-- dao/
    |   `-- impl/
    |-- controlador/
    |-- vista/                    # Cuatro ventanas .java y .form; base compartida
    |-- util/
    `-- main/Main.java
```

## Ejecución

### Desde IntelliJ IDEA

1. Tener un servidor **MySQL** iniciado y accesible. MySQL Workbench permite administrar la conexión; requiere conectarse a un servidor MySQL instalado localmente o disponible en otro equipo.
2. En Workbench, abrir una conexión con un usuario autorizado para crear la base y sus tablas. Abrir [sql/speedfast_db.sql](sql/speedfast_db.sql) mediante **File > Open SQL Script** y ejecutar el script completo. Actualizar **Schemas** y comprobar las tres tablas en plural dentro de `speedfast_db`.
3. Abrir la carpeta `Semana 8` como proyecto en IntelliJ y seleccionar **JDK 17** en **Project Structure > Project** y **Modules**. `src` debe estar marcado como **Sources**; `Ejemplo profesor` debe permanecer excluido.
4. Copiar `config/db.properties.example` como `config/db.properties` y completar los datos propios de la conexión:

   ```properties
   db.url=jdbc:mysql://localhost:3306/speedfast_db
   db.user=SU_USUARIO
   db.password=SU_CLAVE
   ```

   Cambiar host y puerto cuando corresponda. El usuario debe poder consultar, insertar, actualizar y eliminar en las tres tablas. El directorio de trabajo de la ejecución debe ser `Semana 8`; la configuración incluida usa `$PROJECT_DIR$`. Para otra ubicación del archivo se puede usar la opción de VM `-Dspeedfast.config=ruta/al/archivo.properties`.

5. Revisar **Project Structure > Modules > Dependencies**. El módulo incluye `lib/mysql-connector-j-26.7.0.jar`. Si no aparece, agregarlo mediante **+ > JARs or directories**. No se necesita descargar otro conector.
6. Habilitar el plugin **Swing UI Designer**. Los archivos `.form` están vinculados a sus clases en `vista`. En la configuración del diseñador, generar la GUI como **Binary class files** y copiar las clases del runtime del diseñador a la salida de compilación. Ejecutar **Build > Rebuild Project**.
7. Ejecutar la configuración **SpeedFast** o el método `main` de `src/main/Main.java`.
8. Crear un repartidor y un pedido. Abrir **Gestionar entregas**, seleccionar ambas relaciones y guardar con fecha `yyyy-MM-dd` y hora `HH:mm:ss`. Cambiar el estado del pedido desde **Gestionar pedidos**. Cerrar y abrir de nuevo la aplicación para verificar la persistencia.

La aplicación no crea ni migra tablas automáticamente. El [DDL del esquema](sql/speedfast_db.sql) es parte indispensable del proyecto. El archivo local con contraseñas está excluido de Git; entregar únicamente la plantilla de configuración.

### Desde la terminal

Los archivos `.form` requieren el compilador de formularios de IntelliJ; `javac` por sí solo no produce una interfaz ejecutable. Después de compilar con IntelliJ, desde la carpeta `Semana 8` en Windows:

```powershell
java -cp "out/production/SpeedFast;lib/mysql-connector-j-26.7.0.jar" main.Main
```

La ruta de salida puede variar según la configuración local de IntelliJ. El runtime del diseñador debe estar copiado a esa salida, como se indica en el paso 6.

La guía de [evidencias y comprobaciones](evidencias/README.md) contiene 64 comprobaciones de integración aprobadas, 4 verificaciones de reinicio, consultas SQL y capturas de las ventanas. La publicación y revisión final de la entrega siguen siendo manuales. Antes de entregar, revisar que el comprimido y el repositorio incluyan fuentes, los cuatro `.form`, `SpeedFast.iml`, `.idea`, `.run`, el conector, la plantilla de conexión, **`sql/speedfast_db.sql`** y las evidencias. Excluir `out`, archivos temporales y contraseñas. La publicación en GitHub, el historial de commits y el comprimido se gestionan manualmente.

---

Proyecto académico semanal | Duoc UC

## Autor

- Javier A. Moraga Rojas
