package com.fishkj.starter.term.socket.sftp.beans;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SftpBean implements Serializable {

	private static final long serialVersionUID = -4831312017592136613L;
	
	private String currentCatalog;
	private List<SftpFileBean> files;
	
	
}
