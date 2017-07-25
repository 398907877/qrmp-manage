package com.yanyan.service.system.impl;

import com.google.common.collect.Lists;
import com.yanyan.Configs;
import com.yanyan.core.lang.Page;
import com.yanyan.core.spring.validator.group.Create;
import com.yanyan.data.domain.system.Attachment;
import com.yanyan.data.query.system.AttachmentQuery;
import com.yanyan.persist.system.AttachmentDao;
import com.yanyan.service.BaseService;
import com.yanyan.service.system.AttachmentService;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * User: Saintcy
 * Date: 2016/9/1
 * Time: 13:34
 */
@Service
public class AttachmentServiceImpl extends BaseService implements AttachmentService {
    @Autowired
    private AttachmentDao attachmentDao;

    private long createAttachment(Attachment attachment) {
        validate(attachment, Create.class);
        saveFile(attachment);
        attachmentDao.insertAttachment(attachment);

        return attachment.getId();
    }

    private void saveFile(Attachment attachment) {
        String tempDir = FilenameUtils.normalizeNoEndSeparator((Configs.TEMP_FILE_PATH.startsWith("/") ? "" : "/") + Configs.TEMP_FILE_PATH, true);
        String userDir = FilenameUtils.normalizeNoEndSeparator((Configs.USER_FILE_PATH.startsWith("/") ? "" : "/") + Configs.USER_FILE_PATH, true);
        String baseDir = FilenameUtils.normalizeNoEndSeparator(Configs.BASE_FILE_PATH, true);
        String url = FilenameUtils.normalize((attachment.getUrl().startsWith("/") ? "" : "/") + attachment.getUrl(), true);
        if (StringUtils.startsWith(url, tempDir)) {//临时文件
            File src = new File(baseDir + "/" + url);
            //业务类型/业务子类型/原临时上传路径
            String relaDest = FilenameUtils.normalizeNoEndSeparator(userDir + "/" + attachment.getRef_type() + "/" + attachment.getRef_sub_type() + "/" +
                    StringUtils.replaceOnce(url, tempDir, ""), true);
            File dest = new File(baseDir + "/" + relaDest);
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }

            src.renameTo(dest);//移到正式目录
            attachment.setUrl(relaDest);
        }
    }

    private void removeFile(Attachment attachment) {
        String baseDir = FilenameUtils.normalizeNoEndSeparator(Configs.BASE_FILE_PATH, true);
        try {
            File f = new File(baseDir + "/" + attachment.getUrl());
            f.delete();
        } catch (Exception e) {
            //忽略
        }
    }

    public void deleteAttachment(Long attachment_id) {
        Attachment oldAttachment = getAttachment(attachment_id);
        attachmentDao.deleteAttachment(attachment_id);
        removeFile(oldAttachment);//最后删除实体文件
    }

    public Attachment getAttachment(Long attachment_id) {
        return attachmentDao.getAttachment(attachment_id);
    }

    public Page<Attachment> findAttachment(AttachmentQuery query) {
        return attachmentDao.findAttachment(query);
    }

    public void saveAttachment(Long ref_id, Integer ref_type, Integer ref_sub_type, Attachment attachment) {
        if (attachment == null) return;
        saveAttachments(ref_id, ref_type, ref_sub_type, Collections.singletonList(attachment));
    }

    public void saveAttachments(Long ref_id, Integer ref_type, Integer ref_sub_type, List<Attachment> attachments) {
        List<Attachment> oldAttachments = getAttachments(ref_id, ref_type, ref_sub_type);
        List<Attachment> toRemoves = Lists.newArrayList();
        for (Attachment oldAttachment : oldAttachments) {
            boolean delete = true;
            if (attachments != null) {
                for (Attachment attachment : attachments) {
                    if (StringUtils.equals(attachment.getUrl(), oldAttachment.getUrl())) {
                        delete = false;
                        break;//没删的不删文件
                    }
                }
            }
            if (delete) {
                toRemoves.add(oldAttachment);
            }
        }
        attachmentDao.deleteAttachments(ref_id, ref_type, ref_sub_type);
        int priority = 1;
        if (attachments != null) {
            for (Attachment attachment : attachments) {
                attachment.setRef_id(ref_id);
                attachment.setRef_type(ref_type);
                attachment.setRef_sub_type(ref_sub_type);
                attachment.setPriority(priority++);
                createAttachment(attachment);
            }
        }
        //最后删除实体文件
        for (Attachment attachment : toRemoves) {
            removeFile(attachment);
        }
    }

    public void deleteAttachments(Long ref_id, Integer ref_type, Integer ref_sub_type) {
        saveAttachments(ref_id, ref_type, ref_sub_type, null);
    }

    public Attachment getAttachment(Long ref_id, Integer ref_type, Integer ref_sub_type) {
        List<Attachment> attachments = getAttachments(ref_id, ref_type, ref_sub_type);
        return attachments == null || attachments.size() == 0 ? null : attachments.get(0);
    }

    public List<Attachment> getAttachments(Long ref_id, Integer ref_type, Integer ref_sub_type) {
        AttachmentQuery query = new AttachmentQuery();
        query.setRef_id(ref_id);
        query.setRef_type(ref_type);
        query.setRef_sub_type(ref_sub_type);
        query.setCountTotal(false);
        return findAttachment(query).getRows();
    }
}
