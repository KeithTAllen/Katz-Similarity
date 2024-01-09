### Overview
Within the field of Applied Ontology there is a need to measure the correctness of term assignment. 
  Katz Similarity has been used before to compare knowledge graphs and can be used in a similar way to help evaluate tools being developed to aid in ontology building. 
  This project begins to implement Katz Similarity in the specific case where there are two ontologies that share the same terms, but have been build differently. 
  It is composed of both the codebase and a paper. 
  
Also, this project is a sub part of a larger effort to improve tools used in automated and assisted ontology creation, and as such is niche. 

### The Code
The Java implementation of Katz Similarity is based heavily of the work done by [Nayak et. al](https://github.com/guruprasadnk7/DAGSimilarityKatz), which was implemented in C. 
  Since the parent project is being developed in Java, it was decided that this should be also be reimplemented in Java as well. 
  These Katz Similarity calculations are becoming part of a larger suite of programs all focused on automatically assessing tools under development 

This code currently needs a major overhaul. 
  Poor design choices made in the beginning of implmentation led to useless classes such as the Vertex Class, as well as the seperation of the AuxUtils and CalcUtils classes. 
  Instead, the entire vertex class will be scrapped and the AuxUtils and CalcUtils will be combined into a more generalized Graph class, since they both require the same information to perform their respectove jobs. 

### The Paper
In the Documents directory a pdf of the paper written for this project can be found. 
  It provides more background into what the field of Ontology is, its importance, and the specific motivation of this project. 
  The paper shows that an Ontology can be abstrated into a direct acyclic graph, and why Katz Similarity is a wise choice for this use case. 
  Finally, future work and the immediate next steps of the project are laid out. 
  
