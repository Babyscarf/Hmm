import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;
import java.io.FileNotFoundException;

public class BaumWelch {
	double[][] a;
	double[][] b;
	double[][] pi;
	double[][] beta;
	double beta_temp;
	int[] observations;
	Scanner sc;
	int row;
	int column;
	double[][] alpha;
	double[][] alpha_temporary; // Används denna?
	double end_summation = 0;
	double[][] alpha_summation;
	double[][] beta_temp2;
	double scaling_factor;
	double denom;
	double[] C; // vector that stores the scaling values
	double[][][] di_gamma;
	double[][] gamma;
	double[][] beta_time;
	double[][] alpha_time;
	int time = 0;
	double[] test = new double[1];

	HmmMethods hmm_methods = new HmmMethods();

	public static void main(String[] args) throws FileNotFoundException {

		new BaumWelch();

	}

	public BaumWelch() throws FileNotFoundException {

		baumWelch();

	}

	private void baumWelch() throws FileNotFoundException {
		sc = new Scanner(System.in).useLocale(Locale.US);

		/* Read input from Kattis */
		row = sc.nextInt();
		column = sc.nextInt();
		a = new double[row][column];
		hmm_methods.initMatrix(a, row, column, sc);

		row = sc.nextInt();
		column = sc.nextInt();
		b = new double[row][column];
		hmm_methods.initMatrix(b, row, column, sc);

		row = sc.nextInt();
		column = sc.nextInt();

		pi = new double[row][column];
		hmm_methods.initMatrix(pi, row, column, sc);

		beta = new double[row][column];
		beta_temp2 = new double[row][column];

		observations = new int[sc.nextInt()];
		beta_time = new double[observations.length][a.length];
		alpha_time = new double[observations.length][a.length];
		di_gamma = new double[observations.length-1][a.length][a.length];//Ändrade från: [observations.length-1][a.length] till [a.length][a.length]
		gamma = new double[observations.length][a.length];
		C = new double[observations.length];

		observations = hmm_methods.initVector(observations, sc); // Init length
																	// of
																	// observation
																	// vector (#
																	// of
																	// observations
																	// given)
		int maxIters = 1000;
		int iters = 0;
		double oldLogProb = Integer.MIN_VALUE;
		double logProb = 0.0;
		
		while (true){
		forward(pi, a, b);
		backward(beta, a, b);
		gamma();
		reEstimate();
		
		// Compute log[P( O | lambda )]
		logProb = 0.0;
		for (int i = 0; i < observations.length-1; i++){
			logProb += Math.log(C[i]) / Math.log(10);
		}
		logProb = -logProb;
		
		// To iterate or not iterate?
		iters++;
		if ((iters < maxIters) && (logProb > oldLogProb)){
			oldLogProb = logProb;
		} else {
			break;
		}
		}
		printMatrix(a);
		printMatrix(b);
	}
	
	
	/*Prints matrix in correct kattis format*/
	private void printMatrix(double[][] matrix){
		System.out.print(matrix.length + " " + matrix[0].length + " ");
		for (int i = 0; i < matrix.length; i++){
			for (int j = 0; j < matrix[0].length; j++){
				System.out.print(matrix[i][j] + " ");	
			}
		}
		System.out.println();
	}
	
	
	/*Re-estimates A, B and pi matrices*/
	private void reEstimate() {
		// re-estimate pi
		for (int i = 0; i < a.length; i++)
			pi[0][i] = gamma[0][i];
		//System.out.println("pi: " + Arrays.deepToString(pi));
		// re-estimate A
		for (int i = 0; i < a.length; i++){
			for (int j = 0; j < a.length; j++){
				double numer = 0;
				denom = 0;
				for (int t = 0; t < observations.length-1; t++){
					numer += di_gamma[t][i][j]; // ändrade från [i][j] till [t][j]
					denom += gamma[t][i];
				}
				a[i][j] = numer / denom;
		}
		}
		//System.out.println("restimated a: " + Arrays.deepToString(a));
		
		// re-estimate B
		for (int i = 0; i < a.length; i++){
			for (int j = 0; j < b[0].length; j++){
				double numer = 0.0;
				denom = 0.0;
				for (int t = 0; t < observations.length-1; t++){
					if (observations[t] == j)
						numer += gamma[t][i];
					denom += gamma[t][i];
				}
				b[i][j] = numer / denom;
			}
		}
		//System.out.println("RE-ESTIMATED b: " + Arrays.deepToString(b));
		}
	
	
	/*
	 * Calculates gamma and di-gamma
	 * */
	private void gamma() {
		for (int t = 0; t < observations.length - 1; t++) {
			denom = 0;
			for (int i = 0; i < a.length; i++) {
				for (int j = 0; j < a.length; j++) {
					denom += alpha_time[t][i] * a[i][j] * b[j][observations[t + 1]] * beta_time[t + 1][j];
					// System.out.println(alpha_time[t][i] +"*"+ a[i][j] +" * "+
					// b[j][observations[t]] +"*"+ beta_time[t][j]);
					// System.out.print(alpha[0][i] + "*" + a[i][j] + "*" +
					// b[j][observations[t]] + "*" + beta[0][j] +" = " + denom);
				}
			}
			for (int i = 0; i < a.length; i++) {
				gamma[t][i] = 0;
				for (int j = 0; j < a.length; j++) {
					di_gamma[t][i][j] = (alpha_time[t][i] * a[i][j] * b[j][observations[t + 1]] * beta_time[t + 1][j]) / denom;
					gamma[t][i] += di_gamma[t][i][j];
				}
			}
		}
		//System.out.println("gamma_innan: " + Arrays.deepToString(gamma));
		
		// Special case
		denom = 0;
		for (int i = 0; i < a.length; i++){
			denom += alpha_time[observations.length-1][i];
		}
		for (int i = 0; i < a.length; i++){
			gamma[observations.length-1][i] = alpha_time[observations.length-1][i] / denom;
		}
		
	}
	/*Old scaling method*/
	private double[][] scaling(double[][] matrix_to_be_scaled) {
		scaling_factor = 0;
		
		for (int i = 0; i < matrix_to_be_scaled[0].length; i++)
			scaling_factor += matrix_to_be_scaled[0][i];// c_0 = c_0 + beta_0
			
		scaling_factor = 1 / scaling_factor;

		for (int i = 0; i < matrix_to_be_scaled[0].length; i++)
			matrix_to_be_scaled[0][i] = scaling_factor
					* matrix_to_be_scaled[0][i]; // c_0 scale
		return matrix_to_be_scaled;

	}
	
	/*Scales alphas and betas and stores the scaling factor in C vector*/
	private double[][] scaling2(double[][] matrix_to_be_scaled, double[] C, int index) {
		//scaling_factor = 0;
		C[index] = 0;
		for (int i = 0; i < matrix_to_be_scaled[0].length; i++)
			//scaling_factor += matrix_to_be_scaled[0][i];// c_0 = c_0 + beta_0
			C[index] += matrix_to_be_scaled[0][i];
		C[index] = 1 / C[index];

		for (int i = 0; i < matrix_to_be_scaled[0].length; i++)
			matrix_to_be_scaled[0][i] = C[index] * matrix_to_be_scaled[0][i]; // c_0 scale
		return matrix_to_be_scaled;

	}
	
	/* Calculates the alpha-pass*/
	private double[][] forward(double[][] pi, double[][] a, double[][] b) {
		// compute alpha_0(i)
		alpha = hmm_methods.elementWiseProduct(pi, b, observations[0]);
		C[0] = 0.0;
		for (int i = 0; i < a.length; i++)
			C[0] += alpha[0][i];
		// scale the alpha_0(i)
		C[0] = 1 / C[0];
		for (int i = 0; i < a.length; i++)
			alpha[0][i] = C[0] * alpha[0][i];
		//alpha = scaling(alpha);
		//System.out.println("Alpha scaled: " + Arrays.deepToString(alpha));
		int k = 0;
		timeValuesToMatrix(k, alpha, alpha_time);
		// alpha = hmm_methods.elementWiseProductBaumWelch(pi, b,
		// observations[0], alphaScale); // Base case: init alpha_1(i)
		// alpha = scaling(alpha);
		// System.out.println("Alpha scaled: " + Arrays.deepToString(alpha));

		// if(observations.length>1){

		for (int i = 1; i < observations.length; i++) {
			C[i] = 0;
			int observation_index = observations[i];
			alpha_summation = hmm_methods.multMatrix(alpha, a); // part of Eq 2.12
			alpha = hmm_methods.elementWiseProduct(alpha_summation, b,observation_index); // last part of Eq 2.12: b_i(o_t)
			//alpha = scaling(alpha);
			alpha = scaling2(alpha, C, i);
			//System.out.println("Alpha scaled: " + Arrays.deepToString(alpha));
			timeValuesToMatrix(i, alpha, alpha_time);
			// }
		}
		for (int i = 0; i < alpha[0].length; i++) {
			end_summation += alpha[0][i];
		}
		// double test=Math.log(end_summation) / Math.log(2);
		//System.out.println("alpha end_summation" + end_summation);
		return alpha;

	}

	/*
	 * Stores alpha/beta for each time, into matrix alpha_time. For use in gamma
	 */
	private void timeValuesToMatrix(int i, double[][] time_value,
			double[][] time_value_matrix) {
		time = a.length;
		int k = 0;
		while (time > 0) {
			time_value_matrix[i][k] = time_value[0][k];
			k++;
			time--;
		}

	}
	/* Calculates the beta-pass*/
	private double backward(double[][] beta, double[][] a, double[][] b) {
		int k = observations.length - 1;
		for (int i = 0; i < beta[0].length; i++)
			//beta[0][i] = 1;
			beta[0][i] = C[observations.length - 1];
		//beta = scaling(beta);
		timeValuesToMatrix(k, beta, beta_time);
		for (int t = observations.length - 1; t > 0; t--) {
			// System.out.println("t: " + t);
			for (int i = 0; i < a.length; i++) {
				for (int j = 0; j < a.length; j++) {
					beta_temp += a[i][j] * b[j][observations[t]] * beta[0][j];
					// System.out.print(a[i][j] + "*" + b[j][observations[t]] +
					// "*" + beta[0][j] +"\n");
				}
				// System.out.println();
				// System.out.println("=" +beta_temp);
				beta_temp2[0][i] = beta_temp;
				beta_temp = 0;
			}// Updates beta values
			for (int i = 0; i < beta[0].length; i++)
				beta[0][i] = beta_temp2[0][i];
			beta = scaling2(beta, C, t);
			//beta = scaling(beta);
			timeValuesToMatrix(t - 1, beta, beta_time);
		}
		return 0;
	}
}