package org.hana.CPLEX;

import java.util.Random;
import java.text.DecimalFormat;
import java.util.List;
import ilog.concert.*;
import ilog.cplex.*;


public class CplexProcess implements PSOConstants {
	
  	static double[] Location_TET;
	double max;
	double min_FET;
	double min_cost;
//	String Log_map="Localities of Tasks's Best Solution :";
	Random generator = new Random();
	static   int elem_reste_wk ;
	//SS ---- mes variables ------------------
	IloCplex cplex;
    IloIntVar[][] x;
    IloNumVar[] F;   // ----- F représente le temps de fin d'ex des tâches sur les VMs    //99
    int []   Duration_constraint;
	IloLinearNumExpr[] Ex_Time_task; 
	IloLinearNumExpr[] Tot_Ex_Time;
	IloLinearNumExpr objective;
	double[][] c_T_vm ; //----- pour que les élets de C_T_vm seront initialisés tous à 0
	double[] c_1_vm; 	
	double cout_total; //----- coût total des VMs
	List<List<Integer>> Pred_all_tasks;
	int[] id_vm_affecte;
	
	
	public void execute(double [][] TET,int n,double[][] DT , double [][] ET,double[] UC_VM, double D) {    //ajouter TET comme param d'entrée pour pouvoir l'ajouter dans evaluate/updateFitnessList
		// task run **************************************************
		 int[][] task_run=new int[9][5];
		 
			/* t1 */ 	 task_run[0][0]=0;   task_run[0][1]=1;   task_run[0][2]=1;   task_run[0][3]=1;   task_run[0][4]=1;
				 
				 
			/* t2 */	 task_run[1][0]=1;  task_run[1][1]=1;  task_run[1][2]=1;  task_run[1][3]=0;  task_run[1][4]=0;
				 
				 
			/* t3 */	 task_run[2][0]=0;  task_run[2][1]=1;  task_run[2][2]=1;  task_run[2][3]=1;  task_run[2][4]=1;
				 
				 
			/* t4 */	 task_run[3][0]=1;  task_run[3][1]=1;  task_run[3][2]=1;  task_run[3][3]=1;  task_run[3][4]=0;
				 
				 
			/* t5 */	 task_run[4][0]=1  ;task_run[4][1]=1;  task_run[4][2]=1;  task_run[4][3]=0;  task_run[4][4]=0;
				 
				 
			/* t6 */	 task_run[5][0]=1;  task_run[5][1]=1;  task_run[5][2]=0;  task_run[5][3]=1;  task_run[5][4]=0;
				 
				 
			/* t7 */	 task_run[6][0]=1;  task_run[6][1]=1;  task_run[6][2]=1;  task_run[6][3]=0;  task_run[6][4]=0;
				 
				 
			/* t8 */	 task_run[7][0]=1;  task_run[7][1]=1;  task_run[7][2]=1;  task_run[7][3]=0;  task_run[7][4]=0;
				 
				 
			/* t9 */	 task_run[8][0]=0;  task_run[8][1]=0;  task_run[8][2]=0;  task_run[8][3]=1;  task_run[8][4]=1;
		// fin task run ***********************************************
			
			// temps execute ********************************************
			int[]temps_execute= new int[9];
			temps_execute[0]=30;
			temps_execute[1]=20;
			temps_execute[2]=40;
			temps_execute[3]=30;
			temps_execute[4]=25;
			temps_execute[5]=5;
			temps_execute[6]=10;
			temps_execute[7]=25;
			temps_execute[8]=50;
			// fin temps execute ****************************************
		
			
			
			
			
			
			
			
			c_T_vm = new double[n][m]; 
	      try {
	        	// define new model
	         	cplex = new IloCplex();
	         
	        	// variables
	         	x = new IloIntVar[n][];
	            F = new IloIntVar[n]; 
	              
	         	for (int i=0; i<n; i++) {
	            	 F[i] = cplex.numVar(0, D);
	            
	        	}
	         	  
	      	 // cplex.addEq(x[1][0], 0);
	               //-------------------------------------------------- 
	        	for (int i=0; i<n; i++) {
	         		x[i] = cplex.intVarArray(m, 0, 1);
	         	}
	              Ex_Time_task = new IloLinearNumExpr[n];  
	   
	        	for (int i=0; i<n; i++) {
	        		Ex_Time_task[i] = cplex.linearNumExpr();
	        		Ex_Time_task[i].addTerms(x[i],ET[i]);// multiplier la ligne i de x avec la ligne i de T et faire la sommme
	        	
	             }  
	        	///******ajouté par fairouz *******
		    	for (int i=0;i<9;i++)
		    		for(int j=0;j<m;j++)
		    		{
		    			if(task_run[i][j]==0)
		    				x[i][j]=  (IloIntVar) cplex.numVar(0, 0);
		    		}
	 	        	
	 	        		
	 	        		
		        	
		        	//**********************fin *************************
	        
		     	
	        	//-------------- Appel de la fonction "calcul_Total_ex_time" pour -----------------------------------------------
	        	//-------------- Calculer le temps total d'exécution d'une tâche: TET = ET+DT sur toutes les types de VMs---------
	        	 
	       //SS   TET= calcul_Total_ex_time(DT,ET,lag_vm, n, m);//SS c'est inutile, puisque on l'a mis parametre d'entrée à la fonction
	           DecimalFormat dft = new DecimalFormat("0.00"); //pour avoir 2 chiffres après la virgule
	           System.out.println("------ Affichage TET dans Example_cahier ...----");
	        
	           for (int i=0; i<n; i++) {
	          		for (int j=0; j<m; j++) {
	          		    System.out.print(dft.format(TET[i][j])+"  ");
	            		}
	          		System.out.println();
	        	  } 
	        	//------------------------------------------------------------
	           
	           
	           
	           Tot_Ex_Time = new IloLinearNumExpr[n]; 
	          	   
	          	for (int i=0; i<n; i++) {
	          
	          		Tot_Ex_Time[i] = cplex.linearNumExpr();
	          		Tot_Ex_Time[i].addTerms(x[i],TET[i]);// multiplier la ligne i de x avec la ligne i de T
	          		
	               }  
	        	 
	          //--------------- Calculer le coût de chaque tâche sur chaque type de VM ---------------------------
	            System.out.println("*********** UC_VM.length==== "+ UC_VM.length );
	            System.out.println("*********** TET.length==== "+ TET.length );
	            System.out.println("*********** c_T_vm.length==== "+ c_T_vm.length );
	            
	          /*  System.out.println("****UC_VM[0]==== "+ UC_VM[0] );
	            System.out.println("****UC_VM[1]==== "+ UC_VM[1] );
	            System.out.println("****TET[0][1] ==== "+ TET[0][1]);
				 */
	          	for (int i=0; i<n; i++) {
	        		for (int j=0; j<m; j++) {
	        			    System.out.println("***TET["+i+"]["+j+"] ==== "+ TET[i][j]);
	        			 
	        			    System.out.println("****UC_VM["+j+"]==== "+ UC_VM[j] );//je change par le nom de variable utilisée par Hana
	           			   
	        			   // c_T_vm[i][j]  = calcul_Nb_VM(TET[i][j]) * c_1_vm[j]; //---il faut TET au lieu de ET---
	        			    c_T_vm[i][j]  = calcul_Nb_VM(TET[i][j]) * UC_VM[j]; //---il faut TET au lieu de ET---
	            		    //..   System.out.println("***nb_vm======= "+ calcul_Nb_VM(TET[i][j]));
	        			    System.out.println("[[[[[[[[********c_T_vm["+ i+"]["+ j+"] ==== "+ c_T_vm[i][j] );
	        			
	        		}
	        	}	
	        	//------- la fonction objective: diminuer le coût d'exécution ---------------------------
	        	objective = cplex.linearNumExpr();
	        	for (int i=0; i<n; i++) {
	        			objective.addTerms(x[i],c_T_vm[i]); //multiplier la ligne i de x avec la ligne i de c_T_vm
	        			
	        	}
	        	// define objective
	         	cplex.addMinimize(objective);
	      
	        	 //---- Constraint 1 -----------
	        	for (int i=0; i<n; i++) {
	          	  cplex.addEq(cplex.sum(x[i]), 1);
	        		  
	        	}
	        	//---- Constraint 2 -----------
	         	
	          cplex.addLe(Tot_Ex_Time[0],F[0]);//addLe inferieur ou egale
	          
	                 
	      	//---- Constraint 3 -----------   	  
	          for (int i = 0; i < n ; i++) {
	        	   for (int j=1; j<n ; j++) { /// --07/10/2014, normalement ici j<n c pas m, car DT  n ligne et n colonne
	     			     if(DT[i][j] != 0.0)  ///----çàd j est un successeur de i
	     			    	{//System.out.println("[[[[[[***** i=="+ i +" j ==" + j +" Tot_Ex_Time[j]=="+ Tot_Ex_Time[j]);
	      			    	//cplex.addLe(cplex.sum(Tot_Ex_Time[j],cplex.sum(F[i],DT[i][j])),F[j]);}
	     			          cplex.addLe(cplex.sum(Tot_Ex_Time[j],F[i]),F[j]);
	     			          
	     			          //
	      			        }
	     		    }
	             } 
	      	    //---- Constraint 4: contrainte de deadline -----------
	             cplex.addLe(F[chercher_dernier_elet_WF(n,DT)],D);  
	    	 
	             
	             //---- Constraint 5: pour execeuter les taches qui ne depasse pas le contrainte temporelle   -----------
	              
	             Duration_constraint= new int[n]; 
	          	for (int i=0; i<n; i++) {
	          	   
		            cplex.addLe(Tot_Ex_Time[i],temps_execute[i]);
	            
	          	System.out.println("duration ***************"+Duration_constraint[0]);
	                
	      }
	          //---- Constraint 6:pour executer seulement les taches qui ont executable sur les ressources-------
	          	for (int i=0;i<n;i++)
	          		
	          			cplex.addLe(0,Tot_Ex_Time[i]); 
	          			
	          	
	          	 
	            
	             
	             
	             
	             
	           //----------------------- Chercher les prédecesseurs de chaque tâche du WF ---------------------
	           //  Pred_all_tasks= Pred_all_tasks_Workflow(n,DT);
	      
	             cplex.setParam(IloCplex.Param.Simplex.Display, 0);
	 	  
	 	       // solve model
	         	if (cplex.solve()) {//on va demander à CPLEX de résoudre le problème
	               //Si CPLEX a trouvé une solution (cplex.solve()=true), on calcule le coût total des ressources   		 
	         		cout_total = cplex.getObjValue(); //-------- c'est le cout financier des ressources utilisées
	         
	         	}
	         		//-----------Afficher le nombre et le type de chaque VM nécessaire pour chaque tache ------
	       	//.	System.out.println("-------  Affichage de résultat de mappage  ------- ");
	 	    //     Affichage de résultat de mappage 
	 	        id_vm_affecte =new int[n];
	 	       System.out.println("-------  cplex.solve()  ------- "+ cplex.solve());
	 	        if(cplex.solve()==true)
	 	        {	
	 	        	
	 	        	 System.out.println("------ ajouté par fairouz ...----");
	 	        	for (int i1 = 0; i1 < n ; i1++) {
	 	        		double[] xx1     = cplex.getValues(x[i1]);
	 	        		for (int j1=0; j1<m ; j1++) {
	 	        			 System.out.print(xx1[j1]+"  ");
	 	          		}
	 	        		System.out.println("");
	 	        	}	
	 	        	 System.out.println("------ fin ajouté par fairouz ...----");
	 	        		
	 	        		
	 	        	for (int i = 0; i < n ; i++) {
	 	        		double[] xx     = cplex.getValues(x[i]);
	 	        		for (int j=0; j<m ; j++) {
	 	        			if(xx[j] != 0.0)
	 	        				{System.out.println("Variable x ["+ i + "]["+ j +"] == "+ xx[j]+"   Nb VM=="+ calcul_Nb_VM(TET[i][j])); 
	 	        				System.out.println("...Variable TET ["+ i + "]["+ j +"] == "+ dft.format(TET[i][j]) ); 
	 	        				//id_vm_affecte[i]= (int)xx[j];
	 	        				id_vm_affecte[i]= j;
	 	        				//String aa ="------------------La tâche T"+(i+1)+" est affectée à la VM"+ (id_vm_affecte[i]+1); 
	 	        				System.out.println("------------------La tâche T"+(i+1)+" est affectée à la VM"+ (id_vm_affecte[i]+1) +"\n");
	 	        				//Page5.res_cplex.getItems().add(aa);
	     		 				}
	 	        		}  
	 	        	}   
	 	        }
			
			
			
	       
			
			
			
			
			
			
			
	        }
	        catch (IloException exc) {
	        	exc.printStackTrace();
	        }
		  
		}// fin execute
		
		

		/************************ Méthodes utilisée dans CPLEX ****************************************/
		//----------------------- Calculer le nb d'heure (ou de VM) pour une tâche ayant une durée d'ex 'ex_time'
		  public static  int calcul_Nb_VM(double ex_time) {
			  System.out.println("\n**********Entrée dans calcul_Nb_VM**************" );
			  int nb_vm = 0;
			  if(ex_time % 60 ==0)
			      nb_vm = (int) (ex_time/60);
			  else 
				  nb_vm = (int) (ex_time/60+1);
			  
			  return nb_vm;
		  }
		
		
		  
		  
		  //----- Méthode cherche le dernier élèment d'un WF (çàd qui n'a pas de successeur).On a besoin de ça pour la contrainte f(N)<D---------------------------
			
			 int chercher_dernier_elet_WF(int n,double[][] DT)
			  {
				   boolean trouve=false;int i=0;
				   int indice_fin = 0;
						 while( trouve== false && i<n)  {
							
						 		 int nb_succ=0; int j=1; boolean trouve_1=false;// trouve_1=true çàd j'ai trouvé un élèment égale à 1
						 		 while( trouve_1 == false && j<n) { // ------ parcourir les colonnes de la ligne i
				  	        		//  System.out.println("*********** DT["+i+"]["+j+"]=="+ DT[i][j]);
				  	      			     if(DT[i][j] == 0.0)  ///----çàd j est un successeur de i
				  	      			     {
				  	      			    	nb_succ++;
				  	      			     }
				  	      			     else
				  	      			     {	trouve_1=true;  }
				  	      			     j++;
				  	        	   }    	 

						 		 if (nb_succ== n-1)
						 			{
						 			 indice_fin= i;
						 		     trouve=true;
						 		     }
						 		 else 
						 			 i++;
				          	}
						 //..	   System.out.println("**************indice_fin dans sol naive==========="+ indice_fin);
					return indice_fin;
			  }   

			 
		 
}
