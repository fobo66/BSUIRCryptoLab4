package io.fobo66.crypto;

import org.apache.commons.cli.*;

public class Lab4 {
    public static void main(String[] args) {
        int generatedNumbersCount = 10;
        PRNG prng = new PRNG();

        Options options = new Options();
        options.addOption("n", true, "Count of generated random numbers");
        options.addOption("s", "seed", true, "Initial value (\"seed\") for random number generator");

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);

            if (cmd.hasOption('n')) {
                generatedNumbersCount = Integer.parseInt(cmd.getOptionValue('n'));
            }

            if (cmd.hasOption('s')) {
                prng = new PRNG(Long.parseLong(cmd.getOptionValue('s')));
            }

            for (int i = 0; i < generatedNumbersCount; i++) {
                System.out.println(prng.computeLCG());
            }
        } catch (ParseException e) {
            throw new RuntimeException("Invalid arguments", e);
        }
    }
}