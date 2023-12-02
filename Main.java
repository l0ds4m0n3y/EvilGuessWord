import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Main{
    static Map<Integer, Set<String>> wordLengthMap = new HashMap<>();
    static String gameString = "_";
    static ArrayList<String> dictionary;

    public static void main(String[] args) {
        try { setUp(0); 
        } catch (FileNotFoundException e) {}

        char again = 'y';
        Scanner scan = new Scanner(System.in);
        int triesLeft = 0;
        int wordLength = 0;
        Set<String> mainSet = null;
        
        //play again loop
        while(again == 'y'){
            while(mainSet == null || mainSet.size() == 0 || triesLeft <= 0){
                System.out.print("Enter word Length: ");
                wordLength = scan.nextInt();
                mainSet = wordLengthMap.get(wordLength);
                scan.nextLine(); //buffer

                System.out.print("\nEnter num of guesses: ");
                triesLeft = scan.nextInt();
                scan.nextLine(); //buffer
            }
            mainSet.forEach(System.out::println);
            //clearConsole();

            setUpBoard(wordLength);

            // main game loop
            while(gameString.contains("_")){
                if(triesLeft <= 0){
                    break;
                }

                System.out.println(gameString);

                doEvilStuff(scan.nextLine().charAt(0));
            }
            again = scan.nextLine().charAt(0);
        }   
        scan.close(); 
    }

    private static void setUp(int i) throws FileNotFoundException {
        for(int j = 0; j < 30; j++ ){
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
    
    private static void doEvilStuff(char guess) {
        
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
