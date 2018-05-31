CREATE TABLE transmaster_transport_db.storages_to_points
(
  storage VARCHAR(250) NOT NULL,
  point_id INT NOT NULL
);
CREATE UNIQUE INDEX storages_to_points_storage_uindex ON transmaster_transport_db.storages_to_points (storage);

# to populate - use this query
INSERT INTO storages_to_points SELECT storage, warehousePointID FROM (SELECT DISTINCTROW storage,warehousePointID FROM requests WHERE warehousePointID IS NOT NULL) AS storages GROUP BY storage HAVING count(storage) = 1;;