package com.doanth.appointment_service.repository;

import com.doanth.appointment_service.models.Appointment;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class FilterableAppointmentRepositoryImpl implements FilterableAppointmentRepository {

    @Autowired
    EntityManager entityManager;

    @Override
    public Page<Appointment> listWithFilter(Pageable pageable, Map<String, Object> filterFields) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Appointment> entityQuery = builder.createQuery(Appointment.class);

        Root<Appointment> entityRoot = entityQuery.from(Appointment.class);
//        entityQuery.select(builder.construct(Appointment.class,
//                entityRoot.get("code"), entityRoot.get("cityName"),
//                entityRoot.get("regionName"), entityRoot.get("countryName"),
//                entityRoot.get("countryCode"), entityRoot.get("enabled")));

        Predicate[] predicates = createPredicates(filterFields, builder, entityRoot);

        if (predicates.length > 0) entityQuery.where(predicates);

        List<Order> listOrder = new ArrayList<>();

        pageable.getSort().stream().forEach(order -> {
            System.out.println("Order field: " + order.getProperty());

            if (order.isAscending()) {
                listOrder.add(builder.asc(entityRoot.get(order.getProperty())));
            } else {
                listOrder.add(builder.desc(entityRoot.get(order.getProperty())));
            }
        });

        entityQuery.orderBy(listOrder);

        TypedQuery<Appointment> typedQuery = entityManager.createQuery(entityQuery);

        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        List<Appointment> listResult = typedQuery.getResultList();

        long totalRows = getTotalRows(filterFields);

        return new PageImpl<>(listResult, pageable, totalRows);
    }

//    @Override
//    public Page<Appointment> listWithFilterNotEqual(Pageable pageable, Map<String, Object> filterFields) {
//        return null;
//    }

    private Predicate[] createPredicates(Map<String, Object> filterFields, CriteriaBuilder builder, Root<Appointment> root) {
        Predicate[] predicates = new Predicate[filterFields.size() + 1];

        if (!filterFields.isEmpty()) {
            Iterator<String> iterator = filterFields.keySet().iterator();
            int i = 0;

            while (iterator.hasNext()) {
                String fieldName = iterator.next();
                Object filterValue = filterFields.get(fieldName);

                System.out.println(fieldName + " => " + filterValue);

                // Handle nested properties (like specialty.specialtyId)
                if (fieldName.contains(".")) {
                    String[] parts = fieldName.split("\\.");
                    Path<?> path = root;
                    for (String part : parts) {
                        path = path.get(part);
                    }

                    if (fieldName.startsWith("ne_")) {
                        String realField = fieldName.substring(3);
                        predicates[i++] = builder.notEqual(path, filterValue);
                    } else {
                        predicates[i++] = builder.equal(path, filterValue);
                    }
                } else {
                    if (fieldName.startsWith("ne_")) {
                        String realField = fieldName.substring(3);
                        predicates[i++] = builder.notEqual(root.get(realField), filterValue);
                    } else {
                        predicates[i++] = builder.equal(root.get(fieldName), filterValue);
                    }
                }
            }
        }

        predicates[predicates.length - 1] = builder.equal(root.get("trashed"), false);
        return predicates;
    }

    private long getTotalRows(Map<String, Object> filterFields) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);

        Root<Appointment> countRoot = countQuery.from(Appointment.class);

        countQuery.select(builder.count(countRoot));

        Predicate[] predicates = createPredicates(filterFields, builder, countRoot);

        if (predicates.length > 0) countQuery.where(predicates);

        Long rowCount = entityManager.createQuery(countQuery).getSingleResult();
        System.out.println("Total rows: " + rowCount);
        System.out.println("dieu kien " + Arrays.stream(predicates).toList());
        return rowCount;
    }
}
