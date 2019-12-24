package com.fishkj.starter.term.socket.sftp;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.fishkj.starter.term.service.MachineService;
import com.fishkj.starter.term.socket.Machine;
import com.fishkj.starter.term.socket.sftp.beans.SftpBean;
import com.fishkj.starter.term.spring.SpringApplicationContextUtil;
import com.fishkj.starter.term.utils.JsonUtils;

import lombok.extern.slf4j.Slf4j;

@Component
@ServerEndpoint(value="/term/sftp/{sid}/{id}")
@Slf4j
public class SftpServer {
	
	private MachineService machineService;

    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
     */
    private static CopyOnWriteArraySet<SftpServer> webSocketSet = new CopyOnWriteArraySet<>();
    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;
    
    private SftpClient sftpClient = null;
    
    private String sid;
    /**
     * 连接建立成功调用的方法
     **/
    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid, @PathParam("id") String id) {
        this.session = session;
        this.sid = sid;
        //加入set中
        webSocketSet.add(this);
        
        sftpClient = new SftpClient();
		
		try {
			if(machineService == null) {
				machineService = SpringApplicationContextUtil.getBean(MachineService.class);
			}
			Optional<Machine> opt = machineService.get(id);
			if(opt.isPresent()) {
				Machine machine = opt.get();
				if(log.isDebugEnabled()) {
					log.debug("Try to {} connect...", machine.getHostName());
				}
				//连接服务器
				if (sftpClient.connect(machine)) {
					SftpBean sftp = sftpClient.ls();
					sendMessage(JsonUtils.buildNormalBinder().toJson(sftp));
				} else {
					//取消连接
					sftpClient.disconnect();
					if(log.isDebugEnabled()) {
						log.debug("Connect {} failed, please confirm the username or password try agin.", machine.getHostName());
					}
					session.close();
				}
			} else {
				if(log.isDebugEnabled()) {
					log.debug("Connect {} failed, please confirm the username or password try agin.", id);
				}
				session.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
    	if(log.isErrorEnabled()) {
    		log.error("连接关闭");
    	}
        //从set中删除
        webSocketSet.remove(this);
        //关闭连接
  		if (sftpClient != null) {
  			sftpClient.disconnect();
  		}
    }
    /**
     * 收到客户端消息后调用的方法
     * @param message 客户端发送过来的消息
     * @throws IOException 
     **/
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
    	if(log.isDebugEnabled()) {
			log.debug("接收到消息：{}", message);
		}
    	if(StringUtils.hasText(message)) {
	    	//处理连接
			try {
				//当客户端不为空的情况
				if (sftpClient != null) {
					SftpMessage sftpMessage = JsonUtils.buildNormalBinder().getJsonToObject(message, SftpMessage.class);
					sftpMessage.getTyue().exec(session, sftpClient, sftpMessage);
				}
			} catch (Exception e) {
				e.printStackTrace();
				if(log.isErrorEnabled()) {
					log.error("An error occured, websocket is closed.");
				}
				session.close();
				if (sftpClient != null) {
		  			sftpClient.disconnect();
		  		}
			}
    	}
    }
    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误，error={}", error.getMessage());
      //从set中删除
        webSocketSet.remove(this);
        //关闭连接
  		if (sftpClient != null) {
  			sftpClient.disconnect();
  		}
    }
    
    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }
    
    public SftpClient getSftpClient() {
		return sftpClient;
	}
	public String getSid() {
		return sid;
	}
	public static Optional<SftpClient> getSftpClient(String sid) {
    	if(StringUtils.hasText(sid)) {
    		return webSocketSet.stream().filter(a -> a.getSid().equals(sid)).map(a -> a.getSftpClient()).findFirst();
    	} else {
    		return Optional.empty();
    	}
    }
}

