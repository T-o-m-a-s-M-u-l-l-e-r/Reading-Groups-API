package com.muller_tomas.reading_groups.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import com.muller_tomas.reading_groups.dto.GroupCreationRequest;
import com.muller_tomas.reading_groups.dto.GroupResponse;
import com.muller_tomas.reading_groups.dto.MapperHelper;
import com.muller_tomas.reading_groups.dto.UserResponse;
import com.muller_tomas.reading_groups.exception.GroupNotFoundException;
import com.muller_tomas.reading_groups.exception.UserNotFoundException;
import com.muller_tomas.reading_groups.model.Group;
import com.muller_tomas.reading_groups.model.User;
import com.muller_tomas.reading_groups.repository.GroupRepository;
import com.muller_tomas.reading_groups.repository.UserRepository;

@Service
public class GroupService {
	private final GroupRepository groupRepository;
	private final UserRepository userRepository;
	private final MapperHelper mapperHelper;
	private final FileService fileService;

	public GroupService(GroupRepository groupRepository, UserRepository userRepository, MapperHelper mapperHelper, FileService fileService) {
		this.groupRepository = groupRepository;
		this.userRepository = userRepository;
		this.mapperHelper = mapperHelper;
		this.fileService = fileService;
	}

	public Set<GroupResponse> getUserGroups(int userId) throws UserNotFoundException {
		userRepository.findById(userId)
				.orElseThrow(() -> new UserNotFoundException("User with specified id not found"));
		Set<Group> groups = groupRepository.findByUsersId(userId);
		return mapperHelper.mapGroups(groups);
	}

	public Set<UserResponse> getGroupMembers(int userId, int groupId) throws GroupNotFoundException {
		Group group = groupRepository.findById(groupId)
				.orElseThrow(() -> new GroupNotFoundException("Group with specified id not found"));
		Set<User> users = group.getUsers();
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new UserNotFoundException("User with specified id not found"));

		if (!users.contains(user)) {
			throw new BadCredentialsException("User not authenticated to view group");
		}

		return mapperHelper.mapUsers(users);
	}

	public GroupResponse createGroup(int userId, GroupCreationRequest groupCreationRequest) throws UserNotFoundException {
		User administratorUser = userRepository.findById(userId)
				.orElseThrow(() -> new UserNotFoundException("User with specified id not found"));
		String filePath = fileService.saveReadingText(groupCreationRequest.getReadingText());
		HashSet<User> users = new HashSet<User>();
		users.add(administratorUser);
		Group group = groupRepository.save(new Group(filePath, administratorUser, groupCreationRequest.getGroupName(), users));
		return new GroupResponse(group.getId(), group.getAdministratorUser().getId(), group.getCreatedAt(), group.getGroupName());
	}

}
