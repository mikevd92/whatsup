package toj.demo.whatsup.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;

/**
 * Created by mihai.popovici on 10/15/2015.
 */
public abstract class JpaDAO<T,ID extends Serializable> implements DAO<T,ID> {
    protected Class<T> entityClass;

    @PersistenceContext
    protected EntityManager entityManager;
    @SuppressWarnings("unchecked")
    public JpaDAO()  {
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        this.entityClass = (Class<T>) genericSuperclass.getActualTypeArguments()[0];
    }
    public void delete(T deleted){
        entityManager.remove(entityManager.contains(deleted) ? deleted : entityManager.merge(deleted));
    }

    public List<T> findAll(){
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(this.entityClass);
        Root<T> root = cq.from(this.entityClass);
        cq.select(root);
        TypedQuery<T> q = entityManager.createQuery(cq);
        List<T> list = q.getResultList();
        return list;
    }

    public Optional<T> findOne(ID id){
        return Optional.ofNullable(entityManager.find(entityClass, id));
    }

    public T save(T persisted){
        entityManager.persist(persisted);
        return persisted;
    }
}
