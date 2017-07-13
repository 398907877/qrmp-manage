package com.yanyan.persist.system;


import com.yanyan.data.domain.system.Attachment;
import com.yanyan.core.lang.Page;
import com.yanyan.data.query.system.AttachmentQuery;

/**
 * 附件数据存取类
 * User: Saintcy
 * Date: 2015/3/6
 * Time: 14:55
 */
public interface AttachmentDao {

    void insertAttachment(Attachment attachment);

    void updateAttachment(Attachment attachment);

    void deleteAttachment(Long attachment_id);

    void deleteAttachments(Long ref_id);

    void deleteAttachments(Long ref_id, Integer ref_type);

    void deleteAttachments(Long ref_id, Integer ref_type, Integer ref_sub_type);

    Attachment getAttachment(Long attachment_id);

    Page<Attachment> findAttachment(AttachmentQuery query);
}
