package com.papertrading.graphql.resolver;

import org.springframework.stereotype.Component;

import com.papertrading.dto.response.UserResponse;
import com.papertrading.model.User;
import com.papertrading.util.DtoMapper;

@Component
public class UserResolver {

	public UserResponse toResponse(User user) {
		return DtoMapper.toUserResponse(user);
	}
}
