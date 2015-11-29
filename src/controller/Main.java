package controller;

import controller.NetworkManager;

public class Main {
	public static void main(String[] args) throws Exception {
		NetworkManager manager = new NetworkManager();
		manager.login("asdf", "asdf");
	}
}
