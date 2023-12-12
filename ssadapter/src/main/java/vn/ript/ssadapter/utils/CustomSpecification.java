package vn.ript.ssadapter.utils;

import java.sql.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

public class CustomSpecification<T> implements Specification<T> {

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.conjunction();
    }

    public Specification<T> condition(String attribute, Object value, String comparison) {
        return (root, query, builder) -> {
            String[] attributes = attribute.split("\\.");
            Path<Object> object_value = root.get(attributes[0]);
            for (int i = 1; i < attributes.length; i++) {
                object_value = object_value.get(attributes[i]);
            }
            if (value != null) {
                if (value instanceof String) {
                    if (comparison.equalsIgnoreCase("equal")) {
                        return builder.equal(builder.upper(object_value.as(String.class)),
                                value.toString().toUpperCase());
                    } else if (comparison.equalsIgnoreCase("not_equal")) {
                        return builder.notEqual(builder.upper(object_value.as(String.class)),
                                value.toString().toUpperCase());
                    } else if (comparison.equalsIgnoreCase("like_start")) {
                        return builder.like(builder.upper(object_value.as(String.class)),
                                value.toString().toUpperCase() + "%");
                    } else if (comparison.equalsIgnoreCase("like_end")) {
                        return builder.like(builder.upper(object_value.as(String.class)),
                                "%" + value.toString().toUpperCase());
                    } else if (comparison.equalsIgnoreCase("like")) {
                        return builder.like(builder.upper(object_value.as(String.class)),
                                "%" + value.toString().toUpperCase() + "%");
                    } else if (comparison.equalsIgnoreCase("not_like_start")) {
                        return builder.notLike(builder.upper(object_value.as(String.class)),
                                value.toString().toUpperCase() + "%");
                    } else if (comparison.equalsIgnoreCase("not_like_end")) {
                        return builder.notLike(builder.upper(object_value.as(String.class)),
                                "%" + value.toString().toUpperCase());
                    } else if (comparison.equalsIgnoreCase("not_like")) {
                        return builder.notLike(builder.upper(object_value.as(String.class)),
                                "%" + value.toString().toUpperCase() + "%");
                    } else if (comparison.equalsIgnoreCase("date_between")) {
                        String[] objs = value.toString().split("-");
                        return builder.between(object_value.as(Date.class), Utils.YYYY_MM_DD_TO_DATE(objs[0]),
                                Utils.YYYY_MM_DD_TO_DATE(objs[1]));
                    }
                } else {
                    if (comparison.equalsIgnoreCase("equal")) {
                        return builder.equal(object_value, value);
                    } else if (comparison.equalsIgnoreCase("not_equal")) {
                        return builder.notEqual(object_value, value);
                    }
                }
                return null;
            }
            return null;
        };
    }

}
