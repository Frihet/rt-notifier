/**
 *  Project: rtnotifier
 *  Created: Jul 4, 2009
 *  Copyright: 2009, Reidar Øksnevad
 *
 *  This file is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published
 *  by the Free Software Foundation; version 3.
 */
package no.freecode.rtnotifier.xmpp;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

/**
 * @author Reidar Øksnevad (reidar.oksnevad@freecode.no)
 * 
 */
public class XmppManager {

    private String username;
    private String password;
    private String resource;
    private boolean invokeOnStartup;

    private XMPPConnection connection;
    private XmppChatAgent[] agents;

    
    /**
     * Connect to an XMPP (Jabber) server.
     * 
     * @throws XMPPException
     *             if it isn't able to connect, or if there is an error joining
     *             a chat room.
     */
    @PostConstruct
    public void connect() throws XMPPException {
        connection.connect();
        connection.login(getUsername(), getPassword(), getResource());
        System.out.println("Connected to XMPP server: " + connection.getUser());

        for (XmppChatAgent agent : getAgents()) {
            agent.joinChat();

            String greeting = System.getProperty("greeting");
            if (greeting != null) {
            	agent.sendMessage(greeting);
            }
        }
    }

    @PreDestroy
    public void disconnect() {
        System.out.println("Disconnecting.");
        this.connection.disconnect();
    }

    /**
     * Run whatever actions the agents are to do. This is called on a regular
     * basis, reading from RT and posting to the XMPP server.
     */
    public void invokeAgents() {
        for (XmppChatAgent agent : getAgents()) {
            agent.invoke();
        }
    }
    
    public XmppChatAgent[] getAgents() {
        return agents;
    }
    
    @Required
    @Autowired
    public void setAgents(XmppChatAgent[] agents) {
        this.agents = agents;
    }
    
    public String getUsername() {
        return username;
    }

    @Required
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    @Required
    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isInvokeOnStartup() {
        return invokeOnStartup;
    }

    public void setInvokeOnStartup(boolean invokeOnStartup) {
        this.invokeOnStartup = invokeOnStartup;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public XMPPConnection getConnection() {
        return connection;
    }

    @Required
    @Autowired
    public void setConnection(XMPPConnection connection) {
        this.connection = connection;
    }
}
