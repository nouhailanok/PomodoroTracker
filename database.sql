-- MySQL dump 10.13  Distrib 9.4.0, for macos15.4 (arm64)
--
-- Host: localhost    Database: pomo_tracker_db
-- ------------------------------------------------------
-- Server version	9.4.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `pomodoro_sessions`
--

DROP TABLE IF EXISTS `pomodoro_sessions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pomodoro_sessions` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `duration_in_minutes` int NOT NULL,
  `start_time` datetime(6) NOT NULL,
  `status` enum('COMPLETED','INTERRUPTED','IN_PROGRESS') NOT NULL,
  `task_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKnpvatx1dl04up2vkdpp8g6iif` (`task_id`),
  KEY `FK1gfxjxnkmju1xarm4udaljm0u` (`user_id`),
  CONSTRAINT `FK1gfxjxnkmju1xarm4udaljm0u` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKnpvatx1dl04up2vkdpp8g6iif` FOREIGN KEY (`task_id`) REFERENCES `tasks` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pomodoro_sessions`
--

LOCK TABLES `pomodoro_sessions` WRITE;
/*!40000 ALTER TABLE `pomodoro_sessions` DISABLE KEYS */;
INSERT INTO `pomodoro_sessions` VALUES (1,25,'2025-11-12 19:21:28.515005','COMPLETED',1,1),(2,25,'2025-11-12 19:44:31.869737','COMPLETED',4,1),(3,15,'2025-11-12 23:43:24.664099','INTERRUPTED',2,1),(4,25,'2025-11-13 00:03:38.380770','IN_PROGRESS',4,1),(5,10,'2026-01-08 15:52:05.937759','IN_PROGRESS',6,1),(6,4,'2026-01-08 18:29:58.036180','INTERRUPTED',7,2),(9,0,'2026-01-09 01:48:56.017853','INTERRUPTED',13,3),(10,25,'2026-01-09 02:03:51.030220','COMPLETED',13,3);
/*!40000 ALTER TABLE `pomodoro_sessions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tasks`
--

DROP TABLE IF EXISTS `tasks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tasks` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `completed` bit(1) NOT NULL,
  `estimated_pomodoros` int DEFAULT NULL,
  `planned_end_date` datetime(6) DEFAULT NULL,
  `planned_start_date` datetime(6) DEFAULT NULL,
  `title` varchar(255) NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK6s1ob9k4ihi75xbxe2w0ylsdh` (`user_id`),
  CONSTRAINT `FK6s1ob9k4ihi75xbxe2w0ylsdh` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tasks`
--

LOCK TABLES `tasks` WRITE;
/*!40000 ALTER TABLE `tasks` DISABLE KEYS */;
INSERT INTO `tasks` VALUES (1,_binary '\0',0,NULL,NULL,'Finir le Service API',1),(2,_binary '\0',0,NULL,NULL,'task 2 ',1),(4,_binary '\0',3,'2025-11-13 16:00:00.000000','2025-11-13 08:00:00.000000','Nouveau titre',1),(5,_binary '\0',0,NULL,NULL,'task 5',1),(6,_binary '\0',0,NULL,NULL,'trial101',1),(7,_binary '',0,NULL,NULL,'Ma première tâche après auth',2),(8,_binary '\0',0,NULL,NULL,'Ma première tâche trial 101',2),(9,_binary '\0',0,NULL,NULL,'cfcfcf',2),(10,_binary '\0',0,NULL,NULL,'ydgjzv',2),(12,_binary '\0',0,NULL,NULL,'dde',3),(13,_binary '\0',0,NULL,NULL,'trial1',3);
/*!40000 ALTER TABLE `tasks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `password` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKr43af9ap4edm43mmtq01oddj6` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'luhan','user'),(2,'$2a$10$gvEqvxM0feuQpAKYxkveieU6EXhVTXgEvgCOXxnMqoCQEeuEEshie','testuser'),(3,'$2a$10$lVlOioivHhSiLFILXw6HvuOZnpnbPLLo9b8BYtzVn3dEAgXQRN70.','nouhaila');
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

-- Dump completed on 2026-01-09 14:19:19
