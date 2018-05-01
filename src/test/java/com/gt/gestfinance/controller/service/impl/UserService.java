package com.gt.gestfinance.controller.service.impl;

import com.gt.gestfinance.controller.entity.User;
import com.gt.gestfinance.controller.repository.UserRepository;
import com.gt.gestfinance.controller.service.IUserService;
import com.gt.gestfinance.service.impl.BaseEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class for managing users.
 */
@Service
public class UserService extends BaseEntityService<User, Long> implements IUserService {

    /**
     * Le constructeur
     *
     * @param userRepository
     */
    @Autowired
    public UserService(UserRepository userRepository) {
        super(userRepository);
    }
}
