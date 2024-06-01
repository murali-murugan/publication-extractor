package com.srm.miniprj.publicationextractor.services;

import com.srm.miniprj.publicationextractor.domain.ExtractionRunAuditLog;
import com.srm.miniprj.publicationextractor.domain.ExtractionRunStatus;
import com.srm.miniprj.publicationextractor.domain.FinSiteConfigData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;

@Service
public class AllNewsSiteRunner  {

    @Autowired
    private ExtractionWebDriver extractionWebDriver;

    @Autowired
    private EmailService emailService;

    @Autowired
    private SiteConfigDataService siteConfigDataService;

    public void extractAllPublications() {

        List<FinSiteConfigData> siteConfigDataList = siteConfigDataService.getAllConfigurations();

        for (FinSiteConfigData finSiteConfigData : siteConfigDataList) {

            if (!finSiteConfigData.isEnabled()) continue;

            //Convenience
            finSiteConfigData.setLastExtractionDate(finSiteConfigData.getExtractionRunStatus().getLastPublishDate());

            PubExtractionService pubExtractionService = PubExtractionService.getInstance(finSiteConfigData);

            LocalDateTime startTime = LocalDateTime.now(ZoneId.systemDefault());
            siteConfigDataService.updateStartStatus(finSiteConfigData);

            List<PublishedNewsItem> publishedNewsItems = pubExtractionService.getExtractedNews(extractionWebDriver);

            extractionWebDriver.close();

            PublishedNewsItem lastPublishedNewsItem = null;

            int row=1;
            for (PublishedNewsItem publishedNewsItem : publishedNewsItems) {
                if (row==1) {lastPublishedNewsItem = publishedNewsItem;}

                String fullHtmlContent = EmailFormatter.formatEmailHtml(finSiteConfigData, publishedNewsItem);
                try {
                    List recepientList = Arrays.asList(finSiteConfigData.getToRecipientList().split(";"));
                    emailService.sendHtmlEmail(publishedNewsItem.getTitle(), fullHtmlContent, recepientList);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
                row++;
            }

            if (lastPublishedNewsItem != null) {
                siteConfigDataService.updateCompletionStatus(lastPublishedNewsItem);
            } else {
                siteConfigDataService.updateStatus(finSiteConfigData.getSiteId(), ExtractionRunStatus.EXTRACTION_RUN_STATUS_COMPLETED);
            }

            writeAuditLog(finSiteConfigData, startTime, ExtractionRunStatus.EXTRACTION_RUN_STATUS_COMPLETED);
        }
    }

    private void writeAuditLog(FinSiteConfigData finSiteConfigData, LocalDateTime startTime, String status) {
        ExtractionRunAuditLog extractionRunAuditLog = ExtractionRunAuditLog.builder().siteId(finSiteConfigData.getSiteId())
                .runStartDate(startTime)
                .runCompletionDate(LocalDateTime.now(ZoneId.systemDefault()))
                .runCompletionStatus(status)
                .build();

        PublishedNewsItem publishedNewsItem = PublishedNewsItem.builder().siteId(finSiteConfigData.getSiteId())
                        .build();

        siteConfigDataService.addExtractionAuditLog(publishedNewsItem, startTime, status);

    }
}
