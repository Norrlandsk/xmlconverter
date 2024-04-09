package se.salmberg.xmlconverter.utils

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider

class PhoneNumberListSerializer : JsonSerializer<MutableList<String>>() {
    override fun serialize(numbers: MutableList<String>?, gen: JsonGenerator, provider: SerializerProvider?) {

//        if (numbers.isNullOrEmpty()){
////            gen.writeStartObject()
////            gen.writeStringField("","")
////            gen.writeNullField("")
////            gen.writeEndObject()
//        }
        gen.writeStartObject()

        numbers?.forEach {
            if (it.startsWith("07")) {
                gen.writeStringField("mobile", it)
            } else {
                gen.writeStringField("phone", it)
            }
        }
        gen.writeEndObject()
    }
}
