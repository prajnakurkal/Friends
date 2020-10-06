package friends;

import java.util.ArrayList;
import java.util.Collections;

import structures.Queue;
import structures.Stack;

public class Friends {

	/**
	 * Finds the shortest chain of people from p1 to p2.
	 * Chain is returned as a sequence of names starting with p1,
	 * and ending with p2. Each pair (n1,n2) of consecutive names in
	 * the returned chain is an edge in the graph.
	 * 
	 * @param g Graph for which shortest chain is to be found.
	 * @param p1 Person with whom the chain originates
	 * @param p2 Person at whom the chain terminates
	 * @return The shortest chain from p1 to p2. Null or empty array list if there is no
	 *         path from p1 to p2
	 */
	public static ArrayList<String> shortestChain(Graph g, String p1, String p2) 
	{
		
		// vector path stores the shortest path 
		ArrayList <String> path = new ArrayList <String> (); 
	    // predecessor[i] array stores predecessor of 
	    // i and distance array stores distance of i 
	    // from s 
	    int v = g.members.length;
	    String [] pred = new String [v];
	    int [] dist = new int[v]; 
	    
	    if (BFS(g, p1, p2, pred, dist) == false) 
	    { 
	        return path; 
	    }
	    
		path.add(p2);
		int index = g.map.get(p2);

		while((index > 0 && index < pred.length) && pred[index] != null)
		{
			path.add(pred[index]);
			index = g.map.get(pred[index]);
		}
		Collections.reverse(path);	
	return path;
	}
	private static boolean BFS(Graph g, String src, String dest, String pred[], int dist[]) 
	{ 
	    // a queue to maintain queue of vertices whose 
	    // adjacency list is to be scanned as per normal 
	    // DFS algorithm 
	    Queue <Person> queue = new Queue <Person> (); 
	  
	    // boolean array visited[] which stores the 
	    // information whether ith vertex is reached 
	    // at least once in the Breadth first search 
	    boolean [] visited = new boolean[g.members.length]; 
	  
	    // initially all vertices are unvisited 
	    // so v[i] for all i is false 
	    // and as no path is yet constructed 
	    // dist[i] for all i set to infinity 
	    for (int i = 0; i < visited.length; i++) 
	    { 
	        visited[i] = false; 
	        dist[i] = Integer.MAX_VALUE; 
	        pred[i] = null; 
	    } 
	  
	    // now source is first to be visited and 
	    // distance from source to itself should be 0 
	    int srcIndex = g.map.get(src);
	    visited[srcIndex] = true; 
	    dist[srcIndex] = 0; 
	    queue.enqueue(g.members[srcIndex]); 
	  
	    // standard BFS algorithm 
	    while (!queue.isEmpty()) 
	    { 
	        Person u = queue.dequeue();
	        int uIndex = g.map.get(u.name); //index of the name
	        Friend neighbor = u.first;
	        while(neighbor != null) //for each neighbor of u
	        {
	            if (visited[neighbor.fnum] == false) 
	            { 
	                visited[neighbor.fnum] = true; //visit the neighbor
	                dist[neighbor.fnum] = dist[uIndex] + 1; //update neighbor distance
	                pred[neighbor.fnum] = u.name; //set predecessor
	                queue.enqueue(g.members[neighbor.fnum]); //enqueue neighbor
	  
	                // We stop BFS when we find 
	                // destination. 
	                if (neighbor.fnum == g.map.get(dest)) //issue
	                   return true; 
	            }
	        neighbor = neighbor.next; 
	        } 
	    } 
	  
    return false; 
	} 
	/**
	 * Finds all cliques of students in a given school.
	 * 
	 * Returns an array list of array lists - each constituent array list contains
	 * the names of all students in a clique.
	 * 
	 * @param g Graph for which cliques are to be found.
	 * @param school Name of school
	 * @return Array list of clique array lists. Null or empty array list if there is no student in the
	 *         given school
	 */
	public static ArrayList<ArrayList<String>> cliques(Graph g, String school) 
	{
		boolean [] visited = new boolean[g.members.length];
		ArrayList <ArrayList<String>> list = new ArrayList <ArrayList<String>> ();
		//for each person in the graph
		for(int i = 0; i < g.members.length; i++)
		{
			if(!visited[i])
			{
				
				ArrayList<String> temp = bfsClique(g, g.members[i], school, visited);
				if (school.equals(g.members[i].school))
					list.add(temp);
				
				//list.add(bfsClique(g, g.members[i], school, visited));
			}
		}
	return list;
	}
	public static ArrayList <String> bfsClique(Graph g, Person p, String school, boolean [] visited)
	{
		ArrayList<String> names = new ArrayList<String>();
		int index = g.map.get(p.name);
	    
		if(p.school == null || !p.school.equals(school))
		{
			visited[index] = true;
			return names;
		}
		
	    Queue <Person> queue = new Queue <Person> ();   
	    queue.enqueue(p);
	    
	    // standard BFS algorithm 
	    while (!queue.isEmpty()) 
	    { 
	        Person u = queue.dequeue();
	        Friend neighbor = u.first;
	        int uIndex = g.map.get(u.name); //index of the name
	        visited[uIndex] = true;
	        
	        if (!names.contains(u.name))
				names.add(u.name);

	        
	        while(neighbor != null) //for each neighbor of u
	        {
	            if (!visited[neighbor.fnum] && school.equals(g.members[neighbor.fnum].school)) //if it is not visited
	            { 
	                visited[neighbor.fnum] = true; //visit the neighbor
	                queue.enqueue(g.members[neighbor.fnum]); //enqueue neighbor
	                /*
	                if(g.members[neighbor.fnum].school == null)
	                	break;
	                else if(g.members[neighbor.fnum].school.equals(school))
	                	names.add(g.members[neighbor.fnum].name);
	                */
	            }
	        neighbor = neighbor.next; 
	        } 
	    } 
	  
    return names; 
	}
	/**
	 * Finds and returns all connectors in the graph.
	 * 
	 * @param g Graph for which connectors needs to be found.
	 * @return Names of all connectors. Null or empty array list if there are no connectors.
	 */
	public static ArrayList<String> connectors(Graph g) {
		ArrayList <String> connectors = new ArrayList<String>();
		int numPeople = g.members.length;
		Person [] pred = new Person[numPeople];
		boolean [] visited = new boolean [numPeople];
		int [] dfsnum = new int[numPeople], back = new int[numPeople];
		int num = 0;
		
		for(int i = 0; i < numPeople; i++) //for each vertex in the graph
			connectorsDFS(g, g.members[i], num, visited, pred, dfsnum, back, connectors); //call dfs(v)
		
		//removes one-edgers
		for(int i = 0; i < connectors.size(); i++)
		{
			int uIndex = g.map.get(connectors.get(i));
			Friend u = g.members[uIndex].first;
			int numFriends = 0;
			while(u != null)
			{
				u = u.next;
				numFriends++;
			}
			if(numFriends <= 1)
				connectors.remove(i);	
		}
		
	return connectors;
	}
	public static void connectorsDFS(Graph g, Person p, int num, boolean [] visited, Person [] pred, int [] dfsnum, int [] back, ArrayList <String> connectors)
	{
		int pIndex = g.map.get(p.name);
		
		dfsnum[pIndex] = num;
		back[pIndex] = dfsnum[pIndex];
		num++;
		visited[pIndex] = true; //visit
		
		Friend neighbor = p.first;
		while(neighbor != null) //for each neighbor
		{
			if(!visited[neighbor.fnum]) //if neighbor is not visited
			{
				pred[neighbor.fnum] = p;
				connectorsDFS(g, g.members[neighbor.fnum], num, visited,pred, dfsnum, back, connectors); //execute dfs
				back[pIndex] = Math.min(back[pIndex], back[neighbor.fnum]);
				
				if(back[neighbor.fnum] >= dfsnum[pIndex])
				{
					if(dfsnum[pIndex] == 0)
					{
						if(neighbor.next == null) //last friend in the list
						{
							if(!connectors.contains(p.name))
								connectors.add(p.name);
						}
					}
					else
					{
						if(!connectors.contains(p.name))
							connectors.add(p.name);
					}
				}
			}
			else if(g.members[neighbor.fnum] != pred[pIndex] && visited[neighbor.fnum] == true)
				back[pIndex] = Math.min(back[pIndex], dfsnum[neighbor.fnum]);
			
		neighbor = neighbor.next;
		}	
	}
}