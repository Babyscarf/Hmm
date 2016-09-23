
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;


public class Hmm {

		double[][] a;
		double[][] b;
		double[][] pi;
		
		Scanner sc;
		int row;
		int column;
		HmmMethods hmm_methods = new HmmMethods();
		
	public static void main(String[] args) throws FileNotFoundException {
		
		new Hmm();
		
		
	}

		public Hmm() throws FileNotFoundException{
			
			nextEmission();
			
			
		
		}
		// Given the current state probability distribution, nextEmission()
		// gives the probability for the different emissions after the next transition
		private void nextEmission() throws FileNotFoundException {
			sc = new Scanner(System.in).useLocale(Locale.US);
			
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
			sc.close();
			//First, multiply pi*A, this will give the distribution of the states for
			//the next time step
			double[][] pi_2 = hmm_methods.multMatrix(pi,a);
			//Second, we multiply or current state distribution with our emission-matrix
			//This will give us the probability for the different observations after the next transition
			double[][] next_emisson_dist = hmm_methods.multMatrix(pi_2,b);
			System.out.println(Arrays.deepToString(next_emisson_dist));
			
			
		}
		
		
		
		
		
		
}
