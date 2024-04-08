package se.salmberg.xmlconverter.services

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import org.springframework.stereotype.Service
import se.salmberg.xmlconverter.entities.*

@Service
class ConverterService {
    //1. Ta in input
    //2. Dela up i P-objekt
    //3. Dela upp varje P-objekt i T A F
    //4. Bygg ihop till xml med templates

//    P|firstname|lastname
//    T|mobile|telephone
//    A|street|city|postalnumber
//    F|name|year
//    P can be followed by T, A and F
//    F can be followed by T and A

//    P|Carl Gustaf|Bernadotte
//    T|0768-101801|08-101801
//    A|Drottningholms slott|Stockholm|10001
//    F|Victoria|1977
//    A|Haga Slott|Stockholm|10002
//    F|Carl Philip|1979
//    T|0768-101802|08-101802
//    P|Barack|Obama
//    A|1600 Pennsylvania Avenue|Washington, D.C


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
            disable(SerializationFeature.WRAP_ROOT_VALUE)
        }
        val people = People(peopleList)
        val xml = xmlMapper.writeValueAsString(people)
        xml
        return xml
    }
}
