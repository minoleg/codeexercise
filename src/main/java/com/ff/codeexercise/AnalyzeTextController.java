package com.ff.codeexercise;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ff.codeexercise.store.Analysis;
import com.ff.codeexercise.store.AnalysisCount;
import com.ff.codeexercise.store.AnalysisRepository;
import com.ff.codeexercise.store.AnalysisText;

@RestController
public class AnalyzeTextController {

    private @Value("${max-results}") int maxResults;
    private @Value("${num-saved-results}") int numSavedResults;

    @Autowired
    private Parser parser;
    @Autowired
    private AnalysisRepository repository; 

    @PostMapping("/analyze")
    public Result analyze(@RequestParam(name = "exclude-stop-words", required = false, defaultValue = "true") boolean excludeStopWords,
            @RequestParam(name = "use-stems", required = false, defaultValue = "false") boolean useStems, @RequestBody String text) {
        Map<String, Integer> counts = parser.asStream(text, excludeStopWords)
                .collect(Collectors.groupingBy((t) -> useStems ? t.getStem(): t.getWord(), Collectors.summingInt(e -> 1)));
        List<Result.WordCount> retCounts = counts.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(maxResults)
                .map(e -> new Result.WordCount(e.getKey(), e.getValue()))
                .toList();

        AnalysisText analysisText = new AnalysisText(text);
        List<AnalysisCount> analysisCounts = retCounts.stream().map(c -> new AnalysisCount(c.getWord(), c.getCount())).toList();
        Analysis analysis = new Analysis(excludeStopWords, useStems, analysisText, analysisCounts);
        repository.save(analysis);
        // Delete all old analysises
        List<Analysis> analysises = repository.findAllByOrderByIdAsc();
        for (int i = 0; i < analysises.size() - numSavedResults; i++) {
            repository.delete(analysises.get(i));
        }

        Result result = new Result();
        result.setCounts(retCounts);
        return result;
    }

    // 6. BONUS - Save the most recent 10 frequency analyses (original text,
    // stop words setting, and resulting word frequencies), allowing the user to navigate
    // back to view a previous analysis for comparison.
    // These persisted analyses should survive a restart of the server process.
    @GetMapping("/analysis")
    public Result getAnalysis(@RequestParam(name = "prev-result") int prevResult) {
        List<Result.WordCount> retCounts;
        List<Analysis> analysises = repository.findAllByOrderByIdAsc();
        if (prevResult >= 1 && prevResult <= analysises.size()) {
            retCounts = analysises.get(analysises.size() - prevResult).getCounts().stream()
                    .map(r -> new Result.WordCount(r.getWord(), r.getCount())).toList();
        } else {
            retCounts = Collections.emptyList();
        }

        Result result = new Result();
        result.setCounts(retCounts);
        return result;
    }
    
}
