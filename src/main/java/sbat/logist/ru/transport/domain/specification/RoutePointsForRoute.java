package sbat.logist.ru.transport.domain.specification;

import org.springframework.data.jpa.domain.Specification;
import sbat.logist.ru.transport.domain.Route;
import sbat.logist.ru.transport.domain.RoutePoint;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class RoutePointsForRoute {
    public static Specification<RoutePoint> routePointsForRoute(final Route route) {
        return (final Root<RoutePoint> root, final CriteriaQuery<?> criteriaQuery, final CriteriaBuilder criteriaBuilder) -> {
            final List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.equal(root.get("route"), route.getId()));
            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }
}
