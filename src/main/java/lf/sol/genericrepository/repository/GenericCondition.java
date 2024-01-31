package lf.sol.genericrepository.repository;

import jakarta.persistence.criteria.*;
import lf.sol.genericrepository.model.GenericEntity;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GenericCondition<T extends GenericEntity> {

    private static final String LIKE_SEPARATOR = "%";
    private static final String PROPERTY_SEPARATOR = ".";
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

    /**
     * Method that checks if an entity field is equal to a value
     * @param field the entity field
     * @param value the value to compare
     * @return the generic condition
     */
    public GenericCondition<T> equal(String field, Object value){
        equal(field, value, predicates, root);
        if(doCount){
            equal(field, value, predicatesCount, rootCount);
        }
        return this;
    }

    /**
     * Method that checks if an entity field is not equal to a value
     * @param field the entity field
     * @param value the value to compare
     * @return the generic condition
     */
    public GenericCondition<T> notEqual(String field, Object value){
        notEqual(field, value, predicates, root);
        if(doCount){
            notEqual(field, value, predicatesCount, rootCount);
        }
        return this;
    }

    /**
     * Method that checks if an entity field is true
     * @param field the entity field
     * @return the generic condition
     */
    public GenericCondition<T> isTrue(String field){
        isTrue(field, predicates, root);
        if(doCount){
            isTrue(field, predicatesCount, rootCount);
        }
        return this;
    }

    /**
     * Method that checks if an entity field is false
     * @param field the entity field
     * @return the generic condition
     */
    public GenericCondition<T> isFalse(String field){
        isFalse(field, predicates, root);
        if(doCount){
            isFalse(field, predicatesCount, rootCount);
        }
        return this;
    }

    /**
     * Method that checks if an entity field is null
     * @param field the entity field
     * @return the generic condition
     */
    public GenericCondition<T> isNull(String field){
        isNull(field, predicates, root);
        if(doCount){
            isNull(field, predicatesCount, rootCount);
        }
        return this;
    }

    /**
     * Method that checks if an entity field is not null
     * @param field the entity field
     * @return the generic condition
     */
    public GenericCondition<T> isNotNull(String field){
        isNotNull(field, predicates, root);
        if(doCount){
            isNotNull(field, predicatesCount, rootCount);
        }
        return this;
    }

    /**
     * Method that checks if an entity field is greater than another value (string)
     * @param field the entity field
     * @param value the value to compare (string)
     * @param orEqual flag that indicates if it's greater or equal
     * @return the generic condition
     */
    public GenericCondition<T> greater(String field, String value, boolean orEqual){
        greater(field, value, orEqual, false, predicates, root);
        if(doCount){
            greater(field, value, orEqual, false, predicatesCount, rootCount);
        }
        return this;
    }

    /**
     * Method that checks if an entity field is greater than another value (number)
     * @param field the entity field
     * @param value the value to compare (number)
     * @param orEqual flag that indicates if it's greater or equal
     * @return the generic condition
     */
    public GenericCondition<T> greater(String field, Number value, boolean orEqual){
        greater(field, value, orEqual, true, predicates, root);
        if(doCount){
            greater(field, value, orEqual, true, predicatesCount, rootCount);
        }
        return this;
    }

    /**
     * Method that checks if an entity field is less than another value (string)
     * @param field the entity field
     * @param value the value to compare (string)
     * @param orEqual flag that indicates if it's less or equal
     * @return the generic condition
     */
    public GenericCondition<T> less(String field, String value, boolean orEqual){
        less(field, value, orEqual, false, predicates, root);
        if(doCount){
            less(field, value, orEqual, false, predicatesCount, rootCount);
        }
        return this;
    }

    /**
     * Method that checks if an entity field is less than another value (number)
     * @param field the entity field
     * @param value the value to compare (number)
     * @param orEqual flag that indicates if it's less or equal
     * @return the generic condition
     */
    public GenericCondition<T> less(String field, Number value, boolean orEqual){
        less(field, value, orEqual, true, predicates, root);
        if(doCount){
            less(field, value, orEqual, true, predicatesCount, rootCount);
        }
        return this;
    }

    /**
     * Method that checks if an entity field is between another two numbers
     * @param field the entity field
     * @param value1 number one
     * @param value2 number two
     * @return the generic condition
     */
    public GenericCondition<T> between(String field, Number value1, Number value2){
        between(field, value1.toString(), value2.toString(), predicates, root);
        if(doCount){
            between(field, value1.toString(), value2.toString(), predicatesCount, rootCount);
        }
        return this;
    }

    /**
     * Method that checks if an entity field is in a list of values
     * @param field the entity field
     * @param values list of values
     * @return the generic condition
     */
    public GenericCondition<T> in(String field, List<Object> values) {
        in(field, values, predicates, root);
        if (doCount) {
            in(field, values, predicatesCount, rootCount);
        }
        return this;
    }

    /**
     * Method that checks if an entity field is in a list of values
     * @param field the entity field
     * @param values list of values
     * @return the generic condition
     */
    public GenericCondition<T> in(String field, Object[] values){
        return in(field, Arrays.asList(values));
    }

    /**
     * Method that checks if an entity field is like to a value
     * @param field the entity field
     * @param value the value to compare
     * @return the generic condition
     */
    public GenericCondition<T> like(String field, Object value){
        like(field, value, predicates, root);
        if(doCount){
            like(field, value, predicatesCount, rootCount);
        }
        return this;
    }

    /**
     * Method that checks if some entity fields are like to a value
     * @param value the value to compare
     * @param fields the entity fields
     * @return the generic condition
     */
    public GenericCondition<T> disjunctionLike(Object value, String... fields){
        disjunctionLike(value, predicates, root, fields);
        if(doCount){
            disjunctionLike(value, predicatesCount, rootCount, fields);
        }
        return this;
    }

    /**
     * Method that adds an OR generic condition to the another generic condition
     * @param genericCondition the generic condition to add with OR
     * @return the generic condition
     */
    public GenericCondition<T> or(GenericCondition<T> genericCondition){
        predicates.add(criteriaBuilder.or(genericCondition.predicates));
        return this;
    }

    /**
     * Method that adds an AND generic condition to the another generic condition
     * @param genericCondition the generic condition to add with AND
     * @return the generic condition
     */
    public GenericCondition<T> and(GenericCondition<T> genericCondition){
        predicates.add(criteriaBuilder.and(genericCondition.predicates));
        return this;
    }

    /**
     * Method that generates the criteria query
     * @param genericOrders the generic orders to apply
     * @return the criteria query
     */
    protected CriteriaQuery<T> generate(GenericOrder... genericOrders){

        // order by
        List<Order> orders = Arrays.stream(genericOrders).map(genericOrder -> genericOrder.isAscending() ?
                criteriaBuilder.asc(getAttribute(genericOrder.getField(), root)) : criteriaBuilder.desc(getAttribute(genericOrder.getField(), root)))
                .toList();

        return criteriaQuery.select(root).where(predicates.toArray(new Predicate[0])).orderBy(orders);
    }

    /**
     * Method that generates the count criteria query
     * @return the count criteria query
     */
    protected CriteriaQuery<Long> generateCount(){
        return criteriaQueryCount.select(criteriaBuilder.count(rootCount)).where(predicatesCount.toArray(new Predicate[0]));
    }

    /**
     * Method that checks if an entity field is equal to a value
     * @param field the entity field
     * @param value the value to compare
     * @param predicates the conditions to add a new one
     * @param root the root entity to get the field
     */
    private void equal(String field, Object value, List<Predicate> predicates, Root<T> root){
        predicates.add(criteriaBuilder.equal(getAttribute(field, root), value));
    }

    /**
     * Method that checks if an entity field is not equal to a value
     * @param field the entity field
     * @param value the value to compare
     * @param predicates the conditions to add a new one
     * @param root the root entity to get the field
     */
    private void notEqual(String field, Object value, List<Predicate> predicates, Root<T> root){
        predicates.add(criteriaBuilder.notEqual(getAttribute(field, root), value));
    }

    /**
     * Method that checks if an entity field is true
     * @param field the entity field
     * @param predicates the conditions to add a new one
     * @param root the root entity to get the field
     */
    @SuppressWarnings("unchecked")
    private void isTrue(String field, List<Predicate> predicates, Root<T> root){
        Path path = getAttribute(field, root);
        predicates.add(criteriaBuilder.isTrue(path));
    }

    /**
     * Method that checks if an entity field is false
     * @param field the entity field
     * @param predicates the conditions to add a new one
     * @param root the root entity to get the field
     */
    @SuppressWarnings("unchecked")
    private void isFalse(String field, List<Predicate> predicates, Root<T> root){
        Path path = getAttribute(field, root);
        predicates.add(criteriaBuilder.isFalse(path));
    }

    /**
     * Method that checks if an entity field is null
     * @param field the entity field
     * @param predicates the conditions to add a new one
     * @param root the root entity to get the field
     */
    private void isNull(String field, List<Predicate> predicates, Root<T> root){
        predicates.add(criteriaBuilder.isNull(getAttribute(field, root)));
    }

    /**
     * Method that checks if an entity field is not null
     * @param field the entity field
     * @param predicates the conditions to add a new one
     * @param root the root entity to get the field
     */
    private void isNotNull(String field, List<Predicate> predicates, Root<T> root){
        predicates.add(criteriaBuilder.isNotNull(getAttribute(field, root)));
    }

    /**
     * Method that checks if an entity field is greater than another value
     * @param field the entity field
     * @param value the value to compare
     * @param orEqual flag that indicates if it's greater or equal
     * @param isNumber flag that indicates if the comparison is between a number or string
     * @param predicates the conditions to add a new one
     * @param root the root entity to get the field
     */
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

    /**
     * Method that checks if an entity field is less than another value
     * @param field the entity field
     * @param value the value to compare
     * @param orEqual flag that indicates if it's less or equal
     * @param isNumber flag that indicates if the comparison is between a number or string
     * @param predicates the conditions to add a new one
     * @param root the root entity to get the field
     */
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

    /**
     * Method that checks if an entity field is between another two numbers
     * @param field the entity field
     * @param value1 number one
     * @param value2 number two
     * @param predicates the conditions to add a new one
     * @param root the root entity to get the field
     */
    @SuppressWarnings("unchecked")
    private void between(String field, String value1, String value2, List<Predicate> predicates, Root<T> root){
        Path path = getAttribute(field, root);
        predicates.add(criteriaBuilder.between(path, value1, value2));
    }

    /**
     * Method that checks if an entity field is in a list of values
     * @param field the entity field
     * @param values list of values
     * @param predicates the conditions to add a new one
     * @param root the root entity to get the field
     */
    private void in(String field, List<Object> values, List<Predicate> predicates, Root<T> root){
        predicates.add(getAttribute(field, root).in(values));
    }

    /**
     * Method that checks if an entity field is like to a value
     * @param field the entity field
     * @param value the value to compare
     * @param predicates the conditions to add a new one
     * @param root the root entity to get the field
     */
    @SuppressWarnings("unchecked")
    private void like(String field, Object value, List<Predicate> predicates, Root<T> root){
        Path path = getAttribute(field, root);
        predicates.add(criteriaBuilder.like(path, LIKE_SEPARATOR + value + LIKE_SEPARATOR));
    }

    /**
     * Method that checks if some entity fields are like to a value
     * @param value the value to compare
     * @param fields the entity fields
     * @param predicates the conditions to add a new one
     * @param root the root entity to get the field
     */
    @SuppressWarnings("unchecked")
    private void disjunctionLike(Object value, List<Predicate> predicates, Root<T> root, String... fields){
        List<Predicate> orCondition = new ArrayList<>();
        for(Object field : Arrays.stream(fields).toArray()){
            Path path = getAttribute(field.toString(), root);
            orCondition.add(criteriaBuilder.like(path, LIKE_SEPARATOR + value + LIKE_SEPARATOR));
        }
        predicates.add(criteriaBuilder.or(orCondition.toArray(new Predicate[0])));
    }

    /**
     * Method that recursively gets the field of an entity and automatically do the joins between nested entities
     * @param field the field to get
     * @param path the entity
     * @return the field
     */
    private Path<?> getAttribute(String field, Path<?> path){

        // if it's at the end of the nested fields
        if(field.isBlank()){
            return path;
        }
        // gets the next nested fields
        String[] innerFields = field.split("\\" + PROPERTY_SEPARATOR);
        String firstField = innerFields[0];
        return getAttribute(field.substring(Math.min(firstField.length() + 1, field.length())), path.get(firstField));
    }
}
