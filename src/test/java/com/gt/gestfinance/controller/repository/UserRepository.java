package com.gt.gestfinance.controller.repository;

import com.gt.gestfinance.controller.entity.User;
import com.gt.gestfinance.repository.BaseEntityRepository;

/**
 * Spring Data JPA repository for the User entity.
 */
public interface UserRepository extends BaseEntityRepository<User, Long> {
}
