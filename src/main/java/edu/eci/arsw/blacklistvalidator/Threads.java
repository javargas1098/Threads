package edu.eci.arsw.blacklistvalidator;

import java.awt.List;
import java.net.URL;
import java.util.LinkedList;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;

public class Threads extends Thread {

	private static final int BLACK_LIST_ALARM_COUNT = 5;
	public static int count = 0;
	public static int checkedListsCount = 0;
	public static LinkedList<Integer> blackListOcurrences = new LinkedList<>();

	public static boolean flag = true;

	private int n1;
	private int n2;
	private String ipaddress;
	private HostBlacklistsDataSourceFacade skds;

	public Threads(int n1, int n2, String ipaddress, HostBlacklistsDataSourceFacade skds) {
		this.n1 = n1;
		this.n2 = n2;
		this.ipaddress = ipaddress;
		this.skds = skds;

	}

	@Override
	public void run() {
		for (int i = n1; i < n2 && count < BLACK_LIST_ALARM_COUNT; i++) {

			if (skds.isInBlackListServer(i, ipaddress)) {
				count++;
				blackListOcurrences.add(i);
			}
		}

		if (count >= BLACK_LIST_ALARM_COUNT) {

			flag = false;
		}

	}

}