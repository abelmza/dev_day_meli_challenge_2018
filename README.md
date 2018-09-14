# dev_day_meli_challenge_2018
## Desafio MELI para el día del programador del 2018.

Es un proyecto Android, que se encuentra dentro del directorio `/desafio_dev_day`

Para poder correr la app, es necesario
- Copiar el directorio `/splitted` a la raíz de la tarjeta SD
- Una vez instalada, en las opciones de configuración de la app, habilitar el permiso para acceder al almacenamiento externo.

Al ejecutar la app, una vez finalizados los cálculos, esto es lo que muestra:
[![Foo](https://raw.githubusercontent.com/abelmza/dev_day_meli_challenge_2018/master/screenshot.jpg)](https://raw.githubusercontent.com/abelmza/dev_day_meli_challenge_2018/master/screenshot.jpg)

## Funcionamiento

La app utiliza como entrada 400 recortes de la imagen original (se obtuvieron utilizando https://imagesplitter.net/).
Por cada uno de estos recortes, intenta armar el rompecabezas siguiendo la logica de que los colores en las esquinas de cada imagen deben coincidir.

El algoritmo tiene distintas partes:
- Por cada cuadrado, o `Split`, busca cuales son los Splits cercanos (superior, inferior, izquierdo y derecho) mediante la comparación de los colores de las esquinas, tal como se mencionó anteriormente.
- En los casos en donde más de un Split cumple con los requerimientos, se intenta utilizar la siguiente logica:

```
A   B
C   D
```
- Asumiendo que `A`, `B`, `C` y `D` son Splits, supongamos que se quiera buscar el Split que está a la derecha de `A`. Si más de un Split cumple con los requisitos, la app intenta llegar mediante otros caminos. Por ejemplo, en el caso planteado, una opcion sería buscar el inferior de `A` (que sería `C`), para posteriormente buscar el derecho de `C` (en el ejemplo, `D`), y finalmente el superior de `D`, que debe coincidir con el derecho de `A`.
- En algunos casos en los que incluso utilizando esta metodología no fue posible llegar a los splits contiguos, se crearon HashMaps para alimentar el algoritmo.
