/*GRANT
      priv_type [(column_list)]
      [, priv_type [(column_list)]] ...
      ON [object_type] priv_level
      TO user_specification [, user_specification] ...
      [REQUIRE {NONE | ssl_option [[AND] ssl_option] ...}]
      [WITH with_option ...]

      object_type:
      TABLE
      | FUNCTION
      | PROCEDURE

      priv_level:
      *
      | *.*
      | db_name.*
      | db_name.tbl_name
      | tbl_name
      | db_name.routine_name
*/
-- Оператор WITH GRANT OPTION предоставляет пользователю возможность наделять других пользователей любыми привилегиями,
-- которые он сам имеет на указанном уровне привилегий



-- ------------------СОЗДАНИЕ ПОЛЬЗОВАТЕЛЕЙ---------------------



-- Создание пароля для root пользователя.
-- Используется только во время разработки, позволяет не только менять данные, но и создавать новые таблицы, процедуры, ... DDL
# SET PASSWORD FOR 'root'@'localhost' = PASSWORD('aftR179Kp');


--  Создание пользователя для PHP.
--  The 'andy'@'localhost' account is necessary if there is an anonymous-user account for localhost
# DROP USER 'andy'@'localhost';
CREATE USER 'andy'@'localhost' IDENTIFIED BY 'andyandy';

--  позволяем выполнение любых хранимых процедур
GRANT EXECUTE ON `transmaster_transport_db`.* TO 'andy'@'localhost';

--  привелегии для каждой отдельной таблицы
GRANT SELECT ON transmaster_transport_db.clients TO 'andy'@'localhost';
GRANT SELECT ON transmaster_transport_db.data_sources TO 'andy'@'localhost';
GRANT SELECT, UPDATE, INSERT, DELETE ON transmaster_transport_db.distances_between_points TO 'andy'@'localhost';
GRANT SELECT ON transmaster_transport_db.requests_history TO 'andy'@'localhost';
GRANT SELECT ON transmaster_transport_db.request_statuses TO 'andy'@'localhost';
GRANT SELECT ON transmaster_transport_db.request_statuses_for_user_role TO 'andy'@'localhost';
GRANT SELECT, UPDATE, INSERT, DELETE ON transmaster_transport_db.requests TO 'andy'@'localhost';
GRANT SELECT ON transmaster_transport_db.permissions TO 'andy'@'localhost';
GRANT SELECT ON transmaster_transport_db.permissions_for_roles TO 'andy'@'localhost';
GRANT SELECT ON transmaster_transport_db.point_types TO 'andy'@'localhost';
GRANT SELECT, UPDATE ON transmaster_transport_db.points TO 'andy'@'localhost';
GRANT SELECT, UPDATE ON transmaster_transport_db.relations_between_route_points TO 'andy'@'localhost';
GRANT SELECT ON transmaster_transport_db.requests TO 'andy'@'localhost';
GRANT SELECT ON transmaster_transport_db.route_list_history TO 'andy'@'localhost';
GRANT SELECT ON transmaster_transport_db.route_list_statuses TO 'andy'@'localhost';
GRANT SELECT, UPDATE, INSERT, DELETE ON transmaster_transport_db.route_lists TO 'andy'@'localhost';
GRANT SELECT, UPDATE, INSERT, DELETE ON transmaster_transport_db.route_points TO 'andy'@'localhost';
GRANT SELECT, UPDATE, INSERT, DELETE ON transmaster_transport_db.routes TO 'andy'@'localhost';
GRANT SELECT, UPDATE, INSERT, DELETE ON transmaster_transport_db.tariffs TO 'andy'@'localhost';
GRANT SELECT ON transmaster_transport_db.user_roles TO 'andy'@'localhost';
GRANT SELECT, UPDATE, INSERT, DELETE ON transmaster_transport_db.users TO 'andy'@'localhost';
GRANT SELECT, UPDATE, INSERT, DELETE ON transmaster_transport_db.transport_companies TO 'andy'@'localhost';
GRANT SELECT, UPDATE, INSERT, DELETE ON transmaster_transport_db.vehicles TO 'andy'@'localhost';
GRANT SELECT, UPDATE, INSERT, DELETE ON transmaster_transport_db.drivers TO 'andy'@'localhost';



-- Создание пользователя для парсера
# DROP USER 'parser'@'localhost';
CREATE USER 'parser'@'localhost' IDENTIFIED BY 'refka1203';
GRANT ALL ON transmaster_transport_db.* TO 'parser'@'localhost';
GRANT ALL ON backup_db.* TO 'parser'@'localhost';
-- GRANT SELECT, UPDATE, INSERT, DELETE, EXECUTE ON `transmaster_transport_db`.* TO 'parser'@'localhost';
-- GRANT EXECUTE ON PROCEDURE transmaster_transport_db.refreshMaterializedView TO 'parser'@'localhost';