package com.github.andrewgazelka.kotlin_ai.util

import java.util.*

fun <K,V> cachedFunc(block: (K) -> V): (K) -> V{
    val map = WeakHashMap<K,V>()
    return {input ->
        map.getOrPut(input){
            block(input)
        }
    }
}
