package br.com.DataPilots.Fileflow.services;

import br.com.DataPilots.Fileflow.entities.Group;
import br.com.DataPilots.Fileflow.entities.GroupUser;
import br.com.DataPilots.Fileflow.entities.User;
import br.com.DataPilots.Fileflow.exceptions.GroupNotFoundException;
import br.com.DataPilots.Fileflow.repositories.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService {
    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserService userService;

    public Group find(Long id) {
        return groupRepository.findById(id).orElseThrow(() -> new GroupNotFoundException(id));
    }

    public Group create(String name, User owner) {
        Group group = new Group();
        group.setName(name);
        group.setOwner(owner);
        return groupRepository.save(group);
    }

    public Group addMember(Long groupId, Long userId) {
        Group group = find(groupId);
        User member = userService.find(userId);
        GroupUser groupUser = new GroupUser(group, member);
        group.getMembers().add(groupUser);
        return groupRepository.save(group);
    }

    public void removeMember(Long groupId, Long userId) {
        Group group = find(groupId);
        group.getMembers().removeIf(member -> member.getUser().getId().equals(userId));
        groupRepository.save(group);
    }

    public boolean isMember(Long groupId, Long userId) {
        Group group = find(groupId);
        return group.getMembers().stream()
            .anyMatch(member -> member.getUser().getId().equals(userId));
    }

    public List<User> getMembers(Long groupId) {
        Group group = find(groupId);
        return group.getMembers().stream()
            .map(GroupUser::getUser)
            .toList();
    }

    public void deleteById(Long id) {
        groupRepository.deleteById(id);
    }

    public List<Group> getUserGroups(Long userId) {
        return groupRepository.findAll().stream()
            .filter(group -> isMember(group.getId(), userId))
            .toList();
    }
}
