package org.hana.CPLEX;


import hana.Interface_WK_2;
import hana.Read_XML_Rules;
import hana_ev.Read_XMl_Evaluation;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

// this is a driver class to execute the PSO process

public class Test_main_cplex implements PSOConstants {
	
	    static int n;
	//    static double[] lag_vm = {1,1,1} ; // temps de démarrage de VM
	    public static double[][] ET;//Le temps d'execution est généré par Cloudsim
	//	Dependance_tacheconteint uniquement des 0 ou 1 çàd s'il y a des dépendances entre les tâches
		//public static double[][] Dependance_tache ={{0,1,1,0},{0,0,0,1},{0,0,0,1},{0,0,0,0}};
		//static int[][] caract_cloudlet= {{3679539 , 3109 , 2214 , 1},{1893577 , 3601, 3902,1},{3872058,4432 ,4548,1},{2047369,2189 ,3081,1}}; 
		public static double[][] TET ;
		public static double[][] DT;//Le temps de transfert est généré par Cloudsim
		static double[] vm = new double [m];
		static double [] SET;
		static double [] FET;
	//	static double [] Location = {1,0,2,2};
		static double[] UC_VM;	

		static double [][] dependance_task ;
		static int[][] caracters_task ;
		static String Log_map;
		static String  Log_map_reaff;
		static double result_total_cost;
	    static double  cout_total_Tache_execute;
	    static double  cout_total_Tache_execute_sum;
	    static double  cout_total_Tache_restante;
	    static double  cout_total_après_reaffectation;
		static String task_consolid;
		static double result_final;    
//		static  int[][] Caract_task_Ap_Ajout;
//		static double[][] Dependance_task_Ap_Ajout;
///		static double [][] Dependance_task_restante;
//		static int [][] Carcters_task_restante;
//		static double[][] ET2 ; //--FF matrice qui contient le temps d'ex sur les # types de VMs
//	 	static double[][] DT2 ; //--FF matrice qui contient le temps de transfert entre les # tâches
	    static Vector<Integer> Tache_restante = new Vector<Integer>();
	    static	Vector<Integer> Tache_execute = new Vector<Integer>();

	 	static int nb_ap_ajout;
	 	static double new_D;
	 	static int nb_tache_res;
	 	
	 	static double[] SE_actuel ;// pour récupérer les SE des tâches exécutées
	 	static String SE_actuel__ ="SBT Of executed Tasks :\n";
		static double [] FE_actuel;// pour récupérer les FE des tâches exécutées
	 	static String FE_actuel__ ="FBT Of executed Tasks :\n";

	 	static String type;
	

		static ArrayList<String> list_of_value_action = new ArrayList<>();
		static int nb_exception;
       static double moy_cost;
       static double result_final_exec;
	    
       static double moy_cost_reaf;
       static double result_final_reaf;
       static double prcent_cost_dynamique;
       static String value_action;
       
       
   	   static double moy_mapp;
   	   static int som_mapp =0;		
   	   static double overhead_pcent;
   	   static double temps_reaffec;


   

		public static void main(String args[]) throws SAXException, IOException, ParserConfigurationException {
		// ajouter par ahmed ***********************************************************************
			// TET --------------------------
			double[][]TET_1=new double[9][5];
	        
			 TET_1[0][0]=50;  TET_1[0][1]=41;   TET_1[0][2]=34.3;  TET_1[0][3]=28.5;   TET_1[0][4]=25;
			 
			 TET_1[1][0]=27;  TET_1[1][1]=22;   TET_1[1][2]=18.6;  TET_1[1][3]=14.28;   TET_1[1][4]=12.5;
			 
			 TET_1[2][0]=52.7;  TET_1[2][1]=43.3;   TET_1[2][2]=36.3;  TET_1[2][3]=30.3;   TET_1[2][4]=26.5;
			 
			 TET_1[3][0]=42.8;  TET_1[3][1]=34.4;   TET_1[3][2]=28.9;  TET_1[3][3]=23.9;   TET_1[3][4]=21;
			 
			TET_1[4][0]=29.2;  TET_1[4][1]=23.7;   TET_1[4][2]=20.01;  TET_1[4][3]=16;   TET_1[4][4]=14;
			 
			 TET_1[5][0]=4.1;  TET_1[5][1]=3.5;   TET_1[5][2]=2;  TET_1[5][3]=1.91;   TET_1[5][4]=1.4;
			 
			 TET_1[6][0]=10.7;  TET_1[6][1]=8.8;   TET_1[6][2]=7.5;  TET_1[6][3]=6;   TET_1[6][4]=5;
			 
			 TET_1[7][0]=35.8;  TET_1[7][1]=28.8;   TET_1[7][2]=24.4;  TET_1[7][3]=22;   TET_1[7][4]=20;
			 
			 TET_1[8][0]=55;  TET_1[8][1]=50;   TET_1[8][2]=46;  TET_1[8][3]=43.5;   TET_1[8][4]=38.1;
			
			// DT ------------------------------------------------------
			 double [][] DT = new double[9][9]; 
	 	    	for (int i=0; i<n ; i++) {
		    	for (int j=0; j<n; j++) {
		    		if( dependance_task[i][j]!= 0 ) 
		    		{	
		    			DT[i][j] = (double)(caracters_task[i][2]/60)/(100);
		    		}
	    	 	}
		   }
			
	 	    	
	 	    	// caracteristic task ------------------------------------------------
	 	    	double[][]car=new double[9][4];
	 	        
	 			 car[0][0]=3600000;  car[0][1]=6000;   car[0][2]=6000;  car[0][3]=6;
	 			 
	 			 car[1][0]=1800000;  car[1][1]=1000;   car[1][2]=1000;  car[1][3]=2;
	 			 
	 			 car[2][0]=3800000;  car[2][1]=4000;   car[2][2]=4000;  car[2][3]=4;
	 			 
	 			 car[3][0]=3000000;  car[3][1]=3000;   car[3][2]=3000;  car[3][3]=3;
	 			 
	 			 car[4][0]=2000000;  car[4][1]=2000;   car[4][2]=2000;  car[4][3]=2;
	 			 
	 			 car[5][0]=200000;  car[5][1]=200;   car[5][2]=200;  car[5][3]=1;
	 			 
	 			 car[6][0]=700000;  car[6][1]=700;   car[6][2]=700;  car[6][3]=1;
	 			 
	 			 car[7][0]=2500000;  car[7][1]=2500;   car[7][2]=2500;  car[7][3]=2;
	 			 
	 			 car[8][0]=5400000;  car[8][1]=5000;   car[8][2]=5000;  car[8][3]=5;
			
	 			 // dependance task ----------------------------------------------------------
	 			 double[][]dep=new double[9][9];
	 	        
	 			 dep[0][0]=0;    dep[0][1]=6000;    dep[0][2]=0;   dep[0][3]=0;    dep[0][4]=0;      dep[0][5]=0;   dep[0][6]=0;  dep[0][7]=0;   dep[0][8]=0; 
	 			 
	 			 dep[1][0]=0;    dep[1][1]=0;    dep[1][2]=1000;   dep[1][3]=1000;   dep[1][4]=0;    dep[1][5]=0;   dep[1][6]=0;  dep[1][7]=0;   dep[1][8]=0;
	 			 
	 			 dep[2][0]=0;  dep[2][1]=0;  dep[2][2]=0;   dep[2][3]=0;    dep[2][4]=0;    dep[2][5]=0;   dep[2][6]=0;  dep[2][7]=0;   dep[2][8]=4000;
	 			 
	 			 dep[3][0]=0;  dep[3][1]=0;  dep[3][2]=0;   dep[3][3]=0;    dep[3][4]=3000;      dep[3][5]=0;   dep[3][6]=0;  dep[3][7]=0;   dep[3][8]=0;
	 			 
	 			 dep[4][0]=0;  dep[3][1]=0;  dep[3][2]=0;  dep[3][3]=0;      dep[3][4]=0;      dep[4][5]=2000;   dep[4][6]=0;  dep[4][7]=0;   dep[4][8]=0;
	 			 
	 			 dep[5][0]=0;   dep[5][1]=0;   dep[5][2]=0;      dep[5][3]=0;    dep[5][4]=0;     dep[5][5]=0;   dep[5][6]=200;  dep[5][7]=0;   dep[5][8]=0;
	 			 
	 			 dep[6][0]=0;  dep[6][1]=0;   dep[6][2]=0;    dep[6][3]=0;       dep[6][4]=0;       dep[6][5]=0;   dep[6][6]=0;  dep[6][7]=700;   dep[6][8]=0;
	 			 
	 			 dep[7][0]=0;  dep[7][1]=0;  dep[7][2]=0;   dep[7][3]=0;      dep[7][4]=0;      dep[7][5]=0;   dep[7][6]=0;  dep[7][7]=0;   dep[7][8]=2500;
	 			 
	 			 dep[8][0]=0;    dep[8][1]=0;    dep[8][2]=0;     dep[8][3]=0;    dep[8][4]=0;    dep[8][5]=0;   dep[8][6]=0;  dep[8][7]=0;   dep[8][8]=0;
	 			 
	 			 // ET ---------------------------------------------------------------------------------
	 			 double[][]ET_1=new double[9][5];
	 	        
	 			 ET_1[0][0]=50;  ET_1[0][1]=40;   ET_1[0][2]=33.3;  ET_1[0][3]=28.5;   ET_1[0][4]=25;
	 			 
	 			 ET_1[1][0]=25;  ET_1[1][1]=20;   ET_1[1][2]=16.6;  ET_1[1][3]=13.28;   ET_1[1][4]=11.5;
	 			 
	 			 ET_1[2][0]=51;  ET_1[2][1]=42.3;   ET_1[2][2]=35.19;  ET_1[2][3]=30.16;   ET_1[2][4]=26.39;
	 			 
	 			 ET_1[3][0]=41.67;  ET_1[3][1]=33.3;   ET_1[3][2]=27.78;  ET_1[3][3]=23.81;   ET_1[3][4]=20;
	 			 
	 			ET_1[4][0]=27.78;  ET_1[4][1]=22.22;   ET_1[4][2]=18.52;  ET_1[4][3]=15;   ET_1[4][4]=13;
	 			 
	 			 ET_1[5][0]=2.78;  ET_1[5][1]=2.22;   ET_1[5][2]=1.8;  ET_1[5][3]=1.59;   ET_1[5][4]=1.3;
	 			 
	 			 ET_1[6][0]=9.72;  ET_1[6][1]=7.78;   ET_1[6][2]=6.48;  ET_1[6][3]=5;   ET_1[6][4]=4;
	 			 
	 			 ET_1[7][0]=34.72;  ET_1[7][1]=27.78;   ET_1[7][2]=23.15;  ET_1[7][3]=21;   ET_1[7][4]=19;
	 			 
	 			 ET_1[8][0]=53;  ET_1[8][1]=48;   ET_1[8][2]=44;  ET_1[8][3]=42.86;   ET_1[8][4]=37.5;
	 			 
	 			 //UC -------------------------------------------------------------------------------
	 			double[] uc= new double [5];
	 			 uc[0]=0.036;
	 			 uc[1]=0.046;
	 			 uc[2]=0.056;
	 			 uc[3]=0.1;
	 			 uc[4]=0.2;
			
			//fin **********************************************************************************
			String name_WF_file = "Montage_4T.xml";
			String name_rule_File = "Rule_Montage_4.xml";

			
			//Read_XML_Matrices r = new Read_XML_Matrices();
			Read_XMl_Evaluation r = new Read_XMl_Evaluation();
			
			n = r.NbTask(name_WF_file);
			
	 //  System.out.println("**********Affichage de caractéristque Task***************");

		
		//int[][] caracters_task_ = new int [n][c];
	
	 	//caracters_task_=	r.caracteristique_task(name_WF_file);
 
    	
    	//-----------------------------Affichage de la matrice : dependance_task--------------------//
    //	System.out.print("***********Affichage de dependance_Task****************\n");

    	    //double[][] dependance_task_ = new double [n][n];

	    	//dependance_task_=	r.dependance_task(name_WF_file);

	    //	System.out.println("****************Affectation des Tasks before maj**************");

		
		//---------Générer estimations de DT et ET --------------------------//
		//CloudSim_generer_estimation c1= new CloudSim_generer_estimation(dependance_task_, caracters_task_);
		//c1.generer_estimation(n,m, caracters_task_, dependance_task_);
		//ET = c1.Calcul_ET(n,m,c1.newList);//--FF  Calcul de temps d'exécution des tâches sur les # types de VMs
		//DT = c1.Calcul_DT(n,c1.newList,c1.vmlist, dependance_task_ );
		//UC_VM= c1.c_1_vm;
		//---------------------------------------------------------------------	
		
		
		Mapping_Task mt = new Mapping_Task();
		
		//******************Affichage de la matrice de TET*****************//
		// TET = mt.calcul_Total_ex_time(DT, ET, lag_vm, n, m);
		 /*double[][] TET_1 = new double [4][5];
		 TET_1[0][0]=2541;  TET_1[0][1]=332;   TET_1[0][2]=178;  TET_1[0][3]=121;   TET_1[0][4]=92;
		 
		 TET_1[1][0]=5324;  TET_1[1][1]=-1;   TET_1[1][2]=-1;  TET_1[1][3]=914;   TET_1[1][4]=861;
		 
		 TET_1[2][0]=3773;  TET_1[2][1]=1095;   TET_1[2][2]=908;  TET_1[2][3]=840;   TET_1[2][4]=805;
		 
		 TET_1[3][0]=5221;  TET_1[3][1]=1289;   TET_1[3][2]=1012;  TET_1[3][3]=911;   TET_1[3][4]=859;
		 
		/* TET_1[4][0]=5221;  TET_1[3][1]=1289;   TET_1[3][2]=1012;  TET_1[3][3]=911;   TET_1[3][4]=859;
		 
		 TET_1[5][0]=5221;  TET_1[3][1]=1289;   TET_1[3][2]=1012;  TET_1[3][3]=911;   TET_1[3][4]=859;
		 
		 TET_1[6][0]=5221;  TET_1[3][1]=1289;   TET_1[3][2]=1012;  TET_1[3][3]=911;   TET_1[3][4]=859;
		 
		 TET_1[7][0]=5221;  TET_1[3][1]=1289;   TET_1[3][2]=1012;  TET_1[3][3]=911;   TET_1[3][4]=859;
		 
		 TET_1[8][0]=5221;  TET_1[3][1]=1289;   TET_1[3][2]=1012;  TET_1[3][3]=911;   TET_1[3][4]=859;*/
		 

		 
	/*	 System.out.println("***********Affichage de TET**********************");
         for (int i1=0; i1<n; i1++) {
		         for (int j=0; j<m; j++) {
		         System.out.print(TET[i1][j] +"  ");
			//	System.out.println("--------TET["+i1+"]["+j+"]------------ :"+TET[i1][j] +"  ");//affichage de TET
			//		System.out.println("Nombre de VM de TET["+ i1+"]["+ j+"] ======= "+ mt.calcul_Nb_VM(TET[i1][j]));  
					
		        }//fin "j"
		         System.out.println();
		  
		   } // fin for boucle "i"
      */   
        //******************************************Affectation sans exception***************************// 
        
         CplexProcess p = new CplexProcess();

  int nb_task=9;
  int dedline =200;
   
          	long Start_Mapping = System.currentTimeMillis(); 
          	System.out.println("Start_Mapping =="+Start_Mapping);

             //-----appel de la méthode qui utilise CPLEX------
             p.execute(TET_1,nb_task, DT, ET_1,uc,dedline);
             System.out.println("--------------Après fin  p.execute--------------");
             //Calculer le temps de l'exécution de l'algorithme ---------------------------
       //      long End_Mapping = System.currentTimeMillis(); 
       //      System.out.println("End_Mapping =="+End_Mapping);
             
      //       long Time_Mapping = End_Mapping - Start_Mapping; ////----- durée de Mapping
     //        System.out.println("Time_Mapping =="+Time_Mapping);
             
         
             
   
                
   
   
       


 //    result_total_cost = cons.getResult_total_cost();// pour avoir cout total avant consolidation
  //     task_consolid= cons.getTask_consolid(); // pour avoir les tâches consolidées 
  //     result_final= cons.getResult_final(); //pour avoir cout après consolidation
       
//       result_final_exec = result_final_exec + result_final; 
//       moy_cost =  result_final_exec / 9;
//       System.out.println("moy_cost " +moy_cost);
       
    //  }
  //AA   Log_map =
  //SS   Log_map = p.getLog_map(); // pour avoir les best localités des tasks de la meilleure solution
  //AA   System.out.println("Log_map"+Log_map);
         
       
      // Consolidation consolid = new Consolidation();
  //     for (int i=0; i<n ;i++) {//pour avoir les SE et FE des tâcehs exécutées dans le cas normal (pas d'excpetion)
 //      SE_actuel = cons.getSBT_SE();
 //      FE_actuel = cons.getFBT_FE();
       //System.out.println("SE_actuel["+i+"]=="+SE_actuel[i]);
	   //System.out.println("FE_actuel["+i+"]=="+FE_actuel[i]);
       
 //      SE_actuel__ =SE_actuel__ + " SBT["+i+"] == " + SE_actuel[i];
//   FE_actuel__ =FE_actuel__ +" FBT["+i+"] == " + FE_actuel[i];
	   //System.out.println("SE_actuel__ ==" +SE_actuel__);
	   //System.out.println("FE_actuel__ ==" +FE_actuel__);
       
 //      } // fin "for i=0"
       
    //   System.out.println("*******************Avant Reaffecation_Task**************");
  // 		Reaffecation_Task rf = new Reaffecation_Task();  
  //      rf.generer_exception(TET,n, DT ,ET,UC_VM,D,name_WF_file,name_rule_File); //en cas d'exception
  //      exception = rf.getException_num();
		
  
        

        
  //      type = rf.getType(); // pour avoir type de l'exception (Add /Delete /Sibstitute ...)
 //   	
 //   	position_exception = rf.getPosition_exception(); // pour avoir tâche qui a une exception
    	

//
//    	Read_XML_Rules rl = new Read_XML_Rules();
 // 	   list_of_value_action = rl.getList_of_value_action();

//         for (int i=0; i<list_of_value_action.size() ;i++){
 //        	type = list_of_value_action.get(i);
      //   	 System.out.println("type main "+type);

 //        }
 //    	value_action = rf.getValue_action();
//   	System.out.println("value_action ="+value_action);
//   	  System.out.println("*****************************Final Cost Calculation********************************\n");

      
   //     cout_total_Tache_execute = rf.getCout_total_Tache_execute_sum();
      	
  //      cout_total_Tache_restante = rf.getCout_total_Tache_restante();

        // 	  System.out.println("Sum Costs of Exceuted Tasks  ==" +cout_total_Tache_execute);
        //  	  System.out.println("Total Cost of Rest Tasks  ==" +cout_total_Tache_restante);

  //     	   cout_total_après_reaffectation = cout_total_Tache_execute + cout_total_Tache_restante ;
        //      System.out.println("Total Cost After Reaffectation  ==" +cout_total_après_reaffectation);
       	   
 //      	   System.out.println("moy_mapp =="+moy_mapp);
       
 //         	   temps_reaffec = rf.getTemps_reaffec(); // pour récupérer la somme des moyennes de reaffectation
      	//       System.out.println("temps_reaffec_main ="+temps_reaffec);
//          	   System.out.println("temps_reaffec ======"+ temps_reaffec);
      	      
  //        	   overhead_pcent = (temps_reaffec * 100) / moy_mapp; // pour calculer la fonction overhead
  //    	       System.out.println("overhead_pcent ="+overhead_pcent);
   	
     	
    	   
  //   	   temps_reaffec = rf.getTemps_reaffec(); // pour récupérer la somme des moyennes de reaffectation
//       System.out.println("temps_reaffec_main ="+temps_reaffec);
	       
//	       System.out.println("moy_mapp ="+moy_mapp); // pour récupérer le res de mappage (optimal execution)
//	       
//    	   double res_our_algor = moy_mapp + temps_reaffec; // pour récupérer la somme de reaffectation + affectation resultat de (our algorithm)
//	       System.out.println("res_our_algor ="+res_our_algor);
//
//	       
//	       overhead_pcent = (temps_reaffec * 100) / moy_mapp; // pour calculer la fonction overhead
//	       System.out.println("overhead_pcent ="+overhead_pcent);
//    	   
//		}// fin "if" exception>=1

        
        
        
        
        
        
        
        
        
        


} // fin driver
}