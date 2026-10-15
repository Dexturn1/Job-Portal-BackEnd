package com.dexturn.jobportal.util;


import com.dexturn.jobportal.constanst.ApplicationConstants;
import com.dexturn.jobportal.dto.JobDto;
import com.dexturn.jobportal.entity.Job;
import com.dexturn.jobportal.entity.JobPortalUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class ApplicationUtility {
    public static String getLoggedInUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        System.out.println("Authentication: " + authentication);

        if (authentication == null
                || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {

            System.out.println("Returning SYSTEM: "
                    + ApplicationConstants._SYSTEM_);

            return ApplicationConstants._SYSTEM_;
        }

        Object principal = authentication.getPrincipal();

        System.out.println("Principal: " + principal);
        System.out.println("Principal type: "
                + principal.getClass().getName());


        if (principal instanceof JobPortalUser jobPortalUser) {

            String email = jobPortalUser.getEmail();

            System.out.println("Email: " + email);

            return email;
        }

        return principal.toString();
    }


    public static JobDto transformJobToDto(Job job) {
        return new JobDto(
                job.getId(),
                job.getTitle(),
                job.getCompany().getId(),
                job.getCompany().getName(),
                job.getCompany().getLogo(),
                job.getLocation(),
                job.getWorkType(),
                job.getJobType(),
                job.getCategory(),
                job.getExperienceLevel(),
                job.getSalaryMin(),
                job.getSalaryMax(),
                job.getSalaryCurrency(),
                job.getSalaryPeriod(),
                job.getDescription(),
                job.getRequirements(),
                job.getBenefits(),
                job.getPostedDate(),
                job.getApplicationDeadline(),
                job.getApplicationsCount(),
                job.getFeatured(),
                job.getUrgent(),
                job.getRemote(),
                job.getStatus()
        );

    }

}
