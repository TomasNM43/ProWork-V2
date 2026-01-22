-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 22-01-2026 a las 04:13:28
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `ginnet_prowork`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `configuracion_empresa`
--

CREATE TABLE `configuracion_empresa` (
  `id` bigint(20) NOT NULL,
  `empresa_id` bigint(20) NOT NULL,
  `ruta_archivos` varchar(500) NOT NULL,
  `permite_grabacion_pantalla` tinyint(1) DEFAULT 0,
  `permite_grabacion_audio` tinyint(1) DEFAULT 0,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `configuracion_empresa`
--

INSERT INTO `configuracion_empresa` (`id`, `empresa_id`, `ruta_archivos`, `permite_grabacion_pantalla`, `permite_grabacion_audio`, `fecha_creacion`, `fecha_actualizacion`) VALUES
(1, 1, 'C:\\Users\\user\\Documents\\carpeta', 1, 1, '2025-12-16 21:05:02', '2025-12-21 17:26:25');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `empresa`
--

CREATE TABLE `empresa` (
  `id` bigint(20) NOT NULL,
  `nombre` varchar(255) NOT NULL,
  `rut` varchar(50) DEFAULT NULL,
  `direccion` varchar(255) DEFAULT NULL,
  `telefono` varchar(50) DEFAULT NULL,
  `email` varchar(150) DEFAULT NULL,
  `activa` tinyint(1) NOT NULL DEFAULT 1,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `empresa`
--

INSERT INTO `empresa` (`id`, `nombre`, `rut`, `direccion`, `telefono`, `email`, `activa`, `created_at`, `updated_at`) VALUES
(1, 'Empresa Ejemplo', '12.345.678-9', 'Calle Falsa 123', '+56 9 1234 5678', 'contacto@empresa.cl', 1, '2025-12-16 06:23:53', '2025-12-16 06:23:53');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `evento`
--

CREATE TABLE `evento` (
  `id` bigint(20) NOT NULL,
  `tipo` varchar(100) NOT NULL,
  `descripcion` text DEFAULT NULL,
  `usuario` varchar(100) DEFAULT NULL,
  `empresa_id` bigint(20) DEFAULT NULL,
  `ruta_visualizacion` varchar(500) DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `evento`
--

INSERT INTO `evento` (`id`, `tipo`, `descripcion`, `usuario`, `empresa_id`, `ruta_visualizacion`, `fecha_creacion`) VALUES
(1, 'LOGIN', 'Usuario admin inició sesión', 'admin', NULL, NULL, '2025-12-16 06:58:06'),
(2, 'SUPERVISOR_CREADO', 'Nuevo supervisor registrado: JORGE', 'admin', 1, NULL, '2025-12-16 06:58:32'),
(3, 'LOGIN', 'Usuario JORGE inició sesión', 'JORGE', 1, NULL, '2025-12-16 06:58:44'),
(4, 'LOGIN', 'Usuario JORGE inició sesión', 'JORGE', 1, NULL, '2025-12-16 18:41:56'),
(5, 'LOGIN', 'Usuario JORGE inició sesión', 'JORGE', 1, NULL, '2025-12-16 18:44:38'),
(6, 'LOGIN', 'Usuario JORGE inició sesión', 'JORGE', 1, NULL, '2025-12-16 18:46:03'),
(7, 'LOGIN', 'Usuario admin inició sesión', 'admin', NULL, NULL, '2025-12-16 18:46:13'),
(8, 'LOGIN', 'Usuario admin inició sesión', 'admin', NULL, NULL, '2025-12-16 18:48:36'),
(9, 'LOGIN', 'Usuario admin inició sesión', 'admin', NULL, NULL, '2025-12-16 18:51:00'),
(10, 'LOGIN', 'Usuario admin inició sesión', 'admin', NULL, NULL, '2025-12-16 18:52:26'),
(11, 'LOGIN', 'Usuario JORGE inició sesión', 'JORGE', 1, NULL, '2025-12-16 18:55:35'),
(12, 'LOGIN', 'Usuario JORGE inició sesión', 'JORGE', 1, NULL, '2025-12-16 18:59:07'),
(13, 'EMPLEADO_CREADO', 'Nuevo empleado registrado: JORGE', 'JORGE', 1, NULL, '2025-12-16 18:59:15'),
(14, 'LOGIN', 'Usuario JORGE inició sesión', 'JORGE', 1, NULL, '2025-12-16 21:04:47'),
(15, 'CONFIG_ACTUALIZADA', 'Configuración actualizada - Ruta: ${user.home}/ProWork/documentos, Permisos: pantalla audio', 'JORGE', 1, NULL, '2025-12-16 21:05:02'),
(16, 'LOGIN', 'Usuario JORGE inició sesión', 'JORGE', 1, NULL, '2025-12-16 21:20:34'),
(17, 'EMPLEADO_CREADO', 'Nuevo empleado registrado: Tomas', 'JORGE', 1, NULL, '2025-12-16 21:21:06'),
(18, 'LOGIN', 'Usuario JORGE inició sesión', 'JORGE', 1, NULL, '2025-12-16 21:26:32'),
(19, 'EMPLEADO_CREADO', 'Nuevo empleado registrado: tomas', 'JORGE', 1, NULL, '2025-12-16 21:26:44'),
(20, 'LOGIN', 'Usuario JORGE inició sesión', 'JORGE', 1, NULL, '2025-12-16 21:32:06'),
(21, 'EMPLEADO_CREADO', 'Nuevo empleado registrado: tomas', 'JORGE', 1, NULL, '2025-12-16 21:32:19'),
(22, 'INICIO_GRABACION', 'Usuario tomas inició grabación', 'tomas', 1, NULL, '2025-12-16 21:55:11'),
(23, 'FIN_GRABACION', 'Usuario tomas detuvo grabación. Duración: 3 segundos', 'tomas', 1, NULL, '2025-12-16 21:55:14'),
(24, 'LOGIN', 'Usuario admin inició sesión', 'admin', NULL, NULL, '2025-12-16 22:08:43'),
(25, 'SUPERVISOR_CREADO', 'Nuevo supervisor registrado: manuel', 'admin', 1, NULL, '2025-12-16 22:09:15'),
(26, 'LOGIN', 'Usuario manuel inició sesión', 'manuel', 1, NULL, '2025-12-16 22:09:38'),
(27, 'EMPLEADO_CREADO', 'Nuevo empleado registrado: tomas2', 'manuel', 1, NULL, '2025-12-16 22:10:24'),
(28, 'INICIO_GRABACION', 'Usuario tomas2 inició grabación', 'tomas2', 1, NULL, '2025-12-16 22:10:52'),
(29, 'FIN_GRABACION', 'Usuario tomas2 detuvo grabación. Duración: 14 segundos', 'tomas2', 1, NULL, '2025-12-16 22:11:06'),
(30, 'LOGIN', 'Usuario JORGE inició sesión', 'JORGE', 1, NULL, '2025-12-21 16:28:19'),
(31, 'LOGIN', 'Usuario JORGE inició sesión', 'JORGE', 1, NULL, '2025-12-21 17:07:37'),
(32, 'CONFIG_ACTUALIZADA', 'Configuración actualizada - Ruta: ${user.home}/ProWork/documentos, Permisos: pantalla audio', 'JORGE', 1, NULL, '2025-12-21 17:13:56'),
(33, 'LOGIN', 'Usuario JORGE inició sesión', 'JORGE', 1, NULL, '2025-12-21 17:17:42'),
(34, 'LOGIN', 'Usuario JORGE inició sesión', 'JORGE', 1, NULL, '2025-12-21 17:21:00'),
(35, 'LOGIN', 'Usuario JORGE inició sesión', 'JORGE', 1, NULL, '2025-12-21 17:21:45'),
(36, 'LOGIN', 'Usuario JORGE inició sesión', 'JORGE', 1, NULL, '2025-12-21 17:26:12'),
(37, 'CONFIG_ACTUALIZADA', 'Configuración actualizada - Ruta: C:\\Users\\user\\Documents\\carpeta (carpetas VIDEOS y AUDIOS creadas), Permisos: pantalla audio', 'JORGE', 1, NULL, '2025-12-21 17:26:25'),
(38, 'INICIO_GRABACION', 'Usuario tomas2 inició grabación', 'tomas2', 1, NULL, '2025-12-21 17:33:22'),
(39, 'FIN_GRABACION', 'Usuario tomas2 detuvo grabación. Duración: 9 segundos', 'tomas2', 1, NULL, '2025-12-21 17:33:31'),
(40, 'INICIO_GRABACION', 'Usuario tomas2 inició grabación', 'tomas2', 1, NULL, '2025-12-21 17:40:22'),
(41, 'FIN_GRABACION', 'Usuario tomas2 detuvo grabación. Duración: 6 segundos', 'tomas2', 1, NULL, '2025-12-21 17:40:27'),
(42, 'INICIO_GRABACION', 'Usuario tomas2 inició grabación', 'tomas2', 1, NULL, '2025-12-22 05:43:19'),
(43, 'GUARDADO_VIDEO', 'Video guardado: tomas2_20251222_004319.avi', 'tomas2', 1, 'C:\\Users\\user\\Documents\\carpeta\\VIDEOS\\tomas2_20251222_004319.avi', '2025-12-22 05:43:23'),
(44, 'GUARDADO_AUDIO', 'Audio guardado: tomas2_20251222_004319.wav', 'tomas2', 1, 'C:\\Users\\user\\Documents\\carpeta\\AUDIOS\\tomas2_20251222_004319.wav', '2025-12-22 05:43:23'),
(45, 'FIN_GRABACION', 'Usuario tomas2 detuvo grabación. Duración: 4 segundos', 'tomas2', 1, NULL, '2025-12-22 05:43:23'),
(46, 'LOGIN', 'Usuario JORGE inició sesión', 'JORGE', 1, NULL, '2025-12-22 05:48:05'),
(47, 'LOGIN', 'Usuario JORGE inició sesión', 'JORGE', 1, NULL, '2025-12-22 05:50:25'),
(48, 'LOGIN', 'Usuario JORGE inició sesión', 'JORGE', 1, NULL, '2025-12-22 05:54:17'),
(49, 'LOGIN', 'Usuario JORGE inició sesión', 'JORGE', 1, NULL, '2025-12-22 06:34:19'),
(50, 'LOGIN', 'Usuario JORGE inició sesión', 'JORGE', 1, NULL, '2025-12-22 06:36:53'),
(51, 'LOGIN', 'Usuario JORGE inició sesión', 'JORGE', 1, NULL, '2025-12-22 06:39:15'),
(52, 'LOGIN', 'Usuario JORGE inició sesión', 'JORGE', 1, NULL, '2025-12-22 06:40:56'),
(53, 'INICIO_GRABACION', 'Usuario tomas2 inició grabación', 'tomas2', 1, NULL, '2025-12-22 06:44:40'),
(54, 'GUARDADO_VIDEO', 'Video guardado: tomas2_20251222_014440.mp4', 'tomas2', 1, 'C:\\Users\\user\\Documents\\carpeta\\VIDEOS\\tomas2_20251222_014440.mp4', '2025-12-22 06:44:47'),
(55, 'GUARDADO_AUDIO', 'Audio guardado: tomas2_20251222_014440.wav', 'tomas2', 1, 'C:\\Users\\user\\Documents\\carpeta\\AUDIOS\\tomas2_20251222_014440.wav', '2025-12-22 06:44:47'),
(56, 'FIN_GRABACION', 'Usuario tomas2 detuvo grabación. Duración: 7 segundos', 'tomas2', 1, NULL, '2025-12-22 06:44:47'),
(57, 'LOGIN', 'Usuario JORGE inició sesión', 'JORGE', 1, NULL, '2025-12-22 06:47:16'),
(58, 'LOGIN', 'Usuario JORGE inició sesión', 'JORGE', 1, NULL, '2025-12-22 06:49:02'),
(59, 'LOGIN', 'Usuario JORGE inició sesión', 'JORGE', 1, NULL, '2025-12-22 06:50:22'),
(60, 'LOGIN', 'Usuario JORGE inició sesión', 'JORGE', 1, NULL, '2025-12-22 06:52:51'),
(61, 'LOGIN', 'Usuario JORGE inició sesión', 'JORGE', 1, NULL, '2025-12-22 06:54:42'),
(62, 'INICIO_GRABACION', 'Usuario tomas2 inició grabación', 'tomas2', 1, NULL, '2025-12-22 07:00:31'),
(63, 'GUARDADO_VIDEO', 'Video guardado: tomas2_20251222_020031.mp4', 'tomas2', 1, 'C:\\Users\\user\\Documents\\carpeta\\VIDEOS\\tomas2_20251222_020031.mp4', '2025-12-22 07:00:34'),
(64, 'GUARDADO_AUDIO', 'Audio guardado: tomas2_20251222_020031.wav', 'tomas2', 1, 'C:\\Users\\user\\Documents\\carpeta\\AUDIOS\\tomas2_20251222_020031.wav', '2025-12-22 07:00:34'),
(65, 'FIN_GRABACION', 'Usuario tomas2 detuvo grabación. Duración: 4 segundos', 'tomas2', 1, NULL, '2025-12-22 07:00:34'),
(66, 'INICIO_GRABACION', 'Usuario tomas2 inició grabación', 'tomas2', 1, NULL, '2025-12-22 07:02:28'),
(67, 'GUARDADO_VIDEO', 'Video guardado: tomas2_20251222_020228.mp4', 'tomas2', 1, 'C:\\Users\\user\\Documents\\carpeta\\VIDEOS\\tomas2_20251222_020228.mp4', '2025-12-22 07:02:32'),
(68, 'GUARDADO_AUDIO', 'Audio guardado: tomas2_20251222_020228.wav', 'tomas2', 1, 'C:\\Users\\user\\Documents\\carpeta\\AUDIOS\\tomas2_20251222_020228.wav', '2025-12-22 07:02:32'),
(69, 'FIN_GRABACION', 'Usuario tomas2 detuvo grabación. Duración: 4 segundos', 'tomas2', 1, NULL, '2025-12-22 07:02:32'),
(70, 'LOGIN', 'Usuario JORGE inició sesión', 'JORGE', 1, NULL, '2025-12-22 07:03:28'),
(71, 'LOGIN', 'Usuario JORGE inició sesión', 'JORGE', 1, NULL, '2025-12-22 07:03:54'),
(72, 'INICIO_GRABACION', 'Usuario tomas2 inició grabación', 'tomas2', 1, NULL, '2025-12-22 07:05:23'),
(73, 'GUARDADO_VIDEO', 'Video guardado: tomas2_20251222_020523.mp4', 'tomas2', 1, 'C:\\Users\\user\\Documents\\carpeta\\VIDEOS\\tomas2_20251222_020523.mp4', '2025-12-22 07:05:26'),
(74, 'GUARDADO_AUDIO', 'Audio guardado: tomas2_20251222_020523.wav', 'tomas2', 1, 'C:\\Users\\user\\Documents\\carpeta\\AUDIOS\\tomas2_20251222_020523.wav', '2025-12-22 07:05:26'),
(75, 'FIN_GRABACION', 'Usuario tomas2 detuvo grabación. Duración: 3 segundos', 'tomas2', 1, NULL, '2025-12-22 07:05:26'),
(76, 'INICIO_GRABACION', 'Usuario tomas2 inició grabación', 'tomas2', 1, NULL, '2025-12-22 07:10:15'),
(77, 'GUARDADO_VIDEO', 'Video guardado: tomas2_20251222_021015.mp4', 'tomas2', 1, 'C:\\Users\\user\\Documents\\carpeta\\VIDEOS\\tomas2_20251222_021015.mp4', '2025-12-22 07:10:19'),
(78, 'GUARDADO_AUDIO', 'Audio guardado: tomas2_20251222_021015.wav', 'tomas2', 1, 'C:\\Users\\user\\Documents\\carpeta\\AUDIOS\\tomas2_20251222_021015.wav', '2025-12-22 07:10:19'),
(79, 'FIN_GRABACION', 'Usuario tomas2 detuvo grabación. Duración: 4 segundos', 'tomas2', 1, NULL, '2025-12-22 07:10:19'),
(80, 'INICIO_GRABACION', 'Usuario tomas2 inició grabación', 'tomas2', 1, NULL, '2025-12-22 07:13:32'),
(81, 'GUARDADO_VIDEO', 'Video guardado: tomas2_20251222_021332.mp4', 'tomas2', 1, 'C:\\Users\\user\\Documents\\carpeta\\VIDEOS\\tomas2_20251222_021332.mp4', '2025-12-22 07:13:37'),
(82, 'GUARDADO_AUDIO', 'Audio guardado: tomas2_20251222_021332.wav', 'tomas2', 1, 'C:\\Users\\user\\Documents\\carpeta\\AUDIOS\\tomas2_20251222_021332.wav', '2025-12-22 07:13:37'),
(83, 'FIN_GRABACION', 'Usuario tomas2 detuvo grabación. Duración: 4 segundos', 'tomas2', 1, NULL, '2025-12-22 07:13:37'),
(84, 'LOGIN', 'Usuario JORGE inició sesión', 'JORGE', 1, NULL, '2025-12-22 07:15:43'),
(85, 'INICIO_GRABACION', 'Usuario tomas2 inició grabación', 'tomas2', 1, NULL, '2025-12-22 07:16:46'),
(86, 'GUARDADO_VIDEO', 'Video guardado: tomas2_20251222_021646.mp4', 'tomas2', 1, 'C:\\Users\\user\\Documents\\carpeta\\VIDEOS\\tomas2_20251222_021646.mp4', '2025-12-22 07:16:51'),
(87, 'GUARDADO_AUDIO', 'Audio guardado: tomas2_20251222_021646.wav', 'tomas2', 1, 'C:\\Users\\user\\Documents\\carpeta\\AUDIOS\\tomas2_20251222_021646.wav', '2025-12-22 07:16:51'),
(88, 'FIN_GRABACION', 'Usuario tomas2 detuvo grabación. Duración: 5 segundos', 'tomas2', 1, NULL, '2025-12-22 07:16:51'),
(89, 'INICIO_GRABACION', 'Usuario tomas2 inició grabación', 'tomas2', 1, NULL, '2025-12-22 07:20:40'),
(90, 'GUARDADO_VIDEO', 'Video guardado: tomas2_20251222_022040.mp4', 'tomas2', 1, 'C:\\Users\\user\\Documents\\carpeta\\VIDEOS\\tomas2_20251222_022040.mp4', '2025-12-22 07:20:45'),
(91, 'GUARDADO_AUDIO', 'Audio guardado: tomas2_20251222_022040.wav', 'tomas2', 1, 'C:\\Users\\user\\Documents\\carpeta\\AUDIOS\\tomas2_20251222_022040.wav', '2025-12-22 07:20:45'),
(92, 'FIN_GRABACION', 'Usuario tomas2 detuvo grabación. Duración: 5 segundos', 'tomas2', 1, NULL, '2025-12-22 07:20:45'),
(93, 'INICIO_GRABACION', 'Usuario tomas2 inició grabación', 'tomas2', 1, NULL, '2025-12-22 07:21:38'),
(94, 'GUARDADO_VIDEO', 'Video guardado: tomas2_20251222_022138.mp4', 'tomas2', 1, 'C:\\Users\\user\\Documents\\carpeta\\VIDEOS\\tomas2_20251222_022138.mp4', '2025-12-22 07:21:40'),
(95, 'GUARDADO_AUDIO', 'Audio guardado: tomas2_20251222_022138.wav', 'tomas2', 1, 'C:\\Users\\user\\Documents\\carpeta\\AUDIOS\\tomas2_20251222_022138.wav', '2025-12-22 07:21:40'),
(96, 'FIN_GRABACION', 'Usuario tomas2 detuvo grabación. Duración: 1 segundos', 'tomas2', 1, NULL, '2025-12-22 07:21:40'),
(97, 'INICIO_GRABACION', 'Usuario tomas2 inició grabación', 'tomas2', 1, NULL, '2025-12-22 07:23:35'),
(98, 'GUARDADO_VIDEO', 'Video guardado: tomas2_20251222_022335.mp4', 'tomas2', 1, 'C:\\Users\\user\\Documents\\carpeta\\VIDEOS\\tomas2_20251222_022335.mp4', '2025-12-22 07:23:38'),
(99, 'GUARDADO_AUDIO', 'Audio guardado: tomas2_20251222_022335.wav', 'tomas2', 1, 'C:\\Users\\user\\Documents\\carpeta\\AUDIOS\\tomas2_20251222_022335.wav', '2025-12-22 07:23:38'),
(100, 'FIN_GRABACION', 'Usuario tomas2 detuvo grabación. Duración: 3 segundos', 'tomas2', 1, NULL, '2025-12-22 07:23:38'),
(101, 'LOGIN', 'Usuario JORGE inició sesión', 'JORGE', 1, NULL, '2025-12-22 07:24:07'),
(102, 'LOGIN', 'Usuario JORGE inició sesión', 'JORGE', 1, NULL, '2025-12-24 03:39:23'),
(103, 'INICIO_GRABACION', 'Usuario tomas2 inició grabación', 'tomas2', 1, NULL, '2025-12-24 03:40:04'),
(104, 'GUARDADO_VIDEO', 'Video guardado: tomas2_20251223_224003.mp4', 'tomas2', 1, 'C:\\Users\\user\\Documents\\carpeta\\VIDEOS\\tomas2_20251223_224003.mp4', '2025-12-24 03:40:14'),
(105, 'GUARDADO_AUDIO', 'Audio guardado: tomas2_20251223_224004.wav', 'tomas2', 1, 'C:\\Users\\user\\Documents\\carpeta\\AUDIOS\\tomas2_20251223_224004.wav', '2025-12-24 03:40:15'),
(106, 'FIN_GRABACION', 'Usuario tomas2 detuvo grabación. Duración: 11 segundos', 'tomas2', 1, NULL, '2025-12-24 03:40:15'),
(107, 'INICIO_GRABACION', 'Usuario tomas2 inició grabación', 'tomas2', 1, NULL, '2025-12-24 03:40:15'),
(108, 'GUARDADO_VIDEO', 'Video guardado: tomas2_20251223_224015.mp4', 'tomas2', 1, 'C:\\Users\\user\\Documents\\carpeta\\VIDEOS\\tomas2_20251223_224015.mp4', '2025-12-24 03:40:16'),
(109, 'GUARDADO_AUDIO', 'Audio guardado: tomas2_20251223_224015.wav', 'tomas2', 1, 'C:\\Users\\user\\Documents\\carpeta\\AUDIOS\\tomas2_20251223_224015.wav', '2025-12-24 03:40:16'),
(110, 'FIN_GRABACION', 'Usuario tomas2 detuvo grabación. Duración: 0 segundos', 'tomas2', 1, NULL, '2025-12-24 03:40:16'),
(111, 'LOGIN', 'Usuario JORGE inició sesión', 'JORGE', 1, NULL, '2025-12-30 21:06:07'),
(112, 'LOGIN', 'Usuario JORGE inició sesión', 'JORGE', 1, NULL, '2025-12-30 21:09:54'),
(113, 'LOGIN', 'Usuario JORGE inició sesión', 'JORGE', 1, NULL, '2025-12-30 21:12:22'),
(114, 'INICIO_GRABACION', 'Usuario tomas2 inició grabación', 'tomas2', 1, NULL, '2025-12-30 21:13:06'),
(115, 'GUARDADO_VIDEO', 'Video guardado: tomas2_20251230_161306.mp4', 'tomas2', 1, 'C:\\Users\\user\\Documents\\carpeta\\VIDEOS\\tomas2_20251230_161306.mp4', '2025-12-30 21:13:10'),
(116, 'GUARDADO_AUDIO', 'Audio guardado: tomas2_20251230_161306.wav', 'tomas2', 1, 'C:\\Users\\user\\Documents\\carpeta\\AUDIOS\\tomas2_20251230_161306.wav', '2025-12-30 21:13:10'),
(117, 'FIN_GRABACION', 'Usuario tomas2 detuvo grabación. Duración: 4 segundos', 'tomas2', 1, NULL, '2025-12-30 21:13:10'),
(118, 'LOGIN', 'Usuario JORGE inició sesión', 'JORGE', 1, NULL, '2025-12-31 02:03:23'),
(119, 'INICIO_GRABACION', 'Usuario tomas2 inició grabación', 'tomas2', 1, NULL, '2025-12-31 02:04:24'),
(120, 'GUARDADO_VIDEO', 'Video guardado: tomas2_20251230_210424.mp4', 'tomas2', 1, 'C:\\Users\\user\\Documents\\carpeta\\VIDEOS\\tomas2_20251230_210424.mp4', '2025-12-31 02:04:33'),
(121, 'GUARDADO_AUDIO', 'Audio guardado: tomas2_20251230_210424.wav', 'tomas2', 1, 'C:\\Users\\user\\Documents\\carpeta\\AUDIOS\\tomas2_20251230_210424.wav', '2025-12-31 02:04:33'),
(122, 'FIN_GRABACION', 'Usuario tomas2 detuvo grabación. Duración: 9 segundos', 'tomas2', 1, NULL, '2025-12-31 02:04:33'),
(123, 'LOGIN', 'Usuario JORGE inició sesión', 'JORGE', 1, NULL, '2025-12-31 02:23:25'),
(124, 'LOGIN', 'Usuario JORGE inició sesión', 'JORGE', 1, NULL, '2026-01-18 15:33:12');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ginnet_prowork`
--
-- Error leyendo la estructura de la tabla ginnet_prowork.ginnet_prowork: #1932 - Table &#039;ginnet_prowork.ginnet_prowork&#039; doesn&#039;t exist in engine
-- Error leyendo datos de la tabla ginnet_prowork.ginnet_prowork: #1064 - Algo está equivocado en su sintax cerca &#039;FROM `ginnet_prowork`.`ginnet_prowork`&#039; en la linea 1

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `supervisor`
--

CREATE TABLE `supervisor` (
  `id` bigint(20) NOT NULL,
  `usuario_id` bigint(20) NOT NULL,
  `empresa_id` bigint(20) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario`
--

CREATE TABLE `usuario` (
  `id` bigint(20) NOT NULL,
  `username` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(150) DEFAULT NULL,
  `nombre` varchar(100) DEFAULT NULL,
  `apellido` varchar(100) DEFAULT NULL,
  `rol` enum('ADMINISTRADOR','SUPERVISOR','EMPLEADO') NOT NULL,
  `empresa_id` bigint(20) DEFAULT NULL,
  `activo` tinyint(1) NOT NULL DEFAULT 1,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `usuario`
--

INSERT INTO `usuario` (`id`, `username`, `password`, `email`, `nombre`, `apellido`, `rol`, `empresa_id`, `activo`, `created_at`, `updated_at`) VALUES
(3, 'admin', '$2b$10$YliDUrJZLkT/eSCZXazOaOeLp7z8QEVpOvSwm0MEO0tQ4rbXlb6B2', 'admin@example.com', 'Administrador', 'Default', 'ADMINISTRADOR', NULL, 1, '2025-12-16 06:06:58', '2025-12-16 06:06:58'),
(5, 'JORGE', '$2a$10$2aS0iiPEkOVgk0o3DuoaQuBedSSQhZQa1maYgTV23p38A3r7DGO4W', 'vbarrientos@ginnet.com.pe', 'TomÃ¡s', 'Melo', 'SUPERVISOR', 1, 1, '2025-12-16 06:58:32', '2025-12-16 06:58:32'),
(6, 'tomas', '$2a$10$rUSIbAORjmrCDs5iqwCQV..z.PQZKcx.xI.hWhLHbqw9GLrC7Jct2', 'tomasninan2@gmail.com', 'TomÃ¡s NinÃ¡n Melo', '', 'EMPLEADO', 1, 1, '2025-12-16 21:32:19', '2025-12-16 21:32:19'),
(7, 'manuel', '$2a$10$zSnLRGwd.OOE32szrRkbl.6oCgaT/u3epBeaa88vxFlcurno5ayYW', 'tomasninan2@gmail.com', 'Manuel', 'Rojas', 'SUPERVISOR', 1, 1, '2025-12-16 22:09:15', '2025-12-16 22:09:15'),
(8, 'tomas2', '$2a$10$/XCpxi7i0Uko.3tsT5.42OYTHAQW/zfV4rLFBtjQyfdpqSPwTs5Xy', 'tomasninan2@gmail.com', 'TomÃ¡s NinÃ¡n Melo', '', 'EMPLEADO', 1, 1, '2025-12-16 22:10:24', '2025-12-16 22:10:24');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `configuracion_empresa`
--
ALTER TABLE `configuracion_empresa`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `unique_empresa_config` (`empresa_id`),
  ADD KEY `idx_config_empresa_id` (`empresa_id`);

--
-- Indices de la tabla `empresa`
--
ALTER TABLE `empresa`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `evento`
--
ALTER TABLE `evento`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_eventos_empresa` (`empresa_id`);

--
-- Indices de la tabla `supervisor`
--
ALTER TABLE `supervisor`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `ux_supervisor_usuario` (`usuario_id`),
  ADD KEY `fk_supervisor_empresa` (`empresa_id`);

--
-- Indices de la tabla `usuario`
--
ALTER TABLE `usuario`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`),
  ADD KEY `fk_usuarios_empresa` (`empresa_id`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `configuracion_empresa`
--
ALTER TABLE `configuracion_empresa`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT de la tabla `empresa`
--
ALTER TABLE `empresa`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT de la tabla `evento`
--
ALTER TABLE `evento`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=125;

--
-- AUTO_INCREMENT de la tabla `supervisor`
--
ALTER TABLE `supervisor`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `usuario`
--
ALTER TABLE `usuario`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `configuracion_empresa`
--
ALTER TABLE `configuracion_empresa`
  ADD CONSTRAINT `fk_config_empresa` FOREIGN KEY (`empresa_id`) REFERENCES `empresa` (`id`) ON DELETE CASCADE;

--
-- Filtros para la tabla `evento`
--
ALTER TABLE `evento`
  ADD CONSTRAINT `fk_eventos_empresa` FOREIGN KEY (`empresa_id`) REFERENCES `empresa` (`id`) ON DELETE CASCADE;

--
-- Filtros para la tabla `supervisor`
--
ALTER TABLE `supervisor`
  ADD CONSTRAINT `fk_supervisor_empresa` FOREIGN KEY (`empresa_id`) REFERENCES `empresa` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_supervisor_usuario` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`) ON DELETE CASCADE;

--
-- Filtros para la tabla `usuario`
--
ALTER TABLE `usuario`
  ADD CONSTRAINT `fk_usuarios_empresa` FOREIGN KEY (`empresa_id`) REFERENCES `empresa` (`id`) ON DELETE SET NULL ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
