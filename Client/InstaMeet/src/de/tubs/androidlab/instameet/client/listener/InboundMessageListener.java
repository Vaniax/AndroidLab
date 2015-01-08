package de.tubs.androidlab.instameet.client.listener;

import java.util.EventListener;

public interface InboundMessageListener extends EventListener {
	void securityToken(String token);
}
