@file:JvmName("FormatUtils")

package com.iceteaviet.fastfoodfinder.utils

import android.content.Intent
import android.net.Uri
import android.text.SpannableStringBuilder

import java.text.DecimalFormat

/**
 * Created by tom on 7/10/18.
 */

private val distanceFormat = DecimalFormat("##.## Km")
private val oneDecimalFormat = DecimalFormat("#.#")
private val twoDecimalFormat = DecimalFormat("#.##")
private val threeDecimalFormat = DecimalFormat("#.###")
private val fourDecimalFormat = DecimalFormat("#.####")

/**
 * Trim all whitespace
 */
fun trimWhitespace(source: CharSequence?): CharSequence {
    if (source == null)
        return ""

    val builder = SpannableStringBuilder(source)
    var c: Char

    for (i in 0 until source.length) {
        c = source[i]
        if (Character.isWhitespace(c)) {
            try {
                if (i < source.length - 1 && Character.isWhitespace(source[i + 1]))
                //Ignore next char
                //Because it is a whitespace again
                    builder.delete(i, i + 1)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }

        }
    }

    if (Character.isWhitespace(builder[builder.length - 1]))
        builder.delete(builder.length - 1, builder.length)

    return builder.subSequence(0, builder.length)
}

/**
 * Trim instruction
 */
fun getTrimmedShortInstruction(source: CharSequence?): CharSequence {

    if (source == null)
        return ""

    var i = 0
    var newLen = 0

    // loop back to the first non-whitespace character
    while (i < source.length - 1) {
        if (Character.isWhitespace(source[i]) && Character.isWhitespace(source[i + 1])) {
            newLen = i
            break
        }
        i++
    }

    if (newLen <= 1)
        newLen = source.length

    return source.subSequence(0, newLen)
}

/**
 * Get phone call Intent
 */
fun getCallIntent(tel: String): Intent {
    val normalizedTel = tel.replace("\\s".toRegex(), "")
    val callIntent = Intent(Intent.ACTION_CALL)
    callIntent.data = Uri.parse("tel:08$normalizedTel")
    return callIntent
}

/**
 * Format distance with Km unit
 */
fun formatDistance(distance: Double): String {
    return distanceFormat.format(distance)
}

/**
 * Format decimal number to specific decimal plates
 */
fun formatDecimal(decimal: Double, numbOfDecimalPlates: Int): String {
    return when (numbOfDecimalPlates) {
        1 -> oneDecimalFormat.format(decimal)

        2 -> twoDecimalFormat.format(decimal)

        3 -> threeDecimalFormat.format(decimal)

        4 -> fourDecimalFormat.format(decimal)

        else -> decimal.toString()
    }
}