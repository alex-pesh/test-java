package patterns.flyweight;

public class Symbol implements Drawable {

	private String symbol;
	private String style;

	public Symbol(String symbol, String style) {
		this.symbol = symbol;
		this.style = style;
	}

	@Override
	public void draw(PositionContext position) {
		System.out.println(
				String.format("Printing symbol '%s' with style %s at position [%d, %d]",
				symbol, style, position.x(), position.y()) );
	}

}
