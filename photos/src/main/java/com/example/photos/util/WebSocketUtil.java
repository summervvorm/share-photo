package com.example.photos.util;

import com.alibaba.fastjson.JSON;
import com.example.photos.model.vo.NotificationVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @Auther: Arrow
 * @Date: 2023/7/4
 * @Description: com.example.photos.controller
 */
@Component
@Slf4j
@ServerEndpoint("/websocket/{userId}")
public class WebSocketUtil {

    /**
     * 线程安全的无序的集合
     */
    private static final CopyOnWriteArraySet<Session> SESSIONS = new CopyOnWriteArraySet<>();

    /**
     * 存储在线连接数
     */
    private static final Map<String, Session> SESSION_POOL = new HashMap<>();

    private static String userIds;

    @OnOpen
    public static void onOpen(Session session, @PathParam(value = "userId") String userId) {
        try {
            SESSIONS.add(session);
            SESSION_POOL.put(userId, session);
            log.info("【WebSocket消息】有新的连接，总数为：" + SESSIONS.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public static void onClose(Session session) {
        try {
            SESSIONS.remove(session);
            log.info("【WebSocket消息】连接断开，总数为：" + SESSIONS.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnMessage
    public static void onMessage(String message) {
//        Object o= JSON.parseObject(message);
        NotificationVO notificationVO=JSON.parseObject(message,NotificationVO.class);
        log.info("【WebSocket消息】收到客户端消息：" + notificationVO.toString());
        if(notificationVO.getType().equals("heartbeat")){
            sendOneMessage(userIds,new NotificationVO("heartbeat","ok"));
        }
    }

    /**
     * 此为广播消息
     *
     * @param message 消息
     */
    public static void sendAllMessage(String message) {
        log.info("【WebSocket消息】广播消息：" + message);
        for (Session session : SESSIONS) {
            try {
                if (session.isOpen()) {
                    session.getAsyncRemote().sendText(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 此为单点消息
     *
     * @param userId  用户编号
     * @param notificationVO 消息
     */
    public static void sendOneMessage(String userId, NotificationVO notificationVO) {
        userIds=userId;
        Session session = SESSION_POOL.get(userId);
        if (session != null && session.isOpen()) {
            try {
                synchronized (session) {
                    log.info("【WebSocket消息】单点消息：" + notificationVO.getData());
                    session.getAsyncRemote().sendText(JSON.toJSONString(notificationVO));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 此为单点消息(多人)
     *
     * @param userIds 用户编号列表
     * @param message 消息
     */
    public static void sendMoreMessage(String[] userIds, String message) {
        for (String userId : userIds) {
            Session session = SESSION_POOL.get(userId);
            if (session != null && session.isOpen()) {
                try {
                    log.info("【WebSocket消息】单点消息：" + message);
                    session.getAsyncRemote().sendText(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
