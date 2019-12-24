package com.fishkj.starter.term.socket;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Machine implements Serializable {

	private static final long serialVersionUID = 2267325419527164577L;
	
	private String id;
	private String serverName;
	private String hostName;
	private String userName;
	private String pwd;
	private Integer port;
}
