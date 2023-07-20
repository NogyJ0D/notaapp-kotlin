package com.nogyj0d.NotaApp.persistence.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant

@Entity
@Table(name = "notes")
class Note {
    @Id
    @Column(name = "note_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(length = 60)
    var title = "Note title"

    @Column(nullable = false)
    var text: String? = null

    @Column(length = 6)
    var backcolor: String? = null

    @Column(length = 6)
    var forecolor: String? = null

    @Column
    var position: Int? = null

    // Relations
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    var group: Group? = null

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

    init {
        if (group != null) {
            backcolor = group!!.backcolor
            forecolor = group!!.forecolor
            position = group!!.notes?.size?.plus(1)
        }
    }
}
