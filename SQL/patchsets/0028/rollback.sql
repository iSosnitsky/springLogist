DROP PROCEDURE IF EXISTS selectUsers;
create procedure selectUsers ( IN `_startEntry` INTEGER (10), IN `_length` INTEGER (10), IN `_orderby` VARCHAR (255), IN `_isDesc` BOOLEAN(0), IN `_search` TEXT(65535))
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
