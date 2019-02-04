package edu.eci.arsw.blacklistvalidator;

import java.util.LinkedList;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;

public class Threads extends Thread {

	private static final int BLACK_LIST_ALARM_COUNT = 5;
	public static Integer count = 0;
	public static Integer checkedListsCount = 0;
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
	synchronized public void run() {
		for (int i = n1; i < n2 && count < BLACK_LIST_ALARM_COUNT; i++) {

			checkedListsCount++;
			if (skds.isInBlackListServer(i, ipaddress)) {

				count++;
				blackListOcurrences.add(i);

			}
		}

		if (count >= BLACK_LIST_ALARM_COUNT) {

			flag = false;
		}

	}

	public static void resetStatics() {
		// TODO Auto-generated method stub
		count = 0;
		checkedListsCount = 0;
		blackListOcurrences = new LinkedList<>();

	}

}