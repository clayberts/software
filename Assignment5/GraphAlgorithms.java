import java.util.*;

public class GraphAlgorithms {
  
  /* returns -1 if path not found from S to F and 1 if a path is found */
  public static int hasPath( char[][] maze, int size ) {
    int value;
    int[][] map = new int[size][size];
    int startX;
    int startY;
    int depth = -1;
    
    startX = findRow(maze, size);
    startY = findColumn(maze, size);
    setMap(maze, map, size);
    
    if (startX == -1) {
      return -1;
    }

    value = checkEligible (maze, startX, startY, depth, map, size);

    return value; /* Default value to signal it has not been completed yet*/
  }
  
  /* returns -1 if path not found from S to any F */
  /* otherwise returns distance to nearest F starting from S */
  public static int findNearestFinish( char[][] maze, int size ) {
    int[][] map = new int[size][size];
    int[][] weighted = new int[size][size];
    int startX;
    int startY;
    int depth = 0;

    if (hasPath(maze, size) == -1) {
      return -1;
    }
    else {
      startX = findRow(maze, size);
      startY = findColumn(maze, size);
      setMap(maze, map, size);
      depth = setMapDist(maze, weighted, size, startX, startY, depth);
      return depth;
    }

    /* Default value to signal it has not been completed yet*/
  }
  
  /* returns -1 if path not found from S to F */
  /* otherwise returns distance of longest simple (i.e. not self intersecting) path to F starting from S */
  public static int findLongestSimplePath( char[][] maze, int size ) {
    int[][] map = new int[size][size];
    int[][] weighted = new int[size][size];
    int startX;
    int startY;
    int depth = 0;

    return 0;
    // if (hasPath(maze, size) == -1) {
    //   return -1;
    // }
    // else {
    //   startX = findRow(maze, size);
    //   startY = findColumn(maze, size);
    //   setMap(maze, map, size);
    //   depth = setMapDistLong(maze, weighted, size, startX, startY, depth);
    //   return depth;
    // }

  }
  
  /* recursive helper function for hasPath that walks the map leaving a "bread crumb" trail
     that returns on itself if it gets caught in a dead end, returning a 1 if it makes it to 
     the finish, otherwise returns -1 if it runs out of possible moves */
  private static int checkEligible( char[][] maze, int pointX, int pointY, int depth, int[][] map, int size) {
    int a = -1;
    if (maze[pointX][pointY] == 'F') {
      map[pointX][pointY] = 3;
      return 1;
    }
    else if (map[pointX - 1][pointY] != 0 && map[pointX + 1][pointY] != 0 && map[pointX][pointY + 1] != 0 && map[pointX][pointY - 1] != 0) {
      map[pointX][pointY] = 1;
      return 0;
    }
    else {
      map[pointX][pointY] = 1;
      if (map[pointX - 1][pointY] == 0){
        a = checkEligible(maze, pointX - 1, pointY, depth, map, size);
        if (a == 0) {
          a = -1;
        }
        else if (a == 1) {
          return 1;
        }
      }

      if (map[pointX + 1][pointY] == 0){
        a = checkEligible(maze, pointX + 1, pointY, depth, map, size);
        if (a == 0) {
          a = -1;
        }
        else if (a == 1) {
          return 1;
        }
      }

      if (map[pointX][pointY + 1] == 0){
        a = checkEligible(maze, pointX, pointY + 1, depth, map, size);
        if (a == 0) {
          a = -1;
        }
        else if (a == 1) {
          return 1;
        }
      }
      
      if (map[pointX][pointY - 1] == 0){
        a = checkEligible(maze, pointX, pointY - 1, depth, map, size);
        if (a == 0) {
          a = -1;
        }
        else if (a == 1) {
          return 1;
        }
      }
    
    }

    return a;
  }

  /* finds the row index of the starting point*/
  private static int findRow( char[][] maze, int size) {
    int startX = -1;

    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        if (maze[i][j] == 'S') {
          startX = i;
        }
      }
    }
    return startX;
  } 

  /* finds the column index of the starting point*/
  private static int findColumn( char[][] maze, int size) {
    int startY = -1;

    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        if (maze[i][j] == 'S') {
          startY = j;
        }
      }
    }
    return startY;
  } 

  /* fills a new graph with basic values for path checking*/
  private static void setMap( char[][] maze, int[][] map, int size) {
    for (int i = 0; i < size; i++) {
      map[0][i] = 2;
      map[size - 1][i] = 2;
      map[i][0] = 2;
      map[i][size - 1] = 2;
    }
    for (int i = 1; i < size - 1; i++) {
      for (int j = 1; j < size - 1; j++) {
        if (maze[i][j] == 'X') {
          map[i][j] = 2;
        }
        else {
          map[i][j] = 0;
        }
      }
    }
  } 

  /* fills a new graph with weighted distances with the smallest number of steps needed
     to reach any particular point in the graph from the start */
  private static int setMapDist (char[][] maze, int[][] map, int size, int startX, int startY, int depth) {
    for (int i = 0; i < size; i++) {
      map[0][i] = 999;
      map[size - 1][i] = 999;
      map[i][0] = 999;
      map[i][size - 1] = 999;
    }
    for (int i = 1; i < size - 1; i++) {
      for (int j = 1; j < size - 1; j++) {
        if (maze[i][j] == 'X') {
          map[i][j] = 999;
        }
        else {
          map[i][j] = 0;
        }
      }
    }

    depth = fillHelp(maze, startX, startY, map, size, depth);
    return depth;

  }

  /* recursive helper to setMapDist to help walk to every point in the graph*/
  private static int fillHelp( char[][] maze, int pointX, int pointY, int[][] weighted, int size, int depth) {
    if (maze[pointX][pointY] == 'F') {
      if (depth == 1) {
        depth = weighted[pointX][pointY];
        return depth;
      }
      else if ((weighted[pointX][pointY] < depth)) {
        depth = weighted[pointX][pointY];
        return depth;
      }
      else {
        return depth;
      }
    }
    else if (maze[pointX][pointY] == 'S' && depth > 0) {
      weighted[pointX][pointY] = 0;
      return depth;
    }
    else {
      if (depth == 0) {
        depth += 1;
      }

      if (weighted[pointX - 1][pointY] == 999) {
        ;
      }
      else if ((weighted[pointX - 1][pointY] == 0) || (weighted[pointX - 1][pointY] > weighted[pointX][pointY] + 1)) {
        weighted[pointX - 1][pointY] = weighted[pointX][pointY] + 1;
        depth = fillHelp(maze, pointX - 1, pointY, weighted, size, depth);
      }

      if (weighted[pointX + 1][pointY] == 999) {
        ;
      }
      else if ((weighted[pointX + 1][pointY] == 0) || (weighted[pointX + 1][pointY] > weighted[pointX][pointY] + 1)) {
        weighted[pointX + 1][pointY] = weighted[pointX][pointY] + 1;
        depth = fillHelp(maze, pointX + 1, pointY, weighted, size, depth);
      }

      if (weighted[pointX][pointY + 1] == 999) {
        ;
      }
      else if ((weighted[pointX][pointY + 1] == 0) || (weighted[pointX][pointY + 1] > weighted[pointX][pointY] + 1)) {
        weighted[pointX][pointY + 1] = weighted[pointX][pointY] + 1;
        depth = fillHelp(maze, pointX, pointY + 1, weighted, size, depth);
      }
      
      if (weighted[pointX][pointY - 1] == 999) {
        ;
      }
      else if ((weighted[pointX][pointY - 1] == 0) || (weighted[pointX][pointY - 1] > weighted[pointX][pointY] + 1)) {
        weighted[pointX][pointY - 1] = weighted[pointX][pointY] + 1;
        depth = fillHelp(maze, pointX, pointY - 1, weighted, size, depth);
      }

    }

    return depth;

  }

  /* recursively fills a new graph with weighted distances with the largest number of steps needed
    to reach any particular point in the graph from the start */
  private static int setMapDistLong (char[][] maze, int[][] map, int size, int startX, int startY, int depth) {
    for (int i = 0; i < size; i++) {
      map[0][i] = 999;
      map[size - 1][i] = 999;
      map[i][0] = 999;
      map[i][size - 1] = 999;
    }
    for (int i = 1; i < size - 1; i++) {
      for (int j = 1; j < size - 1; j++) {
        if (maze[i][j] == 'X') {
          map[i][j] = 999;
        }
        else {
          map[i][j] = 0;
        }
      }
    }

    depth = fillHelpLong(maze, startX, startY, map, size, depth);
    return depth;

  }

  /* recursive helper to setMapDistLong to help walk to every point in the graph*/
  private static int fillHelpLong( char[][] maze, int pointX, int pointY, int[][] weighted, int size, int depth) {
    if (maze[pointX][pointY] == 'F') {
      if (depth == 1) {
        depth = weighted[pointX][pointY];
        return depth;
      }
      else if ((weighted[pointX][pointY] > depth)) {
        depth = weighted[pointX][pointY];
        return depth;
      }
      else {
        return depth;
      }
    }
    else if (maze[pointX][pointY] == 'S' && depth > 0) {
      weighted[pointX][pointY] = 0;
      return depth;
    }
    else {
      if (depth == 0) {
        depth += 1;
      }

      if (weighted[pointX - 1][pointY] == 999) {
        ;
      }
      else if ((weighted[pointX - 1][pointY] == 0) || (weighted[pointX - 1][pointY] < weighted[pointX][pointY] + 1)) {
        weighted[pointX - 1][pointY] = weighted[pointX][pointY] + 1;
        depth = fillHelpLong(maze, pointX - 1, pointY, weighted, size, depth);
      }

      if (weighted[pointX + 1][pointY] == 999) {
        ;
      }
      else if ((weighted[pointX + 1][pointY] == 0) || (weighted[pointX + 1][pointY] < weighted[pointX][pointY] + 1)) {
        weighted[pointX + 1][pointY] = weighted[pointX][pointY] + 1;
        depth = fillHelpLong(maze, pointX + 1, pointY, weighted, size, depth);
      }

      if (weighted[pointX][pointY + 1] == 999) {
        ;
      }
      else if ((weighted[pointX][pointY + 1] == 0) || (weighted[pointX][pointY + 1] < weighted[pointX][pointY] + 1)) {
        weighted[pointX][pointY + 1] = weighted[pointX][pointY] + 1;
        depth = fillHelpLong(maze, pointX, pointY + 1, weighted, size, depth);
      }
      
      if (weighted[pointX][pointY - 1] == 999) {
        ;
      }
      else if ((weighted[pointX][pointY - 1] == 0) || (weighted[pointX][pointY - 1] < weighted[pointX][pointY] + 1)) {
        weighted[pointX][pointY - 1] = weighted[pointX][pointY] + 1;
        depth = fillHelpLong(maze, pointX, pointY - 1, weighted, size, depth);
      }

    }

    return depth;

  }

}
