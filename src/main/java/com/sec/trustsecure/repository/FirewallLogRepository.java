package com.sec.trustsecure.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.sec.trustsecure.entity.FirewallLog;

public interface FirewallLogRepository extends JpaRepository<FirewallLog, Long> {
	
}