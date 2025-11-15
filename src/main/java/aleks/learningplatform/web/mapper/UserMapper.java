package aleks.learningplatform.web.mapper;

import aleks.learningplatform.domain.user.Profile;
import aleks.learningplatform.domain.user.User;
import aleks.learningplatform.web.dto.user.CreateUserRequest;
import aleks.learningplatform.web.dto.user.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDto toDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        if (user.getProfile() != null) {
            dto.setBio(user.getProfile().getBio());
            dto.setAvatarUrl(user.getProfile().getAvatarUrl());
        }
        return dto;
    }

    public User fromCreate(CreateUserRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());

        if (request.getBio() != null || request.getAvatarUrl() != null) {
            Profile profile = new Profile();
            profile.setBio(request.getBio());
            profile.setAvatarUrl(request.getAvatarUrl());
            profile.setUser(user);
            user.setProfile(profile);
        }
        return user;
    }
}

