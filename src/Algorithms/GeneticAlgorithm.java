package Algorithms;

import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Vm;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Chromosome {
    private final int[] allocation; // allocation of cloudlets to VMs
    private double fitness; // fitness value of the chromosome

    public Chromosome(int cloudletCount, int vmCount) {
        allocation = new int[cloudletCount];
        Random random = new Random();
        for (int i = 0; i < cloudletCount; i++) {
            allocation[i] = random.nextInt(vmCount); // Randomly allocate cloudlets to VMs
        }
    }

    public int[] getAllocation() {
        return allocation;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public void mutate() {
        Random random = new Random();
        int index = random.nextInt(allocation.length);
        allocation[index] = random.nextInt(allocation.length); // Randomly change allocation
    }

    public void crossover(Chromosome partner) {
        Random random = new Random();
        int crossoverPoint = random.nextInt(allocation.length);
        for (int i = crossoverPoint; i < allocation.length; i++) {
            int temp = allocation[i];
            allocation[i] = partner.allocation[i];
            partner.allocation[i] = temp;
        }
    }
}

public class GeneticAlgorithm {
    private final int populationSize = 10; // Size of the population
    private final int generations = 100; // Number of generations
    private final double mutationRate = 0.1; // Mutation rate
    private List<Chromosome> population;
    private List<Cloudlet> cloudletList;
    private List<Vm> vmList;

    public void runAlgorithm(DatacenterBroker broker, List<Vm> vmlist, List<Cloudlet> cloudletList) {
        this.vmList = vmlist;
        this.cloudletList = cloudletList;

        initializePopulation();

        for (int gen = 0; gen < generations; gen++) {
            calculateFitness();
            List<Chromosome> newPopulation = select();
            population = newPopulation;
            if (gen < generations - 1) {
                mutate();
            }
        }

        // Submit the best solution to the broker
        Chromosome bestSolution = getBestSolution();
        for (int i = 0; i < bestSolution.getAllocation().length; i++) {
            broker.bindCloudletToVm(cloudletList.get(i).getCloudletId(), bestSolution.getAllocation()[i]);
        }

        System.out.println("Running Genetic Algorithm...");
    }

    private void initializePopulation() {
        population = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            population.add(new Chromosome(cloudletList.size(), vmList.size()));
        }
    }

    private void calculateFitness() {
        for (Chromosome chromosome : population) {
            double fitness = evaluateFitness(chromosome);
            chromosome.setFitness(fitness);
        }
    }

    private double evaluateFitness(Chromosome chromosome) {
        // Simple fitness function: minimize makespan
        double makespan = 0;
        int[] allocation = chromosome.getAllocation();
        for (int vmId = 0; vmId < vmList.size(); vmId++) {
            double vmTime = 0;
            for (int i = 0; i < allocation.length; i++) {
                if (allocation[i] == vmId) {
                    vmTime += cloudletList.get(i).getCloudletLength(); // Assume cloudlet length as execution time
                }
            }
            makespan = Math.max(makespan, vmTime);
        }
        return 1.0 / makespan; // Inverse of makespan for fitness
    }

    private List<Chromosome> select() {
        List<Chromosome> newPopulation = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < populationSize; i++) {
            Chromosome parent1 = population.get(random.nextInt(population.size()));
            Chromosome parent2 = population.get(random.nextInt(population.size()));
            parent1.crossover(parent2);
            newPopulation.add(parent1);
        }
        return newPopulation;
    }

    private void mutate() {
        for (Chromosome chromosome : population) {
            if (Math.random() < mutationRate) {
                chromosome.mutate();
            }
        }
    }

    private Chromosome getBestSolution() {
        return population.stream().max((c1, c2) -> Double.compare(c1.getFitness(), c2.getFitness())).orElse(null);
    }
}
