package com.nogyj0d.NotaApp.persistence.repositories

import com.nogyj0d.NotaApp.persistence.entities.Group
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface GroupRepository : JpaRepository<Group?, Long?> {
    fun findAllByUserId(userID: Long?): MutableList<Group?>?

    fun findAllByUserIdOrderByPosition(userId: Long?): MutableList<Group?>?

    @Query("SELECT MAX(g.position) FROM Group g WHERE g.user.id = :userId")
    fun findMaxPositionByUserId(@Param("userId") userId: Long?): Int?

    @Query("SELECT g FROM Group g WHERE g.user.id = :userId AND g.name = 'Without group'")
    fun findWithoutGroupByUserId(@Param("userId") userId: Long?): Group?

    fun findByUserIdAndPosition(userId: Long?, position: Int?): Group?
}