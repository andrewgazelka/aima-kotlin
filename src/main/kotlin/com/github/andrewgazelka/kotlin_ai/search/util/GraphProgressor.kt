package com.github.andrewgazelka.kotlin_ai.search.util

//class GraphProgressor<K,V>(private val graph: Graph<K, V>){
//    fun progress(start: K, scanner: Scanner){
//        println("Starting with $start");
//        var on: K? = start
//        while(on != null){
//            val connections = graph.connections(on).toList()
//            println("CONNECTIONS")
//            connections.forEachIndexed { index, to ->  println("$index ::: $to") }
//            println("--------------")
//            print("choice: ")
//            val index = scanner.nextInt()
//            val to = connections.getOrNull(index)
//            on = to?.end
//        }
//    }
//}
