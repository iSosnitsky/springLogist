GRANT ALL ON TABLE clients TO 'andy'@'localhost';

CREATE PROCEDURE selectClients(IN `_startEntry` INT, IN `_length` INT, IN `_orderby` VARCHAR(255),
                               IN `_isDesc`     TINYINT(1), IN `_search` TEXT)
  BEGIN

    SET @searchString = CONCAT('%', _search, '%');

    SELECT SQL_CALC_FOUND_ROWS *
    FROM transmaster_transport_db.clients
    WHERE (_search = '' OR
           INN LIKE @searchString collate utf8_general_ci OR
           clientName LIKE @searchString collate utf8_general_ci OR
           KPP LIKE @searchString collate utf8_general_ci OR
           corAccount LIKE @searchString collate utf8_general_ci OR
           curAccount LIKE @searchString collate utf8_general_ci OR
           contractNumber LIKE @searchString collate utf8_general_ci OR
           BIK LIKE @searchString collate utf8_general_ci)
    ORDER BY NULL,
      CASE WHEN _orderby = ''
        THEN NULL END,
      CASE WHEN _isDesc AND _orderby = 'INN'
        THEN INN END ASC,
      CASE WHEN _isDesc AND _orderby = 'clientName'
        THEN clientName END ASC,
      CASE WHEN _isDesc AND _orderby = 'KPP'
        THEN KPP END ASC,
      CASE WHEN _isDesc AND _orderby = 'contractNumber'
        THEN contractNumber END ASC,
      CASE WHEN _isDesc AND _orderby = 'corAccount'
        THEN corAccount END ASC,
      CASE WHEN _isDesc AND _orderby = 'curAccount'
        THEN curAccount END ASC,
      CASE WHEN _isDesc AND _orderby = 'BIK'
        THEN BIK END ASC,
      CASE WHEN NOT (_isDesc) AND _orderby = 'INN'
        THEN INN END ASC,
      CASE WHEN NOT (_isDesc) AND _orderby = 'clientName'
        THEN clientName END DESC,
      CASE WHEN NOT (_isDesc) AND _orderby = 'KPP'
        THEN KPP END DESC,
      CASE WHEN NOT (_isDesc) AND _orderby = 'contractNumber'
        THEN contractNumber END DESC,
      CASE WHEN NOT (_isDesc) AND _orderby = 'corAccount'
        THEN corAccount END DESC,
      CASE WHEN NOT (_isDesc) AND _orderby = 'curAccount'
        THEN curAccount END DESC,
      CASE WHEN NOT (_isDesc) AND _orderby = 'BIK'
        THEN BIK END DESC
    LIMIT _startEntry, _length;

    -- filtered users
    SELECT FOUND_ROWS() AS `totalFiltered`;

    -- total users
    SELECT COUNT(*) AS `totalCount`
    FROM clients;

  END;


