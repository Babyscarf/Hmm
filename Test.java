import java.util.Arrays;


public class Test {
	double[][] a;
	double[][] b;
	double[][] pi;
	int[] observations;
	double[][] delta;
	double[][] delta_temp;
	double[][] delta_matrix;
	int[][] delta_state_matrix;
	double[][] delta_max_vector;
	int[][] delta_state_index;
	//double[][] delta_mult_matrix = new double[][] {{0.2, 0.3, 0.005, 0.4},{0.2, 0.7, 0.005, 0.4}, {0.1, 0.7, 0.1, 0.9}, {0.1, 0.0, 0.5, 0.4}};
	double[][] delta_mult_matrix;
	double[][] result;

	
	HmmMethods hmm_methods = new HmmMethods();

	public static void main(String[] args) {
		new Test();

	}
	public Test(){
		test();
	}

	public void test(){
		/*HÅRDKODNING AV MATRISER
		 *  4 4 0.0 0.8 0.1 0.1 0.1 0.0 0.8 0.1 0.1 0.1 0.0 0.8 0.8 0.1 0.1 0.0 
			4 4 0.9 0.1 0.0 0.0 0.0 0.9 0.1 0.0 0.0 0.0 0.9 0.1 0.1 0.0 0.0 0.9 
			1 4 1.0 0.0 0.0 0.0 
			4 1 1 2 2  */ 
		a = new double[][] { { 0.0, 0.8, 0.1, 0.1 }, { 0.1, 0.0, 0.8, 0.1 },
				{ 0.1, 0.1, 0.0, 0.8 }, { 0.8, 0.1, 0.1, 0.0 } };
		b = new double[][] { { 0.9, 0.1, 0.0, 0.0 }, { 0.0, 0.9, 0.1, 0.0 },
				{ 0.0, 0.0, 0.9, 0.1  }, { 0.1, 0.0, 0.0, 0.9  } };
		pi = new double[][] { { 1.0, 0.0, 0.0, 0.0 } };
		observations = new int[] {1,1,2,2};
		// INIT av matriser
		delta_matrix = new double[observations.length][a.length]; //Ska innehålla alla max_delta-värde
		delta_state_matrix = new int[][] {{-1,-1,-1,-1},{-1,-1,-1,-1},{-1,-1,-1,-1},{-1,-1,-1,-1}};//[observations.length][a.length]; // Ska innehålla alla tillstånds index för max_index
		delta_max_vector = new double[1][a.length];
		int[][] solution_vector = new int[1][observations.length];
		//delta_state_index = new int[1][a.length];

		/*DELTA 1*/


		/*DELTA 2*/
	



	}
	
	public double[][] multViterbiMatrix(double[][] a, double[][] delta, double[][] b, int observation_index){
		double[][] result = new double[a.length][a.length];
		for(int i = 0; i<a.length; i++){
			for(int j = 0; j<a.length; j++){
				
				result[i][j] = a[j][i] * delta[0][j] * b[i][observation_index];
				System.out.println("result" + "["+i+"]"+"["+j+"] = " + a[j][i] + "*" + delta[0][j] + "*" + b[i][observation_index]+ "=" + a[j][i] * delta[0][j] * b[i][observation_index] );
				
			
			}System.out.println();
		}
		
		return result;
	}


	public void findMax(double[][] vector, double[][] max_vector, int[][] delta_state_index){
		int max_state_index = -1;
		for(int i = 0; i<vector.length; i++){
			double max = -1;

			for(int j = 0; j<vector[0].length; j++){

				double temp = vector[i][j];
				//System.out.println("temp: " + temp);
				if(temp > max){
					max = temp;
					max_state_index = j;
				}
				max_vector[0][i] = max;
				delta_state_index[0][i] = max_state_index;
			}
		}
	}


	public void findMaxSolution(double[][] delta_matrix, int[][] delta_state_matrix, int[][] solution_vector){

		for(int i = 0; i<delta_matrix.length; i++){
			double max = -1;
			int max_state_index_i = -1;
			int max_state_index_j = -1;
			for(int j = 0; j<delta_matrix[0].length; j++){
				double temp = delta_matrix[i][j];
				if(temp > max){
					max = temp;
					max_state_index_i = i;
					max_state_index_j = j;

				}
				solution_vector[0][0] = delta_state_matrix[max_state_index_i][max_state_index_j];

			}

		}

	}
}
