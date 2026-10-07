# TechLab – Sistema de gestión de productos y pedidos (Java)

Aplicación de consola en Java desarrollada para el curso *Iniciación a la Programación Back-End con Java* (Talento Tech).
Permite gestionar un catálogo de productos y crear pedidos con control de stock.

El catálogo inicial son los 10 cursos del e-commerce de los módulos anteriores (Front-End y React).

## Cómo ejecutarlo

**IntelliJ IDEA:** abrir la carpeta del proyecto, abrir `src/com/techlab/Main.java` y ejecutar `Main.main()`.

**Terminal** (requiere JDK 21 o superior):

```bash
mkdir -p out
javac -encoding UTF-8 -d out $(find src -name "*.java")
java -cp out com.techlab.Main
```

## Menú

```
1) Agregar producto
2) Listar productos
3) Buscar/Actualizar producto
4) Eliminar producto
5) Crear un pedido
6) Listar pedidos
7) Salir
```

- **Buscar** por ID o por nombre (coincidencia parcial). Se puede actualizar **precio** o **stock** con validación (no se admiten valores negativos).
- **Eliminar** pide confirmación.
- **Crear pedido**: se elige cuántos productos distintos, y para cada uno ID y cantidad. Se valida el stock, se calcula el total y, **solo si se confirma**, se descuenta el stock. Si un producto se repite en el mismo pedido, las cantidades se suman y se valida el total.
- **Listar pedidos** muestra cada pedido con sus líneas y total.

## Estructura

```
src/com/techlab/
├── Main.java                 Menú interactivo (orquesta, no tiene lógica de negocio)
├── productos/                Producto (id autogenerado, encapsulado) y Curso (herencia + polimorfismo)
├── pedidos/                  Pedido y LineaPedido (producto + cantidad + precio al momento del pedido)
├── excepciones/              ProductoNoEncontradoException, StockInsuficienteException
├── servicios/                ProductoService, PedidoService (lógica de agregar/buscar/eliminar/registrar)
└── util/                     Consola (lectura validada) y Formato (montos en pesos)
```

## Conceptos aplicados

| Consigna | Dónde |
|---|---|
| Variables, operadores, validaciones | `Producto`, `Pedido`, `Consola` |
| Colecciones | `ArrayList<Producto>`, `ArrayList<Pedido>`, `ArrayList<LineaPedido>` |
| Encapsulamiento (atributos privados, getters/setters validados) | `Producto`, `LineaPedido`, `Pedido` |
| Herencia / polimorfismo | `Curso extends Producto` (`getCategoria()` sobrescrito) |
| Sobrecarga | `ProductoService.agregar(...)` |
| Excepciones propias | `StockInsuficienteException`, `ProductoNoEncontradoException` |
| `try/catch` por datos mal ingresados | `Consola` (`NumberFormatException`) y manejo en `Main` |
| Paquetes | `productos`, `pedidos`, `excepciones`, `servicios`, `util` |
