/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blacklistvalidator;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.ws.Holder;

/**
 *
 * @author hcadavid
 */
public class HostBlackListsValidator {

	private static final int BLACK_LIST_ALARM_COUNT = 5;

	/**
	 * Check the given host's IP address in all the available black lists, and
	 * report it as NOT Trustworthy when such IP was reported in at least
	 * BLACK_LIST_ALARM_COUNT lists, or as Trustworthy in any other case. The search
	 * is not exhaustive: When the number of occurrences is equal to
	 * BLACK_LIST_ALARM_COUNT, the search is finished, the host reported as NOT
	 * Trustworthy, and the list of the five blacklists returned.
	 * 
	 * @param ipaddress
	 *            suspicious host's IP address.
	 * @return Blacklists numbers where the given host's IP address was found.
	 */
	synchronized public List<Integer> checkHost(String ipaddress, int n) {

		LinkedList<Integer> blackListOcurrences = new LinkedList<>();
		LinkedList<Threads> hilos = new LinkedList<Threads>();

		int ocurrencesCount = 0;
		int nSection = 1;

		HostBlacklistsDataSourceFacade skds = HostBlacklistsDataSourceFacade.getInstance();

		int listPerThread = skds.getRegisteredServersCount() / n;
		int complemento = skds.getRegisteredServersCount() - (listPerThread * n);
		boolean flag = (complemento != 0) ? true : false;

		// Fragmento de codigo hecho por Javier Vargas y Sebastian Goenaga

		if (complemento != 0) {
			flag = true;
		}

		Threads hilo = null;

		for (int i = 0; i < n; i++) {
			hilo = new Threads(i * listPerThread, (i + 1) * listPerThread, ipaddress, skds);
			hilo.start();
		}

		try {
			hilo.join();
		} catch (InterruptedException e) {
			System.out.println("hola");
			e.printStackTrace();
		}

		if (flag) {
			hilo = new Threads(listPerThread * n, listPerThread * n + complemento, ipaddress, skds);
			hilo.start();
		}

		try {
			hilo.join();
		} catch (InterruptedException e) {
			System.out.println("hola");
			e.printStackTrace();
		}

		if (Threads.count >= BLACK_LIST_ALARM_COUNT) {
			skds.reportAsNotTrustworthy(ipaddress);
		} else {
			skds.reportAsTrustworthy(ipaddress);
		}

		LOG.log(Level.INFO, "Checked Black Lists:{0} of {1}",
				new Object[] { Threads.checkedListsCount, skds.getRegisteredServersCount() });
		
		Collections.sort(Threads.blackListOcurrences);
		return Threads.blackListOcurrences;
	}

	private static final Logger LOG = Logger.getLogger(HostBlackListsValidator.class.getName());

}