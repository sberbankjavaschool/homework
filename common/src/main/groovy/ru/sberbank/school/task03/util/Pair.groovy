package ru.sberbank.school.task03.util

class Pair<K, V> {
    K key
    V value

    Pair(K key, V value) {
        this.key = key
        this.value = value
    }

    K getKey() {
        return key
    }

    V getValue() {
        return value
    }
}
