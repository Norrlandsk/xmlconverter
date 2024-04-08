package se.salmberg.xmlconverter.entities

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import se.salmberg.xmlconverter.utils.PhoneNumberListSerializer

@JacksonXmlRootElement(localName = "people")
data class People(
    @field:JacksonXmlElementWrapper(useWrapping = false)
    @field:JacksonXmlProperty(localName = "person")
    var peopleList: List<Person> = mutableListOf()
)

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class Person(
    var firstname: String? = null,
    var lastname: String? = null,
    @field:JsonSerialize(using = PhoneNumberListSerializer::class)
    var phone: MutableList<String>? = mutableListOf(),
    var address: Address? = null,
    var family: MutableList<FamilyMember> = mutableListOf()
)

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class Address(
    var street: String? = null,
    var city: String? = null,
    var postalnumber: String? = null
)

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class FamilyMember(
    var name: String? = null,
    var born: String? = null,
    @field:JsonSerialize(using = PhoneNumberListSerializer::class)
    var phone: MutableList<String>? = mutableListOf(),
    var address: Address? = null
)

