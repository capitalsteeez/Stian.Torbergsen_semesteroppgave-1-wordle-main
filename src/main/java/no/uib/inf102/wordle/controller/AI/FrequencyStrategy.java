package no.uib.inf102.wordle.controller.AI;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import no.uib.inf102.wordle.model.word.WordleWord;
import no.uib.inf102.wordle.model.word.WordleWordList;


/**
 * This strategy finds the word within the possible words which has the highest expected
 * number of green matches.
 */
public class FrequencyStrategy implements IStrategy {

    private WordleWordList guesses;

    public FrequencyStrategy() {
        reset();
    }

    @Override

    /*
    the method eliminates impossible words based on feedback and the ncalculates the expected number of gree nmatches for each remaining word.
    calculateExpectedGreenMatches method calculates the expected nr of green matches for a given word by comparing it with other possible words.
    The word with the highest expected nr of green matches is returned.
    */
    public String makeGuess(WordleWord feedback) {
        // Task 3
        if (feedback != null) {
            guesses.eliminateWords(feedback);
        }

        String bestGuess = null;
        double maxExpectedGreenMatches = -1;
  
        for (String word : guesses.possibleAnswers()) {
            double expectedGreenMatches = calculateExpectedGreenMatches(word);
            if (expectedGreenMatches > maxExpectedGreenMatches) {
                maxExpectedGreenMatches = expectedGreenMatches;
                bestGuess = word;
            }
        }

        return bestGuess;
    }

    private double calculateExpectedGreenMatches(String word) {
        double totalGreenMatches = 0;
        List<String> possibleAnswers = guesses.possibleAnswers();

        for (String possibleWord : possibleAnswers) {
            for (int i = 0; i < word.length(); i++) {
                if (word.charAt(i) == possibleWord.charAt(i)) {
                    totalGreenMatches++;
                }
            }
        }

        return totalGreenMatches / (double) possibleAnswers.size();  
    }

    @Override
    public void reset() {
        guesses = new WordleWordList();
    }
}