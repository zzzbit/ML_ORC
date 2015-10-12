package HMM2;

import HMM2.Backward;
import HMM2.Forward;
import HMM2.HMM;

public class  BaumWelch {
   /*
	double logProbF,logProbFall, logProbB;
	double logProbInit;// 初始概率
	double logProbFinal;// 最终概率
	double logProbPrev;
	double[] scale;
	// 迭代次数
	int nIter;
	double delta;
	// 分子分母临时变量
	double[][] gamma;
	//double[][][] gammaall;
    double[][][] xi;
	//double[][][][] xiall;
	double[][] alpha;
	double[][] beta;
	double numeratorA, denominatorA;
	double numeratorB, denominatorB;
	
	Forward forward;
	Backward backward;*/
	
	/**
	 * 独立模块，与外面无关
	 * @param hmm hmm模型
	 * @param o 观察对象
	 * @return
	 */
	
	//O[][]观察序列是多序列的情况
    public double coreByMutiObj(HMM hmm, int[][] O,int inter) {
    	double[][] gamma;   //gamma[t][i] t:[0,T-1],i:[0,N]
    	double[][][] xi;    //xi[t][i][j] t:[0,T-2],i:[0,N],j:[0,N]
		double[][] alpha;   //alpha[t][i] t:[0,T-1],i:[0,N]
		double[][] beta;    //beta[t][i]   t:[0,T-1],i:[0,N]
		double[] scale;     // scale[t] t:[0,T-1]
		double delta=0.0,probf,probPrev=0.0,probFinal=0.0;
		double[][] numeratorA = new double[hmm.N][hmm.N];  //numeratorA[i][j]
		double[][] numeratorB = new double[hmm.N][hmm.M];  //numeratorB[i][j]
		double[] denominatorA = new double[hmm.N];         //denominatorA[i]
		double[] denominatorB = new double[hmm.N];         //denominatorB[i]
		double[] pi = new double[hmm.N];		
		Forward forward = new Forward();
		Backward backward = new Backward();
		
		int nIter = 0;
		
		do{
			probf = 0;
			//第一次probf值
			//probf ：每次所有序列的概率之和；denominatorA,denominatorB：每次对所有序列的gamma[t][i]求和；numeratorA,numeratorB:每次对所有序列的xi[t][i][j]求和 		
			for(int l = 0;l < O.length;l ++ ){
			  
			   probf = forward.coreByScale(hmm,O[l],alpha = new double[O[l].length][hmm.N], scale = new double[O[l].length], probf);
			  // System.out.println(probf);
			   backward.coreByScale(hmm, O[l],alpha = new double[O[l].length][hmm.N], beta = new double[O[l].length][hmm.N],scale = new double[O[l].length] , 0);
			   getGamma(hmm, O[l].length, alpha, beta,gamma = new double[O[l].length][hmm.N]);
		       getXi(hmm, O[l], alpha, beta,xi = new double[O[l].length][hmm.N][hmm.N]);
		             
		       for (int i = 0; i < hmm.N; i++) {
		    	   // hmm.pi[i] = 0.001 + 0.999 * gamma[0][i];  //pi[i] 估计
		    	    pi[i] = gamma[0][i];
					for (int t = 0; t < O[l].length-1; t++) {
						denominatorA[i] += gamma[t][i];
						denominatorB[i] += gamma[t][i];
					}
					for (int j = 0; j < hmm.N; j++) {
						for (int t = 0; t < O[l].length-1; t++)
							numeratorA[i][j] += xi[t][i][j];   //gamma[t][i] t:[0,T-2],i:[0,N]求和
					}
					denominatorB[i] += gamma[O[l].length-1][i];

					for (int k = 0; k < hmm.M; k++) {
						for (int t = 0; t < O[l].length; t++) {
							if (O[l][t] == k) {
								numeratorB[i][k] += gamma[t][i];  //gamma[t][i] t:[0,T-1],i:[0,N]求和
							}
						}
					}
				}
		   	}
			System.out.println(probf);
			for (int i = 0; i < hmm.N; i++) {
				//hmm.pi[i] = 0.001 / hmm.N + 0.999 * pi[i] / o.length;
				hmm.pi[i] = 0.001  + 0.999 * pi[i] ;//pi[i] 估计
				for (int j = 0; j < hmm.N; j++) {
					//hmm.A[i][j] = 0.001 / hmm.N + 0.999 * numeratorA[i][j] / denominatorA[i];
					hmm.A[i][j] = 0.001 + 0.999 * numeratorA[i][j] / denominatorA[i];   // A[i][j] 估计
					numeratorA[i][j] = 0.0;  //  归零
				}
				for (int k = 0; k < hmm.M; k++) {
					//hmm.B[i][k] = 0.001 / hmm.M + 0.999 * numeratorB[i][k] / denominatorB[i];
					hmm.B[i][k] = 0.001 + 0.999 * numeratorB[i][k] / denominatorB[i];   // B[i][k] 估计
					numeratorB[i][k] = 0.0;  //  归零
				}
				denominatorA[i] = denominatorB[i] = 0.0; // 归零
			}
			
		   	probPrev = probf;   // 临时值
		   	probf = 0;   // 归零
		   	//第二次probf值
		   	for(int l = 0;l < O.length;l ++ ){
		  		probf = forward.coreByScale(hmm,O[l], alpha = new double[O[l].length][hmm.N], scale = new double[O[l].length],probf);
		   	   	backward.coreByScale(hmm, O[l],alpha = new double[O[l].length][hmm.N], beta = new double[O[l].length][hmm.N],scale = new double[O[l].length], 0);
			    getGamma(hmm, O[l].length, alpha, beta,gamma = new double[O[l].length][hmm.N]);
		        getXi(hmm, O[l], alpha, beta,xi = new double[O[l].length][hmm.N][hmm.N]);
	       	}
			System.out.println(probf);
		   	delta = Math.abs(probf - probPrev); //两次比较
			System.out.println(delta);
			probPrev = probf;
			nIter++;

			} while (delta >0.1);// 收敛条件,差不大即收敛
		    //} while (nIter< 1);
		    //} while (nIter< inter);
			System.out.println("BW迭代了" + nIter + "次！");
			probFinal = probf;
			return probFinal;  //返回最终概率
	}
   
	//观察序列是单条序列的情况
	public double core(HMM hmm, int[] o) {
		double[][] gamma;
		double[][][] xi;
		double[] scale;
		double[][] alpha = new double[o.length][hmm.N];
		double[][] beta = new double[o.length][hmm.N];
		Forward forward = new Forward();
		Backward backward = new Backward();
		gamma = new double[o.length][hmm.N];
		xi = new double[o.length][hmm.N][hmm.N];
		scale = new double[o.length];
		int nIter = 0;
		double pprob=0.0;
		//double logProbInit=0.0;
		double logProbPrev=0.0;
		double logProbFinal=0.0;
		double denominatorA,denominatorB;
		double numeratorA,numeratorB;
		double delta;
		// ---初始化
		// 前向参数估计
		// logProbF = forward.core(hmm, o);
		pprob = forward.coreByScale(hmm, o, alpha,scale,pprob);
		System.out.println(pprob);
		// log P(o|初始概率)
		//logProbInit = pprob;
		// 后向参数估计
		// logProbB废弃
		// logProbB = backward.core(hmm, o);
		
		backward.coreByScale(hmm, o, alpha,beta,scale,0);
		getGamma(hmm, o.length, alpha, beta,gamma);
		
		getXi(hmm, o, alpha, beta,xi);
		
		logProbPrev = pprob;
		System.out.println(logProbPrev);
		// ---初始化结束

		// 迭代修正参数
		do {
			// 重新估计t=1时,状态为i的频率
			for (int i = 0; i < hmm.N; i++) {
				// 为了精度
				
				hmm.pi[i] = 0.001 + 0.999 * gamma[0][i];
				//hmm.pi[i] = gamma[0][i];
			}
			// 重新估计转移矩阵和观察矩阵
			for (int i = 0; i < hmm.N; i++) {
				denominatorA = 0;
				for (int t = 0; t < o.length - 1; t++) {
					denominatorA += gamma[t][i];
				}
				for (int j = 0; j < hmm.N; j++) {
					numeratorA = 0;
					for (int t = 0; t < o.length - 1; t++) {
						numeratorA += xi[t][i][j];
					}
					
					hmm.A[i][j] = 0.001 + 0.999 * numeratorA / denominatorA;
					//hmm.A[i][j] = numeratorA / denominatorA;
					if (Double.isNaN(hmm.A[i][j])) {
						System.out.println("fly...sdf");
					}
				}
				denominatorB = denominatorA + gamma[o.length - 1][i];
				for (int k = 0; k < hmm.M; k++) {
					numeratorB = 0;
					for (int t = 0; t < o.length; t++) {
						if (o[t] == k) {
							numeratorB += gamma[t][i];
						}
					}
					hmm.B[i][k] = 0.001 + 0.999 * numeratorB / denominatorB;
					//hmm.B[i][k] = numeratorB / denominatorB;
				}
			}
			// logProbF = forward.core(hmm, o);
			// logProbB = backward.core(hmm, o);
			alpha = new double[o.length][hmm.N];
			beta = new double[o.length][hmm.N];
			scale = new double[o.length];
			pprob = 0;
			gamma = new double[o.length][hmm.N];
			xi = new double[o.length][hmm.N][hmm.N];
			pprob = forward.coreByScale(hmm, o, alpha,scale,pprob);
			System.out.println(pprob);
			
			backward.coreByScale(hmm, o, alpha,beta,scale,0);
			getGamma(hmm, o.length, alpha, beta,gamma);
			getXi(hmm, o, alpha, beta,xi);

			// 计算两次直接的概率差
			delta = Math.abs(pprob - logProbPrev);
			System.out.println(delta);
			logProbPrev = pprob;
			nIter++;

		} while (delta >0.001);// 收敛条件,差不大即收敛
		System.out.println("BW迭代了" + nIter + "次！");
		logProbFinal = pprob;
		return logProbFinal;
		//return pprob;
	}

	/**
	 * 求概率变量gamma 观察序列从i状态出发的转换的期望概率
	 * @param hmm
	 * @param T
	 * @param alpha
	 * @param beta
	 */
	/*
	private void getGamma(HMM hmm, int T, double[][] alpha, double[][] beta) {
		double tmp;

		for (int t = 0; t < T; t++) {
			tmp = 0;
			for (int i = 0; i < hmm.N; i++) {
				gamma[t][i] = alpha[t][i] * beta[t][i];
				tmp += gamma[t][i];
			}
			for (int i = 0; i < hmm.N; i++) {
				gamma[t][i] = gamma[t][i] / tmp;
			}
		}

	}*/

	public void getGamma(HMM hmm, int T, double[][] alpha, double[][] beta, double[][] gamma) {
		double tmp;

		for (int t = 0; t < T; t++) {
			tmp = 0;
			for (int i = 0; i < hmm.N; i++) {
				gamma[t][i] = alpha[t][i] * beta[t][i];
				tmp += gamma[t][i];
			}
			for (int i = 0; i < hmm.N; i++) {
				gamma[t][i] = gamma[t][i] / tmp;
				//System.out.println(gamma[t][i]);
			}
		}

	}

	/**
	 * 观察序列o中从状态i到状态j的转换的期望概率
	 * @param hmm
	 * @param o
	 * @param alpha
	 * @param beta
	 */
	/*
	private void getXi(HMM hmm, int[] o, double[][] alpha, double[][] beta) {
		double sum;
		for (int t = 0; t < o.length - 1; t++) {
			sum = 0;
			for (int i = 0; i < hmm.N; i++) {
				for (int j = 0; j < hmm.N; j++) {
					xi[t][i][j] = alpha[t][i] * beta[t + 1][j] * hmm.A[i][j] * hmm.B[j][o[t + 1]];
					sum += xi[t][i][j];
				}
			}
			for (int i = 0; i < hmm.N; i++) {
				for (int j = 0; j < hmm.N; j++) {
					xi[t][i][j] /= sum;
				}
			}
		}
	}*/

	private void getXi(HMM hmm, int[] o, double[][] alpha, double[][] beta, double[][][] xi) {
		double sum;
		for (int t = 0; t < o.length - 1; t++) {
			sum = 0;
			for (int i = 0; i < hmm.N; i++) {
				for (int j = 0; j < hmm.N; j++) {
					xi[t][i][j] = alpha[t][i] * beta[t + 1][j] * hmm.A[i][j] * hmm.B[j][o[t + 1]];
					sum += xi[t][i][j];
				}
			}
			for (int i = 0; i < hmm.N; i++) {
				for (int j = 0; j < hmm.N; j++) {
					xi[t][i][j] /= sum;
					//System.out.println(xi[t][i][j]);
				}
			}
		}
	}

}


