DROP PROCEDURE IF EXISTS selectVehicles;
CREATE PROCEDURE `selectVehicles`(IN `_startEntry` INT(11), IN `_length` INT(11), IN `_orderby` VARCHAR(255),
                                  IN `_isDesc`     TINYINT(1), IN `_search` TEXT)
  BEGIN

    SET @searchString = CONCAT('%', _search, '%');

    SELECT SQL_CALC_FOUND_ROWS *
    FROM transmaster_transport_db.vehicles
    WHERE (
            _search = '' OR
            id LIKE @searchString collate utf8_general_ci OR
            transport_company_id LIKE @searchString collate utf8_general_ci OR
            license_number LIKE @searchString collate utf8_general_ci OR
            model LIKE @searchString collate utf8_general_ci OR
            carrying_capacity LIKE @searchString collate utf8_general_ci OR
            volume LIKE @searchString collate utf8_general_ci OR
            loading_type LIKE @searchString collate utf8_general_ci OR
            pallets_quantity LIKE @searchString collate utf8_general_ci OR
            type LIKE @searchString collate utf8_general_ci
          ) AND deleted = FALSE
    ORDER BY NULL,
      CASE WHEN _orderby = ''
        THEN NULL END,
      CASE WHEN _isDesc AND  _orderby = 'id'
        THEN id END ASC,
      CASE WHEN _isDesc AND  _orderby = 'transport_company_id'
        THEN transport_company_id END ASC,
      CASE WHEN _isDesc AND  _orderby = 'license_number'
        THEN license_number END ASC,
      CASE WHEN _isDesc AND  _orderby = 'model'
        THEN model END ASC,
      CASE WHEN _isDesc AND  _orderby = 'carrying_capacity'
        THEN carrying_capacity END ASC,
      CASE WHEN _isDesc AND _orderby = 'volume'
        THEN volume END ASC,
      CASE WHEN _isDesc AND _orderby = 'loading_type'
        THEN loading_type END ASC,
      CASE WHEN _isDesc AND _orderby = 'pallets_quantity'
        THEN pallets_quantity END ASC,
      CASE WHEN _isDesc AND _orderby = 'type'
        THEN type END ASC,
      CASE WHEN _isDesc AND _orderby = 'is_rented'
        THEN type END ASC,
      CASE WHEN NOT (_isDesc) AND  _orderby = 'id'
        THEN id END DESC,
      CASE WHEN NOT (_isDesc) AND  _orderby = 'transport_company_id'
        THEN transport_company_id END DESC,
      CASE WHEN NOT (_isDesc) AND  _orderby = 'license_number'
        THEN license_number END DESC,
      CASE WHEN NOT (_isDesc) AND  _orderby = 'model'
        THEN model END DESC,
      CASE WHEN NOT (_isDesc) AND  _orderby = 'carrying_capacity'
        THEN carrying_capacity END DESC,
      CASE WHEN NOT (_isDesc) AND _orderby = 'volume'
        THEN volume END DESC,
      CASE WHEN NOT (_isDesc) AND _orderby = 'loading_type'
        THEN loading_type END DESC,
      CASE WHEN NOT (_isDesc) AND _orderby = 'pallets_quantity'
        THEN pallets_quantity END DESC,
      CASE WHEN NOT (_isDesc) AND _orderby = 'type'
        THEN type END DESC,
      CASE WHEN NOT (_isDesc) AND _orderby = 'is_rented'
        THEN type END DESC
    LIMIT _startEntry, _length;

    -- filtered routes
    SELECT FOUND_ROWS() AS `totalFiltered`;

    -- total routes
    SELECT COUNT(*) AS `totalCount`
    FROM vehicles
    WHERE deleted = FALSE;

  END