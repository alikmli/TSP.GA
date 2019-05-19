package tsp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class City {
	private final int cityID;
	private double[] dists;
	private List<Integer> invisiblePath=new ArrayList<Integer>();
	public City(int cityID,double[] dists) {
		this.cityID=cityID;
		this.dists=dists;
		
		for(int i=0;i<dists.length;i++) {
			if(dists[i] == -1)
				invisiblePath.add(i);
		}
	}
	
	public List<Integer> getInvisiblePath() {
		return invisiblePath;
	}
	

	public int getCityID() {
		return cityID;
	}
	
	public double distanceFrom(City city) {
		int id=city.getCityID();
		if(id>dists.length)
			throw new RuntimeException("Out of bound");
	
		
		return dists[id];
	}
	
	@Override
	public String toString() {
		return String.valueOf(cityID) + "," + Arrays.toString(dists);
	}
	
}
