# Ejercicio 5 

>Describir en lenguaje natural, pre y postcondiciones, seudocódigo de las operaciones y análisis de ordenes de las siguientes operaciones del Trie y NodoTrie

1. Operación buscar palabra completa.
2. Obtener la lista de palabras por un prefijo dado.
3. Insertar una palabra con un dato asociado.
4. Eliminar una palabra del Trie.


```
NodoTrie
    hijos[26]  : NodoTrie
    esPalabra  : boolean
    dato       : Object

Trie
    raiz : NodoTrie
```

 **El alfabeto es de 26 letras, así que lo tomamos como **constante**

---

## 1. Buscar palabra completa

### Lenguaje natural

Partiendo desde la raíz y vamos letra por letra de la palabra. Por cada letra bajamos al hijo que le corresponde. Si en algún momento ese hijo es `null`, la palabra no está y devolvemos `null`.

Si llegamos al final de la palabra, todavía falta fijarse si el nodo tiene `esPalabra = true`. Si no lo tiene, quiere decir que encontramos un prefijo de otra palabra pero no la palabra en sí (por ejemplo, buscar `"cas"` cuando solo está `"casa"`).

### Precondiciones
- El Trie está creado
- La palabra no es `null` y tiene solo letras de la `a` a la `z`

### Postcondiciones
- Si la palabra está, devuelve el dato asociado
- Si no está, devuelve `null`
- El Trie no se modifica

### Seudocódigo

```
// En Trie
buscar(palabra) : Object
COM
    devolver raiz.buscar(palabra, 0)
FIN

// En NodoTrie
buscar(palabra, pos) : Object
COM
    SI pos == palabra.largo ENTONCES
        SI esPalabra ENTONCES
            devolver dato
        SINO
            devolver null
        FIN SI
    FIN SI

    indice <- palabra[pos] - 'a'
    SI hijos[indice] == null ENTONCES
        devolver null
    FIN SI

    devolver hijos[indice].buscar(palabra, pos + 1)
FIN
```

### Orden

Por cada letra hacemos operaciones constantes (calcular el índice y acceder al arreglo), y como mucho hacemos una llamada por letra.

> **O(m)**, siendo "m" el largo de la palabra. No depende de cuántas palabras haya en el Trie, solo del largo de la que buscamos.

---

## 2. Obtener la lista de palabras con un prefijo

### Lenguaje natural

Primero se recorre el Trie siguiendo los caracteres del prefijo, igual que en la búsqueda. Si el camino se corta, no hay palabras con ese prefijo y se devuelve una lista vacía. Si se llega al nodo final del prefijo, se recorre en profundidad (preorden) todo el subárbol que cuelga de él, armando la palabra a medida que se baja: cada vez que se visita un nodo con esPalabra = true, se agrega la palabra formada hasta ese punto a la lista de resultados. Si el propio prefijo es una palabra, también se incluye.

### Precondiciones
- El Trie está creado
- El prefijo no es `null` y tiene solo letras válidas

### Postcondiciones
- Devuelve una lista con todas las palabras que empiezan con el prefijo
- Si no hay ninguna, la lista está vacía
- El Trie no se modifica

### Seudocódigo

```
// En Trie
predecir(prefijo) : Lista
COM
    lista <- nueva Lista()
    nodo <- raiz.buscarNodo(prefijo, 0)
    SI nodo != null ENTONCES
        nodo.recolectar(prefijo, lista)
    FIN SI
    devolver lista
FIN

// En NodoTrie
buscarNodo(prefijo, pos) : NodoTrie
COM
    SI pos == prefijo.largo ENTONCES
        devolver this
    FIN SI

    indice <- prefijo[pos] - 'a'
    SI hijos[indice] == null ENTONCES
        devolver null
    FIN SI

    devolver hijos[indice].buscarNodo(prefijo, pos + 1)
FIN

// En NodoTrie
recolectar(palabraActual, lista)
COM
    SI esPalabra ENTONCES
        lista.agregar(palabraActual)
    FIN SI

    PARA i DESDE 0 HASTA 25 HACER
        SI hijos[i] != null ENTONCES
            letra <- 'a' + i
            hijos[i].recolectar(palabraActual + letra, lista)
        FIN SI
    FIN PARA
FIN
```

### Orden

Tiene dos partes:
- `buscarNodo` es igual a buscar, entonces es **O(m)**
- `recolectar` pasa una vez por cada nodo que hay debajo del prefijo, y en cada uno revisa los 26 hijos. Como 26 es constante, queda **O(n)**

> **O(m + n)**. Si el prefijo es muy corto (por ejemplo una sola letra), `n` puede ser casi todo el Trie.

---

## 3. Insertar una palabra con un dato asociado

### Lenguaje natural

Se recorre el Trie desde la raíz siguiendo los caracteres de la palabra. Para cada carácter, si el hijo correspondiente no existe se crea un nuevo NodoTrie vacío; luego se baja a ese hijo. Al terminar de procesar la palabra, el nodo alcanzado se marca como fin de palabra y se le guarda el dato. Si la palabra ya existía, se actualiza (sobrescribe) su dato. Las palabras que comparten prefijo reutilizan los nodos ya existentes.

### Precondiciones
- El Trie está creado
- La palabra no es `null`, no está vacía y tiene solo letras válidas

### Postcondiciones
- La palabra queda guardada en el Trie con su dato
- Si ya existía, se actualiza el dato y no se crean nodos nuevos
- Las otras palabras no cambian

### Seudocódigo

```
// En Trie
insertar(palabra, dato)
COM
    raiz.insertar(palabra, 0, dato)
FIN

// En NodoTrie
insertar(palabra, pos, dato)
COM
    SI pos == palabra.largo ENTONCES
        esPalabra <- true
        this.dato <- dato
        devolver
    FIN SI

    indice <- palabra[pos] - 'a'
    SI hijos[indice] == null ENTONCES
        hijos[indice] <- nuevo NodoTrie()
    FIN SI

    hijos[indice].insertar(palabra, pos + 1, dato)
FIN
```

### Orden

Por cada letra hacemos operaciones constantes. Cuando hay que crear un nodo nuevo, se inicializa un arreglo de 26, pero eso también es constante.

> **O(m)**

---

## 4. Eliminar una palabra del Trie

### Lenguaje natural

Se busca la palabra recorriendo el Trie como en la búsqueda. Si no existe (el camino se corta o el último nodo no tiene esPalabra = true), no se hace nada. Si existe, se desmarca el nodo final (esPalabra = false, dato = nulo). Luego, al volver de la recursión, se "podan" los nodos que quedaron inútiles: un nodo puede eliminarse si no marca el fin de otra palabra y no tiene hijos. La poda se detiene en el primer nodo que todavía es necesario (porque es fin de otra palabra o porque tiene otros hijos), de modo que los prefijos compartidos con otras palabras se conservan. La raíz nunca se elimina.

> Por ejemplo, si están `"casa"` y `"cas"` y borramos `"casa"`, solo se borra el nodo de la última `a`. El nodo de la `s` queda porque ahí termina `"cas"`.

La raíz nunca se borra.

### Precondiciones
- El Trie está creado
- La palabra no es `null` y tiene solo letras válidas

### Postcondiciones
- Si la palabra estaba, se elimina y devuelve `true`
- Si no estaba, el Trie queda igual y devuelve `false`
- Las demás palabras siguen estando con sus datos
- No quedan nodos sueltos que no lleven a ninguna palabra

### Seudocódigo

```
// En Trie
eliminar(palabra) : boolean
COM
    SI buscar(palabra) == null ENTONCES
        devolver false
    FIN SI
    raiz.eliminar(palabra, 0)
    devolver true
FIN

// En NodoTrie
// devuelve true si este nodo se puede borrar
eliminar(palabra, pos) : boolean
COM
    SI pos == palabra.largo ENTONCES
        esPalabra <- false
        dato <- null
        devolver NO tieneHijos()
    FIN SI

    indice <- palabra[pos] - 'a'
    SI hijos[indice].eliminar(palabra, pos + 1) ENTONCES
        hijos[indice] <- null
        devolver (NO esPalabra) Y (NO tieneHijos())
    FIN SI

    devolver false
FIN

// En NodoTrie
tieneHijos() : boolean
COM
    PARA i DESDE 0 HASTA 25 HACER
        SI hijos[i] != null ENTONCES
            devolver true
        FIN SI
    FIN PARA
    devolver false
FIN
```

> Primero llamamos a `buscar` para asegurarnos de que la palabra existe. Así, en `eliminar` del nodo ya sabemos que el camino está completo y no hay que chequear `null`.

### Orden

- La búsqueda previa es **O(m)**
- Después bajamos de nuevo `m` niveles, y al volver en cada nivel podemos llamar a `tieneHijos()`, que recorre 26 posiciones (constante)

> **O(m) + O(m) = O(m)**

