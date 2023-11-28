import java.util.Scanner;

public class Main{
    static String gameString = "_";

    public static void main(String[] args) {
        char again = 'y';
        Scanner scan = new Scanner(System.in);
        int triesLeft = 0;
        int wordLength = 0;
        
        //play again loop
        while(again == 'y'){
            System.out.print("Enter word Length: ");
            wordLength = scan.nextInt();
            scan.nextLine(); //buffer

            for(int i = 1; i < wordLength; i++){
                gameString += " _";
            }
            System.out.println(gameString);

            // main game loop
            while(gameString.contains("_")){
                if(triesLeft <= 0){
                    break;
                }
                soEvilStuff(scan.nextLine().charAt(0));
            }
            again = scan.nextLine().charAt(0);
        }    
    }

    private static void soEvilStuff(char guess) {
        
    }
}