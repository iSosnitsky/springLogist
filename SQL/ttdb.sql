-- MySQL dump 10.13  Distrib 5.6.30, for Linux (x86_64)
--
-- Host: localhost    Database: transmaster_transport_db
-- ------------------------------------------------------
-- Server version	5.6.30

# /*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
# /*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
# /*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
# /*!40101 SET NAMES utf8 */;
# /*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
# /*!40103 SET TIME_ZONE='+00:00' */;
# /*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
# /*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
# /*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
# /*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Temporary view structure for view `all_users`
--
DROP SCHEMA IF EXISTS transmaster_transport_db;
CREATE SCHEMA transmaster_transport_db;
USE transmaster_transport_db;

DROP TABLE IF EXISTS `all_users`;
/*!50001 DROP VIEW IF EXISTS `all_users`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `all_users` AS SELECT 
 1 AS `userId`,
 1 AS `login`,
 1 AS `userName`,
 1 AS `position`,
 1 AS `phoneNumber`,
 1 AS `email`,
 1 AS `password`,
 1 AS `pointName`,
 1 AS `userRoleRusName`,
 1 AS `clientID`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `clients`
--

DROP TABLE IF EXISTS `clients`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `clients` (
  `clientID` int(11) NOT NULL AUTO_INCREMENT,
  `clientIDExternal` varchar(255) COLLATE utf8_bin NOT NULL,
  `dataSourceID` varchar(32) COLLATE utf8_bin NOT NULL,
  `INN` varchar(32) COLLATE utf8_bin NOT NULL,
  `clientName` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `KPP` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `corAccount` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `curAccount` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `BIK` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `bankName` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `contractNumber` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `dateOfSigning` date DEFAULT NULL,
  `startContractDate` date DEFAULT NULL,
  `endContractDate` date DEFAULT NULL,
  PRIMARY KEY (`clientID`),
  UNIQUE KEY `clientIDExternal` (`clientIDExternal`,`dataSourceID`),
  KEY `dataSourceID` (`dataSourceID`),
  CONSTRAINT `clients_ibfk_1` FOREIGN KEY (`dataSourceID`) REFERENCES `data_sources` (`dataSourceID`)
) ENGINE=InnoDB AUTO_INCREMENT=766536 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER after_clients_update AFTER UPDATE ON clients
FOR EACH ROW
  BEGIN
    IF (NEW.clientName <> OLD.clientName)
    THEN
      UPDATE mat_view_big_select
      SET clientName = NEW.clientName
      WHERE clientID = NEW.clientID;
    END IF;

    IF (NEW.INN <> OLD.INN)
    THEN
      UPDATE mat_view_big_select
      SET INN = NEW.INN
      WHERE clientID = NEW.clientID;
    END IF;
  END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `data_sources`
--

DROP TABLE IF EXISTS `data_sources`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `data_sources` (
  `dataSourceID` varchar(32) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`dataSourceID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `delivery_route_points`
--

DROP TABLE IF EXISTS `delivery_route_points`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `delivery_route_points` (
  `delivery_route_point_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `routeId` int(11) NOT NULL,
  `pointId` int(11) NOT NULL,
  PRIMARY KEY (`delivery_route_point_id`),
  UNIQUE KEY `delivery_route_points_routeId_pointId_pk` (`routeId`,`pointId`),
  KEY `delivery_route_points_pointId_index` (`pointId`)
) ENGINE=InnoDB AUTO_INCREMENT=65536 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `distances_between_points`
--

DROP TABLE IF EXISTS `distances_between_points`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `distances_between_points` (
  `pointIDFirst` int(11) NOT NULL DEFAULT '0',
  `pointIDSecond` int(11) NOT NULL DEFAULT '0',
  `distance` smallint(6) DEFAULT NULL,
  PRIMARY KEY (`pointIDFirst`,`pointIDSecond`),
  KEY `pointIDSecond` (`pointIDSecond`),
  CONSTRAINT `distances_between_points_ibfk_1` FOREIGN KEY (`pointIDFirst`) REFERENCES `points` (`pointID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `distances_between_points_ibfk_2` FOREIGN KEY (`pointIDSecond`) REFERENCES `points` (`pointID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `drivers`
--

DROP TABLE IF EXISTS `drivers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `drivers` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `vehicle_id` int(11) DEFAULT NULL,
  `transport_company_id` int(11) DEFAULT NULL,
  `full_name` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `passport` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `phone` varchar(18) COLLATE utf8_bin DEFAULT NULL,
  `license` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `deleted` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `drivers_transport_companies_id_fk` (`transport_company_id`),
  KEY `drivers_vehicles_id_fk` (`vehicle_id`),
  CONSTRAINT `drivers_transport_companies_id_fk` FOREIGN KEY (`transport_company_id`) REFERENCES `transport_companies` (`id`),
  CONSTRAINT `drivers_vehicles_id_fk` FOREIGN KEY (`vehicle_id`) REFERENCES `vehicles` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `mat_view_arrival_time_for_request`
--

DROP TABLE IF EXISTS `mat_view_arrival_time_for_request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mat_view_arrival_time_for_request` (
  `requestID` int(11) NOT NULL DEFAULT '0',
  `arrivalTimeToNextRoutePoint` datetime DEFAULT NULL,
  PRIMARY KEY (`requestID`),
  CONSTRAINT `mat_view_arrival_time_for_request_ibfk_1` FOREIGN KEY (`requestID`) REFERENCES `requests` (`requestID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `mat_view_big_select`
--

DROP TABLE IF EXISTS `mat_view_big_select`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mat_view_big_select` (
  `requestID` int(11) NOT NULL DEFAULT '0',
  `requestIDExternal` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `requestNumber` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `requestDate` datetime DEFAULT NULL,
  `invoiceNumber` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `invoiceDate` date DEFAULT NULL,
  `documentNumber` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `documentDate` datetime DEFAULT NULL,
  `firma` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `storage` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `boxQty` int(11) DEFAULT NULL,
  `requestStatusID` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `commentForStatus` text COLLATE utf8_bin,
  `requestStatusRusName` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `clientID` int(11) DEFAULT NULL,
  `clientIDExternal` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `INN` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `clientName` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `marketAgentUserID` int(11) DEFAULT NULL,
  `marketAgentUserName` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `driverUserID` int(11) DEFAULT NULL,
  `driverUserName` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `deliveryPointID` int(11) DEFAULT NULL,
  `deliveryPointName` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `warehousePointID` int(11) DEFAULT NULL,
  `warehousePointName` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `lastVisitedPointID` int(11) DEFAULT NULL,
  `lastVisitedPointName` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `nextPointID` int(11) DEFAULT NULL,
  `nextPointName` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `routeListNumber` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `licensePlate` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `palletsQty` int(11) DEFAULT NULL,
  `routeListID` int(11) DEFAULT NULL,
  `routeID` int(11) DEFAULT NULL,
  `routeName` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `arrivalTimeToNextRoutePoint` datetime DEFAULT NULL,
  PRIMARY KEY (`requestID`),
  KEY `ind1` (`marketAgentUserID`),
  KEY `ind2` (`clientID`),
  KEY `ind3` (`routeID`),
  KEY `ind4` (`invoiceDate`),
  CONSTRAINT `mat_view_big_select_ibfk_1` FOREIGN KEY (`requestID`) REFERENCES `requests` (`requestID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `mat_view_route_points_sequential`
--

DROP TABLE IF EXISTS `mat_view_route_points_sequential`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mat_view_route_points_sequential` (
  `routeID` int(11) NOT NULL DEFAULT '0',
  `routePointID` int(11) NOT NULL DEFAULT '0',
  `nextRoutePointID` int(11) DEFAULT NULL,
  `nextPointID` int(11) DEFAULT NULL,
  `nextPointName` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `timeToNextPoint` int(11) DEFAULT NULL,
  PRIMARY KEY (`routeID`,`routePointID`),
  KEY `nextRoutePointID` (`nextRoutePointID`),
  KEY `nextPointID` (`nextPointID`),
  KEY `ind4` (`routePointID`),
  CONSTRAINT `mat_view_route_points_sequential_ibfk_1` FOREIGN KEY (`routeID`) REFERENCES `routes` (`routeID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `mat_view_route_points_sequential_ibfk_2` FOREIGN KEY (`routePointID`) REFERENCES `route_points` (`routePointID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `mat_view_route_points_sequential_ibfk_3` FOREIGN KEY (`nextRoutePointID`) REFERENCES `route_points` (`routePointID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `mat_view_route_points_sequential_ibfk_4` FOREIGN KEY (`nextPointID`) REFERENCES `points` (`pointID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `mat_view_row_count_for_user`
--

DROP TABLE IF EXISTS `mat_view_row_count_for_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mat_view_row_count_for_user` (
  `userID` int(11) NOT NULL DEFAULT '0',
  `userRole` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `total_count` int(11) DEFAULT NULL,
  PRIMARY KEY (`userID`),
  KEY `ind5` (`userID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `permissions`
--

DROP TABLE IF EXISTS `permissions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `permissions` (
  `permissionID` varchar(32) COLLATE utf8_bin NOT NULL DEFAULT '',
  PRIMARY KEY (`permissionID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `permissions_for_roles`
--

DROP TABLE IF EXISTS `permissions_for_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `permissions_for_roles` (
  `userRoleID` varchar(32) COLLATE utf8_bin NOT NULL DEFAULT '',
  `permissionID` varchar(32) COLLATE utf8_bin NOT NULL DEFAULT '',
  PRIMARY KEY (`userRoleID`,`permissionID`),
  KEY `permissionID` (`permissionID`),
  CONSTRAINT `permissions_for_roles_ibfk_1` FOREIGN KEY (`permissionID`) REFERENCES `permissions` (`permissionID`),
  CONSTRAINT `permissions_for_roles_ibfk_2` FOREIGN KEY (`userRoleID`) REFERENCES `user_roles` (`userRoleID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `point_types`
--

DROP TABLE IF EXISTS `point_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `point_types` (
  `pointTypeID` varchar(32) COLLATE utf8_bin NOT NULL DEFAULT '',
  PRIMARY KEY (`pointTypeID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `points`
--

DROP TABLE IF EXISTS `points`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `points` (
  `pointID` int(11) NOT NULL AUTO_INCREMENT,
  `pointIDExternal` varchar(128) COLLATE utf8_bin NOT NULL,
  `dataSourceID` varchar(32) COLLATE utf8_bin NOT NULL,
  `pointName` varchar(128) COLLATE utf8_bin NOT NULL,
  `region` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `timeZone` tinyint(4) DEFAULT NULL,
  `docs` tinyint(4) DEFAULT NULL,
  `comments` longtext COLLATE utf8_bin,
  `openTime` time DEFAULT NULL,
  `closeTime` time DEFAULT NULL,
  `district` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `locality` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `mailIndex` varchar(6) COLLATE utf8_bin DEFAULT NULL,
  `address` text COLLATE utf8_bin NOT NULL,
  `email` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `phoneNumber` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `responsiblePersonId` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `pointTypeID` varchar(32) COLLATE utf8_bin NOT NULL,
  `x` decimal(20,12) DEFAULT '0.000000000000',
  `y` decimal(20,12) DEFAULT '0.000000000000',
  `requests_count` int(11) DEFAULT '0',
  `city_name` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`pointID`),
  UNIQUE KEY `pointIDExternal` (`pointIDExternal`,`dataSourceID`),
  KEY `dataSourceID` (`dataSourceID`),
  KEY `pointTypeID` (`pointTypeID`),
  CONSTRAINT `points_ibfk_1` FOREIGN KEY (`dataSourceID`) REFERENCES `data_sources` (`dataSourceID`),
  CONSTRAINT `points_ibfk_2` FOREIGN KEY (`pointTypeID`) REFERENCES `point_types` (`pointTypeID`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=957715 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER after_points_update AFTER UPDATE ON points
FOR EACH ROW
  IF (NEW.pointName <> OLD.pointName)
  THEN BEGIN
    UPDATE mat_view_big_select
    SET deliveryPointName = NEW.pointName
    WHERE deliveryPointID = NEW.pointID;

    UPDATE mat_view_big_select
    SET warehousePointName = NEW.pointName
    WHERE warehousePointID = NEW.pointID;

    UPDATE mat_view_big_select
    SET lastVisitedPointName = NEW.pointName
    WHERE lastVisitedPointID = NEW.pointID;

    UPDATE mat_view_big_select
    SET nextPointName = NEW.pointName
    WHERE nextPointID = NEW.pointID;
  END;
  END IF */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `pretensions`
--

DROP TABLE IF EXISTS `pretensions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pretensions` (
  `pretensionID` int(11) NOT NULL AUTO_INCREMENT,
  `pretensionComment` text COLLATE utf8_bin,
  `requestIDExternal` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `pretensionStatus` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `pretensionCathegory` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `sum` decimal(10,2) DEFAULT NULL,
  `positionNumber` text COLLATE utf8_bin,
  `dateAdded` date DEFAULT NULL,
  PRIMARY KEY (`pretensionID`),
  KEY `pretensions_ibfk_3` (`requestIDExternal`),
  CONSTRAINT `pretensions_ibfk_3` FOREIGN KEY (`requestIDExternal`) REFERENCES `requests` (`requestIDExternal`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `relations_between_route_points`
--

DROP TABLE IF EXISTS `relations_between_route_points`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `relations_between_route_points` (
  `routePointIDFirst` int(11) NOT NULL DEFAULT '0',
  `routePointIDSecond` int(11) NOT NULL DEFAULT '0',
  `timeForDistance` int(11) DEFAULT NULL,
  PRIMARY KEY (`routePointIDFirst`,`routePointIDSecond`),
  KEY `routePointIDSecond` (`routePointIDSecond`),
  CONSTRAINT `relations_between_route_points_ibfk_1` FOREIGN KEY (`routePointIDFirst`) REFERENCES `route_points` (`routePointID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `relations_between_route_points_ibfk_2` FOREIGN KEY (`routePointIDSecond`) REFERENCES `route_points` (`routePointID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER after_update AFTER UPDATE ON relations_between_route_points
FOR EACH ROW
  BEGIN
    UPDATE mat_view_route_points_sequential SET mat_view_route_points_sequential.timeToNextPoint = NEW.timeForDistance WHERE
      mat_view_route_points_sequential.routePointID = NEW.routePointIDFirst AND
      mat_view_route_points_sequential.nextRoutePointID IS NOT NULL AND
      mat_view_route_points_sequential.nextRoutePointID = NEW.routePointIDSecond;
  END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `request_statuses`
--

DROP TABLE IF EXISTS `request_statuses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `request_statuses` (
  `requestStatusID` varchar(32) COLLATE utf8_bin NOT NULL DEFAULT '',
  `requestStatusRusName` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `sequence` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`requestStatusID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `request_statuses_for_user_role`
--

DROP TABLE IF EXISTS `request_statuses_for_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `request_statuses_for_user_role` (
  `userRoleID` varchar(32) COLLATE utf8_bin NOT NULL DEFAULT '',
  `requestStatusID` varchar(32) COLLATE utf8_bin NOT NULL DEFAULT '',
  PRIMARY KEY (`userRoleID`,`requestStatusID`),
  KEY `requestStatusID` (`requestStatusID`),
  CONSTRAINT `request_statuses_for_user_role_ibfk_1` FOREIGN KEY (`userRoleID`) REFERENCES `user_roles` (`userRoleID`),
  CONSTRAINT `request_statuses_for_user_role_ibfk_2` FOREIGN KEY (`requestStatusID`) REFERENCES `request_statuses` (`requestStatusID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `requests`
--

DROP TABLE IF EXISTS `requests`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `requests` (
  `requestID` int(11) NOT NULL AUTO_INCREMENT,
  `requestIDExternal` varchar(255) COLLATE utf8_bin NOT NULL,
  `dataSourceID` varchar(32) COLLATE utf8_bin NOT NULL,
  `requestNumber` varchar(255) COLLATE utf8_bin NOT NULL,
  `requestDate` date NOT NULL,
  `clientID` int(11) NOT NULL,
  `destinationPointID` int(11) DEFAULT NULL,
  `marketAgentUserID` int(11) NOT NULL,
  `invoiceNumber` varchar(255) COLLATE utf8_bin NOT NULL,
  `invoiceDate` date DEFAULT NULL,
  `documentNumber` varchar(255) COLLATE utf8_bin NOT NULL,
  `documentDate` date DEFAULT NULL,
  `firma` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `storage` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `contactName` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `contactPhone` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `deliveryOption` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `deliveryDate` datetime DEFAULT NULL,
  `boxQty` int(11) DEFAULT NULL,
  `weight` int(11) DEFAULT NULL,
  `volume` int(11) DEFAULT NULL,
  `goodsCost` decimal(12,2) DEFAULT NULL,
  `lastStatusUpdated` datetime DEFAULT NULL,
  `lastModifiedBy` int(11) DEFAULT NULL,
  `requestStatusID` varchar(32) COLLATE utf8_bin NOT NULL,
  `commentForStatus` text COLLATE utf8_bin,
  `warehousePointID` int(11) DEFAULT NULL,
  `routeListID` int(11) DEFAULT NULL,
  `lastVisitedRoutePointID` int(11) DEFAULT NULL,
  `hoursAmount` int(11) DEFAULT '0',
  `transportCompanyId` int(11) DEFAULT NULL,
  `vehicleId` int(11) DEFAULT NULL,
  `driverId` int(11) DEFAULT NULL,
  PRIMARY KEY (`requestID`),
  UNIQUE KEY `requestIDExternal` (`requestIDExternal`,`dataSourceID`),
  KEY `dataSourceID` (`dataSourceID`),
  KEY `marketAgentUserID` (`marketAgentUserID`),
  KEY `clientID` (`clientID`),
  KEY `destinationPointID` (`destinationPointID`),
  KEY `lastModifiedBy` (`lastModifiedBy`),
  KEY `requestStatusID` (`requestStatusID`),
  KEY `warehousePointID` (`warehousePointID`),
  KEY `routeListID` (`routeListID`),
  KEY `lastVisitedRoutePointID` (`lastVisitedRoutePointID`),
  CONSTRAINT `requests_ibfk_1` FOREIGN KEY (`dataSourceID`) REFERENCES `data_sources` (`dataSourceID`),
  CONSTRAINT `requests_ibfk_2` FOREIGN KEY (`marketAgentUserID`) REFERENCES `users` (`userID`) ON UPDATE CASCADE,
  CONSTRAINT `requests_ibfk_3` FOREIGN KEY (`clientID`) REFERENCES `clients` (`clientID`) ON UPDATE CASCADE,
  CONSTRAINT `requests_ibfk_4` FOREIGN KEY (`destinationPointID`) REFERENCES `points` (`pointID`) ON UPDATE CASCADE,
  CONSTRAINT `requests_ibfk_5` FOREIGN KEY (`lastModifiedBy`) REFERENCES `users` (`userID`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `requests_ibfk_6` FOREIGN KEY (`requestStatusID`) REFERENCES `request_statuses` (`requestStatusID`),
  CONSTRAINT `requests_ibfk_7` FOREIGN KEY (`warehousePointID`) REFERENCES `points` (`pointID`) ON UPDATE CASCADE,
  CONSTRAINT `requests_ibfk_8` FOREIGN KEY (`routeListID`) REFERENCES `route_lists` (`routeListID`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `requests_ibfk_9` FOREIGN KEY (`lastVisitedRoutePointID`) REFERENCES `route_points` (`routePointID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=32153453 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER after_request_insert AFTER INSERT ON requests
FOR EACH ROW
  BEGIN
    INSERT INTO requests_history
      VALUE
      (NULL, NOW(), NEW.requestID, NEW.requestIDExternal, NEW.dataSourceID, NEW.requestNumber, NEW.requestDate,
       NEW.clientID, NEW.destinationPointID, NEW.marketAgentUserID, NEW.invoiceNumber, NEW.invoiceDate, NEW.documentNumber, NEW.documentDate,
       NEW.firma, NEW.storage, NEW.contactName, NEW.contactPhone, NEW.deliveryOption, NEW.deliveryDate, NEW.boxQty, NEW.weight, NEW.volume, NEW.goodsCost,
       NEW.lastStatusUpdated, NEW.lastModifiedBy, 'CREATED', NEW.commentForStatus, NEW.warehousePointID, NEW.routeListID, NEW.lastVisitedRoutePointID);

    CALL singleInsertOrUpdateOnMatViewBigSelect(NEW.requestID);
  END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER before_request_update BEFORE UPDATE ON requests
FOR EACH ROW
       -- берем пользователя, который изменил статус на один из request statuses, затем находим его пункт маршрута, и этот
       -- пункт записываем в таблицу requests в поле lastVisitedRoutePointID
       -- находим маршрут по которому едет накладная.
       IF (NEW.routeListID IS NOT NULL)
       THEN
              BEGIN

                     SET @routeID = (SELECT routeID
                                     FROM route_lists
                                     WHERE NEW.routeListID = route_lists.routeListID);
                     -- находим пункт пользователя, который изменил статус накладной.
                     -- при появлении маршрутного листа сразу выставляем последний посещенный пункт, как первый пункт маршрута
                     IF (NEW.lastVisitedRoutePointID IS NULL)
                     THEN
                            BEGIN
                                   -- установка самого первого пункта маршрута
                                   SET NEW.lastVisitedRoutePointID = (SELECT routePointID
                                                                      FROM route_points
                                                                      WHERE (routeID = @routeID)
                                                                      ORDER BY sortOrder
                                                                      LIMIT 1);
                                   IF (OLD.routeListID IS NULL)
                                   THEN
                                          BEGIN
                                                 SET NEW.requestStatusID = 'DEPARTURE';
                                          END;
                                   END IF;
                            END;
                     -- если была установка статуса в ARRIVED, то последний посещенный пункт меняется на следующий
                     ELSEIF (NEW.requestStatusID = 'ARRIVED' AND NEW.lastVisitedRoutePointID IS NOT NULL)
                            THEN
                                   BEGIN
                                          -- получаем порядковый номер последнего routePoint
                                          SET @lastRoutePointSortOrder = (SELECT sortOrder
                                                                          FROM route_points
                                                                          WHERE route_points.routePointID = OLD.lastVisitedRoutePointID);
                                          -- устанавливаем следующий пункт маршрута
                                          SET NEW.lastVisitedRoutePointID = (SELECT routePointID
                                                                             FROM route_points
                                                                             WHERE (routeID = @routeID AND sortOrder > @lastRoutePointSortOrder)
                                                                             ORDER BY sortOrder
                                                                             LIMIT 1);
                                   END;
                     ELSEIF (NEW.requestStatusID = 'ERROR' AND NEW.lastVisitedRoutePointID IS NOT NULL)
                            THEN
                                   BEGIN
                                          -- получаем порядковый номер последнего routePoint
                                          SET @lastRoutePointSortOrder = (SELECT sortOrder
                                                                          FROM route_points
                                                                          WHERE route_points.routePointID = OLD.lastVisitedRoutePointID);
                                          -- устанавливаем предыдущий пункт маршрута
                                          SET NEW.lastVisitedRoutePointID = (SELECT routePointID
                                                                             FROM route_points
                                                                             WHERE (routeID = @routeID AND sortOrder < @lastRoutePointSortOrder)
                                                                             ORDER BY sortOrder
                                                                             LIMIT 1);
                                   END;
                     ELSEIF (NEW.requestStatusID = 'DELIVERED' AND NEW.lastVisitedRoutePointID IS NOT NULL)
                            THEN
                                   BEGIN
                                          -- установка самого последнего пункта маршрута
                                          SET NEW.lastVisitedRoutePointID = (SELECT routePointID
                                                                             FROM route_points
                                                                             WHERE (routeID = @routeID)
                                                                             ORDER BY sortOrder DESC
                                                                             LIMIT 1);
                                   END;
                     END IF;
              END;
       ELSEIF (OLD.routeListID IS NOT NULL)
              THEN
                     SET NEW.lastVisitedRoutePointID = NULL;
       END IF */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER after_request_update AFTER UPDATE ON requests
FOR EACH ROW
  BEGIN
    INSERT INTO requests_history
      VALUE
      (NULL, NOW(), NEW.requestID, NEW.requestIDExternal, NEW.dataSourceID, NEW.requestNumber, NEW.requestDate,
       NEW.clientID, NEW.destinationPointID, NEW.marketAgentUserID, NEW.invoiceNumber, NEW.invoiceDate, NEW.documentNumber, NEW.documentDate,
       NEW.firma, NEW.storage, NEW.contactName, NEW.contactPhone, NEW.deliveryOption, NEW.deliveryDate, NEW.boxQty, NEW.weight, NEW.volume, NEW.goodsCost,
       NEW.lastStatusUpdated, NEW.lastModifiedBy, NEW.requestStatusID, NEW.commentForStatus, NEW.warehousePointID, NEW.routeListID, NEW.lastVisitedRoutePointID);

#     UPDATE mat_view_big_select SET
#       requestIDExternal = NEW.requestIDExternal, requestNumber = NEW.requestNumber, requestDate = NEW.requestDate,
#       invoiceNumber = NEW.invoiceNumber, invoiceDate = NEW.invoiceDate, documentNumber = NEW.documentNumber,
#       documentDate = NEW.documentDate, firma = NEW.firma, storage = NEW.storage, boxQty = NEW.boxQty,
#       requestStatusID = NEW.requestStatusID, commentForStatus = NEW.commentForStatus, routeListID = NEW.routeListID
#       WHERE mat_view_big_select.requestID = NEW.requestID;

    -- расчет времени прибытия заявки в следующий пункт маршрута
    IF (NEW.requestStatusID = 'DEPARTURE') THEN
      BEGIN
        SET @timeToNextPoint = (SELECT timeToNextPoint FROM mat_view_route_points_sequential WHERE routePointID = NEW.lastVisitedRoutePointID);
        SET @arrivalTimeToNextPoint = TIMESTAMPADD(MINUTE, @timeToNextPoint, NEW.lastStatusUpdated);

        INSERT INTO mat_view_arrival_time_for_request VALUE (NEW.requestID, @arrivalTimeToNextPoint)
        ON DUPLICATE KEY UPDATE
          arrivalTimeToNextRoutePoint = VALUES(arrivalTimeToNextRoutePoint);
      END;
    END IF;

    CALL singleInsertOrUpdateOnMatViewBigSelect(NEW.requestID);
  END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER after_request_delete AFTER DELETE ON requests
FOR EACH ROW
  INSERT INTO requests_history
    VALUE
    (NULL, NOW(), OLD.requestID, OLD.requestIDExternal, OLD.dataSourceID, OLD.requestNumber, OLD.requestDate,
     OLD.clientID, OLD.destinationPointID, OLD.marketAgentUserID, OLD.invoiceNumber, OLD.invoiceDate, OLD.documentNumber, OLD.documentDate,
     OLD.firma, OLD.storage, OLD.contactName, OLD.contactPhone, OLD.deliveryOption, OLD.deliveryDate, OLD.boxQty, OLD.weight, OLD.volume, OLD.goodsCost,
     OLD.lastStatusUpdated, OLD.lastModifiedBy, 'DELETED', OLD.commentForStatus, OLD.warehousePointID, OLD.routeListID, OLD.lastVisitedRoutePointID) */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `requests_history`
--

DROP TABLE IF EXISTS `requests_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `requests_history` (
  `requstHistoryID` bigint(20) NOT NULL AUTO_INCREMENT,
  `autoTimeMark` datetime NOT NULL,
  `requestID` int(11) DEFAULT NULL,
  `requestIDExternal` varchar(255) COLLATE utf8_bin NOT NULL,
  `dataSourceID` varchar(32) COLLATE utf8_bin NOT NULL,
  `requestNumber` varchar(255) COLLATE utf8_bin NOT NULL,
  `requestDate` date NOT NULL,
  `clientID` int(11) NOT NULL,
  `destinationPointID` int(11) DEFAULT NULL,
  `marketAgentUserID` int(11) NOT NULL,
  `invoiceNumber` varchar(255) COLLATE utf8_bin NOT NULL,
  `invoiceDate` date DEFAULT NULL,
  `documentNumber` varchar(255) COLLATE utf8_bin NOT NULL,
  `documentDate` date DEFAULT NULL,
  `firma` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `storage` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `contactName` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `contactPhone` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `deliveryOption` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `deliveryDate` datetime DEFAULT NULL,
  `boxQty` int(11) DEFAULT NULL,
  `weight` int(11) DEFAULT NULL,
  `volume` int(11) DEFAULT NULL,
  `goodsCost` decimal(12,2) DEFAULT NULL,
  `lastStatusUpdated` datetime DEFAULT NULL,
  `lastModifiedBy` int(11) DEFAULT NULL,
  `requestStatusID` varchar(32) COLLATE utf8_bin NOT NULL,
  `commentForStatus` text COLLATE utf8_bin,
  `warehousePointID` int(11) DEFAULT NULL,
  `routeListID` int(11) DEFAULT NULL,
  `lastVisitedRoutePointID` int(11) DEFAULT NULL,
  PRIMARY KEY (`requstHistoryID`),
  KEY `requestStatusID` (`requestStatusID`),
  KEY `ind6` (`requestIDExternal`),
  KEY `requests_history_clientID_index` (`clientID`),
  CONSTRAINT `requests_history_ibfk_1` FOREIGN KEY (`requestStatusID`) REFERENCES `request_statuses` (`requestStatusID`)
) ENGINE=InnoDB AUTO_INCREMENT=113859876 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `route_list_history`
--

DROP TABLE IF EXISTS `route_list_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `route_list_history` (
  `routeListHistoryID` bigint(20) NOT NULL AUTO_INCREMENT,
  `timeMark` datetime NOT NULL,
  `routeListID` int(11) NOT NULL,
  `routeListIDExternal` varchar(255) COLLATE utf8_bin NOT NULL,
  `dataSourceID` varchar(32) COLLATE utf8_bin NOT NULL,
  `routeListNumber` varchar(255) COLLATE utf8_bin NOT NULL,
  `creationDate` date DEFAULT NULL,
  `departureDate` date DEFAULT NULL,
  `palletsQty` int(11) DEFAULT NULL,
  `forwarderId` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `driverId` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `driverPhoneNumber` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `licensePlate` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `status` varchar(32) COLLATE utf8_bin NOT NULL,
  `routeID` int(11) NOT NULL,
  `dutyStatus` enum('CREATED','UPDATED','DELETED') COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`routeListHistoryID`),
  KEY `status` (`status`),
  CONSTRAINT `route_list_history_ibfk_1` FOREIGN KEY (`status`) REFERENCES `route_list_statuses` (`routeListStatusID`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=137596 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `route_list_statuses`
--

DROP TABLE IF EXISTS `route_list_statuses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `route_list_statuses` (
  `routeListStatusID` varchar(32) COLLATE utf8_bin NOT NULL DEFAULT '',
  `routeListStatusRusName` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`routeListStatusID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `route_lists`
--

DROP TABLE IF EXISTS `route_lists`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `route_lists` (
  `routeListID` int(11) NOT NULL AUTO_INCREMENT,
  `routeListIDExternal` varchar(255) COLLATE utf8_bin NOT NULL,
  `dataSourceID` varchar(32) COLLATE utf8_bin NOT NULL,
  `routeListNumber` varchar(255) COLLATE utf8_bin NOT NULL,
  `creationDate` date DEFAULT NULL,
  `departureDate` date DEFAULT NULL,
  `palletsQty` int(11) DEFAULT NULL,
  `forwarderId` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `driverID` int(11) DEFAULT NULL,
  `driverPhoneNumber` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `licensePlate` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `status` varchar(32) COLLATE utf8_bin NOT NULL,
  `routeID` int(11) NOT NULL,
  PRIMARY KEY (`routeListID`),
  UNIQUE KEY `routeListIDExternal` (`routeListIDExternal`,`dataSourceID`),
  KEY `status` (`status`),
  KEY `routeID` (`routeID`),
  KEY `driverID` (`driverID`),
  CONSTRAINT `route_lists_ibfk_1` FOREIGN KEY (`status`) REFERENCES `route_list_statuses` (`routeListStatusID`) ON UPDATE CASCADE,
  CONSTRAINT `route_lists_ibfk_2` FOREIGN KEY (`routeID`) REFERENCES `routes` (`routeID`) ON UPDATE CASCADE,
  CONSTRAINT `route_lists_ibfk_3` FOREIGN KEY (`driverID`) REFERENCES `users` (`userID`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=787479 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER after_route_list_insert AFTER INSERT ON route_lists
FOR EACH ROW
  INSERT INTO route_list_history
  VALUES
    (NULL, NOW(), NEW.routeListID, NEW.routeListIDExternal, NEW.dataSourceID, NEW.routeListNumber, NEW.creationDate,
     NEW.departureDate, NEW.palletsQty, NEW.forwarderId, NEW.driverID,
     NEW.driverPhoneNumber, NEW.licensePlate, NEW.status, NEW.routeID, 'CREATED') */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER after_route_lists_update AFTER UPDATE ON route_lists
FOR EACH ROW
  BEGIN

    INSERT INTO route_list_history
    VALUES
      (NULL, NOW(), NEW.routeListID, NEW.routeListIDExternal, NEW.dataSourceID, NEW.routeListNumber, NEW.creationDate,
       NEW.departureDate, NEW.palletsQty, NEW.forwarderId, NEW.driverID,
       NEW.driverPhoneNumber, NEW.licensePlate, NEW.status, NEW.routeID, 'UPDATED');

    -- синхронизация при изменении маршрутного листа routeListNumber, licencePlate, palletsQty
    IF (NEW.routeListNumber <> OLD.routeListNumber)
    THEN
      UPDATE mat_view_big_select
      SET routeListNumber = NEW.routeListNumber
      WHERE routeListID = NEW.routeListID;
    END IF;

    IF (NEW.licensePlate <> OLD.licensePlate)
    THEN
      UPDATE mat_view_big_select
      SET licensePlate = NEW.licensePlate
      WHERE routeListID = NEW.routeListID;
    END IF;

    IF (NEW.palletsQty <> OLD.palletsQty)
    THEN
      UPDATE mat_view_big_select
      SET palletsQty = NEW.palletsQty
      WHERE routeListID = NEW.routeListID;
    END IF;
  END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER after_route_list_delete AFTER DELETE ON route_lists
FOR EACH ROW
  INSERT INTO route_list_history
  VALUES
    (NULL, NOW(), OLD.routeListID, OLD.routeListIDExternal, OLD.dataSourceID, OLD.routeListNumber, OLD.creationDate,
     OLD.departureDate, OLD.palletsQty, OLD.forwarderId, OLD.driverID,
     OLD.driverPhoneNumber, OLD.licensePlate, OLD.status, OLD.routeID, 'DELETED') */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `route_points`
--

DROP TABLE IF EXISTS `route_points`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `route_points` (
  `routePointID` int(11) NOT NULL AUTO_INCREMENT,
  `sortOrder` int(11) NOT NULL,
  `timeForLoadingOperations` int(11) NOT NULL,
  `pointID` int(11) NOT NULL,
  `routeID` int(11) NOT NULL,
  PRIMARY KEY (`routePointID`),
  UNIQUE KEY `routeID` (`routeID`,`sortOrder`),
  KEY `pointID` (`pointID`),
  CONSTRAINT `route_points_ibfk_1` FOREIGN KEY (`pointID`) REFERENCES `points` (`pointID`) ON UPDATE CASCADE,
  CONSTRAINT `route_points_ibfk_2` FOREIGN KEY (`routeID`) REFERENCES `routes` (`routeID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=193 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER before_route_points_insert BEFORE INSERT ON route_points
FOR EACH ROW
    CALL check_route_points_constraints(NEW.routeID, NEW.sortOrder, NEW.pointID) */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER after_route_points_insert AFTER INSERT ON route_points
FOR EACH ROW
  BEGIN
    -- если в таблице route_points 2 или больше записей, то вставляем новые значения в relations_between_routePoints
    IF (SELECT count(*) FROM route_points WHERE NEW.routeID = route_points.routeID) > 1
    THEN
      BEGIN

        SET @nextRoutePointID = (SELECT routePointID FROM route_points WHERE route_points.routeID = NEW.routeID AND route_points.sortOrder > NEW.sortOrder ORDER BY sortOrder ASC LIMIT 1);
        SET @previousRoutePointID = (SELECT routePointID FROM route_points WHERE route_points.routeID = NEW.routeID AND route_points.sortOrder < NEW.sortOrder ORDER BY sortOrder DESC LIMIT 1);
        IF (@nextRoutePointID IS NULL AND @previousRoutePointID IS NULL ) THEN
          CALL generateLogistError('nextRoutePointID and previousRoutePointID is NULL');
        END IF;

        -- если мы добавили новый пункт в начало
        IF (@previousRoutePointID IS NULL) THEN
          INSERT INTO relations_between_route_points VALUE (NEW.routePointID, @nextRoutePointID, 0);
        -- если мы добавили новый пункт в конец
        ELSEIF (@nextRoutePointID IS NULL) THEN
          INSERT INTO relations_between_route_points VALUE (@previousRoutePointID, NEW.routePointID, 0);
        -- если мы добавили новый пункт в середину
        ELSE
          BEGIN
            UPDATE relations_between_route_points
            SET routePointIDSecond = NEW.routePointID, timeForDistance = 0
            WHERE routePointIDFirst = @previousRoutePointID AND routePointIDSecond = @nextRoutePointID;
            INSERT INTO relations_between_route_points VALUE (NEW.routePointID, @nextRoutePointID, 0);
          END;
        END IF;

        CALL refreshRoutePointsSequential(NEW.routeID);
      END;
    END IF;
  END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER before_route_points_update BEFORE UPDATE ON route_points
FOR EACH ROW
  BEGIN
    CALL generateLogistError('updates on route_points disabled');
  END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER before_route_points_delete BEFORE DELETE ON route_points
FOR EACH ROW
    CALL check_route_points_constraints(OLD.routeID, OLD.sortOrder, OLD.pointID) */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER after_route_points_delete AFTER DELETE ON route_points
FOR EACH ROW
  BEGIN
    -- если в таблице route_points 2 или больше записей, то вставляем новые значения в relations_between_routePoints
    IF (SELECT count(*) FROM route_points WHERE OLD.routeID = route_points.routeID) >= 2
    THEN
      BEGIN

        SET @nextRoutePointID = (SELECT routePointID FROM route_points WHERE route_points.routeID = OLD.routeID AND route_points.sortOrder > OLD.sortOrder ORDER BY sortOrder ASC LIMIT 1);
        SET @previousRoutePointID = (SELECT routePointID FROM route_points WHERE route_points.routeID = OLD.routeID AND route_points.sortOrder < OLD.sortOrder ORDER BY sortOrder DESC LIMIT 1);
        IF (@nextRoutePointID IS NULL AND @previousRoutePointID IS NULL ) THEN
          CALL generateLogistError('nextRoutePointID and previousRoutePointID is NULL');
        END IF;

        -- если мы удалили пункт из начала
        IF (@previousRoutePointID IS NULL) THEN
          DELETE FROM relations_between_route_points WHERE routePointIDFirst = OLD.routePointID;
        -- если мы удалили пункт с конца
        ELSEIF (@nextRoutePointID IS NULL) THEN
          DELETE FROM relations_between_route_points WHERE routePointIDFirst = OLD.routePointID OR routePointIDSecond = OLD.routePointID;
        -- если мы удалили пункт из середины
        ELSE
          BEGIN
            DELETE FROM relations_between_route_points WHERE routePointIDSecond = OLD.routePointID;
            UPDATE relations_between_route_points
            SET routePointIDFirst = @previousRoutePointID, timeForDistance = 0
            WHERE routePointIDFirst = OLD.routePointID;
          END;
        END IF;
      END;
    END IF;

    CALL refreshRoutePointsSequential(OLD.routeID);
  END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `routes`
--

DROP TABLE IF EXISTS `routes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `routes` (
  `routeID` int(11) NOT NULL AUTO_INCREMENT,
  `directionIDExternal` varchar(255) COLLATE utf8_bin NOT NULL,
  `dataSourceID` varchar(32) COLLATE utf8_bin NOT NULL,
  `routeName` varchar(255) COLLATE utf8_bin NOT NULL,
  `firstPointArrivalTime` time NOT NULL DEFAULT '00:00:00',
  `daysOfWeek` set('monday','tuesday','wednesday','thursday','friday','saturday','sunday') COLLATE utf8_bin NOT NULL DEFAULT '',
  `tariffID` int(11) DEFAULT NULL,
  `volume_limit` int(11) DEFAULT '0',
  `weight_limit` int(11) DEFAULT '0',
  `box_limit` int(11) DEFAULT '0',
  PRIMARY KEY (`routeID`),
  UNIQUE KEY `routeName` (`routeName`),
  UNIQUE KEY `directionIDExternal` (`directionIDExternal`,`dataSourceID`),
  KEY `tariffID` (`tariffID`),
  KEY `dataSourceID` (`dataSourceID`),
  CONSTRAINT `routes_ibfk_1` FOREIGN KEY (`tariffID`) REFERENCES `tariffs` (`tariffID`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `routes_ibfk_2` FOREIGN KEY (`dataSourceID`) REFERENCES `data_sources` (`dataSourceID`)
) ENGINE=InnoDB AUTO_INCREMENT=451895 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER after_routes_update AFTER UPDATE ON routes
FOR EACH ROW
  IF (NEW.routeName <> OLD.routeName)
  THEN
    UPDATE mat_view_big_select
    SET routeName = NEW.routeName
    WHERE routeID = NEW.routeID;
  END IF */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `storages_to_points`
--

DROP TABLE IF EXISTS `storages_to_points`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `storages_to_points` (
  `storage` varchar(250) COLLATE utf8_bin NOT NULL,
  `point_id` int(11) NOT NULL,
  UNIQUE KEY `storages_to_points_storage_uindex` (`storage`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tariffs`
--

DROP TABLE IF EXISTS `tariffs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tariffs` (
  `tariffID` int(11) NOT NULL AUTO_INCREMENT,
  `cost` decimal(12,2) DEFAULT '0.00',
  `capacity` decimal(4,2) DEFAULT NULL,
  `carrier` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `cost_per_point` decimal(12,2) DEFAULT '0.00',
  `cost_per_hour` decimal(12,2) DEFAULT '0.00',
  `cost_per_box` decimal(12,2) DEFAULT NULL,
  PRIMARY KEY (`tariffID`)
) ENGINE=InnoDB AUTO_INCREMENT=92 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `transport_companies`
--

DROP TABLE IF EXISTS `transport_companies`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `transport_companies` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `short_name` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `inn` int(11) DEFAULT NULL,
  `KPP` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `BIK` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `cor_account` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `cur_account` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `bank_name` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `legal_address` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `post_address` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `keywords` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `director_fullname` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `chief_acc_fullname` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `deleted` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_roles`
--

DROP TABLE IF EXISTS `user_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_roles` (
  `userRoleID` varchar(32) COLLATE utf8_bin NOT NULL DEFAULT '',
  `userRoleRusName` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`userRoleID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `userID` int(11) NOT NULL AUTO_INCREMENT,
  `userIDExternal` varchar(255) COLLATE utf8_bin NOT NULL,
  `dataSourceID` varchar(32) COLLATE utf8_bin NOT NULL,
  `login` varchar(255) COLLATE utf8_bin NOT NULL,
  `salt` char(16) COLLATE utf8_bin NOT NULL,
  `passAndSalt` varchar(64) COLLATE utf8_bin NOT NULL,
  `userRoleID` varchar(32) COLLATE utf8_bin NOT NULL,
  `userName` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `phoneNumber` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `email` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `position` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `pointID` int(11) DEFAULT NULL,
  `clientID` int(11) DEFAULT NULL,
  PRIMARY KEY (`userID`),
  UNIQUE KEY `userIDExternal` (`userIDExternal`,`dataSourceID`),
  UNIQUE KEY `login` (`login`),
  KEY `dataSourceID` (`dataSourceID`),
  KEY `userRoleID` (`userRoleID`),
  KEY `pointID` (`pointID`),
  KEY `clientID` (`clientID`),
  CONSTRAINT `users_ibfk_1` FOREIGN KEY (`dataSourceID`) REFERENCES `data_sources` (`dataSourceID`),
  CONSTRAINT `users_ibfk_2` FOREIGN KEY (`userRoleID`) REFERENCES `user_roles` (`userRoleID`) ON UPDATE CASCADE,
  CONSTRAINT `users_ibfk_3` FOREIGN KEY (`pointID`) REFERENCES `points` (`pointID`) ON UPDATE CASCADE,
  CONSTRAINT `users_ibfk_4` FOREIGN KEY (`clientID`) REFERENCES `clients` (`clientID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=663904 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER check_users_constraints_insert BEFORE INSERT ON users
FOR EACH ROW
  CALL checkUserConstraints(NEW.userRoleID, NEW.pointID, NEW.clientID, NEW.userIDExternal, NEW.login) */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER check_users_constraints_update BEFORE UPDATE ON users
FOR EACH ROW
  CALL checkUserConstraints(NEW.userRoleID, NEW.pointID, NEW.clientID, NEW.userIDExternal, NEW.login) */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `vehicles`
--

DROP TABLE IF EXISTS `vehicles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `vehicles` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `transport_company_id` int(11) DEFAULT NULL,
  `license_number` varchar(12) COLLATE utf8_bin DEFAULT NULL,
  `model` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `carrying_capacity` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `volume` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `loading_type` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `pallets_quantity` int(11) DEFAULT NULL,
  `type` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `deleted` tinyint(1) NOT NULL DEFAULT '0',
  `wialon_id` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `vehicles_transport_companies_id_fk` (`transport_company_id`),
  CONSTRAINT `vehicles_transport_companies_id_fk` FOREIGN KEY (`transport_company_id`) REFERENCES `transport_companies` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Final view structure for view `all_users`
--

/*!50001 DROP VIEW IF EXISTS `all_users`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `all_users` AS select `u`.`userID` AS `userId`,`u`.`login` AS `login`,`u`.`userName` AS `userName`,`u`.`position` AS `position`,`u`.`phoneNumber` AS `phoneNumber`,`u`.`email` AS `email`,'dummy' AS `password`,`p`.`pointName` AS `pointName`,`r`.`userRoleRusName` AS `userRoleRusName`,`c`.`clientID` AS `clientID` from (((`users` `u` left join `points` `p` on((`p`.`pointID` = `u`.`pointID`))) left join `clients` `c` on((`c`.`clientID` = `u`.`clientID`))) join `user_roles` `r` on((`r`.`userRoleID` = `u`.`userRoleID`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-10-19  4:46:40
