BEGIN;

DROP PROCEDURE transmaster_transport_db.selectTransportCompanies;

-- select transport companies procedure
-- _search - строка для глобального поиска по всем колонкам
CREATE PROCEDURE transmaster_transport_db.selectTransportCompanies(
  _startEntry INTEGER,
  _length     INTEGER,
  _orderby    VARCHAR(255),
  _isDesc     BOOLEAN,
  _search     TEXT
)
  BEGIN

    SET @searchString = CONCAT('%', _search, '%');

    SELECT SQL_CALC_FOUND_ROWS
      tc.*
    FROM transmaster_transport_db.transport_companies tc
    WHERE (
            _search = '' OR
            tc.id LIKE @searchString collate utf8_general_ci  OR
            tc.name LIKE @searchString collate utf8_general_ci OR
            tc.short_name LIKE @searchString collate utf8_general_ci OR
            tc.inn LIKE @searchString collate utf8_general_ci OR
            tc.KPP LIKE @searchString collate utf8_general_ci OR
            tc.BIK LIKE @searchString collate utf8_general_ci OR
            tc.cor_account LIKE @searchString collate utf8_general_ci OR
            tc.cur_account LIKE @searchString collate utf8_general_ci OR
            tc.bank_name LIKE @searchString collate utf8_general_ci OR
            tc.legal_address LIKE @searchString collate utf8_general_ci OR
            tc.post_address LIKE @searchString collate utf8_general_ci OR
            tc.keywords LIKE @searchString collate utf8_general_ci OR
            tc.director_fullname LIKE @searchString collate utf8_general_ci OR
            tc.chief_acc_fullname LIKE @searchString collate utf8_general_ci
          ) AND deleted = FALSE
    ORDER BY NULL,
      CASE WHEN _orderby = ''
        THEN NULL END,
      CASE WHEN _isDesc AND  _orderby = 'id'
        THEN id END ASC,
      CASE WHEN _isDesc AND  _orderby = 'name'
        THEN name END ASC,
      CASE WHEN _isDesc AND  _orderby = 'short_name'
        THEN short_name END ASC,
      CASE WHEN _isDesc AND  _orderby = 'inn'
        THEN inn END ASC,
      CASE WHEN _isDesc AND  _orderby = 'KPP'
        THEN KPP END ASC,
      CASE WHEN _isDesc AND _orderby = 'BIK'
        THEN BIK END ASC,
      CASE WHEN _isDesc AND _orderby = 'cor_account'
        THEN cor_account END ASC,
      CASE WHEN _isDesc AND _orderby = 'cur_account'
        THEN cur_account END ASC,
      CASE WHEN _isDesc AND _orderby = 'bank_name'
        THEN bank_name END ASC,
      CASE WHEN _isDesc AND _orderby = 'legal_address'
        THEN legal_address END ASC,
      CASE WHEN _isDesc AND _orderby = 'post_address'
        THEN post_address END ASC,
      CASE WHEN _isDesc AND _orderby = 'keywords'
        THEN keywords END ASC,
      CASE WHEN _isDesc AND _orderby = 'director_fullname'
        THEN director_fullname END ASC,
      CASE WHEN _isDesc AND _orderby = 'chief_acc_fullname'
        THEN chief_acc_fullname END ASC,
      CASE WHEN NOT (_isDesc) AND  _orderby = 'id'
        THEN id END DESC ,
      CASE  WHEN NOT (_isDesc) AND  _orderby = 'name'
        THEN name END DESC,
      CASE  WHEN NOT (_isDesc) AND  _orderby = 'short_name'
        THEN short_name END DESC,
      CASE  WHEN NOT (_isDesc) AND  _orderby = 'inn'
        THEN inn END DESC,
      CASE  WHEN NOT (_isDesc) AND  _orderby = 'KPP'
        THEN KPP END DESC,
      CASE  WHEN NOT (_isDesc) AND _orderby = 'BIK'
        THEN BIK END DESC,
      CASE  WHEN NOT (_isDesc) AND _orderby = 'cor_account'
        THEN cor_account END DESC,
      CASE  WHEN NOT (_isDesc) AND _orderby = 'cur_account'
        THEN cur_account END DESC,
      CASE  WHEN NOT (_isDesc) AND _orderby = 'bank_name'
        THEN bank_name END DESC,
      CASE  WHEN NOT (_isDesc) AND _orderby = 'legal_address'
        THEN legal_address END DESC,
      CASE  WHEN NOT (_isDesc) AND _orderby = 'post_address'
        THEN post_address END DESC,
      CASE  WHEN NOT (_isDesc) AND _orderby = 'keywords'
        THEN keywords END DESC,
      CASE  WHEN NOT (_isDesc) AND _orderby = 'director_fullname'
        THEN director_fullname END DESC,
      CASE  WHEN NOT (_isDesc) AND _orderby = 'chief_acc_fullname'
        THEN chief_acc_fullname END DESC
    LIMIT _startEntry, _length;

    -- filtered routes
    SELECT FOUND_ROWS() AS `totalFiltered`;

    -- total routes
    SELECT COUNT(*) AS `totalCount`
    FROM transport_companies
    WHERE deleted = FALSE;

  END;

DROP PROCEDURE transmaster_transport_db.selectVehicles;

-- select vehicles procedure
-- _search - строка для глобального поиска по всем колонкам
CREATE PROCEDURE transmaster_transport_db.selectVehicles(
  _startEntry INTEGER,
  _length     INTEGER,
  _orderby    VARCHAR(255),
  _isDesc     BOOLEAN,
  _search     TEXT
)
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
        THEN type END DESC
    LIMIT _startEntry, _length;

    -- filtered routes
    SELECT FOUND_ROWS() AS `totalFiltered`;

    -- total routes
    SELECT COUNT(*) AS `totalCount`
    FROM vehicles
    WHERE deleted = FALSE;

  END;

DROP PROCEDURE transmaster_transport_db.selectDrivers;

-- select drivers procedure
-- _search - строка для глобального поиска по всем колонкам
CREATE PROCEDURE transmaster_transport_db.selectDrivers(
  _startEntry INTEGER,
  _length     INTEGER,
  _orderby    VARCHAR(255),
  _isDesc     BOOLEAN,
  _search     TEXT
)
  BEGIN

    SET @searchString = CONCAT('%', _search, '%');

    SELECT SQL_CALC_FOUND_ROWS *
    FROM transmaster_transport_db.drivers
    WHERE (_search = '' OR
           id LIKE @searchString collate utf8_general_ci OR
           vehicle_id LIKE @searchString collate utf8_general_ci OR
           transport_company_id LIKE @searchString collate utf8_general_ci OR
           full_name LIKE @searchString collate utf8_general_ci OR
           passport LIKE @searchString collate utf8_general_ci OR
           phone LIKE @searchString collate utf8_general_ci OR
           license LIKE @searchString collate utf8_general_ci) AND deleted = FALSE
    ORDER BY NULL,
      CASE WHEN _orderby = ''
        THEN NULL END,
      CASE WHEN _isDesc AND  _orderby = 'id'
        THEN id END ASC,
      CASE WHEN _isDesc AND  _orderby = 'vehicle_id'
        THEN vehicle_id END ASC,
      CASE WHEN _isDesc AND  _orderby = 'transport_company_id'
        THEN transport_company_id END ASC,
      CASE WHEN _isDesc AND  _orderby = 'full_name'
        THEN full_name END ASC,
      CASE WHEN _isDesc AND  _orderby = 'passport'
        THEN passport END ASC,
      CASE WHEN _isDesc AND _orderby = 'phone'
        THEN phone END ASC,
      CASE WHEN _isDesc AND _orderby = 'license'
        THEN license END ASC,
      CASE WHEN NOT (_isDesc) AND  _orderby = 'id'
        THEN id END DESC,
      CASE WHEN NOT (_isDesc) AND  _orderby = 'vehicle_id'
        THEN vehicle_id END DESC,
      CASE WHEN NOT (_isDesc) AND  _orderby = 'transport_company_id'
        THEN transport_company_id END DESC,
      CASE WHEN NOT (_isDesc) AND  _orderby = 'full_name'
        THEN full_name END DESC,
      CASE WHEN NOT (_isDesc) AND  _orderby = 'passport'
        THEN passport END DESC,
      CASE WHEN NOT (_isDesc) AND  _orderby = 'phone'
        THEN phone END DESC,
      CASE WHEN NOT (_isDesc) AND _orderby = 'license'
        THEN license END DESC
    LIMIT _startEntry, _length;

    -- filtered routes
    SELECT FOUND_ROWS() AS `totalFiltered`;

    -- total routes
    SELECT COUNT(*) AS `totalCount`
    FROM drivers
    WHERE deleted = FALSE;

  END;

DROP PROCEDURE transmaster_transport_db.selectRoutes;

-- select routes procedure
-- _search - строка для глобального поиска по всем колонкам
CREATE PROCEDURE transmaster_transport_db.selectRoutes(
  _startEntry INTEGER,
  _length     INTEGER,
  _orderby    VARCHAR(255),
  _isDesc     BOOLEAN,
  _search     TEXT
)
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

DROP PROCEDURE IF EXISTS transmaster_transport_db.selectUsers;

-- select users procedure
-- _search - строка для глобального поиска по всем колонкам
CREATE PROCEDURE transmaster_transport_db.selectUsers(
  _startEntry INTEGER, _length INTEGER,
  _orderby    VARCHAR(255),
  _isDesc     BOOLEAN,
  _search TEXT
)
  BEGIN

    SET @searchString = CONCAT('%', _search, '%');

    SELECT SQL_CALC_FOUND_ROWS *
    FROM transmaster_transport_db.all_users
    WHERE (_search = '' OR
           userName LIKE @searchString collate utf8_general_ci OR
           position LIKE @searchString collate utf8_general_ci OR
           phoneNumber LIKE @searchString collate utf8_general_ci OR
           email LIKE @searchString collate utf8_general_ci OR
           pointName LIKE @searchString collate utf8_general_ci OR
           userRoleRusName LIKE @searchString collate utf8_general_ci)
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
      CASE WHEN _isDesc AND _orderby = 'userRoleRusName'
        THEN userRoleRusName END ASC,
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
      CASE WHEN NOT (_isDesc) AND _orderby = 'userRoleRusName'
        THEN userRoleRusName END DESC,
      CASE WHEN NOT (_isDesc) AND _orderby = 'clientID'
        THEN clientID END DESC
    LIMIT _startEntry, _length;

    -- filtered users
    SELECT FOUND_ROWS() AS `totalFiltered`;

    -- total users
    SELECT COUNT(*) AS `totalCount`
    FROM all_users;

  END;

COMMIT;