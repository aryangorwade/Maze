import becker.robots.*;

/**
 * CS1A - Assignment 3 - "The Maze" <br>
 * Quarter: Fall 2020 <br>
 * <p>
 * In this program, a robot navigates the maze by feeling the right wall. The
 * robot will drop a Thing at every intersection it covers. If there already is
 * a Thing at an intersection it covers, the robot will not put down a Thing. If
 * the robot's backpack is empty, it will not put down a thing either. Before
 * the robot moves, it turns right and checks if the front is clear. If
 * frontIsClear returns false when there is a wall, it will turn left (to return
 * to its original orientation or will continue to turn until the front is
 * clear). Once the front is clear the robot will then move forward 1
 * intersection.
 * </p>
 * <p>
 * Every time the robot moves 1 intersection, the program tracks the direction
 * moved by checking the robot's direction. Once the robot reaches the maze's
 * exit, it will stop. The total number of spaces moved and the the number of
 * spaces moved in all 4 directions will be printed.
 * </p>
 * <br>
 * 
 * @author Aryan Gorwade
 * @author <My partner prefers to remain anonymous>
 */
class MazeBot extends RobotSE
{
    private int movesWest = 0;
    private int movesEast = 0;
    private int movesSouth = 0;
    private int movesNorth = 0;

    public MazeBot(City theCity, int str, int ave, Direction dir, int numThings)
    {
        super(theCity, str, ave, dir, numThings);
    }

    /**
     * This method overrides Becker's original move method. It helps the MazeBot
     * navigate through the maze by feeling the right wall. The MazeBot turns right
     * and determines if its front is clear. If frontIsClear returns false, the
     * MazeBot will turn left until frontIsClear returns true. It will then move one
     * space forward and will increment the value of one of the counters, movesWest,
     * movesEast, movesNorth, and movesSouth according to the direction it moved in.
     */
    public void move()
    {
        super.move();
        if (this.getDirection() == Direction.EAST)
        {
            ++movesEast;
        }
        if (this.getDirection() == Direction.WEST)
        {
            ++movesWest;
        }
        if (this.getDirection() == Direction.NORTH)
        {
            ++movesNorth;
        }
        if (this.getDirection() == Direction.SOUTH)
        {
            ++movesSouth;
        }
    }

    /**
     * The putThing method overrides Becker's original putThing method. It ensures
     * that the MazeBot is able to put things down safely, meaning it will only try
     * to put a thing down if countThingsInBackpack is greater than 0 to prevent the
     * MazeBot from breaking. Additionally, the MazeBot will only put down a thing
     * if there is no other Thing on the intersection.
     */
    public void putThing()
    {
        if (countThingsInBackpack() > 0)
        {
            if (!this.canPickThing())
            {
                super.putThing();
            }
        }

    }

    /**
     * This method prints out a celebratory message for the MazeBot, along with the
     * values of the four counters, movesWest, movesEast, movesNorth, and
     * movesSouth. Additionally, the method will also print out the number of total
     * moves made by the MazeBot.
     */
    public void printEverything()
    {

        System.out.println("Hooray! MazeBot don has succesfully completed the maze!! \n");
        System.out.println("Total number of eastward movements: " + movesEast);
        System.out.println("Total number of westward movements: " + movesWest);
        System.out.println("Total number of northward movements: " + movesNorth);
        System.out.println("Total number of southward movements: " + movesSouth);
        System.out.println("Total number of spaces moved: " + (movesEast + movesWest + movesNorth + movesSouth));

    }

    private boolean isAtEndSpot()
    {
        return getAvenue() == 9 && getStreet() == 10;
    }

    /**
     * The navigateMaze method will make the robot progress through the maze until
     * it reaches the end spot. The robot will progress through the maze by using
     * the overridden methods of putThing and move to safely put a Thing down and
     * progress 1 intersection.
     * 
     */
    public void navigateMaze()
    {

        while (!isAtEndSpot())
        {
            putThing();
            this.turnRight();

            while (!frontIsClear())
            {
                this.turnLeft();
            }
            move();
        }
        printEverything();
    }

}


public class Maze extends Object
{
    private static void makeMaze(City theCity)
    {
        for (int i = 1; i < 11; i++)
        {
            // north wall
            new Wall(theCity, 1, i, Direction.NORTH);

            // Second to north wall
            if (i <= 9)
                new Wall(theCity, 1, i, Direction.SOUTH);

            // Third to north wall
            if (i >= 4)
                new Wall(theCity, 4, i, Direction.SOUTH);

            // south wall
            if (i != 9) // (9, 10, SOUTH), is where the 'exit' is
                new Wall(theCity, 10, i, Direction.SOUTH);

            // west wall
            if (i != 1) // (1, 1, WEST) is where the 'entrance' is
                new Wall(theCity, i, 1, Direction.WEST);

            // second to most western wall
            if (i >= 3 && i < 6)
                new Wall(theCity, i, 6, Direction.WEST);

            // east wall
            new Wall(theCity, i, 10, Direction.EAST);
        }

        // Cul de Sac
        new Wall(theCity, 3, 10, Direction.WEST);
        new Wall(theCity, 3, 10, Direction.SOUTH);

        new Wall(theCity, 2, 8, Direction.WEST);
        new Wall(theCity, 2, 8, Direction.SOUTH);

        new Wall(theCity, 10, 8, Direction.NORTH);
        new Wall(theCity, 10, 9, Direction.EAST);
        new Wall(theCity, 10, 9, Direction.NORTH);
        makeSpiral(theCity, 8, 9, 3);
        new Wall(theCity, 8, 10, Direction.SOUTH);

        makeSpiral(theCity, 10, 5, 4);
    }

    public static void makeSpiral(City theCity, int st, int ave, int size)
    {
        // We start out building the wall northward
        // the walls will be built on the east face of the current
        // intersection
        Direction facing = Direction.EAST;

        while (size > 0)
        {
            int spacesLeft = size;
            int aveChange = 0;
            int stChange = 0;
            switch (facing)
            {
            case EAST:
                stChange = -1;
                break;
            case NORTH:
                aveChange = -1;
                break;
            case WEST:
                stChange = 1;
                break;
            case SOUTH:
                aveChange = 1;
                break;
            }

            while (spacesLeft > 0)
            {
                new Wall(theCity, st, ave, facing);
                ave += aveChange;
                st += stChange;
                --spacesLeft;
            }
            // back up one space
            ave -= aveChange;
            st -= stChange;

            switch (facing)
            {
            case EAST:
                facing = Direction.NORTH;
                break;
            case NORTH:
                facing = Direction.WEST;
                size--;
                break;
            case WEST:
                facing = Direction.SOUTH;
                break;
            case SOUTH:
                facing = Direction.EAST;
                size--;
                break;
            }
        }
    }


    // Main Method

    public static void main(String[] args)
    {
        City calgary = new City(12, 12);
        MazeBot don = new MazeBot(calgary, 1, 1, Direction.EAST, 1000);
        Maze.makeMaze(calgary);

        calgary.showThingCounts(true); // This will help the user see if he/she incorrectly put more than 1 thing down in any
                                       // intersections

        don.navigateMaze(); // <-- HERE'S WHERE THE NavigateMaze() method is called. 
        don.printEverything();
    }
}
