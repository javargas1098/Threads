package edu.eci.arsw.math;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.Scanner;

import org.junit.Test;

import edu.eci.arsw.blacklistvalidator.HostBlackListsValidator;
import edu.eci.arsw.blacklistvalidator.Threads;
import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;

public class HostBlackListsValidatorTest {

	@Test
	public void checkHostTest() {

		HostBlacklistsDataSourceFacade skds = HostBlacklistsDataSourceFacade.getInstance();

		String[] ips = { "200.24.34.55", "201.24.34.55", "202.24.34.55" };
		int[] nHilos = { 20, 200, 2000 };

		int listPerThread;
		int complemento;
		boolean flag;
		int n;
		String ipaddress;
		Threads hilo;

		for (int i = 0; i < ips.length; i++) {
			n = nHilos[i];
			ipaddress = ips[i];
			listPerThread = skds.getRegisteredServersCount() / n;
			complemento = skds.getRegisteredServersCount() - (listPerThread * n);
			flag = (complemento != 0) ? true : false;
			hilo = null;

			for (int i1 = 0; i1 < n; i1++) {
				hilo = new Threads(i1 * listPerThread, (i1 + 1) * listPerThread, ipaddress, skds);
				hilo.start();
			}

			if (flag) {
				Threads hilo2 = new Threads(listPerThread * n, listPerThread * n + complemento, ipaddress, skds);
				hilo2.start();
			}

			try {
				hilo.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Collections.sort(Threads.blackListOcurrences);
			String expected = Threads.blackListOcurrences.toString();
			Threads.resetStatics();
			HostBlackListsValidator validator = new HostBlackListsValidator();
			String result = validator.checkHost(ips[i], nHilos[i]).toString();
			assertEquals(expected, result);
			Threads.resetStatics();
		}

	}

}