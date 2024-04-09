package se.salmberg.xmlconverter.services

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import org.springframework.stereotype.Service
import se.salmberg.xmlconverter.entities.*

@Service
class ConverterService {

    /*
    * Splits input string on \n, iterates line by line and first splits each line into sections on |,
    * determines data type (P,T,A or F) and decides in switch where to store data, either in a Person object,
    * or in a FamilyMember object. If data type is P, possible preexisting Person object is finished and stored in
    * peopleList and currentPerson placeholder is reinitialized with new Person object. If data type is F, possibly
    * preexisting FamilyMember object is finished and stored in currentPerson object's family list, and
    * currentFamilyMember placeholder is reinitialized with new FamilyMember object. When for loop is over, existing
    * FamilyMember/Person objects are stored, and peopleList is returned.
    * */
    fun extractObjectsFromInput(linebasedInput: String?): List<Person> {

        val peopleList: MutableList<Person> = mutableListOf()
        var currentPerson: Person? = null
        var currentFamilyMember: FamilyMember? = null

        val lines = linebasedInput?.trim()?.split("\n") ?: emptyList()

        for (line in lines) {

            val lineSections = line.trim().split("|")

            when (lineSections[0].uppercase()) {
                "P" -> {
                    if (currentFamilyMember != null) {
                        currentPerson?.family?.add(currentFamilyMember)
                        currentFamilyMember = null
                    }

                    if (currentPerson != null) {
                        peopleList.add(currentPerson)
                    }

                    currentPerson = Person(
                        firstname = lineSections.getOrNull(1)?.takeIf { it.isNotBlank() },
                        lastname = lineSections.getOrNull(2)?.takeIf { it.isNotBlank() }
                    )
                }

                "T" -> {
                    val phoneNumbers = lineSections.subList(1, lineSections.size).filter { it.isNotBlank() }

                    if (currentFamilyMember != null) {
                        currentFamilyMember.phone?.addAll(phoneNumbers)
                    } else {
                        currentPerson?.phone?.addAll(phoneNumbers)
                    }
                }

                "A" -> {
                    val address = Address(
                        street = lineSections.getOrNull(1)?.takeIf { it.isNotBlank() },
                        city = lineSections.getOrNull(2)?.takeIf { it.isNotBlank() },
                        postalnumber = lineSections.getOrNull(3)?.takeIf { it.isNotBlank() }
                    )

                    when {
                        currentFamilyMember != null -> currentFamilyMember.address = address
                        else -> currentPerson?.address = address
                    }
                }

                "F" -> {
                    val familyMemberSection = lineSections.subList(1, lineSections.size)
                    val name = familyMemberSection.getOrNull(0)?.takeIf { it.isNotBlank() }
                    val born = familyMemberSection.getOrNull(1)?.takeIf { it.isNotBlank() }

                    currentFamilyMember?.let { currentPerson?.family?.add(it) }
                    currentFamilyMember = FamilyMember(name = name, born = born)
                }
            }
        }

        if (currentFamilyMember != null) {
            currentPerson?.family?.add(currentFamilyMember)
        }
        currentPerson?.let { peopleList.add(it) }

        return peopleList
    }

    /*
    * XmlMapper is initialized and configured. List of Person objects are wrapped in a People object and sent
    * to Xml writing function.*/
    fun convertObjectListToXml(peopleList: List<Person>): String {
        val mapper = XmlMapper(
            JacksonXmlModule().apply { setDefaultUseWrapper(false) }
        ).apply {
            enable(SerializationFeature.INDENT_OUTPUT)
            enable(SerializationFeature.WRAP_ROOT_VALUE)
            setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
            setSerializationInclusion(JsonInclude.Include.NON_NULL)
        }
        return mapper.writeValueAsString(People(peopleList))
    }
}
