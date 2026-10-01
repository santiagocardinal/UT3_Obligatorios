# UT3_Obligatorios
# Ejercicio 11

## Letra

Se desea implementar un programa que, en forma eficiente, permita obtener las frecuencias de ocurrencias de las palabras de un libro. Para ello se debe entonces:

1. Seleccionar la o las clases de la API de colecciones de JAVA más apropiada para esta aplicación. Justificar la elección.
2. Dado un archivo de entrada «libro.txt», desarrollar un programa JAVA utilizando la API de colecciones que permita saber las frecuencias de ocurrencias de las palabras del libro.
3. Graficar los resultados de las 10 palabras que más ocurren.

---

## Resolución

### 1. Elección de las colecciones

El problema tiene dos partes con necesidades distintas: **contar** cada palabra y luego **quedarse con las 10 más frecuentes**.

#### Para contar: `HashMap<String, Integer>`

Cada palabra leída hay que buscarla y, si existe, incrementar su contador; si no, agregarla con valor 1. Es una asociación clave → valor (palabra, frecuencia), por lo que la estructura adecuada es `Map`.

| Implementación | Buscar / insertar | Orden | ¿Conviene? |
|---|---|---|---|
| `HashMap` | **O(1)** promedio | Ninguno | **Sí** |
| `TreeMap` | O(log n) | Alfabético por clave | No, el orden alfabético no se necesita |
| `LinkedHashMap` | O(1) promedio | Orden de inserción | No, agrega costo de memoria sin beneficio |


> Se descartan `List` (buscar una palabra sería O(m), total O(n·m)) y `Set` (no guarda la frecuencia).

#### Para las 10 más frecuentes: `PriorityQueue` (heap) de tamaño 10

El `HashMap` no está ordenado por frecuencia. Por lo que:

- Recorremos las entradas manteniendo un **min-heap de tamaño k = 10** (`PriorityQueue`): cada entrada entra al heap y, si el heap supera 10 elementos, se saca el menor. Costo **O(m log k)**, prácticamente O(m), y memoria O(k).

Se elige la `PriorityQueue`. Al final sólo quedan 10 elementos, que se ordenan de mayor a menor para mostrarlos (costo despreciable). Ante empate de frecuencias se ordena alfabéticamente para que el resultado sea determinista.

#### Complejidad total

- Tiempo: **O(n + m log 10) = O(n)**
- Memoria: **O(m)** para el mapa


**Normalización de palabras:**
- Todo se pasa a minúsculas (`"Casa"` y `"casa"` son la misma palabra).
- Se separa por cualquier carácter que no sea letra (regex `[^\p{L}]+`), así se eliminan signos de puntuación, números y espacios, pero se conservan tildes y la ñ.
- El archivo se lee en UTF-8 línea por línea con `Files.lines`, sin cargar todo el libro en memoria.

### Gráfico

El programa muestra las 10 palabras más frecuentes de dos formas:

- **En consola**, con barras de texto:
  ```
   1. de               5321 ##################################################
   2. la               3870 ####################################
   ...
  ```


