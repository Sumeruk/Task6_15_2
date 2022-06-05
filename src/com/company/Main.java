package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        String  fileName = System.getProperty("user.dir")+ "\\input.txt";

        Map<String, Integer> map = new SimpleHashMap<>(125);
        Logic logic = new Logic(map);
        logic.scanAndOutput(fileName);
    }
}
