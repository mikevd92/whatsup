package toj.demo.whatsup.notify.dao;

import toj.demo.whatsup.dao.JpaDAO;
import toj.demo.whatsup.domain.Keyword;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Set;

/**
 * Created by mihai.popovici on 11/4/2015.
 */
public class JpaKeywordDAO extends JpaDAO<Keyword,Long> implements KeywordDAO {

    private int batchSize=50;

    @Override
    public void saveKeywords(Set<Keyword> keywords) {
        int i=0;
        for(Keyword keyword : keywords){
            persistOrMerge(keyword);
            i++;
            if(i%batchSize==0){
                entityManager.flush();
                entityManager.clear();
            }
        }

    }
    private Keyword persistOrMerge(Keyword keyword){
        if(keyword.getWordId() == null){
            entityManager.persist(keyword);
            return keyword;
        }else{
            return entityManager.merge(keyword);
        }
    }
}
