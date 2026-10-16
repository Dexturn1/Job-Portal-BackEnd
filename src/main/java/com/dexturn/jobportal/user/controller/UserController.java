package com.dexturn.jobportal.user.controller;


import com.dexturn.jobportal.dto.ProfileDto;
import com.dexturn.jobportal.dto.UserDto;
import com.dexturn.jobportal.user.service.IUserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @GetMapping("/search/admin")
    public ResponseEntity<?> searchUserByEmail(@RequestParam String email){
        Optional<UserDto> userDtoOptional = userService.searchUserByEmail(email);
        if(userDtoOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "User not found with email:" + email));
        }
        return ResponseEntity.ok(userDtoOptional.get());
    }

    @PatchMapping("/{userId}/role/employer/admin")
    public ResponseEntity<?> elevateToEmployer(@PathVariable Long userId){
        UserDto updatedUser = userService.elevateToEmployer(userId);
        return ResponseEntity.ok(updatedUser);
    }

    @PatchMapping("/{userId}/company/{companyId}/admin")
    public ResponseEntity<?> assignCompanyToEmployer(@PathVariable Long userId,@PathVariable Long companyId){
        UserDto updatedUser = userService.assignCompanyToEmployer(userId, companyId);
        return ResponseEntity.ok(updatedUser);
    }


    @PutMapping(value = "/profile/jobseeker", version = "1.0",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProfileDto> createOrUpdateProfile(
            @RequestPart(value = "profile") String profileJson,
            @RequestPart(value = "profilePicture", required = false) MultipartFile profilePicture,
            @RequestPart(value = "resume", required = false) MultipartFile resume,
            Authentication authentication) throws JsonProcessingException {

        String userEmail = authentication.getName();

        ProfileDto savedProfile = userService.createOrUpdateProfile(
                userEmail, profileJson, profilePicture, resume);
        return ResponseEntity.ok(savedProfile);
    }


    @GetMapping(value = "/profile/jobseeker", version = "1.0")
    public ResponseEntity<ProfileDto> getProfile(Authentication authentication){
        String email = authentication.getName();
        ProfileDto profileDto = userService.getProfile(email);

        return ResponseEntity.ok(profileDto);
    }








}
