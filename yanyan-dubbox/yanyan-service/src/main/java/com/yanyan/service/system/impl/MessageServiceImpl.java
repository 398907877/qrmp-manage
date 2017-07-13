package com.yanyan.service.system.impl;

import com.yanyan.core.lang.Page;
import com.yanyan.data.domain.system.Message;
import com.yanyan.data.query.system.MessageQuery;
import com.yanyan.persist.system.MessageDao;
import com.yanyan.service.BaseService;
import com.yanyan.service.system.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * User: Saintcy
 * Date: 2015/3/6
 * Time: 15:14
 */
@Service
public class MessageServiceImpl extends BaseService implements MessageService {
    @Autowired
    private MessageDao messageDao;

    public long send(Message message) {
        messageDao.insertMessage(message);

        return message.getId();
    }

    public void send(List<Message> messages) {
        if (messages == null) return;
        for (Message message : messages) {
            send(message);
        }
    }

    public void deleteMessage(Long id) {
        messageDao.deleteMessage(id);
    }

    public void readMessage(Long id) {
        messageDao.readMessage(id, new Date());
    }

    public Page<Message> findMessage(MessageQuery query) {
        return messageDao.findMessage(query);
    }
}
