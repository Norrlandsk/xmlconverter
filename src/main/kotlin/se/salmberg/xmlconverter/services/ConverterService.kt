package se.salmberg.xmlconverter.services

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import org.springframework.stereotype.Service
import se.salmberg.xmlconverter.entities.*

@Service
class ConverterService {

    fun convertToXML(linebasedInput: String): String {
        val peopleList: MutableList<Person> = mutableListOf()
        var currentPerson: Person? = null
        var currentFamilyMember: FamilyMember? = null

        val lines = linebasedInput.trim().split("\n")

        for (line in lines) {
            val lineSections = line.trim().split("|")

            when (lineSections[0]) {
                "P" -> {

                    if (currentFamilyMember != null) {
                        currentPerson?.family?.add(currentFamilyMember)
                        currentFamilyMember = null
                    }

                    if (currentPerson != null) {
                        peopleList.add(currentPerson)
                    }

                    currentPerson = Person(firstname = lineSections[1], lastname = lineSections[2])
                }

                "T" -> {
                    val phoneNumbers = lineSections.subList(1, lineSections.size)
                    if (currentFamilyMember != null) {
                        currentFamilyMember.phone?.addAll(phoneNumbers)
                    } else {
                        currentPerson?.phone?.addAll(phoneNumbers)
                    }
                }

                "A" -> {
                    if (currentFamilyMember != null) {
                        currentFamilyMember.address = Address(
                            street = lineSections.getOrNull(1),
                            city = lineSections.getOrNull(2),
                            postalnumber = lineSections.getOrNull(3)
                        )
                    } else {
                        currentPerson?.address = Address(
                            street = lineSections.getOrNull(1),
                            city = lineSections.getOrNull(2),
                            postalnumber = lineSections.getOrNull(3)
                        )
                    }
                }

                "F" -> {
                    val familyMemberSection = lineSections.subList(1, lineSections.size)
                    if (currentFamilyMember != null) {
                        currentPerson?.family?.add(currentFamilyMember)
                    }
                    currentFamilyMember =
                        FamilyMember(name = familyMemberSection[0], born = familyMemberSection[1])
                }
            }
        }
        if (currentFamilyMember != null) {
            currentPerson?.family?.add(currentFamilyMember)
        }
        currentPerson?.let { peopleList.add(it) }

        val xmlMapper = XmlMapper(
            JacksonXmlModule().apply { setDefaultUseWrapper(false) }
        ).apply {
            enable(SerializationFeature.INDENT_OUTPUT)
            enable(SerializationFeature.WRAP_ROOT_VALUE)
            setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
            setSerializationInclusion(JsonInclude.Include.NON_NULL)
        }
        val people = People(peopleList)
        return xmlMapper.writeValueAsString(people)
    }
}
