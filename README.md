# Valenbici - Conexión AWS

## Descripción
Aplicación Java con interfaz gráfica que consume la API pública de 
Valenbici (Open Data Valencia) para obtener datos de las estaciones 
de bicicletas y almacenarlos en una base de datos MariaDB alojada 
en Amazon RDS (AWS).

## Tecnologías utilizadas
- Java (Eclipse)
- MySQL Workbench
- Amazon RDS (MariaDB) - AWS Academy Learner Lab
- API Valenbici (Open Data Valencia)
- HTML/CSS/JavaScript

## Funcionalidades
- Página web con listado de estaciones en tiempo real
- Página web con mapa geolocalizado de las estaciones de Valencia
- Aplicación de escritorio para cargar X estaciones desde la API
  y guardarlas en la base de datos

## Base de datos en AWS
- Motor: MariaDB
- Host: databasevalenbici.cpbeua9pymas.us-east-1.rds.amazonaws.com
- Puerto: 3306
- Base de datos: valenbisi

## Estructura de la tabla
```sql
CREATE TABLE historico (
    id INT AUTO_INCREMENT PRIMARY KEY,
    estacion_id INT NOT NULL,
    direccion VARCHAR(255),
    bicis_disponibles INT NOT NULL,
    anclajes_libres INT NOT NULL,
    estado_operativo BOOLEAN NOT NULL,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ubicación POINT
);
```

## Autor
Rubén Amigó Lara - 1º Desarrollo de Aplicaciones Web - IES Juan de Garay
