ALTER TABLE vehicles ADD vehicle_id_external VARCHAR(32) NULL;
ALTER TABLE vehicles MODIFY COLUMN vehicle_id_external VARCHAR(32) NOT NULL AFTER id;
ALTER TABLE vehicles ADD data_source_id VARCHAR(32) DEFAULT 'ADMIN_PAGE' NOT NULL AFTER vehicle_id_external;
ALTER TABLE vehicles
  ADD CONSTRAINT vehicles_data_sources_dataSourceID_fk
FOREIGN KEY (data_source_id) REFERENCES data_sources (dataSourceID);