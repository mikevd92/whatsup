package toj.demo.whatsup.notify.dao;

import toj.demo.whatsup.dao.DAO;

import toj.demo.whatsup.domain.Keyword;

import java.util.List;
import java.util.Set;

/**
 * Created by mihai.popovici on 11/4/2015.
 */
public interface KeywordDAO extends DAO<Keyword,Long> {
    void saveKeywords(Set<Keyword> keywords);
    Set<String> checkExistingKeywordTexts(Set<String> texts);
    public Set<Keyword> getExistingKeywords(Set<String> texts);

}
