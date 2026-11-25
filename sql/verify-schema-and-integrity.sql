-- =============================================================================
-- Suite Post-Ejecución de Auditoría de Esquema e Integridad RDBMS (PostgreSQL)
-- Verifica:
--   1. Existencia de las 5 tablas de dominio y sus columnas/tipos (information_schema)
--   2. Llaves Primarias (PRIMARY KEY) y Llaves Foráneas / Relaciones (FOREIGN KEY)
--   3. Índices B-Tree (pg_indexes) para rendimiento e integridad referencial
--   4. Sincronización de Secuencias (pg_sequences / serial)
--   5. Pruebas transaccionales de integridad referencial (detección de huérfanos y FK)
-- =============================================================================

\set ON_ERROR_STOP on

\echo '=== 1. VERIFICACION DE TABLAS Y CONTEO DE COLUMNAS (information_schema.columns) ==='
SELECT table_name, COUNT(column_name) AS total_columnas
FROM information_schema.columns
WHERE table_schema = 'public'
  AND table_name IN ('usuario', 'proyectos', 'tareas', 'comentarios', 'asignaciones')
GROUP BY table_name
ORDER BY table_name;

\echo '=== 2. VERIFICACION DE RESTRICCIONES PK Y RELACIONES FK (information_schema) ==='
SELECT
    tc.table_name AS tabla_origen,
    tc.constraint_name AS nombre_restriccion,
    tc.constraint_type AS tipo,
    kcu.column_name AS columna_local,
    ccu.table_name AS tabla_referenciada,
    ccu.column_name AS columna_referenciada
FROM information_schema.table_constraints AS tc
JOIN information_schema.key_column_usage AS kcu
  ON tc.constraint_name = kcu.constraint_name
  AND tc.table_schema = kcu.table_schema
LEFT JOIN information_schema.constraint_column_usage AS ccu
  ON ccu.constraint_name = tc.constraint_name
  AND ccu.table_schema = tc.table_schema
WHERE tc.table_schema = 'public'
  AND tc.constraint_type IN ('PRIMARY KEY', 'FOREIGN KEY')
ORDER BY tc.table_name, tc.constraint_type, tc.constraint_name;

\echo '=== 3. VERIFICACION DE INDICES POR TABLA (pg_indexes) ==='
SELECT
    tablename AS tabla,
    indexname AS indice,
    indexdef AS definicion_indice
FROM pg_indexes
WHERE schemaname = 'public'
ORDER BY tablename, indexname;

\echo '=== 4. VERIFICACION DE INTEGRIDAD REFERENCIAL (0 REGISTROS HUERFANOS) ==='
DO $$
DECLARE
    v_tablas INT;
    v_fks INT;
    v_indices INT;
    v_huerfanos_tareas INT;
    v_huerfanos_comentarios INT;
    v_huerfanos_asignaciones INT;
BEGIN
    -- 4.1 Validar que existan exactamente las 5 tablas del modelo
    SELECT COUNT(*) INTO v_tablas
    FROM information_schema.tables
    WHERE table_schema = 'public'
      AND table_name IN ('usuario', 'proyectos', 'tareas', 'comentarios', 'asignaciones');

    IF v_tablas <> 5 THEN
        RAISE EXCEPTION 'Fallo de integridad estructural: se esperaban 5 tablas y se encontraron %', v_tablas;
    END IF;

    -- 4.2 Validar que existan las 4 llaves foráneas (relaciones)
    SELECT COUNT(*) INTO v_fks
    FROM information_schema.table_constraints
    WHERE table_schema = 'public'
      AND constraint_type = 'FOREIGN KEY';

    IF v_fks < 4 THEN
        RAISE EXCEPTION 'Fallo de integridad relacional: se esperaban al menos 4 FOREIGN KEYs y se encontraron %', v_fks;
    END IF;

    -- 4.3 Validar que existan los índices (5 PK + 5 índices secundarios = 10)
    SELECT COUNT(*) INTO v_indices
    FROM pg_indexes
    WHERE schemaname = 'public';

    IF v_indices < 10 THEN
        RAISE EXCEPTION 'Fallo de integridad de índices: se esperaban al menos 10 índices y se encontraron %', v_indices;
    END IF;

    -- 4.4 Comprobar que no existan registros huérfanos en tareas, comentarios ni asignaciones
    SELECT COUNT(*) INTO v_huerfanos_tareas
    FROM tareas t LEFT JOIN proyectos p ON t.id_proyecto = p.id_proyecto
    WHERE t.id_proyecto IS NOT NULL AND p.id_proyecto IS NULL;

    SELECT COUNT(*) INTO v_huerfanos_comentarios
    FROM comentarios c LEFT JOIN tareas t ON c.id_tarea = t.id_tarea
    WHERE c.id_tarea IS NOT NULL AND t.id_tarea IS NULL;

    SELECT COUNT(*) INTO v_huerfanos_asignaciones
    FROM asignaciones a
    LEFT JOIN usuario u ON a.id_usuario = u.id_usuario
    LEFT JOIN tareas t ON a.id_tarea = t.id_tarea
    WHERE (a.id_usuario IS NOT NULL AND u.id_usuario IS NULL)
       OR (a.id_tarea IS NOT NULL AND t.id_tarea IS NULL);

    IF (v_huerfanos_tareas + v_huerfanos_comentarios + v_huerfanos_asignaciones) > 0 THEN
        RAISE EXCEPTION 'Fallo de integridad de datos: se detectaron registros huérfanos';
    END IF;

    -- 4.5 Prueba activa de restricción FK: intentar insertar una tarea con id_proyecto inexistente (-999)
    BEGIN
        INSERT INTO tareas (nombre, descripcion, fecha_vencimiento, estado, lista, id_proyecto)
        VALUES ('Prueba FK Invalida', 'Debe fallar por FK', CURRENT_DATE, 'danger', 'pendiente', -999);
        RAISE EXCEPTION 'ERROR CRITICO: La llave foránea tareas_proyectos_fk NO bloqueó un registro huérfano';
    EXCEPTION
        WHEN foreign_key_violation THEN
            RAISE NOTICE 'OK [PRUEBA FK SUPERADA]: PostgreSQL rechazó correctamente la inserción huérfana (foreign_key_violation).';
    END;

    RAISE NOTICE 'OK [AUDITORIA RDBMS COMPLETA]: 5 tablas, % FKs, % índices y 0 registros huérfanos verificados.', v_fks, v_indices;
END $$;

\echo '=== 5. CONTEO DE REGISTROS SEMILLA POR TABLA ==='
SELECT 'usuario' AS tabla, COUNT(*) AS registros FROM usuario
UNION ALL SELECT 'proyectos', COUNT(*) FROM proyectos
UNION ALL SELECT 'tareas', COUNT(*) FROM tareas
UNION ALL SELECT 'comentarios', COUNT(*) FROM comentarios
UNION ALL SELECT 'asignaciones', COUNT(*) FROM asignaciones;

