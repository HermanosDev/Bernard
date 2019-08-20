package net.hermanos.ac.utils;

import java.util.*;

public final class UtilPath
{
    private static final int[][] ADJACENT;
    private Tile[][][] area;
    private Tile start;
    private Tile end;
    private List<Tile> open;
    private List<Tile> closed;
    
    static {
        ADJACENT = new int[][] { { -1, 0, 0 }, { 0, -1, 0 }, { 0, 0, -1 }, { 1, 0, 0 }, { 0, 1, 0 }, { 0, 0, 1 } };
    }
    
    public static boolean hasPath(final Tile[][][] area, final Tile start, final Tile end) {
        return new UtilPath(area, start, end).process();
    }
    
    public static List<Tile> getPath(final Tile[][][] area, final Tile start, Tile end) {
        final UtilPath pathFinder = new UtilPath(area, start, end);
        if (!pathFinder.process()) {
            return null;
        }
        final LinkedList<Tile> route = new LinkedList<Tile>();
        route.add(end);
        Tile parent;
        while ((parent = end.getParent()) != null) {
            route.add(parent);
            end = parent;
        }
        Collections.reverse(route);
        return new ArrayList<Tile>(route);
    }
    
    private boolean process() {
        for (int x = 0; x < this.area.length; ++x) {
            for (int y = 0; y < this.area[x].length; ++y) {
                for (int z = 0; z < this.area[x][y].length; ++z) {
                    this.area[x][y][z].setH(Math.abs(this.end.getX() - x) + Math.abs(this.end.getY() - y) + Math.abs(this.end.getZ() - z));
                }
            }
        }
        this.open.add(this.start);
        this.start.setG(0.0);
        while (!this.closed.contains(this.end)) {
            final Tile current = this.getNextTile();
            if (current == null) {
                return false;
            }
            this.processAdjacentTiles(current);
        }
        return true;
    }
    
    private Tile getNextTile() {
        double f = Double.MAX_VALUE;
        Tile next = null;
        for (final Tile tile : this.open) {
            if (tile.getF() >= f && f != Double.MAX_VALUE) {
                continue;
            }
            f = tile.getF();
            next = tile;
        }
        if (next == null) {
            return null;
        }
        this.open.remove(next);
        this.closed.add(next);
        return next;
    }
    
    private void processAdjacentTiles(final Tile base) {
        int[][] adjacent;
        for (int length = (adjacent = UtilPath.ADJACENT).length, i = 0; i < length; ++i) {
            final int[] modifier = adjacent[i];
            final int x = base.getX() + modifier[0];
            final int y = base.getY() + modifier[1];
            final int z = base.getZ() + modifier[2];
            final Tile current;
            if (x >= 0 && y >= 0 && z >= 0 && this.area.length > x && this.area[x].length > y + 1 && this.area[x][y].length > z && (current = this.area[x][y][z]).isPassable() && this.area[x][y + 1][z].isPassable()) {
                if (current.getG() > base.getG() + 1.0) {
                    current.setG(base.getG() + 1.0);
                    current.setParent(base);
                    this.open.add(current);
                }
            }
        }
    }
    
    public UtilPath(final Tile[][][] area, final Tile start, final Tile end) {
        this.open = new ArrayList<Tile>();
        this.closed = new ArrayList<Tile>();
        this.area = area;
        this.start = start;
        this.end = end;
    }
    
    public Tile[][][] getArea() {
        return this.area;
    }
    
    public Tile getStart() {
        return this.start;
    }
    
    public Tile getEnd() {
        return this.end;
    }
    
    public List<Tile> getOpen() {
        return this.open;
    }
    
    public List<Tile> getClosed() {
        return this.closed;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof UtilPath)) {
            return false;
        }
        final UtilPath other = (UtilPath)o;
        if (!Arrays.deepEquals(this.getArea(), other.getArea())) {
            return false;
        }
        final Tile this$start = this.getStart();
        final Tile other$start = other.getStart();
        Label_0071: {
            if (this$start == null) {
                if (other$start == null) {
                    break Label_0071;
                }
            }
            else if (this$start.equals(other$start)) {
                break Label_0071;
            }
            return false;
        }
        final Tile this$end = this.getEnd();
        final Tile other$end = other.getEnd();
        Label_0108: {
            if (this$end == null) {
                if (other$end == null) {
                    break Label_0108;
                }
            }
            else if (this$end.equals(other$end)) {
                break Label_0108;
            }
            return false;
        }
        final List<Tile> this$open = this.getOpen();
        final List<Tile> other$open = other.getOpen();
        Label_0147: {
            if (this$open == null) {
                if (other$open == null) {
                    break Label_0147;
                }
            }
            else if (this$open.equals(other$open)) {
                break Label_0147;
            }
            return false;
        }
        final List<Tile> this$closed = this.getClosed();
        final List<Tile> other$closed = other.getClosed();
        if (this$closed == null) {
            if (other$closed == null) {
                return true;
            }
        }
        else if (this$closed.equals(other$closed)) {
            return true;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        @SuppressWarnings("unused")
		final int PRIME = 59;
        int result = 1;
        result = result * 59 + Arrays.deepHashCode(this.getArea());
        final Tile $start = this.getStart();
        result = result * 59 + (($start == null) ? 43 : $start.hashCode());
        final Tile $end = this.getEnd();
        result = result * 59 + (($end == null) ? 43 : $end.hashCode());
        final List<Tile> $open = this.getOpen();
        result = result * 59 + (($open == null) ? 43 : $open.hashCode());
        final List<Tile> $closed = this.getClosed();
        result = result * 59 + (($closed == null) ? 43 : $closed.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "UtilPath(area=" + Arrays.deepToString(this.getArea()) + ", start=" + this.getStart() + ", end=" + this.getEnd() + ", open=" + this.getOpen() + ", closed=" + this.getClosed() + ")";
    }
}
