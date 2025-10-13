# Spendly - Control de Gastos Inteligente

## Descripción del Proyecto
App móvil para control de gastos personales enfocada en usuarios costarricenses, 
con captura automática de transacciones bancarias mediante SMS/correo.

## Características principales
-  Registro manual de gastos en efectivo
- Captura automática de transacciones bancarias
- Vinculación de múltiples entidades bancarias (sin datos sensibles)
- Categorización inteligente de gastos
- Resumen visual por banco y categoría
- Adjuntar fotos de recibos
- Alertas de presupuesto

##CRUD Implementado
| Operación | Funcionalidad |
|-----------|---------------|
| CREATE    | Agregar gastos, categorías, bancos |
| READ      | Ver historial, resúmenes, reportes |
| UPDATE    | Editar transacciones y categorías |
| DELETE    | Eliminar gastos duplicados |



## Tecnologías
- Kotlin
-  Firebase y room (cuando no hay internet )para base de datos
- APIs de SMS y permisos del sistema

## Diseño y Mockups

### Pantallas principales:
1. **Login/Registro**
   <img width="498" height="900" alt="sign up" src="https://github.com/user-attachments/assets/6f2c06e3-d1d0-47ed-bba4-2df99db36abe" />

3. **Dashboard** - Resumen de gastos
4. **Agregar gasto manual**
5. **Vincular bancos**
6. **Historial de transacciones**
7. **Estadísticas y gráficos**

