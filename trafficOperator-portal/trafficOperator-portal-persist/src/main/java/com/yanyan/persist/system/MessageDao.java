package com.yanyan.persist.system;


import com.yanyan.data.domain.system.Message;
import com.yanyan.core.lang.Page;
import com.yanyan.data.query.system.MessageQuery;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * 提醒存取控制对象
 * User: Saintcy
 * Date: 2015/8/21
 * Time: 23:10
 */
public interface MessageDao {

    void insertMessage(Message message);

    void updateMessage(Message message);

    void deleteMessage(Long id);

    void readMessage(@Param("id") Long id, @Param("read_time") Date read_time);

    Message getMessage(Long id);

    Page<Message> findMessage(MessageQuery query);
}
