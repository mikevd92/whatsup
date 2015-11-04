package toj.demo.whatsup.notify.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import toj.demo.whatsup.domain.Keyword;
import toj.demo.whatsup.notify.dao.KeywordDAO;

import java.util.Set;

/**
 * Created by mihai.popovici on 11/4/2015.
 */
@Transactional
public class PersistentKeywordService implements KeywordService {

    private final KeywordDAO keywordDAO;
    @Autowired
    public PersistentKeywordService(KeywordDAO keywordDAO) {
        this.keywordDAO = keywordDAO;
    }

    @Override
    public void saveKeywords(Set<Keyword> keywords) {
        keywordDAO.saveKeywords(keywords);
    }
}
