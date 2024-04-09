package se.salmberg.xmlconverter.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import se.salmberg.xmlconverter.services.ConverterService

@RestController
class ConverterController (
    @Autowired val converterService: ConverterService
){
    @PostMapping("/submit")
    fun submit(
        @RequestParam lineBasedInput: String
    ): String {
        val objectList = converterService.extractObjectsFromInput(lineBasedInput)
        return converterService.convertObjectListToXml(objectList)
    }
}