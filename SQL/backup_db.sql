SET GLOBAL sql_mode = 'STRICT_TRANS_TABLES';
DROP DATABASE IF EXISTS backup_db;
CREATE DATABASE backup_db
  CHARACTER SET utf8
  COLLATE utf8_bin;
USE backup_db;


CREATE TABLE exchange (
  packageNumber  INTEGER,
  serverName     VARCHAR(32),
  packageAdded   TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
  packageCreated DATETIME,
  packageData    LONGTEXT,
  PRIMARY KEY (packageNumber, serverName)
);