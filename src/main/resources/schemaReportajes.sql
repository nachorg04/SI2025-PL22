DROP TABLE IF EXISTS `Ofrecimiento`;
DROP TABLE IF EXISTS `Version_Reportaje`;
DROP TABLE IF EXISTS `Reportaje`;
DROP TABLE IF EXISTS `Asignacion`;
DROP TABLE IF EXISTS `Empresa_Comunicacion`;
DROP TABLE IF EXISTS `Evento`;
DROP TABLE IF EXISTS `Reportero`;
DROP TABLE IF EXISTS `Agencia`;

CREATE TABLE `Agencia` (
  `id_agencia` INTEGER PRIMARY KEY AUTOINCREMENT,
  `nombre` varchar(255)
);

CREATE TABLE `Reportero` (
  `id_reportero` INTEGER PRIMARY KEY AUTOINCREMENT,
  `nombre` varchar(255),
  `telefono` varchar(255),
  `id_agencia` INTEGER,
  FOREIGN KEY (`id_agencia`) REFERENCES `Agencia` (`id_agencia`)
);

CREATE TABLE `Evento` (
  `id_evento` INTEGER PRIMARY KEY AUTOINCREMENT,
  `descripcion` varchar(255),
  `fecha` date,
  `id_agencia` INTEGER,
  FOREIGN KEY (`id_agencia`) REFERENCES `Agencia` (`id_agencia`)
);

CREATE TABLE `Empresa_Comunicacion` (
  `id_empresa` INTEGER PRIMARY KEY AUTOINCREMENT,
  `nombre` varchar(255),
  `email` varchar(255)
);

CREATE TABLE `Asignacion` (
  `id_evento` INTEGER,
  `id_reportero` INTEGER,
  PRIMARY KEY (`id_evento`, `id_reportero`),
  FOREIGN KEY (`id_evento`) REFERENCES `Evento` (`id_evento`),
  FOREIGN KEY (`id_reportero`) REFERENCES `Reportero` (`id_reportero`)
);

CREATE TABLE `Reportaje` (
  `id_reportaje` INTEGER PRIMARY KEY AUTOINCREMENT,
  `id_evento` INTEGER UNIQUE,
  `id_reportero` INTEGER,
  `titulo` varchar(255) UNIQUE,
  `subtitulo` varchar(255),
  `cuerpo` text,
  `fecha_entrega` datetime,
  FOREIGN KEY (`id_evento`) REFERENCES `Evento` (`id_evento`),
  FOREIGN KEY (`id_reportero`) REFERENCES `Reportero` (`id_reportero`)
);

CREATE TABLE `Version_Reportaje` (
  `id_version` INTEGER PRIMARY KEY AUTOINCREMENT,
  `id_reportaje` INTEGER,
  `fecha_cambio` date,
  `hora_cambio` time,
  `subtitulo_guardado` varchar(255),
  `cuerpo_guardado` text,
  FOREIGN KEY (`id_reportaje`) REFERENCES `Reportaje` (`id_reportaje`)
);

CREATE TABLE `Ofrecimiento` (
  `id_evento` INTEGER,
  `id_empresa` INTEGER,
  `estado` varchar(255),
  `tiene_acceso` boolean DEFAULT false,
  PRIMARY KEY (`id_evento`, `id_empresa`),
  FOREIGN KEY (`id_evento`) REFERENCES `Evento` (`id_evento`),
  FOREIGN KEY (`id_empresa`) REFERENCES `Empresa_Comunicacion` (`id_empresa`)
);