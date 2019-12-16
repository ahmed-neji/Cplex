package org.hana.CPLEX;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletSchedulerSpaceShared;
import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.MyVm;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;
public class CloudSim_generer_estimation implements PSOConstants {




	/** The cloudlet list. */
	private static List<Cloudlet> cloudletList;
    /** The vmlist. */
	static List<MyVm> vmlist;
 	static double [][] dependance_task;
	static int [][] caracters_task;

	 double[] c_1_vm;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              
	 List<Cloudlet> newList;
	 
	//------------- Constructeur ----------------------- 
	public CloudSim_generer_estimation(double[][] dependance_tache,
			int[][] caracters_task) {
		super();
		dependance_task = dependance_tache;
		this.caracters_task = caracters_task;
				 
	}
	//-----------------Méthode crée une liste de VM-------------------
	private static List<MyVm> createVM(int userId, int vms) {
	
 	 	//Creates a container to store VMs. This list is passed to the broker later
		LinkedList<MyVm> list = new LinkedList<MyVm>();
		
		//------------------------------ VM1 ---------------------
		//VM Parameters
		long size = 100; //image size (MB)
		int ram = 1; //vm memory (MB) //était 2048 avent 09/09/2016
		int mips = 15;
		long bw = 100;
	    int pesNumber = 1; //number of cpus
		String vmm = "Xen"; //VMM name
		double cost_hour=0.036; //cost per hour of vm //
		//create VMs
		
		// les caractéristiques de VM sont modifiées le 09/09/2016 selon tableau
		MyVm[] vm = new MyVm[vms];
		
		vm[0] = new MyVm(0, userId, mips+100*0, pesNumber, ram*2, bw, size, vmm, new CloudletSchedulerSpaceShared(),0.036);
		list.add(vm[0]);

 		vm[1] = new MyVm(1, userId, mips+100*1, pesNumber*2,  ram*4, bw, size, vmm, new CloudletSchedulerSpaceShared(),0.072);
		list.add(vm[1]);

		vm[2] = new MyVm(2, userId, mips+100*2, pesNumber*2, ram*8, bw, size, vmm, new CloudletSchedulerSpaceShared(),0.134);
		list.add(vm[2]);
		
		vm[3] = new MyVm(3, userId, mips+100*3, pesNumber*4, ram*16, bw, size, vmm, new CloudletSchedulerSpaceShared(),0.491);
		list.add(vm[3]); //add le 07/09
		
		vm[4] = new MyVm(4, userId, mips+100*4, pesNumber*16, ram*64, bw, size, vmm, new CloudletSchedulerSpaceShared(),1.966);
		list.add(vm[4]); //add le 07/09
 		return list;
	}

	//-----------------Méthode crée une liste de Cloudlets-------------------

	private static List<Cloudlet> createCloudlet(int userId, int nb_cloudlets, int[][] caract_cloudlet){
		// Creates a container to store Cloudlets
		ArrayList<Cloudlet> list = new ArrayList<Cloudlet>();
		 
		//cloudlet parameters
		long length ;
		long fileSize;
		long outputSize ;
		int pesNumber;
		//UtilizationModel utilizationModel = new UtilizationModelFull();

		Cloudlet[] cloudlet = new Cloudlet[nb_cloudlets];
	//--FF	Créer n cloudlets -----------------------------
		/////mapp////System.out.println(" uuuuu============cloudlets====== "+ cloudlets);
		
		for(int i=0;i<nb_cloudlets;i++)
			{   
				length = caract_cloudlet[i][0];
				fileSize = caract_cloudlet[i][1];
				outputSize = caract_cloudlet[i][2];
				pesNumber = caract_cloudlet[i][3];
				UtilizationModel utilizationModel = new UtilizationModelFull();

				cloudlet[i] = new Cloudlet(i, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
				cloudlet[i].setUserId(userId);
				list.add(cloudlet[i]);
			}

		return list;
	}



	private static Datacenter createDatacenter(String name){

		// Here are the steps needed to create a PowerDatacenter:
		// 1. We need to create a list to store one or more
		//    Machines
		List<Host> hostList = new ArrayList<Host>();
		// 2. A Machine contains one or more PEs or CPUs/Cores. Therefore, should
		//    create a list to store these PEs before creating
		//    a Machine.
		List<Pe> peList1 = new ArrayList<Pe>();

		//--FF  int mips = 1000;
		   int mips = 1000000;   //--FF 
		// 3. Create PEs and add these into the list.
		//for a quad-core machine, a list of 4 PEs is required:
		peList1.add(new Pe(0, new PeProvisionerSimple(mips))); // need to store Pe id and MIPS Rating
		peList1.add(new Pe(1, new PeProvisionerSimple(mips)));
		peList1.add(new Pe(2, new PeProvisionerSimple(mips)));
		peList1.add(new Pe(3, new PeProvisionerSimple(mips)));

		//Another list, for a dual-core machine
		List<Pe> peList2 = new ArrayList<Pe>();

		peList2.add(new Pe(0, new PeProvisionerSimple(mips)));
		peList2.add(new Pe(1, new PeProvisionerSimple(mips)));

		//4. Create Hosts with its id and list of PEs and add them to the list of machines
		int hostId=0;
	//FF--	int ram = 2048; //host memory (MB)
		int ram = 204800; //host memory (MB)
//FF--	long storage = 1000000; //host storage
		long storage = 10000000; //host storage
//--FF		int bw = 10000;
		int bw = 100000;
		
		hostList.add(
    			new Host(
    				hostId,
    				new RamProvisionerSimple(ram),
    				new BwProvisionerSimple(bw),
    				storage,
    				peList1,
    				new VmSchedulerTimeShared(peList1)
    			)
    		); // This is our first machine

		hostId++;

		hostList.add(
    			new Host(
    				hostId,
    				new RamProvisionerSimple(ram),
    				new BwProvisionerSimple(bw),
    				storage,
    				peList2,
    				new VmSchedulerTimeShared(peList2)
    			)
    		); // Second machine

		// 5. Create a DatacenterCharacteristics object that stores the
		//    properties of a data center: architecture, OS, list of
		//    Machines, allocation policy: time- or space-shared, time zone
		//    and its price (G$/Pe time unit).
		String arch = "x86";      // system architecture
		String os = "Linux";          // operating system
		String vmm = "Xen";
		double time_zone = 10.0;         // time zone this resource located
		double cost = 3.0;              // the cost of using processing in this resource
		double costPerMem = 0.05;		// the cost of using memory in this resource
		double costPerStorage = 0.1;	// the cost of using storage in this resource
		double costPerBw = 0.1;			// the cost of using bw in this resource
		LinkedList<Storage> storageList = new LinkedList<Storage>();	//we are not adding SAN devices by now

		DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
                arch, os, vmm, hostList, time_zone, cost, costPerMem, costPerStorage, costPerBw);


		// 6. Finally, we need to create a PowerDatacenter object.
		Datacenter datacenter = null;
		try {
			datacenter = new Datacenter(name, characteristics, new VmAllocationPolicySimple(hostList), storageList, 0);
		} catch (Exception e) {
			e.printStackTrace();
		} 

		return datacenter;
	}

	//We strongly encourage users to develop their own broker policies, to submit vms and cloudlets according
	//to the specific rules of the simulated scenario
	private static DatacenterBroker createBroker(){

		DatacenterBroker broker = null;
		try {
			broker = new DatacenterBroker("Broker");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return broker;
	}
	 
	//--FF Calcul de temps d'exécution des tâches sur les # types de VMs ------------------
 	static double[][] Calcul_ET(int n,int m, List<Cloudlet> list) {
 		double[][] ET = new double[n][m]; 
 		DecimalFormat dft = new DecimalFormat("0.00");
 	//	//mapp////System.out.println(" ============== list.size() ========= "+ list.size());
 	//	//mapp////System.out.println(" ============== list========= "+ list);
 	 	
 		for (int i=0; i<list.size(); i++) {
 	//		//mapp////System.out.println("list.size()=="+list.size());
			int cloudlet_id=list.get(i).getCloudletId();
	//		//mapp////System.out.println(" ============== list.get(i)========= "+ list.get(i));
		 	 
			////mapp////System.out.println(" ============== list.get(i).getActualCPUTime()========= "+ list.get(i).getActualCPUTime());
	 	 	
			int vm_id=list.get(i).getVmId();
		
			ET[cloudlet_id][vm_id] = list.get(i).getActualCPUTime()/60;
			
		}	
 		
 		//mapp////System.out.println(" ****** --------------- Calcul_ET ----------------***** ");
     	for (int i=0; i<n ; i++) {
	    	for (int j=0; j<m; j++) {
    	 		//System.out.print(dft.format(ET[i][j])+"  ");
    		}
    		//mapp////System.out.println();
	   } 
		return ET;
		
		 
 }
 	
 	//--FF Calcul de temps de transfert entre les tâches : la matrice DT  ------------------
 	static  double[][]  Calcul_DT(int n, List<Cloudlet> list, List<MyVm> vmlist, double[][] dependance_task) {
 		double [][] DT = new double[n][n]; 
 	//	DecimalFormat dft = new DecimalFormat("0.00");
 	
    	for (int i=0; i<n ; i++) {
	    	for (int j=0; j<n; j++) {
	    		if( dependance_task[i][j]!= 0 ) //---Il faut calculer le temps de transfert entre i et j
	    		{	
	    			DT[i][j] = (double)(caracters_task[i][2]/60)/(vmlist.get(0).getBw());//--- (double) pour afficher les chiffres
	    			//--- pour récupérer la valeur de bandwith, j'ai pris la valeur du 1er élèment de vmlist puisque la valeur de bw des VMs sont égaux
	    		}
    	 	}
	   }
 		//mapp////System.out.println(" ** --------------- Calcul_DT ---------------- ** ");
 		for (int i=0; i<n ; i++) {
	    	for (int j=0; j<n; j++) {
    	 		//System.out.print(DT[i][j]+"    ");
    		}
    		//mapp////System.out.println();
	     }  
 		
 		return DT;
 	}
 	
 		
 	/**
	 * Prints the Cloudlet objects
	 * @param list  list of Cloudlets
	 */
	private static void printCloudletList(List<Cloudlet> list) {	
		int size = list.size();
		Cloudlet cloudlet;

		String indent = "    ";
		Log.printLine();
	//	Log.printLine("========== OUTPUT ==========");
	//	Log.printLine("Cloudlet ID" + indent + "STATUS" + indent +
			//	"Data center ID" + indent + "VM ID" + indent + indent + "Time" + indent + "Start Time" + indent + "Finish Time");

		DecimalFormat dft = new DecimalFormat("###.##");
		
		for (int i = 0; i < size; i++) {
			cloudlet = list.get(i);
		//	Log.print(indent + cloudlet.getCloudletId() + indent + indent);

			if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS){
			//	Log.print("SUCCESS");

			//	Log.printLine( indent + indent + cloudlet.getResourceId() + indent + indent + indent + cloudlet.getVmId() +
			//			indent + indent + indent + dft.format(cloudlet.getActualCPUTime()/60) +
			//			indent + indent + dft.format(cloudlet.getExecStartTime()/60)+ indent + indent + indent + dft.format(cloudlet.getFinishTime()/60));
			}
		}
	 
  }
	//--------------
	  void generer_estimation(int n,int m,int[][] caracters_task, double[][] dependance_task){
			
		
			try {
				// First step: Initialize the CloudSim package. It should be called
				// before creating any entities.
				int num_user = 1;   // number of grid users
				Calendar calendar = Calendar.getInstance();
				boolean trace_flag = false;  // mean trace events

				// Initialize the CloudSim library
				CloudSim.init(num_user, calendar, trace_flag);

				// Second step: Create Datacenters
				//Datacenters are the resource providers in CloudSim. We need at list one of them to run a CloudSim simulation
				@SuppressWarnings("unused")
				Datacenter datacenter0 = createDatacenter("Datacenter_0");
				
				//Third step: Create Broker
				DatacenterBroker broker = createBroker();
			
				int brokerId = broker.getId();
				//mapp////System.out.println("=======brokerId ============= "+ brokerId);	
				//Fourth step: Create VMs and Cloudlets and send them to broker
				
				vmlist = createVM(brokerId,m); //creating m vms
				 //--FF --------- Remplir le tableau c_1_vm de coût par heure de chaque type de VM -----------
				
			//	//mapp////System.out.println("vmlist.size() 11=="+vmlist.size());

				c_1_vm = new double[vmlist.size()];
				for(int i=0;i<vmlist.size();i++)
					{
				//	//mapp////System.out.println(" ii =" +i);

				//	//mapp////System.out.println("vmlist.size() =="+vmlist.size());
				//	//mapp////System.out.println("c_1_vm["+i+"] ==" +c_1_vm[i]);
				//	//mapp////System.out.println("vmlist.get("+i+") ==" +vmlist.get(i).getCost_hour());
					    c_1_vm[i]= vmlist.get(i).getCost_hour();
					}
				
				//---
				broker.submitVmList(vmlist);

         		//mapp////System.out.println("Nb_VMs : "+ m);
				//mapp////System.out.println("Nb_Tasks : "+ n);
				
			for(int k=0;k<m;k++)
		   	{   cloudletList = createCloudlet(brokerId,n,caracters_task); // creating  cloudletList à n elets
		   	//	//mapp////System.out.println("=======222222 cloudletList.size()  ============= "+ cloudletList.size());
			    broker.submitCloudletList(cloudletList); //-- Le nbre de soumission de cloudletList est le nbre de type de VMs
			
				for(int i=0;i<cloudletList.size();i++)
				{	/////mapp////System.out.println("=======222222 i  ====== "+ i + "----k===="+ k);
					////mapp////System.out.println("=======222222 cloudletList.size()  ============= "+ cloudletList.size());
					//-//mapp////System.out.println("=======222222 vmlist.get("+k+").getId()  ====== "+ vmlist.get(k).getId());
					////mapp////System.out.println("=======222222   cloudletList.get(i)  ====== "+   cloudletList.get(i));
					
				    cloudletList.get(i).setVmId(vmlist.get(k).getId()); //--- J'utilise le type 'k' de VM
			       //- //mapp////System.out.println("FF=======cloudletList.get("+i+").getVmId()============= "+  cloudletList.get(i).getVmId());	
			       // //mapp////System.out.println("FF=======cloudletList.get("+i+").getActualCPUTime()==== "+  cloudletList.get(i).getActualCPUTime());	
			       // //mapp////System.out.println("FF=======cloudletList.get("+i+").getFinishTime()======= "+  cloudletList.get(i).getFinishTime());	
			       // //mapp////System.out.println("FF=======cloudletList.get("+i+").getExecStartTime()==== "+  cloudletList.get(i).getExecStartTime());	
			    ///	broker.bindCloudletToVm(cloudletList.get(i).getCloudletId(),vmlist.get(k).getId());
				}
				//// CloudSim.pause(0, 100.0);
		   	}	
		//****--------------------------------****------------------------------****-----------------
				// Fifth step: Starts the simulation
				CloudSim.startSimulation();

				newList = broker.getCloudletReceivedList();
				//mapp////System.out.println("============== newList.size()  =============== "+ newList.size());
				// Final step: Print results when simulation is over
				
	            CloudSim.stopSimulation();
	        	printCloudletList(newList); //--- Tout l'affichage sera dans la même liste (newList):pour les m=7 types de VM
	        	
	        	////mapp////System.out.println("======n  ============= "+ n);
	        	//mapp////System.out.println(" ************************************************ ");
	        	

     	
	    		
			}
			catch (Exception e)
			{
				e.printStackTrace();
		//		Log.printLine("The simulation has been terminated due to an unexpected error");
			} 
		}
	public double[] getC_1_vm() {
		return c_1_vm;
	}
	public void setC_1_vm(double[] c_1_vm) {
		this.c_1_vm = c_1_vm;
	}  
	
	
}
