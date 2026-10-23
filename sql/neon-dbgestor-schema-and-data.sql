-- ========================================================
-- Script compatible con PostgreSQL (Neon DB)
-- Migrado desde dump-dbgestor-202406261453.sql (MariaDB/MySQL)
-- ========================================================

-- Eliminar tablas si existen (en orden de dependencias)
DROP TABLE IF EXISTS asignaciones CASCADE;
DROP TABLE IF EXISTS comentarios CASCADE;
DROP TABLE IF EXISTS tareas CASCADE;
DROP TABLE IF EXISTS proyectos CASCADE;
DROP TABLE IF EXISTS usuario CASCADE;
DROP TABLE IF EXISTS hibernate_sequence CASCADE;

-- --------------------------------------------------------
-- Tabla: usuario
-- --------------------------------------------------------
CREATE TABLE usuario (
    id_usuario SERIAL PRIMARY KEY,
    nombre VARCHAR(100),
    correo_electronico VARCHAR(100),
    contrasena VARCHAR(100),
    rol VARCHAR(100),
    imagen_usuario VARCHAR(255)
);

INSERT INTO usuario (id_usuario, nombre, correo_electronico, contrasena, rol, imagen_usuario) VALUES 
(131, 'Ana Paola Ramírez Vázquez', 'ana22533@cbtis75.edu.mx', '0722', 'Estudiante', '#70FAB7'),
(172, 'Liliana Gómez Martínez', 'liliana22512@cbtis75.edu.mx', 'ada', 'Estudiante', '#54EA01'),
(196, 'Perla Joceline Martínez Meza', 'perla22522@cbtis75.edu.mx', '12345', 'Estudiante', '#805611');

-- Ajustar secuencia al ID máximo
SELECT setval(pg_get_serial_sequence('usuario', 'id_usuario'), COALESCE(MAX(id_usuario), 1)) FROM usuario;

-- --------------------------------------------------------
-- Tabla: proyectos
-- --------------------------------------------------------
CREATE TABLE proyectos (
    id_proyecto SERIAL PRIMARY KEY,
    nombre VARCHAR(100),
    descripcion VARCHAR(255),
    fecha_inicio DATE,
    fecha_finalizacion DATE,
    estado_proyecto VARCHAR(100),
    fondo VARCHAR(100)
);

INSERT INTO proyectos (id_proyecto, nombre, descripcion, fecha_inicio, fecha_finalizacion, estado_proyecto, fondo) VALUES 
(197, 'My API Rest', 'Desarrollaran un Api Rest, que satisfaga un sistema de información con al menos 5 tablas.', '2024-05-31', '2024-06-29', 'En proceso', '/Images/animacion1.gif');

SELECT setval(pg_get_serial_sequence('proyectos', 'id_proyecto'), COALESCE(MAX(id_proyecto), 1)) FROM proyectos;

-- --------------------------------------------------------
-- Tabla: tareas
-- --------------------------------------------------------
CREATE TABLE tareas (
    id_tarea SERIAL PRIMARY KEY,
    nombre VARCHAR(100),
    descripcion VARCHAR(255),
    fecha_vencimiento DATE,
    estado VARCHAR(255),
    lista VARCHAR(255),
    id_proyecto INT
);

INSERT INTO tareas (id_tarea, nombre, descripcion, fecha_vencimiento, estado, lista, id_proyecto) VALUES 
(199, 'Base de datos', 'Realizar la estructura de las tablas', '2024-06-13', 'danger', 'terminado', 197);

SELECT setval(pg_get_serial_sequence('tareas', 'id_tarea'), COALESCE(MAX(id_tarea), 1)) FROM tareas;

-- --------------------------------------------------------
-- Tabla: comentarios
-- --------------------------------------------------------
CREATE TABLE comentarios (
    id_comentario SERIAL PRIMARY KEY,
    texto_comentario VARCHAR(250),
    fecha_hora TIMESTAMP,
    id_usuario VARCHAR(100),
    id_tarea INT
);

INSERT INTO comentarios (id_comentario, texto_comentario, fecha_hora, id_usuario, id_tarea) VALUES 
(201, 'Subirlo YA!!!', '2024-06-26 13:48:00', 'Perla Joceline Martínez Meza', 199),
(202, 'hola', '2024-06-26 13:49:00', 'Liliana Gómez Martínez', 199);

SELECT setval(pg_get_serial_sequence('comentarios', 'id_comentario'), COALESCE(MAX(id_comentario), 1)) FROM comentarios;

-- --------------------------------------------------------
-- Tabla: asignaciones
-- --------------------------------------------------------
CREATE TABLE asignaciones (
    id_asignacion SERIAL PRIMARY KEY,
    id_usuario INT,
    id_tarea INT,
    fecha_asignacion DATE
);

-- --------------------------------------------------------
-- Secuencia / Tabla Hibernate para compatibilidad
-- --------------------------------------------------------
CREATE SEQUENCE IF NOT EXISTS hibernate_sequence START WITH 205 INCREMENT BY 1;
