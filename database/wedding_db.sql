-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: wedding_db
-- ------------------------------------------------------
-- Server version	8.4.6

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `booking_services`
--

DROP TABLE IF EXISTS `booking_services`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `booking_services` (
  `booking_id` bigint NOT NULL,
  `service_code` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`booking_id`,`service_code`),
  CONSTRAINT `fk_booking_services_booking` FOREIGN KEY (`booking_id`) REFERENCES `bookings` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `booking_services`
--

LOCK TABLES `booking_services` WRITE;
/*!40000 ALTER TABLE `booking_services` DISABLE KEYS */;
/*!40000 ALTER TABLE `booking_services` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bookings`
--

DROP TABLE IF EXISTS `bookings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bookings` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `bride_name` varchar(150) COLLATE utf8mb4_unicode_ci NOT NULL,
  `groom_name` varchar(150) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(150) COLLATE utf8mb4_unicode_ci NOT NULL,
  `phone` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
  `event_date` date NOT NULL,
  `time_slot` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `hall_id` bigint DEFAULT NULL,
  `guest_count` int DEFAULT NULL,
  `budget_min` decimal(15,2) DEFAULT NULL,
  `budget_max` decimal(15,2) DEFAULT NULL,
  `status` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `notes` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `flag` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_booking_hall` (`hall_id`),
  CONSTRAINT `fk_booking_hall` FOREIGN KEY (`hall_id`) REFERENCES `halls` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bookings`
--

LOCK TABLES `bookings` WRITE;
/*!40000 ALTER TABLE `bookings` DISABLE KEYS */;
/*!40000 ALTER TABLE `bookings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contacts`
--

DROP TABLE IF EXISTS `contacts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `contacts` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `full_name` varchar(150) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(150) COLLATE utf8mb4_unicode_ci NOT NULL,
  `phone` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
  `subject` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `message` varchar(1000) COLLATE utf8mb4_unicode_ci NOT NULL,
  `flag` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `assigned_to` bigint DEFAULT NULL,
  `resolved_at` datetime DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_contact_assignee` (`assigned_to`),
  CONSTRAINT `fk_contact_assignee` FOREIGN KEY (`assigned_to`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contacts`
--

LOCK TABLES `contacts` WRITE;
/*!40000 ALTER TABLE `contacts` DISABLE KEYS */;
/*!40000 ALTER TABLE `contacts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flyway_schema_history`
--

DROP TABLE IF EXISTS `flyway_schema_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `flyway_schema_history` (
  `installed_rank` int NOT NULL,
  `version` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `description` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `type` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `script` varchar(1000) COLLATE utf8mb4_unicode_ci NOT NULL,
  `checksum` int DEFAULT NULL,
  `installed_by` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `installed_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `execution_time` int NOT NULL,
  `success` tinyint(1) NOT NULL,
  PRIMARY KEY (`installed_rank`),
  KEY `flyway_schema_history_s_idx` (`success`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flyway_schema_history`
--

LOCK TABLES `flyway_schema_history` WRITE;
/*!40000 ALTER TABLE `flyway_schema_history` DISABLE KEYS */;
INSERT INTO `flyway_schema_history` VALUES (1,'1','init schema','SQL','V1__init_schema.sql',-1023838736,'root','2025-11-23 08:59:53',47,1),(2,'2','seed data','SQL','V2__seed_data.sql',737142500,'root','2025-11-23 08:59:53',14,1),(3,'3','admin seed','SQL','V3__admin_seed.sql',-1581653789,'root','2025-11-23 08:59:53',7,1);
/*!40000 ALTER TABLE `flyway_schema_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `halls`
--

DROP TABLE IF EXISTS `halls`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `halls` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `name` varchar(150) COLLATE utf8mb4_unicode_ci NOT NULL,
  `capacity` int NOT NULL,
  `base_price` decimal(15,2) NOT NULL,
  `description` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `amenities` json DEFAULT NULL,
  `image_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `display_order` int DEFAULT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `halls`
--

LOCK TABLES `halls` WRITE;
/*!40000 ALTER TABLE `halls` DISABLE KEYS */;
INSERT INTO `halls` VALUES (1,'GRAND','Grand Ballroom',500,150000000.00,'Sảnh lớn sang trọng phù hợp tiệc cưới cao cấp','[\"LED Wall\", \"Premium Sound\", \"Private Lounge\"]','https://example.com/images/halls/grand.jpg',1,1,'2025-11-07 01:29:31',NULL),(2,'GARDEN','Garden View',300,95000000.00,'Sảnh ngoài trời phong cách nhiệt đới','[\"Outdoor Stage\", \"Live Music\", \"Decor Lighting\"]','https://example.com/images/halls/garden.jpg',2,1,'2025-11-07 01:29:31',NULL),(3,'CRYSTAL','Crystal Hall',450,135000000.00,'Sảnh kính cao cấp, phù hợp gala & cưới tối.','[\"LED Wall\", \"Lighting Package\", \"VIP Lounge\"]','https://example.com/images/halls/crystal.jpg',3,1,'2025-11-07 01:29:31',NULL),(4,'RUBY','Ruby Terrace',250,89000000.00,'Không gian sân vườn mở, phong cách rustic.','[\"Outdoor Stage\", \"Canopy\", \"Live Band Area\"]','https://example.com/images/halls/ruby.jpg',4,1,'2025-11-07 01:29:31',NULL);
/*!40000 ALTER TABLE `halls` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `media_albums`
--

DROP TABLE IF EXISTS `media_albums`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `media_albums` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `media_albums`
--

LOCK TABLES `media_albums` WRITE;
/*!40000 ALTER TABLE `media_albums` DISABLE KEYS */;
INSERT INTO `media_albums` VALUES (1,'Elegant Weddings','Khoảnh khắc cưới sang trọng tại Crystal Hall','2025-11-07 01:29:31',NULL),(2,'Garden Moments','Ảnh ngoài trời tại Ruby Terrace','2025-11-07 01:29:31',NULL),(3,'Elegant Weddings','Khoảnh khắc cưới sang trọng tại Crystal Hall','2025-11-07 01:51:26',NULL),(4,'Garden Moments','Ảnh ngoài trời tại Ruby Terrace','2025-11-07 01:51:26',NULL),(5,'Elegant Weddings','Khoảnh khắc cưới sang trọng tại Crystal Hall','2025-11-07 01:51:57',NULL),(6,'Garden Moments','Ảnh ngoài trời tại Ruby Terrace','2025-11-07 01:51:57',NULL),(7,'Elegant Weddings','Khoảnh khắc cưới sang trọng tại Crystal Hall','2025-11-23 15:59:53',NULL),(8,'Garden Moments','Ảnh ngoài trời tại Ruby Terrace','2025-11-23 15:59:53',NULL);
/*!40000 ALTER TABLE `media_albums` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `media_items`
--

DROP TABLE IF EXISTS `media_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `media_items` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `type` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `url` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `thumbnail_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `album_id` bigint DEFAULT NULL,
  `display_order` int DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_media_item_album` (`album_id`),
  CONSTRAINT `fk_media_item_album` FOREIGN KEY (`album_id`) REFERENCES `media_albums` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `media_items`
--

LOCK TABLES `media_items` WRITE;
/*!40000 ALTER TABLE `media_items` DISABLE KEYS */;
INSERT INTO `media_items` VALUES (1,'Crystal Hall Decor','IMAGE','https://example.com/images/albums/crystal-decor.jpg','https://example.com/images/albums/crystal-thumb.jpg',1,1,'2025-11-07 01:29:31',NULL),(2,'Garden Sunset','IMAGE','https://example.com/images/albums/garden-sunset.jpg','https://example.com/images/albums/garden-thumb.jpg',2,1,'2025-11-07 01:29:31',NULL),(3,'Crystal Hall Decor','IMAGE','https://example.com/images/albums/crystal-decor.jpg','https://example.com/images/albums/crystal-thumb.jpg',1,1,'2025-11-07 01:51:26',NULL),(4,'Crystal Hall Decor','IMAGE','https://example.com/images/albums/crystal-decor.jpg','https://example.com/images/albums/crystal-thumb.jpg',3,1,'2025-11-07 01:51:26',NULL),(6,'Garden Sunset','IMAGE','https://example.com/images/albums/garden-sunset.jpg','https://example.com/images/albums/garden-thumb.jpg',2,1,'2025-11-07 01:51:26',NULL),(7,'Garden Sunset','IMAGE','https://example.com/images/albums/garden-sunset.jpg','https://example.com/images/albums/garden-thumb.jpg',4,1,'2025-11-07 01:51:26',NULL),(9,'Crystal Hall Decor','IMAGE','https://example.com/images/albums/crystal-decor.jpg','https://example.com/images/albums/crystal-thumb.jpg',1,1,'2025-11-07 01:51:57',NULL),(10,'Crystal Hall Decor','IMAGE','https://example.com/images/albums/crystal-decor.jpg','https://example.com/images/albums/crystal-thumb.jpg',3,1,'2025-11-07 01:51:57',NULL),(11,'Crystal Hall Decor','IMAGE','https://example.com/images/albums/crystal-decor.jpg','https://example.com/images/albums/crystal-thumb.jpg',5,1,'2025-11-07 01:51:57',NULL),(12,'Garden Sunset','IMAGE','https://example.com/images/albums/garden-sunset.jpg','https://example.com/images/albums/garden-thumb.jpg',2,1,'2025-11-07 01:51:57',NULL),(13,'Garden Sunset','IMAGE','https://example.com/images/albums/garden-sunset.jpg','https://example.com/images/albums/garden-thumb.jpg',4,1,'2025-11-07 01:51:57',NULL),(14,'Garden Sunset','IMAGE','https://example.com/images/albums/garden-sunset.jpg','https://example.com/images/albums/garden-thumb.jpg',6,1,'2025-11-07 01:51:57',NULL),(15,'Crystal Hall Decor','IMAGE','https://example.com/images/albums/crystal-decor.jpg','https://example.com/images/albums/crystal-thumb.jpg',1,1,'2025-11-23 15:59:53',NULL),(16,'Crystal Hall Decor','IMAGE','https://example.com/images/albums/crystal-decor.jpg','https://example.com/images/albums/crystal-thumb.jpg',3,1,'2025-11-23 15:59:53',NULL),(17,'Crystal Hall Decor','IMAGE','https://example.com/images/albums/crystal-decor.jpg','https://example.com/images/albums/crystal-thumb.jpg',5,1,'2025-11-23 15:59:53',NULL),(18,'Crystal Hall Decor','IMAGE','https://example.com/images/albums/crystal-decor.jpg','https://example.com/images/albums/crystal-thumb.jpg',7,1,'2025-11-23 15:59:53',NULL),(22,'Garden Sunset','IMAGE','https://example.com/images/albums/garden-sunset.jpg','https://example.com/images/albums/garden-thumb.jpg',2,1,'2025-11-23 15:59:53',NULL),(23,'Garden Sunset','IMAGE','https://example.com/images/albums/garden-sunset.jpg','https://example.com/images/albums/garden-thumb.jpg',4,1,'2025-11-23 15:59:53',NULL),(24,'Garden Sunset','IMAGE','https://example.com/images/albums/garden-sunset.jpg','https://example.com/images/albums/garden-thumb.jpg',6,1,'2025-11-23 15:59:53',NULL),(25,'Garden Sunset','IMAGE','https://example.com/images/albums/garden-sunset.jpg','https://example.com/images/albums/garden-thumb.jpg',8,1,'2025-11-23 15:59:53',NULL);
/*!40000 ALTER TABLE `media_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `menus`
--

DROP TABLE IF EXISTS `menus`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `menus` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(150) COLLATE utf8mb4_unicode_ci NOT NULL,
  `price` decimal(15,2) NOT NULL,
  `description` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `category` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `image_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `is_featured` tinyint(1) DEFAULT '0',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menus`
--

LOCK TABLES `menus` WRITE;
/*!40000 ALTER TABLE `menus` DISABLE KEYS */;
INSERT INTO `menus` VALUES (1,'Luxury Oriental',3500000.00,'Thực đơn Á cao cấp 10 món','ORIENTAL','https://example.com/images/menus/oriental.jpg',1,'2025-11-07 01:29:31',NULL),(2,'Western Delight',3200000.00,'Thực đơn Âu hiện đại','WESTERN','https://example.com/images/menus/western.jpg',0,'2025-11-07 01:29:31',NULL),(3,'Garden Vegan Delight',2800000.00,'Thực đơn chay cao cấp 8 món','VEGAN','https://example.com/images/menus/vegan.jpg',0,'2025-11-07 01:29:31',NULL),(4,'Premium Fusion',3800000.00,'Kết hợp món Âu - Á hiện đại','FUSION','https://example.com/images/menus/fusion.jpg',1,'2025-11-07 01:29:31',NULL),(5,'Luxury Oriental',3500000.00,'Thực đơn Á cao cấp 10 món','ORIENTAL','https://example.com/images/menus/oriental.jpg',1,'2025-11-07 01:51:26',NULL),(6,'Western Delight',3200000.00,'Thực đơn Âu hiện đại','WESTERN','https://example.com/images/menus/western.jpg',0,'2025-11-07 01:51:26',NULL),(7,'Garden Vegan Delight',2800000.00,'Thực đơn chay cao cấp 8 món','VEGAN','https://example.com/images/menus/vegan.jpg',0,'2025-11-07 01:51:26',NULL),(8,'Premium Fusion',3800000.00,'Kết hợp món Âu - Á hiện đại','FUSION','https://example.com/images/menus/fusion.jpg',1,'2025-11-07 01:51:26',NULL),(9,'Luxury Oriental',3500000.00,'Thực đơn Á cao cấp 10 món','ORIENTAL','https://example.com/images/menus/oriental.jpg',1,'2025-11-07 01:51:57',NULL),(10,'Western Delight',3200000.00,'Thực đơn Âu hiện đại','WESTERN','https://example.com/images/menus/western.jpg',0,'2025-11-07 01:51:57',NULL),(11,'Garden Vegan Delight',2800000.00,'Thực đơn chay cao cấp 8 món','VEGAN','https://example.com/images/menus/vegan.jpg',0,'2025-11-07 01:51:57',NULL),(12,'Premium Fusion',3800000.00,'Kết hợp món Âu - Á hiện đại','FUSION','https://example.com/images/menus/fusion.jpg',1,'2025-11-07 01:51:57',NULL),(13,'Luxury Oriental',3500000.00,'Thực đơn Á cao cấp 10 món','ORIENTAL','https://example.com/images/menus/oriental.jpg',1,'2025-11-23 15:59:53',NULL),(14,'Western Delight',3200000.00,'Thực đơn Âu hiện đại','WESTERN','https://example.com/images/menus/western.jpg',0,'2025-11-23 15:59:53',NULL),(15,'Garden Vegan Delight',2800000.00,'Thực đơn chay cao cấp 8 món','VEGAN','https://example.com/images/menus/vegan.jpg',0,'2025-11-23 15:59:53',NULL),(16,'Premium Fusion',3800000.00,'Kết hợp món Âu - Á hiện đại','FUSION','https://example.com/images/menus/fusion.jpg',1,'2025-11-23 15:59:53',NULL);
/*!40000 ALTER TABLE `menus` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `promotions`
--

DROP TABLE IF EXISTS `promotions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `promotions` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `slug` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` varchar(1000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `start_date` date DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  `terms` varchar(1000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `slug` (`slug`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `promotions`
--

LOCK TABLES `promotions` WRITE;
/*!40000 ALTER TABLE `promotions` DISABLE KEYS */;
INSERT INTO `promotions` VALUES (1,'Early Bird 2025','early-bird-2025','Ưu đãi giảm 15% cho hợp đồng ký trước 31/03/2025','2025-01-01','2025-03-31','Áp dụng cho hợp đồng có giá trị từ 200 triệu','2025-11-07 01:29:31',NULL),(2,'Summer Love','summer-love','Tặng gói trang trí cao cấp cho tiệc cưới tháng 6-8','2025-06-01','2025-08-31','Không áp dụng đồng thời chương trình khác','2025-11-07 01:29:31',NULL),(3,'Luxury Combo 2025','luxury-combo-2025','Ưu đãi combo sảnh Crystal + menu Fusion giảm 10%','2025-04-01','2025-06-30','Áp dụng hoá đơn từ 250 triệu trở lên.','2025-11-07 01:29:31',NULL),(4,'Weekday Special','weekday-special','Giảm 5% cho tiệc cưới tổ chức từ Thứ 2 đến Thứ 5','2025-01-15','2025-12-20','Không áp dụng đồng thời chương trình khác.','2025-11-07 01:29:31',NULL);
/*!40000 ALTER TABLE `promotions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `refresh_tokens`
--

DROP TABLE IF EXISTS `refresh_tokens`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `refresh_tokens` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `token` varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL,
  `expires_at` datetime NOT NULL,
  `revoked` tinyint(1) DEFAULT '0',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `token` (`token`),
  KEY `fk_refresh_tokens_user` (`user_id`),
  CONSTRAINT `fk_refresh_tokens_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `refresh_tokens`
--

LOCK TABLES `refresh_tokens` WRITE;
/*!40000 ALTER TABLE `refresh_tokens` DISABLE KEYS */;
INSERT INTO `refresh_tokens` VALUES (12,3,'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyQHdlZGRpbmcubG9jYWwiLCJpc3MiOiJ3ZWRkaW5nLWJvb2tpbmciLCJpYXQiOjE3NjU2NzY2ODcsImV4cCI6MTc2NjI4MTQ4N30.671QClyVnmDE4IXo1L35kOkAha9THnd7zu6TgF52NdI5bIHBJ5ErDhg5nSoiXfeFXRGZxAjTv0suc34jNh898g','2025-12-21 08:44:47',0,'2025-12-14 08:44:47','2025-12-14 08:44:47'),(17,2,'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbkB3ZWRkaW5nLmxvY2FsIiwiaXNzIjoid2VkZGluZy1ib29raW5nIiwiaWF0IjoxNzY1Njc4Nzg5LCJleHAiOjE3NjYyODM1ODl9.dgfDVL-Er2xC55ehacSn2YHWMLA2wmOC-tmmmvNVXHp1ockhlXzpl_8NKJm1TJq_xrKDulzQ9rf24hgERmt2LA','2025-12-21 09:19:49',0,'2025-12-14 09:19:49','2025-12-14 09:19:49');
/*!40000 ALTER TABLE `refresh_tokens` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (1,'ADMIN','Administrator'),(2,'STAFF','Staff user'),(3,'USER','End user');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_permissions`
--

DROP TABLE IF EXISTS `user_permissions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_permissions` (
  `user_id` bigint NOT NULL,
  `permission` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  KEY `fk_user_permissions_user` (`user_id`),
  CONSTRAINT `fk_user_permissions_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_permissions`
--

LOCK TABLES `user_permissions` WRITE;
/*!40000 ALTER TABLE `user_permissions` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_permissions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_roles`
--

DROP TABLE IF EXISTS `user_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_roles` (
  `user_id` bigint NOT NULL,
  `role_id` bigint NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `fk_user_roles_role` (`role_id`),
  CONSTRAINT `fk_user_roles_role` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`),
  CONSTRAINT `fk_user_roles_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_roles`
--

LOCK TABLES `user_roles` WRITE;
/*!40000 ALTER TABLE `user_roles` DISABLE KEYS */;
INSERT INTO `user_roles` VALUES (1,1),(2,1),(8,1);
/*!40000 ALTER TABLE `user_roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `full_name` varchar(150) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(150) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password_hash` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `primary_role` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (2,'admin','System Administrator','admin@wedding.local','$2a$10$WQhfhEwZ6o3iPqqSa5.hqeWbxx/jez7r4sopHuESy84SqsbaGmQLm','ACTIVE','ADMIN','2025-11-07 02:19:49','2025-11-23 15:59:56'),(3,'user','System user','user@wedding.local','$2a$10$WQhfhEwZ6o3iPqqSa5.hqeWbxx/jez7r4sopHuESy84SqsbaGmQLm','ACTIVE','USER','2025-11-07 02:19:49','2025-11-07 02:36:48');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-12-15 22:25:04
