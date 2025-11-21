package com.foobarlogistics.frontend_api.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

data class Greeting(val message: String)

@RestController
class HelloWorldController {

    @GetMapping("/hello")
    fun hello(): Greeting = Greeting("Hello World!")

    @GetMapping("/hello/{name}")
    fun hello(@PathVariable name: String): Greeting = Greeting("Hello $name!. You are the best!")

}
