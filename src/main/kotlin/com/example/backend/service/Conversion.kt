package com.example.backend.service

class Conversion {

    companion object {

        fun asLong(value: Any?): Long {
            return (value as Number).toLong()
        }

        fun asInt(value: Any?): Int {
            return (value as Number).toInt()
        }
    }

}