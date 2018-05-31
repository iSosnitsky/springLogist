ALTER TABLE transmaster_transport_db.route_lists ADD transport_company_id INT NULL;
ALTER TABLE transmaster_transport_db.route_lists
  ADD CONSTRAINT route_lists___ibfk_4
FOREIGN KEY (transport_company_id) REFERENCES transport_companies (id);
ALTER TABLE transmaster_transport_db.route_lists ADD vehicle_id INT NULL;
ALTER TABLE transmaster_transport_db.route_lists
  ADD CONSTRAINT route_lists_ibfk_5
FOREIGN KEY (vehicle_id) REFERENCES vehicles (id);

