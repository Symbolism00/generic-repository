package lf.sol.abstractrepository.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lf.sol.abstractrepository.model.AbstractEntity;
import lf.sol.abstractrepository.exception.NoCountPaginationException;
import lf.sol.abstractrepository.exception.WrongPaginationParamsException;

import java.util.List;

public class GenericRepository<T extends AbstractEntity, Object> implements IGenericRepository<T, Object> {

    @PersistenceContext
    private EntityManager entityManager;
    private final Class<T> abstractEntityClass;

    public GenericRepository(Class<T> abstractEntityClass) {
        this.abstractEntityClass = abstractEntityClass;
    }

    @Override
    public GenericCondition<T> initCondition() {
        return initCondition(false);
    }

    @Override
    public final GenericCondition<T> initCondition(boolean doCount){
        // creates the builder
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(abstractEntityClass);
        CriteriaQuery<Long> criteriaBuilderQueryCount = doCount ? criteriaBuilder.createQuery(Long.class) : null;

        // select + from
        Root<T> root = criteriaQuery.from(abstractEntityClass);
        Root<T> rootCount = doCount ? criteriaBuilderQueryCount.from(abstractEntityClass) : null;
        return new GenericCondition<T>(root, rootCount, criteriaBuilder, criteriaQuery, criteriaBuilderQueryCount, doCount);
    }

    @Override
    public T saveOrUpdate(T entity) {
        return entityManager.merge(entity);
    }

    @Override
    public void save(T entity) {
        entityManager.persist(entity);
    }

    @Override
    public void delete(T entity) {
        entityManager.remove(entity);
    }

    @Override
    public T getById(Object id) {
        return entityManager.find(abstractEntityClass, id);
    }

    @Override
    public Long count() {
        return entityManager.createQuery(initCondition(true).generateCount()).getSingleResult();
    }

    @Override
    public Long count(GenericCondition<T> genericCondition) {
        return genericCondition.isDoCount() ? entityManager.createQuery(genericCondition.generateCount()).getSingleResult() : 0L;
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
        if(!genericCondition.isDoCount()){
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
}
