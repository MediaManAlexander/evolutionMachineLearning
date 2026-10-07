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
            populationList.add(new Agent(populationList, targetString.length()));
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

    public static Agent bestFitness(ArrayList<Agent> agentList) {
        Agent bestFit = agentList.get(0);

        for (Agent agent : agentList) {
            if (agent.fitness > bestFit.fitness) {
                bestFit = agent;
            }
        }

        return bestFit;
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
    public static ArrayList<Agent> tournamentSelection(int numGroups) {
        if (numGroups <= 0) numGroups = 1;
        if (numGroups > populationList.size()) return populationList;

        // Make sure numGroups is even
        if (numGroups % 2 != 0) numGroups++;

        ArrayList<Agent> parents = new ArrayList<Agent>();

        ArrayList<Agent>[] groups = new ArrayList[numGroups];
        for (int i = 0; i < groups.length; i++) {
            groups[i] = new ArrayList<Agent>();
        }
        
        int currGroup = 0;
        for (Agent agent : populationList) {
            groups[currGroup].add(agent);

            currGroup++;
            if (currGroup > (numGroups - 1)) currGroup = 0;
        }

        for (ArrayList<Agent> group : groups) {
            parents.add(bestFitness(group));
        }

        return parents;
    }

    

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Input string to be guessed (Can use all lower and capital letters, numbers, spaces, commas, and periods): ");
        targetString = scanner.nextLine();

        System.out.println();
        scanner.close();
        
        int initPopulationSize = 24;
        int maxAllowedMutations = 3;
        int minOffSpring = 2;
        int maxOffSpring = 4;

        int currentGeneration = 1;

        populate(initPopulationSize);

        for (Agent agent : populationList) {
            agent.calcFitness(targetString);
        }

        while (true) {
            // The position in populationList of the strings with the highest and the second highest accuracy
            
            ArrayList<Agent> parents = tournamentSelection(4);

            for (int i = 0; i < populationList.size(); i++) {
                if (populationList.get(i).advanceGeneration(0.25) == 1) {
                    i--;
                }
            }

            for (int i = 0; i < parents.size() - 1; i += 2) {
                parents.get(i).breed(parents.get(i + 1), maxAllowedMutations, random.nextInt(minOffSpring, maxOffSpring));
            }

            System.out.println(random.nextInt(minOffSpring, maxOffSpring));
            System.out.println(populationList);
            System.out.println(parents);
            System.out.println("Generation " + currentGeneration + "'s best match: " + evolutionMachineLearning.bestFitness(populationList).genes);
    
            if (evolutionMachineLearning.bestFitness(populationList).genes == targetString) break;
            
            currentGeneration++;
        }

    }
}

class Agent {
    Random random = new Random();

    ArrayList<Agent> population; // The population this agent is a part of
    int generationsAlive = 0; // How many generations has this agent lived through 
    
    String genes = "";
    double fitness;
    

    public Agent(ArrayList<Agent> population, int geneLen) {
        this.population = population;

        for (int j = 0; j < geneLen; j++) {
            this.genes += evolutionMachineLearning.rngChar();
        }
    }

    private Agent(ArrayList<Agent> population, Agent parent1, Agent parent2, int maxMutations) {
        this.population = population;

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

        this.die();
        mate.die();

        return 0; // Agent was able to breed successfully
    }

    public int breed(Agent mate, int maxMutations, int numOffspring, boolean dieWhenBreed) {
        for (int i = 0; i < numOffspring; i++) {
            this.population.add(new Agent(this.population, this, mate, maxMutations));
        }

        if (dieWhenBreed) {
            this.die();
            mate.die();
        }

        return 0; // Agent was able to breed successfully
    }

    public void die() {
        this.population.remove(this);
    }

    public int advanceGeneration(double chanceOfDeath) {
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