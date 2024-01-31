package lf.sol.genericrepository.repository;

import lf.sol.genericrepository.model.GenericEntity;
import lf.sol.genericrepository.exception.NoCountPaginationException;
import lf.sol.genericrepository.exception.WrongPaginationParamsException;

import java.util.List;

public interface IGenericRepository<T extends GenericEntity, Object> {

    /**
     * Method that initializes a generic condition related to a specific entity
     * It does not initialize a parallel generic condition for counting
     * @return the generic condition
     */
    GenericCondition<T> initCondition();

    /**
     * Method that initializes a generic condition related to a specific entity, and it's parallel
     * condition for counting the number of results
     * @return the generic condition
     */
    GenericCondition<T> initConditionWithCount();

    /**
     * Method that saves an entity if it does not exist or refreshes it if already exists
     * @param entity the entity to save or update
     * @return the entity persisted
     */
    T saveOrRefresh(T entity);

    /**
     * Method that saves a new entity
     * @param entity the entity to save
     */
    void saveNew(T entity);

    /**
     * Method that removes an entity
     * @param entity the entity to delete
     */
    void remove(T entity);

    /**
     * Method that checks if an entity exists with the specific ID
     * @param id the ID
     * @return true if exists, false if not
     */
    boolean existsById(Object id);

    /**
     * Method that gets an entity by its ID
     * @param id the ID
     * @return the entity
     */
    T getById(Object id);

    /**
     * Method that gets all the results from an entity in a specific order
     * @param genericOrders the order condition
     * @return all the results from an entity
     */
    List<T> getAll(GenericOrder... genericOrders);

    /**
     * Method that gets all the results from an entity with a condition and in a specific order
     * @param genericCondition the condition to filter the results
     * @param genericOrders the order condition
     * @return all the results from an entity
     */
    List<T> getAll(GenericCondition<T> genericCondition, GenericOrder... genericOrders);

    /**
     * Method that gets total number of results of an entity
     * @return the number of results of an entity
     */
    Long total();

    /**
     * Method that gets the total number of results of an entity with a specific condition
     * @return the number of results of an entity
     */
    Long total(GenericCondition<T> genericCondition);

    /**
     * Method that gets some results from an entity in a specific order
     * @param offset the offset of the pagination
     * @param limit the number of results to return
     * @param genericOrders the order condition
     * @return some results of a specific entity
     * @throws NoCountPaginationException if the count operation was not started in the generic condition
     * @throws WrongPaginationParamsException if the pagination arguments are invalid
     */
    Pagination<T> getSome(int offset, int limit, GenericOrder... genericOrders) throws NoCountPaginationException, WrongPaginationParamsException;

    /**
     * Method that gets some results from an entity with a generic condition in a specific order
     * @param genericCondition the condition to filter the results
     * @param offset the offset of the pagination
     * @param limit the number of results to return
     * @param genericOrders the order condition
     * @return some results of a specific entity
     * @throws NoCountPaginationException if the count operation was not started in the generic condition
     * @throws WrongPaginationParamsException if the pagination arguments are invalid
     */
    Pagination<T> getSome(GenericCondition<T> genericCondition, int offset, int limit, GenericOrder... genericOrders) throws NoCountPaginationException, WrongPaginationParamsException;

    /**
     * Method that gets the first nth results from an entity in a specific order
     * @param nth the number of results to return from the first one
     * @param genericOrders the order condition
     * @return the first nth results from an entity
     */
    List<T> getNth(int nth, GenericOrder... genericOrders);

    /**
     * Method that gets the first nth results from an entity with a condition in a specific order
     * @param genericCondition the condition to filter the results
     * @param nth the number of results to return from the first one
     * @param genericOrders the order condition
     * @return the first nth results from an entity
     */
    List<T> getNth(GenericCondition<T> genericCondition, int nth, GenericOrder... genericOrders);

    /**
     * Method that gets the first result from an entity in a specific order
     * @param genericOrders the order condition
     * @return the first result
     */
    T getFirst(GenericOrder... genericOrders);

    /**
     * Method that gets the first result from an entity with a specific condition in a specific order
     * @param genericCondition the condition to filter the results
     * @param genericOrders the order condition
     * @return the first result
     */
    T getFirst(GenericCondition<T> genericCondition, GenericOrder... genericOrders);

}
