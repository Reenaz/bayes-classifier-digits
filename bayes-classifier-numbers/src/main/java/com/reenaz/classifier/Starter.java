package com.reenaz.classifier;

import de.daslaboratorium.machinelearning.classifier.Classifier;
import de.daslaboratorium.machinelearning.classifier.bayes.BayesClassifier;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class Starter {
    public static void main(String[] args) throws FileNotFoundException {
        String filePath = "";

        if(args.length > 0) {
            filePath = args[0];
        }else{
            System.out.println("No file path");
            System.exit(0);
        }

        File inputFile = new File(filePath);

        Scanner sc = new Scanner(inputFile);

        int i = 0;

        List<String> trainingList = new LinkedList<>();
        List<String> testList = new LinkedList<>();

        while(sc.hasNextLine() && i<100) {
            List<String> blockList = new LinkedList<>(Arrays.asList(sc.nextLine().replaceAll("[\\s]{2,}", " ").split(" ")));
            blockList.remove(0);
            trainingList.addAll(blockList);
            i++;
        }

        while(sc.hasNextLine()) {
            List<String> blockList = new LinkedList<>(Arrays.asList(sc.nextLine().replaceAll("[\\s]{2,}", " ").split(" ")));
            blockList.remove(0);
            testList.addAll(blockList);
        }

        sc.close();

        final Classifier<String, Integer> bayesClassifier = new BayesClassifier<>();

        bayesClassifier.setMemoryCapacity(100);


        trainingList.forEach(block -> {
            List<String> blockArr = Arrays.stream(block.split("")).collect(Collectors.toList());
            blockArr.remove(4);

            bayesClassifier.learn(calculateBlockCategory(block), blockArr);

        });

        int matchedCount = 0;
        for (String block : testList) {
            List<String> blockAsList = Arrays.stream(block.split("")).collect(Collectors.toList());
            blockAsList.remove(4);
            final int category = bayesClassifier.classify(blockAsList).getCategory();
            System.out.println("block: " + block + " category: " + category + " real category: " + calculateBlockCategory(block));
            if(category == calculateBlockCategory(block)) matchedCount++;
        }

        System.out.println("\n\nList count " + testList.size() + " Matched count: " + matchedCount + " percentage: " + (double) matchedCount /(double) testList.size()*100);

    }

    private static Integer calculateBlockCategory(String block) {
        block = block.substring(0, 4);
        String[] nubmArr = block.split("");
        int category = 0;
        for(int i = 0; i < 4; i++ ) {
            category += Integer.parseInt(nubmArr[i]);
        }
        return category%7;
    }
}
