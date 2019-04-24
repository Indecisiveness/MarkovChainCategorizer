# MarkovChainCategorizer
Project for Algorithms class that took sample text as input to get sequence frequency for
two different text samples, then determined the relative likelihood that a third text sample
came from the same source as the first vs the second

seqNode is the base unit of the linked list constructed for bestModel.
It contains the values calculated for a given sequence in each model as well as their difference.
It also has a pointer to the next node in the chain, if one exists.

MarkovModel accepts an integer representing the degree of the desired model and a string representation of the
input text. It can output the Laplace probabilty or a logrithmic representation of the Laplace probability for an
inputted sequence of the appropriate length for the model.

BestModel takes as input an integer representing the desired degree, two strings representing the file names to retrieve
the sample input from, and then any number of additional file names containing text to be categorized as more similar
to the first or more similar to the second.

The package line is a constraint of the assignment, but is not strictly necessary for the function of the code, and should be
renamed appropriately depending on the needs of the end user.
