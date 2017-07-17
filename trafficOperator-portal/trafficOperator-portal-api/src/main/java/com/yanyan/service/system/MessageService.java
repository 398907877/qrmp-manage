package com.yanyan.service.system;

import com.yanyan.data.domain.system.Message;
import com.yanyan.core.lang.Page;
import com.yanyan.data.query.system.MessageQuery;

import java.util.List;

/**
 * 提醒服务
 * User: Saintcy
 * Date: 2015/3/6
 * Time: 14:07
 */
public interface MessageService {
    /**
     * 提醒
     *
     * @param message
     */
    long send(Message message);

    /**
     * 批量提醒
     *
     * @param messages
     */
    void send(List<Message> messages);

    /**
     * 删除提醒
     *
     * @param id
     */
    void deleteMessage(Long id);

    /**
     * 阅读提醒
     *
     * @param id
     */
    void readMessage(Long id);

    /**
     * 查询提醒信息
     *
     * @param query
     * @return
     */
    Page<Message> findMessage(MessageQuery query);
}
