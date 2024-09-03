import java.io.*;
import java.util.*;

public class Driver {
  
  public static final int MAX_VALUE = 500000;
  public static void main(String[] args) {
    String input = "TEST_DATA.txt";
    try {
      File f = new File( input );
      Scanner myReader = new Scanner(f);
      
      /* run test maze until reach end of input file */
      while ( myReader.hasNextLine() ) {
        testMaze( myReader );
      }
      myReader.close();
    } catch (FileNotFoundException e) {
      System.out.println("File "+ input + " not found.");
    }
    
  }
  
  //Reads the test case from Scanner and runs the referenced student method on it.
  public static void testMaze( Scanner myReader ) {
    String data = myReader.nextLine();
    StringTokenizer tokens = new StringTokenizer(data);
      
      if( tokens.countTokens() != 4 )
      return;
    
    String method = tokens.nextToken();
    int size = Integer.parseInt( tokens.nextToken() );
    int pathExists = Integer.parseInt( tokens.nextToken() );
    int pathLength = Integer.parseInt( tokens.nextToken() );
    char maze[][] = readMaze( myReader, size );
    
    int ret, expectedRet;
    
    if( method.equals("hasPath") ){
      ret = GraphAlgorithms.hasPath( maze, size );
      if( pathExists==0 )
        expectedRet = -1;
      else
        expectedRet = 1;
    }
    else if( method.equals("findNearestFinish") ){
      ret = GraphAlgorithms.findNearestFinish( maze, size );
      if( pathExists==0 )
        expectedRet = -1;
      else
        expectedRet = pathLength;
    }
    else if( method.equals("findLongestSimplePath") ){
      ret = GraphAlgorithms.findLongestSimplePath( maze, size );
      if( pathExists==0 )
        expectedRet = -1;
      else
        expectedRet = pathLength;
    }
    else{
      System.out.println( "ERROR - TEST DATA INCORRECTLY FORMATTED - "+data);
      return;
    }
    
    if( ret != expectedRet && ret != 0 ){
      System.out.println( "INCORRECT " + method + size + " - EXPECTED " + expectedRet + " YOUR OUTPUT " + ret);
      // printMaze( maze, size );
    }
    else if( ret != 0 ){
      System.out.println( "CORRECT " + method + size + " - YOUR OUTPUT MATCHED EXPECTED " + ret );
    }
  }
  
  public static char[][] readMaze( Scanner myReader, int size ) {
    char[][] maze = new char[size][size];
    
    for( int i=0; i<size; i++ ){
      String data = myReader.nextLine();
      for( int j=0; j<size; j++ ){
        maze[i][j] = data.charAt(j);
      }
    }  
    return maze;
  }
  
  public static void printMaze( char maze[][], int size ) {
    for( int i=0; i<size; i++ ){
      for( int j=0; j<size; j++ ){
        System.out.print( maze[i][j] );
      }
      System.out.println();
    }  
    System.out.println();
  }
}
