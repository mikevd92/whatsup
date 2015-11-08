package toj.demo.whatsup.notify.services;

import toj.demo.whatsup.domain.Keyword;

import java.util.List;
import java.util.Set;

/**
 * Created by mihai.popovici on 11/4/2015.
 */
public interface KeywordService {
    public void saveKeywords(Set<Keyword> keywords);
    public Set<String> checkExistingKeywordTexts(Set<String> texts);
    public Set<Keyword> getExistingKeywords(Set<String> texts);
}
