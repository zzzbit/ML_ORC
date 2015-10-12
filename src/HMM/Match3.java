package HMM;

public class Match3 {
	  public static void main(String[] args) {
	    dice();
	    CpG();
	  }

	  static void dice() {
	    String[] state = { "F", "L" };
	    double[][] aprob = { { 0.95, 0.05 },
	                         { 0.10, 0.90 } };
	    String esym = "123456";
	    double[][] eprob = { { 1.0/6, 1.0/6, 1.0/6, 1.0/6, 1.0/6, 1.0/6 },
	                         { 0.10,  0.10,  0.10,  0.10,  0.10,  0.50  } };
	    
	    HMM hmm = new HMM(state, aprob, esym, eprob);
	   
	    String x = 
	      "315116246446644245311321631164152133625144543631656626566666"
	    + "651166453132651245636664631636663162326455236266666625151631"
	    + "222555441666566563564324364131513465146353411126414626253356"
	    + "366163666466232534413661661163252562462255265252266435353336"
	    + "233121625364414432335163243633665562466662632666612355245242";
//	      Viterbi vit = new Viterbi(hmm, x);
//	      // vit.print(new SystemOut());
//	      System.out.println(vit.getPath());
//	      Forward fwd = new Forward(hmm, x);
//	      //   fwd.print(new SystemOut());
//	      System.out.println(fwd.logprob());
//	      Backward bwd = new Backward(hmm, x);
//	      //    bwd.print(new SystemOut());
//	      System.out.println(bwd.logprob());
//	      PosteriorProb postp = new PosteriorProb(fwd, bwd);
//	      for (int i=0; i<x.length(); i++)
//	        System.out.println(postp.posterior(i, 1));
	    String[] xs = { x };
	    HMM estimate = HMM.baumwelch(xs, state, esym, 0.00001);
	    estimate.print(new SystemOut());
	  }

	  static void CpG() {
	    String[] state = { "A+", "C+", "G+", "T+", "A-", "C-", "G-", "T-" };
	    double p2m = 0.05;          // P(switch from plus to minus)
	    double m2p = 0.01;          // P(switch from minus to plus)
	    double[][] aprob = { 
	      { 0.180-p2m, 0.274-p2m, 0.426-p2m, 0.120-p2m, p2m, p2m, p2m, p2m },
	      { 0.171-p2m, 0.368-p2m, 0.274-p2m, 0.188-p2m, p2m, p2m, p2m, p2m },
	      { 0.161-p2m, 0.339-p2m, 0.375-p2m, 0.125-p2m, p2m, p2m, p2m, p2m }, 
	      { 0.079-p2m, 0.335-p2m, 0.384-p2m, 0.182-p2m, p2m, p2m, p2m, p2m },
	      { m2p, m2p, m2p, m2p,  0.300-m2p, 0.205-m2p, 0.285-m2p, 0.210-m2p },
	      { m2p, m2p, m2p, m2p,  0.322-m2p, 0.298-m2p, 0.078-m2p, 0.302-m2p },
	      { m2p, m2p, m2p, m2p,  0.248-m2p, 0.246-m2p, 0.298-m2p, 0.208-m2p },
	      { m2p, m2p, m2p, m2p,  0.177-m2p, 0.239-m2p, 0.292-m2p, 0.292-m2p } };

	    String esym = "ACGT";
	    double[][] eprob = { { 1, 0, 0, 0 },
	                         { 0, 1, 0, 0 },
	                         { 0, 0, 1, 0 },
	                         { 0, 0, 0, 1 },
	                         { 1, 0, 0, 0 },
	                         { 0, 1, 0, 0 },
	                         { 0, 0, 1, 0 },
	                         { 0, 0, 0, 1 } };

	    HMM hmm = new HMM(state, aprob, esym, eprob);

	    String x = "CGCG";
	    Viterbi vit = new Viterbi(hmm, x);
	    vit.print(new SystemOut());
	    System.out.println(vit.getPath());
	    
	    Forward fwd = new Forward(hmm, x);
	    System.out.println(fwd.logprob());
	  }
	}
