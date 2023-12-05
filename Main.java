import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Main {
    static Map<Integer, Set<String>> wordLengthMap = new HashMap<>();
    static Map<Integer, Set<String>> mapOfIndecies = new HashMap<>();
    static StringBuilder gameString = new StringBuilder("_ ");
    static int wordLength = 0;
    static Set<String> mainSet;
    static String triedWords;
    static int triesLeft = 0;

    public static void main(String[] args) {
        try {
            setUp(1);
        } catch (FileNotFoundException e) {
        }

        char again = 'y';
        Scanner scan = new Scanner(System.in);

        // play again loop
        while (again == 'y') {
            // SET UP
            clearConsole();
            triedWords = "";
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
            //

            setUpBoard(wordLength);

            // main game loop
            while (gameString.toString().contains("_")) {
                if (triesLeft <= 0) {
                    clearConsole();
                    System.out.println(triedWords);
                    System.out.println("LOSER");
                    System.out.println(mainSet.iterator().next().toUpperCase() + " was the final answer \n");
                    break;
                }
                clearConsole();
                mainSet.forEach(System.out::println); // TODO comment this out
                System.out.println("Guesses Remaining: " + triesLeft);
                System.out.println(gameString);
                System.out.println(triedWords);

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

    private static void setUp(int i) throws FileNotFoundException {
        for (int j = 0; j < 30; j++) {
            wordLengthMap.put(j, new HashSet<String>());
        }

        File dictionaryFile;
        if (i == 1) {
            dictionaryFile = new File("SmallDictionary.txt");
        } else {
            dictionaryFile = new File("Dictionary.txt");
        }

        Scanner fileScan = new Scanner(dictionaryFile);

        while (fileScan.hasNext()) {
            String str = fileScan.nextLine();
            wordLengthMap.get(str.length()).add(str);
        }
        fileScan.close();
    }

    private static int findLargestSet(Map<Integer, Set<String>> map) {
        Set<String> testSet = new HashSet<>();
        int largestKey = -1;
        for (Map.Entry<Integer, Set<String>> set : mapOfIndecies.entrySet()) {
            if (testSet.size() < set.getValue().size()) {
                testSet = set.getValue();
                largestKey = set.getKey();
            }
        }
        return largestKey;
    }

    private static void doEvilStuff(char guess) {

        setUpMapOfIndicies(guess);

        int largestSetIndex = findLargestSet(mapOfIndecies);
        Set<String> returnSet = mapOfIndecies.get(largestSetIndex);

        if (largestSetIndex == -1) {
            triedWords += guess + " ";
            triesLeft--;
        } else {
            gameString.setCharAt(largestSetIndex * 2, guess);
        }

        // System.out.println(mapOfIndecies.toString());
        // System.out.println(returnSet.toString());

        mapOfIndecies.clear();
        mainSet = returnSet;
    }

    private static void setUpMapOfIndicies(char guess) {
        for (int i = -1; i < wordLength; i++) {
            mapOfIndecies.put(i, new HashSet<>());
        }

        for (String s : mainSet) {
            mapOfIndecies.get(s.indexOf(guess)).add(s);
            mapOfIndecies.get(s.lastIndexOf(guess)).add(s);
        }
    }

    private static void setUpBoard(int length) {
        gameString = new StringBuilder("_ ");
        for (int i = 1; i < length; i++) {
            gameString.append("_ ");
        }
    }

    private static void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
