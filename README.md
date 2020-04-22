# CC5114
<h1>Redes Neuronales y Programación Genética - Tareas del curso</h1>

<h2>Consideraciones</h2>

1) Las tareas del curso fueron realizadas en Java en el entorno IntelliJ. El código también contiene asserts que no funcionarán hasta usar el comando:

```
java ~ea
```

2) Si al ejecutar Test.java, ve que sólo aparece un gráfico en vez de dos, es probable que uno esté debajo del otro, por lo que sólo debe arrastrar la ventana del gráfico.

3) Requiere del uso de las siguientes librerías externas: JFreeChart, JHeatChart, Spring Framework Context.

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

<h2>Tarea 2</h2>

Para la tarea 2 se utilizaron las siguientes librerías externas:

<ul>
  <li>JFreeChart (El plot que lo extiende es importado desde Tarea 1)</li>
  <li>JHeatChart</li>
  <li>Spring Framework Context</li>
</ul>

El paquete Tarea 2 contiene lo necesario para crear un algoritmo genético. El algoritmo genético consta de las siguientes clases:

<ul>
  <li><b>Population</b>: Esta clase implementa tanto las funciones de selección como de reproducción. Hasta el momento,
  existen StringPopulation y BinaryPopulation.</li>
  <li><b>Fitfun</b>: Corresponde a una interfaz capaz de manejar funciones lambda, esto con el objetivo de pasar funciones como
  parámetro.</li>
  <li><b>GeneticAlgorithm</b>: El algoritmo genético en cuestión.</li>
</ul>

Para crear un algoritmo genético, se debe inicializar un GeneticAlgorithm que reciba la tasa de mutación, tamaño de la población, número de genes, el método de reproducción ("mutation" o "crossover"), dos valores booleanos para indicar si es elitista y si usa optimalidad de pareto, una población y una lista de Pairs, pares que consisten en una función de activación y su prioridad.

El siguiente ejemplo ilustra el problema de encontrar una palabra:

```
ApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
PopulationFactory fac = (PopulationFactory) context.getBean("populationFactory");
GeneticAlgorithm ga = new GeneticAlgorithm(
        0.1,
        1000,
        10,
        "crossover",
        false,
        false,
        fac.createStringPopulation(),
        new ArrayList<Pair<FitFun, Double>>(){
            {
                add(new Pair<FitFun, Double>(
                        individual -> {
                            String[] ind = (String[]) individual;
                            String[] answer = new String[]{"h","e","l","l","o","w","o","r","l","d"};
                            double score = 0.0;
                            for(int i=0; i<answer.length; i++){
                                if(answer[i].equalsIgnoreCase(ind[i])){
                                    score++;
                                }
                            }
                            return score;
                        },
                        0.0
                ));
            }
        }
);

double[][] fitnessess = ga.solve(20);
LinePlot chart1 = new LinePlot("Fitness v/s Generations", fitnessess);
chart1.show_plot();
```

Aquí vemos que el algoritmo es creado con una tasa de mutación de 0.1, tamaño de población de 1000, 10 genes por individuo, usando método del crossover, sin elitismo ni Pareto, inicializado con una población de Strings. La población consiste en individuos representados como arreglos, donde cada celda almacena un gen (en este caso, un caracter). A continuación, se le entrega una lista de pares que contiene un par con la función lambda que representa el fitness, junto con prioridad 0.0 por defecto al ser la única función presente. El experimento se realiza iterando a través de 20 generaciones y termina su ejecución al llegar a este número. Como resultado, se muestra un gráfico de 3 líneas que representan el fitness más bajo, promedio y más alto de cada generación en ese orden. El mejor fitness se alcanza en la quinta generación.

![alt text](https://github.com/Vito217/CC5114/blob/master/src/tarea2/ej1_cross.png)

Si ahora usamos mutación, se observa que el fitness oscila aún más, alcanzando su mejor fitness promedio en la décima generación.

![alt text](https://github.com/Vito217/CC5114/blob/master/src/tarea2/ej1_mut.png)

Podemos repetir el mismo proceso con el ejercicio 2, para tratar de buscar una secuencia de bits. 

```
GeneticAlgorithm ga = new GeneticAlgorithm(
            0.2,
            1000,
            14,
            "crossover",
            false,
            false,
            fac.createBinaryPopulation(),
            new ArrayList<Pair<FitFun, Double>>(){
                {
                    add(new Pair<FitFun, Double>(
                            individual -> {
                                Integer[] ind = (Integer[]) individual;
                                Integer[] answer = new Integer[]{0,0,1,0,1,0,1,0,1,1,0,1,0,1};
                                double score = 0.0;
                                for(int i=0; i<answer.length; i++){
                                    if(ind[i] != null && answer[i].equals(ind[i])){
                                        score++;
                                    }
                                }
                                return score;
                            },
                            0.0
                    ));
                }
            }
    );
```

Usando Crossover:

![alt text](https://github.com/Vito217/CC5114/blob/master/src/tarea2/ej2_cross.png)

Y usando mutación:

![alt text](https://github.com/Vito217/CC5114/blob/master/src/tarea2/ej2_mut.png)

Dado que los resultados en la mutación varían, nos gustaría saber para qué combinaciones de población y tasa de mutación obtenemos mejores resultados. El código para generar un heatmap se encuentra en GeneticArlgorithm, y su uso luce de la siguiente manera:

```
ga.get_heatmap(
    new int[]{50, 1000},
    new double[]{0.0, 1.0},
    50,
    0.1,
    "mutation",
    20
);
```

Donde el primer y segundo parámetro corresponden al rango de la población y de la tasa de mutación, mientras que el tercer y cuarto parámetro representan los Steps para cada uno. El quinto sirve para elegir el método de reproducción en caso de querer ver la evolución de crossover, mientras que el último representa el número de generaciones. El resultado final es un HeatMap representado el mejor fitness por combinación.

En el caso del ejercicio 1, se tiene que el mejor fitness se alcanza con 850 individuos y tasa de mutación 0.6:

![alt text](https://github.com/Vito217/CC5114/blob/master/src/tarea2/ej1_heat.png)

Para el ejercicio 2, el mejor fitness se obtiene con 650 individuos y tasa de mutación 0.9:

![alt text](https://github.com/Vito217/CC5114/blob/master/src/tarea2/ej2_heat.png)

Para el ejercicio opcional, elegiremos el problema de Unbound Knapsack. Aquí, nuestra función de fitness entregará un Score equivalente al máximo valor de una combinación tal que no exceda el peso de 15 Kg. Los genes que utilizaremos consisten en pares de pesos y valores representados como un string <peso>/<valor>. Nuestro algoritmo genético será entonces el siguiente:

```
GeneticAlgorithm ga = new GeneticAlgorithm(
            0.2,
            10,
            5,
            "mutation",
            false,
            false,
            fac.createUBKPopulation(),
            new ArrayList<Pair<FitFun, Double>>(){
                {
                    add(new Pair<FitFun, Double>(
                            individual -> {
                                int[] weights = new int[individual.length];
                                int[] values = new int[individual.length];
                                int total_weight = 0;
                                double score = 0.0;
                                for(int i=0; i<individual.length; i++){
                                    String ind = (String) individual[i];
                                    if(ind != null){
                                        String[] pair = ind.split("/");
                                        weights[i] = Integer.parseInt(pair[0]);
                                        values[i] = Integer.parseInt(pair[1]);
                                    }
                                    else{
                                        weights[i] = 0;
                                        values[i] = 0;
                                    }
                                }
                                for(int i=0; i<individual.length; i++){
                                    total_weight += weights[i];
                                    if(total_weight > 15){
                                        score = 0.0;
                                    }
                                    else{
                                        score += values[i];
                                    }
                                }
                                return score;
                            },
                            0.0
                    ));
                }
            }
    );
```

Usando Crossover, se tiene que el mejor fit se alcanza en la segunda generación, mientras que el mejor promedio se alcanza en la tercera generación:

![alt text](https://github.com/Vito217/CC5114/blob/master/src/tarea2/ej3_cross.png)

Usando Mutación, podemos ver que el mejor fitness promedio se alcanza en la novena generación:

![alt text](https://github.com/Vito217/CC5114/blob/master/src/tarea2/ej3_mut.png)

El Heatmap muestra que las mejores configuraciones de cantidad de individuos por población y tasa de mutación son (750, 0.5) y (100, 1.0):

![alt text](https://github.com/Vito217/CC5114/blob/master/src/tarea2/ej3_heat.png)

Para concluir, podemos ver que en general el crossover funciona mucho mejor que la mutación, aunque el primero sea un tanto más costoso, pues debe iterar sobre todos los genes de un individuo, mientras que la mutación sólo altera un número finito de genes. Su efectividad podría explicarse debido a que mezcla a dos padres que de por sí ya tienen buen fitness, mientras que en la mutación, mientras más grande sea el vocabulario, hay más posibilidades de que un gen bueno sea intercambiado por uno malo.

Es por esta razón que en la práctica la reproducción mediante crossover es más útil. Cabe señalar además las dificultades de eficiencia de esta implementación, pues está sujeta a las limitaciones de Java, por lo que lo recomendable es buscar soluciones alternativas.

<h2>Tarea 3</h2>

Para la tarea 3 se usaron las siguientes librerías:

<ul>
  <li>JFreeChart</li>
  <li>JHeatChart</li>
  <li>Spring Framework Context</li>
</ul>

El paquete Tarea 3 contiene la implementación de un algoritmo genético que se compone de:

<ul>
  <li><b>AST</b>: Generador de árboles aleatorios, basado en el código en Python provisto por Alexandre Bergel.</li>
  <li><b>Node</b>: Clase abstracta con la que se implementan diferentes tipos de nodo, junto con su correspondiente evaluación.</li>
  <li><b>Function</b>: Interfaz de funciones lambda usada para definir funciones de evaluación.</li>
  <li><b>GPFitFun: Interfaz usada para definir funciones de fitness aplicables a árboles.</b></li>
  <li><b>GeneticProgram</b>: El programa genético en cuestión.</li>
</ul>

GeneticProgram debe recibir un tamaño de población, tasa de mutación, función de reproducción como un String (mutation o crossover), un booleano que indica si es elitista, una lista de funciones de fitness y su prioridad, profundidad máxima del árbol, probabilidad de detener la recursión al crear un árbol, una lista de funciones como Nodos, y una lista de terminales como valores.

<h3>Ejercicio 1: Encontrar un número</h3>

Dado el conjunto de funciones +, -, * y MAX, y el conjunto de valores 25, 7, 8, 100, 4 y 2, queremos encontrar una expresión que al evaluarla se acerca al número 65346. Para esto definimos las operaciones extendiendo Node. Por ejemplo, AddNode es de la forma:

```
public class AddNode extends BinaryNode{

    public AddNode(Node left, Node right) {
        super(args -> {
            try{
                return (int) args[0] + (int) args[1];
            } catch (ClassCastException e) {
                return "("+ args[0] + "+" + args[1]+")";
            }
        },
        left,
        right);
    }

    ...
}
```

donde al evaluarse, recibe un par de argumentos y retorna la suma. De manera análoga, podemos definir MultNode, SubNode y MaxNode. Creamos entonces una lista de NodeFactories para generar el árbol aleatorio:

```
ArrayList<NodeFactory> allowed_functions = new ArrayList<NodeFactory>(){
    {
        add(addFact);
        add(subFact);
        add(multFact);
        add(maxFact);
    }
};
```

Paralelamente, definimos la lista de valores:

```
ArrayList<Object> allowed_terminals = new ArrayList<Object>(){
    {
        add(25);
        add(7);
        add(8);
        add(100);
        add(4);
        add(2);
    }
};
```

La función de fitness a usar premia al programa por obtener la mínima diferencia entre el valor esperado y el evaluado. En consecuencia, usamos el inverso de la diferencia.

```
ArrayList<Pair<GPFitFun, Double>> fitfuns = new ArrayList<Pair<GPFitFun, Double>>(){{
    add(new Pair<GPFitFun, Double>(
            tree -> {
                int distance = Integer.MAX_VALUE;
                if(tree != null){
                    int target = 65346;
                    int res = (int) tree.eval();
                    distance = Math.abs(target - res);
                }
                return distance == 0 ? 1 : 1.0/distance;
            },
            0.0
    ));
}};
```

Inicializamos nuestro programa con una población de 1000 individuos, tasa de mutación de 0.1, profundidad máxima del árbol de 10 y probabilidad de 0.4 de detener la recursión.

```
GeneticProgram gp = new GeneticProgram(
      1000,
      0.1,
      "mutation",
      true,
       fitfuns,
      10,
      0.4f,
      allowed_functions,
      allowed_terminals
);
```

Tras 20 generaciones, se obtiene el siguiente gráfico, donde el mejor fitness se alcanza en la generación 14.

![alt text](https://github.com/Vito217/CC5114/blob/master/src/tarea3/ej_1_1.png)

Si queremos penalizar la altura del árbol, podemos suponer que su altura tiene igual prioridad, por lo que añadimos el valor de la profundidad al denominador.

```
ArrayList<Pair<GPFitFun, Double>> fitfuns = new ArrayList<Pair<GPFitFun, Double>>(){{
    add(new Pair<GPFitFun, Double>(
            tree -> {
                int score = Integer.MAX_VALUE;
                if(tree != null){
                    int target = 65346;
                    int res = (int) tree.eval();
                    int distance = Math.abs(target - res);
                    int depth = tree.depth();
                    score = distance + depth;
                }
                return score == 0 ? 1 : 1.0/score;
            },
            0.0
    ));
}};
```

Usando la misma configuración anterior, obtenemos el siguiente gráfico en que el mejor fitness se obtiene en la generación 7:

![alt text](https://github.com/Vito217/CC5114/blob/master/src/tarea3/ej_1_2.png)

Similar al enfoque anterior, añadimos a la expresión el total de repeticiones en un árbol, obtenido como la suma de las repeticiones almacenadas en un hashmap:

```
ArrayList<Pair<GPFitFun, Double>> fitfuns = new ArrayList<Pair<GPFitFun, Double>>(){{
    add(new Pair<GPFitFun, Double>(
            tree -> {
                int score = Integer.MAX_VALUE;
                if(tree != null){
                    int target = 65346;
                    int res = (int) tree.eval();
                    int distance = Math.abs(target - res);
                    int depth = tree.depth();
                    HashMap<Object, Integer> reps = new HashMap<>();
                    tree.repetitions(reps);
                    Collection values = reps.values();
                    int repetitions = 0;
                    for(Object value: values){
                        repetitions += (int) value;
                    }
                    score = distance + depth + repetitions;
                }
                return score == 0 ? 1 : 1.0/score;
            },
            0.0
    ));
}};
```

Vemos que el mejor fitness se alcanza en la generación 11:

![alt text](https://github.com/Vito217/CC5114/blob/master/src/tarea3/ej_1_3.png)

<h3>Ejercicio 2: Encontrar ecuación</h3>

Queremos encontrar la ecuación x^2 + x - 6. Para esto, definimos las siguientes funciones y terminales:

```
ArrayList<NodeFactory> allowed_functions = new ArrayList<NodeFactory>(){
    {
        add(addFact);
        add(subFact);
        add(multFact);
    }
};

ArrayList<Object> allowed_terminals = new ArrayList<Object>(){
    {
        add(-10);
        add(-9);
        add(-8);
        add(-7);
        add(-6);
        add(-5);
        add(-4);
        add(-3);
        add(-2);
        add(-1);
        add(0);
        add(1);
        add(2);
        add(3);
        add(4);
        add(5);
        add(6);
        add(7);
        add(8);
        add(9);
        add(10);
        add("x");
    }
};
```

La función de fitness a usar será aquella que evalúe diferentes valores en un rango determinado y cuyo resultado sea comparado con el output deseado a partir de la ecuación. El score será el inverso del promedio de las diferencias entre el valor esperado y el obtenido.

```
ArrayList<Pair<GPFitFun, Double>> fitfuns = new ArrayList<Pair<GPFitFun, Double>>(){{
    add(new Pair<GPFitFun, Double>(
            tree -> {
                // Encontrar funcion x*x + x - 6
                int distance = Integer.MAX_VALUE;
                if(tree != null){
                    int[] input_range = new int[]{-10,-9,-8,-7,-6,-5,-4,-3,-2,-1,0,1,2,3,4,5,6,7,8,9,10};
                    int[] target_range = new int[]{84,66,50,36,24,14,6,0,-4,-6,-6,-4,0,6,14,24,36,50,66,84,104};
                    double mean_distance = 0.0;
                    HashMap<Object, Integer> vars = new HashMap<>();
                    vars.put("x", 0);
                    for(int i=0; i<input_range.length; i++){
                        vars.put("x", input_range[i]);
                        int res = (int) tree.eval(vars);
                        mean_distance += Math.abs(res - target_range[i]) / (double) input_range.length;
                    }
                    distance = (int) Math.round(mean_distance);
                }
                return distance == 0 ? 1 : 1.0/distance;
            },
            0.0
    ));
}};
```

El siguiente gráfico muestra que el mejor fitness promedio se alcanza en la generación 2:

![alt text](https://github.com/Vito217/CC5114/blob/master/src/tarea3/ej_2.png)

<h3>Ejercicio 3: Implementar división</h3>

Implementamos la clase DivNode de la siguiente manera:

```
public class DivNode extends BinaryNode{

    public DivNode(Node left, Node right){
        super(args -> {
            try{
                return (int) args[0] / (int) args[1];
            } catch (ArithmeticException e) {
                return "undefined";
            }
        },
        left,
        right);
    }

    ...
}
```

La idea es que al encontrarse con una división por cero, la función capta el error aritmético y, en lugar de enviar un valor numérico, envía un string indicando que es indefinido. Con esto, nuestra función de fitness será la siguiente: evaluamos el árbol, y si el valor que arroja es un string (detectado como un error de Cast de String a Int sobre la palabra "indefinido"), entonces se penaliza al árbol con el score más bajo posible, que es cero.

```
ArrayList<Pair<GPFitFun, Double>> fitfuns = new ArrayList<Pair<GPFitFun, Double>>(){{
    add(new Pair<GPFitFun, Double>(
            tree -> {
                // Encontrar funcion x*x + x - 6
                int distance = Integer.MAX_VALUE;
                if(tree != null){
                    int[] input_range = new int[]{-10,-9,-8,-7,-6,-5,-4,-3,-2,-1,0,1,2,3,4,5,6,7,8,9,10};
                    int[] target_range = new int[]{84,66,50,36,24,14,6,0,-4,-6,-6,-4,0,6,14,24,36,50,66,84,104};
                    double mean_distance = 0.0;
                    HashMap<Object, Integer> vars = new HashMap<>();
                    vars.put("x", 0);
                    for(int i=0; i<input_range.length; i++){
                        vars.put("x", input_range[i]);
                        try{
                            int res = (int) tree.eval(vars);
                            mean_distance += Math.abs(res - target_range[i]) / (double) input_range.length;
                        }
                        catch (ClassCastException e){
                            mean_distance = Integer.MAX_VALUE;
                            break;
                        }
                    }
                    distance = (int) Math.round(mean_distance);
                }
                return distance == 0 ? 1 : 1.0/distance;
            },
            0.0
    ));
}};
```

Los resultados se observan en el siguiente gráfico, y no presentta diferencias notorias con el caso anterior:

![alt text](https://github.com/Vito217/CC5114/blob/master/src/tarea3/ej_3.png)

<h3>Heatmap Ejercicio 1</h3>

El siguiente heatmap aplicado sobre el Ejercicio 1 muestra que las mejores combinaciones de tamaño de población y tasa de mutación son (1000, 0.1) y (1000, 0.3).

![alt text](https://github.com/Vito217/CC5114/blob/master/src/tarea3/heatchart.png)

<h3>Análisis</h3>

Como puede verse en los ejercicios, tanto el fitness promedio como el fitness más bajo se mantienen en niveles muy cercanos a cero, mientras que el mejor fitness adquiere un valor relativamente alto. Esto nos dice que los caso en que encuentra la solución son muy pocos, lo que implica que hay que revisar ciertos parámetros, además de la función de fitness, pues el score utilizado es bastante simplificado. Una solución sería usar un parámetro regulador.
