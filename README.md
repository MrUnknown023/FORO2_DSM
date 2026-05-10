# FORO2_DSM
### 📋 Funcionalidades Pendientes (Core)
- [x] **Historial de Gastos:** Implementar una pantalla con un componente `LazyColumn` (Jetpack Compose) para recuperar y listar los documentos de la colección `gastos` en Firestore, filtrando únicamente los que coincidan con el `userId` actual.
- [x] **Cálculo de Total Mensual:** Desarrollar la lógica en el ViewModel para sumar los montos del mes en curso y mostrar un indicador dinámico en la pantalla principal.
- [x] **Filtros de Búsqueda (Opcional):** Añadir opciones en la interfaz para que el usuario pueda filtrar su historial por categoría de gasto o por mes específico.

### 🛡️ Validaciones y Experiencia de Usuario (UX)
- [x] **Validación Estricta de Fechas:** Reemplazar el `TextField` de texto libre para la fecha por un componente `DatePickerDialog` para asegurar que el formato (dd/MM/yyyy) sea consistente en la base de datos.
- [x] **Menú Desplegable de Categorías:** Cambiar el campo de texto de "Categoría" por un `DropdownMenu`. Restringir las opciones a valores predefinidos (ej. Alimentación, Transporte, Salud) para evitar discrepancias en la escritura y facilitar el filtrado posterior.
- [x] **Persistencia de Sesión:** Agregar validación en el `AppNavigation` o `MainActivity` para verificar si `FirebaseAuth.getInstance().currentUser` no es nulo al abrir la app, redirigiendo al usuario directamente a la pantalla de gastos sin tener que loguearse cada vez.
- [ ] **Reglas de Seguridad en Firestore:** Actualmente la base de datos está en "Modo de Prueba". Se deben escribir reglas en la consola de Firebase para garantizar que un usuario solo pueda leer y escribir documentos donde el campo `userId` coincida con su `request.auth.uid`.
