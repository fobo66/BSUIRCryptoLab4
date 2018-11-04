package io.fobo66.crypto

import org.apache.commons.cli.DefaultParser
import org.apache.commons.cli.Options
import org.apache.commons.cli.ParseException

object Lab4 {
    @JvmStatic
    fun main(args: Array<String>) {
        var generatedNumbersCount = 10
        var prng = PRNG()
        var formula = "Linear"
        var randomNumber: Long

        val options = Options()
        options.addOption("n", true, "Count of generated random numbers")
        options.addOption("s", "seed", true,
                "Initial value (\"seed\") for random number generator")
        options.addOption("f", "formula", true,
                "Formula for random number generator to generate random numbers. "
                        + "Can be either Linear, Quadratic or Cubic. Default value is Linear")

        val parser = DefaultParser()
        try {
            val cmd = parser.parse(options, args)

            if (cmd.hasOption('n')) {
                generatedNumbersCount = Integer.parseInt(cmd.getOptionValue('n'))
            }

            if (cmd.hasOption('s')) {
                prng = PRNG(java.lang.Long.parseLong(cmd.getOptionValue('s')))
            }

            if (cmd.hasOption('f')) {
                formula = cmd.getOptionValue('f')
                println("Using ${formula.toLowerCase()} formula for generating random numbers...")
            }

            for (i in 0 until generatedNumbersCount) {

                randomNumber = when (formula.toLowerCase()) {
                    "linear" -> prng.computeLCG()
                    "quadratic" -> prng.computeQuadraticLCG()
                    "cubic" -> prng.computeCubicLCG()
                    else -> throw IllegalArgumentException("Formula can be either Linear, Quadratic or Cubic")
                }
                println(randomNumber)
            }
        } catch (e: ParseException) {
            throw RuntimeException("Invalid arguments", e)
        }

    }
}