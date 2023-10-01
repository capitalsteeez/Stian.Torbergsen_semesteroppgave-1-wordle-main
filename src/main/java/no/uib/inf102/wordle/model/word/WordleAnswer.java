package no.uib.inf102.wordle.model.word;

import java.util.Random;
import java.util.HashMap;
import no.uib.inf102.wordle.resources.GetWords;

/**
 * This class represents an answer to a Wordle puzzle.
 * 
 * The answer must be one of the words in the LEGAL_WORDLE_LIST.
 */
public class WordleAnswer {

    private final String WORD;

    private static Random random = new Random();

    /**
     * Creates a WordleAnswer object with a given word.
     * @param answer
     */
    public WordleAnswer(String answer) {
        this.WORD = answer.toLowerCase();
    }

    /**
     * Creates a WordleAnswer object with a random word from the answer word list
     */
    public WordleAnswer() {
        this(random);
    }

    /**
     * Creates a WordleAnswer object with a random word from the answer word list
     * using a specified random object.
     * This gives us the opportunity to set a seed so that tests are repeatable.
     */
    public WordleAnswer(Random random) {
        this(getRandomWordleAnswer(random));
	}

    /**
     * Gets a random wordle answer
     * 
     * @param random
     * @return
     */
    private static String getRandomWordleAnswer(Random random) {
        int randomIndex = random.nextInt(GetWords.ANSWER_WORDS_LIST.size());
        String newWord = GetWords.ANSWER_WORDS_LIST.get(randomIndex);
        return newWord;
    }

    /**
     * Guess the Wordle answer. Checks each character of the word guess and gives
     * feedback on which that is in correct position, wrong position and which is
     * not in the answer word.
     * This is done by updating the AnswerType of each WordleCharacter of the
     * WordleWord.
     * 
     * @param wordGuess
     * @return wordleWord with updated answertype for each character.
     */
    public WordleWord makeGuess(String wordGuess) {
        if (!GetWords.isLegalGuess(wordGuess))
            throw new IllegalArgumentException("The word '" + wordGuess + "' is not a legal guess");

        WordleWord guessFeedback = matchWord(wordGuess, WORD);
        return guessFeedback;
    }

    /**
     * Generates a WordleWord showing the match between <code>guess</code> and
     * <code>answer</code>
     * 
     * @param guess
     * @param answer
     * @return
     */
    public static WordleWord matchWord(String guess, String answer) {
        int wordLength = answer.length();
        if (guess.length() != wordLength)
            throw new IllegalArgumentException("Guess and answer must have same number of letters but guess = " + guess
                    + " and answer = " + answer);

        // Task 1
        
        AnswerType[] feedback = new AnswerType[wordLength];
        HashMap<Character, Integer> charFrequency = new HashMap<>();

        // Populating the fequency map with chars from answer
        for (char c : answer.toCharArray()) {
            charFrequency.put(c, charFrequency.getOrDefault(c, 0) + 1);
        }

        // First pass: Tag CORRECT and update the frequency map
        for (int i = 0; i < wordLength; i++) {
            char guessChar = guess.charAt(i);
            char answerChar = answer.charAt(i);

            if (guessChar == answerChar) {
                feedback[i] = AnswerType.CORRECT;
                charFrequency.put(answerChar, charFrequency.get(answerChar) - 1);
                if (charFrequency.get(answerChar) == 0 ) {
                    charFrequency.remove(answerChar);
                }
            }
        }

        // Second pass: Tag WRONG and update the frequency map
        for (int i = 0; i < wordLength; i++) {
            char guessChar = guess.charAt(i);
    
            if (feedback[i] != AnswerType.CORRECT && charFrequency.containsKey(guessChar)) {
                feedback[i] = AnswerType.WRONG_POSITION;
                charFrequency.put(guessChar, charFrequency.get(guessChar) - 1);
                if (charFrequency.get(guessChar) == 0) {
                    charFrequency.remove(guessChar);
                }
            }
        }
           
        // Third pass: Tag remaining chars

        for (int i=0; i<wordLength; i++) {
            if (feedback[i] == null) {
                feedback[i] = AnswerType.WRONG;
            }
        }

        return new WordleWord(guess,feedback);
    }
}
