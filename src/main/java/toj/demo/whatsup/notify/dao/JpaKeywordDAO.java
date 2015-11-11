package toj.demo.whatsup.notify.dao;

import org.springframework.stereotype.Repository;
import toj.demo.whatsup.dao.JpaDAO;
import toj.demo.whatsup.domain.Keyword;
import toj.demo.whatsup.domain.Keyword_;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by mihai.popovici on 11/4/2015.
 */
@Repository
public class JpaKeywordDAO extends JpaDAO<Keyword,Long> implements KeywordDAO {

    private int batchSize=50;
    private static final long serialVersionUID = 2L;
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

    @Override
    public Set<String> checkExistingKeywordTexts(Set<String> texts) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<String> cq = cb.createQuery(String.class);
        Root<Keyword> keywordRoot = cq.from(this.entityClass);
        cq.where(cb.isTrue(keywordRoot.get(Keyword_.text).in(texts)));
        cq.select(keywordRoot.get(Keyword_.text));
        TypedQuery<String> query=entityManager.createQuery(cq);
        return new LinkedHashSet<>(query.getResultList());
    }

    @Override
    public Set<Keyword> getExistingKeywords(Set<String> texts) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Keyword> cq = cb.createQuery(entityClass);
        Root<Keyword> keywordRoot = cq.from(this.entityClass);
        cq.where(cb.isTrue(keywordRoot.get(Keyword_.text).in(texts)));
        cq.select(keywordRoot);
        TypedQuery<Keyword> query=entityManager.createQuery(cq);
        return new LinkedHashSet<>(query.getResultList());
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
