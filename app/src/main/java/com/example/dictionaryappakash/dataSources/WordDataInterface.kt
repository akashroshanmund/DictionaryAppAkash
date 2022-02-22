package com.example.dictionaryappakash.dataSources


/* should be implemented by all the data class */
interface wordDataInterface {
    val word : String?
    val phoneticText : String?
    val phoneticAudioUri : String?
    val origin : String?
    val partOfSpeech: String?
    val definition : String?
    val synonyms : String?
}