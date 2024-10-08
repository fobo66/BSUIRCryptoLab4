package dev.fobo66.crypto

import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.default

private const val DEFAULT_NUMBERS_COUNT = 10

fun main(args: Array<String>) {
    var randomNumber: Long

    val parser = ArgParser("lab4")

    val generatedNumbersCount by parser
        .option(
            ArgType.Int,
            shortName = "n",
            description = "Count of generated random numbers",
        ).default(DEFAULT_NUMBERS_COUNT)

    val seed by parser.option(
        ArgType.Int,
        shortName = "s",
        fullName = "seed",
        description = "Initial value (\"seed\") for random number generator",
    )

    val formula by parser
        .option(
            ArgType.Choice<Formula>(),
            shortName = "f",
            fullName = "formula",
            description =
                "Formula for random number generator to generate random numbers. " +
                    "Can be either Linear, Quadratic or Cubic. Default value is Linear",
        ).default(Formula.Linear)

    parser.parse(args)

    val generator = PseudoRandomNumberGenerator(seed ?: 0)

    println("Using $formula formula for generating random numbers...")

    repeat(generatedNumbersCount) {
        randomNumber =
            when (formula) {
                Formula.Linear -> generator.computeLCG()
                Formula.Quadratic -> generator.computeQuadraticLCG()
                Formula.Cubic -> generator.computeCubicLCG()
            }
        println(randomNumber)
    }
}
