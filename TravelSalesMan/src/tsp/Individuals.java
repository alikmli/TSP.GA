package tsp;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Individuals {
	private int[] chromsome;
	private double fitness;
	
	
	public Individuals(int[] chromsome) {
		this.chromsome=chromsome;
	}
	
	public Individuals(int chromosomeLength) {
		int[] individual=getRandomIndividual(chromosomeLength).getChromsome();
		this.chromsome=individual;
	}
	
	public static Individuals getRandomIndividual(int chromosomeLength) {
		List<Integer> tmpCh=new ArrayList<Integer>();
		for(int i=0;i<chromosomeLength;i++)
			tmpCh.add(i);
		
		Collections.shuffle(tmpCh, new SecureRandom());
		
		int[] chrom=new int[chromosomeLength];
		for(int i=0;i<chromosomeLength;i++)
			chrom[i]=tmpCh.get(i);
		
		Individuals in=new Individuals(chrom);
		
		return in;
		
	}
	
	public void initChromsome(int value) {
		for(int i=0;i<chromsome.length;i++)
			chromsome[i]=value;
	}
	
	
	public void setGene(int index,int value) {
		chromsome[index]=value;
	}
	
	public int getGene(int index) {
		return chromsome[index];
	}

	public int[] getChromsome() {
		return chromsome;
	}

	public int getChromsomeSize() {
		return chromsome.length;
	}

	public double getFitness() {
		return fitness;
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}
	
	public boolean containGene(int gene) {
		for(int i=0;i<chromsome.length;i++) {
			if(chromsome[i] == gene)
				return true;
		}
		return false;
	}
	
	public int getGeneIndex(int gene) {
		for(int i=0;i<chromsome.length;i++) {
			if(chromsome[i] == gene)
				return i;
		}
			
		return -1;
	}
	
	public String toString() {
		StringBuilder str=new StringBuilder();
		for(int i=0;i<chromsome.length;i++) {
			str.append(chromsome[i]+ "  ");
		}
		
		return str.toString();
	}
	
	
	public void setChromsome(int[] chromsome) {
		this.chromsome = chromsome;
	}
}
