# Biblioteca Escolar - Desarrollo Orientado a Objetos II

Evaluación Final Transversal de la Semana 9 para la asignatura Desarrollo Orientado a Objetos II.

## Objetivo

Gestionar una biblioteca escolar mediante autenticación por roles, libros, estudiantes, usuarios, categorías, préstamos, devoluciones y reportes, con persistencia en MySQL. El proyecto es independiente de SpeedFast y conserva los documentos oficiales de `EFT`.

## Diseño

- `Persona` es abstracta y reúne nombre, RUT y correo. `Estudiante` y la clase abstracta `Usuario` la especializan.
- `Bibliotecario` y `UsuarioEstudiante` sobrescriben presentación y permisos. `Usuario` implementa `Autenticable` y `Autorizable`; las sobrecargas de `puedeRealizar` verifican una acción general o la propiedad de un préstamo.
- Las vistas Swing (`JFrame` y ocho `.form`) llaman a controladores; los servicios aplican reglas y permisos; las interfaces DAO y sus implementaciones contienen las operaciones `create`, `readAll`, `update` y `delete`.
- `DatabaseConnection` aplica Singleton y administra una conexión por proceso. Los DAO usan `PreparedStatement`, `ResultSet` y cierre de recursos sin cerrar la conexión compartida.
- El bibliotecario administra todas las entidades. El estudiante consulta el catálogo y gestiona únicamente sus préstamos e historial. Los servicios vuelven a comprobar el rol y la identidad en MySQL.
- Registrar un estudiante crea su cuenta en la misma transacción. Editar datos compartidos sincroniza ambas tablas por RUT normalizado. En Usuarios también se puede vincular una cuenta con una ficha existente.
- Las claves nuevas usan PBKDF2 con sal; las claves del SQL oficial se convierten al iniciar sesión correctamente. No se muestran claves ni hashes en tablas o reportes.
- El stock representa ejemplares disponibles. El vencimiento predeterminado es de siete días; el bibliotecario puede editar fechas y relaciones de préstamos activos. Eliminar un préstamo activo cancela su reserva; eliminar uno devuelto conserva el stock.
- `fecha_devolucion` es el vencimiento. `fecha_devolucion_real` se registra al devolver. Los históricos oficiales ya devueltos conservan su fecha real desconocida; no se inventan datos.

## Sincronización

`SwingWorker` ejecuta JDBC en segundo plano y su método `done` actualiza Swing en el hilo de eventos. Las ventanas deshabilitan acciones durante su operación y refrescan listados y relaciones después de guardar. Refrescar y ordenar conservan los campos en edición; Limpiar permite volver a seleccionar datos actualizados.

`PrestamoServicio` utiliza métodos `synchronized`. `DatabaseConnection` sincroniza el acceso a su conexión durante toda la consulta o transacción. El préstamo, el descuento de stock y su registro histórico se confirman juntos con `commit`; un error provoca `rollback`. Las filas se bloquean con `FOR UPDATE`, protegiendo también solicitudes desde distintos procesos. La devolución incrementa el stock una sola vez. No se reintentan escrituras automáticamente.

Editar un libro compara el stock actual con el que tenía al seleccionarlo, evitando sobrescribir un préstamo concurrente. Si cambió, se informa al usuario y debe limpiar, refrescar y seleccionar nuevamente.

## Estructura

```text
Semana 9/
|-- README.md
|-- BibliotecaEscolar.iml
|-- BibliotecaEscolar.jar
|-- .idea/                            # Incluye el artefacto BibliotecaEscolar
|-- .run/
|-- config/db.properties.example
|-- lib/mysql-connector-j-26.7.0.jar
|-- sql/
|   |-- PRY2203_EFT_S9_Script_crea_tablas_biblioteca.sql
|   |-- PRY2203_EFT_S9_Script_poblado_tablas_biblioteca.sql
|   `-- 03_fecha_devolucion_real.sql
|-- src/
|   |-- modelo/
|   |-- dao/impl/
|   |-- controlador/
|   |-- servicio/
|   |-- vista/                       # Clases Java y ocho formularios .form
|   |-- util/
|   `-- main/Main.java
```

## Ejecución

### Preparar MySQL desde Workbench

1. Disponer de un servidor MySQL iniciado y abrir su conexión en MySQL Workbench. Workbench es el cliente; requiere un servidor accesible.
2. Sobre una instalación donde todavía no exista `biblioteca`, abrir y ejecutar, en este orden:
   - [1. Creación oficial de tablas](sql/PRY2203_EFT_S9_Script_crea_tablas_biblioteca.sql).
   - [2. Poblamiento oficial](sql/PRY2203_EFT_S9_Script_poblado_tablas_biblioteca.sql).
   - [3. Ampliación de fecha real de devolución](sql/03_fecha_devolucion_real.sql).
3. Refrescar Schemas. Comprobar las cinco tablas, sus claves y `prestamos.fecha_devolucion_real`. Los scripts de creación y poblamiento son idénticos a los recibidos; se ejecutan una vez, no al iniciar la aplicación. No volver a poblar una base en uso.
4. Copiar `config/db.properties.example` como `config/db.properties` y completar host, puerto, usuario y contraseña propios:

```properties
db.url=jdbc:mysql://localhost:3306/biblioteca
db.user=SU_USUARIO_MYSQL
db.password=SU_CONTRASENA_MYSQL
```

La cuenta necesita SELECT, INSERT, UPDATE y DELETE sobre `biblioteca`. Para ejecutar los scripts se requiere una cuenta con permisos de creación/alteración. La instalación del profesor no depende de Docker. En el equipo de desarrollo se verificó MySQL 8.4 con puerto 3307; cada instalación debe usar su puerto real.

El archivo de credenciales está excluido de Git y debe excluirse del comprimido. También se admiten `BIBLIOTECA_DB_URL`, `BIBLIOTECA_DB_USER` y `BIBLIOTECA_DB_PASSWORD`, con prioridad sobre el archivo, o `-Dbiblioteca.config=RUTA` para elegir otro archivo.

### Cuentas del poblamiento oficial

| Rol | Nombre | RUT | Contraseña inicial |
|---|---|---|---|
| Bibliotecario | Antonia Pérez | 12345678-9 | clave123 |
| Estudiante | Carlos Ruiz | 98765432-1 | clave123 |
| Estudiante | María Torres | 11222333-4 | clave123 |

Son datos ficticios proporcionados por la actividad. Los RUT se validan por formato y unicidad, sin exigir un dígito verificador que rechace esos ejemplos. Los estudiantes oficiales sin cuenta, como Luis Gómez, pueden vincularse desde Usuarios usando su RUT existente.

### Desde IntelliJ IDEA

1. Abrir la carpeta `Semana 9` como proyecto y seleccionar JDK 17.
2. Habilitar el plugin **Swing UI Designer**. Revisar que `src` esté marcado como Sources Root.
3. En **Project Structure > Modules > Dependencies**, comprobar el conector incluido en `lib`. Si no aparece, agregarlo con **JARs or directories**.
4. En **Settings > Editor > GUI Designer**, seleccionar la generación en archivos binarios e incluir el runtime de formularios en la salida. La configuración se incluye en `.idea/uiDesigner.xml`.
5. Ejecutar **Build > Rebuild Project**. Abrir cualquier `.form` para ver el diseñador.
6. Ejecutar la configuración **BibliotecaEscolar** o `main.Main`, con la carpeta Semana 9 como directorio de trabajo.

### Desde la terminal y el ejecutable

Con Java 17 y la configuración preparada, ejecutar desde Semana 9:

```powershell
java -jar BibliotecaEscolar.jar
```

El JAR contiene el conector y el runtime de formularios; no necesita IntelliJ para ejecutarse. Busca `config/db.properties` junto al propio JAR, incluso si se inicia desde otra carpeta. El servidor MySQL debe estar accesible.

---

Proyecto académico semanal | Duoc UC

## Autor

- Javier A. Moraga Rojas
