#include <stdio.h>
#include "output.h"

static int write_text(graph_t *g, const char *filename) {
    FILE *fo = fopen(filename, "w");
    if (!fo) return -1;

    for (int i = 0; i < g->n_vertices; i++)
        fprintf(fo, "%d %g %g\n",
                g->vertices[i].id,
                g->vertices[i].x,
                g->vertices[i].y);

    fclose(fo);
    return 0;
}

static int write_binary(graph_t *g, const char *filename) {
    FILE *fo = fopen(filename, "wb");
    if (!fo) return -1;

    // records: id (int), x (double), y (double) 
    for (int i = 0; i < g->n_vertices; i++) {
        fwrite(&g->vertices[i].id, sizeof(int),    1, fo);
        fwrite(&g->vertices[i].x,  sizeof(double), 1, fo);
        fwrite(&g->vertices[i].y,  sizeof(double), 1, fo);
    }

    fclose(fo);
    return 0;
}

int output_write(graph_t *g, const char *filename, int binary) {
    if (binary) {
        return write_binary(g, filename);
    }

    return write_text(g, filename);
}
