package com.dexturn.jobportal.repository;

import com.dexturn.jobportal.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {


}