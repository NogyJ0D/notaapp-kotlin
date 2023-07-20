package com.nogyj0d.NotaApp.persistence.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant

@Entity
@Table(name = "groups")
class Group {
    @Id
    @Column(name = "group_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(nullable = false, length = 30)
    var name: String = ""

    @Column(length = 6)
    var backcolor = "ffffff"

    @Column(length = 6)
    var forecolor = "000000"

    @Column
    var position: Int? = null

    // Relations
    @JsonIgnore
    @OneToMany(mappedBy = "group")
    var notes: MutableList<Note>? = null

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    var user: User? = null

    // Dates
    @CreationTimestamp
    @JsonIgnore
    @Column(name = "created_at", updatable = false)
    var createdAt: Instant? = null

    @UpdateTimestamp
    @JsonIgnore
    @Column(name = "updated_at")
    var updatedAt: Instant? = null

    fun appendNote(note: Note) {
        notes!!.add(note)
    }
}
