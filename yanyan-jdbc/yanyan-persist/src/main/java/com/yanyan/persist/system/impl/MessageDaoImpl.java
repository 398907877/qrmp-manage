package com.yanyan.persist.system.impl;

import com.yanyan.core.db.NamedParameterJdbcDaoSupport;
import com.yanyan.core.db.NamedSqlBuilder;
import com.yanyan.core.db.ParameterBuilder;
import com.yanyan.core.db.ReflectiveRowMapperUtils;
import com.yanyan.core.lang.Page;
import com.yanyan.data.domain.system.Message;
import com.yanyan.data.query.system.MessageQuery;
import com.yanyan.persist.system.MessageDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Map;

/**
 * 提醒存取控制对象
 * User: Saintcy
 * Date: 2015/8/21
 * Time: 23:10
 */
@Repository
public class MessageDaoImpl extends NamedParameterJdbcDaoSupport implements MessageDao {
    private static final String INSERT_MESSAGE =
            "INSERT INTO s_message\n" +
                    "  (id, business_code, from_staff_id, to_staff_id, content, post_time,\n" +
                    "   read_time, status, corp_id)\n" +
                    "VALUES\n" +
                    "  (:id, :business_code, :from_staff_id, :to_staff_id, :content, :post_time,\n" +
                    "   :read_time, :status, :corp_id)";


    private static final String UPDATE_MESSAGE =
            "UPDATE euc.s_message\n" +
                    "   SET business_code = :business_code, from_staff_id = :from_staff_id,\n" +
                    "       to_staff_id = :to_staff_id, content = :content,\n" +
                    "       post_time = :post_time, read_time = :read_time\n" +
                    " WHERE id = :id";

    private static final String READ_MESSAGE = "update s_message set read_time = :read_time where id = :id";
    private static final String DELETE_MESSAGE = "update s_message set status = 0 where id=:id";
    private static final String GET_MESSAGE_INFO =
            "SELECT id, business_code, business_id, from_staff_id,\n" +
                    "       (SELECT t.nickname FROM s_staff t WHERE t.id = a.from_staff_id) from_staff_name,\n" +
                    "       to_staff_id,\n" +
                    "       (SELECT t.nickname FROM s_staff t WHERE t.id = a.to_staff_id) to_staff_name,\n" +
                    "       content, post_time, read_time, status, corp_id\n" +
                    "  FROM s_message a" +
                    " where 1 = 1\n";


    public void insertMessage(Message message) {
        Map<String, Object> parameters = new ParameterBuilder(message).create();
        this.getNamedParameterJdbcTemplate().update(INSERT_MESSAGE, parameters);
        message.setId(this.getNamedParameterJdbcTemplate().queryForObject("select @@IDENTITY", Long.class));
    }

    public void updateMessage(Message message) {
        Map<String, Object> parameters = new ParameterBuilder(message).create();

        this.getNamedParameterJdbcTemplate().update(UPDATE_MESSAGE, parameters);
    }

    public void deleteMessage(Long id) {
        Map<String, Object> parameters = new ParameterBuilder("id", id).create();
        //删除通知信息
        this.getNamedParameterJdbcTemplate().update(DELETE_MESSAGE, parameters);
    }

    public void readMessage(Long id, Date read_time) {
        Map<String, Object> parameters = new ParameterBuilder("id", id).put("read_time", read_time).create();
        //标记通知为已读
        this.getNamedParameterJdbcTemplate().update(READ_MESSAGE, parameters);
    }


    public Message getMessage(Long msg_id) {
        try {
            NamedSqlBuilder sqlBuilder = new NamedSqlBuilder(GET_MESSAGE_INFO);
            sqlBuilder.append(" and a.id = :id");
            Message message = this.getNamedParameterJdbcTemplate().queryForObject(sqlBuilder.toString(), new ParameterBuilder("id", msg_id).create(), ReflectiveRowMapperUtils.getRowMapper(Message.class));

            return message;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public Page<Message> findMessage(MessageQuery query) {
        NamedSqlBuilder builder = new NamedSqlBuilder(GET_MESSAGE_INFO);
        if (query != null) {
            builder.appendIfNotEmpty(" and a.corp_id = :corp_id", query.getCorp_id());
            builder.appendIfNotEmpty(" and a.to_staff_id = :to_staff_id", query.getTo_staff_id());
            builder.appendIfNotEmpty(" and a.from_staff_id = :from_staff_id", query.getFrom_staff_id());
            if (query.getRead() != null) {
                builder.appendIfNotEmpty(" and a.read_time <> ''", query.getRead());
                builder.appendIfNotEmpty(" and a.read_time is null", !query.getRead());
            }
            builder.appendIfNotEmpty(" and a.post_time >= :post_time_min", query.getPost_time_min());
            builder.appendIfNotEmpty(" and a.post_time >= :post_time_max", query.getPost_time_max());
            builder.appendSetIfNotEmpty(" and a.status in (:status)", "status", StringUtils.split(query.getStatus(), ","), true);
            builder.appendIfNotEmpty(" and (a.title like concat('%', :keyword, '%') and a.content like concat('%', :keyword, '%'))", query.getKeyword());
        }
        builder.append(" order by a.post_time desc");

        return this.getNamedParameterJdbcTemplate().queryForPage(builder.getSql(), new ParameterBuilder(query).create(), ReflectiveRowMapperUtils.getRowMapper(Message.class));
    }
}
