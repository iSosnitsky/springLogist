CREATE VIEW transmaster_transport_db.big_select AS
  SELECT
    requests.requestID, -- служебное поле
    requests.requestIDExternal,
    requests.requestNumber,
    requests.requestDate,
    requests.invoiceNumber,
    requests.invoiceDate,
    requests.documentNumber,
    requests.documentDate,
    requests.firma,
    requests.storage,
    requests.commentForStatus,
    requests.boxQty,
    requests.marketAgentUserID, -- служебное поле
    requests.requestStatusID, -- служебное поле
    requests.routeListID, -- служебное поле
    request_statuses.requestStatusRusName,
    clients.clientID, -- служебное поле
    clients.clientIDExternal,
    clients.INN,
    clients.clientName,
    users.userName,
    delivery_points.pointName                              AS deliveryPointName,
    w_points.pointName                                     AS warehousePointName,
    w_points.pointID                                       AS warehousePointID, -- служебное поле
    last_visited_points.pointName                          AS currentPointName,
    next_route_points.pointName                            AS nextPointName,
    routes.routeID, -- служебное поле
    routes.routeName,
    getUserNameByID(route_lists.driverID) AS driverId,
    route_lists.licensePlate,
    route_lists.palletsQty,
    route_lists.routeListNumber,
    getArrivalDateTime(routes.routeID, requests.requestID) AS arrivalTime

  FROM requests
    INNER JOIN (request_statuses, clients, users)
      ON (
      requests.requestStatusID = request_statuses.requestStatusID AND
      requests.clientID = clients.clientID AND
      requests.marketAgentUserID = users.userID
      )
    LEFT JOIN (points AS delivery_points, points AS w_points)
      ON (
      requests.warehousePointID = w_points.pointID AND
      requests.destinationPointID = delivery_points.pointID
      )
    -- because routeList in requests table can be null, we use left join.
    LEFT JOIN (route_lists, routes)
      ON (
      requests.routeListID = route_lists.routeListID AND
      route_lists.routeID = routes.routeID
      )
    LEFT JOIN (route_points AS rp1, points AS last_visited_points)
      ON (
      rp1.routePointID = requests.lastVisitedRoutePointID AND
      last_visited_points.pointID = rp1.pointID
      )
    LEFT JOIN (route_points AS rp2, points AS next_route_points)
      ON (
      route_lists.routeID = routes.routeID AND
      routes.routeID = rp2.routeID AND
      rp2.routePointID = getNextRoutePointID(routes.routeID, requests.lastVisitedRoutePointID) AND
      next_route_points.pointID = rp2.pointID
      );


-- get DATE and TIME when route should be finished
CREATE FUNCTION getArrivalDateTime(_routeID INTEGER, _requestID INTEGER)
  RETURNS TIMESTAMP
  BEGIN
    DECLARE $routeStartDate TIMESTAMP;
    DECLARE $arrivalDateTime TIMESTAMP;


    SET $routeStartDate = (SELECT lastStatusUpdated
                           FROM requests_history
                           WHERE _requestID = requestID AND requestStatusID = 'DEPARTURE'
                           ORDER BY lastStatusUpdated
                           LIMIT 1
    );

    IF ($routeStartDate IS NULL) THEN
      RETURN NULL;
    END IF;


    SET $arrivalDateTime = (SELECT TIMESTAMPADD(MINUTE, getDurationForRoute(_routeID), $routeStartDate));
    RETURN $arrivalDateTime;
  END;



-- get total time in minutes consumed for delivery
CREATE FUNCTION getDurationForRoute(_routeID INTEGER)
  RETURNS VARCHAR(255)
  BEGIN
    DECLARE done INT DEFAULT FALSE;
    DECLARE $currentRoutePointID INTEGER;
    DECLARE $previousRoutePointID INTEGER;
    DECLARE $timeForLoadingOperations INTEGER;
    DECLARE $timeCount INTEGER DEFAULT 0;
    DECLARE $timeResult INTEGER DEFAULT 0;
    DECLARE cur CURSOR FOR
      SELECT
        routePointID,
        timeForLoadingOperations
      FROM route_points
      WHERE _routeID = route_points.routeID
      ORDER BY sortOrder;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

    OPEN cur;

    read_loop: LOOP
      FETCH cur
      INTO $currentRoutePointID, $timeForLoadingOperations;
      IF done
      THEN LEAVE read_loop; END IF;
      IF $previousRoutePointID IS NOT NULL
      THEN
        SET $timeCount = (SELECT timeForDistance
                          FROM relations_between_route_points
                          WHERE
                            routePointIDFirst = $previousRoutePointID AND routePointIDSecond = $currentRoutePointID);
      END IF;
      SET $timeResult = $timeResult + $timeCount + $timeForLoadingOperations;
      SET $previousRoutePointID = $currentRoutePointID;
    END LOOP;

    CLOSE cur;
    RETURN $timeResult;
    -- SELECT firstPointArrivalTime FROM routes WHERE routeID = _routeID;
  END;


CREATE FUNCTION getNextRoutePointID(_routeID INTEGER, _lastVisitedRoutePointID INTEGER)
  RETURNS INTEGER
  BEGIN
    -- получаем порядковый номер последнего routePoint
    SET @lastRoutePointSortOrder = (SELECT sortOrder
                                    FROM route_points
                                    WHERE route_points.routePointID = _lastVisitedRoutePointID);
    -- устанавливаем следующий пункт маршрута
    RETURN (SELECT routePointID
            FROM route_points
            WHERE (routeID = _routeID AND sortOrder > @lastRoutePointSortOrder)
            ORDER BY sortOrder
            LIMIT 1);
  END;


SELECT
  requests_history.requestID,
  requests_history.lastStatusUpdated,
  MAX(requests_history.lastStatusUpdated) AS departureTimeFromLastRoutePoint
FROM requests_history
WHERE requests_history.requestStatusID = 'DEPARTURE'
GROUP BY requestID
ORDER BY NULL;

