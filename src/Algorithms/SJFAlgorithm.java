package Algorithms;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.Vm;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SJFAlgorithm {
    public void runAlgorithm(DatacenterBroker broker, List<Vm> vmlist, List<Cloudlet> cloudletList) {
        // Sort cloudlets by length (execution time)
        cloudletList.sort(Comparator.comparingLong(Cloudlet::getCloudletLength));

        // Allocate cloudlets to VMs using SJF
        for (Cloudlet cloudlet : cloudletList) {
            // Find the first available VM (or you can implement other logic to choose the VM)
            Vm selectedVm = vmlist.get(0); // This can be improved to find the least loaded VM
            
            // Bind cloudlet to selected VM
            broker.bindCloudletToVm(cloudlet.getCloudletId(), selectedVm.getId());
        }

        System.out.println("Running Shortest Job First (SJF) Algorithm...");
    }
}
