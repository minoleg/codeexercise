package com.ff.codeexercise.store;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class AnalysisCount {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
    private String word;
    private int count;
    private Integer analysisId;

    protected AnalysisCount() {
    }

    public AnalysisCount(String word, int count) {
        this.word = word;
        this.count = count;
    }

    public Integer getId() {
        return id;
    }

    public String getWord() {
        return word;
    }

    public int getCount() {
        return count;
    }

    public Integer getAnalysisId() {
        return analysisId;
    }

}
