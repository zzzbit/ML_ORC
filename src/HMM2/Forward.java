package HMM2;

/**
 * 前向算法：用于计算观察到序列的概率
 * @author yang
 */
public class Forward {

	/**
	 * 局部概率
	 */
	//public double[][] alpha;

	/**
	 * 前向算法内容
	 * @param hmm HMM模型
	 * @param o 观察序列
	 * @return 观察序列概率
	 */
	public double core(HMM hmm, int[] o,double[][] alpha,double pprob) {
		double sum;
		//double retVal = 0;
		//alpha = new double[o.length][hmm.N];
		// 初始化t=1
		for (int i = 0; i < hmm.N; i++) {
			alpha[0][i] = hmm.pi[i] * hmm.B[i][o[0]];
		}
		// 递归
		for (int t = 0; t < o.length - 1; t++) {
			for (int j = 0; j < hmm.N; j++) {
				sum = 0;
				for (int i = 0; i < hmm.N; i++) {
					sum += alpha[t][i] * hmm.A[i][j];
				}
				alpha[t + 1][j] = sum * hmm.B[j][o[t + 1]];

			}
		}
		// 终止
		for (int i = 0; i < hmm.N; i++) {
			// System.out.println(alpha[o.length-1][i]);
			pprob += alpha[o.length - 1][i];
		}
		return pprob;
	}

	/**
	 * 前向算法估计参数，带比例因子且取对数 解决alpha趋向0，即下溢，除0出现的NaN
	 * @param hmm HMM模型
	 * @param o 观察值序列
	 * @param scale 比例因子
	 * @return
	 */
	public double coreByScale(HMM hmm, int[] o, double[][] alpha,double[] scale,double pprob) {
		double sum;
		// double retVal = 1;
		//double retVal = 0;
		//alpha = new double[o.length][hmm.N];
		// 初始化t=1
		scale[0] = 0;
		for (int i = 0; i < hmm.N; i++) {
			alpha[0][i] = hmm.pi[i] * hmm.B[i][o[0]];
			scale[0] += alpha[0][i];
		}
		for (int i = 0; i < hmm.N; i++) {
			alpha[0][i] /= scale[0];
		}

		// 递归
		for (int t = 0; t < o.length - 1; t++) {
			scale[t + 1] = 0;
			for (int j = 0; j < hmm.N; j++) {
				sum = 0;
				for (int i = 0; i < hmm.N; i++) {
					sum += alpha[t][i] * hmm.A[i][j];
				}
				alpha[t + 1][j] = sum * hmm.B[j][o[t + 1]];
				scale[t + 1] += alpha[t + 1][j];
			}
			for (int i = 0; i < hmm.N; i++) {
				alpha[t + 1][i] /= scale[t + 1];
			}
		}

		// 修正后的alpha值
		// for(int t = 0; t < o.length; t++){
		// for (int j = 0; j < hmm.N; j++) {
		// System.out.print(alpha[t][j]);
		// }
		// System.out.println();
		// }

		// 终止
		// for (int i = 0; i < hmm.N; i++) {
		// System.out.println(alpha[o.length-1][i]);
		// retVal += scale[i];
		// retVal += Math.log(scale[i]);
		// }

		// 求修正后的概率与原来一致的转换
		for (int i = 0; i < o.length; i++) {
			// System.out.println(scale[i]);
			// retVal *= scale[i];
			pprob += Math.log(scale[i]);
		}
		return pprob;
		// return Math.log(retVal);
	}

}
