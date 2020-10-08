/*Code by: Priyavart Rajain
 * University of Calgary
 * UCID : 30074184
 * This program implements Gale Shapley Algorithm using Arrays and Linked Lits in O(n^2) time.
 * References: Section 2.3 Page 42-47. Kleinberg, Tardos, & Tardos, Éva. (2006). Algorithm design. Pearson/Addison-Wesley*/


import java.util.LinkedList;


public class GaleShapley {

	public static void main(String[] args) {
		int manPref[][] = new int[][] {{1,0,3,2,4},
					        {3,1,2,1,4},
						{2,1,0,3,4},
						{4,2,3,1,0},
			       		        {3,1,0,2,4}};
		int womanPref[][] = new int[][] {{3,2,1,4,0},
						{2,1,3,0,4},
						{0,2,1,4,3},
						{3,1,0,2,4},
						{1,0,3,2,4}};
		stabilize(manPref, womanPref);
	}
	
	public static void stabilize(int[][] manPref, int[][] womanPref) {
		
		int num = manPref.length;
		
		//Initializing Linked list freeMen of all the free men. Addition and removal takes place from index 0
		LinkedList<Integer> freeMen = new LinkedList<Integer>();
		
		//Initially all men are free, so fill freeMen with all men from 0 to n
		for(int i=0;i<num;i++) {
			freeMen.add(i);
		}
		
		//Initialize array engagement that stores the engagements.
		// if w is free, then engagement[w] = -1. Initially all women are free.
		// if w is not free, then engagement[w] = m, i.e w is engaged to m
		
		int engagement[] = new int[num];
		for(int i=0;i<engagement.length;i++) {
			engagement[i] = -1;
		}
		
		//Initialize array "next" which stores for each m his next highest ranked woman w
		// m proposes to woman w = ManPref[m,next[m]]
		// set next[m] = 0 for all m because they all will propose to ManPref[m,0] first 
		
		int next[] = new int[num];
		for(int i=0; i<num;i++) {
			next[i] = 0;			
		}
		
		//Ranking array : A n*n array that contains the rank of man m in the sorted order of w's preferences.
		int rank[][] = new int[num][num]; 
		for(int w=0;w<num;w++) {
			for(int r=0;r<num;r++) {
				int m = r;
				rank[w][womanPref[w][m]]=r;
			}
		}
		
		while(freeMen.size()!=0 && next[freeMen.getFirst()]<num ) {		// next[freeMen.getFirst()]<num, means that 
		   									// if next[m] for the free man is greater than num, then he has already proposed 
											// all women, in which case we exit the loop. We also exit the loop if the size of linked list = 0 i.e no free man available
			
			
			int currentMan = freeMen.getFirst();					//Grab the first free man from front of the list
			System.out.println("Current Man: "+currentMan);
			int currentWoman = manPref[currentMan][next[currentMan]];		//Grab the highest ranked woman w on man m's list, m will propose to this w
			System.out.println("Current Woman: "+currentWoman);				
			

			
			next[currentMan] = next[currentMan]+1;					// Update the woman w to which m will propose next time (the next highest w on his list) if he remains free. 
												// This makes sure that m does not propose to the same woman again.

			if(isFree(currentWoman, engagement)) {					// check if the woman to which m is proposing is free. w is free if engagement[w] = -1
				engagement[currentWoman] = currentMan;				// if she is free. Update engagement[w] = m.
				freeMen.removeFirst();						// remove m from the front of the list.

			}
			else if(!isFree(currentWoman, engagement)) {				//if w happens to be engaged

				int anotherMan = engagement[currentWoman];			// grab the other man m' to which w is engaged.


				if(rank[currentWoman][anotherMan]<rank[currentWoman][currentMan]) {		// if m' ranks lesser on w's preference list than m (smaller ranks are better)
					continue;																	// then m remains free and we go back to beginning of loop
				}
				else {																			// otherwise
					engagement[currentWoman] = currentMan;					// we update engagement[w] = m from engagement[w] = m'
					freeMen.removeFirst();							// m is removed from the front of the free man list
					freeMen.addFirst(anotherMan);						// m' is added to the front of the free man list.
				}
			}

		}
		
		int j = 0;
		System.out.println("Women    Men");								// Printing the engagements.
		for(int i : engagement) {
			
			System.out.print(j+"        ");
			System.out.print(i);
			System.out.println();
			j++;
			
		}

	}
	
	// This method returns true if a man is currently free else, it returns false.
	public static boolean isFree(int m,LinkedList<Integer> freeMen) {
		return freeMen.contains(m);
	}
	
	//This method returns true if a woman is free by checking engagement[w] == -1 or not
	public static boolean isFree(int w, int engagement[]) {
		return engagement[w]==-1;
	}
	
	//This method returns true if m has already proposed a given woman w.
	public static boolean hasProposed(int m, int w, int next[]) {
		return w < next[m];
	}
}
