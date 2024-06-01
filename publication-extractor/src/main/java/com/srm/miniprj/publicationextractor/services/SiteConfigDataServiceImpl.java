package com.srm.miniprj.publicationextractor.services;

import com.srm.miniprj.publicationextractor.domain.ExtractionRunAuditLog;
import com.srm.miniprj.publicationextractor.domain.ExtractionRunStatus;
import com.srm.miniprj.publicationextractor.domain.FinSiteConfigData;
import com.srm.miniprj.publicationextractor.repository.ExtractionRunAuditLogRepo;
import com.srm.miniprj.publicationextractor.repository.ExtractionRunStatusRepo;
import com.srm.miniprj.publicationextractor.repository.FinSiteConfigDataRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
public class SiteConfigDataServiceImpl implements SiteConfigDataService{

    private final FinSiteConfigDataRepo finSiteConfigDataRepo;
    private final ExtractionRunStatusRepo extractionRunStatusRepo;
    private final ExtractionRunAuditLogRepo extractionRunAuditLogRepo;

    @Value("${update.lastpubdate.flag}")
    private boolean updateLastPubdateFlg = true;

    public SiteConfigDataServiceImpl(FinSiteConfigDataRepo finSiteConfigDataRepo,
                                     ExtractionRunStatusRepo extractionRunStatusRepo,
                                     ExtractionRunAuditLogRepo extractionRunAuditLogRepo) {
        this.finSiteConfigDataRepo = finSiteConfigDataRepo;
        this.extractionRunStatusRepo = extractionRunStatusRepo;
        this.extractionRunAuditLogRepo = extractionRunAuditLogRepo;
    }


    @Override
    public List<FinSiteConfigData> getAllConfigurations() {
        return finSiteConfigDataRepo.findAll();
    }


    @Override
    public PublishedNewsItem updateStatus(String siteId, String status) {

        ExtractionRunStatus extractionRunStatus = extractionRunStatusRepo.findById(siteId).orElse(null);
        if (extractionRunStatus != null) {
            extractionRunStatus.setUpdateDate(LocalDateTime.now(ZoneId.systemDefault()));
            extractionRunStatus.setLastRunDate(LocalDateTime.now(ZoneId.systemDefault()));
            extractionRunStatus.setRunStatus(status);
            extractionRunStatusRepo.save(extractionRunStatus);
        }

        return null;
    }

    @Override
    public PublishedNewsItem updateStartStatus(FinSiteConfigData finSiteConfigData) {

        ExtractionRunStatus extractionRunStatus = extractionRunStatusRepo.findById(finSiteConfigData.getSiteId()).orElse(null);
        if (extractionRunStatus != null) {
            extractionRunStatus.setUpdateDate(LocalDateTime.now(ZoneId.systemDefault()));
            extractionRunStatus.setRunStatus(ExtractionRunStatus.EXTRACTION_RUN_STATUS_RUNNING);
            extractionRunStatusRepo.save(extractionRunStatus);
        }

        return null;
    }

    @Override
    public PublishedNewsItem updateCompletionStatus(PublishedNewsItem publishedNewsItem) {

        ExtractionRunStatus extractionRunStatus = extractionRunStatusRepo.findById(publishedNewsItem.getSiteId()).orElse(null);
        if (extractionRunStatus != null) {
            extractionRunStatus.setLastRunDate(LocalDateTime.now(ZoneId.systemDefault()));
            extractionRunStatus.setUpdateDate(LocalDateTime.now(ZoneId.systemDefault()));
            if (updateLastPubdateFlg) { extractionRunStatus.setLastPublishDate(publishedNewsItem.getPublishedDate()); }
            extractionRunStatus.setRunStatus(ExtractionRunStatus.EXTRACTION_RUN_STATUS_COMPLETED);

            extractionRunStatusRepo.save(extractionRunStatus);
        }

        return null;
    }

    @Override
    public void addExtractionAuditLog(PublishedNewsItem publishedNewsItem, LocalDateTime startDateTime, String completionStatus) {
        ExtractionRunAuditLog extractionRunAuditLog = ExtractionRunAuditLog.builder().siteId(publishedNewsItem.getSiteId())
                .runStartDate(startDateTime)
                .runCompletionDate(LocalDateTime.now(ZoneId.systemDefault() ) )
                .runCompletionStatus(completionStatus)
                .build();

        extractionRunAuditLogRepo.save(extractionRunAuditLog);

    }
}
