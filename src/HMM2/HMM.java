package HMM2;

/**
 * HMM对象类
 * @author yang
 */
public class HMM implements Cloneable{
	/**
	 * 隐藏状态数目
	 */
	public int N;
	/**
	 * 观察状态数目
	 */
	public int M;
	/**
	 * 状态转移矩阵
	 * 一个隐状态到另一个隐状态的概率
	 */
	public double[][] A;
	

	/**
	 * 符号输出矩阵(混淆矩阵)
	 * 隐状态和观察状态的映射
	 */
	public double[][] B;
	/**
	 * 初始向量
	 */
	public double[] pi;
	
	
	@Override
	public Object clone() {
		HMM hmm = null;
		try {
			hmm = (HMM) super.clone();
			hmm.A = A.clone();
			for (int i = 0; i < A.length; i++) {
				hmm.A[i] = A[i].clone();
			}
			hmm.B = B.clone();
			for (int i = 0; i < B.length; i++) {
				hmm.B[i] = B[i].clone();
			}
			hmm.pi = pi.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		
		return hmm;
	}

	public HMM() {
		super();
	}

	/**
	 * @param n 隐藏状态数目
	 * @param m 观察状态数目
	 * @param a 状态转移矩阵
	 * @param b 混淆矩阵
	 * @param pi 初始向量
	 */
	public HMM(int n, int m, double[][] a, double[][] b, double[] pi) {
		super();
		N = n;
		M = m;
		A = a;
		B = b;
		this.pi = pi;
	}
	/**
	 * 用于参数估计
	 * @param n 隐藏状态数目
	 * @param m 观察状态数目
	 */
	public HMM(int n, int m) {
		super();
		N = n;
		M = m;
		A = new double[N][N];
		B = new double[N][M];
		pi = new double[N];		                
	}
	/**用于测试已知模型
	 * @param n 隐藏状态数目
	 * @param m 观察状态数目
	 * @param a 状态转移矩阵
	 * @param b 符号输出矩阵
	 * @param pi 初始向量
	 */
	public HMM(double[][] a, double[][] b, double[] pi) {
		super();
		N = a.length;
		if (a.length == b[0].length) {
			M = b.length;
		}else {
			M = b[0].length;
		}
		A = a;
		B = b;
		this.pi = pi;
	}

	// geter/seter
	public int getN() {
		return N;
	}

	public void setN(int n) {
		N = n;
	}

	public int getM() {
		return M;
	}

	public void setM(int m) {
		M = m;
	}

	public double[][] getA() {
		return A;
	}

	public void setA(double[][] a) {
		A = a;
	}

	public double[][] getB() {
		return B;
	}

	public void setB(double[][] b) {
		B = b;
	}

	public double[] getPi() {
		return pi;
	}

	public void setPi(double[] pi) {
		this.pi = pi;
	}

}
