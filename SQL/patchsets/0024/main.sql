ALTER TABLE route_lists ADD transport_company_id INT UNSIGNED NULL;
ALTER TABLE route_lists ADD driver_id_internal INT UNSIGNED NULL;
ALTER TABLE route_lists ADD vehicle_id INT UNSIGNED NULL;

ALTER TABLE requests DROP transportCompanyId;
ALTER TABLE requests DROP driverId;
ALTER TABLE requests DROP vehicleId;