


import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;
import java.io.FileNotFoundException;

public class Viterbi {
	double[][] a;
	double[][] b;
	double[][] pi;
	double[][] result;
	int[] observations;
	Scanner sc;
	int row;
	int column;
	double[][] delta;
	double[][] delta_1;
	double[][] delta_temporary;
	double[][] max_vector;
	int[][] max_vector_index;
	



	HmmMethods hmm_methods = new HmmMethods();

	public static void main(String[] args) throws FileNotFoundException {

		new Viterbi();

	}

	public Viterbi() throws FileNotFoundException {

		viterbi();

	}

	private void viterbi() throws FileNotFoundException {
		//sc = new Scanner(System.in).useLocale(Locale.US);
		// Hårkodning av matris 
		a = new double[][] { { 0.6, 0.1, 0.1, 0.1 }, { 0.0, 0.3, 0.2, 0.5 },
				{ 0.8, 0.1, 0.0, 0.1 }, { 0.2, 0.0, 0.1, 0.7 } };
		b = new double[][] { { 0.6, 0.2, 0.1, 0.1 }, { 0.1, 0.4, 0.1, 0.4 },
				{ 0.0, 0.0, 0.7, 0.3  }, { 0.0, 0.0, 0.1, 0.9 } };
		pi = new double[][] { { 0.5, 0.0, 0.0, 0.5 } };
		observations = new int[] {2,0,3,1};
		//System.out.println(observations.length);
		delta_1 = new double[1][observations.length];
		delta = new double[a.length][observations.length];
		max_vector = new double[1][observations.length];
		max_vector_index = new int[1][observations.length];
		
		/*Read input from Kattis*/
//		row = sc.nextInt();
//		column = sc.nextInt();
//		a = new double[row][column];
//		hmm_methods.initMatrix(a, row, column,sc);
//		
//		row = sc.nextInt();
//		column = sc.nextInt();
//		b = new double[row][column];
//		hmm_methods.initMatrix(b, row, column,sc);
//		
//		row = sc.nextInt();
//		column = sc.nextInt();
//		
//		pi = new double[row][column];
//		hmm_methods.initMatrix(pi, row, column,sc);
//		
//		
//		observations = new int[sc.nextInt()];
//		observations=hmm_methods.initVector(observations, sc); //Init length of observation vector (# of observations given)
//		//System.out.println(Arrays.toString(observations));
//		sc.close();
		
		/* Calculations of alpha_t(i)
		 * equations 2.9 - 2.14 in the lab manual*/
		
		System.out.println("pi: " + Arrays.deepToString(pi));
		delta_1 = hmm_methods.elementWiseProduct(pi, b, observations[0]); // Base case: init alpha_1(i)
		hmm_methods.maxToMatrix(delta_1, delta, 0); // HÅRDKODAT
		//System.out.println("delta_1: " + Arrays.deepToString(delta));
		
		delta_temporary = hmm_methods.multViterbiMatrix(delta_1, a, b, observations[1]); // HÅRDKODAT observations[1], ska vara [i]
		hmm_methods.findMax(delta_temporary, max_vector, max_vector_index);
		delta = hmm_methods.maxToMatrix(max_vector, delta, 1);
		System.out.println("delta_uppdaterad: " + Arrays.deepToString(delta));
	//	System.out.println("max_vector: " + Arrays.toString(max_vector) + "max_vector_index: " + Arrays.toString(max_vector_index));
		//System.out.println("delta_temporary: " + Arrays.deepToString(delta_temporary));
//		if(observations.length>1){
//		 for (int i = 1; i < observations.length; i++) {
//			int observation_index = observations[i];
//
//			alpha_summation = hmm_methods.multMatrix(alpha, a); // part of Eq 2.12
//			
//			alpha = hmm_methods.elementWiseProduct(alpha_summation, b, observation_index); // last part of Eq 2.12: b_i(o_t)
//		}
		 

	}
}

