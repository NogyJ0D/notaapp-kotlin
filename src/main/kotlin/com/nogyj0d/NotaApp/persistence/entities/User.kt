package com.nogyj0d.NotaApp.persistence.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant
import java.util.*

@Entity
@Table(name = "users")
class User {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(nullable = false, unique = true, length = 30)
    var username: String? = null

    @Column(nullable = false, length = 60)
    var firstname: String? = null

    @Column(nullable = false, length = 60)
    var lastname: String? = null

    @Column(nullable = false, unique = false, length = 250)
    var email: String? = null

    @Column(nullable = false)
    var password: String? = null

    @Column(nullable = false)
    var birthdate: Date? = null

    // Relations
    @JsonIgnore
    @OneToMany(mappedBy = "user")
    var notes: List<Note>? = null

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    var groups: List<Group>? = null

    // Dates
    @CreationTimestamp
    @JsonIgnore
    @Column(name = "created_at", updatable = false)
    var createdAt: Instant? = null

    @UpdateTimestamp
    @JsonIgnore
    @Column(name = "updated_at")
    var updatedAt: Instant? = null
}
