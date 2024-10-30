import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.system.exitProcess


/**
 * You can edit, run, and share this code.
 * play.kotlinlang.org
 */

private var withSS = true

fun main() {

    askAnswer()
}

fun askAnswer() {
    println("What you want to use as baseline?")
    println("1 - Monthly Salary")
    println("2 - Annual Salary")
    println("3 - Hourly Salary")
    println("4 - Daily Salary")
    print("Your answer: ")
    val userInput = readlnOrNull()
    userInput?.let {
        if (it == "quit" || it == "q") {
            exitProcess(200)
        }
        val isDigit = it.all { char -> char.isDigit() }
        if (isDigit) {
            val answer = it.toIntOrNull()
            println("With SS? (Y/N)")
            print("Your answer: ")
            val userInputSN = readlnOrNull()
            userInputSN?.let {
                if (userInputSN.lowercase() in arrayOf("y", "n")) {
                    withSS = userInputSN.lowercase() == "y"
                    when (answer) {
                        1 -> {
                            calculateWithMonthBase(askForValueInput())
                        }

                        2 -> {
                            calculateWithYearBase(askForValueInput())
                        }

                        3 -> {
                            calculateWithHourBase(askForValueInput())
                        }

                        4 -> {
                            calculateWithDailyBase(askForValueInput())
                        }

                        else -> {
                            notAValidAnswerInput()
                        }
                    }
                } else {
                    notAValidAnswerInput()
                }
            } ?: run {
                notAValidAnswerInput()
            }
        } else {
            notAValidAnswerInput()
        }
    } ?: run {
        notAValidAnswerInput()
    }
}

fun calculateWithDailyBase(valueInput: Double?) {
    valueInput?.let {
        println("---------- Gross Values ----------")
        val day = it
        println("Day = ${day.round()}")
        val hora = it / 8
        println("Hour = ${hora.round()}")
        val month = day * 21
        println("Month = ${month.round()}")
        val annual = month * 12
        println("Annual = ${annual.round()}")
        taxCalculation(month)
    } ?: run {
        notAValidAnswerInput()
    }
}

fun calculateWithMonthBase(valueInput: Double?) {
    valueInput?.let {
        println("---------- Gross Values ----------")
        val month = it
        println("Month = ${month.round()}")
        val annual = month * 12
        println("Annual = ${annual.round()}")
        val hora = month / 160
        println("Hour = ${hora.round()}")
        taxCalculation(month)
    } ?: run {
        notAValidAnswerInput()
    }
}

fun calculateWithYearBase(valueInput: Double?) {
    valueInput?.let {
        println("---------- Gross Values ----------")
        val annual = it
        println("Annual = ${annual.round()}")
        val month = it / 12
        println("Month = ${month.round()}")
        val hora = month / 160
        println("Hour = ${hora.round()}")
        taxCalculation(month)
    } ?: run {
        notAValidAnswerInput()
    }
}

fun calculateWithHourBase(valueInput: Double?) {
    valueInput?.let {
        println("---------- Gross Values ----------")
        val hora = it
        println("Hour = ${hora.round()}")
        val month = it * 160
        println("Month = ${month.round()}")
        val annual = month * 12
        println("Annual = ${annual.round()}")
        taxCalculation(month)
    } ?: run {
        notAValidAnswerInput()
    }
}

val notValidInputMessage = "Not a valid input. Let's restart."

fun notAValidAnswerInput() {
    println(notValidInputMessage)
    askAnswer()
}

fun notAValidValueInput() {
    println(notValidInputMessage)
    askForValueInput()
}

fun askForValueInput(): Double? {
    println()
    print("Insert the desired amount: ")
    val userInput = readlnOrNull()
    var answer: Double? = -1.0
    userInput?.let {
        if (it == "quit" || it == "q") {
            exitProcess(200)
        }
        val normalize = it.replace(",", ".")
        val isDigit = isNumber(normalize)
        if (isDigit) {
            answer = normalize.toDoubleOrNull()
        } else {
            notAValidValueInput()
        }
    } ?: run {
        notAValidValueInput()
    }
    return answer
}

fun taxCalculation(grossMonth: Double) {
    println("---------- Taxes ----------")
    val iva = grossMonth * 0.23
    println("VAT at 23% = ${iva.round()}")
    val ss = if (withSS) {
        (grossMonth * 0.7) * 0.214 // Apenas 70% do rendimento é considerado para SS
    } else {
        0.0
    }
    println("ss = ${ss.round()}")
    val irs =
        (grossMonth * 0.75) * 0.25 // Como é prestação de serviços, apenas 75% é considerado para IRS. Atenção que convém apresentar despesas (https://www.vendus.pt/blog/despesas-dedutiveis-empresarios-nome-individual/)
    println("irs = ${irs.round()}")
    netCalculations(grossMonth, ss, irs, iva)
}

fun netCalculations(month: Double, ss: Double, irs: Double, iva: Double) {
    println("---------- Net Calculation Without VAT ----------")
    val total = if (withSS) {
        month - ss - irs
    } else {
        month - irs
    }
    println("Net Month = ${total.round()}")
    val annual = total * 12
    println("Net Year x12 = ${annual.round()}")
    val annual2 = total * 11
    println("Net Year x11 = ${annual2.round()}")

    println("---------- Net Calculation With VAT ----------")
    val totalIva = if (withSS) {
        month - ss - irs - iva
    } else {
        month - irs - iva
    }
    println("Net Month (VAT) = ${totalIva.round()}")
    val annualVat = totalIva * 12
    println("Net Year x12 (VAT) = ${annualVat.round()}")
    val annual2Var = totalIva * 11
    println("Net Year x11 (VAT) = ${annual2Var.round()}")
    println()
    println()
    main()
}

val integerChars = listOf('1', '2', '3', '4', '5', '6', '7', '8', '9', '0')

fun isNumber(input: String): Boolean {
    var dotOccurred = 0
    return input.all { it in integerChars || it == '.' && dotOccurred++ < 1 }
}

fun Double.round(places: Int = 2): Double {
    require(places >= 0)
    var bd = BigDecimal.valueOf(this)
    bd = bd.setScale(places, RoundingMode.HALF_UP)
    return bd.toDouble()
}