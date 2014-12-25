import networkx as nx
import sys

if __name__ == '__main__':
    usage = "usage: python %prog pajek_filename tagert_filename"
    print("# loading input...")
    h = nx.read_pajek(sys.argv[1])
    print("Finding cliques...")
    cliques = list(nx.find_cliques(h))
    f = open(sys.argv[2], 'w')
    print("Printing the cliques...")
    for item in cliques:
        if(len(item))>=4:
	    f.writelines(', '.join(str(j) for j in item) + '\n')
    f.close()
