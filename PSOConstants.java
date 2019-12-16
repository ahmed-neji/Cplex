package org.hana.CPLEX;

/* author: Hana */

// this is an interface to keep the configuration for the PSO
// you can modify the value depends on your needs

public interface PSOConstants {
	
	
	static int m =5; //Nb_VM
	// int n = 5; // Nb_Tache au lieu de Problem_Dimension et changer au 4 au lieu de 2//hana
	double D=4500;// deadline (utlisé juste lorsque on execute Test_main_2)
	static double[] lag_vm = {1,1,1,1,1} ; // temps de démarraghe de VM
	//static int temps_reaff = 10 ; //add new
	static int c = 4; //caractéristiques de task
	
}
