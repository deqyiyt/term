package com.fishkj.starter.term.socket.terminal;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import javax.websocket.Session;

import org.springframework.util.StringUtils;

import com.fishkj.starter.term.socket.Machine;

import ch.ethz.ssh2.Connection;
import lombok.extern.slf4j.Slf4j;

/**
 * shell 控制台
 * @date: 2019年12月25日 上午12:39:44
 * @author: jiuzhou.hu
 */
@Slf4j
public class SshClient {
	
	private Connection conn;
	private ch.ethz.ssh2.Session sess;
	private InputStream in;
	private OutputStream out;
	private BufferedWriter inToShell;
	
	public boolean connect(Machine machine) {
		try {
			conn = new Connection(machine.getHostName(), machine.getPort());
			conn.connect();
			if(!StringUtils.isEmpty(machine.getPwd())) {
				if (!conn.authenticateWithPassword(machine.getUserName(), machine.getPwd())) {
					return false;
				}
			}
			sess = conn.openSession();
			sess.requestPTY("xterm", 90, 30, 0, 0, null);
			sess.startShell();
			in = sess.getStdout();
			out = sess.getStdin();
			inToShell = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
		} catch (Exception e) {
			if(log.isErrorEnabled()) {
				log.error("连接关闭，user={}，id={}，host={}，e={}", machine.getUserId(), machine.getId(), machine.getHostName(), e.getMessage());
			}
			return false;
		}
		return true;
	}
	
	/**
	 * 写命令到服务
	 * @param text
	 * @throws IOException
	 */
	public void write(String text) throws IOException {
		if (inToShell != null) {
			//写命令到服务器
			inToShell.write(text);
			//刷到服务器上
			inToShell.flush();
		}
	}
	
	public void startShellOutPutTask(Session session) {
		new ShellOutPutTask(session, in).start();
	}
	
	public void disconnect() {
		if (conn != null)
			conn.close();
		if (sess != null)
			sess.close();
		conn = null;
		sess = null;
	}
}
