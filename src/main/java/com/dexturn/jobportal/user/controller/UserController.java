package com.dexturn.jobportal.user.controller;


import com.dexturn.jobportal.dto.UserDto;
import com.dexturn.jobportal.user.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
