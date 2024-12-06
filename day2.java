import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class day2 {

    public static int part1(List<List<Integer>> input) {
        return (int) input.stream().filter(day2::isSafe).count();
    }

    public static int part2(List<List<Integer>> input) {
        return (int) input.stream().filter(levels -> damp(levels, new ArrayList<>())).count();
    }

    private static boolean damp(List<Integer> levels, List<Integer> prefix) {
        if (!levels.isEmpty()) {
            int level = levels.get(0);
            List<Integer> remaining = levels.subList(1, levels.size());

            // Try both keeping the prefix and adding the current level to it
            return isSafe(reverseAndCombine(prefix, remaining)) || damp(remaining, prepend(prefix, level));
        }

        // If levels is empty, check the prefix
        return isSafe(prefix);
    }

    private static boolean isSafe(List<Integer> levels) {
        if (levels.size() < 2) {
            return true; // Single element is trivially safe
        }

        for (int i = 1; i < levels.size(); i++) {
            int diff = levels.get(i) - levels.get(i - 1);

            // Check if the difference is within the safe range
            if (diff < -3 || diff > 3 || diff == 0) {
                return false;
            }
        }

        int sign = Integer.signum(levels.get(1) - levels.get(0));
        return checkChunks(levels, sign);
    }

    private static boolean checkChunks(List<Integer> levels, int sign) {
        for (int i = 1; i < levels.size(); i++) {
            int diff = levels.get(i) - levels.get(i - 1);
            if (sign * diff < 1 || sign * diff > 3) {
                return false;
            }
        }
        return true;
    }

    private static List<Integer> reverseAndCombine(List<Integer> prefix, List<Integer> remaining) {
        List<Integer> combined = new ArrayList<>(prefix);
        Collections.reverse(combined);
        combined.addAll(remaining);
        return combined;
    }

    private static List<Integer> prepend(List<Integer> prefix, int level) {
        List<Integer> newPrefix = new ArrayList<>(prefix);
        newPrefix.add(0, level);
        return newPrefix;
    }

    public static List<List<Integer>> parseInput(List<String> input) {
        return input.stream()
                .map(line -> Arrays.stream(line.split(" "))
                        .map(Integer::parseInt)
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        try {
            // Read input file
            List<String> lines = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader("day2.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                }
            }

            // Parse input
            List<List<Integer>> reports = parseInput(lines);

            // Part 1
            int safeCountPart1 = part1(reports);
            System.out.println("Part 1: Number of safe reports = " + safeCountPart1);

            // Part 2
            int safeCountPart2 = part2(reports);
            System.out.println("Part 2: Number of damped safe reports = " + safeCountPart2);

        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
        }
    }
}
