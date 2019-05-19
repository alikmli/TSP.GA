package tsp;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;

public class TSP {
	static int numberOfCities = -1;
	static int Max_Gen = -1;
	static double mutationRate = -1;
	static double crossOverRate = -1;
	static int populationSize = -1;
	static final String FilePath = "./inputFile/sample3.txt";

	public static void main(String[] args) throws IOException, InterruptedException {
		City[] cities = initParams();

		GeneticAlgorithm gAlgorithm = new GeneticAlgorithm(populationSize, mutationRate, crossOverRate, 0, 5);
		Population population = gAlgorithm.initPopulation(cities.length);
		gAlgorithm.evalPopulation(population, cities);

		int generation = 1;
		int choosenGen = 1;
		double maxFitest = 0;
		double finalDist=0;
		String finalPath="";
		double finalFitness=0;

		StringBuilder builder = new StringBuilder();

		while (gAlgorithm.isTerminationConditionMet(generation, Max_Gen) == false) {
			double tmpMax = population.getFittest(0).getFitness();


			if (tmpMax*10000 > maxFitest*10000) {
				maxFitest = tmpMax;
				choosenGen = generation;
				
				finalDist=(new Route(population.getFittest(0), cities)).getDistance();
				finalFitness=population.getFittest(0).getFitness();
				finalPath=population.getFittest(0).toString();
			}

			System.err.println(generation + "\t" + tmpMax + "      " + choosenGen + "\t" + maxFitest );
			builder.append("\n\n" + generation + "\t" + tmpMax + "      " + choosenGen + "\t" + maxFitest + "\n");
			for (Individuals item : population.getPopulation()) {
				System.out.println(item + "  " + item.getFitness());
				builder.append(item + "  " + item.getFitness() + "\n");
			}

			population = gAlgorithm.orderedCrossover(population);
			population = gAlgorithm.scrambleMutation(population, cities);
			gAlgorithm.evalPopulation(population, cities);
			generation++;

			System.out.println();
			builder.append("\n");
		}

		System.out.println("Stoped after " + Max_Gen + " generation");
		System.err.println("Path : " + finalPath);
		System.out.println("Best Distance " + finalDist + "  Fitness " + finalFitness);
		try (FileWriter writer = new FileWriter(new File(FilePath), true)) {
			writer.write(builder.toString());
			writer.flush();
		}


	}

	public static City[] initParams() {
		City[] cities = null;
		try (RandomAccessFile RAF = new RandomAccessFile(FilePath, "r")) {
			for (int i = 0; i < 5; i++) {
				String value = RAF.readLine();
				switch (i) {
				case 0:
					numberOfCities = Integer.valueOf(value);
					break;
				case 1:
					Max_Gen = Integer.valueOf(value);
					break;
				case 2:
					mutationRate = Double.valueOf(value);
					break;
				case 3:
					crossOverRate = Double.valueOf(value);
					break;
				case 4:
					populationSize = Integer.valueOf(value);
					break;

				}
			}
			cities = new City[numberOfCities];

			for (int i = 0; i < numberOfCities; i++) {
				String[] ditsStr = RAF.readLine().split(" ");
				double[] dists = new double[numberOfCities];
				for (int k = 0; k < numberOfCities; k++) {
					dists[k] = Integer.valueOf(ditsStr[k]);
				}
				cities[i] = new City(i, dists);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return cities;
	}

}
