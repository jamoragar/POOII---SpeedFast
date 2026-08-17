# 🛵 SpeedFast - Desarrollo Orientado a Objetos II

Proyecto de trabajo semanal para la asignatura de **Desarrollo Orientado a Objetos II**. Esta actividad modela la asignación de repartidores de SpeedFast mediante herencia, sobrecarga, sobrescritura y polimorfismo. 📚

## 👥 Integrantes

- Javier A. Moraga Rojas

## 🎯 Objetivo

Implementar una jerarquía de clases para representar pedidos de comida, encomiendas y compras express, aplicando polimorfismo para asignar repartidores según las necesidades de cada servicio.

## 📁 Estructura

```text
.
├── .idea/
├── README.md
├── SpeedFast.iml
└── src/
    └── main/
        └── java/
            └── speedfast/
                ├── Main.java
                ├── Pedido.java
                ├── PedidoComida.java
                ├── PedidoEncomienda.java
                └── PedidoExpress.java
```

## 🛠️ Software necesario

- **Java Development Kit (JDK) 17 LTS**: requerido para compilar y ejecutar el proyecto.
- **IntelliJ IDEA Community o Ultimate**: entorno de desarrollo utilizado para abrir y ejecutar el proyecto.

## 🚀 Pasos para ejecutar el proyecto

### Desde IntelliJ IDEA

1. Abrir IntelliJ IDEA.
2. Seleccionar **Open** y elegir la carpeta `SpeedFast`.
3. Confirmar que el SDK del proyecto sea **JDK 17** en **File > Project Structure > Project**.
4. Abrir `src/main/java/speedfast/Main.java`.
5. Ejecutar el método `main` con el botón de ejecución o `Shift + F10`.
6. Revisar la consola para observar la asignación de repartidores.

### Desde la terminal

1. Abrir una terminal en la carpeta raíz del proyecto.
2. Compilar las clases Java:

```bash
javac -encoding UTF-8 -d out src/main/java/speedfast/*.java
```

3. Ejecutar la clase principal:

```bash
java -cp out speedfast.Main
```

La salida mostrará la asignación de pedidos de comida, encomienda y compra express, junto con las demostraciones de sobrescritura, sobrecarga y polimorfismo.

---

📌 Proyecto académico semanal | **Duoc UC**
