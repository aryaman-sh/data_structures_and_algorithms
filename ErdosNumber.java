import java.util.*;

public class ErdosNumbers {

    //A graph representation of given data
    private Graph data;
    //A copy of input data
    private Map<String, String> dataPaperToAuthor;

    /**
     * String representing Paul Erdos's name to check against
     */
    public static final String ERDOS = "Paul ErdÃ¶s";

    /**
     * Initialises the class with a list of papers and authors.
     *
     * Each element in 'papers' corresponds to a String of the form:
     *
     * [paper name]:[author1][|author2[|...]]]
     *
     * Note that for this constructor and the below methods, authors and papers
     * are unique (i.e. there can't be multiple authors or papers with the exact same name or title).
     *
     * @param papers List of papers and their authors
     */
    public ErdosNumbers(List<String> papers) {
        //graph representation of input data
        this.data = new Graph();
        //copy of input data, maps paper Title to authors
        this.dataPaperToAuthor = new HashMap<>();

        for (String data : papers) {
            //Separates data as paper title , authors
            String[] titleAndAuthor = data.split(":");
            //title of this paper
            String paperTitle = titleAndAuthor[0];
            this.dataPaperToAuthor.put(paperTitle, titleAndAuthor[1]);
            //authors who have collaborated on this paper
            String[] authors = titleAndAuthor[1].split("\\|");

            //Add all authors as vertices
            //Case where author already exists is handled in add Graph method
            for (String authorName : authors) {
                this.data.insertVertex(authorName);
            }

            //create edges between all authors
            //Already existing edge are taken care of in insertEdge method
            for (int i = 0; i < authors.length; i++) {
                for (int j = i + 1; j < authors.length; j++) {
                    this.data.insertEdge(authors[i], authors[j], paperTitle);
                }
            }
        }

        //Calculate erdos number for all vertices and store in the vertex
        this.data.erdosAndWeightedErdosEvaluator(ERDOS, false);
        //Calculate weighted erdos number for all vertices and add to vertex
        this.data.erdosAndWeightedErdosEvaluator(ERDOS, true);
    }

    /**
     * Gets all the unique papers the author has written (either solely or
     * as a co-author).
     *
     * @param author to get the papers for.
     * @return the unique set of papers this author has written.
     */
    public Set<String> getPapers(String author) {
        return (Set<String>) new HashSet(this.data.getDistinctPapers(author));
    }

    /**
     * Gets all the unique co-authors the author has written a paper with.
     *
     * @param author to get collaborators for
     * @return the unique co-authors the author has written with.
     */
    public Set<String> getCollaborators(String author) {
        return this.data.getAdjacentVerticesNames(author);
    }

    /**
     * Checks if Erdos is connected to all other author's given as input to
     * the class constructor.
     *
     * In other words, does every author in the dataset have an Erdos number?
     *
     * @return the connectivity of Erdos to all other authors.
     */
    public boolean isErdosConnectedToAll() {
        //for all vertices connected to this graph
        for (Graph.Vertex vertex : this.data.vertices.values()) {
            //if some vertex does not a erdos number returns false
            if (vertex.erdosNumber == Integer.MAX_VALUE) {
                return false;
            }
        }
        return true;
    }

    /**
     * Calculate the Erdos number of an author.
     *
     * This is defined as the length of the shortest path on a graph of paper
     * collaborations (as explained in the assignment specification).
     *
     * If the author isn't connected to Erdos (and in other words, doesn't have
     * a defined Erdos number), returns Integer.MAX_VALUE.
     *
     * Note: Erdos himself has an Erdos number of 0.
     *
     * @param author to calculate the Erdos number of
     * @return authors' Erdos number or otherwise Integer.MAX_VALUE
     */
    public int calculateErdosNumber(String author) {
        //get erdos number stored in vertex representation of this author
        return this.data.authorVertex(author).getErdosNumber();
    }

    /**
     * Gets the average Erdos number of all the authors on a paper.
     * If a paper has just a single author, this is just the author's Erdos number.
     *
     * Note: Erdos himself has an Erdos number of 0.
     *
     * @param paper to calculate it for
     * @return average Erdos number of paper's authors
     */
    public double averageErdosNumber(String paper) {
        //sum of erdos numbers
        double erdosSum = 0;
        //number of authors
        double authorCounter = 0;
        //get authors from paper form dataPaperToAuthor map, for each author get their erdos number
        for (String names : this.dataPaperToAuthor.get(paper).split("\\|")) {
            erdosSum += this.calculateErdosNumber(names);
            authorCounter += 1;
        }
        return erdosSum / authorCounter;
    }

    /**
     * Calculates the "weighted Erdos number" of an author.
     *
     * If the author isn't connected to Erdos (and in other words, doesn't have
     * an Erdos number), returns Double.MAX_VALUE.
     *
     * Note: Erdos himself has a weighted Erdos number of 0.
     *
     * @param author to calculate it for
     * @return author's weighted Erdos number
     */
    public double calculateWeightedErdosNumber(String author) {
        //get weighted erdos number stored in vertex representation of author
        return this.data.authorVertex(author).getWeightedErdos();
    }

    /**
     * A graph representation.
     */
    private class Graph {

        /**
         * Vertex object in a graph
         * (Represents a author)
         */
        private class Vertex {

            //name of author
            private String authorName;
            //map of adjacent vertices mapped to edges connecting them
            private Map<Vertex, Edge> adjacentVertices;
            //Erdos number of this vertex
            private int erdosNumber;
            //Weighted erdos number of this vertex
            private double weightedErdos;

            /**
             * Creates a new vertex representing a author using author name.
             * @param authorName Name of author this vertex represents
             */
            private Vertex(String authorName) {
                this.authorName = authorName;
                adjacentVertices = new HashMap<>();
                //Default values of erdos number and weighted erdos number
                this.erdosNumber = Integer.MAX_VALUE;
                this.weightedErdos = Double.MAX_VALUE;
            }

            /**
             * Changes erdos number of this vertex to the given number
             * @param erdosNumber new erdos number of vertex
             */
            private void setErdosNumber(int erdosNumber) {
                this.erdosNumber = erdosNumber;
            }

            /**
             * Returns the erdos number of this vertex
             * @return erdos number of this vertex
             */
            private int getErdosNumber() {
                return this.erdosNumber;
            }

            /**
             * Changes the weighted erdos number of this vertex to the given number
             * @param weightedErdos new weighted erdos number of this vertex
             */
            private void setWeightedErdos(double weightedErdos) {
                this.weightedErdos = weightedErdos;
            }

            /**
             * Returns the weighted erdos number of this vertex
             * @return weighted erdos number of this vertex
             */
            private double getWeightedErdos() {
                return this.weightedErdos;
            }

            /**
             * Returns the name author represented by this vertex.
             * @return name of author
             */
            private String getAuthor() {
                return this.authorName;
            }

            /**
             * Returns a map of vertices adjacent to this vertex and edges connecting them
             * @return adjacency map of this graph
             */
            private Map<Vertex, Edge> getMap() {
                return this.adjacentVertices;
            }
        }

        /**
         * Edge in a graph
         * (Edge represents paper collaboration between two authors(vertices))
         */
        private class Edge {

            //Papers represented by this edge
            private List<String> papers;
            //endpoints of this edge
            private Vertex[] endpoints;

            /**
             * Creates a new edge between two vertices (authors)
             * @param author1 vertex representing author1
             * @param author2 vertex representing author2
             * @param paper paper collaborated on by author1 and author2
             */
            private Edge(Vertex author1, Vertex author2, String paper) {
                this.papers = new ArrayList<>();
                this.papers.add(paper);
                this.endpoints = new Vertex[]{author1, author2};
            }

            /**
             * Returns papers represented by this edge.
             * Papers collaborated on by endpoints of this edge
             * @return paper represented by this edge
             */
            private List<String> getPapersList() {
                return this.papers;
            }

            /**
             * Returns endpoints of this edge
             * @return endpoints of this edge
             */
            private Vertex[] getEndpoints() {
                return this.endpoints;
            }

            /**
             * Adds a new paper to this edge
             * @param paperToAdd title of paper
             */
            private void addPaper(String paperToAdd) {
                this.papers.add(paperToAdd);
            }

            /**
             * Returns the number of papers this edge represents
             * @return number of papers this edge represents
             */
            private Double papersNumber() {
                return (double) this.papers.size();
            }
        }

        //vertices in this graph
        //(author name : vertex representation of author)
        private Map<String, Vertex> vertices;

        /**
         * Creates a empty graph with no vertices or edges
         */
        private Graph() {
            this.vertices = new HashMap<>();
        }

        /**
         * Returns the edge connecting two vertices
         * Null if no edge exists (vertices are not adjacent)
         * @param vertex1 endpoint of edge
         * @param vertex2 endpoint of edge
         * @return Edge incident on both vertices
         */
        private Edge getEdge(Vertex vertex1, Vertex vertex2) {
            try {
                //throws nullPointerException if vertex is not adjacent
                return vertex1.getMap().get(vertex2);
            } catch (NullPointerException nullPointerException) {
                return null;
            }
        }

        /**
         * Return vertex representation of author.
         * If vertex representation of author does not exist return null
         * @param authorName author whose vertex needs to be returned
         * @return vertex of author or null
         */
        private Vertex authorVertex(String authorName) {
            return this.vertices.get(authorName);
        }

        /**
         * If a vertex representation of author does not exist in this graph.
         * Creates a new vertex representation of author and adds it to this graph.
         * @param authorName name of author new vertex represents
         */
        private void insertVertex(String authorName) {
            if (authorVertex(authorName) == null) {
                //vertex representation of authorName does not exist
                Vertex newAuthor = new Vertex(authorName);
                this.vertices.put(authorName, newAuthor);
            }
        }

        /**
         * If a edge between two authors does not exists create a new edge with paper
         * Else if edge between two authors exists, add papers to the edges papers.
         * @param authorName1 Name of first author
         * @param authorName2 name of second author
         * @param paper title of paper
         * @return
         */
        private void insertEdge(String authorName1, String authorName2, String paper) {
            //vertex representation of authors
            Vertex vertex1 = authorVertex(authorName1);
            Vertex vertex2 = authorVertex(authorName2);
            if (getEdge(vertex1, vertex2) == null) {
                //edge connecting both authors does not exists
                Edge edge = new Edge(vertex1, vertex2, paper);
                vertex1.getMap().put(vertex2, edge);
                vertex2.getMap().put(vertex1, edge);
            } else {
                //edge between authors exists
                vertex1.getMap().get(vertex2).addPaper(paper);
            }
        }

        /**
         * Returns all distinct papers author has written
         * All distinct papers in edges incident to vertex representation of author
         * @param authorName name of author
         * @return distinct papers
         */
        private List<String> getDistinctPapers(String authorName) {
            //Vertex representation of author
            Vertex author = authorVertex(authorName);
            //all papers written by author
            List<String> allPapers = new ArrayList<>();
            //for all edges connected to author vertex
            for (Edge edge : author.getMap().values()) {
                allPapers.addAll(edge.getPapersList());
            }
            //distinct papers
            List<String> distinctPapers = new ArrayList<>();
            allPapers.stream().distinct().forEach(distinctPapers::add);
            return distinctPapers;
        }

        /**
         * Returns all adjacent authors of given author.
         * (All authors who have collaborated with given author)
         * @param authorName name of author
         * @return adjacent authors of author
         */
        private Set<String> getAdjacentVerticesNames(String authorName) {
            //Vertex representing author
            Vertex author = authorVertex(authorName);
            //names of all collaborators
            List<String> collaborators = new ArrayList<>();
            //for all adjacent vertices of author
            for (Vertex vertex : author.getMap().keySet()) {
                collaborators.add(vertex.getAuthor());
            }
            return new HashSet<>(collaborators);
        }

        /**
         * Evaluates erdos number and weighted erdos number from source vertex and stores in vertex.
         * @param source source vertex
         * @param weightedErdos if false erdos number is calculated, if true weighted erdos number is calculated
         */
        private void erdosAndWeightedErdosEvaluator(String source, boolean weightedErdos) {
            //vertex representation of source author
            Vertex sourceVertex = authorVertex(source);
            //Graph traversal queue
            Queue<Vertex> exploringSeq = new LinkedList<>();
            //has vertex been traversed
            Map<Vertex, Boolean> checked = new HashMap<>();
            for (Vertex v : this.vertices.values()) {
                //Initially all false i.e. no vertex has been traversed
                checked.put(v, false);
            }

            //Assign erdos/weighted erdos number to source and vertices adjacent to source
            if (! weightedErdos) {
                //if evaluating erdos number
                sourceVertex.setErdosNumber(0);
                //sets erdos number of all vertices adjacent to source to 1
                for (Vertex vertex : sourceVertex.getMap().keySet()) {
                    exploringSeq.add(vertex);
                    vertex.setErdosNumber(1);
                }
            } else {
                sourceVertex.setWeightedErdos(0);
                //set weighted erdos numbers of vertices adjacent to source
                for (Vertex vertex : sourceVertex.getMap().keySet()) {
                    exploringSeq.add(vertex);
                    vertex.setWeightedErdos(1 / getEdge(vertex, sourceVertex).papersNumber());
                }
            }

            //source has been visited
            checked.replace(sourceVertex, true);
            //traversing other vertices
            while (! exploringSeq.isEmpty()) {
                //current vertex
                Vertex current = exploringSeq.remove();

                //for all unchecked adjacent vertices of current
                for (Vertex vertex : current.getMap().keySet()) {
                    //if current vertex is not checked
                    if (! checked.get(vertex)) {
                        exploringSeq.add(vertex);
                        if (! weightedErdos) {
                            //if erdos number is smaller then a smaller path exists
                            if (vertex.getErdosNumber() > current.getErdosNumber()) {
                                //set new erdos number
                                vertex.setErdosNumber(current.getErdosNumber() + 1);
                            }
                        } else if (weightedErdos) {
                            //if weighted erdos is smaller then a smaller path exists
                            if (vertex.getWeightedErdos() > current.getWeightedErdos()
                                    + 1 / getEdge(vertex, current).papersNumber()) {
                                //set new weighted erdos number
                                vertex.setWeightedErdos(current.getWeightedErdos()
                                        + 1 / getEdge(vertex, current).papersNumber());
                            }
                        }
                    }
                }

                checked.replace(current, true);
            }
        }
    }
}