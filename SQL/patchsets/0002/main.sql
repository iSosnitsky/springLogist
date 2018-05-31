BEGIN;

DROP TRIGGER IF EXISTS transmaster_transport_db.before_request_update;

CREATE TRIGGER before_request_update BEFORE UPDATE ON requests
FOR EACH ROW
  -- берем пользователя, который изменил статус на один из request statuses, затем находим его пункт маршрута, и этот
  -- пункт записываем в таблицу requests в поле lastVisitedRoutePointID
  -- находим маршрут по которому едет накладная.
  IF (NEW.routeListID IS NOT NULL)
  THEN
    BEGIN

      SET @routeID = (SELECT routeID
                      FROM route_lists
                      WHERE NEW.routeListID = route_lists.routeListID);
      -- находим пункт пользователя, который изменил статус накладной.
      -- при появлении маршрутного листа сразу выставляем последний посещенный пункт, как первый пункт маршрута
      IF (NEW.lastVisitedRoutePointID IS NULL)
      THEN
        BEGIN
          -- установка самого первого пункта маршрута
          SET NEW.lastVisitedRoutePointID = (SELECT routePointID
                                             FROM route_points
                                             WHERE (routeID = @routeID)
                                             ORDER BY sortOrder
                                             LIMIT 1);
          IF (OLD.routeListID IS NULL)
          THEN
            BEGIN
              SET NEW.requestStatusID = 'DEPARTURE';
            END;
          END IF;
        END;
      -- если была установка статуса в ARRIVED, то последний посещенный пункт меняется на следующий
      ELSEIF (NEW.requestStatusID = 'ARRIVED' AND NEW.lastVisitedRoutePointID IS NOT NULL)
        THEN
          BEGIN
            -- получаем порядковый номер последнего routePoint
            SET @lastRoutePointSortOrder = (SELECT sortOrder
                                            FROM route_points
                                            WHERE route_points.routePointID = OLD.lastVisitedRoutePointID);
            -- устанавливаем следующий пункт маршрута
            SET NEW.lastVisitedRoutePointID = (SELECT routePointID
                                               FROM route_points
                                               WHERE (routeID = @routeID AND sortOrder > @lastRoutePointSortOrder)
                                               ORDER BY sortOrder
                                               LIMIT 1);
          END;
      ELSEIF (NEW.requestStatusID = 'ERROR' AND NEW.lastVisitedRoutePointID IS NOT NULL)
        THEN
          BEGIN
            -- получаем порядковый номер последнего routePoint
            SET @lastRoutePointSortOrder = (SELECT sortOrder
                                            FROM route_points
                                            WHERE route_points.routePointID = OLD.lastVisitedRoutePointID);
            -- устанавливаем предыдущий пункт маршрута
            SET NEW.lastVisitedRoutePointID = (SELECT routePointID
                                               FROM route_points
                                               WHERE (routeID = @routeID AND sortOrder < @lastRoutePointSortOrder)
                                               ORDER BY sortOrder
                                               LIMIT 1);
          END;
      ELSEIF (NEW.requestStatusID = 'DELIVERED' AND NEW.lastVisitedRoutePointID IS NOT NULL)
        THEN
          BEGIN
            -- установка самого последнего пункта маршрута
            SET NEW.lastVisitedRoutePointID = (SELECT routePointID
                                               FROM route_points
                                               WHERE (routeID = @routeID)
                                               ORDER BY sortOrder DESC
                                               LIMIT 1);
          END;
      END IF;
    END;
  ELSEIF (OLD.routeListID IS NOT NULL)
    THEN
      SET NEW.lastVisitedRoutePointID = NULL;
  END IF;

COMMIT;