package Algorithms;

import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.Cloudlet;

import java.util.ArrayList;
import java.util.List;

class Ant {
    private Cloudlet cloudlet;
    private Vm vm;

    public Ant(Cloudlet cloudlet) {
        this.cloudlet = cloudlet;
    }

    public Cloudlet getCloudlet() {
        return cloudlet;
    }

    public Vm getVm() {
        return vm;
    }

    public void setVm(Vm vm) {
        this.vm = vm;
    }
}

public class ACOAlgorithm {
    private final int numAnts = 10; // Number of ants
    private final double evaporationRate = 0.5; // Pheromone evaporation rate
    private final double pheromoneIncrease = 1.0; // Pheromone increase for a successful path
    private final int iterations = 100; // Number of iterations for the algorithm
    private List<Cloudlet> cloudletList;
    private List<Vm> vmList;

    public void runAlgorithm(DatacenterBroker broker, List<Vm> vmlist, List<Cloudlet> cloudletList) {
        this.vmList = vmlist;
        this.cloudletList = cloudletList;

        double[][] pheromoneMatrix = initializePheromoneMatrix();
        List<Ant> ants = new ArrayList<>();

        for (int iter = 0; iter < iterations; iter++) {
            ants = createAnts();
            for (Ant ant : ants) {
                Vm selectedVm = selectVm(ant, pheromoneMatrix);
                ant.setVm(selectedVm);
                // Update pheromone matrix based on the cloudlet allocation
                updatePheromone(pheromoneMatrix, ant, selectedVm);
            }
            evaporatePheromones(pheromoneMatrix);
        }

        // Submit the allocated cloudlets to the broker
        for (Ant ant : ants) {
            broker.bindCloudletToVm(ant.getCloudlet().getCloudletId(), ant.getVm().getId());
        }

        System.out.println("Running ACO Algorithm...");
    }

    private double[][] initializePheromoneMatrix() {
        double[][] pheromoneMatrix = new double[cloudletList.size()][vmList.size()];
        for (int i = 0; i < cloudletList.size(); i++) {
            for (int j = 0; j < vmList.size(); j++) {
                pheromoneMatrix[i][j] = 1.0; // Initialize pheromones uniformly
            }
        }
        return pheromoneMatrix;
    }

    private List<Ant> createAnts() {
        List<Ant> ants = new ArrayList<>();
        for (Cloudlet cloudlet : cloudletList) {
            ants.add(new Ant(cloudlet));
        }
        return ants;
    }

    private Vm selectVm(Ant ant, double[][] pheromoneMatrix) {
        // Implement a probability distribution based on pheromone levels to select a VM
        // For simplicity, randomly select a VM from the list
        int randomIndex = (int) (Math.random() * vmList.size());
        return vmList.get(randomIndex);
    }

    private void updatePheromone(double[][] pheromoneMatrix, Ant ant, Vm selectedVm) {
        int cloudletIndex = cloudletList.indexOf(ant.getCloudlet());
        int vmIndex = vmList.indexOf(selectedVm);
        pheromoneMatrix[cloudletIndex][vmIndex] += pheromoneIncrease; // Increase pheromone for successful allocation
    }

    private void evaporatePheromones(double[][] pheromoneMatrix) {
        for (int i = 0; i < pheromoneMatrix.length; i++) {
            for (int j = 0; j < pheromoneMatrix[i].length; j++) {
                pheromoneMatrix[i][j] *= (1 - evaporationRate); // Evaporate pheromone
            }
        }
    }
}
