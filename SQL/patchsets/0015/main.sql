DROP FUNCTION IF EXISTS caltulateDistanceBetweenPoints;
CREATE FUNCTION calculateDistanceBetweenPoints(lon1 DOUBLE, lat1 DOUBLE, lon2 DOUBLE, lat2 DOUBLE)
  RETURNS DOUBLE
  BEGIN
    RETURN (6371 * acos( cos( radians(lat1) )
                         * cos( radians(lat2) )
                         * cos( radians(lon2) - radians(lon1)) + sin(radians(lat1))
                                                                 * sin( radians(lat2) )));
  END;