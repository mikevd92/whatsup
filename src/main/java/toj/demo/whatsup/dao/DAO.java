package toj.demo.whatsup.dao;


import org.springframework.data.repository.NoRepositoryBean;
import java.io.Serializable;
import java.util.Optional;


@NoRepositoryBean
public interface DAO<T,ID extends Serializable>  {

    T save(T persisted);

}
