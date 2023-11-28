import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main{
    static String gameString = "_";
    static ArrayList<String> dictionary;

    public static void main(String[] args) {
        dictionary = loadDictionary(0);
        char again = 'y';
        Scanner scan = new Scanner(System.in);
        int triesLeft = 0;
        int wordLength = 0;
        
        //play again loop
        while(again == 'y'){
            while(wordLength <= 2 || wordLength >= 29 || triesLeft <= 0){
                System.out.print("Enter word Length: ");
                wordLength = scan.nextInt();
                scan.nextLine(); //buffer

                System.out.print("\nEnter num of guesses: ");
                triesLeft = scan.nextInt();
                scan.nextLine();

                for(String s : findWordOfLenght(wordLength)){
                    System.out.println(s);
                }
            }
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
    }

    private static ArrayList<String> findWordOfLenght(int length){
        ArrayList<String> wordsOfLengthN = new ArrayList<>();
        for(String s : dictionary){
            if(s.length() == length){
                wordsOfLengthN.add(s);
            }
        }
        return wordsOfLengthN; 
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

    private static ArrayList<String> loadDictionary(int i){ //TODO remove the if statement
        File dictionaryFile;
        ArrayList<String> dictionaryArray = new ArrayList<>();
        if(i == 1){
            dictionaryFile = new File("SmallDictionary.txt");
        }else{
            dictionaryFile = new File("Dictionary.txt");
        }
        
        try {
            Scanner fileScan = new Scanner(dictionaryFile);
            while(fileScan.hasNext()){
                dictionaryArray.add(fileScan.nextLine());
            }
            fileScan.close();
        } catch (FileNotFoundException e) {}

        return dictionaryArray;
    }
}
