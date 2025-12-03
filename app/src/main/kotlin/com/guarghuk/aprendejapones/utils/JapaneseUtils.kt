/**
 * Utility class for Japanese language processing.
 * Contains functions for string manipulation, character conversions, and more.
 *
 * @constructor Creates an instance of JapaneseUtils.
 */
class JapaneseUtils {
    /**
     * Converts Hiragana characters to Katakana.
     *
     * @param hiragana the string containing Hiragana characters.
     * @return the converted string in Katakana.
     */
    fun convertHiraganaToKatakana(hiragana: String): String {
        return hiragana.map { c ->
            if (c in 'あ'..'ん') {
                (c.toInt() + 96).toChar() // Move from Hiragana to Katakana in Unicode
            } else {
                c
            }
        }.joinToString("")
    }

    /**
     * Checks if a string is in Hiragana script.
     *
     * @param input the string to check.
     * @return true if the string is in Hiragana, false otherwise.
     */
    fun isHiragana(input: String): Boolean {
        return input.all { it in 'あ'..'ん' }
    }

    /**
     * Checks if a string is in Katakana script.
     *
     * @param input the string to check.
     * @return true if the string is in Katakana, false otherwise.
     */
    fun isKatakana(input: String): Boolean {
        return input.all { it in 'ア'..'ン' }
    }

    /**
     * Converts a Kanji string to Romaji. This is a placeholder function.
     *
     * @param kanji the Kanji string to convert.
     * @return the Romaji conversion (placeholder - actual implementation needed).
     */
    fun convertKanjiToRomaji(kanji: String): String {
        // Placeholder implementation
        return "Romaji for \"$kanji\""
    }
}