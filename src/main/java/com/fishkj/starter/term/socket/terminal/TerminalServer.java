package com.fishkj.starter.term.socket.terminal;

import java.io.IOException;
import java.util.Arrays;
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

import com.fishkj.starter.term.service.MachineService;
import com.fishkj.starter.term.socket.Machine;
import com.fishkj.starter.term.spring.SpringApplicationContextUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * shell websocket 控制器
 * @date: 2019年12月25日 上午12:38:12
 * @author: jiuzhou.hu
 */
@Component
@ServerEndpoint(value="/term/terminal/{id}")
@Slf4j
public class TerminalServer {
	
	private MachineService machineService;

    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
     */
    private static CopyOnWriteArraySet<TerminalServer> webSocketSet = new CopyOnWriteArraySet<>();
    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;
    
    private SshClient sshClient = null;
    /**
     * 连接建立成功调用的方法
     **/
    @OnOpen
    public void onOpen(Session session, @PathParam("id") String id) {
        this.session = session;
        //加入set中
        webSocketSet.add(this);
        
        sshClient = new SshClient();
		
		try {
			if(machineService == null) {
				machineService = SpringApplicationContextUtil.getBean(MachineService.class);
			}
			Optional<Machine> opt = machineService.get(id);
			if(opt.isPresent()) {
				sendMessage("Try to connect...\r");
				//连接服务器
				if (sshClient.connect(opt.get())) {
					//开启线程，用于写数据到服务器上的
					sshClient.startShellOutPutTask(session);
				}else {
					//取消连接
					sshClient.disconnect();
					sendMessage("Connect failed, please confirm the username or password try agin.");
					session.close();
				}
			} else {
				sendMessage("Connect failed, please confirm the username or password try agin.");
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
  		if (sshClient != null) {
  			sshClient.disconnect();
  		}
    }
    /**
     * 收到客户端消息后调用的方法
     * @param message 客户端发送过来的消息
     * @throws IOException 
     **/
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
    	/*if(log.isDebugEnabled()) {
    		log.debug("收到消息 {}", message);
    	}*/
    	
    	if("keepalive".equals(message)) {
    		return;
    	}
    	
    	//处理连接
		try {
			//当客户端不为空的情况
			if (sshClient != null) {
				
				//receive a close cmd ?
				if (Arrays.equals("exit".getBytes(), message.getBytes())) {
					
					if (sshClient != null) {
						sshClient.disconnect();
					}
					
					session.close();
					return ;
				}
				//写入前台传递过来的命令，发送到目标服务器上
				sshClient.write(new String(message.getBytes(), "UTF-8"));
			}
		} catch (Exception e) {
			sendMessage("An error occured, websocket is closed.");
			session.close();
			if (sshClient != null) {
				sshClient.disconnect();
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
  		if (sshClient != null) {
  			sshClient.disconnect();
  		}
    }
    
    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }
    /**
     * 群发自定义消息
     * */
    /*public static void sendInfo(String message,@PathParam("sid") String sid) throws IOException {
        log.info("推送消息到窗口"+sid+"，推送内容:"+message);
        for (WebSocketServer item : webSocketSet) {
            try {
                //这里可以设定只推送给这个sid的，为null则全部推送
                if(sid==null) {
                    item.sendMessage(message);
                }else if(item.sid.equals(sid)){
                    item.sendMessage(message);
                }
            } catch (IOException e) {
                continue;
            }
        }
    }*/
}

