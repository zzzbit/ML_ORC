package HMM2;

/**
 * Viterbi算法用于计算隐藏状态序列
 * @author yang
 */
public class Viterbi {

	/**
	 * 局部概率
	 */
	//public double[][] delta;

	/**
	 * 反向指针-记录路径
	 */
	//public int[][] psi;
	
	//private static int MAXNUM = 100000000;

	/**
	 * Viterbi算法
	 * @param hmm HMM模型
	 * @param o 观察序列
	 * @return 最佳隐藏状态序列
	 */
	public double core(HMM hmm, int[] o,double[][] delta, int[][] psi, int[] q) {
        double pprob = 0.0;
		//q = new int[o.length];
		//delta = new double[o.length][hmm.N];
		//psi = new int[o.length][hmm.N];
		double maxValue, tmpValue;
		int maxIndex;
		//double p = 0;

		// 初始化
		for (int i = 0; i < hmm.N; i++) {
			delta[0][i] = hmm.pi[i] * hmm.B[i][o[0]];
			psi[0][i] = 0;
		}
		// 迭代计算
		for (int t = 1; t < o.length; t++) {
			for (int j = 0; j < hmm.N; j++) {
				maxIndex = 1;
				maxValue = 0;
				for (int i = 0; i < hmm.N; i++) {
					tmpValue = delta[t - 1][i] * hmm.A[i][j];
					if (tmpValue > maxValue) {
						maxValue = tmpValue;
						maxIndex = i;
					}
				}
				delta[t][j] = maxValue * hmm.B[j][o[t]];
				psi[t][j] = maxIndex;
			}
		}
		// 终止
		q[o.length - 1] = 1;
		for (int i = 0; i < hmm.N; i++) {
			if (delta[o.length - 1][i] > pprob) {
				pprob = delta[o.length - 1][i];
				q[o.length - 1] = i;
			}
		}
		System.out.print("最佳路径概率:" +pprob);
		// 求解最佳路径
		for (int t = o.length - 2; t >= 0; t--) {
			q[t] = psi[t + 1][q[t + 1]];
			System.out.println(q[t]);
		}
        System.out.println();
		return pprob;
	}


	/**
	 * 取对数方式
	 * @param hmm
	 * @param o
	 * @return
	 */
	public double coreByLog(HMM hmm, int[] o,double[][] delta, int[][] psi, int[] q) {
		//q = new int[o.length];
		//delta = new double[o.length][hmm.N];
		//psi = new int[o.length][hmm.N];
		double maxValue, tmpValue;
		int maxIndex;
		double pprob = 0;
		double MAXFLOAT = 3.40282347E+38;
		double[][] bReplace = new double[hmm.N][o.length];
		double MAXNUM = 3.40282347E+38;
		// 预处理
		for (int i = 0; i < hmm.N; i++) {
			hmm.pi[i] = Math.log(hmm.pi[i]);
		}
		for (int i = 0; i < hmm.N; i++) {
			for (int j = 0; j < hmm.N; j++) {
				hmm.A[i][j] = Math.log(hmm.A[i][j]);
			}
		}
		for (int i = 0; i < hmm.N; i++) {
			for (int t = 0; t < o.length; t++) {
				bReplace[i][t] = Math.log(hmm.B[i][o[t]]);
			}
		}

		// 初始化
		for (int i = 0; i < hmm.N; i++) {
			//delta[0][i] = hmm.pi[i] * bReplace[i][o[0]];
			delta[0][i] = hmm.pi[i] + bReplace[i][o[0]];
			psi[0][i] = 0;
		}
		// 迭代计算
		for (int t = 1; t < o.length; t++) {
			for (int j = 0; j < hmm.N; j++) {
				maxIndex = 1;
				maxValue = -MAXNUM;
				for (int i = 0; i < hmm.N; i++) {
					tmpValue = delta[t - 1][i] + hmm.A[i][j];
					if (tmpValue > maxValue) {
						maxValue = tmpValue;
						maxIndex = i;
					}
				}
				delta[t][j] = maxValue + bReplace[j][t];
				psi[t][j] = maxIndex;
			}
		}
		// 终止
		q[o.length - 1] = 1;
		pprob = -MAXNUM;
		for (int i = 0; i < hmm.N; i++) {
			if (delta[o.length - 1][i] > pprob) {
				pprob = delta[o.length - 1][i];
				q[o.length - 1] = i;
			}
		}
		System.out.println("最佳路径概率:" + pprob);
		// 求解最佳路径
		for (int t = o.length - 2; t >= 0; t--) {
			q[t] = psi[t + 1][q[t + 1]];
			//System.out.println(q[t]);
		}

		return pprob;
	}
	
	public int[] coreByLogQ(HMM hmm, int[] o,double[][] delta, int[][] psi, int[] q) {
		this.coreByLog(hmm, o, delta, psi, q);
		System.out.print("最佳路径：");
		return q;
	}
}
