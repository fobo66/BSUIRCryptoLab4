package dev.fobo66.crypto

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.parameters.options.check
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.help
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.transform.theme
import com.github.ajalt.clikt.parameters.types.choice
import com.github.ajalt.clikt.parameters.types.int

private const val DEFAULT_NUMBERS_COUNT = 10

class Lab4 : CliktCommand() {
    val generatedNumbersCount by option("-n")
        .int()
        .default(DEFAULT_NUMBERS_COUNT)
        .help { theme.info("Count of generated random numbers") }
        .check { it > 0 }
    val seed by option("-s", "--seed")
        .int()
        .default(0)
        .help { theme.info("Initial value (\"seed\") for random number generator") }
    val formula by option("-f", "--formula")
        .choice("Linear" to Formula.Linear, "Quadratic" to Formula.Quadratic, "Cubic" to Formula.Cubic)
        .default(Formula.Linear)

    override fun run() {
        val generator = PseudoRandomNumberGenerator(seed)

        echo("Using $formula formula for generating random numbers...")

        var randomNumber: Long

        repeat(generatedNumbersCount) {
            randomNumber =
                when (formula) {
                    Formula.Linear -> generator.computeLCG()
                    Formula.Quadratic -> generator.computeQuadraticLCG()
                    Formula.Cubic -> generator.computeCubicLCG()
                }
            echo(randomNumber)
        }
    }

}

fun main(args: Array<String>) = Lab4().main(args)
