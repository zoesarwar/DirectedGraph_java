//program #3
//Zoe Sarwar cssc0735
//Stephanie Bekker cssc0754
package edu.sdsu.cs.datastructures;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;


public class App<V> extends DirectedGraph<V>
{
    public static void main( String[] args ) throws IOException
    {
        String filePath;
        String pathToUse;
        List<String> vertices = new ArrayList<>();

        if(args.length == 0){
            pathToUse = "layout.csv";
        } else{
            pathToUse = args[0];
        }
        File file = new File(pathToUse);
        filePath = file.getAbsolutePath();
        Path file_path = Paths.get(filePath);
        try{
            List<String> mapInfo = Files.readAllLines(file_path, Charset.defaultCharset());
            IGraph mapGraph = new DirectedGraph();
            for(String item: mapInfo){
                String array[] = item.split(",");
                if(array.length == 1){
                    mapGraph.add(array[0]);
                    vertices.add(array[0]);
                } else {
                    if(!mapGraph.contains(array[0])){
                        mapGraph.add(array[0]);
                        vertices.add(array[0]);
                    } if(!mapGraph.contains(array[1])){
                        mapGraph.add(array[1]);
                        vertices.add(array[1]);
                    }
                    mapGraph.connect(array[0], array[1]);
                }
            }
            System.out.println(mapGraph.vertices().toString());
            for(String vertex: vertices){
                System.out.println(((DirectedGraph) mapGraph).map.get(vertex));
            }
            Scanner starter = new Scanner(System.in);
            System.out.println("Enter a starting vertex:");
            String start = starter.nextLine();
            Scanner destinationer = new Scanner(System.in);
            System.out.println("Enter a destination vertex:");
            String destination = destinationer.nextLine();

            List shortestPath = mapGraph.shortestPath(start, destination);
            System.out.println("The distance of the shortest path between " + start
                    + " and " + destination + " is " + shortestPath.size());
            System.out.println("The shortest path is: " + shortestPath);

        } catch(IOException e){
            System.out.println("Error: Unable to open " + args[0] + ". Verify the file exists, is accessible, and meets the syntax requirements.");
        }
    }
}