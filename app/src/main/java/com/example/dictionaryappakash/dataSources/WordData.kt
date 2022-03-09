package com.example.dictionaryappakash.dataSources

/* data class to use through the program and to fetch data for UI */
data class WordData (
    override val word : String? = "",
    override val phoneticText : String? = "",
    override val phoneticAudioUri : String?= "",
    override val origin : String? = "",
    override val partOfSpeech: String? = "",
    override val definition : String? = "",
    override val synonyms : String? = ""
) : WordDataInterface
{
    companion object {
        fun convertWordDataToWordEntity(data: WordData): wordsEntity =
            wordsEntity(
                0,
                data.word ?: "",
                data.phoneticText ?: "",
                data.phoneticAudioUri ?: "",
                data.origin ?: "",
                data.partOfSpeech ?: "",
                data.definition ?: "",
                data.synonyms ?: ""
            )
    }
}
