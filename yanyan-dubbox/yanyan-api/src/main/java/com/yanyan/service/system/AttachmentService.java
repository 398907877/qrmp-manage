package com.yanyan.service.system;

import com.yanyan.core.lang.Page;
import com.yanyan.data.domain.system.Attachment;
import com.yanyan.data.query.system.AttachmentQuery;

import java.util.List;

/**
 * 文件服务
 * User: Saintcy
 * Date: 2016/9/1
 * Time: 13:32
 */
public interface AttachmentService {
    /**
     * 查询文件
     *
     * @param query
     * @return
     */
    Page<Attachment> findAttachment(AttachmentQuery query);

    /**
     * 保存分类下文件
     * @param ref_id 关联id
     * @param ref_type 关联类型
     * @param ref_sub_type 关联子类型
     * @param attachment 文件
     */
    void saveAttachment(Long ref_id, Integer ref_type, Integer ref_sub_type, Attachment attachment);

    /**
     * 批量保存分类下文件
     *
     * @param ref_id 关联id
     * @param ref_type 关联类型
     * @param ref_sub_type 关联子类型
     * @param attachments 附件列表
     */
    void saveAttachments(Long ref_id, Integer ref_type, Integer ref_sub_type, List<Attachment> attachments);

    /**
     * 获取文件
     * @param attachment_id 附件id
     * @return
     */
    Attachment getAttachment(Long attachment_id);

    /**
     * 获取分类下文件(获得列表中的第一个)
     * @param ref_id 关联id
     * @param ref_type 关联类型
     * @param ref_sub_type 关联子类型
     * @return
     */
    Attachment getAttachment(Long ref_id, Integer ref_type, Integer ref_sub_type);

    /**
     * 获取文件列表
     *
     * @param ref_id 关联id
     * @param ref_type 关联类型
     * @param ref_sub_type 关联子类型
     * @return
     */
    List<Attachment> getAttachments(Long ref_id, Integer ref_type, Integer ref_sub_type);

    /**
     * 删除文件
     * @param attachment_id 文件id
     */
    void deleteAttachment(Long attachment_id);

    /**
     * 删除分类下所有文件
     * @param ref_id 关联id
     * @param ref_type 关联类型
     * @param ref_sub_type 关联子类型
     */
    void deleteAttachments(Long ref_id, Integer ref_type, Integer ref_sub_type);
}
