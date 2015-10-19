package toj.demo.whatsup.dao;


import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * Created by mihai.popovici on 10/7/2015.
 */
@NoRepositoryBean
public interface DAO<T,ID extends Serializable>  {
    void delete(T deleted);

    List<T> findAll();

    Optional<T> findOne(ID id);

    T save(T persisted);
    void removeAll();

}
