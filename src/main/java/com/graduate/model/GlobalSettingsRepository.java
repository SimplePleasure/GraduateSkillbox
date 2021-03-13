package com.graduate.model;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface GlobalSettingsRepository extends CrudRepository<GlobalSettings, Integer> {

    @Query(value = "SELECT code, (case when value = 'NO' then 'false' else 'true' end) as value FROM blog.global_settings", nativeQuery = true)
    List<Settings> getSettings();

    @Modifying
    @Transactional
    @Query(value = "UPDATE blog.global_settings SET value = CASE code " +
            "WHEN 'MULTIUSER_MODE' THEN CASE :multiuser WHEN TRUE THEN 'YES' ELSE 'NO' END " +
            "WHEN 'POST_PREMODERATION' THEN CASE :premoderation WHEN TRUE THEN 'YES' ELSE 'NO' END " +
            "WHEN 'STATISTICS_IS_PUBLIC' THEN CASE :statistic WHEN TRUE THEN 'YES' ELSE 'NO' END ELSE NULL END", nativeQuery = true)
    void setSettings(@Param("multiuser") boolean multiuser,
                     @Param("premoderation") boolean premoderation, @Param("statistic") boolean statistic);

    @Query(value = "SELECT code, CASE value WHEN 'YES' THEN 'TRUE' ELSE 'FALSE' END AS 'value' FROM blog.global_settings WHERE code = :code", nativeQuery = true)
    Settings isParameterEnabled(@Param("code") String code);

    interface Settings {
        String getCode();
        boolean getValue();
    }
}
