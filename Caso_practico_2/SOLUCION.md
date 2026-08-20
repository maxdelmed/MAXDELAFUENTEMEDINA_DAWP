## Entidad Prestamo
Para poder manejar los préstamos se agregó la entidad `Prestamo`, que básicamente guarda el libro que se prestó, el usuario que lo pidió, la fecha en que se hizo el préstamo, la fecha límite y la fecha de devolución cuando ya se entrega el libro.
La relación con `Libro` se hizo con `@ManyToOne` porque un mismo libro puede aparecer en varios préstamos con el tiempo, pero cada préstamo solamente corresponde a un libro. Con `Usuario` pasa algo parecido, un usuario puede tener varios préstamos pero cada préstamo pertenece a un solo usuario.
Cuando se registra un préstamo se toma la fecha actual y la fecha límite se calcula 14 días después. La fecha de devolución queda vacía hasta que el libro se devuelve. También se baja una copia disponible del libro, y cuando se registra la devolución esa copia se vuelve a sumar.

## Roles y seguridad
Para los roles se usó un `enum Rol` con los valores `BIBLIOTECARIO` y `LECTOR`, así no se tienen que estar usando Strings diferentes para los roles en varias partes del código.
El bibliotecario tiene acceso a las funciones administrativas como crear, editar y eliminar libros, registrar préstamos, devolverlos y ver la lista completa de préstamos. El lector por otro lado puede ver el catálogo y revisar sus propios préstamos.
Para controlar eso se utilizó `@PreAuthorize`. La idea es que la seguridad no dependa solamente de esconder botones en el HTML, porque un lector todavía podría intentar entrar directamente escribiendo una ruta en el navegador. Con `@PreAuthorize` Spring revisa el rol antes de dejar entrar a la operación.
También se agregó una página 403 para cuando un usuario ya inició sesión pero intenta entrar a una parte donde no tiene permisos.

## Préstamos de cada usuario
Para la opción de "Mis préstamos" se toma el usuario que tiene la sesión iniciada y se buscan solamente los préstamos que pertenecen a ese usuario.
De esta manera un lector puede revisar lo suyo sin poder ver la lista de préstamos de los demás usuarios.

## API REST

También se agregó una API REST para los libros.
Con `GET /api/libros` se puede obtener el catálogo completo en formato JSON.
Con `GET /api/libros/{id}` se busca un libro específico. Si existe devuelve 200 y si no existe devuelve 404. Para manejar esas respuestas se utilizó `ResponseEntity`.
Para crear libros se utiliza `POST /api/libros`. Esta operación solamente la puede hacer un bibliotecario y los datos recibidos se validan con `@Valid`. Si todo está correcto devuelve 201, si los datos están mal devuelve 400 y si un lector intenta usarlo recibe 403.

## Préstamos atrasados

Para los préstamos atrasados se hizo una consulta JPQL en el repositorio.
La consulta busca préstamos que todavía no tengan fecha de devolución y que además tengan una fecha límite menor a la fecha actual.
La consulta se hizo con JPQL porque trabaja directamente con las entidades y atributos de Java en vez de trabajar con los nombres de las tablas como se haría directamente en SQL.
También se utilizó `JOIN FETCH` para traer de una vez la información del libro y del usuario que se necesita mostrar.
Esa misma consulta se usa tanto en la vista de préstamos atrasados como en `GET /api/prestamos/atrasados`, entonces no fue necesario hacer una lógica diferente para cada parte.

## Pruebas en Postman

se probaron cosas como obtener todos los libros, buscar un libro existente, buscar uno que no existe, crear un libro como bibliotecario, itentar hacerlo como lector y también enviar datos inválidos.
Para iniciar sesión desde Postman se utilizó la misma sesión de Spring Security. Primero se hizo un `GET /login` para obtener el token CSRF y después se mandaron las credenciales junto con ese token en el `POST /login`.
Luego de iniciar sesión Postman mantiene la cookie de sesión y se pueden probar las rutas protegidas.

## Organización del proyecto
Se mantuvo la estructura por capas que se ha usado durante el curso.
Los controladores reciben las solicitudes, los servicios manejan la lógica del caso y los repositorios son los que se comunican con la base de datos.
Por ejemplo en un préstamo, el controlador recibe la solicitud, el servicio se encarga de crear el préstamo y cambiar las copias disponibles, y después el repositorio guarda esos cambios.

## Inconsistencia encontrada en las credenciales del proyecto base
Al probar las credenciales que venían con el proyecto se encontró una diferencia entre lo que sería el README y el archivo `seed-data.sql`.
En el README se indicaba que la contraseña de ejemplo era `password123`, pero el hash BCrypt que venía originalmente en el archivo SQL no correspondía con esa contraseña entonces para mantener las credenciales que venían en el proyecto solo se tuvo que reemplazar el hash BCrypt por uno válido para `password123` y no hubieron más problemas con el resto del caso.
Y no se cambiaron los nombres de usuario ni los roles existentes.