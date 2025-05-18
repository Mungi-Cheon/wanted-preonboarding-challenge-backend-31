package com.wanted.ecommerce.user.repository;

import com.wanted.ecommerce.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
