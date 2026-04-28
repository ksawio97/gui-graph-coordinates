# JIMP2 Project C — Graph Layout

A C program that reads an undirected weighted graph from a file and computes a 2D layout using one of two algorithms: Fruchterman-Reingold or Tutte embedding. The result can be exported as text or binary, and visualized as a PNG image.

Made by Mikołaj Nierodziński and Ho Nghia Bao Phuc.

---

## Usage

```
./bin/graph <input> [output] [algorithm] [format]
```

| Argument    | Description                          | Default    |
|-------------|--------------------------------------|------------|
| `input`     | Path to edge-list input file         | (required) |
| `output`    | Path to output file                  | `out.txt`  |
| `algorithm` | `fr` or `tutte`                      | `fr`       |
| `format`    | `text` or `bin`                      | `text`     |

### Examples

```bash
./bin/graph inp.txt out.txt fr text
./bin/graph inp.txt out.txt tutte text
./bin/graph inp.txt out.bin fr bin
```

---

## Input Format

Each line describes one edge:

```
<name> <vertex_A> <vertex_B> <weight>
```

- `name` — edge label (no spaces)
- `vertex_A`, `vertex_B` — integer vertex IDs (> 0)
- `weight` — positive floating-point number

### Example

```
AB  1  2  4.0
BC  2  3  1.0
CD  3  4  1.0
DB  4  2  1.407
```

---

## Output Format

### Text (`text`)

One line per vertex:

```
<id> <x> <y>
```

### Binary (`bin`)

Sequence of records, each 3 × 8 bytes (little-endian doubles):

```
[id as double][x][y] ...
```

---

## Algorithms

### Fruchterman-Reingold (`fr`)

Force-directed algorithm. Vertices repel each other and edges act as springs, iteratively reaching an equilibrium layout.

### Tutte Embedding (`tutte`)

Fixes the first 3 vertices on a triangle, then places each inner vertex at the average position of its neighbors (Gauss-Seidel iteration until convergence).

---

## Exit Codes

| Code | Situation            | Message                              |
|------|----------------------|--------------------------------------|
| 0    | Success              | —                                    |
| 1    | Invalid arguments    | `Usage: ./graph <input> <output> ...` |
| 2    | File not found       | `Error: Cannot open input file`      |
| 3    | Memory allocation    | `Error: Memory allocation failed`    |
| 4    | Invalid data format  | `Error: Invalid data format at line X` |
| 5    | Empty graph          | `Error: Graph contains no vertices`  |
| 6    | File write error     | `Error: Failed to write output file` |
| 7    | Invalid vertex ID    | `Error: Vertex ID must be > 0 at line X` |
| 8    | Self-loop            | `Error: Self-loop detected at line X` |
| 9    | Negative weight      | `Error: Negative weight at line X`   |
| 10   | Duplicate edge       | `Error: Duplicate edge detected`     |
| 11   | Disconnected graph   | `Error: Graph is disconnected`       |
| 12   | Invalid algorithm    | `Error: Unknown algorithm 'X'`       |
| 13   | Invalid format       | `Error: Invalid output format`       |

---

## Build

### Local (Linux / macOS / WSL2)

Requires `gcc` and `make`.

```bash
make
```

### Using Docker (no dependencies required)

Requires [Docker Desktop](https://www.docker.com/products/docker-desktop).

```bash
docker build -t graph-app .
docker run --rm -v $(pwd):/app graph-app
```

After running, `out.txt` and `graph.png` will appear in the current directory.

---

## Visualization

Requires Python 3 and `matplotlib`.

```bash
python3 visualize.py
```

Reads `inp.txt` and `out.txt`, saves the layout to `graph.png`.

---

## Memory Check

Requires `valgrind` (Linux / WSL2).

```bash
valgrind --leak-check=full ./bin/graph inp.txt out.txt fr text
```

---

## Project Structure

```
.
├── Dockerfile
├── Makefile
├── inp.txt
├── visualize.py
└── src/
    ├── main.c
    ├── graph.c / graph.h
    ├── edge.c / edge.h
    ├── vertex.c / vertex.h
    ├── output.c / output.h
    ├── layout_fr.c / layout_fr.h
    └── layout_tutte.c / layout_tutte.h
```
