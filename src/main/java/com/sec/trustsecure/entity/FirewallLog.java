package com.sec.trustsecure.entity;
import jakarta.persistence.*;
@Entity
@Table(name = "firewall_logs")
public class FirewallLog {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	
	String date;
	String time;
	String action;
	String protocol;
	String sourceIp;
	String destIp;
	String sourcePort;
	String destPort;
		
	public FirewallLog(String date, String time, String action, String protocol, String sourceIp, String destIp,
			String parts, String parts2) {
		
		this.date = date;
		this.time = time;
		this.action = action;
		this.protocol = protocol;
		this.sourceIp = sourceIp;
		this.destIp = destIp;
		this.sourcePort = parts;
		this.destPort = parts2;
		
	}
	public FirewallLog() {}
}