package com.nogyj0d.NotaApp.controllers

import com.nogyj0d.NotaApp.model.ApiResponse
import com.nogyj0d.NotaApp.persistence.entities.User
import com.nogyj0d.NotaApp.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("users")
class UserController @Autowired constructor(private val userService: UserService) {

    // ---- GET
    @GetMapping("")
    fun allUsers(): ResponseEntity<ApiResponse> {
        val users = userService.allUsers
        return if (users.isEmpty()) {
            ResponseEntity(ApiResponse("empty", users), HttpStatus.OK)
        } else ResponseEntity(ApiResponse("success", users), HttpStatus.OK)
    }

    // ---- POST
    @PostMapping("")
    fun createUser(@RequestBody data: User?): ResponseEntity<ApiResponse> {
        val user = userService.createUser(data!!)
        return if (user.javaClass != User::class.java) {
            ResponseEntity(ApiResponse("error", user), HttpStatus.BAD_REQUEST)
        } else ResponseEntity(ApiResponse("success", user), HttpStatus.CREATED)
    }

    // ---- PUT

    // ---- DELETE
}
