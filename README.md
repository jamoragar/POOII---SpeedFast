# 🛵 SpeedFast - Desarrollo Orientado a Objetos II

Proyecto de trabajo semanal para la asignatura de **Desarrollo Orientado a Objetos II**. Esta segunda iteración modela tiempos estimados y asignación de repartidores mediante abstracción, herencia, sobrecarga, sobrescritura y polimorfismo. 📚

## 👥 Integrantes

- Javier A. Moraga Rojas

## 🎯 Objetivo

Implementar una clase abstracta y reutilizable para representar pedidos de comida, encomiendas y compras express. Cada subclase calcula su tiempo de entrega con una regla propia y conserva las validaciones de asignación desarrolladas previamente.

## 🧩 Diseño orientado a objetos

- **Abstracción**: `Pedido` concentra `idPedido`, `direccionEntrega`, `distanciaKm`, el resumen común y el contrato abstracto de cálculo.
- **Herencia**: `PedidoComida`, `PedidoEncomienda` y `PedidoExpress` extienden la clase abstracta `Pedido`.
- **Sobrescritura**: cada tipo redefine `calcularTiempoEntrega()` y personaliza la asignación según sus reglas.
- **Sobrecarga**: `Pedido` ofrece `asignarRepartidor()`, `asignarRepartidor(String)`, `asignarRepartidor(Repartidor)` y una variante para varios candidatos.
- **Polimorfismo**: `Main` almacena los pedidos derivados en un arreglo `Pedido[]` y ejecuta tanto el resumen como el cálculo y la asignación sin convertirlos a tipos concretos.

## ⏱️ Cálculo de tiempos

- **PedidoComida**: 15 minutos base + 2 minutos por kilómetro.
- **PedidoEncomienda**: 20 minutos base + 1,5 minutos por kilómetro, redondeado al entero más cercano.
- **PedidoExpress**: 10 minutos base; si la distancia supera 5 kilómetros, agrega 5 minutos.

Las decisiones de asignación validan datos reales:

- Comida exige disponibilidad inmediata y mochila térmica.
- Encomienda exige embalaje adecuado y capacidad suficiente de peso y dimensiones.
- Express compara las distancias y selecciona al repartidor disponible más cercano.

## 🖥️ Salida referencial

```text
PedidoComida #001
Dirección: Av. Italia 456
Distancia: 4 km
Factores: 15 min base + 2 min por kilómetro
Tiempo estimado de entrega: 23 minutos

PedidoEncomienda #002
Dirección: Av. Independencia 123
Distancia: 6 km
Factores: 20 min base + 1.5 min por kilómetro
Tiempo estimado de entrega: 29 minutos

PedidoExpress #003
Dirección: Av. Apoquindo 1500
Distancia: 7 km
Factores: 10 min base + 5 min si supera 5 km
Tiempo estimado de entrega: 15 minutos
```

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
                ├── PedidoExpress.java
                └── Repartidor.java
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
6. Revisar la consola para observar los tiempos estimados y la asignación de repartidores.

### Desde la terminal

1. Abrir una terminal en la carpeta raíz del proyecto.
2. Compilar las clases Java:

```bash
javac -encoding UTF-8 -d out src/main/java/speedfast/*.java
```

3. Ejecutar la clase principal:

```bash
java "-Dfile.encoding=UTF-8" -cp out speedfast.Main
```

La salida muestra los tiempos estimados 23, 29 y 15 minutos para los casos referenciales, además de sobrecarga, sustitución polimórfica, asignaciones aceptadas, rechazos justificados y selección efectiva por cercanía.

---

📌 Proyecto académico semanal | **Duoc UC**
