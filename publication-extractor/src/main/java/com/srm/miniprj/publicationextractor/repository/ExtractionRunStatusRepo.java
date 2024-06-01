package com.srm.miniprj.publicationextractor.repository;

import com.srm.miniprj.publicationextractor.domain.ExtractionRunStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExtractionRunStatusRepo extends JpaRepository<ExtractionRunStatus, String> {
}
