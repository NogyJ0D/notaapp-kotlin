package com.nogyj0d.NotaApp.service

import com.nogyj0d.NotaApp.persistence.entities.Group
import com.nogyj0d.NotaApp.persistence.entities.User
import com.nogyj0d.NotaApp.persistence.repositories.GroupRepository
import com.nogyj0d.NotaApp.persistence.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService @Autowired constructor(private val userRepository: UserRepository, private val groupRepository: GroupRepository) {

    val allUsers: List<User?>
        get() = userRepository.findAll()

    fun createUser(data: User): Any {
        if (userRepository.findFirstByEmail(data.email) != null) {
            return "That email is already used."
        }
        if (userRepository.findFirstByUsername(data.username) != null) {
            return "That username is already used."
        }
        val user = userRepository.save(data)
        val defaultGroup = Group()
        defaultGroup.name = "Without group"
        defaultGroup.user = user
        defaultGroup.position = 1
        groupRepository.save(defaultGroup)
        return user
    }
}
