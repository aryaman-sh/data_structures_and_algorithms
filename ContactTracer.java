import java.util.*;

public class ContactTracer {

    //A graph representation of contact tracing data
    private Graph contactTracingData;

    /**
     * Initialises an empty ContactTracer with no populated contact traces.
     */
    public ContactTracer() {
        this.contactTracingData = new Graph();
    }

    /**
     * Initialises the ContactTracer and populates the internal data structures
     * with the given list of contract traces.
     *
     * @param traces to populate with
     * @require traces != null
     */
    public ContactTracer(List<Trace> traces) {
        this.contactTracingData = new Graph();
        for (Trace trace : traces) {
            addTrace(trace);
        }
    }

    /**
     * Adds a new contact trace to
     *
     * If a contact trace involving the same two people at the exact same time is
     * already stored, do nothing.
     *
     * @param trace to add
     * @require trace != null
     */
    public void addTrace(Trace trace) {
        //if vertex representation does not exists create new vertex
        if (! this.contactTracingData.doesVertexExist(trace.getPerson1())) {
            this.contactTracingData.addVertex(trace.getPerson1());
        }
        if (! this.contactTracingData.doesVertexExist(trace.getPerson2())) {
            this.contactTracingData.addVertex(trace.getPerson2());
        }
        //Record contact time
        this.contactTracingData.addContactTime(trace.getPerson1(),
                trace.getPerson2(), trace.getTime());
    }

    /**
     * Gets a list of times that person1 and person2 have come into direct
     * contact (as per the tracing data).
     *
     * If the two people haven't come into contact before, an empty list is returned.
     *
     * Otherwise the list should be sorted in ascending order.
     *
     * @param person1
     * @param person2
     * @return a list of contact times, in ascending order.
     * @require person1 != null && person2 != null
     */
    public List<Integer> getContactTimes(String person1, String person2) {
        return this.contactTracingData.getContactTimes(person1, person2);
    }

    /**
     * Gets all the people that the given person has been in direct contact with
     * over the entire history of the tracing dataset.
     *
     * @param person to list direct contacts of
     * @return set of the person's direct contacts
     */
    public Set<String> getContacts(String person) {
        //people contacted by person
        Set<String> contactedPeople = new HashSet<>();
        //Return all adjacent vertices
        for (Graph.Vertex vertex : this.contactTracingData.nameToVertex(person).getMap().keySet()) {
            //get name of person vertex represents
            contactedPeople.add(vertex.getName());
        }
        return contactedPeople;
    }

    /**
     * Gets all the people that the given person has been in direct contact with
     * at OR after the given timestamp (i.e. inclusive).
     *
     * @param person to list direct contacts of
     * @param timestamp to filter contacts being at or after
     * @return set of the person's direct contacts at or after the timestamp
     */
    public Set<String> getContactsAfter(String person, int timestamp) {
        //people who were in contact with person at/after time timestamp
        Set<String> contactAfterTimeStamp = new HashSet<>();
        for (String contactedPerson : getContacts(person)) {
            //size of contacted list, gets edge between contactedPerson and person and compares the latest contact time
            int size = this.contactTracingData.getEdge(contactTracingData.nameToVertex(person),
                    contactTracingData.nameToVertex(contactedPerson)).getTimeList().size();
            //if latest meeting time is after timestamp then they met after timestamp
            if (this.contactTracingData.getEdge(contactTracingData.nameToVertex(person),
                    contactTracingData.nameToVertex(contactedPerson)).getTimeList().get(size - 1) >= timestamp) {
                contactAfterTimeStamp.add(contactedPerson);
            }
        }
        return contactAfterTimeStamp;
    }

    /**
     * Initiates a contact trace starting with the given person, who
     * became contagious at timeOfContagion.
     *
     * Note that the return set shouldn't include the original person the trace started from.
     *
     * @param person to start contact tracing from
     * @param timeOfContagion the exact time person became contagious
     * @return set of people who may have contracted the disease, originating from person
     */
    public Set<String> contactTrace(String person, int timeOfContagion) {
        //time which person becomes contagious
        int contagiousTime = 60;
        //maps all vertices to if they are traversed or not
        Map<Graph.Vertex, Boolean> checked = new HashMap<>();
        //add all vertices and mark as not traversed
        for (Graph.Vertex vertex : this.contactTracingData.allNames.values()) {
            checked.put(vertex,false);
        }
        //traversal queue for traversing graph
        Queue<Graph.Vertex> traversal = new LinkedList<>();
        //maps vertex to its meeting time to its source
        Map<Graph.Vertex, Integer> meetingTimeWithPrev = new HashMap<>();
        //set of vertices to return, contact tracing
        Set<String> contactTracingAlert = new HashSet<>();

        //vertex representation of source
        Graph.Vertex source = this.contactTracingData.nameToVertex(person);
        //for all adjacent vertices to source which contact after timeOfContagion add in traversal queue and return list
        for (String name : getContactsAfter(person, timeOfContagion)) {
            traversal.add(contactTracingData.nameToVertex(name));
            contactTracingAlert.add(name);
            //get time of contact and put in meetingTimeWithPrev (for vertices adjacent to source)
            meetingTimeWithPrev.put(this.contactTracingData.nameToVertex(name),
                    getBestTimeContact(source, this.contactTracingData.nameToVertex(name), timeOfContagion));
        }
        //mark source as traversed
        checked.replace(source, true);

        while(! traversal.isEmpty()) {
            //current vertex in traversal
            Graph.Vertex current = traversal.remove();
            //contact time with previous
            int timeContactedPrevious = meetingTimeWithPrev.get(current);

            for (String name : getContactsAfter(current.getName(), timeContactedPrevious)) {
                if (! checked.get(this.contactTracingData.nameToVertex(name))) {
                    traversal.add(this.contactTracingData.nameToVertex(name));
                    meetingTimeWithPrev.put(this.contactTracingData.nameToVertex(name),
                            getBestTimeContact(current, this.contactTracingData.nameToVertex(name),
                                    timeContactedPrevious + contagiousTime));
                    // -1 is sentinel for not meeting
                    if (meetingTimeWithPrev.get(this.contactTracingData.nameToVertex(name)) != -1) {
                        contactTracingAlert.add(name);
                    }
                }
            }
            //mark as traversed
            checked.replace(current, true);
        }
        return contactTracingAlert;
    }

    /**
     * returns the time vertex1 and vertex2 made contact after/at timeFrom, -1 if they did not meet
     * @param vertex1 first vertex
     * @param vertex2 second vertex
     * @param timeFrom time
     * @return time vertex1 and vertex2 made contact after/at time
     */
    private int getBestTimeContact(Graph.Vertex vertex1, Graph.Vertex vertex2, int timeFrom) {
        //meeting times is in ascending order
        for (int timeMeet : this.contactTracingData.getEdge(vertex1, vertex2).getTimeList()) {
            if (timeMeet >= timeFrom) {
                return timeMeet;
            }
        }
        //sentinel : they did not meet
        return -1;
    }

    /**
     * A graph object
     */
    private class Graph {

        /**
         * Class represents a vertex in the graph
         * (Represents a person for contact tracing)
         */
        private class Vertex {

            //Name of person represented by this graph vertex
            private String name;
            //mapping of adjacent vertices to edges connecting them
            private Map<Vertex, Edge> adjacentVertices;

            /**
             * Creates a new vertex object representing a person with name name.
             * @param name String name of person represented by vertex
             */
            private Vertex(String name) {
                this.name = name;
                adjacentVertices = new HashMap<>();
            }

            /**
             * Returns name of person represented by this vertex
             * @return name
             */
            private String getName() {
                return this.name;
            }

            /**
             * Returns reference to adjacency map of this vertex
             * @return adjacency map of this vertex
             */
            private Map<Vertex, Edge> getMap() {
                return this.adjacentVertices;
            }
        }

        /**
         * Class represents a edge for this graph
         * (Edge between two vertices represents they were in contact at one or more times)
         */
        private class Edge {

            //times of contact
            private List<Integer> time;
            //endpoints of this vertex (people connected by this edge)
            private Vertex[] endpoints;

            /**
             * Creates a new edge between the two given vertices of graph
             * And adds the first meeting time
             * @param vertex1 vertex incident with this edge
             * @param vertex2 vertex incident with this edge
             * @param meetTime time of meeting
             */
            private Edge(Vertex vertex1, Vertex vertex2, Integer meetTime) {
                this.endpoints = new Vertex[]{vertex1, vertex2};
                this.time = new ArrayList<>();
                //record contact time between vertex1 and vertex2
                this.time.add(meetTime);
            }

            /**
             * Returns sorted list of time stored in this edge.
             * (Returns a list of time two people made contact)
             * @return list of times stored in edge
             */
            private List<Integer> getTimeList() {
                //sorted list
                Collections.sort(this.time);
                return this.time;
            }

            /**
             * Records a new contact time between endpoints of this edge
             * (Records time of contact between two people)
             * @param newTime new contact time between two people
             */
            private void addTime(Integer newTime) {
                this.time.add(newTime);
            }

            /**
             * Returns vertices connect by this edge
             * @return arrya of vertices connected by this edge
             */
            private Vertex[] getEndpoints() {
                return this.endpoints;
            }
        }

        //Mapping of names to vertex object for that name in this graph
        private Map<String, Vertex> allNames;

        /**
         * Creates a new graph with no vertices and edges
         */
        private Graph() {
            this.allNames = new HashMap<>();
        }

        /**
         * Adds a vertex to this graph
         * (A vertex represents person in contact tracing)
         * @param name name of person represented by this vertex
         */
        private void addVertex(String name) {
            Vertex person = new Vertex(name);
            this.allNames.put(name, person);
        }

        /**
         * Returns true if vertex representing person with name exists, else false
         * @param name name of person
         * @return true if vertex representation exists, false otherwise
         */
        private boolean doesVertexExist(String name) {
            return nameToVertex(name) != null;
        }

        /**
         * Returns the vertex object which represents a person with given name
         * If person does not exists returns null
         * @param name name of person
         * @return Vertex representing person in this graph
         */
        private Vertex nameToVertex(String name) {
            return this.allNames.get(name);
        }

        /**
         * Returns the edge between given two vertices
         * @param vertex1 vertex edge is incident to
         * @param vertex2 vertex edge is incident to
         * @return Edge incident to both vertices
         */
        private Edge getEdge(Vertex vertex1, Vertex vertex2) {
            try {
                return vertex1.getMap().get(vertex2);
            } catch (NullPointerException nullPointerException) {
                //edge does not exist
                return null;
            }
        }

        /**
         * Adds a edge between two vertices.
         * If edge already exists adds time to edge
         * (Records contact time between two people)
         * @param name1 name of person
         * @param name2 name of person
         * @param time time of contact between person1 and person2
         */
        private void addContactTime (String name1, String name2, Integer time) {
            //vertex representing name1
            Vertex vertexName1 = nameToVertex(name1);
            //vertex representing name2
            Vertex vertexName2 = nameToVertex(name2);
            if (getEdge(vertexName1, vertexName2) == null) {
                //edge does not exist
                Edge edge = new Edge(vertexName1, vertexName2, time);
                //add edge to vertex map
                vertexName1.getMap().put(vertexName2, edge);
                vertexName2.getMap().put(vertexName1, edge);
            } else {
                //edge already exists, add time to edge
                vertexName1.getMap().get(vertexName2).addTime(time);
            }
        }

        /**
         * Returns the list of times person1 and person2 came in contact
         * @param person1
         * @param person2
         * @return list of contact times
         */
        private List<Integer> getContactTimes(String person1, String person2) {
            try {
                return getEdge(nameToVertex(person1), nameToVertex(person2)).getTimeList();
            } catch (NullPointerException nullPointerException) {
                //Edge does not exist, returns empty list
                //person1 and person2 have never met
                return new ArrayList<>();
            }
        }
    }
}