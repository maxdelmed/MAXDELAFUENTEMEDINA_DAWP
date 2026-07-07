# Nota: siendo sincero, tuve un error al copiar el zip original, entonces puede ser que tuve que reparar algo que no era parte directa del caso. Me di cuenta hasta que no me funcionó varias veces y ya despues lo volvi a importar. Espero que todo esté en orden ya en el producto final.

## Endpoints implementados
- Para empezar, en "EventoController" agregue las rutas que faltaban para completar el CRUD de eventos.
- Para crear eventos hice "GET /eventos/nuevo", que abre el formulario vacio, y "POST /eventos", que guarda el evento cuando los datos pasan las validaciones.
- Para editar use "GET /eventos/{id}/editar", que abre el mismo formulario pero con los datos del evento cargados. Con "POST /eventos/{id}" se guardan los cambios.
-Para eliminar eventos agregue "POST /eventos/{id}/eliminar". No lo deje para que borrara de una vez, sino que primero se abre un modal para confirmar.
- Tambien agregue el filtro por categoria con "GET /eventos/categoria/{categoria}". Lo probe entrando a rutas como "/eventos/categoria/Torneo", para que el listado mostrara solo eventos de esa categoria. (En las evidencias cree uno de Torneo y filtré por Taller)

## Validaciones
- En el "pom.xml" agregue la dependencia de validaciones para poder usar las anotaciones en la entidad.
- En la clase "Evento" agregue validaciones para evitar que se guarden eventos con datos vacios o incorrectos.
- El nombre, el lugar y la categoria los deje como obligatorios porque son datos que deberían de ser basicos para poder registrar un evento y tambien puse limites de tamaño en algunos textos para que no se escriban valores demasiado largos.
- La fecha no puede quedar vacia y debe ser especificamente desde la fecha actual a cualquier fecha futura. Lo hice asi porque como la aplicacion esta pensada para eventos que todavia se van a realizar, sería raro poder tener eventos en el pasado, ya que se podrían falsificar (si fuera un caso de la vida real).
-El cupo maximo debe ser mayor que cero y el precio puede ser cero, por si acaso se hace un evento gratis, pero no puede ser un número negativo.
- En los metodos POST use "@Valid" y "BindingResult" y sirven para revisar si el formulario tiene errores antes de guardar. Si hay errores, se vuelve al formulario y se muestran los mensajes como se muestra en evidencia 3 de "validaciones".

## Formulario
- Agregué el archivo "eventos/form.html" para usarlo al crear y editar eventos.
- Cuando se crea un evento el formulario se abre (vacío) y cuando se edita se busca el evento por id y luego se carga el formulario con la informacion que ya tenia.
- Tambien dejé un campo "oculto" para el id para que al editar no se cree otro evento nuevo.

## Listado y botones
- En el listado agregue el boton de nuevo evento.
- Tambien agregue los botones de editar y eliminar en cada evento. Originalmente estaba intentando crear el boton de ver detalle pero como mencioné antes, al volver a importarlo me dí cuenta que ya venia funcionando en el proyecto base y que era error mío al intentar importarlo como zip en vez de descomprimirlo primer, entonces lo deje como estaba.

## Modal de eliminar
- Para eliminar eventos usé un modal de Bootstrap en lugar de "confirm()".
- Cada boton de eliminar lleva datos como "data-id" y con eso el modal sabe cual evento se va a eliminar y puede mostrar el nombre antes de confirmar.
- Tambien la accion del formulario del modal para mandar el POST se cambió a la ruta "/eventos/{id}/eliminar"
- Asi primero se confirma la accion y despues se elimina el evento.

## Decisiones
- No agregué funciones extra porque me enfoque en lo obligatorio por falta de control de tiempo con otros cursos. Entonces me enfoqué en crear, editar, eliminar, validar, filtrar por categoria y los botones de accion.
-Usé el mismo formulario para crear y editar porque los campos eran los mismos. Me parecio mas simple que hacer dos vistas separadas.
## Extra
-Vacío por ahora si es que no hago un cambio antes de la hora de entrega.