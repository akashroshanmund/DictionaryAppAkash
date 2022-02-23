package com.example.dictionaryappakash.dataSources


import androidx.room.Entity
import androidx.room.PrimaryKey

/*
* entity structure to store in the database
* */

@Entity
data class wordsEntity @JvmOverloads constructor(
    @PrimaryKey(autoGenerate = true) val Id: Int = 0,
    override var word: String ="",
    override var phoneticText: String = "",
    override var phoneticAudioUri: String = "",
    override var origin: String = "",
    override var partOfSpeech: String = "",
    override var definition: String = "",
    override var synonyms: String = "") : WordDataInterface
{

}