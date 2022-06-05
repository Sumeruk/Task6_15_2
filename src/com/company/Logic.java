package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.*;

public class Logic {
    private Map<String, Integer> map;

    public Logic(Map<String, Integer> map) {
        this.map = map;
    }

    public List<String> readLinesFromFile(String fileName) throws FileNotFoundException {
        List<String> lines;
        try (Scanner scanner = new Scanner(new File(fileName))) {
            lines = new ArrayList<>();
            while (scanner.hasNext()) {
                lines.add(scanner.nextLine());
            }
        }
        return lines;
    }

    public List<String> addStatisticalDataToMap(String fileName, int n) throws FileNotFoundException {
        List<String> lines = readLinesFromFile(fileName);

        enterAllWordsIntoDictionary(lines, map);

        Set<String> keys = map.keySet();
        List<String> list = new ArrayList<>();

        for (String key : keys) {
            if (map.get(key) == n) {
                list.add(key);
            }

        }

        return list;
    }

    private void enterAllWordsIntoDictionary(List<String> lines, Map<String, Integer> map) {
        for (String line : lines) {
            line = line.replaceAll("— ", "");
            line = line.replaceAll("…", "");
            line = line.replaceAll("[\\s\\d-,.?!:;()'\"]+", " ");
            String[] arr = line.split(" ");
            for (String word : arr) {
                if (map.containsKey(word)) {
                    map.put(word, map.get(word) + 1);
                } else {
                    map.put(word, 1);
                }
            }

        }
    }

    public List<String> scanAndOutput(String fileName) throws FileNotFoundException {
        System.out.println("Введите желаемое количество слов:");

        Scanner scan = new Scanner(System.in);
        int n = scan.nextInt();

        Map<String, Integer> map = new SimpleHashMap<>(125);
        Logic logic = new Logic(map);
        List<String> list = logic.addStatisticalDataToMap(fileName, n);
        if (list.isEmpty()) {
            System.out.println("Такого количества слов нет, попробуйте еще раз!");
            list = logic.scanAndOutput(fileName);
        } else {
            String lastWord = "";
            if ((n % 10 == 2) || (n % 10 == 3) || (n % 10 == 4)) {
                lastWord = " раза: ";
            } else {
                lastWord = " раз: ";
            }
            System.out.print("Слова, которые встретились в тексте " + n + lastWord);
            for (String word : list) {
                System.out.print(word + " ");
            }
        }
        return list;
    }
}

