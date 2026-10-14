import java.util.ArrayList;

class testing {
    public static void main(String[] args) {
        String targetString = "target";

        ArrayList<Agent> testList = new ArrayList<Agent>();

        testList.add(new Agent(targetString, targetString));
        testList.add(new Agent(targetString, "dsefgr"));
        testList.add(new Agent(targetString, "tttttt"));
        testList.add(new Agent(targetString, "rallll"));
        testList.add(new Agent(targetString, "tarppp"));

        evolutionMachineLearning.populationList = testList;

        for (Agent agent : evolutionMachineLearning.bestPerformersSelection(2)) {
            System.out.println(agent.genes + " : " + agent.fitness);
        }
    }
}