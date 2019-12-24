package com.fishkj.starter.term.socket.sftp;

import java.io.Serializable;

import com.fishkj.starter.term.socket.sftp.enums.SftpTyue;

import lombok.Data;

@Data
public class SftpMessage implements Serializable {

	private static final long serialVersionUID = -7402973369300174223L;
	
	private SftpTyue tyue;
	
	private String dir;
	
	private String fileName;
	
	private String newName;
	
	private boolean directory;
}
