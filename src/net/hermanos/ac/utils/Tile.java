package net.hermanos.ac.utils;

public final class Tile
{
    private final int x;
    private final int y;
    private final int z;
    private boolean passable;
    private double g;
    private double h;
    private Tile parent;
    
    public double getF() {
        return this.h + this.g;
    }
    
    public Tile(final int x, final int y, final int z) {
        this.passable = true;
        this.g = Double.MAX_VALUE;
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public int getZ() {
        return this.z;
    }
    
    public boolean isPassable() {
        return this.passable;
    }
    
    public double getG() {
        return this.g;
    }
    
    public double getH() {
        return this.h;
    }
    
    public Tile getParent() {
        return this.parent;
    }
    
    public void setPassable(final boolean passable) {
        this.passable = passable;
    }
    
    public void setG(final double g) {
        this.g = g;
    }
    
    public void setH(final double h) {
        this.h = h;
    }
    
    public void setParent(final Tile parent) {
        this.parent = parent;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Tile)) {
            return false;
        }
        final Tile other = (Tile)o;
        if (this.getX() != other.getX()) {
            return false;
        }
        if (this.getY() != other.getY()) {
            return false;
        }
        if (this.getZ() != other.getZ()) {
            return false;
        }
        if (this.isPassable() != other.isPassable()) {
            return false;
        }
        if (Double.compare(this.getG(), other.getG()) != 0) {
            return false;
        }
        if (Double.compare(this.getH(), other.getH()) != 0) {
            return false;
        }
        final Tile this$parent = this.getParent();
        final Tile other$parent = other.getParent();
        if (this$parent == null) {
            if (other$parent == null) {
                return true;
            }
        }
        else if (this$parent.equals(other$parent)) {
            return true;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        @SuppressWarnings("unused")
		final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getX();
        result = result * 59 + this.getY();
        result = result * 59 + this.getZ();
        result = result * 59 + (this.isPassable() ? 79 : 97);
        final long $g = Double.doubleToLongBits(this.getG());
        result = result * 59 + (int)($g >>> 32 ^ $g);
        final long $h = Double.doubleToLongBits(this.getH());
        result = result * 59 + (int)($h >>> 32 ^ $h);
        final Tile $parent = this.getParent();
        result = result * 59 + (($parent == null) ? 43 : $parent.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "Tile(x=" + this.getX() + ", y=" + this.getY() + ", z=" + this.getZ() + ", passable=" + this.isPassable() + ", g=" + this.getG() + ", h=" + this.getH() + ", parent=" + this.getParent() + ")";
    }
}
