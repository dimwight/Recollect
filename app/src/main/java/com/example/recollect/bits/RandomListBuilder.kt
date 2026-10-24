package com.example.recollect.bits

import kotlin.math.round
import kotlin.random.Random

class RandomListBuilder(
    val src: MutableList<Int> = (0..31).toMutableList(),
    val random: Random = Random(1)
) {
    private val debug=false
    val dst = mutableListOf<Int>()
    var toEnd: Int = 0
    var toHalf: Int = 0
    fun build(): List<Int> {
        val stopSize = src.size * 9 / 10
        while (dst.size < stopSize) {
            if (debug) println("R1: src = $src")
            toEnd = src.lastIndex
            toHalf = toEnd / 2

            addNext(0, toHalf)
            if (src.isEmpty())break
            addNext(toHalf, toEnd)
            if (src.isEmpty())break
        }
        if (debug) println("R1: dst = $dst")
        return dst.toList()
    }

    private fun addNext(fromTop: Int, fromTo: Int) {
        if (src.size<=1){
            if (src.isNotEmpty()) dst.add(src.removeAt(0))
            return
        }
        val nextSrc = src.subList(fromTop, fromTo)
        if (debug) println("R1: nextSrc = $nextSrc")
        if (nextSrc.isEmpty())return
        val lastAt = nextSrc.lastIndex
        val getAt = round(
            lastAt *
                    if (false) 1.0 else random.nextDouble()
        ).toInt()
        val add = nextSrc.removeAt(getAt)
        dst.add(add)
        if (debug) println("R1: dst = $dst")
    }
}