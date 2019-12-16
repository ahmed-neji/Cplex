package org.hana.CPLEX;

/* author: Hana */

// contient les méthodes utilisés avant consolidation

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

public class Mapping_Task   implements PSOConstants {

	static int n;
    static int swarm_size;
	static double[][] TC_VM = new double[n][m]; //cout total de VM
//	static double[] UC_VM ;//= {1.5,2.5,4}; //cout d'une unité de VM
	double[] lag_vm;
	double[][] ET;
	static double[][] DT;
//	double[] VM_Cost = {1.5,2.5,4}; // cout de chaque VM
	static double[] SE;  //--- SE: start execution de tâche
	static double[] FE;  //--- FE: finish execution de tâche
	static double [] Location_TET = new double[n];
	static double [] Location_FET =  new double [n];
	static int [] Location_SET = new int [n];
	static int max=0;

	
	
	
//----------------------- Calculer le Temps d'exécution totale : TET-------------------// Utilisée


public static  double[][] calcul_Total_ex_time(double[][] DT, double[][] ET,double[] lag_vm, int n, int m) {
	 
	  double[] DTT=new double[DT.length]; //DTT: un tableau contient le temps de transfert de chaque tâche
	  									//----(en calculant le max de tous les temps de transfert s'il y'en a plusieurs)

	  double[][] TET=new double[n][m];//TET: un tableau contient le temps d'ex total des tâches sur tous les types de VMs
	  								
	  for (int i = 0; i < DT.length ; i++) {
		 double transfer_data =0;
		     for (int j=0; j<DT.length ; j++) {
		    	if(transfer_data< DT[j][i])
		    		{  
		    		transfer_data = DT[j][i];
 		    	 
 		    	}
		     }
		     DTT[i]=transfer_data;
		
	  }   
	  
	  for (int i = 0; i < n ; i++) {
	        for (int j=0; j< m ; j++) {
    TET[i][j]= ET[i][j]+ DTT[i] + lag_vm[j];
            
		   }
	  
  }
	return TET;	
}


//----------------------- Calculer le nb d'heure (ou de VM) pour une tâche ayant une durée d'ex 'ex_time'----------------//Utilisée


public static  int calcul_Nb_VM(double ex_time) {
	  int nb_vm = 0;
	  if(ex_time % 60 ==0)
	      nb_vm = (int) (ex_time/60);
	  else 
		  nb_vm = (int) (ex_time/60+1);
	  
	  return nb_vm;

}


//-----------Méthode retourne le type de VM associé à la tâche i -------------------------//Non-utlisée

public static  int chercher_VM_Type(double[] xx ,int m) {
	  int i=0; 
	  boolean existe= false;
	  int type_vm = 0;
	   while(i<m &&  existe== false)  {
		     if(xx[i] != 0.0)
		     { type_vm = i;
		         
		       existe = true;
		     }
		     else
		    	 i++;
	 	}
	   return type_vm;
} 

//------------------------méthode pour caluler cout total---------------------------//Non-utlisée
/* public static double calcul_CT( double [][] TET) {
	System.out.println("------------calcul_CT ==========");
	System.out.println("------------n =========="+ n);
	System.out.println("------------m =========="+ m);

	double result = 0;
		for (int i=0 ; i<n ; i++){
			for (int j=0; j<m; j++) {
				System.out.println("------------	TET["+i+"]["+j+"]  =========="+	TET[i][j] );
				System.out.println("------------	UC_VM["+j+"]  =========="+	UC_VM[j] );
				
				TC_VM[i][j]  = calcul_Nb_VM(TET[i][j]) * UC_VM[j];
				System.out.println("------------	TC_VM["+i+"]["+j+"]  =========="+	TC_VM[i][j] );
				result = result + TC_VM[i][j] ;
			}
		}
		System.out.println("------------Cout total de VM: =========="+result);
		return result;
}
*/
//----- Chercher le dernier élèment d'un WF (çàd qui n'a pas de successeur)----------// Utilisée
public static int chercher_dernier_elet_WF(double[][] DT)
 {     int n= DT.length;
	   boolean trouve=false;
	   int i=0;
	   int indice_fin = 0;
			 while( trouve== false && i<n)  {
				
			 		 int nb_succ=0; int j=1; boolean trouve_1=false;// trouve_1=true çàd j'ai trouvé un élèment égale à 1
			 		 while( trouve_1 == false && j<n) { // ------ parcourir les colonnes de la ligne i
	  	        	//	  System.out.println("--------------DT["+i+"]["+j+"]--------------"+ DT[i][j]);
	  	      			     if(DT[i][j] == 0.0)  ///----çàd j est un successeur de i (pk??) !!!!!!!!!
	  	      			     {
	  	      			    	nb_succ++;
	  	      			     }
	  	      			     else
	  	      			     {	trouve_1=true;  }
	  	      			    
	  	      			     j++;
	  	        	   }    	 

			 		 if (nb_succ== n-1)
			 			{
			 			 indice_fin= i; //jusqu'à i=3 (dans notre cas)
			 		     trouve=true;
			 		     }
			 		 else 
			 			 i++;
	          	}
			 	 //  System.out.println("--------------Indice de Fin--------------"+ indice_fin);
		return indice_fin;
 }

//----- Chercher le premier élèment d'un WF (çàd qui n'a pas de predecesseur)----------//Précedement Utilisée
public static ArrayList<Integer> chercher_premier_elet_WF(double[][] DT) {
    
	
	ArrayList<Integer> list_first_elem = new ArrayList<>();
	int n= DT.length;
 //    System.out.println("n_mapping" +n);

	   boolean trouve=false;
	   int i=1;
	   int indice_start = 0;
			 while( trouve== false && i<n)  {
				
				// System.out.println("n_mapping_task=="+n);
				 
			 		 int nb_pred=0; int j=0; boolean trouve_1=false;// trouve_1=true çàd j'ai trouvé un élèment égale à 1
			 		 while( trouve_1 == false && j<n) { // ------ parcourir les colonnes de la ligne i
	  	        	//	  System.out.println("--------------DT["+j+"]["+i+"]--------------"+ DT[j][i]);
	  	      			     if(DT[j][i] != 0.0)  ///----çàd j est un successeur de i (pk??) !!!!!!!!!
	  	      			     {
	  	      			    	nb_pred++;
	  	      			     }
	  	      			     else
	  	      			     {	
	  	      			    	 trouve_1=true;  }
	  	      			    
	  	      			     i++;
	  	        	   }    	 

			 	//	 System.out.println("nb_pred =="+nb_pred);
			 		 if (nb_pred==1)
			 			{
			 			indice_start= j; //jusqu'à i=3 (dans notre cas)
			 			list_first_elem.add(indice_start);
			 		     trouve=true;
			 		     }
			 		 else 
			 			 j++;
			 		 
	          	}// fin "while"
			 	  // System.out.println("--------------Indice_Start--------------"+ indice_start);
		return list_first_elem;
}



//----- Chercher le premier élèment d'un WF (çàd qui n'a pas de predecesseur)----------//Utilisée autre méthode -hana
public static ArrayList<Integer> chercher_premier_elet_WF__(double[][] DT) {
    
	
	ArrayList<Integer> list_first_elem = new ArrayList<>();
	int n= DT.length;
   // System.out.println("n_mapping" +n);

	   int nb_pred=0;
	   
				for (int j=0; j<DT.length ;j++){ // parcourir les colonnes de la matrice DT
				
					for (int i=0; i<DT.length ;i++){

			 	//		System.out.println("--------------DT["+j+"]["+i+"]--------------"+ DT[j][i]);
						
	  	      			     if(DT[i][j] != 0.0)  ///----çàd j est un successeur de i (si on trouve dans DT; entre parent et child il y a une transition)
	  	      			     {
	  	      			   	nb_pred++; // on incrémente dasn les pred; cad "j" à un pred "i"( donc "j" n'est pas un first elem)
	  	      		//	    System.out.println("nb_pred =="+nb_pred);
	  	      			     }
//	  	      			    
	  	        	   }  // fin "for i"  	 

					
					//if (nb_pred >=1){ // si il ya  au moins un pred pour la colonne "j" ; donc on incrémente dans colonne (puisque elle ne s'agit pas du first elem)
                       // j++;	
      			 	  // System.out.println("--------------jjjj-------------"+ j);
					//}
					//else{
						if (nb_pred ==0){ // sinon; si lorsque on parcourt toutes les lignes avec cette colonne, et on ne trouve aucun pred; càad la colonne "j" n'a pas un pred "i"; 
							list_first_elem.add(j);	//d'ou "j" est un first elem du WK; on l'ajoute dans la liste
			 	//			System.out.println("--------------list_first_elem_method--------------"+ list_first_elem);

						
						} //"fin if"
					//}// fin "else"

					 nb_pred=0; // updated 15-07-2016

	          	}// fin "for j"
			 	  // System.out.println("--------------list_first_elem--------------"+ list_first_elem);
		return list_first_elem;
}




//----- Chercher le reste d'élèments d'un WF (çàd qui ne sont pas les premiers elem du WK)----------//Utilisée //add le 16/08/2016
public static ArrayList<Integer> chercher_reste_elet_WF__(double[][] DT) {
  
	
	ArrayList<Integer> list_reste_elem = new ArrayList<>();
	int n= DT.length;
 // System.out.println("n_mapping" +n);

	   int nb_pred=0;
	   
				for (int j=0; j<DT.length ;j++){ // parcourir les colonnes de la matrice DT
				
					for (int i=0; i<DT.length ;i++){

			 	//		System.out.println("--------------DT["+j+"]["+i+"]--------------"+ DT[j][i]);
						
	  	      			     if(DT[i][j] != 0.0)  ///----çàd j est un successeur de i (si on trouve dans DT; entre parent et child il y a une transition)
	  	      			     {
	  	      			   	nb_pred++; // on incrémente dasn les pred; cad "j" à un pred "i"( donc "j" n'est pas un first elem)
	  	      		//	    System.out.println("nb_pred =="+nb_pred);
	  	      			     }
//	  	      			    
	  	        	   }  // fin "for i"  	 

					
					//if (nb_pred >=1){ // si il ya  au moins un pred pour la colonne "j" ; donc on incrémente dans colonne (puisque elle ne s'agit pas du first elem)
                     // j++;	
    			 	  // System.out.println("--------------jjjj-------------"+ j);
					//}
					//else{
						if (nb_pred >=1){ // sinon; si lorsque on parcourt toutes les lignes avec cette colonne, et on ne trouve aucun pred; càad la colonne "j" n'a pas un pred "i"; 
							list_reste_elem.add(j);	//d'ou "j" est un first elem du WK; on l'ajoute dans la liste
			 	//			System.out.println("--------------list_first_elem_method--------------"+ list_first_elem);

						
						} //"fin if"
					//}// fin "else"

					 nb_pred=0; // updated 15-07-2016

	          	}// fin "for j"
			 	  // System.out.println("--------------list_first_elem--------------"+ list_first_elem);
		return list_reste_elem;
}


//----------------------- Contrainte de deadline------------------//Non_utilisée
/* SS
 * public static void RespectD(double D,double [][] TET,double [][] ET) {

	double [] Tot_Exc = new double[n];
	double result = 0;
	boolean r=false;
	
	for (int i=0 ; i<n ; i++){
    Tot_Exc[i] = TET[0][loc.getLoc()[i]]+ET[i][loc.getLoc()[i]];
	
	   System.out.println("Tot_Exc["+i+"] :"+Tot_Exc[i]);

	   
result = result + Tot_Exc[i];
	}
	 if( result <=D){
	 	   System.out.println("--------------Deadline respecté--------------");
		
	 }else{
	 	   System.out.println("--------------Deadline Non respecté--------------");
		}
	}
SS  */
//-------------------Calcul de SE (Start execution) et FE (Finish execution)-------------------------// Non-utilisée
	//----------- pour la tâche T0 -------------------   	
public static double calcul_SE_FE(double[] vm,double [][] TET) {

	double result =0;
    SE[0]=0; 
	    int k=0;
	    boolean calcul=false;
	    while (k<m && calcul==false)
	    {  
	     System.out.println(" *------------------* "); 
	    	if(vm[k] != 0.0) 
	    	{
	    		FE[0]=SE[0]+TET[0][k];
	    		calcul=true;
	    		result = result+ FE[0];   	  
	    	  System.out.println(""+ TET[0][k]+" ***____SE[0]== "+ SE[0]+"----FE[0]="+ FE[0]);
	    
	    	}  
	    	else {
	    		k++;
	    		
	    }
}
	    System.out.println("res"+result);
		  return result;
}

  	 
//-----------Méthode retourne la liste des prédecesseurs d'une tâche-----------//Utilisée 
public static List<List<Integer>> pred_all_tasks_Workflow(int n,double[][] DT)
{
  //List<Integer> Pred_T0= new Vector<Integer>(); 
  List<Integer> Pred_T0= new ArrayList<Integer>(); 
  List<List<Integer>> Pred_all_tasks_wf = new ArrayList<List<Integer>>(); 
  //	 Location_SET[0]=0;	


 // Pred_T0.add(-1); //---- çàd la tâche 0 n'a pas de prédecesseur //hana: changer -1 par 0 "modifi" le 16/08/2016 "car on remarqué lors de l'évaluation que la T0 peut avoiir des pred"
       //   Pred_all_tasks_wf.add(Pred_T0);
    
          for (int j=0; j<n ; j++) {
        	 List<Integer> Pred_1_task= new ArrayList<Integer>(); 
	        	  for (int i = 0; i < n ; i++) {  // ------ parcourir les lignes de chaque colonne j
	        		// System.out.println("** DT["+i+"]["+j+"]=="+ DT[i][j]);
	      			     if(DT[i][j] != 0.0)  ///----çàd i est un successeur de j
	      			     {

	      			    	
	      			    	 Pred_1_task.add(i); //j'ai essayée avec new Integer(i)
	      			    	
	      			     //   System.out.println("le max est: " + list.stream().reduce(Integer::max));
	      			    	 
	      			  
		 	      			     
	      			     }
	      			   
	      		
	        	   }    	 
	        	Pred_all_tasks_wf.add(Pred_1_task);
	        	
	        	

        	}
       
		   //----------------------- Afficher les prédecesser de chaque tâche -----------------------  
	    //for (int i=0; i< Pred_all_tasks_wf.size()  ; i++) {
	   // for (int j=0; j< Pred_all_tasks_wf.get(i).size(); j++) {
		//		 System.out.println("Pred_T["+ i +"] ==  "+( Pred_all_tasks_wf.get(i).get(j)));
 //}//fin boucle "j"
           
   // }//fin boucle "i"	
	    
	  return Pred_all_tasks_wf;        
}



//-----------Méthode retourne la liste des Location_FET du dernier elem de chaque particule------// Non-utilisée
public static List<Integer> Loc_FET_last_elem_partic (double [] Location_FET ){
	///double [] Location_FET;
	List<Integer> Loc_FET_Part = new ArrayList<Integer>(); 

    for (int i=0; i<swarm_size ; i++) {
    	Location_FET[i]= Location_FET[chercher_dernier_elet_WF(DT)];
    	Loc_FET_Part.add((int)Location_FET[i]);
    }

	return Loc_FET_Part;
}

//----- Méthode cherche le sous indice de elet ds VM_Tache2----------------------------

int chercher_indice_s_elet(Vector<Integer> v,int elet)
{
	   boolean trouve=false;int k=0;
			 while(k<v.size() &&  trouve== false)  
			   { if (v.get(k)==elet)
				  trouve = true;
				 else
					 k++;
			   }
	   return k;
}

//----- Méthode cherche l'indice de elet ds VM_Tache2----------------------------//Utilisée
int chercher_indice_elet(	Vector<Vector<Integer>> VM_Tache2,int elet)
{
	   boolean trouve=false;int j=0;
	   while(j<VM_Tache2.size() &&  trouve== false)  
		   {  Vector<Integer> v = new Vector<Integer>();
			  v = VM_Tache2.elementAt(j);
			  int k= 0;
			 while(k<v.size() &&  trouve== false)  
			   { 
				 if (v.get(k)==elet)
				  trouve = true;
				 else
					 k++;
			   }
			 if(trouve== false)
				 j++;
		   } 
	   return j;
}

//---------------------- Méthode retourne les successeurs d'une tâche --------------------------//Utilisée

static Vector<Integer>  Succ_1tache_de_Wf(int indice_t,double[][] DT)
{  /// System.out.println("é~~é~~é~~é~~é~~é~~ é~~é~~é~~  n ======" + n);
	  Vector<Integer> succ_task= new Vector<Integer>();
	  for (int j=1; j<DT.length ; j++) {
		   if(DT[indice_t][j] !=0)
			   succ_task.add(j);
	  }
	  //..	 System.out.println("é~~é~~é~~ Les succeurs de ::"+ indice_t + "::: sont:::::" + succ_task);
    return  succ_task;
}

//---------------------- Méthode retourne le predecesseurs d'une tâche --------------------------//Utilisée

static Vector<Integer>  Pred_1tache_de_Wf(int indice_t,double[][] DT)
{  /// System.out.println("é~~é~~é~~é~~é~~é~~ é~~é~~é~~  n ======" + n);
	  Vector<Integer> pred_task= new Vector<Integer>();
	  for (int j=0; j<DT.length ; j++) {
		   if(DT[j][indice_t] !=0)
			   pred_task.add(j);
	  }
	  //..	 System.out.println("é~~é~~é~~ Les succeurs de ::"+ indice_t + "::: sont:::::" + succ_task);
  return  pred_task;
}


}

