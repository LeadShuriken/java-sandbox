package server;

import common.ClientIF;

public class UserPOJO {

	public String name;
	public ClientIF client;

	public UserPOJO(String name, ClientIF client) {
		this.name = name;
		this.client = client;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setClient(ClientIF client) {
		this.client = client;
	}

	public String getName() {
		return name;
	}

	public ClientIF getClient() {
		return client;
	}
	
	@Override
	public String toString() {
		return "ClientPOJO [name=" + name + ", client=" + client + "]";
	}
}
