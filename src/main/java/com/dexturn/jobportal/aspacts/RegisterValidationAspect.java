package com.dexturn.jobportal.aspacts;


import com.dexturn.jobportal.dto.RegisterRequestDto;
import com.dexturn.jobportal.entity.JobPortalUser;
import com.dexturn.jobportal.exception.RegistrationValidationException;
import com.dexturn.jobportal.repository.JobPortalUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.authentication.password.CompromisedPasswordDecision;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class RegisterValidationAspect {


    private final CompromisedPasswordChecker compromisedPasswordChecker;
    private final JobPortalUserRepository jobPortalUserRepository;



    @Before("""
            execution(* com.dexturn.jobportal.auth.AuthController.registerUser(..))
            """)
    private void validateBeforeRegister(JoinPoint joinPoint){


        Object[] args = joinPoint.getArgs();

        RegisterRequestDto requestDto = (RegisterRequestDto) args[0];


        log.info("🔍 Validating user registration request");
        Map<String, String> errors = new HashMap<>();

        // 1 Compromised password check
        CompromisedPasswordDecision decision =
                compromisedPasswordChecker.check(requestDto.password());

        if(decision.isCompromised()){
            errors.put("password", "Choose a strong password");
        }
        // 2 Existing User check

        Optional<JobPortalUser> existingUser =
                jobPortalUserRepository.readUserByEmailOrMobileNumber(
                        requestDto.email(),requestDto.mobileNumber()
                );

        if(existingUser.isPresent()){
            JobPortalUser user = existingUser.get();


            if(user.getEmail().equalsIgnoreCase(requestDto.email())){
                errors.put("eamil","Email is already registered");
            }

            if(user.getMobileNumber().equals(requestDto.mobileNumber())){
                errors.put("mobileNumber", "Mobile number is already registered");
            }
        }

        // 3 Stop executing if validation fails
        if(!errors.isEmpty()){
            log.warn("❌ Registration validation falied: {}",errors);
            throw new RegistrationValidationException(errors);
        }

        log.info("✅ Registration validation passed");

    }



}
