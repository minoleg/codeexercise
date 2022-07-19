package com.ff.codeexercise;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Parser {

    private static final Logger LOG = LoggerFactory.getLogger(Parser.class);
    private static final Pattern REGEX = Pattern.compile("([^a-zA-Z])+");

    private Set<String> stopWrods;
    private List<AbstractMap.SimpleImmutableEntry<String,String>> suffixes;
    private final int minStemLength;

    public Parser(@Value("${stopwords.file}") String stopWordsFile, @Value("${suffixes.file}") String suffixesFile, @Value("${min-stem-length}") int minStemLength) {
        readStopWrodsFile(stopWordsFile);
        readSuffixesFile(suffixesFile);
        this.minStemLength = minStemLength;
    }

    Stream<Token> asStream(String text, boolean excludeStopWords) {
        return REGEX
                .splitAsStream(text)
                .map(w -> w.toUpperCase())
                .filter(w -> !(excludeStopWords && stopWrods.contains(w)))
                .map(w -> new Token(w, getStem(w)));
    }

    private String getStem(String word) {
        for (var entry : suffixes) {
            String suffix = entry.getKey();
            if (word.endsWith(suffix)) {
                String addition = entry.getValue();
                String stem = word.substring(0, word.length() - suffix.length()) + addition;
                // 5. BONUS: Not all words that end with the given suffixes are root words,
                // come up with an algorithm to help determine when the resulting word is an actual word
                // and not a word that happens to end in those letters.
                // A stem should be at least min-stem-length
                if (stem.length() >= minStemLength) {
                    return stem;
                }
            }
        }
        return word;
    }

    private void readStopWrodsFile(String fileName) {
        try {
            stopWrods = Files.readAllLines(Paths.get(fileName))
                    .stream()
                    .map(w -> w.toUpperCase())
                    .collect(Collectors.toSet());
        } catch (IOException ex) {
            LOG.error(null, ex);
        }
    }

    private void readSuffixesFile(String fileName) {
        try {
            Comparator<StringBuilder> comp = (StringBuilder::compareTo);
            suffixes = Files.readAllLines(Paths.get(fileName))
                    .stream()
                    .map(w -> w.toUpperCase())
                    .map(w -> w.split("\\s+"))
                    .map(a -> new AbstractMap.SimpleImmutableEntry<String, String>(a[0], a.length > 1 ? a[1] : ""))
                    .sorted((p1, p2) -> comp.reversed().compare(new StringBuilder(p1.getKey()).reverse(), new StringBuilder(p2.getKey()).reverse()))
                    .toList();
        } catch (IOException ex) {  
            LOG.error(null, ex);
        }
    }

    public class Token {
        private final String word;
        private final String stem;

        private Token(String word, String stem) {
            this.word = word;
            this.stem = stem;
        }

        public String getWord() {
            return word;
        }

        public String getStem() {
            return stem;
        }

    }
}
