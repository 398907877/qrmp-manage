package com.yanyan.service.system;


import com.yanyan.core.lang.Page;
import com.yanyan.data.domain.system.Corporation;
import com.yanyan.data.query.system.CorporationQuery;

/**
 * 企业服务
 * User: Saintcy
 * Date: 2015/8/21
 * Time: 23:10
 */
public interface CorporationService {
    /**
     * 创建新企业
     *
     * @param corporation
     * @return
     */
    long createCorporation(Corporation corporation);

    /**
     * 更新企业
     *
     * @param corporation
     */
    void updateCorporation(Corporation corporation);

    /**
     * 删除企业
     *
     * @param corp_id
     */
    void deleteCorporation(Long corp_id);

    /**
     * 恢复企业
     *
     * @param corp_id
     */
    void restoreCorporation(Long corp_id);

    /**
     * 获得企业
     *
     * @param corp_id
     * @return
     */
    Corporation getCorporation(Long corp_id);

    /**
     * 查询企业
     *
     * @param query 查询条件
     * @return
     */
    Page<Corporation> findCorporation(CorporationQuery query);
}
