/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.threads;

/**
 *
 * @author hcadavid
 */
public class CountThread extends Thread {
	private int a;
	private int b;

	public CountThread(int n1, int n2) {
		this.a = n1;
		this.b = n2;
	}

	@Override
	public void run() {
		for (int i = a; i < b; i++) {
			System.out.println(i);

		}

	}
}
