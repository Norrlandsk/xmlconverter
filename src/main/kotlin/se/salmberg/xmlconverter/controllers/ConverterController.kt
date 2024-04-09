package se.salmberg.xmlconverter.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import se.salmberg.xmlconverter.services.ConverterService
import se.salmberg.xmlconverter.utils.XmlPostProcessing

@RestController
class ConverterController (
    @Autowired val converterService: ConverterService,
    @Autowired val xmlPostProcessing: XmlPostProcessing
){
    @PostMapping("/submit")
    fun submit(
        @RequestParam lineBasedInput: String?
    ): String {
        val objectList = converterService.extractObjectsFromInput(lineBasedInput)
        val xml = converterService.convertObjectListToXml(objectList)
        return xmlPostProcessing.createMobileXmlTags(xml)
    }
}