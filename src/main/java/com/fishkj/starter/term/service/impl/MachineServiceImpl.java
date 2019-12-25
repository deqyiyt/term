package com.fishkj.starter.term.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.fishkj.starter.term.pojo.TermServer;
import com.fishkj.starter.term.repo.TermServerRepository;
import com.fishkj.starter.term.service.MachineService;
import com.fishkj.starter.term.socket.Machine;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MachineServiceImpl implements MachineService {
	
	private final TermServerRepository repository;
	
	@Override
	public List<Machine> list() {
		List<TermServer> items = repository.findAll();
		if(items != null && !items.isEmpty()) {
			return items.stream().map(server -> {
				return Machine.builder()
				.id(server.getId())
				.serverName(server.getServerName())
				.hostName(server.getHostName())
				.userName(server.getUserName())
				.pwd(server.getPwd())
				.port(server.getPort())
				.remark(server.getRemark())
				.build();
			}).collect(Collectors.toList());
		} else {
			return new ArrayList<>();
		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void saveOrUpdate(Machine machine) {
		if(machine != null) {
			repository.save(TermServer.builder()
				.id(machine.getId())
				.serverName(machine.getServerName())
				.hostName(machine.getHostName())
				.userName(machine.getUserName())
				.pwd(machine.getPwd())
				.port(machine.getPort())
				.remark(machine.getRemark())
				.build());
		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void remove(String id) {
		if(!StringUtils.isEmpty(id)) {
			repository.deleteInBatch(Arrays.stream(id.split(",")).map(a -> TermServer.builder().id(a).build()).collect(Collectors.toList()));
		}
	}

	@Override
	public Optional<Machine> get(String id) {
		if(StringUtils.isEmpty(id)) {
			return Optional.empty();
		}
		Optional<TermServer> optional = repository.findById(id);
		if(optional.isPresent()) {
			TermServer server = optional.get();
			return Optional.of(Machine.builder()
					.id(server.getId())
					.serverName(server.getServerName())
					.hostName(server.getHostName())
					.userName(server.getUserName())
					.pwd(server.getPwd())
					.port(server.getPort())
					.remark(server.getRemark())
					.build());
		} else {
			return Optional.empty();
		}
	}
}
