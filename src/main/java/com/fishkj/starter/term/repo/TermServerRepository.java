package com.fishkj.starter.term.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fishkj.starter.term.pojo.TermServer;

public interface TermServerRepository extends JpaRepository<TermServer, String>{

}
