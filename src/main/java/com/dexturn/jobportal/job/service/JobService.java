package com.dexturn.jobportal.job.service;


import com.dexturn.jobportal.dto.JobDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface JobService {
    /**
     * Get all the jobs posted by the employer's company
     * @param employerEmail the email of the emplyer
     * @return list of jobs
     */
    List<JobDto> getEmployerJobs(String employerEmail);


    /**
     * update the status of a job
     * @param jobId is The id of the job
     * @param status is the new status (ACTIVE, CLOSED, DRAFT)
     * @param employerEmail is email of the employer making the requst
     * @return updated jobDto
     */
    JobDto updateJobStatus(Long jobId, String status, String employerEmail);

    /**
     * Create a new job for the Employer's company
     * @param jobDto the job data
     * @param employerEmail the email of the employer createing the job
     * @return created job
     */
    JobDto createJob(JobDto jobDto, String employerEmail);
}
