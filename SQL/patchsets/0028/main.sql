INSERT INTO user_roles (userRoleID, userRoleRusName) VALUE ('TRANSPORT_COMPANY','Транспортная компания');
ALTER TABLE users ADD transport_company_id INT NULL;
ALTER TABLE users
  ADD CONSTRAINT users_transport_companies_id_fk
FOREIGN KEY (transport_company_id) REFERENCES transport_companies (id);

DROP VIEW IF EXISTS all_users;
create view all_users as
  select
    `u`.`userID`          AS `userId`,
    `u`.`login`           AS `login`,
    `u`.`userName`        AS `userName`,
    `u`.`position`        AS `position`,
    `u`.`phoneNumber`     AS `phoneNumber`,
    `u`.`email`           AS `email`,
    'dummy'               AS `password`,
    `p`.`pointName`       AS `pointName`,
    `r`.`userRoleRusName` AS `userRoleRusName`,
    `c`.`clientID`        AS `clientID`,
    `tc`.name AS `transport_company_id`
  from (((`users` `u` left join `points` `p`
      on ((`p`.`pointID` = `u`.`pointID`))) left join `clients` `c`
      on ((`c`.`clientID` = `u`.`clientID`))) join `user_roles` `r`
      on ((`r`.`userRoleID` = `u`.`userRoleID`))) LEFT JOIN transport_companies tc
      ON (u.transport_company_id = tc.id);


DROP PROCEDURE IF EXISTS selectUsers;
create procedure selectUsers ( IN `_startEntry` INTEGER (10), IN `_length` INTEGER (10), IN `_orderby` VARCHAR (255), IN `_isDesc` BOOLEAN, IN `_search` TEXT)
  BEGIN

    SET @searchString = CONCAT('%', _search, '%');

    SELECT SQL_CALC_FOUND_ROWS *
    FROM transmaster_transport_db.all_users
    WHERE (_search = '' OR
           userName LIKE @searchString collate utf8_general_ci OR
           login LIKE @searchString collate utf8_general_ci OR
           position LIKE @searchString collate utf8_general_ci OR
           phoneNumber LIKE @searchString collate utf8_general_ci OR
           email LIKE @searchString collate utf8_general_ci OR
           transport_company_id LIKE @searchString collate utf8_general_ci OR
           pointName LIKE @searchString collate utf8_general_ci)
    ORDER BY NULL,
      CASE WHEN _orderby = ''
        THEN NULL END,
      CASE WHEN _isDesc AND _orderby = 'userId'
        THEN userId END ASC,
      CASE WHEN _isDesc AND _orderby = 'login'
        THEN login END ASC,
      CASE WHEN _isDesc AND _orderby = 'userName'
        THEN userName END ASC,
      CASE WHEN _isDesc AND _orderby = 'position'
        THEN position END ASC,
      CASE WHEN _isDesc AND _orderby = 'phoneNumber'
        THEN phoneNumber END ASC,
      CASE WHEN _isDesc AND _orderby = 'email'
        THEN email END ASC,
      CASE WHEN _isDesc AND _orderby = 'pointName'
        THEN pointName END ASC,
      CASE WHEN _isDesc AND _orderby = 'clientID'
        THEN clientID END ASC,
      CASE WHEN _isDesc AND _orderby = 'transport_company_id'
        THEN transport_company_id END ASC,
      CASE WHEN NOT (_isDesc) AND _orderby = 'userId'
        THEN userId END ASC,
      CASE WHEN NOT (_isDesc) AND _orderby = 'login'
        THEN login END DESC,
      CASE WHEN NOT (_isDesc) AND _orderby = 'userName'
        THEN userName END DESC,
      CASE WHEN NOT (_isDesc) AND _orderby = 'position'
        THEN position END DESC,
      CASE WHEN NOT (_isDesc) AND _orderby = 'phoneNumber'
        THEN phoneNumber END DESC,
      CASE WHEN NOT (_isDesc) AND _orderby = 'email'
        THEN email END DESC,
      CASE WHEN NOT (_isDesc) AND _orderby = 'pointName'
        THEN pointName END DESC,
      CASE WHEN NOT (_isDesc) AND _orderby = 'transport_company_id'
        THEN transport_company_id END DESC,
      CASE WHEN NOT (_isDesc) AND _orderby = 'clientID'
        THEN clientID END DESC
    LIMIT _startEntry, _length;

    -- filtered users
    SELECT FOUND_ROWS() AS `totalFiltered`;

    -- total users
    SELECT COUNT(*) AS `totalCount`
    FROM all_users;

  END
;

GRANT EXECUTE ON PROCEDURE test_ttdb.selectUsers TO 'test_db_user'@'localhost';
