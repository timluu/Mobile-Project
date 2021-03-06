
import java.util.Enumeration;
import java.util.Random;
import java.util.Vector;
import javax.microedition.m3g.Transform;

// This class creates the maze
class Maze {

    private final static int MAX_SIZE = 31;
    // dimension of the maze
    private final float mazeSideLength, mazeHeight;
    // store the location on X of the start and end
    private int startX = -1, endX = -1;
    // maze rows are encoded as bits in a long field
    private long[] maze;
    // utility variables
    private float mazeOrigin, spaceBetweenPlanes;

    Maze(int corridorCount, float mazeSideLength, float mazeHeight) {
        if (corridorCount > MAX_SIZE) {
            throw new IllegalArgumentException("Maze too big");
        }
        this.mazeSideLength = mazeSideLength;
        this.mazeHeight = mazeHeight;
        createNew(corridorCount);
    }

    // creates a plane located at the start of the maze
    Plane createStartMark() {
        Transform markTransform = new Transform();
        markTransform.postTranslate(mazeOrigin
                + startX * spaceBetweenPlanes,
                mazeHeight / 2, mazeOrigin);
        markTransform.postScale(3 * spaceBetweenPlanes / 4, 1f, 1f);

        return new Plane(markTransform, 1f);
    }

    // creates a plane located at the end of the maze
    Plane createEndMark() {
        Transform markTransform = new Transform();
        markTransform.postTranslate(mazeOrigin
                + endX * spaceBetweenPlanes,
                mazeHeight / 2, -mazeOrigin);
        markTransform.postScale(3 * spaceBetweenPlanes / 4, 1f, 1f);
        markTransform.postRotate(180f, 0f, 1f, 0f);

        return new Plane(markTransform, 1f);
    }

    // Calculate the start X coordinate
    float findStartLocationX() {
        return mazeOrigin + (startX + 0.5f) * spaceBetweenPlanes;
    }

    // Calculate the start Z coordinate
    float findStartLocationZ() {
        return mazeOrigin + spaceBetweenPlanes;
    }

    // checks if the location (x, z) is at the end given certain tolerance
    boolean isAtTheEnd(float x, float z, float tolerance) {
        // just use linear distances
        return Math.abs(x - (mazeOrigin + endX * spaceBetweenPlanes))
                < tolerance
                && Math.abs(z + mazeOrigin) < tolerance;
    }

    // creates the horizontal and vertical planes and puts
    // them in an enumeration
    Enumeration createPlanes() {
        // Space between walls
        spaceBetweenPlanes = mazeSideLength / (maze.length - 1);
        mazeOrigin = -mazeSideLength / 2;

        Vector allPlanes = new Vector();
        for (int i = 0; i < maze.length; i++) {
            int startX = -1;
            for (int j = 0; j < maze.length; j++) {
                long shift = (0x1L << j);
                if ((maze[i] & shift) == shift && startX == -1) {
                    startX = j;
                    continue;
                }
                if ((((maze[i] & shift) == 0) || (j == (maze.length - 1)))
                        && (startX >= 0)) {
                    int steps = j - startX;
                    // Don't create walls of side 1 since they
                    // will be created on the other direction
                    if (steps == 1) {
                        startX = -1;
                        continue;
                    }
                    // compensate that the last item is always 1
                    if (j == (maze.length - 1)) {
                        steps++;
                    }
                    Transform planeTransform = new Transform();
                    // Divided by 2 since the original square is of side 2;
                    float wallWidth = (spaceBetweenPlanes * (steps - 1) / 2);
                    //      System.out.println((int)wallWidth);
                    // Move to the correct position
                    planeTransform.postTranslate(
                            mazeOrigin + spaceBetweenPlanes * startX + wallWidth,
                            mazeHeight, mazeOrigin + spaceBetweenPlanes * i);
                    // give the actual size
                    planeTransform.postScale(wallWidth, mazeHeight, 1f);
                    allPlanes.addElement(new Plane(planeTransform, 1f));
                    startX = -1;
                }
            }
        }
        // System.out.println("begin");
        for (int i = 0; i < maze.length; i++) {
            int startY = -1;
            long shift = (0x1L << i);
            for (int j = 0; j < maze.length; j++) {
                if ((maze[j] & shift) == shift && startY == -1) {
                    startY = j;
                    continue;
                }
                if ((((maze[j] & shift) == 0) || (j == (maze.length - 1)))
                        && (startY >= 0)) {
                    int steps = j - startY;
                    if (steps == 1) {
                        startY = -1;
                        continue;
                    }
                    if (j == (maze.length - 1)) {
                        steps++;
                    }
                    Transform planeTransform = new Transform();
                    // Divided by 2 since the original square is of side 2;
                    float wallWidth = (spaceBetweenPlanes * (steps - 1) / 2);
                    // translate to the correct position
                    planeTransform.postTranslate(
                            mazeOrigin + spaceBetweenPlanes * i, mazeHeight,
                            mazeOrigin + spaceBetweenPlanes * startY + wallWidth);
                    // rotate 90 degrees since this is a vertical wall
                    planeTransform.postRotate(90f, 0f, 1f, 0f);
                    //  ����
                    planeTransform.postScale(wallWidth, mazeHeight, 1f);
                    allPlanes.addElement(new Plane(planeTransform, 1f));
                    startY = -1;
                }
            }
        }
        //System.out.println(allPlanes.size());
        return allPlanes.elements();
    }

    // Create new random maze
    private void createNew(int mazeSize) {
        Random random = new Random();
        int totalSize = mazeSize * 2 + 1;
        maze = new long[totalSize];
        int backtrack[] = new int[mazeSize * mazeSize];
        int backtrackIndex = 0;
        for (int i = 0; i < maze.length; i++) {
            maze[i] = -1;
        }
        int x = 1;
        int y = 1;
        int UP = 0;
        int DOWN = 1;
        int LEFT = 2;
        int RIGHT = 3;

        // traverse the maze finding unconnected spots
        while (true) {
            //	System.out.println("("+x+","+y+")");
            maze[x] &= ~(0x1 << y);
            int currentBacktrackIndex = backtrackIndex;
            int directions[] = new int[4];
            int availableSpaces = 0;
            if (y - 2 > 0 && (maze[x] & (0x1 << (y - 2))) != 0) {
                directions[availableSpaces++] = UP;
            }
            if (y + 2 < totalSize && (maze[x] & (0x1 << (y + 2))) != 0) {
                directions[availableSpaces++] = DOWN;
            }
            if (x + 2 < totalSize && (maze[x + 2] & (0x1 << y)) != 0) {
                directions[availableSpaces++] = LEFT;
            }
            if (x - 2 > 0 && (maze[x - 2] & (0x1 << y)) != 0) {
                directions[availableSpaces++] = RIGHT;
            }

            if (availableSpaces > 0) {
                int chosenDirection =
                        directions[(random.nextInt() >>> 1) % availableSpaces];
                backtrack[backtrackIndex++] = chosenDirection;
                if (chosenDirection == UP) {
                    maze[x] &= ~(0x1 << (y - 1));
                    y -= 2;
                }
                if (chosenDirection == DOWN) {
                    maze[x] &= ~(0x1 << (y + 1));
                    y += 2;
                }
                if (chosenDirection == LEFT) {
                    maze[x + 1] &= ~(0x1 << y);
                    x += 2;
                }
                if (chosenDirection == RIGHT) {
                    maze[x - 1] &= ~(0x1 << y);
                    x -= 2;
                }
            } // if nothing is not visitied in the neigbourhood backtrack
            else {
                if (backtrackIndex == 0) {
                    // end of algorithm
                    break;
                }
                backtrackIndex--;
                if (backtrack[backtrackIndex] == UP) {
                    y += 2;
                } else if (backtrack[backtrackIndex] == DOWN) {
                    y -= 2;
                } else if (backtrack[backtrackIndex] == LEFT) {
                    x -= 2;
                } else if (backtrack[backtrackIndex] == RIGHT) {
                    x += 2;
                }
            }
        }

        // find start and end points
        while (true) {
            int pos = (random.nextInt() >>> 1) % totalSize;
            if (startX < 0 && (maze[1] & (0x1 << pos)) == 0) {
                startX = pos;
                maze[0] &= ~(0x1 << pos);
            }
            if (endX < 0 && (maze[maze.length - 2] & (0x1 << pos)) == 0) {
                endX = pos;
                maze[maze.length - 1] &= ~(0x1 << pos);
            }
            if (endX >= 0 && startX >= 0) {
                break;
            }
        }
        /*
         * for(int i=0;i<maze.length;i++){ System.out.println(maze[i]);
         *
         * }
         * System.out.println(startX);
    System.out.println(endX);
         */
    }
}
