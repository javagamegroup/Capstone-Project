
public class TurtleDuckVector extends SpriteVector{
	
	public TurtleDuckVector(Background back) {
		super(back);
	}

	public int add(Sprite s) {
		// Only allow up to sprites at once
		if (size() <= 5)
			return super.add(s);
		return -1;
	}
	
	protected boolean collision(int i, int iHit) {
		// Do nothing!
		return false;
	}
}
