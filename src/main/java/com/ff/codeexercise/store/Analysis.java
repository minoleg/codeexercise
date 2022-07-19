package com.ff.codeexercise.store;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Analysis {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    private boolean excludeStopWords;

    private boolean useStems;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "analysisId", referencedColumnName = "id")
    private AnalysisText text;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "analysisId", referencedColumnName = "id")
    private List<AnalysisCount> counts = new ArrayList<>();

    protected Analysis() {
    }

    public Analysis(boolean excludeStopWords, boolean useStems, AnalysisText text, List<AnalysisCount> counts) {
        this.excludeStopWords = excludeStopWords;
        this.useStems = useStems;
        this.text = text;
        this.counts = counts;
    }

    public Integer getId() {
        return id;
    }

    public boolean isExcludeStopWords() {
        return excludeStopWords;
    }

    public boolean isUseStems() {
        return useStems;
    }

    public AnalysisText getText() {
        return text;
    }

    public List<AnalysisCount> getCounts() {
        return counts;
    }

}
