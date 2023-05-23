package ru.zaycev.pocketchief.data

import org.intellij.lang.annotations.RegExp

/**
 * VerificationRequirement - класс, описывающий требование (условие) для текстовой строки.
 * @param text текст условия.
 * @param condition регулярное выражение, задающее требование (условие).
 * @param state логическое значение, обозначающее истино требование (условие) или ложно.
 */
class VerificationRequirement(
    val text: String,
    @RegExp val condition: String,
    var state: Boolean = false
) {
    /** Метод служит для проверки, выполняет ли переданная строка заданое условие. */
    fun checkCondition(stringToCheck: String) {
        this.state = Regex(this@VerificationRequirement.condition).containsMatchIn(stringToCheck)
    }
}