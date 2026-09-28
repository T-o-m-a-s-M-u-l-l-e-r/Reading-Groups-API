package com.muller_tomas.reading_groups.dto;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.muller_tomas.reading_groups.model.Group;
import com.muller_tomas.reading_groups.model.User;

@Component
public class MapperHelper {

	public Set<GroupResponse> mapGroups(Set<Group> groups) {
		Set<GroupResponse> groupResponses = groups.stream().map(
				group -> new GroupResponse(group.getId(), group.getAdministratorUser().getId(), group.getCreatedAt(), group.getGroupName()))
				.collect(Collectors.toSet());
		return groupResponses;
	}

	public Set<UserResponse> mapUsers(Set<User> users) {
		Set<UserResponse> userResponses = users.stream().map(
				user -> new UserResponse(user.getId(), user.getUsername()))
				.collect(Collectors.toSet());
		return userResponses;
	}

}
