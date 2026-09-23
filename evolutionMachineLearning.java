import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

class evolutionMachineLearning {
    static Random random = new Random();
    static String targetString;
    
    static ArrayList<Agent> populationList = new ArrayList<Agent>();

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
            populationList.add(new Agent(populationList, targetString.length()))
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

    // This is a function for breeding methods which takes the population and splits it into groups,
    // the most fit of each group becomes a parent. The function returns an array of parents.
    public Agent[] tournamentSelection(int numGroups) {
        if (populationList.size() % numGroups == 0) {
            Agent[] parents = new Agent[numGroups];
            for (int i = 0; i < numGroups; i++) {
                Agent bestFit = populationList.get(0);
                for (int j = 1; j < populationList.size() / numGroups; j++) {
                    if (populationList.get(populationList.size() / numGroups * i + j))
                        populationList.
                }
            }
        }
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

        for (Agent agent : populationList) {
            agent.calcFitness(targetString);
        }

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

class Agent {
    Random random = new Random();

    ArrayList<Agent> population; // The population this agent is a part of
    int generationsAlive = 0; // How many generations has this agent lived through 
    
    String genes;
    double fitness;
    

    public Agent(ArrayList<Agent> population, int geneLen) {
        this.population = population;

        for (int j = 0; j < geneLen; j++) {
            this.genes += evolutionMachineLearning.rngChar();
        }
    }

    public Agent(ArrayList<Agent> population, Agent parent1, Agent parent2, int maxMutations) {
        int midpoint = (int) Math.ceil((parent1.genes.length() / 2.0)); // Finds where the middle of string is, if the string is odd in size, then it will round up

        String startGenes = parent1.genes.substring(0, midpoint);
        String endGenes = parent2.genes.substring(midpoint, parent2.genes.length());

        String resultGenes = startGenes + endGenes;

        for (int i = 0; i < this.random.nextInt(1, maxMutations); i++) {
            int mutatedCharPos = random.nextInt(0, resultGenes.length());

            resultGenes = evolutionMachineLearning.replaceCharInStr(resultGenes, evolutionMachineLearning.rngChar(), mutatedCharPos);
        }

        this.genes = resultGenes;
    }

    public int breed(Agent mate, int maxMutations, int numOffspring) {
        for (int i = 0; i < numOffspring; i++) {
            this.population.add(new Agent(this.population, this, mate, maxMutations));
        }

        return 0; // Agent was able to breed successfully
    }

    public void die() {
        this.population.remove(this);
    }

    public int advanceGeneration(int chanceOfDeath) {
        this.generationsAlive++;

        if (this.random.nextDouble() < chanceOfDeath) {
            this.die();
            return 1; // Code for agent death
        }

        return 0; // Code for agent successfully making it to the next generation.
    }

    public void calcFitness(String target) {
        int strSize = target.length();
        int numCorrect = 0;

        for (int i = 0; i < strSize; i++) {

            if (this.genes.charAt(i) == target.charAt(i)) {
                numCorrect++;
            }
        }

        fitness = (double) numCorrect / strSize;
    }

    public void reconstructGenes(int newGeneLen) {
        for (int j = 0; j < newGeneLen; j++) {
            this.genes += evolutionMachineLearning.rngChar();
        }
    }
}