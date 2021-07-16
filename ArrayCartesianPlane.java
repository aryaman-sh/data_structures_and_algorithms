/**
 * A 2D cartesian plane implemented as with an array. Each (x,y) coordinate can
 * hold a single item of type <T>.
 *
 * @param <T> The type of element held in the data structure
 */
public class ArrayCartesianPlane<T> implements CartesianPlane<T> {
    /* Minimum value X in this plane*/
    private int minimumX;
    /* Minimum value of Y in this plane */
    private int minimumY;
    /* Maximum value of X in this plane */
    private int maximumX;
    /* Maximum value of Y in this plane */
    private int maximumY;
    /* number of elements in x and y*/
    private int xSize;
    private int ySize;
    private Object[][] plane;

    /**
     * Constructs a new ArrayCartesianPlane object with given minimum and
     * maximum bounds.
     *
     * Note that these bounds are allowed to be negative.
     *
     * @param minimumX A new minimum bound for the x values of
     *         elements.
     * @param maximumX A new maximum bound for the x values of
     *         elements.
     * @param minimumY A new minimum bound for the y values of
     *         elements.
     * @param maximumY A new maximum bound for the y values of
     *         elements.
     * @throws IllegalArgumentException if the x minimum is greater
     *         than the x maximum (and resp. with y min/max)
     */
    public ArrayCartesianPlane(int minimumX, int maximumX, int minimumY,
                               int maximumY) throws IllegalArgumentException {
        if (minimumX > maximumX || minimumY > maximumY) {
            throw new IllegalArgumentException();
        }
        this.minimumX = minimumX;
        this.maximumX = maximumX;
        this.minimumY = minimumY;
        this.maximumY = maximumY;
        this.xSize = this.maximumX - this.minimumX + 1;
        this.ySize = this.maximumY - this.minimumY + 1;
        this.plane = new Object[this.xSize][this.ySize];
    }

    /**
     * Add an element at a fixed position, overriding any existing element
     * there.
     *
     * @param x The x-coordinate of the element's position
     * @param y The y-coordinate of the element's position
     * @param element The element to be added at the indicated
     *         position
     * @throws IllegalArgumentException If the x or y value is out of
     *         the grid's minimum/maximum bounds
     */
    @Override
    public void add(int x, int y, T element) throws IllegalArgumentException {
        if (x < this.minimumX || x > this.maximumX || y < this.minimumY || y > this.maximumY) {
            throw new IllegalArgumentException("x or y out of bounds of plane");
        }
        this.plane[x - this.minimumX][y - this.minimumY] = element;
    }

    /**
     * Returns the element at the indicated position.
     *
     * @param x The x-coordinate of the element to retrieve
     * @param y The y-coordinate of the element to retrieve
     * @return The element at this position, or null is no elements exist
     * @throws IndexOutOfBoundsException If the x or y value is out of
     *         the grid's minimum/maximum bounds
     */
    @Override
    public T get(int x, int y) throws IndexOutOfBoundsException {
        if (x < this.minimumX || x > this.maximumX || y < this.minimumY || y > this.maximumY) {
            throw new IndexOutOfBoundsException();
        }
        @SuppressWarnings("unchecked")
        T returnObj = (T) this.plane[x - this.minimumX][y - this.minimumY];
        return returnObj;
    }

    /**
     * Removes the element at the indicated position.
     *
     * @param x The x-coordinate of the element to remove
     * @param y The y-coordinate of the element to remove
     * @return true if an element was successfully removed, false if no element
     *         exists at (x, y)
     * @throws IndexOutOfBoundsException If the x or y value is out of
     *         the grid's minimum/maximum bounds
     */
    @Override
    public boolean remove(int x, int y) throws IndexOutOfBoundsException {
        boolean returnValue;
        if (x < this.minimumX || x > this.maximumX || y < this.minimumY || y > this.maximumY) {
            throw new IndexOutOfBoundsException();
        }
        if (plane[x - this.minimumX][y - this.minimumY] == null) {
            returnValue = false;
        } else {
            plane[x - this.minimumX][y - this.minimumY] = null;
            returnValue = true;
        }
        return returnValue;
    }

    /**
     * Removes all elements stored in the grid.
     */
    @Override
    public void clear() {
        for (int i = 0; i < this.xSize; i++) {
            for (int j = 0; j < this.ySize; j++) {
                plane[i][j] = null;
            }
        }
    }

    /**
     * Changes the size of the grid. Existing elements should remain at the
     * same (x, y) coordinate If a resizing operation has invalid dimensions or
     * causes an element to be lost, the grid should remain unmodified and an
     * IllegalArgumentException thrown
     *
     * @param newMinimumX A new minimum bound for the x values of
     *         elements.
     * @param newMaximumX A new maximum bound for the x values of
     *         elements.
     * @param newMinimumY A new minimum bound for the y values of
     *         elements.
     * @param newMaximumY A new maximum bound for the y values of
     *         elements.
     * @throws IllegalArgumentException if the x minimum is greater
     *         than the x maximum (and resp. with y min/max) or if an element
     *         would be lost after this resizing operation
     */
    @Override
    public void resize(int newMinimumX, int newMaximumX, int newMinimumY, int newMaximumY)
            throws IllegalArgumentException {
        try {
            if (newMinimumX > newMaximumX || newMinimumY > newMaximumY) {
                throw new IllegalArgumentException();
            }
            if (newMinimumX > this.minimumX) {
                for (int i = 0; i < (newMaximumX - this.minimumX); i++) {
                    for (int j = 0; j < ySize; j++) {
                        if (this.plane[i][j] != null) {
                            throw new IllegalArgumentException();
                        }
                    }
                }
            }
            if (newMaximumX < this.maximumX) {
                for (int i = (xSize - (this.maximumX - newMaximumX)); i < xSize; i++) {
                    for (int j = 0; j < ySize; j++) {
                        if (this.plane[i][j] != null) {
                            throw new IllegalArgumentException();
                        }
                    }
                }
            }
            if (newMinimumY > this.minimumY) {
                for (int i = 0; i < (newMaximumY - this.minimumY); i++) {
                    for (int j = 0; j < xSize; j++) {
                        if (this.plane[j][i] != null) {
                            throw new IllegalArgumentException();
                        }
                    }
                }
            }
            if (newMaximumY < this.maximumY) {
                for (int i = (ySize - (this.maximumY - newMaximumY)); i < ySize; i++) {
                    for (int j = 0; j < xSize; j++) {
                        if (this.plane[j][i] != null) {
                            throw new IllegalArgumentException();
                        }
                    }
                }
            }
            //Create a new ArrayCartesianPlane object
            ArrayCartesianPlane<T> newPlane = new ArrayCartesianPlane<>(newMinimumX, newMaximumX,
                    newMinimumY, newMaximumY);
            for (int i = 0; i < this.xSize; i++) {
                for (int j = 0; j < this.ySize; j++) {
                    if (this.plane[i][j] != null) {
                        newPlane.add(i + this.minimumX, j + this.minimumY, this.get(i
                                + this.minimumX, j + this.minimumY));
                    }
                }
            }
            this.clear();
            this.plane = new Object[newMaximumX - newMinimumX + 1][newMaximumY - newMinimumY + 1];
            this.xSize = newMaximumX - newMinimumX + 1;
            this.ySize = newMaximumY - newMinimumY + 1;
            this.minimumX = newMinimumX;
            this.maximumX = newMaximumX;
            this.minimumY = newMinimumY;
            this.maximumY = newMaximumY;
            for (int i = 0; i < this.xSize; i++) {
                for (int j = 0; j < this.ySize; j++) {
                    this.plane[i][j] = newPlane.get(i + minimumX, j + minimumY);
                }
            }
            //new plane deleted when garbage collector runs
            newPlane = null;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException();
        }
    }
}
