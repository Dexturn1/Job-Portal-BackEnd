package com.dexturn.jobportal.user.service;

import com.dexturn.jobportal.dto.UserDto;

import java.util.Optional;

public interface IUserService {

    Optional<UserDto> searchUserByEmail(String email);

    UserDto elevateToEmployer(Long userId);

    UserDto assignCompanyToEmployer(Long userId, Long companyId);

}
