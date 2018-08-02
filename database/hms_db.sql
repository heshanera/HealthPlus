-- MySQL dump 10.16  Distrib 10.1.34-MariaDB, for Linux (x86_64)
--
-- Host: localhost    Database: test_HMS2
-- ------------------------------------------------------
-- Server version	10.1.34-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `BloodGroupingRh`
--

DROP TABLE IF EXISTS `BloodGroupingRh`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `BloodGroupingRh` (
  `tst_bloodG_id` varchar(10) NOT NULL DEFAULT '',
  `prescription_id` varchar(10) DEFAULT NULL,
  `bloodGroup` varchar(10) DEFAULT NULL,
  `rhesusD` varchar(10) DEFAULT NULL,
  `appointment_id` varchar(15) DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  PRIMARY KEY (`tst_bloodG_id`),
  KEY `prescription_id` (`prescription_id`),
  CONSTRAINT `BloodGroupingRh_ibfk_1` FOREIGN KEY (`prescription_id`) REFERENCES `prescription` (`prescription_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `BloodGroupingRh`
--

LOCK TABLES `BloodGroupingRh` WRITE;
/*!40000 ALTER TABLE `BloodGroupingRh` DISABLE KEYS */;
INSERT INTO `BloodGroupingRh` VALUES ('BG0001','pr0003','A','positive',NULL,'2016-12-09 23:41:02'),('BG0002','pr0004','B','negative',NULL,'2016-12-09 23:41:03'),('bg0003',NULL,' a ',' a ','  ','2016-12-09 23:41:04'),('bg0004',NULL,' a ',' a ','  ','2016-12-09 23:41:05'),('bg0005',NULL,' A ',' A ','  ','2016-12-09 23:41:05'),('bg0006',NULL,' A+ ',' positive ',' lapp001 ','2016-12-09 23:41:06');
/*!40000 ALTER TABLE `BloodGroupingRh` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `LipidTest`
--

DROP TABLE IF EXISTS `LipidTest`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `LipidTest` (
  `tst_li_id` varchar(10) NOT NULL DEFAULT '',
  `prescription_id` varchar(10) DEFAULT NULL,
  `cholestrolHDL` varchar(10) DEFAULT NULL,
  `cholestrolLDL` varchar(10) DEFAULT NULL,
  `triglycerides` varchar(10) DEFAULT NULL,
  `totalCholestrolLDLHDLratio` varchar(10) DEFAULT NULL,
  `appointment_id` varchar(15) DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  PRIMARY KEY (`tst_li_id`),
  KEY `prescription_id` (`prescription_id`),
  CONSTRAINT `LipidTest_ibfk_1` FOREIGN KEY (`prescription_id`) REFERENCES `prescription` (`prescription_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `LipidTest`
--

LOCK TABLES `LipidTest` WRITE;
/*!40000 ALTER TABLE `LipidTest` DISABLE KEYS */;
INSERT INTO `LipidTest` VALUES ('LI0001','pr0001','56','45','89','5',NULL,'2016-12-09 23:40:40'),('LI0002','pr0002','89','12','23','6',NULL,'2016-12-09 23:40:41'),('li0003',NULL,' a ',' a ',' a ',' a ','  ','2016-12-09 23:40:42'),('li0004',NULL,' a ',' a ',' a ',' a ','  ','2016-12-09 23:40:43'),('li0005',NULL,' a ',' a ',' a ',' a ',' lapp001 ','2016-12-09 23:40:43'),('li0006',NULL,' a ',' a ',' a ',' a ',' lapp001 ','2016-12-09 23:40:44'),('li0007',NULL,' a ',' a ',' a ',' a ',' lapp001 ','2016-12-09 23:40:45'),('li0008',NULL,' a ',' a ',' a ',' a ',' lapp001 ','2016-12-09 23:40:46'),('li0009',NULL,' a ',' a ',' a ',' a ','  ','2016-12-09 23:40:47');
/*!40000 ALTER TABLE `LipidTest` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `LiverFunctionTest`
--

DROP TABLE IF EXISTS `LiverFunctionTest`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `LiverFunctionTest` (
  `tst_liver_id` varchar(10) NOT NULL DEFAULT '',
  `prescription_id` varchar(10) DEFAULT NULL,
  `totalProtein` float DEFAULT NULL,
  `albumin` float DEFAULT NULL,
  `globulin` float DEFAULT NULL,
  `totalBilirubin` float DEFAULT NULL,
  `directBilirubin` float DEFAULT NULL,
  `sgotast` float DEFAULT NULL,
  `sgptalt` float DEFAULT NULL,
  `alkalinePhospates` float DEFAULT NULL,
  `appointment_id` varchar(15) DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  PRIMARY KEY (`tst_liver_id`),
  KEY `prescription_id` (`prescription_id`),
  CONSTRAINT `LiverFunctionTest_ibfk_1` FOREIGN KEY (`prescription_id`) REFERENCES `prescription` (`prescription_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `LiverFunctionTest`
--

LOCK TABLES `LiverFunctionTest` WRITE;
/*!40000 ALTER TABLE `LiverFunctionTest` DISABLE KEYS */;
INSERT INTO `LiverFunctionTest` VALUES ('LV0001','pr0007',56,5,89,95,65,23,35,56,NULL,'2016-12-09 23:40:27'),('LV0002','pr0008',56,5,89,95,65,23,35,22,NULL,'2016-12-09 23:40:28'),('lv0003',NULL,1,2,3,4,5,6,7,8,'  ','2016-12-09 23:40:29');
/*!40000 ALTER TABLE `LiverFunctionTest` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `RenalFunctionTest`
--

DROP TABLE IF EXISTS `RenalFunctionTest`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `RenalFunctionTest` (
  `tst_renal_id` varchar(10) NOT NULL DEFAULT '',
  `prescription_id` varchar(10) DEFAULT NULL,
  `creatinine` float DEFAULT NULL,
  `urea` float DEFAULT NULL,
  `totalBilirubin` float DEFAULT NULL,
  `directBilirubin` float DEFAULT NULL,
  `sgotast` float DEFAULT NULL,
  `sgptalt` float DEFAULT NULL,
  `alkalinePhospates` float DEFAULT NULL,
  `appointment_id` varchar(15) DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  PRIMARY KEY (`tst_renal_id`),
  KEY `prescription_id` (`prescription_id`),
  CONSTRAINT `RenalFunctionTest_ibfk_1` FOREIGN KEY (`prescription_id`) REFERENCES `prescription` (`prescription_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `RenalFunctionTest`
--

LOCK TABLES `RenalFunctionTest` WRITE;
/*!40000 ALTER TABLE `RenalFunctionTest` DISABLE KEYS */;
INSERT INTO `RenalFunctionTest` VALUES ('RE0001','pr0009',56,5,89,95,65,23,35,NULL,'2016-12-09 23:40:10'),('RE0002','pr0010',56,5,89,95,65,23,35,NULL,'2016-12-09 23:40:11'),('re0003',NULL,0,0,0,0,0,0,0,'  ','2016-12-09 23:40:12');
/*!40000 ALTER TABLE `RenalFunctionTest` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SeriumCreatinePhosphokinase`
--

DROP TABLE IF EXISTS `SeriumCreatinePhosphokinase`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SeriumCreatinePhosphokinase` (
  `tst_SCP_id` varchar(10) NOT NULL DEFAULT '',
  `prescription_id` varchar(10) DEFAULT NULL,
  `hiv12ELISA` varchar(10) DEFAULT NULL,
  `appointment_id` varchar(15) DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  PRIMARY KEY (`tst_SCP_id`),
  KEY `prescription_id` (`prescription_id`),
  CONSTRAINT `SeriumCreatinePhosphokinase_ibfk_1` FOREIGN KEY (`prescription_id`) REFERENCES `prescription` (`prescription_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SeriumCreatinePhosphokinase`
--

LOCK TABLES `SeriumCreatinePhosphokinase` WRITE;
/*!40000 ALTER TABLE `SeriumCreatinePhosphokinase` DISABLE KEYS */;
INSERT INTO `SeriumCreatinePhosphokinase` VALUES ('SCP0001','pr0011','78',NULL,'2016-05-09 20:41:27'),('SCP0002','pr0012','25',NULL,'2016-05-09 20:41:27');
/*!40000 ALTER TABLE `SeriumCreatinePhosphokinase` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SeriumCreatinePhosphokinaseTotal`
--

DROP TABLE IF EXISTS `SeriumCreatinePhosphokinaseTotal`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SeriumCreatinePhosphokinaseTotal` (
  `tst_SCPT_id` varchar(10) NOT NULL DEFAULT '',
  `test_id` varchar(10) DEFAULT NULL,
  `prescription_id` varchar(10) DEFAULT NULL,
  `cpkTotal` int(10) DEFAULT NULL,
  `appointment_id` varchar(15) DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  PRIMARY KEY (`tst_SCPT_id`),
  KEY `test_id` (`test_id`),
  KEY `prescription_id` (`prescription_id`),
  CONSTRAINT `SeriumCreatinePhosphokinaseTotal_ibfk_1` FOREIGN KEY (`test_id`) REFERENCES `lab_test` (`test_id`),
  CONSTRAINT `SeriumCreatinePhosphokinaseTotal_ibfk_2` FOREIGN KEY (`prescription_id`) REFERENCES `prescription` (`prescription_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SeriumCreatinePhosphokinaseTotal`
--

LOCK TABLES `SeriumCreatinePhosphokinaseTotal` WRITE;
/*!40000 ALTER TABLE `SeriumCreatinePhosphokinaseTotal` DISABLE KEYS */;
INSERT INTO `SeriumCreatinePhosphokinaseTotal` VALUES ('scpt0001',NULL,NULL,0,'lapp001','2016-05-09 20:41:27'),('scpt0002',NULL,NULL,54,' lapp001 ','2016-05-09 20:41:27');
/*!40000 ALTER TABLE `SeriumCreatinePhosphokinaseTotal` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `UrineFullReport`
--

DROP TABLE IF EXISTS `UrineFullReport`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `UrineFullReport` (
  `tst_ur_id` varchar(10) NOT NULL DEFAULT '',
  `prescription_id` varchar(10) DEFAULT NULL,
  `appearance` varchar(10) DEFAULT NULL,
  `sgRefractometer` varchar(10) DEFAULT NULL,
  `ph` float DEFAULT NULL,
  `protein` varchar(10) DEFAULT NULL,
  `glucose` varchar(10) DEFAULT NULL,
  `ketoneBodies` varchar(10) DEFAULT NULL,
  `bilirubin` varchar(10) DEFAULT NULL,
  `urobilirubin` varchar(10) DEFAULT NULL,
  `contrifugedDepositsphaseContrastMicroscopy` varchar(10) DEFAULT NULL,
  `pusCells` varchar(10) DEFAULT NULL,
  `redCells` varchar(10) DEFAULT NULL,
  `epithelialCells` varchar(10) DEFAULT NULL,
  `casts` varchar(10) DEFAULT NULL,
  `cristals` varchar(10) DEFAULT NULL,
  `appointment_id` varchar(15) DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  PRIMARY KEY (`tst_ur_id`),
  KEY `prescription_id` (`prescription_id`),
  CONSTRAINT `UrineFullReport_ibfk_1` FOREIGN KEY (`prescription_id`) REFERENCES `prescription` (`prescription_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `UrineFullReport`
--

LOCK TABLES `UrineFullReport` WRITE;
/*!40000 ALTER TABLE `UrineFullReport` DISABLE KEYS */;
INSERT INTO `UrineFullReport` VALUES ('ur0001',NULL,' as ',' sa ',0,' as ',' asa ',' ass ',' assa ',' sa ',' asa ',' asas ',' asa ',' asa ',' asa ',' asa ','  ','2016-12-09 23:41:41'),('ur0002',NULL,' a ',' b ',0,' d ',' e ',' g ',' h ',' i ',' j ',' k ',' l ',' m ',' o ',' n ','  ','2016-12-09 23:41:42');
/*!40000 ALTER TABLE `UrineFullReport` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `appointment`
--

DROP TABLE IF EXISTS `appointment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `appointment` (
  `appointment_id` varchar(15) NOT NULL DEFAULT '',
  `date` datetime DEFAULT NULL,
  `info` varchar(100) DEFAULT NULL,
  `patient_id` varchar(10) DEFAULT NULL,
  `bill_id` varchar(10) DEFAULT NULL,
  `slmc_reg_no` varchar(20) DEFAULT NULL,
  `cancelled` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`appointment_id`),
  KEY `patient_id` (`patient_id`),
  KEY `slmc_reg_no` (`slmc_reg_no`),
  KEY `bill_id` (`bill_id`),
  CONSTRAINT `appointment_ibfk_1` FOREIGN KEY (`patient_id`) REFERENCES `patient` (`patient_id`),
  CONSTRAINT `appointment_ibfk_2` FOREIGN KEY (`slmc_reg_no`) REFERENCES `doctor` (`slmc_reg_no`),
  CONSTRAINT `appointment_ibfk_3` FOREIGN KEY (`bill_id`) REFERENCES `bill` (`bill_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `appointment`
--

LOCK TABLES `appointment` WRITE;
/*!40000 ALTER TABLE `appointment` DISABLE KEYS */;
INSERT INTO `appointment` VALUES ('app001','2016-07-08 10:30:00','info','hms0001pa','hms0001b','15682',1),('app002','2016-07-08 11:30:00','info','hms0002pa','hms0002b','16787',1),('app003','2016-12-06 16:00:00',NULL,'hms0001pa','hms0003b','15682',1),('app004','2017-01-08 16:00:00',NULL,'hms0001pa','hms0004b','22387',0),('app005','2017-01-08 15:00:00',NULL,'hms0001pa',NULL,'21987',0),('app006','2017-01-10 16:00:00',NULL,'hms0001pa',NULL,'15682',0),('app007','2017-01-09 16:00:00',NULL,'hms0001pa',NULL,'16787',0),('app008','2017-01-10 16:00:00',NULL,'hms0001pa',NULL,'15682',0),('app009','2015-01-10 16:00:00',NULL,'hms0001pa',NULL,'15682',0);
/*!40000 ALTER TABLE `appointment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bill`
--

DROP TABLE IF EXISTS `bill`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bill` (
  `bill_id` varchar(10) NOT NULL DEFAULT '',
  `bill_date` datetime DEFAULT NULL,
  `doctor_fee` int(11) DEFAULT NULL,
  `hospital_fee` int(11) DEFAULT NULL,
  `pharmacy_fee` int(11) DEFAULT NULL,
  `laboratory_fee` int(11) DEFAULT NULL,
  `appointment_fee` int(11) DEFAULT NULL,
  `vat` int(11) DEFAULT NULL,
  `discount` int(11) DEFAULT NULL,
  `total` int(11) DEFAULT NULL,
  `payment_method` varchar(25) DEFAULT NULL,
  `consultant_id` varchar(10) DEFAULT NULL,
  `patient_id` varchar(10) DEFAULT NULL,
  `refund` int(11) DEFAULT NULL,
  PRIMARY KEY (`bill_id`),
  KEY `patient_id` (`patient_id`),
  KEY `consultant_id` (`consultant_id`),
  CONSTRAINT `bill_ibfk_1` FOREIGN KEY (`patient_id`) REFERENCES `patient` (`patient_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bill`
--

LOCK TABLES `bill` WRITE;
/*!40000 ALTER TABLE `bill` DISABLE KEYS */;
INSERT INTO `bill` VALUES ('hms0001b','2016-12-30 14:30:00',0,0,0,0,500,0,0,500,'online','19245','hms0001pa',1),('hms0002b','2017-01-02 10:30:00',0,0,0,0,500,0,0,500,'cash','19245','hms0002pa',0),('hms0003b','2017-01-05 13:30:00',0,0,0,0,500,0,0,500,'online','18452','hms0003pa',0),('hms0004b','2017-01-10 14:41:45',200,150,450,0,0,40,NULL,840,'Cash','22387','hms0001pa',NULL),('hms0005b','2017-01-10 22:31:18',0,0,0,0,500,25,NULL,525,'Cash','null','hms0001pa',NULL),('hms0006b','2017-01-10 22:32:43',200,150,0,1000,0,65,NULL,1415,'Cash','null','hms0001pa',NULL),('hms0007b','2016-08-30 14:30:00',200,150,300,0,500,60,0,1210,'cash','18452','hms0001pa',0);
/*!40000 ALTER TABLE `bill` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `completeBloodCount`
--

DROP TABLE IF EXISTS `completeBloodCount`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `completeBloodCount` (
  `tst_CBC_id` varchar(10) NOT NULL DEFAULT '',
  `prescription_id` varchar(10) DEFAULT NULL,
  `totalWhiteCellCount` int(10) DEFAULT NULL,
  `differentialCount` int(10) DEFAULT NULL,
  `neutrophils` int(10) DEFAULT NULL,
  `lymphocytes` int(10) DEFAULT NULL,
  `monocytes` int(10) DEFAULT NULL,
  `eosonophils` int(10) DEFAULT NULL,
  `basophils` int(10) DEFAULT NULL,
  `haemoglobin` float DEFAULT NULL,
  `redBloodCells` float DEFAULT NULL,
  `meanCellVolume` float DEFAULT NULL,
  `haematocrit` float DEFAULT NULL,
  `meanCellHaemoglobin` float DEFAULT NULL,
  `mchConcentration` float DEFAULT NULL,
  `redCellsDistributionWidth` float DEFAULT NULL,
  `plateletCount` int(15) DEFAULT NULL,
  `appointment_id` varchar(15) DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  PRIMARY KEY (`tst_CBC_id`),
  KEY `prescription_id` (`prescription_id`),
  CONSTRAINT `completeBloodCount_ibfk_1` FOREIGN KEY (`prescription_id`) REFERENCES `prescription` (`prescription_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `completeBloodCount`
--

LOCK TABLES `completeBloodCount` WRITE;
/*!40000 ALTER TABLE `completeBloodCount` DISABLE KEYS */;
INSERT INTO `completeBloodCount` VALUES ('CBC0001','pr0005',0,56,5,89,95,65,23,35,87,100,500,36,56,48,500,NULL,'2016-05-09 20:41:27'),('CBC0002','pr0006',0,56,5,89,95,65,23,35,87,100,500,36,56,53,600,NULL,'2016-05-09 20:41:27'),('cbc0003',NULL,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,'  ','2016-05-09 20:41:27'),('cbc0004',NULL,23,34,0,10,23,12,12,12,12,12,12,12,12,12,12,'  ','2016-05-09 20:41:27');
/*!40000 ALTER TABLE `completeBloodCount` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `diagnose_history`
--

DROP TABLE IF EXISTS `diagnose_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `diagnose_history` (
  `diagnostic_id` varchar(10) NOT NULL DEFAULT '',
  `patient_id` varchar(10) DEFAULT NULL,
  `diagnose` varchar(150) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `consultant_id` varchar(10) DEFAULT NULL,
  `prescription_id` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`diagnostic_id`),
  KEY `patient_id` (`patient_id`),
  KEY `consultant_id` (`consultant_id`),
  KEY `prescription_id` (`prescription_id`),
  CONSTRAINT `diagnose_history_ibfk_1` FOREIGN KEY (`patient_id`) REFERENCES `patient` (`patient_id`),
  CONSTRAINT `diagnose_history_ibfk_2` FOREIGN KEY (`consultant_id`) REFERENCES `doctor` (`slmc_reg_no`),
  CONSTRAINT `diagnose_history_ibfk_3` FOREIGN KEY (`prescription_id`) REFERENCES `prescription` (`prescription_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `diagnose_history`
--

LOCK TABLES `diagnose_history` WRITE;
/*!40000 ALTER TABLE `diagnose_history` DISABLE KEYS */;
/*!40000 ALTER TABLE `diagnose_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `doctor`
--

DROP TABLE IF EXISTS `doctor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `doctor` (
  `slmc_reg_no` varchar(20) NOT NULL DEFAULT '',
  `user_id` varchar(10) DEFAULT NULL,
  `education` varchar(100) DEFAULT NULL,
  `training` varchar(100) DEFAULT NULL,
  `experienced_areas` varchar(100) DEFAULT NULL,
  `experience` varchar(100) DEFAULT NULL,
  `achievements` varchar(100) DEFAULT NULL,
  `channelling_fee` int(11) DEFAULT NULL,
  PRIMARY KEY (`slmc_reg_no`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `doctor_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `doctor`
--

LOCK TABLES `doctor` WRITE;
/*!40000 ALTER TABLE `doctor` DISABLE KEYS */;
INSERT INTO `doctor` VALUES ('15682','hms0005u','MBBS SriLanka(Colombo) MD Australia','intern at general hospital Badulla','physiology','13 years','8 years of experirnce in consulting',800),('16787','hms0009u','MBBS SriLanka(Karapitiya) MD Australia','intern at general hospital Badulla','paediatrics','12 years','7 years of experirnce in consulting',500),('18452','hms0003u','MBBS SriLanka(Karapitiya) MD China','intern at general hospital Badulla','neurology','10 years','4 years of experirnce in consulting',1000),('19245','hms0002u','MBBS SriLanka(Colombo) MD China','intern at general hospital Colombo','cardiology','9 years','3 years of experience in consulting',700),('19993','hms0010u','MBBS SriLanka(Colombo) MD USA','intern at general hospital Matara','endocrineology','9 years','4 years of experirnce in consulting',650),('21462','hms0004u','MBBS SriLanka(Peradeniya) MD USA','intern at general hospital Kandy','neurology','7 years','2 years of experirnce in consulting',900),('21987','hms0006u','MBBS SriLanka(Colombo) MD USA','intern at general hospital Maharagama','physiology','7 years','2 years of experirnce in consulting',700),('22287','hms0008u','MBBS SriLanka(Colombo) MD China','intern at general hospital Colombo','orthopedics','8 years','3 years of experirnce in consulting',600),('22387','hms0001u','MBBS SriLanka(Colombo) MD USA','intern at general hospital Karapitiya','cardiology','6 years','1 year of experience in consulting',600),('22987','hms0007u','MBBS SriLanka(Peradeniya) MD USA','intern at general hospital Monaragala','orthopedics','6 years','1 year of experirnce in consulting',600),('28987','hms0011u','MBBS SriLanka(Karapitiya) Diploma in Disaster Management','intern at general hospital Monaragala','risk managment','4 years','best Asian disaster management personel award from Japan in 2014',400);
/*!40000 ALTER TABLE `doctor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `doctor_availability`
--

DROP TABLE IF EXISTS `doctor_availability`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `doctor_availability` (
  `time_slot_id` varchar(10) NOT NULL DEFAULT '',
  `slmc_reg_no` varchar(20) DEFAULT NULL,
  `day` int(11) DEFAULT NULL,
  `time_slot` varchar(15) DEFAULT NULL,
  `current_week_appointments` int(11) DEFAULT NULL,
  `next_week_appointments` int(11) DEFAULT NULL,
  PRIMARY KEY (`time_slot_id`),
  KEY `slmc_reg_no` (`slmc_reg_no`),
  CONSTRAINT `doctor_availability_ibfk_1` FOREIGN KEY (`slmc_reg_no`) REFERENCES `doctor` (`slmc_reg_no`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `doctor_availability`
--

LOCK TABLES `doctor_availability` WRITE;
/*!40000 ALTER TABLE `doctor_availability` DISABLE KEYS */;
INSERT INTO `doctor_availability` VALUES ('t0002','19245',2,'14:00-18:00',1,0),('t0003','18452',2,'16:00-18:00',2,0),('t0004','21462',1,'7:00-10:00',0,0),('t0005','15682',3,'16:00-18:00',1,0),('t0006','21987',1,'15:00-18:00',1,0),('t0007','22987',2,'9:00-12:00',1,0),('t0008','22287',1,'16:00-18:00',0,1),('t0009','16787',2,'16:00-18:00',2,0),('t0010','19993',1,'15:00-19:00',2,0),('t0011','28987',2,'16:00-18:00',0,0),('t0012','22387',4,'16:00-18:00',0,0),('t0013','22387',3,'09.00-11.00',0,0),('t0014','22387',7,'10.00-12.00',0,0);
/*!40000 ALTER TABLE `doctor_availability` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `drug`
--

DROP TABLE IF EXISTS `drug`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `drug` (
  `drug_id` varchar(10) NOT NULL DEFAULT '',
  `drug_name` varchar(20) DEFAULT NULL,
  `dangerous_drug` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`drug_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `drug`
--

LOCK TABLES `drug` WRITE;
/*!40000 ALTER TABLE `drug` DISABLE KEYS */;
INSERT INTO `drug` VALUES ('d0001','paracetamol',1),('d0002','nafcillin',1),('d0003','acetic acid',1),('d0004','amoxicillin',0),('d0005','Cloxacillin',0);
/*!40000 ALTER TABLE `drug` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `drug_brand_names`
--

DROP TABLE IF EXISTS `drug_brand_names`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `drug_brand_names` (
  `brand_id` varchar(10) NOT NULL DEFAULT '',
  `brand_name` varchar(40) DEFAULT NULL,
  `generic_name` varchar(40) DEFAULT NULL,
  `drug_type` varchar(20) DEFAULT NULL,
  `drug_unit` varchar(10) DEFAULT NULL,
  `unit_price` int(11) DEFAULT NULL,
  PRIMARY KEY (`brand_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `drug_brand_names`
--

LOCK TABLES `drug_brand_names` WRITE;
/*!40000 ALTER TABLE `drug_brand_names` DISABLE KEYS */;
INSERT INTO `drug_brand_names` VALUES ('br0001','panadol','paracetamol','tablet','mg',2),('br0002','calpol','paracetamol','tablet','mg',5),('br0003','tylenol','paracetamol','tablet','mg',3),('br0004','tipol','paracetamol','tablet','mg',2),('br0005','nafcil','nafcillin','tablet','mg',4),('br0006','nallpen','nafcillin','tablet','mg',5),('br0007','unipen','nafcillin','tablet','mg',4),('br0008','vosol ','acetic acid','tablet','mg',3),('br0009','acetasol','acetic acid','tablet','mg',2),('br0010','vasotate','acetic acid','tablet','mg',2),('br0011','amoxil ','amoxicillin','tablet','mg',3),('br0012','polymox','amoxicillin','tablet','mg',5),('br0013','trimox','amoxicillin','tablet','mg',4),('br0014','wymox','amoxicillin','tablet','mg',2),('br0015','Cloxapen','Cloxacillin','tablet','mg',3),('br0016','Clobex','Cloxacillin','tablet','mg',4);
/*!40000 ALTER TABLE `drug_brand_names` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lab_appointment`
--

DROP TABLE IF EXISTS `lab_appointment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lab_appointment` (
  `lab_appointment_id` varchar(15) NOT NULL DEFAULT '',
  `test_id` varchar(10) DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  `info` varchar(100) DEFAULT NULL,
  `patient_id` varchar(10) DEFAULT NULL,
  `bill_id` varchar(10) DEFAULT NULL,
  `lab_assistant_id` varchar(20) DEFAULT NULL,
  `cancelled` tinyint(1) DEFAULT NULL,
  `doctor_id` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`lab_appointment_id`),
  KEY `patient_id` (`patient_id`),
  KEY `lab_assistant_id` (`lab_assistant_id`),
  KEY `bill_id` (`bill_id`),
  KEY `test_id` (`test_id`),
  CONSTRAINT `lab_appointment_ibfk_1` FOREIGN KEY (`patient_id`) REFERENCES `patient` (`patient_id`),
  CONSTRAINT `lab_appointment_ibfk_2` FOREIGN KEY (`lab_assistant_id`) REFERENCES `lab_assistant` (`lab_assistant_id`),
  CONSTRAINT `lab_appointment_ibfk_3` FOREIGN KEY (`bill_id`) REFERENCES `bill` (`bill_id`),
  CONSTRAINT `lab_appointment_ibfk_4` FOREIGN KEY (`test_id`) REFERENCES `lab_test` (`test_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lab_appointment`
--

LOCK TABLES `lab_appointment` WRITE;
/*!40000 ALTER TABLE `lab_appointment` DISABLE KEYS */;
INSERT INTO `lab_appointment` VALUES ('lapp001','t001','2016-12-03 16:04:40',NULL,'hms0001pa','hms0001b',NULL,0,NULL),('lapp002','t002','2017-01-17 14:00:00',NULL,'hms0001pa',NULL,NULL,0,'22387'),('lapp003','t002','2017-01-17 14:00:00',NULL,'hms0001pa',NULL,NULL,0,'22387'),('lapp004','t002','2017-01-17 14:00:00',NULL,'hms0001pa',NULL,NULL,0,'19245'),('lapp005','t001','2017-01-17 16:00:00',NULL,'hms0001pa',NULL,NULL,0,'22387'),('lapp006','t002','2017-01-17 14:00:00',NULL,'hms0001pa',NULL,NULL,0,'19245');
/*!40000 ALTER TABLE `lab_appointment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lab_appointment_timetable`
--

DROP TABLE IF EXISTS `lab_appointment_timetable`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lab_appointment_timetable` (
  `app_id` varchar(15) NOT NULL DEFAULT '',
  `app_test_id` varchar(15) DEFAULT NULL,
  `app_day` int(11) DEFAULT NULL,
  `time_slot` varchar(15) DEFAULT NULL,
  `current_week_appointments` int(11) DEFAULT NULL,
  `next_week_appointments` int(11) DEFAULT NULL,
  PRIMARY KEY (`app_id`),
  KEY `app_test_id` (`app_test_id`),
  CONSTRAINT `lab_appointment_timetable_ibfk_1` FOREIGN KEY (`app_test_id`) REFERENCES `lab_test` (`test_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lab_appointment_timetable`
--

LOCK TABLES `lab_appointment_timetable` WRITE;
/*!40000 ALTER TABLE `lab_appointment_timetable` DISABLE KEYS */;
INSERT INTO `lab_appointment_timetable` VALUES ('labt0001','t001',1,'14:00-18:00',0,0),('labt0002','t001',2,'14:00-18:00',4,0),('labt0003','t001',3,'16:00-18:00',4,0),('labt0004','t002',1,'08:00-10:00',1,0),('labt0005','t002',7,'14:00-16:00',0,0),('labt0006','t002',3,'14:00-18:00',5,0),('labt0007','t003',5,'15:00-19:00',6,0),('labt0008','t003',1,'16:00-18:00',0,0),('labt0009','t003',2,'16:00-19:00',2,0),('labt0010','t004',5,'17:00-20:00',0,0),('labt0011','t004',1,'08:00-12:00',5,0),('labt0012','t004',3,'14:00-18:00',1,0),('labt0013','t005',2,'16:00-18:00',8,0),('labt0014','t005',7,'16:00-18:00',5,0),('labt0015','t005',1,'15:00-19:00',0,0),('labt0016','t006',5,'17:00-18:00',8,0),('labt0017','t006',3,'12:00-14:00',6,0),('labt0018','t006',6,'14:00-18:00',3,0),('labt0019','t007',6,'19:00-21:00',7,0),('labt0020','t007',2,'09:00-12:00',0,0),('labt0021','t007',1,'13:00-16:00',2,0),('labt0022','t008',7,'08:00-11:00',0,0),('labt0023','t008',4,'14:00-16:00',2,0),('labt0024','t008',1,'12:00-14:00',3,0);
/*!40000 ALTER TABLE `lab_appointment_timetable` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lab_assistant`
--

DROP TABLE IF EXISTS `lab_assistant`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lab_assistant` (
  `lab_assistant_id` varchar(10) NOT NULL DEFAULT '',
  `user_id` varchar(10) DEFAULT NULL,
  `education` varchar(100) DEFAULT NULL,
  `training` varchar(100) DEFAULT NULL,
  `experience` varchar(100) DEFAULT NULL,
  `achievements` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`lab_assistant_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `lab_assistant_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lab_assistant`
--

LOCK TABLES `lab_assistant` WRITE;
/*!40000 ALTER TABLE `lab_assistant` DISABLE KEYS */;
INSERT INTO `lab_assistant` VALUES ('hms0001l','hms0012u','high school diploma','trainee at clinical laboratory assistant certificate program','1 yr','Strong attention to detail|Excellent manual dexterity|Good analytical judgment'),('hms0002l','hms0013u','high school diploma','trainee at general hospital Karapitiya','2 yr','Strong attention to detail'),('hms0003l','hms0014u','high school diploma','trainee at clinical laboratory assistant certificate program','4 yr','Good analytical judgment'),('hms0004l','hms0015u','high school diploma','on the job','2 yr','Strong attention to detail|Excellent manual dexterity|Good analytical judgment');
/*!40000 ALTER TABLE `lab_assistant` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lab_test`
--

DROP TABLE IF EXISTS `lab_test`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lab_test` (
  `test_id` varchar(10) NOT NULL DEFAULT '',
  `test_name` varchar(50) DEFAULT NULL,
  `test_description` varchar(150) DEFAULT NULL,
  `test_fee` int(11) DEFAULT NULL,
  PRIMARY KEY (`test_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lab_test`
--

LOCK TABLES `lab_test` WRITE;
/*!40000 ALTER TABLE `lab_test` DISABLE KEYS */;
INSERT INTO `lab_test` VALUES ('t001','Urine Full Report','Urine test to analyze the body mineral and fluid levels',500),('t002','Lipid Test','Checks the lipid/cholesterol levels in the body',1000),('t003','Blood Grouping Rh','Checks the blood group',1500),('t004','Complete Blood Count','Counts the total in each type of blood cells',1000),('t005','Liver Function Test','Checks the functionality of the liver',2000),('t006','Renal Function Test','Checks the functionality of the kideny',2000),('t007','Serium Creatine Phosphokinase','measures the amount of creatine phosphokinase (CPK) present in the blood',3000),('t008','Serium Creatine Phosphokinase Total','measures the amount of creatine phosphokinase (CPK) present in the blood',3000);
/*!40000 ALTER TABLE `lab_test` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `medical_history`
--

DROP TABLE IF EXISTS `medical_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `medical_history` (
  `history_id` varchar(15) NOT NULL DEFAULT '',
  `patient_id` varchar(10) DEFAULT NULL,
  `doctor_id` varchar(20) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `history` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`history_id`),
  KEY `patient_id` (`patient_id`),
  KEY `doctor_id` (`doctor_id`),
  CONSTRAINT `medical_history_ibfk_1` FOREIGN KEY (`patient_id`) REFERENCES `patient` (`patient_id`),
  CONSTRAINT `medical_history_ibfk_2` FOREIGN KEY (`doctor_id`) REFERENCES `doctor` (`slmc_reg_no`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `medical_history`
--

LOCK TABLES `medical_history` WRITE;
/*!40000 ALTER TABLE `medical_history` DISABLE KEYS */;
INSERT INTO `medical_history` VALUES ('his0001','hms0001pa','22387','2016-01-01','Took medicine for dengue fever'),('his0002','hms0002pa','19245','2016-02-21','Did heart surgery 2 months ago|Allergy to heavy dose could cause unconsciousness'),('his0003','hms0003pa','15682','2016-01-31','Took medicine for dengue fever'),('his0004','hms0004pa','22387','2016-03-01','Wheezing|Allergic to antibiotics|Runny nose'),('his0005','hms0005pa','21462','2016-05-01','Diagnosed with hypertension and began on unknown medication|Stopped after 6 months because of drowsiness|Drugs were used to treat seizures|Diarrhea '),('his0006','hms0006pa','19245','2016-04-01','Lung problems and previous heart diseases|Allergic to penicillin|Constipation'),('his0007','hms0007pa','21987','2016-08-01','Migraine|Allergic to chocolate'),('his0008','hms0008pa','22387','2016-01-10','Heart attack due to heavy drinking|Chemotherapy drug|Breathing difficulties'),('his0055','hms0055pa','15682','2016-01-01','Sufferd with kidney failure|Previous Penecilin usage|Vomiting or diarrhea'),('his0056','hms0056pa','15682','2016-07-12','Motion abnormalities'),('his0057','hms0057pa','21987','2016-04-23','No palpable nodes in the cervical supraclavicular auxillary or inguinal areas|Chemotherapy drugs|Nausea or abdominal cramps'),('his0058','hms0058pa','19983','2016-05-12','Cholesterol is elevated|Sulfur drugs|Weak/rapid pulse'),('his0059','hms0059pa','22387','2016-05-26','Risk of myocardial infarction'),('his0060','hms0060pa','22387','2016-04-14','Systolic murmur'),('his0061','hms0061pa','21987','2016-03-31','Lumbo-sacral back pain|Previous Pencilin usage|Frequent drop in blood pressure'),('his0062','hms0062pa','21987','2016-03-28','Fibrocystic breast disease'),('his0063','hms0063pa','22387','2016-01-01','Chest pain with features of angina pectoris|Bee pollen products'),('his0064','hms0064pa','22387','2016-02-25','Systolic ejection murmur'),('his0065','hms0065pa','21287','2016-02-22','Lumbosacral back pain|Aspirin|Itchy|Watery eyes '),('his0066','hms0002pa','22387','2016-12-03','testings tests 0001'),('his0067','hms0001pa','22387','2017-01-09','Translate proponoj amba? profesia kaj homa ma?ino tradukoj inter 75 lingvoj. Tradukistoj povas anka? redakti pagis laborpostenoj pere de nia enreta portalo. Libera tradukisto de Cambridge Vortaroj - Kembri?o Vortaro traduki');
/*!40000 ALTER TABLE `medical_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `patient`
--

DROP TABLE IF EXISTS `patient`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `patient` (
  `patient_id` varchar(10) NOT NULL DEFAULT '',
  `person_id` varchar(15) DEFAULT NULL,
  `drug_allergies_and_reactions` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`patient_id`),
  KEY `person_id` (`person_id`),
  CONSTRAINT `patient_ibfk_1` FOREIGN KEY (`person_id`) REFERENCES `person` (`person_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patient`
--

LOCK TABLES `patient` WRITE;
/*!40000 ALTER TABLE `patient` DISABLE KEYS */;
INSERT INTO `patient` VALUES ('hms0001pa','hms00018','None,asas,asasas'),('hms0002pa','hms00058  ','Alergy to heavy dose could cause unconsciousness'),('hms0003pa','hms00051  ','none'),('hms0004pa','hms00044  ','Allergy to antibiotics'),('hms0005pa','hms00001  ','Diagnosed with hypertension and began on unknown medication|Stopped after 6 months because of drowsiness'),('hms0006pa','hms00002  ','Allergy to penicillin'),('hms0007pa','hms00003  ','Migraines and allergies occur due to food'),('hms0008pa','hms00004  ','Breathing difficulties to sulfur containing drugs'),('hms0009pa','hms00005  ','Constipation can be occured ue to antibiotics'),('hms0010pa','hms00006  ','None'),('hms0011pa','hms00007  ','None'),('hms0012pa','hms00008  ','none'),('hms0013pa','hms00009  ','Bee/Pollen products'),('hms0014pa','hms00010  ','Non'),('hms0015pa','hms00011  ','None'),('hms0016pa','hms00012  ','Easy bruising'),('hms0017pa','hms00013  ','Dyes can cause severe itching in palms'),('hms0018pa','hms00014  ','None'),('hms0019pa','hms00015  ','None '),('hms0020pa','hms00016  ','Intestianal bleeding can be occured if Ibuprofen is used '),('hms0021pa','hms00017  ','Bee/Pollen products'),('hms0022pa','hms00019  ','Morphine allergy ');
/*!40000 ALTER TABLE `patient` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `patient_message_receive`
--

DROP TABLE IF EXISTS `patient_message_receive`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `patient_message_receive` (
  `message_id` varchar(15) NOT NULL DEFAULT '',
  `receiver` varchar(20) DEFAULT NULL,
  `sender` varchar(20) DEFAULT NULL,
  `subject` varchar(50) DEFAULT NULL,
  `message` varchar(500) DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  PRIMARY KEY (`message_id`),
  KEY `receiver` (`receiver`),
  KEY `sender` (`sender`),
  CONSTRAINT `patient_message_receive_ibfk_1` FOREIGN KEY (`receiver`) REFERENCES `patient` (`patient_id`),
  CONSTRAINT `patient_message_receive_ibfk_2` FOREIGN KEY (`sender`) REFERENCES `sys_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patient_message_receive`
--

LOCK TABLES `patient_message_receive` WRITE;
/*!40000 ALTER TABLE `patient_message_receive` DISABLE KEYS */;
INSERT INTO `patient_message_receive` VALUES ('000001','pt0085','dr0001','information','test message','2016-10-20 00:00:00'),('000004','pt0066','rec001','Information','Cancellation of appointment on 2016-12-31','2016-09-20 00:00:00'),('000007','pt0066','dr0004','Information','Appointment cancelled','2016-10-29 00:00:00'),('000008','pt0066','dr0005','Information','Appointment cancelled','2016-10-29 00:00:00'),('000009','pt0066','dr0008','Information','Appointment cancelled','2016-10-29 00:00:00');
/*!40000 ALTER TABLE `patient_message_receive` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `patient_message_send`
--

DROP TABLE IF EXISTS `patient_message_send`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `patient_message_send` (
  `message_id` varchar(15) NOT NULL DEFAULT '',
  `receiver` varchar(20) DEFAULT NULL,
  `sender` varchar(50) DEFAULT NULL,
  `email` varchar(20) DEFAULT NULL,
  `message` varchar(500) DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  PRIMARY KEY (`message_id`),
  KEY `receiver` (`receiver`),
  CONSTRAINT `patient_message_send_ibfk_1` FOREIGN KEY (`receiver`) REFERENCES `sys_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patient_message_send`
--

LOCK TABLES `patient_message_send` WRITE;
/*!40000 ALTER TABLE `patient_message_send` DISABLE KEYS */;
INSERT INTO `patient_message_send` VALUES ('100000','rc0001','Kumudika','kumu@gmail.com','Good Service. Thank you!','2016-11-27 00:00:00'),('100001','rc0001','Prian','priyan@gmail.com','Good service.','2016-11-27 00:00:00');
/*!40000 ALTER TABLE `patient_message_send` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `patient_useraccount`
--

DROP TABLE IF EXISTS `patient_useraccount`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `patient_useraccount` (
  `patient_id` varchar(10) NOT NULL DEFAULT '',
  `person_id` varchar(15) DEFAULT NULL,
  `username` varchar(15) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`patient_id`),
  KEY `person_id` (`person_id`),
  CONSTRAINT `patient_useraccount_ibfk_1` FOREIGN KEY (`person_id`) REFERENCES `person` (`person_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patient_useraccount`
--

LOCK TABLES `patient_useraccount` WRITE;
/*!40000 ALTER TABLE `patient_useraccount` DISABLE KEYS */;
INSERT INTO `patient_useraccount` VALUES ('hms0001pa','hms00018  ','pts001','001pa'),('hms0002pa','hms00058  ','pts002','002pa'),('hms0003pa','hms00051  ','pts003','003pa'),('hms0004pa','hms00044  ','pts004','004pa'),('hms0005pa','hms00001  ','pts005','005pa'),('hms0006pa','hms00002  ','pts006','006pa'),('hms0007pa','hms00003  ','pts007','007pa'),('hms0008pa','hms00004  ','pts008','008pa'),('hms0009pa','hms00005  ','pts009','009pa'),('hms0010pa','hms00006  ','pts010','010pa'),('hms0011pa','hms00007  ','pts011','011pa'),('hms0012pa','hms00008  ','pts012','012pa'),('hms0013pa','hms00009  ','pts013','013pa'),('hms0014pa','hms00010  ','pts014','014pa'),('hms0015pa','hms00011  ','pts015','015pa'),('hms0016pa','hms00012  ','pts016','016pa'),('hms0017pa','hms00013  ','pts017','017pa'),('hms0018pa','hms00014  ','pts018','018pa'),('hms0019pa','hms00015  ','pts019','019pa'),('hms0020pa','hms00016  ','pts020','020pa'),('hms0021pa','hms00017  ','pts021','021pa'),('hms0022pa','hms00019  ','pts022','022pa');
/*!40000 ALTER TABLE `patient_useraccount` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `person`
--

DROP TABLE IF EXISTS `person`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `person` (
  `person_id` varchar(15) NOT NULL DEFAULT '',
  `user_id` varchar(10) DEFAULT NULL,
  `nic` varchar(10) DEFAULT NULL,
  `gender` char(1) DEFAULT NULL,
  `date_of_birth` date DEFAULT NULL,
  `address` varchar(50) DEFAULT NULL,
  `mobile` varchar(10) DEFAULT NULL,
  `first_name` char(20) DEFAULT NULL,
  `last_name` char(20) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `nationality` varchar(20) DEFAULT NULL,
  `religion` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`person_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `person_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `person`
--

LOCK TABLES `person` WRITE;
/*!40000 ALTER TABLE `person` DISABLE KEYS */;
INSERT INTO `person` VALUES ('hms00001',NULL,'652489712V','M','1965-09-04','No 26 Galla Estate Rajamawatha JaEla','0774523687','Kasun','Weerasekara','kasun35@gmail.com','Sri Lankan','Buddhism'),('hms00002',NULL,'895630096V','F','1989-03-03','G10 Brilliance Ambuldeniya Road Madiwala Kotte','0777658889','Bhagya','Silva','bhagsilva@gmail.com','Sri Lankan','Buddhism'),('hms00003',NULL,'765875355V','F','1976-03-27','54 Gonahena Weboda Kadawatha','0715422896','Thilini','Madugalle','thilimadu2@gmail.com','Sri Lankan','Buddhism'),('hms00004',NULL,'581665742V','M','1958-06-14','267/c/4 Sampathpura Koratota Kaduwela','0715655322','Supun','Peiris','supunp@gmail.com','Sri Lankan','Buddhism'),('hms00005',NULL,'475266897V','F','1947-01-26','457 Chapel Lane Nugegoda','0712256987','Nethmi','Pathirana','','Sri Lankan','Buddhism'),('hms00006',NULL,'557896324V','F','1955-10-15','334/d Balagolla Kegalle','0719987562','Tharushi','Perera','','Sri Lankan','Buddhism'),('hms00007',NULL,'611653222V','M','1961-06-13','22 Suhada Pedesa Mankada Road Kadawatha','0715547822','Tharindu','Gunawardena','tharindugu@gmail.com','Sri Lankan','Buddhism'),('hms00008',NULL,'566757991V','F','1956-06-23','466 Peradeniya Road Kadawatha','0754879665','Hiruni','Herath','','Sri Lankan','Buddhism'),('hms00009',NULL,'585339441V','F','1958-02-02','4 Better Homes Kotugoda','0714456985','Thilini','Premaratne','','Sri Lankan','Buddhism'),('hms00010',NULL,'496125348V','F','1949-04-21','No 1 Kohilawatte Road Kotikawaththa','0775442198','Nipuni','Jayakody','','Sri Lankan','Buddhism'),('hms00011',NULL,'521147146V','M','1952-04-23','98 Sri Saranankara Road Kalubovila','0714479858','Pasindu','Pathirana','','Sri Lankan','Buddhism'),('hms00012',NULL,'451698742V','M','1945-06-17','25/1 Wellampitiya Road Sedawatte','0724459896','Chathura','Senanayake','','Sri Lankan','Buddhism'),('hms00013',NULL,'668562143V','F','1966-12-21','13/2 A Fairline Road Dehiwala','0775479625','Dinithi','Dahanayake','dinithidaha22@yahoo.com','Sri Lankan','Buddhism'),('hms00014',NULL,'812235673V','M','1981-08-10','249/9 Pipe Road Koswatta','0754782659','Sandun','Cooray','sanduncoo5@hotmail.com','Sri Lankan','Buddhism'),('hms00015',NULL,'715236548V','F','1971-01-23','236 Gangarama Road Piliyandala','0772256965','Upeksha','Jayawickrama','upekshajaya7@yahoo.com','Sri Lankan','Buddhism'),('hms00016',NULL,'568562143V','F','1956-12-21','4 Old Kesbawa Road Nugegoda','0777215695','Shashini','Jayatilleka','','Sri Lankan','Buddhism'),('hms00017',NULL,'651298742V','M','1965-05-08','87 Mihidu Mawatha Colombo','0777459651','Tharaka','Bandaranaike','tharaka89@gmail.com','Sri Lankan','Buddhism'),('hms00018',NULL,'801876589V','M','1980-07-05','54  Vijemangala Roadd Kohuwala','0774126562','Ishara','Madugalle','ishmadu33@outlook.com','Sri Lankan','Buddhism'),('hms00019',NULL,'625844762V','F','1962-03-24','72/a Dehiwela Road Pepiliyana','0774788512','Nathasha','Rajapaksa','natharaj@gmail.com','Sri Lankan','Buddhism'),('hms00020',NULL,'587999856V','F','1958-10-25','No 2 2nd Lane Rawathawatte','0712259743','Sarah','Wickremasinghe','','Sri Lankan','Buddhism'),('hms00021',NULL,'471988956V','M','0000-00-00','16 Galwala Road Mirihana','0724785236','Roshan','Silva',' ','Sri Lankan','Buddhism'),('hms00022',NULL,'591298724V','M','1959-05-08','22 Seevali Mawatha Kiribathgoda','0785365259','Nuwan','Ranaweera','nuwan1876@gmail.com','Sri Lankan','Buddhism'),('hms00023',NULL,'601147852V','M','1960-04-23','65/4 Asiri Mawatha Kalubovila','0777891145','Hashan','Priyantha','hashan2pri@gmail.com','Sri Lankan','Buddhism'),('hms00024',NULL,'635585891V','F','1963-02-27','3 Union Place Colombo 2','0777459621','Vinodi','Peiris','vinodipeirs@gmail.com','Sri Lankan','Buddhism'),('hms00025',NULL,'547789656V','F','1954-10-04','150/b Koswatta Road Kalapaluwawa Rajagiriya','0718967412','Iresha','Karunaratne','','Sri Lankan','Buddhism'),('hms00026',NULL,'561098742V','M','1956-04-18','22 Henawatta Road Pannipitiya','0758789625','Dinesh','Jayakody','','Sri Lankan','Buddhism'),('hms00027',NULL,'451740982V','M','1945-06-22','610 Kotte Road Pitakotte','0774785665','Sasindu','Gunawardena','sasindu@gmail.com','Sri Lankan','Buddhism'),('hms00028',NULL,'751274589V','M','1975-05-06','217 Bandaragama Road Gammanpila','0757896651','Dilshan','Jayatilleka','dilsh67@gmail.com','Sri Lankan','Buddhism'),('hms00029',NULL,'517424585V','F','1951-08-29','15/1 Bethasda Place Colombo 5','0777896525','Vinodi','Jayasinghe','vinodi@gmail.com','Sri Lankan','Buddhism'),('hms00030',NULL,'412987421V','M','1941-10-24','No 43 Yakabadda Road Akurassa','0714469852','Chanaka','Pathirana','','Sri Lankan','Buddhism'),('hms00031',NULL,'641859725V','M','1964-07-03','24 Pamunuwa Road Maharagama','0754496878','Nishan','Ranaweera','nishrana6@gmail.com','Sri Lankan','Buddhism'),('hms00032',NULL,'712755897V','M','1971-10-01','No 65140 Alwitigala Mawatha Colombo 5','0774751125','Sameera','Gunasekera','sameeraguna32@gmail.com','Sri Lankan','Buddhism'),('hms00033',NULL,'682799985V','M','1968-10-05','76 Chandrika Kumaratunga Mawatha Malabe','0778745655','Gihan','Peiris','gihan365@gmail.com','Sri Lankan','Buddhism'),('hms00034',NULL,'622478896V','M','1962-09-03','264/3 Atigala Mawatha Ampilipity','0773117864','Hashan','Ekanayake','hashaneka9@gmail.com','Sri Lankan','Buddhism'),('hms00035',NULL,'742256848V','M','1974-08-12','66/6 Rathnayaka Mawatha Battaramulla','0779995487','Malindu','Jagath','malindujaga5@gmail.com','Sri Lankan','Buddhism'),('hms00036',NULL,'561024585V','M','1956-04-11','No 33 School Road Kadirana Negombo','0724876652','Harsha','Karunaratne','harshaK@gmail.com','Sri Lankan','Buddhism'),('hms00037',NULL,'701276589V','M','1970-05-06','22 Medankara Road Dehiwala','0779945521','Maduranga','Dissanayake','madu43@gmail.com','Sri Lankan','Buddhism'),('hms00038',NULL,'415587965V','F','1941-02-27','458 Somadevi Place Kirulapone','0716654896','Geethma','Gunawardena','null','Sri Lankan','Buddhism'),('hms00039',NULL,'556298742V','F','1955-05-08','358 Thuththiripitiya Watta Olupaliyawa','0772658741','Anuradha','Chandrasiri','anuchandi@gmail.com','Sri Lankan','Buddhism'),('hms00040',NULL,'487596221V','F','1948-09-15','No 218/13  Katukurunda Road  Moratuwa','0777456698','Sithmi','Pathirana','SithmiP@gmail.com','Sri Lankan','Buddhism'),('hms00041',NULL,'552985522V','M','1955-10-24','245  Veediya Bandara Mawatha Mulleriyawa','0717421332','Uvindu','Jayasinghe','uvindu@gmail.com','Sri Lankan','Buddhism'),('hms00042',NULL,'661771272V','M','1966-06-25','111/E Jude Mawatha Thimbirigusgatuwa','0771756625','Sasith','Balasuriya',' sasithbala3@gmail.com','Sri Lankan','Buddhism'),('hms00043',NULL,'682714985V','M','1968-09-27','No 63/2 Circular Road Attidiya','0772441665','Thushara','Hemantha','thushhema61@gmail.com','Sri Lankan','Buddhism'),('hms00044',NULL,'625144896V','F','1962-01-14','No 37/10 Hanwella Road Padukka','0714526321','Kalani','Fernando','kalanif@gmail.com','Sri Lankan','Buddhism'),('hms00045',NULL,'541877265V','M','1954-07-05','88 Rajamawatha Road Ratmalana','0754789958','Chinthaka','Jayatilleka','chinthaka@gmail.com','Sri Lankan','Buddhism'),('hms00046',NULL,'725116848V','F','1972-01-11','43/c Nagoda Kandana Road Kadewatta','0776452113','Chamudi','Abeynaike','chamudi9abey@gmail.com','Sri Lankan','Buddhism'),('hms00047',NULL,'391807065V','M','1939-06-28','245/8 Jayathilaka Mawatha Panadura','0752455617','Janitha','Perera','janitha@gmail.com','Sri Lankan','Buddhism'),('hms00048',NULL,'552478962V','M','1955-09-03','32 Suramya Gama Bandarawela','0775487452','Sumudu','Samarasinghe','sumudu@gmail.com','Sri Lankan','Buddhism'),('hms00049',NULL,'645155496V','F','1964-01-15','216 de Soyza Road Moratumulla','0774469852','Nishi','Chandrasiri','nishichand3@gmail.com','Sri Lankan','Buddhism'),('hms00050',NULL,'801145236V','M','1980-04-23','8 Balagalawatta Hendala Wattala','0754478962','Rajitha','Perera','rajith56@hotmail.com','Sri Lankan','Buddhism'),('hms00051',NULL,'745526821V','F','1974-02-21','456 Kaluwala Road Pahala','0777896254','Maleesha','Silva','malee3@yahoo.com','Sri Lankan','Buddhism'),('hms00052',NULL,'692752586V','M','1969-10-01','No 15 Patalirukkarama Road Wekada','0776578922','Kevin','Dissanayake','kevin5@gmail.com','Sri Lankan','Buddhism'),('hms00053',NULL,'712245698V','M','1971-08-11','24/6 Susantha Sirirathne Mawatha Koralawella','0754489628','Mahesh','Hemantha','mahesheman5@gmail.com','Sri Lankan','Buddhism'),('hms00055',NULL,'782144658V','M','1978-08-01','779/1/A Negambo Road Mabola','0775412364','Sumudu','Wijewardene','sumu7wije@gmail.com','Sri Lankan','Buddhism'),('hms00056',NULL,'625125686V','F','1962-01-12','547 Ranagala Mawatha Colombo 08','0774558231','Sathya','Mahadevan','sathya@gmail.com','Sri Lankan','Hinduism'),('hms00057',NULL,'602456988V','M','1960-09-01','110/4 Polhena Road Madapatha ','0716322514','Kavindu','Karunaratne','kavindukaru6@gmail.com','Sri Lankan','Buddhism'),('hms00058',NULL,'637151420V','F','1963-08-02','32 Poorwarama Road Colombo 5','0778521651','Sanjula','Subramaniam','sanju77@gmail.com','Sri Lankan','Hinduism'),('hms00059',NULL,'651259686V','M','1965-05-04','265 Kanaththa Road Mirihana','0776542897','Akila','Fernando','akila2gmail.com','Sri Lankan','Buddhism'),('hms00060',NULL,'911175265V','M','1991-04-26','44 Katuwana Road Homagama ','0758976653','Nuwan','Ekanayake','nuwan78@gmail.com','Sri Lankan','Buddhism'),('hms00061',NULL,'642256848V','M','1964-08-12','407/A Mahinda Mawatha Alubomulla','0778526649','Ahmad','Mushraf','mush56@gmail.com','Sri Lankan','Islam'),('hms00062',NULL,'511758265V','M','1951-06-23','881 Melder Place Nugegoda','0712441265','Dinesh','Seneviratne','Dinesh@gamil.com','Sri Lankan','Buddhism'),('hms00063',NULL,'602976988V','M','1960-10-23','64/2 Dharamanikethana Place Nawala','0776522589','Janitha','Ranaweera','janitha@gmail.com','Sri Lankan','Buddhism'),('hms00064',NULL,'627526586V','F','1962-09-08','No 383 Raja Road Rajagiriya','0755477456','Chamudi','Premaratne','chamu6@gmail.com','Sri Lankan','Buddhism'),('hms00065',NULL,'940273862V','M','1994-01-27','Nandana Sarammudali Mawatha Matara','0712925487','Harsha','Dhananjaya','harshadhananjaya27@gmail.com','Sri Lankan','Buddhism'),('hms00066',NULL,'938433020V','F','1993-12-08','318/b Kadurupokuna Tangalle','0773060152','Piumi','Dinuka','piumidinuka@gmail.com','Sri Lankan','Buddhism'),('hms00067',NULL,'905402342V','F','1990-02-09','123/a Gangodawila Nugegoda','0773322401','Nawab','Irishad','navab1232gmail.com','Sri Lankan','Islam'),('hms00068',NULL,'982345621V','M','1998-08-21','45/a Gangodawila Nugegoda','0762323123','Lasini','Wasana','lasini98@gmail.com','Sri Lankan','Budhdhism'),('hms00069',NULL,'912368745V','M','1991-08-23','234/23 Kalapaluwawa Rajagiriya','0712354980','Punujith','Hewagamage','punujith91@gmail.com','Sri Lankan','Budhdhism'),('hms00070',NULL,'795456721V','F','1979-02-14','318/b Kadurupokuna Tangalle','0772619619','Dilakshi','Anurudhdhika','dilakshi1015@gmail.com','Sri Lankan','Budhdhism'),('hms00071',NULL,'893452312V','M','1989-12-10','318/b Kadurupokuna Tangalle','0771876386','Charuka','Dilshan','charukadilshan89@gmail.com','Sri Lankan','Budhdhism'),('hms00072',NULL,'946782356V','F','1994-06-26','Aruna Nelumkulama Vavuniya','0775686234','Kirushanthi','Thyagaraja','kirushanthi94@yahoo.com','Sri Lankan','Hinduism'),('hms00073',NULL,'926782312V','F','1991-06-26','21/a Weeraketiya Tangalle','0763060152','Shashini','Maleesha','shashinimaleesha@gmail.com','Sri Lankan','Budhdhism'),('hms00074',NULL,'816753456V','F','1981-06-23','234/23 Kalapaluwawa Rajagiriya','0773336260','Sumudu','Kumari','sumudu81@gmail.com','Sri Lankan','Budhdhism'),('hms00075',NULL,'865912345V','F','1986-03-31','318/b Kadurupokuna Tangalle','777512150','Yasintha','Sewwandi','sewwandiyasintha@gmail.com','Sri Lankan','Budhdhism'),('hms00076',NULL,'523312334V','M','1952-11-26','45/a Kirulapana Nugegoda','0777200292','Bandu','Wijethilaka','bandu1952@gmail.com','Sri Lankan','Budhdhism'),('hms00077',NULL,'935463498V','F','1993-02-15','Vikalpa Polommaruwa Tangalle','0714470127','Darshika','Vikalpani','darshikavikalpani@gmail.com','Sri Lankan','Budhdhism'),('hms00078',NULL,'936782156V','F','1993-06-26','132/C Nawam mawatha Hambantota','0472243546','Arosha','Sale','shehaniarosha@gmail.com','Sri Lankan','Islam'),('hms00079',NULL,'932420054V','M','1993-08-29','Thiraj Udugama Galle','0773324312','Thiraj','Senevirathna','senevirathna93@yahoo.com','Sri Lankan','Budhdhism'),('hms00080',NULL,'935945667V','F','1993-04-03','Dilini Yanthampalawa Kurunegala','0712345863','Priyanwada','Kulasooriya','dilinipriyanwada@yahoo.com','Sri Lankan','Budhdhism'),('hms00081','hms0001u','723452312V','M','1972-12-10','67/a Jambugasmulla Lane Nugegoda','0772343544','Keerthi','Perera','keerthiperera72@yahoo.com','Sri Lankan','Buddhism'),('hms00082','hms0002u','792343467V','M','1979-08-21','89/23 Liyangemulla Seeduwa','0711849117','Ruwan','Wanigasinghe','ruwan1979@gmail.com','Sri Lankan','Buddhism'),('hms00083','hms0003u','732501278V','M','1973-09-06','76/24 Batalandahena IDH','0712002924','Haris','Silva','haris0609@gmail.com','Sri Lankan','Buddhism'),('hms00084','hms0004u','805672341V','F','1980-03-07','376/b Alawaththa Road Maharagama','0713060152','Harshika','Kumuduni','harshika1234@gmail.com','Sri Lankan','Buddhism'),('hms00085','hms0005u','758433020V','F','1975-12-08','23/a Pepiliyana Road Kohuwala','0712619619','Deepika','Wijethilaka','deepikawijethilaka@yahoo.com','Sri Lankan','Buddhism'),('hms00086','hms0006u','652483466V','M','1965-09-04','13/b Arawwala Maharagama','0773338567','Upul','Dasanayake','upuldasanayake65@gmail.com','Sri Lankan','Buddhism'),('hms00087','hms0007u','781254566V','M','1978-05-04','124/b Isadin Town Matara','0783563722','Benat','Wijethilaka','benatwijethilaka@gmail.com','Sri Lankan','Buddhism'),('hms00088','hms0008u','806892223V','F','1980-07-07','123/d Sunethradevi Road Pepiliyana','0710899455','Charithra','Fenando','chaarlie123@gmail.com','Sri Lankan','Buddhism'),('hms00089','hms0009u','892894357V','M','1989-10-15','23/c Granberg Road Maharagama','0777224559','Chalith','Desaman','chalith89@gmil.com','Sri Lankan','Buddhism'),('hms00090','hms0010u','682784669V','M','1968-10-04','12/b Depanama Pannipitiya','0783060152','Sandaruwan','Jayawickrama','jayawickrama6827@gmail.com','Sri Lankan','Buddhism'),('hms00091','hms0011u','882467952V','M','1988-09-23','20 Rohitha Road Aththidiya','0775489698','Sumedha','Dahanayake','sumedaha56@gmail.com','Sri Lankan','Buddhism'),('hms00092','hms0012u','732776595V','M','1973-10-03','79/2b Kawdana Road Dehiwela','0756678544','Malindu','Weerasekara','malindu5@gmail.com','Sri Lankan','Buddhism'),('hms00093','hms0013u','795532875V','F','1979-07-22','29/2 Kalutara Road Moratuwa','0716654985','Prabhani','Weerakoon','prabwee2@gmail.com','Sri Lankan','Buddhism'),('hms00094','hms0014u','698566321V','F','1969-12-21','44/a Koswatta Road Nawala Rajagiriya','0776522489','Hiruni','Nanayakkara','hirunana5@yahoo.com','Sri Lankan','Buddhism'),('hms00095','hms0015u','803349756V','M','1980-11-29','151/1 Amarasekara Mawatha Nugegoda','0754489632','Subash','Madushanka','submad87@gmail.com','Sri Lankan','Buddhism'),('hms00096','hms0016u','752466879V','M','1975-09-02','78 Station Road Biyagama','0772785641','Dhanura','Edirisinghe','dhanuedi32@gmail.com','Sri Lankan','Buddhism'),('hms00097','hms0017u','915895439V','F','1991-03-29','132/c Pitakotta Madiwela','0775565454','Gihani','Ekanayake','gihanikavindra@gmail.com','Sri Lankan','Buddhism'),('hms00098','hms0018u','895552344V','F','1989-02-24','Sewana Athuraliya Matara','0783564534','Dulanji','Manohari','dulanji89@yahoo.com','Sri Lankan','Buddhism'),('hms00099','hms0019u','822345679V','M','1982-08-21','Amana Kalapaluwawa Rajagiriya','0772943454','Gihan','Chamindu','gihan1234@gmail.com','Sri Lankan','Buddhism'),('hms00100','hms0020u','872984565V','M','1987-10-24','123/f Yanthampalawa Kurunegala','0713457779','Lakshitha','Rangana','lakshithaasd@gmail.com','Sri Lankan','Buddhism'),('hms00101','hms0021u','943562172V','m','1994-12-21','514/2 Vinayalankara Mawatha, Horana','0712453714','Heshan','Eranga','erangamx@gmail.com','null','null');
/*!40000 ALTER TABLE `person` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pharmacist`
--

DROP TABLE IF EXISTS `pharmacist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pharmacist` (
  `pharmacist_id` varchar(10) NOT NULL DEFAULT '',
  `user_id` varchar(10) DEFAULT NULL,
  `education` varchar(100) DEFAULT NULL,
  `training` varchar(100) DEFAULT NULL,
  `experience` varchar(100) DEFAULT NULL,
  `achievements` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`pharmacist_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `pharmacist_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pharmacist`
--

LOCK TABLES `pharmacist` WRITE;
/*!40000 ALTER TABLE `pharmacist` DISABLE KEYS */;
INSERT INTO `pharmacist` VALUES ('hms0001p','hms0016u','Master Pharmaceutical Sciences','trainee at general hospital Kalubowila','3 yrs','Award for pre-pharmacy studies'),('hms0002p','hms0017u','Bachelor Pharmaceutical Sciences','trainee at general hospital Colombo','2 yrs','Associate in pre-pharmacy studies');
/*!40000 ALTER TABLE `pharmacist` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pharmacy_history`
--

DROP TABLE IF EXISTS `pharmacy_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pharmacy_history` (
  `history_id` varchar(15) NOT NULL DEFAULT '',
  `prescription_id` varchar(15) DEFAULT NULL,
  `bill_id` varchar(15) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `no_of_drugs` int(11) DEFAULT NULL,
  `excluded` varchar(150) DEFAULT NULL,
  PRIMARY KEY (`history_id`),
  KEY `prescription_id` (`prescription_id`),
  KEY `bill_id` (`bill_id`),
  CONSTRAINT `pharmacy_history_ibfk_1` FOREIGN KEY (`prescription_id`) REFERENCES `prescription` (`prescription_id`),
  CONSTRAINT `pharmacy_history_ibfk_2` FOREIGN KEY (`bill_id`) REFERENCES `bill` (`bill_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pharmacy_history`
--

LOCK TABLES `pharmacy_history` WRITE;
/*!40000 ALTER TABLE `pharmacy_history` DISABLE KEYS */;
INSERT INTO `pharmacy_history` VALUES ('ph0001','pres00001','hms0001b','2016-08-09',NULL,'Ritaline'),('ph0002','pres00002','hms0002b','2016-09-09',NULL,'');
/*!40000 ALTER TABLE `pharmacy_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pharmacy_stock`
--

DROP TABLE IF EXISTS `pharmacy_stock`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pharmacy_stock` (
  `stock_id` varchar(10) NOT NULL DEFAULT '',
  `drug_id` varchar(15) DEFAULT NULL,
  `brand_id` varchar(15) DEFAULT NULL,
  `stock` int(11) DEFAULT NULL,
  `remaining_quantity` int(11) DEFAULT NULL,
  `manufac_date` date DEFAULT NULL,
  `exp_date` date DEFAULT NULL,
  `supplier_id` varchar(10) DEFAULT NULL,
  `date` date DEFAULT NULL,
  PRIMARY KEY (`stock_id`),
  KEY `drug_id` (`drug_id`),
  CONSTRAINT `pharmacy_stock_ibfk_1` FOREIGN KEY (`drug_id`) REFERENCES `drug` (`drug_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pharmacy_stock`
--

LOCK TABLES `pharmacy_stock` WRITE;
/*!40000 ALTER TABLE `pharmacy_stock` DISABLE KEYS */;
INSERT INTO `pharmacy_stock` VALUES ('stk0001','d0001','br0001',500,450,'2016-08-10','2017-09-01','sup0001','2016-08-20'),('stk0002','d0002','br0005',500,400,'2016-08-10','2017-09-01','sup0002','2016-08-20'),('stk0003','d0003','br0008',500,400,'2016-08-10','2017-09-01','sup0003','2016-08-20'),('stk0004','d0004','br0011',500,400,'2016-08-10','2017-09-01','sup0004','2016-08-20'),('stk0005','d0001','br0002',500,50,'2016-08-10','2017-09-01','sup0005','2016-08-20'),('stk0006','d0002','br0006',500,50,'2016-08-10','2017-09-01','sup0006','2016-08-20'),('stk0007','d0002','br0007',500,400,'2016-08-10','2017-09-01','sup0007','2016-08-20'),('stk0008','d0003','br0009',500,100,'2016-08-10','2017-09-01','sup0008','2016-08-20'),('stk0009','d0001','br0003',500,400,'2016-08-10','2017-09-01','sup0001','2016-08-20'),('stk0010','d0003','br0010',500,480,'2016-08-10','2017-09-01','sup0002','2016-08-20'),('stk0011','d0002','br0005',500,450,'2016-08-10','2017-09-01','sup0005','2016-08-20'),('stk0012','d0001','br0004',500,400,'2016-08-10','2017-09-01','sup0004','2016-08-20'),('stk0013','d0004','br0012',500,350,'2016-08-10','2017-09-01','sup0003','2016-08-20'),('stk0014','d0003','br0008',500,400,'2016-08-10','2017-09-01','sup0005','2016-08-20'),('stk0015','d0002','br0005',500,400,'2016-08-10','2017-09-01','sup0007','2016-08-20'),('stk0016','d0001','br0002',500,50,'2016-08-10','2017-09-01','sup0008','2016-08-20'),('stk0017','d0003','br0009',500,200,'2016-08-10','2017-09-01','sup0001','2016-08-20'),('stk0018','d0001','br0003',500,350,'2016-08-10','2017-09-01','sup0004','2016-08-20'),('stk0019','d0002','br0007',500,400,'2016-08-10','2017-09-01','sup0002','2016-08-20'),('stk0020','d0003','br0010',500,400,'2016-08-10','2017-09-01','sup0005','2016-08-20'),('stk0021','d0004','br0013',500,450,'2016-08-10','2017-09-01','sup0003','2016-08-20'),('stk0022','d0004','br0014',500,400,'2016-08-10','2017-09-01','sup0004','2016-08-20'),('stk0023','d0001','br0004',500,350,'2016-08-10','2017-09-01','sup0006','2016-08-20'),('stk0024','d0001','br0001',500,500,'2017-01-01','2019-01-01','sup0001','2017-01-11'),('stk0025','d0002','br0006',400,400,'2017-01-01','2019-01-16','sup0005','2017-01-11'),('stk0026','d0005','br0015',400,400,'2017-01-03','2019-01-23','sup0006','2017-01-11'),('stk0027','d0005','br0016',600,600,'2017-01-17','2018-01-19','sup0008','2017-01-11');
/*!40000 ALTER TABLE `pharmacy_stock` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prescription`
--

DROP TABLE IF EXISTS `prescription`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `prescription` (
  `prescription_id` varchar(10) NOT NULL DEFAULT '',
  `patient_id` varchar(10) DEFAULT NULL,
  `consultant_id` varchar(20) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `drugs_dose` varchar(300) DEFAULT NULL,
  `tests` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`prescription_id`),
  KEY `patient_id` (`patient_id`),
  KEY `consultant_id` (`consultant_id`),
  CONSTRAINT `prescription_ibfk_1` FOREIGN KEY (`patient_id`) REFERENCES `patient` (`patient_id`),
  CONSTRAINT `prescription_ibfk_2` FOREIGN KEY (`consultant_id`) REFERENCES `doctor` (`slmc_reg_no`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prescription`
--

LOCK TABLES `prescription` WRITE;
/*!40000 ALTER TABLE `prescription` DISABLE KEYS */;
INSERT INTO `prescription` VALUES ('pres00001','hms0001pa','22387','2016-01-01','paracetamol 2 pills 3 times perday','ECG|Blood test'),('pres00002','hms0003pa','15682','2016-02-21','methadone 1 pill 3 times per day afetr meals|ondanstron 1 pill per day at night after meals','FBC/APTT'),('pres00003','hms0007pa','21987','2016-05-06','topiramate 1 pill per day after meals per day time','ECG|Blood test'),('pres00004','hms0024pa','15682','2016-03-25','clariten-D 2 pills per day','Blood culture'),('pres00005','hms0033pa','22287','2016-05-08','paracetamol 2 pills 3 times per day','ECG|Blood test'),('pres00006','hms0043pa','15682','2016-02-21','methadone 1 pill 3 times per day afetr meals|Ondanstron 1 pill per day at night after meals','FBC/APTT'),('pres00007','hms0057pa','21987','2016-05-06','topiramate 1 pill per day after meals per day time','ECG|Blood test'),('pres00008','hms0064pa','15682','2016-03-25','clariten-D 2 pills per day','blood culture'),('pres00009','hms0012pa','16787','2016-04-14','ritalin 10mg 1 pill 3 times per day',''),('pres00010','hms0002pa','22387','2016-12-03','','NULL'),('pres00011','hms0001pa','22387','2017-01-09','acetasol 100mg|nallpen 50mg|calpol 200mg','NULL');
/*!40000 ALTER TABLE `prescription` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `refund`
--

DROP TABLE IF EXISTS `refund`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `refund` (
  `refund_id` varchar(15) NOT NULL DEFAULT '',
  `bill_id` varchar(15) DEFAULT NULL,
  `payment_type` varchar(50) DEFAULT NULL,
  `reason` varchar(150) DEFAULT NULL,
  `amount` int(11) DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  PRIMARY KEY (`refund_id`),
  KEY `bill_id` (`bill_id`),
  CONSTRAINT `refund_ibfk_1` FOREIGN KEY (`bill_id`) REFERENCES `bill` (`bill_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `refund`
--

LOCK TABLES `refund` WRITE;
/*!40000 ALTER TABLE `refund` DISABLE KEYS */;
INSERT INTO `refund` VALUES ('r0001','hms0003b','Cash','unable to attend on that day',400,'2016-10-06 00:00:00'),('r0002','hms0002b','cash','NO reason',700,'2017-01-05 15:23:02'),('r0003','hms0001b','cash','NO reason',500,'2017-01-05 15:23:02'),('r0004','hms0004b','cash','NO reason',600,'2017-01-05 15:23:02');
/*!40000 ALTER TABLE `refund` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `signup`
--

DROP TABLE IF EXISTS `signup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `signup` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fname` varchar(50) NOT NULL,
  `lname` varchar(50) DEFAULT NULL,
  `nic` varchar(10) DEFAULT NULL,
  `address` varchar(100) DEFAULT NULL,
  `contact` int(10) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `gender` varchar(50) DEFAULT NULL,
  `dob` varchar(50) DEFAULT NULL,
  `religion` varchar(50) DEFAULT NULL,
  `nationality` varchar(50) DEFAULT NULL,
  `maritalstatus` varchar(50) DEFAULT NULL,
  `medicalhistory` varchar(50) DEFAULT NULL,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `signup`
--

LOCK TABLES `signup` WRITE;
/*!40000 ALTER TABLE `signup` DISABLE KEYS */;
INSERT INTO `signup` VALUES (1,'','','','',0,'','','','\n','','','','',''),(2,'kumudika','Rupasinghe','936180124V','12/9 godakanda',7733333,'kumu@gmail.com','female','2016-10-26','Buddhist\n','Sinhalese','Single','Good Health Condition','kumu','123'),(3,'Sanath','Kamal','69435232V','23/ Mathara',443332,'sanath@gmail.com','male','2016-10-03','Buddhist\n','Sinhalese','Married','Elbow Injury','sanath','123'),(4,'hasi','rupe','1266868','fefe',86868,'hryryr','female','2322-02-02','Buddhist\n','Sinhalese','Single','','hasi','rupe'),(5,'aravinda','de silva','25646','gfhfghf',54345,'gf','male','25','Buddhist\n','Sinhalese','Single','','aravi','119'),(6,'lalanka','de silva','1425','junun',255,'hyhy','male','5355-05-05','Buddhist\n','Sinhalese','Single','','lalanka','lala'),(7,'Sanduni','Thrimahavithana','936070884V','Sathara, Kurunduwattha, Walgama, Matara',719219254,'ssthrima@gmail.com','female','1993-04-16','Buddhist\n','Sinhalese','Single','','sanduni','ucsc@123'),(8,'vv','vv','44','44',44,'44','male','2016-10-22','Buddhist\n','Sinhalese','Single','44','44','44'),(9,'tt','tt','33','55',55,'55','female','2016-10-28','Buddhist\n','Sinhalese','Single','555','55','55'),(10,'gg','gg','6','6',6,'6','female','2016-10-27','Buddhist\n','Sinhalese','Single','6','6','6'),(11,'u','u','8','8',8,'8','female','2016-10-21','Buddhist\n','Sinhalese','Single','88','88','88'),(12,'k','k','9','9',9,'9','female','2016-10-28','Buddhist\n','Sinhalese','Single','9','9','9'),(13,'ku','kuuuo','8','8',8,'8','female','2016-10-27','Buddhist\n','Sinhalese','Single','8','8','8'),(14,'r','r','4','44',44,'4','female','2016-10-27','Buddhist\n','Sinhalese','Single','4','4','4'),(15,'Sanath','rupasinghe','3333333333','3',2,'9','male','2016-10-21','Buddhist\n','Sinhalese','Single','e','e','e'),(16,'sampath','silva','99','99',99,'99','m','2016-10-22','Buddhist\n','Sinhalese','Single','9','9','9'),(17,'Kumudika','Rupasinghe','9303453V','12/1 , Godakanda',776854545,'kumu@gmail.com','f','1993-06-06','Buddhist\n','Sinhalese','Single','Good Condition','kumudika','123'),(18,'Kumudika','Rupasinghe','3424','galle',8687,'kumu@gmail.com','f','1993-10-22','Buddhist\n','Sinhalese','Single','Good condition','kumudika','123'),(19,'Kumudika','Rupasinghe','9303453V','galle,Galle',771231232,'kumu@gmail.com','f','1993-10-26','Buddhist\n','Sinhalese','Single','Good Condition','kumudika','123'),(20,'Test','Test patient','000000V','test road , test.',0,'test@testmail.com','m','2016-10-21','Buddhist\n','Sinhalese','Single','None','test','123'),(21,'Nuwan','De Zoysa','6854492V','123/2 , Souther Road , Mavanella',77685434,'nuwan@gmail.com','m','1980-10-19','Buddhist\n','Sinhalese','Single','Shoulder Injury','nuwan','nuwan'),(22,'Ruwan','Rajapaksha','653422V','12 , Madamulana , Hambanthota',7545343,'ruwan@gmail.com','m','1967-11-02','Buddhist\n','Sinhalese','Single','Brain Damage','ruwan','123'),(23,'Rasidu','Rupasinghe','20049682V','godakanda,galle',918564458,'rasindu@gmail.com','m','2004-12-11','Buddhist\n','Sinhalese','Single','Dengue','rasi','rasidu'),(24,'chathura','rajapaksha','915656644','godakanda',91256631,'chathu@gmail.com','m','2255-06-12','Buddhist\n','Sinhalese','Single','dengue','chathu','chathu'),(25,'Dilhara','Perera','9343432V','12/B , Mawathagama , Ja Ela',76543456,'dilhara@gmail.com','m','1990-10-29','Buddhist\n','Sinhalese','Single','Good Conditon','dilhara','123'),(26,'Avishka','Gunawardana','934332V','23 B , Kagalle Road , Kegalle',76584545,'avishka@gmail.com','m','2016-10-21','Buddhist\n','Sinhalese','Single','Dislocated Shoulder','avishka','123'),(27,'Avishka','Gunawardana','934332V','23 B , Kagalle Road , Kegalle',76584545,'avishka@gmail.com','m','2016-10-21','Buddhist\n','Sinhalese','Single','Dislocated Shoulder','avishka','123'),(28,'Arjuna','Ranathunga','453242V','33L , Mathara',776453423,'arjuna@gmail.com','m','1968-10-26','Buddhist\n','Sinhalese','Single','None','arjuna','123'),(29,'Siddharth','Rampaul','9433423V','2 , Maharashta , India',77645344,'sidd@gmail.com','m','1980-10-29','Buddhist\n','Sinhalese','Single','Dislocated Shoulder','sidd','123'),(30,'Rasindu','Weerasooriya','9423243V','23, Panadura road , Panadura',77654434,'rasi@gmail.com','m','1994-10-26','Buddhist\n','Sinhalese','Single','NONE','rasi','123'),(31,'Thilini','Bandara','9534223V','34, Mahalwarawa, Maharagama',7123232,'thilini@gmail.com','f','1990-10-31','Buddhist\n','Sinhalese','Single','Dengue Fever','thilini','123'),(32,'Lalith','Kothalawala','6532423V','2, Low level road , Maharagama',776564545,'lalith@yahoo.com','m','1967-10-26','Buddhist\n','Sinhalese','Single','Good Condition','lalith','123'),(33,'Kasuni','Wathsala','954343V','23, Kuliyapitya',7765654,'kasuni@gmail.com','f','1995-10-19','Buddhist\n','Sinhalese','Single','Dislocated elbow','kasuni','123'),(34,'Upul','Chandana','954334V','12, Udaha road , Kurunegala',754343,'upul@gmail.com','m','1991-10-26','Buddhist\n','Sinhalese','Single','NONE','upul','123');
/*!40000 ALTER TABLE `signup` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `suppliers`
--

DROP TABLE IF EXISTS `suppliers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `suppliers` (
  `supplier_id` varchar(10) DEFAULT NULL,
  `supplier_name` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `suppliers`
--

LOCK TABLES `suppliers` WRITE;
/*!40000 ALTER TABLE `suppliers` DISABLE KEYS */;
INSERT INTO `suppliers` VALUES ('sup0001','Biolase'),('sup0002','Biomet Inc.'),('sup0003','JTech Medical'),('sup0004','Medtronic Inc.'),('sup0005','Via Biomedical Inc.'),('sup0006','Solta Medical Inc.'),('sup0007','Nelson Laboratories Inc.'),('sup0008','Hospira Inc.');
/*!40000 ALTER TABLE `suppliers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user`
--

DROP TABLE IF EXISTS `sys_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_user` (
  `person_id` varchar(15) DEFAULT NULL,
  `user_id` varchar(10) NOT NULL DEFAULT '',
  `user_name` varchar(20) DEFAULT NULL,
  `user_type` varchar(15) DEFAULT NULL,
  `other_info` varchar(100) DEFAULT NULL,
  `password` varchar(35) DEFAULT NULL,
  `online` tinyint(1) DEFAULT NULL,
  `login` datetime DEFAULT NULL,
  `logout` datetime DEFAULT NULL,
  `profile_pic` varchar(20) DEFAULT 'p2.png',
  `suspend` int(11) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  KEY `person_id` (`person_id`),
  CONSTRAINT `sys_user_ibfk_1` FOREIGN KEY (`person_id`) REFERENCES `person` (`person_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user`
--

LOCK TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT INTO `sys_user` VALUES ('hms00081','hms0001u','user001','doctor','specialist in cardiology','1234',0,'2018-07-25 02:17:41','2018-07-25 02:18:12','user001ProfPic.png',NULL),('hms00083','hms0003u','user003','doctor','specialist in neurology','ccc123',NULL,NULL,NULL,'p2.png',NULL),('hms00084','hms0004u','user004','doctor','specialist in neurology','ddd123',1,'2018-07-25 10:21:49',NULL,'p2.png',NULL),('hms00085','hms0005u','user005','doctor','specialist in physiology','eee123',NULL,NULL,NULL,'p2.png',NULL),('hms00086','hms0006u','user006','doctor','specialist in physiology','aaa456',NULL,NULL,NULL,'p2.png',NULL),('hms00087','hms0007u','user007','doctor','specialist in orthopedics','bbb456',NULL,NULL,NULL,'p2.png',NULL),('hms00088','hms0008u','user008','doctor','specialist in orthopedics','ccc456',NULL,NULL,NULL,'p2.png',NULL),('hms00089','hms0009u','user009','doctor','specialist in paediatrics','ddd456',NULL,NULL,NULL,'p2.png',NULL),('hms00090','hms0010u','user010','doctor','specialist in endocrineology','eee456',NULL,NULL,NULL,'p2.png',NULL),('hms00091','hms0011u','user011','doctor','opd doctor','fff456',NULL,NULL,NULL,'p2.png',NULL),('hms00092','hms0012u','user012','lab_assistant','3 years of experience','1234',NULL,NULL,NULL,'user012ProfPic.png',NULL),('hms00093','hms0013u','user013','lab_assistant','3 years of experience','laaa2',NULL,NULL,NULL,'p2.png',NULL),('hms00094','hms0014u','user014','lab_assistant','4 years of experience','laaa3',NULL,NULL,NULL,'p2.png',NULL),('hms00095','hms0015u','user015','lab_assistant','2 years of experience','laaa4',NULL,NULL,NULL,'p2.png',NULL),('hms00096','hms0016u','user016','pharmacist','5 years of experience','1234',0,'2018-07-25 02:18:31','2018-07-25 02:19:10','user016ProfPic.png',NULL),('hms00097','hms0017u','user017','pharmacist','3 years of experience','pxxx123',NULL,NULL,NULL,'p2.png',NULL),('hms00098','hms0018u','user018','receptionist','diploma in british council','1234',NULL,NULL,NULL,'user018ProfPic.png',NULL),('hms00099','hms0019u','user019','receptionist','diploma in british council','rxx02',NULL,NULL,NULL,'p2.png',NULL),('hms00100','hms0020u','user020','cashier','CIMA qualified','1234',0,'2018-07-25 02:29:01','2018-07-25 02:29:06','user020ProfPic.png',NULL),('hms00101','hms0021u','user021','admin','','1234',1,'2018-07-25 10:33:32','2016-12-07 13:43:27','user021ProfPic.png',NULL);
/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tempappointment`
--

DROP TABLE IF EXISTS `tempappointment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tempappointment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `doctor_id` varchar(15) DEFAULT NULL,
  `time` time DEFAULT NULL,
  `date` date DEFAULT NULL,
  `patient_id` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tempappointment`
--

LOCK TABLES `tempappointment` WRITE;
/*!40000 ALTER TABLE `tempappointment` DISABLE KEYS */;
INSERT INTO `tempappointment` VALUES (1,'Haris Silva','08:00:00','2016-12-12','pr0101'),(2,'Haris Silva','08:00:00','2016-12-12','pr0101'),(3,'Harshika Kumudu','08:00:00','2016-12-12','pr0101'),(4,'Keerthi Perera','08:00:00','2016-12-12','pr0101'),(5,'Harshika Kumudu','08:00:00','2016-12-12','pr0101'),(6,'Chalith Desaman','08:00:00','2016-12-12','pr0101'),(7,'','08:00:00','2016-12-12','pr0119'),(8,'Benat Wijethila','08:00:00','2016-12-12','pr0102'),(9,'Keerthi Perera','08:00:00','2016-12-12','pr0102'),(10,'Haris Silva','08:00:00','2016-12-12','pr0101'),(11,'Haris Silva','08:00:00','2016-12-12','pr0101'),(12,'Haris Silva','08:00:00','2016-12-12','pr0101'),(13,'Deepika Wijethi','08:00:00','2016-12-12','pr0102'),(14,'Harshika Kumudu','08:00:00','2016-12-12','pr0101'),(15,'Haris Silva','08:00:00','2016-12-12','pr0101'),(16,'Deepika Wijethi','08:00:00','2016-12-12','pr0101'),(17,'Ruwan Wanigasin','08:00:00','2016-12-12','pr0101'),(18,'Benat Wijethila','08:00:00','2016-12-12','pr0101'),(19,'Ruwan Wanigasin','08:00:00','2016-12-12','pr0101'),(20,'Ruwan Wanigasin','08:00:00','2016-12-12','pr0101'),(21,'Harshika Kumudu','08:00:00','2016-12-12','pr0018  '),(22,'undefined','08:00:00','2016-12-12','pr0058  ');
/*!40000 ALTER TABLE `tempappointment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tmp_bill`
--

DROP TABLE IF EXISTS `tmp_bill`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tmp_bill` (
  `tmp_bill_id` varchar(10) NOT NULL DEFAULT '',
  `doctor_fee` int(11) DEFAULT NULL,
  `hospital_fee` int(11) DEFAULT NULL,
  `pharmacy_fee` int(11) DEFAULT NULL,
  `laboratory_fee` int(11) DEFAULT NULL,
  `appointment_fee` int(11) DEFAULT NULL,
  `vat` int(11) DEFAULT NULL,
  `discount` int(11) DEFAULT NULL,
  `consultant_id` varchar(10) DEFAULT NULL,
  `patient_id` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`tmp_bill_id`),
  KEY `patient_id` (`patient_id`),
  KEY `consultant_id` (`consultant_id`),
  CONSTRAINT `tmp_bill_ibfk_1` FOREIGN KEY (`patient_id`) REFERENCES `patient` (`patient_id`),
  CONSTRAINT `tmp_bill_ibfk_2` FOREIGN KEY (`consultant_id`) REFERENCES `doctor` (`slmc_reg_no`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tmp_bill`
--

LOCK TABLES `tmp_bill` WRITE;
/*!40000 ALTER TABLE `tmp_bill` DISABLE KEYS */;
/*!40000 ALTER TABLE `tmp_bill` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_message`
--

DROP TABLE IF EXISTS `user_message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_message` (
  `message_id` varchar(15) NOT NULL DEFAULT '',
  `reciver` varchar(20) DEFAULT NULL,
  `sender` varchar(20) DEFAULT NULL,
  `subject` varchar(50) DEFAULT NULL,
  `message` varchar(500) DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  `rd` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`message_id`),
  KEY `reciver` (`reciver`),
  KEY `sender` (`sender`),
  CONSTRAINT `user_message_ibfk_1` FOREIGN KEY (`reciver`) REFERENCES `sys_user` (`user_id`),
  CONSTRAINT `user_message_ibfk_2` FOREIGN KEY (`sender`) REFERENCES `sys_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_message`
--

LOCK TABLES `user_message` WRITE;
/*!40000 ALTER TABLE `user_message` DISABLE KEYS */;
INSERT INTO `user_message` VALUES ('msg00002','hms0001u','hms0018u','test','test message','2017-01-08 12:11:26',1),('msg00003','hms0001u','hms0018u','test','test message 3','2017-01-08 12:27:09',0),('msg00004','hms0001u','hms0018u','test','test message 04','2017-01-08 12:41:32',0),('msg00005','hms0018u','hms0001u','test','test message 5','2017-01-08 12:48:31',1),('msg00007','hms0001u','hms0018u','test','test message 6','2017-01-08 15:10:56',0),('msg00009','hms0018u','hms0008u','subject of the message','test message 8 kjsd dask asdk asjdh asjd askdj aksjd kjasd askjd askdj askjd asdkj asd asd asd asasd asd wer rer eref wedw qww etre dfer werwe werwe erwew','2017-01-09 00:48:31',1),('msg00010','hms0001u','hms0012u','testing','test message 20','2017-01-09 20:40:10',1),('msg00011','hms0012u','hms0001u','reply for the test','test message 21','2017-01-09 20:41:28',0),('msg00012','hms0016u','hms0001u','test message','test message 50','2017-01-09 22:42:43',1),('msg00013','hms0016u','hms0020u','test','test message 55','2017-01-10 01:55:26',0),('msg00014','hms0020u','hms0016u','test','test message 28','2017-01-10 01:56:18',0),('msg00015','hms0021u','hms0016u','test','testing message 78','2017-01-12 01:01:47',0),('msg00016','hms0001u','hms0021u','test','test message 88','2017-01-12 01:34:57',0);
/*!40000 ALTER TABLE `user_message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `website_messages`
--

DROP TABLE IF EXISTS `website_messages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `website_messages` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(60) DEFAULT NULL,
  `last_name` varchar(70) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `message` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1014 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `website_messages`
--

LOCK TABLES `website_messages` WRITE;
/*!40000 ALTER TABLE `website_messages` DISABLE KEYS */;
INSERT INTO `website_messages` VALUES (1000,'Kumudika','Rupasinghe','kumu@gmail.com','Good Service'),(1001,'Kumudika','Rupasinghe','kumu@gmail.com','1000'),(1002,'Chandana','Perera','chandana@yahoo.com','Good health Care. Thank you.'),(1003,'Lasith','Bandara','lasi@yahoo.com','Thank you for the care.'),(1004,'Sanath','Jayasooriya','sanath@gmail.com','Good Service. Thank you.'),(1005,'Hasini','Samarawickrama','hasini@gmail.com','I appreciate your service.'),(1006,'Dilshan','Perera','dilshan@gmail.com','Average Service.'),(1007,'Nuwan','Silva','nuwan@gmail.com','Good capable doctors. Thank you for the service.'),(1008,'Chathura','Dassanayaka','chathudass@gmail.com','Could you please send me your consultant list in Cardiology.'),(1009,'Sanduni','Thrimahavithana','ssthrima@gmail.com','ABC'),(1010,'Ku','Mudika','ku@gmail.com','Good Service.!'),(1011,'Eanjan','Ramanayaka','ran@gmail.com','Good Service!'),(1012,'','','',''),(1013,'Amali','Perera','ama@gmail.com','Should have more improvements');
/*!40000 ALTER TABLE `website_messages` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-08-02 12:51:13
