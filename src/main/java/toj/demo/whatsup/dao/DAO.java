package toj.demo.whatsup.dao;


import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;


@NoRepositoryBean
public interface DAO<T,ID extends Serializable>  {
    void delete(T deleted);

    List<T> findAll();

    Optional<T> findOne(ID id);

    T save(T persisted);
    void update(T updated);

    void removeAll();

}
