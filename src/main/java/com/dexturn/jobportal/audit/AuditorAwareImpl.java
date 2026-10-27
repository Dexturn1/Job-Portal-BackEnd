package com.dexturn.jobportal.audit;

import com.dexturn.jobportal.util.ApplicationUtility;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditorAwareImpl")
public class AuditorAwareImpl implements AuditorAware<String> {
//
//    @Override
//    public Optional<String> getCurrentAuditor() {
//        return Optional.of(ApplicationUtility.getLoggedInUser());
//    }


    @Override
    public Optional<String> getCurrentAuditor() {

        String auditor = ApplicationUtility.getLoggedInUser();

        System.out.println("Auditor returned: " + auditor);

        return Optional.of(auditor);
    }
}