package se.salmberg.xmlconverter.utils

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider

/*
* NOT WORKING PROPERLY*/
class PhoneNumberListSerializer : JsonSerializer<MutableList<String>>() {

    override fun serialize(numbers: MutableList<String>?, gen: JsonGenerator, provider: SerializerProvider) {

//            if (numbers.isNullOrEmpty()){
//                return
//            }

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
