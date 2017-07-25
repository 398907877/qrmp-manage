package com.yanyan.persist.system;


import com.yanyan.data.domain.system.Corporation;
import com.yanyan.data.query.system.CorporationQuery;
import com.yanyan.core.lang.Page;

/**
 * 企业DAO
 * User: Saintcy
 * Date: 2015/8/21
 * Time: 23:10
 */
public interface CorporationDao {
    void insertCorporation(Corporation corporation);

    void updateCorporation(Corporation corporation);

    void deleteCorporation(Long corp_id);

    void restoreCorporation(Long corp_id);

    Corporation getCorporation(Long corp_id);

    Page<Corporation> findCorporation(CorporationQuery query);
}
