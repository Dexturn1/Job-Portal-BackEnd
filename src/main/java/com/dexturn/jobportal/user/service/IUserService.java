package com.dexturn.jobportal.user.service;

import com.dexturn.jobportal.dto.ProfileDto;
import com.dexturn.jobportal.dto.UserDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface IUserService {

    Optional<UserDto> searchUserByEmail(String email);

    UserDto elevateToEmployer(Long userId);

    UserDto assignCompanyToEmployer(Long userId, Long companyId);

    ProfileDto createOrUpdateProfile(String userEmail, String profileJson,
                                     MultipartFile profilePicture, MultipartFile resume) throws JsonProcessingException;

    ProfileDto getProfile(String userEmail);


}
