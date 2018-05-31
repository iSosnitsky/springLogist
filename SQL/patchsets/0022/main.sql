DROP PROCEDURE IF EXISTS selectClientSideData;
create procedure selectClientSideData ( IN `_userID` INTEGER (10))
  BEGIN

    SET @userRoleID = getRoleIDByUserID(_userID);

    SET @isAdmin = FALSE;
    SET @isMarketAgent = FALSE;
    SET @isClientManager = FALSE;
    SET @isDispatcherOrWDispatcher = FALSE;

    IF (@userRoleID = 'ADMIN')
    THEN
      SET @isAdmin = TRUE;
    ELSEIF (@userRoleID = 'MARKET_AGENT')
      THEN
        SET @isMarketAgent = TRUE;
    ELSEIF (@userRoleID = 'CLIENT_MANAGER')
      THEN
        SET @isClientManager = TRUE;
    ELSEIF (@userRoleID = 'DISPATCHER' OR @userRoleID = 'W_DISPATCHER')
      THEN
        SET @isDispatcherOrWDispatcher = TRUE;
    END IF;

    IF (@isClientManager)
    THEN
      SET @clientID = getClientIDByUserID(_userID);
    END IF;

    IF (@isDispatcherOrWDispatcher)
    THEN
      SET @userPointID = getPointIDByUserID(_userID);
      SET @allRoutesWithUserPointID = selectAllRoutesIDWithThatPointAsString(@userPointID);
    END IF;

    -- 1) если у пользователя роль админ, то показываем все записи из БД
    -- 2) если статус пользователя - агент, то показываем ему только те заявки которые он породил.
    -- 3) если пользователь находится на складе, на котором формируется заявка, то показываем ему эти записи
    -- 4) если маршрут накладной проходит через пользователя, то показываем ему эти записи
    SET @columnsPart =
    '
    SELECT
      requestIDExternal,
      requestNumber,
      requestDate,
      invoiceNumber,
      invoiceDate,
      documentNumber,
      documentDate,
      firma,
      storage,
      boxQty,
      requestStatusID,
      commentForStatus,
      requestStatusRusName,
      clientIDExternal,
      INN,
      clientName,
      marketAgentUserName,
      driverUserName,
      deliveryPointName,
      warehousePointName,
      lastVisitedPointName,
      nextPointName,
      routeListNumber,
      licensePlate,
      palletsQty,
      routeListID,
      routeName,
      arrivalTimeToNextRoutePoint
    FROM mat_view_big_select
    ';

    #     SET @wherePart = '';

    SET @wherePart = CONCAT('WHERE', ' (requestDate>= NOW() - INTERVAL 10 DAY) ');

    #     IF @isAdmin
    #     THEN
    #       SET @wherePart = CONCAT('WHERE ', generateLikeCondition(_search));
    #     ELSEIF @isMarketAgent
    #       THEN
    #         SET @wherePart = CONCAT('WHERE (marketAgentUserID = ', _userID, ') AND ', generateLikeCondition(_search));
    #     ELSEIF @isClientManager
    #       THEN
    #         SET @wherePart = CONCAT('WHERE (clientID = ', @clientID, ') AND ', generateLikeCondition(_search));
    #     ELSEIF @isDispatcherOrWDispatcher
    #       THEN
    #         SET @wherePart = CONCAT('WHERE (routeID IN (', @allRoutesWithUserPointID, ')) AND ',
    #                                 generateLikeCondition(_search));
    #     END IF;

    #     SET @orderByPart = generateOrderByPart(_orderby, _isDesc);
    #
    #     SET @limitPart = ' LIMIT ?, ? ';

    SET @sqlString = CONCAT(@columnsPart, @wherePart);
    #
    PREPARE getDataStm FROM @sqlString;
    #
    #     SET @_startEntry = _startEntry;
    #     SET @_length = _length;
    #
    EXECUTE getDataStm;
    #     USING @_startEntry, @_length;
    DEALLOCATE PREPARE getDataStm;

    -- filtered в случае, если присутвуют фильтры, то возвращается всегда -1.
    -- SELECT 200*40 as `totalFiltered`;

    #     SELECT FOUND_ROWS() AS `totalFiltered`;

    -- total
    #     IF (@isAdmin OR @isMarketAgent OR @isClientManager)
    #     THEN
    #       BEGIN
    #         SELECT total_count AS totalCount
    #         FROM mat_view_row_count_for_user
    #         WHERE mat_view_row_count_for_user.userID = _userID;
    #       END;
    #     ELSEIF (@isDispatcherOrWDispatcher)
    #       THEN
    #         SET @countTotalSql = CONCAT('SELECT COUNT(*) as `totalCount` FROM mat_view_big_select ',
    #                                     CONCAT('WHERE (routeID IN (', @allRoutesWithUserPointID, '))'));
    #         PREPARE getTotalStm FROM @countTotalSql;
    #         EXECUTE getTotalStm;
    #         DEALLOCATE PREPARE getTotalStm;
    #     END IF;

  END
;
