# ViaCTG

ViaCTG es una API REST para registrar y hacer seguimiento a daños viales de Cartagena. Usa Java 21, Spring Boot 3.3.4, Spring Data MongoDB, Spring Security y JWT.

## Requisitos y configuración

- JDK 21 o superior.
- MongoDB accesible desde la aplicación.
- Un secreto JWT en Base64 de 32 bytes o más.

```bash
export MONGODB_URI='mongodb://localhost:27017/viasctg_db'
export JWT_SECRET="$(openssl rand -base64 32)"
./mvnw spring-boot:run
```

No se guardan credenciales en el repositorio. Mongo crea al iniciar los índices únicos de `usuarios.email` y `confirmaciones(reporteId, usuarioId)`.

## Arquitectura

El código se organiza bajo `com.viactg` por responsabilidad: `controller`, `service`, `repository`, `model`, `dto`, `mapper`, `security`, `exception` y `config`. Los controladores solo usan DTOs; los servicios concentran reglas de negocio y validan referencias MongoDB.

`Calle` está embebida en `Barrio`, y `HistorialEstado` en `Reporte`. Las demás entidades tienen su propia colección.

## Seguridad

| Acción | Acceso |
| --- | --- |
| Registro y login | Público |
| Consultar reportes, barrios, categorías, comentarios y confirmaciones | Público |
| Crear/editar reportes, comentar, confirmar y consultar datos propios | Usuario autenticado |
| Cambiar estado e historial de reportes | `ADMIN` o `MODERADOR` |
| Administrar barrios y categorías; listar usuarios | `ADMIN` |

El registro público crea un usuario `CIUDADANO`. La base migrada debe contar con un `ADMIN` inicial. Usa el JWT recibido en el encabezado `Authorization: Bearer <token>`.

## Rutas principales

| Recurso | Rutas |
| --- | --- |
| Autenticación | `POST /api/auth/registro`, `POST /api/auth/login` |
| Perfil | `GET/PUT /api/usuarios/me`, `GET /api/usuarios` (`ADMIN`) |
| Reportes | `GET/POST /api/reportes`, `GET/PUT /api/reportes/{id}` |
| Administración de reportes | `GET /api/admin/reportes/pendientes`, `PATCH /api/admin/reportes/{id}/estado`, `GET /api/admin/reportes/{id}/historial` |
| Barrios y calles | `GET/POST /api/barrios`, `GET/PUT/DELETE /api/barrios/{id}`, `POST /api/barrios/{id}/calles` |
| Categorías | `GET/POST /api/categorias`, `GET/PUT /api/categorias/{id}`, `PATCH /api/categorias/{id}/estado` |
| Confirmaciones | `GET/POST /api/reportes/{reporteId}/confirmaciones`, `DELETE /api/reportes/{reporteId}/confirmaciones/me` |
| Comentarios | `GET/POST /api/reportes/{reporteId}/comentarios`, `PUT/DELETE /api/comentarios/{id}` |
| Notificaciones | `GET /api/notificaciones`, `PATCH /api/notificaciones/{id}/leida` |

La documentación interactiva está en `http://localhost:8080/swagger-ui/index.html`.

## Verificación

```bash
./mvnw test
./mvnw clean verify
```

Consulta [HELP.md](HELP.md) para solucionar problemas comunes y [PROGRESS.md](PROGRESS.md) para conocer el trabajo pendiente.
