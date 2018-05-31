create table freight
(
  freight_id int(10) unsigned auto_increment
    primary key,
  transport_company_id int null,
  driver_id int null,
  vehicle_id int null,
  vehicle_2_id int null,
  vehicle_3_id int null,
  route_id int null,
  distance int null,
  continuance int null,
  fuel_consumption int null,
  stall_hours int null,
  unique_addresses int null,
  total_box_amount int null,
  speed_readings_end int null,
  speed_readings_begin int null
);

ALTER TABLE route_lists ADD freight_id INT NULL;

ALTER TABLE freight ADD status_id VARCHAR(64) NULL;
ALTER TABLE freight ADD freight_number VARCHAR(64) NULL;
ALTER TABLE freight
  ADD CONSTRAINT freight_request_statuses_requestStatusID_fk
FOREIGN KEY (status_id) REFERENCES request_statuses (requestStatusID);
ALTER TABLE route_lists ADD vehicle_3_id INT NULL;
ALTER TABLE route_lists ADD vehicle_2_id INT NULL;
ALTER TABLE route_lists
  MODIFY COLUMN freight_id INT AFTER vehicle_3_id,
  MODIFY COLUMN driver_id_internal INT UNSIGNED AFTER vehicle_3_id;
ALTER TABLE route_lists DROP FOREIGN KEY route_lists___ibfk_4;
ALTER TABLE route_lists DROP FOREIGN KEY route_lists_ibfk_5;
GRANT ALL ON TABLE freight TO 'andy'@'localhost';
