package com.fishkj.starter.term.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TermServer implements Serializable {

	private static final long serialVersionUID = 2267325419527164577L;
	
	@Id
    @GenericGenerator(name="guidGenerator", strategy = "com.fishkj.starter.term.jpa.UuidGenerator") 
    @GeneratedValue(generator="guidGenerator")
	@Column(length = 32, nullable=false)
	private String id;
	private String serverName;
	private String hostName;
	private String userName;
	private String pwd;
	private Integer port;
}