# SpeedFast - Desarrollo Orientado a Objetos II

Actividad formativa de la Semana 7 para la asignatura Desarrollo Orientado a Objetos II.

## Objetivo

Guardar y consultar pedidos, repartidores y entregas en MySQL mediante JDBC. La aplicación continúa la interfaz gráfica de Semana 6 e incorpora persistencia para conservar los datos entre ejecuciones.

## Diseño

- `Pedido`, `Repartidor` y `Entrega` representan las tablas del modelo. MySQL genera sus identificadores con `AUTO_INCREMENT`.
- `ConexionBD` abre conexiones mediante `DriverManager` y lee los parámetros de `config/db.properties`.
- `PedidoDAO`, `RepartidorDAO` y `EntregaDAO` guardan y consultan información con `PreparedStatement` y `ResultSet`.
- `ControladorPedidos` valida los modelos y comunica los formularios con los DAO, siguiendo la organización del ejemplo del profesor.
- `VentanaRegistroPedido` guarda pedidos; `VentanaRepartidores` registra y lista repartidores; `VentanaListaPedidos` consulta los pedidos y permite iniciar o finalizar entregas.
- `Main` inicia la ventana principal en el hilo de eventos de Swing. Las cuatro ventanas conservan sus archivos `.form` para editarlas con Swing UI Designer.

## Sincronización

Las operaciones JDBC se ejecutan con `SwingWorker` fuera del hilo de eventos. Los listados consultan MySQL al abrirse, después de registrar o modificar datos y al pulsar **Refrescar**. Los cambios externos se muestran al refrescar; no se utiliza una consulta periódica automática.

`EntregaDAO` guarda la asignación y cambia el pedido a `EN_REPARTO` en una misma transacción. Bloquea al repartidor durante la asignación y revierte los cambios con `rollback` ante errores. La simulación dura tres segundos y luego persiste `ENTREGADO`. Si se interrumpe el proceso o falla la conexión, el pedido en reparto puede finalizarse desde el listado al recuperar la conexión.

Los recursos JDBC se cierran con `try-with-resources`, como en el ejemplo docente. Las excepciones llegan a la interfaz para informar el error sin confundir una conexión fallida con una lista vacía. La aplicación espera que terminen las operaciones antes de permitir el cierre normal.

## Estructura

```text
Semana 7/
|-- README.md
|-- S7_ Instrucciones y pauta de evaluación.docx
|-- SpeedFast.iml
|-- config/
|   |-- db.properties             # Configuración local de la conexión
|   `-- db.properties.example     # Plantilla para configurar otro equipo
|-- lib/
|   `-- mysql-connector-j-26.7.0.jar
|-- sql/
|   `-- speedfast_db.sql
`-- src/
    |-- controlador/
    |   `-- ControladorPedidos.java
    |-- dao/
    |   |-- ConexionBD.java
    |   |-- EntregaDAO.java
    |   |-- PedidoDAO.java
    |   `-- RepartidorDAO.java
    |-- main/
    |   `-- Main.java
    |-- modelo/
    |   |-- Entrega.java
    |   |-- EstadoPedido.java
    |   |-- Pedido.java
    |   |-- Repartidor.java
    |   `-- TipoPedido.java
    `-- vista/                     # Cuatro ventanas .java y sus formularios .form
```

## Ejecución

### Desde IntelliJ IDEA

1. Disponer de un servidor MySQL en ejecución y de una cuenta con permisos para crear la base de datos y sus tablas. MySQL Workbench permite conectarse a ese servidor y ejecutar el script; el servidor MySQL debe estar instalado o disponible por separado.
2. En MySQL Workbench, conectarse al servidor con las credenciales propias, abrir `sql/speedfast_db.sql` y ejecutar el script completo. Se creará la base `speedfast_db` con las tablas `repartidor`, `pedido` y `entrega`, incluyendo sus claves foráneas.
3. En el proyecto, copiar `config/db.properties.example` como `config/db.properties`. Si `db.properties` ya existe, editarlo. Completar host, puerto, usuario y contraseña con los datos del servidor al que se conectó en Workbench, siguiendo el ejemplo indicado más abajo.
4. Abrir la carpeta `Semana 7` en IntelliJ y configurar el módulo `SpeedFast` con JDK 17 o superior. `src` debe estar marcado como **Sources Root**.
5. En **File > Project Structure > Modules > Dependencies**, verificar la biblioteca `lib/mysql-connector-j-26.7.0.jar`, incluida en el proyecto y vinculada en `SpeedFast.iml`. Si falta, agregarla con **+ > JARs or Directories**.
6. Habilitar **Swing UI Designer**. En **Settings > Editor > GUI Designer**, seleccionar **Generate GUI into: Binary class files** y activar **Automatically copy form runtime classes to the output directory**.
7. En **Run > Edit Configurations**, seleccionar **SpeedFast** y verificar que use `main.Main`, el módulo `SpeedFast` y la carpeta `Semana 7` como **Working directory**. La ruta `config/db.properties` se resuelve desde esa carpeta.
8. Ejecutar **Build > Rebuild Project** y luego el método `main` de `src/main/Main.java`.

Ejemplo de `config/db.properties` para un servidor MySQL en el mismo equipo y con el puerto predeterminado `3306`:

```properties
db.url=jdbc:mysql://localhost:3306/speedfast_db
db.user=TU_USUARIO_MYSQL
db.password=TU_CONTRASENA_MYSQL
```

Reemplazar `TU_USUARIO_MYSQL` y `TU_CONTRASENA_MYSQL` por las credenciales propias de MySQL. Si el servidor utiliza otro host o puerto, reemplazar también `localhost` y `3306` por los valores de la conexión utilizada en Workbench. Mantener `speedfast_db` como nombre de la base. La cuenta configurada en Java debe tener permisos de lectura, inserción y actualización sobre esa base.

La aplicación Java se conecta directamente al servidor mediante JDBC; no necesita que Workbench permanezca abierto. El archivo `db.properties` no crea usuarios ni cambia sus contraseñas: sus valores deben corresponder a una cuenta existente en MySQL.

`ConexionBD` lee el archivo al abrir cada conexión. Si el equipo tiene definidas las variables `SPEEDFAST_DB_URL`, `SPEEDFAST_DB_USER` o `SPEEDFAST_DB_PASSWORD`, estas prevalecen sobre el archivo; quitarlas de la configuración de ejecución o ajustarlas al mismo servidor para evitar usar otros datos de conexión.

`config/db.properties` contiene las credenciales de quien ejecuta el proyecto y está excluido de Git. La plantilla `config/db.properties.example` sirve como base, pero sus valores deben adaptarse al servidor del profesor.

### Desde la terminal

Como alternativa a Workbench, si el cliente MySQL está disponible, abrir una terminal en `Semana 7` y conectarse con las credenciales propias. Por ejemplo, para un servidor local en el puerto `3306`:

```bash
mysql -h localhost -P 3306 -u TU_USUARIO_MYSQL -p
```

Reemplazar `TU_USUARIO_MYSQL` por el usuario de MySQL. El cliente solicitará la contraseña. Dentro de la consola, ejecutar:

```sql
SOURCE sql/speedfast_db.sql;
USE speedfast_db;
SHOW TABLES;
```

Deben aparecer las tablas `repartidor`, `pedido` y `entrega`. Este paso es equivalente a ejecutar el script en Workbench y no es necesario repetirlo si ya se creó la base desde allí.

La compilación con `javac` por sí sola no procesa los archivos `.form`. Para ejecutar la interfaz, compilar desde IntelliJ siguiendo los pasos anteriores.

Registrar primero un repartidor y un pedido. Al iniciar la entrega se guarda la relación con fecha y hora; después de tres segundos el pedido queda entregado. Al cerrar y volver a abrir la aplicación, los registros permanecen en MySQL. No se cargan datos ficticios ni se guarda información únicamente en memoria.

---

Proyecto académico semanal | Duoc UC

## Autor

- Javier A. Moraga Rojas
