package com.wanted.ecommerce.user.dto.response;

import com.wanted.ecommerce.user.domain.User;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record UserResponse(
    Long id,
    String name,
    String email,
    String avatarUrl,
    LocalDateTime createdAt
) {

    public static UserResponse of(User user){
        return UserResponse.builder()
            .id(user.getId())
            .name(user.getName())
            .avatarUrl(user.getAvatarUrl())
            .build();
    }
}
