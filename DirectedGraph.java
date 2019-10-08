//program #3
//Zoe Sarwar cssc0735
//Stephanie Bekker cssc0754
package edu.sdsu.cs.datastructures;

import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Set;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.Collections;
import java.util.ArrayDeque;

public class DirectedGraph<V> implements IGraph<V> {
    private ArrayList<V> shortestPath = new ArrayList<>();
    List<V> justRemoved = new ArrayList<>();
    Map<V, Set<V>> map = new HashMap<>();

    @Override
    public void add(V vertexName) {
        map.put(vertexName, new HashSet<>());
    }

    @Override
    public void connect(V start, V destination) {
        if(!map.containsKey(start) || !map.containsKey(destination)){
            throw new NoSuchElementException("Both nodes must be in the graph.");
        }
        map.get(start).add(destination);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public boolean contains(V label) {
        return map.containsKey(label);
    }

    @Override
    public void disconnect(V start, V destination) {
        if(!map.containsKey(start) || !map.containsKey(destination)){
            throw new NoSuchElementException("Both nodes must be in the graph.");
        }
        map.get(start).remove(destination);
    }

    @Override
    public boolean isConnected(V start, V destination) {
        if(!map.containsKey(start) || !map.containsKey(destination)){
            throw new NoSuchElementException("Both nodes must be in the graph.");
        }
        boolean result = map.get(start).contains(destination);
        if(result == true){
            return true;
        }
        List<V> inPath = BFS(start);
        if(inPath.contains(start) && inPath.contains(destination)){
            return true;
        }
        return false;
    }

    private List<V> BFS(V start){
        List<V> visitedObject = new ArrayList<>();
        List<Boolean> visited = new ArrayList<>();
        LinkedList<V> queue = new LinkedList<>();
        List<V> inPath = new ArrayList<>();
        visitedObject.add(start);
        visited.add(true);
        queue.add(start);

        while(queue.size() != 0) {
            start = queue.poll();
            while(justRemoved.contains(start)){
                start = queue.poll();
            }
            if(start == null){
                return inPath;
            }
            inPath.add(start);
            Iterator<V> i = map.get(start).iterator();
            while (i.hasNext()) {
                V n = i.next();
                if (map.get(start).contains(n)) {
                    if (!visited.contains(n)) {
                        visited.add(true);
                        visitedObject.add(n);
                        queue.add(n);
                    }
                }
            }
        }
        return inPath;
    }

    @Override
    public Iterable<V> neighbors(V vertexName) {
        Set<V> neighbors = map.get(vertexName);
        if(map.get(vertexName) == null){
            throw new NoSuchElementException("Vertex not in the graph");
        }
        return Collections.unmodifiableSet(neighbors);
    }


    @Override
    public void remove(V vertexName) {
        if(!map.containsKey(vertexName)){
            throw new NoSuchElementException("Node not in graph.");
        }
        map.remove(vertexName);
        map.entrySet().remove(vertexName);
        justRemoved.add(vertexName);

    }


    @Override
    public List<V> shortestPath(V start, V destination) {
        ArrayList<V> path = new ArrayList<>();

        if(!map.containsKey(start) || !map.containsKey(destination)){
            throw new NoSuchElementException("Either start or destination does not exist in map.");
        }
        if(start.equals(destination)){
            path.add(start);
            return path;
        }

        ArrayDeque<V> queue = new ArrayDeque<>();
        ArrayDeque<V> visited = new ArrayDeque<>();

        queue.offer(start);
        while(!queue.isEmpty()){
            V vertex = queue.poll();
            visited.offer(vertex);

            Set<V> neighborsList = map.get(vertex);
            Iterator<V> neighbors = neighborsList.iterator();
            while(neighbors.hasNext()){
                V neighbor = neighbors.next();

                path.add(neighbor);
                path.add(vertex);

                if(neighbor.equals(destination)){
                    return processPath(start, destination, path);
                } else{
                    if(!visited.contains(neighbor)){
                        queue.offer(neighbor);
                    }
                }
            }
        }
        return null;
    }

    private List<V> processPath(V start, V destination, ArrayList<V> path){
        int index = path.indexOf(destination);
        V source = path.get(index + 1);

        shortestPath.add(0, destination);

        if(source.equals(start)){
            shortestPath.add(0,start);
            return shortestPath;
        } else {
            return processPath(start, source, path);
        }
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public Iterable<V> vertices() {
        return map.keySet();
    }

    @Override
    public IGraph<V> connectedGraph(V origin) {
        IGraph connectedGraph = new DirectedGraph();
        List<V> visitedObject = new ArrayList<>();
        List<Boolean> visited = new ArrayList<>();
        LinkedList<V> queue = new LinkedList<>();
        List<V> inPath = new ArrayList<>();
        visitedObject.add(origin);
        visited.add(true);
        queue.add(origin);
        V lastItem = null;

        while(queue.size() != 0) {
            origin = queue.poll();
            inPath.add(origin);
            Iterator<V> i = map.get(origin).iterator();
            while (i.hasNext()) {
                V n = i.next();
                if (map.get(origin).contains(n)) {
                    if (!visited.contains(n)) {
                        visited.add(true);
                        visitedObject.add(n);
                        queue.add(n);
                    }
                }
                for(V item: inPath){
                    if(lastItem != null){
                        connectedGraph.add(item);
                        connectedGraph.connect(lastItem,item);
                        lastItem = item;
                    } else{
                        connectedGraph.add(item);
                        lastItem = item;
                    }

                }
                inPath.clear();
                visited.clear();
                visitedObject.clear();
            }
        }
        for(V item: inPath){
            connectedGraph.add(item);
            connectedGraph.connect(lastItem,item);
        }
        return connectedGraph;
    }
}