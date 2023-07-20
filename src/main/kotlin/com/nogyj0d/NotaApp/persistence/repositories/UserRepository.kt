package com.nogyj0d.NotaApp.persistence.repositories

import com.nogyj0d.NotaApp.persistence.entities.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User?, Long?> {
    fun findFirstByEmail(email: String?): User?

    fun findFirstByUsername(username: String?): User?
}
