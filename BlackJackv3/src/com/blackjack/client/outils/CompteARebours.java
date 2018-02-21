package com.blackjack.client.outils;

import java.util.concurrent.TimeUnit;

public class CompteARebours {
	
	public void compte(int temps){
	for(int i= temps; i>=0;i--) {
			
			try {
				TimeUnit.SECONDS.sleep(1);
				System.out.print(i+" |");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}
	}
}
