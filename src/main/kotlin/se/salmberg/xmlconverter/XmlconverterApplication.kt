package se.salmberg.xmlconverter

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class XmlconverterApplication

fun main(args: Array<String>) {
	runApplication<XmlconverterApplication>(*args)
}
