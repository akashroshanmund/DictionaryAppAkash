import com.example.dictionaryappakash.dataSources.WordData

data class Definition (
    val definition: String,
    val synonyms: ArrayList<String>,
    val antonyms: ArrayList<String>,
    val example: String
)
data class Meaning (

    val partOfSpeech: String = "" ,
    val definitions: ArrayList<Definition>,
    val synonyms: ArrayList<String>,
    val antonyms: ArrayList<String>? = null
)

data class Phonetic (
    val text: String ="",
    val audio: String = "",
    val sourceUrl: String = "",
    val license: License
)

data class License (
    val name: String = "",
    val url: String = ""
)


data class WordRoot(
    val word: String = "",
    val phonetic: String = "",
    val phonetics: ArrayList<Phonetic>,
    val origin: String  = "",
    val meanings: ArrayList<Meaning>,
    val license: License,
    val sourceUrls: ArrayList<String>
){
    fun getWordData(): WordData {
        var phoneticText = ""
        var phoneticAudioUri = ""
        var definition = ""
        var synonyms = ""

        if (!phonetics.isEmpty()) phoneticText = phonetics.get(0).text
        if (!phonetics.isEmpty()) phoneticAudioUri = phonetics.get(0).audio
        if (!meanings.isEmpty() && !meanings[0].definitions.isEmpty()) definition = meanings.get(0).definitions.get(0).definition
        if (!meanings.isEmpty()) synonyms = meanings.get(0).synonyms.joinToString("_")


        return WordData(
            word = word,
            phoneticText = phoneticText,
            phoneticAudioUri = phoneticAudioUri,
            origin = origin,
            definition = definition,
            synonyms = synonyms
        )
    }
}
