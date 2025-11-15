package aleks.learningplatform.web.dto.user;

import aleks.learningplatform.domain.user.UserRole;
import lombok.Data;

@Data
public class CreateUserRequest {
    private String name;
    private String email;
    private UserRole role;
    private String bio;
    private String avatarUrl;
}
