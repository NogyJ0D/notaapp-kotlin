package com.nogyj0d.NotaApp.service

import com.nogyj0d.NotaApp.persistence.entities.Note
import com.nogyj0d.NotaApp.persistence.repositories.GroupRepository
import com.nogyj0d.NotaApp.persistence.repositories.NoteRepository
import com.nogyj0d.NotaApp.persistence.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class NoteService @Autowired constructor(private val noteRepository: NoteRepository, private val userRepository: UserRepository, private val groupRepository: GroupRepository) {
    fun allNotesByUserId(userId: Long): Any? {
        val user = userRepository.findById(userId).orElse(null) ?: return "User not found"
        return noteRepository.findAllByUserIdOrderByGroupPositionAscPositionAsc(userId)
    }

    fun allNotesByUserIdAndGroupId(userId: Long, groupId: Long): Any? {
        val user = userRepository.findById(userId).orElse(null) ?: return "User not found"
        val group = groupRepository.findById(groupId).orElse(null) ?: return "Group not found"
        return noteRepository.findAllByUserIdAndGroupIdOrderByPosition(userId, groupId)
    }

    fun createNote(data: Note): Any {
        val user = userRepository.findById(data.user!!.id!!).orElse(null) ?: return "User not found"
        val group = groupRepository.findById(data.group!!.id!!).orElse(null) ?: return "Group not found"
        var maxPos = noteRepository.findMaxPositionByUserIdAndGroupId(user.id, group.id)
        if (maxPos == null) {
            maxPos = 0
        }
        data.user = user
        data.position = maxPos + 1
        data.group = group
        data.backcolor = group.backcolor
        data.forecolor = group.forecolor
        return noteRepository.save(data)
    }

    fun updateNote(data: Note): Any {
        val note = noteRepository.findById(data.id!!).orElse(null) ?: return "Note not found"

        // If position changes, update notes accordingly
        if (note.position != data.position) {
            val userNotes = noteRepository.findAllByUserIdAndGroupIdOrderByPosition(note.user!!.id, data.group!!.id)
            val positionToUpdate = if (note.position!! < data.position!!) note.position else data.position

            // Update notes with positions greater or equal to positionToUpdate
            userNotes!!.filter { it?.position!! >= positionToUpdate!! }.forEach {
                if (it!!.position == note.position) {
                    it.position = data.position
                } else {
                    it.position = it.position!! + 1
                }
                noteRepository.save(it)
            }
        }

        // Update note data
        note.text = data.text
        note.backcolor = data.backcolor
        note.forecolor = data.forecolor
        note.group = data.group
        note.title = data.title
        note.position = data.position

        return noteRepository.save(note)
    }


    fun deleteNoteById(noteId: Long): Any {
        val note = noteRepository.findById(noteId).orElse(null) ?: return "Note not found"
        noteRepository.delete(note)

        // Reorder notes position
        val notes = noteRepository.findAllByUserIdAndGroupIdOrderByPosition(note.user!!.id, note.group!!.id)
        var pos = 1
        for (originalNote in notes!!) {
            originalNote!!.position = pos
            noteRepository.save(originalNote)
            pos++
        }
        return ""
    }
}
