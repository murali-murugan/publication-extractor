package com.srm.miniprj.publicationextractor.repository;

import com.srm.miniprj.publicationextractor.domain.FinSiteConfigData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FinSiteConfigDataRepo extends JpaRepository<FinSiteConfigData, String> {
}
