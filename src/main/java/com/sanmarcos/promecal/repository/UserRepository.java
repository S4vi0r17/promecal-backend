package com.sanmarcos.promecal.repository;

import com.sanmarcos.promecal.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>{
    User findByUsername(String username);
}
