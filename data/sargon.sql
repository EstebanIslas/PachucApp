-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Versión del servidor:         10.1.40-MariaDB - mariadb.org binary distribution
-- SO del servidor:              Win64
-- HeidiSQL Versión:             10.2.0.5599
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Volcando estructura de base de datos para sargon
CREATE DATABASE IF NOT EXISTS `sargon` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `sargon`;

-- Volcando estructura para tabla sargon.categorias
CREATE TABLE IF NOT EXISTS `categorias` (
  `id_categoria` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) NOT NULL,
  `descripcion` text NOT NULL,
  `logo` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id_categoria`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- Volcando datos para la tabla sargon.categorias: ~8 rows (aproximadamente)
/*!40000 ALTER TABLE `categorias` DISABLE KEYS */;
INSERT INTO `categorias` (`id_categoria`, `nombre`, `descripcion`, `logo`) VALUES
	(1, 'Comercios', 'En esta seccion encontraras las diferentes sucursales y negocios Pachuca encargadas de ofrecer la mercancia basica es decir alimentos productos para el hoga entre otras', 'images_cat/comercio.jpg'),
	(2, 'Entretenimiento', 'Encuentra los lugares mas increibles y divertidos a los que puedes llevar a tus hijos pareja o incluso ir a disfrutar en la capital Hidalguense', 'images_cat/entretenimiento.jpg'),
	(3, 'Escuelas', 'Aqui puedes encontrar el lugar perfecto donde estudiar segun la oferta educativa o grados que se imparten actualmente en Pachuca', 'images_cat/escuelas.jpg'),
	(4, 'Hoteles', 'A la hora de elejir donde alojarse en la capital descubre las opciones que mejor se acoplen a tus necesidades y en el lugar mas conveniente para ti', 'images_cat/hoteles.jpg'),
	(5, 'Otros', 'En Pachuca se encuentra una diversa gama de servicios a disposicion de lo que esta buscado y lo mejor es que cada una de ellas son leales y de confianza', 'images_cat/otros.jpg'),
	(6, 'Profesionales', 'En esta parte encontraras los diferentes servicios profesionales que la capital hidalguense tiene para ti', 'images_cat/profesionales.jpg'),
	(7, 'Restaurantes', 'Los alrededores de Pachuca son conocidos por su deliciosa oferta gastronomica en la que destacan sus alimentos regionales y tradicionales', 'images_cat/restaurantes.jpg'),
	(8, 'Transportes', 'Encuentra las mejores opciones para desplazarte por la ciudad de Pachuca', 'images_cat/transportes.jpg');
/*!40000 ALTER TABLE `categorias` ENABLE KEYS */;

-- Volcando estructura para tabla sargon.clientes
CREATE TABLE IF NOT EXISTS `clientes` (
  `id_cliente` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) NOT NULL,
  `correo` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `rol` varchar(45) NOT NULL DEFAULT 'cliente',
  `ruta_imagen` varchar(70) DEFAULT NULL,
  PRIMARY KEY (`id_cliente`),
  UNIQUE KEY `correo` (`correo`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- Volcando datos para la tabla sargon.clientes: ~3 rows (aproximadamente)
/*!40000 ALTER TABLE `clientes` DISABLE KEYS */;
INSERT INTO `clientes` (`id_cliente`, `nombre`, `correo`, `password`, `rol`, `ruta_imagen`) VALUES
	(1, 'Esteban', 'champagnestephan@gmail.com', '4d186321c1a7f0f354b297e8914ab240', 'cliente', NULL),
	(2, 'Stephan', 'stephanislas@gmail.com', '4d186321c1a7f0f354b297e8914ab240', 'cliente', NULL),
	(5, 'Andrea', 'ejemplo23@gmail.com', '4d186321c1a7f0f354b297e8914ab240', 'cliente', 'disponible');
/*!40000 ALTER TABLE `clientes` ENABLE KEYS */;

-- Volcando estructura para tabla sargon.cliente_codigo
CREATE TABLE IF NOT EXISTS `cliente_codigo` (
  `id_cliente` int(11) NOT NULL,
  `id_codigo` int(11) NOT NULL,
  KEY `id_cliente` (`id_cliente`),
  KEY `id_codigo` (`id_codigo`),
  CONSTRAINT `id_cliente` FOREIGN KEY (`id_cliente`) REFERENCES `clientes` (`id_cliente`),
  CONSTRAINT `id_codigo` FOREIGN KEY (`id_codigo`) REFERENCES `codigos` (`id_codigo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Volcando datos para la tabla sargon.cliente_codigo: ~0 rows (aproximadamente)
/*!40000 ALTER TABLE `cliente_codigo` DISABLE KEYS */;
/*!40000 ALTER TABLE `cliente_codigo` ENABLE KEYS */;

-- Volcando estructura para tabla sargon.codigos
CREATE TABLE IF NOT EXISTS `codigos` (
  `id_codigo` int(11) NOT NULL AUTO_INCREMENT,
  `codigo` varchar(11) NOT NULL,
  `id_promo` int(11) NOT NULL,
  `estado` varchar(35) NOT NULL DEFAULT 'disponible',
  PRIMARY KEY (`id_codigo`),
  KEY `id_promo` (`id_promo`),
  CONSTRAINT `id_promo` FOREIGN KEY (`id_promo`) REFERENCES `promociones` (`id_promo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Volcando datos para la tabla sargon.codigos: ~0 rows (aproximadamente)
/*!40000 ALTER TABLE `codigos` DISABLE KEYS */;
/*!40000 ALTER TABLE `codigos` ENABLE KEYS */;

-- Volcando estructura para tabla sargon.login
CREATE TABLE IF NOT EXISTS `login` (
  `id` int(11) DEFAULT NULL,
  `correo` varchar(50) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  `rol` varchar(25) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Volcando datos para la tabla sargon.login: ~15 rows (aproximadamente)
/*!40000 ALTER TABLE `login` DISABLE KEYS */;
INSERT INTO `login` (`id`, `correo`, `password`, `rol`) VALUES
	(1, 'champagnestephan@gmail.com', '4d186321c1a7f0f354b297e8914ab240', 'cliente'),
	(2, 'stephanislas@gmail.com', '4d186321c1a7f0f354b297e8914ab240', 'cliente'),
	(1, 'tacos@gmail.com', '4d186321c1a7f0f354b297e8914ab240', 'user'),
	(9, 'sanpete@gmail.com', '4d186321c1a7f0f354b297e8914ab240', 'user'),
	(12, 'tuzo@gmail.com', '4d186321c1a7f0f354b297e8914ab240', 'user'),
	(13, 'hola@hola', '4d186321c1a7f0f354b297e8914ab240', 'user'),
	(14, 'ferre@gmail.com', '4d186321c1a7f0f354b297e8914ab240', 'user'),
	(19, 'alitas@gmail.com', '4d186321c1a7f0f354b297e8914ab240', 'user'),
	(22, 'taquito@gmail.com', '4d186321c1a7f0f354b297e8914ab240', 'user'),
	(5, 'ejemplo23@gmail.com', '4d186321c1a7f0f354b297e8914ab240', 'cliente'),
	(23, 'hola@gmail.com', '4d186321c1a7f0f354b297e8914ab240', 'user'),
	(30, 'hola@gmail.com', '4d186321c1a7f0f354b297e8914ab240', 'user'),
	(31, 'avenpizza@gmail.com', '4d186321c1a7f0f354b297e8914ab240', 'user'),
	(35, 'hdkdhsk', '4d186321c1a7f0f354b297e8914ab240', 'user'),
	(40, 'hdkdhsk', '4d186321c1a7f0f354b297e8914ab240', 'user');
/*!40000 ALTER TABLE `login` ENABLE KEYS */;

-- Volcando estructura para tabla sargon.promociones
CREATE TABLE IF NOT EXISTS `promociones` (
  `id_promo` int(11) NOT NULL AUTO_INCREMENT,
  `titulo` varchar(60) NOT NULL,
  `descripcion` text NOT NULL,
  `id` int(11) NOT NULL,
  PRIMARY KEY (`id_promo`),
  KEY `id` (`id`),
  CONSTRAINT `id` FOREIGN KEY (`id`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Volcando datos para la tabla sargon.promociones: ~0 rows (aproximadamente)
/*!40000 ALTER TABLE `promociones` DISABLE KEYS */;
/*!40000 ALTER TABLE `promociones` ENABLE KEYS */;

-- Volcando estructura para tabla sargon.usuarios
CREATE TABLE IF NOT EXISTS `usuarios` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) NOT NULL,
  `correo` varchar(45) NOT NULL,
  `calle` varchar(45) NOT NULL,
  `colonia` varchar(45) NOT NULL,
  `numero` varchar(45) NOT NULL,
  `logo` varchar(45) DEFAULT NULL,
  `maps_url` varchar(45) DEFAULT NULL,
  `descripcion` text NOT NULL,
  `telefono1` varchar(45) NOT NULL,
  `telefono2` varchar(45) DEFAULT NULL,
  `sitio_web` varchar(45) DEFAULT NULL,
  `password` varchar(45) NOT NULL,
  `banner` varchar(45) DEFAULT NULL,
  `rol` varchar(45) NOT NULL DEFAULT 'user',
  `categoria` varchar(45) NOT NULL,
  `municipio` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `correo` (`correo`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8;

-- Volcando datos para la tabla sargon.usuarios: ~9 rows (aproximadamente)
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` (`id`, `nombre`, `correo`, `calle`, `colonia`, `numero`, `logo`, `maps_url`, `descripcion`, `telefono1`, `telefono2`, `sitio_web`, `password`, `banner`, `rol`, `categoria`, `municipio`) VALUES
	(1, 'Tacos', 'tacos@gmail.com', 'mayo', 'Arboledas', '45', 'imageusuarios/Tacos.jpg', NULL, 'Vender', '7757578912', NULL, NULL, '4d186321c1a7f0f354b297e8914ab240', NULL, 'user', 'Restaurantes', 'Tulancingo'),
	(9, 'Hotel', 'sanpete@gmail.com', 'Jalisco', 'Miposke', '45', 'imageusuarios/Hotel.jpg', NULL, 'Duerme', '7757578969', NULL, NULL, '4d186321c1a7f0f354b297e8914ab240', NULL, 'user', 'Hoteles', 'Tula'),
	(12, 'Tuzos', 'tuzo@gmail.com', 'hola', 'hola', '56', 'imageusuarios/Tuzos.jpg', NULL, 'hola', '7757578963', NULL, NULL, '4d186321c1a7f0f354b297e8914ab240', NULL, 'user', 'Transportes', 'hola'),
	(13, 'Bamba', 'hola@hola', 'hola', 'hola', '558', 'imageusuarios/Bamba.jpg', NULL, 'hola', '555', NULL, NULL, '4d186321c1a7f0f354b297e8914ab240', NULL, 'user', 'Hoteles', 'hola'),
	(14, 'Ferreteria', 'ferre@gmail.com', 'hola', 'hola', '56', 'imageusuarios/Ferreteria.jpg', NULL, 'hola', '7757256498', NULL, NULL, '4d186321c1a7f0f354b297e8914ab240', NULL, 'user', 'Hoteles', 'hola'),
	(19, 'Alitas Calientes', 'alitas@gmail.com', 'Kiosco de Arboledas', 'Militar', '75', 'imageusuarios/Alitas.jpg', NULL, 'Venta de Alitas ', '7717020869', NULL, NULL, '4d186321c1a7f0f354b297e8914ab240', NULL, 'user', 'Restaurantes', 'Pachuca de Soto'),
	(22, 'Taquitos', 'taquito@gmail.com', 'Del Valle', 'Privada Marmolejo', '106', 'imageusuarios/Taquitos.jpg', NULL, 'Vendemos taquitos para toda la ocasion bebes', '7717096458', NULL, NULL, '4d186321c1a7f0f354b297e8914ab240', NULL, 'user', 'Restaurantes', 'Pachuca de Soto'),
	(30, 'Criminal', 'hola@gmail.com', '10 de mayo', 'San Isidro', '103', 'imageusuarios/Criminal.jpg', NULL, 'Vendemos todo tipo de comida, Buffet para toda la familia', '7717020765', NULL, NULL, '4d186321c1a7f0f354b297e8914ab240', NULL, 'user', 'Restaurantes', 'Tulancingo de Bravo'),
	(31, 'Avengers Pizza', 'avenpizza@gmail.com', 'Arboledas', '1era de mayo', '75', 'imageusuarios/Avengers Pizza.jpg', NULL, 'Somos una empresa de comida rapida que ofrece los mejores servicios a las y los clientes', '7757976352', NULL, NULL, '4d186321c1a7f0f354b297e8914ab240', NULL, 'user', 'Restaurantes', 'Tulancingo de Bravo');
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;

-- Volcando estructura para disparador sargon.nuevo_cliente
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `nuevo_cliente` AFTER INSERT ON `clientes` FOR EACH ROW INSERT INTO  login (id,correo, password, rol) VALUES
    (new.id_cliente, new.correo ,new.password, new.rol)//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

-- Volcando estructura para disparador sargon.nuevo_usuario
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `nuevo_usuario` AFTER INSERT ON `usuarios` FOR EACH ROW INSERT INTO  login (id,correo, password, rol) VALUES
    (new.id, new.correo ,new.password, new.rol)//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
