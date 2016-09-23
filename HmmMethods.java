import java.util.Arrays;
import java.util.Scanner;


public class HmmMethods {
	double[][] result;
	double[][] element_wise_product;
	int max_index = 0;
	
	public double[][] multMatrix(double[][] m1, double[][] m2){
		int number_of_rows_m1 = m1.length;
		int number_of_columns_m1 = m1[0].length;
		int number_of_rows_m2 = m2.length;
		int number_of_columns_m2 = m2[0].length;
		if(number_of_columns_m1 !=number_of_rows_m2)
			System.out.println("The matrix product is not defined: number_of_columns_m1 !=number_of_rows_m2 ");
		result = new double[number_of_rows_m1][number_of_columns_m2];
		for (int i=0; i<number_of_rows_m1; ++i)
		    for (int j=0; j<number_of_columns_m2; ++j)
		      for (int k=0; k<number_of_columns_m1; ++k) {
		        result[i][j] += m1[i][k] * m2[k][j];
		      }
		//System.out.println(Arrays.deepToString(result));
		return result;
	}
	
	public double[][] maxToMatrix(double[][] delta_temp, double[][] delta, int column_index){
		for(int i = 0; i<delta[0].length; i++){
			delta[i][column_index] = delta_temp[0][i];	
		}System.out.print("DELTA= " );
		System.out.println(Arrays.deepToString(delta));
		return delta;
	}
	
	public double[][] multViterbiMatrix(double[][] m1, double[][] m2, double[][] m3, int column_number){
		int number_of_rows_m1 = m1.length;
		System.out.println("number_of_rows_m1 = " + number_of_rows_m1);
		int number_of_columns_m1 = m1[0].length;
		int number_of_rows_m2 = m2.length;
		int number_of_columns_m2 = m2[0].length;
		
		if(number_of_columns_m1 !=number_of_rows_m2)
			System.out.println("The matrix product is not defined: number_of_columns_m1 !=number_of_rows_m2 ");
		result = new double[number_of_rows_m2][number_of_columns_m2];
		for (int i=0; i<number_of_rows_m1; ++i){
			
		    for (int j=0; j<number_of_columns_m2; ++j){
		    	
		      for (int k=0; k<number_of_columns_m1; ++k) {
		    	  
		        result[j][k] = m1[i][k] * m2[k][j] * m3[j][column_number] ;
//				System.out.println("result"+ "[" + j + "]" + "[" + k + "]" + "=" + m1[i][k] + "*"+  m2[k][j]+ "*"+ m3[j][column_number]
//						+"=" + result[k][j]);
		        
		      }
		      //System.out.println();
		      }}
		//System.out.println(Arrays.deepToString(result));
		return result;
	}
	public void findMax(double[][] find_max_matrix, double[][] max_vector, int[][] max_vector_index){
		double max;
		
		for(int i = 0; i < find_max_matrix.length; i++){
			max = -1;
			for(int j = 0; j < find_max_matrix[0].length; j++){
				double temp = find_max_matrix[i][j];
				if(temp > max){
					//System.out.println("temp = " + temp);
					max = temp;
					max_index = j;
					System.out.println("max_indexLoop = " + j);
					System.out.println("maxLoop = " + max);
				}
				
			}
			
			max_vector[0][i] = max;
			max_vector_index[0][i] = max_index;
			//System.out.println("max_value = " +max_vector[i] );
			//System.out.println("max_index = " + 	max_vector_index[i]);
			
		}
		
	}
	

	public void initMatrix(double[][] matrix, int row, int column, Scanner sc) {
		for(int i = 0; i < row; i++){
			for(int j = 0; j < column; j++){
				
				matrix[i][j] = sc.nextDouble();
				
				//System.out.print(matrix[i][j] +" ");
			}
			//System.out.println();
			
		}//System.out.println(Arrays.deepToString(matrix));
		//System.out.println();
		
	}
	public double[][] elementWiseProduct(double[][] matrix1,double[][] matrix2, int column_number){
		int number_of_columns_m1 = matrix1.length;
		int number_of_rows_m2 = matrix2.length;
		element_wise_product = new double[number_of_columns_m1][number_of_rows_m2];
		for(int i=0;i<matrix2.length;i++){
			element_wise_product[0][i]=matrix1[0][i]*matrix2[i][column_number];
			//System.out.println(element_wise_product[0][i]);
		}
		return element_wise_product;
	}
	
	public int[] initVector(int[] vector, Scanner sc){
		for(int i = 0; i<vector.length; i++){
			vector[i] = sc.nextInt();
		}
		return vector;
	}

}
