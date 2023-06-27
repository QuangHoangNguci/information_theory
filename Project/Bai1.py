#Bài 1:
# a.Nhập vào ma trận xác suất kết hợp P(x,y) có cỡ MxN với M và N nhập từ bàn phím. 
# Cảnh báo nếu nhập xác suất âm, yêu cầu nhập lại.
# b.Tính và hiển thịH(X), H(Y ),H(X | Y ), H(Y | X),H(X, Y ),H(Y ) − H(Y | X),I(X; Y )
# c.Tính D(P(x)||P(y)) và D(P(y)||P(x))

import numpy as np
# a)
"""
def init_probability_matrix():
    # Nhạp ma trận kích thước MxN
    M = int(input("Nhập số hàng (M): "))
    N = int(input("Nhập số cột (N): "))

    if(M <= 0 or N<= 0):
        print("Yêu cầu nhập số hàng, số cột là số dương")
        M = int(input("Nhập số hàng (M): "))
        N = int(input("Nhập số cột (N): "))
    
    # Nhập ma trận xác suất kết hợp P(x, y)
    print("Nhập ma trận xác suất P(x, y):\n")
    probability_Matrix = np.zeros((M, N)) # Khởi tạo ma trận 0 kích thước M, N
    print(probability_Matrix)

    for i in range(M):
        for j in range(N):
            probability = float(input(f"P({i}, {j}): "))
            if(probability < 0):
                print("Không được nhập ma trận chứa xác suất âm!")
                probability = float(input(f"P({i}, {j}): "))
            probability_Matrix[i, j] = probability
            
    print(probability_Matrix)
    return probability_Matrix
    
"""

# b)
def cal_entropy(probabilities):
    entropy = 0
    for probability in probabilities:
        if probability > 0:
            entropy += probability * np.log2(probability)
    return -entropy

def cal_joint_entropy(matrix):
    probabilities = np.array(matrix).flatten()
    return cal_entropy(probabilities)

def cal_conditional_entropy(matrix, axis):
    joint_entropy = cal_joint_entropy(matrix)
    entropy = cal_entropy(np.sum(matrix, axis=axis))
    
    return joint_entropy - entropy

def cal_mutual_information(matrix):
    matrix = np.array(matrix)  # Chuyển đổi danh sách thành mảng đa chiều
    probability_row = np.sum(matrix, axis=1)
    probability_col = np.sum(matrix, axis=0)
    
    mutual_info = 0
    for i in range(matrix.shape[0]):
        for j in range(matrix.shape[1]):
            if matrix[i, j] > 0:
                mutual_info += matrix[i, j] * np.log2(matrix[i, j] / (probability_row[i] * probability_col[j]))
    
    return mutual_info

# c) Tính D(P(x)||P(y)) và D(P(y)||P(x))
def cal_relative_entropy(p, q):
    relative_entropy = 0
    for i in range(len(p)):
        if p[i] > 0:
            relative_entropy += p[i] * np.log2(p[i]/ q[i])
    
    return relative_entropy
    



# Test
#probability_Matrix = init_probability_matrix()
probability_Matrix = [[2/9, 1/18, 1/18],
                      [1/18, 2/9, 1/18],
                      [1/18, 1/18, 2/9]]
px = np.sum(probability_Matrix, axis=1)
py = np.sum(probability_Matrix, axis=0)

h_X = cal_entropy(px)
h_Y = cal_entropy(py)

h_X_Y = cal_joint_entropy(probability_Matrix)
h_Y_given_X = cal_conditional_entropy(probability_Matrix, 1)
h_X_given_Y = cal_conditional_entropy(probability_Matrix, 0)
i_XY = cal_mutual_information(probability_Matrix)



dpxy = cal_relative_entropy(px, py)
print("H(X, Y): ",h_X_Y)
print("H(X): ",h_X)
print("H(Y|X)= ", h_Y_given_X)
print("H(X|Y)= ", h_X_given_Y)
print("I(X, Y)= ", i_XY)
    



