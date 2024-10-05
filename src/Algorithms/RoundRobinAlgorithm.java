package Algorithms;

import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.Cloudlet;

import java.util.List;

public class RoundRobinAlgorithm {
    public void runAlgorithm(DatacenterBroker broker, List<Vm> vmlist, List<Cloudlet> cloudletList) {
        // Round-Robin scheduling algorithm implementation
        int vmIndex = 0;
        for (Cloudlet cloudlet : cloudletList) {
            Vm vm = vmlist.get(vmIndex);
            cloudlet.setVmId(vm.getId());
            vmIndex = (vmIndex + 1) % vmlist.size(); // Circular allocation
        }
        System.out.println("Running Round Robin Algorithm...");
    }
}
