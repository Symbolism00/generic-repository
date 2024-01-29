package lf.sol.abstractrepository.repository;

import jakarta.persistence.criteria.*;
import lf.sol.abstractrepository.model.AbstractEntity;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GenericCondition<T extends AbstractEntity> {

    private final List<Predicate> predicates;
    private final List<Predicate> predicatesCount;

    private final Root<T> root;
    private final Root<T> rootCount;
    private final CriteriaBuilder criteriaBuilder;
    private final CriteriaQuery<T> criteriaQuery;
    private final CriteriaQuery<Long> criteriaQueryCount;
    @Getter(AccessLevel.PROTECTED)
    private final boolean doCount;


    protected GenericCondition(Root<T> root, Root<T> rootCount, CriteriaBuilder criteriaBuilder,
                               CriteriaQuery<T> criteriaQuery, CriteriaQuery<Long> criteriaQueryCount, boolean doCount) {
        this.predicates = new ArrayList<>();
        this.predicatesCount = new ArrayList<>();
        this.root = root;
        this.rootCount = rootCount;
        this.criteriaBuilder = criteriaBuilder;
        this.criteriaQuery = criteriaQuery;
        this.criteriaQueryCount = criteriaQueryCount;
        this.doCount = doCount;
    }

    public GenericCondition<T> equal(String field, Object value){
        equal(field, value, predicates, root);
        if(doCount){
            equal(field, value, predicatesCount, rootCount);
        }
        return this;
    }

    private void equal(String field, Object value, List<Predicate> predicates, Root<T> root){
        predicates.add(criteriaBuilder.equal(getAttribute(field, root), value));
    }

    public GenericCondition<T> notEqual(String field, Object value){
        notEqual(field, value, predicates, root);
        if(doCount){
            notEqual(field, value, predicatesCount, rootCount);
        }
        return this;
    }

    private void notEqual(String field, Object value, List<Predicate> predicates, Root<T> root){
        predicates.add(criteriaBuilder.notEqual(getAttribute(field, root), value));
    }

    public GenericCondition<T> greater(String field, String value, boolean orEqual){
        greater(field, value, orEqual, false, predicates, root);
        if(doCount){
            greater(field, value, orEqual, false, predicatesCount, rootCount);
        }
        return this;
    }

    public GenericCondition<T> greater(String field, Number value, boolean orEqual){
        greater(field, value, orEqual, true, predicates, root);
        if(doCount){
            greater(field, value, orEqual, true, predicatesCount, rootCount);
        }
        return this;
    }

    @SuppressWarnings("unchecked")
    private void greater(String field, Object value, boolean orEqual, boolean isNumber, List<Predicate> predicates, Root<T> root){
        Path path = getAttribute(field, root);
        if(!isNumber){
            predicates.add(orEqual ? criteriaBuilder.greaterThanOrEqualTo(path, (String) value) :
                    criteriaBuilder.greaterThan(path, (String) value));
        }else{
            predicates.add(orEqual ? criteriaBuilder.ge(path, (Number) value) :
                    criteriaBuilder.gt(path, (Number) value));
        }
    }

    public GenericCondition<T> less(String field, String value, boolean orEqual){
        less(field, value, orEqual, false, predicates, root);
        if(doCount){
            less(field, value, orEqual, false, predicatesCount, rootCount);
        }
        return this;
    }

    public GenericCondition<T> less(String field, Number value, boolean orEqual){
        less(field, value, orEqual, true, predicates, root);
        if(doCount){
            less(field, value, orEqual, true, predicatesCount, rootCount);
        }
        return this;
    }

    @SuppressWarnings("unchecked")
    private void less(String field, Object value, boolean orEqual, boolean isNumber, List<Predicate> predicates, Root<T> root){
        Path path = getAttribute(field, root);
        if(!isNumber){
            predicates.add(orEqual ? criteriaBuilder.lessThanOrEqualTo(path, (String) value) :
                    criteriaBuilder.lessThan(path, (String) value));
        }else{
            predicates.add(orEqual ? criteriaBuilder.le(path, (Number) value) :
                    criteriaBuilder.lt(path, (Number) value));
        }
    }

    public GenericCondition<T> between(String field, Number value1, Number value2){
        between(field, value1.toString(), value2.toString(), predicates, root);
        if(doCount){
            between(field, value1.toString(), value2.toString(), predicatesCount, rootCount);
        }
        return this;
    }

    @SuppressWarnings("unchecked")
    private void between(String field, String value1, String value2, List<Predicate> predicates, Root<T> root){
        Path path = getAttribute(field, root);
        predicates.add(criteriaBuilder.between(path, value1, value2));
    }

    public GenericCondition<T> in(String field, List<Object> values) {
        in(field, values, predicates, root);
        if (doCount) {
            in(field, values, predicatesCount, rootCount);
        }
        return this;
    }

    public GenericCondition<T> in(String field, Object[] values){
        return in(field, Arrays.asList(values));
    }

    private void in(String field, List<Object> values, List<Predicate> predicates, Root<T> root){
        predicates.add(getAttribute(field, root).in(values));
    }

    public GenericCondition<T> like(String field, Object value){
        like(field, value, predicates, root);
        if(doCount){
            like(field, value, predicatesCount, rootCount);
        }
        return this;
    }

    @SuppressWarnings("unchecked")
    private void like(String field, Object value, List<Predicate> predicates, Root<T> root){
        Path path = getAttribute(field, root);
        predicates.add(criteriaBuilder.like(path, "%" + value + "%"));
    }

    public GenericCondition<T> disjunctionLike(Object value, String... fields){
        disjunctionLike(value, predicates, root, fields);
        if(doCount){
            disjunctionLike(value, predicatesCount, rootCount, fields);
        }
        return this;
    }

    @SuppressWarnings("unchecked")
    private void disjunctionLike(Object value, List<Predicate> predicates, Root<T> root, String... fields){
        List<Predicate> orCondition = new ArrayList<>();
        for(Object field : Arrays.stream(fields).toArray()){
            Path path = getAttribute(field.toString(), root);
            orCondition.add(criteriaBuilder.like(path, "%" + value + "%"));
        }
        predicates.add(criteriaBuilder.or(orCondition.toArray(new Predicate[0])));
    }

    private Path<?> getAttribute(String field, Path<?> path){

        if(field.isBlank()){
            return path;
        }

        String[] innerFields = field.split("\\.");
        String firstField = innerFields[0];
        return getAttribute(field.substring(Math.min(firstField.length() + 1, field.length())), path.get(firstField));
    }

    protected CriteriaQuery<T> generate(GenericOrder... genericOrders){

        // order by
        List<Order> orders = Arrays.stream(genericOrders).map(genericOrder -> genericOrder.isAscending() ?
                criteriaBuilder.asc(getAttribute(genericOrder.getField(), root)) : criteriaBuilder.desc(getAttribute(genericOrder.getField(), root)))
                .toList();

        return criteriaQuery.select(root).where(predicates.toArray(new Predicate[0])).orderBy(orders);
    }

    protected CriteriaQuery<Long> generateCount(){
        return criteriaQueryCount.select(criteriaBuilder.count(rootCount)).where(predicatesCount.toArray(new Predicate[0]));
    }
}
