class testing {
    public static void main(String[] args) {
        int populationSize = 8;
        int numGroups = 3;

        int groupSize = Math.round((long) populationSize / numGroups);
        if (populationSize % groupSize != 0) {
            while ((populationSize + 1) % groupSize != 0) {
                groupSize++;
            }
        }

        System.out.println(groupSize);
    }
}