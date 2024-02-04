package lf.sol.genericrepository.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lf.sol.genericrepository.model.GenericEntity;
import lf.sol.genericrepository.exception.NoCountPaginationException;
import lf.sol.genericrepository.exception.WrongPaginationParamsException;

import java.util.List;

public class GenericRepository<T extends GenericEntity, Object> implements IGenericRepository<T, Object> {

    @PersistenceContext
    private EntityManager entityManager;
    private final Class<T> genericEntityClass;

    public GenericRepository(Class<T> genericEntityClass) {
        this.genericEntityClass = genericEntityClass;
    }

    @Override
    public GenericCondition<T> initCondition() {
        return initCondition(false);
    }

    @Override
    public GenericCondition<T> initConditionWithCount() {
        return initCondition(true);
    }

    @Override
    public T saveOrRefresh(T entity) {
        return entityManager.merge(entity);
    }

    @Override
    public void saveNew(T entity) {
        entityManager.persist(entity);
    }

    @Override
    public void remove(T entity) {
        entityManager.remove(entity);
    }

    @Override
    public boolean existsById(Object id) {
        return getById(id) != null;
    }

    @Override
    public T getById(Object id) {
        return entityManager.find(genericEntityClass, id);
    }

    @Override
    public Long total() {
        return entityManager.createQuery(initCondition(true).generateCount()).getSingleResult();
    }

    @Override
    public Long total(GenericCondition<T> genericCondition) {
        return genericCondition.doCount ? entityManager.createQuery(genericCondition.generateCount()).getSingleResult() : 0L;
    }

    @Override
    public List<T> getAll(GenericOrder... genericOrders) {
        return getAll(initCondition());
    }

    @Override
    public List<T> getAll(GenericCondition<T> genericCondition, GenericOrder... genericOrders){
        return entityManager.createQuery(genericCondition.generate(genericOrders)).getResultList();
    }

    @Override
    public Pagination<T> getSome(int offset, int limit, GenericOrder... genericOrders) throws NoCountPaginationException, WrongPaginationParamsException {
        GenericCondition<T> genericCondition = initCondition(true);
        return getSome(genericCondition, offset, limit);
    }

    @Override
    public Pagination<T> getSome(GenericCondition<T> genericCondition, int offset, int limit, GenericOrder... genericOrders) throws NoCountPaginationException, WrongPaginationParamsException {

        // if it does not have count then it cant return a pagination
        if(!genericCondition.doCount){
            throw new NoCountPaginationException("No count operation was created to do the pagination");
        }

        // if the pagination params are wrong
        if(offset < 0 || limit < 0){
            throw new WrongPaginationParamsException("The offset or limit pagination params can't be less than zero");
        }

        List<T> results = entityManager.createQuery(genericCondition.generate(genericOrders)).setFirstResult(offset)
                .setMaxResults(limit).getResultList();
        Long count = entityManager.createQuery(genericCondition.generateCount()).getSingleResult();
        return new Pagination<>(results, count);
    }

    @Override
    public List<T> getNth(int nth, GenericOrder... genericOrders) {
        return getNth(initCondition(), nth);
    }

    @Override
    public List<T> getNth(GenericCondition<T> genericCondition, int nth, GenericOrder... genericOrders) {
        return entityManager.createQuery(genericCondition.generate(genericOrders)).setFirstResult(0)
                .setMaxResults(nth).getResultList();
    }

    @Override
    public T getFirst(GenericOrder... genericOrders) {
        return getNth(1, genericOrders).getFirst();
    }

    @Override
    public T getFirst(GenericCondition<T> genericCondition, GenericOrder... genericOrders) {
        return getNth(genericCondition, 1, genericOrders).getFirst();
    }

    /**
     * Method that initializes a generic condition related to a specific entity
     * @param doCount a flag that indicates if the count condition is to initialize in parallel
     * @return the generic condition
     */
    private GenericCondition<T> initCondition(boolean doCount){
        // creates the builder
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(genericEntityClass);
        CriteriaQuery<Long> criteriaBuilderQueryCount = doCount ? criteriaBuilder.createQuery(Long.class) : null;

        // select + from
        Root<T> root = criteriaQuery.from(genericEntityClass);
        Root<T> rootCount = doCount ? criteriaBuilderQueryCount.from(genericEntityClass) : null;
        return new GenericCondition<>(root, rootCount, criteriaBuilder, criteriaQuery, criteriaBuilderQueryCount, doCount);
    }
}
