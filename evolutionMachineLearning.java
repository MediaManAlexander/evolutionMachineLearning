import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

class evolutionMachineLearning {
    static Random random = new Random();
    static String targetString;
    
    static ArrayList<String> populationList = new ArrayList<String>();

    public static String replaceCharInStr(String str, char newChar, int index) {
        String beginStr = str.substring(0, index);
        String endStr = str.substring(index + 1, str.length());

        return beginStr + newChar + endStr;
    }
    
    public static char rngChar() {
        String charList = "abcdefghijklmnopqrstuvwxyz ABCDEFGHIJKLMNOPQRSTUVWXYZ,.1234567890";

        return charList.charAt(random.nextInt(charList.length()));

    }

    public static void populate(int populationSize) {
        for (int i = 0; i < populationSize; i++) {
            String currStr = "";

            for (int j = 0; j < targetString.length(); j++) {
                currStr += rngChar();
            }

            populationList.add(currStr);
        }
    }

    // Finds how many chars in a str match that of the target string and returns a percentage as a decimal
    public static double stringAccuracy(String str) {
        int strSize = targetString.length();
        int numCorrect = 0;

        for (int i = 0; i < strSize; i++) {

            if (str.charAt(i) == targetString.charAt(i)) {
                numCorrect++;
            }
        }

        return (double) numCorrect / strSize;
    }

    public static String breedStrings(String str1, String str2, int maxMutations) {
        int startStrCutoff = (int) Math.ceil((str1.length() / 2.0)); // Finds where the middle of string is, if the string is odd in size, then it will round up

        String startString = str1.substring(0, startStrCutoff);
        String endString = str2.substring(startStrCutoff, str2.length());

        String resultString = startString + endString;

        for (int i = 0; i < random.nextInt(1, maxMutations); i++) {
            int mutatedCharPos = random.nextInt(0, resultString.length());

            resultString = replaceCharInStr(resultString, rngChar(), mutatedCharPos);
        }

        return resultString;
    }

    

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Input string to be guessed (Can use all lower and capital letters, numbers, spaces, commas, and periods): ");
        targetString = scanner.nextLine();

        System.out.println();
        scanner.close();
        
        int populationSize = 24;
        int maxAllowedMutations = 3;
        int currentGeneration = 1;

        populate(populationSize);

        while (true) {
            // The position in populationList of the strings with the highest and the second highest accuracy
            int highestAccuracyPos = 0;
            int secondHighestAccuracyPos = 1;
    
            for (int i = 1; i < populationSize; i++) {
                double currStrAccuracy = stringAccuracy(populationList.get(i));
                double highestStrAccuracy = stringAccuracy(populationList.get(highestAccuracyPos));
    
                // If the current string's accuracy is higher than the highest accuracy string, highest and second highest accuracy will swap and currString will become the highest accuracy 
                if (currStrAccuracy > highestStrAccuracy) {
                    secondHighestAccuracyPos = highestAccuracyPos;
                    highestAccuracyPos = i;
    
                    continue;
                }
    
                double secondHighestStrAccuracy = stringAccuracy(populationList.get(secondHighestAccuracyPos));
    
                if (currStrAccuracy > secondHighestStrAccuracy) {
                    secondHighestAccuracyPos = i;
                }
            }
    
            String bestFitString = populationList.get(highestAccuracyPos);
            String secondBestFitString = populationList.get(secondHighestAccuracyPos);
    
            if (bestFitString.equals(targetString)) {
                System.out.println("Generation " + currentGeneration + "'s best match: " + bestFitString);
                System.out.println("Target string found in " + currentGeneration + " iterations");

                // Exit loop when target is found
                break;
            }
            
            ArrayList<String> newGeneration = new ArrayList<String>();
            for (int i = 0; i < populationSize; i++) {
                String childString = breedStrings(bestFitString, secondBestFitString, maxAllowedMutations);
                newGeneration.add(childString);
            }
    
            populationList = newGeneration;
    
            System.out.println("Generation " + currentGeneration + "'s best match: " + bestFitString);
    
            currentGeneration++;
        }

    }
}