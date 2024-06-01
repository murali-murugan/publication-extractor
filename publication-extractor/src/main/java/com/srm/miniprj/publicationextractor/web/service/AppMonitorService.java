package com.srm.miniprj.publicationextractor.web.service;

import com.srm.miniprj.publicationextractor.domain.FinSiteConfigData;
import com.srm.miniprj.publicationextractor.services.SiteConfigDataService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppMonitorService {

    private final SiteConfigDataService siteConfigDataService;

    public AppMonitorService(SiteConfigDataService siteConfigDataService) {
        this.siteConfigDataService = siteConfigDataService;
    }

    public List<FinSiteConfigData> getSiteConfigData() {

        List<FinSiteConfigData>  siteConfigDataList = siteConfigDataService.getAllConfigurations();


        return siteConfigDataList;

    }




}
