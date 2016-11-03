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
	
	double[][] di_gamma;
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
		di_gamma = new double[observations.length-1][a.length];
		gamma = new double[observations.length][a.length];

		observations = hmm_methods.initVector(observations, sc); // Init length
																	// of
																	// observation
																	// vector (#
																	// of
																	// observations
																	// given)

		forward(pi, a, b);
		backward(beta, a, b);
		gamma();
		re_estimate();

	}

	private void re_estimate() {
		//re-estimate pi
		for (int i = 0; i < a.length; i++)
			pi[0][i] = gamma[0][i];
		System.out.println("pi: " + Arrays.deepToString(pi));
		//re-estimate A
		for (int i = 0; i < a.length; i++){
			for (int j = 0; j < a.length; j++){
				double numer = 0;
				denom = 0;
				for (int t = 0; t < observations.length-1; t++){
					
					numer += di_gamma[i][j];
					denom += gamma[t][i];
//					System.out.println("numer: " + numer);
//					System.out.println("denom: " + denom);
				}
				
				a[i][j] = numer / denom;
			
			}
		}
		System.out.println("restimated a: " + Arrays.deepToString(a));
	}

	private void gamma() {
		System.out.println("alpha: " + Arrays.deepToString(alpha));
		System.out.println("a: " + Arrays.deepToString(a));
		System.out.println("b: " + Arrays.deepToString(b));
		System.out.println("beta: " + Arrays.deepToString(beta));
		for (int t = 0; t < observations.length - 1; t++) {
			denom = 0;
			for (int i = 0; i < a.length; i++) {
				for (int j = 0; j < a.length; j++) {

					denom += alpha_time[t][i] * a[i][j]
							* b[j][observations[t + 1]] * beta_time[t + 1][j];
					// System.out.println(alpha_time[t][i] +"*"+ a[i][j] +" * "+
					// b[j][observations[t]] +"*"+ beta_time[t][j]);
					// System.out.print(alpha[0][i] + "*" + a[i][j] + "*" +
					// b[j][observations[t]] + "*" + beta[0][j] +" = " + denom);
				}
			}
			for (int i = 0; i < a.length; i++) {
				gamma[t][i] = 0;
				for (int j = 0; j < a.length; j++) {
					di_gamma[t][j] = (alpha_time[t][i] * a[i][j]
							* b[j][observations[t + 1]] * beta_time[t + 1][j])
							/ denom;
					gamma[t][i] += di_gamma[t][j];
				}
			}
		}
		//System.out.println("gamma: " + Arrays.deepToString(gamma));
		System.out.println("di_gamma: " + Arrays.deepToString(di_gamma));
		denom = 0;
		for (int i = 0; i < a.length; i++){
			denom += alpha_time[observations.length-1][i];
		}
		for (int i = 0; i < a.length; i++){
			gamma[observations.length-1][i] = alpha_time[observations.length-1][i] / denom;
		}
		System.out.println("gamma: " + Arrays.deepToString(gamma));

	}

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

	private double[][] forward(double[][] pi, double[][] a, double[][] b) {

		alpha = hmm_methods.elementWiseProduct(pi, b, observations[0]);
		System.out.println("Alpha: " + Arrays.deepToString(alpha));
		alpha = scaling(alpha);
		System.out.println("Alpha scaled: " + Arrays.deepToString(alpha));
		int k = 0;
		timeValuesToMatrix(k, alpha, alpha_time);
		// alpha = hmm_methods.elementWiseProductBaumWelch(pi, b,
		// observations[0], alphaScale); // Base case: init alpha_1(i)
		// alpha = scaling(alpha);
		// System.out.println("Alpha scaled: " + Arrays.deepToString(alpha));

		// if(observations.length>1){

		for (int i = 1; i < observations.length; i++) {
			int observation_index = observations[i];
			alpha_summation = hmm_methods.multMatrix(alpha, a); // part of Eq
																// 2.12
			alpha = hmm_methods.elementWiseProduct(alpha_summation, b,observation_index); // last part of Eq 2.12: b_i(o_t)
			alpha = scaling(alpha);
			timeValuesToMatrix(i, alpha, alpha_time);

			// }

		}
		System.out.println("alpha_time: " + Arrays.deepToString(alpha_time));
		// alpha = scaling(alpha);
		// System.out.println("Alpha scaled: " + Arrays.deepToString(alpha));

		for (int i = 0; i < alpha[0].length; i++) {
			end_summation += alpha[0][i];
		}

		// double test=Math.log(end_summation) / Math.log(2);
		System.out.println("alpha end_summation" + end_summation);
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

	private double backward(double[][] beta, double[][] a, double[][] b) {
		int k = observations.length - 1;
		for (int i = 0; i < beta[0].length; i++)
			beta[0][i] = 1;
		beta = scaling(beta);
		// System.out.println(Arrays.deepToString(beta));
		// System.out.println("Observations: " +
		// Arrays.toString((observations)));
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
			beta = scaling(beta);
			timeValuesToMatrix(t - 1, beta, beta_time);

			// System.out.println(Arrays.deepToString(beta));

		}
		System.out.println("beta_time: " + Arrays.deepToString(beta_time));

		return 0;
	}
}