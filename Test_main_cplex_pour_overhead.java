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

public class Test_main_cplex_pour_overhead implements PSOConstants {
	
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

//	 	static double []new_SBT ;// pour récupérer les SE des tâches reaffectées(pour tous Workflow)
//	 	static String new_SBT__ ="SBT Of reaffected Tasks :\n";
//	 	static double[] new_FBT ;// pour récupérer les FE des tâches reaffectées(pour tous Workflow)
//	 	static String new_FBT__ ="FBT Of reaffected Tasks :\n";
	 	
	 	static int exception;
	 	static String type;
		static int position_exception;
		static int task_excep_delete__;
	    static ArrayList<Integer> 	task_excep_add__;
	    static int  task_excep_substitute;

//	    static int reaff;
//	    static int reaff_size;
	//    static String value_action;
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
		
			String name_WF_file = "Montage_4T.xml";
			String name_rule_File = "Rule_Montage_4.xml";

		//	String name_WF_file = "Montage_25.xml";
		//	String name_rule_File = "Rule_Montage_25.xml";

			
			
			
			//Read_XML_Matrices r = new Read_XML_Matrices();
			Read_XMl_Evaluation r = new Read_XMl_Evaluation();
			
			n = r.NbTask(name_WF_file);
			
		//	System.out.println("nounou =="+n);
			
      //  System.out.println("**********Affichage de caractéristque Task***************");

		
		int[][] caracters_task_ = new int [n][c];
	/*	  int[][] caracters_task_ = {{398,  398,  4167,  1},  
	    	   {275,  258,  4171,  1},  
	    	   {464,  289 , 4157,  1},  
	    	   {280 , 303,  4174 , 1} , 
	    	  };
		*/
	 	caracters_task_=	r.caracteristique_task(name_WF_file);
 
    	
    	//-----------------------------Affichage de la matrice : dependance_task--------------------//
    //	System.out.print("***********Affichage de dependance_Task****************\n");

    	    double[][] dependance_task_ = new double [n][n];

	    	dependance_task_=	r.dependance_task(name_WF_file);

	    //	System.out.println("****************Affectation des Tasks before maj**************");

		
		//---------Générer estimations de DT et ET --------------------------//
		CloudSim_generer_estimation c1= new CloudSim_generer_estimation(dependance_task_, caracters_task_);
		c1.generer_estimation(n,m, caracters_task_, dependance_task_);
		ET = c1.Calcul_ET(n,m,c1.newList);//--FF  Calcul de temps d'exécution des tâches sur les # types de VMs
		DT = c1.Calcul_DT(n,c1.newList,c1.vmlist, dependance_task_ );
		UC_VM= c1.c_1_vm;
		//---------------------------------------------------------------------	
		
		
		Mapping_Task mt = new Mapping_Task();
		
		//******************Affichage de la matrice de TET*****************//
		 TET = mt.calcul_Total_ex_time(DT, ET, lag_vm, n, m);

		 
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

  
     //    for (int i=0; i<100 ;i++){ // tester mappage 100 fois
          	long Start_Mapping = System.currentTimeMillis(); 
          	System.out.println("Start_Mapping =="+Start_Mapping);

             //-----fc d'experimentation------
             p.execute(TET,n, DT, ET,UC_VM,D);
             System.out.println("--------------Après fin  p.execute--------------");
             long End_Mapping = System.currentTimeMillis(); 
             System.out.println("End_Mapping =="+End_Mapping);
             
             long Time_Mapping = End_Mapping - Start_Mapping; ////----- durée de Mapping
             System.out.println("Time_Mapping =="+Time_Mapping);
             
         
             
       //      }
                
             moy_mapp = som_mapp;
           System.out.println("moy_mapp =="+moy_mapp);
   
       


  //     result_total_cost = cons.getResult_total_cost();// pour avoir cout total avant consolidation
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