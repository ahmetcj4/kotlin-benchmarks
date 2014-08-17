package org.jetbrains

import org.openjdk.jmh.annotations.*
import java.util.concurrent.*
import org.openjdk.jmh.infra.Blackhole
import java.util.ArrayList

BenchmarkMode(Mode.AverageTime)
OutputTimeUnit(TimeUnit.NANOSECONDS)
open class IntStreamBenchmark : SizedBenchmark() {
    private var _data: Stream<Int>? = null
    val data: Stream<Int>
        get() = _data!!

    Setup fun setup() {
        _data = intValues(size).stream()
    }

    Benchmark fun copy(): List<Int> {
        return data.toList()
    }

    Benchmark fun copyManual(): List<Int> {
        val list = ArrayList<Int>()
        for (item in data) {
            list.add(item)
        }
        return list
    }

    Benchmark fun filterAndCount(): Int {
        return data.filter { it and 1 == 0 }.count()
    }

    Benchmark fun filterAndCountWithValue(): Int {
        val value = data.filter { it and 1 == 0 }.count()
        return value
    }

    Benchmark fun filterAndMap(bh: Blackhole) {
        for (item in data.filter { it and 1 == 0 }.map { it * 10 })
            bh.consume(item)
    }

    Benchmark fun filterAndMapManual(): ArrayList<Int> {
        val list = ArrayList<Int>()
        for (item in data) {
            if (item and 1 == 0) {
                val value = item * 10
                list.add(value)
            }
        }
        return list
    }

    Benchmark fun filter(bh: Blackhole) {
        for (item in data.filter { it and 1 == 0 })
            bh.consume(item)
    }

    Benchmark fun filterManual(): List<Int> {
        val list = ArrayList<Int>()
        for (item in data) {
            if (item and 1 == 0)
                list.add(item)
        }
        return list
    }

    Benchmark fun countFilteredManual(): Int {
        var count = 0
        for (it in data) {
            if (it and 1 == 0)
                count++
        }
        return count
    }

    Benchmark fun countFiltered(): Int {
        return data.count { it and 1 == 0 }
    }

    Benchmark fun countFilteredWithValue(): Int {
        val value = data.count { it and 1 == 0 }
        return value
    }

    Benchmark fun countFilteredLocal(): Int {
        return data.cnt { it and 1 == 0 }
    }

    Benchmark fun countFilteredLocalWithValue(): Int {
        val local = data.cnt { it and 1 == 0 }
        return local
    }

    Benchmark fun reduce(): Int {
        return data.fold(0) {(acc, it) -> if (it and 1 == 0) acc + 1 else acc }
    }
}