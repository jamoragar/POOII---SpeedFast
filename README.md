# 🛵 SpeedFast - Desarrollo Orientado a Objetos II

Proyecto de trabajo semanal para la asignatura de **Desarrollo Orientado a Objetos II**. Esta actividad modela la asignación de repartidores de SpeedFast mediante herencia, sobrecarga, sobrescritura y polimorfismo. 📚

## 👥 Integrantes

- Javier A. Moraga Rojas

## 🎯 Objetivo

Implementar una jerarquía de clases para representar pedidos de comida, encomiendas y compras express, aplicando polimorfismo para asignar repartidores según las necesidades de cada servicio.

## 📁 Estructura

```text
.
├── build.xml
├── manifest.mf
├── nbproject/
├── README.md
└── src/
    └── speedfast/
        ├── Main.java
        ├── Pedido.java
        ├── PedidoComida.java
        ├── PedidoEncomienda.java
        └── PedidoExpress.java
```

## 🛠️ Software necesario

- **Java Development Kit (JDK) 25**: configurado actualmente en el proyecto NetBeans.
- **Apache NetBeans**: para abrir, compilar y ejecutar el proyecto.
- **Apache Ant**: incluido o configurado mediante NetBeans para construir el proyecto.

## 🚀 Pasos para ejecutar el proyecto

### Desde Apache NetBeans

1. Abrir Apache NetBeans.
2. Seleccionar **File > Open Project** y elegir la carpeta `SpeedFast`.
3. Verificar que el proyecto use el JDK configurado.
4. Ejecutar el proyecto con **Run Project** o la tecla `F6`.
5. Revisar la consola para observar la asignación de repartidores.

### Desde la terminal

1. Abrir una terminal en la carpeta raíz del proyecto.
2. Compilar las clases Java:

```bash
javac -encoding UTF-8 -d build/classes src/speedfast/*.java
```

3. Ejecutar la clase principal:

```bash
java -cp build/classes speedfast.Main
```

La salida mostrará la asignación de pedidos de comida, encomienda y compra express, junto con las demostraciones de sobrescritura, sobrecarga y polimorfismo.

---

📌 Proyecto académico semanal | **Duoc UC**
