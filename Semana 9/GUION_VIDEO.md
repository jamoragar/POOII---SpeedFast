# Guion de presentación — Biblioteca Escolar

Duración objetivo: **3 minutos 45 segundos**. La pauta admite 3 a 5 minutos; este guion sigue el objetivo de 3 a 4 solicitado. Leer solo los párrafos de narración, con pausas breves. Ensayar para ajustar el ritmo; los tiempos son orientativos.

## Preparación antes de grabar

1. Iniciar MySQL y comprobar el ingreso. No mostrar `config/db.properties`, contraseñas personales ni hashes en pantalla.
2. Abrir IntelliJ en Semana 9 y preparar las pestañas indicadas. Tener el JAR y los accesos a Libros, Estudiantes, Préstamos y Reportes listos. Grabar con Kaltura según la pauta.
3. Crear desde la GUI una categoría y un libro ficticio para la demostración con ISBN único y **stock 1**. Registrar un estudiante ficticio y su clave. No usar préstamos oficiales para la devolución de demostración.
4. Preparar otra instancia como bibliotecario para la segunda solicitud del mismo libro, seleccionando otro estudiante. Tener además una sesión estudiante preparada para mostrar su menú. Ocultar las contraseñas al grabar.
5. En Estudiantes preparar los datos para explicar la creación conjunta. En Libros seleccionar el registro y modificar un campo durante la toma. Eliminar solo los datos propios de demostración si se desea mostrar esa acción.
6. En Reportes dejar visible un préstamo oficial activo: las fechas históricas permiten mostrar atraso. Las fechas reales desconocidas se explican como tales.
7. Dejar el rostro visible al inicio y al cierre, o en una esquina durante toda la grabación. Cerrar notificaciones y ventanas ajenas al proyecto.
8. Ensayar el cierre y la reapertura del JAR. Si el ingreso demora, mantener la narración mientras se realiza; no afirmar un resultado que no se muestre.

## Narración y acciones

### 0:00–0:20

**En pantalla:** Rostro visible y menú principal en pantalla.

Hola, soy Javier Moraga y presento Biblioteca Escolar, mi Evaluación Final Transversal de Desarrollo Orientado a Objetos II. Esta aplicación de escritorio está desarrollada en Java y permite administrar libros, estudiantes y préstamos, conservando la información en MySQL.

### 0:20–0:45

**En pantalla:** Ingresar con Antonia. Mostrar el menú del estudiante en una segunda instancia preparada.

El acceso utiliza RUT y contraseña. El bibliotecario tiene acceso a la administración completa. El estudiante puede consultar el catálogo y trabajar solamente con sus propios préstamos, devoluciones e historial. Esta separación se aplica en las ventanas y también en los servicios, para evitar operaciones sobre información de otro estudiante.

### 0:45–1:20

**En pantalla:** Mostrar un libro creado previamente. Cambiar su editorial y guardar. Mostrar Estudiantes y Categorías. Intentar guardar un campo obligatorio vacío.

En libros puedo registrar, consultar, modificar y eliminar registros. Cada libro tiene categoría, ISBN y ejemplares disponibles. Al seleccionar una fila se cargan sus datos para editar. Aquí modifico la editorial y la tabla se actualiza. Registrar un estudiante también crea su cuenta de acceso. Las validaciones controlan campos obligatorios, formatos y duplicados. Si falta un dato, aparece un mensaje y la operación no se guarda.

### 1:20–2:05

**En pantalla:** Preseleccionar libro de demostración con stock uno. Prestar a un estudiante. Intentar otro préstamo desde la segunda instancia; volver a la primera y devolver.

Ahora registro un préstamo. El sistema propone siete días de plazo, verifica disponibilidad y descuenta un ejemplar. El préstamo aparece inmediatamente en el historial. Este libro tenía un solo ejemplar: una segunda solicitud es rechazada porque ya no queda stock. Al devolverlo, se registra la fecha real y el ejemplar vuelve a estar disponible. Una devolución repetida se rechaza, evitando aumentar el inventario dos veces. También puedo cancelar un préstamo activo o corregir sus relaciones manteniendo el stock consistente.

### 2:05–2:25

**En pantalla:** Mostrar reportes de libros más prestados, activos e historial oficial con atraso.

Los reportes muestran los libros más prestados, los préstamos activos y el historial por estudiante. Aquí aparece un préstamo atrasado y sus días de demora. Para las devoluciones históricas sin fecha real, el sistema indica que no existe una fecha registrada, sin inventar ese dato.

### 2:25–3:25

**En pantalla:** IntelliJ: preparar pestañas Persona, Usuario, Bibliotecario, DAO, DatabaseConnection, VentanaGestion y PrestamoServicio.

En el código, Persona y Usuario son clases abstractas. Bibliotecario y UsuarioEstudiante sobrescriben comportamientos y permisos. Usuario implementa las interfaces Autenticable y Autorizable, con métodos sobrecargados para comprobar acciones generales o préstamos propios. La organización sigue MVC: las vistas capturan la interacción, los controladores coordinan y los servicios aplican reglas de negocio. Los DAO concentran el SQL y utilizan sentencias preparadas. Las sentencias y los resultados se cierran después de cada consulta, mientras la conexión permanece administrada por un único componente del sistema. DatabaseConnection implementa Singleton y administra una conexión compartida por proceso. SwingWorker ejecuta JDBC fuera del hilo visual. Los métodos sincronizados y los bloqueos de filas protegen el stock. La transacción confirma préstamo e inventario juntos; si algo falla, rollback revierte los cambios. Esto mantiene la interfaz disponible y evita préstamos inconsistentes.

### 3:25–3:45

**En pantalla:** Cerrar completamente y abrir el JAR. Consultar el préstamo realizado. Terminar con rostro visible.

Finalmente, cierro y vuelvo a abrir la aplicación. El préstamo y el stock permanecen guardados en MySQL. El proyecto incluye fuentes, formularios, scripts de base de datos, ejecutable y evidencias de las comprobaciones. Con esto integro orientación a objetos, concurrencia, interfaz gráfica y persistencia. Muchas gracias.

## Revisión antes de subir

- Narración: 458 palabras, sin contar acciones ni preparación. Ensayar a un ritmo cercano a 130 palabras por minuto.
- Mostrar rostro, funcionamiento real, IDE y justificación de MVC, DAO, Singleton, hilos y sincronización.
- El rechazo del segundo préstamo mostrado en el video es una demostración funcional. La prueba verdaderamente simultánea de dos procesos está documentada en `evidencias`; no confundir ambas.
- Revisar audio, textos legibles y duración final. La grabación, publicación del repositorio, compresión y subida al AVA son manuales.
