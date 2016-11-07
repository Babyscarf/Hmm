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
	double[][] delta_matrix;
	double[][] delta;
	double[][] delta_mult_matrix;
	//double[][] delta_temp;
	int[][] delta_transition_index;
	int[][] delta_transition_matrix;
	int[][] solution;
	int[] stateSpace;
	//int[][]delta_transition_index2;
	



	HmmMethods hmm_methods = new HmmMethods();

	public static void main(String[] args) throws FileNotFoundException {

		new Viterbi();

	}

	public Viterbi() throws FileNotFoundException {

		viterbi();

	}

	private void viterbi() throws FileNotFoundException {
		sc = new Scanner(System.in).useLocale(Locale.US);
		// HÅRDKODNING AV MATRISER
//		a = new double[][] { { 0.6, 0.1, 0.1, 0.1 }, { 0.0, 0.3, 0.2, 0.5 },
//				{ 0.8, 0.1, 0.0, 0.1 }, { 0.2, 0.0, 0.1, 0.7 } };
//		b = new double[][] { { 0.6, 0.2, 0.1, 0.1 }, { 0.1, 0.4, 0.1, 0.4 },
//				{ 0.0, 0.0, 0.7, 0.3  }, { 0.0, 0.0, 0.1, 0.9 } };
//		pi = new double[][] { { 0.5, 0.0, 0.0, 0.5 } };
//		observations = new int[] {2,0,3,1};
		//System.out.println(observations.length);
		
		
		/*Read input from Kattis*/
		row = sc.nextInt();
		column = sc.nextInt();
		a = new double[row][column];
		hmm_methods.initMatrix(a, row, column,sc);
		
		row = sc.nextInt();
		column = sc.nextInt();
		b = new double[row][column];
		hmm_methods.initMatrix(b, row, column,sc);
		
		row = sc.nextInt();
		column = sc.nextInt();
		
		pi = new double[row][column];
		hmm_methods.initMatrix(pi, row, column,sc);
		
		
		observations = new int[sc.nextInt()];
		observations=hmm_methods.initVector(observations, sc); //Init length of observation vector (# of observations given)
		
		sc.close();
		
		/*Init matrices*/
		delta = new double[1][a.length];
		delta_matrix = new double[observations.length][a.length];
		//delta_temp = new double[1][a.length];
		delta_transition_index = new int[1][a.length];
		//delta_transition_index2 = new int[1][observations.length];
		delta_transition_matrix = new int[observations.length][a.length];
		solution=new int[1][observations.length];
		int[] stateSpace=new int[b.length];
		for(int i=0;i<b.length;i++)
			stateSpace[i]=i;
		
		/* Calculations of alpha_t(i)
		 * equations 2.9 - 2.14 in the lab manual*/
		
		delta = hmm_methods.elementWiseProduct(pi, b, observations[0]); // Base case: init delta_1(i)
		hmm_methods.deltaMax(delta_matrix, delta, delta_transition_matrix);
		
		for(int i = 1; i < observations.length; i++){
			int observation_index = observations[i];
			delta_mult_matrix = hmm_methods.multViterbiMatrix(delta, a, b, observation_index);
			hmm_methods.findMax(delta_mult_matrix, delta, delta_transition_index);
			
			//delta = delta_temp;
//			System.out.println("delta: " + i + Arrays.deepToString(delta));
//			System.out.println("delta_matrix_index: " + i + Arrays.deepToString(delta_matrix));
//			System.out.println("delta_transition_index: " + i + Arrays.deepToString(delta_transition_index));
			hmm_methods.maxToMatrix(delta, delta_matrix, delta_transition_index, delta_transition_matrix, i);
			
			}
//		    System.out.println();
//		    System.out.println();
//			System.out.println("delta_matrix: " + Arrays.deepToString(delta_matrix));
			//System.out.println("delta_transition_matrix: " + Arrays.deepToString(delta_transition_matrix));
			
			hmm_methods.findMaxSolution(delta_matrix, delta_transition_matrix, solution,stateSpace);
		//	System.out.println("Solution: " + solution[0].length);
		    
			hmm_methods.writeAnswer(solution);
		 

	}
}