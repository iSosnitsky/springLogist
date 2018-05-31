CREATE INDEX delivery_route_points_pointId_index ON delivery_route_points (pointId);
CREATE INDEX route_points_pointID_index ON route_points (pointID);
CREATE INDEX requests_routeListID_index ON requests (routeListID);
CREATE INDEX route_lists_routeID_index ON route_lists (routeID);

ALTER TABLE route_lists DROP INDEX route_lists_routeID_index;
ALTER TABLE requests DROP INDEX requests_routeListID_index;
ALTER TABLE route_points DROP INDEX route_points_pointID_index;
ALTER TABLE delivery_route_points DROP INDEX delivery_route_points_pointId_index;