package br.com.DataPilots.Fileflow.controllers;

import br.com.DataPilots.Fileflow.entities.Group;
import br.com.DataPilots.Fileflow.entities.User;
import br.com.DataPilots.Fileflow.services.GroupService;
import br.com.DataPilots.Fileflow.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/group")
public class GroupController {
    @Autowired
    private GroupService groupService;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<Group> createGroup(@RequestBody Group group) {
        User owner = userService.find(group.getOwner().getId());
        return ResponseEntity.ok(groupService.create(group.getName(), owner));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Group> getGroup(@PathVariable Long id) {
        return ResponseEntity.ok(groupService.find(id));
    }

    @PostMapping("/{groupId}/members/{userId}")
    public ResponseEntity<Group> addMember(@PathVariable Long groupId, @PathVariable Long userId) {
        return ResponseEntity.ok(groupService.addMember(groupId, userId));
    }

    @DeleteMapping("/{groupId}/members/{userId}")
    public ResponseEntity<Void> removeMember(@PathVariable Long groupId, @PathVariable Long userId) {
        groupService.removeMember(groupId, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{groupId}/members")
    public ResponseEntity<List<User>> getMembers(@PathVariable Long groupId) {
        return ResponseEntity.ok(groupService.getMembers(groupId));
    }

    @GetMapping("/{groupId}/members/{userId}")
    public ResponseEntity<Boolean> isMember(@PathVariable Long groupId, @PathVariable Long userId) {
        return ResponseEntity.ok(groupService.isMember(groupId, userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long id) {
        groupService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
