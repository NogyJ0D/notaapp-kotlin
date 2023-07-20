package com.nogyj0d.NotaApp

import io.github.cdimascio.dotenv.Dotenv
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class NotaAppApplication

fun main(args: Array<String>) {
	val dotenv: Dotenv = Dotenv.load()

	System.setProperty("DB_URL", dotenv["DB_URL"])
	System.setProperty("DB_USERNAME", dotenv["DB_USERNAME"])
	System.setProperty("DB_PASSWORD", dotenv["DB_PASSWORD"])
	System.setProperty("DB_NAME", dotenv["DB_NAME"])

	runApplication<NotaAppApplication>(*args)

	println("---- RUNNING ----")
}
