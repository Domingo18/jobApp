package com.example.project_2_cmp431.util

val Any.TAG: String

    get() {

        return if (!javaClass.isAnonymousClass){

            val name = javaClass.simpleName



            if (name.length <= 23) name else name.substring(0,23)

        } else {

            val name = javaClass.name

            if (name.length <= 23) name else name.substring(name.length - 23, name.length)

        }

    }



fun String.capitalizeWords(delimiter: String = " ") =

    split(delimiter).joinToString(delimiter) { word ->

        val lowercaseWord = word.lowercase()

        lowercaseWord.replaceFirstChar (Char:: titlecaseChar)

    }