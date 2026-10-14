package com.dexturn.jobportal.user.service.impl;

import com.dexturn.jobportal.constanst.ApplicationConstants;
import com.dexturn.jobportal.dto.UserDto;
import com.dexturn.jobportal.entity.Company;
import com.dexturn.jobportal.entity.JobPortalUser;
import com.dexturn.jobportal.entity.Role;
import com.dexturn.jobportal.repository.CompanyRepository;
import com.dexturn.jobportal.repository.JobPortalUserRepository;
import com.dexturn.jobportal.repository.RoleRepository;
import com.dexturn.jobportal.user.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;



@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final JobPortalUserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CompanyRepository companyRepository;


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
