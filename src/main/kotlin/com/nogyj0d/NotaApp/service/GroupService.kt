package com.nogyj0d.NotaApp.service

import com.nogyj0d.NotaApp.persistence.entities.Group
import com.nogyj0d.NotaApp.persistence.entities.Note
import com.nogyj0d.NotaApp.persistence.entities.User
import com.nogyj0d.NotaApp.persistence.repositories.GroupRepository
import com.nogyj0d.NotaApp.persistence.repositories.NoteRepository
import com.nogyj0d.NotaApp.persistence.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class GroupService @Autowired constructor(
        private val groupRepository: GroupRepository,
        private val userRepository: UserRepository,
        private val noteRepository: NoteRepository)
{
    fun getGroupsByUserId(userId: Long?): List<Group?>? {
        return groupRepository.findAllByUserIdOrderByPosition(userId)
    }

    fun createGroup(data: Group): Any {
        val user: User = userRepository.findById(data.user!!.id!!).orElse(null) ?: return "User not found"
        if (data.name.equals("Without group", ignoreCase = true)) {
            return "Invalid name"
        }
        var maxPos: Int? = groupRepository.findMaxPositionByUserId(user.id)
        if (maxPos == null) {
            maxPos = 0
        }
        data.user = user
        data.position = maxPos + 1
        return groupRepository.save(data)
    }

    fun updateGroup(data: Group): Any {
        val group: Group = groupRepository.findById(data.id!!).orElse(null) ?: return "Group not found"

        // Check if the group is reserved or the new position is set to 1
        if (group.position == 1) {
            return "Cannot modify reserved group"
        }
        if (data.position == 1) {
            return "Cannot set position 1 for a group"
        }

        // If position changes, update groups accordingly
        if (group.position != data.position) {
            val userGroups = groupRepository.findAllByUserIdOrderByPosition(data.user!!.id)
            val positionToUpdate = if (group.position!! < data.position!!) group.position else data.position

            // Update groups with positions greater or equal to positionToUpdate
            userGroups!!.filter { it!!.position!! >= positionToUpdate!! }.forEach {
                if (it!!.position == group.position) {
                    it.position = data.position
                } else {
                    it.position = it.position!! + 1
                }
                groupRepository.save(it)
            }
        }

        // Update group data
        group.name = data.name
        group.backcolor = data.backcolor
        group.forecolor = data.forecolor
        group.position = data.position

        return groupRepository.save(group)
    }


    fun deleteGroupById(groupId: Long): String {
        val group: Group = groupRepository.findById(groupId).orElse(null) ?: return "Group not found"
        if (group.position == 1 && group.name.equals("Without group", ignoreCase = true)) {
            return "Cannot delete reserved group"
        }

        // Move notes from this group to reserved group
        val user: User? = group.user
        val withoutGroup: Group? = groupRepository.findByUserIdAndPosition(user?.id, 1)
        val notes: MutableList<Note>? = group.notes
        if (!notes.isNullOrEmpty()) {
            for (note in notes) {
                note.group = withoutGroup
                withoutGroup!!.appendNote(note)
            }
        }
        groupRepository.delete(group)

        // Add position to new notes in reserved group
        val withoutGroupNotes: MutableList<Note>? = withoutGroup!!.notes
        if (!withoutGroupNotes.isNullOrEmpty()) {
            var pos = 1
            for (note in withoutGroupNotes) {
                note.position = pos
                noteRepository.save(note)
                pos++
            }
        }

        // Reorder groups position
        val groups = groupRepository.findAllByUserIdOrderByPosition(group.user!!.id)
        if (groups!!.isNotEmpty()) {
            var pos: Int = 1
            for (group2 in groups) {
                group2!!.position = pos
                groupRepository.save(group2)
                pos++
            }
        }
        return ""
    }
}
