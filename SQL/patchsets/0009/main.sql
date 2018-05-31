ALTER TABLE transmaster_transport_db.points ADD requests_count INT DEFAULT 0 NULL;
# DROP EVENT countRequestForPoints;
USE transmaster_transport_db;
CREATE EVENT countRequestForPoints
  ON SCHEDULE EVERY 1 DAY STARTS NOW()
DO
  UPDATE points p, (SELECT DISTINCT destinationPointID AS pID, COUNT(r.requestID) AS count from transmaster_transport_db.points LEFT JOIN requests as r ON destinationPointID=points.pointID  WHERE x<>0.0 AND r.requestDate > (NOW() - INTERVAL 1 MONTH) GROUP BY pointName) rcount
  SET p.requests_count = rcount.count WHERE p.pointID = rcount.pID;