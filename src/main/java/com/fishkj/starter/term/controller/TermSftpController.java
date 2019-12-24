package com.fishkj.starter.term.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.fishkj.starter.term.manager.VelocityManager;
import com.fishkj.starter.term.socket.sftp.SftpClient;
import com.fishkj.starter.term.socket.sftp.SftpServer;
import com.fishkj.starter.term.utils.UUIDUtils;
import com.fishkj.starter.term.utils.Utils;

import lombok.RequiredArgsConstructor;

/**
 * sftp控制器
 * @date: 2019年12月25日 上午12:51:02
 * @author: jiuzhou.hu
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TermSftpController {
	
	private final VelocityManager velocityManager;
	
	@Value("${fish.term.resource:fish/term/resources}")
	private String resourcePath;
	
	/**
	 * sftp 控制台
	 * @author jiuzhou.hu
	 * @date 2019年12月25日 上午12:51:11
	 * @param id
	 * @return
	 */
	@GetMapping(value= "/term/sftp/{id}")
	public String sftp(@PathVariable(value="id") String id) {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("id", id);
		parameters.put("sid", UUIDUtils.createSystemDataPrimaryKey());
		String filePath = resourcePath + "/sftp.html";
		return velocityManager.parseVMContent(Utils.readFromResource(filePath), parameters);
	}
	
	/**
	 * 下载文件
	 * @author jiuzhou.hu
	 * @date 2019年12月25日 上午12:51:29
	 * @param sid
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	@GetMapping(value= "/term/sftp/download/{sid}/{fileName}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public byte[] download(@PathVariable(value="sid") String sid, @PathVariable(value="fileName") String fileName) throws IOException {
		Optional<SftpClient> opt = SftpServer.getSftpClient(sid);
		if(opt.isPresent()) {
			try {
				return opt.get().downloadFile(fileName);
			} catch (Exception e) {
				return "下载异常".getBytes();
			}
		} else {
			return "通道已关闭".getBytes();
		}
	}
}
