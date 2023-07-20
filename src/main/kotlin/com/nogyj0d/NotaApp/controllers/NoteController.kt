package com.nogyj0d.NotaApp.controllers

import com.nogyj0d.NotaApp.model.ApiResponse
import com.nogyj0d.NotaApp.persistence.entities.Note
import com.nogyj0d.NotaApp.service.NoteService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("notes")
class NoteController @Autowired constructor(private val noteService: NoteService) {
    // ---- GET
    @GetMapping("user_id/{userId}")
    fun allNotesByUserId(@PathVariable("userId") userId: Long?): ResponseEntity<ApiResponse> {
        val notes = noteService.allNotesByUserId(userId!!)
        return if (notes!!.javaClass == String::class.java) {
            ResponseEntity(ApiResponse("error", notes), HttpStatus.BAD_REQUEST)
        } else ResponseEntity(ApiResponse("success", notes), HttpStatus.OK)
    }

    @GetMapping("user_id/{userId}/group_id/{groupId}")
    fun allNotesByUserIdAndGroupId(@PathVariable("userId") userId: Long?, @PathVariable("groupId") groupId: Long?): ResponseEntity<ApiResponse> {
        val notes = noteService.allNotesByUserIdAndGroupId(userId!!, groupId!!)
        return if (notes!!.javaClass == String::class.java) {
            ResponseEntity(ApiResponse("error", notes), HttpStatus.BAD_REQUEST)
        } else ResponseEntity(ApiResponse("success", notes), HttpStatus.OK)
    }

    // ---- POST
    @PostMapping("")
    fun createNote(@RequestBody data: Note?): ResponseEntity<ApiResponse> {
        val note = noteService.createNote(data!!)
        return if (note.javaClass != Note::class.java) {
            ResponseEntity(ApiResponse("error", note), HttpStatus.BAD_REQUEST)
        } else ResponseEntity(ApiResponse("success", note), HttpStatus.CREATED)
    }

    // ---- PUT
    @PutMapping("/{noteId}")
    fun updateNote(@PathVariable noteId: Long?, @RequestBody data: Note): ResponseEntity<ApiResponse> {
        data.id = noteId
        val note = noteService.updateNote(data)
        return if (note.javaClass != Note::class.java) {
            ResponseEntity(ApiResponse("error", note), HttpStatus.BAD_REQUEST)
        } else ResponseEntity(ApiResponse("success", note), HttpStatus.OK)
    }

    // ---- DELETE
    @DeleteMapping("{noteId}")
    fun deleteNoteById(@PathVariable noteId: Long?): ResponseEntity<ApiResponse> {
        val res = noteService.deleteNoteById(noteId!!)
        return if (res !== "") {
            ResponseEntity(ApiResponse("error", res), HttpStatus.BAD_REQUEST)
        } else ResponseEntity(ApiResponse("success", res), HttpStatus.OK)
    }
}
