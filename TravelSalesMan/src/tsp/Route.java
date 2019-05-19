package tsp;

public class Route {
	private City[] route;

	public Route(Individuals individuals, City[] cities) {
		int[] chromsome = individuals.getChromsome();
		route = new City[cities.length];

		for (int i = 0; i < chromsome.length; i++) {
			route[i] = cities[chromsome[i]];
		}
	}

	public double getDistance() {

		double totalDistance = 0;
		for (int i = 0; i + 1 < route.length; i++) {
			double dist = route[i].distanceFrom(route[i + 1]);
			if (dist == -1)
				return 0;
			totalDistance += dist;
		}
		double dist = route[route.length - 1].distanceFrom(route[0]);
		if (dist == -1)
			return 0;
		totalDistance += dist;

		return totalDistance;
	}
}
