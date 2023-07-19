/*Bài 1:
a)Nhập vào ma trận xác suất kết hợp P(x,y) có cỡ MxN với M và N nhập từbàn phím. 
Cảnh báo nếu nhập xác suất âm, yêu cầu nhập lại.
b)Tính và hiển thị H(X), H(Y ),H(X | Y ), H(Y | X),H(X, Y ),H(Y ) − H(Y | X),I(X; Y )
c)Tính D(P(x)||P(y)) và D(P(y)||P(x)) 
*/
import java.util.Scanner;

public class Bai1 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        //Nhập kích thước ma trận xác suất
        System.out.print("Nhập số hàng (M): ");
        int M = sc.nextInt();
        System.out.print("Nhập số cột (N): ");
        int N = sc.nextInt();

        // Khởi tạo ma trận xác suất
        double[][] probability_Matrix = new double[M][N];

        // Nhập các giá trị xác suất
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                System.out.printf("Nhập giá trị xác suất P(%d, %d): ", i, j);
                String input = sc.next();

                // Kiểm tra kiểu dữ liệu của đầu vào
                if (input.contains("/")) {
                    // Trường hợp là phân số dạng chuỗi
                    String[] parts = input.split("/");
                    int numerator = Integer.parseInt(parts[0]);
                    int denominator = Integer.parseInt(parts[1]);
                    double decimal = (double) numerator / denominator;

                    // Kiểm tra mẫu số khác 0
                    while (decimal < 0) {
                        System.out.println("khong duoc < 0. Vui lòng nhập lại.");
                        System.out.printf("Nhập giá trị xác suất P(%d, %d): ", i, j);
                        input = sc.next();
                        parts = input.split("/");
                        numerator = Integer.parseInt(parts[0]);
                        denominator = Integer.parseInt(parts[1]);
                        decimal = (double) numerator / denominator;
                    }

                    // Gán giá trị phân số vào ma trận xác suất
                    probability_Matrix[i][j] = decimal;
                } else {

                    // Trường hợp là số thập phân
                    double decimal = Double.parseDouble(input);
                    while (decimal< 0)
                    {
                        System.out.println("khong duoc < 0. Vui lòng nhập lại.");
                        System.out.printf("Nhập giá trị xác suất P(%d, %d): ", i, j);
                        decimal = sc.nextDouble();
                    }

                    // Gán giá trị số thập phân vào ma trận xác suất
                    probability_Matrix[i][j] = decimal;
                }
                System.out.println(probability_Matrix[i][j]);

            }
        }




        /*Test
        double[][] matrix_test = {
                {0.08, 0.32, 0.4},
                {0.015, 0.06, 0.075},
                {0.005, 0.02, 0.025}
                    };
         */
        double entropyX = cal_Entropy_X(probability_Matrix);
        System.out.println("H(X)= "+ entropyX);

        double entropyY = cal_Entropy_Y(probability_Matrix);
        System.out.println("H(Y)= "+ entropyY);

        double conditional_Entropy_X_Given_Y = cal_Conditional_Entropy_X_Given_Y(probability_Matrix);
        System.out.println("H(X|Y)= " + conditional_Entropy_X_Given_Y);

        double conditional_Entropy_Y_Given_X = cal_Conditional_Entropy_Y_Given_X(probability_Matrix);
        System.out.println("H(Y|X)= " + conditional_Entropy_Y_Given_X);

        double joint_Entropy = cal_Joint_Entropy(probability_Matrix);
        System.out.println("H(X,Y)= " + joint_Entropy);

        double redundancy = cal_Redundancy(entropyY, conditional_Entropy_Y_Given_X);
        System.out.println("H(Y) − H(Y|X)= " + String.format("%.15f", redundancy));

        double mutual_info = cal_Mutual_Information(entropyX, conditional_Entropy_X_Given_Y);
        System.out.println("I(X,Y) = " + String.format("%.15f", mutual_info));


        double KL_divergence_XY = cal_KL_divergence_XY(probability_Matrix);
        System.out.println("D(P(x)||P(y)) = " + String.format("%.15f", KL_divergence_XY));

        double KL_divergence_YX = cal_KL_divergence_YX(probability_Matrix);
        System.out.println("D(P(y)||P(x)) = " + String.format("%.15f", KL_divergence_YX));

        sc.close();
    }



    //Xây dựng hàm tính log2
    public static double log2(double x) {
        if (x ==0 ) return 0;
        return Math.log(x) / Math.log(2);
    }
    public static double cal_Entropy_X(double[][] probability_Matrix){
        int M = probability_Matrix.length;
        int N = probability_Matrix[0].length;
        double entropyX =0;

        for (int i =0;i< M; i++)
        {
            double row_Sum =0;
            for (int j =0; j< N; j++)
            {
                row_Sum += probability_Matrix[i][j];
            }
            entropyX += row_Sum * log2(row_Sum);
        }
        entropyX *= -1;
        return entropyX;
    }

    public static double cal_Entropy_Y(double[][] probability_Matrix){
        int M = probability_Matrix.length;
        int N = probability_Matrix[0].length;
        double entropyY =0;

        for (int j =0;j< N; j++)
        {
            double col_Sum =0;
            for (int i =0; i< M; i++)
            {
                col_Sum += probability_Matrix[i][j];
            }
            entropyY += col_Sum * log2(col_Sum);
        }
        entropyY *= -1;
        return entropyY;
    }
    public static double cal_Conditional_Entropy_X_Given_Y(double[][] probability_Matrix){
        int M = probability_Matrix.length;
        int N = probability_Matrix[0].length;
        double conditional_Entropy_X_Given_Y =0;

        for(int j=0; j< N; j++)
        {
            double col_Sum =0;
            for (int i =0; i< M; i++)
            {
                col_Sum += probability_Matrix[i][j];
            }

            for (int i=0; i< M; i++){
                if(probability_Matrix[i][j] > 0)
                {
                    conditional_Entropy_X_Given_Y -= (probability_Matrix[i][j]) * log2(probability_Matrix[i][j] / col_Sum);
                }
            }
        }

        return conditional_Entropy_X_Given_Y;
    }
    public static double cal_Conditional_Entropy_Y_Given_X(double[][] probability_Matrix){
        int M = probability_Matrix.length;
        int N = probability_Matrix[0].length;
        double conditional_Entropy_Y_Given_X =0;

        for(int i=0; i< M; i++)
        {
            double row_Sum =0;
            for (int j =0; j< N; j++)
            {
                row_Sum += probability_Matrix[i][j];
            }
            for (int j=0; j< N; j++){
                if(probability_Matrix[i][j] > 0)
                {
                    conditional_Entropy_Y_Given_X -= (probability_Matrix[i][j]) * log2(probability_Matrix[i][j] / row_Sum);
                }
            }
        }

        return conditional_Entropy_Y_Given_X;
    }

    public static double cal_Joint_Entropy(double[][] probability_Matrix){
        int M = probability_Matrix.length;
        int N = probability_Matrix[0].length;
        double joint_Entropy = 0;
        for (int i=0; i< M; i++)
        {
            for (int j=0; j< N;j++)
            {
                if (probability_Matrix[i][j] > 0){
                    joint_Entropy -= probability_Matrix[i][j] * log2(probability_Matrix[i][j]);
                }
            }
        }

        return joint_Entropy;
    }

    public static double cal_Redundancy(double entropyY, double conditional_Entropy_Y_Given_X) {
        return entropyY - conditional_Entropy_Y_Given_X;
    }

    public static double cal_Mutual_Information(double entropyX, double conditional_Entropy_X_Given_Y) {
        return entropyX - conditional_Entropy_X_Given_Y;
    }

    public static double cal_KL_divergence_XY(double[][] probability_Matrix){
        int M = probability_Matrix.length;
        int N = probability_Matrix[0].length;
        double divergence_XY =0;

        for (int i =0; i< M; i++)
        {
            double row_Sum =0;
            double col_sum =0;
            for(int j =0; j< N; j++){
                row_Sum += probability_Matrix[i][j];
                col_sum += probability_Matrix[j][i];
            }
            divergence_XY += row_Sum * log2(row_Sum/ col_sum);
        }

        return divergence_XY;
    }

    public static double cal_KL_divergence_YX(double[][] probability_Matrix){
        int M = probability_Matrix.length;
        int N = probability_Matrix[0].length;
        double divergence_YX =0;

        for (int i =0; i< M; i++)
        {
            double row_Sum =0;
            double col_Sum =0;
            for(int j =0; j< N; j++){
                row_Sum += probability_Matrix[i][j];
                col_Sum += probability_Matrix[j][i];
            }
            divergence_YX += col_Sum * log2(col_Sum/ row_Sum);
        }

        return divergence_YX;
    }
}
