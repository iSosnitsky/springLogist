CREATE TABLE exchange_log
(
  entry_id INT(10) AUTO_INCREMENT PRIMARY KEY,
  packet_id INT(10)  NOT NULL,
  date     DATETIME  NULL,
  server VARCHAR(64) NULL ,
  status   ENUM ('OK','ERROR') NULL
);
GRANT ALL ON TABLE transmaster_transport_db.exchange_log TO 'andy'@'localhost'