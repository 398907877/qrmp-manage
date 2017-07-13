package com.yanyan.persist.system.impl;

import com.yanyan.data.domain.system.Attachment;
import com.yanyan.persist.system.AttachmentDao;
import com.yanyan.core.db.NamedParameterJdbcDaoSupport;
import com.yanyan.core.db.NamedSqlBuilder;
import com.yanyan.core.db.ParameterBuilder;
import com.yanyan.core.db.ReflectiveRowMapperUtils;
import com.yanyan.core.lang.Page;
import com.yanyan.data.query.system.AttachmentQuery;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * User: Saintcy
 * Date: 2016/9/1
 * Time: 11:07
 */
@Repository
public class AttachmentDaoImpl extends NamedParameterJdbcDaoSupport implements AttachmentDao {

    private String INSERT_ATTACHMENT = "INSERT INTO s_attachment\n" +
            "  (id, NAME, url, type, size, ref_type, ref_sub_type, ref_id, create_staff_id,\n" +
            "   priority, create_time)\n" +
            "VALUES\n" +
            "  (:id, :name, :url, :type, :size, :ref_type, :ref_sub_type, :ref_id,\n" +
            "   :create_staff_id, :priority, :create_time)";
    private String UPDATE_ATTACHMENT =
            "UPDATE s_attachment\n" +
                    "   SET name = :name, url = :url, type = :type,  size = :size, ref_type = :ref_type,\n" +
                    "       ref_sub_type = :ref_sub_type, ref_id = :ref_id,\n" +
                    "       create_staff_id = :create_staff_id, priority = :priority,\n" +
                    "       update_time = :update_time\n" +
                    " WHERE id = :id";
    private String DELETE_ATTACHMENT = "DELETE FROM s_attachment WHERE id = :id";
    private String DELETE_ATTACHMENTS = "DELETE FROM s_attachment WHERE ref_id = :ref_id";
    private String GET_ATTACHMENT =
            "SELECT id, name, url, type, size, ref_type, ref_sub_type, ref_id, create_staff_id,\n" +
                    "       priority, create_time, update_time\n" +
                    "  FROM s_attachment a\n" +
                    " WHERE 1 = 1";

    public void insertAttachment(Attachment attachment) {
        Map<String, Object> parameters = new ParameterBuilder(attachment).create();
        this.getNamedParameterJdbcTemplate().update(INSERT_ATTACHMENT, parameters);
        attachment.setId(this.getNamedParameterJdbcTemplate().queryForObject("select @@IDENTITY", Long.class));
    }

    public void updateAttachment(Attachment attachment) {
        Map<String, Object> parameters = new ParameterBuilder(attachment).create();

        this.getNamedParameterJdbcTemplate().update(UPDATE_ATTACHMENT, parameters);
    }

    public void deleteAttachment(Long attachment_id) {
        Map<String, Object> parameters = new ParameterBuilder("id", attachment_id).create();
        this.getNamedParameterJdbcTemplate().update(DELETE_ATTACHMENT, parameters);
    }

    public void deleteAttachments(Long ref_id) {
        Map<String, Object> parameters = new ParameterBuilder("ref_id", ref_id).create();
        this.getNamedParameterJdbcTemplate().update(DELETE_ATTACHMENTS, parameters);
    }

    public void deleteAttachments(Long ref_id, Integer ref_type) {
        Map<String, Object> parameters = new ParameterBuilder("ref_id", ref_id).put("ref_type", ref_type).create();
        this.getNamedParameterJdbcTemplate().update(DELETE_ATTACHMENTS + " AND ref_type = :ref_type", parameters);
    }

    public void deleteAttachments(Long ref_id, Integer ref_type, Integer ref_sub_type) {
        Map<String, Object> parameters = new ParameterBuilder("ref_id", ref_id).put("ref_type", ref_type).put("ref_sub_type", ref_sub_type).create();
        this.getNamedParameterJdbcTemplate().update(DELETE_ATTACHMENTS + " AND ref_type = :ref_type AND ref_sub_type = :ref_sub_type", parameters);
    }

    public Attachment getAttachment(Long corp_id) {
        try {
            NamedSqlBuilder sqlBuilder = new NamedSqlBuilder(GET_ATTACHMENT);
            sqlBuilder.append(" and a.id = :id");
            Attachment attachment = this.getNamedParameterJdbcTemplate().queryForObject(sqlBuilder.toString(), new ParameterBuilder("id", corp_id).create(), ReflectiveRowMapperUtils.getRowMapper(Attachment.class));

            return attachment;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public Page<Attachment> findAttachment(AttachmentQuery query) {
        NamedSqlBuilder builder = new NamedSqlBuilder(GET_ATTACHMENT);
        if (query != null) {
            builder.appendIfNotEmpty(" and a.ref_id = :ref_id", query.getRef_id());
            builder.appendIfNotEmpty(" and a.ref_type = :ref_type", query.getRef_type());
            builder.appendIfNotEmpty(" and a.ref_sub_type = :ref_sub_type", query.getRef_sub_type());
        }
        builder.append(" order by a.priority, a.create_time desc");

        return this.getNamedParameterJdbcTemplate().queryForPage(builder.getSql(), new ParameterBuilder(query).create(), ReflectiveRowMapperUtils.getRowMapper(Attachment.class));
    }
}
