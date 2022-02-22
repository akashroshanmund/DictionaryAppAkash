package com.example.dictionaryappakash.dataSources

interface wordDataInterface {
    val word : String?
    val phoneticText : String?
    val phoneticAudioUri : String?
    val origin : String?
    val partOfSpeech: String?
    val definition : String?
    val synonyms : String?
}