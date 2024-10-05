# CloudSim Scheduling Algorithms

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![CloudSim](https://img.shields.io/badge/CloudSim-0077B5?style=for-the-badge&logo=cloud&logoColor=white)
![Algorithms](https://img.shields.io/badge/Algorithms-%F0%9F%A7%91%E2%80%8D%F0%9F%94%A7-4C8CBF?style=for-the-badge)

This project simulates scheduling in a cloud computing environment using **CloudSim**. Multiple scheduling algorithms are implemented, allowing for flexibility in assigning tasks (cloudlets) to virtual machines (VMs).

## Features

- Implemented scheduling algorithms:
  - Round Robin
  - First-Come-First-Serve (FCFS)
  - Shortest Job First (SJF)
  - Genetic Algorithm
  - Ant Colony Optimization (ACO)

## How It Works

1. **Create Datacenter and VMs:** A datacenter with a pool of VMs is initialized.
2. **Cloudlets:** Multiple cloudlets (tasks) are created, each representing a unit of workload.
3. **Scheduling:** The user can select an algorithm to schedule cloudlets to VMs.
4. **Algorithms:** Based on the selected algorithm, the cloudlets are assigned to VMs in an optimized manner.
5. **Simulation:** The CloudSim engine runs the simulation and outputs the scheduling results.

## Algorithms Usage

```bash
Enter the algorithm to be used: (roundrobin, fcfs, ant, genetic, sjf): genetic
```

Choose any of the following algorithms:
- **roundrobin:** Round-robin scheduling.
- **fcfs:** First-Come-First-Serve scheduling.
- **ant:** Ant Colony Optimization scheduling.
- **genetic:** Genetic Algorithm-based scheduling.
- **sjf:** Shortest Job First scheduling.

## Requirements

- Java 8 or higher
- CloudSim library

## Setup

1. Clone the repository:
    ```bash
    git clone https://github.com/Ravikisha/CloudSim-Examples.git
    ```
2. Add the [CloudSim library](http://www.cloudbus.org/cloudsim/) to your project.
3. Compile and run the project:
    ```bash
    javac CloudSimDemo.java
    java CloudSimDemo
    ```

## Example Output

```plaintext
========== OUTPUT ==========
CloudletID     STATUS    DataCenterID   VM ID    Time      StartTime       FinishTime
0              SUCCESS   0              1        10.0      0.5            10.5
1              SUCCESS   0              0        8.5       1.0            9.5
```

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## Contributing

Feel free to fork this project, create issues, and make pull requests to contribute!

---

Happy Scheduling with CloudSim! 🚀
