import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.core.CloudSim;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;

import Algorithms.ACOAlgorithm;
import Algorithms.FCFSAlgorithm;
import Algorithms.GeneticAlgorithm;
import Algorithms.RoundRobinAlgorithm;
import Algorithms.SJFAlgorithm;

public class CloudSimDemo {
    public static void main(String[] args) {
        try {
            // Initialize the CloudSim package
            int numUser = 1; // number of cloud users
            Calendar calendar = Calendar.getInstance();
            boolean traceFlag = false; // mean trace events

            // Initialize CloudSim
            CloudSim.init(numUser, calendar, traceFlag);

            // Create Datacenter
            Datacenter datacenter0 = createDatacenter("Datacenter_0");

            // Create DatacenterBroker
            DatacenterBroker broker = createBroker();
            int brokerId = broker.getId();

            // Create VMs and Cloudlets
            List<Vm> vmList = createVMs(brokerId, 5); // creating 5 VMs
            List<Cloudlet> cloudletList = createCloudlets(brokerId, 10); // creating 10 Cloudlets

            // Submit VM list to the broker
            broker.submitGuestList(vmList);
            // submit cloudlet list to the broker
            broker.submitCloudletList(cloudletList);

            // Apply Genetic Algorithm for Scheduling Cloudlets to VMs
            // List<Integer> gaSchedule = runGeneticAlgorithm(vmList.size(), cloudletList.size());
            // List<Integer> gaSchedule = runRoundRobinAlgorithm(vmList.size(), cloudletList.size());
            // List<Integer> gaSchedule = runFirstComeFirstServeAlgorithm(vmList.size(), cloudletList.size());
            // List<Integer> gaSchedule = runSJFAlgorithm(vmList, cloudletList);

            // Assign Cloudlets to VMs based on GA output
            // for (int i = 0; i < cloudletList.size(); i++) {
            //     if (i < cloudletList.size() && gaSchedule.get(i) < vmList.size()) {
            //         Vm vm = vmList.get(gaSchedule.get(i));
            //         Cloudlet cloudlet = cloudletList.get(i);
            //         broker.bindCloudletToVm(cloudlet.getCloudletId(), vm.getId());
            //     } else {
            //         System.out.println("Invalid Cloudlet or VM index: " + i);
            //     }
            // }

            Scanner sc = new Scanner(System.in);
            System.out.println("Enter the algorithm to be used: (roundrobin, fcfs, ant, genetic, sjf): ");
            String algorithm = sc.next();
            switch (algorithm.toLowerCase()) {
                // case "genetic":
                case "roundrobin":
                    RoundRobinAlgorithm rrAlgo = new RoundRobinAlgorithm();
                    rrAlgo.runAlgorithm(broker, vmList, cloudletList);
                    break;
                case "fcfs":
                    FCFSAlgorithm fcfsAlgo = new FCFSAlgorithm();
                    fcfsAlgo.runAlgorithm(broker, vmList, cloudletList);
                    break;
                case "ant":
                    ACOAlgorithm antAlgo = new ACOAlgorithm();
                    antAlgo.runAlgorithm(broker, vmList, cloudletList);
                    break;
                case "genetic":
                    GeneticAlgorithm geneticAlgo = new GeneticAlgorithm();
                    geneticAlgo.runAlgorithm(broker, vmList, cloudletList);
                    break;
                case "sjf":
                    SJFAlgorithm sjfAlgo = new SJFAlgorithm();
                    sjfAlgo.runAlgorithm(broker, vmList, cloudletList);
                    break;
                default:
                    Log.printLine("Invalid algorithm selection");
                    return;
            }

            // Start the simulation
            CloudSim.startSimulation();

            // Stop the simulation
            CloudSim.stopSimulation();

            // Print results
            List<Cloudlet> newList = broker.getCloudletReceivedList();
            printCloudletList(newList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    

    // Create a simple Datacenter
    private static Datacenter createDatacenter(String name) {
        // Here are the steps needed to create a PowerDatacenter:
        // 1. We need to create a list to store
        // our machine
        List<Host> hostList = new ArrayList<>();

        // 2. A Machine contains one or more PEs or CPUs/Cores.
        // In this example, it will have only one core.
        List<Pe> peList = new ArrayList<>();

        int mips = 1000;

        // 3. Create PEs and add these into a list.
        peList.add(new Pe(0, new PeProvisionerSimple(mips))); // need to store Pe id and MIPS Rating

        // 4. Create Host with its id and list of PEs and add them to the list
        // of machines
        int ram = 2048; // host memory (MB)
        long storage = 1000000; // host storage
        int bw = 10000;

        for (int i = 0; i < 5; i++) {
            hostList.add(
                    new Host(
                            i,
                            new RamProvisionerSimple(ram),
                            new BwProvisionerSimple(bw),
                            storage,
                            peList,
                            new VmSchedulerTimeShared(peList)));
        }

        // 5. Create a DatacenterCharacteristics object that stores the
        // properties of a data center: architecture, OS, list of
        // Machines, allocation policy: time- or space-shared, time zone
        // and its price (G$/Pe time unit).
        String arch = "x86"; // system architecture
        String os = "Linux"; // operating system
        String vmm = "Xen";
        double time_zone = 10.0; // time zone this resource located
        double cost = 3.0; // the cost of using processing in this resource
        double costPerMem = 0.05; // the cost of using memory in this resource
        double costPerStorage = 0.001; // the cost of using storage in this
                                       // resource
        double costPerBw = 0.0; // the cost of using bw in this resource
        LinkedList<Storage> storageList = new LinkedList<>(); // we are not adding SAN
        // devices by now

        DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
                arch, os, vmm, hostList, time_zone, cost, costPerMem,
                costPerStorage, costPerBw);

        // 6. Finally, we need to create a PowerDatacenter object.
        Datacenter datacenter = null;
        try {
            datacenter = new Datacenter(name, characteristics, new VmAllocationPolicySimple(hostList), storageList, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return datacenter;
    }

    // Create Broker
    private static DatacenterBroker createBroker() {
        DatacenterBroker broker = null;
        try {
            broker = new DatacenterBroker("Broker");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return broker;
    }

    // Create VM
    private static List<Vm> createVMs(int brokerId, int count) {
        List<Vm> list = new ArrayList<>();

        // VM description
        int mips = 1000;
        int ram = 512; // VM memory (MB)
        long bw = 1000;
        long size = 10000; // VM image size (GB)
        String vmm = "Xen"; // VMM name

        for (int i = 0; i < count; i++) {
            list.add(new Vm(i, brokerId, mips, 1, ram, bw, size, vmm, new CloudletSchedulerTimeShared()));
        }

        return list;
    }

    // Create Cloudlets
    private static List<Cloudlet> createCloudlets(int brokerId, int count) {
        List<Cloudlet> list = new ArrayList<>();
        long length = 40000;
        int pesNumber = 1;
        long fileSize = 300;
        long outputSize = 300;
        UtilizationModel utilizationModel = new UtilizationModelFull();

        for (int i = 0; i < count; i++) {
            Cloudlet cloudlet = new Cloudlet(i, length, pesNumber, fileSize, outputSize, utilizationModel,
                    utilizationModel, utilizationModel);
            cloudlet.setUserId(brokerId);
            cloudlet.setGuestId(0);
            list.add(cloudlet);
        }

        return list;
    }

    public static int getRandomIntInRange(int min, int max) {
        Random rand = new Random();
        // nextInt(max - min + 1) generates a number between 0 and (max-min),
        // so adding min ensures it's within the desired range
        return rand.nextInt((max - min) + 1) + min;
    }

    // Simulate Genetic Algorithm for Scheduling
    private static List<Integer> runGeneticAlgorithm(int vmCount, int cloudletCount) {
        List<Integer> schedule = new ArrayList<>();
        // Random rand = new Random();

        // Randomly assign each Cloudlet to a VM as the initial population
        for (int i = 0; i < cloudletCount; i++) {
            schedule.add(getRandomIntInRange(0, vmCount - 1));
        }

        // Normally you would have fitness functions and selection/crossover/mutation
        // here,
        // but this is a simple random scheduler for illustration.
        return schedule;
    }

    // Simulate Round Robin Algorithm for Scheduling
    private static List<Integer> runRoundRobinAlgorithm(int vmCount, int cloudletCount) {
        List<Integer> schedule = new ArrayList<>();

        for (int i = 0; i < cloudletCount; i++) {
            schedule.add(i % vmCount);
        }

        return schedule;
    }

    // Simulate First Come First Serve Algorithm for Scheduling
    private static List<Integer> runFirstComeFirstServeAlgorithm(int vmCount, int cloudletCount) {
        List<Integer> schedule = new ArrayList<>();

        for (int i = 0; i < cloudletCount; i++) {
            schedule.add(0);
        }

        return schedule;
    }

    // Simulate Shortest Job First Algorithm for Scheduling
    private static List<Integer> runSJFAlgorithm(List<Vm> vmList, List<Cloudlet> cloudletList) {
        List<Integer> schedule = new ArrayList<>();
        List<Cloudlet> sortedCloudletList = new ArrayList<>(cloudletList);
        sortedCloudletList.sort((a, b) -> Long.compare(a.getCloudletLength(), b.getCloudletLength()));

        for (int i = 0; i < cloudletList.size(); i++) {
            schedule.add(0);
        }

        return schedule;
    }

    // Simulate Ant Colony Optimization Algorithm for Scheduling


    // Print the results of Cloudlets execution
    private static void printCloudletList(List<Cloudlet> list) {
        System.out.println("========== OUTPUT ==========");
        // System.out.println("Cloudlet ID" + indent + "STATUS" + indent + "Data center
        // ID" + indent + "VM ID" + indent
        // + "Time" + indent + "Start Time" + indent + "Finish Time");
        System.out.printf("%-15s%-10s%-15s%-10s%-10s%-15s%-15s\n", "CloudletID", "STATUS", "DataCenterID", "VM ID",
                "Time", "StartTime", "FinishTime");

        for (Cloudlet cloudlet : list) {
            // System.out.print(indent + cloudlet.getCloudletId() + indent + indent);

            if (cloudlet.getStatus() == Cloudlet.CloudletStatus.SUCCESS) {
                // System.out.println("SUCCESS" + indent + indent + cloudlet.getResourceId() +
                // indent + indent
                // + cloudlet.getVmId() + indent + indent + String.format("%.5f",
                // cloudlet.getActualCPUTime()) + indent + indent
                // + cloudlet.getExecStartTime() + indent + indent + cloudlet.getFinishTime());
                System.out.printf("%-15d%-10s%-15d%-10d%-10.2f%-15.2f%-15.2f\n", cloudlet.getCloudletId(), "SUCCESS",
                        cloudlet.getResourceId(), cloudlet.getVmId(), cloudlet.getActualCPUTime(),
                        cloudlet.getExecStartTime(), cloudlet.getFinishTime());
            }
        }
    }
}
