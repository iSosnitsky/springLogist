BEGIN;

SET GLOBAL sql_mode = 'STRICT_TRANS_TABLES';

CREATE DATABASE `transmaster_transport_db`
  CHARACTER SET utf8
  COLLATE utf8_bin;
USE `transmaster_transport_db`;

-- _errorMessage must be less or equal 110 symbols
CREATE PROCEDURE generateLogistError(_errorMessage TEXT)
  BEGIN
    SET @message_text = concat('LOGIST ERROR: ', _errorMessage);
    SIGNAL SQLSTATE '45000'
    SET MESSAGE_TEXT = @message_text;
  END;

-- -------------------------------------------------------------------------------------------------------------------
--                                                  DATA SOURCES
-- -------------------------------------------------------------------------------------------------------------------

CREATE TABLE data_sources (
  dataSourceID VARCHAR(32) PRIMARY KEY
);
INSERT INTO data_sources
VALUES
  ('LOGIST_1C')
  , ('ADMIN_PAGE');

-- -------------------------------------------------------------------------------------------------------------------
--                                                 CLIENTS
-- -------------------------------------------------------------------------------------------------------------------

CREATE TABLE clients (
  clientID          INTEGER AUTO_INCREMENT,
  clientIDExternal  VARCHAR(255) NOT NULL,
  dataSourceID      VARCHAR(32)  NOT NULL,
  INN               VARCHAR(32)  NOT NULL,
  clientName        VARCHAR(255) NULL,
  KPP               VARCHAR(64)  NULL,
  corAccount        VARCHAR(64)  NULL,
  curAccount        VARCHAR(64)  NULL,
  BIK               VARCHAR(64)  NULL,
  bankName          VARCHAR(128) NULL,
  contractNumber    VARCHAR(64)  NULL,
  dateOfSigning     DATE         NULL,
  startContractDate DATE         NULL,
  endContractDate   DATE         NULL,
  PRIMARY KEY (clientID),
  FOREIGN KEY (dataSourceID) REFERENCES data_sources (dataSourceID),
  UNIQUE (clientIDExternal, dataSourceID)
);

-- -------------------------------------------------------------------------------------------------------------------
--                                        USERS ROLES PERMISSIONS AND POINTS
-- -------------------------------------------------------------------------------------------------------------------

CREATE TABLE user_roles (
  userRoleID      VARCHAR(32),
  userRoleRusName VARCHAR(128),
  PRIMARY KEY (userRoleID)
);

INSERT INTO user_roles (userRoleID, userRoleRusName)
VALUES
  -- администратор, ему доступен полный графический интерфейс сайта и самые высокие права на изменение в БД:
  -- имеет право изменить роль пользователя
  ('ADMIN', 'Администратор'),
  -- показывать заявки, которые проходят через пункт к которому приписан W_DISPATCHER
  -- диспетчер склада, доступна часть GUI и соответсвующие права на изменения в БД  возможность для каждого маршрутного листа или отдельной накладной заносить кол-во паллет и статус убыл
  ('W_DISPATCHER', 'Диспетчер_склада'),
  -- показывать заявки, которые проходят через пункт к которому приписан DISPATCHER
  -- диспетчер, доступен GUI для установки статуса накладных или маршрутных листов и соответсвующие права на изменения в БД, статус прибыл, и статус убыл, статус "ошибка".
  ('DISPATCHER', 'Диспетчер'),
  -- пользователь клиента, доступен GUI для для просмотра всех заявок данного клиента.
  ('CLIENT_MANAGER', 'Пользователь_клиента'),
  -- торговый представитель, доступ только на чтение тех заявок, в которых он числится торговым
  ('MARKET_AGENT', 'Торговый_представитель'),
  ('TEMP_REMOVED', 'Временно_удален');
-- TODO сделать правильну работу для указанных ниже ролей пользователей, прописать ограничения в таблице users
-- временно удален, доступен GUI только для страницы авторизации, также после попытки войти необходимо выводить сообщение,
-- что данный пользователь зарегистрирован в системе, но временно удален. Полный запрет на доступ к БД.

-- ('VIEW_LAST_TEN', 'Последние_десять')
;

CREATE TABLE point_types (
  pointTypeID VARCHAR(32),
  PRIMARY KEY (pointTypeID)
);

INSERT INTO point_types
VALUES
  ('WAREHOUSE'),
  ('AGENCY');

CREATE TABLE points (
  pointID             INTEGER AUTO_INCREMENT,
  pointIDExternal     VARCHAR(128)   NOT NULL,
  dataSourceID        VARCHAR(32)    NOT NULL,
  pointName           VARCHAR(128)   NOT NULL,
  region              VARCHAR(128)   NULL,
  timeZone            TINYINT SIGNED NULL, -- сдвиг времени по гринвичу GMT + value
  docs                TINYINT SIGNED NULL, -- количество окон разгрузки
  comments            LONGTEXT       NULL,
  openTime            TIME           NULL, -- например 9:00
  closeTime           TIME           NULL, -- например 17:00
  district            VARCHAR(64)    NULL,
  locality            VARCHAR(64)    NULL,
  mailIndex           VARCHAR(6)     NULL,
  address             TEXT           NOT NULL,
  email               VARCHAR(255)   NULL,
  phoneNumber         VARCHAR(255)   NULL,
  responsiblePersonId VARCHAR(128)   NULL,
  pointTypeID         VARCHAR(32)    NOT NULL,
  PRIMARY KEY (pointID),
  FOREIGN KEY (dataSourceID) REFERENCES data_sources (dataSourceID),
  FOREIGN KEY (pointTypeID) REFERENCES point_types (pointTypeID)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  UNIQUE (pointIDExternal, dataSourceID)
);

-- TODO CONSTRAINT pointIDFirst must not be equal pointIDSecond
CREATE TABLE distances_between_points (
  pointIDFirst  INTEGER,
  pointIDSecond INTEGER,
  distance      SMALLINT, -- distance between routePoints measured in km.
  PRIMARY KEY (pointIDFirst, pointIDSecond),
  FOREIGN KEY (pointIDFirst) REFERENCES points (pointID)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  FOREIGN KEY (pointIDSecond) REFERENCES points (pointID)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

CREATE TABLE users (
  userID         INTEGER AUTO_INCREMENT,
  userIDExternal VARCHAR(255) NOT NULL,
  dataSourceID   VARCHAR(32)  NOT NULL,
  login          VARCHAR(255) NOT NULL,
  salt           CHAR(16)     NOT NULL,
  passAndSalt    VARCHAR(64)  NOT NULL,
  userRoleID     VARCHAR(32)  NOT NULL,
  userName       VARCHAR(255) NULL,
  phoneNumber    VARCHAR(255) NULL,
  email          VARCHAR(255) NULL,
  position       VARCHAR(255) NULL, -- должность
  pointID        INTEGER      NULL, -- у ADMIN и CLIENT_MANAGER и MARKET_AGENT не может быть пункта, у W_DISPATCHER и DISPATCHER обязан быть пункт
  clientID       INTEGER      NULL, -- у CLIENT_MANAGER обязан быть clientID, у ADMIN W_DISPATCHER DISPATCHER и MARKET_AGENT его быть не должно
  PRIMARY KEY (userID),
  FOREIGN KEY (dataSourceID) REFERENCES data_sources (dataSourceID),
  FOREIGN KEY (userRoleID) REFERENCES user_roles (userRoleID)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  FOREIGN KEY (pointID) REFERENCES points (pointID)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  FOREIGN KEY (clientID) REFERENCES clients (clientID)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  UNIQUE (userIDExternal, dataSourceID),
  UNIQUE (login)
);

CREATE PROCEDURE checkUserConstraints(_userRoleID     VARCHAR(255), _pointID INTEGER, _clientID INTEGER,
                                      _userIDExternal VARCHAR(255), _login VARCHAR(255))
  BEGIN
    IF (_userRoleID IN ('ADMIN', 'CLIENT_MANAGER', 'MARKET_AGENT') AND _pointID IS NOT NULL)
    THEN
      CALL generateLogistError(
          CONCAT('ADMIN, CLIENT_MANAGER OR MARKET_AGENT  can\'t have a point. userIDExternal = ', _userIDExternal,
                 ', login = ', _login));
    END IF;

    IF (_userRoleID IN ('DISPATCHER', 'W_DISPATCHER') AND _pointID IS NULL)
    THEN
      CALL generateLogistError(
          CONCAT('DISPATCHER OR W_DISPATCHER must have a point. userIDExternal = ', _userIDExternal, ', login = ',
                 _login));
    END IF;

    IF (_userRoleID = 'CLIENT_MANAGER' AND _clientID IS NULL)
    THEN
      CALL generateLogistError(
          CONCAT('CLIENT_MANAGER must have clientID. userIDExternal = ', _userIDExternal, ', login = ', _login));
    END IF;

    IF (_userRoleID IN ('DISPATCHER', 'W_DISPATCHER', 'MARKET_AGENT', 'ADMIN') AND _clientID IS NOT NULL)
    THEN
      CALL generateLogistError(
          CONCAT('clientID must be null for userIDExternal = ', _userIDExternal, ', login = ', _login));
    END IF;
  END;

CREATE TRIGGER check_users_constraints_insert BEFORE INSERT ON users
FOR EACH ROW
  CALL checkUserConstraints(NEW.userRoleID, NEW.pointID, NEW.clientID, NEW.userIDExternal, NEW.login);

CREATE TRIGGER check_users_constraints_update BEFORE UPDATE ON users
FOR EACH ROW
  CALL checkUserConstraints(NEW.userRoleID, NEW.pointID, NEW.clientID, NEW.userIDExternal, NEW.login);


CREATE TABLE permissions (
  permissionID VARCHAR(32),
  PRIMARY KEY (permissionID)
);

INSERT INTO permissions (permissionID)
VALUES
  ('updateUserRole'),
  ('updateUserAttributes'),
  ('insertUser'),
  ('deleteUser'),
  ('selectUser'),
  ('insertPoint'),
  ('updatePoint'),
  ('deletePoint'),
  ('selectPoint'),
  ('insertRoute'),
  ('updateRoute'),
  ('deleteRoute'),
  ('selectRoute'),
  ('updateRequestStatus'),
  ('updateRouteListStatus'),
  ('selectOwnHistory');

CREATE TABLE permissions_for_roles (
  userRoleID   VARCHAR(32),
  permissionID VARCHAR(32),
  PRIMARY KEY (userRoleID, permissionID),
  FOREIGN KEY (permissionID) REFERENCES permissions (permissionID)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  FOREIGN KEY (userRoleID) REFERENCES user_roles (userRoleID)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT
);

CREATE PROCEDURE insert_permission_for_role(IN user_role_name VARCHAR(32), IN permission_name VARCHAR(32))
  BEGIN
    INSERT INTO permissions_for_roles (userRoleID, permissionID) SELECT
                                                                   user_roles.userRoleID,
                                                                   permissions.permissionID
                                                                 FROM user_roles, permissions
                                                                 WHERE user_roles.userRoleID = user_role_name AND
                                                                       permissions.permissionID = permission_name;
  END;

-- add all permissions to 'ADMIN'
INSERT INTO permissions_for_roles (userRoleID, permissionID)
  SELECT *
  FROM (SELECT userRoleID
        FROM user_roles
        WHERE userRoleID = 'ADMIN') AS qwe1, (SELECT permissionID
                                              FROM permissions) AS qwe;
-- TODO add permissions to 'WAREHOUSE_MANAGER'
-- TODO add permissions to 'MANAGER'
-- TODO add permissions to 'TEMP_REMOVED'
CALL insert_permission_for_role('CLIENT_MANAGER', 'updateRequestStatus');
CALL insert_permission_for_role('CLIENT_MANAGER', 'selectDataProcedure');

INSERT INTO points (pointIDExternal, dataSourceID, pointName, region, timeZone, docs, comments, openTime, closeTime,
                    district, locality, mailIndex, address, email, phoneNumber, responsiblePersonId, pointTypeID)
VALUES
  ('wle', 'LOGIST_1C', 'point1', 'moscow', 3, 1, 'some_comment1', '9:00:00', '17:00:00', 'some_district', 'efregrthr',
   '123456',
   'ergersghrth', 'srgf@ewuf.ru', '89032343556', 'resp_personID1', 'WAREHOUSE');

INSERT INTO users (userID, userIDExternal, dataSourceID, login, salt, passAndSalt, userRoleID, userName, phoneNumber, email, position, pointID, clientID)
VALUES
  (1, 'eebrfiebreiubritbvritubvriutbv', 'ADMIN_PAGE', 'parser', 'nvuritneg4785231',
      md5(CONCAT(md5('nolpitf43gwer'), 'nvuritneg4785231')),
      'ADMIN', 'parser', '', 'fff@fff', '', NULL, NULL),
  (2, 'eebrfiebreiubrrervritubvriutbv', 'ADMIN_PAGE', 'test', 'nvuritneg4785231',
      md5(CONCAT(md5('test'), 'nvuritneg4785231')),
      'ADMIN', 'ivanov i.i.', '904534356', 'test@test.ru', 'position', NULL, NULL);

-- -------------------------------------------------------------------------------------------------------------------
--                                          ROUTE AND ROUTE LISTS
-- -------------------------------------------------------------------------------------------------------------------


CREATE TABLE tariffs (
  tariffID       INTEGER AUTO_INCREMENT,
  cost           DECIMAL(12, 2) NULL, -- цена доставки
  cost_per_point DECIMAL(12, 2) NULL, -- цена за точку
  cost_per_hour  DECIMAL(12, 2) NULL, -- цена за час
  capacity       DECIMAL(4, 2)  NULL, -- грузоподъёмность в тоннах
  carrier        VARCHAR(64), -- перевозчик
  PRIMARY KEY (tariffID)
);

-- zero time is 00:00(GMT) of that day, when carrier arrives at first point of route.
CREATE TABLE routes (
  routeID               INTEGER AUTO_INCREMENT,
  directionIDExternal   VARCHAR(255)              NOT NULL,
  dataSourceID          VARCHAR(32)               NOT NULL,
  routeName             VARCHAR(255)              NOT NULL,
  firstPointArrivalTime TIME DEFAULT '00:00:00'   NOT NULL,
  daysOfWeek            SET ('monday',
                             'tuesday',
                             'wednesday',
                             'thursday',
                             'friday',
                             'saturday',
                             'sunday') DEFAULT '' NOT NULL,
  tariffID              INTEGER                   NULL,
  PRIMARY KEY (routeID),
  FOREIGN KEY (tariffID) REFERENCES tariffs (tariffID)
    ON DELETE SET NULL
    ON UPDATE CASCADE,
  FOREIGN KEY (dataSourceID) REFERENCES data_sources (dataSourceID),
  UNIQUE (routeName),
  UNIQUE (directionIDExternal, dataSourceID)
);

CREATE TABLE route_list_statuses (
  routeListStatusID      VARCHAR(32),
  routeListStatusRusName VARCHAR(255),
  PRIMARY KEY (routeListStatusID)
);

INSERT INTO route_list_statuses
VALUES
  ('CREATED', 'Маршрутный лист создается'),
  ('APPROVED', 'Формирование маршрутного листа завершено');

CREATE TABLE route_points (
  routePointID             INTEGER AUTO_INCREMENT,
  sortOrder                INTEGER NOT NULL,
  timeForLoadingOperations INTEGER NOT NULL, -- time in minutes
  pointID                  INTEGER NOT NULL,
  routeID                  INTEGER NOT NULL,
  PRIMARY KEY (routePointID),
  FOREIGN KEY (pointID) REFERENCES points (pointID)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  FOREIGN KEY (routeID) REFERENCES routes (routeID)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  UNIQUE (routeID, sortOrder)
);


CREATE PROCEDURE check_route_points_constraints(_routeID INTEGER, _sortOrder INTEGER, _pointID INTEGER)
  BEGIN
    -- получить пункт сверху и снизу
    SET @nextPointID = (SELECT pointID
                        FROM route_points
                        WHERE route_points.routeID = _routeID AND route_points.sortOrder > _sortOrder
                        ORDER BY sortOrder ASC
                        LIMIT 1);
    SET @previousPointID = (SELECT pointID
                            FROM route_points
                            WHERE route_points.routeID = _routeID AND route_points.sortOrder < _sortOrder
                            ORDER BY sortOrder DESC
                            LIMIT 1);
    IF ((@nextPointID IS NOT NULL AND @previousPointID IS NOT NULL AND
         ((_pointID = @nextPointID) OR (_pointID = @previousPointID))) OR
        (@nextPointID IS NULL AND (_pointID = @previousPointID)) OR
        (@previousPointID IS NULL AND (_pointID = @nextPointID)))
    THEN
      CALL generateLogistError('can\'t add new route point because same neighbors');
    END IF;
  END;

-- CONSTRAINT routePointIDFirst not equal routePointIDSecond
CREATE TRIGGER before_route_points_insert BEFORE INSERT ON route_points
FOR EACH ROW
  CALL check_route_points_constraints(NEW.routeID, NEW.sortOrder, NEW.pointID);
-- CONSTRAINT routePointIDFirst not equal routePointIDSecond
CREATE TRIGGER before_route_points_delete BEFORE DELETE ON route_points
FOR EACH ROW
  CALL check_route_points_constraints(OLD.routeID, OLD.sortOrder, OLD.pointID);


CREATE TRIGGER before_route_points_update BEFORE UPDATE ON route_points
FOR EACH ROW
  BEGIN
    CALL generateLogistError('updates on route_points disabled');
  END;

CREATE TABLE relations_between_route_points (
  routePointIDFirst  INTEGER,
  routePointIDSecond INTEGER,
  timeForDistance    INTEGER, -- time in minutes for carrier drive through distance
  PRIMARY KEY (routePointIDFirst, routePointIDSecond),
  FOREIGN KEY (routePointIDFirst) REFERENCES route_points (routePointID)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  FOREIGN KEY (routePointIDSecond) REFERENCES route_points (routePointID)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

-- ВНИМАНИЕ! при изменении в этой таблице нужно согласовать все с триггерами и таблицей route_list_history
CREATE TABLE route_lists (
  routeListID         INTEGER AUTO_INCREMENT,
  routeListIDExternal VARCHAR(255) NOT NULL,
  dataSourceID        VARCHAR(32)  NOT NULL,
  routeListNumber     VARCHAR(255) NOT NULL,
  creationDate        DATE         NULL,
  departureDate       DATE         NULL,
  palletsQty          INTEGER      NULL,
  forwarderId         VARCHAR(255) NULL,
  driverID            INTEGER      NULL,
  driverPhoneNumber   VARCHAR(255) NULL,
  licensePlate        VARCHAR(255) NULL, -- государственный номер автомобиля
  status              VARCHAR(32)  NOT NULL,
  routeID             INTEGER      NOT NULL,
  PRIMARY KEY (routeListID),
  FOREIGN KEY (status) REFERENCES route_list_statuses (routeListStatusID)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  FOREIGN KEY (routeID) REFERENCES routes (routeID)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  FOREIGN KEY (driverID) REFERENCES users (userID)
    ON DELETE SET NULL
    ON UPDATE CASCADE,
  UNIQUE (routeListIDExternal, dataSourceID)
);


CREATE TABLE route_list_history (
  routeListHistoryID  BIGINT AUTO_INCREMENT,
  timeMark            DATETIME                               NOT NULL,
  routeListID         INTEGER                                NOT NULL,
  routeListIDExternal VARCHAR(255)                           NOT NULL,
  dataSourceID        VARCHAR(32)                            NOT NULL,
  routeListNumber     VARCHAR(255)                           NOT NULL,
  creationDate        DATE                                   NULL,
  departureDate       DATE                                   NULL,
  palletsQty          INTEGER                                NULL,
  forwarderId         VARCHAR(255)                           NULL,
  driverId            VARCHAR(255)                           NULL,
  driverPhoneNumber   VARCHAR(255)                           NULL,
  licensePlate        VARCHAR(255)                           NULL, -- государственный номер автомобиля
  status              VARCHAR(32)                            NOT NULL,
  routeID             INTEGER                                NOT NULL,
  dutyStatus          ENUM ('CREATED', 'UPDATED', 'DELETED') NOT NULL,
  PRIMARY KEY (routeListHistoryID),
  FOREIGN KEY (status) REFERENCES route_list_statuses (routeListStatusID)
    ON DELETE RESTRICT
    ON UPDATE CASCADE
);

-- -------------------------------------------------------------------------------------------------------------------
--                                                REQUESTS
-- -------------------------------------------------------------------------------------------------------------------


CREATE TABLE request_statuses (
  requestStatusID      VARCHAR(32),
  requestStatusRusName VARCHAR(128),
  sequence             TINYINT,
  PRIMARY KEY (requestStatusID)
);

INSERT INTO request_statuses
VALUES
  -- duty statuses
  ('CREATED', 'заявка добавлена в БД', 0),
  ('UPDATED', 'заявка обновлена из 1С', -2),
  ('DELETED', 'заявка удалена из БД', -1),
  -- insider request statuses
  ('UNKNOWN', 'неизвестный статус', 1),
  ('SAVED', 'Заявка в состоянии черновика', 1),
  ('APPROVING', 'Выгружена на утверждение торговому представителю', 2),
  ('RESERVED', 'Резерв', 3),
  ('APPROVED', 'Утверждена к сборке', 4),
  ('STOP_LIST', 'Стоп-лист', -2),
  ('CREDIT_LIMIT', 'Кредитный лимит', -3),
  ('RASH_CREATED', 'Создана расходная накладная', 5),
  ('COLLECTING', 'Выдана на сборку', 6),
  ('CHECK', 'На контроле', 7),
  ('CHECK_PASSED', 'Контроль пройден', 8),
  ('ADJUSTMENTS_MADE', 'Сделаны коргыукытировки\распечатаны документы', 9),
  ('PACKAGING', 'Упаковано', 10),
  ('CHECK_BOXES', 'Проверка коробок в зоне отгрузки', 11),
  ('READY', 'Проверка в зоне отгрузки/Готова к отправке', 12),
  ('TRANSPORTATION', 'Маршрутный лист закрыт, товар передан экспедитору на погрузку', 13),
  -- invoice statuses
  ('DEPARTURE', 'В транзите', 14),
  ('ARRIVED', 'Накладная прибыла в пункт', 15),
  ('ERROR', 'Ошибка. Возвращение в пункт', -4),
  ('DELIVERED', 'Доставлено', 16);

CREATE TABLE request_statuses_for_user_role (
  userRoleID      VARCHAR(32),
  requestStatusID VARCHAR(32),
  PRIMARY KEY (userRoleID, requestStatusID),
  FOREIGN KEY (userRoleID) REFERENCES user_roles (userRoleID),
  FOREIGN KEY (requestStatusID) REFERENCES request_statuses (requestStatusID)
);

INSERT INTO request_statuses_for_user_role
VALUES
  ('ADMIN', 'APPROVING'),
  ('ADMIN', 'RESERVED'),
  ('ADMIN', 'APPROVED'),
  ('ADMIN', 'STOP_LIST'),
  ('ADMIN', 'READY'),
  ('ADMIN', 'DEPARTURE'),
  ('ADMIN', 'ERROR'),
  ('ADMIN', 'ARRIVED'),
  ('W_DISPATCHER', 'DEPARTURE'),
  ('DISPATCHER', 'DEPARTURE'),
  ('DISPATCHER', 'ARRIVED'),
  ('DISPATCHER', 'ERROR'),
  ('DISPATCHER', 'DELIVERED'),
  ('MARKET_AGENT', 'ERROR'),
  ('MARKET_AGENT', 'DELIVERED'),
  ('CLIENT_MANAGER', 'ERROR'),
  ('CLIENT_MANAGER', 'DELIVERED');

-- request объеденяет в себе внутреннюю заявку и накладную,
-- при создании request мы сразу делаем ссылку на пункт типа склад. участки склада не участвуют в нашей модели.
-- ВНИМАНИЕ! при изменении порядка полей их также нужно менять в триггерах и в таблице requests_history!!!
CREATE TABLE requests (

  -- JSON fields
  requestID               INTEGER AUTO_INCREMENT,
  requestIDExternal       VARCHAR(255)   NOT NULL,
  dataSourceID            VARCHAR(32)    NOT NULL,
  requestNumber           VARCHAR(255)   NOT NULL,
  requestDate             DATE           NOT NULL, -- дата заявки создаваемой клиентом или торговым представителем
  clientID                INTEGER        NOT NULL,
  destinationPointID      INTEGER        NULL, -- пункт доставки (addressId)
  marketAgentUserID       INTEGER        NOT NULL, -- traderId
  invoiceNumber           VARCHAR(255)   NOT NULL,
  invoiceDate             DATE           NULL,
  documentNumber          VARCHAR(255)   NOT NULL,
  documentDate            DATE           NULL,
  firma                   VARCHAR(255)   NULL,
  storage                 VARCHAR(255)   NULL, -- наименование склада на котором собиралась накладная
  contactName             VARCHAR(255)   NULL,
  contactPhone            VARCHAR(255)   NULL,
  deliveryOption          VARCHAR(255)   NULL, -- вариант доставки (с пересчетом/без пересчета/самовывоз/доставка ТП)
  deliveryDate            DATETIME       NULL,
  boxQty                  INTEGER        NULL,

  -- logist fields
  weight                  INTEGER        NULL, -- масса в граммах
  volume                  INTEGER        NULL, -- в кубических сантиметрах
  goodsCost               DECIMAL(12, 2) NULL, -- цена всех товаров в заявке
  lastStatusUpdated       DATETIME       NULL, -- date and time when status was updated by any user
  -- TODO это поле нужно перенести в users, саму таблицу users разбить на три таблицы из-за пункта.
  lastModifiedBy          INTEGER        NULL, -- один из пользователей - это parser.
  requestStatusID         VARCHAR(32)    NOT NULL,
  commentForStatus        TEXT           NULL,
  warehousePointID        INTEGER        NULL,
  routeListID             INTEGER        NULL, -- может быть NULL до тех пор пока не создан маршрутный лист
  lastVisitedRoutePointID INTEGER        NULL, -- может быть NULL до тех пор пока не создан маршрутный лист
  hoursAmount             INTEGER        NULL, -- Фактическое кол-во часов, может быть NULL, пока статус не "Доставлено"
  transportCompanyId      INTEGER        NULL, -- Транспортная компания
  vehicleId               INTEGER        NULL, -- Транспортное средство
  driverId                INTEGER        NULL,  -- Водитель

  PRIMARY KEY (requestID),
  FOREIGN KEY (dataSourceID) REFERENCES data_sources (dataSourceID),
  FOREIGN KEY (marketAgentUserID) REFERENCES users (userID)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  FOREIGN KEY (clientID) REFERENCES clients (clientID)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  FOREIGN KEY (destinationPointID) REFERENCES points (pointID)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  FOREIGN KEY (lastModifiedBy) REFERENCES users (userID)
    ON DELETE SET NULL
    ON UPDATE CASCADE,
  FOREIGN KEY (requestStatusID) REFERENCES request_statuses (requestStatusID)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  FOREIGN KEY (warehousePointID) REFERENCES points (pointID)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  FOREIGN KEY (routeListID) REFERENCES route_lists (routeListID)
    ON DELETE SET NULL
    ON UPDATE CASCADE,
  FOREIGN KEY (lastVisitedRoutePointID) REFERENCES route_points (routePointID)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  UNIQUE (requestIDExternal, dataSourceID)
);

# CREATE TRIGGER check_for_market_agents_and_warehouse_point BEFORE INSERT ON requests
# FOR EACH ROW
#   BEGIN
#
#     (SELECT
#        userRoleID,
#        login
#      INTO @userRoleID, @login
#      FROM users
#      WHERE userID = NEW.marketAgentUserID);
#
#     IF (@userRoleID <> 'MARKET_AGENT')
#     THEN
#       CALL generateLogistError(CONCAT('Can\'t insert row: only MARKET_AGENT users allowed for login = ', @login));
#     END IF;
#
#     SET @pointType = (SELECT pointTypeID
#                       FROM points
#                       WHERE pointID = NEW.warehousePointID);
#     IF (@pointType IS NOT NULL)
#     THEN
#       BEGIN
#         IF (@pointType <> 'WAREHOUSE')
#         THEN
#           CALL generateLogistError('Can\'t insert row: only WAREHOUSE points allowed');
#         END IF;
#       END;
#     END IF;
#
#   END;




CREATE TABLE requests_history (
  requstHistoryID         BIGINT AUTO_INCREMENT,
  autoTimeMark            DATETIME       NOT NULL,

  -- JSON fields
  requestID               INTEGER,
  requestIDExternal       VARCHAR(255)   NOT NULL,
  dataSourceID            VARCHAR(32)    NOT NULL,
  requestNumber           VARCHAR(255)   NOT NULL,
  requestDate             DATE           NOT NULL, -- дата заявки создаваемой клиентом или торговым представителем
  clientID                INTEGER        NOT NULL,
  destinationPointID      INTEGER        NULL, -- пункт доставки (addressId)
  marketAgentUserID       INTEGER        NOT NULL, -- traderId
  invoiceNumber           VARCHAR(255)   NOT NULL,
  invoiceDate             DATE           NULL,
  documentNumber          VARCHAR(255)   NOT NULL,
  documentDate            DATE           NULL,
  firma                   VARCHAR(255)   NULL,
  storage                 VARCHAR(255)   NULL, -- наименование склада на котором собиралась накладная
  contactName             VARCHAR(255)   NULL,
  contactPhone            VARCHAR(255)   NULL,
  deliveryOption          VARCHAR(255)   NULL, -- вариант доставки (с пересчетом/без пересчета/самовывоз/доставка ТП)
  deliveryDate            DATETIME       NULL,
  boxQty                  INTEGER        NULL,

  -- logist fields
  weight                  INTEGER        NULL, -- масса в граммах
  volume                  INTEGER        NULL, -- в кубических сантиметрах
  goodsCost               DECIMAL(12, 2) NULL, -- цена всех товаров в накладной
  lastStatusUpdated       DATETIME       NULL, -- date and time when status was updated by any user
  lastModifiedBy          INTEGER        NULL, -- один из пользователей - это parser.
  requestStatusID         VARCHAR(32)    NOT NULL,
  commentForStatus        TEXT           NULL,
  warehousePointID        INTEGER        NULL,
  routeListID             INTEGER        NULL, -- может быть NULL до тех пор пока не создан маршрутный лист
  lastVisitedRoutePointID INTEGER        NULL, -- может быть NULL до тех пор пока не создан маршрутный лист

  PRIMARY KEY (requstHistoryID),
  FOREIGN KEY (requestStatusID) REFERENCES request_statuses (requestStatusID)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT
);

-- pretensions
CREATE TABLE pretensions
(
  pretensionID        INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  pretensionComment   TEXT,
  requestIDExternal   VARCHAR(255),
  pretensionStatus    VARCHAR(32),
  pretensionCathegory VARCHAR(32),
  sum                 DECIMAL(10, 2),
  positionNumber      TEXT,
  dateAdded           DATE,
  CONSTRAINT pretensions_ibfk_3 FOREIGN KEY (requestIDExternal) REFERENCES requests (requestIDExternal)
);
CREATE INDEX pretensions_ibfk_3 ON pretensions (requestIDExternal);

-- -------------------------------------------------------------------------------------------------------------------
--                                                   GETTERS
-- -------------------------------------------------------------------------------------------------------------------


CREATE FUNCTION getRoleIDByUserID(_userID INTEGER)
  RETURNS VARCHAR(32)
  BEGIN
    DECLARE result VARCHAR(32);
    SET result = (SELECT userRoleID
                  FROM users
                  WHERE userID = _userID);
    RETURN result;
  END;

CREATE FUNCTION getPointIDByUserID(_userID INTEGER)
  RETURNS INTEGER
  BEGIN
    DECLARE result INTEGER;
    SET result = (SELECT pointID
                  FROM users
                  WHERE userID = _userID);
    RETURN result;
  END;

CREATE FUNCTION getClientIDByUserID(_userID INTEGER)
  RETURNS INTEGER
  BEGIN
    DECLARE result INTEGER;
    SET result = (SELECT clientID
                  FROM users
                  WHERE userID = _userID);
    RETURN result;
  END;

-- находит все routeID, которые содержат указанный пункт
CREATE FUNCTION selectAllRoutesIDWithThatPointAsString(_pointID INTEGER)
  RETURNS TEXT
  BEGIN
    SET @result = (SELECT GROUP_CONCAT(DISTINCT routes.routeID)
                   FROM routes
                     INNER JOIN (route_points, points)
                       ON (routes.routeID = route_points.routeID AND route_points.pointID = points.pointID)
                   WHERE _pointID = points.pointID);
    RETURN @result;
  END;

-- -------------------------------------------------------------------------------------------------------------------
--                                                BIG SELECT
-- -------------------------------------------------------------------------------------------------------------------


CREATE FUNCTION splitString(stringSpl TEXT, delim VARCHAR(12), pos INT)
  RETURNS TEXT
  RETURN REPLACE(SUBSTRING(SUBSTRING_INDEX(stringSpl, delim, pos),
                           CHAR_LENGTH(SUBSTRING_INDEX(stringSpl, delim, pos - 1)) + 1),
                 delim, '');


CREATE FUNCTION generateLikeCondition(map TEXT)
  RETURNS TEXT
  BEGIN

    DECLARE i INT DEFAULT 0;
    DECLARE pair VARCHAR(255) DEFAULT '----';
    DECLARE result TEXT DEFAULT '';

    IF (map = '')
    THEN RETURN 'TRUE';
    END IF;

    wloop: WHILE (TRUE) DO
      SET i = i + 1;
      SET pair = splitString(map, ';', i);
      IF pair = ''
      THEN LEAVE wloop;
      END IF;
      SET @columnName = splitString(pair, ',', 1);
      SET @searchString = splitString(pair, ',', 2);
      SET @searchString = CONCAT('%', @searchString, '%');

      SET result = CONCAT(result, @columnName, ' LIKE ', '\'', @searchString, '\'', ' AND ');
    END WHILE;

    -- remove redundant END
    SET result = SUBSTR(result, 1, CHAR_LENGTH(result) - 4);
    SET result = CONCAT('(', result, ')');

    RETURN result;
  END;


CREATE FUNCTION generateOrderByPart(_orderby VARCHAR(255), _isDesc BOOLEAN)
  RETURNS VARCHAR(255)
  BEGIN

    IF (_orderby = '')
    THEN RETURN '';
    END IF;

    IF (_isDesc)
    THEN
      RETURN CONCAT(' ORDER BY ', _orderby, ' DESC');
    ELSE
      RETURN CONCAT(' ORDER BY ', _orderby);
    END IF;

  END;

-- _search - array of strings
-- _orderby 'id'  <> - название колонки из файла main.js
-- _search - передача column_name1,search_string1;column_name1,search_string1;... если ничего нет то передавать пустую строку

CREATE PROCEDURE selectData(_userID INTEGER, _startEntry INTEGER, _length INTEGER, _orderby VARCHAR(255),
                            _isDesc BOOLEAN, _search TEXT)
  BEGIN

    SET @userRoleID = getRoleIDByUserID(_userID);

    SET @isAdmin = FALSE;
    SET @isMarketAgent = FALSE;
    SET @isClientManager = FALSE;
    SET @isDispatcherOrWDispatcher = FALSE;

    IF (@userRoleID = 'ADMIN')
    THEN
      SET @isAdmin = TRUE;
    ELSEIF (@userRoleID = 'MARKET_AGENT')
      THEN
        SET @isMarketAgent = TRUE;
    ELSEIF (@userRoleID = 'CLIENT_MANAGER')
      THEN
        SET @isClientManager = TRUE;
    ELSEIF (@userRoleID = 'DISPATCHER' OR @userRoleID = 'W_DISPATCHER')
      THEN
        SET @isDispatcherOrWDispatcher = TRUE;
    END IF;

    IF (@isClientManager)
    THEN
      SET @clientID = getClientIDByUserID(_userID);
    END IF;

    IF (@isDispatcherOrWDispatcher)
    THEN
      SET @userPointID = getPointIDByUserID(_userID);
      SET @allRoutesWithUserPointID = selectAllRoutesIDWithThatPointAsString(@userPointID);
    END IF;

    -- 1) если у пользователя роль админ, то показываем все записи из БД
    -- 2) если статус пользователя - агент, то показываем ему только те заявки которые он породил.
    -- 3) если пользователь находится на складе, на котором формируется заявка, то показываем ему эти записи
    -- 4) если маршрут накладной проходит через пользователя, то показываем ему эти записи
    SET @columnsPart =
    '
    SELECT SQL_CALC_FOUND_ROWS
      requestIDExternal,
      requestNumber,
      requestDate,
      invoiceNumber,
      invoiceDate,
      documentNumber,
      documentDate,
      firma,
      storage,
      boxQty,
      requestStatusID,
      commentForStatus,
      requestStatusRusName,
      clientIDExternal,
      INN,
      clientName,
      marketAgentUserName,
      driverUserName,
      deliveryPointName,
      warehousePointName,
      lastVisitedPointName,
      nextPointName,
      routeListNumber,
      licensePlate,
      palletsQty,
      routeListID,
      routeName,
      arrivalTimeToNextRoutePoint
    FROM mat_view_big_select
    ';

    SET @wherePart = '';

    IF @isAdmin
    THEN
      SET @wherePart = CONCAT('WHERE ', generateLikeCondition(_search));
    ELSEIF @isMarketAgent
      THEN
        SET @wherePart = CONCAT('WHERE (marketAgentUserID = ', _userID, ') AND ', generateLikeCondition(_search));
    ELSEIF @isClientManager
      THEN
        SET @wherePart = CONCAT('WHERE (clientID = ', @clientID, ') AND ', generateLikeCondition(_search));
    ELSEIF @isDispatcherOrWDispatcher
      THEN
        SET @wherePart = CONCAT('WHERE (routeID IN (', @allRoutesWithUserPointID, ')) AND ',
                                generateLikeCondition(_search));
    END IF;

    SET @orderByPart = generateOrderByPart(_orderby, _isDesc);

    SET @limitPart = ' LIMIT ?, ? ';

    SET @sqlString = CONCAT(@columnsPart, @wherePart, @orderByPart, @limitPart);

    PREPARE getDataStm FROM @sqlString;

    SET @_startEntry = _startEntry;
    SET @_length = _length;

    EXECUTE getDataStm
    USING @_startEntry, @_length;
    DEALLOCATE PREPARE getDataStm;

    -- filtered в случае, если присутвуют фильтры, то возвращается всегда -1.
    -- SELECT 200*40 as `totalFiltered`;

    SELECT FOUND_ROWS() AS `totalFiltered`;

    -- total
    IF (@isAdmin OR @isMarketAgent OR @isClientManager)
    THEN
      BEGIN
        SELECT total_count AS totalCount
        FROM mat_view_row_count_for_user
        WHERE mat_view_row_count_for_user.userID = _userID;
      END;
    ELSEIF (@isDispatcherOrWDispatcher)
      THEN
        SET @countTotalSql = CONCAT('SELECT COUNT(*) as `totalCount` FROM mat_view_big_select ',
                                    CONCAT('WHERE (routeID IN (', @allRoutesWithUserPointID, '))'));
        PREPARE getTotalStm FROM @countTotalSql;
        EXECUTE getTotalStm;
        DEALLOCATE PREPARE getTotalStm;
    END IF;

  END;

-- used in pretensionWindow
CREATE PROCEDURE `selectDataByClientIdAndInvoiceNumber`(_userID INTEGER, _clientID TEXT, _invoiceNumber TEXT)
  BEGIN

    SET @userRoleID = getRoleIDByUserID(_userID);

    SET @isAdmin = FALSE;
    SET @isMarketAgent = FALSE;
    SET @isClientManager = FALSE;
    SET @isDispatcherOrWDispatcher = FALSE;

    IF (@userRoleID = 'ADMIN')
    THEN
      SET @isAdmin = TRUE;
    ELSEIF (@userRoleID = 'MARKET_AGENT')
      THEN
        SET @isMarketAgent = TRUE;
    ELSEIF (@userRoleID = 'CLIENT_MANAGER')
      THEN
        SET @isClientManager = TRUE;
    ELSEIF (@userRoleID = 'DISPATCHER' OR @userRoleID = 'W_DISPATCHER')
      THEN
        SET @isDispatcherOrWDispatcher = TRUE;
    END IF;

    IF (@isClientManager)
    THEN
      SET @clientID = getClientIDByUserID(_userID);
    END IF;

    IF (@isDispatcherOrWDispatcher)
    THEN
      SET @userPointID = getPointIDByUserID(_userID);
      SET @allRoutesWithUserPointID = selectAllRoutesIDWithThatPointAsString(@userPointID);
    END IF;

    -- 1) если у пользователя роль админ, то показываем все записи из БД
    -- 2) если статус пользователя - агент, то показываем ему только те заявки которые он породил.
    -- 3) если пользователь находится на складе, на котором формируется заявка, то показываем ему эти записи
    -- 4) если маршрут накладной проходит через пользователя, то показываем ему эти записи
    SET @columnsPart =
    '
    SELECT SQL_CALC_FOUND_ROWS
      requestIDExternal,
      requestNumber,
      requestDate,
      invoiceNumber,
      invoiceDate,
      documentNumber,
      documentDate,
      firma,
      storage,
      boxQty,
      requestStatusID,
      commentForStatus,
      requestStatusRusName,
      clientIDExternal,
      INN,
      clientName,
      marketAgentUserName,
      driverUserName,
      deliveryPointName,
      warehousePointName,
      lastVisitedPointName,
      nextPointName,
      routeListNumber,
      licensePlate,
      palletsQty,
      routeListID,
      routeName,
      arrivalTimeToNextRoutePoint
    FROM mat_view_big_select
    ';

    SET @wherePart = '';

    IF @isAdmin
    THEN
      SET @wherePart = CONCAT('WHERE (clientIDExternal = "', _clientID, '" AND invoiceNumber = "', _invoiceNumber,
                              '")');
    ELSEIF @isMarketAgent
      THEN
        SET @wherePart = CONCAT('WHERE (marketAgentUserID = "', _userID, '") AND clientIDExternal = "', _clientID,
                                '" AND invoiceNumber = "', _invoiceNumber, '"');
    ELSEIF @isClientManager
      THEN
        SET @wherePart = CONCAT('WHERE (clientID = ', @clientID, ') AND  clientIDExternal = "', _clientID,
                                '" AND invoiceNumber = "', _invoiceNumber, '"');
    ELSEIF @isDispatcherOrWDispatcher
      THEN
        SET @wherePart = CONCAT('WHERE (routeID IN (', @allRoutesWithUserPointID, ')) AND  clientIDExternal = "',
                                _clientID, '" AND invoiceNumber = "', _invoiceNumber, '"');
    END IF;

    SET @limitPart = ' LIMIT 1';

    SET @sqlString = CONCAT(@columnsPart, @wherePart, @limitPart);

    PREPARE getDataStm FROM @sqlString;

    EXECUTE getDataStm;
    DEALLOCATE PREPARE getDataStm;


  END;

-- -------------------------------------------------------------------------------------------------------------------
--                                                OPTIMIZATION TABLES AND PROCEDURES
-- -------------------------------------------------------------------------------------------------------------------

--  таблица обновляется через триггеры на таблице requests и через запрос к таблице mat_view_route_points_sequential внутри этих триггеров

CREATE TABLE mat_view_arrival_time_for_request (
  requestID                   INTEGER,
  arrivalTimeToNextRoutePoint DATETIME,
  PRIMARY KEY (requestID),
  FOREIGN KEY (requestID) REFERENCES requests (requestID)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

-- таблица содержащая размеры массива данных с заявками
CREATE TABLE mat_view_row_count_for_user (
  userID      INTEGER,
  userRole    VARCHAR(32),
  total_count INTEGER,
  PRIMARY KEY (userID)
);

-- таблица должна обновляться за счте триггеров при изменении в таблицах requests, route_points, relations_between_route_points
-- clients - обновлять clientName, INN,
-- users - userName
-- points - pointName
-- route_lists - routeListNumber, licensePlate, palletsQty
-- routes - routeName
--
CREATE TABLE mat_view_big_select (
  requestID                   INTEGER,
  requestIDExternal           VARCHAR(255),
  requestNumber               VARCHAR(255),
  requestDate                 DATETIME,
  invoiceNumber               VARCHAR(255),
  invoiceDate                 DATETIME,
  documentNumber              VARCHAR(255),
  documentDate                DATETIME,
  firma                       VARCHAR(255),
  storage                     VARCHAR(255),
  boxQty                      INTEGER,
  requestStatusID             VARCHAR(32),
  commentForStatus            TEXT,
  requestStatusRusName        VARCHAR(255),
  clientID                    INTEGER, -- id field
  clientIDExternal            VARCHAR(255),
  INN                         VARCHAR(255),
  clientName                  VARCHAR(255),
  marketAgentUserID           INTEGER, -- id field
  marketAgentUserName         VARCHAR(255),
  driverUserID                INTEGER, -- id field
  driverUserName              VARCHAR(255),
  deliveryPointID             INTEGER, -- id field
  deliveryPointName           VARCHAR(255),
  warehousePointID            INTEGER, -- id field
  warehousePointName          VARCHAR(255),
  lastVisitedPointID          INTEGER, -- id field
  lastVisitedPointName        VARCHAR(255),
  nextPointID                 INTEGER, -- id field
  nextPointName               VARCHAR(255),
  routeListNumber             VARCHAR(255),
  licensePlate                VARCHAR(255),
  palletsQty                  INTEGER,
  routeListID                 INTEGER, -- id field
  routeID                     INTEGER, -- id field
  routeName                   VARCHAR(255),
  arrivalTimeToNextRoutePoint DATETIME,
  PRIMARY KEY (requestID),
  FOREIGN KEY (requestID) REFERENCES requests (requestID)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

CREATE PROCEDURE singleInsertOrUpdateOnMatViewBigSelect(_requestID INTEGER)
  BEGIN
    INSERT INTO mat_view_big_select
      SELECT
        _requestID                    AS requestID,
        requests.requestIDExternal,
        requests.requestNumber,
        requests.requestDate,
        requests.invoiceNumber,
        requests.invoiceDate,
        requests.documentNumber,
        requests.documentDate,
        requests.firma,
        requests.storage,
        requests.boxQty,
        requests.requestStatusID,
        requests.commentForStatus,
        request_statuses.requestStatusRusName,
        clients.clientID,
        clients.clientIDExternal,
        clients.INN,
        clients.clientName,
        market_agent_users.userID     AS marketAgentUserID,
        market_agent_users.userName   AS marketAgentUserName,
        driver_users.userID           AS driverUserID,
        driver_users.userName         AS driverUserName,
        delivery_points.pointID       AS deliveryPointID,
        delivery_points.pointName     AS deliveryPointName,
        w_points.pointID              AS warehousePointID,
        w_points.pointName            AS warehousePointName,
        last_visited_points.pointID   AS lastVisitedPointID,
        last_visited_points.pointName AS lastVisitedPointName,
        mat_view_route_points_sequential.nextPointID,
        mat_view_route_points_sequential.nextPointName,
        route_lists.routeListNumber,
        route_lists.licensePlate,
        route_lists.palletsQty,
        route_lists.routeListID,
        routes.routeID,
        routes.routeName,
        mat_view_arrival_time_for_request.arrivalTimeToNextRoutePoint

      FROM requests
        INNER JOIN (request_statuses) USING (requestStatusID)
        INNER JOIN (clients) USING (clientID)
        INNER JOIN (users AS market_agent_users) ON (requests.marketAgentUserID = market_agent_users.userID)
        LEFT JOIN (route_lists) USING (routeListID)
        LEFT JOIN (users AS driver_users) ON (route_lists.driverID = driver_users.userID)
        LEFT JOIN (routes) ON (route_lists.routeID = routes.routeID)
        LEFT JOIN (route_points AS last_visited_route_points)
          ON (requests.lastVisitedRoutePointID = last_visited_route_points.routePointID)
        LEFT JOIN (mat_view_route_points_sequential)
          ON (requests.lastVisitedRoutePointID = mat_view_route_points_sequential.routePointID)
        LEFT JOIN (points AS delivery_points) ON (requests.destinationPointID = delivery_points.pointID)
        LEFT JOIN (points AS w_points) ON (requests.warehousePointID = w_points.pointID)
        LEFT JOIN (points AS last_visited_points) ON (last_visited_route_points.pointID = last_visited_points.pointID)
        LEFT JOIN (mat_view_arrival_time_for_request)
          ON (requests.requestID = mat_view_arrival_time_for_request.requestID)
      WHERE requests.requestID = _requestID

    ON DUPLICATE KEY UPDATE
      requestIDExternal           = VALUES(requestIDExternal),
      requestNumber               = VALUES(requestNumber),
      requestDate                 = VALUES(requestDate),
      invoiceNumber               = VALUES(invoiceNumber),
      invoiceDate                 = VALUES(invoiceDate),
      documentNumber              = VALUES(documentNumber),
      documentDate                = VALUES(documentDate),
      firma                       = VALUES(firma),
      storage                     = VALUES(storage),
      boxQty                      = VALUES(boxQty),
      requestStatusID             = VALUES(requestStatusID),
      commentForStatus            = VALUES(commentForStatus),
      requestStatusRusName        = VALUES(requestStatusRusName),
      clientID                    = VALUES(clientID),
      clientIDExternal            = VALUES(clientIDExternal),
      INN                         = VALUES(INN),
      clientName                  = VALUES(clientName),
      marketAgentUserID           = VALUES(marketAgentUserID),
      marketAgentUserName         = VALUES(marketAgentUserName),
      driverUserID                = VALUES(driverUserID),
      driverUserName              = VALUES(driverUserName),
      deliveryPointID             = VALUES(deliveryPointID),
      deliveryPointName           = VALUES(deliveryPointName),
      warehousePointID            = VALUES(warehousePointID),
      warehousePointName          = VALUES(warehousePointName),
      lastVisitedPointID          = VALUES(lastVisitedPointID),
      lastVisitedPointName        = VALUES(lastVisitedPointName),
      nextPointID                 = VALUES(nextPointID),
      nextPointName               = VALUES(nextPointName),
      routeListNumber             = VALUES(routeListNumber),
      licensePlate                = VALUES(licensePlate),
      palletsQty                  = VALUES(palletsQty),
      routeListID                 = VALUES(routeListID),
      routeID                     = VALUES(routeID),
      routeName                   = VALUES(routeName),
      arrivalTimeToNextRoutePoint = VALUES(arrivalTimeToNextRoutePoint);
  END;

-- таблица используется для оптимизации запроса big select
CREATE TABLE mat_view_route_points_sequential (
  routeID          INTEGER,
  routePointID     INTEGER,
  nextRoutePointID INTEGER      NULL,
  nextPointID      INTEGER      NULL,
  nextPointName    VARCHAR(255) NULL,
  timeToNextPoint  INTEGER      NULL,
  PRIMARY KEY (routeID, routePointID),
  FOREIGN KEY (routeID) REFERENCES routes (routeID)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  FOREIGN KEY (routePointID) REFERENCES route_points (routePointID)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  FOREIGN KEY (nextRoutePointID) REFERENCES route_points (routePointID)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  FOREIGN KEY (nextPointID) REFERENCES points (pointID)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

CREATE PROCEDURE refreshMaterializedView()
  BEGIN

    TRUNCATE mat_view_row_count_for_user;

    -- расчет размеров всех результатов для пользователей
    INSERT INTO mat_view_row_count_for_user
    -- MARKET_AGENT
      (SELECT
         requests.marketAgentUserID,
         users.userRoleID,
         COUNT(requests.requestID)
       FROM users
         INNER JOIN requests ON (users.userID = requests.marketAgentUserID)
       GROUP BY requests.marketAgentUserID
       ORDER BY NULL)

      UNION ALL
      -- CLIENT_MANAGER
      (SELECT
         users.userID,
         users.userRoleID,
         COUNT(requests.requestID)
       FROM users
         INNER JOIN requests ON (users.clientID = requests.clientID)
       GROUP BY requests.clientID
       ORDER BY NULL)

      UNION ALL
      -- ADMIN
      (SELECT
         users.userID,
         users.userRoleID,
         (SELECT COUNT(requests.requestID)
          FROM requests)
       FROM users
       WHERE users.userRoleID = 'ADMIN');

  END;

CREATE PROCEDURE refreshRoutePointsSequential(_routeID INTEGER)
  BEGIN

    DELETE FROM mat_view_route_points_sequential
    WHERE routeID = _routeID;

    INSERT INTO mat_view_route_points_sequential
      SELECT
        mainRP.routeID,
        mainRP.routePointID,
        innerRP.routePointID                           AS nextRoutePointID,
        innerRP.pointID                                AS nextPointID,
        points.pointName                               AS nextPointName,
        relations_between_route_points.timeForDistance AS timeToNextPoint
      FROM route_points mainRP
        LEFT JOIN route_points innerRP ON (
          innerRP.routeID = _routeID AND
          mainRP.routeID = _routeID AND
          innerRP.sortOrder = (SELECT innerRP2.sortOrder
                               FROM route_points innerRP2
                               WHERE (innerRP2.routeID = _routeID AND innerRP2.sortOrder > mainRP.sortOrder)
                               ORDER BY innerRP.sortOrder
                               LIMIT 1)
          )
        LEFT JOIN points ON points.pointID = innerRP.pointID
        LEFT JOIN relations_between_route_points ON (
          relations_between_route_points.routePointIDFirst = mainRP.routePointID AND
          relations_between_route_points.routePointIDSecond = innerRP.routePointID
          )
      WHERE mainRP.routeID = _routeID;

  END;

-- -------------------------------------------------------------------------------------------------------------------
--                                                UPDATE TRIGGERS
-- -------------------------------------------------------------------------------------------------------------------

-- 1) при появлении новой заявки в системе появляется новая запись в mat_view_big_select(after INSERT on requests trigger)
-- 2) при обновлении заявки делается обновление данных в mat_view_big_select(after UPDATE on requests trigger)
-- 3) при удалении заявки ничего не делаем, т.к. mat_view_big_select ссылается на requestID с каскадным удалением
-- 4) при изменении имени пункта - надо обновлять его в таблице

CREATE TRIGGER after_route_points_insert AFTER INSERT ON route_points
FOR EACH ROW
  BEGIN
    -- если в таблице route_points 2 или больше записей, то вставляем новые значения в relations_between_routePoints
    IF (SELECT count(*)
        FROM route_points
        WHERE NEW.routeID = route_points.routeID) > 1
    THEN
      BEGIN

        SET @nextRoutePointID = (SELECT routePointID
                                 FROM route_points
                                 WHERE route_points.routeID = NEW.routeID AND route_points.sortOrder > NEW.sortOrder
                                 ORDER BY sortOrder ASC
                                 LIMIT 1);
        SET @previousRoutePointID = (SELECT routePointID
                                     FROM route_points
                                     WHERE route_points.routeID = NEW.routeID AND route_points.sortOrder < NEW.sortOrder
                                     ORDER BY sortOrder DESC
                                     LIMIT 1);
        IF (@nextRoutePointID IS NULL AND @previousRoutePointID IS NULL)
        THEN
          CALL generateLogistError('nextRoutePointID and previousRoutePointID is NULL');
        END IF;

        -- если мы добавили новый пункт в начало
        IF (@previousRoutePointID IS NULL)
        THEN
          INSERT INTO relations_between_route_points VALUE (NEW.routePointID, @nextRoutePointID, 0);
        -- если мы добавили новый пункт в конец
        ELSEIF (@nextRoutePointID IS NULL)
          THEN
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
  END;

CREATE TRIGGER after_route_points_delete AFTER DELETE ON route_points
FOR EACH ROW
  BEGIN
    -- если в таблице route_points 2 или больше записей, то вставляем новые значения в relations_between_routePoints
    IF (SELECT count(*)
        FROM route_points
        WHERE OLD.routeID = route_points.routeID) >= 2
    THEN
      BEGIN

        SET @nextRoutePointID = (SELECT routePointID
                                 FROM route_points
                                 WHERE route_points.routeID = OLD.routeID AND route_points.sortOrder > OLD.sortOrder
                                 ORDER BY sortOrder ASC
                                 LIMIT 1);
        SET @previousRoutePointID = (SELECT routePointID
                                     FROM route_points
                                     WHERE route_points.routeID = OLD.routeID AND route_points.sortOrder < OLD.sortOrder
                                     ORDER BY sortOrder DESC
                                     LIMIT 1);
        IF (@nextRoutePointID IS NULL AND @previousRoutePointID IS NULL)
        THEN
          CALL generateLogistError('nextRoutePointID and previousRoutePointID is NULL');
        END IF;

        -- если мы удалили пункт из начала
        IF (@previousRoutePointID IS NULL)
        THEN
          DELETE FROM relations_between_route_points
          WHERE routePointIDFirst = OLD.routePointID;
        -- если мы удалили пункт с конца
        ELSEIF (@nextRoutePointID IS NULL)
          THEN
            DELETE FROM relations_between_route_points
            WHERE routePointIDFirst = OLD.routePointID OR routePointIDSecond = OLD.routePointID;
        -- если мы удалили пункт из середины
        ELSE
          BEGIN
            DELETE FROM relations_between_route_points
            WHERE routePointIDSecond = OLD.routePointID;
            UPDATE relations_between_route_points
            SET routePointIDFirst = @previousRoutePointID, timeForDistance = 0
            WHERE routePointIDFirst = OLD.routePointID;
          END;
        END IF;
      END;
    END IF;

    CALL refreshRoutePointsSequential(OLD.routeID);
  END;


CREATE TRIGGER after_update AFTER UPDATE ON relations_between_route_points
FOR EACH ROW
  BEGIN
    UPDATE mat_view_route_points_sequential
    SET mat_view_route_points_sequential.timeToNextPoint = NEW.timeForDistance
    WHERE
      mat_view_route_points_sequential.routePointID = NEW.routePointIDFirst AND
      mat_view_route_points_sequential.nextRoutePointID IS NOT NULL AND
      mat_view_route_points_sequential.nextRoutePointID = NEW.routePointIDSecond;
  END;


CREATE TRIGGER after_request_insert AFTER INSERT ON requests
FOR EACH ROW
  BEGIN
    INSERT INTO requests_history
      VALUE
      (NULL, NOW(), NEW.requestID, NEW.requestIDExternal, NEW.dataSourceID, NEW.requestNumber, NEW.requestDate,
             NEW.clientID, NEW.destinationPointID, NEW.marketAgentUserID, NEW.invoiceNumber, NEW.invoiceDate,
                                                                                             NEW.documentNumber,
                                                                                             NEW.documentDate,
                                                                                             NEW.firma, NEW.storage,
                                                                                             NEW.contactName,
                                                                                             NEW.contactPhone,
                                                                                             NEW.deliveryOption,
                                                                                             NEW.deliveryDate,
                                                                                             NEW.boxQty, NEW.weight,
                                                                                                         NEW.volume,
                                                                                                         NEW.goodsCost,
                                                                                                         NEW.lastStatusUpdated,
                                                                                                         NEW.lastModifiedBy,
                                                                                                         'CREATED',
                                                                                                         NEW.commentForStatus,
                                                                                                         NEW.warehousePointID,
                                                                                                         NEW.routeListID,
                                                                                                         NEW.lastVisitedRoutePointID);

    CALL singleInsertOrUpdateOnMatViewBigSelect(NEW.requestID);
  END;


CREATE TRIGGER after_request_delete AFTER DELETE ON requests
FOR EACH ROW
  INSERT INTO requests_history
    VALUE
    (NULL, NOW(), OLD.requestID, OLD.requestIDExternal, OLD.dataSourceID, OLD.requestNumber, OLD.requestDate,
           OLD.clientID, OLD.destinationPointID, OLD.marketAgentUserID, OLD.invoiceNumber, OLD.invoiceDate,
                                                                                           OLD.documentNumber,
                                                                                           OLD.documentDate,
                                                                                           OLD.firma, OLD.storage,
                                                                                           OLD.contactName,
                                                                                           OLD.contactPhone,
                                                                                           OLD.deliveryOption,
                                                                                           OLD.deliveryDate, OLD.boxQty,
      OLD.weight, OLD.volume, OLD.goodsCost,
      OLD.lastStatusUpdated, OLD.lastModifiedBy, 'DELETED', OLD.commentForStatus, OLD.warehousePointID, OLD.routeListID,
      OLD.lastVisitedRoutePointID);

CREATE TRIGGER after_request_update AFTER UPDATE ON requests
FOR EACH ROW
  BEGIN
    INSERT INTO requests_history
      VALUE
      (NULL, NOW(), NEW.requestID, NEW.requestIDExternal, NEW.dataSourceID, NEW.requestNumber, NEW.requestDate,
             NEW.clientID, NEW.destinationPointID, NEW.marketAgentUserID, NEW.invoiceNumber, NEW.invoiceDate,
                                                                                             NEW.documentNumber,
                                                                                             NEW.documentDate,
                                                                                             NEW.firma, NEW.storage,
                                                                                             NEW.contactName,
                                                                                             NEW.contactPhone,
                                                                                             NEW.deliveryOption,
                                                                                             NEW.deliveryDate,
                                                                                             NEW.boxQty, NEW.weight,
                                                                                                         NEW.volume,
                                                                                                         NEW.goodsCost,
                                                                                                         NEW.lastStatusUpdated,
                                                                                                         NEW.lastModifiedBy,
                                                                                                         NEW.requestStatusID,
                                                                                                         NEW.commentForStatus,
                                                                                                         NEW.warehousePointID,
                                                                                                         NEW.routeListID,
                                                                                                         NEW.lastVisitedRoutePointID);

    #     UPDATE mat_view_big_select SET
    #       requestIDExternal = NEW.requestIDExternal, requestNumber = NEW.requestNumber, requestDate = NEW.requestDate,
    #       invoiceNumber = NEW.invoiceNumber, invoiceDate = NEW.invoiceDate, documentNumber = NEW.documentNumber,
    #       documentDate = NEW.documentDate, firma = NEW.firma, storage = NEW.storage, boxQty = NEW.boxQty,
    #       requestStatusID = NEW.requestStatusID, commentForStatus = NEW.commentForStatus, routeListID = NEW.routeListID
    #       WHERE mat_view_big_select.requestID = NEW.requestID;

    -- расчет времени прибытия заявки в следующий пункт маршрута
    IF (NEW.requestStatusID = 'DEPARTURE')
    THEN
      BEGIN
        SET @timeToNextPoint = (SELECT timeToNextPoint
                                FROM mat_view_route_points_sequential
                                WHERE routePointID = NEW.lastVisitedRoutePointID);
        SET @arrivalTimeToNextPoint = TIMESTAMPADD(MINUTE, @timeToNextPoint, NEW.lastStatusUpdated);

        INSERT INTO mat_view_arrival_time_for_request VALUE (NEW.requestID, @arrivalTimeToNextPoint)
        ON DUPLICATE KEY UPDATE
          arrivalTimeToNextRoutePoint = VALUES(arrivalTimeToNextRoutePoint);
      END;
    END IF;

    CALL singleInsertOrUpdateOnMatViewBigSelect(NEW.requestID);
  END;

CREATE TRIGGER before_request_update BEFORE UPDATE ON requests
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
        -- установка самого первого пункта маршрута
        SET NEW.lastVisitedRoutePointID = (SELECT routePointID
                                           FROM route_points
                                           WHERE (routeID = @routeID)
                                           ORDER BY sortOrder
                                           LIMIT 1);
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
          -- установка самого последнего пункта маршрута
          SET NEW.lastVisitedRoutePointID = (SELECT routePointID
                                             FROM route_points
                                             WHERE (routeID = @routeID)
                                             ORDER BY sortOrder DESC
                                             LIMIT 1);
      END IF;
    END;
  ELSEIF (OLD.routeListID IS NOT NULL)
    THEN
      SET NEW.lastVisitedRoutePointID = NULL;
  END IF;

-- синхронизация при изменении имени пункта
CREATE TRIGGER after_points_update AFTER UPDATE ON points
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
  END IF;

-- синхронизация при изменении имени clientName или при изменении INN
CREATE TRIGGER after_clients_update AFTER UPDATE ON clients
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
  END;

-- синхронизация при изменении имени пользователя для driverName и marketAgentUserName
-- TODO работает не правильно временно отключен
# CREATE TRIGGER after_users_update AFTER UPDATE ON users
# FOR EACH ROW
#   IF (NEW.userName <> OLD.userName)
#   THEN
#     UPDATE mat_view_big_select
#     SET marketAgentUserName = NEW.userName
#     WHERE marketAgentUserID = NEW.userID;
#
#     UPDATE mat_view_big_select
#     SET driverUserName = NEW.userName
#     WHERE driverUserName = NEW.userID;
#   END IF;

CREATE TRIGGER after_route_list_insert AFTER INSERT ON route_lists
FOR EACH ROW
  INSERT INTO route_list_history
  VALUES
    (NULL, NOW(), NEW.routeListID, NEW.routeListIDExternal, NEW.dataSourceID, NEW.routeListNumber, NEW.creationDate,
           NEW.departureDate, NEW.palletsQty, NEW.forwarderId, NEW.driverID,
     NEW.driverPhoneNumber, NEW.licensePlate, NEW.status, NEW.routeID, 'CREATED');

CREATE TRIGGER after_route_list_delete AFTER DELETE ON route_lists
FOR EACH ROW
  INSERT INTO route_list_history
  VALUES
    (NULL, NOW(), OLD.routeListID, OLD.routeListIDExternal, OLD.dataSourceID, OLD.routeListNumber, OLD.creationDate,
           OLD.departureDate, OLD.palletsQty, OLD.forwarderId, OLD.driverID,
     OLD.driverPhoneNumber, OLD.licensePlate, OLD.status, OLD.routeID, 'DELETED');

CREATE TRIGGER after_route_lists_update AFTER UPDATE ON route_lists
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
  END;

-- синхронизация при изменении имени маршрута
CREATE TRIGGER after_routes_update AFTER UPDATE ON routes
FOR EACH ROW
  IF (NEW.routeName <> OLD.routeName)
  THEN
    UPDATE mat_view_big_select
    SET routeName = NEW.routeName
    WHERE routeID = NEW.routeID;
  END IF;

-- -------------------------------------------------------------------------------------------------------------------
--                                                USERS SELECT
-- -------------------------------------------------------------------------------------------------------------------

CREATE VIEW transmaster_transport_db.all_users AS
  SELECT
    u.userId,
    u.login,
    u.userName,
    u.position,
    u.phoneNumber,
    u.email,
    'dummy' AS password,
    p.pointName,
    r.userRoleRusName,
    c.clientID
  FROM transmaster_transport_db.users u
    LEFT JOIN transmaster_transport_db.points p ON p.pointID = u.pointID
    LEFT JOIN transmaster_transport_db.clients c ON c.clientID = u.clientID
    INNER JOIN transmaster_transport_db.user_roles r ON r.userRoleID = u.userRoleID;

-- select users procedure
-- _search - строка для глобального поиска по всем колонкам
CREATE PROCEDURE transmaster_transport_db.selectUsers(_startEntry INTEGER, _length INTEGER,
                                                      _orderby    VARCHAR(255),
                                                      _isDesc     BOOLEAN, _search TEXT)
  BEGIN

    SET @searchString = CONCAT('%', _search, '%');

    SELECT SQL_CALC_FOUND_ROWS *
    FROM transmaster_transport_db.all_users
    WHERE (_search = '' OR
           userName LIKE @searchString collate utf8_general_ci OR
           position LIKE @searchString collate utf8_general_ci OR
           phoneNumber LIKE @searchString collate utf8_general_ci OR
           email LIKE @searchString collate utf8_general_ci OR
           pointName LIKE @searchString collate utf8_general_ci OR
           userRoleRusName LIKE @searchString collate utf8_general_ci)
    ORDER BY NULL,
      CASE WHEN _orderby = ''
        THEN NULL END,
      CASE WHEN _isDesc AND _orderby = 'userId'
        THEN userId END ASC,
      CASE WHEN _isDesc AND _orderby = 'login'
        THEN login END ASC,
      CASE WHEN _isDesc AND _orderby = 'userName'
        THEN userName END ASC,
      CASE WHEN _isDesc AND _orderby = 'position'
        THEN position END ASC,
      CASE WHEN _isDesc AND _orderby = 'phoneNumber'
        THEN phoneNumber END ASC,
      CASE WHEN _isDesc AND _orderby = 'email'
        THEN email END ASC,
      CASE WHEN _isDesc AND _orderby = 'pointName'
        THEN pointName END ASC,
      CASE WHEN _isDesc AND _orderby = 'userRoleRusName'
        THEN userRoleRusName END ASC,
      CASE WHEN _isDesc AND _orderby = 'clientID'
        THEN clientID END ASC,
      CASE WHEN NOT (_isDesc) AND _orderby = 'userId'
        THEN userId END ASC,
      CASE WHEN NOT (_isDesc) AND _orderby = 'login'
        THEN login END DESC,
      CASE WHEN NOT (_isDesc) AND _orderby = 'userName'
        THEN userName END DESC,
      CASE WHEN NOT (_isDesc) AND _orderby = 'position'
        THEN position END DESC,
      CASE WHEN NOT (_isDesc) AND _orderby = 'phoneNumber'
        THEN phoneNumber END DESC,
      CASE WHEN NOT (_isDesc) AND _orderby = 'email'
        THEN email END DESC,
      CASE WHEN NOT (_isDesc) AND _orderby = 'pointName'
        THEN pointName END DESC,
      CASE WHEN NOT (_isDesc) AND _orderby = 'userRoleRusName'
        THEN userRoleRusName END DESC,
      CASE WHEN NOT (_isDesc) AND _orderby = 'clientID'
        THEN clientID END DESC
    LIMIT _startEntry, _length;

    -- filtered users
    SELECT FOUND_ROWS() AS `totalFiltered`;

    -- total users
    SELECT COUNT(*) AS `totalCount`
    FROM all_users;

  END;


-- select routes procedure
-- _search - строка для глобального поиска по всем колонкам
CREATE PROCEDURE transmaster_transport_db.selectRoutes(
  _startEntry INTEGER,
  _length     INTEGER,
  _orderby    VARCHAR(255),
  _isDesc     BOOLEAN,
  _search     TEXT
)
  BEGIN

    SET @searchString = CONCAT('%', _search, '%');

    SELECT SQL_CALC_FOUND_ROWS
      r.routeID,
      r.routeName,
      t.tariffID,
      t.cost,
      t.cost_per_point,
      t.cost_per_hour
    FROM transmaster_transport_db.routes r
      LEFT JOIN transmaster_transport_db.tariffs t ON r.tariffID = t.tariffID
    WHERE (
      _search = '' OR
      r.routeName LIKE @searchString collate utf8_general_ci OR
      t.cost LIKE @searchString collate utf8_general_ci OR
      t.cost_per_point LIKE @searchString collate utf8_general_ci OR
      t.cost_per_hour LIKE @searchString collate utf8_general_ci
    )
    ORDER BY NULL,
      CASE WHEN _orderby = ''
        THEN NULL END,
      CASE WHEN _isDesc AND _orderby = 'routeName'
        THEN routeName END ASC,
      CASE WHEN _isDesc AND _orderby = 'cost'
        THEN cost END ASC,
      CASE WHEN _isDesc AND _orderby = 'cost_per_point'
        THEN cost_per_point END ASC,
      CASE WHEN _isDesc AND _orderby = 'cost_per_housr'
        THEN cost_per_hour END ASC,
      CASE WHEN NOT (_isDesc) AND _orderby = 'routeName'
        THEN routeName END DESC,
      CASE WHEN NOT (_isDesc) AND _orderby = 'cost'
        THEN cost END DESC,
      CASE WHEN NOT (_isDesc) AND _orderby = 'cost_per_point'
        THEN cost_per_point END DESC,
      CASE WHEN NOT (_isDesc) AND _orderby = 'cost_per_hour'
        THEN cost_per_hour END DESC
    LIMIT _startEntry, _length;

    -- filtered routes
    SELECT FOUND_ROWS() AS `totalFiltered`;

    -- total routes
    SELECT COUNT(*) AS `totalCount`
    FROM routes;

  END;

-- select transport companies procedure
-- _search - строка для глобального поиска по всем колонкам
CREATE PROCEDURE transmaster_transport_db.selectTransportCompanies(
  _startEntry INTEGER,
  _length     INTEGER,
  _orderby    VARCHAR(255),
  _isDesc     BOOLEAN,
  _search     TEXT
)
  BEGIN

    SET @searchString = CONCAT('%', _search, '%');

    SELECT SQL_CALC_FOUND_ROWS
      tc.*
    FROM transmaster_transport_db.transport_companies tc
    WHERE (
            _search = '' OR
            tc.id LIKE @searchString collate utf8_general_ci  OR
            tc.name LIKE @searchString collate utf8_general_ci OR
            tc.short_name LIKE @searchString collate utf8_general_ci OR
            tc.inn LIKE @searchString collate utf8_general_ci OR
            tc.KPP LIKE @searchString collate utf8_general_ci OR
            tc.BIK LIKE @searchString collate utf8_general_ci OR
            tc.cor_account LIKE @searchString collate utf8_general_ci OR
            tc.cur_account LIKE @searchString collate utf8_general_ci OR
            tc.bank_name LIKE @searchString collate utf8_general_ci OR
            tc.legal_address LIKE @searchString collate utf8_general_ci OR
            tc.post_address LIKE @searchString collate utf8_general_ci OR
            tc.keywords LIKE @searchString collate utf8_general_ci OR
            tc.director_fullname LIKE @searchString collate utf8_general_ci OR
            tc.chief_acc_fullname LIKE @searchString collate utf8_general_ci
          ) AND deleted = FALSE
    ORDER BY NULL,
      CASE WHEN _orderby = ''
        THEN NULL END,
      CASE WHEN _isDesc AND  _orderby = 'id'
        THEN id END ASC,
      CASE WHEN _isDesc AND  _orderby = 'name'
        THEN name END ASC,
      CASE WHEN _isDesc AND  _orderby = 'short_name'
        THEN short_name END ASC,
      CASE WHEN _isDesc AND  _orderby = 'inn'
        THEN inn END ASC,
      CASE WHEN _isDesc AND  _orderby = 'KPP'
        THEN KPP END ASC,
      CASE WHEN _isDesc AND _orderby = 'BIK'
        THEN BIK END ASC,
      CASE WHEN _isDesc AND _orderby = 'cor_account'
        THEN cor_account END ASC,
      CASE WHEN _isDesc AND _orderby = 'cur_account'
        THEN cur_account END ASC,
      CASE WHEN _isDesc AND _orderby = 'bank_name'
        THEN bank_name END ASC,
      CASE WHEN _isDesc AND _orderby = 'legal_address'
        THEN legal_address END ASC,
      CASE WHEN _isDesc AND _orderby = 'post_address'
        THEN post_address END ASC,
      CASE WHEN _isDesc AND _orderby = 'keywords'
        THEN keywords END ASC,
      CASE WHEN _isDesc AND _orderby = 'director_fullname'
        THEN director_fullname END ASC,
      CASE WHEN _isDesc AND _orderby = 'chief_acc_fullname'
        THEN chief_acc_fullname END ASC,
      CASE WHEN NOT (_isDesc) AND  _orderby = 'id'
        THEN id END DESC ,
      CASE  WHEN NOT (_isDesc) AND  _orderby = 'name'
        THEN name END DESC,
      CASE  WHEN NOT (_isDesc) AND  _orderby = 'short_name'
        THEN short_name END DESC,
      CASE  WHEN NOT (_isDesc) AND  _orderby = 'inn'
        THEN inn END DESC,
      CASE  WHEN NOT (_isDesc) AND  _orderby = 'KPP'
        THEN KPP END DESC,
      CASE  WHEN NOT (_isDesc) AND _orderby = 'BIK'
        THEN BIK END DESC,
      CASE  WHEN NOT (_isDesc) AND _orderby = 'cor_account'
        THEN cor_account END DESC,
      CASE  WHEN NOT (_isDesc) AND _orderby = 'cur_account'
        THEN cur_account END DESC,
      CASE  WHEN NOT (_isDesc) AND _orderby = 'bank_name'
        THEN bank_name END DESC,
      CASE  WHEN NOT (_isDesc) AND _orderby = 'legal_address'
        THEN legal_address END DESC,
      CASE  WHEN NOT (_isDesc) AND _orderby = 'post_address'
        THEN post_address END DESC,
      CASE  WHEN NOT (_isDesc) AND _orderby = 'keywords'
        THEN keywords END DESC,
      CASE  WHEN NOT (_isDesc) AND _orderby = 'director_fullname'
        THEN director_fullname END DESC,
      CASE  WHEN NOT (_isDesc) AND _orderby = 'chief_acc_fullname'
        THEN chief_acc_fullname END DESC
    LIMIT _startEntry, _length;

    -- filtered routes
    SELECT FOUND_ROWS() AS `totalFiltered`;

    -- total routes
    SELECT COUNT(*) AS `totalCount`
    FROM transport_companies
    WHERE deleted = FALSE;

  END;

-- select vehicles procedure
-- _search - строка для глобального поиска по всем колонкам
CREATE PROCEDURE transmaster_transport_db.selectVehicles(
  _startEntry INTEGER,
  _length     INTEGER,
  _orderby    VARCHAR(255),
  _isDesc     BOOLEAN,
  _search     TEXT
)
  BEGIN

    SET @searchString = CONCAT('%', _search, '%');

    SELECT SQL_CALC_FOUND_ROWS *
    FROM transmaster_transport_db.vehicles
    WHERE (
            _search = '' OR
            id LIKE @searchString collate utf8_general_ci OR
            transport_company_id LIKE @searchString collate utf8_general_ci OR
            license_number LIKE @searchString collate utf8_general_ci OR
            model LIKE @searchString collate utf8_general_ci OR
            carrying_capacity LIKE @searchString collate utf8_general_ci OR
            volume LIKE @searchString collate utf8_general_ci OR
            loading_type LIKE @searchString collate utf8_general_ci OR
            pallets_quantity LIKE @searchString collate utf8_general_ci OR
            type LIKE @searchString collate utf8_general_ci
          ) AND deleted = FALSE
    ORDER BY NULL,
      CASE WHEN _orderby = ''
        THEN NULL END,
      CASE WHEN _isDesc AND  _orderby = 'id'
        THEN id END ASC,
      CASE WHEN _isDesc AND  _orderby = 'transport_company_id'
        THEN transport_company_id END ASC,
      CASE WHEN _isDesc AND  _orderby = 'license_number'
        THEN license_number END ASC,
      CASE WHEN _isDesc AND  _orderby = 'model'
        THEN model END ASC,
      CASE WHEN _isDesc AND  _orderby = 'carrying_capacity'
        THEN carrying_capacity END ASC,
      CASE WHEN _isDesc AND _orderby = 'volume'
        THEN volume END ASC,
      CASE WHEN _isDesc AND _orderby = 'loading_type'
        THEN loading_type END ASC,
      CASE WHEN _isDesc AND _orderby = 'pallets_quantity'
        THEN pallets_quantity END ASC,
      CASE WHEN _isDesc AND _orderby = 'type'
        THEN type END ASC,
      CASE WHEN NOT (_isDesc) AND  _orderby = 'id'
        THEN id END DESC,
      CASE WHEN NOT (_isDesc) AND  _orderby = 'transport_company_id'
        THEN transport_company_id END DESC,
      CASE WHEN NOT (_isDesc) AND  _orderby = 'license_number'
        THEN license_number END DESC,
      CASE WHEN NOT (_isDesc) AND  _orderby = 'model'
        THEN model END DESC,
      CASE WHEN NOT (_isDesc) AND  _orderby = 'carrying_capacity'
        THEN carrying_capacity END DESC,
      CASE WHEN NOT (_isDesc) AND _orderby = 'volume'
        THEN volume END DESC,
      CASE WHEN NOT (_isDesc) AND _orderby = 'loading_type'
        THEN loading_type END DESC,
      CASE WHEN NOT (_isDesc) AND _orderby = 'pallets_quantity'
        THEN pallets_quantity END DESC,
      CASE WHEN NOT (_isDesc) AND _orderby = 'type'
        THEN type END DESC
    LIMIT _startEntry, _length;

    -- filtered routes
    SELECT FOUND_ROWS() AS `totalFiltered`;

    -- total routes
    SELECT COUNT(*) AS `totalCount`
    FROM vehicles
    WHERE deleted = FALSE;

  END;

-- select drivers procedure
-- _search - строка для глобального поиска по всем колонкам
CREATE PROCEDURE transmaster_transport_db.selectDrivers(
  _startEntry INTEGER,
  _length     INTEGER,
  _orderby    VARCHAR(255),
  _isDesc     BOOLEAN,
  _search     TEXT
)
  BEGIN

    SET @searchString = CONCAT('%', _search, '%');

    SELECT SQL_CALC_FOUND_ROWS *
    FROM transmaster_transport_db.drivers
    WHERE (_search = '' OR
           id LIKE @searchString collate utf8_general_ci OR
           vehicle_id LIKE @searchString collate utf8_general_ci OR
           transport_company_id LIKE @searchString collate utf8_general_ci OR
           full_name LIKE @searchString collate utf8_general_ci OR
           passport LIKE @searchString collate utf8_general_ci OR
           phone LIKE @searchString collate utf8_general_ci OR
           license LIKE @searchString collate utf8_general_ci) AND deleted = FALSE
    ORDER BY NULL,
      CASE WHEN _orderby = ''
        THEN NULL END,
      CASE WHEN _isDesc AND  _orderby = 'id'
        THEN id END ASC,
      CASE WHEN _isDesc AND  _orderby = 'vehicle_id'
        THEN vehicle_id END ASC,
      CASE WHEN _isDesc AND  _orderby = 'transport_company_id'
        THEN transport_company_id END ASC,
      CASE WHEN _isDesc AND  _orderby = 'full_name'
        THEN full_name END ASC,
      CASE WHEN _isDesc AND  _orderby = 'passport'
        THEN passport END ASC,
      CASE WHEN _isDesc AND _orderby = 'phone'
        THEN phone END ASC,
      CASE WHEN _isDesc AND _orderby = 'license'
        THEN license END ASC,
      CASE WHEN NOT (_isDesc) AND  _orderby = 'id'
        THEN id END DESC,
      CASE WHEN NOT (_isDesc) AND  _orderby = 'vehicle_id'
        THEN vehicle_id END DESC,
      CASE WHEN NOT (_isDesc) AND  _orderby = 'transport_company_id'
        THEN transport_company_id END DESC,
      CASE WHEN NOT (_isDesc) AND  _orderby = 'full_name'
        THEN full_name END DESC,
      CASE WHEN NOT (_isDesc) AND  _orderby = 'passport'
        THEN passport END DESC,
      CASE WHEN NOT (_isDesc) AND  _orderby = 'phone'
        THEN phone END DESC,
      CASE WHEN NOT (_isDesc) AND _orderby = 'license'
        THEN license END DESC
    LIMIT _startEntry, _length;

    -- filtered routes
    SELECT FOUND_ROWS() AS `totalFiltered`;

    -- total routes
    SELECT COUNT(*) AS `totalCount`
    FROM drivers
    WHERE deleted = FALSE;

  END;

-- -------------------------------------------------------------------------------------------------------------------
--                                    REQUEST STATUS HISTORY SELECT
-- -------------------------------------------------------------------------------------------------------------------


-- get history for some request
CREATE PROCEDURE selectRequestStatusHistory(_requestIDExternal VARCHAR(255))
  BEGIN
    SELECT
      MAX(requests_history.lastStatusUpdated) AS timeMarkWhenRequestWasChanged,
      requests_history.boxQty                 AS boxQty,
      request_statuses.requestStatusRusName   AS requestStatusRusName,
      points.pointName                        AS pointWhereStatusWasChanged,
      users.userName                          AS userNameThatChangedStatus,
      route_lists.routeListIDExternal         AS routeListIDExternal,
      route_lists.routeListNumber             AS routeListNumber

    FROM requests_history
      INNER JOIN (request_statuses)
        ON (
        requests_history.requestStatusID = request_statuses.requestStatusID
        )
      LEFT JOIN (points, route_points) ON (
        requests_history.lastVisitedRoutePointID = route_points.routePointID AND
        route_points.pointID = points.pointID
        )
      LEFT JOIN (users) ON (
        requests_history.lastModifiedBy = users.userID
        )
      LEFT JOIN (route_lists) ON (
        requests_history.routeListID = route_lists.routeListID
        )
    WHERE requests_history.requestIDExternal = _requestIDExternal
          AND requests_history.lastStatusUpdated IS NOT NULL
          AND requests_history.lastStatusUpdated != '0000-00-00 00:00:00'
    GROUP BY requests_history.requestStatusID
    ORDER BY timeMarkWhenRequestWasChanged;
  END;

-- -------------------------------------------------------------------------------------------------------------------
--                                TIME AND DISTANCE BETWEEN ROUTE POINTS SELECT
-- -------------------------------------------------------------------------------------------------------------------


-- procedure for getting time and distance between routePoints
CREATE PROCEDURE getRelationsBetweenRoutePoints(_routeID INTEGER)
  BEGIN
    SELECT
      CONCAT_WS('_', routePointIDFirst, routePointIDSecond) AS relationID,
      pointFirst.pointName                                  AS `pointNameFirst`,
      pointSecond.pointName                                 AS `pointNameSecond`,
      timeForDistance,
      distance,
      routePointFirst.sortOrder
    FROM routes
      JOIN (
          route_points AS routePointFirst,
          route_points AS routePointSecond,
          relations_between_route_points,
          points AS pointFirst,
          points AS pointSecond
        ) ON
            (routePointFirst.routePointID = routePointIDFirst AND routePointSecond.routePointID = routePointIDSecond AND
             routePointFirst.routeID = `routes`.routeID AND routePointSecond.routeID = `routes`.routeID AND
             routePointFirst.pointID = pointFirst.pointID AND routePointSecond.pointID = pointSecond.pointID)
      LEFT JOIN (`distances_between_points`)
        ON
          (pointIDFirst = routePointFirst.pointID AND pointIDSecond = routePointSecond.pointID) OR
          (pointIDFirst = routePointSecond.pointID AND pointIDSecond = routePointFirst.pointID)
    WHERE routes.routeID = _routeID
    ORDER BY routePointFirst.sortOrder;
  END;

-- -------------------------------------------------------------------------------------------------------------------
--                                    PRETENSIONS SELECT, CREATE AND DELETE
-- -------------------------------------------------------------------------------------------------------------------

-- creating pretension
CREATE PROCEDURE `insert_pretension`(IN _requestIDExternal        VARCHAR(255), IN _pretensionStatus VARCHAR(32),
                                     IN _pretensionComment        TEXT, IN _pretensionCathegory VARCHAR(32),
                                     IN _pretensionPositionNumber TEXT, IN _pretensionSum DECIMAL(10, 2))
  BEGIN
    INSERT INTO pretensions (requestIDExternal, pretensionStatus, pretensionComment, pretensionCathegory, PositionNumber, sum, dateAdded)
    VALUES (_requestIDExternal, _pretensionStatus, _pretensionComment, _pretensionCathegory, _pretensionPositionNumber,
            _pretensionSum, CURRENT_DATE);
  END;

-- updating pretension
CREATE PROCEDURE `updatePretension`(_pretensionId        INTEGER, _requestIDExternal VARCHAR(255),
                                    _pretensionCathegory VARCHAR(32), _pretensionComment TEXT, _positionNumber TEXT,
                                    _pretensionSum       DECIMAL(10, 2))
  BEGIN
    UPDATE pretensions
    SET pretensionCathegory = _pretensionCathegory, pretensionComment = _pretensionComment,
      positionNumber        = _positionNumber, sum = _pretensionSum
    WHERE pretensionID = _pretensionId AND requestIDExternal = _requestIDExternal;
  END;

-- deleting (closing) pretension
CREATE PROCEDURE `deletePretension`(_pretensionId INTEGER, _requestIDExternal VARCHAR(255))
  BEGIN
    DELETE FROM pretensions
    WHERE pretensionID = _pretensionId AND requestIDExternal = _requestIDExternal;
  END;

-- getting pretension
CREATE PROCEDURE `getPretensionsByReqIdExt`(_requestIDExternal VARCHAR(255))
  BEGIN
    SELECT *
    FROM pretensions
    WHERE requestIDExternal = _requestIDExternal;
  END;

-- -------------------------------------------------------------------------------------------------------------------
--                                    TRANSPORT COMPANIES, VEHICLES, DRIVERS
-- -------------------------------------------------------------------------------------------------------------------

CREATE TABLE transmaster_transport_db.transport_companies
(
  id INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  name VARCHAR(64),
  short_name VARCHAR(32),
  inn INT(11),
  KPP VARCHAR(64),
  BIK VARCHAR(64),
  cor_account VARCHAR(64),
  cur_account VARCHAR(64),
  bank_name VARCHAR(128),
  legal_address VARCHAR(128),
  post_address VARCHAR(128),
  keywords VARCHAR(64),
  director_fullname VARCHAR(128),
  chief_acc_fullname VARCHAR(128),
  deleted BOOLEAN DEFAULT FALSE NOT NULL
);

CREATE TABLE transmaster_transport_db.vehicles
(
  id                   INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  transport_company_id INT(11),
  license_number       VARCHAR(12),
  model                VARCHAR(32),
  carrying_capacity    VARCHAR(32),
  volume               VARCHAR(32),
  loading_type         VARCHAR(32),
  pallets_quantity     INT(11),
  type                 VARCHAR(32),
  deleted BOOLEAN DEFAULT FALSE NOT NULL,
  CONSTRAINT vehicles_transport_companies_id_fk FOREIGN KEY (transport_company_id) REFERENCES transport_companies (id)
);


CREATE TABLE transmaster_transport_db.drivers
(
  id INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  vehicle_id INT(11),
  transport_company_id INT(11),
  full_name VARCHAR(64),
  passport VARCHAR(128),
  phone VARCHAR(18),
  license VARCHAR(128),
  deleted BOOLEAN DEFAULT FALSE NOT NULL,
  CONSTRAINT drivers_vehicles_id_fk FOREIGN KEY (vehicle_id) REFERENCES vehicles (id),
  CONSTRAINT drivers_transport_companies_id_fk FOREIGN KEY (transport_company_id) REFERENCES transport_companies (id)
);
CREATE INDEX drivers_transport_companies_id_fk ON drivers (transport_company_id);
CREATE INDEX drivers_vehicles_id_fk ON drivers (vehicle_id);



-- -------------------------------------------------------------------------------------------------------------------
--                                                 INDEXES
-- -------------------------------------------------------------------------------------------------------------------


-- индексы на фильтры по роли пользователя
CREATE INDEX ind1 ON mat_view_big_select (marketAgentUserID);
CREATE INDEX ind2 ON mat_view_big_select (clientID);
CREATE INDEX ind3 ON mat_view_big_select (routeID);

-- индекс для поиска следующего пункта маршрута
CREATE INDEX ind4 ON mat_view_route_points_sequential (routePointID);

-- индекс для поиска общего количества записей для конкретного пользователя
CREATE INDEX ind5 ON mat_view_row_count_for_user (userID);

-- индекс для оптимизации запроса selectRequestStatusHistory
CREATE INDEX ind6 ON requests_history (requestIDExternal);

COMMIT;