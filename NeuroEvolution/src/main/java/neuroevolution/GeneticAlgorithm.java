package neuroevolution;

import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.util.TransferFunctionType;

public class GeneticAlgorithm {
	//instance variables
	private NeuralNetwork[] population;
	private double[] currentFitness;
	private final int POPULATION_SIZE = 10;
	private final int INPUT_SIZE = 3;
	private final int HIDDEN_SIZE = 8;
	private final int OUTPUT_SIZE = 1;
	private double MUTATION_RATE = 0.05;
	
	//default constructor. initialize
	public GeneticAlgorithm() {
		population = new NeuralNetwork[POPULATION_SIZE];
		currentFitness = new double[POPULATION_SIZE];
		for(int i = 0 ; i<POPULATION_SIZE;i++) {
			currentFitness[i] = 0.0;
			population[i] = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, INPUT_SIZE, HIDDEN_SIZE, OUTPUT_SIZE);
			population[i].randomizeWeights(-1, 1);
		}
	}
	
	//evolve the population of neural networks
	public void evolvePopulation(double[] fitness) {
		int[] topFit = mostFit(fitness);
		NeuralNetwork parent1 = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, INPUT_SIZE, HIDDEN_SIZE, OUTPUT_SIZE);
		NeuralNetwork parent2 = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, INPUT_SIZE, HIDDEN_SIZE, OUTPUT_SIZE);
		parent1.setWeights(ArrayUtils.toPrimitive(population[topFit[0]].getWeights()));
		parent2.setWeights(ArrayUtils.toPrimitive(population[topFit[1]].getWeights()));
		

		//create the next population using uniform crossover from the genes (weights) of the top 2 best performing neural networks
		for(int i = 0; i<POPULATION_SIZE;i++) {
			population[i] = createOffspring(parent1,parent2,true);
		}
	}
	
	//determine if a network should jump based off input
	public boolean shouldJump(double[] inputs, int network) {
		population[network].setInput(inputs);
		population[network].calculate();
		double output = population[network].getOutput()[0];
		return output >0.5;
	}
	
	//create offspring using uniform crossover with a chance for mutation
	private NeuralNetwork createOffspring(NeuralNetwork net1, NeuralNetwork net2, boolean applyMutation) {
		int weightLength = net1.getWeights().length;
		double[] firstWeights = ArrayUtils.toPrimitive(net1.getWeights());
		double[] secondWeights = ArrayUtils.toPrimitive(net2.getWeights());
		double[] newWeights = new double[weightLength];
	
		
		for(int i = 0; i<weightLength;i++) {
			int crossoverGene = (int)(Math.random());
			newWeights[i] = (crossoverGene == 0) ? firstWeights[i] : secondWeights[i];
			if(Math.random()<MUTATION_RATE) {
				//apply mutation
				if(applyMutation) 
					newWeights[i] = Math.random() * 2 - 1;
			}
		}
		
		NeuralNetwork offspring = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, INPUT_SIZE, HIDDEN_SIZE, OUTPUT_SIZE);
		offspring.setWeights(newWeights);
		return offspring;
	}
	
	//return the index position of the 2 most fit networks in the population
	private int[] mostFit(double[] fitness) {
		int[] fittest = new int[2];
		fittest[0] = 0;
		fittest[1] = 0;
		for(int i = 0; i<fitness.length;i++) {
			if(fitness[fittest[0]] < fitness[i])
				fittest[0] = i;
		}
		for(int i = 0; i<fitness.length;i++) {
			if(i == fittest[0])
				continue;
			if(fitness[fittest[1]] < fitness[i]) 
					fittest[1] = i;
		}

		return fittest;
	}
}
