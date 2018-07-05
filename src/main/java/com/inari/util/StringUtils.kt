package com.inari.util

import java.util.*


/** Defines utility methods for String manipulation
 *
 * @author andreas hefti
 */
object StringUtils {

    const val EMPTY_STRING = ""
    const val VALUE_SEPARATOR = ','
    const val VALUE_SEPARATOR_STRING = ","

    const val KEY_VALUE_SEPARATOR = '='
    const val KEY_VALUE_SEPARATOR_STRING = "="

    const val LIST_VALUE_SEPARATOR = '|'
    const val LIST_VALUE_SEPARATOR_STRING = "|"


    /** Indicates if the String s is null or an empty String
     * @param s the String
     * @return true if the String s is null or an empty String
     */
    fun isEmpty(s: String?): Boolean =
        s == null || s.isEmpty()

    /** Indicates if the String s is null, an empty String or a blank String
     * @param s the String
     * @return true if the String s is null, an empty String or a blank String
     */
    fun isBlank(s: String?): Boolean =
        s == null || s.trim().isEmpty()

    fun split(string: String, separator: String): Collection<String> {
        val list = ArrayList<String>()
        val st = StringTokenizer(string, separator)
        while (st.hasMoreTokens())
            list.add(st.nextToken())
        return list
    }

    fun splitToArray(string: String, separator: String): Array<String> {
        val st = StringTokenizer(string, separator)

        val tokens: Array<String> =Array (st.countTokens()) { EMPTY_STRING }
        var i = 0
        while (st.hasMoreTokens()) {
            tokens[i] = st.nextToken()
            i++
        }
        return tokens
    }

    fun splitToMap(string: String, separator: String?, keyValueSeparator: String?): Map<String, String>? {
        if (StringUtils.isBlank(string) || separator == null || keyValueSeparator == null)
            return null

        val map = LinkedHashMap<String, String>()
        val stEntry = StringTokenizer(string, separator)
        while (stEntry.hasMoreTokens()) {
            val entry = stEntry.nextToken()
            val index = entry.indexOf(keyValueSeparator)
            if (index > 0)
                map[entry.substring(0, index)] = entry.substring(index + 1, entry.length)
        }
        return map
    }

    fun fillPrepending(string: String?, fill: Char, length: Int): String? {
        if (string == null)
            return null
        if (string.length >= length)
            return string

        val sb = StringBuilder(string)
        while (sb.length < length)
            sb.insert(0, fill)
        return sb.toString()
    }

    fun fillAppending(string: String?, fill: Char, length: Int): String? {
        if (string == null)
            return null
        if (string.length >= length)
            return string

        val sb = StringBuilder(string)
        while (sb.length < length)
            sb.append(fill)
        return sb.toString()
    }

    fun escapeSeparatorKeys(value: String): String {
        // TODO
        return value
    }

    fun array2DToString(grid: Array<IntArray>?): String? {
        if (grid == null)
            return null

        val sb = StringBuilder()
        sb.append("[")
        for (i in grid.indices)
            sb.append(Arrays.toString(grid[i]))
        sb.append("]")
        return sb.toString()
    }

    fun join(collection: Collection<*>?, separatorString: String): String? {
        if (collection == null)
            return null
        val result = StringBuilder()

        val iterator = collection.iterator()
        while (iterator.hasNext()) {
            val next = iterator.next()
            val strValue = next?.toString() ?: ""
            // TODO escape separator character

            result.append(strValue)
            if (iterator.hasNext()) {
                result.append(separatorString)
            }
        }

        return result.toString()
    }

    fun bitSetFromString(pixelsString: String): BitSet {
        val pixels = BitSet(pixelsString.length)
        var index = 0
        while (index < pixelsString.length) {
            val c = pixelsString[index]
            if (c == '\n') {
                continue
            }

            pixels.set(index, c != '0')
            index++
        }
        return pixels
    }

    fun bitSetToString(bitSet: BitSet, width: Int, height: Int): String {
        val builder = StringBuilder()
        var y = 0
        while (y < height) {
            var x = 0
            while (x < width) {
                builder.append(if (bitSet.get(y * width + x)) 1 else 0)
                x++
            }
            if (y < height - 1)
                builder.append("\n")
            y++
        }
        return builder.toString()
    }
}
