SHOW VARIABLES;
SHOW VARIABLES LIKE '%innodb_log_waits%';



CREATE PROCEDURE examplePrStatement(id VARCHAR(255), _orderBy VARCHAR(255))
  BEGIN
    SET @s = CONCAT('SELECT * FROM points WHERE pointID <> ? ORDER BY ', _orderBy);
    PREPARE statement FROM @s;
    SET @_id = id;
    EXECUTE statement USING @_id;
    DEALLOCATE PREPARE statement;
  END;


DROP PROCEDURE test_ttdb.selectData;

-- ----------------------------

USE test_ttdb;
CALL refreshMaterializedView();


-- MARKET_AGENT
(SELECT
   big_select_materialized.marketAgentUserID,
   users.userRoleID,
   COUNT(big_select_materialized.requestID)
 FROM users
   INNER JOIN big_select_materialized ON (users.userID = big_select_materialized.marketAgentUserID)
 GROUP BY big_select_materialized.marketAgentUserID
 ORDER BY NULL)

UNION ALL
-- CLIENT_MANAGER
(SELECT
   users.userID,
   users.userRoleID,
   COUNT(big_select_materialized.requestID)
 FROM users
   INNER JOIN big_select_materialized ON (users.clientID = big_select_materialized.clientID)
 GROUP BY big_select_materialized.clientID
 ORDER BY NULL)

UNION ALL
-- ADMIN
(SELECT
   users.userID,
   users.userRoleID,
   COUNT(big_select_materialized.requestID)
 FROM users
   INNER JOIN big_select_materialized
 WHERE userRoleID = 'ADMIN'
 GROUP BY users.userID
 ORDER BY NULL);

CREATE INDEX ind3 ON big_select_materialized (requestDate);
DROP INDEX ind2 ON big_select_materialized;
CREATE FULLTEXT INDEX ind6 ON big_select_materialized (documentNumber(10));

USE test_ttdb;

-- CREATE FULLTEXT INDEX f_text_index ON big_select_materialized (requestIDExternal(10));
CALL selectData(1, 120, 40, '', TRUE, 'requestIDExternal,12;');

-- узнать размер таблицы
SELECT TABLE_NAME, DATA_LENGTH FROM information_schema.TABLES WHERE TABLE_NAME = 'text_index'; -- 8мб при colID TINYINT