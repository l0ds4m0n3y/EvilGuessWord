import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * David Luna
 * CS 270
 * Lab 10
 * Everthing should be working according to the lab instructions 
 */
public class EvilGuessWord {
    final static File smallDictionaryFile = new File("SmallDictionary.txt");
    final static File fullDictionaryFile = new File("Dictionary.txt");

    static Map<Integer, Set<String>> wordLengthMap = new HashMap<>();
    static Map<ArrayList<Integer>, Set<String>> mapOfIndecies = new HashMap<>();
    static StringBuilder gameString;
    static Set<String> mainSet;
    static int wordLength = 0;
    static String triedChars;
    static int triesLeft = 0;

    /**
     * Where all the magic happens. Utilizes a while loop to continue the game if the player
     * loses and wants to play again, and another loop within as a main game that breaks once
     * the player loses all their guesses or if they found the missing word, prompting them 
     * to play again. 
     *  */
    public static void main(String[] args) {
        try { setUp(fullDictionaryFile); } catch (FileNotFoundException e) {}

        char again = 'y';
        Scanner scan = new Scanner(System.in);

        // play again loop
        while (again == 'y') {

            setUpGame(scan);

            while (gameString.toString().contains("_")) { // main game loop
                if (triesLeft <= 0) {
                    gameOver();
                    break;
                }
                clearConsole();
                // mainSet.forEach(System.out::println);
                System.out.println("Guesses Remaining: " + triesLeft);
                System.out.println(gameString);
                System.out.println(triedChars);

                doEvilStuff(scan.nextLine().charAt(0));
            }

            if (!gameString.toString().contains("_")) {
                clearConsole();
                System.out.println(gameString);
                System.out.println("YAY");
            }

            System.out.println("type y to play again");
            again = scan.nextLine().toLowerCase().charAt(0);
        }
        scan.close();
    }

    /**
     * Propts the player on a length of word and how many guesses they want.
     * It then sets up the game by setting the main set to a set of words of that length
     * utilizing a map.
     * @param scan Console Scanner
     * @throws Exception if the dictionary does not have words of that length or if the number 
     * of guesses is unrealistic.
     */
    private static void setUpGame(Scanner scan) {
        clearConsole();
        triedChars = "";
        mainSet = null;
        while (mainSet == null || mainSet.size() == 0) {
            try {
                System.out.print("Enter word Length: ");
                wordLength = scan.nextInt();
                mainSet = wordLengthMap.get(wordLength);

                scan.nextLine(); // buffer
                if (mainSet.size() <= 0)
                    throw new Exception();
            } catch (Exception e) {
                clearConsole();
                System.out.println("please enter a valid word length");
            }
        }

        while (triesLeft <= 0) {
            System.out.print("\nEnter a realistic number of guesses: ");
            triesLeft = scan.nextInt();
            scan.nextLine(); // buffer
            clearConsole();
        }

        setUpBoard(wordLength);
    }

    /**
     * Simple game over screen diplaying the work done by the player, their previous
     * guesses, and the "final" answer *wink wink*
     */
    private static void gameOver() {
        clearConsole();
        System.out.println(gameString);
        System.out.println(triedChars);
        System.out.println("HOW UNFORTUNATE");
        System.out.println(mainSet.iterator().next().toUpperCase() + " was the final answer \n");
    }

    /**
     * Maps words to a set depending on the lenght of the word
     * @param dictionaryFile
     * @throws FileNotFoundException
     */
    private static void setUp(File dictionaryFile) throws FileNotFoundException {
        Scanner fileScan = new Scanner(dictionaryFile);

        while (fileScan.hasNext()) {
            String str = fileScan.nextLine();
            if (!wordLengthMap.containsKey(str.length()))
                wordLengthMap.put(str.length(), new HashSet<String>());

            wordLengthMap.get(str.length()).add(str);
        }

        fileScan.close();
    }

    /**
     * Main method that handles all the "evil" part of the game.
     * It starts by mapping every word to a family (set) of words that contain
     * the guessed character at any index combination without making unecessary families:
     * 
     * guess: 'a'
     * [0] {abc acd} 
     * [0 1] {aab aac aad} 
     * [2] {bca} 
     * [] {jjj lll}
     * 
     * Next it iterates through the map finding the set that contatins the most words and setting
     * the main set to it.
     * 
     * Finally if it is to use a set that does contain the guess, it modifies the game board to reflect
     * the correct guess. If it does not it decreased the number of guesses left.
     * 
     * @param guess Character guesssed by the player
     */
    private static void doEvilStuff(char guess) {

        setUpMapOfIndicies(guess);
        // System.out.println(mapOfIndecies);

        ArrayList<Integer> largestSetIndex = findLargestSet(mapOfIndecies);
        
        /*
        * if the largest set would not add a letter to the answer or if the player guesses 
        * the same letter again it substracts the number of guesses 
        */
        if (largestSetIndex.isEmpty() || triedChars.contains(guess + " ")) {
            triesLeft--;
        } else {
            addLetterToAnswer(guess, largestSetIndex);
        }
        
        if (!triedChars.contains(guess + " ")) {
            triedChars += guess + " ";
        }
        
        mainSet = mapOfIndecies.get(largestSetIndex);
        mapOfIndecies.clear();
    }

    /**
     * Iterates through the map finding the largest set of words with the most contents
     * 
     * @param map to Search
     * @return List of indecies whose set is the largest
     */
    private static ArrayList<Integer> findLargestSet(Map<ArrayList<Integer>, Set<String>> map) {
        Set<String> testSet = new HashSet<>();
        ArrayList<Integer> largestKey = new ArrayList<>();
        for (Map.Entry<ArrayList<Integer>, Set<String>> set : mapOfIndecies.entrySet()) {
            if (testSet.size() < set.getValue().size()) {
                testSet = set.getValue();
                largestKey = set.getKey();
            }
        }
        return largestKey;
    }

    /**
     * Sets up a map using a list of indecies of the character guessed as a key
     * and adding words whose list of indicies matches with the key to a set of words
     * 
     * @param guess
     */
    private static void setUpMapOfIndicies(char guess) {
        for (String s : mainSet) {
            ArrayList<Integer> list = countOccurances(guess, s);
            if (!mapOfIndecies.containsKey(list))
                mapOfIndecies.put(list, new HashSet<>());
            mapOfIndecies.get(list).add(s);
        }
    }

    /**
     * Creates a list containing the indicies of where to find the guessed character
     * 
     * @param guess Character guessed by the player
     * @param str String to check for guessed character
     * @return  List of every index where the charcter guessed can be found
     */
    private static ArrayList<Integer> countOccurances(char guess, String str) {
        ArrayList<Integer> indecies = new ArrayList<>(str.length());
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == guess) {
                indecies.add(i);
            }
        }
        return indecies;
    }

    /**
     * Modifies the game board to include the correcly guessed character in the
     * String
     * 
     * @param ch Character to add
     * @param indecies Where to add the character
     */
    private static void addLetterToAnswer(char ch, ArrayList<Integer> indecies) {
        for (int i : indecies) {
            gameString.setCharAt(i * 2, ch);
        }
    }

    /**
     * Sets up the game board with underscores representing each cell.
     * 
     * @param length How long the word is
     */
    private static void setUpBoard(int length) {
        gameString = new StringBuilder();
        for (int i = 0; i < length; i++) {
            gameString.append("_ ");
        }
    }

    /**
     * Clears the console for a clean game experience
     */
    private static void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
