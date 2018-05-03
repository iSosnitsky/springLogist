package sbat.logist.ru.transport.domain.specification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import sbat.logist.ru.transport.domain.Client;
import sbat.logist.ru.transport.domain.MatViewBigSelect;
import sbat.logist.ru.transport.domain.Request;
import sbat.logist.ru.transport.domain.User;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class RequestForUser {
    public static Specification<MatViewBigSelect> requestsForMarketAgent(final User user) {
        return (final Root<MatViewBigSelect> root, final CriteriaQuery<?> criteriaQuery, final CriteriaBuilder criteriaBuilder) -> {
            final List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.equal(root.get("marketAgentUserID"), user.getUserID()));
            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

    public static Specification<MatViewBigSelect> requestsForClient(final Client client) {
        return (final Root<MatViewBigSelect> root, final CriteriaQuery<?> criteriaQuery, final CriteriaBuilder criteriaBuilder) -> {
            final List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.equal(root.get("clientID"), client.getClientID()));
            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

    public static Specification<MatViewBigSelect> requestsForDispatcher(final Client client) {
        return (final Root<MatViewBigSelect> root, final CriteriaQuery<?> criteriaQuery, final CriteriaBuilder criteriaBuilder) -> {
            final List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.equal(root.get("ClientID"), client.getClientID()));
            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }
}
