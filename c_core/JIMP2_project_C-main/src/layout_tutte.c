#include <math.h>
#include <stdlib.h>
#include "layout_tutte.h"

#ifndef M_PI
#define M_PI 3.14159265358979323846
#endif

#define MAX_ITER  1000
#define TOLERANCE 1e-6

// build adjacency list: adj[i] = array of neighbor indices (0-based) 
static void build_adjacency(graph_t *g, int **adj, int *deg) {
    // count degrees 
    for (int i = 0; i < g->n_edges; i++) {
        int a = g->edges[i].ver_A - 1;
        int b = g->edges[i].ver_B - 1;
        deg[a]++;
        deg[b]++;
    }

    // allocate neighbor arrays 
    for (int i = 0; i < g->n_vertices; i++)
        adj[i] = malloc(deg[i] * sizeof(int));

    // fill neighbor arrays 
    int *pos = calloc(g->n_vertices, sizeof(int));

    for (int i = 0; i < g->n_edges; i++) {
        int a = g->edges[i].ver_A - 1;
        int b = g->edges[i].ver_B - 1;
        adj[a][pos[a]++] = b;
        adj[b][pos[b]++] = a;
    }

    free(pos);
}

// place n_fixed vertices evenly on a unit circle 
static void fix_outer(graph_t *g, int n_fixed) {
    for (int i = 0; i < n_fixed; i++) {
        double angle = 2.0 * M_PI * i / n_fixed;
        g->vertices[i].x = cos(angle);
        g->vertices[i].y = sin(angle);
    }
}

void layout_tutte(graph_t *g) {
    int n = g->n_vertices;

    // outer face: use up to 3 vertices (triangle), or all if n <= 3 
    int n_fixed = (n >= 3) ? 3 : n;
    fix_outer(g, n_fixed);

    // initialise inner vertices at origin 
    for (int i = n_fixed; i < n; i++) {
        g->vertices[i].x = 0.0;
        g->vertices[i].y = 0.0;
    }

    // build adjacency 
    int **adj = calloc(n, sizeof(int*));
    int  *deg = calloc(n, sizeof(int));
    build_adjacency(g, adj, deg);

    // Gauss-Seidel iterations: move each inner vertex to avg of neighbors 
    for (int iter = 0; iter < MAX_ITER; iter++) {
        double max_delta = 0.0;

        for (int i = n_fixed; i < n; i++) {
            if (deg[i] == 0) continue;

            double sx = 0.0, sy = 0.0;

            for (int k = 0; k < deg[i]; k++) {
                sx += g->vertices[adj[i][k]].x;
                sy += g->vertices[adj[i][k]].y;
            }

            double new_x = sx / deg[i];
            double new_y = sy / deg[i];

            double dx = new_x - g->vertices[i].x;
            double dy = new_y - g->vertices[i].y;
            double delta = dx*dx + dy*dy;

            if (delta > max_delta) max_delta = delta;

            g->vertices[i].x = new_x;
            g->vertices[i].y = new_y;
        }

        if (max_delta < TOLERANCE * TOLERANCE) break;
    }

    // free adjacency 
    for (int i = 0; i < n; i++)
        free(adj[i]);

    free(adj);
    free(deg);
}
