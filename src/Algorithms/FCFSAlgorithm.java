package Algorithms;

import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.Cloudlet;

import java.util.List;

public class FCFSAlgorithm {
    public void runAlgorithm(DatacenterBroker broker, List<Vm> vmlist, List<Cloudlet> cloudletList) {
        // First-Come-First-Serve (FCFS) scheduling
        for (int i = 0; i < cloudletList.size(); i++) {
            Cloudlet cloudlet = cloudletList.get(i);
            Vm vm = vmlist.get(i % vmlist.size()); // Allocate in a sequential manner
            cloudlet.setVmId(vm.getId());
        }
        System.out.println("Running FCFS Algorithm...");
    }
}
