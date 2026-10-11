-- Ejecutar después de los dos scripts oficiales, una sola vez.
USE biblioteca;
ALTER TABLE prestamos ADD COLUMN fecha_devolucion_real DATE NULL;
-- Los préstamos históricos devueltos conservan NULL: no se inventa su fecha real.
