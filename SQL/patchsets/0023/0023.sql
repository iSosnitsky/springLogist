CREATE TABLE points_to_managers
(
  manager_id INT UNSIGNED NOT NULL,
  point_id INT UNSIGNED NOT NULL
);
ALTER TABLE points_to_managers ADD CONSTRAINT points_to_managers_manager_id_point_id_pk UNIQUE (manager_id, point_id);

INSERT IGNORE INTO points_to_managers SELECT DISTINCT marketAgentUserID, destinationPointID FROM requests WHERE marketAgentUserID IS NOT NULL AND destinationPointID IS NOT NULL;
INSERT IGNORE INTO points_to_managers SELECT DISTINCT marketAgentUserID, warehousePointID FROM requests WHERE marketAgentUserID IS NOT NULL AND warehousePointID IS NOT NULL;

GRANT ALL ON TABLE test_ttdb.points_to_managers TO 'test_db_user'@'localhost';