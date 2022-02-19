/*
 * This file was last modified at 2022.01.24 19:51 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * Main.java
 * $Id$
 */

package su.svn;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Welcome!
 */
public class Main {

    private static void check(String consoleOutput) {
        List<Integer> list = Arrays.stream(consoleOutput.trim().split(" "))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        for (int i = 0; i <
                UnsafeSequenceThreadExecutor.MAX_SIZE; i++) {
            if ( ! list.contains(i)) {
                System.out.println("Fail, result not contained: " + i);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Welcome!");
        ConsoleOutput consoleOutput = ConsoleOutput.redirect();
        UnsafeSequenceThreadExecutor.race();
        consoleOutput.revertBack();
        check(consoleOutput.toString());
        System.out.println("Bye!");
    }
}
