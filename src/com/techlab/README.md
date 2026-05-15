# TechLab

Sistema de gestión de productos y pedidos desarrollado en Java puro (sin frameworks ni dependencias externas).

## Estructura del proyecto

```
src/
└── com/techlab/
    ├── Main.java                          # Punto de entrada, menú interactivo
    ├── productos/
    │   └── Producto.java                  # Entidad producto con ID autoincremental
    ├── excepciones/
    │   └── StockInsuficienteException.java
    ├── pedidos/
    │   ├── LineaPedido.java               # Línea de un pedido (producto + cantidad)
    │   └── Pedido.java                    # Pedido con lista de líneas
    └── servicios/
        └── ProductoService.java           # CRUD de productos en memoria
```

## Requisitos

- Java 14 o superior (se usan *switch expressions*)

## Compilación y ejecución

Desde la raíz del proyecto:

```bash
# Compilar
javac -d out $(find src -name "*.java")

# Ejecutar
java -cp out com.techlab.Main
```

En Windows (CMD):

```cmd
for /r src %f in (*.java) do javac -d out "%f"
java -cp out com.techlab.Main
```

## Funcionalidades

| Opción | Descripción |
|--------|-------------|
| 1 | Agregar producto (nombre, precio, stock) |
| 2 | Listar todos los productos |
| 3 | Buscar producto por ID o nombre, con opción de actualizar precio y stock |
| 4 | Eliminar producto por ID |
| 5 | Crear pedido seleccionando productos y cantidades |
| 6 | Listar todos los pedidos registrados |
| 7 | Salir |

## Diseño del sistema

El proyecto aplica principios de POO con clases separadas por responsabilidad: `Producto`, `Pedido`, `LineaPedido`, `ProductoService` y `Main`. Los atributos son privados con acceso mediante getters/setters, y los IDs de producto se generan de forma autoincremental.

Los productos se almacenan en un `ArrayList<Producto>` en memoria. Cada producto tiene nombre (`String`), precio (`double`) y stock (`int`). La búsqueda admite tanto ID como nombre, y desde el mismo resultado se puede actualizar precio o stock.

Los pedidos se construyen como una lista de `LineaPedido` (cada una asocia un `Producto` con una cantidad). El total se calcula como precio × cantidad por línea. El stock **no se descuenta hasta confirmar el pedido**: si en cualquier línea la cantidad supera el stock disponible, se lanza `StockInsuficienteException` con un mensaje descriptivo, pero el pedido puede seguir armándose con otros productos.

## Manejo de errores

- Todas las entradas numéricas están protegidas con `try/catch NumberFormatException`: si el usuario ingresa texto donde se espera un número, se vuelve a solicitar el valor sin interrumpir el programa.
- Al crear un pedido, si la cantidad solicitada supera el stock disponible se lanza `StockInsuficienteException` con un mensaje descriptivo; el pedido puede seguir armándose con otros productos.
- El stock se descuenta únicamente al confirmar el pedido.
