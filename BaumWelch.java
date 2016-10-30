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
	double[][] alpha_temporary; //Används denna?
	double end_summation=0;
	double[][] alpha_summation;
	double[][] beta_temp2;
	double scaling_factor;

	HmmMethods hmm_methods = new HmmMethods();

	public static void main(String[] args) throws FileNotFoundException {

		new BaumWelch();

	}

	public BaumWelch() throws FileNotFoundException {

		baumWelch();

	}

	private void baumWelch() throws FileNotFoundException {
		sc = new Scanner(System.in).useLocale(Locale.US);

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

		beta = new double[row][column];
		beta_temp2= new double[row][column];


		observations = new int[sc.nextInt()];
		observations=hmm_methods.initVector(observations, sc); //Init length of observation vector (# of observations given)

		//forward(pi,a,b);
		backward(beta,a,b);



	}
	
	private double[][] scaling(double[][] matrix_to_be_scaled){
		scaling_factor = 0;
		for(int i=0;i<matrix_to_be_scaled[0].length;i++)
			scaling_factor += matrix_to_be_scaled[0][i];//c_0 = c_0 + beta_0
		
		scaling_factor = 1 / scaling_factor;
		
		for(int i=0;i<matrix_to_be_scaled[0].length;i++)
			matrix_to_be_scaled[0][i] = scaling_factor * matrix_to_be_scaled[0][i]; //c_0 scale
		return matrix_to_be_scaled;
		
	}



	private double[][] forward(double[][] pi, double[][] a, double[][] b){
		
		alpha = hmm_methods.elementWiseProduct(pi, b, observations[0]);
		//alpha = hmm_methods.elementWiseProductBaumWelch(pi, b, observations[0], alphaScale); // Base case: init alpha_1(i)
		alpha = scaling(alpha);
		System.out.println("Alpha scaled: " + Arrays.deepToString(alpha));
		
		
		if(observations.length>1){
			for (int i = 1; i < observations.length; i++) {
				int observation_index = observations[i];
				alpha_summation = hmm_methods.multMatrix(alpha, a); // part of Eq 2.12
				alpha = hmm_methods.elementWiseProduct(alpha_summation, b, observation_index); // last part of Eq 2.12: b_i(o_t)
			}
		}
	
		alpha = scaling(alpha);
		System.out.println("Alpha scaled: " + Arrays.deepToString(alpha));

			
		//double test=Math.log(end_summation) / Math.log(2);
		//System.out.println("test"+ test);
		return alpha;

	}

	private double backward(double[][] beta, double[][] a, double[][] b){
		
		for(int i = 0; i < beta[0].length; i++ )
			beta[0][i] = 1;
		beta = scaling(beta);
		System.out.println(Arrays.deepToString(beta));
			
			for(int t = observations.length-1; t>0; t--){
				for(int i = 0; i<a.length;i++){
					for(int j = 0; j<a.length; j++){
						beta_temp += a[i][j] * b[j][observations[t]]*beta[0][j];
						System.out.print(a[i][j] + "*" + b[j][observations[t]] + "*" + beta[0][j] +" + ");
					}
					System.out.println("=" +beta_temp);
					
					beta_temp2[0][i] = beta_temp;
					beta_temp = 0;
					
	
				} 
				 for(int i=0;i<beta[0].length;i++)
					 beta[0][i]=beta_temp2[0][i];
				beta = scaling(beta);
				//beta=beta_temp2;
				System.out.println(Arrays.deepToString(beta));
				
			}

		
		return 0;
	}
}