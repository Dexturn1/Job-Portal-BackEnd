package com.dexturn.jobportal.job.service.impl;

import com.dexturn.jobportal.dto.JobApplicationDto;
import com.dexturn.jobportal.dto.JobDto;
import com.dexturn.jobportal.dto.UpdateJobApplicationDto;
import com.dexturn.jobportal.entity.Job;
import com.dexturn.jobportal.entity.JobApplication;
import com.dexturn.jobportal.entity.JobPortalUser;
import com.dexturn.jobportal.job.service.JobService;
import com.dexturn.jobportal.repository.JobApplicationRepository;
import com.dexturn.jobportal.repository.JobPortalUserRepository;
import com.dexturn.jobportal.repository.JobRepository;
import com.dexturn.jobportal.util.ApplicationUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final JobPortalUserRepository jobPortalUserRepository;
    private final JobApplicationRepository jobApplicationRepository;

    @Override
    public List<JobDto> getEmployerJobs(String employerEmail) {

        JobPortalUser employer = jobPortalUserRepository.findJobPortalUserByEmail(employerEmail)
                .orElseThrow(()-> new RuntimeException("Employer Not Found"));

        if(employer.getCompany() == null){
            throw new RuntimeException("Employer does not have a company assigned");
        }

        List<Job> jobs = employer.getCompany().getJobs();
        return jobs.stream()
                .map(ApplicationUtility::transformJobToDto).toList();

    }


    @Transactional
    @Override
    public JobDto updateJobStatus(Long jobId, String status, String employerEmail) {
        //  Validate Status
        if(!status.equals("ACTIVE") && !status.equals("CLOSED") && status.equals("DRAFT")){
            throw new RuntimeException("Invalid status. must be ACTIVE, CLOSED or DRAFT");
        }

        JobPortalUser employer = jobPortalUserRepository.findJobPortalUserByEmail(employerEmail)
                .orElseThrow(()-> new RuntimeException("Employer does not have a company assigned"));


        Job job  = employer.getCompany().getJobs().stream().filter(j->j.getId().equals(jobId)).findFirst().orElseThrow(()->
                new RuntimeException("Job not found"));

        job.setStatus(status);
        return ApplicationUtility.transformJobToDto(job);
    }

    @Override
    @Transactional
    public JobDto createJob(JobDto jobDto, String employerEmail) {
        // validate Employer and get their company
        JobPortalUser employer = jobPortalUserRepository.findJobPortalUserByEmail(employerEmail)
                .orElseThrow(()-> new RuntimeException("Employer not found"));

        if(employer.getCompany() == null){
            throw new RuntimeException("Employer does not have a company assigned. Please contact admin.");
        }
        Job job = transformDtoToEntity(jobDto);
        job.setPostedDate(Instant.now());
        job.setApplicationsCount(0);
        job.setStatus("DRAFT");
        job.setCompany(employer.getCompany());
        Job savedjob = jobRepository.save(job);

        return ApplicationUtility.transformJobToDto(savedjob);
    }

    @Override
    public List<JobApplicationDto> getApplicationsByJobForEmployer(Long jobId) {
        List<JobApplication> applications = jobApplicationRepository.findByJobIdOrderByAppliedAtAsc(jobId);
        return applications.stream()
                .map(ApplicationUtility::mapToJobApplicationDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public boolean updateJobApplication(UpdateJobApplicationDto dto) {
        int updatedRows = jobApplicationRepository.updateStatusAndNotesById(
                dto.status().name(), dto.notes(),dto.applicationId(), ApplicationUtility.getLoggedInUser());
        return updatedRows > 0;
    }

    private Job transformDtoToEntity(JobDto jobDto){
        Job job = new Job();
        BeanUtils.copyProperties(jobDto, job);
        return job;
    }
}
