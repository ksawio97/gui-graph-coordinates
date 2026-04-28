#include "layout_fruchterman_reingold.h"
#include <stdlib.h>
#include <math.h>

#define HEIGHT 600
#define WIDTH 800
#define EPSILON 1e-4
#define iterations 1000

void layout_fruchterman_reingold(graph_t *g) {
    int n = g->n_vertices;
    if (n == 0) return;

    double area = (double)WIDTH * HEIGHT;
    double k = sqrt(area / n);
    double temperature = WIDTH / 10.0;
    double dt = temperature / iterations;

    // Tablice na wektory przesunięć
    double *disp_x = calloc(n, sizeof(double));
    double *disp_y = calloc(n, sizeof(double));

    // 1. Losowe rozmieszczenie początkowe wewnątrz zdefiniowanego obszaru
    for (int i = 0; i < n; i++) {
        g->vertices[i].x = (double)(rand() % WIDTH);
        g->vertices[i].y = (double)(rand() % HEIGHT);
    }

    // Główna pętla algorytmu
    for (int iter = 0; iter < iterations; iter++) {
        
        // A. Siły odpychania
        for (int i = 0; i < n; i++) {
            disp_x[i] = 0.0;
            disp_y[i] = 0.0;
            for (int j = 0; j < n; j++) {
                if (i == j) continue;
                double dx = g->vertices[i].x - g->vertices[j].x;
                double dy = g->vertices[i].y - g->vertices[j].y;
                double dist = sqrt(dx * dx + dy * dy) + EPSILON;
                
                double fr = (k * k) / dist;
                disp_x[i] += (dx / dist) * fr;
                disp_y[i] += (dy / dist) * fr;
            }
        }

        // B. Siły przyciągania (krawędzie)
        for (int i = 0; i < g->n_edges; i++) {
            int v_idx = g->edges[i].ver_A - 1;
            int u_idx = g->edges[i].ver_B - 1;
            
            double dx = g->vertices[v_idx].x - g->vertices[u_idx].x;
            double dy = g->vertices[v_idx].y - g->vertices[u_idx].y;
            double dist = sqrt(dx * dx + dy * dy) + EPSILON;
            
	    double weight = g->edges[i].weight;
	    double fa = ((dist * dist) / k) * weight;
            double move_x = (dx / dist) * fa;
            double move_y = (dy / dist) * fa;

            disp_x[v_idx] -= move_x;
            disp_y[v_idx] -= move_y;
            disp_x[u_idx] += move_x;
            disp_y[u_idx] += move_y;
        }

        // C. Aktualizacja pozycji z uwzględnieniem temperatury i granic
        for (int i = 0; i < n; i++) {
            double dist = sqrt(disp_x[i] * disp_x[i] + disp_y[i] * disp_y[i]) + EPSILON;
            double limited_dist = (dist < temperature) ? dist : temperature;

            g->vertices[i].x += (disp_x[i] / dist) * limited_dist;
            g->vertices[i].y += (disp_y[i] / dist) * limited_dist;

            // Utrzymanie wierzchołków w kadrze WIDTH x HEIGHT
            if (g->vertices[i].x < 0) g->vertices[i].x = 0;
            if (g->vertices[i].x > WIDTH) g->vertices[i].x = WIDTH;
            if (g->vertices[i].y < 0) g->vertices[i].y = 0;
            if (g->vertices[i].y > HEIGHT) g->vertices[i].y = HEIGHT;
        }

        // Chłodzenie
        temperature -= dt;
        if (temperature < 0) temperature = 0;
    }

    free(disp_x);
    free(disp_y);
}
