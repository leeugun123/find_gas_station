package org.techtown.find_gas_station.Data.kakaoResponseModel;

import java.util.List;

public class OneRouteResponse {

    private String trans_id;
    private List<Route> routes;

    public String getTransId() {
        return trans_id;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public static class Route {
        private int result_code;
        private String result_msg;
        private Summary summary;
        private List<Section> sections;

        public int getResultCode() {
            return result_code;
        }

        public String getResultMessage() {
            return result_msg;
        }

        public Summary getSummary() {
            return summary;
        }

        public List<Section> getSections() {
            return sections;
        }
    }

    public static class Summary {
        private Location origin;
        private Location destination;
        private List<Location> waypoints;
        private String priority;
        private Bound bound;
        private Fare fare;
        public int distance;
        public int duration;

        public Location getOrigin() {
            return origin;
        }

        public Location getDestination() {
            return destination;
        }

        public List<Location> getWaypoints() {
            return waypoints;
        }

        public String getPriority() {
            return priority;
        }

        public Bound getBound() {
            return bound;
        }

        public Fare getFare() {
            return fare;
        }

        public int getDistance() {
            return this.distance;
        }

        public int getDuration() {
            return this.duration;
        }

    }

    public static class Location {
        private String name;
        private double x;
        private double y;

        public String getName() {
            return name;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }
    }

    public static class Bound {
        private double min_x;
        private double min_y;
        private double max_x;
        private double max_y;

        public double getMinX() {
            return min_x;
        }

        public double getMinY() {
            return min_y;
        }

        public double getMaxX() {
            return max_x;
        }

        public double getMaxY() {
            return max_y;
        }
    }

    public static class Fare {
        private int taxi;
        private int toll;

        public int getTaxi() {
            return taxi;
        }

        public int getToll() {
            return toll;
        }
    }

    public static class Section {
        private int distance;
        private int duration;
        private Bound bound;
        private List<Road> roads;
        private List<Guide> guides;

        public int getDistance() {
            return distance;
        }

        public int getDuration() {
            return duration;
        }

        public Bound getBound() {
            return bound;
        }

        public List<Road> getRoads() {
            return roads;
        }

        public List<Guide> getGuides() {
            return guides;
        }
    }

    public static class Road {
        private String name;
        private int distance;
        private int duration;
        private int traffic_speed;
        private int traffic_state;
        private List<Double> vertexes;

        public String getName() {
            return name;
        }

        public int getDistance() {
            return distance;
        }

        public int getDuration() {
            return duration;
        }

        public int getTrafficSpeed() {
            return traffic_speed;
        }

        public int getTrafficState() {
            return traffic_state;
        }

        public List<Double> getVertexes() {
            return vertexes;
        }
    }

    public static class Guide {
        private String name;
        private double x;
        private double y;
        private int distance;
        private int duration;
        private int type;
        private String guidance;
        private int road_index;

        public String getName() {
            return name;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public int getDistance() {
            return distance;
        }

        public int getDuration() {
            return duration;
        }

        public int getType() {
            return type;
        }

        public String getGuidance() {
            return guidance;
        }

        public int getRoadIndex() {
            return road_index;
        }
    }

}
