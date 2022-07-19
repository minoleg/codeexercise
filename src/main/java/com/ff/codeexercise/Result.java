package com.ff.codeexercise;

import java.util.List;

class Result {
    private List<WordCount> counts;

    public void setCounts(List<WordCount> counts) {
        this.counts = counts;
    }

    public List<WordCount> getCounts() {
        return counts;
    }

    public static class WordCount {

        private final String word;
        private final int count;
        
        public WordCount(String word, int count) {
            this.word = word;
            this.count = count;
        }

        public String getWord() {
            return word;
        }

        public int getCount() {
            return count;
        }

    }
}
