import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Main {
    static Map<Integer, Set<String>> wordLengthMap = new HashMap<>();
    static Map<ArrayList<Integer>, Set<String>> mapOfIndecies = new HashMap<>();
    static StringBuilder gameString;
    static Set<String> mainSet;
    static int wordLength = 0;
    static String triedChars;
    static int triesLeft = 0;

    public static void main(String[] args) {
        try {
            setUp(0);
        } catch (FileNotFoundException e) {
        }

        char again = 'y';
        Scanner scan = new Scanner(System.in);

        // play again loop
        while (again == 'y') {
            
            setUpGame(scan);

            setUpBoard(wordLength);

            // main game loop
            while (gameString.toString().contains("_")) {
                if (triesLeft <= 0) {
                    gameOver();
                    break;
                }
                clearConsole();
                //mainSet.forEach(System.out::println); // TODO comment this out
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

    private static void setUpGame(Scanner scan){
            clearConsole();
            triedChars = "";
            mainSet = null;
            while (mainSet == null || mainSet.size() == 0) {
                try {
                    System.out.print("Enter word Length: ");
                    wordLength = scan.nextInt();
                    mainSet = wordLengthMap.get(wordLength);

                    scan.nextLine(); // buffer
                    if (mainSet.size() <= 0) throw new Exception();
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
    }

    private static void gameOver() {
        clearConsole();
        System.out.println(gameString);
        System.out.println(triedChars);
        System.out.println("HOW UNFORTUNATE");
        System.out.println(mainSet.iterator().next().toUpperCase() + " was the final answer \n");
    }

    private static void setUp(int i) throws FileNotFoundException {
        File dictionaryFile;
        if (i == 1) {
            dictionaryFile = new File("SmallDictionary.txt");
        } else {
            dictionaryFile = new File("Dictionary.txt");
        }

        Scanner fileScan = new Scanner(dictionaryFile);

        while (fileScan.hasNext()) {
            String str = fileScan.nextLine();
            if(!wordLengthMap.containsKey(str.length()))
                wordLengthMap.put(str.length(), new HashSet<String>());
 
            wordLengthMap.get(str.length()).add(str);
        }
        fileScan.close();
    }

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

    private static void doEvilStuff(char guess) {

        setUpMapOfIndicies(guess);
        //System.out.println(mapOfIndecies); //TODO comment this


        ArrayList<Integer> largestSetIndex = findLargestSet(mapOfIndecies);
        Set<String> returnSet = mapOfIndecies.get(largestSetIndex);
        
        
        if (largestSetIndex.isEmpty() || triedChars.contains(guess + " ")) {
            triesLeft--;
        } else {
            addLetterToAnswer(guess, largestSetIndex);
        }
        
        if(!triedChars.contains(guess + " ")){
            triedChars += guess + " ";
        }

        mapOfIndecies.clear();
        mainSet = returnSet;
    }

    private static void setUpMapOfIndicies(char guess) {
        for (String s : mainSet) {
            ArrayList<Integer> list = countOccurances(guess, s);
            if(mapOfIndecies.containsKey(list)){
                mapOfIndecies.get(list).add(s);
            }else{
                mapOfIndecies.put(list, new HashSet<>());
                mapOfIndecies.get(list).add(s);
            }
        }
    }

    private static ArrayList<Integer> countOccurances(char guess, String str){
        ArrayList<Integer> indecies = new ArrayList<>(str.length());
        for(int i = 0; i < str.length(); i++){
            if(str.charAt(i) == guess){
                indecies.add(i);
            }
        }
        return indecies;
    }

    private static void addLetterToAnswer(char ch, ArrayList<Integer> indecies){
        for(int i : indecies){
            gameString.setCharAt(i * 2, ch);
        }
    }

    private static void setUpBoard(int length) {
        gameString = new StringBuilder();
        for (int i = 0; i < length; i++) {
            gameString.append("_ ");
        }
    }

    private static void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
