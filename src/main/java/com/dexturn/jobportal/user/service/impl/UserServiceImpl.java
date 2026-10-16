package com.dexturn.jobportal.user.service.impl;

import com.dexturn.jobportal.constanst.ApplicationConstants;
import com.dexturn.jobportal.dto.ProfileDto;
import com.dexturn.jobportal.dto.UserDto;
import com.dexturn.jobportal.entity.Company;
import com.dexturn.jobportal.entity.JobPortalUser;
import com.dexturn.jobportal.entity.Profile;
import com.dexturn.jobportal.entity.Role;
import com.dexturn.jobportal.repository.CompanyRepository;
import com.dexturn.jobportal.repository.JobPortalUserRepository;
import com.dexturn.jobportal.repository.ProfileRepository;
import com.dexturn.jobportal.repository.RoleRepository;
import com.dexturn.jobportal.user.service.IUserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;



@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final JobPortalUserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CompanyRepository companyRepository;
    private final ProfileRepository profileRepository;


    @Override
    public Optional<UserDto> searchUserByEmail(String email) {
        return userRepository.findJobPortalUserByEmail(email).map(this::mapToUserDto);
    }

    @Override
    @Transactional
    public UserDto elevateToEmployer(Long userId) {

        JobPortalUser user = userRepository.findById(userId)
                .orElseThrow(()->new RuntimeException("User not found with Id"+ userId));

        // Check if user if already an employer
        if (ApplicationConstants.ROLE_EMPLOYER.equals(user.getRole().getName())){
            return mapToUserDto(user);
        }

        // Check if user is already an admin
        if(ApplicationConstants.ROLE_ADMIN.equals(user.getRole().getName())){
            return mapToUserDto(user);
        }

        //Find ROLE_EMPLOYER
        Role employerRole = roleRepository.findRoleByName(ApplicationConstants.ROLE_EMPLOYER)
                .orElseThrow(()-> new RuntimeException("ROLE_EMPLOYER not found"));

        user.setRole(employerRole);

        // JobPoralUser updateUser = userRepository.save(user);
        /**
         * Why it's unnecessary
         * findById() returns a managed entity
         * You modify it inside a transaction
         * Dirty checking automatically updates it
         */
        return mapToUserDto(user);
    }

    @Transactional
    @Override
    public UserDto assignCompanyToEmployer(Long userId, Long companyId) {
        JobPortalUser user = userRepository.findById(userId)
                .orElseThrow(()->new RuntimeException("User not found with ID: "+ userId));
        // verify user is an employer
        if(!ApplicationConstants.ROLE_EMPLOYER.equals(user.getRole().getName())){
            throw new RuntimeException("User must be an employer to be assigned to a company");
        }
        Company company = companyRepository.findById(companyId)
                .orElseThrow(()-> new RuntimeException("Company not found with ID: "+ companyId));
        user.setCompany(company);
        return mapToUserDto(user);
    }


    @Transactional
    @Override
    public ProfileDto createOrUpdateProfile(String userEmail, String profileJson,
                                            MultipartFile profilePicture, MultipartFile resume) throws JsonProcessingException {

        JobPortalUser user = userRepository.findJobPortalUserByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + userEmail));

        Profile profile = user.getProfile();

        if (null == profile) {
            profile = new Profile();
            profile.setUser(user);
        }

        ObjectMapper objectMapper = new ObjectMapper();

        // Parse JSON string to ProfileDto
        ProfileDto profileDto = objectMapper.readValue(profileJson, ProfileDto.class);
        Profile savedProfile = profileRepository.save(mapToProfile(profile, profileDto, profilePicture, resume));
        return mapToProfileDto(savedProfile, false);
    }

    @Override
    public ProfileDto getProfile(String userEmail) {

        JobPortalUser user = userRepository.findJobPortalUserByEmail(userEmail)
                .orElseThrow(()->new RuntimeException("User not found with email:"+ userEmail));

        if(user.getProfile() == null){
            return null;
        }

        return mapToProfileDto(user.getProfile(),false);

    }

    @Override
    public ProfileDto getProfilePicture(String userEmail) {

        JobPortalUser user = userRepository.findJobPortalUserByEmail(userEmail)
                .orElseThrow(()-> new RuntimeException("User not found with email:"+ userEmail));

        if(user.getProfile() == null){
            return null;
        }

        return mapToProfileDto(user.getProfile(), true);

    }

    @Override
    public ProfileDto getResume(String userEmail) {
        JobPortalUser user = userRepository.findJobPortalUserByEmail(userEmail)
                .orElseThrow(()-> new RuntimeException("User not found with email:"+ userEmail));

        if(user.getProfile() == null){
            return null;
        }

        return mapToProfileDto(user.getProfile(), true);

    }


    private Profile mapToProfile(Profile profile, ProfileDto profileDto,
                                 MultipartFile profilePicture, MultipartFile resume) {
        // Update text fields
        profile.setJobTitle(profileDto.jobTitle());
        profile.setLocation(profileDto.location());
        profile.setExperienceLevel(profileDto.experienceLevel());
        profile.setProfessionalBio(profileDto.professionalBio());
        profile.setPortfolioWebsite(profileDto.portfolioWebsite());

        // Handle profile picture upload
        if (profilePicture != null && !profilePicture.isEmpty()) {
            try {
                profile.setProfilePicture(profilePicture.getBytes());
                profile.setProfilePictureName(profilePicture.getOriginalFilename());
                profile.setProfilePictureType(profilePicture.getContentType());
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload profile picture", e);
            }
        }
        // Handle resume upload
        if (resume != null && !resume.isEmpty()) {
            try {
                profile.setResume(resume.getBytes());
                profile.setResumeName(resume.getOriginalFilename());
                profile.setResumeType(resume.getContentType());
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload resume", e);
            }
        }
        return profile;
    }



    //--------------------------------------------- Transformation Methods ---------------------------------------------------------------------





    private ProfileDto mapToProfileDto(Profile profile, boolean includeBinaryData) {
        ProfileDto dto;
        if (includeBinaryData) {
            dto = new ProfileDto(profile.getId(), profile.getUser().getId(),
                    profile.getJobTitle(), profile.getLocation(), profile.getExperienceLevel(),
                    profile.getProfessionalBio(), profile.getPortfolioWebsite(), profile.getProfilePicture(),
                    profile.getProfilePictureName(), profile.getProfilePictureType(), profile.getResume(),
                    profile.getResumeName(), profile.getResumeType(), profile.getCreatedAt(), profile.getUpdatedAt()
            );
        } else {
            dto = new ProfileDto(profile.getId(), profile.getUser().getId(),
                    profile.getJobTitle(), profile.getLocation(), profile.getExperienceLevel(),
                    profile.getProfessionalBio(), profile.getPortfolioWebsite(), null,
                    profile.getProfilePictureName(), profile.getProfilePictureType(), null,
                    profile.getResumeName(), profile.getResumeType(), profile.getCreatedAt(), profile.getUpdatedAt());
        }
        return dto;
    }

    private UserDto mapToUserDto(JobPortalUser user) {
        UserDto dto = new UserDto();
        BeanUtils.copyProperties(user, dto);
        dto.setUserId(user.getId());
        dto.setRole(user.getRole() != null ? user.getRole().getName() : null);
        dto.setCompanyId(user.getCompany() != null ? user.getCompany().getId() : null);
        dto.setCompanyName(user.getCompany() != null ? user.getCompany().getName() : null);
        return dto;
    }


}
