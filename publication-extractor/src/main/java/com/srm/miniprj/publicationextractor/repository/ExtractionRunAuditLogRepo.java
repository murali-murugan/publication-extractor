package com.srm.miniprj.publicationextractor.repository;

import com.srm.miniprj.publicationextractor.domain.ExtractionRunAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExtractionRunAuditLogRepo extends JpaRepository<ExtractionRunAuditLog, Long> {
}
