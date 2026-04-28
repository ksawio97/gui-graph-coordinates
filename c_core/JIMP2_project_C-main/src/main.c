#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "graph.h"
#include "output.h"
#include "layout_fruchterman_reingold.h"
#include "layout_tutte.h"

#define DEFAULT_ALGO    "fr"
#define DEFAULT_FORMAT  "text"
#define DEFAULT_OUTPUT  "out.txt"

//remember to add tag

static void usage(const char *prog) {
    fprintf(stderr,
        "Usage: %s <input> [output] [algorithm] [format]\n"
        "  input      path to edge-list file\n"
        "  output     output file (default: %s)\n"
        "  algorithm  fr | tutte (default: %s)\n"
        "  format     text | bin  (default: %s)\n",
        prog, DEFAULT_OUTPUT, DEFAULT_ALGO, DEFAULT_FORMAT);
}

int main(int argc, char **argv) {
    if (argc < 2) {
        usage(argv[0]);
        return 1;
    }

    const char *input_file  = argv[1];
    const char *output_file = (argc >= 3) ? argv[2] : DEFAULT_OUTPUT;
    const char *algo        = (argc >= 4) ? argv[3] : DEFAULT_ALGO;
    const char *format      = (argc >= 5) ? argv[4] : DEFAULT_FORMAT;

    // validate algorithm
    if (strcmp(algo, "fr") != 0 && strcmp(algo, "tutte") != 0) {
        fprintf(stderr, "Error: Unknown algorithm '%s'\n", algo);
        return 12;
    }

    // validate format
    if (strcmp(format, "text") != 0 && strcmp(format, "bin") != 0) {
        fprintf(stderr, "Error: Invalid output format '%s'\n", format);
        return 13;
    }

    // load graph
    graph_err_t err;
    graph_t *g = graph_load(input_file, &err);
    if (!g)
        return (int)err;

    printf("loaded %d vertices, %d edges\n", g->n_vertices, g->n_edges);

    // run chosen layout algorithm
    switch (algo[0]) {
    case 'f':
        layout_fruchterman_reingold(g);
        break;
    case 't':
    default:
        layout_tutte(g);
        break;
    }

    // write output 
    int binary = (strcmp(format, "bin") == 0);
    if (output_write(g, output_file, binary) != 0) {
        fprintf(stderr, "error: failed to write output to '%s'\n", output_file);
        graph_free(g);
        return 1;
    }

    printf("result written to '%s'\nprogram proceeded succesful\n", output_file);

    graph_free(g);
    return 0;
}
