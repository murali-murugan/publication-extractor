package com.srm.miniprj.publicationextractor;


import com.srm.miniprj.publicationextractor.services.AllNewsSiteRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class PublicationExtractorApplication {

    public static void main(String[] args) {
       ApplicationContext ctx = (ApplicationContext) SpringApplication.run(PublicationExtractorApplication.class, args);

        //AllNewsSiteRunner allNewsSiteRunner = (AllNewsSiteRunner) ctx.getBean("allNewsSiteRunner");
       // allNewsSiteRunner.extractAllPublications();
    }

}
