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
	private String risk;
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
	 public String getRisk() {
	        return risk;
	    }
	 public String getField1() {
		    return date;
		}

		public String getField2() {
		    return time;
		}

		public String getField3() {
		    return action;
		}

		public String getField4() {
		    return protocol;
		}

		public String getField5() {
		    return sourceIp;
		}

		public String getField6() {
		    return destIp;
		}

		public String getField7() {
		    return sourcePort;
		}

		public String getField8() {
		    return destPort;
		}

		
	    public void setRisk(String risk) {
	        this.risk = risk;
	    }
	public FirewallLog() {}
}