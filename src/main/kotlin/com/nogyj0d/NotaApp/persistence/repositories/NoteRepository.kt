package com.nogyj0d.NotaApp.persistence.repositories

import com.nogyj0d.NotaApp.persistence.entities.Note
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface NoteRepository : JpaRepository<Note?, Long?> {
    fun findAllByUserIdOrderByGroupPositionAscPositionAsc(userId: Long?): MutableList<Note?>?

    fun findAllByUserIdAndGroupIdOrderByPosition(userId: Long?, groupId: Long?): MutableList<Note?>?

    @Query("SELECT MAX(n.position) FROM Note n WHERE n.user.id = :userId AND n.group.id = :groupId")

    fun findMaxPositionByUserIdAndGroupId(@Param("userId") userId: Long?, @Param("groupId") groupId: Long?): Int?

    fun findByUserIdAndGroupIdAndPosition(userId: Long?, groupId: Long?, position: Int?): Note?
}