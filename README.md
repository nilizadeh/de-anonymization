de-anonymization
================

Community Enhanced De-anonymization of Online Social Networks

The deAnon code is in java. You may consider any of the "code/deAnon/Comm_Enhanced.java" or "code/deAnon/Comm_Blind.java" as the main file to run.

To get degree of anonymity in addition to success and error rates:

First, run exps with Prob/Deg parameter as False, to compute the probabilities.
Then, average the probabilities and run the exps with Prob/Deg parameter as True. 



To run the code, you should have: 

1) added jung 2.0.1 lib to the project

2) network files in pajek format,

3) run infomap (http://www.tp.umu.se/~rosvall/code.html) to get communities for each network,

4) run find-cliques.py to get cliques of the original network.



Notes:

	- You may use "code/"re-wiring-edges.jar" to re-wire the edges and generate noisy networks

	- You may use "code/deAnon/graph/OverlappedGraphs.java" to generate two overlapped networks from a reference network.


Please cite our paper using:

@inproceedings{deanon-ccs14,

    author = {Shirin Nilizadeh and Apu Kapadia and Yong-Yeol Ahn},

    title = {Community-Enhanced De-anonymization of Online Social Networks},

    booktitle = {The 21st ACM Conference on Computer and Communications Security (CCS ’14)},

    year = {2014},

    doi = {10.1145/2660267.2660324},

    month = nov
}

If you use provided data, please cite following papers:


	-Condensed matter collaborations 2005: 
  		
		M. E. J. Newman, The structure of scientific collaboration networks, Proc. Natl. Acad. Sci. USA 98, 404-409 (2001).

	- Twitter Mention Network:
		
		Lilian Weng, Filippo Menczer, and Yong-Yeol Ahn. Virality Prediction and Community Structure in Social Networks. Nature Scientific Report. (3)2522, 2013.




If you have questions, please contact Shirin Nilizadeh at shirnili@indiana.edu. Thanks.
