import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Main{
    static Map<Integer, Set<String>> wordLengthMap = new HashMap<>();
    static Map<Integer, Set<String>> mapOfIndecies = new HashMap<>();
    static String gameString = "_";
    static int wordLength = 0;
    static Set<String> mainSet = null;
    static String triedWords;
    static int triesLeft = 0;

    public static void main(String[] args) {
        try { setUp(0); } catch (FileNotFoundException e) {}

        char again = 'y';
        Scanner scan = new Scanner(System.in);
        
        //play again loop
        while(again == 'y'){
            triedWords = "";
            while(mainSet == null || mainSet.size() == 0 || triesLeft <= 0){
                System.out.print("Enter word Length: ");
                wordLength = scan.nextInt();
                mainSet = wordLengthMap.get(wordLength);
                scan.nextLine(); //buffer

                System.out.print("\nEnter num of guesses: ");
                triesLeft = scan.nextInt();
                scan.nextLine(); //buffer
            }
            //clearConsole();
            
            setUpBoard(wordLength);
            
            // main game loop
            while(gameString.contains("_")){
                if(triesLeft <= 0){
                    clearConsole();
                    System.out.println("LOSER");
                    System.out.println(mainSet.iterator().next() + " was the final answer");
                    System.out.println("types y to continue");
                    break;
                }
                clearConsole();
                mainSet.forEach(System.out::println);
                System.out.println(gameString);
                System.out.println(triedWords);
                
                mainSet = doEvilStuff(scan.nextLine().charAt(0));
            }
            again = scan.nextLine().toLowerCase().charAt(0);
        }   
        scan.close(); 
    }

    private static void setUp(int i) throws FileNotFoundException {
        for(int j = 0; j < 30; j++){
            wordLengthMap.put(j, new HashSet<String>());
        }

        File dictionaryFile;
        if(i == 1){
            dictionaryFile = new File("SmallDictionary.txt");
        }else{
            dictionaryFile = new File("Dictionary.txt");
        }

        Scanner fileScan = new Scanner(dictionaryFile);

        while(fileScan.hasNext()){
            String str = fileScan.nextLine();
            wordLengthMap.get(str.length()).add(str);
        }
        fileScan.close();
    }
    
    private static Set<String> doEvilStuff(char guess) {
        for(int i = -1; i < wordLength; i++){
            mapOfIndecies.put(i, new HashSet<>());
        }
        for(String s : mainSet){
            mapOfIndecies.get(s.indexOf(guess)).add(s);
            mapOfIndecies.get(s.lastIndexOf(guess)).add(s);
        }

        Set<String> testSet = new HashSet<>();
        for(Map.Entry<Integer, Set<String>> set : mapOfIndecies.entrySet()){
            if(testSet.size() < set.getValue().size()){
                testSet = set.getValue();
            }
        }
        if(testSet.equals(mapOfIndecies.get(-1))){
            triedWords += guess + " ";
            triesLeft--;
        }
        return testSet;
    }   
    
    private static void setUpBoard(int length){
        for(int i = 1; i < length; i++){
                gameString += " _";
            }
    }

    private static void clearConsole(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
