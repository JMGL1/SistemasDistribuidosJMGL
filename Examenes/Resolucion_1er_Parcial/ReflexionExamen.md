# Reflexión sobre el Primer Parcial de Sistemas Distribuidos

Como estudiante, tras revisar el desarrollo de mi código original para el **Micro-sistema de Venta de Tours con Pago Bancario**, me doy cuenta de los motivos por los cuales el código quedó inconcluso y no pude alcanzar el 100% de la calificación. A continuación, detallo mis principales falencias durante el desarrollo del examen:

## 1. Falta de Apuntes y Fragmentos de Código Base (Snippets)
Durante el examen, me faltó tener a la mano mis apuntes con las plantillas de código para las conexiones de RMI y Sockets. Al intentar programar la infraestructura "de memoria", olvidé reglas y pasos fundamentales:
* **Detalles de configuración:** Olvidé colocar ciertas anotaciones e interfaces obligatorias en las clases de transferencia, un paso estrictamente necesario para que los objetos puedan viajar a través de los diferentes nodos de la red sin generar errores.
* **Arranque de servidores:** No recordé la sintaxis exacta para levantar, configurar y publicar los servidores en la red, lo que ocasionó que algunos de mis nodos principales quedaran como clases totalmente vacías y sin lograr conectarse entre sí.

## 2. Bloqueo en la Lógica Distribuida
Aunque logré crear la estructura básica de los archivos, me faltó soltura para implementar la lógica de negocios que demandaba el diagrama:
* **Roles múltiples:** Tuve dificultades para programar nodos que debían cumplir roles duales al mismo tiempo (por ejemplo, ser un Servidor para recibir peticiones y a la vez actuar como un Cliente para consultar a otro nodo). Esta arquitectura mixta me bloqueó.
* **Ausencia de nodos:** Debido a la presión del tiempo y la confusión, terminé omitiendo por completo la creación de algunos nodos enteros del sistema, dejando el flujo de comunicación roto.
* **Procesamiento de datos:** Si bien en algunos casos logré establecer la conexión de los Sockets, fallé al momento de procesar los mensajes de texto entrantes, separarlos adecuadamente y aplicar la lógica o las fórmulas que se requerían para responder correctamente.

## 3. Confusión entre Tecnologías y Protocolos
* Se me mezclaron los conceptos y las implementaciones prácticas de las conexiones **TCP** frente a las **UDP**. En el caso de los datagramas, tuve problemas para extraer la información correctamente del paquete y generar una respuesta válida.
* A nivel general, costó asimilar cómo mantener los ciclos de escucha de los servidores activos sin que la aplicación se congelara esperando una respuesta.

## 4. Presión del Tiempo y Mala Organización
* Gasté demasiado tiempo inicial creando los esqueletos de todas las interfaces y los modelos de datos de manera aislada, sin probar la comunicación.
* Al dejar la implementación de la lógica interna para el final, el tiempo me jugó en contra y no alcancé a interconectar el ecosistema. Si hubiera programado y testeado nodo por nodo (probando uno y asegurando que se comunicara con el siguiente), habría entregado un proyecto mucho más sólido.

En conclusión, el examen no se logró al 100% porque faltó más práctica integrando múltiples protocolos de red de forma simultánea. No haber contado con los fragmentos de código base de apoyo y no seguir una estrategia de desarrollo incremental fueron los factores principales del fallo.
