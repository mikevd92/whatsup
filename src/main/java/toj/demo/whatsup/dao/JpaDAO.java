package toj.demo.whatsup.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Optional;


public abstract class JpaDAO<T,ID extends Serializable> implements DAO<T,ID> {
    protected Class<T> entityClass;

    @PersistenceContext
    protected EntityManager entityManager;
    @SuppressWarnings("unchecked")
    public JpaDAO()  {

        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        this.entityClass = (Class<T>) genericSuperclass.getActualTypeArguments()[0];
    }

    public T save(T persisted){
        entityManager.persist(persisted);
        return persisted;
    }


}
