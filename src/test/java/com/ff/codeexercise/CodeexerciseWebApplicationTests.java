package com.ff.codeexercise;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CodeexerciseWebApplicationTests {

    @Value("${test.file}")
    private String testFile;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testFileExcludeStopWordsFalseUseStemsFalse() throws Exception {
        byte[] bytes = Files.readAllBytes(Paths.get(testFile));
        this.mockMvc.perform(post("/analyze").param("exclude-stop-words", "false").param("use-stems", "false").content(bytes))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.counts.length()", is(25)))
                .andExpect(jsonPath("$.counts[0].word").value("KMZ"))
                .andExpect(jsonPath("$.counts[0].count").value(188))
                .andExpect(jsonPath("$.counts[1].word").value("HA"))
                .andExpect(jsonPath("$.counts[1].count").value(141));
    }

    @Test
    public void testFileExcludeStopWordsFalseUseStemsTrue() throws Exception {
        byte[] bytes = Files.readAllBytes(Paths.get(testFile));
        this.mockMvc.perform(post("/analyze").param("exclude-stop-words", "false").param("use-stems", "true").content(bytes))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.counts.length()", is(25)))
                .andExpect(jsonPath("$.counts[0].word").value("KMZ"))
                .andExpect(jsonPath("$.counts[0].count").value(205))
                .andExpect(jsonPath("$.counts[1].word").value("HA"))
                .andExpect(jsonPath("$.counts[1].count").value(141));
    }

    @Test
    public void testFileExcludeStopWordsTrueUseStemsFalse() throws Exception {
        byte[] bytes = Files.readAllBytes(Paths.get(testFile));
        this.mockMvc.perform(post("/analyze").param("exclude-stop-words", "true").param("use-stems", "false").content(bytes))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.counts.length()", is(25)))
                .andExpect(jsonPath("$.counts[0].word").value("YVEAZ"))
                .andExpect(jsonPath("$.counts[0].count").value(44))
                .andExpect(jsonPath("$.counts[1].word").value("L"))
                .andExpect(jsonPath("$.counts[1].count").value(43));
    }

    @Test
    public void testFileExcludeStopWordsTrueUseStemsTrue() throws Exception {
        byte[] bytes = Files.readAllBytes(Paths.get(testFile));
        this.mockMvc.perform(post("/analyze").param("exclude-stop-words", "true").param("use-stems", "true").content(bytes))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.counts.length()", is(25)))
                .andExpect(jsonPath("$.counts[0].word").value("YVEAZ"))
                .andExpect(jsonPath("$.counts[0].count").value(62))
                .andExpect(jsonPath("$.counts[1].word").value("L"))
                .andExpect(jsonPath("$.counts[1].count").value(43));
    }

    @Test
    public void testAllExcludeStopWordsFalseUseStemsFalse() throws Exception {
        String content = "D, XR.\n" // Stop words
                + "BL, BLZ, BEVM, BZQ, BZL, BPZL, BEZL?\n" // Words with suffixes
                + "BB, BBB, BBBB, BB, BBB, BBBB, BBB, BBBB, BBBB!\n"; // Words to count
        this.mockMvc.perform(post("/analyze").param("exclude-stop-words", "false").param("use-stems", "false").content(content))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.counts.length()", is(12)))
                .andExpect(jsonPath("$.counts[0].word").value("BBBB"))
                .andExpect(jsonPath("$.counts[0].count").value(4))
                .andExpect(jsonPath("$.counts[1].word").value("BBB"))
                .andExpect(jsonPath("$.counts[1].count").value(3))
                .andExpect(jsonPath("$.counts[2].word").value("BB"))
                .andExpect(jsonPath("$.counts[2].count").value(2))
                .andExpect(jsonPath("$.counts[*].word", containsInAnyOrder("BBBB", "BBB", "BB", "BLZ", "D", "BZL", "BEVM", "BZQ", "BEZL", "BL", "XR", "BPZL")));
    }

    @Test
    public void testAllExcludeStopWordsFalseUseStemsTrue() throws Exception {
        String content = "D, XR.\n" // Stop words
                + "BL, BLZ, BEVM, BZQ, BZL, BPZL, BEZL?\n" // Words with suffixes
                + "BB, BBB, BBBB, BB, BBB, BBBB, BBB, BBBB, BBBB!\n"; // Words to count
        this.mockMvc.perform(post("/analyze").param("exclude-stop-words", "false").param("use-stems", "true").content(content))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.counts.length()", is(12)))
                .andExpect(jsonPath("$.counts[0].word").value("BBBB"))
                .andExpect(jsonPath("$.counts[0].count").value(4))
                .andExpect(jsonPath("$.counts[1].word").value("BBB"))
                .andExpect(jsonPath("$.counts[1].count").value(3))
                .andExpect(jsonPath("$.counts[2].word").value("BB"))
                .andExpect(jsonPath("$.counts[2].count").value(2))
                .andExpect(jsonPath("$.counts[*].word", containsInAnyOrder("BBBB", "BBB", "BB", "BR", "BLZ", "D", "BEVM", "BZQ", "BL", "XR", "BAZ", "BA")));
    }

    @Test
    public void testAllExcludeStopWordsTrueUseStemsFlase() throws Exception {
        String content = "D, XR.\n" // Stop words
                + "BL, BLZ, BEVM, BZQ, BZL, BPZL, BEZL?\n" // Words with suffixes
                + "BB, BBB, BBBB, BB, BBB, BBBB, BBB, BBBB, BBBB!\n"; // Words to count
        this.mockMvc.perform(post("/analyze").param("exclude-stop-words", "true").param("use-stems", "false").content(content))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.counts.length()", is(10)))
                .andExpect(jsonPath("$.counts[0].word").value("BBBB"))
                .andExpect(jsonPath("$.counts[0].count").value(4))
                .andExpect(jsonPath("$.counts[1].word").value("BBB"))
                .andExpect(jsonPath("$.counts[1].count").value(3))
                .andExpect(jsonPath("$.counts[2].word").value("BB"))
                .andExpect(jsonPath("$.counts[2].count").value(2))
                .andExpect(jsonPath("$.counts[*].word", containsInAnyOrder("BBBB", "BBB", "BB", "BLZ", "BZL", "BEVM", "BZQ", "BEZL", "BL", "BPZL")));
    }

    @Test
    public void testAllExcludeStopWordsTrueUseStemsTrue() throws Exception {
        String content = "D, XR.\n" // Stop words
                + "BL, BLZ, BEVM, BZQ, BZL, BPZL, BEZL?\n" // Words with suffixes
                + "BB, BBB, BBBB, BB, BBB, BBBB, BBB, BBBB, BBBB!\n"; // Words to count
        this.mockMvc.perform(post("/analyze").param("exclude-stop-words", "true").param("use-stems", "true").content(content))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.counts.length()", is(10)))
                .andExpect(jsonPath("$.counts[0].word").value("BBBB"))
                .andExpect(jsonPath("$.counts[0].count").value(4))
                .andExpect(jsonPath("$.counts[1].word").value("BBB"))
                .andExpect(jsonPath("$.counts[1].count").value(3))
                .andExpect(jsonPath("$.counts[2].word").value("BB"))
                .andExpect(jsonPath("$.counts[2].count").value(2))
                .andExpect(jsonPath("$.counts[*].word", containsInAnyOrder("BBBB", "BBB", "BB", "BR", "BLZ", "BEVM", "BZQ", "BL", "BAZ", "BA")));
    }

    @Test
    public void testEmptyAnalysis() throws Exception {
        this.mockMvc.perform(get("/analysis?prev-result=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.counts.length()", is(0)));
        this.mockMvc.perform(get("/analysis?prev-result=1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.counts.length()", is(0)));
    }

    @Test
    public void test1Analysis() throws Exception {
        String content = "D, XR.\n" // Stop words
                + "BL, BLZ, BEVM, BZQ, BZL, BPZL, BEZL?\n" // Words with suffixes
                + "BB, BBB, BBBB, BB, BBB, BBBB, BBB, BBBB, BBBB!\n"; // Words to count
        this.mockMvc.perform(post("/analyze").param("exclude-stop-words", "true").param("use-stems", "true").content(content))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.counts.length()", is(10)))
                .andExpect(jsonPath("$.counts[0].word").value("BBBB"))
                .andExpect(jsonPath("$.counts[0].count").value(4))
                .andExpect(jsonPath("$.counts[1].word").value("BBB"))
                .andExpect(jsonPath("$.counts[1].count").value(3))
                .andExpect(jsonPath("$.counts[2].word").value("BB"))
                .andExpect(jsonPath("$.counts[2].count").value(2))
                .andExpect(jsonPath("$.counts[*].word", containsInAnyOrder("BBBB", "BBB", "BB", "BR", "BLZ", "BEVM", "BZQ", "BL", "BAZ", "BA")));
        this.mockMvc.perform(post("/analyze").param("exclude-stop-words", "true").param("use-stems", "true").content(content))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.counts.length()", is(10)))
                .andExpect(jsonPath("$.counts[0].word").value("BBBB"))
                .andExpect(jsonPath("$.counts[0].count").value(4))
                .andExpect(jsonPath("$.counts[1].word").value("BBB"))
                .andExpect(jsonPath("$.counts[1].count").value(3))
                .andExpect(jsonPath("$.counts[2].word").value("BB"))
                .andExpect(jsonPath("$.counts[2].count").value(2))
                .andExpect(jsonPath("$.counts[*].word", containsInAnyOrder("BBBB", "BBB", "BB", "BR", "BLZ", "BEVM", "BZQ", "BL", "BAZ", "BA")));
        this.mockMvc.perform(post("/analyze").param("exclude-stop-words", "true").param("use-stems", "true").content(content))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.counts.length()", is(10)))
                .andExpect(jsonPath("$.counts[0].word").value("BBBB"))
                .andExpect(jsonPath("$.counts[0].count").value(4))
                .andExpect(jsonPath("$.counts[1].word").value("BBB"))
                .andExpect(jsonPath("$.counts[1].count").value(3))
                .andExpect(jsonPath("$.counts[2].word").value("BB"))
                .andExpect(jsonPath("$.counts[2].count").value(2))
                .andExpect(jsonPath("$.counts[*].word", containsInAnyOrder("BBBB", "BBB", "BB", "BR", "BLZ", "BEVM", "BZQ", "BL", "BAZ", "BA")));
        this.mockMvc.perform(get("/analysis?prev-result=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.counts.length()", is(0)));
        this.mockMvc.perform(get("/analysis?prev-result=1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.counts.length()", is(10)))
                .andExpect(jsonPath("$.counts[0].word").value("BBBB"))
                .andExpect(jsonPath("$.counts[0].count").value(4))
                .andExpect(jsonPath("$.counts[1].word").value("BBB"))
                .andExpect(jsonPath("$.counts[1].count").value(3))
                .andExpect(jsonPath("$.counts[2].word").value("BB"))
                .andExpect(jsonPath("$.counts[2].count").value(2))
                .andExpect(jsonPath("$.counts[*].word", containsInAnyOrder("BBBB", "BBB", "BB", "BR", "BLZ", "BEVM", "BZQ", "BL", "BAZ", "BA")));
        this.mockMvc.perform(get("/analysis?prev-result=2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.counts.length()", is(10)))
                .andExpect(jsonPath("$.counts[0].word").value("BBBB"))
                .andExpect(jsonPath("$.counts[0].count").value(4))
                .andExpect(jsonPath("$.counts[1].word").value("BBB"))
                .andExpect(jsonPath("$.counts[1].count").value(3))
                .andExpect(jsonPath("$.counts[2].word").value("BB"))
                .andExpect(jsonPath("$.counts[2].count").value(2))
                .andExpect(jsonPath("$.counts[*].word", containsInAnyOrder("BBBB", "BBB", "BB", "BR", "BLZ", "BEVM", "BZQ", "BL", "BAZ", "BA")));
        this.mockMvc.perform(get("/analysis?prev-result=3"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.counts.length()", is(0)));
    }

}
