# Gestión Automotriz - Control de Inventario de Taller

## 1. Nombre del Proyecto
**Gestión Automotriz** (Aplicación móvil Android + Backend Spring Boot)

## 2. Integrantes
- Benjamin Ramirez 
- Tomás Rodríguez

## 3. Funcionalidades
- Autenticación por roles (`Propietario` y `Operador`).
- CRUD completo de productos e insumos automotrices con alertas de stock mínimo.
- Registro de movimientos de inventario (Ingreso, Venta, Consumo, Ajuste, Devolución) con trazabilidad y validación antinegativos.
- Módulo de reposición agrupado por proveedor con generación automática de borrador de correo.
- Sincronización en tiempo real entre almacenamiento local (Room) y microservicios REST con MySQL.
- Consumo de API externa (`JSONPlaceholder`) para gestión de publicaciones técnicas.

## 4. Endpoints Utilizados
### Microservicios Propios (`http://10.0.2.2:8080/api/...`)
- `GET /api/productos` | `POST /api/productos` | `PUT /api/productos/{code}` | `DELETE /api/productos/{code}`
- `GET /api/movimientos` | `POST /api/movimientos`
- `GET /api/proveedores` | `POST /api/proveedores` | `DELETE /api/proveedores/{id}`

### API Externa (`https://jsonplaceholder.typicode.com/`)
- `GET /posts` | `POST /posts` | `PUT /posts/{id}` | `DELETE /posts/{id}`

## 5. Pasos para Ejecutar
1. Iniciar el servicio de **MySQL** en el puerto `3306`.
2. Abrir la carpeta `backend-automotriz` en IntelliJ IDEA / VS Code y ejecutar `BackendAutomotrizApplication.java` (puerto `8080`).
3. Abrir el proyecto Android `gestion_automotriz` en Android Studio y ejecutar en un emulador (API 24+).
4. Iniciar sesión con `admin` / PIN `1234` o `operador` / PIN `0000`.

## 6. Captura del APK Firmado y Llave `.jks`
![Captura APK Firmado](ruta/captura_apk_firmado.png)
