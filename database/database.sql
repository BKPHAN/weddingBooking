-- MySQL dump 10.13  Distrib 8.0.38, for Win64 (x86_64)
--
-- Host: localhost    Database: wedding_db
-- ------------------------------------------------------
-- Server version	8.0.39

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
-- Table structure for table `booking`
--

DROP TABLE IF EXISTS `booking`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `booking` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `customer_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL,
  `location` varchar(255) COLLATE utf8mb3_bin DEFAULT NULL,
  `wedding_date` datetime(6) DEFAULT NULL,
  `guest_count` int DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL,
  `phone` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL,
  `create_date` datetime(6) DEFAULT NULL,
  `update_date` datetime(6) DEFAULT NULL,
  `flag` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `booking`
--

LOCK TABLES `booking` WRITE;
/*!40000 ALTER TABLE `booking` DISABLE KEYS */;
INSERT INTO `booking` VALUES (1,'bkk',NULL,NULL,448,'fdfdf@fdsdf','094343434','2024-11-14 14:20:13.467000','2024-11-14 14:20:13.467000','0'),(2,'bkk',NULL,NULL,545,'dfgdfgd@gfgdg','0955323232','2024-11-14 14:26:01.360000','2024-11-14 14:26:01.360000','0'),(3,'bkk',NULL,NULL,444,'dfgdfgd@gfgdg','53453453','2024-11-14 14:28:20.478000','2024-11-14 14:28:20.478000','0'),(4,'bkk',NULL,'2024-11-06 07:00:00.000000',444,'dfgdfgd@gfgdg','0955323232','2024-11-14 14:29:27.315000','2024-11-14 14:29:27.315000','0'),(5,'bkk',NULL,'2024-11-07 07:00:00.000000',555,'dfgdfgd@gfgdg','0955323232','2024-11-14 14:38:04.171000','2024-11-14 14:38:04.171000','0'),(6,'bkk',NULL,'2024-11-06 07:00:00.000000',455,'dfgdfgd@gfgdg','0955323232','2024-11-14 15:26:24.073000','2024-11-14 15:26:24.073000','0');
/*!40000 ALTER TABLE `booking` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contact`
--

DROP TABLE IF EXISTS `contact`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `contact` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(255) COLLATE utf8mb3_bin DEFAULT NULL,
  `email` varchar(255) COLLATE utf8mb3_bin DEFAULT NULL,
  `message` varchar(255) COLLATE utf8mb3_bin DEFAULT NULL,
  `flag` varchar(255) COLLATE utf8mb3_bin DEFAULT NULL,
  `create_date` datetime(6) DEFAULT NULL,
  `update_date` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contact`
--

LOCK TABLES `contact` WRITE;
/*!40000 ALTER TABLE `contact` DISABLE KEYS */;
INSERT INTO `contact` VALUES (1,'BK','fsdfsdfsd@sfdsf','đồ ngon quá','0','2024-11-14 13:19:51.167000','2024-11-14 13:19:51.167000'),(2,'KB','sfsdf@fsfsd','đồ quá ngon','0','2024-11-14 13:19:59.119000','2024-11-14 13:19:59.119000'),(3,'Phan','khjkjh@hfhfgh','quua tuyet voi','0','2024-11-14 13:20:08.334000','2024-11-14 13:20:08.334000'),(4,'phan phan','phan@phan','dịch vụ thật xuất sắc, tôi sẽ đặt vào lần tiếp theo','0','2024-11-14 13:28:47.441000','2024-11-14 13:28:47.441000'),(5,'phan bk','pha@phandfsfs','quá tuyệt vời, hãy phát huy','0','2024-11-14 13:30:45.252000','2024-11-14 13:30:45.252000'),(6,'bkphan','pha@phan','thật tuyệt vời','0','2024-11-14 13:58:02.149000','2024-11-14 13:58:02.149000'),(7,'BKPHAN','phan@gmail.com','dịch vu tuyệt vời','0','2024-11-14 14:36:52.297000','2024-11-14 14:36:52.297000'),(8,'bk','dfgdfgd@gfgdg','thật vui','0','2024-11-14 15:25:36.452000','2024-11-14 15:25:36.452000');
/*!40000 ALTER TABLE `contact` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(255) COLLATE utf8mb3_bin DEFAULT NULL,
  `email` varchar(255) COLLATE utf8mb3_bin DEFAULT NULL,
  `password` varchar(255) COLLATE utf8mb3_bin DEFAULT NULL,
  `create_date` datetime(6) DEFAULT NULL,
  `update_date` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'admin','admin@gmail.com','$2a$10$.CgwfMyZedmwIyD6GmltB.FxuP7XbZZTUmLq6SQmS6dwqcUqmCCA6',NULL,NULL);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-11-18 19:35:38
