package com.nogyj0d.NotaApp.controllers

import com.nogyj0d.NotaApp.model.ApiResponse
import com.nogyj0d.NotaApp.persistence.entities.Group
import com.nogyj0d.NotaApp.service.GroupService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("groups")
class GroupController @Autowired constructor(private val groupService: GroupService) {
    // ---- GET
    @GetMapping("user_id/{userId}")
    fun getAllGroupsByUserId(@PathVariable("userId") userId: Long?): ResponseEntity<ApiResponse> {
        val groups = groupService.getGroupsByUserId(userId)
        return if (groups!!.isEmpty()) {
            ResponseEntity(ApiResponse("empty", groups), HttpStatus.OK)
        } else ResponseEntity(ApiResponse("success", groups), HttpStatus.OK)
    }

    // ---- POST
    @PostMapping("")
    fun createGroup(@RequestBody data: Group?): ResponseEntity<ApiResponse> {
        val group = groupService.createGroup(data!!)
        return if (group.javaClass != Group::class.java) {
            ResponseEntity(ApiResponse("error", group), HttpStatus.BAD_REQUEST)
        } else ResponseEntity(ApiResponse("success", group), HttpStatus.CREATED)
    }

    // ---- PUT
    @PutMapping("/{groupId}")
    fun updateGroup(@PathVariable groupId: Long?, @RequestBody data: Group): ResponseEntity<ApiResponse> {
        data.id = groupId
        val group = groupService.updateGroup(data)
        return if (group.javaClass != Group::class.java) {
            ResponseEntity(ApiResponse("error", group), HttpStatus.BAD_REQUEST)
        } else ResponseEntity(ApiResponse("success", group), HttpStatus.OK)
    }

    // ---- DELETE
    @DeleteMapping("{groupId}")
    fun deleteGroupById(@PathVariable groupId: Long?): ResponseEntity<ApiResponse> {
        val res: Any = groupService.deleteGroupById(groupId!!)
        return if (res !== "") {
            ResponseEntity(ApiResponse("error", res), HttpStatus.BAD_REQUEST)
        } else ResponseEntity(ApiResponse("success", res), HttpStatus.OK)
    }
}
