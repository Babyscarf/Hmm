import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;
import java.io.FileNotFoundException;

public class Backward {
	double[][] a;
	double[][] b;
	double[][] pi;
	double[][] result;
	int[] observations;
	Scanner sc;
	int row;
	int column;
	double[][] alpha;
	double[][] alpha_temporary; //Används denna?
	double end_summation=0;
	double[][] alpha_summation;

	HmmMethods hmm_methods = new HmmMethods();

	public static void main(String[] args) throws FileNotFoundException {

		new Backward();

	}

	public Backward() throws FileNotFoundException {

		backward();

	}

	private void backward() throws FileNotFoundException {
		sc = new Scanner(System.in).useLocale(Locale.US);
		// Hårkodning av matris 
//		a = new double[][] { { 0.0, 0.8, 0.1, 0.1 }, { 0.1, 0.0, 0.8, 0.1 },
//				{ 0.1, 0.1, 0.0, 0.8 }, { 0.8, 0.1, 0.1, 0.0 } };
//		b = new double[][] { { 0.9, 0.1, 0.0, 0.0 }, { 0.0, 0.9, 0.1, 0.0 },
//				{ 0.0, 0.0, 0.9, 0.1  }, { 0.1, 0.0, 0.0, 0.9 } };
//		pi = new double[][] { { 1.0, 0.0, 0.0, 0.0 } };
//		observations = new int[] {0,2,1,1};
//		//System.out.println(observations.length);
//		alpha = new double[1][observations.length];
		
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
		//System.out.println(Arrays.toString(observations));
		sc.close();
		
		/* Calculations of alpha_t(i)
		 * equations 2.9 - 2.14 in the lab manual*/
		
		System.out.println("pi: " + Arrays.deepToString(pi));
		alpha = hmm_methods.elementWiseProduct(pi, b, observations[0]); // Base case: init alpha_1(i)
		System.out.println("alpha_1: " + Arrays.deepToString(alpha));
		if(observations.length>1){
		 for (int i = 1; i < observations.length; i++) {
			int observation_index = observations[i];

			alpha_summation = hmm_methods.multMatrix(alpha, a); // part of Eq 2.12
			
			alpha = hmm_methods.elementWiseProduct(alpha_summation, b, observation_index); // last part of Eq 2.12: b_i(o_t)
		}
		 System.out.println("alpha_summation: " + Arrays.deepToString(alpha));
		System.out.println("alpha_t: " + Arrays.deepToString(alpha));
		for(int i = 0;i<alpha[0].length;i++){
			end_summation += alpha[0][i];
		//	System.out.println("end_summation=" + end_summation + "+" +alpha[0][i] );

		}}
		
		System.out.println(end_summation);

	}
}
