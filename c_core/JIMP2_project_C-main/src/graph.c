#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "graph.h"

// portable strdup replacement
static char *xstrdup(const char *s) {
    size_t len = strlen(s) + 1;
    char *copy = malloc(len);
    if (copy) memcpy(copy, s, len);
    return copy;
}

// check graph connectivity via BFS, returns 1 if connected
static int is_connected(graph_t *g) {
    int n = g->n_vertices;
    if (n <= 1) return 1;

    int *visited = calloc(n, sizeof(int));
    int *queue   = malloc(n * sizeof(int));
    if (!visited || !queue) {
        free(visited);
        free(queue);
        return 1; // skip check on allocation failure
    }

    int head = 0, tail = 0;
    visited[0] = 1;
    queue[tail++] = 0;

    while (head < tail) {
        int u = queue[head++];
        for (int i = 0; i < g->n_edges; i++) {
            int a = g->edges[i].ver_A - 1;
            int b = g->edges[i].ver_B - 1;
            int nb = -1;
            if (a == u) nb = b;
            else if (b == u) nb = a;
            if (nb >= 0 && !visited[nb]) {
                visited[nb] = 1;
                queue[tail++] = nb;
            }
        }
    }

    int connected = 1;
    for (int i = 0; i < n; i++)
        if (!visited[i]) { connected = 0; break; }

    free(visited);
    free(queue);
    return connected;
}

graph_t *graph_load(const char *filename, graph_err_t *err) {
    *err = GRAPH_OK;

    FILE *f = fopen(filename, "r");
    if (!f) {
        fprintf(stderr, "Error: Cannot open input file '%s'\n", filename);
        *err = GRAPH_ERR_OPEN;
        return NULL;
    }

    // first pass: validate and count edges, find max vertex id
    int n_edges = 0;
    int max_id  = 0;
    char buf[256];
    int line_no = 0;

    while (fgets(buf, sizeof(buf), f)) {
        line_no++;
        char name[64];
        int a, b;
        double w;
        if (sscanf(buf, "%63s %d %d %lf", name, &a, &b, &w) != 4)
            continue; // skip blank lines or comments

        if (a <= 0 || b <= 0) {
            fprintf(stderr, "Error: Vertex ID must be > 0 at line %d\n", line_no);
            fclose(f);
            *err = GRAPH_ERR_ID;
            return NULL;
        }
        if (a == b) {
            fprintf(stderr, "Error: Self-loop detected at line %d\n", line_no);
            fclose(f);
            *err = GRAPH_ERR_LOOP;
            return NULL;
        }
        if (w < 0.0) {
            fprintf(stderr, "Error: Negative weight at line %d\n", line_no);
            fclose(f);
            *err = GRAPH_ERR_WEIGHT;
            return NULL;
        }

        n_edges++;
        if (a > max_id) max_id = a;
        if (b > max_id) max_id = b;
    }

    if (n_edges == 0) {
        fprintf(stderr, "Error: Graph contains no vertices\n");
        fclose(f);
        *err = GRAPH_ERR_EMPTY;
        return NULL;
    }

    // allocate graph
    graph_t *g = malloc(sizeof(*g));
    if (!g) {
        fprintf(stderr, "Error: Memory allocation failed\n");
        fclose(f);
        *err = GRAPH_ERR_MEMORY;
        return NULL;
    }

    g->n_vertices = max_id;
    g->n_edges    = n_edges;
    g->vertices   = malloc(max_id * sizeof(*g->vertices));
    g->edges      = malloc(n_edges * sizeof(*g->edges));

    if (!g->vertices || !g->edges) {
        fprintf(stderr, "Error: Memory allocation failed\n");
        free(g->vertices);
        free(g->edges);
        free(g);
        fclose(f);
        *err = GRAPH_ERR_MEMORY;
        return NULL;
    }

    // initialise vertices: id = 1-based, position = 0 by default
    for (int i = 0; i < max_id; i++) {
        g->vertices[i].id = i + 1;
        g->vertices[i].x  = 0.0;
        g->vertices[i].y  = 0.0;
    }

    // second pass: parse edges and check for duplicates
    rewind(f);
    int idx = 0;

    while (fgets(buf, sizeof(buf), f) && idx < n_edges) {
        char name[64];
        int a, b;
        double w;
        if (sscanf(buf, "%63s %d %d %lf", name, &a, &b, &w) != 4)
            continue;

        // check duplicate edge
        for (int i = 0; i < idx; i++) {
            int ea = g->edges[i].ver_A, eb = g->edges[i].ver_B;
            if ((ea == a && eb == b) || (ea == b && eb == a)) {
                fprintf(stderr, "Error: Duplicate edge detected (%d-%d)\n", a, b);
                for (int j = 0; j < idx; j++) free(g->edges[j].name);
                free(g->edges);
                free(g->vertices);
                free(g);
                fclose(f);
                *err = GRAPH_ERR_DUP;
                return NULL;
            }
        }

        g->edges[idx].name   = xstrdup(name);
        g->edges[idx].ver_A  = a;
        g->edges[idx].ver_B  = b;
        g->edges[idx].weight = w;
        idx++;
    }

    fclose(f);

    // check connectivity
    if (!is_connected(g)) {
        fprintf(stderr, "Error: Graph is disconnected\n");
        graph_free(g);
        *err = GRAPH_ERR_DISCONN;
        return NULL;
    }

    return g;
}

void graph_free(graph_t *g) {
    if (!g) return;
    for (int i = 0; i < g->n_edges; i++)
        free(g->edges[i].name);
    free(g->edges);
    free(g->vertices);
    free(g);
}
