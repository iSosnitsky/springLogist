package sbat.logist.ru.transport.EntityListeners;

import org.springframework.stereotype.Component;
import sbat.logist.ru.transport.domain.RoutePoint;

import javax.persistence.PrePersist;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Component
public class RoutePointListener {

    @PrePersist
    public void prePersist(RoutePoint routePoint) {
        if (routePoint.getSortOrder() == -1) {
            List<RoutePoint> routePoints = Objects.requireNonNull(routePoint.getRoute().getRoutePoints());

            RoutePoint maxedRoutePoint = routePoints.stream()
                    .max(Comparator.comparingInt(RoutePoint::getSortOrder))
                    .orElse(null);
            if(maxedRoutePoint!=null){
                routePoint.setSortOrder(maxedRoutePoint.getSortOrder()+1);
            } else {
                routePoint.setSortOrder(1);
            }
        }
    }
}
