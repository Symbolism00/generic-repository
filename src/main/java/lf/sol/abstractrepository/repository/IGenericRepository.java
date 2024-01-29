package lf.sol.abstractrepository.repository;

import lf.sol.abstractrepository.model.AbstractEntity;
import lf.sol.abstractrepository.exception.NoCountPaginationException;
import lf.sol.abstractrepository.exception.WrongPaginationParamsException;

import java.util.List;

public interface IGenericRepository<T extends AbstractEntity, Object> {

    /**
     * Method that initializes a generic condition related to a specific entity
     * It does not initialize a parallel generic condition for counting
     * @return the generic condition
     */
    GenericCondition<T> initCondition();

    /**
     * Method that initializes a generic condition related to a specific entity
     * @param doCount flag that indicates if a parallel generic condition is to be created
     * @return the generic condition
     */
    GenericCondition<T> initCondition(boolean doCount);

    /**
     * Method that saves an entity if it does not exist or updates it if already exists
     * @param entity the entity to save or update
     * @return the entity persisted
     */
    T saveOrUpdate(T entity);

    /**
     * Method that saves an entity
     * @param entity the entity to save
     */
    void save(T entity);

    /**
     * Method that deletes an entity
     * @param entity the entity to delete
     */
    void delete(T entity);

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

    List<T> getAll(GenericCondition<T> genericCondition, GenericOrder... genericOrders);

    Long count();

    Long count(GenericCondition<T> genericCondition);

    Pagination<T> getSome(int offset, int limit, GenericOrder... genericOrders) throws NoCountPaginationException, WrongPaginationParamsException;

    Pagination<T> getSome(GenericCondition<T> genericCondition, int offset, int limit, GenericOrder... genericOrders) throws NoCountPaginationException, WrongPaginationParamsException;

    List<T> getNth(int nth, GenericOrder... genericOrders);

    List<T> getNth(GenericCondition<T> genericCondition, int nth, GenericOrder... genericOrders);

    T getFirst(GenericOrder... genericOrders);

    T getFirst(GenericCondition<T> genericCondition, GenericOrder... genericOrders);

}
