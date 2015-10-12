package HMM2;

/**
 * 后向算法
 * @author yang
 */
public class Backward {

	/**
	 * 后向变量(局部概率)
	 */
	//public double[][] beta;

	/**
	 * 后向算法内容
	 * @param hmm HMM模型
	 * @param o 观察序列
	 * @return 这里返回观察序列的概率
	 */
	public double core(HMM hmm, int[] o,double[][] beta,double pprob) {

		//double retVal = 0;
		double sum;
		//beta = new double[o.length][hmm.N];

		// 初始化
		for (int i = 0; i < hmm.N; i++) {
			beta[o.length - 1][i] = 1;
		}
		// 迭代计算
		for (int t = o.length - 2; t >= 0; t--) {
			for (int i = 0; i < hmm.N; i++) {
				sum = 0;
				for (int j = 0; j < hmm.N; j++) {
					sum += hmm.A[i][j] * hmm.B[j][o[t + 1]] * beta[t + 1][j];
				}
				beta[t][i] = sum;
			}
		}
		// 修正后的beta值
		/*for(int t = 0; t < o.length; t++){ for (int j = 0; j < hmm.N; j++) {
		 * System.out.print(beta[t][j]+","); } System.out.println(); } */
		// 终止
		for (int i = 0; i < hmm.N; i++) {
			// System.out.println(beta[0][i]);
			// System.out.println(hmm.pi[i] * hmm.B[i][o[0]] * beta[0][i]);
			pprob += hmm.pi[i] * hmm.B[i][o[0]] * beta[0][i];
		}
		return pprob;
	}

	/**
	 * 后向算法估计参数（带比例因子修正）
	 * @param hmm HMM模型
	 * @param o 观察序列
	 * @param scale 比例因子
	 * @return 返回概率
	 */

	public double coreByScale(HMM hmm, int[] o,double[][] alpha,double[][] beta, double[] scale,double pprob) {
		// double retVal = 1;
		//double retVal = 0;// 取对数用的
		// double sum;
		double sum, tmp;
		tmp = 0;
		//beta = new double[o.length][hmm.N];

		new Forward().coreByScale(hmm, o, alpha,scale,pprob);// 比例因子要一致，采用forward里的scale值；
		/*for (int i = 0; i < o.length; i++) { System.out.println(scale[i]+",");
		 * } */
		// 初始化
		for (int i = 0; i < hmm.N; i++) {
			beta[o.length - 1][i] = 1;
			// scale[o.length - 1]=1;
			// beta[o.length - 1][i] = 1/scale[o.length -1];
		}

		// 迭代计算
		for (int t = o.length - 2; t >= 0; t--) {
			for (int i = 0; i < hmm.N; i++) {
				sum = 0;
				for (int j = 0; j < hmm.N; j++) {
					sum += hmm.A[i][j] * hmm.B[j][o[t + 1]] * beta[t + 1][j];
				}
				// beta[t][i] = sum;
				// scale[t] += beta[t][i];
				// beta[t][i] = sum / scale[o.length -1];
				beta[t][i] = sum / scale[t + 1];
			}

			// for (int i = 0; i < hmm.N; i++) {
			// beta[t][i] /= scale[t];
			// }
		}

		// 修正后的beta值
		/*for(int t = 0; t < o.length; t++){ for (int j = 0; j < hmm.N; j++) {
		 * System.out.print(beta[t][j]+","); } System.out.println(); } */

		for (int i = 0; i < hmm.N; i++) {
			// System.out.print(beta[0][i]+",");
			// System.out.println(hmm.pi[i] * hmm.B[i][o[0]] * beta[0][i]+",");
			tmp += hmm.pi[i] * hmm.B[i][o[0]] * beta[0][i];
		}

		for (int i = 1; i < o.length; i++) {
			// System.out.println(scale[i]);
			pprob += Math.log(scale[i]);
		}
		pprob += Math.log(tmp);
		// 评估参数修正beta为主要目的,观察序列概率由前向算法提供
		// 返回修正后的概率*/
		return pprob;
	}
}