# CC5114
<h1>Tareas del curso</h1>

<h2>Tarea 1</h2>

La Tarea 1 contiene lo necesario para crear una red neuronal en Java. Para crear una red neuronal, se debe crear una NeuralNetwork, y asignarle un array Layer[ ] con las capas a usar. Cada capa se inicializa como un Layer cuyo numero de inputs por perceptrón es igual al número de dimensiones de cada dato de entrada, y el número de outputs corresponde a la cantidad de neuronas a usar.. Además, se le puede asignar un learning rate, función de activación, y función de costo.

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
  <li><b>void train(data, target, iterations)</b>, donde se le entrega la data a entrenar, el conjunto de valores deseados y el número de iteraciones. 
  
  </li>
  <li><b>DataTuple eval(data, target)</b>, donde se le entrega la data a evaluar, el conjunto de valores deseados, y retorna una tupla que contiene la misma data de evaluación y los resultados obtenidos.

</li>
</ul>

Tanto la data como el target se entregan en formato double [ ] [ ]. Estas matrices pueden generarse usando el método <b>read_class_dataset(file, separator)</b>, donde file es el path al archivo y separator el caracter separador entre cada valor. El resultado es una tupla con la matriz de datos y la matriz de datos objetivo. Este último tiene cada fila (o clase) en formato One Hot Encoding. 

Se debe tener en cuenta que el programa asume que:

<ul>
  <li>Cada fila de datos tiene una sola clase</li>
  <li>La clase correspondiente a cada fila se encuentra al último lugar. Así, cada fila de datos debe venir de la forma "x1,x2,x3,x4,clase1", sea cual sea el separador entre cada valor</li>
  <li>Las filas están ordenadas y agrupadas según clase. Es decir, no puede haber una clase diferente entre dos filas de igual clase</li>
</ul>
