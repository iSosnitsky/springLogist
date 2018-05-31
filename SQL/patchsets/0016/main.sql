INSERT INTO data_sources VALUE ('REQUESTS_ASSIGNER');
ALTER TABLE data_sources ADD dataSourceName VARCHAR(32) NULL;
UPDATE data_sources SET dataSourceName='Создано из системы' WHERE dataSourceID='ADMIN_PAGE';
UPDATE data_sources SET dataSourceName='Создано из 1С' WHERE dataSourceID='LOGIST_1C';
UPDATE data_sources SET dataSourceName='Создано автоматически' WHERE dataSourceID='REQUESTS_ASSIGNER';