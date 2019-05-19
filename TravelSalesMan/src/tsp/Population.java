package tsp;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Comparator;

public class Population {

	private Individuals[] population;
	private double populationFitness = -1;

	public Population(int populationSize) {
		population = new Individuals[populationSize];
	}

	public Population(int populationSize, int chromsomeLength) {
		population = new Individuals[populationSize];

		for (int i = 0; i < populationSize; i++) {
			population[i] = new Individuals(chromsomeLength);
		}
	}

	public Individuals[] getPopulation() {
		return population;
	}

	public double getPopulationFitness() {
		return populationFitness;
	}

	public void setPopulationFitness(double populationFitness) {
		this.populationFitness = populationFitness;
	}

	public int populationSize() {
		return population.length;
	}

	public void setIndividual(int index, Individuals item) {
		population[index] = item;
	}

	public Individuals getIndividual(int index) {
		return population[index];
	}

	public Individuals getFittest(int offset) {
		Arrays.sort(population, new Comparator<Individuals>() {

			@Override
			public int compare(Individuals arg0, Individuals arg1) {
				return arg0.getFitness() > arg1.getFitness() ? -1 : arg0.getFitness() < arg1.getFitness() ? 1 : 0;
			}
		});

		return population[offset];
	}

	public void shuffle() {
		SecureRandom random = new SecureRandom();
		for (int i = population.length - 1; i >= 0; i--) {
			int index = random.nextInt(i + 1);
			Individuals tmp = population[index];
			population[index] = population[i];
			population[i] = tmp;
		}
	}

}
