# Manual de Instalación y Uso — GestFCT

**Aplicación web de Gestión de Formación en Empresa**
Desarrollada con Spring Boot + Thymeleaf + MySQL

---

## Requisitos previos

| Software | Versión mínima |
|---|---|
| Java JDK | 17 o superior |
| MySQL Server | 8.0 o superior |
| Maven | 3.8 o superior (o usar el wrapper incluido `mvnw`) |
| Cuenta Gmail | Con contraseña de aplicación generada |

---

## 1. Configuración de la base de datos

Crear la base de datos en MySQL antes de arrancar:

```sql
CREATE DATABASE gestionfp CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

Abrir el fichero `src/main/resources/application.properties` y ajustar las credenciales si fuera necesario:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/gestionfp?useSSL=false&serverTimezone=Europe/Madrid
spring.datasource.username=root
spring.datasource.password=root
```

### Persistencia de datos entre reinicios

La propiedad más importante para el comportamiento de los datos es:

```properties
spring.jpa.hibernate.ddl-auto=create-drop
```

| Valor | Comportamiento |
|---|---|
| `create-drop` | **Borra y recrea** todas las tablas al arrancar y al parar. **Los datos se pierden.** Útil solo en desarrollo. |
| `update` | **Mantiene los datos** entre reinicios. Solo añade columnas nuevas si las hay. **Recomendado para la defensa.** |
| `validate` | No modifica nada, solo comprueba que el esquema coincide. Para producción. |

**Antes de la defensa**, cambiar a `update` para que los datos introducidos persistan:

```properties
spring.jpa.hibernate.ddl-auto=update
```

> Al cambiar de `create-drop` a `update` por primera vez, Hibernate creará las tablas automáticamente la primera vez que arranque la aplicación. No hace falta crear las tablas manualmente.

---

## 2. Configuración del envío de emails

La aplicación envía un email automático al alumno cada vez que se le crea una práctica. Usa Gmail como servidor SMTP.

### Paso 1 — Activar la verificación en dos pasos en Gmail

La cuenta Gmail configurada debe tener la verificación en dos pasos activada. Ir a: **Cuenta de Google → Seguridad → Verificación en dos pasos**.

### Paso 2 — Generar una contraseña de aplicación

1. Ir a **Cuenta de Google → Seguridad → Contraseñas de aplicación**
2. Seleccionar "Otra aplicación (nombre personalizado)" → escribir `GestFCT`
3. Google genera una contraseña de 16 caracteres (formato `xxxx xxxx xxxx xxxx`)
4. Copiar esa contraseña en `application.properties`:

```properties
spring.mail.username=tu-correo@gmail.com
spring.mail.password=xxxx xxxx xxxx xxxx
```

> La contraseña de aplicación no es la contraseña habitual de Gmail. Es una clave específica generada por Google para apps externas. Solo se muestra una vez al crearla.

### Verificar que el email funciona

Al crear una práctica para un alumno, la aplicación envía automáticamente un email con:
- Nombre de la empresa y del tutor laboral
- Email del tutor laboral
- Fechas de inicio y fin de la práctica

Si el envío falla (por ejemplo, credenciales incorrectas), la práctica **sí se guarda** igualmente en la base de datos; solo falla el email.

---

## 3. Arrancar la aplicación

Desde la raíz del proyecto, ejecutar:

```bash
./mvnw spring-boot:run
```

O en Windows:

```cmd
mvnw.cmd spring-boot:run
```

La aplicación estará disponible en: **http://localhost:8080**

---

## 4. Primer acceso

Al arrancar por primera vez, se crea automáticamente un usuario administrador:

| Campo | Valor |
|---|---|
| Email | `admin@fpemp.com` |
| Contraseña | `admin123` |
| Rol | Directiva |

> Se recomienda cambiar la contraseña del administrador tras el primer acceso.

---

## 5. Funcionalidades por rol

### Profesores de la Directiva

Tienen acceso completo a toda la aplicación:

- **Gestionar Profesores** — crear, editar y eliminar profesores. No se puede eliminar el último miembro de la Directiva ni el propio usuario autenticado.
- **Gestionar Cursos** — crear, editar y eliminar cursos. No se puede eliminar un curso que tenga alumnos matriculados.
- **Ver Estadísticas** — gráficas y tablas con el número de alumnos asignados a cada empresa y por cada curso.
- Todo lo que puede hacer un profesor normal (ver abajo).

### Todos los profesores

- **Gestionar Empresas** — CRUD completo. Campos: nombre, descripción, nombre y email del tutor laboral.
- **Gestionar Alumnos** — CRUD completo + importación desde CSV + filtro por curso.
- **Gestionar Prácticas** — CRUD completo. Al crear una práctica se envía email automático al alumno.

---

## 6. Importación de alumnos desde CSV

En la sección de Alumnos, el botón **"Importar CSV"** permite cargar todos los alumnos de un curso desde un fichero de texto.

**Formato del fichero CSV:**

```
nombre,apellidos,email,fechaNacimiento
Ana,García López,ana.garcia@email.com,15/03/2005
Carlos,Martínez Ruiz,carlos.martinez@email.com,22/07/2004
```

- La primera fila es la cabecera y se ignora automáticamente.
- El formato de fecha es `dd/MM/yyyy`.
- Si un alumno con ese email ya existe, se omite (no genera error).
- Hay que seleccionar el curso al que se asignarán los alumnos importados.

---

## 7. Internacionalización (español / inglés)

La página de **lista de alumnos** está disponible en dos idiomas. Para cambiar el idioma, usar los botones de la propia página o añadir el parámetro a la URL:

- Español: `http://localhost:8080/alumnos?lang=es`
- Inglés: `http://localhost:8080/alumnos?lang=en`

El idioma se guarda en la sesión del usuario, por lo que no es necesario indicarlo en cada petición.

---

## 8. Servicio REST (para app móvil)

La API REST no requiere autenticación y está disponible en `/api/alumnos`:

| Método | URL | Descripción |
|---|---|---|
| GET | `/api/alumnos` | Lista todos los alumnos |
| GET | `/api/alumnos/{id}` | Obtiene un alumno por ID |
| GET | `/api/alumnos/curso/{cursoId}` | Alumnos de un curso concreto |
| POST | `/api/alumnos` | Crea un alumno nuevo (JSON) |
| PUT | `/api/alumnos/{id}` | Actualiza un alumno (JSON) |

**Ejemplo de cuerpo JSON para crear o actualizar:**

```json
{
  "nombre": "Ana",
  "apellidos": "García López",
  "email": "ana.garcia@email.com",
  "fechaNacimiento": "2005-03-15",
  "curso": { "id": 1 }
}
```

---

## 9. Resumen rápido para la defensa

1. Cambiar `ddl-auto=update` en `application.properties`
2. Configurar la contraseña de aplicación de Gmail correcta
3. Arrancar la aplicación con `mvnw spring-boot:run`
4. Acceder con `admin@fpemp.com` / `admin123`
5. Crear cursos → crear empresas → crear/importar alumnos → crear prácticas
6. Verificar que el email llega al alumno al crear la práctica
7. Acceder a Estadísticas para ver las gráficas
8. Demostrar el cambio de idioma en la lista de alumnos
