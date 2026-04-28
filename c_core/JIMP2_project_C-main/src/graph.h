#ifndef __GRAPH__
#define __GRAPH__
#include "vertex.h"
#include "edge.h"

typedef struct {
    int n_vertices;
    int n_edges;
    vertex_t *vertices;
    edge_t *edges;
} graph_t;

typedef enum {
    GRAPH_OK          = 0,
    GRAPH_ERR_OPEN    = 2,  // cannot open input file
    GRAPH_ERR_MEMORY  = 3,  // memory allocation failed
    GRAPH_ERR_FORMAT  = 4,  // invalid data format at line X
    GRAPH_ERR_EMPTY   = 5,  // graph contains no vertices
    GRAPH_ERR_ID      = 7,  // vertex id must be > 0
    GRAPH_ERR_LOOP    = 8,  // self-loop detected
    GRAPH_ERR_WEIGHT  = 9,  // negative weight
    GRAPH_ERR_DUP     = 10, // duplicate edge
    GRAPH_ERR_DISCONN = 11, // graph is disconnected
} graph_err_t;

graph_t *graph_load(const char *filename, graph_err_t *err);
void graph_free(graph_t *g);

#endif