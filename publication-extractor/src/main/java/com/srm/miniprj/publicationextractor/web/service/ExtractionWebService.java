package com.srm.miniprj.publicationextractor.web.service;

import com.srm.miniprj.publicationextractor.services.AllNewsSiteRunner;
import org.springframework.stereotype.Service;

@Service
public class ExtractionWebService {

    private AllNewsSiteRunner allNewsSiteRunner;

    public ExtractionWebService(AllNewsSiteRunner allNewsSiteRunner) {
        this.allNewsSiteRunner = allNewsSiteRunner;
    }

    public void extractAllConfiguredSites() {
        allNewsSiteRunner.extractAllPublications();
    }


}
