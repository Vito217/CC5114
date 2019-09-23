# CC5114
<h1>Tareas del curso</h1>

<h2>Consideraciones</h2>

1) Las tareas del curso fueron realizadas en Java en el entorno IntelliJ. El código también contiene asserts que no funcionarán hasta usar el comando:

```
java ~ea
```

2) Si al ejecutar Test.java, ve que sólo aparece un gráfico en vez de dos, es probable que uno esté debajo del otro, por lo que sólo debe arrastrar la ventana del gráfico.

<h2>Tarea 1</h2>

La Tarea 1 contiene lo necesario para crear una red neuronal en Java. Para crear una red neuronal, se debe crear una NeuralNetwork, y asignarle un array Layer[ ] con las capas a usar. Cada capa se inicializa como un Layer cuyo numero de inputs por perceptrón es igual al número de dimensiones de cada dato de entrada, y el número de outputs corresponde a la cantidad de neuronas a usar. Además, se le puede asignar un learning rate, función de activación, y función de costo.

A continuación se muestra un ejemplo:

```
NeuralNetwork net = new NeuralNetwork(
                new Layer[]{
                        new Layer(3,
                                  2,
                                  0.1,
                                  "sigmoid",
                                  "cross"),
                        new Layer(2,
                                  4,
                                  0.1,
                                  "sigmoid",
                                  "cross")
                }
        );
```

En el ejemplo anterior, la primera capa tiene valores input_size = 3, n_neurons = 2, learning rate = 0.1, con función de activación Sigmoide y función de costo Cross-Entropy. Esto indica que por cada neurona entrará un vector de 3 dimensiones (x1,x2,x3) que da como resultado un vector de 2 dimensiones (y1,y2). La siguiente capa tiene un input_size = 2, dado que recibe el output de la capa anterior.

Los métodos que contiene una red neuronal son:

<ul>
  <li><b>Tuple train(data, target, iterations)</b>, donde se le entrega la data a entrenar, el conjunto de valores deseados y el número de iteraciones. El resultado es una tupla que contiene tanto los Loss como los resultados correctos por iteración.
  
  </li>
  <li><b>Tuple eval(data, target)</b>, donde se le entrega la data a evaluar, el conjunto de valores deseados, y retorna una tupla que contiene la misma data de evaluación y los resultados obtenidos.

</li>
</ul>

Tanto la data como el target se entregan en formato double [ ] [ ]. Estas matrices pueden generarse usando el método <b>read_class_dataset(file, separator)</b> de DataUtils.java, donde file es el path al archivo y separator el caracter separador entre cada valor. El resultado es una tupla con la matriz de datos y la matriz de datos objetivo. Este último tiene cada fila (o clase) en formato One Hot Encoding.

El algoritmo usado para codificación OHE es el siguiente:
<ul>
  <li>Se hace append de las clases a un array list</li>
  <li>Por cada fila de datos, crear un vector de largo igual a la cantidad de clases, inicializado con ceros</li>
  <li>vector_clase[fila][índice de la clase] = 1.0</li>
</ul>

Se debe tener en cuenta que el programa asume que:

<ul>
  <li>Cada fila de datos tiene una sola clase</li>
  <li>La clase correspondiente a cada fila se encuentra al último lugar. Así, cada fila de datos debe venir de la forma "x1,x2,x3,x4,clase1", sea cual sea el separador entre cada valor</li>
  <li>Las filas están ordenadas y agrupadas según clase. Es decir, no puede haber una clase diferente entre dos filas de igual clase</li>
</ul>

También es posible separar la data en subconjuntos de entrenamiento y evaluación usando la función <b>separate_train_and_eval_data(data, target, percentage)</b> de DataUtils.java, donde percentage es el porcentaje entre 0.00 y 1.00 de datos que se irá al subconjunto de training, mientras que el resto se irá al subconjunto de entrenamiento.

Todo los elementos señalados, así como el detalle de la implementación, se encuentran en la clase Test.java. El ejemplo empleado tomó como datos el archivo iris.data, con un total de 150 datos, usando un 80 por ciento para entrenamiento. Se utilizó una red neuronal de dos capas con función de activación sigmoide y mean square error, con un learning rate de 0.1. En total se hicieron 100000 iteraciones, cada iteración correspondiente a un epoch (pasa toda la data). 

```
NeuralNetwork n = new NeuralNetwork(
                new Layer[]{
                        new Layer(
                                4,
                                4,
                                0.1,
                                "sigmoid",
                                "mse"
                        ),
                        new Layer(
                                4,
                                3,
                                0.1,
                                "sigmoid",
                                "mse"
                        )
                }
        );
```

Como se aprecia en los siguientes gráficos, el número de aciertos crece rápidamente, mientras que el loss tabién decrece rápidamente.

![alt text](https://github.com/Vito217/CC5114/blob/master/src/tarea1/l_per_it.png)

![alt text](https://github.com/Vito217/CC5114/blob/master/src/tarea1/s_per_it.png)

La matriz de confusión obtenida fue:

```
[   ,97      ,03      ,00    ]
[   ,03      ,95      ,02    ]
[   ,00      ,07      ,93    ]
```

Como puede observarse, la red da buenos resultados, teniendo más de un 90 porciento final de precisión, con un error y número de predicciones correctas que convergen rápidamente.

La principal dificultad al momento de implementar esta red no fue precisamente el conocimiento con respecto a redes neuronales, pues ya existía experiencia previa. El mayor desafío que se enfrentó fue realizar una programiación de lo más eficiente en Java, pues como puede observarse en el código fuente, los cálculos se hacen de manera iterativa, y apenas se hace uso de librerías.
