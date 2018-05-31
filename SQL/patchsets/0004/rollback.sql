ALTER TABLE transmaster_transport_db.tariffs DROP cost_per_box;

DROP PROCEDURE transmaster_transport_db.selectRoutes;
CREATE PROCEDURE transmaster_transport_db.selectRoutes(IN `_startEntry` INT, IN `_length` INT,
                                                       IN `_orderby`    VARCHAR(255), IN `_isDesc` TINYINT(1),
                                                       IN `_search`     TEXT)
  BEGIN

    SET @searchString = CONCAT('%', _search, '%');

    SELECT SQL_CALC_FOUND_ROWS
      r.routeID,
      r.routeName,
      t.tariffID,
      t.cost,
      t.cost_per_point,
      t.cost_per_hour
    FROM transmaster_transport_db.routes r
      LEFT JOIN transmaster_transport_db.tariffs t ON r.tariffID = t.tariffID
    WHERE (
      _search = '' OR
      r.routeName LIKE @searchString collate utf8_general_ci OR
      t.cost LIKE @searchString collate utf8_general_ci OR
      t.cost_per_point LIKE @searchString collate utf8_general_ci OR
      t.cost_per_hour LIKE @searchString collate utf8_general_ci
    )
    ORDER BY NULL,
      CASE WHEN _orderby = ''
        THEN NULL END,
      CASE WHEN _isDesc AND _orderby = 'routeName'
        THEN routeName END ASC,
      CASE WHEN _isDesc AND _orderby = 'cost'
        THEN cost END ASC,
      CASE WHEN _isDesc AND _orderby = 'cost_per_point'
        THEN cost_per_point END ASC,
      CASE WHEN _isDesc AND _orderby = 'cost_per_housr'
        THEN cost_per_hour END ASC,
      CASE WHEN NOT (_isDesc) AND _orderby = 'routeName'
        THEN routeName END DESC,
      CASE WHEN NOT (_isDesc) AND _orderby = 'cost'
        THEN cost END DESC,
      CASE WHEN NOT (_isDesc) AND _orderby = 'cost_per_point'
        THEN cost_per_point END DESC,
      CASE WHEN NOT (_isDesc) AND _orderby = 'cost_per_hour'
        THEN cost_per_hour END DESC
    LIMIT _startEntry, _length;

    -- filtered routes
    SELECT FOUND_ROWS() AS `totalFiltered`;

    -- total routes
    SELECT COUNT(*) AS `totalCount`
    FROM routes;

  END;


