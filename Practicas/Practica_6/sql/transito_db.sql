CREATE DATABASE IF NOT EXISTS transito_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE transito_db;

CREATE TABLE IF NOT EXISTS personas (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    ci         VARCHAR(20)  NOT NULL UNIQUE,
    nombre     VARCHAR(100) NOT NULL,
    apellido   VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS multas (
    id               INT AUTO_INCREMENT PRIMARY KEY,
    codigo           VARCHAR(30)  NOT NULL UNIQUE,
    ci_conductor     VARCHAR(20)  NOT NULL,
    nombre_conductor VARCHAR(200) NOT NULL,
    tipo_infraccion  VARCHAR(150) NOT NULL,
    lugar            VARCHAR(250) NOT NULL,
    monto            DECIMAL(10,2) NOT NULL,
    fecha            DATE NOT NULL,
    estado           ENUM('pendiente','pagada','anulada') NOT NULL DEFAULT 'pendiente',
    fecha_pago       DATETIME NULL DEFAULT NULL,
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

INSERT INTO personas (ci, nombre, apellido) VALUES
('1234567', 'Juan',    'Pérez'),
('7654321', 'María',   'López'),
('9876543', 'Carlos',  'Mamani'),
('1122334', 'Rosa',    'Quispe'),
('5566778', 'Pedro',   'Choque'),
('4433221', 'Lucía',   'Flores'),
('6677889', 'Andrés',  'Condori'),
('3344556', 'Elena',   'Vargas');

INSERT INTO multas (codigo, ci_conductor, nombre_conductor, tipo_infraccion, lugar, monto, fecha, estado) VALUES
('BOL-20261101000001', '1234567', 'Juan Pérez',   'Exceso de velocidad',       'Av. del Maestro Nro. 123',  150.00, '2026-11-01', 'pendiente'),
('BOL-20261102000002', '7654321', 'María López',  'Semáforo en rojo',          'Plaza 25 de Mayo',          200.00, '2026-11-02', 'pendiente'),
('BOL-20261103000003', '9876543', 'Carlos Mamani','Sin cinturón de seguridad', 'Av. Circunvalación Km. 2',  100.00, '2026-11-03', 'pagada');
