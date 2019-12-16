package org.hana.CPLEX;

import java.util.ArrayList;
import java.util.Random;
public class caract_cloudlet_generation {
	  int n;
	  int[][] caract_cloudlet ;
	  int h_length = 100;//4679539; ///-lower_length
	  int l_length =89;// 1893580; ///-higher_length
	  int l_fileSize = 20;//2000; 
	  int h_fileSize = 100; //4000; 
	  int l_outputSize = 70; //2000; 
	  int h_outputSize =100;// 7000; 	
	  int h_nbOfPes = 2;// 4;
	  int l_nbOfPes = 1;
	
		
		
		
	public caract_cloudlet_generation() {
			 
		}

	public caract_cloudlet_generation(int n) {
		this.n=n;
		caract_cloudlet=new int[n][n];
	}
	
	public int[][] generate_caract_cloudlet(){
		
		
		
		for(int i=0;i<n;i++){
			//int id = i;
			int random_length = (int)(Math.random() * (h_length+1-l_length)) + l_length; 
			int random_fileSize = (int)(Math.random() * (h_fileSize+1-l_fileSize)) + l_fileSize; 
			int random_outputSize = (int)(Math.random() * (h_outputSize+1-l_outputSize)) + l_outputSize; 
			int random_nbOfPes = (int)(Math.random() * (h_nbOfPes+1-l_nbOfPes)) + l_nbOfPes; 
		
			caract_cloudlet[i][0]= random_length;				
			caract_cloudlet[i][1]= random_fileSize;
			caract_cloudlet[i][2]= random_outputSize;
			caract_cloudlet[i][3]= 1;
		
		 	
			 
		}
		//mapp////System.out.println("=================caract_cloudlet=============");
		for(int i=0;i<n  ;i++)
		{ 
			for(int j=0;j<4  ;j++)
			{	// //System.out.print(caract_cloudlet[i][j]+"  ");
		    }
      	//mapp////System.out.println();
        }
       
		return caract_cloudlet;
		
	}
	
	

		//------------------------------------------------------------------------
		public int generate_length(){
			int random_length = (int)(Math.random() * (h_length+1-l_length)) + l_length; 
		 	return random_length;
			
		}
		
		public int generate_fileSize(){
			int random_fileSize = (int)(Math.random() * (h_fileSize+1-l_fileSize)) + l_fileSize; 
		 	return random_fileSize;
			
		}
		
		public int generate_outputSize(){
			int random_outputSize = (int)(Math.random() * (h_outputSize+1-l_outputSize)) + l_outputSize; 
		 	return random_outputSize;
			
		}
		
		
		public int generate_nbOfPes(){
			int random_nbOfPes = (int)(Math.random() * (h_nbOfPes+1-l_nbOfPes)) + l_nbOfPes; 
		 	return random_nbOfPes;
			
		}
		
		 
//	public ArrayList<Integer> getId(){
//		
//		ArrayList<Integer> list_id = new ArrayList<Integer>(); 
//		int id;
//		for (int i=0; i<n ;i++){
//			id = i;
//			list_id.add(i);
//		}
//		
//		return list_id;
//		
//	}
		 
	
	public int getID(int id){
		
		return id;
	}
	//--------------------------Main de cette classe (just pour le test)--------------------------------------
	public static void main(String args[]) {
		
		caract_cloudlet_generation GenerAlgo = new caract_cloudlet_generation(7); 

		  int[][] caract_cloudlet = GenerAlgo.generate_caract_cloudlet();
	    //mapp////System.out.println("---------- Affichange final de caract_cloudlet ------");
	 	for(int i=0;i<GenerAlgo.n  ;i++)
		{ 
	 		for(int j=0;j<4  ;j++)
			{	 
			  //System.out.print(caract_cloudlet[i][j]+"  ");
		    }
      	//mapp////System.out.println();
        }
		  
		
	}
}
