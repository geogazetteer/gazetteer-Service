/**
 * 
 */
package top.geomatics.gazetteer.model;

/**
 * @author whudyj
 *
 */
public class BoundingBox {
	private Double minx;
	private Double miny;
	private Double maxx;
	private Double maxy;

	public BoundingBox(Double minx, Double miny, Double maxx, Double maxy) {
		super();
		this.minx = minx;
		this.miny = miny;
		this.maxx = maxx;
		this.maxy = maxy;
	}
	

	public BoundingBox() {
		super();
	}


	public Double getMinx() {
		return minx;
	}

	public void setMinx(Double minx) {
		this.minx = minx;
	}

	public Double getMiny() {
		return miny;
	}

	public void setMiny(Double miny) {
		this.miny = miny;
	}

	public Double getMaxx() {
		return maxx;
	}

	public void setMaxx(Double maxx) {
		this.maxx = maxx;
	}

	public Double getMaxy() {
		return maxy;
	}

	public void setMaxy(Double maxy) {
		this.maxy = maxy;
	}

}
