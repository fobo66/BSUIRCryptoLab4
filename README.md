# BSUIRCryptoLab4 – LCG
===

Simple linear congruential generator: http://en.wikipedia.org/wiki/Linear_congruential_generator

Modifications:

 * Added randomness and safety by DES cipher in CTR mode
 * Replaced builtin Random with Apache Commons RNG
 * Added more formulas to generator from assignment
 * Converted to Kotlin

 To see this project in action, run ```./gradlew run``` or ```./gradlew.bat run```

To add options for app, pass args to gradle via: ```./gradlew run --args="-key value"```. 
You can see list of the available command line options below:

 * -n – (optional) count of generated random numbers. Default value is ```10```.
 * -s, or -seed – (optional) initial value ("seed") for random number generator. By default it's generated
 * -f, or -formula – (optional) formula for random number generator to generate random numbers. Can be either ```Linear```, ```Quadratic``` or ```Cubic```. Default value is ```Linear```.