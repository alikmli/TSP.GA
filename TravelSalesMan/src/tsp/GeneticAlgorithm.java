package tsp;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class GeneticAlgorithm {
	private int populationSize;
	private double mutationRate;
	private double crossoverRate;
	private int elitismCount;
	private final int tournamentSize;

	public GeneticAlgorithm(int populationSize, double mutationRate, double crossoverRate, int elitismCount,
			int tournamentSize) {
		this.populationSize = populationSize;
		this.mutationRate = mutationRate;
		this.crossoverRate = crossoverRate;
		this.elitismCount = elitismCount;
		this.tournamentSize = tournamentSize;
	}

	public Population initPopulation(int chromsomeLength) {
		Population population = new Population(this.populationSize, chromsomeLength);
		return population;
	}

	public boolean isTerminationConditionMet(int generationsCount, int maxGenerations) {
		return (generationsCount > maxGenerations);
	}

	public Individuals RWSelction(Population population) {
		Individuals[] tmp = population.getPopulation();
		double getPopFitness = population.getPopulationFitness();
		SecureRandom random = new SecureRandom();
		double RWPosition = random.nextInt() * getPopFitness;

		double spinWheel = 0;
		for (int i = 0; i < tmp.length; i++) {
			spinWheel += tmp[i].getFitness();
			if (spinWheel >= RWPosition) {
				return tmp[i];
			}
		}

		return tmp[tmp.length - 1];
	}

	public double calcDistance(Individuals individuals, City[] routes) {
		Route route = new Route(individuals, routes);
		double routeDist = route.getDistance();
		if (routeDist == 0) {
			individuals.setFitness(0);
			return 0;
		}

		double fitness = 1 / routeDist;

		individuals.setFitness(fitness);
		return fitness;
	}

	public void evalPopulation(Population population, City cities[]) {
		double totalFitness = 0;
		for (int i = 0; i < population.populationSize(); i++) {
			totalFitness += calcDistance(population.getIndividual(i), cities);
		}

		double avgFitness = totalFitness / population.populationSize();
		population.setPopulationFitness(avgFitness);
	}

	public Individuals tournamentSelection(Population population) {
		Population tournamentPop = new Population(tournamentSize);

		tournamentPop.shuffle();
		for (int i = 0; i < tournamentSize; i++) {
			tournamentPop.setIndividual(i, population.getIndividual(i));
		}

		return tournamentPop.getFittest(0);
	}

	public Population orderedCrossover(Population population) {
		Population resultPop = new Population(population.populationSize());
		SecureRandom random = new SecureRandom();
		for (int i = 0; i < population.populationSize(); i++) {
			Individuals parent1 = population.getFittest(i);
			if (random.nextInt() < crossoverRate && i >= elitismCount) {
				Individuals parent2 = tournamentSelection(population);

				int[] offspringChromosome = new int[parent1.getChromsomeSize()];
				Arrays.fill(offspringChromosome, -1);
				Individuals offspring = new Individuals(offspringChromosome);

				int subPos1 = random.nextInt(parent1.getChromsomeSize());
				int subPos2 = random.nextInt(parent1.getChromsomeSize());

				final int startIndex = Math.min(subPos1, subPos2);
				final int endIndex = Math.max(subPos1, subPos2);

				for (int k = startIndex; k < endIndex; k++) {
					offspring.setGene(k, parent1.getGene(k));
				}

				for (int k = 0; k < parent2.getChromsomeSize(); k++) {
					int parent2Gene = k + endIndex;
					if (parent2Gene >= parent2.getChromsomeSize()) {
						parent2Gene -= parent2.getChromsomeSize();
					}

					if (offspring.containGene(parent2.getGene(parent2Gene)) == false) {
						for (int j = 0; j < offspring.getChromsomeSize(); j++) {
							if (offspring.getGene(j) == -1) {
								offspring.setGene(j, parent2.getGene(parent2Gene));
								break;
							}
						}
					}
				}
				resultPop.setIndividual(i, offspring);
			} else {
				resultPop.setIndividual(i, parent1);
			}
		}
		return resultPop;
	}

	public Population pmxCrossover(Population population) {
		Population resultPop = new Population(population.populationSize());
		SecureRandom random = new SecureRandom();
		for (int i = 0; i < population.populationSize(); i++) {
			Individuals parent1 = population.getFittest(i);
			if (random.nextInt() < crossoverRate && i >= elitismCount) {
				Individuals parent2 = RWSelction(population);

				int[] offspringChromosome = new int[parent1.getChromsomeSize()];
				Arrays.fill(offspringChromosome, -1);
				Individuals offspring = new Individuals(offspringChromosome);

				int subPos1 = random.nextInt(parent1.getChromsomeSize());
				int subPos2 = random.nextInt(parent1.getChromsomeSize());

				final int startIndex = Math.min(subPos1, subPos2);
				final int endIndex = Math.max(subPos1, subPos2);

				for (int k = startIndex; k < endIndex; k++) {
					offspring.setGene(k, parent1.getGene(k));
				}

				for (int k = startIndex; k < endIndex; k++) {
					int p2Gen = parent2.getGene(k);
					if (offspring.containGene(p2Gen)) {
						continue;
					} else {
						int tmpIndex = k;
						do {
							int value = offspring.getGene(tmpIndex);
							tmpIndex = parent2.getGeneIndex(value);
						} while (offspring.getGene(tmpIndex) != -1);
						offspring.setGene(tmpIndex, p2Gen);
					}
				}

				for (int k = 0; k < parent2.getChromsomeSize(); k++) {
					if (offspring.getGene(k) == -1) {
						offspring.setGene(k, parent2.getGene(k));
					}
				}
				resultPop.setIndividual(i, offspring);

			} else {
				resultPop.setIndividual(i, parent1);
			}
		}

		return resultPop;
	}

	public Population swapMutation(Population population, City[] cities) {
		Population resultPop = new Population(population.populationSize());
		SecureRandom random = new SecureRandom();
		for (int i = 0; i < population.populationSize(); i++) {
			Individuals individual = population.getFittest(i);
			if (i >= elitismCount) {
				for (int k = 0; k < individual.getChromsomeSize(); k++) {
					if (mutationRate > random.nextInt()) {
						int newGenePos = random.nextInt(individual.getChromsomeSize());
						int gene1 = individual.getGene(newGenePos);
						int gene2 = individual.getGene(k);

						individual.setGene(k, gene1);
						individual.setGene(newGenePos, gene2);
					}
				}
			}
			resultPop.setIndividual(i, individual);

		}
		return resultPop;
	}

	public Population insertMutation(Population population, City[] cities) {
		Population resultPop = new Population(population.populationSize());
		SecureRandom random = new SecureRandom();
		for (int i = 0; i < population.populationSize(); i++) {
			Individuals individual = population.getFittest(i);
			if (i >= elitismCount) {
				for (int k = 0; k < individual.getChromsomeSize(); k++) {
					if (mutationRate > random.nextInt()) {
						int tmp1 = k;
						int tmp2 = random.nextInt(individual.getChromsomeSize());

						int pos1 = Math.min(tmp1, tmp2);
						int pos2 = Math.max(tmp1, tmp2);

						if (pos1 == pos2)
							continue;
						int tmpValue = 1;

						for (int j = 0; j < individual.getChromsomeSize(); j++) {
							if (j == pos1 + 1) {
								tmpValue = individual.getGene(j);
								individual.setGene(j, individual.getGene(pos2));
							}
							if (j > pos1 + 1 && j <= pos2) {
								int tmp = individual.getGene(j);
								individual.setGene(j, tmpValue);
								tmpValue = tmp;
							}
						}

					}

				}

			}
			resultPop.setIndividual(i, individual);

		}

		return resultPop;
	}

	public Population scrambleMutation(Population population, City[] cities) {
		Population resultPop = new Population(population.populationSize());
		SecureRandom random = new SecureRandom();
		for (int i = 0; i < population.populationSize(); i++) {
			Individuals individual = population.getFittest(i);
			if (i >= elitismCount) {
				if (mutationRate > random.nextInt()) {
					int tmpIndex1 = random.nextInt(individual.getChromsomeSize());
					int tmpIndex2 = random.nextInt(individual.getChromsomeSize());
					int minIndex = Math.min(tmpIndex1, tmpIndex2);
					int maxIndex = Math.max(tmpIndex1, tmpIndex2);

					List<Integer> tmp = new LinkedList<Integer>();
					for (int index = minIndex; index < maxIndex; index++) {
						tmp.add(individual.getGene(index));
					}
					Collections.shuffle(tmp, new SecureRandom());

					for (int index = minIndex; index < maxIndex; index++) {
						individual.setGene(index, tmp.remove(0));
					}
				}
			}
			resultPop.setIndividual(i, individual);

		}

		return resultPop;
	}

	public Population inverseMutation(Population population, City[] cities) {
		Population resultPop = new Population(population.populationSize());
		SecureRandom random = new SecureRandom();
		for (int i = 0; i < population.populationSize(); i++) {
			Individuals individual = population.getFittest(i);
			if (i >= elitismCount) {
				for (int k = 0; k < individual.getChromsomeSize(); k++) {
					if (mutationRate > random.nextInt()) {
						int tmpIndex1 = k;
						int tmpIndex2 = random.nextInt(individual.getChromsomeSize());
						int minIndex = Math.min(tmpIndex1, tmpIndex2);
						int maxIndex = Math.max(tmpIndex1, tmpIndex2);

						List<Integer> tmp = new LinkedList<Integer>();
						for (int index = minIndex; index < maxIndex; index++) {
							tmp.add(individual.getGene(index));
						}
						for (int index = minIndex; index < maxIndex; index++) {
							individual.setGene(index, tmp.remove(tmp.size() - 1));
						}
					}
				}
			}
			resultPop.setIndividual(i, individual);

		}

		return resultPop;
	}

}
