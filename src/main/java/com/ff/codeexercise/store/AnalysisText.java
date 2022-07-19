package com.ff.codeexercise.store;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class AnalysisText {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    @Column(columnDefinition = "VARCHAR(100000)")
    private String text;

    private Integer analysisId;

    protected AnalysisText() {
    }

    public AnalysisText(String text) {
        this.text = text;
    }

    public Integer getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public Integer getAnalysisId() {
        return analysisId;
    }

}
