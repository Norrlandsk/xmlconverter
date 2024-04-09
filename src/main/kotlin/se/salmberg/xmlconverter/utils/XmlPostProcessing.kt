package se.salmberg.xmlconverter.utils

import org.springframework.stereotype.Component

@Component
class XmlPostProcessing {

    fun createMobileXmlTags(xml: String): String {
        return Regex("<phone>07(.*?)</phone>").replace(xml) { result ->
            "<mobile>07${result.groupValues[1]}</mobile>"
        }
    }

}