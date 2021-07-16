import java.util.*;

public class FactChecker {

    /**
     * Checks if a list of facts is internally consistent.
     * That is, can they all hold true at the same time?
     * Or are two (or potentially more) facts logically incompatible?
     *
     * @param facts list of facts to check consistency of
     * @return true if all the facts are internally consistent, otherwise false.
     */
    public static boolean areFactsConsistent(List<Fact> facts) {
        //Creating new graph to represent collected data
        Graph factRepresentation = new Graph();
        //Add all tutors as vertices and facts as edges
        for (Fact fact : facts) {
            //already existing vertices handled in addVertex method
            factRepresentation.addVertex(fact.getPersonA());
            factRepresentation.addVertex(fact.getPersonB());
            if (fact.getType() == Fact.FactType.TYPE_ONE) {
                //true = type 1 vertex
                try {
                    factRepresentation.addEdge(fact.getPersonA(), fact.getPersonB(), true);
                } catch (edgeAlreadyExistsException edgeAlreadyExistsException) {
                    //edge already exists for these vertices hence inconsistent data
                    //Assumed no data duplication
                    return false;
                }
            } else if (fact.getType() == Fact.FactType.TYPE_TWO) {
                //false = type two vertex
                try {
                    factRepresentation.addEdge(fact.getPersonA(), fact.getPersonB(), false);
                } catch (edgeAlreadyExistsException edgeAlreadyExistsException) {
                    //edge already exists inconsistent data
                    //assumed no data duplication
                    return false;
                }
            }
        }
        //bfs traversal to check inconsistent data
        for (String startName : factRepresentation.names.keySet()) {
            //order for bfs traversal
            Queue<String> traversal = new LinkedList<>();
            //set to keep record of vertices that have been visited
            Set<String> visitedRecord = new HashSet<>();
            //set to keep record of visited edges
            Set<Graph.Edge> traversedEdgeSet = new HashSet<>();
            //add starting vertex to traversal
            traversal.add(startName);
            while (! traversal.isEmpty()) {
                //name of current vertex
                String currentName = traversal.remove();
                //vertex representation
                Graph.Vertex current = factRepresentation.nameToVertex(currentName);

                //get all vertices and adjacent vertices
                for (Graph.Vertex mappedVertex : current.getMap().keySet()) {
                    //if edge is not traversed
                    if (! traversedEdgeSet.contains(current.getMap().get(mappedVertex))) {

                        //edge is type2 edge, not a type1 edge
                        if (! current.getMap().get(mappedVertex).type1) {
                            //if already visited then a cycle exists
                            if (visitedRecord.contains(mappedVertex.getName())) {
                                return false;
                            } else {
                                //add to traversal queue
                                if (! traversedEdgeSet.contains(current.getMap().get(mappedVertex))) {
                                    traversal.add(mappedVertex.getName());
                                }
                            }
                            //edge has been traversed
                            traversedEdgeSet.add(current.getMap().get(mappedVertex));
                        } else if (current.getMap().get(mappedVertex).type1) {
                            //check if directed edge allows traversal in direction
                            if (current.getMap().get(mappedVertex).fromVertex().equals(currentName)) {
                                if (visitedRecord.contains(mappedVertex.getName())) {
                                    return false;
                                } else {
                                    //if not visited add to traversal
                                    if (! traversedEdgeSet.contains(current.getMap().get(mappedVertex))) {
                                        traversal.add(mappedVertex.getName());
                                    }
                                }
                                traversedEdgeSet.add(current.getMap().get(mappedVertex));
                            }
                        }
                    }
                }
                visitedRecord.add(current.getName());
            }
        }
        return true;
    }

    /**
     * A graph object
     */
    private static class Graph {

        //Mapping of name to their vertex representation
        private Map<String, Vertex> names;

        /**
         * Represents a vertex in the graph
         * (A vertex represents a person)
         */
        private class Vertex {

            //name of the person
            private String name;
            //Edges connecting this vertex to other vertices
            private Map<Vertex, Edge> adjacency;

            /**
             * Creates a new vertex representing a person with name
             * @param name name of person
             */
            private Vertex(String name) {
                this.name = name;
                this.adjacency = new HashMap<>();
            }

            /**
             * Returns the name of person this vertex represents
             * @return name
             */
            private String getName() {
                return this.name;
            }

            /**
             * Returns a pointer to adjacency map of this vertex
             * @return
             */
            private Map<Vertex, Edge> getMap() {
                return this.adjacency;
            }
        }

        /**
         * An edge object in this graph.
         * There are two types of edges type1 and type2
         * Type1 edges are directed edges between their endpoints
         * Type2 edges are not directed
         */
        private class Edge {

            //endpoint string representation of vertex of this edge
            private String[] endpoints;
            //true if edge is type1 false otherwise
            private boolean type1;

            /**
             * Creates a new edge between the given people
             * @param vertex1 endpoint of edge
             * @param vertex2 endpoint of edge
             * @param type1 True if edge is type1
             */
            private Edge(String vertex1, String vertex2, boolean type1) {
                this.endpoints = new String[]{vertex1, vertex2};
                this.type1 = type1;
            }

            /**
             * Returns whether edge is type1 or not
             * @return true if edge is type1, false otherwise
             */
            private boolean isType1() {
                return this.type1;
            }

            /**
             * Returns a endpoint of this vertex.
             * For a type2 edge this is the origin vertex
             * @return name of endpoint of this vertex
             */
            private String fromVertex() {
                //first element of endpoints is from
                return this.endpoints[0];
            }

            /**
             * Returns a endpoint of this vertex.
             * For a type2 edge this is the to vertex
             * @return name of endpoint of this vertex
             */
            private String toVertex() {
                //second element of endpoints is to
                return this.endpoints[1];
            }
        }

        /**
         * Creates a new Graph with vertices and edges
         */
        private Graph() {
            this.names = new HashMap<>();
        }

        /**
         * Creates a vertex representing person with name.
         * A a vertex representation already exists does not make a new vertex
         * @param name name of person
         */
        private void addVertex(String name) {
            if (this.names.get(name) == null) {
                //if a vertex representing this name does not exist add new
                Vertex toAdd = new Vertex(name);
                System.out.println("added vertex " + name);
                this.names.put(name, toAdd);
            }
        }

        /**
         * Creates and adds a edge between two vertices
         * Adds new edge if edge dosent already exist
         * @param vertex1 name of vertex
         * @param vertex2 name of vertex
         * @param type1 true if vertex is type1 vertex, false if type2 vertex
         * @throws edgeAlreadyExistsException if edge already exists between these vertices in the graph
         */
        private void addEdge(String vertex1, String vertex2, boolean type1) throws edgeAlreadyExistsException {
            Vertex vertex1Representation = this.names.get(vertex1);
            Vertex vertex2Representation = this.names.get(vertex2);
            if (vertex1Representation.getMap().get(vertex2Representation) == null) {
                Edge newData = new Edge(vertex1, vertex2, type1);
                vertex1Representation.getMap().put(vertex2Representation, newData);
                vertex2Representation.getMap().put(vertex1Representation, newData);
            } else {
                //if a edge already exists between two vertices (people)
                //Assumed no duplicte data exists
                throw new edgeAlreadyExistsException();
            }
        }

        /**
         * Returns the vertex representing person with the given name in this graph
         * @param name name of person
         * @return vertex representing the person
         */
        private Vertex nameToVertex(String name) {
            return this.names.get(name);
        }
    }

    /**
     * Exception thrown if an edge already exists between two vertices
     */
    private static class edgeAlreadyExistsException extends Exception {

        /**
         * Constructs a normal edgeAlreadyExistsException with no error message or cause
         */
        private edgeAlreadyExistsException() {
            super();
        }
    }
}