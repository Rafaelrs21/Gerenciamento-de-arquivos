package br.com.DataPilots.Fileflow.services;

import br.com.DataPilots.Fileflow.entities.Group;
import br.com.DataPilots.Fileflow.entities.GroupUser;
import br.com.DataPilots.Fileflow.entities.User;
import br.com.DataPilots.Fileflow.repositories.GroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GroupServiceTest {
    @InjectMocks
    private GroupService service;

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private UserService userService;

    private Group group;
    private User userOwner;
    private User otherUser;

    @BeforeEach
    void setUp() {
        userOwner = new User("userOwner", "pass", 1L);
        otherUser = new User("otherUser", "pass", 2L);
        group = new Group(1L, "Test Group", userOwner);
    }

    @Test
    void findShouldReturnGroupWhenIdExists() {
        when(groupRepository.findById(group.getId())).thenReturn(Optional.of(group));

        Group result = service.find(group.getId());

        assertNotNull(result);
        assertEquals(group.getId(), result.getId());
        assertEquals("Test Group", result.getName());
        assertEquals(userOwner, result.getOwner());
    }

    @Test
    void addMemberShouldAddUserToGroup() {
        when(groupRepository.findById(group.getId())).thenReturn(Optional.of(group));
        when(userService.find(otherUser.getId())).thenReturn(otherUser);
        when(groupRepository.save(any(Group.class))).thenReturn(group);

        Group result = service.addMember(group.getId(), otherUser.getId());

        assertNotNull(result);
        assertEquals(1, result.getMembers().size());
        assertEquals(otherUser, result.getMembers().get(0).getUser());
        verify(groupRepository).save(any(Group.class));
    }

    @Test
    void removeMemberShouldRemoveUserFromGroup() {
        GroupUser groupUser = new GroupUser(group, otherUser);
        group.getMembers().add(groupUser);
        when(groupRepository.findById(group.getId())).thenReturn(Optional.of(group));
        when(groupRepository.save(any(Group.class))).thenReturn(group);

        service.removeMember(group.getId(), otherUser.getId());

        assertTrue(group.getMembers().isEmpty());
        verify(groupRepository).save(any(Group.class));
    }

    @Test
    void isMemberShouldReturnTrueWhenUserIsMember() {
        GroupUser groupUser = new GroupUser(group, otherUser);
        group.getMembers().add(groupUser);
        when(groupRepository.findById(group.getId())).thenReturn(Optional.of(group));

        boolean result = service.isMember(group.getId(), otherUser.getId());

        assertTrue(result);
    }

    @Test
    void getMembersShouldReturnListOfUsers() {
        GroupUser groupUser = new GroupUser(group, otherUser);
        group.getMembers().add(groupUser);
        when(groupRepository.findById(group.getId())).thenReturn(Optional.of(group));

        List<User> result = service.getMembers(group.getId());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(otherUser, result.get(0));
    }
}
